package org.os.javaee.jms.core.message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import javax.jms.Message;

/**
 * <p>Title: SimpleMessageImpl.java</p>
 * <p><b>Description:</b> SimpleMessageImpl</p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: Open Source Development.</p>
 * @author Murali Reddy
 * @version 1.0
 */
public class SimpleMessageImpl implements MessageImpl{

	private static final long serialVersionUID = 3417560915489751417L;
	
	/** Properties of the (@link Message) body.*/
	protected Map<String,MessagePropertyImpl> bodyProps = new HashMap<String,MessagePropertyImpl>();
	/** Serializable Payload of the (@link Message).*/
	protected Serializable payLoadData;
	/** (@link Destination)(i.e either (@link Topic) or (@link Queue) ) to which (@link Message) has to sent.*/
	protected String destinationName;
	/** Message Type.*/
	protected int messageType =TEXT_MESSAGE;
	/** Message DeliveryMode. Default will be PERSISTENT.*/
	protected int deliveryMode = new Integer(System.getProperty(DEFAULT_DELIVERY_MODE,Message.DEFAULT_DELIVERY_MODE+"")).intValue();
	/** Message Priority. Default will be 4.*/
	protected int priority = Message.DEFAULT_PRIORITY;
	/** Message Time to Live. Default will be unlimited.*/
	protected long timeToLive = Message.DEFAULT_TIME_TO_LIVE;

	
	/**
	 * Returns the Property Value of the Message.
	 * @param key - Property Key
	 * @return Property Value.
	 */
	public Object getProperty(String key) { return this.bodyProps.get(key).getValue(); }
	/**
	 * Returns all the Properties of the Message
	 * @return All the Message properties in Map
	 */
	public Map<String,MessagePropertyImpl> getProperties() { return this.bodyProps; }
	
	/**
	 * Gets the name of the (@link Destination)(i.e either (@link Topic) or (@link Queue) ) to which (@link Message) has to sent.
	 * @return Destination Name
	 */
	public String getDestinationName() { return destinationName; }
	/**
	 * Sets the name of the (@link Destination)(i.e either (@link Topic) or (@link Queue) ) to which (@link Message) has to sent.
	 * @param destinationName - Destination Name
	 */
	public void setDestinationName(String destinationName) { this.destinationName = destinationName; }
	
	/**
	 * Gets the MessageTyps of this MessageImpl. 
	 * @return messageType in integer.
	 */
	public int getMessageType() { return messageType; }
	
	/**
	 * Sets the MessageTyps of this MessageImpl.
	 * @param messageType - MessageType
	 */
	public void setMessageType(int messageType) { this.messageType = messageType; }

	/**
	 * Gets the Message Deliver Mode.
	 * @return - Message Delivery Mode
	 */
	public int getDeliveryMode() { return deliveryMode; }
	/**
	 * sets the Message Deliver Mode.
	 * @param deliveryMode - Message Delivery Mode
	 */
	public void setDeliveryMode(int deliveryMode) { this.deliveryMode = deliveryMode; }
	
	/**
	 * Gets the Message Priority.
	 * @return Message Priority
	 */
	public int getPriority() { return priority; }
	/**
	 * Sets the Message Priority.
	 * @param priority - Message Priority
	 */
	public void setPriority(int priority) { this.priority = priority; }
	
	/**
	 * Gets how much time the message can live in milli seconds
	 * @return - How much time the message can live
	 */
	public long getTimeToLive() { return timeToLive; }
	/**
	 * Sets how much time the message can live in milli seconds
	 * @param timeToLive - How much time the message can live
	 */
	public void setTimeToLive(long timeToLive) { this.timeToLive = timeToLive; }
	
	/**
	 * Sets the Property Value of the Message.
	 * @param key - Property Key
	 * @param key - Property Value
	 */
	public void setProperty(String key, Object value) {
		this.bodyProps.put(key, new MessagePropertyImpl(key,value,MessagePropertyImpl.STRING_PROPERTY));
	}

	/**
	 * Sets the Property Value of the Message.
	 * @param key - Property Key
	 * @param key - Property Value
	 * @param type - Property Type
	 */
	public void setProperty(String key, Object value, int type) {
		this.bodyProps.put(key, new MessagePropertyImpl(key,value,type));
	}
	
	/**
	 * Sets all the Properties of the Message
	 * 
	 * @param props  - Properties of the Message.
	 * @param type - Type of the Property
	 */
	public void setProperties(Properties props, int type) {
		for(Iterator iter =props.entrySet().iterator();iter.hasNext();) {
			Entry keyEntry = (Entry)iter.next();
			String key = (String)keyEntry.getKey();
			Object value = keyEntry.getValue();
			this.bodyProps.put(key, new MessagePropertyImpl(key,value,type));
		}
	}
	/**
	 * Sets all the Properties of the Message as String
	 * 
	 * @param props  - Properties of the Message.
	 */
	public void setProperties(Properties props) { setProperties(props,MessagePropertyImpl.STRING_PROPERTY);}

	/**
	 * Returns the Message Payload.
	 * @return Payload of the Message.
	 */
	public Serializable getPayLoad() { return this.payLoadData; }

	/**
	 * Sets the Message Payload.
	 * @param payload - Payload of the Message.
	 */
	public void setPayLoadData(Serializable payload) {
		if((payload != null) && (payload.getClass() instanceof Serializable)) {
			this.payLoadData = payload;
		}else {
			throw new RuntimeException("The Payload is either NULL or NON SERIALIZABLE  -->:"+(payload));
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\nDestination -->:"+(this.getDestinationName()));
		sb.append("\nMessageType -->:"+(this.getMessageType()));
		for (Iterator iter = this.getProperties().entrySet().iterator();iter.hasNext();) {
			sb.append("\n"+((MessagePropertyImpl)((Entry)iter.next()).getValue()).toString());
		}
		return sb.toString();
	}
	
	/**
	 * Validates the MessageImpl object. The MessageImpl is 'validate' if and only of the destinationName is not null
	 * and the messageType should be either of 'TEXT_MESSAGE','OBJECT_MESSAGE','BYTES_MESSAGE','MAP_MESSAGE' or 'STREAM_MESSAGE'. 
	 * @return MessageImpl is valid or not in boolean. 
	 */
	public boolean validate(){
		boolean isValid = false;
		int _mesType = this.getMessageType();
		if((_mesType == TEXT_MESSAGE || _mesType == OBJECT_MESSAGE || _mesType == BYTES_MESSAGE
			|| _mesType == MAP_MESSAGE || _mesType == STREAM_MESSAGE)
			&& (this.getDestinationName() != null)){
			isValid = true;
		}
		return isValid;	
	}
	
	/**
	 * If any Message specific Message Producer configurations present, then will return true,otherwise false.
	 * @return - Is Any Message Specific Configurations for MessageProducer.
	 */
	public boolean isAnyMessageSpeificProducerConfigurations(){
		return ((this.getDeliveryMode() != Message.DEFAULT_DELIVERY_MODE) ||
				(this.getPriority() != Message.DEFAULT_PRIORITY) ||
				(this.getTimeToLive() != Message.DEFAULT_TIME_TO_LIVE)
				);
	}
}