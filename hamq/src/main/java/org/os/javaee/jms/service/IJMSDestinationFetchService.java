package org.os.javaee.jms.service;

import javax.jms.Destination;
import javax.jms.JMSException;

/**
 * <p>Title: IJMSDestinationFetchService.java</p>
 * <p>Description: IJMSDestinationFetchService.java</p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: Open Source Development.</p>
 * @author Murali Reddy
 * @version 1.0
 */
public interface IJMSDestinationFetchService {

	public Destination getDestination(String destinationName,String connectionFactoryName) throws JMSException;
}
