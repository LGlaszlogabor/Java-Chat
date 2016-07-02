package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import client.Client;
import data.Message;

public class ChatTab extends JPanel{
	private static final long serialVersionUID = 1L;
	private JTextPane chatPane;
	private JTextField chatArea;
	private String text = "<html><body><div style=\"overflow-y: auto; height:300\">";
	private String name;
	private JButton sendButton;
	
	ChatTab(String name){
		super();
		this.name = name;
		setLayout(null);
		chatPane = new JTextPane();
		chatPane.setBounds(10, 11, 279, 207);
		chatPane.setEditable(false);
		chatPane.setContentType("text/html");
		JScrollPane scroll = new JScrollPane (chatPane,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBounds(10, 11, 279, 207);
		add(scroll);	
		chatArea = new JTextField();
		chatArea.setBounds(10, 229, 204, 55);
		add(chatArea);		
		sendButton = new JButton("Send");
		sendButton.setBounds(224, 229, 65, 55);
		sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!chatArea.getText().isEmpty()){			
					Message m = new Message(chatArea.getText(),name,Client.name);
					Client.sendMessage(m);
					putMessage(m,"right");
					chatArea.setText("");
				}
			}
		});		
		add(sendButton);		
	}
	
	public String getNaame(){
		return name;
	}

	public void putMessage(Message m,String orientation){
		String color = "red";
		if("right".equals(orientation))
			color = "green";
		else color = "blue";
		text += "<p align=\""+orientation+"\"><font color=\""+color+"\">"+m.getFrom()+"</font>:"+m.getContent()+"</p>";
		chatPane.setText(text+"</div></body></html>");
		chatPane.setCaretPosition(chatPane.getDocument().getLength());
	}
}
