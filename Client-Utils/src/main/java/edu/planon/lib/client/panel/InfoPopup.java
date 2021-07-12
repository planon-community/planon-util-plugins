package edu.planon.lib.client.panel;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import edu.planon.lib.client.common.behavior.CloseModalBehavior;
import edu.planon.lib.client.common.event.IAjaxEventListener;

public class InfoPopup extends Panel {
	private static final long serialVersionUID = 1L;
	private CloseModalBehavior closeEventBehavior;
	private final List<String> messageList = new ArrayList<String>();
	
	public InfoPopup(String id, String message) {
		this(id, "Close", message);
	}
	
	public InfoPopup(String id, String closeLabel, String message) {
		super(id);
		this.messageList.add(message);
		this.initComponents(closeLabel);
	}
	
	public InfoPopup(String id, List<String> messages) {
		this(id, "Close", messages);
	}
	
	public InfoPopup(String id, String closeLabel, List<String> messages) {
		super(id);
		this.messageList.addAll(messages);
		this.initComponents(closeLabel);
	}
	
	private void initComponents(String closeLabel) {
		ListView<String> messageListView = new ListView<String>("messages", this.messageList) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(ListItem<String> item) {
				String message = item.getModelObject();
				item.add(new Label("content", Model.of(message)));
			}
		};
		this.add(messageListView);
		
		Button btnClose = new Button("btnClose", Model.of(closeLabel));
		this.closeEventBehavior = new CloseModalBehavior("click");
		btnClose.add(this.closeEventBehavior);
		this.add(btnClose);
	}
	
	public List<String> getMessageList() {
		return this.messageList;
	}
	
	public void addCloseListener(IAjaxEventListener eventListener) {
		this.closeEventBehavior.addEventListener(eventListener);
	}
	
	public void addCloseListener(List<IAjaxEventListener> eventListeners) {
		this.closeEventBehavior.addEventListener(eventListeners);
	}
}
