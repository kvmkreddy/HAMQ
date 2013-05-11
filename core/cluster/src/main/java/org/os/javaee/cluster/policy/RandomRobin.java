package org.os.javaee.cluster.policy;

import java.util.List;
import java.util.Random;

import org.os.javaee.cluster.worker.ClusteredWorker;
import org.os.javase.util.logger.Logger;

/**
 * <p>Title: RandomRobin.java</p>
 * <p><b>Description:</b> RandomRobin.java</p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: Open Source Development.</p>
 * @author Murali Reddy
 * @version 1.0
 */
public class RandomRobin<T> implements IPolicy<T>{

	/** This needs to be a class variable or else you end up with multiple
	 * Random numbers with the same seed when many clients lookup.
	 */
	public static final Random localRandomizer = new Random (System.currentTimeMillis ());
	private static final Logger log = Logger.getLogger(RandomRobin.class);
	/**
	 * This method will fetch Worker based on in build random algorithm.
	 *
	 * @see org.os.javaee.cluster.policy.IPolicy#getWorkerObject(org.os.javaee.cluster.policy.ClusterInfo)
	 */
	public <U extends ClusteredWorker<T>> T getWorkerObject(ClusterInfo<U> clusterInfo) throws RuntimeException{
		  log.info("Fetching Worker using RandomRobin Policy");
	      List<U> targets = clusterInfo.getClusterInfo();
	      int max = targets.size();
	      if (max == 0){
	         throw new RuntimeException("All Workers/WorkerPools are not active.");
	      }
	      int cursor = localRandomizer.nextInt (max);
	      return targets.get(cursor).getWorker();
	}
}