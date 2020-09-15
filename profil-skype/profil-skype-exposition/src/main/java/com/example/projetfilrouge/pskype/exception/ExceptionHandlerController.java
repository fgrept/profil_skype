package com.example.projetfilrouge.pskype.exception;

import com.example.projetfilrouge.pskype.infrastructure.exception.JpaTechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Classe réservée pour capter les exceptions transverses à la couche de persistance (exceptions techniques)
 * @Author : Judicaël
 */
@RestControllerAdvice
public class ExceptionHandlerController {

    private static Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);

    /**
     * Traitement des exceptions techniques JPA (principalement en écritute : insert/update/delete)
     * @param request
     * @param exception
     * @return
     */
    @ExceptionHandler(JpaTechnicalException.class)
    public ResponseEntity<ExceptionMessageResponse> JpaTechnicalExceptionHandler (HttpServletRequest request, JpaTechnicalException exception){

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        logger.info("Exception Jpa détectée");
        ExceptionMessageResponse message = new ExceptionMessageResponse(format.format(new Date()),
                                                                            exception.getMessage(),
                                                                            request.getRequestURI().toString());
        if (logger.isInfoEnabled()) {
            logger.info(message.toString());
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ExceptionMessageResponse> NullPointerExceptionHandler (HttpServletRequest request, NullPointerException exception){

        //Exception éventuellement à personnaliser  
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        logger.info("NullPointerException détectée");
        ExceptionMessageResponse message = new ExceptionMessageResponse(format.format(new Date()),
                exception.getMessage(),
                request.getRequestURI().toString());
        if (logger.isInfoEnabled()) {
            logger.info(message.toString());
        }
        return new ResponseEntity<>(message, HttpStatus.OK);

    }
}
