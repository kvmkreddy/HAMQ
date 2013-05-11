package org.os.javaee.jms.util;

import java.util.Enumeration;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.os.javaee.jms.core.message.SimpleMessageImpl;

/**
 * <p>Title: MessageLoggerUtil.java</p>
 * <p>Description: MessageLoggerUtil is used as Logger utility for the MQMessageServices.</p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: Open Source Development.</p>
 * @author Murali Reddy
 * @version 1.0
 */
public class MessageLoggerUtil {
	
	/**
	 * Log MessageImpl object data.
	 *
	 * @param messageImpl - MessageImpl class.
	 * @return String representation of MessageImpl Object Data 
	 * @throws JMSException
	 */
	public static String logMessageImpl(SimpleMessageImpl messageImpl) throws JMSException {
		StringBuffer sb = new StringBuffer();
		sb.append("\n******************************************************************************************************************");
		sb.append("\nMessageImpl Object : ");
		sb.append(messageImpl.toString());
		if(messageImpl.getPayLoad() != null) {
			sb.append("\nMessageImpl Object PayLoad -->: " + messageImpl.getPayLoad().toString());
		}
		sb.append("\n******************************************************************************************************************");
		return sb.toString();
	}

	/**
	 * Log Message object data.
	 *
	 * @param msg - Message class.
	 * @return String representaion of Message Object Data
	 * @throws JMSException
	 */
	public static String logMessage(Message message) throws JMSException {
		StringBuffer sb = new StringBuffer();
		sb.append("\n\t******************************************************************************************************************\n");
		sb.append("\n\tMessage Object : ");
		Enumeration msgEnum = message.getPropertyNames();
		while(msgEnum.hasMoreElements()) {
			String prop = (String)msgEnum.nextElement();
			sb.append("\n\t\t Property Name -->:"+(prop)+"\t Property Value -->:"+(message.getObjectProperty(prop)));
		}
		if((message instanceof ObjectMessage) && ((ObjectMessage)message).getObject() != null) {
			sb.append("\n\t\tMessage Object PayLoad -->: " + ((ObjectMessage)message).getObject().toString());
		}else if((message instanceof TextMessage) && ((TextMessage)message).getText() != null) {
			sb.append("\n\t\tMessage Text -->: " + ((TextMessage)message).getText().toString());
		}else if(message instanceof MapMessage) {
			Enumeration mapMsgEnum = ((MapMessage)message).getMapNames();
			while(mapMsgEnum.hasMoreElements()) {
				String prop1 = (String)mapMsgEnum.nextElement();
				sb.append("\n\t\t Map Message Property Name -->:"+(prop1)+"\t Property Value -->:"+(message.getObjectProperty(prop1)));
			}			
			sb.append("\n\t\tMessage Text -->: " + ((TextMessage)message).getText().toString());
		}
		sb.append("\n\t******************************************************************************************************************\n");
		return sb.toString();
	}
}
