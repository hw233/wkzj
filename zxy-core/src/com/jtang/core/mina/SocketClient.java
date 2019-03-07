package com.jtang.core.mina;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.DefaultConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.executor.OrderedThreadPoolExecutor;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.jtang.core.mina.codec.CodecFactory;
import com.jtang.core.thread.NamedThreadFactory;

/**
 * socket客户端.
 * @author 0x737263
 *
 */
public class SocketClient {
	private static final Log LOGGER = LogFactory.getLog(SocketClient.class);

	private String clientName;
	private ProtocolCodecFactory codecFactory;
	private IoHandler ioHandler;
	private IoFilter[] ioFilters;
	
	/** 该线程池执行会按顺序 */
	private OrderedThreadPoolExecutor filterExecutor;
	private NioSocketConnector socketConnector;
	private ConnectFuture connectFuture;
	
	private int workPoolMin = 8;
	private int workPoolMax = 24;
	private int workPoolIdle = 30;
	
	private int sendBufferSize = 1024 * 1024;
	private int receiveBufferSize = 512 * 1024;
	
	private InetSocketAddress address;
	
	private boolean isConnecting = false;


	public SocketClient(String clientName, ProtocolEncoder encoder, ProtocolDecoder decoder, IoHandler ioHandler, IoFilter... filters) {
		this.clientName = clientName;
		this.codecFactory = new CodecFactory(encoder, decoder);
		this.ioHandler = ioHandler;
		this.ioFilters = filters;
	}
	
	public void setSendBufferSize(int sendBufferSize) {
		this.sendBufferSize = sendBufferSize;
	}
	
	public void setReceiveBufferSize(int receiveBufferSize) {
		this.receiveBufferSize = receiveBufferSize;
	}
	
	public void setWorkPool(int poolMin, int poolMax, int poolIdle) {
		this.workPoolMin = poolMin;
		this.workPoolMax = poolMax;
		this.workPoolIdle = poolIdle;
	}
	
	/**
	 * 
	 * @param connectIp
	 * @param connectPort
	 */
	public void start(String connectIp, int connectPort) {
		address = new InetSocketAddress(connectIp, connectPort);
		if (this.codecFactory == null) {
			throw new NullPointerException("socket client ProtocolCodecFactory is null...");
		}

		if (this.ioHandler == null) {
			throw new NullPointerException("socket client IoHandler is null...");
		}

		NamedThreadFactory threadFactory = new NamedThreadFactory(new ThreadGroup(clientName), clientName);
		filterExecutor = new OrderedThreadPoolExecutor(workPoolMin, workPoolMax, workPoolIdle, TimeUnit.SECONDS, threadFactory);

		// new socket connector
		socketConnector = new NioSocketConnector();

		DefaultIoFilterChainBuilder filterChain = socketConnector.getFilterChain();
		if (this.ioFilters != null) {
			for (IoFilter filter : this.ioFilters) {
				filterChain.addLast(filter.getClass().getName(), filter);
			}
		}

		filterChain.addLast("codecFactory", new ProtocolCodecFilter(this.codecFactory));
		filterChain.addLast("threadPool", new ExecutorFilter(filterExecutor));

		socketConnector.getSessionConfig().setSoLinger(0);
		socketConnector.getSessionConfig().setSendBufferSize(this.sendBufferSize);
		socketConnector.getSessionConfig().setReceiveBufferSize(this.receiveBufferSize);
		socketConnector.setHandler(this.ioHandler);
		connect();
		
	}
	
	
	/**
	 * 建立新的连接
	 */
	public void reconnect() {
		if (socketConnector.isActive() && isConnecting == false) {
			return;
		} 
		connect();
	}
	
	private void connect() {
		isConnecting = true;
		connectFuture = socketConnector.connect(address);
		connectFuture.addListener(new IoFutureListener<DefaultConnectFuture>() {

			@Override
			public void operationComplete(DefaultConnectFuture arg0) {
				if (arg0.isConnected()) {
					isConnecting = false;
					LOGGER.info(clientName + "connect complete...");
				} else {
					isConnecting = true;
					LOGGER.info(clientName + "connect fail...");
				}
			}
		});
		connectFuture.awaitUninterruptibly();
		
	}
	
	
}
