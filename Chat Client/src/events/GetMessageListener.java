package events;

import java.util.EventListener;

public interface GetMessageListener extends EventListener {
	public void getMessage(GetMessageEvent e);
}
