package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import data.Message;

public class Client {
	private int hostport;
	private Socket s;
	private static PrintWriter out;
	private BufferedReader in;
	private ReaderThread readerThread;
	public static String name;
	
	public Client(int hostport){
		this.hostport = hostport;
		connect();
	}
	
	public void connect(){
		try {
			s = new Socket(InetAddress.getLocalHost(),hostport);
			out = new PrintWriter(s.getOutputStream());
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			readerThread = new ReaderThread(in);
		} catch (IOException e) {
			System.out.println("Client connection error!");
		}
	}
	
	public ReaderThread getReader(){
		return readerThread;
	}
	
	public String loginToServer(String name) throws IOException{
		out.println("Login:" + name);
		out.flush();
		String response =  in.readLine();
		if("ok".equals(response)){
			Client.name = name;
			return "ok";
		}
		else{
			return "error";
		}
	}
	
	public static void sendMessage(Message m){
		out.println("Message####"+m.getContent()+"####"+m.getTo()+"####"+Client.name);
		out.flush();
	}
		
	public void disconnect(){
		try {
			out.println("DISCONNECT");
			out.flush();
			s.close();
		}catch (IOException e) {
			System.out.println("Disconnection error!!!");
		}
	}	
}
