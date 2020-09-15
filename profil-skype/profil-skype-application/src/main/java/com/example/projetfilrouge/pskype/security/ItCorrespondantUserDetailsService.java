package com.example.projetfilrouge.pskype.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.example.projetfilrouge.pskype.domain.user.IItCorrespondantDomain;
import com.example.projetfilrouge.pskype.domain.user.ItCorrespondant;
import com.example.projetfilrouge.pskype.domain.user.RoleTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implémentation de l'interface de Spring security
 * @author Judicaël
 *
 */
@Service
@Transactional
public class ItCorrespondantUserDetailsService implements UserDetailsService {
	
	private static Logger logger = LoggerFactory.getLogger(ItCorrespondantUserDetailsService.class);
	
	@Autowired
	IItCorrespondantDomain itCorrespondantDomain;
	/**
	 * Méthode permettant de créer un UserDetails à partir de l'id annuaire.
	 * @param idAnnuaire issu de la couche expositon. Le nom de la variable doit être identique avec celui reçu en entrée de l'API.
	 */
	@Override
	public UserDetails loadUserByUsername(String idAnnuaire) throws UsernameNotFoundException {
		
		ItCorrespondant itCorrespondant = itCorrespondantDomain.findItCorrespondantByCollaboraterId(idAnnuaire);
		if (itCorrespondant ==null) {
			throw new UsernameNotFoundException("Utilisateur, id annuaire : "+idAnnuaire+" non trouvé");
		}
		String sLogDebug = itCorrespondant.getCollaboraterId()+" , password : "+itCorrespondant.getPassword();
		logger.debug(sLogDebug);
		
		return new User(idAnnuaire, itCorrespondant.getPassword(), getRolesAuthorities(itCorrespondant));
	}
	
	
	/**
	 * Méthode pour mapper le set de rôles avec la liste de type GrantedAuthority
	 * @param itCorrespondant
	 * @return List de type GrantedAuthoriy
	 */
	private Collection<? extends GrantedAuthority> getRolesAuthorities(ItCorrespondant itCorrespondant)throws UsernameNotFoundException {
		
		List<GrantedAuthority> rolesAuthorities = new ArrayList<GrantedAuthority>();
		if (rolesAuthorities !=null) {
			for (RoleTypeEnum role : itCorrespondant.getRoles()) {
				rolesAuthorities.add(new SimpleGrantedAuthority(role.name()));
				if (logger.isDebugEnabled()){
					String sLogDebug = itCorrespondant.getCollaboraterId()+" : ROLE - "+role.name();
					logger.debug(sLogDebug);
				}
			}
		}
		return rolesAuthorities;
	}


}
