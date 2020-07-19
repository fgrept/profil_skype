package com.bnpparibas.projetfilrouge.pskype.batchstatut;

import org.springframework.batch.item.ItemProcessor;

import com.bnpparibas.projetfilrouge.pskype.domain.StatusSkypeProfileEnum;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.skypeprofile.SkypeProfileEntity;

public class BatchProcessor implements ItemProcessor<SkypeProfileEntity, SkypeProfileEntity> {

	@Override
	public SkypeProfileEntity process(SkypeProfileEntity item) throws Exception {
		System.out.println("Processor : "+item.getSIP()+" "+item.getCollaborater().getCollaboraterId()+" "+item.getStatusProfile().name());
		if (item.getExpirationDate() ==null) {
			item.setStatusProfile(StatusSkypeProfileEnum.DISABLED);
		}
		return item;
	}

}
