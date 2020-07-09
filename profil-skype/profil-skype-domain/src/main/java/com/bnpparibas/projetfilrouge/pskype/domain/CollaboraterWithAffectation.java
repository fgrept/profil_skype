package com.bnpparibas.projetfilrouge.pskype.domain;

/**
 * 
 * @author 116453
 * cette classe est un collaborateur avec ses information d'affectation associée
 * elle permet de le manipuler dans les méthodes métiers qui en ont besoin
 * 
 */
public class CollaboraterWithAffectation {
	
	private Collaborater collab;
	private OrganizationUnity collabUO;
	private Site collabSite;
	
	public Collaborater getCollab() {
		return collab;
	}
	public void setCollab(Collaborater collab) {
		this.collab = collab;
	}
	public OrganizationUnity getCollabUO() {
		return collabUO;
	}
	public void setCollabUO(OrganizationUnity collabUO) {
		this.collabUO = collabUO;
	}
	public Site getCollabSite() {
		return collabSite;
	}
	public void setCollabSite(Site collabSite) {
		this.collabSite = collabSite;
	}
	
	
	
}
