package org.os.javaee.jms.exception;

import javax.jms.JMSException;

/**
 * <p>Title: HAMQJMSException.java</p>
 * <p><b>Description:</b> HAMQJMSException</p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: Open Source Development.</p>
 * @author Murali Reddy
 * @version 1.0
 */
public class HAMQJMSException extends JMSException {

	private static final long serialVersionUID = 8518711472061989314L;
	protected boolean needsToCloseConnection;
	protected boolean needsToRefreshStatus;
	
	public HAMQJMSException(String arg0) {
		super(arg0);
	}

	public HAMQJMSException(String arg0, String arg1) {
		super(arg0, arg1);
	}

	public HAMQJMSException(String arg0, String arg1, Exception linedException) {
		super(arg0, arg1);
		super.setLinkedException(linedException);
	}

	public HAMQJMSException(String arg0, String arg1, Exception linedException, Throwable cause) {
		super(arg0, arg1);
		super.setLinkedException(linedException);
		super.initCause(cause);
	}
	
	public void recycle() {
		this.setNeedsToCloseConnection(true);
		this.setNeedsToRefreshStatus(true);
	}
	
	public boolean isNeedsToCloseConnection() {
		return needsToCloseConnection;
	}

	public void setNeedsToCloseConnection(boolean needsToCloseConnection) {
		this.needsToCloseConnection = needsToCloseConnection;
	}

	public boolean isNeedsToRefreshStatus() {
		return needsToRefreshStatus;
	}

	public void setNeedsToRefreshStatus(boolean needsToRefreshStatus) {
		this.needsToRefreshStatus = needsToRefreshStatus;
	}
}