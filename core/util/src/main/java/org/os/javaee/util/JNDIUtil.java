package org.os.javaee.util;



import java.sql.Connection;
import java.util.Properties;

import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * <p>Title: JNDIUtil.java</p>
 * <p>Description: Helper class for JNDI Invocation.</p>
 * <p> This class will provide utility methods for 
 * <ul>
 * <li><code>lookup</code> for </li>
 * <ul>
 * <li><code>InitialContext</code></li>,
 * <li><code>ConnectionFactory</code></li>,
 * <li><code>QueueConnectionFactory</code></li>, 
 * <li><code>TopicConnnectionFactory</code></li>,
 * <li><code>Destination</code></li>,
 * <li><code>Queue</code></li>, 
 * <li><code>Topic</code></li>, 
 * <li><code>EJBHome</code></li>,
 * <li><code>EJBLocalHome</code></li>
 * </ul>
 * <li><code>InitialContext</code></li> services.</p>
 * </ul>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: Open Source Development.</p>
 * @author Murali Reddy
 * @version 1.0
 */
public class JNDIUtil {
	
	private static Properties properties ;
	
	static {
		properties = System.getProperties();
	}
	
	public static Context getInitialContext() {
		try {
			return new InitialContext(properties);
		} catch (Exception e) {
			throw new RuntimeException("unable to create InitialContext",e);
		}
	}

	public static Object lookup(String name) {
		try {
			return getInitialContext().lookup(name);
		} catch (Exception e) {
			throw new RuntimeException("lookup failed for : " + name,e);
		}
	}

	public static Object lookup(String name, Class<?> klass) {
		Object object = lookup(name);
		if (!klass.isAssignableFrom(object.getClass())) {
		    throw new RuntimeException("lookup of '" + name + "' found " + object.getClass().getName() + " but expected " + klass.getName());
		}
		return object;
	}

	public static Connection getDbConnection(String name) {
		try {
			return getDataSource(name).getConnection();
		} catch (Exception e) {
			throw new RuntimeException("failed to get database connection",e);
		}
	}

	public static DataSource getDataSource(String name) { return (DataSource)lookup(name, DataSource.class); }
	
	public static EJBHome getEJBHome(String name) { return (EJBHome)lookup(name, EJBHome.class); }
	public static EJBLocalHome getEJBLocalHome(String name) { return (EJBLocalHome)lookup(name, EJBLocalHome.class); }

	public static Destination getDestination(String name) { return (Destination)lookup(name, Destination.class); }
	public static Queue getQueue(String name) { return (Queue)lookup(name, Queue.class); }
	public static Topic getTopic(String name) { return (Topic)lookup(name, Topic.class); }

	public static ConnectionFactory getConnectionFactory(String name) { return (ConnectionFactory)lookup(name, ConnectionFactory.class); }
	public static QueueConnectionFactory getQueueConnectionFactory(String name) { return (QueueConnectionFactory)lookup(name, QueueConnectionFactory.class);}
	public static TopicConnectionFactory getTopicConnectionFactory(String name) { return (TopicConnectionFactory)lookup(name, TopicConnectionFactory.class); }
}
