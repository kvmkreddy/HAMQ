package org.os.javaee.jms.exception;

/**
 * <p>Title: JMSProviderFailureException.java</p>
 * <p>Description: JMSProviderFailureException.java</p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: Open Source Development.</p>
 * @author Murali Reddy
 * @version 1.0
 */
public class JMSProviderFailureException extends Exception {

	private static final long serialVersionUID = 1L;

	public JMSProviderFailureException() {
		super();
	}

	public JMSProviderFailureException(String arg0) {
		super(arg0);
	}

	public JMSProviderFailureException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public JMSProviderFailureException(Throwable arg0) {
		super(arg0);
	}
}
