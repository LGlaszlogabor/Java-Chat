package events;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public class ListChangeEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	private List<String> userList;
	
	public ListChangeEvent(Object source, List<String> userList){
		super(source);
		this.userList = new ArrayList<String>();
		this.userList.addAll(userList);
	}
	
	public List<String> getUserList(){
		return userList;
	}
}
