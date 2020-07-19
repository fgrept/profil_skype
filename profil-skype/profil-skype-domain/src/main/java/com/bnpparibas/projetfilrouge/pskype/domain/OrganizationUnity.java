package com.bnpparibas.projetfilrouge.pskype.domain;

import javax.validation.constraints.NotNull;
/**
 * Contient la liste des UO d'appartenance aux collaborateurs avec le site associé.
 * Ces UO représentent une vision logique
 * Les informations de cette classe sont issues d'un référentiel externe et ne sont pas modifiables par la transaction.
 * @author Judicaël
 * @version : V0.1
 *
 */
public class OrganizationUnity {
	private String orgaUnityCode;
	private String orgaUnityType;
	private String orgaShortLabel;
	@NotNull
	private Site orgaSite;
	
	public OrganizationUnity (String code,String type,String label,Site site) {
		this.orgaUnityType=type;
		this.orgaUnityCode=code;
		this.orgaShortLabel=label;
		this.orgaSite=site;
	}

	public String getOrgaUnityCode() {
		return orgaUnityCode;
	}

	public String getOrgaUnityType() {
		return orgaUnityType;
	}

	public String getOrgaShortLabel() {
		return orgaShortLabel;
	}

	public Site getOrgaSite() {
		return orgaSite;
	}
	
}
