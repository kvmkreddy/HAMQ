package org.os.javaee.jms.core.mapper;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.os.javaee.jms.core.message.SimpleMessageImpl;

/**
 * <p>Title: IMessageMapper.java</p>
 * <p>Description: IMessageMapper interface</p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: Open Source Development.</p>
 * @author Murali Reddy
 * @version 1.0
 */
public interface IMessageMapper {
	
	/**
	 * Map method which will be used to populate the data from disconnected (@link MessageImpl) to (@link Message) object.
	 * 
	 * @param msg - JMS Message instance
	 * @param msgImpl - Disconnected MessageImpl instance
	 * @throws JMSException
	 */
	public void map(Message msg, SimpleMessageImpl msgImpl) throws JMSException;
	
	/**
	 * Map method which will be used to populate the data from disconnected (@link MessageImpl) to (@link Message) object.
	 * 
	 * @param session - JMS (@link Session)
	 * @param msgImpl - Disconnected MessageImpl instance
	 * @return - JMS (@link Message) object
	 * @throws JMSException
	 */
	public Message map(Session session, SimpleMessageImpl msgImpl) throws JMSException;
}
