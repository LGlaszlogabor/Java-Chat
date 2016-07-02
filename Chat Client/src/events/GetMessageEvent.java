package events;

import java.util.EventObject;

import data.Message;

public class GetMessageEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	private Message message;

	public GetMessageEvent(Object source, Message message){
		super(source);
		this.message = message;
	}

	public Message getMessage(){
		return message;
	}
}
