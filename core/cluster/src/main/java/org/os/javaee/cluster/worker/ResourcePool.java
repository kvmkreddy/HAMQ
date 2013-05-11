package org.os.javaee.cluster.worker;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * <p>Title: ResourcePool.java</p>
 * <p><b>Description:</b> ResourcePool</p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: Open Source Development.</p>
 * @author Murali Reddy
 * @version 1.0
 */
public class ResourcePool<T> implements ClusteredWorker<T>{

	private Queue<T> pool = new ConcurrentLinkedQueue<T>(); 

	public ResourcePool() {	}

	public ResourcePool(Queue<T> pool) {
		this.pool = pool;
	}
	
	public T getWorker() { return deQueue(); }
	
	public Queue<T> getPool() { return pool; }
	public void setPool(Queue<T> pool) { this.pool = pool; }
	
	public T deQueue(){
		return this.getPool().poll();
	}
	
	public void enQueue(T pooledObject){
		this.getPool().add(pooledObject);
	}

	public boolean remove(T pooledObject){
		return this.getPool().remove(pooledObject);
	}
}