package com.bnpparibas.projetfilrouge.pskype.domain;

import java.util.List;

public interface IItCorrespondantDomain {
	void create(ItCorrespondant itCorrespondant);
	List<ItCorrespondant> findAllItCorrespondant();
}
