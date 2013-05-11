package org.os.javaee.jms.core;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import javax.jms.Connection;

import org.os.javaee.util.JMSUtil;

/**
 * <p>Title: ConnectionWrapper.java</p>
 * <p><b>Description:</b> ConnectionWrapper</p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: Open Source Development.</p>
 * @author Murali Reddy
 * @version 1.0
 */
public class ConnectionWrapper extends ReentrantLock{

	private static final long serialVersionUID = -6904378338081953049L;
	private static final int DEFAULT_MAX_USAGE = 100;
	private static final AtomicInteger RESET_USAGE_TO_ZERO = new AtomicInteger(0);
	protected int maxUsage = DEFAULT_MAX_USAGE;
	protected AtomicInteger usage = new AtomicInteger(RESET_USAGE_TO_ZERO.intValue());
	
	/**
	 * JMS Connection which will be wrapped by this class.
	 */
	private volatile Connection connection = null;

	private volatile boolean inUse = false;
	
	public ConnectionWrapper(Connection connection) {
		this.connection = connection;
		
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void acquire(){
		this.setInUse(true);
	}
	
	public void release(){
		this.setInUse(false);
	}
	
	public void close(){
		for(;;){
			if(! isInUse()){
				this.lock();
					try{
						JMSUtil.close(this.connection);
						this.connection = null;
					}finally{
						this.unlock();
					}
			}
		}
	}

	protected boolean isInUse() {
		return inUse;
	}

	protected void setInUse(boolean inUse) {
		this.inUse = inUse;
	}
}