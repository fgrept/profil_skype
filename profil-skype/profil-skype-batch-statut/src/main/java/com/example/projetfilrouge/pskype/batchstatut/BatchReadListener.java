package com.example.projetfilrouge.pskype.batchstatut;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;

import com.example.projetfilrouge.pskype.infrastructure.skypeprofile.SkypeProfileEntity;

/**
 * Classe pour tracer les lecture de la table et les cas d'erreur
 * @author Judicaël
 *
 */
public class BatchReadListener implements ItemReadListener<SkypeProfileEntity> {

	Logger log = LoggerFactory.getLogger(BatchReadListener.class);
	
	@Override
	public void beforeRead() {

	}

	@Override
	public void afterRead(SkypeProfileEntity item) {
		log.debug(item.toString());
		
	}

	@Override
	public void onReadError(Exception ex) {
		log.error(ex.getMessage());
		
	}
	

}
