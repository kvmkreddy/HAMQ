package org.os.javaee.cluster.policy;

import org.os.javaee.cluster.worker.ClusteredWorker;


/**
 * <p>Title: IPolicy.java</p>
 * <p><b>Description:</b> IPolicy.java</p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: Open Source Development.</p>
 * @author Murali Reddy
 * @version 1.0
 */
public interface IPolicy<T> {
	   /**
	    * Fetches the actualWorker based on load balancing algorithm(i.e implementation).
	    * 
	    * @param clusterFamily A list of potential target nodes
	    * @return The selected target for the next invocation
	    */   
	   public <U extends ClusteredWorker<T>> T getWorkerObject(ClusterInfo<U> clusterInfo) throws RuntimeException;
}
