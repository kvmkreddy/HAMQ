package org.os.javaee.jms.service;

import javax.jms.Destination;
import javax.jms.JMSException;

import org.os.javaee.util.JNDIUtil;

/**
 * <p>Title: JNDIJMSDestinationFetchService.java</p>
 * <p>Description: JNDIJMSDestinationFetchService.java</p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: Open Source Development.</p>
 * @author Murali Reddy
 * @version 1.0
 */
public class JNDIJMSDestinationFetchService implements IJMSDestinationFetchService {

	/**
	 * Fetch the <code>Destination</code> reference by using the <code>destinationName</code> in JNDI.
	 * @return - Destination reference from JNDI using the destinationName.   
	 * @see org.os.javaee.jms.service.IJMSDestinationFetchService#getDestination(java.lang.String)
	 */
	public Destination getDestination(String destinationName,String connectionFactoryName) throws JMSException {
		return (Destination)JNDIUtil.getDestination(destinationName);
	}
}
