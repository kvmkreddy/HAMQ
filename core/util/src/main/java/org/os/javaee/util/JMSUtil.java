package org.os.javaee.util;

import javax.jms.Connection;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;


import org.os.javase.util.logger.Logger;

/**
 * <p>Title: JMSUtil.java</p>
 * <p>Description: JMSUtil is used as the Utility for the JMS package operations.</p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: Open Source Development.</p>
 * @author Murali Reddy
 * @version 1.0
 */
public class JMSUtil {

	private static Logger log = Logger.getLogger(JMSUtil.class.getCanonicalName());
	
    /**
     * <p>Close a reference Object and suppress errors.</p>
     * <p> Uses for Connection, Session, MessageProducer, MessageConsumer.</p>
     */
    public static void close(Object reference) {
    	if(reference instanceof Connection){
			close((Connection)reference);
		}else if(reference instanceof Session){
			close((Session)reference);
		}else if(reference instanceof MessageProducer){
			close((MessageProducer)reference);
		}else if(reference instanceof MessageConsumer){
			close((MessageConsumer)reference);
		}
    }
    
    /**
     * <p>Close a Connection and suppress errors.</p>
     * <p> Uses for QueueConnection, TopicConnection, XAConnection, XAQueueConnection, XATopicConnection.</p>
     */
    public static void close(Connection connection) {

        try {
            if (connection != null) {
            	log.debug("close(connection)");
                connection.close();
            }
        }
        catch (Throwable e) {
        	log.warn("unable to close connection : " + connection, e);
        }
    }
    
    /**
     * <p> Close a Session and suppress errors.</p> 
     * <p> Uses for Session, QueueSession, TopicSession, XAQueueSession, XASession, XATopicSession.</p>
     */
    public static void close(Session session) {

        try {
            if (session != null) {
            	log.debug("close(session)");
                session.close();
            }
        }
        catch (Throwable e) {
        	log.warn("unable to close session : " + session, e);
        }
    }
    /**
     * <p>Close a MessageProducer and suppress errors.</p> 
     * <p>Uses for MessageProducer, QueueSender, TopicSender.</p>
     */
    public static void close(MessageProducer messageProducer) {

        try {
            if (messageProducer != null) {
            	log.debug("close(messageProducer)");
                messageProducer.close();
            }
        }
        catch (Throwable e) {
        	log.warn("unable to close messageProducer : " + messageProducer, e);
        }
    }

    /**
     * <p>Close a MessageProducer and suppress errors.</p> 
     * <p>Uses for MessageConsumer, QueueReceiver, TopicReceiver.</p>
     */
    public static void close(MessageConsumer messageConsumer) {

        try {
            if (messageConsumer != null) {
            	log.debug("close(messageConsumer)");
                messageConsumer.close();
            }
        }
        catch (Throwable e) {
        	log.warn("unable to close messageConsumer : " + messageConsumer, e);
        }
    }

    /**
     * <p>Close a Connection, Session and MessageProducer and suppress errors.</p> 
     * <p>Uses for Connection, Session, MessageProducer.</p>
     */
    public static void close( Connection connection, Session session, MessageProducer producer) {
        close(producer);
        close(session);
        close(connection);
    }
}
