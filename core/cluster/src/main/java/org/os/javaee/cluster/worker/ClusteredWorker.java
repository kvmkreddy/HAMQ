package org.os.javaee.cluster.worker;

/**
 * <p>Title: ClusteredWorker.java</p>
 * <p><b>Description:</b> ClusteredWorker.java</p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: Open Source Development.</p>
 * @author Murali Reddy
 * @version 1.0
 */
public interface ClusteredWorker<T> {

	public T getWorker();
	
}
