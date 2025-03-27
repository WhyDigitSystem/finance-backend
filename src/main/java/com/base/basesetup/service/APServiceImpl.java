package com.base.basesetup.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.basesetup.dto.ApBillBalanceDTO;
import com.base.basesetup.dto.PaymentDTO;
import com.base.basesetup.dto.PaymentInvDtlsDTO;
import com.base.basesetup.dto.TaxInvoiceDTO;
import com.base.basesetup.dto.TdsPaymentDTO;
import com.base.basesetup.entity.ApBillBalanceVO;
import com.base.basesetup.entity.DocumentTypeMappingDetailsVO;
import com.base.basesetup.entity.PaymentInvDtlsVO;
import com.base.basesetup.entity.PaymentVO;
import com.base.basesetup.entity.TaxInvoiceVO;
import com.base.basesetup.entity.TdsPaymentVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.ApBillBalanceRepo;
import com.base.basesetup.repo.DocumentTypeMappingDetailsRepo;
import com.base.basesetup.repo.PartyMasterRepo;
import com.base.basesetup.repo.PaymentInvDtlsRepo;
import com.base.basesetup.repo.PaymentRepo;
import com.base.basesetup.repo.TdsPaymentRepo;

@Service
public class APServiceImpl implements APService {

	public static final Logger LOGGER = LoggerFactory.getLogger(APServiceImpl.class);

	@Autowired
	PaymentRepo paymentRepo;

	@Autowired
	PaymentInvDtlsRepo paymentInvDtlsRepo;

	@Autowired
	ApBillBalanceRepo apBillBalanceRepo;

	@Autowired
	PartyMasterRepo partyMasterRepo;

	@Autowired
	DocumentTypeMappingDetailsRepo documentTypeMappingDetailsRepo;
	
	@Autowired
	TdsPaymentRepo tdsPaymentRepo;

	@Override
	public List<PaymentVO> getAllPaymentByOrgId(Long orgId) {
		List<PaymentVO> paymentVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(orgId)) {
			LOGGER.info("Successfully Received  Payment BY OrgId: {}", orgId);
			paymentVO = paymentRepo.getAllPaymentByOrgId(orgId);
		}
		return paymentVO;
	}

	@Override
	public List<PaymentVO> getPaymentById(Long id) {
		List<PaymentVO> paymentVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received  Payment BY Id : {}", id);
			paymentVO = paymentRepo.getPaymentById(id);
		}
		return paymentVO;
	}

	@Override
	public Map<String, Object> updateCreatePayment(PaymentDTO paymentDTO) throws ApplicationException {
		PaymentVO paymentVO = new PaymentVO();
		String screenCode = "PT";
		String message;
		if (ObjectUtils.isNotEmpty(paymentDTO.getId())) {

			paymentVO = paymentRepo.findById(paymentDTO.getId())
					.orElseThrow(() -> new ApplicationException("Invalid Payment details"));
			paymentVO.setUpdatedBy(paymentDTO.getCreatedBy());
			getPaymentVOFromPaymentDTO(paymentDTO, paymentVO);
			message = "Payment Updated Successfully";
		} else {


//			GETDOCID API
			String docId = paymentRepo.getPaymentDocId(paymentDTO.getOrgId(), paymentDTO.getFinYear(),
					paymentDTO.getBranchCode(), screenCode);

			paymentVO.setDocId(docId);

//			// GETDOCID LASTNO +1
			DocumentTypeMappingDetailsVO documentTypeMappingDetailsVO = documentTypeMappingDetailsRepo
					.findByOrgIdAndFinYearAndBranchCodeAndScreenCode(paymentDTO.getOrgId(), paymentDTO.getFinYear(),
							paymentDTO.getBranchCode(), screenCode);
			documentTypeMappingDetailsVO.setLastno(documentTypeMappingDetailsVO.getLastno() + 1);
			documentTypeMappingDetailsRepo.save(documentTypeMappingDetailsVO);
			
			getPaymentVOFromPaymentDTO(paymentDTO, paymentVO);
			paymentVO.setUpdatedBy(paymentDTO.getCreatedBy());
			paymentVO.setCreatedBy(paymentDTO.getCreatedBy());
			message = "Payment Created Successfully";
		}

		paymentRepo.save(paymentVO);
		Map<String, Object> response = new HashMap<>();
		response.put("paymentVO", paymentVO);
		response.put("message", message);
		return response;
	}

	private void getPaymentVOFromPaymentDTO(PaymentDTO paymentDTO, PaymentVO paymentVO) {

		paymentVO.setPaymentType(paymentDTO.getPaymentType());
		paymentVO.setBankChargeAcc(paymentDTO.getBankChargeAcc());

		paymentVO.setBankCharges(paymentDTO.getBankCharges());
		paymentVO.setBankInCurrency(paymentDTO.getBankInCurrency());
		paymentVO.setType(paymentDTO.getType());
		paymentVO.setPartyCode(paymentDTO.getPartyCode());
		paymentVO.setServiceTaxAmt(paymentDTO.getServiceTaxAmt());
		paymentVO.setSTaxInCurrency(paymentDTO.getSTaxInCurrency());
		paymentVO.setPartyName(paymentDTO.getPartyName());
		paymentVO.setChequeBank(paymentDTO.getChequeBank());
		paymentVO.setGstState(paymentDTO.getGstState());
		paymentVO.setGstIn(paymentDTO.getGstIn());
		paymentVO.setChequeNo(paymentDTO.getChequeNo());
		paymentVO.setChequeDate(paymentDTO.getChequeDate());
		paymentVO.setBankCashAcc(paymentDTO.getBankCashAcc());
		paymentVO.setPayTo(paymentDTO.getPayTo());
		paymentVO.setPaymentAmt(paymentDTO.getPaymentAmt());
		paymentVO.setTdsAcc(paymentDTO.getTdsAcc());
		paymentVO.setTdsAmt(paymentDTO.getTdsAmt());
		paymentVO.setCurrency(paymentDTO.getCurrency());
		paymentVO.setCurrencyAmt(paymentDTO.getCurrencyAmt());
		paymentVO.setReceiptAmt(paymentDTO.getReceiptAmt());

		paymentVO.setBranch(paymentDTO.getBranch());
		paymentVO.setBranchCode(paymentDTO.getBranchCode());
		paymentVO.setActive(paymentDTO.isActive());
		paymentVO.setCancel(paymentDTO.isCancel());
		paymentVO.setCancelRemarks(paymentDTO.getCancelRemarks());
		paymentVO.setFinYear(paymentDTO.getFinYear());
		paymentVO.setOrgId(paymentDTO.getOrgId());
		
		
		if (ObjectUtils.isNotEmpty(paymentDTO.getId())) {
			List<PaymentInvDtlsVO> paymentInvDtlsVOList = paymentInvDtlsRepo.findByPaymentVO(paymentVO);
			paymentInvDtlsRepo.deleteAll(paymentInvDtlsVOList);
			
			List<TdsPaymentVO> tdsPaymentVO1 = tdsPaymentRepo.findByPaymentVO(paymentVO);
			tdsPaymentRepo.deleteAll(tdsPaymentVO1);
			
		}
		
		BigDecimal netAmount = BigDecimal.ZERO;
		
		List<PaymentInvDtlsVO> paymentInvDtlsVOs = new ArrayList<>();
		for (PaymentInvDtlsDTO paymentInvDtlsDTO : paymentDTO.getPaymentInvDtlsDTO()) {
			PaymentInvDtlsVO paymentInvDtlsVO = new PaymentInvDtlsVO();
			paymentInvDtlsVO.setInvNo(paymentInvDtlsDTO.getInvNo());
			paymentInvDtlsVO.setInvDate(paymentInvDtlsDTO.getInvDate());
			paymentInvDtlsVO.setRefNo(paymentInvDtlsDTO.getRefNo());
			paymentInvDtlsVO.setRefDate(paymentInvDtlsDTO.getRefDate());
			paymentInvDtlsVO.setSupplierRefNo(paymentInvDtlsDTO.getSupplierRefNo());
			paymentInvDtlsVO.setSupplierRefDate(paymentInvDtlsDTO.getSupplierRefDate());
			paymentInvDtlsVO.setCurrency(paymentInvDtlsDTO.getCurrency());
			paymentInvDtlsVO.setExRate(paymentInvDtlsDTO.getExRate());
			paymentInvDtlsVO.setAmount(paymentInvDtlsDTO.getAmount());
			paymentInvDtlsVO.setChargeAmt(paymentInvDtlsDTO.getChargeAmt());
			paymentInvDtlsVO.setOutstanding(paymentInvDtlsDTO.getOutstanding());
			paymentInvDtlsVO.setSettled(paymentInvDtlsDTO.getSettled());
			paymentInvDtlsVO.setPayExRate(paymentInvDtlsDTO.getPayExRate());
			paymentInvDtlsVO.setTxnSettled(paymentInvDtlsDTO.getTxnSettled());
			paymentInvDtlsVO.setGainOrLossAmt(paymentInvDtlsDTO.getGainOrLossAmt());
			paymentInvDtlsVO.setRemarks(paymentInvDtlsDTO.getRemarks());
			paymentInvDtlsVO.setFromDate(paymentInvDtlsDTO.getFromDate());
			paymentInvDtlsVO.setToDate(paymentInvDtlsDTO.getToDate());
			
			netAmount=netAmount.add(paymentInvDtlsDTO.getAmount());

			paymentInvDtlsVO.setPaymentVO(paymentVO);

			paymentInvDtlsVOs.add(paymentInvDtlsVO);

		}
		paymentVO.setPaymentInvDtlsVO(paymentInvDtlsVOs);

		List<TdsPaymentVO> tdsPaymentVOs = new ArrayList<>();
		for (TdsPaymentDTO tdsPaymentDTO : paymentDTO.getTdsPaymentDTO()) {
			TdsPaymentVO tdsPaymentVO = new TdsPaymentVO();

			tdsPaymentVO.setTdsWithHolding(tdsPaymentDTO.getTdsWithHolding());
			tdsPaymentVO.setTdsWithHoldingPer(tdsPaymentDTO.getTdsWithHoldingPer());
			tdsPaymentVO.setSection(tdsPaymentDTO.getSection());

			BigDecimal totTdsWhAmt = BigDecimal.ZERO;
			BigDecimal tdsWhPercent = tdsPaymentDTO.getTdsWithHoldingPer();
			System.out.println("TOTAL LC AMOUNT IS :" + netAmount);
			totTdsWhAmt = netAmount.multiply(tdsWhPercent.divide(BigDecimal.valueOf(100)));
			tdsPaymentVO.setTotTdsWhAmnt(totTdsWhAmt);

			tdsPaymentVO.setPaymentVO(paymentVO);
			tdsPaymentVOs.add(tdsPaymentVO);
		}
		paymentVO.setNetAmount(netAmount);
		
		paymentVO.setTdsPaymentVO(tdsPaymentVOs);
		
		
		}

	@Override
	public String getPaymentDocId(Long orgId, String finYear, String branch, String branchCode) {
		String ScreenCode = "PT";
		String result = paymentRepo.getPaymentDocId(orgId, finYear, branchCode, ScreenCode);
		return result;
	}

	// ApBillBalance
	@Override
	public List<ApBillBalanceVO> getAllApBillBalanceByOrgId(Long orgId) {
		List<ApBillBalanceVO> apBillBalanceVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(orgId)) {
			LOGGER.info("Successfully Received ApBillBalance BY OrgId : {}", orgId);
			apBillBalanceVO = apBillBalanceRepo.getAllApBillBalanceByOrgId(orgId);
		} 
		return apBillBalanceVO;
	}

	@Override
	public List<ApBillBalanceVO> getAllApBillBalanceById(Long id) {
		List<ApBillBalanceVO> apBillBalanceVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received ApBillBalance BY Id : {}", id);
			apBillBalanceVO = apBillBalanceRepo.getAllApBillBalanceById(id);
		} 
		return apBillBalanceVO;
	}

		@Override
		public Map<String, Object> updateCreateApBillBalance(
				@Valid ApBillBalanceDTO apBillBalanceDTO) throws ApplicationException {
			String screenCode = "APB";
			ApBillBalanceVO apBillBalanceVO = new ApBillBalanceVO();
			String message;
			if (ObjectUtils.isNotEmpty(apBillBalanceDTO.getId())) {
				apBillBalanceVO = apBillBalanceRepo.findById(apBillBalanceDTO.getId())
						.orElseThrow(() -> new ApplicationException("Invalid ApBillBalance details"));
				createUpdateApBillBalanceVOByApBillBalanceDTO(apBillBalanceDTO, apBillBalanceVO);
				message = "AR Bill Balance Updated Successfully";
				apBillBalanceVO.setUpdatedBy(apBillBalanceDTO.getCreatedBy());
			} else {
//				// GETDOCID API
//				String docId = apBillBalanceRepo.getArBillBalanceDocId(apBillBalanceDTO.getOrgId(),
//						apBillBalanceDTO.getFinYear(), apBillBalanceDTO.getBranchCode(), screenCode);
//				apBillBalanceVO.setDocId(docId);
//
//				// GETDOCID LASTNO +1
//				DocumentTypeMappingDetailsVO documentTypeMappingDetailsVO = documentTypeMappingDetailsRepo
//						.findByOrgIdAndFinYearAndBranchCodeAndScreenCode(apBillBalanceDTO.getOrgId(),
//								apBillBalanceDTO.getFinYear(), apBillBalanceDTO.getBranchCode(), screenCode);
//				documentTypeMappingDetailsVO.setLastno(documentTypeMappingDetailsVO.getLastno() + 1);
//				documentTypeMappingDetailsRepo.save(documentTypeMappingDetailsVO);
				apBillBalanceVO.setUpdatedBy(apBillBalanceDTO.getCreatedBy());
				apBillBalanceVO.setCreatedBy(apBillBalanceDTO.getCreatedBy());
				createUpdateApBillBalanceVOByApBillBalanceDTO(apBillBalanceDTO, apBillBalanceVO);
				message = "AP Bill Balance Created Successfully";
			}

			apBillBalanceRepo.save(apBillBalanceVO);
			Map<String, Object> response = new HashMap<>();
			response.put("apBillBalanceVO", apBillBalanceVO);
			response.put("message", message);
			return response;
    }
	private void createUpdateApBillBalanceVOByApBillBalanceDTO(@Valid ApBillBalanceDTO apBillBalanceDTO,
			ApBillBalanceVO apBillBalanceVO) {
		apBillBalanceVO.setAccName(apBillBalanceDTO.getAccName());
		apBillBalanceVO.setPartyName(apBillBalanceDTO.getPartyName());
		apBillBalanceVO.setPartyCode(apBillBalanceDTO.getPartyCode());
		apBillBalanceVO.setCreditDays(apBillBalanceDTO.getCreditDays());
		apBillBalanceVO.setDocType(apBillBalanceDTO.getDocType());
		apBillBalanceVO.setCurrency(apBillBalanceDTO.getCurrency());
		apBillBalanceVO.setYearEndExRate(apBillBalanceDTO.getYearEndExRate());
		apBillBalanceVO.setBillExRate(apBillBalanceDTO.getBillExRate());
		apBillBalanceVO.setPostBillExRate(apBillBalanceDTO.isPostBillExRate());
		apBillBalanceVO.setBillNo(apBillBalanceDTO.getBillNo());
		apBillBalanceVO.setBillDate(apBillBalanceDTO.getBillDate());
		apBillBalanceVO.setSuppRefNo(apBillBalanceDTO.getSuppRefNo());
		apBillBalanceVO.setSuppRefDate(apBillBalanceDTO.getSuppRefDate());
		apBillBalanceVO.setDueDate(apBillBalanceDTO.getDueDate());
		apBillBalanceVO.setDebitAmt(apBillBalanceDTO.getDebitAmt());
		apBillBalanceVO.setCreditAmt(apBillBalanceDTO.getCreditAmt());
		apBillBalanceVO.setVoucherNo(apBillBalanceDTO.getVoucherNo());
		apBillBalanceVO.setAdjustmentDone(apBillBalanceDTO.isAdjustmentDone());
		apBillBalanceVO.setActive(apBillBalanceDTO.isActive());
		apBillBalanceVO.setBranch(apBillBalanceDTO.getBranch());
		apBillBalanceVO.setBranchCode(apBillBalanceDTO.getBranchCode());
		apBillBalanceVO.setCreatedBy(apBillBalanceDTO.getCreatedBy());
		apBillBalanceVO.setCancel(apBillBalanceDTO.isCancel());
		apBillBalanceVO.setCancelRemarks(apBillBalanceDTO.getCancelRemarks());
		apBillBalanceVO.setFinYear(apBillBalanceDTO.getFinYear());
		apBillBalanceVO.setOrgId(apBillBalanceDTO.getOrgId());
	}

	@Override
	public List<ApBillBalanceVO> getApBillBalanceByActive() {
		return apBillBalanceRepo.findApBillBalanceByActive();
	}

	@Override
	public List<Map<String, Object>> getPartyNameAndCodeForApBillBalance(Long orgId) {
		Set<Object[]> partyName = apBillBalanceRepo.getPartyNameAndCodeForApBillBalance(orgId);
		return getPartyNameForAPB(partyName);
	}

	private List<Map<String, Object>> getPartyNameForAPB(Set<Object[]> customer) {
		List<Map<String, Object>> doctypeMappingDetails = new ArrayList<>();
		for (Object[] sup : customer) {
			Map<String, Object> doctype = new HashMap<>();
			doctype.put("partyName", sup[0] != null ? sup[0].toString() : "");
			doctype.put("partyCode", sup[1] != null ? sup[1].toString() : "");
			doctypeMappingDetails.add(doctype);
		}

		return doctypeMappingDetails;
	}

	
	// Payment Service
	@Override
	public List<Map<String, Object>> getAllPaymentRegister(Long orgId, String fromDate, String toDate,
			String subLedgerName) {
		Set<Object[]> payment = paymentRepo.findAllPaymentRegister(orgId, fromDate, toDate,
				subLedgerName);
		return getPayment(payment);
	}

	private List<Map<String, Object>> getPayment(Set<Object[]> getRegister) {
		List<Map<String, Object>> doctypeMappingDetails = new ArrayList<>();
		for (Object[] sup : getRegister) {
			Map<String, Object> doctype = new HashMap<>();
			doctype.put("docId", sup[0] != null ? sup[0].toString() : "");
			doctype.put("docDate", sup[1] != null ? sup[1].toString() : "");
			doctype.put("subLedgerName", sup[2] != null ? sup[2].toString() : "");
			doctype.put("bankCash", sup[3] != null ? sup[3].toString() : "");
			doctype.put("receiptAmount", sup[4] != null ? sup[4].toString() : "");
			doctype.put("bankCharges", sup[5] != null ? sup[5].toString() : "");
			doctype.put("tdsAmount", sup[6] != null ? sup[6].toString() : "");
			doctype.put("chequeBank", sup[7] != null ? sup[7].toString() : "");
			doctype.put("chequeNo", sup[8] != null ? sup[8].toString() : "");
			doctype.put("invoiceNo", sup[9] != null ? sup[9].toString() : "");
			doctype.put("invoiceDate", sup[10] != null ? sup[10].toString() : "");
			doctype.put("refNo", sup[11] != null ? sup[11].toString() : "");
			doctype.put("refDate", sup[12] != null ? sup[12].toString() : "");
			doctype.put("amount", sup[13] != null ? sup[13].toString() : "");
			doctype.put("outstanding", sup[14] != null ? sup[14].toString() : "");
			doctype.put("setteled", sup[15] != null ? sup[15].toString() : "");
			doctype.put("createdOn", sup[16] != null ? sup[16].toString() : "");
			doctype.put("createdBy", sup[17] != null ? sup[17].toString() : "");
			doctypeMappingDetails.add(doctype);
		}

		return doctypeMappingDetails;
	}

	@Override
	public List<Map<String, Object>> getPartyNameAndCodeForPayment(Long orgId,String partyName,String branch,String finYear) {
		Set<Object[]> partyName1 = paymentRepo.findPartyNameAndCodeForPayment(orgId,partyName,branch,finYear);
		return getPartyName(partyName1);
	}

	private List<Map<String, Object>> getPartyName(Set<Object[]> customer) {
		List<Map<String, Object>> doctypeMappingDetails = new ArrayList<>();
		for (Object[] sup : customer) {
			Map<String, Object> doctype = new HashMap<>();
			doctype.put("partyName", sup[0] != null ? sup[0].toString() : "");
			doctype.put("partyCode", sup[1] != null ? sup[1].toString() : "");
			doctype.put("currency", sup[2] != null ? sup[2].toString() : "");
			doctype.put("stateCode", sup[3] != null ? sup[3].toString() : "");
			doctype.put("gstin", sup[4] != null ? sup[4].toString() : "");
			doctype.put("gstin", sup[4] != null ? sup[4].toString() : "");
			
			
			doctypeMappingDetails.add(doctype);
		}

		return doctypeMappingDetails;
	}

	@Override
	public List<Map<String, Object>> getCurrencyAndTransCurrencyForPayment(Long orgId, String branch, String branchCode,
			String finYear, String partyName) {
		Set<Object[]> Currency = paymentRepo.findCurrencyAndTransCurrencyForPayment(orgId, branch, branchCode, finYear,
				partyName);
		return getCurrency(Currency);
	}

	private List<Map<String, Object>> getCurrency(Set<Object[]> customer) {
		List<Map<String, Object>> currency = new ArrayList<>();
		for (Object[] sup : customer) {
			Map<String, Object> currencyname = new HashMap<>();
			currencyname.put("inCurrency", sup[0] != null ? sup[0].toString() : "");
//			currencyname.put("transactionCurrency", sup[0] != null ? sup[0].toString() : "");
			currency.add(currencyname);
		}

		return currency;
	}

	@Override
	public List<Map<String, Object>> getStateCodeByOrgIdForPayment(Long orgId) {
		Set<Object[]> state = paymentRepo.findStateCodeByOrgIdForPayment(orgId);
		return getStateCode(state);
	}

	private List<Map<String, Object>> getStateCode(Set<Object[]> customer) {
		List<Map<String, Object>> state = new ArrayList<>();
		for (Object[] sup : customer) {
			Map<String, Object> statecode = new HashMap<>();
			statecode.put("stateCode", sup[0] != null ? sup[0].toString() : "");
			state.add(statecode);
		}

		return state;
	}

//	@Override
//	public List<ApBillBalanceVO> getAllApBillBalanceByOrgId(Long orgId, String branch, String branchCode,
//			String finYear) {
//		return apBillBalanceRepo.findAll(orgId,branch,branchCode,finYear);
//	}

	@Override
	public List<Map<String, Object>> getAccountGroupNameByOrgIdForPayment(Long orgId) {
		Set<Object[]> group = paymentRepo.findAccountGroupNameByOrgIdForPayment(orgId);
		return getAccountGroupName(group);
	}

	private List<Map<String, Object>> getAccountGroupName(Set<Object[]> customer) {
		List<Map<String, Object>> payment = new ArrayList<>();
		for (Object[] sup : customer) {
			Map<String, Object> accountgroupname = new HashMap<>();
			accountgroupname.put("TDSAccount", sup[0] != null ? sup[0].toString() : "");
			payment.add(accountgroupname);
		}

		return payment;
	}

	@Override
	public List<Map<String, Object>> getPartyNameAndPartyCode(Long orgId,String branch,String finYear) {
		Set<Object[]> group = paymentRepo.findPartyNameAndPartyCode(orgId,branch,finYear);
		return getPartyName1(group);
	}

	private List<Map<String, Object>> getPartyName1(Set<Object[]> customer) {
		List<Map<String, Object>> payment = new ArrayList<>();
		for (Object[] sup : customer) {
			Map<String, Object> accountgroupname = new HashMap<>();
			accountgroupname.put("partyName", sup[0] != null ? sup[0].toString() : "");
			accountgroupname.put("partyCode", sup[1] != null ? sup[1].toString() : "");
			payment.add(accountgroupname);
		}

		return payment;
	}

}
