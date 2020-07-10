package com.bnpparibas.projetfilrouge.pskype.infrastructure.user;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.skypeprofile.SkypeProfileEntity;

/**
 * 
 * @author 116453
 *
 *	Cette classe exécute une requête JPA sous le contrôle de l'entity manager
 *  de SpringBoot. Elle attend en entrée une List avec dans chacun une partie
 *  de la clause where en notation JPA.
 *  Exemple :
 *	"p.collaborater.orgaUnit.orgaShortLabel = :<<value>>"
 */
@Component
public class SkypeProfilRepositoryJDBCImpl implements ISkypeProfilRepositoryFG2 {
	
	@PersistenceContext
    private EntityManager em;

	@Override
	public List<SkypeProfileEntity> searchProfileByCriteria(List<String> clauseWhereElement) {
		
		String reqJPA = "select p from SkypeProfileEntity p where ";
		
		for (int i = 0; i < clauseWhereElement.size(); i++) {
			reqJPA += clauseWhereElement;
			if (i < clauseWhereElement.size()) reqJPA += " and ";
		}
		
		Query query = em.createQuery(reqJPA);
		return query.getResultList();
		
	}


}
