package org.os.javaee.jms.service;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.JMSException;

import org.os.javaee.util.JNDIUtil;
import org.os.javase.util.logger.Logger;

/**
 * <p>Title: ActualNameJMSDestinationFetchService.java</p>
 * <p>Description: ActualNameJMSDestinationFetchService.java</p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: Open Source Development.</p>
 * @author Murali Reddy
 * @version 1.0
 */
public class ActualNameJMSDestinationFetchService implements IJMSDestinationFetchService {

	private static final Logger log = Logger.getLogger(ActualNameJMSDestinationFetchService.class.getCanonicalName());
	
	private Map<String,Map<String,String>> destinationMappings = new HashMap<String,Map<String,String>>();
	
	
	public Map<String, Map<String, String>> getDestinationMappings() {
		return destinationMappings;
	}

	public void setDestinationMappings(
			Map<String, Map<String, String>> destinationMappings) {
		this.destinationMappings = destinationMappings;
	}

	/**
	 * Fetch the <code>Destination</code> reference by using the actual <code>destinationName</code>.
	 * @return - Destination reference using the destinationName.   
	 * @see org.os.javaee.jms.service.IJMSDestinationFetchService#getDestination(java.lang.String)
	 */
	public Destination getDestination(String destinationName,String connectionFactoryName) throws JMSException {
		String finalConnectionFactoryName = connectionFactoryName.substring(connectionFactoryName.lastIndexOf("/")+1);
		log.debug(" Input DestinationName -->:"+(destinationName)+"\t and Final ConnectionFactoryName -->:"+(finalConnectionFactoryName));
		if(this.getDestinationMappings().containsKey(destinationName)){
			if(this.getDestinationMappings().get(destinationName).containsKey(finalConnectionFactoryName)){
				String destinationJNDIName = this.getDestinationMappings().get(destinationName).get(finalConnectionFactoryName);
				log.debug(" Destination JNDI Name -->:"+(destinationJNDIName));
				if(destinationJNDIName != null){
					return JNDIUtil.getDestination(destinationJNDIName);
				}
			}
		}
		return null;
	}
}