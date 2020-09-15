package com.example.projetfilrouge.pskype.batchstatut;


import com.example.projetfilrouge.pskype.domain.email.IMailDomain;
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
	private IMailDomain mailSenderProfile;
	
	@Override
	public void beforeStep(StepExecution stepExecution) {
		//Non utilisé pour le moment
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {


		log.info("Nb de profils skype mis à jour : "+stepExecution.getWriteCount());
		log.info("Nb de profils skype non écrits : "+stepExecution.getWriteSkipCount());
		if (stepExecution.getWriteCount() > 0 && !("".equals(BatchStatutLoaderApplication.message))) {
			String subject = "Liste des profils skype désactivés";
			try {
				mailSenderProfile.sendMail(subject, BatchStatutLoaderApplication.message, mailReceiver);
			} catch (Exception e) {
				log.error("erreur lors de l'envoi du mail");
			}
		}
		return ExitStatus.COMPLETED;
	}

}
