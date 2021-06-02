package edu.planon.lib.client.panel;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import edu.planon.lib.client.action.CloseModalBehavior;
import edu.planon.lib.client.exception.PnClientException;
import edu.planon.lib.client.exception.PnUserException;
import edu.planon.lib.esapi.ESErrorUtil;

public class ErrorPopup extends Panel {
	private static final long serialVersionUID = 1L;
	private final List<String> messageList = new ArrayList<String>();

	public ErrorPopup(String id, Throwable throwable) {
		super(id);
		this.getErrorMessage(throwable);
		this.initComponents();
	}
	
	public ErrorPopup(String id, String message) {
		super(id);
		this.messageList.add(message);
		this.initComponents();
	}
	
	public ErrorPopup(String id, List<String> messages) {
		super(id);
		this.messageList.addAll(messages);
		this.initComponents();
	}
	
	private void initComponents() {
		this.addOrReplace(new Label("header", Model.of("Error")));
		
		ListView<String> messageListView = new ListView<String>("messages", this.messageList) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(ListItem<String> item) {
				String message = item.getModelObject();
				item.add(new Label("message", Model.of(message)));
			}
		};
		this.add(messageListView);
		
		
		Button btnClose = new Button("btnClose", Model.of("Close"));
		btnClose.add(new CloseModalBehavior("click"));
		this.add(btnClose);
	}
	
	private void getErrorMessage(Throwable throwable) {
		if((throwable instanceof PnClientException || throwable instanceof PnUserException) && throwable.getCause() == null) {
			this.messageList.add(throwable.getMessage());
		}
		else {
			this.messageList.addAll(ESErrorUtil.getErrorMessage(throwable));
		}
	}
}
