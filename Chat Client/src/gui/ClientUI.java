package gui;

import javax.swing.JFrame;

import client.Client;
import events.DisconnectionEvent;
import events.DisconnectionListener;
import events.GetMessageEvent;
import events.GetMessageListener;
import events.ListChangeEvent;
import events.ListChangeListener;
import javax.swing.JTabbedPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;

public class ClientUI extends JFrame implements ListChangeListener, GetMessageListener, DisconnectionListener{
	private static final long serialVersionUID = 1L;
	private Client client;
	private HashMap<String,ChatTab> chatTabs;
	private JTabbedPane messageTabs;
	private DefaultListModel<String> listModel;
	private JList<String> userListList;
	public ClientUI(){
		String name = JOptionPane.showInputDialog("Please enter your username:");
		if(name == null || "".equals(name)){
			JOptionPane.showMessageDialog(null, "Enter an userName to login to the ChatServer.");
			System.exit(0);
		}	
		else{
			client = new Client(2222);		
			try {
				if("error".equals(client.loginToServer(name))){
					JOptionPane.showMessageDialog(null, "This username already exist. Exiting...");
					System.exit(0);
				}
			} catch (IOException e) {
					System.out.println("Client login error!");
			}
		}
		Client.name = name;
		setTitle("Chat: "+name);
		
		getContentPane().setLayout(null);
		messageTabs = new JTabbedPane(JTabbedPane.TOP);
		messageTabs.setBounds(10, 11, 315, 323);
		getContentPane().add(messageTabs);
		chatTabs = new HashMap<String,ChatTab>();
		chatTabs.put("all", new ChatTab("all"));
		
		messageTabs.addTab("all", null, chatTabs.get("all"), null);
		
		listModel = new DefaultListModel<String>();
		userListList = new JList<String>(listModel);
		userListList.setBounds(351, 36, 113, 264);
		
		userListList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane listScroller = new JScrollPane(userListList);
		listScroller.setBounds(351, 36, 113, 264);
		getContentPane().add(listScroller);	
		JLabel lblAvailableUsers = new JLabel("Available users:");
		lblAvailableUsers.setBounds(351, 11, 113, 14);
		getContentPane().add(lblAvailableUsers);
		
		JButton start = new JButton("Start chat");
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String s = userListList.getSelectedValue();
				if(s != null && !s.isEmpty() ){
					if(!chatTabs.containsKey(s)){
						chatTabs.put(s, new ChatTab(s));
						messageTabs.addTab(s, null, chatTabs.get(s), null);
					}
					messageTabs.setSelectedComponent(chatTabs.get(s));
				}
			}
		});
		start.setBounds(356, 311, 108, 23);
		getContentPane().add(start);
		
		JButton btnprivate = new JButton("->Private");
		btnprivate.setBounds(366, 337, 89, 23);
		getContentPane().add(btnprivate);
		btnprivate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String s = userListList.getSelectedValue();
				if(s != null && !s.isEmpty() ){
					String message = JOptionPane.showInputDialog("Please enter your message(not sent to"+s+"):");
					
				}
				else JOptionPane.showMessageDialog(null, "Please select a user.");
				
			}
		});
		
		client.getReader().addListChangeListener(this);
		client.getReader().addGetMessageListener(this);
		client.getReader().addDisconnectionListener(this);
		client.getReader().start();
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(100,100,500,445);
		setResizable(false);
		setVisible(true);
		addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent arg0) {}
			@Override
			public void windowIconified(WindowEvent arg0) {}
			@Override
			public void windowDeiconified(WindowEvent arg0) {}
			@Override
			public void windowDeactivated(WindowEvent arg0) {}
			@Override
			public void windowClosing(WindowEvent arg0) {
				client.getReader().deactivateReader();
				client.disconnect();
			}
			@Override
			public void windowActivated(WindowEvent arg0) {}
			@Override
			public void windowClosed(WindowEvent arg0) {}
		});
	}
	
	@Override
	public void listChanged(ListChangeEvent e) {
		listModel.clear();
		for(String s:e.getUserList()){
			if(!s.equals(Client.name))
				listModel.addElement(s);
		}
	}

	@Override
	public void getMessage(GetMessageEvent e) {
		String from = e.getMessage().getFrom();
		if(e.getMessage().getTo().equals("all")){
			messageTabs.setSelectedComponent(chatTabs.get("all"));
			chatTabs.get("all").putMessage(e.getMessage(),"left");
		}
		else if(!chatTabs.containsKey(from)){
			chatTabs.put(from, new ChatTab(from));
			messageTabs.addTab(from, null, chatTabs.get(from), null);
			chatTabs.get(from).putMessage(e.getMessage(),"left");
		}else{
				messageTabs.setSelectedComponent(chatTabs.get(from));
				chatTabs.get(from).putMessage(e.getMessage(),"left");
		}		
	}

	@Override
	public void disconnect(DisconnectionEvent e) {
		String user = e.getUser();
		messageTabs.remove(chatTabs.get(user));
		chatTabs.remove(user);
	}	
}