package br.ucs.shopping.views.beans;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import br.ucs.shopping.ejb.intf.CrudServiceIntf;
import br.ucs.shopping.models.PaginatedRecords;

public abstract class AbstractBean<T> {
	final int PAGINATION = 12;
	private T entity;
	private List<T> entities;
	private int currentPage = 1;
	private boolean nextPage;
	private boolean prevPage;
	private int entitiesCount;
	private int offset;

	/**
	 * @return
	 */
	protected abstract CrudServiceIntf<T> getcrudService();

	/**
	 * @return
	 */
	public List<T> getEntities() {
		return entities;
	}

	/**
	 * @return
	 */
	public T getEntity() {
		return entity;
	}

	/**
	 * 
	 */
	public void loadEntities() {
		loadEntities(1);
	}

	/**
	 * @param page
	 */
	public void loadEntities(int page) {
		this.currentPage = page;

		PaginatedRecords<T> pagination = getcrudService().list(currentPage, PAGINATION);

		this.entities = pagination.getCollection();
		this.nextPage = pagination.hasNextPage();
		this.prevPage = pagination.hasPrevPage();
		this.entitiesCount = pagination.getTotal();
		this.offset = pagination.offset() + this.entities.size();
	}
	
	/**
	 * 
	 */
	public void nextPage() {
		this.loadEntities(this.currentPage + 1);
	}
	
	/**
	 * 
	 */
	public void prevPage() {
		this.loadEntities(this.currentPage - 1);
	}

	/**
	 * 
	 */
	public void build() {
		this.entity = getcrudService().build();
	}

	/**
	 * @param entity
	 */
	public void edit(T entity) {
		this.entity = entity;
	}

	/**
	 * @param entity
	 */
	public void remove(T entity) {
		this.entity = entity;
		getcrudService().destroy(this.entity);

		setFlashInfo("Registro excluido com sucesso");
		loadEntities();
	}

	/**
	 * @return
	 */
	public boolean getNextPage() {
		return nextPage;
	}

	/**
	 * @return
	 */
	public boolean getPrevPage() {
		return prevPage;
	}
	
	/**
	 * @return
	 */
	public int getOffset() {
		return offset;
	}
	
	/**
	 * @return
	 */
	public int getEntitiesCount() {
		return entitiesCount;
	}

	/**
	 * 
	 */
	public String save() {
		getcrudService().save(entity);

		setFlashInfo("Registro salvo com sucesso!");
		loadEntities();

		return "index";
	}

	/**
	 * @return
	 */
	protected FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	/**
	 * @return
	 */
	protected ExternalContext getExternalContext() {
		return getFacesContext().getExternalContext();
	}

	/**
	 * @param message
	 */
	protected void setFlashInfo(String message) {
		getFacesContext().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, message, message));
	}
}