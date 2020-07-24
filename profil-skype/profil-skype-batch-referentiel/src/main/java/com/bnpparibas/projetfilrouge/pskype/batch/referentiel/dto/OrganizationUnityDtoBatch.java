package com.bnpparibas.projetfilrouge.pskype.batch.referentiel.dto;

public class OrganizationUnityDtoBatch {
	
	private String orgaUnityCode;
	private String orgaUnityType;
	private String orgaShortLabel;
	private String siteCode;
	
	public OrganizationUnityDtoBatch() {
	}
		
	public OrganizationUnityDtoBatch (String code,String type,String label,String siteCode) {
		this.orgaUnityType=type;
		this.orgaUnityCode=code;
		this.orgaShortLabel=label;
		this.siteCode=siteCode;
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

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

}
