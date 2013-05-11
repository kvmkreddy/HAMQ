package org.os.javaee.cluster.policy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * <p>Title: ClusterInfo.java</p>
 * <p><b>Description:</b> ClusterInfo</p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: Open Source Development.</p>
 * @author Murali Reddy
 * @version 1.0
 */
@SuppressWarnings("hiding")
public class ClusterInfo<ClusteredWorker> {

	public final static int UNINITIALIZED_CURSOR = 999999999;

	private AtomicInteger cursor;
	private List<ClusteredWorker> clusterInfo = null;

	public ClusterInfo() { }

	public ClusterInfo(List<ClusteredWorker> clusterInfo) {
		this.clusterInfo = cloneList(clusterInfo);
	}

	public AtomicInteger getCursor() { return cursor; }
	public void setCursor(int cursor) { getCursor().getAndSet(cursor); }

	public List<ClusteredWorker> getClusterInfo() { return clusterInfo; }
	public void setClusterInfo(List<ClusteredWorker> clusterInfo) { this.clusterInfo = clusterInfo; }
	
	@SuppressWarnings("unchecked")
	private List<ClusteredWorker> cloneList(List<ClusteredWorker> toClone) {
		if (toClone instanceof ArrayList) {
			synchronized (toClone) {
				return (ArrayList<ClusteredWorker>) ((ArrayList<ClusteredWorker>) toClone).clone();
			}
		}
		List<ClusteredWorker> clone = new ArrayList<ClusteredWorker>(toClone.size());
		synchronized (toClone) {
			clone.addAll(toClone);
		}
		return clone;
	}
}