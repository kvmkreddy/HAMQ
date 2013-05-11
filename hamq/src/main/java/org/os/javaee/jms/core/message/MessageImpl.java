package org.os.javaee.jms.core.message;

import java.io.Serializable;

/**
 * <p>Title: MessageImpl.java</p>
 * <p>Description: Disconnected Message Object implementation.</p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: Open Source Development.</p>
 * @author Murali Reddy
 * @version 1.0
 */
public interface MessageImpl extends Serializable{
	
	public static final int TEXT_MESSAGE 	= 00000000;
	public static final int OBJECT_MESSAGE 	= 00000001;
	public static final int MAP_MESSAGE 	= 00000010;
	public static final int BYTES_MESSAGE 	= 00000011;
	public static final int STREAM_MESSAGE 	= 00000100;
	
	public static final String DEFAULT_DELIVERY_MODE = "DEFAULT_DELIVERY_MODE";
	
	public static final int DELIVERY_MODE_PERSISTENT = javax.jms.DeliveryMode.PERSISTENT;
	public static final int DELIVERY_MODE_NON_PERSISTENT = javax.jms.DeliveryMode.NON_PERSISTENT;
	
/*	public Object getProperty(String key) ;
	public Map<String,MessagePropertyImpl> getProperties();
	
	public String getDestinationName();
	public void setDestinationName(String destinationName);
	
	public int getMessageType();
	public void setMessageType(int messageType);

	public int getDeliveryMode();
	public void setDeliveryMode(int deliveryMode);
	
	public int getPriority();
	public void setPriority(int priority);
	
	public long getTimeToLive();
	public void setTimeToLive(long timeToLive);
	
	public void setProperty(String key, Object value);
	public void setProperty(String key, Object value, int type);
	public void setProperties(Properties props, int type);
	public void setProperties(Properties props);

	public Serializable getPayLoad();
	public void setPayLoadData(Serializable payload);
	
	public boolean isAnyMessageSpeificProducerConfigurations();*/
}