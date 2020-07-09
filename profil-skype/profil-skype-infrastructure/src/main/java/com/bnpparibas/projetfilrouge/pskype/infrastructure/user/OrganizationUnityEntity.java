package com.bnpparibas.projetfilrouge.pskype.infrastructure.user;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * 
 * @author 116453
 * assure la persistence de la classe UO
 * plusieurs UO sont  rattachées à un site
 * fetch en eager car les données sont toujours affichées avec les infos adresse du site
 */

public class OrganizationUnityEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orgaUnityId;
	private String orgaUnityCode;
	private String orgaUnityType;
	private String orgaShortLabel;
	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	private SiteEntity orgaSite;
	
	
	public Long getOrgaUnityId() {
		return orgaUnityId;
	}
	public void setOrgaUnityId(Long orgaUnityId) {
		this.orgaUnityId = orgaUnityId;
	}
	public String getOrgaUnityCode() {
		return orgaUnityCode;
	}
	public void setOrgaUnityCode(String orgaUnityCode) {
		this.orgaUnityCode = orgaUnityCode;
	}
	public String getOrgaUnityType() {
		return orgaUnityType;
	}
	public void setOrgaUnityType(String orgaUnityType) {
		this.orgaUnityType = orgaUnityType;
	}
	public String getOrgaShortLabel() {
		return orgaShortLabel;
	}
	public void setOrgaShortLabel(String orgaShortLabel) {
		this.orgaShortLabel = orgaShortLabel;
	}
	public SiteEntity getOrgaSite() {
		return orgaSite;
	}
	public void setOrgaSite(SiteEntity orgaSite) {
		this.orgaSite = orgaSite;
	}

}
