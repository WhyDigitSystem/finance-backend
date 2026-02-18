package com.base.basesetup.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.base.basesetup.dto.CancelIRNDTO;
import com.base.basesetup.dto.EInvoiceDTO;
import com.base.basesetup.dto.EInvoiceGetToketDTO;
import com.base.basesetup.dto.EwayBillDTO;
import com.base.basesetup.dto.EwayBillNonIRNDTO;
import com.base.basesetup.entity.EInvoiceVO;
import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public interface EInvoiceService {

	
	List<EInvoiceVO> getEInvoiceByDocId(String docId);

	EInvoiceDTO getEInvoicePayloadByDocId(String docId);

	Map<String, Object> createEinvoice(List<String> docId) throws JsonProcessingException;

	String generateIRN(List<String> docid);

	Map<String, Object> createEWayBill(List<String> irn) throws JsonProcessingException;

	EwayBillDTO getEWayBillByDocIdnew(String docIds);
	
//	Map<String, Object> generateToken(EInvoiceGetToketDTO eInvoiceGetToketDTO) throws JsonProcessingException, Exception;

	Map<String, Object> generateToken(List<EInvoiceGetToketDTO> eInvoiceGetToketDTO1) throws Exception;

	EwayBillNonIRNDTO generateEwayBillByNonIRN(String docIds);

	Map<String, Object> createEWayBillNonIRN(List<String> docId) throws JsonProcessingException;

	CancelIRNDTO cancelIRN(String docIds);

	Map<String, Object> cancelIRNInvoice(List<String> docIds) throws JsonProcessingException;

	

}
