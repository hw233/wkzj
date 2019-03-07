package com.jtang.core.protocol;

/**
 * 附带角色id请求包头类
 * @author 0x737263
 *
 */
public class ActorRequest extends Request {

	private long actorId;
	
	public long getActorId() {
		return this.actorId;
	}
	
	public void setActorId(long actorId) {
		this.actorId = actorId;
	}
	
	public static ActorRequest valueOf(byte module, byte cmd) {
		ActorRequest request = new ActorRequest();
		request.setCmd(cmd);
		request.setModule(module);
		return request;
	}
	
	public static ActorRequest valueOf(byte module, byte cmd, byte[] value) {
		return valueOf(module, cmd, 0, value);
	}
	
	public static ActorRequest valueOf(byte module, byte cmd, long actorId, byte[] value) {
		ActorRequest request = new ActorRequest();
		request.setModule(module);
		request.setCmd(cmd);
		request.setActorId(actorId);
		request.setValue(value);
		return request;
	}
	
	public ActorResponse convert2Response() {
		ActorResponse response = new ActorResponse();
		response.setModule(this.getModule());
		response.setCmd(this.getCmd());
		response.setActorId(this.getActorId());
		return response;
	}
}
