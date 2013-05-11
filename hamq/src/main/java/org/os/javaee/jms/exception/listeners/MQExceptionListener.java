package org.os.javaee.jms.exception.listeners;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;

import org.os.javase.util.logger.Logger;

/**
 * <p>Title: MQExceptionListener.java</p>
 * <p>Description: MQExceptionListener is used as the default JMS ExceptionListener.</p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: Open Source Development.</p>
 * @author Murali Reddy
 * @version 1.0
 */
public class MQExceptionListener implements ExceptionListener{
	protected Logger log;
	
	public MQExceptionListener() {
		log = Logger.getLogger(getClass().getCanonicalName());
	}
	
	/**
	 * <p>onException call back handler method.</p>
	 * <p> Will log the exception details and the Linked Exception details.</p>
	 *
	 * @see javax.jms.ExceptionListener#onException(javax.jms.JMSException)
	 */
	 public void onException(JMSException jmsExp) {
		 log.info("Exception occured. Invoked from onException() method.");
		 if(jmsExp != null) {
			log.debug("JMSException: Exception -->:"+(jmsExp)+" And Message is -->:"+(jmsExp.getMessage())+" And Error Code -->:"+(jmsExp.getErrorCode()));
			Exception jmsLinkedException = jmsExp.getLinkedException();
			while(jmsLinkedException != null) {
				log.debug("JMSException:Linked Exception is  -->:"+(jmsLinkedException)+" And Message is -->:"+(jmsLinkedException.getMessage()));
				jmsLinkedException = (jmsLinkedException instanceof JMSException) ? ((JMSException)jmsLinkedException).getLinkedException():null;
		    }
		 }
	 }
}
