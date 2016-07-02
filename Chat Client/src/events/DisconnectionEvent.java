package events;

import java.util.EventObject;

public class DisconnectionEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	private String user;

	public DisconnectionEvent(Object source, String user){
		super(source);
		this.user = user;
	}

	public String getUser(){
		return user;
	}
}
