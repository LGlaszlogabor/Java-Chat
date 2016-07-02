package events;

import java.util.EventListener;

public interface ListChangeListener extends EventListener {
	public void listChanged(ListChangeEvent e);
}