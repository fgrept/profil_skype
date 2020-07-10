package com.bnpparibas.projetfilrouge.pskype.domain;

public interface ISkypeProfilManagemementFG {
	
	// pour une V2 ... multi-critères dynamiques
	public SkypeProfileWithAffectation searchByCriteria (String ... criteria);
	
	// pour une V1 : ci-dessous recherches mono-critères
	public SkypeProfileWithAffectation searchByUoName (String name);
	public SkypeProfileWithAffectation serachBySiteName (String name);
	
}
