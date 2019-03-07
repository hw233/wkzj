package com.jtang.core.protocol;

/**
 * 响应的协议类
 * 
 * @author 0x737263
 * 
 */
public class Response extends DataPacket {
	
	/**
	 * 返回状态码 0 正常
	 */
	private short statusCode = 0;

	public short getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(short statusCode) {
		this.statusCode = statusCode;
	}
	
	public static Response valueOf(byte module, byte cmd) {
		Response response = new Response();
		response.setModule(module);
		response.setCmd(cmd);
		return response;
	}

	public static Response valueOf(byte module, byte cmd, short status) {
		Response response = valueOf(module, cmd);
		response.setStatusCode(status);
		return response;
	}

	public static Response valueOf(byte module, byte cmd, byte[] value) {
		Response response = valueOf(module, cmd);
		response.setValue(value);
		return response;
	}

	@Override
	public String toString() {
		return "Response Object: [module=" + this.getModule() + ", cmd=" + this.getCmd() + ",value=" + this.getValue() + ",statusCode="
				+ this.getStatusCode() + "]";
	}

}