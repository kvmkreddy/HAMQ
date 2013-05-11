package org.os.javaee.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.io.IOException;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.ReflectionException;
import javax.management.AttributeNotFoundException;
import javax.management.RuntimeOperationsException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.os.javase.util.logger.Logger;



/**
 * <p>Title: MBeanServerHelper</p>
 * <p>Description: MBeanServerHelper is uesd for all mbeanserver interacting events.</p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: Open Source Development.</p>
 * @author Murali Reddy
 * @version 1.0
 */
public class MBeanServerHelper {

	private static final Logger log = Logger.getLogger(MBeanServerHelper.class);

	private static final String LOCAL_JBOSS_SERVER = "LOCAL_JBOSS";

	private static final int MBEAN_METHOD_INVOCATION = 0;
	private static final int MBEAN_GET_ATTRIBUTE_INVOCATION = 1;
	
	private static final String DEFAULT_ADAPTOR="jmx/rmi/RMIAdaptor";
	private static final String INITIAL_CONTEXT_FACTORY = "org.jnp.interfaces.NamingContextFactory";
	private static final String URL_PKG_PREFIXES = "org.jboss.naming:org.jnp.interfaces";
	
	/** MBeanServerhelper singleton implementaion instance.*/
	private static MBeanServerHelper helper = null;
	/** MBeanServerConnection object instance.*/
	private MBeanServerConnection server = null;
	/** MBeanServerHelper instance local Cache.
	 * 	If the helper class alreadt existed in the cache, will return the same otherwise creates new helper instance.
	 */
	private static Map<String,MBeanServerHelper> helperObjects = new HashMap<String,MBeanServerHelper>();

	private String serverURL;

	/**
	 * <p>Default constructor of the MBeanServerHelper.<p>
	 *
	 * @throws NamingException
	 */
	private MBeanServerHelper()throws NamingException{
		initialize(null);
	}

	/**
	 * <p>Overaloaded constructor of the MBeanServerHelper.<p>
	 * <p>Ths constructor will accepts the <code>providerURL</code> as an argument, to which this helper calss has to create the MBeanServerConnnection.</p>
	 *
	 * @param providerURL - ProviderURL to which this helper needs to create MBeanServerConnection.
	 * @throws NamingException
	 */
	private MBeanServerHelper(String providerURL)throws NamingException{
		initialize(providerURL);
	}

	/**
	 * <p>This method is used to creats the MBeanServerConnection.</p>
	 * @param providerURL
	 * @throws NamingException
	 */
	private void initialize(String providerURL)throws NamingException{
		setServer(getMBeanProxy(providerURL));
	}

	/**
	 * <p>Used to fetch the <code>InitialContext</code> object.</p>
	 * @param strURL - ProviderURL to which the InitialContext obejct has to create.
	 * @return Context object.
	 * @throws NamingException
	 */
	public static Context getInitialContext(String strURL) throws NamingException{
		if (strURL == null)
			return new InitialContext();
		Properties props = new Properties();
		props.put(Context.PROVIDER_URL,strURL);
		props.put(Context.INITIAL_CONTEXT_FACTORY,INITIAL_CONTEXT_FACTORY);
		props.put(Context.URL_PKG_PREFIXES,URL_PKG_PREFIXES);
		return  new InitialContext(props);
	}

	/**
	 * <p>This method will actually creats the MBeanServerConnection.</p>
	 * @param strURL	- ProviderURL to which the MBeanServerConnection has to create.
	 * @return MBeanServerConnection object.
	 * @throws NamingException
	 */
	public static MBeanServerConnection getMBeanProxy(String strURL) throws NamingException{
		MBeanServerConnection _server = null;
		Context ctx = null;
	    try {
	    	/** Get the initialContext object */
	    	ctx = getInitialContext(strURL);
	    	/** Get the MBeanServerConnection object */
	    	_server = (MBeanServerConnection) ctx.lookup(DEFAULT_ADAPTOR);

	    }catch (NamingException exp) {
	    	log.error("Error while trying to LOOKP up for the MBeanSeverConnection",exp);
	    	throw new NamingException("Error while trying to LOOKP up for the MBeanSeverConnection");
	    }
	    return _server;
	}

	/**
	 * <p>Singleton implementation of the MBeanServerHelper.</p>.
	 * @param url  - <p>ProviderURL. If the URL is null,
	 * 					then use new InitialContext() otherwise will use new InitialContext(url) for fetching MBeanServerConnection object.</p>
	 * @return MBeanServerHelper object.
	 * @throws NamingException
	 */
	public static synchronized MBeanServerHelper getInstance(String url)throws NamingException{
		log.debug(" URL -->:"+(url)+" and Helper :"+(helper)+" And Is Helper presents for this URL -->:"+(helperObjects.containsKey(url)));
		String key = (url == null)? LOCAL_JBOSS_SERVER:url;
		//url = (url == null)? LOCAL_JBOSS_SERVER:url;
		if(! helperObjects.containsKey(key)){
			helper = new MBeanServerHelper(url);
			helperObjects.put(key,helper);
			helper.setServerURL(url);
			return helper;
		}else {
			return (MBeanServerHelper)helperObjects.get(key);
		}
	}

	/**
	 * <p>This method is used to call server.invoke() on MBeanServerConnection object</p>
	 *
	 * @param objectName - ObjectName in the String format. Internall this method will creates the ObjectName instance.
	 * @param operationName - MBean OpearionName.
	 * @param args - MBean operation arguments.
	 * @param argTypes - MBean operation argument types.
	 * @return Object - Result Object if any
	 * @throws Exception
	 */
	public Object invokeOpByName(String objectName, String operationName, Object[] args, String[] argTypes) throws Exception{
		return invokeOpByName(new ObjectName(objectName), operationName, args, argTypes);
	}

	/**
	 * <p>This method is used to call server.invoke() on MBeanServerConnection object</p>
	 *
	 * @param objectName - ObjectName in the <code>ObjectName</code> format.
	 * @param operationName - MBean OpearionName.
	 * @param args - MBean operation arguments.
	 * @param argTypes - MBean operation argument types.
	 * @return Object - Result Object if any
	 * @throws Exception
	 */
	public Object invokeOpByName(ObjectName object, String operationName, Object[] args, String[] argTypes) throws Exception{
		log.debug("invokeOpByName() call with ObjectName : "+(object.getCanonicalName())+" , OperationName : "+(operationName));
		Object result = null;
		try {
			result = invokeOnMBean(MBEAN_METHOD_INVOCATION,object,operationName,args,argTypes);
		} catch (RuntimeException exp) {
			log.error("Exception in invokeOpByName() of MBeanServerhelper -->:"+(exp)+" and the Message is -->:"+(exp.getMessage()));
		}
		log.debug("Completed invokeOpByName() call. And the result is:"+((result != null)?result.toString():" Result is NULL"));
		return result;
	}

	/**
	 * <p>This method is used to call server.getAttribute() on MBeanServerObject.</p>
	 * @param objectName - ObjectName in the String format. Internall this method will creates the ObjectName instance.
	 * @param attributeName - MBean AttributeName
	 * @return Object - Result Object if any.
	 * @throws Exception
	 */
	public Object getAttribute(String objectName, String attributeName) throws Exception{
		return getAttribute(new ObjectName(objectName), attributeName);
	}

	/**
	 * <p>This method is used to call server.getAttribute() on MBeanServerObject.</p>
	 * @param objectName - ObjectName in the <code>ObjectName</code> format.
	 * @param attributeName - MBean AttributeName
	 * @return Object - Result Object if any.
	 * @throws Exception
	 */
	public Object getAttribute(ObjectName object, String attributeName) throws Exception{
		log.debug("getAttribute() call with ObjectName : "+(object.getCanonicalName())+" , AttributeName : "+(attributeName)+" , by the server -->:"+(server));
		Object result = null;
		try {
			result = invokeOnMBean(MBEAN_GET_ATTRIBUTE_INVOCATION,object,attributeName,null,null);
		} catch (RuntimeException exp) {
			log.error("Exception in getAttribute() of MBeanServerhelper -->:"+(exp)+" and the Message is -->:"+(exp.getMessage()));
		}
		log.debug("Completed getAttribute() call. And the result is:"+((result != null)?result.toString():" Result is NULL"));
		return result;
	}

	protected Object invokeOnMBean(int mBeanInovocationTye,ObjectName object, String invocationName, Object[] args, String[] argTypes) throws Exception{
		Object result = null;
		switch(mBeanInovocationTye){
			case MBEAN_METHOD_INVOCATION:
				try{
					result = this.getServer().invoke(object, invocationName, args, argTypes);
				}catch(Exception exp){
					// Intentionally I put log 'DEBUG' level here. Don't change it.
					log.debug("Exception: Exception in the invokeOnMBean(). Check the exception and take appropriate action.");
					boolean isNeedstoReCreateMBeanServerStub = checkException(exp);
					if(isNeedstoReCreateMBeanServerStub){
						reCreateMBeanStub();
						try{
							result = this.getServer().invoke(object, invocationName, args, argTypes);
						}catch(Exception reExp){
							log.error("Exception: Exception even after re creating the MBean Server Stub."+(reExp.getMessage()),exp);
							throw reExp;
						}
					}else{
						log.error("Exception: Exception in the invokeOnMBean() which does not need Re Creation of MBean Server Stub.");
						throw exp;
					}
				}
				break;
			case MBEAN_GET_ATTRIBUTE_INVOCATION:
				try{
					result = this.getServer().getAttribute(object, invocationName);
				}catch(Exception exp){
					// Intentionally I put log 'DEBUG' level here. Don't change it.
					log.debug("Exception: Exception in the invokeOnMBean(). Check the exception and take appropriate action.");
					boolean isNeedstoReCreateMBeanServerStub = checkException(exp);
					if(isNeedstoReCreateMBeanServerStub){
						reCreateMBeanStub();
						try{
							result = this.getServer().getAttribute(object, invocationName);
						}catch(Exception reExp){
							log.error("Exception: Exception even after re creating the MBean Server Stub."+(reExp.getMessage()),exp);
							throw reExp;
						}
					}else{
						log.error("Exception: Exception in the invokeOnMBean() which does not need Re Creation of MBean Server Stub.");
						throw exp;
					}
				}
				break;
			default:
				log.warn("Invalid MBean INVOCATION TYPE.");
		}
		return result;
	}

	private boolean checkException(Exception exp){
		if(exp != null)	{
			if(exp instanceof InstanceNotFoundException){
				log.error("InstanceNotFoundException: The MBean specified is not registered in the MBean server."+(exp.getMessage()),exp);
				return false;
			}else if (exp instanceof IOException){
				log.error("IOException: A communication problem occurred when talking to the MBean server."+(exp.getMessage()),exp);
				return true;
			}else if (exp instanceof MBeanException){
				log.error("MBeanException: Wraps an exception thrown by the MBean's invoked method or getter."+(exp.getMessage()),exp);
				return true;
			}else if (exp instanceof ReflectionException){
				log.error("ReflectionException: Wraps a java.lang.Exception thrown while trying to invoke the method or getter."+(exp.getMessage()),exp);
				return false;
			}else if (exp instanceof AttributeNotFoundException){
				log.error("AttributeNotFoundException: The attribute specified is not accessible in the MBean."+(exp.getMessage()),exp);
				return false;
			}else if (exp instanceof RuntimeOperationsException){
				log.error("RuntimeOperationsException: Wraps a java.lang.IllegalArgumentException: The object name in parameter is null or the attribute in parameter is null."+(exp.getMessage()),exp);
				return false;
			}
		}
		return false;
	}

	private void reCreateMBeanStub() throws Exception{
		log.debug("In reCreateMBeanStub()");
		this.setServer(MBeanServerHelper.getMBeanProxy(this.getServerURL()));
		log.debug("Completed reCreateMBeanStub()");
	}

	public MBeanServerConnection getServer() { return server; }
	public void setServer(MBeanServerConnection server) { this.server = server; }
	public String getServerURL() { return serverURL; }
	public void setServerURL(String serverURL) { this.serverURL = serverURL; }


	public String ping(){
		return "Hey I m Active";
	}
}
