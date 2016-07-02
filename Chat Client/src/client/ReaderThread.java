package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import data.Message;
import events.DisconnectionEvent;
import events.DisconnectionListener;
import events.GetMessageEvent;
import events.GetMessageListener;
import events.ListChangeEvent;
import events.ListChangeListener;

public class ReaderThread extends Thread{
	private LinkedList<ListChangeListener> listListeners;
	private LinkedList<GetMessageListener> messageListeners;
	private LinkedList<DisconnectionListener> disconnectionListeners;
	private BufferedReader in;
	private boolean active;	
	
	public ReaderThread(BufferedReader in){
		listListeners = new LinkedList<ListChangeListener>();
		messageListeners = new LinkedList<GetMessageListener>();
		disconnectionListeners = new LinkedList<DisconnectionListener>();
		this.in= in;
		active = true;
	}
	
	public void run(){
		while(active){
			try{				
				String response = in.readLine();
				if(response.startsWith("Message")){
					String[] rs = response.split("####");
					notifyGetMessage(new Message(rs[1],rs[2],rs[3]));					
				}else if(response.startsWith("List:")){
					String[] users = response.substring(5).split(" ");
					List<String> userList = new ArrayList<String>();
					for(String u:users){
						userList.add(u);
					}					
					notifyListChange(userList);
				}
				else if(response.startsWith("Disconnect:")){
					String[] rs = response.split(":");
					notifyDisconnection(rs[1]);
				}
			}catch (IOException e) {
			}
		}
	}
	
	public synchronized void notifyListChange(List<String> userList) {
		ListChangeEvent lce = new ListChangeEvent(this, userList); 
		for(ListChangeListener lcl:listListeners) { 
			lcl.listChanged(lce); 
		} 
	}
	
	public synchronized void addListChangeListener(ListChangeListener lcl){ 
		listListeners.add(lcl);
	}
	
	public synchronized void removeListChangeListener(ListChangeListener lcl){ 
		listListeners.remove(lcl);
	}
	
	public synchronized void notifyGetMessage(Message message) {
		GetMessageEvent gme = new GetMessageEvent(this, message); 
		for(GetMessageListener gml:messageListeners) { 
			gml.getMessage(gme); 
		} 
	}
	
	public synchronized void addGetMessageListener(GetMessageListener gml){ 
		messageListeners.add(gml);
	}
	
	public synchronized void removeGetMessageListener(GetMessageListener gml){ 
		listListeners.remove(gml);
	}
	
	public synchronized void notifyDisconnection(String user) {
		DisconnectionEvent dce = new DisconnectionEvent(this, user); 
		for(DisconnectionListener dcl:disconnectionListeners) { 
			dcl.disconnect(dce); 
		} 
	}
	
	public synchronized void addDisconnectionListener(DisconnectionListener dcl){ 
		disconnectionListeners.add(dcl);
	}
	
	public synchronized void removeDisconnectionListener(DisconnectionListener dcl){ 
		disconnectionListeners.remove(dcl);
	}
	
	public void deactivateReader(){
		active = false;
	}
}
