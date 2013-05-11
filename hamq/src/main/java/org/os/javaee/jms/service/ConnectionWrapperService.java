package org.os.javaee.jms.service;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;

import org.os.javaee.jms.core.ConnectionWrapper;
import org.os.javaee.jms.exception.HAMQJMSException;
import org.os.javaee.util.JNDIUtil;
import org.os.javase.util.logger.Logger;

/**
 * <p>Title: ConnectionWrapperService.java</p>
 * <p><b>Description:</b> ConnectionWrapperService.java</p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: Open Source Development.</p>
 * @author Murali Reddy
 * @version 1.0
 */
public class ConnectionWrapperService {

	protected static final Logger log = Logger.getLogger(ConnectionWrapperService.class.getCanonicalName());
	
	private static final int DEFAULT_MAX_USAGE = 100;
	protected ExceptionListener exceptionListener;
	private String connectionFactoryName = null;
	protected int maxUsage = DEFAULT_MAX_USAGE;
	
	protected volatile ConnectionWrapper wrapper = null;
	
	public ConnectionWrapperService(ExceptionListener exceptionListener, String connectionFactoryName, int maxUsage) {
		this.exceptionListener = exceptionListener;
		this.connectionFactoryName = connectionFactoryName;
		this.maxUsage = maxUsage;
	}
	
	public ExceptionListener getExceptionListener() { return exceptionListener; }
	public void setExceptionListener(ExceptionListener exceptionListener) { this.exceptionListener = exceptionListener; }
	
	public String getConnectionFactoryName() { return connectionFactoryName; }
	public void setConnectionFactoryName(String connectionFactoryName) { this.connectionFactoryName = connectionFactoryName; }
	
	public int getMaxUsage() { return maxUsage; }
	public void setMaxUsage(int maxUsage) { this.maxUsage = maxUsage; }
	
	public ConnectionWrapper fetchConnectionWrapper() throws HAMQJMSException{
		if(wrapper == null){
			wrapper = new ConnectionWrapper(createConnection());
		}
		return wrapper;
	}
	
	/**
	 *<p>This method is used to create QueueConnection object</p>
	 * @throws Exception - throws Exception, if any arises.
	 */
	private Connection createConnection() throws HAMQJMSException {
		Connection connection = null;
		try {
			log.debug("Looking up outbound Connection Factory -->:"+(connectionFactoryName));
			ConnectionFactory  connectionFactory = JNDIUtil.getConnectionFactory(connectionFactoryName);
			log.debug("Creating a Connection with the ConnectionFactory -->:"+(connectionFactory));
			if(connectionFactory instanceof QueueConnectionFactory){
				connection = (QueueConnection)((QueueConnectionFactory)connectionFactory).createConnection();
			}else if (connectionFactory instanceof TopicConnectionFactory){
				connection = (TopicConnection)((TopicConnectionFactory)connectionFactory).createConnection();
			}
			if(this.getExceptionListener() != null)
				connection.setExceptionListener(this.getExceptionListener());
		} catch (JMSException e) {
			log.fatal("Exception in createConnection -->:"+(connectionFactoryName), e);
			throw new HAMQJMSException("Exception in createConnection -->:"+(connectionFactoryName)+": ANd ExceptionMessage -->:"+(e.getMessage()));
		}
		return connection;
	}
	
	public synchronized void closeWrapper(){
		wrapper.release();
		wrapper.close();
		wrapper = null;
	}
}