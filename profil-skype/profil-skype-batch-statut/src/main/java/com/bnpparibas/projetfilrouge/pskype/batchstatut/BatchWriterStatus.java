package com.bnpparibas.projetfilrouge.pskype.batchstatut;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.bnpparibas.projetfilrouge.pskype.infrastructure.skypeprofile.SkypeProfileEntity;


public class BatchWriterStatus implements ItemWriter<SkypeProfileEntity> {

	@Override
	public void write(List<? extends SkypeProfileEntity> items) throws Exception {
		for (SkypeProfileEntity entity  : items) {
			System.out.println("Writing the data " + entity.getSIP()+ "  "+entity.getStatusProfile()+" "+entity.getExpirationDate());
		}
		
	}


}
