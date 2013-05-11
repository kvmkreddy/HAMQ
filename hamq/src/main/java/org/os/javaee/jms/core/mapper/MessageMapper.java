package org.os.javaee.jms.core.mapper;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;

import org.os.javaee.jms.core.message.MessageImpl;
import org.os.javaee.jms.core.message.MessagePropertyImpl;
import org.os.javaee.jms.core.message.SimpleMessageImpl;

/**
 * <p>Title: MessageMapper.java</p>
 * <p>Description: Default implementation of the IMessageMapper</p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: Open Source Development.</p>
 * @author Murali Reddy
 * @version 1.0
 */
public class MessageMapper implements IMessageMapper{

	/**
	 * Map method which will be used to populate the data from disconnected (@link MessageImpl) to (@link Message) object.
	 * 
	 * @param session - JMS (@link Session)
	 * @param msgImpl - Disconnected MessageImpl instance
	 * @throws JMSException
	 */	
	public Message map(Session session, SimpleMessageImpl msgImpl) throws JMSException {
		Message message = createMessage(session,msgImpl.getMessageType()); 
		map(message,msgImpl);
		return message;
		
	}

	/**
	 * Map method which will be used to populate the data from disconnected (@link MessageImpl) to (@link Message) object.
	 * 
	 * @param msg - JMS Message instance
	 * @param msgImpl - Disconnected MessageImpl instance
	 * @throws JMSException
	 */
	public void map(Message msg, SimpleMessageImpl msgImpl) throws JMSException{
		if(msg == null) {
			throw new RuntimeException("RuntimeException: Message object is NULL.");
		}else if(msgImpl == null) {
			throw new RuntimeException("RuntimeException: MessageImpl object is NULL.");
		}else if (! msgImpl.validate()){
			throw new RuntimeException("RuntimeException: MessageImpl validation failed -->:"+(msgImpl.toString()));
		}else {
			switch(getMessageType(msg)) {
				case MessageImpl.TEXT_MESSAGE:
					populateHeader(msg,msgImpl);
					populateBody(msg,msgImpl,MessageImpl.TEXT_MESSAGE);
					break;
				case MessageImpl.OBJECT_MESSAGE:
					populateHeader(msg,msgImpl);
					populateBody(msg,msgImpl,MessageImpl.OBJECT_MESSAGE);
					break;
				case MessageImpl.STREAM_MESSAGE:
					populateHeader(msg,msgImpl);
					break;
				case MessageImpl.BYTES_MESSAGE:
					populateHeader(msg,msgImpl);
					break;
				case MessageImpl.MAP_MESSAGE:
					populateHeader(msg,msgImpl);
					break;
				default:
					throw new RuntimeException("INVALID JMS Message Type");
			}
		}
	}

	private Message createMessage(Session session, int messageType) throws JMSException {
		switch(messageType){
			case MessageImpl.TEXT_MESSAGE:
				return session.createTextMessage();
			case MessageImpl.BYTES_MESSAGE:
				return session.createBytesMessage();
			case MessageImpl.MAP_MESSAGE:
				return session.createMapMessage();
			case MessageImpl.STREAM_MESSAGE:
				return session.createStreamMessage();
			case MessageImpl.OBJECT_MESSAGE:
				return session.createObjectMessage();
			default:
				return session.createTextMessage();
		}
	}
	
	/**
	 * This method is used to populate only the Header information
	 * 
	 * @param msg - JMS Message instance
	 * @param msgImpl - Disconnected MessageImpl instance
	 * @throws JMSException
	 */
	private void populateHeader(Message msg, SimpleMessageImpl msgImpl) throws JMSException{
		Map<String,MessagePropertyImpl> bodyProps = msgImpl.getProperties();
		if (bodyProps != null && ! bodyProps.isEmpty()) {
			for (Iterator iter = bodyProps.entrySet().iterator(); iter.hasNext();) {
				Entry entry  = (Entry)iter.next();
				MessagePropertyImpl propertyImpl = (MessagePropertyImpl)entry.getValue();
				populateProperty(propertyImpl.getKey(), propertyImpl.getValue(),propertyImpl.getType(),msg);
				propertyImpl = null; // Clean up unnecessary objects.
			}
		}
		bodyProps = null;// Clean up unnecessary objects.
	}
	
	/**
	 * This method is used to populate only the Body information
	 * 
	 * @param msg - JMS Message instance
	 * @param msgImpl - Disconnected MessageImpl instance
	 * @throws JMSException
	 */	
	private void populateBody(Message msg, SimpleMessageImpl msgImpl, int messageType) throws JMSException{
		if(msgImpl.getPayLoad() != null) {
			switch(messageType) {
				case MessageImpl.TEXT_MESSAGE:
					if(msgImpl.getPayLoad() != null && msgImpl.getPayLoad() instanceof String)
						((TextMessage)msg).setText((String)msgImpl.getPayLoad());
					break;
				case MessageImpl.OBJECT_MESSAGE:
					((ObjectMessage)msg).setObject(msgImpl.getPayLoad());
					break;
				case MessageImpl.MAP_MESSAGE:
				case MessageImpl.BYTES_MESSAGE:
				case MessageImpl.STREAM_MESSAGE:
					break;
				default:
					break;
			}
		}

	}
	/**
	 * This method is used to populate the individual properties.
	 * 
	 * @param key - Property Key 
	 * @param value - Property Value
	 * @param type - Type of the property.
	 */
	private void populateProperty(String key, Object value, int type, Message msg) throws JMSException{
		switch(type) {
			case MessagePropertyImpl.BOOLEAN_PROPERTY:
				msg.setBooleanProperty(key, (Boolean)value);
				break;
			case MessagePropertyImpl.BYTE_PROPERTY:
				msg.setByteProperty(key, (Byte)value);
				break;
			case MessagePropertyImpl.DOUBLE_PROPERTY:
				msg.setDoubleProperty(key, (Double)value);
				break;
			case MessagePropertyImpl.FLOAT_PROPERTY:
				msg.setFloatProperty(key, (Float)value);
				break;
			case MessagePropertyImpl.INTEGER_PROPERTY:
				msg.setIntProperty(key, (Integer)value);
				break;
			case MessagePropertyImpl.LONG_PROPERTY:
				msg.setLongProperty(key, (Long)value);
				break;
			case MessagePropertyImpl.SHORT_PROPERTY:
				msg.setShortProperty(key, (Short)value);
				break;
			case MessagePropertyImpl.STRING_PROPERTY:
				msg.setStringProperty(key, (String)value);
				break;
			case MessagePropertyImpl.OBJECT_PROPERTY:
				msg.setObjectProperty(key, value);
				break;
			default:
				msg.setObjectProperty(key, value);
				break;
		}
	}

	/**
	 * Gets the MessageType 
	 * @param msg - Message instance.
	 * @return int - which identifies the MessageType.
	 */
	private int getMessageType(Message msg) {
		if(msg instanceof TextMessage) {
			return MessageImpl.TEXT_MESSAGE;
		}else if(msg instanceof ObjectMessage) {
			return MessageImpl.OBJECT_MESSAGE;
		}else if(msg instanceof StreamMessage) {
			return MessageImpl.STREAM_MESSAGE;
		}else if(msg instanceof BytesMessage) {
			return MessageImpl.BYTES_MESSAGE;
		}else if(msg instanceof MapMessage) {
			return MessageImpl.MAP_MESSAGE;
		}else {
			throw new RuntimeException("INVALID JMS Message Type");
		}
	}
}