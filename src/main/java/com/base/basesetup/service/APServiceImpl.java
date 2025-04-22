package com.base.basesetup.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import com.base.basesetup.dto.ReceiptInvDetailsDTO;
import com.base.basesetup.entity.ApBillBalanceVO;
import com.base.basesetup.entity.ArapAdjustmentsVO;
import com.base.basesetup.entity.DocumentTypeMappingDetailsVO;
import com.base.basesetup.entity.PartyMasterVO;
import com.base.basesetup.entity.PaymentInvDtlsVO;
import com.base.basesetup.entity.PaymentVO;
import com.base.basesetup.entity.ReceiptInvDetailsVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.ApBillBalanceRepo;
import com.base.basesetup.repo.ArapAdjustmentsRepo;
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
	ArapAdjustmentsRepo arapAdjustmentsRepo;

	@Autowired
	ApBillBalanceRepo apBillBalanceRepo;

	@Autowired
	PartyMasterRepo partyMasterRepo;

	@Autowired
	DocumentTypeMappingDetailsRepo documentTypeMappingDetailsRepo;

	@Autowired
	TdsPaymentRepo tdsPaymentRepo;

	@Override
	public List<PaymentVO> getAllPaymentByOrgId(Long orgId, String finYear, String branchCode) {

		return paymentRepo.getAllPaymentByOrgId(orgId, finYear, branchCode);
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

		paymentVO = paymentRepo.save(paymentVO);
		List<PaymentInvDtlsVO> paymentInvDtlsVOs = paymentInvDtlsRepo.findByPaymentVO(paymentVO);
		for (PaymentInvDtlsVO dtlsVO : paymentInvDtlsVOs) {
			ArapAdjustmentsVO adjustmentsVO = new ArapAdjustmentsVO();
			adjustmentsVO.setCancel(false);
			adjustmentsVO.setActive(true);
			adjustmentsVO.setCreatedBy(paymentVO.getCreatedBy());
			adjustmentsVO.setUpdatedBy(paymentVO.getUpdatedBy());
			adjustmentsVO.setFinYear(paymentVO.getFinYear());
			adjustmentsVO.setDocId(paymentVO.getDocId());
			adjustmentsVO.setDocDate(paymentVO.getDocDate());
			adjustmentsVO.setRefNo(dtlsVO.getInvNo());
			adjustmentsVO.setRefDate(dtlsVO.getInvDate());
			adjustmentsVO.setSubLedgerCode(paymentVO.getPartyCode());
			adjustmentsVO.setSubLedgerName(paymentVO.getPartyName());
			adjustmentsVO.setCurrency(dtlsVO.getCurrency());
			adjustmentsVO.setExRate(dtlsVO.getExRate());
			adjustmentsVO.setAmount(dtlsVO.getSettled());
			adjustmentsVO.setBaseAmt(dtlsVO.getSettled());
			adjustmentsVO.setNativeAmt(dtlsVO.getSettled());
			adjustmentsVO.setOrgId(paymentVO.getOrgId());
			adjustmentsVO.setAccCurrency(dtlsVO.getCurrency());
			// adjustmentsVO.setAccountName(paymentVO.getPartyName());
			adjustmentsVO.setBranch(paymentVO.getBranch());
			adjustmentsVO.setSourceId(dtlsVO.getId());

			PartyMasterVO masterVO = partyMasterRepo.findByPartyCode(paymentVO.getPartyCode());
			adjustmentsVO.setAccountName(masterVO.getAccountType());

			arapAdjustmentsRepo.save(adjustmentsVO);

			ArapAdjustmentsVO negativeAdjustmentsVO = new ArapAdjustmentsVO();
			negativeAdjustmentsVO.setCancel(false);
			negativeAdjustmentsVO.setActive(true);
			negativeAdjustmentsVO.setCreatedBy(paymentVO.getCreatedBy());
			negativeAdjustmentsVO.setUpdatedBy(paymentVO.getUpdatedBy());
			negativeAdjustmentsVO.setFinYear(paymentVO.getFinYear());
			negativeAdjustmentsVO.setDocId(dtlsVO.getInvNo());
			negativeAdjustmentsVO.setDocDate(dtlsVO.getInvDate());
			negativeAdjustmentsVO.setRefNo(paymentVO.getDocId());
			negativeAdjustmentsVO.setRefDate(paymentVO.getDocDate());
			negativeAdjustmentsVO.setSubLedgerCode(paymentVO.getPartyCode());
			negativeAdjustmentsVO.setSubLedgerName(paymentVO.getPartyName());
			negativeAdjustmentsVO.setCurrency(dtlsVO.getCurrency());
			negativeAdjustmentsVO.setExRate(dtlsVO.getExRate());
			negativeAdjustmentsVO.setAmount(dtlsVO.getSettled().negate());
			negativeAdjustmentsVO.setBaseAmt(dtlsVO.getSettled().negate());
			negativeAdjustmentsVO.setNativeAmt(dtlsVO.getSettled().negate());
			negativeAdjustmentsVO.setOrgId(paymentVO.getOrgId());
			negativeAdjustmentsVO.setAccCurrency(dtlsVO.getCurrency());
			negativeAdjustmentsVO.setAccountName(masterVO.getAccountType());
			negativeAdjustmentsVO.setBranch(paymentVO.getBranch());
			negativeAdjustmentsVO.setSourceId(dtlsVO.getId());
			arapAdjustmentsRepo.save(negativeAdjustmentsVO);
		}
		Map<String, Object> response = new HashMap<>();
		response.put("paymentVO", paymentVO);
		response.put("message", message);
		return response;
	}

	private void getPaymentVOFromPaymentDTO(PaymentDTO paymentDTO, PaymentVO paymentVO) throws ApplicationException {

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
		paymentVO.setActive(true);
		paymentVO.setBranch(paymentDTO.getBranch());
		paymentVO.setBranchCode(paymentDTO.getBranchCode());
		paymentVO.setFinYear(paymentDTO.getFinYear());
		paymentVO.setOrgId(paymentDTO.getOrgId());

		if (ObjectUtils.isNotEmpty(paymentDTO.getId())) {
			List<PaymentInvDtlsVO> paymentInvDtlsVOList = paymentInvDtlsRepo.findByPaymentVO(paymentVO);
			paymentInvDtlsRepo.deleteAll(paymentInvDtlsVOList);
		}

//		BigDecimal netAmount = BigDecimal.ZERO; 
//		BigDecimal onAccount = BigDecimal.ZERO;
//
//		List<PaymentInvDtlsVO> paymentInvDtlsVOs = new ArrayList<>();
//		BigDecimal totalSettled = BigDecimal.ZERO;
//
//		if (paymentDTO.getPaymentInvDtlsDTO() != null || !paymentDTO.getPaymentInvDtlsDTO().isEmpty()) {
//			for (PaymentInvDtlsDTO paymentInvDtlsDTO : paymentDTO.getPaymentInvDtlsDTO()) {
//				PaymentInvDtlsVO paymentInvDtlsVO = new PaymentInvDtlsVO();
//				paymentInvDtlsVO.setInvNo(paymentInvDtlsDTO.getInvNo());
//				paymentInvDtlsVO.setInvDate(paymentInvDtlsDTO.getInvDate());
//				paymentInvDtlsVO.setRefNo(paymentInvDtlsDTO.getRefNo());
//				paymentInvDtlsVO.setRefDate(paymentInvDtlsDTO.getRefDate());
//				paymentInvDtlsVO.setSupplierRefNo(paymentInvDtlsDTO.getSupplierRefNo());
//				paymentInvDtlsVO.setSupplierRefDate(paymentInvDtlsDTO.getSupplierRefDate());
//				paymentInvDtlsVO.setCurrency(paymentInvDtlsDTO.getCurrency());
//				paymentInvDtlsVO.setExRate(paymentInvDtlsDTO.getExRate());
//
//				BigDecimal paymentAmt = paymentDTO.getPaymentAmt();
//
//
//
//				paymentInvDtlsVO.setAmount(paymentInvDtlsDTO.getAmount());
//
//				// Calculate netAmount (sum of settled amounts)
//				netAmount = paymentDTO.getPaymentInvDtlsDTO().stream().map(PaymentInvDtlsDTO::getSettled)
//						.reduce(BigDecimal.ZERO, BigDecimal::add);
//				totalSettled = totalSettled.add(paymentInvDtlsDTO.getSettled());
//
//				// Calculate onAccount (the difference between paymentAmt and settled amounts)
//				onAccount = paymentAmt.subtract(totalSettled);
//
//				paymentInvDtlsVO.setOutstanding(paymentInvDtlsDTO.getOutstanding());
//				paymentInvDtlsVO.setSettled(paymentInvDtlsDTO.getSettled());
//
//				paymentInvDtlsVO.setPaymentVO(paymentVO);
//				paymentInvDtlsVOs.add(paymentInvDtlsVO);
//			}
//			paymentVO.setPaymentInvDtlsVO(paymentInvDtlsVOs);
//			if (netAmount.compareTo(paymentDTO.getPaymentAmt()) > 0) {
//				throw new ApplicationException("Total Settled Amount should not be greater than Payment Amount");
//			}
//
//			onAccount = paymentDTO.getPaymentAmt().subtract(netAmount);
//			paymentVO.setNetAmount(netAmount);
//			paymentVO.setOnAccount(onAccount);
//		} else {
//			paymentVO.setOnAccount(paymentDTO.getPaymentAmt());
//		}

		BigDecimal netAmount = BigDecimal.ZERO;
		BigDecimal onAccount = BigDecimal.ZERO;
//		BigDecimal totalTdsAmount = BigDecimal.ZERO;
		BigDecimal totalOutstanding = BigDecimal.ZERO;
		BigDecimal receiptAmount = paymentDTO.getReceiptAmt();

		List<PaymentInvDtlsVO> paymentInvDtlsVOs = new ArrayList<>();
		List<PaymentInvDtlsDTO> paymentDetailsList = paymentDTO.getPaymentInvDtlsDTO();

		if (paymentDetailsList != null && !paymentDetailsList.isEmpty()) {
			for (PaymentInvDtlsDTO dto : paymentDetailsList) {
				PaymentInvDtlsVO vo = new PaymentInvDtlsVO();

				// Copy basic fields
				vo.setInvNo(dto.getInvNo());
				vo.setInvDate(dto.getInvDate());
				vo.setRefNo(dto.getRefNo());
				vo.setRefDate(dto.getRefDate());
				vo.setCurrency(dto.getCurrency());
				vo.setExRate(dto.getExRate());
				vo.setSupplierRefNo(dto.getSupplierRefNo());
				vo.setSupplierRefDate(dto.getSupplierRefDate());
				vo.setSettled(dto.getSettled());

				vo.setAmount(dto.getAmount());

				if (dto.getSettled().compareTo(dto.getAmount()) > 0) {
					throw new ApplicationException(
							"Settled amount (" + dto.getSettled() + ") cannot be greater than charge amount ("
									+ dto.getAmount() + ") for invoice: " + dto.getInvNo());
				}

				BigDecimal outstanding = dto.getAmount().subtract(dto.getSettled());
				if (outstanding.compareTo(BigDecimal.ZERO) < 0) {
					outstanding = BigDecimal.ZERO;
				}
				vo.setOutstanding(outstanding);

				totalOutstanding = totalOutstanding.add(vo.getOutstanding());

				if (dto.getSettled().compareTo(BigDecimal.ZERO) > 0) {
					netAmount = netAmount.add(dto.getSettled());
				}

				if (dto.getAmount().compareTo(dto.getSettled()) == 0) {
					vo.setIsSettled(true);
				} else {
					vo.setIsSettled(false);
				}

				paymentInvDtlsVOs.add(vo);
				vo.setPaymentVO(paymentVO);
			}

			if (receiptAmount.compareTo(netAmount) > 0) {
				onAccount = receiptAmount.subtract(netAmount);
			} else {
				onAccount = BigDecimal.ZERO;
			}

			paymentVO.setPaymentInvDtlsVO(paymentInvDtlsVOs);
		} else {

			onAccount = receiptAmount;
		}

		paymentVO.setNetAmount(netAmount);
		paymentVO.setOnAccount(onAccount);
//		paymentVO.setTdsAmt(totalTdsAmount);
		paymentVO.setOutStandingTotal(totalOutstanding);
//		return paymentVO;

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
	public Map<String, Object> updateCreateApBillBalance(@Valid ApBillBalanceDTO apBillBalanceDTO)
			throws ApplicationException {
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
		Set<Object[]> payment = paymentRepo.findAllPaymentRegister(orgId, fromDate, toDate, subLedgerName);
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
	public List<Map<String, Object>> getPartyNameAndCodeForPayment(Long orgId, String partyName) {
		Set<Object[]> partyName1 = paymentRepo.findPartyNameAndCodeForPayment(orgId, partyName);
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
	public List<Map<String, Object>> getPartyNameAndPartyCode(Long orgId) {
		Set<Object[]> group = paymentRepo.findPartyNameAndPartyCode(orgId);
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

	@Override
	public List<Map<String, Object>> getAPAgeing(String Asondate, String partyname, String pdate, Long orgId) {
		Set<Object[]> group = arapAdjustmentsRepo.findAPAgenig(Asondate, partyname, pdate, orgId);
		return getAPAgeing(group);
	}

	private List<Map<String, Object>> getAPAgeing(Set<Object[]> customer) {
		List<Map<String, Object>> apage = new ArrayList<>();
		for (Object[] sup : customer) {
			Map<String, Object> apageing = new HashMap<>();
			apageing.put("partyName", sup[0] != null ? sup[0].toString() : "");
			apageing.put("partyCode", sup[1] != null ? sup[1].toString() : "");

			apageing.put("orgid", sup[2] != null ? sup[2].toString() : "");
			apageing.put("arapdetailsid", sup[3] != null ? sup[3].toString() : "");
			apageing.put("doctypecode", sup[4] != null ? sup[4].toString() : "");
			apageing.put("branch", sup[5] != null ? sup[5].toString() : "");
			apageing.put("subledgercode", sup[6] != null ? sup[6].toString() : "");
			apageing.put("subledgername", sup[7] != null ? sup[7].toString() : "");
			apageing.put("partytype", sup[8] != null ? sup[8].toString() : "");
			apageing.put("subledgerdivision", sup[9] != null ? sup[9].toString() : "");
			apageing.put("currency", sup[10] != null ? sup[10].toString() : "");
			apageing.put("docid", sup[11] != null ? sup[11].toString() : "");
			apageing.put("docdate", sup[12] != null ? sup[12].toString() : "");
			apageing.put("supprefno", sup[13] != null ? sup[13].toString() : "");
			apageing.put("duedate", sup[14] != null ? sup[14].toString() : "");
			apageing.put("refno", sup[15] != null ? sup[15].toString() : "");
			apageing.put("refdate", sup[16] != null ? sup[16].toString() : "");
			apageing.put("amount", sup[17] != null ? sup[17].toString() : "");
			apageing.put("outstanding", sup[18] != null ? sup[18].toString() : "");
			apageing.put("totaldue", sup[19] != null ? sup[19].toString() : "");
			apageing.put("unadjusted", sup[20] != null ? sup[20].toString() : "");
			apageing.put("ddays", sup[21] != null ? sup[21].toString() : "");
			apageing.put("mslab1", sup[22] != null ? sup[22].toString() : "");
			apageing.put("mslab2", sup[23] != null ? sup[23].toString() : "");
			apageing.put("mslab3", sup[24] != null ? sup[24].toString() : "");
			apageing.put("mslab4", sup[25] != null ? sup[25].toString() : "");
			apageing.put("mslab5", sup[26] != null ? sup[26].toString() : "");
			apageing.put("name", sup[27] != null ? sup[27].toString() : "");

			apage.add(apageing);
		}
		return apage;
	}

	@Override
	public List<Map<String, Object>> getAPOutstanding(String Asondate, String partyname, String pdate, Long orgId) {
		Set<Object[]> group = arapAdjustmentsRepo.findAPOutstanding(Asondate, partyname, pdate, orgId);
		return getAPOutstanding(group);
	}

	private List<Map<String, Object>> getAPOutstanding(Set<Object[]> customer) {
		List<Map<String, Object>> apage = new ArrayList<>();
		for (Object[] sup : customer) {
			Map<String, Object> apageing = new HashMap<>();
			apageing.put("orgId", sup[0] != null ? sup[0].toString() : "");
			apageing.put("subledgerCode", sup[1] != null ? sup[1].toString() : "");
			apageing.put("subledgerName", sup[2] != null ? sup[2].toString() : "");
			apageing.put("partyType", sup[3] != null ? sup[3].toString() : "");
			apageing.put("creditDays", sup[4] != null ? sup[4].toString() : "");
			apageing.put("creditLimit", sup[5] != null ? sup[5].toString() : "");
			apageing.put("amount", sup[6] != null ? sup[6].toString() : "");
			apageing.put("outstanding", sup[7] != null ? sup[7].toString() : "");
			apageing.put("unadjusted", sup[8] != null ? sup[8].toString() : "");
			apageing.put("totalDue", sup[9] != null ? sup[9].toString() : "");
			apageing.put("mslab1", sup[10] != null ? sup[10].toString() : "");
			apageing.put("mslab2", sup[11] != null ? sup[11].toString() : "");
			apageing.put("mslab3", sup[12] != null ? sup[12].toString() : "");
			apageing.put("mslab4", sup[13] != null ? sup[13].toString() : "");
			apageing.put("mslab5", sup[14] != null ? sup[14].toString() : "");
			apageing.put("name", sup[15] != null ? sup[15].toString() : "");

			apage.add(apageing);
		}
		return apage;
	}

	@Override
	public List<Map<String, Object>> getPaymentFillGrid(Long orgId, String partyCode, String branchCode,
			String docDate) {
		Set<Object[]> group = paymentRepo.getPaymentFillGrid(orgId, partyCode, branchCode, docDate);

		if (group != null) {
			System.out.println("YES values are there");
		}
		return getPaymentFillGrid(group);
	}

	private List<Map<String, Object>> getPaymentFillGrid(Set<Object[]> customer) {
		List<Map<String, Object>> payfill = new ArrayList<>();
		for (Object[] sup : customer) {
			Map<String, Object> payfillgrid = new HashMap<>();
			payfillgrid.put("orgId", sup[0] != null ? sup[0].toString() : "");
			payfillgrid.put("docId", sup[1] != null ? sup[1].toString() : "");
			payfillgrid.put("docDate", sup[2] != null ? sup[2].toString() : "");
			payfillgrid.put("refNo", sup[3] != null ? sup[3].toString() : "");
			payfillgrid.put("refDate", sup[4] != null ? sup[4].toString() : "");
			payfillgrid.put("suppRefNo", sup[5] != null ? sup[5].toString() : "");
			payfillgrid.put("suppRefDate", sup[6] != null ? sup[6].toString() : "");
			payfillgrid.put("currency", sup[7] != null ? sup[7].toString() : "");
			payfillgrid.put("exRate", sup[8] != null ? sup[8].toString() : "");
			payfillgrid.put("amount", sup[9] != null ? sup[9].toString() : "");
			payfillgrid.put("outstanding", sup[10] != null ? sup[10].toString() : "");
			payfillgrid.put("settled", sup[11] != null ? sup[11].toString() : "");
			payfillgrid.put("arapDetailsId", sup[12] != null ? sup[12].toString() : "");

			payfill.add(payfillgrid);
		}
		return payfill;
	}

	@Override
	public List<Map<String, Object>> getarapoffsetadjustmentFillGrid(Long orgId, String partyCode, String branchCode,
			String docDate, String docId) {
		Set<Object[]> group = arapAdjustmentsRepo.findarapoffsetadjustmentFillGrid(orgId, partyCode, branchCode,
				docDate, docId);

		if (group != null) {
			System.out.println("YES values are there");
		}
		return getarapoffsetadjustmentFillGrid(group);
	}

	private List<Map<String, Object>> getarapoffsetadjustmentFillGrid(Set<Object[]> customer) {
		List<Map<String, Object>> arapoffsetfill = new ArrayList<>();
		for (Object[] sup : customer) {
			Map<String, Object> arapOffsetAdjustmentFillGrid = new HashMap<>();

			arapOffsetAdjustmentFillGrid.put("orgId", sup[0] != null ? sup[0].toString() : "");
			arapOffsetAdjustmentFillGrid.put("docId", sup[1] != null ? sup[1].toString() : "");
			arapOffsetAdjustmentFillGrid.put("docDate", sup[2] != null ? sup[2].toString() : "");
			arapOffsetAdjustmentFillGrid.put("refNo", sup[3] != null ? sup[3].toString() : "");
			arapOffsetAdjustmentFillGrid.put("refDate", sup[4] != null ? sup[4].toString() : "");
			arapOffsetAdjustmentFillGrid.put("currency", sup[5] != null ? sup[5].toString() : "");
			arapOffsetAdjustmentFillGrid.put("exRate", sup[6] != null ? sup[6].toString() : "");
			arapOffsetAdjustmentFillGrid.put("amount", sup[7] != null ? sup[7].toString() : "");
			arapOffsetAdjustmentFillGrid.put("outstanding", sup[8] != null ? sup[8].toString() : "");
			arapOffsetAdjustmentFillGrid.put("settled", sup[9] != null ? sup[9].toString() : "");
			arapOffsetAdjustmentFillGrid.put("arapDetailsId", sup[10] != null ? sup[10].toString() : "");

			arapoffsetfill.add(arapOffsetAdjustmentFillGrid);
		}
		return arapoffsetfill;
	}
}
