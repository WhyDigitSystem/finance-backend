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
import com.base.basesetup.entity.PaymentVO;
import com.base.basesetup.entity.ReceiptInvDetailsVO;
import com.base.basesetup.entity.ReceiptVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.AccountsDetailsRepo;
import com.base.basesetup.repo.AccountsRepo;
import com.base.basesetup.repo.ArBillBalanceRepo;
import com.base.basesetup.repo.ArapAdjustmentsRepo;
import com.base.basesetup.repo.ArapDetailsRepo;
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
					        docId, invNo, savedReceiptVO.getOrgId(), partyCode
					    );
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
				arapadjustments.setSubLedgerName(savedReceiptVO.getCustomerName());

				arapadjustments.setBaseAmt(savedReceiptInvDetails.getSettled());

				PartyMasterVO partyMaster = partyMasterRepo.findByPartyCode(savedReceiptVO.getCustomerCode());

				arapadjustments.setAccountName(partyMaster.getAccountType());
				System.out.println("ACCOUNT TYPE : " + partyMaster.getAccountType());
				arapAdjustmentsRepo.save(arapadjustments);

				
			    ArapAdjustmentsVO existingReverse = arapAdjustmentsRepo.findByDocIdAndRefNoAndOrgIdAndSubledgerCode(
				        invNo,  docId, savedReceiptVO.getOrgId(), partyCode
				    );
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
				negativeArapAdjustments.setBaseAmt(savedReceiptInvDetails.getSettled()); // Negative value
				negativeArapAdjustments.setNativeAmt(savedReceiptInvDetails.getSettled()); // Negative value
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
				
//				AccountsVO accountsVO = new AccountsVO();
//				accountsVO.setDocId(savedReceiptVO.getDocId());
//				accountsVO.setSourceScreen(savedReceiptVO.getScreenName());
//				accountsVO.setSourceId(savedReceiptVO.getId());
//				accountsVO.setCreatedBy(savedReceiptVO.getCreatedBy());
//				accountsVO.setModifiedBy(savedReceiptVO.getUpdatedBy());
//				accountsVO.setOrgId(savedReceiptVO.getOrgId());
//				accountsVO.setBranch(savedReceiptVO.getBranch());
//				accountsVO.setBranchCode(savedReceiptVO.getBranchCode());
//				accountsVO.setRefNo(savedReceiptVO.getDocId());
//				accountsVO.setRefDate(savedReceiptVO.getDocDate());
////				accountsVO.setVId(savedReceiptVO.getVId());
////				accountsVO.setVDate(savedReceiptVO.getVDate());
//				accountsVO.setCurrency(savedReceiptInvDetails.getCurrency());
//				accountsVO.setExRate(savedReceiptInvDetails.getExRate());
//				accountsVO.setRemarks(savedReceiptVO.getRemarks());
//				accountsVO.setFinYear(savedReceiptVO.getFinYear());
//	
//
//				accountsVO.setTotalDebitAmount(savedReceiptVO.getReceiptAmt());
//				accountsVO.setTotalCreditAmount(savedReceiptVO.getReceiptAmt());
////				accountsVO.setCreditDays(taxInvoiceVO.getCreditDays());
////				accountsVO.setAmountInWords(savedReceiptVO.getAmountInWords());
////				accountsVO.setStTaxAmount(taxInvoiceVO.getTotalTaxableAmountLc());
////				accountsVO.setChargeableAmount(taxInvoiceVO.getTotalChargeAmountLc());
//	
//				// Create AccountsDetailsVO list and populate it
//				List<AccountsDetailsVO> accountsDetailsVOs = new ArrayList<>();
//
//				// RECEIVABLE A/C entry (Credit)
//				AccountsDetailsVO accountsDetailsVO = new AccountsDetailsVO();
//				accountsDetailsVO.setNDebitAmount(BigDecimal.ZERO);
//				accountsDetailsVO.setACategory("RECEIVABLE A/C");
//				accountsDetailsVO.setAccountName("RECEIVABLE A/C");
//				accountsDetailsVO.setSubLedgerCode(savedReceiptVO.getCustomerCode());
//				accountsDetailsVO.setDebitAmount(BigDecimal.ZERO);
//				accountsDetailsVO.setNCreditAmount(savedReceiptVO.getReceiptAmt());
//				accountsDetailsVO.setCreditAmount(savedReceiptVO.getReceiptAmt());
//				accountsDetailsVO.setArapFlag(true);
//				accountsDetailsVO.setArapAmount(savedReceiptVO.getReceiptAmt().multiply(BigDecimal.valueOf(-1)));
//				accountsDetailsVO.setBDebitAmount(BigDecimal.ZERO);
//				accountsDetailsVO.setBCrAmount(savedReceiptVO.getReceiptAmt());
//				accountsDetailsVO.setBArapAmount(savedReceiptVO.getReceiptAmt().multiply(BigDecimal.valueOf(-1)));
//				accountsDetailsVO.setACurrency(savedReceiptVO.getCurrency());
//				accountsDetailsVO.setSubledgerName(savedReceiptVO.getCustomerName());
//				accountsDetailsVO.setNArapAmount(savedReceiptVO.getReceiptAmt().multiply(BigDecimal.valueOf(-1)));
//				accountsDetailsVO.setGstflag(1);
//				accountsDetailsVO.setAccountsVO(accountsVO);
//				accountsDetailsVOs.add(accountsDetailsVO);
//
//				// BANK/CASH entry (Debit)
//				AccountsDetailsVO accountsDetailsVO1 = new AccountsDetailsVO();
//				accountsDetailsVO1.setNDebitAmount(savedReceiptVO.getReceiptAmt());
//				accountsDetailsVO1.setAccountName(savedReceiptVO.getBankCashAcc());
//				accountsDetailsVO1.setSubLedgerCode("None");
//				accountsDetailsVO1.setDebitAmount(savedReceiptVO.getReceiptAmt());
//				accountsDetailsVO1.setNCreditAmount(BigDecimal.ZERO);
//				accountsDetailsVO1.setCreditAmount(BigDecimal.ZERO);
//				accountsDetailsVO1.setArapFlag(false);
//				accountsDetailsVO1.setArapAmount(BigDecimal.ZERO);
//				accountsDetailsVO1.setBDebitAmount(savedReceiptVO.getReceiptAmt());
//				accountsDetailsVO1.setBCrAmount(BigDecimal.ZERO);
//				accountsDetailsVO1.setBArapAmount(BigDecimal.ZERO);
//				accountsDetailsVO1.setACurrency(savedReceiptVO.getCurrency());
//				accountsDetailsVO1.setSubledgerName("None");
//				accountsDetailsVO1.setNArapAmount(BigDecimal.ZERO);
//				accountsDetailsVO1.setGstflag(3);
//				accountsDetailsVO1.setAccountsVO(accountsVO);
//				accountsDetailsVOs.add(accountsDetailsVO1); 
//			
//				accountsVO.setAccountsDetailsVO(accountsDetailsVOs);
//
//
//				// Save AccountsVO and update TaxInvoiceVO
//				AccountsVO savedAccountsVO = accountsRepo.save(accountsVO);
//				int gstflag = 1;
//
//				AccountsDetailsVO accountsDetailsVOs2 = accountsDetailsRepo.findByAccountsVOAndGstflag(savedAccountsVO,
//						gstflag);
//				ArapDetailsVO arapDetailsVO = new ArapDetailsVO();
//				arapDetailsVO.setSourceTransid(accountsDetailsVOs2.getId());
//				arapDetailsVO.setCreatedBy(savedAccountsVO.getCreatedBy());
//				arapDetailsVO.setUpdatedBy(savedAccountsVO.getModifiedBy());
//				arapDetailsVO.setBranch(savedAccountsVO.getBranch());
//				arapDetailsVO.setBranchCode(savedAccountsVO.getBranchCode());
//				arapDetailsVO.setFinYear(savedAccountsVO.getFinYear());
//				arapDetailsVO.setRefNo(savedAccountsVO.getRefNo());
//				arapDetailsVO.setOrgId(savedAccountsVO.getOrgId());
//				arapDetailsVO.setRefDate(savedAccountsVO.getRefDate());
//				arapDetailsVO.setSubLedgerCode(accountsDetailsVOs2.getSubLedgerCode());
//				arapDetailsVO.setCurrency(accountsDetailsVOs2.getACurrency());
//				arapDetailsVO.setExRate(accountsDetailsVOs2.getAExRate());
//				arapDetailsVO.setAmount(accountsDetailsVOs2.getArapAmount());
//				arapDetailsVO.setBaseAmt(accountsDetailsVOs2.getArapAmount());
//				arapDetailsVO.setDueDate(savedAccountsVO.getDueDate());
//				arapDetailsVO.setCreditDays(savedAccountsVO.getCreditDays());
//				arapDetailsVO.setDocId(savedAccountsVO.getDocId());
//				arapDetailsVO.setDocDate(savedAccountsVO.getDocDate());
//				arapDetailsVO.setAccCurrency(savedAccountsVO.getCurrency());
//				arapDetailsVO.setExRate(savedAccountsVO.getExRate());
//				arapDetailsVO.setAccName(accountsDetailsVOs2.getAccountName());
//				arapDetailsVO.setGstFlag(accountsDetailsVOs2.getGstflag());
//				arapDetailsVO.setSubLedgerName(accountsDetailsVOs2.getSubledgerName());
//				arapDetailsVO.setSalesType(savedAccountsVO.getSalesType());
//				arapDetailsVO.setNativeAmt(accountsDetailsVOs2.getArapAmount());
//				arapDetailsRepo.save(arapDetailsVO);
//				
//			}
//		}

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
		receiptVO.setRemarks(receiptDTO.getRemarks());

		if (ObjectUtils.isNotEmpty(receiptVO.getId())) {
			List<ReceiptInvDetailsVO> receiptInvDetailsVO1 = receiptInvDetailsRepo.findByReceiptVO(receiptVO);
			receiptInvDetailsRepo.deleteAll(receiptInvDetailsVO1);
		}

		BigDecimal netAmount = BigDecimal.ZERO;
		BigDecimal onAccount = BigDecimal.ZERO;

		List<ReceiptInvDetailsVO> receiptInvDetailsVOs = new ArrayList<>();
		BigDecimal totalSettled = BigDecimal.ZERO;

		List<ReceiptInvDetailsDTO> receiptDetailsList = receiptDTO.getReceiptInvDetailaDTO();
		BigDecimal receiptAmount = receiptDTO.getReceiptAmt(); // Assign receipt amount

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
				receiptInvDetailsVO.setChargeAmt(receiptInvDetailsDTO.getChargeAmt());
				receiptInvDetailsVO.setOutstanding(receiptInvDetailsDTO.getOutstanding());
				receiptInvDetailsVO.setTds(receiptInvDetailsDTO.getTds());
				receiptInvDetailsVO.setGstAmt(receiptInvDetailsDTO.getGstAmt());
				BigDecimal paymentAmt = receiptDTO.getReceiptAmt();

				receiptInvDetailsVO.setAmount(receiptInvDetailsDTO.getAmount());

				// Calculate netAmount (sum of settled amounts)
				netAmount = receiptDTO.getReceiptInvDetailaDTO().stream().map(ReceiptInvDetailsDTO::getSettled)
						.reduce(BigDecimal.ZERO, BigDecimal::add);
				totalSettled = totalSettled.add(receiptInvDetailsDTO.getSettled());

				// Calculate onAccount (the difference between paymentAmt and settled amounts)
				onAccount = paymentAmt.subtract(totalSettled);

				receiptInvDetailsVO.setSettled(receiptInvDetailsDTO.getSettled());
				receiptInvDetailsVO.setRecExRate(receiptInvDetailsDTO.getRecExRate());
				receiptInvDetailsVO.setTxnSettled(receiptInvDetailsDTO.getTxnSettled());
				receiptInvDetailsVO.setGainAmt(receiptInvDetailsDTO.getGainAmt());
				receiptInvDetailsVO.setAmount(receiptInvDetailsDTO.getAmount());
				receiptInvDetailsVO.setReceiptVO(receiptVO);
				receiptInvDetailsVOs.add(receiptInvDetailsVO);

			}

			receiptVO.setReceiptInvDetailsVO(receiptInvDetailsVOs);


			if (netAmount.compareTo(receiptDTO.getReceiptAmt()) > 0) {

				throw new ApplicationException("Total Settled Amount should not be greater than Receipt Amount");
			}

			onAccount = receiptDTO.getReceiptAmt().subtract(netAmount);
			receiptVO.setNetAmount(netAmount);
			receiptVO.setOnAccount(onAccount);
		} else {
			receiptVO.setOnAccount(receiptDTO.getReceiptAmt());
		}

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

	    if (receiptVO == null) {
	        throw new ApplicationException("Payment not found for the given details.");
	    }

	    if ("Approved".equalsIgnoreCase(receiptVO.getApproveStatus())) {
	        throw new ApplicationException("This Payment Already Approved");
	    } else if ("Rejected".equals(receiptVO.getApproveStatus())) {
	        throw new ApplicationException("This Payment Already Rejected");
	    }

	    if (!"SUBMIT".equalsIgnoreCase(receiptVO.getStatus())) {
	        throw new ApplicationException("Only SETTLED payments can be approved or rejected.");
	    }

	    if (receiptVO.getApproveStatus() == null || 
	        (!"Approved".equals(receiptVO.getApproveStatus()) && !"Rejected".equals(receiptVO.getApproveStatus()))) {

	    	String screenCode1 = "AC";
			String sourceScreenCode = receiptVO.getScreenCode();

			String accountsDocId = accountsRepo.getApproveDocId(receiptVO.getOrgId(),receiptVO.getFinYear(),receiptVO.getBranchCode(),sourceScreenCode,screenCode1);

			MultipleDocIdGenerationDetailsVO multipleDocIdGenerationDetailsVO = multipleDocIdGenerationDetailsRepo
			        .findByOrgIdAndFinYearAndBranchCodeAndSourceScreenCodeAndScreenCode(receiptVO.getOrgId(),receiptVO.getFinYear(),receiptVO.getBranchCode(),sourceScreenCode,
			                screenCode1
			        );
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
//			accountsVO.setVId(savedReceiptVO.getVId());
//			accountsVO.setVDate(savedReceiptVO.getVDate());
			accountsVO.setCurrency(receiptVO.getCurrency());
			accountsVO.setExRate(BigDecimal.ONE);
			accountsVO.setRemarks(receiptVO.getRemarks());
			accountsVO.setFinYear(receiptVO.getFinYear());


			accountsVO.setTotalDebitAmount(receiptVO.getReceiptAmt());
			accountsVO.setTotalCreditAmount(receiptVO.getReceiptAmt());
//			accountsVO.setCreditDays(taxInvoiceVO.getCreditDays());
//			accountsVO.setAmountInWords(savedReceiptVO.getAmountInWords());
//			accountsVO.setStTaxAmount(taxInvoiceVO.getTotalTaxableAmountLc());
//			accountsVO.setChargeableAmount(taxInvoiceVO.getTotalChargeAmountLc());

			// Create AccountsDetailsVO list and populate it
			List<AccountsDetailsVO> accountsDetailsVOs = new ArrayList<>();

			// RECEIVABLE A/C entry (Credit)
			AccountsDetailsVO accountsDetailsVO = new AccountsDetailsVO();
			accountsDetailsVO.setNDebitAmount(BigDecimal.ZERO);
			accountsDetailsVO.setACategory("RECEIVABLE A/C");
			accountsDetailsVO.setAccountName("RECEIVABLE A/C");
			accountsDetailsVO.setSubLedgerCode(receiptVO.getCustomerCode());
			accountsDetailsVO.setDebitAmount(BigDecimal.ZERO);
			accountsDetailsVO.setNCreditAmount(receiptVO.getReceiptAmt());
			accountsDetailsVO.setCreditAmount(receiptVO.getReceiptAmt());
			accountsDetailsVO.setArapFlag(true);
			accountsDetailsVO.setArapAmount(receiptVO.getReceiptAmt().multiply(BigDecimal.valueOf(-1)));
			accountsDetailsVO.setBDebitAmount(BigDecimal.ZERO);
			accountsDetailsVO.setBCrAmount(receiptVO.getReceiptAmt());
			accountsDetailsVO.setBArapAmount(receiptVO.getReceiptAmt().multiply(BigDecimal.valueOf(-1)));
			accountsDetailsVO.setACurrency(receiptVO.getCurrency());
			accountsDetailsVO.setSubledgerName(receiptVO.getCustomerName());
			accountsDetailsVO.setNArapAmount(receiptVO.getReceiptAmt().multiply(BigDecimal.valueOf(-1)));
			accountsDetailsVO.setGstflag(1);
			accountsDetailsVO.setAccountsVO(accountsVO);
			accountsDetailsVOs.add(accountsDetailsVO);

			// BANK/CASH entry (Debit)
			AccountsDetailsVO accountsDetailsVO1 = new AccountsDetailsVO();
			accountsDetailsVO1.setNDebitAmount(receiptVO.getReceiptAmt());
			accountsDetailsVO1.setAccountName(receiptVO.getBankCashAcc());
			accountsDetailsVO1.setSubLedgerCode("None");
			accountsDetailsVO1.setDebitAmount(receiptVO.getReceiptAmt());
			accountsDetailsVO1.setNCreditAmount(BigDecimal.ZERO);
			accountsDetailsVO1.setCreditAmount(BigDecimal.ZERO);
			accountsDetailsVO1.setArapFlag(false);
			accountsDetailsVO1.setArapAmount(BigDecimal.ZERO);
			accountsDetailsVO1.setBDebitAmount(receiptVO.getReceiptAmt());
			accountsDetailsVO1.setBCrAmount(BigDecimal.ZERO);
			accountsDetailsVO1.setBArapAmount(BigDecimal.ZERO);
			accountsDetailsVO1.setACurrency(receiptVO.getCurrency());
			accountsDetailsVO1.setSubledgerName("None");
			accountsDetailsVO1.setNArapAmount(BigDecimal.ZERO);
			accountsDetailsVO1.setGstflag(3);
			accountsDetailsVO1.setAccountsVO(accountsVO);
			accountsDetailsVOs.add(accountsDetailsVO1); 
		
			accountsVO.setAccountsDetailsVO(accountsDetailsVOs);


			// Save AccountsVO and update TaxInvoiceVO
			AccountsVO savedAccountsVO = accountsRepo.save(accountsVO);
			int gstflag = 1;

			AccountsDetailsVO accountsDetailsVOs2 = accountsDetailsRepo.findByAccountsVOAndGstflag(savedAccountsVO,
					gstflag);
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
			arapDetailsVO.setSubLedgerName(accountsDetailsVOs2.getSubledgerName());
			arapDetailsVO.setSalesType(savedAccountsVO.getSalesType());
			arapDetailsVO.setNativeAmt(accountsDetailsVOs2.getArapAmount());
			arapDetailsRepo.save(arapDetailsVO);
	    	
	    	
	    	
	    	
	        // Update approval status
			receiptVO.setApproveStatus(action);
			receiptVO.setApproveBy(actionBy);
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
	        receiptVO.setApproveOn(LocalDateTime.now().format(formatter).toUpperCase());

	        return receiptRepo.save(receiptVO);
	    }

	    return receiptVO;

	}
}
