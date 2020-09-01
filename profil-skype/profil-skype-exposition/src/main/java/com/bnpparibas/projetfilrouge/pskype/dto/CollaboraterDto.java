
package com.bnpparibas.projetfilrouge.pskype.dto;

import javax.validation.constraints.Size;
import com.bnpparibas.projetfilrouge.pskype.domain.control.EmailControl;
import com.bnpparibas.projetfilrouge.pskype.domain.control.PhoneControl;

/**
 * Classe dédiée à l'ItCollborater
 * Elle contient les critères de recherches 
 * @author Judicaël
 *
 */
public class CollaboraterDto {
	
	@Size(min = 1, max = 17)
	private String collaboraterId;
	@Size(min = 1, max = 50)
	private String lastName;
	@Size(min = 1, max = 50)
	private String firstName;
	@PhoneControl
	private String deskPhoneNumber;
	@PhoneControl
	private String mobilePhoneNumber;
	@EmailControl
	private String mailAdress;
	@Size(min = 1)
	private String orgaUnitCode;
	private String orgaUnityType;
	private String orgaShortLabel;
	@Size(min = 1)
	private String siteCode;
	private String siteName;
	private String siteAddress;
	private String sitePostalCode;
	private String siteCity;
	
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
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getSiteAddress() {
		return siteAddress;
	}
	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}
	public String getSitePostalCode() {
		return sitePostalCode;
	}
	public void setSitePostalCode(String sitePostalCode) {
		this.sitePostalCode = sitePostalCode;
	}
	public String getSiteCity() {
		return siteCity;
	}
	public void setSiteCity(String siteCity) {
		this.siteCity = siteCity;
	}
	public String getOrgaUnitCode() {
		return orgaUnitCode;
	}
	public void setOrgaUnitCode(String orgaUnitCode) {
		this.orgaUnitCode = orgaUnitCode;
	}
	public String getCollaboraterId() {
		return collaboraterId;
	}
	public String getLastName() {
		return lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getDeskPhoneNumber() {
		return deskPhoneNumber;
	}
	public String getMobilePhoneNumber() {
		return mobilePhoneNumber;
	}
	public String getMailAdress() {
		return mailAdress;
	}
	public void setCollaboraterId(String collaboraterId) {
		this.collaboraterId = collaboraterId;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setDeskPhoneNumber(String deskPhoneNumber) {
		this.deskPhoneNumber = deskPhoneNumber;
	}
	public void setMobilePhoneNumber(String mobilePhoneNumber) {
		this.mobilePhoneNumber = mobilePhoneNumber;
	}
	public void setMailAdress(String mailAdress) {
		this.mailAdress = mailAdress;
	}
	
}
