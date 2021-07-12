package edu.planon.lib.client.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import edu.planon.lib.client.common.component.AjaxIconLink;
import edu.planon.lib.client.common.dto.PnFieldDefDTO;
import edu.planon.lib.client.common.event.IAjaxEventListener;
import edu.planon.lib.client.common.event.IAjaxEventSource;
import edu.planon.lib.client.search.dto.PnSearchFieldDTO;
import edu.planon.lib.client.search.model.PnSearchFilterModel;
import nl.planon.enterprise.service.api.PnESOperator;
import nl.planon.util.pnlogging.PnLogger;

public class PnSearchPanel extends Panel implements IAjaxEventSource {
	private static final long serialVersionUID = 1L;
	private final List<PnSearchFieldDTO> searchFieldList = new ArrayList<PnSearchFieldDTO>();
	private final List<PnSearchPanelRow> searchBarList = new ArrayList<PnSearchPanelRow>();
	private AjaxIconLink addButton;
	private AjaxIconLink removeButton;
	private AjaxIconLink searchButton;
	private boolean eligible = true;
	private final List<PnSearchFilterModel> filterList = new ArrayList<PnSearchFilterModel>();
	
	public PnSearchPanel(String wicketId, List<PnFieldDefDTO> fieldList) {
		super(wicketId);
		this.setSearchFieldList(fieldList);
		this.initcomponents();
		this.setOutputMarkupId(true);
	}
	
	private void initcomponents() {
		SearchPanelRefreshingView searchPanelRepeater = new SearchPanelRefreshingView("repeater", this);
		this.add(searchPanelRepeater);
		
		this.add(this.createAddButton("addButton"));
		this.add(this.createRemoveButton("removeButton"));
		this.add(this.createSearchButton("searchButton"));
	}
	
	private AjaxIconLink createAddButton(String wicketId) {
		this.addButton = new AjaxIconLink(wicketId, "pnicon-plus");
		this.addButton.addEventListener((event, sourceComponent, target) -> {
			int searchPanelSize = this.searchBarList.size();
			if (searchPanelSize != this.searchFieldList.size()) {
				PnSearchPanelRow newSearchPanel = new PnSearchPanelRow("searchBar", searchPanelSize, this.searchFieldList);
				newSearchPanel.setPreviousPanel(this.searchBarList.get(searchPanelSize - 1));
				newSearchPanel.updateAvailableList();
				this.addSearchActionListeners(newSearchPanel);
				this.searchBarList.add(newSearchPanel);
				target.add(this);
			}
		});
		return this.addButton;
	}
	
	private AjaxIconLink createRemoveButton(String wicketId) {
		this.removeButton = new AjaxIconLink(wicketId, "pnicon-times");
		this.removeButton.addEventListener((event, sourceComponent, target) -> {
			if (this.searchBarList.size() != 1) {
				PnSearchPanelRow searchBarPanel = this.searchBarList.remove(this.searchBarList.size() - 1);
				searchBarPanel.getSearchFilter().getSearchField().setSearchBarID(-1);
				searchBarPanel.getPreviousPanel().setNextPanel(null);
				searchBarPanel.setPreviousPanel(null);
				target.add(this);
			}
		});
		return this.removeButton;
	}
	
	private AjaxIconLink createSearchButton(String wicketId) {
		this.searchButton = new AjaxIconLink(wicketId, "pnicon-search");
		this.searchButton.addEventListener((event, sourceComponent, target) -> {
			MarkupContainer parent = sourceComponent.getParent();
			if (parent instanceof PnSearchPanel) {
				for (PnSearchPanelRow searchBar : this.searchBarList) {
					PnSearchFilterModel searchFilter = searchBar.getSearchFilter();
					searchFilter.setActiveSearch(!searchFilter.getSearchValues().isEmpty() || searchFilter.getOperator().equals(PnESOperator.NULL)
							|| searchFilter.getOperator().equals(PnESOperator.NOT_NULL));
				}
			}
			target.add(this);
			
			this.filterList.clear();
			for (PnSearchPanelRow searchBar : this.searchBarList) {
				if (searchBar.isActiveSearch()) {
					this.filterList.add(searchBar.getSearchFilter());
				}
			}
			PnLogger.getLogger(this.getClass()).info("Filter list : " + this.filterList);
		});
		return this.searchButton;
	}
	
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		this.initialSearchBar();
		this.addButton.setEnabled(this.searchBarList.size() != this.searchFieldList.size());
		this.removeButton.setEnabled(this.searchBarList.size() != 1);
		this.setVisible(this.eligible);
	}
	
	public void initialSearchBar() {
		if (this.searchBarList.isEmpty()) {
			PnSearchPanelRow newSearchPanel = new PnSearchPanelRow("searchBar", 0, this.searchFieldList);
			this.searchBarList.add(newSearchPanel);
			this.addSearchActionListeners(newSearchPanel);
		}
	}
	
	private final void setSearchFieldList(List<PnFieldDefDTO> fieldList) {
		if (this.searchFieldList.isEmpty()) {
			if (fieldList == null || fieldList.isEmpty()) {
				this.eligible = false;
				return;
			}
			this.eligible = true;
			boolean isSupported = false;
			for (PnFieldDefDTO field : fieldList) {
				PnSearchFieldDTO searchField = new PnSearchFieldDTO(field);
				if (searchField.isSupported() && searchField.isQueryFilterField()) {
					this.searchFieldList.add(searchField);
				}
				isSupported = isSupported || searchField.isSupported();
			}
			if (!isSupported) {
				this.eligible = false;
			}
			
			//sort them by label
			this.searchFieldList.sort((PnFieldDefDTO f1, PnFieldDefDTO f2) -> f1.getLabel().compareTo(f2.getLabel()));
		}
	}
	
	private void addSearchActionListeners(PnSearchPanelRow newSearchPanel) {
		for (IAjaxEventListener searchListener : this.searchButton.getEventListeners()) {
			newSearchPanel.addEventListener(searchListener);
		}
	}
	
	public List<PnSearchFilterModel> getSearchFilter() {
		return Collections.unmodifiableList(this.filterList);
	}
	
	public List<PnSearchPanelRow> getSearchBarList() {
		return this.searchBarList;
	}
	
	@Override
	public void addEventListener(IAjaxEventListener eventListener) {
		this.searchButton.addEventListener(eventListener);
	}
	
	@Override
	public void addEventListener(List<IAjaxEventListener> eventListeners) {
		this.searchButton.addEventListener(eventListeners);
	}
	
	@Override
	public List<IAjaxEventListener> getEventListeners() {
		return Collections.unmodifiableList(this.searchButton.getEventListeners());
	}
	
	private class SearchPanelRefreshingView extends RefreshingView<PnSearchPanelRow> {
		private static final long serialVersionUID = 1L;
		private final PnSearchPanel searchPanel;
		
		SearchPanelRefreshingView(String wicketId, PnSearchPanel searchPanel) {
			super(wicketId);
			this.searchPanel = searchPanel;
			this.setOutputMarkupId(true);
		}
		
		@Override
		protected Iterator<IModel<PnSearchPanelRow>> getItemModels() {
			this.searchPanel.initialSearchBar();
			
			List<IModel<PnSearchPanelRow>> itemModels = new ArrayList<>();
			for (PnSearchPanelRow searchRow : this.searchPanel.getSearchBarList()) {
				itemModels.add(Model.of(searchRow));
			}
			return itemModels.iterator();
		}
		
		@Override
		protected void populateItem(Item<PnSearchPanelRow> columnItem) {
			columnItem.setOutputMarkupId(true);
			columnItem.addOrReplace(columnItem.getModelObject());
		}
	}
}
