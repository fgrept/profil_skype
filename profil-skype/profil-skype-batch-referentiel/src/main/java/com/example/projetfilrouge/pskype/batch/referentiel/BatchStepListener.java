package com.example.projetfilrouge.pskype.batch.referentiel;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

/**
 * Classe spécifique aux step 

 * @author Judicaël
 *
 */
public class BatchStepListener implements StepExecutionListener {

	Logger log = LoggerFactory.getLogger(BatchStepListener.class);

	@Override
	public void beforeStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub

	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {

		if (log.isInfoEnabled()){
			String sLogNbUpdate = "Nb enregistrements mis à jour : "+stepExecution.getWriteCount();
			String sLogNbError = "Nb enregistrements écartés : "+stepExecution.getWriteSkipCount();
			log.info(sLogNbUpdate);
			log.info(sLogNbError);
		}



		return ExitStatus.COMPLETED;
	}

}
