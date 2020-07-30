package com.bnpparibas.projetfilrouge.pskype.domain;

import java.util.List;


/**
 * Interface SkypeProfil pour la couche Domain
 * @author La Fabrique
 * 
 */
public interface ISkypeProfileDomain {

		boolean create(SkypeProfile SkypeProfile);
		
		boolean update(SkypeProfile SkypeProfile);
		
		boolean delete(String sip);
		
		SkypeProfile consultSkypeProfile(String sip, StatusSkypeProfileEnum status);
		
		SkypeProfile findSkypeProfileBySip(String sip);
		
		SkypeProfile findSkypeProfileByIdCollab(String idAnnuaire);
		
		SkypeProfile findSkypeProfileByCollaborater(Collaborater collaborater);
		
		List<SkypeProfile> findAllSkypeProfile();
		
		List<SkypeProfile> findAllSkypeProfileFilters(Boolean enterpriseVoiceEnabled, String voicePolicy,
				String dialPlan, String samAccountName, Boolean exUmEnabled, String exchUser,
				StatusSkypeProfileEnum statusProfile, String orgaUnityCode, String siteCode);

		Long countSkypeProfile();

		List<SkypeProfile> findAllSkypeProfilePage(int numberPage, int sizePage, String criteria,
				boolean sortAscending);

		List<SkypeProfile> findAllSkypeProfileFiltersPage(SkypeProfile profilDom, int numberPage, int sizePage,
				String sortCriteria, boolean sortAscending);

	
}
