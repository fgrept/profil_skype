package com.example.projetfilrouge.pskype.batchstatut;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;

import com.example.projetfilrouge.pskype.domain.StatusSkypeProfileEnum;
import com.example.projetfilrouge.pskype.infrastructure.skypeprofile.SkypeProfileEntity;
/**
 * Classe processor où sont implémentées les règles de gestion
 * @author Judicaël
 *
 */
public class BatchProcessor implements ItemProcessor<SkypeProfileEntity, SkypeProfileEntity> {
	
	Logger log = LoggerFactory.getLogger(BatchProcessor.class);
	@Value("${daysexpired}")
	private int nbDaysExpired;
	
	@Override
	public SkypeProfileEntity process(SkypeProfileEntity item) throws Exception {
		Date date = new Date();
		log.debug("Processor : "+item.getSIP()+" "+item.getCollaborater().getCollaboraterId()+" "+item.getStatusProfile().name());
		log.info("Date actuelle : "+date.toString());
		log.info("Date avec 7 jours : "+ajouterJour(date,nbDaysExpired).toString());
		if (item.getExpirationDate() ==null ||(item.getExpirationDate().compareTo(ajouterJour(date,nbDaysExpired)))<0) {
			item.setStatusProfile(StatusSkypeProfileEnum.EXPIRED);
		}

		return item;
	}

	/**
	 * A voir si cette méthode ne doit pas être externalisée dans une classe dédiée aux dates (y a déjà le calcul de la date d'expiration
	 * @param date
	 * @param nbJour
	 * @return Date 
	 */
	private static Date ajouterJour(Date date, int nbJour) { 
		  Calendar cal = Calendar.getInstance(); 
		  cal.setTime(date);
		  cal.add(Calendar.DATE, nbJour);
		  return cal.getTime();
		}
}
