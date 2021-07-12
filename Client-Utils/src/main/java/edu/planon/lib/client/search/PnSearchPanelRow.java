package edu.planon.lib.client.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import edu.planon.lib.client.common.behavior.PnCSSAttributeAppender;
import edu.planon.lib.client.common.component.AjaxIconLink;
import edu.planon.lib.client.common.event.IAjaxEventListener;
import edu.planon.lib.client.common.event.IAjaxEventSource;
import edu.planon.lib.client.common.event.PnAjaxEventCollector;
import edu.planon.lib.client.common.util.PnOperatorUtils;
import edu.planon.lib.client.search.dto.PnSearchFieldDTO;
import edu.planon.lib.client.search.dto.PnSearchOperatorDTO;
import edu.planon.lib.client.search.editor.IPnSearchEditor;
import edu.planon.lib.client.search.editor.PnMultiValueEditor;
import edu.planon.lib.client.search.editor.PnSingleValueEditor;
import edu.planon.lib.client.search.model.PnSearchFilterModel;
import nl.planon.enterprise.service.api.PnESOperator;

public class PnSearchPanelRow extends Panel implements IAjaxEventSource {
	private static final long serialVersionUID = 1L;
	private final int searchID;
	private PnSearchFilterModel searchFilter = new PnSearchFilterModel();
	private DropDownChoice<PnSearchOperatorDTO> operatorChoice;
	private final List<PnSearchFieldDTO> fieldList;
	private final List<PnSearchFieldDTO> availableFieldList = new ArrayList<PnSearchFieldDTO>();
	private IPnSearchEditor searchEditor;
	private AjaxIconLink clearButton;
	private PnSearchPanelRow previousPanel;
	private PnSearchPanelRow nextPanel;
	private final PnAjaxEventCollector eventCollector = new PnAjaxEventCollector();
	
	public PnSearchPanelRow(String wicketId, int aSearchID, List<PnSearchFieldDTO> fieldList) {
		super(wicketId);
		this.searchID = aSearchID;
		this.fieldList = fieldList;
		this.setOutputMarkupId(true);
		this.setDefaultModel(new CompoundPropertyModel<PnSearchFilterModel>(this.searchFilter));
		this.initComponents();
	}
	
	private void initComponents() {
		DropDownChoice<PnSearchFieldDTO> searchFieldChoice = new DropDownChoice<PnSearchFieldDTO>("searchField", this.availableFieldList);
		searchFieldChoice.setChoiceRenderer(new ChoiceRenderer<PnSearchFieldDTO>("label"));
		searchFieldChoice.add(AjaxFormComponentUpdatingBehavior.onUpdate("change", target -> this.onSearchFieldChoiceUpdate(target)));
		this.add(searchFieldChoice);
		
		this.operatorChoice = new DropDownChoice<PnSearchOperatorDTO>("searchOperator");
		this.operatorChoice.setChoiceRenderer(new ChoiceRenderer<PnSearchOperatorDTO>("label"));
		this.operatorChoice.add(AjaxFormComponentUpdatingBehavior.onUpdate("change", target -> this.onOperatorChoiceUpdate(target)));
		this.add(this.operatorChoice);
		
		this.clearButton = new AjaxIconLink("clearButton", "pn-clear-icon");
		this.clearButton.addEventListener((event, sourceComponent, target) -> {
			this.searchFilter.setActiveSearch(false);
			this.searchFilter.clearSearchValues();
			target.add(this);
		});
		this.clearButton.addEventListener(this.eventCollector);
		this.add(this.clearButton);
		
		this.add(new PnCSSAttributeAppender("activeSearch") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isEnabled(Component aComponent) {
				if (aComponent instanceof PnSearchPanelRow) {
					return ((PnSearchPanelRow)aComponent).isActiveSearch();
				}
				return false;
			}
		});
		this.add(new PnCSSAttributeAppender("noSearch") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isEnabled(Component aComponent) {
				if (aComponent instanceof PnSearchPanelRow) {
					return !((PnSearchPanelRow)aComponent).isActiveSearch();
				}
				return false;
			}
		});
	}
	
	private void onSearchFieldChoiceUpdate(AjaxRequestTarget target) {
		//clear link on previous fieldDTO
		for (PnSearchFieldDTO fieldDTO : this.fieldList) {
			if (fieldDTO.getSearchBarID() == this.searchID) {
				fieldDTO.setSearchBarID(-1);
				break;
			}
		}
		
		PnSearchFieldDTO selectedField = this.searchFilter.getSearchField();
		selectedField.setSearchBarID(this.searchID);
		this.updateOperatorBasedOnSelectedField();
		
		this.searchFilter.clearSearchValues();
		this.searchFilter.setActiveSearch(false);
		target.add(this);
		
		this.refreshPrevPanel(target, this.getPreviousPanel());
		this.refreshNextPanel(target, this.getNextPanel());
	}
	
	private void onOperatorChoiceUpdate(AjaxRequestTarget target) {
		this.updateEditorBasedOnOperator();
		this.searchFilter.clearSearchValues();
		this.searchFilter.setActiveSearch(false);
		target.add(this);
	}
	
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		this.updateAvailableList();
	}
	
	public void updateAvailableList() {
		this.availableFieldList.clear();
		for (PnSearchFieldDTO fieldDTO : this.fieldList) {
			if (!fieldDTO.isAvailable(this.searchID)) {
				continue;
			}
			this.availableFieldList.add(fieldDTO);
		}
		
		if (this.searchFilter.getSearchField() == null && !this.availableFieldList.isEmpty()) {
			PnSearchFieldDTO searchField = this.availableFieldList.get(0);
			searchField.setSearchBarID(this.searchID);
			this.searchFilter.setSearchField(searchField);
			this.updateOperatorBasedOnSelectedField();
		}
	}
	
	public void updateOperatorBasedOnSelectedField() {
		List<PnSearchOperatorDTO> operatorList = PnOperatorUtils.getOperatorsForType(this.searchFilter.getSearchField().getFieldType());
		this.operatorChoice.setChoices(operatorList);
		this.searchFilter.setSearchOperator(this.getOperatorByPreference(operatorList));
		this.updateEditorBasedOnOperator();
	}
	
	private PnSearchOperatorDTO getOperatorByPreference(List<PnSearchOperatorDTO> operatorList) {
		HashMap<PnESOperator, PnSearchOperatorDTO> tempMap = new HashMap<PnESOperator, PnSearchOperatorDTO>();
		for (PnSearchOperatorDTO operatorDTO : operatorList) {
			tempMap.put(operatorDTO.getOperator(), operatorDTO);
		}
		PnSearchOperatorDTO operator = tempMap.get(PnESOperator.CONTAINS);
		if (operator != null) {
			return operator;
		}
		operator = tempMap.get(PnESOperator.EQUAL);
		if (operator != null) {
			return operator;
		}
		return operatorList.get(0);
	}
	
	public void updateEditorBasedOnOperator() {
		if (this.searchFilter.getOperator().equals(PnESOperator.BETWEEN)) {
			this.searchEditor = new PnMultiValueEditor("searchInputEditor", this.searchFilter);
		}
		else {
			this.searchEditor = new PnSingleValueEditor("searchInputEditor", this.searchFilter);
		}
		
		this.searchEditor.addEventListener((event, sourceComponent, target) -> {
			if ((event.equals("blur") || event.equals("change")) && !this.isActiveSearch()) {
				this.searchFilter.setActiveSearch(false);
				target.add(this);
			}
		});
		this.searchEditor.addEventListener(this.eventCollector);
		this.addOrReplace((Component)this.searchEditor);
	}
	
	public PnSearchFilterModel getSearchFilter() {
		return this.searchFilter;
	}
	
	public PnSearchPanelRow getPreviousPanel() {
		return this.previousPanel;
	}
	
	public void setPreviousPanel(PnSearchPanelRow previousPanel) {
		this.previousPanel = previousPanel;
		if (previousPanel != null) {
			previousPanel.setNextPanel(this);
		}
	}
	
	public PnSearchPanelRow getNextPanel() {
		return this.nextPanel;
	}
	
	public void setNextPanel(PnSearchPanelRow aNextPanel) {
		this.nextPanel = aNextPanel;
	}
	
	public void refreshNextPanel(AjaxRequestTarget target, PnSearchPanelRow nextPanel) {
		if (nextPanel != null) {
			target.add(nextPanel);
			this.refreshNextPanel(target, nextPanel.getNextPanel());
		}
	}
	
	public void refreshPrevPanel(AjaxRequestTarget target, PnSearchPanelRow prevPanel) {
		if (prevPanel != null) {
			target.add(prevPanel);
			this.refreshPrevPanel(target, prevPanel.getPreviousPanel());
		}
	}
	
	public boolean isActiveSearch() {
		return this.searchFilter.isActiveSearch();
	}
	
	public IPnSearchEditor getSearchEditor() {
		return this.searchEditor;
	}
	
	@Override
	public void addEventListener(IAjaxEventListener eventListener) {
		this.eventCollector.addEventListener(eventListener);
	}
	
	@Override
	public void addEventListener(List<IAjaxEventListener> eventListeners) {
		this.eventCollector.addEventListener(eventListeners);
	}
	
	@Override
	public List<IAjaxEventListener> getEventListeners() {
		return this.eventCollector.getEventListeners();
	}
}
