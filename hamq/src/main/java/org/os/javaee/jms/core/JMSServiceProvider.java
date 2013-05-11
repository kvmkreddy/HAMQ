package org.os.javaee.jms.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.InvalidDestinationException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageFormatException;
import javax.jms.MessageProducer;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;

import org.os.javaee.jms.core.mapper.IMessageMapper;
import org.os.javaee.jms.core.message.MessageImpl;
import org.os.javaee.jms.core.message.SimpleMessageImpl;
import org.os.javaee.jms.exception.HAMQJMSException;
import org.os.javaee.jms.service.ConnectionWrapperService;
import org.os.javaee.jms.service.IJMSDestinationFetchService;
import org.os.javaee.jms.util.MessageLoggerUtil;
import org.os.javaee.util.JMSUtil;
import org.os.javaee.util.JNDIUtil;
import org.os.javase.util.logger.Logger;

/**
 * <p>Title: JMSServiceProvider.java</p>
 * <p>Description: This is the class which actually connects to the underlying Messaging System.</p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: Open Source Development.</p>
 * @author Murali Reddy
 * @version 1.0
 */
public class JMSServiceProvider {

	private static final int DEFAULT_MAX_USAGE = 100;
	private static final AtomicInteger RESET_USAGE_TO_ZERO = new AtomicInteger(0);
	
	protected static final Logger log = Logger.getLogger(JMSServiceProvider.class.getCanonicalName());

	protected volatile Connection connection;
	protected ExceptionListener exceptionListener;
	protected IMessageMapper defaultMapper;
	protected IJMSDestinationFetchService jmsDestinationFetchService;
	protected ConnectionWrapperService connectionWrapperService;
	protected ConnectionWrapper connectionWrapper;
	
	protected Map<String, Destination> destinationMap = new ConcurrentHashMap<String, Destination>();
	protected int maxUsage = DEFAULT_MAX_USAGE;
	protected AtomicInteger usage = new AtomicInteger(RESET_USAGE_TO_ZERO.intValue());
	private String connectionFactoryName = null;
	protected boolean isServiceUp = true;
	
	public JMSServiceProvider() { }
	
	public JMSServiceProvider(String connectionFactoryName, ExceptionListener exceptionListener, int maxUsage) {
		initialize(connectionFactoryName,exceptionListener,maxUsage);
	}

	/**
	 * Initialise the JMSProvider.
	 *
	 * @param _connectionFactoryName ConnectionFactory Name.
	 * @param _exceptionListener Exception Listener class.
	 */
	private void initialize(String _connectionFactoryName, ExceptionListener _exceptionListener,int maxUsage){
		this.setConnectionFactoryName(_connectionFactoryName);
		this.setExceptionListener(_exceptionListener);
		this.setMaxUsage(maxUsage);
	}

	/**
	 *<p>This method is used to initialize the QueueConnection, QueueSession and QueueSender objects</p>
	 * @throws Exception - throws Exception, if any arises.
	 */
	private void initializeJMS() throws HAMQJMSException {
		try {
			log.debug("Looking up outbound Connection Factory -->:"+(connectionFactoryName));
			ConnectionFactory  connectionFactory = JNDIUtil.getConnectionFactory(connectionFactoryName);
			log.debug("Creating a Connection with the ConnectionFactory -->:"+(connectionFactory));
			if(connectionFactory instanceof QueueConnectionFactory){
				connection = (QueueConnection)((QueueConnectionFactory)connectionFactory).createConnection();
			}else if (connectionFactory instanceof TopicConnectionFactory){
				connection = (TopicConnection)((TopicConnectionFactory)connectionFactory).createConnection();
			}
			if(this.getExceptionListener() != null)
				connection.setExceptionListener(this.getExceptionListener());
		} catch (JMSException e) {
			log.fatal("Exception initializing initializeJMS -->:"+(connectionFactoryName), e);
			this.closeJMS();
			throw new HAMQJMSException("Exception initializing initializeJMS -->:"+(connectionFactoryName)+": ANd ExceptionMessage -->:"+(e.getMessage()));
		}
	}
	/**
	 * Gets the ConnectionFactory Name.
	 * @return - ConnectionFactory Name
	 */
	public String getConnectionFactoryName() { return connectionFactoryName; }
	/**
	 * Sets the ConnectionFactory Name
	 * @param connectionFactoryName - ConnectionFactory Name.
	 */
	public void setConnectionFactoryName(String connectionFactoryName) { this.connectionFactoryName = connectionFactoryName; }

	/**
	 * Gets the JMS Exception Listener Name.
	 * @return JMS Exception Listener Name.
	 */
	public ExceptionListener getExceptionListener() { return exceptionListener;	}
	/**
	 * Sets the JMS Exception Listener Name.
	 * @param exceptionListenerName - JMS Exception Listener Name.
	 */
	public void setExceptionListener(ExceptionListener exceptionListener) { this.exceptionListener = exceptionListener;	}

	/**
	 * Returns the MQ Connection (hold by this provider) status.
	 * @return Returns whether the Connection hold by this provider is live or stale
	 */
	public boolean isServiceUp() { return isServiceUp; }


	/**
	 * Sets the MQ Connection (hold by this provider) status.
	 *
	 * @param isServiceUp - Connection(hold by this provider) status.
	 */
	public void setServiceUp(boolean isServiceUp) { this.isServiceUp = isServiceUp; }

	/**
	 * Returns the current Message Usage
	 * @return Current Message Usage
	 */
	public AtomicInteger getUsage() { return usage; }
	/**
	 * Sets the current Message Usage
	 * @param usage - current Message Usage
	 */
	public void setUsage(AtomicInteger usage) { this.usage = new AtomicInteger(usage.get());}

	/**
	 * Gets the Maximum Message Usage.
	 * @return Maximum Message Usage.
	 */
	public int getMaxUsage() { return maxUsage; }

	/**
	 * Sets the Maximum Message Usage.
	 * @param maxUsage - Maximum Message Usage.
	 */
	public void setMaxUsage(int maxUsage) { this.maxUsage = maxUsage; }

	
	/**
	 * Gets the default IMessageMapper  associated/configured to this JMSProvider.
	 * @return IMessageMaper
	 */
	public IMessageMapper getDefaultMapper() {
		return defaultMapper;
	}


	/**
	 * Sets the default IMessageMapper associated/configured to this JMSProvider.
	 * @param defaultMapper - Default IMessageMapper
	 */
	public void setDefaultMapper(IMessageMapper defaultMapper) {
		this.defaultMapper = defaultMapper;
	}

	public IJMSDestinationFetchService getJmsDestinationFetchService() {
		return jmsDestinationFetchService;
	}

	public void setJmsDestinationFetchService(IJMSDestinationFetchService jmsDestinationFetchService) {
		this.jmsDestinationFetchService = jmsDestinationFetchService;
	}
	
	public ConnectionWrapperService getConnectionWrapperService() {
		return connectionWrapperService;
	}

	public void setConnectionWrapperService(
			ConnectionWrapperService connectionWrapperService) {
		this.connectionWrapperService = connectionWrapperService;
	}

	/**
	 * <p>This method is overloaded method.
	 * @see send(MessageImpl msgImpl,IMessageMapper mapper, boolean transacated,int acknowledgeType)
	 * </p>
	 *
	 * @param msgImpl - Disconnected MessageImpl object
	 * @throws JMSException
	 */
	public void send(MessageImpl msgImpl) throws HAMQJMSException {
		send(msgImpl,this.getDefaultMapper());
	}
	
	/**
	 * <p>This method is overloaded method.
	 * @see send(MessageImpl msgImpl,IMessageMapper mapper, boolean transacated,int acknowledgeType)
	 * </p>
	 *
	 * @param msgImpl - Disconnected MessageImpl object
	 * @param mapper - IMessageMapper instance to map Message from MessageImpl.
	 * @throws JMSException
	 */
	public void send(MessageImpl msgImpl,IMessageMapper mapper) throws HAMQJMSException {
		send(msgImpl,mapper,false,Session.AUTO_ACKNOWLEDGE);
	}

	/**
	 * <p>This method is overloaded method.
	 * @see send(MessageImpl msgImpl,IMessageMapper mapper, boolean transacated,int acknowledgeType)
	 * </p>
	 * 
	 * @param msgImpl - Disconnected MessageImpl object
	 * @param mapper - IMessageMapper instance to map Message from MessageImpl.
	 * @param transacated - Is Session transacated? 
	 * @throws JMSException
	 */
	public void send(MessageImpl msgImpl,IMessageMapper mapper,boolean transacated) throws HAMQJMSException {
		send(msgImpl,mapper,transacated,Session.AUTO_ACKNOWLEDGE);
	}

	/**
	 * <p>This method is overloaded method.
	 * @see send(MessageImpl msgImpl,IMessageMapper mapper, boolean transacated,int acknowledgeType)
	 * </p>
	 *
	 * @param msgImpl - Disconnected MessageImpl object
	 * @param mapper - IMessageMapper instance to map Message from MessageImpl.
	 * @param acknowledgeType - Session acknowledge Type
	 * @throws JMSException
	 */
	public void send(MessageImpl msgImpl,IMessageMapper mapper,int acknowledgeType) throws HAMQJMSException {
		send(msgImpl,mapper,false,acknowledgeType);
	}
	
	/**
	 * <p>This method will send the message to the underlying Messaging system.</p>
	 * <p><pre>This method will do the following things in the process of sending the message.
	 * 1.Initialise the {@link Connection} if it is not initialised.
	 * 1.Creates the {@link Session}.
	 * 2.Creates the {@link Message} instance from the {@link Session}
	 * 3.Creates the {@link Messageproducer}.
	 * 4.Map/Translate the {@link Message} from {@link MessageImpl} by using the provided {@link IMessageMapper} instance.
	 * 5.Sends the Message.
	 * 6.Close all the JMS stubs, if any exception happens.
	 * </pre>
	 * </p>
	 *
	 *##################################################################################################################################################################################
	 * TODO 
	 * <h2> This method is not thread safe. If two threads are executing this method on the same object then improper results will occur.
	 * As of now, made this method method as synchronized. But this will impact if the caller/client is sharing the same object and executing/pumping multiple messages in multiple threads. 
	 * Needs to fix the threading issue.</h2>
	 * 
	 * Updated@05/11/13:
	 * As of now only <code>Connection</code> object is thread safe. Remaining all objects(Session, Producer etc) are thread safe(created only in the method). 
	 * The idea for Connection to create a wrapper which holds the actual <code>Connection</code> object reference and all threads will directly use this wrapper(which will be implemented as thread safe).  
	 *##################################################################################################################################################################################
	 *
	 * @param msgImpl - Disconnected MessageImpl object
	 * @param mapper - IMessageMapper instance to map Message from MessageImpl.
	 * @param transacted - boolean represents whether needs to use the Session which will participate in the Transaction.
	 * @param acknowledgeType - Session acknowledge Type
	 * @throws JMSException
	 */
	public synchronized void send(MessageImpl msgImpl,IMessageMapper mapper, boolean transacated,int acknowledgeType) throws HAMQJMSException{
		
		Session session = null;
		try{
			long startMessage = System.currentTimeMillis();
			log.debug("Current Usage -->:"+(this.getUsage())+"\t And Max usage is -->:"+(this.getMaxUsage()));
			checkAndReInitializeJMSStubs();
			log.debug("Going to create a Session");
			session = getSession(transacated,acknowledgeType);
			if(msgImpl instanceof SimpleMessageImpl){
				SimpleMessageImpl _msgImpl = (SimpleMessageImpl)msgImpl;
				MessageProducer producer =  null;
				try {
					log.debug("Going to create the Message Producer for destination -->:"+(_msgImpl.getDestinationName()));
					producer = getMessageProducer(session,_msgImpl.getDestinationName());
					sendMessage(_msgImpl, mapper, session,producer);
				} catch (JMSException jmsx) {
					log.error("Exception in sendMessage() -->:" +(_msgImpl.toString()), jmsx);
					closeJMS(producer);
					
					if(jmsx instanceof HAMQJMSException && ((HAMQJMSException)jmsx).isNeedsToCloseConnection()){
						closeJMS(connection,session,null);
					}
					throw jmsx;
				}finally{
					closeJMS(producer);
				}
			}
			 /**
			  * ##################################################################################################################################################################################  
			  *  TODO
			  *  <h2> Needs to add flexibility where clients can able to send multiple messages(may be to different/same destinations) by using same session (may be transacated).</h2>
			  * ##################################################################################################################################################################################
			  */
			if(isTransacatedSession(transacated, session)){
				session.commit();
			}
			usage.incrementAndGet();
			
			long endMessage = System.currentTimeMillis();
			log.info("Time taken for JMSProvider.send() is -->:"+(endMessage-startMessage)+" milli secs");
			
		}catch(JMSException jmsx) {
			log.error("Exception in send() -->:" +(msgImpl.toString()), jmsx);
			if(isTransacatedSession(transacated, session)){
				try {
					session.rollback();
				} catch (JMSException ignore) { }
			}
			closeJMS(connection, session, null);
			throw createHAMQJMSException(jmsx);
		}finally{
			closeJMS(session);
			checkUsage();
		}
	}

	/**
	 * <p>This method will send the message to the underlying Messaging system.</p>
	 *
	 * @param msgImpl - Disconnected MessageImpl object
	 * @param mapper - IMessageMapper instance to map Message from MessageImpl.
	 * @param session - JMS Session object
	 * @param producer - JMS MessageProducer object
	 * @throws JMSException
	 */
	protected void sendMessage(SimpleMessageImpl msgImpl,IMessageMapper mapper, Session session, MessageProducer producer) throws JMSException{
		boolean isRecycleNeeded = false;
		try {
			addMessageSpecificProducerConfigurations(producer,msgImpl);
			log.debug("Created the MessageProducer for destination -->:"+(msgImpl.getDestinationName())+"\t Producer is -->:"+(producer));
			log.debug("Going to MAP the message with the mapper -->:"+(mapper));
			long startPop = System.currentTimeMillis();
			Message msg = mapper.map(session,msgImpl);
			long endPop = System.currentTimeMillis();
			log.info("Time taken for populateMessage() is -->:"+(endPop-startPop)+" milli secs");
			String message = MessageLoggerUtil.logMessage(msg);
			log.debug("Completed the mapping");
			try {
				producer.send(msg);
			} catch (MessageFormatException  msgFormatExp) {
				log.error("MessageFormatException: Exception message -->:"+(msgFormatExp.getMessage()),msgFormatExp);
			}catch (InvalidDestinationException invalidDestExp) {
				log.error("InvalidDestinationException : Exception message -->:"+(invalidDestExp.getMessage()),invalidDestExp);
			}catch (UnsupportedOperationException unSupOpExp) {
				log.error("UnsupportedOperationException : Exception message -->:"+(unSupOpExp.getMessage()),unSupOpExp);
			}catch (JMSException jmsExp) {
				log.error("JMSException: Exception message -->:"+(jmsExp.getMessage()),jmsExp);
				isRecycleNeeded = true;
			}
			log.debug("Send the message Sucessfully -->:"+(message));
		} catch (JMSException jmsx) {
			log.error("Exception in sendMessage() call.", jmsx);
			HAMQJMSException jmsExp = createHAMQJMSException(jmsx);
			if(isRecycleNeeded){
				jmsExp.recycle();
			}
			throw jmsExp;
		}
	}

	/**
	 * @param transacated
	 * @param session
	 * @return
	 */
	private boolean isTransacatedSession(boolean transacated, Session session) {
		return session != null && transacated;
	}

	/**
	 * @param jmsx
	 * @return
	 */
	private HAMQJMSException createHAMQJMSException(JMSException jmsx) {
		HAMQJMSException hamqJMSException = null;
		hamqJMSException = new HAMQJMSException(jmsx.getMessage(), jmsx.getErrorCode(), jmsx.getLinkedException(), jmsx.getCause());
		if (jmsx instanceof HAMQJMSException){
			hamqJMSException.setNeedsToCloseConnection(((HAMQJMSException)jmsx).isNeedsToCloseConnection());
			hamqJMSException.setNeedsToRefreshStatus(((HAMQJMSException)jmsx).isNeedsToRefreshStatus());
			
		}
		return hamqJMSException;
	}

	/**
	 * Retrieves a MessageProducer from the producerMap for the passed </code>destination</code.
	 * If the MessageProducer for the passed in </code>destination</code> is not found in the producerMap,
	 * a new one is created, stored in the producerMap and then returned.
	 * @param destination The destination to acquire a MessageProducer for.
	 * @return A MessageProducer for the passed destination.
	 * @throws JMSException Thrown is an exception occurs when creating a new MessageProducer.
	 */
	private MessageProducer getMessageProducer(Session session, String destinationName) throws JMSException {
		try {
			return session.createProducer(getDestination(destinationName));
		} catch(JMSException jmsx) {
			log.error("Exception Creating Message Producer for the given Destination -->:" +(destinationName), jmsx);
			throw jmsx;
		}
	}

	/**
	 * Retrieves a Destination from the destinationMap for the passed </code>destination</code.
	 * If the Destination for the passed in </code>destination</code> is not found in the destinationMap,
	 * destination is looked up in the JNDI, stored in the destinationMap and then returned.
	 * @param destination The destinationName to look up
	 * @return A Destination for the passed destinationName.
	 */
	private Destination getDestination(String destinationName) throws JMSException {
		if(destinationMap.containsKey(destinationName))
			return destinationMap.get(destinationName);
		Destination destination = this.getJmsDestinationFetchService().getDestination(destinationName, this.getConnectionFactoryName());
		destinationMap.put(destinationName, destination);
		return destination;
	}

	/**
	 * <p>Will close the Connection, Session and MessageProducer objects</p>
	 * @see org.murali.javaee.util.JMSUtil#close(javax.jms.Connection connection, javax.jms.Session session, javax.jms.MessageProducer producer)
	 */
	private void closeJMS() {
		closeJMS(connection, null, null); 
	}
	
	/**
	 * <p>Will close the Connection, Session and MessageProducer objects</p>
	 * @see org.murali.javaee.util.JMSUtil#close(javax.jms.Connection connection, javax.jms.Session session, javax.jms.MessageProducer producer)
	 */
	private void closeJMS(Connection connection, Session session, MessageProducer producer) {
		log.debug("In closeJMS() call.");
		JMSUtil.close(connection, session, producer); 
		connection = null;
		session = null;
		producer = null;
	}
	
	/**
	 * <p>Will close the Reference object if it is either of Connection, Session , MessageProducer and MessageConsumet objects</p>
	 * @see org.murali.javaee.util.JMSUtil#close(Object reference)
	 */
	private void closeJMS(Object reference) {
		log.debug("In closeJMS() call.");
		if(reference != null){
			JMSUtil.close(reference);
			reference = null;
		}
	}	
	
	/**
	 * This method is used to check the JMS Connection object.
	 * If it is null then try to (re)initialize the stub connections.
	 *
	 * @throws JMSException
	 */
	private void checkAndReInitializeJMSStubs() throws JMSException {
		if (connection == null){
			try {
			initializeJMS();
			} catch (JMSException jmsExp) {
				log.error("JMSException: Exception in initializing JMS. Exception Message -->:"+(jmsExp.getMessage()),jmsExp);
				throw new JMSException("JMSException: Exception in initializing JMS. " + (jmsExp.getMessage()));
			}
		}
	}

	private Session getSession(boolean transacated, int acoknowledeType) throws JMSException{
		return connection.createSession(transacated, acoknowledeType);
	}

	private void checkUsage(){
		if(this.getMaxUsage() == -1){
			log.debug("No need to do anything. Because Max Usage is -1.");
		}else if(this.getUsage().intValue() >= this.getMaxUsage()){
			this.closeJMS();
			this.setUsage(RESET_USAGE_TO_ZERO);
		}else{
			log.debug("Current usage :"+(getUsage())+" did not reached Max Usage :"+(getMaxUsage())+". Do not close the JMS.");
		}
	}

	private void addMessageSpecificProducerConfigurations(MessageProducer producer, SimpleMessageImpl msgImpl) throws JMSException{
		if(msgImpl.isAnyMessageSpeificProducerConfigurations()){
			producer.setDeliveryMode(msgImpl.getDeliveryMode());
			producer.setPriority(msgImpl.getPriority());
			producer.setTimeToLive(msgImpl.getTimeToLive());
		}else{
			log.debug("There is no Message Specfic Message Producer configurations exist.");
		}
	}

	@Deprecated
	private void switchToDefaultForMessageProducerConfigurations(MessageProducer producer) throws JMSException{
		producer.setDeliveryMode(Message.DEFAULT_DELIVERY_MODE);
		producer.setPriority(Message.DEFAULT_PRIORITY);
		producer.setTimeToLive(Message.DEFAULT_TIME_TO_LIVE);
	}
}
