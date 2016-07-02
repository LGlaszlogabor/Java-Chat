package events;

import java.util.EventListener;

public interface DisconnectionListener extends EventListener {
	public void disconnect(DisconnectionEvent dce);
}
