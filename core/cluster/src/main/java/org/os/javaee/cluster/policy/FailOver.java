package org.os.javaee.cluster.policy;

import org.os.javaee.cluster.worker.ClusteredWorker;
import org.os.javase.util.logger.Logger;

/**
 * <p>Title: FailOver.java</p>
 * <p><b>Description:</b> FailOver.java</p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: Open Source Development.</p>
 * @author Murali Reddy
 * @version 1.0
 */
public class FailOver<T> implements IPolicy<T> {

	private static final Logger log = Logger.getLogger(FailOver.class);

	/**
	 * @see org.os.javaee.cluster.policy.IPolicy#getWorkerObject(org.os.javaee.cluster.policy.ClusterInfo)
	 */
	public <U extends ClusteredWorker<T>> T getWorkerObject(ClusterInfo<U> clusterInfo) throws RuntimeException{

		log.info("Fetching Worker using FailOver Policy");
		
		if(clusterInfo != null && clusterInfo.getClusterInfo()!= null){
			switch(clusterInfo.getClusterInfo().size()){
			  case 0:
					throw new RuntimeException("All Workers/WorkerPools are not active.");
			  default:
                    return clusterInfo.getClusterInfo().get(0).getWorker();
		    }
		}else{
			StringBuffer sb = new StringBuffer();
			if(clusterInfo == null){
				sb.append("ClusterInfo is NULL.");
			}else if(clusterInfo.getClusterInfo() == null){
				sb.append("Worker List is NULL.");
			}else if(clusterInfo.getClusterInfo().size()<2){
				sb.append("Number of Workers/WorkerPools should be at least two(i.e Primary and BackUp).");
			}else{
				sb.append("Some issue in fetching Worker object.");
			}
			throw new RuntimeException(sb.toString());
		}
	}
}