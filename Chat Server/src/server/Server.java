package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Server{
	private static ServerSocket ss;
	private static boolean isRunning;
	public  static HashMap<String,PrintWriter> userList = new HashMap<String,PrintWriter>();
	
	public static void main(String[] args){
		isRunning = true; 
		try {
			ss = new ServerSocket(2222);
			System.out.println("Server started! --- Date:"+  new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
			while(isRunning){
				new ClientHandle(ss.accept()).start();
			}
		} catch (IOException e) {
			System.out.println("Server error! --- Date:"+  new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		}
	}

	public static void stopServer(){
		isRunning = false;
	}
}
