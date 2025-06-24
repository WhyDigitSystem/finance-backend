package com.base.basesetup.service;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.base.basesetup.dto.ApBillBalanceDTO;
import com.base.basesetup.dto.PaymentDTO;
import com.base.basesetup.entity.ApBillBalanceVO;
import com.base.basesetup.entity.PaymentVO;
import com.base.basesetup.exception.ApplicationException;

@Service
public interface APService {

//	Payment
	List<PaymentVO> getAllPaymentByOrgId(Long orgId, String finYear, String branchCode);

	List<PaymentVO> getPaymentById(Long id);

	PaymentVO getPaymentByDocId(Long orgId, String docId);

	Map<String, Object> updateCreatePayment(PaymentDTO paymentDTO) throws ApplicationException;

	List<Map<String, Object>> getPartyNameAndCodeForPayment(Long orgId, String partyName);

	List<Map<String, Object>> getCurrencyAndTransCurrencyForPayment(Long orgId, String branch, String branchCode,
			String finYear, String partyName);

	List<Map<String, Object>> getStateCodeByOrgIdForPayment(Long orgId);

	List<Map<String, Object>> getAccountGroupNameByOrgIdForPayment(Long orgId);

	List<Map<String, Object>> getPaymentDetails(Long orgId,  String partyname, String fromDate,
			String toDate, String branchCode);

	List<Map<String, Object>> getPaymentSummary(Long orgId,  String partyname, String fromDate,
			String toDate, String branchCode);

	String getPaymentDocId(Long orgId, String finYear, String branch, String branchCode);

	// ARBillBalance
	List<ApBillBalanceVO> getAllApBillBalanceByOrgId(Long orgId);

	List<ApBillBalanceVO> getAllApBillBalanceById(Long id);

	Map<String, Object> updateCreateApBillBalance(@Valid ApBillBalanceDTO apBillBalanceDTO) throws ApplicationException;

	List<ApBillBalanceVO> getApBillBalanceByActive();

	List<Map<String, Object>> getPartyNameAndCodeForApBillBalance(Long orgId);

// 	PaymentRegister
	List<Map<String, Object>> getAllPaymentRegister(Long orgId, String fromDate, String toDate, String subLedgerName);

	List<Map<String, Object>> getPartyNameAndPartyCode(Long orgId);

	List<Map<String, Object>> getPaymentFillGrid(Long orgId, String partyCode, String branchCode);

//	String getApBillBalanceDocId(Long orgId, String finYear, String branch, String branchCode);

	
	List<Map<String, Object>> getAPAgeing(
		    @RequestParam(required = true) String Asondate,
		    @RequestParam(required = true) String partyname,
		    @RequestParam(required = false) String pdate,
		    @RequestParam(required = true) Long orgId
		);

	
	List<Map<String, Object>> getAPOutstanding(   @RequestParam(required = true) String Asondate, 
			@RequestParam(required = true) String partyname,@RequestParam(required = true) String branch,  
			@RequestParam(required = true)  Long orgId, 
			@RequestParam(required = false) String pdate
			    
		);
	



//	List<Map<String, Object>> getarapoffsetadjustmentFillGrid(Long orgId, String partyCode,String branchCode, String docDate,String docId);

	List<Map<String, Object>> getAllPaymentByOrgIdAndBranchCode(Long orgId, String branchCode, String partyName);

	PaymentVO approvePayment(Long orgId, Long id, String docId, String action, String actionBy)
			throws ApplicationException;

}
