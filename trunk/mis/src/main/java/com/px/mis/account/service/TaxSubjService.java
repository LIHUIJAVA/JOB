package com.px.mis.account.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.TaxSubj;

public interface TaxSubjService {

	ObjectNode insertTaxSubj(TaxSubj taxSubj);
	
	ObjectNode updateTaxSubjById(List<TaxSubj> taxSubj);
	
	ObjectNode deleteTaxSubjById(String autoId);
	
	TaxSubj selectTaxSubjById(Integer id);
	
    String selectTaxSubjList(Map map);

    String selectTaxSubjPrint(Map map);

    String uploadFileAddDb(MultipartFile file);
}
