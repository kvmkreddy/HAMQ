package org.os.javase.util.logger;

import org.apache.log4j.Level;
import org.apache.log4j.Priority;

/**
 * <p>Title: Logger.java</p>
 * <p>Description: Logger which acts as the Serializable Logger. This class wraps all the calls to the underlying org.apache.log4j.Logger.</p>
 * 
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: Open Source Development.</p>
 * @author Murali Reddy
 * @version 1.0
 */
public class Logger implements java.io.Serializable {
	
	private static final long serialVersionUID = 7171720937628642333L;

	transient protected org.apache.log4j.Logger LOG = null;
	
	protected String loggerName = null;
	  
	private Logger(org.apache.log4j.Logger logger) { LOG = logger; }
	
	public static Logger getLogger(String className){
		return new Logger(org.apache.log4j.Logger.getLogger(className));
	}
	
	public static Logger getLogger(Class<?> clazz){
		return new Logger(org.apache.log4j.Logger.getLogger(clazz));
	}
	
	public void debug(Object message) { if(LOG.isDebugEnabled()) LOG.debug(message); }
	public void debug(Object message, Throwable t) { LOG.debug(message, t); }
	
	public void info(Object message) { if(LOG.isInfoEnabled()) LOG.info(message); }
	public void info(Object message, Throwable t) { LOG.info(message, t); }
	
	public void error(Object message) { LOG.error(message); }
	public void error(Object message, Throwable t) { LOG.error(message, t); }

	public void warn(Object message) { LOG.warn(message); }
	public void warn(Object message, Throwable t) { LOG.warn(message, t); }
  
	public void fatal(Object message) { LOG.fatal(message); }
	public void fatal(Object message, Throwable t) { LOG.fatal(message,t); }
	
	public boolean isEnabledFor(Priority level) { return LOG.isEnabledFor(level); }
	public boolean isDebugEnabled() { return isEnabledFor(Level.DEBUG);}
	public boolean isInfoEnabled() { return isEnabledFor(Level.INFO); }
	
	public final Level getLevel() { return LOG.getLevel(); }
	public void setLevel(Level level) { LOG.setLevel(level); }
	
	public void log(Priority priority, Object message) { LOG.log(priority, message); }
}
