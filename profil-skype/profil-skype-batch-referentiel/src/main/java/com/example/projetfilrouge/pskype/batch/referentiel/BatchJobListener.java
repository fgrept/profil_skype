package com.example.projetfilrouge.pskype.batch.referentiel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
/**
 * logging du traitement batch.
 * @author Judicaël
 *
 */
public class BatchJobListener implements JobExecutionListener {

	Logger log = LoggerFactory.getLogger(BatchJobListener.class);
	
	@Override
	public void beforeJob(JobExecution jobExecution) {
		String sLogInfo = "Job "+jobExecution.getJobInstance().getJobName()+" date de début : "+jobExecution.getStartTime();
		log.info(sLogInfo);

	}

	@Override
	public void afterJob(JobExecution jobExecution) {

		String sLogInfo = "Job "+jobExecution.getJobInstance().getJobName()+" date de fin : "+jobExecution.getEndTime()+" code retour"+jobExecution.getExitStatus();
		log.info(sLogInfo);

	}

}
