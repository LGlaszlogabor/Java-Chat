package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientHandle extends Thread{
	private Socket client;
	private BufferedReader reader;
	private PrintWriter writer;
	private boolean isActive;
	public ClientHandle(Socket client){
		this.client = client;
		InputStream in = null;
		OutputStream out = null;
		try {
			out = client.getOutputStream();
			in = client.getInputStream();
		} catch (IOException e) {
			System.out.println("IO Error!!! - ID:"+client.getInetAddress().toString() + 
					" --- Date:"+  new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		}
		reader = new BufferedReader(new InputStreamReader(in));
		writer = new PrintWriter(new OutputStreamWriter(out));
		isActive = true;
	}
	
	public void run(){
		try {
			System.out.println("Client Connect --- ID:"+client.getInetAddress().toString()+
					" --- Date:"+  new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
           	
			String input;
			while(isActive){
				input = reader.readLine();
				if(input.startsWith("Login")){
					String[] sr = input.split(":");
					if(Server.userList.containsKey(sr[1])){
						writer.println("error");
						writer.flush();
						isActive = false;
					}
					else{
						Server.userList.put(sr[1], writer);
						writer.println("ok");
						writer.flush();
						sendListToAllClients();
					}
				}else if(input.startsWith("Message")){
					String[] sr = input.split("####");
					if("all".equals(sr[2])){
						for(PrintWriter pw:Server.userList.values()){
							if(pw != writer){
								pw.println("Message####"+sr[1]+"####"+sr[2]+"####"+sr[3]);
								pw.flush();
							}		
						}
					}else{
						Server.userList.get(sr[2]).println("Message####"+sr[1]+"####"+sr[2]+"####"+sr[3]);
						Server.userList.get(sr[2]).flush();
					}
				}else if("DISCONNECT".equals(input)){
					isActive = false;
					String key = "";
					for(String k:Server.userList.keySet()){
						if(writer == Server.userList.get(k)){
							key = k;
						}
					}		
					Server.userList.remove(key, writer);
					sendListToAllClients();
					sendDisconnectionEvent(key);
				}
			}			
			reader.close();
			writer.close();
			client.close();
			System.out.println("Client Disconnect --- ID:"+client.getInetAddress().toString()+
					" --- Date:"+  new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		} catch (IOException e) {
			System.out.println("Client input error!!! - ID:"+client.getInetAddress().toString() + 
					" --- Date:"+  new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		}
	}
	
	private void sendListToAllClients(){
		String output = "List:";
		for(String k:Server.userList.keySet()){
			output+=" "+k;
		}
		for(PrintWriter pw:Server.userList.values()){
				pw.println(output);
				pw.flush();
		}
	}
	
	private void sendDisconnectionEvent(String key){
		for(PrintWriter pw:Server.userList.values()){
			pw.println("Disconnect:"+key);
			pw.flush();
		}
	}
}