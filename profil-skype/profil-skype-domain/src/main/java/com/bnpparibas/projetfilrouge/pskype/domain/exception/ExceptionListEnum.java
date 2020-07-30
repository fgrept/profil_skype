package com.bnpparibas.projetfilrouge.pskype.domain.exception;
/**
 * Cette classe recense les différents type d'exception métier qui peuvent être remontés à l'utilisateur
 * 
 * @author La fabrique
 *
 */
public enum ExceptionListEnum {
	
	// il s'agit ci-dessous d'exceptions levées par les packages Profil Skype
	ALLREADY1("AllReadyExistException", "Le collaborateur a déjà un profil skype"),
	ALLREADY2("AllReadyExistException", "L'adresse SIP existe dejà"),
	NOTFOUND1("NotFoundException", "Le profil skype à supprimer n'existe pas"),
	NOTFOUND2("NotFoundException", "Le profil skype à mettre à jour n'existe pas"),
	NOTFOUND3("NotFoundException", "Le collaborateur dont on veut maj le profil skype n'existe pas"),
	NOTFOUND4("NotFoundException", "cil demandant la modif du profil non trouvé"),
	NOTAUTHORIZED1("NotAuthorized", "demande de mise à jour du profil à expiré"),
	NOTAUTHORIZED2("NotAuthorized", "création d'un nouveau profil autrement que actif interdite"),
	NOTAUTHORIZED3("NotAuthorized", "Modification d'autre champs interdite en cas de désactivation de profil"),
	NOTAUTHORIZED4("NotAuthorized", "Modification d'autre champs interdite en cas de résactivation de profil"),
	// il s'agit ci-dessous d'exceptions levées par les packages User
	NOTFOUND5("NotFoundException", "collaborateur à passer en cil non trouvé"),
	NOTFOUND6("NotFoundException", "collaborateur cil pour mise à jour droits non trouvé"),
	NOTFOUND7("NotFoundException", "collaborateur cil pour mise à jour password non trouvé"),
	NOTFOUND8("NotFoundException", "mot de passe précédent non identique à celui en base"),
	NOTFOUND9("NotFoundException", "cil dont on supprime les roles n'existe pas en base"),
	ALLREADY3("AllReadyExistException", "L'adresse SIP existe dejà");

	private String ExceptionType;
	private String ExceptionDetail;
	
	ExceptionListEnum(final String ExceptionType, final String ExceptionDetail) {
		this.ExceptionType = ExceptionType;
		this.ExceptionDetail = ExceptionDetail;
	}

	public String getExceptionType() {
		return ExceptionType;
	}

	public String getExceptionDetail() {
		return ExceptionDetail;
	}

}
