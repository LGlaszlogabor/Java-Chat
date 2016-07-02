package data;

public class Message {
	private String content;
	private String to;
	private String from;
	public Message(String content, String to, String from){
		this.content = content;
		this.to = to;
		this.from = from;
	}
	
	public void setContent(String content){
		this.content = content;
	}
	
	public void setTo(String to){
		this.to = to;
	}
	
	public void setFrom(String from){
		this.from = from;
	}
	
	public String getContent(){
		return content;
	}
	
	public String getTo(){
		return to;
	}
	
	public String getFrom(){
		return from;
	}
}
