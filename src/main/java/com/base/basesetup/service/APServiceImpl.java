package com.base.basesetup.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import com.base.basesetup.entity.AccountsDetailsVO;
import com.base.basesetup.entity.AccountsVO;
import com.base.basesetup.entity.ApBillBalanceVO;
import com.base.basesetup.entity.ArapAdjustmentsVO;
import com.base.basesetup.entity.ArapDetailsVO;
import com.base.basesetup.entity.DocumentTypeMappingDetailsVO;
import com.base.basesetup.entity.MultipleDocIdGenerationDetailsVO;
import com.base.basesetup.entity.PartyMasterVO;
import com.base.basesetup.entity.PaymentInvDtlsVO;
import com.base.basesetup.entity.PaymentVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.AccountsDetailsRepo;
import com.base.basesetup.repo.AccountsRepo;
import com.base.basesetup.repo.ApBillBalanceRepo;
import com.base.basesetup.repo.ArapAdjustmentsRepo;
import com.base.basesetup.repo.ArapDetailsRepo;
import com.base.basesetup.repo.DocumentTypeMappingDetailsRepo;
import com.base.basesetup.repo.MultipleDocIdGenerationDetailsRepo;
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

	@Autowired
	AccountsRepo accountsRepo;

	@Autowired
	AccountsDetailsRepo accountsDetailsRepo;

	@Autowired
	ArapDetailsRepo arapDetailsRepo;

	@Autowired
	MultipleDocIdGenerationDetailsRepo multipleDocIdGenerationDetailsRepo;

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

//       paymentRepo.save(paymentVO);

		paymentVO = paymentRepo.save(paymentVO);
		List<PaymentInvDtlsVO> paymentInvDtlsVOs = paymentInvDtlsRepo.findByPaymentVO(paymentVO);

		for (PaymentInvDtlsVO dtlsVO : paymentInvDtlsVOs) {

			String partyCode = paymentVO.getPartyCode();
			String docId = paymentVO.getDocId();
			LocalDate docDate = paymentVO.getDocDate();
			String invNo = dtlsVO.getInvNo();
			LocalDate invDate = dtlsVO.getInvDate();

			// Delete existing Forward adjustment if exists
			ArapAdjustmentsVO existingForward = arapAdjustmentsRepo.findByDocIdAndRefNoAndOrgIdAndSubledgerCode(docId,
					invNo, paymentVO.getOrgId(), partyCode);
			if (existingForward != null) {
				arapAdjustmentsRepo.delete(existingForward);
			}

			// Save new Forward adjustment
			ArapAdjustmentsVO forward = new ArapAdjustmentsVO();
			forward.setCancel(false);
			forward.setActive(true);
			forward.setCreatedBy(paymentVO.getCreatedBy());
			forward.setUpdatedBy(paymentVO.getUpdatedBy());
			forward.setFinYear(paymentVO.getFinYear());
			forward.setDocId(docId);
			forward.setDocDate(docDate);
			forward.setRefNo(invNo);
			forward.setRefDate(invDate);
			forward.setSubLedgerCode(partyCode);
			forward.setSubLedgerName(paymentVO.getPartyName());
			forward.setCurrency(dtlsVO.getCurrency());
			forward.setExRate(dtlsVO.getExRate());
			forward.setAmount(dtlsVO.getSettled());
			forward.setBaseAmt(dtlsVO.getSettled());
			forward.setNativeAmt(dtlsVO.getSettled());
			forward.setOrgId(paymentVO.getOrgId());
			forward.setAccCurrency(dtlsVO.getCurrency());
			forward.setBranchCode(paymentVO.getBranchCode());
			forward.setBranch(paymentVO.getBranch());
			forward.setSourceId(dtlsVO.getId());

			PartyMasterVO partyMaster = partyMasterRepo.findByPartyCode(partyCode);
			if (partyMaster != null) {
				forward.setAccountName(partyMaster.getAccountType());
			}

			arapAdjustmentsRepo.save(forward);

			ArapAdjustmentsVO existingReverse = arapAdjustmentsRepo.findByDocIdAndRefNoAndOrgIdAndSubledgerCode(invNo,
					docId, paymentVO.getOrgId(), partyCode);
			if (existingReverse != null) {
				arapAdjustmentsRepo.delete(existingReverse);
			}

			ArapAdjustmentsVO reverse = new ArapAdjustmentsVO();
			reverse.setCancel(false);
			reverse.setActive(true);
			reverse.setCreatedBy(paymentVO.getCreatedBy());
			reverse.setUpdatedBy(paymentVO.getUpdatedBy());
			reverse.setFinYear(paymentVO.getFinYear());
			reverse.setDocId(invNo);
			reverse.setDocDate(invDate);
			reverse.setRefNo(docId);
			reverse.setRefDate(docDate);
			reverse.setSubLedgerCode(partyCode);
			reverse.setSubLedgerName(paymentVO.getPartyName());
			reverse.setCurrency(dtlsVO.getCurrency());
			reverse.setExRate(dtlsVO.getExRate());
			reverse.setAmount(dtlsVO.getSettled().negate());
			reverse.setBaseAmt(dtlsVO.getSettled().negate());
			reverse.setNativeAmt(dtlsVO.getSettled().negate());
			reverse.setOrgId(paymentVO.getOrgId());
			reverse.setAccCurrency(dtlsVO.getCurrency());
			reverse.setBranchCode(paymentVO.getBranchCode());
			reverse.setBranch(paymentVO.getBranch());
			reverse.setSourceId(dtlsVO.getId());

			if (partyMaster != null) {
				reverse.setAccountName(partyMaster.getAccountType());
			}

			arapAdjustmentsRepo.save(reverse);
		}

		Map<String, Object> response = new HashMap<>();
		response.put("paymentVO", paymentVO);
		response.put("message", message);
		return response;

	}

	private PaymentVO getPaymentVOFromPaymentDTO(PaymentDTO paymentDTO, PaymentVO paymentVO)
			throws ApplicationException {

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
		paymentVO.setStatus(paymentDTO.getStatus());
		paymentVO.setBranch(paymentDTO.getBranch());
		paymentVO.setBranchCode(paymentDTO.getBranchCode());
		paymentVO.setFinYear(paymentDTO.getFinYear());
		paymentVO.setOrgId(paymentDTO.getOrgId());

		if (ObjectUtils.isNotEmpty(paymentDTO.getId())) {
			List<PaymentInvDtlsVO> paymentInvDtlsVOList = paymentInvDtlsRepo.findByPaymentVO(paymentVO);
			paymentInvDtlsRepo.deleteAll(paymentInvDtlsVOList);
		}

		BigDecimal netAmount = BigDecimal.ZERO;
		BigDecimal onAccount = BigDecimal.ZERO;
//		BigDecimal totalTdsAmount = BigDecimal.ZERO;
		BigDecimal totalOutstanding = BigDecimal.ZERO;
		BigDecimal totalSettled = BigDecimal.ZERO;

		List<PaymentInvDtlsVO> paymentInvDtlsVOs = new ArrayList<>();
		List<PaymentInvDtlsDTO> paymentDetailsList = paymentDTO.getPaymentInvDtlsDTO();

		if (paymentDetailsList != null && !paymentDetailsList.isEmpty()) {
			for (PaymentInvDtlsDTO dto : paymentDetailsList) {
				PaymentInvDtlsVO vo = new PaymentInvDtlsVO();
				vo.setInvNo(dto.getInvNo());
				vo.setInvDate(dto.getInvDate());
				vo.setRefNo(dto.getRefNo());
				vo.setRefDate(dto.getRefDate());
				vo.setExRate(dto.getExRate());
				vo.setGstAmount(dto.getGstAmount());
				vo.setCurrency(dto.getCurrency());
				vo.setExRate(dto.getExRate());
				vo.setSupplierRefNo(dto.getSupplierRefNo());
				vo.setSupplierRefDate(dto.getSupplierRefDate());
				vo.setSettled(dto.getSettled());
				vo.setChargeAmt(dto.getAmount().add(dto.getGstAmount()));
				vo.setAmount(dto.getAmount());
				vo.setPaymentVO(paymentVO);
				BigDecimal reciptAmount = paymentDTO.getPaymentAmt();
				netAmount = paymentDTO.getPaymentInvDtlsDTO().stream().map(PaymentInvDtlsDTO::getSettled)
						.reduce(BigDecimal.ZERO, BigDecimal::add);
				totalSettled = totalSettled.add(dto.getSettled());
				onAccount = reciptAmount.subtract(totalSettled);
				paymentInvDtlsVOs.add(vo);
				if (dto.getSettled().compareTo(dto.getChargeAmt()) > 0) {
					throw new ApplicationException(
							"Settled amount (" + dto.getSettled() + ") cannot be greater than charge amount ("
									+ dto.getChargeAmt() + ") for invoice: " + dto.getInvNo());
				}
//
//				BigDecimal outstanding = dto.getOutStanding();
				vo.setOutstanding(vo.getChargeAmt().subtract(vo.getSettled()));

				totalOutstanding = totalOutstanding.add(vo.getOutstanding());

			}

			paymentVO.setPaymentInvDtlsVO(paymentInvDtlsVOs);
			if (netAmount.compareTo(paymentDTO.getPaymentAmt()) > 0) {
				throw new ApplicationException("Total Settled Amount should not be greater than Payment Amount");
			}

			onAccount = paymentDTO.getPaymentAmt().subtract(netAmount);
			paymentVO.setNetAmount(netAmount);
			paymentVO.setOnAccount(onAccount);
		} else

		{
			paymentVO.setOnAccount(paymentDTO.getPaymentAmt());
		}
		paymentVO.setOutStandingTotal(totalOutstanding);
		return paymentVO;
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
			apageing.put("orgid", sup[0] != null ? sup[0].toString() : "");
			apageing.put("arapdetailsid", sup[1] != null ? sup[1].toString() : "");
			apageing.put("doctypecode", sup[2] != null ? sup[2].toString() : "");
			apageing.put("branch", sup[3] != null ? sup[3].toString() : "");
			apageing.put("subledgercode", sup[4] != null ? sup[4].toString() : "");
			apageing.put("subledgername", sup[5] != null ? sup[5].toString() : "");
			apageing.put("partytype", sup[6] != null ? sup[6].toString() : "");
			apageing.put("subledgerdivision", sup[7] != null ? sup[7].toString() : "");
			apageing.put("currency", sup[8] != null ? sup[8].toString() : "");
			apageing.put("docid", sup[9] != null ? sup[9].toString() : "");
			apageing.put("docdate", sup[10] != null ? sup[10].toString() : "");
			apageing.put("supprefno", sup[11] != null ? sup[11].toString() : "");
			apageing.put("duedate", sup[12] != null ? sup[12].toString() : "");
			apageing.put("refno", sup[13] != null ? sup[13].toString() : "");
			apageing.put("refdate", sup[14] != null ? sup[14].toString() : "");
			apageing.put("amount", sup[15] != null ? sup[15].toString() : "");
			apageing.put("outstanding", sup[16] != null ? sup[16].toString() : "");
			apageing.put("totaldue", sup[17] != null ? sup[17].toString() : "");
			apageing.put("unadjusted", sup[18] != null ? sup[18].toString() : "");
			apageing.put("ddays", sup[19] != null ? sup[19].toString() : "");
			apageing.put("mslab1", sup[20] != null ? sup[20].toString() : "");
			apageing.put("mslab2", sup[21] != null ? sup[21].toString() : "");
			apageing.put("mslab3", sup[22] != null ? sup[22].toString() : "");
			apageing.put("mslab4", sup[23] != null ? sup[23].toString() : "");
			apageing.put("mslab5", sup[24] != null ? sup[24].toString() : "");
			apageing.put("name", sup[25] != null ? sup[25].toString() : "");

			apage.add(apageing);
		}
		return apage;
	}

	@Override
	public List<Map<String, Object>> getAPOutstanding(String Asondate, String partyname, String branch, Long orgId,String pdate) {
		Set<Object[]> group = arapAdjustmentsRepo.findAPOutstanding(Asondate, partyname,branch,orgId,pdate);
		return getAPOutstanding(group);
	}

	private List<Map<String, Object>> getAPOutstanding(Set<Object[]> customer) {
		List<Map<String, Object>> apage = new ArrayList<>();
		for (Object[] sup : customer) {
			Map<String, Object> apageing = new HashMap<>();
			apageing.put("no", sup[0] != null ? sup[0].toString() : "");
			apageing.put("orgId", sup[1] != null ? sup[1].toString() : "");
			apageing.put("subledgerCode", sup[2] != null ? sup[2].toString() : "");
			apageing.put("partyName", sup[3] != null ? sup[3].toString() : "");
			apageing.put("subledgerName", sup[4] != null ? sup[4].toString() : "");
			apageing.put("partyType", sup[5] != null ? sup[5].toString() : "");
			apageing.put("branch", sup[6] != null ? sup[6].toString() : "");
			apageing.put("currency", sup[7] != null ? sup[7].toString() : "");
			apageing.put("creditDays", sup[8] != null ? sup[8].toString() : "");
			apageing.put("creditLimit", sup[9] != null ? sup[9].toString() : "");
			apageing.put("amount", sup[10] != null ? sup[10].toString() : "");
			apageing.put("outstanding", sup[11] != null ? sup[11].toString() : "");
			apageing.put("unadjusted", sup[12] != null ? sup[12].toString() : "");
			apageing.put("totaldue", sup[13] != null ? sup[13].toString() : "");

			apage.add(apageing);
		}
		return apage;
	}

	@Override
	public List<Map<String, Object>> getPaymentFillGrid(Long orgId, String partyCode, String branchCode) {
		Set<Object[]> group = paymentRepo.getPaymentFillGrid(orgId, partyCode, branchCode);

		if (group != null) {
			System.out.println("YES values are there");
		}
		return getPaymentFillGrid(group);
	}

	private List<Map<String, Object>> getPaymentFillGrid(Set<Object[]> customer) {
		List<Map<String, Object>> payfill = new ArrayList<>();
		for (Object[] sup : customer) {
			Map<String, Object> doctype = new HashMap<>();
			doctype.put("arapDetailsId", sup[0] != null ? sup[0].toString() : "");
			doctype.put("branch", sup[1] != null ? sup[1].toString() : "");
			doctype.put("subledgerCode", sup[2] != null ? sup[2].toString() : "");
			doctype.put("vid", sup[3] != null ? sup[3].toString() : "");
			doctype.put("vdate", sup[4] != null ? sup[4].toString() : "");
			doctype.put("refno", sup[5] != null ? sup[5].toString() : "");
			doctype.put("refdate", sup[6] != null ? sup[6].toString() : "");
			doctype.put("supprefno", sup[7] != null ? sup[7].toString() : "");
			doctype.put("suprefdate", sup[8] != null ? sup[8].toString() : "");
			doctype.put("acccurrency", sup[9] != null ? sup[9].toString() : "");
			doctype.put("exrate", sup[10] != null ? sup[10].toString() : "");
			doctype.put("billamount", sup[11] != null ? sup[11].toString() : "");
			doctype.put("chargeAmt", sup[12] != null ? sup[12].toString() : "");
			doctype.put("chargableamt", sup[13] != null ? sup[13].toString() : "");
			doctype.put("gstamount", sup[14] != null ? sup[14].toString() : "");

			payfill.add(doctype);
		}
		return payfill;
	}

	@Override
	public List<Map<String, Object>> getAllPaymentByOrgIdAndBranchCode(Long orgId, String branchCode,
			String partyName) {
		Set<Object[]> group = paymentRepo.getAllPaymentByOrgIdAndBranchCode(orgId, branchCode, partyName);

		return getAllPaymentByOrgIdAnd(group);
	}

	private List<Map<String, Object>> getAllPaymentByOrgIdAnd(Set<Object[]> customer) {
		List<Map<String, Object>> payfill = new ArrayList<>();
		for (Object[] sup : customer) {
			Map<String, Object> doctype = new HashMap<>();
			doctype.put("netAmount", sup[0] != null ? new BigDecimal(sup[0].toString()) : BigDecimal.ZERO);
			doctype.put("docId", sup[1] != null ? sup[1].toString() : "");
			doctype.put("docDate", sup[2] != null ? sup[2].toString() : "");
			payfill.add(doctype);
		}
		return payfill;

	}

//	@Override
//	public List<Map<String, Object>> getarapoffsetadjustmentFillGrid(Long orgId, String partyCode, String branchCode,
//			String docDate, String docId) {
//		Set<Object[]> group = arapAdjustmentsRepo.findarapoffsetadjustmentFillGrid(orgId, partyCode, branchCode,
//				docDate, docId);
//
//		if (group != null) {
//			System.out.println("YES values are there");
//		}
//		return getarapoffsetadjustmentFillGrid(group);
//	}
//
//	private List<Map<String, Object>> getarapoffsetadjustmentFillGrid(Set<Object[]> customer) {
//		List<Map<String, Object>> arapoffsetfill = new ArrayList<>();
//		for (Object[] sup : customer) {
//			Map<String, Object> arapOffsetAdjustmentFillGrid = new HashMap<>();
//
//			arapOffsetAdjustmentFillGrid.put("orgId", sup[0] != null ? sup[0].toString() : "");
//			arapOffsetAdjustmentFillGrid.put("docId", sup[1] != null ? sup[1].toString() : "");
//			arapOffsetAdjustmentFillGrid.put("docDate", sup[2] != null ? sup[2].toString() : "");
//			arapOffsetAdjustmentFillGrid.put("refNo", sup[3] != null ? sup[3].toString() : "");
//			arapOffsetAdjustmentFillGrid.put("refDate", sup[4] != null ? sup[4].toString() : "");
//			arapOffsetAdjustmentFillGrid.put("currency", sup[5] != null ? sup[5].toString() : "");
//			arapOffsetAdjustmentFillGrid.put("exRate", sup[6] != null ? sup[6].toString() : "");
//			arapOffsetAdjustmentFillGrid.put("amount", sup[7] != null ? sup[7].toString() : "");
//			arapOffsetAdjustmentFillGrid.put("outstanding", sup[8] != null ? sup[8].toString() : "");
//			arapOffsetAdjustmentFillGrid.put("settled", sup[9] != null ? sup[9].toString() : "");
//			arapOffsetAdjustmentFillGrid.put("arapDetailsId", sup[10] != null ? sup[10].toString() : "");
//
//			arapoffsetfill.add(arapOffsetAdjustmentFillGrid);
//		}
//		return arapoffsetfill;
//	}

	@Override
	public PaymentVO approvePayment(Long orgId, Long id, String docId, String action, String actionBy)
			throws ApplicationException {

		PaymentVO paymentVO = paymentRepo.findByOrgIdAndIdAndDocId(orgId, id, docId);

		// Null check
		if (paymentVO == null) {
			throw new ApplicationException("Payment not found for the given details.");
		}

		// Approval status validation
		if ("Approved".equalsIgnoreCase(paymentVO.getApproveStatus())) {
			throw new ApplicationException("This Payment Already Approved");
		} else if ("Rejected".equalsIgnoreCase(paymentVO.getApproveStatus())) {
			throw new ApplicationException("This Payment Already Rejected");
		}

		// Status must be SUBMIT
//		if (!"SUBMIT".equalsIgnoreCase(paymentVO.getStatus())) {
//			throw new ApplicationException("Only SUBMIT payments can be approved or rejected.");
//		}

		String screenCode1 = "AC";
		String sourceScreenCode = paymentVO.getScreenCode();

		String accountsDocId = accountsRepo.getApproveDocId(paymentVO.getOrgId(), paymentVO.getFinYear(),
				paymentVO.getBranchCode(), sourceScreenCode, screenCode1);

		MultipleDocIdGenerationDetailsVO multipleDocIdGenerationDetailsVO = multipleDocIdGenerationDetailsRepo
				.findByOrgIdAndFinYearAndBranchCodeAndSourceScreenCodeAndScreenCode(paymentVO.getOrgId(),
						paymentVO.getFinYear(), paymentVO.getBranchCode(), sourceScreenCode, screenCode1);

		multipleDocIdGenerationDetailsVO.setLastno(multipleDocIdGenerationDetailsVO.getLastno() + 1);
		multipleDocIdGenerationDetailsRepo.save(multipleDocIdGenerationDetailsVO);

		AccountsVO accountsVO = new AccountsVO();
		accountsVO.setDocId(accountsDocId);
		accountsVO.setSourceScreen(paymentVO.getScreenName());
		accountsVO.setSourceId(paymentVO.getId());
		accountsVO.setCreatedBy(paymentVO.getCreatedBy());
		accountsVO.setModifiedBy(paymentVO.getUpdatedBy());
		accountsVO.setOrgId(paymentVO.getOrgId());
		accountsVO.setBranch(paymentVO.getBranch());
		accountsVO.setBranchCode(paymentVO.getBranchCode());
		accountsVO.setRefNo(paymentVO.getDocId());
		accountsVO.setRefDate(paymentVO.getDocDate());
		accountsVO.setVId(paymentVO.getDocId());
		accountsVO.setVDate(paymentVO.getDocDate());
		accountsVO.setCurrency(paymentVO.getCurrency());
		accountsVO.setRemarks(paymentVO.getCancelRemarks());
		accountsVO.setFinYear(paymentVO.getFinYear());
		accountsVO.setSourceScreenCode(paymentVO.getScreenCode());
		accountsVO.setModifiedon(paymentVO.getCommonDate().getModifiedon().toUpperCase());
		accountsVO.setCreatedon(paymentVO.getCommonDate().getModifiedon().toUpperCase());
//	        accountsVO.setTotalDebitAmount(dtlsVO.getSettled());
//	        accountsVO.setTotalCreditAmount(dtlsVO.getSettled());

		BigDecimal netAmount = paymentVO.getNetAmount();
		BigDecimal paymentAmt = paymentVO.getPaymentAmt();
		
		BigDecimal effectivepaymentAmt = (netAmount == null || netAmount.compareTo(BigDecimal.ZERO) == 0)
				? (paymentAmt != null ? paymentAmt : BigDecimal.ZERO)
				: netAmount;
		
		BigDecimal effectiveNetAmount = (netAmount == null || netAmount.compareTo(BigDecimal.ZERO) == 0)
				? (paymentAmt != null ? paymentAmt : BigDecimal.ZERO)
				: netAmount;

		List<AccountsDetailsVO> accountsDetailsVOs = new ArrayList<>();

		// PAYABLE A/C (Debit)
		AccountsDetailsVO accPayable = new AccountsDetailsVO();
		accPayable.setNDebitAmount(effectiveNetAmount);
		accPayable.setACategory("PAYABLE A/C");
		accPayable.setAccountName("PAYABLE A/C");
		accPayable.setSubLedgerCode(paymentVO.getPartyCode());
		accPayable.setDebitAmount(effectiveNetAmount);
		accPayable.setNCreditAmount(BigDecimal.ZERO);
		accPayable.setCreditAmount(BigDecimal.ZERO);
		accPayable.setArapFlag(true);
		accPayable.setArapAmount(effectiveNetAmount.multiply(BigDecimal.valueOf(-1)));
		accPayable.setBDebitAmount(effectiveNetAmount);
		accPayable.setBCrAmount(BigDecimal.ZERO);
		accPayable.setBArapAmount(effectiveNetAmount.multiply(BigDecimal.valueOf(-1)));
		accPayable.setACurrency(paymentVO.getCurrency());
		accPayable.setSubledgerName(paymentVO.getPartyName());
		accPayable.setNArapAmount(effectiveNetAmount.multiply(BigDecimal.valueOf(-1)));
		accPayable.setGstflag(2);
		accPayable.setAccountsVO(accountsVO);
		accountsDetailsVOs.add(accPayable);

		// BANK/CASH A/C (Credit)
		AccountsDetailsVO accBank = new AccountsDetailsVO();
		accBank.setNDebitAmount(BigDecimal.ZERO);
		accBank.setAccountName(paymentVO.getBankCashAcc());
		accBank.setSubLedgerCode("None");
		accBank.setDebitAmount(BigDecimal.ZERO);
		accBank.setNCreditAmount(effectivepaymentAmt);
		accBank.setCreditAmount(effectivepaymentAmt);
		accBank.setArapFlag(false);
		accBank.setArapAmount(BigDecimal.ZERO);
		accBank.setBDebitAmount(BigDecimal.ZERO);
		accBank.setBCrAmount(effectivepaymentAmt);
		accBank.setBArapAmount(BigDecimal.ZERO);
		accBank.setACurrency(paymentVO.getCurrency());
		accBank.setSubledgerName("None");
		accBank.setNArapAmount(BigDecimal.ZERO);
		accBank.setGstflag(3);
		accBank.setAccountsVO(accountsVO);
		accountsDetailsVOs.add(accBank);

		accountsVO.setTotalDebitAmount(effectiveNetAmount);
		accountsVO.setTotalCreditAmount(effectivepaymentAmt);
		accountsVO.setAccountsDetailsVO(accountsDetailsVOs);
		AccountsVO savedAccountsVO = accountsRepo.save(accountsVO);

		// Create ARAP Entry
		AccountsDetailsVO arapDetailsSource = accountsDetailsRepo.findByAccountsVOAndGstflag(savedAccountsVO, 2);

		ArapDetailsVO arapDetailsVO = new ArapDetailsVO();
		arapDetailsVO.setSourceTransid(arapDetailsSource.getId());
		arapDetailsVO.setCreatedBy(savedAccountsVO.getCreatedBy());
		arapDetailsVO.setUpdatedBy(savedAccountsVO.getModifiedBy());
		arapDetailsVO.setBranch(savedAccountsVO.getBranch());
		arapDetailsVO.setBranchCode(savedAccountsVO.getBranchCode());
		arapDetailsVO.setFinYear(savedAccountsVO.getFinYear());
		arapDetailsVO.setRefNo(savedAccountsVO.getRefNo());
		arapDetailsVO.setOrgId(savedAccountsVO.getOrgId());
		arapDetailsVO.setRefDate(savedAccountsVO.getRefDate());
		arapDetailsVO.setSubLedgerCode(arapDetailsSource.getSubLedgerCode());
		arapDetailsVO.setCurrency(arapDetailsSource.getACurrency());
		arapDetailsVO.setExRate(arapDetailsSource.getAExRate());
		arapDetailsVO.setAmount(arapDetailsSource.getArapAmount());
		arapDetailsVO.setBaseAmt(arapDetailsSource.getArapAmount());
		arapDetailsVO.setDueDate(savedAccountsVO.getDueDate());
		arapDetailsVO.setCreditDays(savedAccountsVO.getCreditDays());
		arapDetailsVO.setDocId(savedAccountsVO.getDocId());
		arapDetailsVO.setDocDate(savedAccountsVO.getDocDate());
		arapDetailsVO.setAccCurrency(savedAccountsVO.getCurrency());
		arapDetailsVO.setExRate(savedAccountsVO.getExRate());
		arapDetailsVO.setActive(savedAccountsVO.isActive());
		arapDetailsVO.setAccName(arapDetailsSource.getAccountName());
		arapDetailsVO.setGstFlag(arapDetailsSource.getGstflag());
		arapDetailsVO.setSubLedgerName(arapDetailsSource.getSubledgerName());
		arapDetailsVO.setSalesType(savedAccountsVO.getSalesType());
		arapDetailsVO.setNativeAmt(arapDetailsSource.getArapAmount());

		arapDetailsRepo.save(arapDetailsVO);

		paymentVO.setPurVoucherNo(savedAccountsVO.getDocId());
		paymentVO.setPurVoucherDate(savedAccountsVO.getDocDate());
		
		paymentVO.setApproveStatus(action);
		paymentVO.setApproveBy(actionBy);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
		paymentVO.setApproveOn(LocalDateTime.now().format(formatter).toUpperCase());

		return paymentRepo.save(paymentVO);
	}

	@Override
	public List<Map<String, Object>> getPaymentDetails(Long orgId, String finYear, String partyname, String fromDate,
			String toDate, String branchCode) {
		Set<Object[]> chType = paymentRepo.getPaymentDetails(orgId, finYear, partyname, fromDate, toDate, branchCode);
		return getPaymentDetails(chType);
	}

	private List<Map<String, Object>> getPaymentDetails(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();
			map.put("finyear", ch[0] != null ? ch[0].toString() : "");
			map.put("invno", ch[1] != null ? ch[1].toString() : "");
			map.put("invdate", ch[2] != null ? ch[2].toString() : "");
			map.put("docid", ch[3] != null ? ch[3].toString() : "");
			map.put("docdate", ch[4] != null ? ch[4].toString() : "");
			map.put("refno", ch[5] != null ? ch[5].toString() : "");
			map.put("refdate", ch[6] != null ? ch[6].toString() : "");
			map.put("partyname", ch[7] != null ? ch[7].toString() : "");
			map.put("partycode", ch[8] != null ? ch[8].toString() : "");
			map.put("paymentamt", ch[9] != null ? new BigDecimal(ch[9].toString()) : BigDecimal.ZERO);
			map.put("netamount", ch[10] != null ?new BigDecimal(ch[10].toString()) : BigDecimal.ZERO);
			map.put("onaccount", ch[11] != null ? new BigDecimal(ch[11].toString()) : BigDecimal.ZERO);
			map.put("chequeno", ch[12] != null ?ch[12].toString() : "");
			map.put("chequedate", ch[13] != null ? ch[13].toString() : "");
			map.put("tdsamt", ch[14] != null ? new BigDecimal(ch[14].toString()) : BigDecimal.ZERO);
			map.put("amount", ch[15] != null ? new BigDecimal(ch[15].toString()) : BigDecimal.ZERO);
			map.put("gstamount", ch[16] != null ? new BigDecimal(ch[16].toString()) : BigDecimal.ZERO);
			map.put("chargeamt", ch[17] != null ? new BigDecimal(ch[17].toString()) : BigDecimal.ZERO);
			map.put("settled", ch[18] != null ? new BigDecimal(ch[18].toString()) : BigDecimal.ZERO);
			map.put("outstanding", ch[19] != null ? new BigDecimal(ch[19].toString()) : BigDecimal.ZERO);
			map.put("status", ch[20] != null ? ch[20].toString() : "");
			map.put("approvestatus", ch[21] != null ? ch[21].toString() : "");
			List1.add(map);
		}
		return List1;
	}

	@Override
	public List<Map<String, Object>> getPaymentSummary(Long orgId, String finYear, String partyname, String fromDate,
			String toDate, String branchCode) {
		Set<Object[]> chType = paymentRepo.getPaymentSummary(orgId, finYear, partyname, fromDate, toDate, branchCode);
		return getPaymentSummary(chType);
	}

	private List<Map<String, Object>> getPaymentSummary(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();
			map.put("finyear", ch[0] != null ? ch[0].toString() : "");
			map.put("docid", ch[1] != null ? ch[1].toString() : "");
			map.put("docdate", ch[2] != null ? ch[2].toString() : "");
			map.put("partyname", ch[3] != null ? ch[3].toString() : "");
			map.put("partycode", ch[4] != null ? ch[4].toString() : "");
			map.put("chequeno", ch[5] != null ? ch[5].toString() : "");
			map.put("chequedate", ch[6] != null ? ch[6].toString() : "");
			map.put("paymentamt", ch[7] != null ? new BigDecimal(ch[7].toString()) : BigDecimal.ZERO);
			map.put("netamount", ch[8] != null ? new BigDecimal(ch[8].toString()) : BigDecimal.ZERO);
			map.put("tdsamt", ch[9] != null ? new BigDecimal(ch[9].toString()) : BigDecimal.ZERO);
			map.put("onaccount", ch[10] != null ? new BigDecimal(ch[10].toString()) : BigDecimal.ZERO);
			map.put("bankcashacc", ch[11] != null ? ch[11].toString() : "");
			map.put("status", ch[12] != null ? ch[12].toString() : "");
			map.put("approvestatus", ch[13] != null ? ch[13].toString() : "");
			List1.add(map);
		}
		return List1;
	}
	
	@Override
	public PaymentVO getPaymentByDocId(Long orgId, String docId) {
		// TODO Auto-generated method stub
		return paymentRepo.findAllPaymentByDocId(orgId, docId);
	}

}
