package com.example.projetfilrouge.pskype.batchstatut;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * Classe dédiée à l'envoi de mail via Spring Mail
 * Elle pourra être externalisée dans la couche infrastructure si le besoin n'est pas circonscrit au batch
 * @author Judicael
 *
 */
@Component
public class MailSenderProfile {

	@Autowired
	private JavaMailSender javaMailSender;
	
	/**
	 * Envoi d'un mail à partir d'un text de  corps de message et d'un destinataire
	 * @param message
	 * @param recipient
	 * @throws Exception
	 */
	public void sendMail(String message, String recipient) throws Exception{
		
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setSubject("Liste des profils skype désactivés");
		mail.setText(message);
		mail.setTo(recipient);
		javaMailSender.send(mail);
	}
}
