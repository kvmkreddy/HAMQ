package org.os.javaee.cluster.policy;

import java.util.List;

import org.os.javaee.cluster.worker.ClusteredWorker;
import org.os.javase.util.logger.Logger;
/**
 * <p>Title: RoundRobin.java</p>
 * <p><b>Description:</b> RoundRobin.java</p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: Open Source Development.</p>
 * @author Murali Reddy
 * @version 1.0
 */
public class RoundRobin<T> implements IPolicy<T> {

	private static final Logger log = Logger.getLogger(RoundRobin.class);
	/**
	 * This method will fetch Worker based on in build <i><b>Round Robin</b></i> algorithm.
	 *
	 * @see org.os.javaee.cluster.policy.IPolicy#getWorkerObject(org.os.javaee.cluster.policy.ClusterInfo)
	 */
	public <U extends ClusteredWorker<T>> T getWorkerObject(ClusterInfo<U> clusterInfo) throws RuntimeException{
		log.info("Fetching Worker using RoundRobin Policy");
	      int cursor = clusterInfo.getCursor().get();
	      List<U> targets = clusterInfo.getClusterInfo();
	       if (targets.size () == 0)
	    	  throw new RuntimeException("All Workers/WorkerPools are not active.");

	      if (cursor == ClusterInfo.UNINITIALIZED_CURSOR){
	         // Obtain a random index into targets
	         cursor = RandomRobin.localRandomizer.nextInt(targets.size());
	      }else{
	         // Choose the next target
	         cursor = ( (cursor + 1) % targets.size());
	      }
	      clusterInfo.setCursor(cursor);
	      return targets.get(cursor).getWorker();
	}
}