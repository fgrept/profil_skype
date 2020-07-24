package com.bnpparibas.projetfilrouge.pskype.batchstatut;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
/**
 * Classe spécifique aux step.
 * A la fin du step, un mail est envoyé
 * @author Stagiaire
 *
 */
public class BatchStepListener implements StepExecutionListener {

	Logger log = LoggerFactory.getLogger(BatchStepListener.class);
	
	@Value("${mailreceiver}")
	private String mailReceiver;
	
	@Autowired
	private MailSenderProfile mailSender;
	
	@Override
	public void beforeStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub

	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		
		log.info("Nb de profils skype mis à jour : "+stepExecution.getWriteCount());
		log.info("Nb de profils skype non écrits : "+stepExecution.getWriteSkipCount());
		try {
			mailSender.sendMail(BatchStatutLoaderApplication.message, mailReceiver);
		}
		catch (Exception e) {
			log.error("erreur lors de l'envoi du mail");
		}
		return ExitStatus.COMPLETED;
	}

}
