package com.jtang.core.mina;

import com.jtang.core.mina.codec.CodecFactory;
import com.jtang.core.thread.NamedThreadFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.SimpleIoProcessorPool;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.executor.OrderedThreadPoolExecutor;
import org.apache.mina.filter.logging.MdcInjectionFilter;
import org.apache.mina.transport.socket.DefaultSocketSessionConfig;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioProcessor;
import org.apache.mina.transport.socket.nio.NioSession;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 * socket服务端.
 * @author 0x737263
 * 
 */
public class SocketServer {

	private static final Log LOGGER = LogFactory.getLog(SocketServer.class);

	private IoHandler ioHandler;

	private SocketAcceptor acceptor;

	private InetSocketAddress address;

	private IoFilter[] ioFilters;

	private ProtocolCodecFactory codecFactory;

	/** 该线程池执行会按顺序 */
	private OrderedThreadPoolExecutor filterExecutor;

	/** nio处理数据 */
	private SimpleIoProcessorPool<NioSession> nioProcess;
	
	private String socketName = "socket name";
	
	private int workPoolMin = 8;
	private int workPoolMax = 24;
	private int workPoolIdle = 30;
	
	private int backLog = 8000;
	private boolean tcpNoDelay = true;
	private int sendBufferSize = 4096;
	private int readBufferSize = 4096;
	private int receiveBufferSize = 4096;
	private int bothIdleTime = 30;
	private int writeTimeout = 30;
	
	/**
	 * Constructor.
	 * @param codec
	 * @param ioHandler
	 * @param workPoolMin
	 * @param workPoolMax
	 * @param workPoolIdle
	 * @param nioProcessNum
	 */
	public SocketServer(String socketName, ProtocolEncoder encoder, ProtocolDecoder decoder, IoHandler ioHandler, IoFilter... filters) {
		this.socketName = socketName;
		this.codecFactory = new CodecFactory(encoder, decoder);
		this.ioHandler = ioHandler;
		this.ioFilters = filters;
	}
	
	public void setWorkPool(int poolMin, int poolMax, int poolIdle) {
		this.workPoolMin = poolMin;
		this.workPoolMax = poolMax;
		this.workPoolIdle = poolIdle;
	}
	
	public void setBackLog(int backLog) {
		this.backLog = backLog;
	}
	
	public void setTcpNoDelay(boolean delay) {
		this.tcpNoDelay = delay;
	}
	
	public void setReadBufferSize(int readBufferSize) {
		this.readBufferSize = readBufferSize;
	}
	
	public void setSendBufferSize(int sendBufferSize) {
		this.sendBufferSize = sendBufferSize;
	}
	
	public void setReceiveBufferSize(int receiveBufferSize) {
		this.receiveBufferSize = receiveBufferSize;
	}
	
	public void setWriteTimeout(int writeTimeout) {
		this.writeTimeout = writeTimeout;
	}

	public void setBothIdleTime(int idleTime) {
		this.bothIdleTime = idleTime;
	}
	
	/**
	 * 启动
	 * @param port		监听端口
	 * @throws Exception
	 */
	public void start(int port) throws Exception {		
		if (this.codecFactory == null) {
			throw new NullPointerException("ProtocolCodecFactory is null...");
		}

		if (this.ioHandler == null) {
			throw new NullPointerException("IoHandler is null...");
		}
		
		NamedThreadFactory threadFactory = new NamedThreadFactory(new ThreadGroup(socketName), socketName);
		filterExecutor = new OrderedThreadPoolExecutor(workPoolMin, workPoolMax, workPoolIdle, TimeUnit.SECONDS, threadFactory);
		nioProcess = new SimpleIoProcessorPool<NioSession>(NioProcessor.class);

		IoBuffer.setUseDirectBuffer(false);
		IoBuffer.setAllocator(new SimpleBufferAllocator());
		this.acceptor = new NioSocketAcceptor(nioProcess);
		this.acceptor.setReuseAddress(true);
		this.acceptor.setBacklog(this.backLog);
		this.acceptor.getSessionConfig().setAll(getSessionConfig());

		MdcInjectionFilter mdcInjectionFilter = new MdcInjectionFilter();
		DefaultIoFilterChainBuilder filterChain = this.acceptor.getFilterChain();
		filterChain.addLast("mdcInjectionFilter", mdcInjectionFilter);
		if (ioFilters != null) {
			for (IoFilter e : ioFilters) {
				filterChain.addLast(e.getClass().getName(), e);
			}
		}
		filterChain.addLast("codecFactory", new ProtocolCodecFilter(this.codecFactory));
		filterChain.addLast("threadPool", new ExecutorFilter(filterExecutor));
		this.acceptor.setHandler(this.ioHandler);
		this.address = new InetSocketAddress(port);
		this.acceptor.bind(this.address);
		LOGGER.info(socketName + " listening on " + this.address.getHostName() + ":" + this.address.getPort());
	}

	private SocketSessionConfig getSessionConfig() {
		SocketSessionConfig sessionConfig = new DefaultSocketSessionConfig();
		sessionConfig.setSoLinger(0);
		sessionConfig.setKeepAlive(true);
		sessionConfig.setReuseAddress(true);
		sessionConfig.setTcpNoDelay(this.tcpNoDelay);
		sessionConfig.setReadBufferSize(this.readBufferSize);
		sessionConfig.setSendBufferSize(this.sendBufferSize);
		sessionConfig.setReceiveBufferSize(this.receiveBufferSize);
		sessionConfig.setBothIdleTime(this.bothIdleTime);
		sessionConfig.setWriteTimeout(this.writeTimeout);
		return sessionConfig;
	}
	
	public void stop() {
		if (this.acceptor != null) {
			this.acceptor.unbind();
			this.acceptor.dispose();
			this.acceptor = null;
		}

		if (filterExecutor != null) {
			filterExecutor.shutdown();
			try {
				filterExecutor.awaitTermination(5000L, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				LOGGER.error("stop server exception:", e);
			} finally {

			}
		}
	}
}