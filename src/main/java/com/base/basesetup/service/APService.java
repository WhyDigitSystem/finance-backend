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
	List<PaymentVO> getAllPaymentByOrgId(Long orgId);

	List<PaymentVO> getPaymentById(Long id);

	PaymentVO updateCreatePayment(@Valid PaymentDTO paymentDTO) throws ApplicationException;

	List<Map<String, Object>> getPartyNameAndCodeForPayment(Long orgId,String partyName, String branch, String finYear);

	List<Map<String, Object>> getCurrencyAndTransCurrencyForPayment(Long orgId, String branch, String branchCode,
			String finYear, String partyName);

	List<Map<String, Object>> getStateCodeByOrgIdForPayment(Long orgId);

	List<Map<String, Object>> getAccountGroupNameByOrgIdForPayment(Long orgId);

	String getPaymentDocId(Long orgId, String finYear, String branch, String branchCode);

	// ARBillBalance
	List<ApBillBalanceVO> getAllApBillBalanceByOrgId(Long orgId);

	List<ApBillBalanceVO> getAllApBillBalanceById(Long id);

	Map<String, Object> updateCreateApBillBalance(@Valid ApBillBalanceDTO apBillBalanceDTO) throws ApplicationException;

	List<ApBillBalanceVO> getApBillBalanceByActive();

	List<Map<String, Object>> getPartyNameAndCodeForApBillBalance(Long orgId);

// 	PaymentRegister
	List<Map<String, Object>> getAllPaymentRegister(Long orgId, String fromDate, String toDate, String subLedgerName);

	List<Map<String, Object>> getPartyNameAndPartyCode(Long orgId,String branch,String finYear);

//	String getApBillBalanceDocId(Long orgId, String finYear, String branch, String branchCode);

}
