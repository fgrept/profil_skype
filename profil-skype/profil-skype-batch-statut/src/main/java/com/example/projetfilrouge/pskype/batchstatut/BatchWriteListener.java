package com.example.projetfilrouge.pskype.batchstatut;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;

import com.example.projetfilrouge.pskype.infrastructure.skypeprofile.SkypeProfileEntity;

/**
 * Classe d'écriture en base de données.
 * Elle est appelée à chaque commit effectué
 * @author Judicaël
 *
 */
public class BatchWriteListener implements ItemWriteListener<SkypeProfileEntity> {

	Logger log = LoggerFactory.getLogger(BatchWriteListener.class);

	

	@Override
	public void beforeWrite(List<? extends SkypeProfileEntity> items) {

		
	}

	/**
	 * Ajout des adresses mails skype mises à jour dans le corps de message.
	 */
	@Override
	public void afterWrite(List<? extends SkypeProfileEntity> items) {
		int nbUpdate =items.size();
		String lineSeparator=System.getProperty("line.separator");
		if (BatchStatutLoaderApplication.nbProfilUpdate < BatchStatutLoaderApplication.MAX_PROFILES) {
			for (SkypeProfileEntity entity : items) {
				BatchStatutLoaderApplication.nbProfilUpdate++;
				BatchStatutLoaderApplication.message+=lineSeparator+entity.getSIP();
				if (BatchStatutLoaderApplication.nbProfilUpdate>=BatchStatutLoaderApplication.MAX_PROFILES) {
					BatchStatutLoaderApplication.message+=lineSeparator+"Maximum de profils atteint";
					break;
				}
			}
		}
		if (nbUpdate>0) {
			log.info("Nb de profils skype écrits : "+nbUpdate);
		}
	}

	@Override
	public void onWriteError(Exception exception, List<? extends SkypeProfileEntity> items) {
		
		if (items.size() >0) {
			log.error("Nb de profils skype non écrits : "+items.size());
		}
	}
}
