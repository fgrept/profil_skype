package com.bnpparibas.projetfilrouge.pskype.exposition;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bnpparibas.projetfilrouge.pskype.application.ICollaboraterManagment;
import com.bnpparibas.projetfilrouge.pskype.domain.Collaborater;
import com.bnpparibas.projetfilrouge.pskype.dto.CollaboraterDto;

/**
 * 
 * @author Judicaël
 *
 */
@RestController
@RequestMapping("/collaborater")
@Secured({"ROLE_RESP","ROLE_ADMIN"})
public class CollaboraterController {
	
	private static Logger logger = LoggerFactory.getLogger(CollaboraterController.class);
	
	@Autowired
	private ICollaboraterManagment collaboraterManagment;
	
	@GetMapping("/list")
	public ResponseEntity<List<Collaborater>> listCollaborater(@RequestBody CollaboraterDto dto){
//		 new ResponseEntity<List<Collaborater>>(collaboraterManagment.);
		return null;
	}
}
