package com.base.basesetup.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

import com.base.basesetup.dto.ArBillBalanceDTO;
import com.base.basesetup.dto.ReceiptDTO;
import com.base.basesetup.dto.ReceiptInvDetailsDTO;
import com.base.basesetup.entity.AccountsDetailsVO;
import com.base.basesetup.entity.AccountsVO;
import com.base.basesetup.entity.ArBillBalanceVO;
import com.base.basesetup.entity.ArapAdjustmentsVO;
import com.base.basesetup.entity.ArapDetailsVO;
import com.base.basesetup.entity.DocumentTypeMappingDetailsVO;
import com.base.basesetup.entity.MultipleDocIdGenerationDetailsVO;
import com.base.basesetup.entity.PartyMasterVO;
import com.base.basesetup.entity.ReceiptInvDetailsVO;
import com.base.basesetup.entity.ReceiptVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.AccountsDetailsRepo;
import com.base.basesetup.repo.AccountsRepo;
import com.base.basesetup.repo.ArBillBalanceRepo;
import com.base.basesetup.repo.ArapAdjustmentsRepo;
import com.base.basesetup.repo.ArapDetailsRepo;
import com.base.basesetup.repo.CostInvoiceRepo;
import com.base.basesetup.repo.DocumentTypeMappingDetailsRepo;
import com.base.basesetup.repo.MultipleDocIdGenerationDetailsRepo;
import com.base.basesetup.repo.PartyMasterRepo;
import com.base.basesetup.repo.ReceiptInvDetailsRepo;
import com.base.basesetup.repo.ReceiptRepo;

@Service
public class ARServiceImpl implements ARService {

	public static final Logger LOGGER = LoggerFactory.getLogger(ARServiceImpl.class);

	@Autowired
	ReceiptRepo receiptRepo;

	@Autowired
	ReceiptInvDetailsRepo receiptInvDetailsRepo;

	@Autowired
	ArBillBalanceRepo arBillBalanceRepo;

	@Autowired
	DocumentTypeMappingDetailsRepo documentTypeMappingDetailsRepo;

	@Autowired
	ArapAdjustmentsRepo arapAdjustmentsRepo;

	@Autowired
	PartyMasterRepo partyMasterRepo;

	@Autowired
	AccountsRepo accountsRepo;

	@Autowired
	AccountsDetailsRepo accountsDetailsRepo;

	@Autowired
	ArapDetailsRepo arapDetailsRepo;

	@Autowired
	MultipleDocIdGenerationDetailsRepo multipleDocIdGenerationDetailsRepo;

	@Autowired
	CostInvoiceRepo costInvoiceRepo;

	// Receipt
	@Override
	public List<ReceiptVO> getAllReceiptReceivableByOrgId(Long orgId, String finYear, String branchCode) {

		return receiptRepo.getAllReceiptReceivableByOrgId(orgId, finYear, branchCode);
	}

	@Override
	public List<ReceiptVO> getAllReceiptReceivableById(Long id) {
		List<ReceiptVO> receiptReceivableVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received ReceiptReceivable BY Id : {}", id);
			receiptReceivableVO = receiptRepo.getAllReceiptReceivableById(id);
		}
		return receiptReceivableVO;
	}

	@Override
	public Map<String, Object> updateCreateReceiptReceivable(@Valid ReceiptDTO receiptDTO) throws ApplicationException {
		String screenCode = "RT";
		ReceiptVO receiptVO = new ReceiptVO();
		String message;
		if (ObjectUtils.isNotEmpty(receiptDTO.getId())) {
			receiptVO = receiptRepo.findById(receiptDTO.getId())
					.orElseThrow(() -> new ApplicationException("Receipt Not Found!"));
			receiptVO.setUpdatedBy(receiptDTO.getCreatedBy());
			createUpdateReceiptVOByReceiptDTO(receiptDTO, receiptVO);
			message = "Receipt Updated Successfully";
		} else {
			// GETDOCID API
			String docId = receiptRepo.getReceiptDocId(receiptDTO.getOrgId(), receiptDTO.getFinYear(),
					receiptDTO.getBranchCode(), screenCode);
			receiptVO.setDocId(docId);

			// GETDOCID LASTNO +1
			DocumentTypeMappingDetailsVO documentTypeMappingDetailsVO = documentTypeMappingDetailsRepo
					.findByOrgIdAndFinYearAndBranchCodeAndScreenCode(receiptDTO.getOrgId(), receiptDTO.getFinYear(),
							receiptDTO.getBranchCode(), screenCode);
			documentTypeMappingDetailsVO.setLastno(documentTypeMappingDetailsVO.getLastno() + 1);
			documentTypeMappingDetailsRepo.save(documentTypeMappingDetailsVO);

			receiptVO.setUpdatedBy(receiptDTO.getCreatedBy());
			receiptVO.setCreatedBy(receiptDTO.getCreatedBy());
			createUpdateReceiptVOByReceiptDTO(receiptDTO, receiptVO);
			message = "Receipt Created Successfully";
		}

//		// Validate receipt amount and settled amount
//		BigDecimal receiptAmt = receiptVO.getReceiptAmt();
//		BigDecimal settledAmt = receiptVO.getNetAmount();

		BigDecimal netAmount = BigDecimal.ZERO;
		if (netAmount.compareTo(receiptVO.getReceiptAmt()) > 0) {
			throw new ApplicationException("Total Settled Amount should not be greater than Payment Amount");
		}

		ReceiptVO savedReceiptVO = receiptRepo.save(receiptVO);

		List<ReceiptInvDetailsVO> savedReceiptInvDetailsVO = savedReceiptVO.getReceiptInvDetailsVO();
		if (savedReceiptInvDetailsVO != null && !savedReceiptInvDetailsVO.isEmpty()) {
			for (ReceiptInvDetailsVO savedReceiptInvDetails : savedReceiptInvDetailsVO) {

				String partyCode = savedReceiptVO.getCustomerCode();
				String docId = savedReceiptVO.getDocId();
				String invNo = savedReceiptInvDetails.getInvNo();

				ArapAdjustmentsVO existingForward = arapAdjustmentsRepo.findByDocIdAndRefNoAndOrgIdAndSubledgerCode(
						docId, invNo, savedReceiptVO.getOrgId(), partyCode);
				if (existingForward != null) {
					arapAdjustmentsRepo.delete(existingForward);
				}

				ArapAdjustmentsVO arapadjustments = new ArapAdjustmentsVO();
				arapadjustments.setBranch(savedReceiptVO.getBranch());
				arapadjustments.setFinYear(savedReceiptVO.getFinYear());
				arapadjustments.setSourceId(savedReceiptVO.getId());
				arapadjustments.setDocId(savedReceiptVO.getDocId());
				arapadjustments.setTdsAmt(savedReceiptVO.getTdsAmt());
				arapadjustments.setRefNo(savedReceiptInvDetails.getInvNo());
				arapadjustments.setRefDate(savedReceiptInvDetails.getInvDate());
				arapadjustments.setCurrency(savedReceiptVO.getCurrency());
				arapadjustments.setAccCurrency(savedReceiptVO.getCurrency());
				arapadjustments.setCurrency(savedReceiptVO.getCurrency());
				arapadjustments.setBaseAmt(savedReceiptInvDetails.getSettled());
				arapadjustments.setNativeAmt(savedReceiptInvDetails.getSettled());
				arapadjustments.setVoucherType(savedReceiptVO.getType());
				arapadjustments.setSubLedgerCode(savedReceiptVO.getCustomerCode());
				arapadjustments.setExRate(savedReceiptInvDetails.getExRate());
//					arapadjustments.setCreditDays(savedGrnVO.getNetAmount());
//					arapadjustments.setDueDate(detailsVO.getStatus());	

				arapadjustments.setOrgId(savedReceiptVO.getOrgId());
				arapadjustments.setActive(savedReceiptVO.isActive());
				arapadjustments.setCancel(savedReceiptVO.isCancel());
				arapadjustments.setCreatedBy(savedReceiptVO.getCreatedBy());
				arapadjustments.setUpdatedBy(savedReceiptVO.getUpdatedBy());
				arapadjustments.setBranchCode(savedReceiptVO.getBranchCode());
				arapadjustments.setSubLedgerName(savedReceiptVO.getCustomerName());
				arapadjustments.setAmount(savedReceiptInvDetails.getSettled());
				arapadjustments.setBaseAmt(savedReceiptInvDetails.getSettled());

				PartyMasterVO partyMaster = partyMasterRepo.findByPartyCode(savedReceiptVO.getCustomerCode());

				arapadjustments.setAccountName(partyMaster.getAccountType());
				System.out.println("ACCOUNT TYPE : " + partyMaster.getAccountType());
				arapAdjustmentsRepo.save(arapadjustments);

				ArapAdjustmentsVO existingReverse = arapAdjustmentsRepo.findByDocIdAndRefNoAndOrgIdAndSubledgerCode(
						invNo, docId, savedReceiptVO.getOrgId(), partyCode);
				if (existingReverse != null) {
					arapAdjustmentsRepo.delete(existingReverse);
				}

				// Second posting with negative values
				ArapAdjustmentsVO negativeArapAdjustments = new ArapAdjustmentsVO();
				negativeArapAdjustments.setBranch(savedReceiptVO.getBranch());
				negativeArapAdjustments.setFinYear(savedReceiptVO.getFinYear());
				negativeArapAdjustments.setSourceId(savedReceiptVO.getId());
				negativeArapAdjustments.setDocId(savedReceiptInvDetails.getInvNo()); // Changed as per request
				negativeArapAdjustments.setTdsAmt(savedReceiptVO.getTdsAmt());
				negativeArapAdjustments.setRefNo(savedReceiptVO.getDocId()); // Changed as per request
				negativeArapAdjustments.setRefDate(savedReceiptInvDetails.getInvDate());
				negativeArapAdjustments.setCurrency(savedReceiptVO.getCurrency());
				negativeArapAdjustments.setBaseAmt(savedReceiptInvDetails.getSettled().negate()); // Negative value
				negativeArapAdjustments.setNativeAmt(savedReceiptInvDetails.getSettled().negate()); // Negative value
				negativeArapAdjustments.setVoucherType(savedReceiptVO.getType());
				negativeArapAdjustments.setSubLedgerCode(savedReceiptVO.getCustomerCode());
				negativeArapAdjustments.setExRate(savedReceiptInvDetails.getExRate());
				negativeArapAdjustments.setOrgId(savedReceiptVO.getOrgId());
				negativeArapAdjustments.setActive(savedReceiptVO.isActive());
				negativeArapAdjustments.setCancel(savedReceiptVO.isCancel());
				negativeArapAdjustments.setCreatedBy(savedReceiptVO.getCreatedBy());
				negativeArapAdjustments.setUpdatedBy(savedReceiptVO.getUpdatedBy());
				negativeArapAdjustments.setBranchCode(savedReceiptVO.getBranchCode());
				negativeArapAdjustments.setSubLedgerName(savedReceiptVO.getCustomerName());
				negativeArapAdjustments.setAmount(savedReceiptInvDetails.getSettled().negate()); // Negative value
				negativeArapAdjustments.setAccountName(partyMaster.getAccountType());
				arapAdjustmentsRepo.save(negativeArapAdjustments);
			}
		}

		Map<String, Object> response = new HashMap<>();
		response.put("receiptVO", receiptVO);
		response.put("message", message);
		return response;
	}

	private ReceiptVO createUpdateReceiptVOByReceiptDTO(@Valid ReceiptDTO receiptDTO, ReceiptVO receiptVO)
			throws ApplicationException {
		receiptVO.setBranch(receiptDTO.getBranch());
		receiptVO.setBranchCode(receiptDTO.getBranchCode());
		receiptVO.setCustomer(receiptDTO.getCustomer());
		receiptVO.setClient(receiptDTO.getClient());
		receiptVO.setCreatedBy(receiptDTO.getCreatedBy());
		receiptVO.setActive(receiptDTO.isActive());
		receiptVO.setStatus(receiptDTO.getStatus());
		receiptVO.setCancel(receiptDTO.isCancel());
		receiptVO.setCancelRemarks(receiptDTO.getCancelRemarks());
		receiptVO.setFinYear(receiptDTO.getFinYear());
		receiptVO.setType(receiptDTO.getType());
		receiptVO.setCustomerName(receiptDTO.getCustomerName());
		receiptVO.setCustomerCode(receiptDTO.getCustomerCode());
		receiptVO.setBankCashAcc(receiptDTO.getBankCashAcc());
		BigDecimal reciptAmount = receiptDTO.getReceiptAmt();
		receiptVO.setReceiptAmt(reciptAmount);
		receiptVO.setBankChargeAcc(receiptDTO.getBankChargeAcc());
		receiptVO.setBankCharges(receiptDTO.getBankCharges());
		receiptVO.setInCurrencyBnkChargs(receiptDTO.getInCurrencyBnkChargs());
		receiptVO.setTdsAmt(receiptDTO.getTdsAmt());
		receiptVO.setInCurrencyTdsAmt(receiptDTO.getInCurrencyTdsAmt());
		receiptVO.setChequeBank(receiptDTO.getChequeBank());
		receiptVO.setReceiptType(receiptDTO.getReceiptType());
		receiptVO.setChequeUtiNo(receiptDTO.getChequeUtiNo());
		receiptVO.setChequeUtiDate(receiptDTO.getChequeUtiDate());
		receiptVO.setReceivedFrom(receiptDTO.getReceivedFrom());
		receiptVO.setReceiptType1(receiptDTO.getReceiptType1());
		receiptVO.setCurrency(receiptDTO.getCurrency());
		receiptVO.setCurrencyAmount(receiptDTO.getCurrencyAmount());
		receiptVO.setTaxAmt(receiptDTO.getTaxAmt());
		receiptVO.setBranchCode(receiptDTO.getBranchCode());
		receiptVO.setOrgId(receiptDTO.getOrgId());
		receiptVO.setShortName(receiptDTO.getShortName());
		receiptVO.setRemarks(receiptDTO.getRemarks());

		if (ObjectUtils.isNotEmpty(receiptVO.getId())) {
			List<ReceiptInvDetailsVO> receiptInvDetailsVO1 = receiptInvDetailsRepo.findByReceiptVO(receiptVO);
			receiptInvDetailsRepo.deleteAll(receiptInvDetailsVO1);
		}

		BigDecimal netAmount = BigDecimal.ZERO;
		BigDecimal onAccount = BigDecimal.ZERO;
		BigDecimal totalTds = BigDecimal.ZERO;
		BigDecimal totalOutStanding = BigDecimal.ZERO;
		BigDecimal totalChargeAmount = BigDecimal.ZERO;
		BigDecimal receiableAmount = BigDecimal.ZERO;

		List<ReceiptInvDetailsVO> receiptInvDetailsVOs = new ArrayList<>();
		BigDecimal totalSettled = BigDecimal.ZERO;

		List<ReceiptInvDetailsDTO> receiptDetailsList = receiptDTO.getReceiptInvDetailaDTO();
		BigDecimal receiptAmount = receiptDTO.getReceiptAmt();

		if (receiptDetailsList != null && !receiptDetailsList.isEmpty()) {
			for (ReceiptInvDetailsDTO receiptInvDetailsDTO : receiptDetailsList) {
				ReceiptInvDetailsVO receiptInvDetailsVO = new ReceiptInvDetailsVO();
				receiptInvDetailsVO.setInvNo(receiptInvDetailsDTO.getInvNo());
				receiptInvDetailsVO.setInvDate(receiptInvDetailsDTO.getInvDate());
				receiptInvDetailsVO.setRefNo(receiptInvDetailsDTO.getRefNo());
				receiptInvDetailsVO.setRefDate(receiptInvDetailsDTO.getRefDate());
				receiptInvDetailsVO.setMasterRef(receiptInvDetailsDTO.getMasterRef());
				receiptInvDetailsVO.setHouseRef(receiptInvDetailsDTO.getHouseRef());
				receiptInvDetailsVO.setCurrency(receiptInvDetailsDTO.getCurrency());
				receiptInvDetailsVO.setExRate(receiptInvDetailsDTO.getExRate());
				receiptInvDetailsVO.setTds(receiptInvDetailsDTO.getTds());

				BigDecimal amount = receiptInvDetailsDTO.getAmount() != null ? receiptInvDetailsDTO.getAmount()
						: BigDecimal.ZERO;
				BigDecimal gstAmt = receiptInvDetailsDTO.getGstAmt() != null ? receiptInvDetailsDTO.getGstAmt()
						: BigDecimal.ZERO;
				BigDecimal chargeAmt = amount.add(gstAmt);
				receiptInvDetailsVO.setChargeAmt(chargeAmt);
				
				totalChargeAmount=totalChargeAmount.add(receiptInvDetailsVO.getChargeAmt());
				

				BigDecimal tds = receiptInvDetailsDTO.getTds() != null ? receiptInvDetailsDTO.getTds()
						: BigDecimal.ZERO;
				BigDecimal tdsValue = amount.multiply(tds).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

				BigDecimal settled = receiptInvDetailsDTO.getSettled() != null ? receiptInvDetailsDTO.getSettled()
						: BigDecimal.ZERO;
				
				receiableAmount=receiableAmount.add(chargeAmt.subtract(tdsValue));
				
				BigDecimal outstanding = chargeAmt.subtract(settled).subtract(tdsValue);
				totalTds = totalTds.add(tdsValue);
				receiptInvDetailsVO.setOutstanding(outstanding);
				totalOutStanding = totalOutStanding.add(receiptInvDetailsVO.getOutstanding());

				receiptInvDetailsVO.setGstAmt(gstAmt);
				receiptInvDetailsVO.setAmount(amount);
				receiptInvDetailsVO.setSettled(settled);
				receiptInvDetailsVO.setTdsAmount(tdsValue);
				receiptInvDetailsVO.setRecExRate(receiptInvDetailsDTO.getRecExRate());
				receiptInvDetailsVO.setTxnSettled(receiptInvDetailsDTO.getTxnSettled());
				receiptInvDetailsVO.setGainAmt(receiptInvDetailsDTO.getGainAmt());
				receiptInvDetailsVO.setReceiptVO(receiptVO);

				totalSettled = totalSettled.add(settled);

				receiptInvDetailsVOs.add(receiptInvDetailsVO);
			}

			netAmount = totalSettled;

			if (netAmount.compareTo(receiptAmount) > 0) {
				throw new ApplicationException("Total Settled Amount should not be greater than Receipt Amount");
			}

			onAccount = receiptAmount.subtract(netAmount);
			receiptVO.setNetAmount(netAmount);
			receiptVO.setOnAccount(onAccount);
		} else {
			receiptVO.setOnAccount(receiptAmount);
		}
		receiptVO.setTdsAmt(totalTds);
		receiptVO.setTotalOutStanding(totalOutStanding);
		receiptVO.setTotalChargeAmount(totalChargeAmount);
		receiptVO.setReceivableAmount(receiableAmount);
		receiptVO.setReceiptInvDetailsVO(receiptInvDetailsVOs);

		return receiptVO;

	}

	@Override
	public List<ReceiptVO> getReceiptReceivableByActive() {
		return receiptRepo.findReceiptReceivablesByActive();
	}

	@Override
	public List<Map<String, Object>> getCustomerNameAndCodeForReceipt(Long orgId) {
		Set<Object[]> customerName = receiptRepo.getCustomerNameAndCodeForReceipt(orgId);
		return getCustomerName(customerName);
	}

	private List<Map<String, Object>> getCustomerName(Set<Object[]> customer) {
		List<Map<String, Object>> doctypeMappingDetails = new ArrayList<>();
		for (Object[] sup : customer) {
			Map<String, Object> doctype = new HashMap<>();
			doctype.put("customerName", sup[0] != null ? sup[0].toString() : "");
			doctype.put("customerCode", sup[1] != null ? sup[1].toString() : "");
			doctype.put("shortName", sup[2] != null ? sup[2].toString() : "");
			doctypeMappingDetails.add(doctype);
		}

		return doctypeMappingDetails;
	}

	@Override
	public String getReceiptDocId(Long orgId, String finYear, String branch, String branchCode) {
		String ScreenCode = "RT";
		String result = receiptRepo.getReceiptDocId(orgId, finYear, branchCode, ScreenCode);
		return result;
	}

	// ArBillBalance
	@Override
	public List<ArBillBalanceVO> getAllArBillBalanceByOrgId(Long orgId) {
		List<ArBillBalanceVO> arBillBalanceVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(orgId)) {
			LOGGER.info("Successfully Received ArApBillBalance BY OrgId : {}", orgId);
			arBillBalanceVO = arBillBalanceRepo.getAllArBillBalanceByOrgId(orgId);
		}
		return arBillBalanceVO;
	}

	@Override
	public List<ArBillBalanceVO> getAllArBillBalanceById(Long id) {
		List<ArBillBalanceVO> arBillBalanceVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received ArApBillBalance BY Id : {}", id);
			arBillBalanceVO = arBillBalanceRepo.getAllArBillBalanceById(id);
		}
		return arBillBalanceVO;
	}

	@Override
	public Map<String, Object> updateCreateArBillBalance(@Valid ArBillBalanceDTO arBillBalanceDTO)
			throws ApplicationException {
		String screenCode = "ARB";
		ArBillBalanceVO arBillBalanceVO = new ArBillBalanceVO();
		String message;
		if (ObjectUtils.isNotEmpty(arBillBalanceDTO.getId())) {
			arBillBalanceVO = arBillBalanceRepo.findById(arBillBalanceDTO.getId())
					.orElseThrow(() -> new ApplicationException("AR Bill Balance Not Found!"));
			createUpdateArBillBalanceVOByArBillBalanceDTO(arBillBalanceDTO, arBillBalanceVO);
			message = "AR Bill Balance Updated Successfully";
			arBillBalanceVO.setUpdatedBy(arBillBalanceDTO.getCreatedBy());
		} else {
			// GETDOCID API
			String docId = arBillBalanceRepo.getArBillBalanceDocId(arBillBalanceDTO.getOrgId(),
					arBillBalanceDTO.getFinYear(), arBillBalanceDTO.getBranchCode(), screenCode);
			arBillBalanceVO.setDocId(docId);

			// GETDOCID LASTNO +1
			DocumentTypeMappingDetailsVO documentTypeMappingDetailsVO = documentTypeMappingDetailsRepo
					.findByOrgIdAndFinYearAndBranchCodeAndScreenCode(arBillBalanceDTO.getOrgId(),
							arBillBalanceDTO.getFinYear(), arBillBalanceDTO.getBranchCode(), screenCode);
			documentTypeMappingDetailsVO.setLastno(documentTypeMappingDetailsVO.getLastno() + 1);
			documentTypeMappingDetailsRepo.save(documentTypeMappingDetailsVO);
			arBillBalanceVO.setUpdatedBy(arBillBalanceDTO.getCreatedBy());
			arBillBalanceVO.setCreatedBy(arBillBalanceDTO.getCreatedBy());
			createUpdateArBillBalanceVOByArBillBalanceDTO(arBillBalanceDTO, arBillBalanceVO);
			message = "AR Bill Balance Created Successfully";
		}

		arBillBalanceRepo.save(arBillBalanceVO);
		Map<String, Object> response = new HashMap<>();
		response.put("arBillBalanceVO", arBillBalanceVO);
		response.put("message", message);
		return response;
	}

	private void createUpdateArBillBalanceVOByArBillBalanceDTO(@Valid ArBillBalanceDTO arBillBalanceDTO,
			ArBillBalanceVO arBillBalanceVO) {
		arBillBalanceVO.setAccName(arBillBalanceDTO.getAccName());
		arBillBalanceVO.setPartyName(arBillBalanceDTO.getPartyName());
		arBillBalanceVO.setPartyCode(arBillBalanceDTO.getPartyCode());
		arBillBalanceVO.setCreditDays(arBillBalanceDTO.getCreditDays());
		arBillBalanceVO.setDocType(arBillBalanceDTO.getDocType());
		arBillBalanceVO.setCurrency(arBillBalanceDTO.getCurrency());
		arBillBalanceVO.setYearEndExRate(arBillBalanceDTO.getYearEndExRate());
		arBillBalanceVO.setBillExRate(arBillBalanceDTO.getBillExRate());
		arBillBalanceVO.setPostBillExRate(arBillBalanceDTO.isPostBillExRate());
		arBillBalanceVO.setBillNo(arBillBalanceDTO.getBillNo());
		arBillBalanceVO.setBillDate(arBillBalanceDTO.getBillDate());
		arBillBalanceVO.setSuppRefNo(arBillBalanceDTO.getSuppRefNo());
		arBillBalanceVO.setSuppRefDate(arBillBalanceDTO.getSuppRefDate());
		arBillBalanceVO.setDueDate(arBillBalanceDTO.getDueDate());
		arBillBalanceVO.setDebitAmt(arBillBalanceDTO.getDebitAmt());
		arBillBalanceVO.setCreditAmt(arBillBalanceDTO.getCreditAmt());
		arBillBalanceVO.setVoucherNo(arBillBalanceDTO.getVoucherNo());
		arBillBalanceVO.setAdjustmentDone(arBillBalanceDTO.isAdjustmentDone());
		arBillBalanceVO.setActive(arBillBalanceDTO.isActive());
		arBillBalanceVO.setBranch(arBillBalanceDTO.getBranch());
		arBillBalanceVO.setBranchCode(arBillBalanceDTO.getBranchCode());
		arBillBalanceVO.setCreatedBy(arBillBalanceDTO.getCreatedBy());
		arBillBalanceVO.setCancel(arBillBalanceDTO.isCancel());
		arBillBalanceVO.setCancelRemarks(arBillBalanceDTO.getCancelRemarks());
		arBillBalanceVO.setFinYear(arBillBalanceDTO.getFinYear());
		arBillBalanceVO.setOrgId(arBillBalanceDTO.getOrgId());
	}

	@Override
	public List<ArBillBalanceVO> getArBillBalanceByActive() {
		return arBillBalanceRepo.findArBillBalanceByActive();
	}

	@Override
	public List<Map<String, Object>> getPartyNameAndCodeForArBillBalance(Long orgId) {
		Set<Object[]> partyName = arBillBalanceRepo.getPartyNameAndCodeForArBillBalance(orgId);
		return getPartyName(partyName);
	}

	private List<Map<String, Object>> getPartyName(Set<Object[]> customer) {
		List<Map<String, Object>> doctypeMappingDetails = new ArrayList<>();
		for (Object[] sup : customer) {
			Map<String, Object> doctype = new HashMap<>();
			doctype.put("partyName", sup[0] != null ? sup[0].toString() : "");
			doctype.put("partyCode", sup[1] != null ? sup[1].toString() : "");
			doctypeMappingDetails.add(doctype);
		}

		return doctypeMappingDetails;
	}

	// Receipt Register
	@Override
	public List<Map<String, Object>> getAllReceiptRegister(Long orgId, String fromDate, String toDate,
			String subLedgerName) {
		Set<Object[]> register = receiptRepo.findAllReceiptRegister(orgId, fromDate, toDate, subLedgerName);
		return getRegister(register);
	}

	private List<Map<String, Object>> getRegister(Set<Object[]> getRegister) {
		List<Map<String, Object>> doctypeMappingDetails = new ArrayList<>();
		for (Object[] sup : getRegister) {
			Map<String, Object> doctype = new HashMap<>();
			doctype.put("docId", sup[0] != null ? sup[0].toString() : "");
			doctype.put("docDate", sup[1] != null ? sup[1].toString() : "");
			doctype.put("subLedgerName", sup[2] != null ? sup[2].toString() : "");
			doctype.put("bankCash", sup[3] != null ? sup[3].toString() : "");
			doctype.put("receiptAmount", sup[4] != null ? sup[4].toString() : "");
			doctype.put("bankCharges", sup[5] != null ? sup[5].toString() : "");
			doctype.put("taxAmount", sup[6] != null ? sup[6].toString() : "");
			doctype.put("tdsAmount", sup[7] != null ? sup[7].toString() : "");
			doctype.put("invoiceNo", sup[8] != null ? sup[8].toString() : "");
			doctype.put("invoiceDate", sup[9] != null ? sup[9].toString() : "");
			doctype.put("refNo", sup[10] != null ? sup[10].toString() : "");
			doctype.put("refDate", sup[11] != null ? sup[11].toString() : "");
			doctype.put("chequeBank", sup[12] != null ? sup[12].toString() : "");
			doctype.put("chequeNo", sup[13] != null ? sup[13].toString() : "");
			doctype.put("amount", sup[14] != null ? sup[14].toString() : "");
			doctype.put("outstanding", sup[15] != null ? sup[15].toString() : "");
			doctype.put("setteled", sup[16] != null ? sup[16].toString() : "");
			doctype.put("createdOn", sup[17] != null ? sup[17].toString() : "");
			doctype.put("createdBy", sup[18] != null ? sup[18].toString() : "");

			doctypeMappingDetails.add(doctype);
		}

		return doctypeMappingDetails;
	}

	@Override
	public String getArBillBalanceDocId(Long orgId, String finYear, String branch, String branchCode) {
		String ScreenCode = "ARB";
		String result = receiptRepo.getArBillBalanceDocId(orgId, finYear, branchCode, ScreenCode);
		return result;
	}

	@Override
	public List<Map<String, Object>> getReciptFillGrid(Long orgId, String partyCode, String branchCode) {
		Set<Object[]> register = receiptRepo.findReciptFillGrid(orgId, partyCode, branchCode);
		return getRecipt(register);
	}

	private List<Map<String, Object>> getRecipt(Set<Object[]> getRegister) {
		List<Map<String, Object>> doctypeMappingDetails = new ArrayList<>();
		for (Object[] sup : getRegister) {
			Map<String, Object> doctype = new HashMap<>();
			doctype.put("arapDetailsId", sup[0] != null ? sup[0].toString() : "");
			doctype.put("branch", sup[1] != null ? sup[1].toString() : "");
			doctype.put("subLedgerCode", sup[2] != null ? sup[2].toString() : "");
			doctype.put("vid", sup[3] != null ? sup[3].toString() : "");
			doctype.put("vdate", sup[4] != null ? sup[4].toString() : "");
			doctype.put("refNo", sup[5] != null ? sup[5].toString() : "");
			doctype.put("refate", sup[6] != null ? sup[6].toString() : "");
			doctype.put("supprefNo", sup[7] != null ? sup[7].toString() : "");
			doctype.put("supprefDate", sup[8] != null ? sup[8].toString() : "");
			doctype.put("acccurrency", sup[9] != null ? sup[9].toString() : "");
			doctype.put("exrate", sup[10] != null ? sup[10].toString() : "");
			doctype.put("billamount", sup[11] != null ? sup[11].toString() : "");
			doctype.put("chargeAmt", sup[12] != null ? sup[12].toString() : "");
			doctype.put("chargableamt", sup[13] != null ? sup[13].toString() : "");
			doctype.put("tdsamt", sup[14] != null ? sup[14].toString() : "");
			doctype.put("gstpercent", sup[15] != null ? sup[15].toString() : "");
			doctype.put("gstamount", sup[16] != null ? sup[16].toString() : "");

			doctypeMappingDetails.add(doctype);
		}

		return doctypeMappingDetails;
	}

	@Override
	public ReceiptVO approveReceipt(Long orgId, Long id, String docId, String action, String actionBy)
			throws ApplicationException {

		ReceiptVO receiptVO = receiptRepo.findByOrgIdAndIdAndDocId(orgId, id, docId);

		// Null check before any processing
		if (receiptVO == null) {
			throw new ApplicationException("Receipt not found for the given details.");
		}

		// Check approval status
		if ("Approved".equalsIgnoreCase(receiptVO.getApproveStatus())) {
			throw new ApplicationException("This Receipt Already Approved");
		} else if ("Rejected".equals(receiptVO.getApproveStatus())) {
			throw new ApplicationException("This Receipt Already Rejected");
		}

		// Only allow action if status is SUBMIT
		if (!"SUBMIT".equalsIgnoreCase(receiptVO.getStatus())) {
			throw new ApplicationException("Only SUBMIT Receipt can be approved or rejected.");
		}

		String screenCode1 = "AC";
		String sourceScreenCode = receiptVO.getScreenCode();

		String accountsDocId = accountsRepo.getApproveDocId(receiptVO.getOrgId(), receiptVO.getFinYear(),
				receiptVO.getBranchCode(), sourceScreenCode, screenCode1);

		MultipleDocIdGenerationDetailsVO multipleDocIdGenerationDetailsVO = multipleDocIdGenerationDetailsRepo
				.findByOrgIdAndFinYearAndBranchCodeAndSourceScreenCodeAndScreenCode(receiptVO.getOrgId(),
						receiptVO.getFinYear(), receiptVO.getBranchCode(), sourceScreenCode, screenCode1);

		multipleDocIdGenerationDetailsVO.setLastno(multipleDocIdGenerationDetailsVO.getLastno() + 1);
		multipleDocIdGenerationDetailsRepo.save(multipleDocIdGenerationDetailsVO);

		AccountsVO accountsVO = new AccountsVO();
		accountsVO.setDocId(accountsDocId);
		accountsVO.setSourceScreen(receiptVO.getScreenName());
		accountsVO.setSourceId(receiptVO.getId());
		accountsVO.setCreatedBy(receiptVO.getCreatedBy());
		accountsVO.setModifiedBy(receiptVO.getUpdatedBy());
		accountsVO.setOrgId(receiptVO.getOrgId());
		accountsVO.setBranch(receiptVO.getBranch());
		accountsVO.setBranchCode(receiptVO.getBranchCode());
		accountsVO.setRefNo(receiptVO.getDocId());
		accountsVO.setRefDate(receiptVO.getDocDate());
		accountsVO.setVId(receiptVO.getDocId());
		accountsVO.setVDate(receiptVO.getDocDate());
		accountsVO.setCurrency(receiptVO.getCurrency());
		accountsVO.setExRate(BigDecimal.ONE);
		accountsVO.setRemarks(receiptVO.getRemarks());
		accountsVO.setFinYear(receiptVO.getFinYear());
		// accountsVO.setTotalDebitAmount(dtlsVO.getSettled());
		// accountsVO.setTotalCreditAmount(dtlsVO.getSettled());

		BigDecimal netAmount = receiptVO.getNetAmount();
		BigDecimal receiptAmt = receiptVO.getReceiptAmt();

		BigDecimal effectivereceiptAmt = (netAmount == null || netAmount.compareTo(BigDecimal.ZERO) == 0)
				? (receiptAmt != null ? receiptAmt : BigDecimal.ZERO)
				: netAmount;
		
		BigDecimal effectiveNetAmount = (netAmount == null || netAmount.compareTo(BigDecimal.ZERO) == 0)
				? (receiptAmt != null ? receiptAmt : BigDecimal.ZERO)
				: netAmount;

		List<AccountsDetailsVO> accountsDetailsVOs = new ArrayList<>();

		// RECEIVABLE A/C (Credit)
		AccountsDetailsVO receivable = new AccountsDetailsVO();
		receivable.setNDebitAmount(BigDecimal.ZERO);
		receivable.setACategory("RECEIVABLE A/C");
		receivable.setAccountName("RECEIVABLE A/C");
		receivable.setSubLedgerCode(receiptVO.getCustomerCode());
		receivable.setDebitAmount(BigDecimal.ZERO);
		receivable.setNCreditAmount(effectiveNetAmount.add(receiptVO.getTdsAmt()));
		receivable.setCreditAmount(effectiveNetAmount.add(receiptVO.getTdsAmt()));
		receivable.setArapFlag(true);
		receivable.setArapAmount(effectiveNetAmount.add(receiptVO.getTdsAmt()).negate());
		receivable.setBDebitAmount(BigDecimal.ZERO);
		receivable.setBCrAmount(effectiveNetAmount.add(receiptVO.getTdsAmt()));
		receivable.setBArapAmount(effectiveNetAmount.add(receiptVO.getTdsAmt()).negate());
		receivable.setACurrency(receiptVO.getCurrency());
		receivable.setSubledgerName(receiptVO.getCustomerName());
		receivable.setNArapAmount(effectiveNetAmount.add(receiptVO.getTdsAmt()).negate());
		receivable.setGstflag(1);
		receivable.setAccountsVO(accountsVO);
		accountsDetailsVOs.add(receivable);

		// BANK/CASH (Debit)
		AccountsDetailsVO cashBank = new AccountsDetailsVO();
		cashBank.setNDebitAmount(effectivereceiptAmt);
		cashBank.setAccountName(receiptVO.getBankCashAcc());
		cashBank.setSubLedgerCode("None");
		cashBank.setDebitAmount(effectivereceiptAmt);
		cashBank.setNCreditAmount(BigDecimal.ZERO);
		cashBank.setCreditAmount(BigDecimal.ZERO);
		cashBank.setArapFlag(false);
		cashBank.setArapAmount(BigDecimal.ZERO);
		cashBank.setBDebitAmount(effectivereceiptAmt);
		cashBank.setBCrAmount(BigDecimal.ZERO);
		cashBank.setBArapAmount(BigDecimal.ZERO);
		cashBank.setACurrency(receiptVO.getCurrency());
		cashBank.setSubledgerName("None");
		cashBank.setNArapAmount(BigDecimal.ZERO);
		cashBank.setGstflag(3);
		cashBank.setAccountsVO(accountsVO);
		accountsDetailsVOs.add(cashBank);

		// Set totals
		accountsVO.setTotalDebitAmount(effectiveNetAmount.add(receiptVO.getTdsAmt()));
		accountsVO.setTotalCreditAmount(effectivereceiptAmt.add(receiptVO.getTdsAmt()));
		accountsVO.setAccountsDetailsVO(accountsDetailsVOs);

		Set<Object[]> tdsLedgers = costInvoiceRepo.getTdsLedgerFromAccountReceivable(receiptVO.getOrgId());
		for (Object[] ledger : tdsLedgers) {
			AccountsDetailsVO tdsDetail = new AccountsDetailsVO();
			tdsDetail.setAccountName(ledger[0].toString());
			tdsDetail.setACategory(ledger[1].toString());
			tdsDetail.setDebitAmount(receiptVO.getTdsAmt());
			tdsDetail.setCreditAmount(BigDecimal.ZERO);
			tdsDetail.setNDebitAmount(receiptVO.getTdsAmt());
			tdsDetail.setNCreditAmount(BigDecimal.ZERO);
			tdsDetail.setBDebitAmount(receiptVO.getTdsAmt());
			tdsDetail.setBCrAmount(BigDecimal.ZERO);
			tdsDetail.setArapAmount(BigDecimal.ZERO);
			tdsDetail.setBArapAmount(BigDecimal.ZERO);
			tdsDetail.setNArapAmount(BigDecimal.ZERO);
			tdsDetail.setArapFlag(false);
			tdsDetail.setACurrency(receiptVO.getCurrency());
//		            tdsDetail.setAExRate(receiptVO.getExRate());
			tdsDetail.setSubledgerName("None");
			tdsDetail.setSubLedgerCode("None");
			tdsDetail.setGstflag(3);
			tdsDetail.setAccountsVO(accountsVO);
			accountsDetailsVOs.add(tdsDetail);

		}

		// Set totals
//		accountsVO.setTotalDebitAmount(effectiveNetAmount);
//		accountsVO.setTotalCreditAmount(receiptAmt);
		accountsVO.setAccountsDetailsVO(accountsDetailsVOs);

		AccountsVO savedAccountsVO = accountsRepo.save(accountsVO);

		// Create ARAP entry
		AccountsDetailsVO accountsDetailsVOs2 = accountsDetailsRepo.findByAccountsVOAndGstflag(savedAccountsVO, 1);
		ArapDetailsVO arapDetailsVO = new ArapDetailsVO();
		arapDetailsVO.setSourceTransid(accountsDetailsVOs2.getId());
		arapDetailsVO.setCreatedBy(savedAccountsVO.getCreatedBy());
		arapDetailsVO.setUpdatedBy(savedAccountsVO.getModifiedBy());
		arapDetailsVO.setBranch(savedAccountsVO.getBranch());
		arapDetailsVO.setBranchCode(savedAccountsVO.getBranchCode());
		arapDetailsVO.setFinYear(savedAccountsVO.getFinYear());
		arapDetailsVO.setRefNo(savedAccountsVO.getRefNo());
		arapDetailsVO.setOrgId(savedAccountsVO.getOrgId());
		arapDetailsVO.setRefDate(savedAccountsVO.getRefDate());
		arapDetailsVO.setSubLedgerCode(accountsDetailsVOs2.getSubLedgerCode());
		arapDetailsVO.setCurrency(accountsDetailsVOs2.getACurrency());
		arapDetailsVO.setExRate(accountsDetailsVOs2.getAExRate());
		arapDetailsVO.setAmount(accountsDetailsVOs2.getArapAmount());
		arapDetailsVO.setBaseAmt(accountsDetailsVOs2.getArapAmount());
		arapDetailsVO.setDueDate(savedAccountsVO.getDueDate());
		arapDetailsVO.setCreditDays(savedAccountsVO.getCreditDays());
		arapDetailsVO.setDocId(savedAccountsVO.getDocId());
		arapDetailsVO.setDocDate(savedAccountsVO.getDocDate());
		arapDetailsVO.setAccCurrency(savedAccountsVO.getCurrency());
		arapDetailsVO.setExRate(savedAccountsVO.getExRate());
		arapDetailsVO.setAccName(accountsDetailsVOs2.getAccountName());
		arapDetailsVO.setGstFlag(accountsDetailsVOs2.getGstflag());
		arapDetailsVO.setActive(savedAccountsVO.isActive());
		arapDetailsVO.setSubLedgerName(accountsDetailsVOs2.getSubledgerName());
		arapDetailsVO.setSalesType(savedAccountsVO.getSalesType());
		arapDetailsVO.setNativeAmt(accountsDetailsVOs2.getArapAmount());

		arapDetailsRepo.save(arapDetailsVO);

		receiptVO.setApproveStatus(action);
		receiptVO.setApproveBy(actionBy);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
		receiptVO.setApproveOn(LocalDateTime.now().format(formatter).toUpperCase());

		return receiptRepo.save(receiptVO);
	}

	// Report

	@Override
	public List<Map<String, Object>> getReceiptDetails(Long orgId, String finYear, String partyname, String fromDate,
			String toDate,String branchCode) {
		Set<Object[]> chType = receiptRepo.getReceiptDetails(orgId, finYear, partyname, fromDate, toDate,branchCode);
		return getReceiptDetails(chType);
	}

	private List<Map<String, Object>> getReceiptDetails(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();
			map.put("finyear", ch[0] != null ? ch[0].toString() : "");
			map.put("invoiceNo", ch[1] != null ? ch[1].toString() : ""); // 1
			map.put("invoiceDate", ch[2] != null ? ch[2].toString() : ""); // 2
			map.put("docid", ch[3] != null ? ch[3].toString() : ""); // 3
			map.put("docdate", ch[4] != null ? ch[4].toString() : ""); // 4
			map.put("refNo", ch[5] != null ? ch[5].toString() : ""); // 5
			map.put("refDate", ch[6] != null ? ch[6].toString() : ""); // 6
			map.put("customerName", ch[7] != null ? ch[7].toString() : ""); // 7
			map.put("customerCode", ch[8] != null ? ch[8].toString() : ""); // 8
			map.put("receiptAmount", ch[9] != null ? new BigDecimal(ch[9].toString()) : BigDecimal.ZERO); // 9
			map.put("netAmount", ch[10] != null ? new BigDecimal(ch[10].toString()) : BigDecimal.ZERO); // 10
			map.put("onAccount", ch[11] != null ? new BigDecimal(ch[11].toString()) : BigDecimal.ZERO); // 11
			map.put("chequeNo", ch[12] != null ? ch[12].toString() : ""); // 12
			map.put("chequeDate", ch[13] != null ? ch[13].toString() : ""); // 13
			map.put("tdsAmount", ch[14] != null ? new BigDecimal(ch[14].toString()) : BigDecimal.ZERO); // 14
			map.put("amount", ch[15] != null ? new BigDecimal(ch[15].toString()) : BigDecimal.ZERO); // 15
			map.put("gstAmount", ch[16] != null ? new BigDecimal(ch[16].toString()) : BigDecimal.ZERO); // 16
			map.put("chargeamount", ch[17] != null ? new BigDecimal(ch[17].toString()) : BigDecimal.ZERO); // 17
			map.put("tdsPercentage", ch[18] != null ? new BigDecimal(ch[18].toString()) : BigDecimal.ZERO); // 18
			map.put("settledAmount", ch[19] != null ? new BigDecimal(ch[19].toString()) : BigDecimal.ZERO); // 19
			map.put("outStanding", ch[20] != null ? new BigDecimal(ch[20].toString()) : BigDecimal.ZERO); // 20
			List1.add(map);
		}
		return List1;
	}

	@Override
	public List<Map<String, Object>> getReceiptSummary(Long orgId, String finYear, String partyname, String fromDate,
			String toDate,String branchCode) {
		Set<Object[]> chType = receiptRepo.getReceiptSummary(orgId, finYear, partyname, fromDate, toDate,branchCode);
		return getReceiptSummary(chType);
	}

	private List<Map<String, Object>> getReceiptSummary(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();
			map.put("finyear", ch[0] != null ? ch[0].toString() : "");
			map.put("docId", ch[1] != null ? ch[1].toString() : "");
			map.put("docDate", ch[2] != null ? ch[2].toString() : "");
			map.put("customerName", ch[3] != null ? ch[3].toString() : "");
			map.put("customerCode", ch[4] != null ? ch[4].toString() : "");
			map.put("chequeNo", ch[5] != null ? ch[5].toString() : "");
			map.put("chequeDate", ch[6] != null ? ch[6].toString() : "");
			map.put("receiptAmount", ch[7] != null ? new BigDecimal(ch[7].toString()) : BigDecimal.ZERO);
			map.put("netAmount", ch[8] != null ? new BigDecimal(ch[8].toString()) : BigDecimal.ZERO);
			map.put("tdsAmount", ch[9] != null ? new BigDecimal(ch[9].toString()) : BigDecimal.ZERO);
			map.put("onAccount", ch[10] != null ? new BigDecimal(ch[10].toString()) : BigDecimal.ZERO);
			map.put("bankAccount", ch[11] != null ? ch[11].toString() : "");
			List1.add(map);
		}
		return List1;
	}
	
	
	@Override
	public ReceiptVO getReceiptByDocIdAndScreenCode(String docId) {
		
	  return receiptRepo.getReceiptByDocId(docId);
	
	}

}
