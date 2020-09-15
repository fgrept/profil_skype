package com.example.projetfilrouge.pskype.domain.email;


import com.example.projetfilrouge.pskype.domain.exception.SpringMailException;

/**
 * Classe comprenant les méthodes associées au mail
 */
public interface IMailDomain {

    void sendMail(String subject, String message, String recipient) throws SpringMailException;

}
