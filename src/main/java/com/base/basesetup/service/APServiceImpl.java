package com.base.basesetup.service;

import java.math.BigDecimal;
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
			ArapAdjustmentsVO adjustmentsVO = new ArapAdjustmentsVO();
			adjustmentsVO.setCancel(false);
			adjustmentsVO.setActive(true);
			adjustmentsVO.setCreatedBy(paymentVO.getCreatedBy());
			adjustmentsVO.setUpdatedBy(paymentVO.getUpdatedBy());
			adjustmentsVO.setFinYear(paymentVO.getFinYear());
			adjustmentsVO.setDocId(paymentVO.getDocId());
			adjustmentsVO.setDocDate(paymentVO.getDocDate());
			adjustmentsVO.setRefNo(dtlsVO.getInvNo());
			adjustmentsVO.setBranchCode(paymentVO.getBranchCode());
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
			adjustmentsVO.setBranch(paymentVO.getBranch());
			adjustmentsVO.setSourceId(dtlsVO.getId());

			PartyMasterVO masterVO = partyMasterRepo.findByPartyCode(paymentVO.getPartyCode());
			adjustmentsVO.setAccountName(masterVO.getAccountType());

			arapAdjustmentsRepo.save(adjustmentsVO);

			ArapAdjustmentsVO negativeAdjustmentsVO = new ArapAdjustmentsVO();
			negativeAdjustmentsVO.setCancel(false);
			negativeAdjustmentsVO.setActive(true);
			negativeAdjustmentsVO.setBranchCode(paymentVO.getBranchCode());
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
			
			AccountsVO accountsVO = new AccountsVO();
			accountsVO.setDocId(paymentVO.getDocId());
			accountsVO.setSourceScreen(paymentVO.getScreenName());
			accountsVO.setSourceId(paymentVO.getId());
			accountsVO.setCreatedBy(paymentVO.getCreatedBy());
			accountsVO.setModifiedBy(paymentVO.getUpdatedBy());
			accountsVO.setOrgId(paymentVO.getOrgId());
			accountsVO.setBranch(paymentVO.getBranch());
			accountsVO.setBranchCode(paymentVO.getBranchCode());
			accountsVO.setRefNo(paymentVO.getDocId());
			accountsVO.setRefDate(paymentVO.getDocDate());
//			accountsVO.setVId(savedReceiptVO.getVId());
//			accountsVO.setVDate(savedReceiptVO.getVDate());
			accountsVO.setCurrency(paymentVO.getCurrency());
			accountsVO.setExRate(dtlsVO.getExRate());
			accountsVO.setRemarks(paymentVO.getCancelRemarks());
			accountsVO.setFinYear(paymentVO.getFinYear());


			accountsVO.setTotalDebitAmount(paymentVO.getPaymentAmt());
			accountsVO.setTotalCreditAmount(paymentVO.getPaymentAmt());
//			accountsVO.setCreditDays(taxInvoiceVO.getCreditDays());
//			accountsVO.setAmountInWords(savedReceiptVO.getAmountInWords());
//			accountsVO.setStTaxAmount(taxInvoiceVO.getTotalTaxableAmountLc());
//			accountsVO.setChargeableAmount(taxInvoiceVO.getTotalChargeAmountLc());

			// Create AccountsDetailsVO list and populate it
			List<AccountsDetailsVO> accountsDetailsVOs = new ArrayList<>();

			// RECEIVABLE A/C entry (Credit)
			AccountsDetailsVO accountsDetailsVO = new AccountsDetailsVO();
			accountsDetailsVO.setNDebitAmount(paymentVO.getPaymentAmt());
			accountsDetailsVO.setACategory("PAYABLE A/C");
			accountsDetailsVO.setAccountName("PAYABLE A/C");
			accountsDetailsVO.setSubLedgerCode(paymentVO.getPartyCode());
			accountsDetailsVO.setDebitAmount(paymentVO.getPaymentAmt());
			accountsDetailsVO.setNCreditAmount(BigDecimal.ZERO);
			accountsDetailsVO.setCreditAmount(BigDecimal.ZERO);
			accountsDetailsVO.setArapFlag(true);
			accountsDetailsVO.setArapAmount(paymentVO.getPaymentAmt().multiply(BigDecimal.valueOf(-1)));
			accountsDetailsVO.setBDebitAmount(paymentVO.getPaymentAmt());
			accountsDetailsVO.setBCrAmount(BigDecimal.ZERO);
			accountsDetailsVO.setBArapAmount(paymentVO.getPaymentAmt().multiply(BigDecimal.valueOf(-1)));
			accountsDetailsVO.setACurrency(paymentVO.getCurrency());
			accountsDetailsVO.setSubledgerName(paymentVO.getPartyName());
			accountsDetailsVO.setNArapAmount(paymentVO.getPaymentAmt().multiply(BigDecimal.valueOf(-1)));
			accountsDetailsVO.setGstflag(2);
			accountsDetailsVO.setAccountsVO(accountsVO);
			accountsDetailsVOs.add(accountsDetailsVO);

			// BANK/CASH entry (Debit)
			AccountsDetailsVO accountsDetailsVO1 = new AccountsDetailsVO();
			accountsDetailsVO1.setNDebitAmount(BigDecimal.ZERO);
			accountsDetailsVO1.setAccountName(paymentVO.getBankCashAcc());
			accountsDetailsVO1.setSubLedgerCode("None");
			accountsDetailsVO1.setDebitAmount(BigDecimal.ZERO);
			accountsDetailsVO1.setNCreditAmount(paymentVO.getPaymentAmt());
			accountsDetailsVO1.setCreditAmount(paymentVO.getPaymentAmt());
			accountsDetailsVO1.setArapFlag(false);
			accountsDetailsVO1.setArapAmount(BigDecimal.ZERO);
			accountsDetailsVO1.setBDebitAmount(BigDecimal.ZERO);
			accountsDetailsVO1.setBCrAmount(paymentVO.getPaymentAmt());
			accountsDetailsVO1.setBArapAmount(BigDecimal.ZERO);
			accountsDetailsVO1.setACurrency(paymentVO.getCurrency());
			accountsDetailsVO1.setSubledgerName("None");
			accountsDetailsVO1.setNArapAmount(BigDecimal.ZERO);
			accountsDetailsVO1.setGstflag(3);
			accountsDetailsVO1.setAccountsVO(accountsVO);
			accountsDetailsVOs.add(accountsDetailsVO1); 
		
			accountsVO.setAccountsDetailsVO(accountsDetailsVOs);


			// Save AccountsVO and update TaxInvoiceVO
			AccountsVO savedAccountsVO = accountsRepo.save(accountsVO);
			int gstflag = 2;

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
		}
		Map<String, Object> response = new HashMap<>();
		response.put("paymentVO", paymentVO);
		response.put("message", message);
		return response;
	}

	private PaymentVO getPaymentVOFromPaymentDTO(PaymentDTO paymentDTO, PaymentVO paymentVO) throws ApplicationException {

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
				if (dto.getSettled().compareTo(dto.getAmount().add(dto.getGstAmount())) > 0) {
					throw new ApplicationException(
							"Settled amount (" + dto.getSettled() + ") cannot be greater than charge amount ("
									+ dto.getAmount() + ") for invoice: " + dto.getInvNo());
				}

				BigDecimal outstanding = dto.getOutStanding();
				vo.setOutstanding(outstanding);

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
	public List<PaymentVO> getAllPaymentByOrgIdAndBranchCode(Long orgId, String branchCode, String partyName) {

		return paymentRepo.getAllPaymentByOrgIdAndBranchCode(orgId,branchCode,partyName);
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
	
//	@Override
//	public PaymentVO approvePayment(Long orgId, Long id, String docId, String action, String actionBy)
//	        throws ApplicationException {
//
//	    PaymentVO paymentVO = paymentRepo.findByOrgIdAndIdAndDocId(orgId, id, docId);
//
//	    if (paymentVO == null) {
//	        throw new ApplicationException("Payment not found for the given details.");
//	    }
//
//	    if ("Approved".equalsIgnoreCase(paymentVO.getApproveStatus())) {
//	        throw new ApplicationException("This Payment Already Approved");
//	    } else if ("Rejected".equals(paymentVO.getApproveStatus())) {
//	        throw new ApplicationException("This Payment Already Rejected");
//	    }
//
//	    if (!"SETTLED".equals(paymentVO.getStatus())) {
//	        throw new ApplicationException("Only SETTLED payments can be approved or rejected.");
//	    }
//
//	    if (paymentVO.getApproveStatus() == null || 
//	        (!"Approved".equals(paymentVO.getApproveStatus()) && !"Rejected".equals(paymentVO.getApproveStatus()))) {
//
////	        List<PaymentInvDtlsVO> paymentInvDtlsVOs = paymentVO.getPaymentInvDtlsVO();
////
////	        for (PaymentInvDtlsVO dtlsVO : paymentInvDtlsVOs) {
////
////	            // Create positive adjustment
////	            ArapAdjustmentsVO adjustmentsVO = new ArapAdjustmentsVO();
////	            adjustmentsVO.setCancel(false);
////	            adjustmentsVO.setActive(true);
////	            adjustmentsVO.setCreatedBy(paymentVO.getCreatedBy());
////	            adjustmentsVO.setUpdatedBy(paymentVO.getUpdatedBy());
////	            adjustmentsVO.setFinYear(paymentVO.getFinYear());
////	            adjustmentsVO.setDocId(paymentVO.getDocId());
////	            adjustmentsVO.setDocDate(paymentVO.getDocDate());
////	            adjustmentsVO.setRefNo(dtlsVO.getInvNo());
////	            adjustmentsVO.setRefDate(dtlsVO.getInvDate());
////	            adjustmentsVO.setSubLedgerCode(paymentVO.getPartyCode());
////	            adjustmentsVO.setSubLedgerName(paymentVO.getPartyName());
////	            adjustmentsVO.setCurrency(dtlsVO.getCurrency());
////	            adjustmentsVO.setExRate(dtlsVO.getExRate());
////	            adjustmentsVO.setAmount(dtlsVO.getSettled());
////	            adjustmentsVO.setBaseAmt(dtlsVO.getSettled());
////	            adjustmentsVO.setNativeAmt(dtlsVO.getSettled());
////	            adjustmentsVO.setOrgId(paymentVO.getOrgId());
////	            adjustmentsVO.setAccCurrency(dtlsVO.getCurrency());
////	            adjustmentsVO.setBranch(paymentVO.getBranch());
////	            adjustmentsVO.setBranchCode(paymentVO.getBranchCode());
////	            adjustmentsVO.setSourceId(dtlsVO.getId());
////
////	            PartyMasterVO masterVO = partyMasterRepo.findByPartyCode(paymentVO.getPartyCode());
////	            adjustmentsVO.setAccountName(masterVO.getAccountType());
////
////	            arapAdjustmentsRepo.save(adjustmentsVO);
////
////	            // Create negative adjustment
////	            ArapAdjustmentsVO negativeAdjustmentsVO = new ArapAdjustmentsVO();
////	            negativeAdjustmentsVO.setCancel(false);
////	            negativeAdjustmentsVO.setActive(true);
////	            negativeAdjustmentsVO.setCreatedBy(paymentVO.getCreatedBy());
////	            negativeAdjustmentsVO.setUpdatedBy(paymentVO.getUpdatedBy());
////	            negativeAdjustmentsVO.setFinYear(paymentVO.getFinYear());
////	            negativeAdjustmentsVO.setDocId(dtlsVO.getInvNo());
////	            negativeAdjustmentsVO.setDocDate(dtlsVO.getInvDate());
////	            negativeAdjustmentsVO.setRefNo(paymentVO.getDocId());
////	            negativeAdjustmentsVO.setRefDate(paymentVO.getDocDate());
////	            negativeAdjustmentsVO.setSubLedgerCode(paymentVO.getPartyCode());
////	            negativeAdjustmentsVO.setSubLedgerName(paymentVO.getPartyName());
////	            negativeAdjustmentsVO.setCurrency(dtlsVO.getCurrency());
////	            negativeAdjustmentsVO.setExRate(dtlsVO.getExRate());
////	            negativeAdjustmentsVO.setAmount(dtlsVO.getSettled().negate());
////	            negativeAdjustmentsVO.setBaseAmt(dtlsVO.getSettled().negate());
////	            negativeAdjustmentsVO.setNativeAmt(dtlsVO.getSettled().negate());
////	            negativeAdjustmentsVO.setOrgId(paymentVO.getOrgId());
////	            negativeAdjustmentsVO.setAccCurrency(dtlsVO.getCurrency());
////	            negativeAdjustmentsVO.setAccountName(masterVO.getAccountType());
////	            negativeAdjustmentsVO.setBranch(paymentVO.getBranch());
////	            negativeAdjustmentsVO.setBranchCode(paymentVO.getBranchCode());
////	            negativeAdjustmentsVO.setSourceId(dtlsVO.getId());
////
////	            arapAdjustmentsRepo.save(negativeAdjustmentsVO);
////	        }
//
//	        // Create AccountsVO and related details
//	        AccountsVO accountsVO = new AccountsVO();
//	        accountsVO.setDocId(paymentVO.getDocId());
//	        accountsVO.setSourceScreen(paymentVO.getScreenName());
//	        accountsVO.setSourceId(paymentVO.getId());
//	        accountsVO.setCreatedBy(paymentVO.getCreatedBy());
//	        accountsVO.setModifiedBy(paymentVO.getUpdatedBy());
//	        accountsVO.setOrgId(paymentVO.getOrgId());
//	        accountsVO.setBranch(paymentVO.getBranch());
//	        accountsVO.setBranchCode(paymentVO.getBranchCode());
//	        accountsVO.setRefNo(paymentVO.getDocId());
//	        accountsVO.setRefDate(paymentVO.getDocDate());
//	        accountsVO.setCurrency(paymentVO.getCurrency());
//	        accountsVO.setExRate(BigDecimal.ONE); // Replace if necessary
//	        accountsVO.setRemarks(paymentVO.getCancelRemarks());
//	        accountsVO.setFinYear(paymentVO.getFinYear());
//	        accountsVO.setTotalDebitAmount(paymentVO.getPaymentAmt());
//	        accountsVO.setTotalCreditAmount(paymentVO.getPaymentAmt());
//
//	        List<AccountsDetailsVO> accountsDetailsVOs = new ArrayList<>();
//
//	        // Payable Entry
//	        AccountsDetailsVO payable = new AccountsDetailsVO();
//	        payable.setNDebitAmount(paymentVO.getPaymentAmt());
//	        payable.setDebitAmount(paymentVO.getPaymentAmt());
//	        payable.setNCreditAmount(BigDecimal.ZERO);
//	        payable.setCreditAmount(BigDecimal.ZERO);
//	        payable.setArapFlag(true);
//	        payable.setArapAmount(paymentVO.getPaymentAmt().negate());
//	        payable.setBDebitAmount(paymentVO.getPaymentAmt());
//	        payable.setBCrAmount(BigDecimal.ZERO);
//	        payable.setBArapAmount(paymentVO.getPaymentAmt().negate());
//	        payable.setACurrency(paymentVO.getCurrency());
//	        payable.setAccountName("PAYABLE A/C");
//	        payable.setACategory("PAYABLE A/C");
//	        payable.setSubLedgerCode(paymentVO.getPartyCode());
//	        payable.setSubledgerName(paymentVO.getPartyName());
//	        payable.setNArapAmount(paymentVO.getPaymentAmt().negate());
//	        payable.setGstflag(2);
//	        payable.setAccountsVO(accountsVO);
//
//	        accountsDetailsVOs.add(payable);
//
//	        // Bank/Cash Entry
//	        AccountsDetailsVO bank = new AccountsDetailsVO();
//	        bank.setNDebitAmount(BigDecimal.ZERO);
//	        bank.setDebitAmount(BigDecimal.ZERO);
//	        bank.setNCreditAmount(paymentVO.getPaymentAmt());
//	        bank.setCreditAmount(paymentVO.getPaymentAmt());
//	        bank.setArapFlag(false);
//	        bank.setArapAmount(BigDecimal.ZERO);
//	        bank.setBDebitAmount(BigDecimal.ZERO);
//	        bank.setBCrAmount(paymentVO.getPaymentAmt());
//	        bank.setBArapAmount(BigDecimal.ZERO);
//	        bank.setACurrency(paymentVO.getCurrency());
//	        bank.setAccountName(paymentVO.getBankCashAcc());
//	        bank.setSubLedgerCode("None");
//	        bank.setSubledgerName("None");
//	        bank.setNArapAmount(BigDecimal.ZERO);
//	        bank.setGstflag(3);
//	        bank.setAccountsVO(accountsVO);
//
//	        accountsDetailsVOs.add(bank);
//
//	        accountsVO.setAccountsDetailsVO(accountsDetailsVOs);
//
//	        // Save AccountsVO and get saved instance
//	        AccountsVO savedAccountsVO = accountsRepo.save(accountsVO);
//
//	        // Link to ARAP
//	        AccountsDetailsVO arapDetailsSrc = accountsDetailsRepo.findByAccountsVOAndGstflag(savedAccountsVO, 2);
//
//	        ArapDetailsVO arapDetailsVO = new ArapDetailsVO();
//	        arapDetailsVO.setSourceTransid(arapDetailsSrc.getId());
//	        arapDetailsVO.setCreatedBy(savedAccountsVO.getCreatedBy());
//	        arapDetailsVO.setUpdatedBy(savedAccountsVO.getModifiedBy());
//	        arapDetailsVO.setBranch(savedAccountsVO.getBranch());
//	        arapDetailsVO.setBranchCode(savedAccountsVO.getBranchCode());
//	        arapDetailsVO.setFinYear(savedAccountsVO.getFinYear());
//	        arapDetailsVO.setRefNo(savedAccountsVO.getRefNo());
//	        arapDetailsVO.setRefDate(savedAccountsVO.getRefDate());
//	        arapDetailsVO.setOrgId(savedAccountsVO.getOrgId());
//	        arapDetailsVO.setSubLedgerCode(arapDetailsSrc.getSubLedgerCode());
//	        arapDetailsVO.setCurrency(arapDetailsSrc.getACurrency());
//	        arapDetailsVO.setExRate(arapDetailsSrc.getAExRate());
//	        arapDetailsVO.setAmount(arapDetailsSrc.getArapAmount());
//	        arapDetailsVO.setBaseAmt(arapDetailsSrc.getArapAmount());
//	        arapDetailsVO.setNativeAmt(arapDetailsSrc.getArapAmount());
//	        arapDetailsVO.setDueDate(savedAccountsVO.getDueDate());
//	        arapDetailsVO.setCreditDays(savedAccountsVO.getCreditDays());
//	        arapDetailsVO.setDocId(savedAccountsVO.getDocId());
//	        arapDetailsVO.setDocDate(savedAccountsVO.getDocDate());
//	        arapDetailsVO.setAccCurrency(savedAccountsVO.getCurrency());
//	        arapDetailsVO.setAccName(arapDetailsSrc.getAccountName());
//	        arapDetailsVO.setGstFlag(arapDetailsSrc.getGstflag());
//	        arapDetailsVO.setActive(true);
//	        arapDetailsVO.setSubLedgerName(arapDetailsSrc.getSubledgerName());
//	        arapDetailsVO.setSalesType(savedAccountsVO.getSalesType());
//
//	        arapDetailsRepo.save(arapDetailsVO);
//
//	        // Update approval status
//	        paymentVO.setApproveStatus(action);
////	        paymentVO.setApproveBy(actionBy);
//	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
//	        paymentVO.setApproveOn(LocalDateTime.now().format(formatter).toUpperCase());
//
//	        return paymentRepo.save(paymentVO);
//	    }
//
//	    return paymentVO;
//	}


}

