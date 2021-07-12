package edu.planon.lib.client.recordlist.paging;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigation;

import nl.planon.util.pnlogging.PnLogger;

public class PnAjaxPagingNavigator extends AjaxPagingNavigator {
	private static final long serialVersionUID = 1L;
	private static final PnLogger LOGGER = PnLogger.getLogger(PnAjaxPagingNavigator.class);
	
	public PnAjaxPagingNavigator(String wicketId, IPageable pageable) {
		super(wicketId, pageable);
		this.setOutputMarkupId(true);
		this.setRenderBodyOnly(false);
		//LOGGER.setLevel(Level.DEBUG);
	}
	
	@Override
	public boolean isVisible() {
		return (this.getPageable().getPageCount() > 1L);
	}
	
	public void setCurrentPage(int pageIndex, boolean forceUpdate, AjaxRequestTarget target) {
		LOGGER.debug("setCurrentPage (pageIndex: " + pageIndex + "; forceUpdate: " + forceUpdate + ")");
		if (forceUpdate || this.getPageable().getCurrentPage() != pageIndex && pageIndex < this.getPageable().getPageCount()) {
			this.getPageable().setCurrentPage(pageIndex);
			
			if (this.findParent(Page.class) != null) {
				target.add(this);
			}
		}
	}
	
	@Override
	protected PagingNavigation newNavigation(final String wicketId, final IPageable pageable, final IPagingLabelProvider labelProvider) {
		return new PnAjaxPagingNavigation(wicketId, pageable, labelProvider);
	}
	
	@Override
	protected AbstractLink newPagingNavigationIncrementLink(String wicketId, IPageable pageable, int increment) {
		return new PnAjaxPagingNavigationIncrementLink(wicketId, pageable, increment);
	}
	
	@Override
	protected AbstractLink newPagingNavigationLink(String wicketId, IPageable pageable, int pageNumber) {
		return new PnAjaxPagingNavigationLink(wicketId, pageable, pageNumber);
	}
}
