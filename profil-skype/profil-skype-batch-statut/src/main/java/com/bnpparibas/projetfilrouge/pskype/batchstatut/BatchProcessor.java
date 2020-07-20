package com.bnpparibas.projetfilrouge.pskype.batchstatut;

import java.util.Date;

import org.springframework.batch.item.ItemProcessor;

import com.bnpparibas.projetfilrouge.pskype.domain.StatusSkypeProfileEnum;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.skypeprofile.SkypeProfileEntity;

public class BatchProcessor implements ItemProcessor<SkypeProfileEntity, SkypeProfileEntity> {

	@Override
	public SkypeProfileEntity process(SkypeProfileEntity item) throws Exception {
		Date date = new Date();
		System.out.println("Processor : "+item.getSIP()+" "+item.getCollaborater().getCollaboraterId()+" "+item.getStatusProfile().name());
		System.out.println("Date actuelle : "+date.toString());
		System.out.println("Date profile : "+item.getExpirationDate().toString());
		if (item.getExpirationDate() ==null ||(item.getExpirationDate().compareTo(date))<0) {
			item.setStatusProfile(StatusSkypeProfileEnum.EXPIRED);
		}
		return item;
	}

}
