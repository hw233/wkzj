package com.jtang.core.mina.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 编解码工厂类
 * @author 0x737263
 *
 */
public class CodecFactory implements ProtocolCodecFactory {
	private static final Logger logger = LoggerFactory.getLogger(CodecFactory.class);
	private ProtocolEncoder encoder;
	private ProtocolDecoder decoder;

	public CodecFactory(ProtocolEncoder encoder, ProtocolDecoder decoder) {
		if (encoder == null) {
			logger.error("ProtocolEncoder is null!");
			throw new NullPointerException("ProtocolEncoder is null!");
		}

		if (decoder == null) {
			logger.error("ProtocolDecoder is null!");
			throw new NullPointerException("ProtocolDecoder is null!");
		}

		this.encoder = encoder;
		this.decoder = decoder;
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		return this.decoder;
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return this.encoder;
	}

}