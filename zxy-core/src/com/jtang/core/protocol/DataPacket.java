package com.jtang.core.protocol;

/**
 * 请求包、响应包的抽象类
 * @author 0x737263
 *
 */
public abstract class DataPacket {

	/**
	 * 模块id
	 */
	private byte module;

	/**
	 * 命令id
	 */
	private byte cmd;

	/**
	 * 消息具体数据
	 */
	private byte[] value;

	public byte getModule() {
		return this.module;
	}

	public void setModule(byte module) {
		this.module = module;
	}

	public byte getCmd() {
		return this.cmd;
	}

	public void setCmd(byte cmd) {
		this.cmd = cmd;
	}

	public byte[] getValue() {
		return this.value;
	}

	public void setValue(byte[] value) {
		this.value = value;
	}
	
	/**
	 * 转换为Response
	 * @return
	 */
	public Response convert2Response() {
		Response response = new Response();
		response.setModule(this.module);
		response.setCmd(this.cmd);
		return response;
	}
	
	/**
	 * 转换为Request
	 * @return
	 */
	public Request convert2Request() {
		Request request = new Request();
		request.setModule(this.module);
		request.setCmd(this.cmd);
		return request;
	}
	
}
