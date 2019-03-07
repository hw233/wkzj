package com.jtang.core.protocol;

/**
 * 请求的协议类
 * 
 * @author 0x737263
 * 
 */
public class Request extends DataPacket {

	public static Request valueOf(byte module, byte cmd) {
		Request request = new Request();
		request.setCmd(cmd);
		request.setModule(module);
		return request;
	}

	public static Request valueOf(byte module, byte cmd, byte[] value) {
		Request request = valueOf(module, cmd);
		request.setValue(value);
		return request;
	}

	public String toString() {
		return "Request Object: [module=" + this.getModule() + ", cmd=" + this.getCmd() + ",value=" + this.getValue().toString() + "]";
	}
}