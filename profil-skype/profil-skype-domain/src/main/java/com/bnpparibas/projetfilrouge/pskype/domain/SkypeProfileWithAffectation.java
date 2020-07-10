package com.bnpparibas.projetfilrouge.pskype.domain;

/**
 * @author 116543
 * 
 * Cette classe permet de manipuler un ProfilSkype avec les informations attachées au collaborateur
 * et à son affectation.
 * Elle est notammment utile pour les recherche de profils qui peuvent se faire
 * sur des critères divers et qui ramènent des informations de ces différentes entités 
 *
 */

public class SkypeProfileWithAffectation {
	
	private SkypeProfile skypeProfil;
	private CollaboraterWithAffectation skypeProfilAffected;
	
	public SkypeProfile getSkypeProfil() {
		return skypeProfil;
	}
	public void setSkypeProfil(SkypeProfile skypeProfil) {
		this.skypeProfil = skypeProfil;
	}
	public CollaboraterWithAffectation getSkypeProfilAffected() {
		return skypeProfilAffected;
	}
	public void setSkypeProfilAffected(CollaboraterWithAffectation skypeProfilAffected) {
		this.skypeProfilAffected = skypeProfilAffected;
	}
	
	
	
}
