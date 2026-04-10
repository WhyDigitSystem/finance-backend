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

import com.base.basesetup.dto.ApAdjustmentOffSetDTO;
import com.base.basesetup.dto.ApOffSetInvoiceDetailsDTO;
import com.base.basesetup.dto.ArAdjustmentOffSetDTO;
import com.base.basesetup.dto.ArOffSetInvoiceDetailsDTO;
import com.base.basesetup.entity.AccountsDetailsVO;
import com.base.basesetup.entity.AccountsVO;
import com.base.basesetup.entity.ApAdjustmentOffSetVO;
import com.base.basesetup.entity.ApOffSetInvoiceDetailsVO;
import com.base.basesetup.entity.ArAdjustmentOffSetVO;
import com.base.basesetup.entity.ArOffSetInvoiceDetailsVO;
import com.base.basesetup.entity.ArapAdjustmentsVO;
import com.base.basesetup.entity.ArapDetailsVO;
import com.base.basesetup.entity.DocumentTypeMappingDetailsVO;
import com.base.basesetup.entity.MultipleDocIdGenerationDetailsVO;
import com.base.basesetup.entity.PartyMasterVO;
import com.base.basesetup.entity.PaymentVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.AccountsDetailsRepo;
import com.base.basesetup.repo.AccountsRepo;
import com.base.basesetup.repo.ApAdjustmentOffSetRepo;
import com.base.basesetup.repo.ApOffSetInvoiceDetailsRepo;
import com.base.basesetup.repo.ArAdjustmentOffSetRepo;
import com.base.basesetup.repo.ArOffSetInvoiceDetailsRepo;
import com.base.basesetup.repo.ArapAdjustmentsRepo;
import com.base.basesetup.repo.ArapDetailsRepo;
import com.base.basesetup.repo.DocumentTypeMappingDetailsRepo;
import com.base.basesetup.repo.MultipleDocIdGenerationDetailsRepo;
import com.base.basesetup.repo.PartyMasterRepo;
import com.base.basesetup.repo.PaymentRepo;
import com.base.basesetup.repo.ReceiptRepo;

@Service
public class ArAdjustmentOffSetServiceImpl implements ArAdjustmentOffSetService {

	public static final Logger LOGGER = LoggerFactory.getLogger(ArAdjustmentOffSetServiceImpl.class);

	@Autowired
	ArAdjustmentOffSetRepo arAdjustmentOffSetRepo;

	@Autowired
	ArOffSetInvoiceDetailsRepo arOffSetInvoiceDetailsRepo;

	@Autowired
	ApAdjustmentOffSetRepo apAdjustmentOffSetRepo;

	@Autowired
	ReceiptRepo receiptRepo;

	@Autowired
	PaymentRepo paymentRepo;

	@Autowired
	DocumentTypeMappingDetailsRepo documentTypeMappingDetailsRepo;

	@Autowired
	ApOffSetInvoiceDetailsRepo apOffSetInvoiceDetailsRepo;

	@Autowired
	AccountsRepo accountsRepo;

	@Autowired
	AccountsDetailsRepo accountsDetailsRepo;

	@Autowired
	ArapDetailsRepo arapDetailsRepo;

	@Autowired
	MultipleDocIdGenerationDetailsRepo multipleDocIdGenerationDetailsRepo;

	@Autowired
	PartyMasterRepo partyMasterRepo;

	@Autowired
	ArapAdjustmentsRepo arapAdjustmentsRepo;

	@Override
	public List<ArAdjustmentOffSetVO> getAllArAdjustmentOffSetByOrgId(Long orgId) {
		List<ArAdjustmentOffSetVO> arAdjustmentOffSetVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(orgId)) {
			LOGGER.info("Successfully Received  ArAdjustmentOffSet BY OrgId: {}", orgId);
			arAdjustmentOffSetVO = arAdjustmentOffSetRepo.getAllArAdjustmentOffSetByOrgId(orgId);
		}
		return arAdjustmentOffSetVO;
	}

	@Override
	public List<ArAdjustmentOffSetVO> getArAdjustmentOffSetById(Long id) {
		List<ArAdjustmentOffSetVO> arAdjustmentOffSetVO = new ArrayList<>();

		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received ArAdjustmentOffSet BY Id : {}", id);
			arAdjustmentOffSetVO = arAdjustmentOffSetRepo.getArAdjustmentOffSetById(id);
		}
		return arAdjustmentOffSetVO;
	}

	@Override
	public Map<String, Object> updateCreateArAdjustmentOffSet(@Valid ArAdjustmentOffSetDTO arAdjustmentOffSetDTO)
			throws ApplicationException {
		String screenCode = "ARA";
		ArAdjustmentOffSetVO arAdjustmentOffSetVO = new ArAdjustmentOffSetVO();
		String message;
		if (ObjectUtils.isNotEmpty(arAdjustmentOffSetDTO.getId())) {
			arAdjustmentOffSetVO = arAdjustmentOffSetRepo.findById(arAdjustmentOffSetDTO.getId())
					.orElseThrow(() -> new ApplicationException("ArAdjustmentOffSet not found"));

			arAdjustmentOffSetVO.setUpdatedBy(arAdjustmentOffSetDTO.getCreatedBy());
			createUpdateArAdjustmentOffSetVOByArAdjustmentOffSetDTO(arAdjustmentOffSetDTO, arAdjustmentOffSetVO);
			message = "ArAdjustmentOffSet Updated Successfully";
		} else {
			// GETDOCID API
//			String docId = arAdjustmentOffSetRepo.getArAdjustmentOffSetByDocId(arAdjustmentOffSetDTO.getOrgId(),
//					arAdjustmentOffSetDTO.getFinYear(), arAdjustmentOffSetDTO.getBranchCode(), screenCode);
//
//			arAdjustmentOffSetVO.setDocId(docId);

			List<Object[]> taxInvoiceDoc = arAdjustmentOffSetRepo.getArAdjustmentOffSetByDocId(
					arAdjustmentOffSetDTO.getOrgId(), arAdjustmentOffSetDTO.getFinYear(),
					arAdjustmentOffSetDTO.getBranchCode(), screenCode);

			if (taxInvoiceDoc != null && !taxInvoiceDoc.isEmpty()) {

				Object[] row = taxInvoiceDoc.get(0);

				// ✅ Set docId
				arAdjustmentOffSetVO.setDocId((String) row[0]);

				// ✅ Convert java.sql.Date → LocalDate
				if (row[1] != null) {
					arAdjustmentOffSetVO.setDocDate(((java.sql.Date) row[1]).toLocalDate());
				}
			}

//						// GETDOCID LASTNO +1
			DocumentTypeMappingDetailsVO documentTypeMappingDetailsVO = documentTypeMappingDetailsRepo
					.findByOrgIdAndFinYearAndBranchCodeAndScreenCode(arAdjustmentOffSetDTO.getOrgId(),
							arAdjustmentOffSetDTO.getFinYear(), arAdjustmentOffSetDTO.getBranchCode(), screenCode);
			documentTypeMappingDetailsVO.setLastno(documentTypeMappingDetailsVO.getLastno() + 1);
			documentTypeMappingDetailsRepo.save(documentTypeMappingDetailsVO);

			arAdjustmentOffSetVO.setCreatedBy(arAdjustmentOffSetDTO.getCreatedBy());
			arAdjustmentOffSetVO.setUpdatedBy(arAdjustmentOffSetDTO.getCreatedBy());
			createUpdateArAdjustmentOffSetVOByArAdjustmentOffSetDTO(arAdjustmentOffSetDTO, arAdjustmentOffSetVO);
			message = "ArAdjustmentOffSet Created Successfully";
		}

		arAdjustmentOffSetVO = arAdjustmentOffSetRepo.save(arAdjustmentOffSetVO);

		List<ArOffSetInvoiceDetailsVO> savedArOffSetInvoiceDetailsVO = arAdjustmentOffSetVO
				.getArOffSetInvoiceDetailsVO();
		if (savedArOffSetInvoiceDetailsVO != null && !savedArOffSetInvoiceDetailsVO.isEmpty()) {
			for (ArOffSetInvoiceDetailsVO savedReceiptInvDetails : savedArOffSetInvoiceDetailsVO) {

				String partyCode = arAdjustmentOffSetVO.getSubLedgerCode();
				String docId = arAdjustmentOffSetVO.getReceiptDocId();
				String invNo = savedReceiptInvDetails.getInvoiceNo();

				ArapAdjustmentsVO existingForward = arapAdjustmentsRepo.findByDocIdAndRefNoAndOrgIdAndSubledgerCode(
						docId, invNo, arAdjustmentOffSetVO.getOrgId(), partyCode);
				if (existingForward != null) {
					arapAdjustmentsRepo.delete(existingForward);
				}

				ArapAdjustmentsVO arapadjustments = new ArapAdjustmentsVO();
				arapadjustments.setBranch(arAdjustmentOffSetVO.getBranch());
				arapadjustments.setFinYear(arAdjustmentOffSetVO.getFinYear());
				arapadjustments.setSourceId(arAdjustmentOffSetVO.getId());
				arapadjustments.setDocId(arAdjustmentOffSetVO.getReceiptDocId());
				arapadjustments.setDocDate(arAdjustmentOffSetVO.getReceiptDocDate());
//				arapadjustments.setTdsAmt(arAdjustmentOffSetVO.getT());
				arapadjustments.setRefNo(savedReceiptInvDetails.getInvoiceNo());
				arapadjustments.setRefDate(savedReceiptInvDetails.getInvoiceDate());
				arapadjustments.setCurrency(arAdjustmentOffSetVO.getCurrency());
				arapadjustments.setAccCurrency(arAdjustmentOffSetVO.getCurrency());
				arapadjustments.setBaseAmt(savedReceiptInvDetails.getSettled());
				arapadjustments.setNativeAmt(savedReceiptInvDetails.getSettled());
//				arapadjustments.setVoucherType(arAdjustmentOffSetVO.getType());
				arapadjustments.setSubLedgerCode(arAdjustmentOffSetVO.getSubLedgerCode());
				arapadjustments.setExRate(savedReceiptInvDetails.getExRate());
//					arapadjustments.setCreditDays(savedGrnVO.getNetAmount());
//					arapadjustments.setDueDate(detailsVO.getStatus());	

				arapadjustments.setOrgId(arAdjustmentOffSetVO.getOrgId());
				arapadjustments.setActive(arAdjustmentOffSetVO.isActive());
				arapadjustments.setCancel(arAdjustmentOffSetVO.isCancel());
				arapadjustments.setCreatedBy(arAdjustmentOffSetVO.getCreatedBy());
				arapadjustments.setUpdatedBy(arAdjustmentOffSetVO.getUpdatedBy());
				arapadjustments.setBranchCode(arAdjustmentOffSetVO.getBranchCode());
				arapadjustments.setSubLedgerName(arAdjustmentOffSetVO.getSubLedgerName());
				arapadjustments.setAmount(savedReceiptInvDetails.getSettled());
				arapadjustments.setOffDocId(arAdjustmentOffSetVO.getDocId());
				arapadjustments.setBaseAmt(savedReceiptInvDetails.getSettled());

				PartyMasterVO partyMaster = partyMasterRepo.findByPartyCode(arAdjustmentOffSetVO.getSubLedgerCode());

				arapadjustments.setAccountName(partyMaster.getAccountType());
				System.out.println("ACCOUNT TYPE : " + partyMaster.getAccountType());
				arapAdjustmentsRepo.save(arapadjustments);

				ArapAdjustmentsVO existingReverse = arapAdjustmentsRepo.findByDocIdAndRefNoAndOrgIdAndSubledgerCode(
						invNo, docId, arAdjustmentOffSetVO.getOrgId(), partyCode);
				if (existingReverse != null) {
					arapAdjustmentsRepo.delete(existingReverse);
				}

				ArapAdjustmentsVO negativeArapAdjustments = new ArapAdjustmentsVO();
				negativeArapAdjustments.setBranch(arAdjustmentOffSetVO.getBranch());
				negativeArapAdjustments.setFinYear(arAdjustmentOffSetVO.getFinYear());
				negativeArapAdjustments.setSourceId(arAdjustmentOffSetVO.getId());
				negativeArapAdjustments.setDocId(savedReceiptInvDetails.getInvoiceNo());
				negativeArapAdjustments.setDocDate(savedReceiptInvDetails.getInvoiceDate());
				negativeArapAdjustments.setRefNo(arAdjustmentOffSetVO.getReceiptDocId());
				negativeArapAdjustments.setRefDate(arAdjustmentOffSetVO.getReceiptDocDate());
				negativeArapAdjustments.setCurrency(arAdjustmentOffSetVO.getCurrency());
				negativeArapAdjustments.setBaseAmt(savedReceiptInvDetails.getSettled().negate());
				negativeArapAdjustments.setNativeAmt(savedReceiptInvDetails.getSettled().negate());
				negativeArapAdjustments.setVoucherType(arAdjustmentOffSetVO.getSubLedgerType());
				negativeArapAdjustments.setSubLedgerCode(arAdjustmentOffSetVO.getSubLedgerCode());
				negativeArapAdjustments.setExRate(savedReceiptInvDetails.getExRate());
				negativeArapAdjustments.setOrgId(arAdjustmentOffSetVO.getOrgId());
				negativeArapAdjustments.setActive(arAdjustmentOffSetVO.isActive());
				negativeArapAdjustments.setCancel(arAdjustmentOffSetVO.isCancel());
				negativeArapAdjustments.setAccCurrency(arAdjustmentOffSetVO.getCurrency());
				negativeArapAdjustments.setCreatedBy(arAdjustmentOffSetVO.getCreatedBy());
				negativeArapAdjustments.setUpdatedBy(arAdjustmentOffSetVO.getUpdatedBy());
				negativeArapAdjustments.setBranchCode(arAdjustmentOffSetVO.getBranchCode());
				negativeArapAdjustments.setOffDocId(arAdjustmentOffSetVO.getDocId());
				negativeArapAdjustments.setSubLedgerName(arAdjustmentOffSetVO.getSubLedgerName());
				negativeArapAdjustments.setAmount(savedReceiptInvDetails.getSettled().negate());
				negativeArapAdjustments.setAccountName(partyMaster.getAccountType());
				arapAdjustmentsRepo.save(negativeArapAdjustments);
			}
		}

		Map<String, Object> response = new HashMap<>();
		response.put("arAdjustmentOffSetVO", arAdjustmentOffSetVO);
		response.put("message", message);
		return response;
	}

	private ArAdjustmentOffSetVO createUpdateArAdjustmentOffSetVOByArAdjustmentOffSetDTO(
			@Valid ArAdjustmentOffSetDTO arAdjustmentOffSetDTO, ArAdjustmentOffSetVO arAdjustmentOffSetVO)
			throws ApplicationException {
		// Basic field mapping
		arAdjustmentOffSetVO.setReceiptDocId(arAdjustmentOffSetDTO.getReceiptDocId());
		arAdjustmentOffSetVO.setReceiptDocDate(arAdjustmentOffSetDTO.getReceiptDocDate());
		arAdjustmentOffSetVO.setSubLedgerType(arAdjustmentOffSetDTO.getSubLedgerType());
		arAdjustmentOffSetVO.setSubLedgerName(arAdjustmentOffSetDTO.getSubLedgerName());
		arAdjustmentOffSetVO.setSubLedgerCode(arAdjustmentOffSetDTO.getSubLedgerCode());
		arAdjustmentOffSetVO.setCurrency(arAdjustmentOffSetDTO.getCurrency());
		arAdjustmentOffSetVO.setExRate(arAdjustmentOffSetDTO.getExRate());
		arAdjustmentOffSetVO.setSupplierRefNo(arAdjustmentOffSetDTO.getSupplierRefNo());
		arAdjustmentOffSetVO.setNarration(arAdjustmentOffSetDTO.getNarration());
		arAdjustmentOffSetVO.setAmount(arAdjustmentOffSetDTO.getAmount());
		arAdjustmentOffSetVO.setBranch(arAdjustmentOffSetDTO.getBranch());
		arAdjustmentOffSetVO.setBranchCode(arAdjustmentOffSetDTO.getBranchCode());
		arAdjustmentOffSetVO.setFinYear(arAdjustmentOffSetDTO.getFinYear());
		arAdjustmentOffSetVO.setStatus(arAdjustmentOffSetDTO.getStatus());
		arAdjustmentOffSetVO.setOrgId(arAdjustmentOffSetDTO.getOrgId());
		arAdjustmentOffSetVO.setActive(arAdjustmentOffSetDTO.isActive());
		if (ObjectUtils.isNotEmpty(arAdjustmentOffSetDTO.getId())) {
			// Delete existing child records if updating
			List<ArOffSetInvoiceDetailsVO> existingDetails = arOffSetInvoiceDetailsRepo
					.findByArAdjustmentOffSetVO(arAdjustmentOffSetVO);
			arOffSetInvoiceDetailsRepo.deleteAll(existingDetails);
		}

		BigDecimal netAmount = BigDecimal.ZERO;
		BigDecimal totalSettled = BigDecimal.ZERO;
		BigDecimal onAccount = BigDecimal.ZERO;
		BigDecimal receiptAmount = arAdjustmentOffSetDTO.getAmount();
		List<ArOffSetInvoiceDetailsVO> arOffSetInvoiceDetailsVOs = new ArrayList<>();

		for (ArOffSetInvoiceDetailsDTO dto : arAdjustmentOffSetDTO.getArOffSetInvoiceDetailsDTO()) {
			ArOffSetInvoiceDetailsVO vo = new ArOffSetInvoiceDetailsVO();

			vo.setInvoiceNo(dto.getInvoiceNo());
			vo.setInvoiceDate(dto.getInvoiceDate());
			vo.setRefNo(dto.getRefNo());
			vo.setRefDate(dto.getRefDate());
			vo.setCurr(dto.getCurr());
			vo.setExRate(dto.getExRate());
			vo.setSetExRate(dto.getSetExRate());
			vo.setTnxSettled(dto.getTnxSettled());
			vo.setGainOrLoss(dto.getGainOrLoss());

			BigDecimal amount = dto.getInvAmount() != null ? dto.getInvAmount() : BigDecimal.ZERO;
			BigDecimal gstAmt = dto.getGstAmt() != null ? dto.getGstAmt() : BigDecimal.ZERO;
			BigDecimal chargeAmt = amount.add(gstAmt);
			vo.setChargeAmt(chargeAmt);

			BigDecimal settled = dto.getSettled() != null ? dto.getSettled() : BigDecimal.ZERO;
			BigDecimal outstanding = chargeAmt.subtract(settled);

			vo.setOutStanding(outstanding);
			vo.setGstAmt(gstAmt);
			vo.setInvAmount(amount);
			vo.setSettled(settled);
			vo.setArAdjustmentOffSetVO(arAdjustmentOffSetVO);

			totalSettled = totalSettled.add(settled);

			arOffSetInvoiceDetailsVOs.add(vo);
		}

		netAmount = totalSettled;

		if (netAmount.compareTo(receiptAmount) > 0) {
			throw new ApplicationException("Total Settled Amount should not be greater than Receipt Amount");
		}

		onAccount = receiptAmount.subtract(netAmount);
		arAdjustmentOffSetVO.setNetAmount(netAmount);
		arAdjustmentOffSetVO.setOnAccount(onAccount);

		arAdjustmentOffSetVO.setArOffSetInvoiceDetailsVO(arOffSetInvoiceDetailsVOs);

		return arAdjustmentOffSetVO;

	}

//	@Override
//	public String getArAdjustmentOffSetDocId(Long orgId, String finYear, String branch, String branchCode) {
//		String ScreenCode = "ARA";
//		String result = arAdjustmentOffSetRepo.getArAdjustmentOffSetDocId(orgId, finYear, branchCode, ScreenCode);
//		return result;
//	}

	@Override
	public Map<String, Object> getArAdjustmentOffSetDocId(Long orgId, String finYear, String branch,
			String branchCode) {

		String screenCode = "ARA";

		List<Object[]> results = arAdjustmentOffSetRepo.getArAdjustmentOffSetDocId(orgId, finYear, branchCode,
				screenCode);

		Map<String, Object> map = new HashMap<>();

		if (results != null && !results.isEmpty()) {
			Object[] row = results.get(0);

			map.put("docId", row[0]);
			map.put("docDate", row.length > 1 ? row[1] : null);
		}

		return map;
	}

	@Override
	public List<Map<String, Object>> getAllCustomerReceiptByOrgIdAndBranchCode(Long orgId, String branchCode,
			String customerName) {

		Set<Object[]> group = receiptRepo.getAllReceiptByOrgIdAndBranchCode(orgId, branchCode, customerName);

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

	@Override
	public ArAdjustmentOffSetVO approveArAdjustmentOffSet(Long orgId, Long id, String docId, String action,
			String actionBy) throws ApplicationException {

		ArAdjustmentOffSetVO receiptVO = arAdjustmentOffSetRepo.findByOrgIdAndIdAndDocId(orgId, id, docId);

		if (receiptVO == null) {
			throw new ApplicationException("ArAdjustmentOffSet not found for the given details.");
		}

		if ("Approved".equalsIgnoreCase(receiptVO.getApproveStatus())) {
			throw new ApplicationException("This ArAdjustmentOffSet Already Approved");
		} else if ("Rejected".equals(receiptVO.getApproveStatus())) {
			throw new ApplicationException("This ArAdjustmentOffSet Already Rejected");
		}
//
//	    if (!"SUBMIT".equalsIgnoreCase(receiptVO.getStatus())) {
//	        throw new ApplicationException("Only SETTLED ArAdjustmentOffSet can be approved or rejected.");
//	    }

		if (receiptVO.getApproveStatus() == null || (!"Approved".equals(receiptVO.getApproveStatus())
				&& !"Rejected".equals(receiptVO.getApproveStatus()))) {

			String screenCode1 = "AC";
			String sourceScreenCode = receiptVO.getScreenCode();

//	        String accountsDocId = accountsRepo.getApproveDocId(
//	            receiptVO.getOrgId(),
//	            receiptVO.getFinYear(),
//	            receiptVO.getBranchCode(),
//	            sourceScreenCode,
//	            screenCode1
//	        );

			List<Object[]> taxInvoiceDoc = accountsRepo.getApproveDocId(receiptVO.getOrgId(), receiptVO.getFinYear(),
					receiptVO.getBranchCode(), sourceScreenCode, screenCode1);

			String generatedDocId = null;
			LocalDate generatedDocDate = null;

			if (taxInvoiceDoc != null && !taxInvoiceDoc.isEmpty()) {
				Object[] row = taxInvoiceDoc.get(0);
				generatedDocId = (String) row[0];
				if (row[1] != null) {
					generatedDocDate = ((java.sql.Date) row[1]).toLocalDate();
				}
			}
			receiptVO.setPurVoucherNo(generatedDocId);
			receiptVO.setPurVoucherDate(generatedDocDate);

			MultipleDocIdGenerationDetailsVO multipleDocIdGenerationDetailsVO = multipleDocIdGenerationDetailsRepo
					.findByOrgIdAndFinYearAndBranchCodeAndSourceScreenCodeAndScreenCode(receiptVO.getOrgId(),
							receiptVO.getFinYear(), receiptVO.getBranchCode(), sourceScreenCode, screenCode1);
			multipleDocIdGenerationDetailsVO.setLastno(multipleDocIdGenerationDetailsVO.getLastno() + 1);
			multipleDocIdGenerationDetailsRepo.save(multipleDocIdGenerationDetailsVO);

			AccountsVO accountsVO = new AccountsVO();
			accountsVO.setDocId(generatedDocId);
			accountsVO.setDocDate(generatedDocDate);
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
			accountsVO.setCancelRemarks(receiptVO.getCancelRemarks());
			accountsVO.setFinYear(receiptVO.getFinYear());
//	        accountsVO.setTotalDebitAmount(dtlsVO.getSettled());
//	        accountsVO.setTotalCreditAmount(dtlsVO.getSettled());

			BigDecimal netAmount = receiptVO.getNetAmount();
			BigDecimal receiptAmt = receiptVO.getAmount();
			BigDecimal effectiveNetAmount = (netAmount == null || netAmount.compareTo(BigDecimal.ZERO) == 0)
					? (receiptAmt != null ? receiptAmt : BigDecimal.ZERO)
					: netAmount;

			List<AccountsDetailsVO> accountsDetailsVOs = new ArrayList<>();

			// RECEIVABLE A/C (Credit)
			AccountsDetailsVO receivable = new AccountsDetailsVO();
			receivable.setNDebitAmount(BigDecimal.ZERO);
			receivable.setACategory("RECEIVABLE A/C");
			receivable.setAccountName("RECEIVABLE A/C");
			receivable.setSubLedgerCode(receiptVO.getSubLedgerCode());
			receivable.setDebitAmount(BigDecimal.ZERO);
			receivable.setNCreditAmount(effectiveNetAmount);
			receivable.setCreditAmount(effectiveNetAmount);
			receivable.setArapFlag(true);
			receivable.setArapAmount(effectiveNetAmount.negate());
			receivable.setBDebitAmount(BigDecimal.ZERO);
			receivable.setBCrAmount(effectiveNetAmount);
			receivable.setBArapAmount(effectiveNetAmount.negate());
			receivable.setACurrency(receiptVO.getCurrency());
			receivable.setSubledgerName(receiptVO.getSubLedgerName());
			receivable.setNArapAmount(effectiveNetAmount.negate());
			receivable.setGstflag(1);
			receivable.setAccountsVO(accountsVO);
			accountsDetailsVOs.add(receivable);

			// BANK/CASH (Debit)
			AccountsDetailsVO cashBank = new AccountsDetailsVO();
			cashBank.setNDebitAmount(receiptAmt);
//	        cashBank.setAccountName(receiptVO.getBankCashAcc());
			cashBank.setSubLedgerCode("None");
			cashBank.setDebitAmount(receiptAmt);
			cashBank.setNCreditAmount(BigDecimal.ZERO);
			cashBank.setCreditAmount(BigDecimal.ZERO);
			cashBank.setArapFlag(false);
			cashBank.setArapAmount(BigDecimal.ZERO);
			cashBank.setBDebitAmount(receiptAmt);
			cashBank.setBCrAmount(BigDecimal.ZERO);
			cashBank.setBArapAmount(BigDecimal.ZERO);
			cashBank.setACurrency(receiptVO.getCurrency());
			cashBank.setSubledgerName("None");
			cashBank.setNArapAmount(BigDecimal.ZERO);
			cashBank.setGstflag(3);
			cashBank.setAccountsVO(accountsVO);
			accountsDetailsVOs.add(cashBank);

			// Set totals
			accountsVO.setTotalDebitAmount(effectiveNetAmount);
			accountsVO.setTotalCreditAmount(receiptAmt);
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

			return arAdjustmentOffSetRepo.save(receiptVO);
		}

		return receiptVO;
	}

	// AP ADJUSTMENT OFFSET

	@Override
	public List<ApAdjustmentOffSetVO> getAllApAdjustmentOffSetByOrgId(Long orgId) {
		List<ApAdjustmentOffSetVO> apAdjustmentOffSetVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(orgId)) {
			LOGGER.info("Successfully Received  ApAdjustmentOffSet BY OrgId: {}", orgId);
			apAdjustmentOffSetVO = apAdjustmentOffSetRepo.getAllApAdjustmentOffSetByOrgId(orgId);
		}
		return apAdjustmentOffSetVO;
	}

	@Override
	public List<ApAdjustmentOffSetVO> getApAdjustmentOffSetById(Long id) {
		List<ApAdjustmentOffSetVO> apAdjustmentOffSetVO = new ArrayList<>();

		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received ApAdjustmentOffSet BY Id : {}", id);
			apAdjustmentOffSetVO = apAdjustmentOffSetRepo.getApAdjustmentOffSetById(id);
		}
		return apAdjustmentOffSetVO;
	}

	@Override
	public String getApAdjustmentOffSetDocId(Long orgId, String finYear, String branch, String branchCode) {
		String ScreenCode = "APA";
		String result = apAdjustmentOffSetRepo.getApAdjustmentOffSetDocId(orgId, finYear, branchCode, ScreenCode);
		return result;
	}

	@Override
	public List<PaymentVO> getAllVendorPaymentByOrgIdAndBranchCode(Long orgId, String branchCode) {
		List<PaymentVO> paymentVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(orgId) && ObjectUtils.isNotEmpty(branchCode)) {
			LOGGER.info("Successfully Received payment BY OrgId: {} and BranchCode: {}", orgId, branchCode);
			paymentVO = paymentRepo.getAllVendorPaymentByOrgIdAndBranchCode(orgId, branchCode);
		}
		return paymentVO;
	}

	@Override
	public Map<String, Object> updateCreateApAdjustmentOffSet(@Valid ApAdjustmentOffSetDTO apAdjustmentOffSetDTO)
			throws ApplicationException {
		String screenCode = "APA";
		ApAdjustmentOffSetVO apAdjustmentOffSetVO = new ApAdjustmentOffSetVO();
		String message;
		if (ObjectUtils.isNotEmpty(apAdjustmentOffSetDTO.getId())) {
			apAdjustmentOffSetVO = apAdjustmentOffSetRepo.findById(apAdjustmentOffSetDTO.getId())
					.orElseThrow(() -> new ApplicationException("ApAdjustmentOffSet not found"));

			apAdjustmentOffSetVO.setUpdatedBy(apAdjustmentOffSetDTO.getCreatedBy());
			createUpdateApAdjustmentOffSetVOByApAdjustmentOffSetDTO(apAdjustmentOffSetDTO, apAdjustmentOffSetVO);
			message = "ApAdjustmentOffSet Updated Successfully";
		} else {
			// GETDOCID API
			String docId = apAdjustmentOffSetRepo.getApAdjustmentOffSetByDocId(apAdjustmentOffSetDTO.getOrgId(),
					apAdjustmentOffSetDTO.getFinYear(), apAdjustmentOffSetDTO.getBranchCode(), screenCode);

			apAdjustmentOffSetVO.setDocId(docId);

//						// GETDOCID LASTNO +1
			DocumentTypeMappingDetailsVO documentTypeMappingDetailsVO = documentTypeMappingDetailsRepo
					.findByOrgIdAndFinYearAndBranchCodeAndScreenCode(apAdjustmentOffSetDTO.getOrgId(),
							apAdjustmentOffSetDTO.getFinYear(), apAdjustmentOffSetDTO.getBranchCode(), screenCode);
			documentTypeMappingDetailsVO.setLastno(documentTypeMappingDetailsVO.getLastno() + 1);
			documentTypeMappingDetailsRepo.save(documentTypeMappingDetailsVO);

			apAdjustmentOffSetVO.setCreatedBy(apAdjustmentOffSetDTO.getCreatedBy());
			apAdjustmentOffSetVO.setUpdatedBy(apAdjustmentOffSetDTO.getCreatedBy());
			createUpdateApAdjustmentOffSetVOByApAdjustmentOffSetDTO(apAdjustmentOffSetDTO, apAdjustmentOffSetVO);
			message = "ApAdjustmentOffSet Created Successfully";
		}

		apAdjustmentOffSetVO = apAdjustmentOffSetRepo.save(apAdjustmentOffSetVO);

		List<ApOffSetInvoiceDetailsVO> apOffSetInvoiceDetailsVO = apOffSetInvoiceDetailsRepo
				.findByApAdjustmentOffSetVO(apAdjustmentOffSetVO);

		for (ApOffSetInvoiceDetailsVO dtlsVO : apOffSetInvoiceDetailsVO) {

			String partyCode = apAdjustmentOffSetVO.getSubLedgerCode();
			String docId = apAdjustmentOffSetVO.getPaymentDocId();
			LocalDate docDate = apAdjustmentOffSetVO.getPaymentDocDate();
			String invNo = dtlsVO.getInvoiceNo();
			LocalDate invDate = dtlsVO.getInvoiceDate();

			PartyMasterVO partyMaster = partyMasterRepo.findByPartyCode(partyCode);

			ArapAdjustmentsVO existingForward = arapAdjustmentsRepo.findByDocIdAndRefNoAndOrgIdAndSubledgerCode(docId,
					invNo, apAdjustmentOffSetVO.getOrgId(), partyCode);
			if (existingForward != null) {
				arapAdjustmentsRepo.delete(existingForward);
			}

			ArapAdjustmentsVO forward = new ArapAdjustmentsVO();
			forward.setCancel(false);
			forward.setActive(true);
			forward.setCreatedBy(apAdjustmentOffSetVO.getCreatedBy());
			forward.setUpdatedBy(apAdjustmentOffSetVO.getUpdatedBy());
			forward.setFinYear(apAdjustmentOffSetVO.getFinYear());
			forward.setDocId(docId);
			forward.setDocDate(docDate);
			forward.setRefNo(invNo);
			forward.setRefDate(invDate);
			forward.setSubLedgerCode(partyCode);
			forward.setSubLedgerName(apAdjustmentOffSetVO.getSubLedgerName());
			forward.setCurrency(apAdjustmentOffSetVO.getCurrency());
			forward.setExRate(dtlsVO.getExRate());
			forward.setAmount(dtlsVO.getSettled());
			forward.setBaseAmt(dtlsVO.getSettled());
			forward.setNativeAmt(dtlsVO.getSettled());
			forward.setAccCurrency(apAdjustmentOffSetVO.getCurrency());
			forward.setOrgId(apAdjustmentOffSetVO.getOrgId());
			forward.setBranchCode(apAdjustmentOffSetVO.getBranchCode());
			forward.setBranch(apAdjustmentOffSetVO.getBranch());
			forward.setSourceId(dtlsVO.getId());
			forward.setOffDocId(apAdjustmentOffSetVO.getDocId());
			forward.setAccountName(partyMaster.getAccountType());
			arapAdjustmentsRepo.save(forward);

			ArapAdjustmentsVO existingReverse = arapAdjustmentsRepo.findByDocIdAndRefNoAndOrgIdAndSubledgerCode(invNo,
					docId, apAdjustmentOffSetVO.getOrgId(), partyCode);
			if (existingReverse != null) {
				arapAdjustmentsRepo.delete(existingReverse);
			}

			ArapAdjustmentsVO reverse = new ArapAdjustmentsVO();
			reverse.setCancel(false);
			reverse.setActive(true);
			reverse.setCreatedBy(apAdjustmentOffSetVO.getCreatedBy());
			reverse.setUpdatedBy(apAdjustmentOffSetVO.getUpdatedBy());
			reverse.setFinYear(apAdjustmentOffSetVO.getFinYear());
			reverse.setDocId(invNo);
			reverse.setDocDate(invDate);
			reverse.setRefNo(docId);
			reverse.setRefDate(docDate);
			reverse.setSubLedgerCode(partyCode);
			reverse.setSubLedgerName(apAdjustmentOffSetVO.getSubLedgerName());
			reverse.setCurrency(apAdjustmentOffSetVO.getCurrency());
			reverse.setExRate(dtlsVO.getExRate());
			reverse.setAmount(dtlsVO.getSettled().negate());
			reverse.setBaseAmt(dtlsVO.getSettled().negate());
			reverse.setNativeAmt(dtlsVO.getSettled().negate());
			reverse.setOrgId(apAdjustmentOffSetVO.getOrgId());
			reverse.setAccCurrency(apAdjustmentOffSetVO.getCurrency());
			reverse.setAccountName(partyMaster.getAccountType());
			reverse.setBranchCode(apAdjustmentOffSetVO.getBranchCode());
			reverse.setBranch(apAdjustmentOffSetVO.getBranch());
			reverse.setOffDocId(apAdjustmentOffSetVO.getDocId());
			reverse.setSourceId(dtlsVO.getId());
			arapAdjustmentsRepo.save(reverse);
		}

		Map<String, Object> response = new HashMap<>();
		response.put("apAdjustmentOffSetVO", apAdjustmentOffSetVO);
		response.put("message", message);
		return response;
	}

	private ApAdjustmentOffSetVO createUpdateApAdjustmentOffSetVOByApAdjustmentOffSetDTO(
			@Valid ApAdjustmentOffSetDTO apAdjustmentOffSetDTO, ApAdjustmentOffSetVO apAdjustmentOffSetVO)
			throws ApplicationException {

		apAdjustmentOffSetVO.setPaymentDocId(apAdjustmentOffSetDTO.getPaymentDocId());
		apAdjustmentOffSetVO.setPaymentDocDate(apAdjustmentOffSetDTO.getPaymentDocDate());
		apAdjustmentOffSetVO.setSubLedgerType(apAdjustmentOffSetDTO.getSubLedgerType());
		apAdjustmentOffSetVO.setSubLedgerName(apAdjustmentOffSetDTO.getSubLedgerName());
		apAdjustmentOffSetVO.setSubLedgerCode(apAdjustmentOffSetDTO.getSubLedgerCode());
		apAdjustmentOffSetVO.setCurrency(apAdjustmentOffSetDTO.getCurrency());
		apAdjustmentOffSetVO.setExRate(apAdjustmentOffSetDTO.getExRate());
		apAdjustmentOffSetVO.setSupplierRefNo(apAdjustmentOffSetDTO.getSupplierRefNo());
		apAdjustmentOffSetVO.setNarration(apAdjustmentOffSetDTO.getNarration());
		apAdjustmentOffSetVO.setAmount(apAdjustmentOffSetDTO.getAmount());
		apAdjustmentOffSetVO.setBranch(apAdjustmentOffSetDTO.getBranch());
		apAdjustmentOffSetVO.setBranchCode(apAdjustmentOffSetDTO.getBranchCode());
		apAdjustmentOffSetVO.setFinYear(apAdjustmentOffSetDTO.getFinYear());
		apAdjustmentOffSetVO.setOrgId(apAdjustmentOffSetDTO.getOrgId());
		apAdjustmentOffSetVO.setActive(apAdjustmentOffSetDTO.isActive());
		apAdjustmentOffSetVO.setStatus(apAdjustmentOffSetDTO.getStatus());

		if (ObjectUtils.isNotEmpty(apAdjustmentOffSetDTO.getId())) {
			List<ApOffSetInvoiceDetailsVO> existingDetails = apOffSetInvoiceDetailsRepo
					.findByApAdjustmentOffSetVO(apAdjustmentOffSetVO);
			apOffSetInvoiceDetailsRepo.deleteAll(existingDetails);
		}

//		// 3. Prepare new invoice details
//		List<ApOffSetInvoiceDetailsVO> invoiceDetailsVOs = new ArrayList<>();
//		BigDecimal netAmount = BigDecimal.ZERO;
//		BigDecimal totalOutstanding = BigDecimal.ZERO;
//
//		for (ApOffSetInvoiceDetailsDTO dto : apAdjustmentOffSetDTO.getApOffSetInvoiceDetailsDTO()) {
//			ApOffSetInvoiceDetailsVO vo = new ApOffSetInvoiceDetailsVO();
//			vo.setInvoiceNo(dto.getInvoiceNo());
//			vo.setInvoiceDate(dto.getInvoiceDate());
//			vo.setRefNo(dto.getRefNo());
//			vo.setRefDate(dto.getRefDate());
//			vo.setCurr(dto.getCurr());
//			vo.setExRate(dto.getExRate());
//			vo.setGstAmount(dto.getGstAmount());
//			vo.setInvAmount(dto.getInvAmount());
//			vo.setOutStanding(dto.getChargeAmt().subtract(dto.getSettled()));
//			vo.setSettled(dto.getSettled());
//			vo.setSetExRate(dto.getSetExRate());
//			vo.setTnxSettled(dto.getTnxSettled());
//			vo.setGainOrLoss(dto.getGainOrLoss());
//			vo.setRemarks(dto.getRemarks());
//
//			BigDecimal chargeAmount = dto.getInvAmount().add(dto.getGstAmount());
//			vo.setChargeAmt(chargeAmount);
//			vo.setApAdjustmentOffSetVO(apAdjustmentOffSetVO);
//
//			if (dto.getSettled().compareTo(chargeAmount) > 0) {
//				throw new ApplicationException(
//						"Settled amount (" + dto.getSettled() + ") cannot be greater than charge amount ("
//								+ chargeAmount + ") for invoice: " + dto.getInvoiceNo());
//			}
//
//			netAmount = netAmount.add(dto.getSettled());
//			totalOutstanding = totalOutstanding.add(dto.getOutStanding());
//
//			invoiceDetailsVOs.add(vo);
//		}
//
//		BigDecimal paymentAmount = apAdjustmentOffSetDTO.getAmount();
//
//		if (netAmount.compareTo(paymentAmount) > 0) {
//			throw new ApplicationException("Total Settled Amount should not be greater than Payment Amount");
//		}
//
//		BigDecimal onAccount = paymentAmount.subtract(netAmount);
//		apAdjustmentOffSetVO.setApOffSetInvoiceDetailsVO(invoiceDetailsVOs);
//		apAdjustmentOffSetVO.setNetAmount(netAmount);
//		apAdjustmentOffSetVO.setOnAccount(onAccount);
////	    apAdjustmentOffSetVO.setOutStandingTotal(totalOutstanding);
//
//		return apAdjustmentOffSetVO;

		BigDecimal netAmount = BigDecimal.ZERO;
		BigDecimal totalSettled = BigDecimal.ZERO;
		BigDecimal onAccount = BigDecimal.ZERO;
		BigDecimal receiptAmount = apAdjustmentOffSetDTO.getAmount();
		List<ApOffSetInvoiceDetailsVO> invoiceDetailsVOs = new ArrayList<>();

		for (ApOffSetInvoiceDetailsDTO dto : apAdjustmentOffSetDTO.getApOffSetInvoiceDetailsDTO()) {
			ApOffSetInvoiceDetailsVO vo = new ApOffSetInvoiceDetailsVO();
			vo.setInvoiceNo(dto.getInvoiceNo());
			vo.setInvoiceDate(dto.getInvoiceDate());
			vo.setRefNo(dto.getRefNo());
			vo.setRefDate(dto.getRefDate());
			vo.setCurr(dto.getCurr());
			vo.setExRate(dto.getExRate());
			vo.setSetExRate(dto.getSetExRate());
			vo.setTnxSettled(dto.getTnxSettled());
			vo.setGainOrLoss(dto.getGainOrLoss());

			BigDecimal amount = dto.getInvAmount() != null ? dto.getInvAmount() : BigDecimal.ZERO;
			BigDecimal gstAmt = dto.getGstAmount() != null ? dto.getGstAmount() : BigDecimal.ZERO;
			BigDecimal chargeAmt = amount.add(gstAmt);
			vo.setChargeAmt(chargeAmt);

			BigDecimal settled = dto.getSettled() != null ? dto.getSettled() : BigDecimal.ZERO;
			BigDecimal outstanding = chargeAmt.subtract(settled);

			vo.setOutStanding(outstanding);
			vo.setGstAmount(gstAmt);
			vo.setInvAmount(amount);
			vo.setSettled(settled);
			vo.setApAdjustmentOffSetVO(apAdjustmentOffSetVO);

			totalSettled = totalSettled.add(settled);

			invoiceDetailsVOs.add(vo);
		}

		netAmount = totalSettled;

		if (netAmount.compareTo(receiptAmount) > 0) {
			throw new ApplicationException("Total Settled Amount should not be greater than Receipt Amount");
		}

		onAccount = receiptAmount.subtract(netAmount);
		apAdjustmentOffSetVO.setNetAmount(netAmount);
		apAdjustmentOffSetVO.setOnAccount(onAccount);

		apAdjustmentOffSetVO.setApOffSetInvoiceDetailsVO(invoiceDetailsVOs);
		return apAdjustmentOffSetVO;

	}

	@Override
	public List<Map<String, Object>> getArOffsetFillgrid(Long orgId, String subLedgerCode, String docId, String branch,
			String docDate) {
		Set<Object[]> register = receiptRepo.getArOffsetFillgrid(orgId, subLedgerCode, docId, branch, docDate);
		return getArOffset(register);
	}

	private List<Map<String, Object>> getArOffset(Set<Object[]> getRegister) {
		List<Map<String, Object>> doctypeMappingDetails = new ArrayList<>();
		for (Object[] sup : getRegister) {

			Map<String, Object> doctype = new HashMap<>();
			doctype.put("docId", sup[0] != null ? sup[0].toString() : "");
			doctype.put("docDate", sup[1] != null ? sup[1].toString() : "");
			doctype.put("refNo", sup[2] != null ? sup[2].toString() : "");
			doctype.put("refDate", sup[3] != null ? sup[3].toString() : "");
			doctype.put("currency", sup[4] != null ? sup[4].toString() : "");
			doctype.put("exRate", sup[5] != null ? sup[5].toString() : "");
			doctype.put("amount", sup[6] != null ? new BigDecimal(sup[6].toString()) : BigDecimal.ZERO);
			doctype.put("outstanding", sup[7] != null ? new BigDecimal(sup[7].toString()) : BigDecimal.ZERO);
			doctype.put("settled", sup[8] != null ? new BigDecimal(sup[8].toString()) : BigDecimal.ZERO);

			doctypeMappingDetails.add(doctype);
		}

		return doctypeMappingDetails;
	}

	@Override
	public ApAdjustmentOffSetVO approveApAdjustmentOffSet(Long orgId, Long id, String docId, String action,
			String actionBy) throws ApplicationException {

		ApAdjustmentOffSetVO paymentVO = apAdjustmentOffSetRepo.findByOrgIdAndIdAndDocId(orgId, id, docId);

		if (paymentVO == null) {
			throw new ApplicationException("apAdjustmentOffSet not found for the given details.");
		}

		if ("Approved".equalsIgnoreCase(paymentVO.getApproveStatus())) {
			throw new ApplicationException("This ApAdjustmentOffSet Already Approved");
		} else if ("Rejected".equals(paymentVO.getApproveStatus())) {
			throw new ApplicationException("This ApAdjustmentOffSet Already Rejected");
		}

//		if (!"SUBMIT".equalsIgnoreCase(paymentVO.getStatus())) {
//			throw new ApplicationException("Only SETTLED ApAdjustmentOffSetVO can be approved or rejected.");
//		}

		if (paymentVO.getApproveStatus() == null || (!"Approved".equals(paymentVO.getApproveStatus())
				&& !"Rejected".equals(paymentVO.getApproveStatus()))) {

			String screenCode1 = "AC";
			String sourceScreenCode = paymentVO.getScreenCode();

//			String accountsDocId = accountsRepo.getApproveDocId(paymentVO.getOrgId(), paymentVO.getFinYear(),
//					paymentVO.getBranchCode(), sourceScreenCode, screenCode1);

			List<Object[]> taxInvoiceDoc = accountsRepo.getApproveDocId(paymentVO.getOrgId(), paymentVO.getFinYear(),
					paymentVO.getBranchCode(), sourceScreenCode, screenCode1);

			String generatedDocId = null;
			LocalDate generatedDocDate = null;

			if (taxInvoiceDoc != null && !taxInvoiceDoc.isEmpty()) {
				Object[] row = taxInvoiceDoc.get(0);
				generatedDocId = (String) row[0];
				if (row[1] != null) {
					generatedDocDate = ((java.sql.Date) row[1]).toLocalDate();
				}
			}
			paymentVO.setPurVoucherNo(generatedDocId);
			paymentVO.setPurVoucherDate(generatedDocDate);

			MultipleDocIdGenerationDetailsVO multipleDocIdGenerationDetailsVO = multipleDocIdGenerationDetailsRepo
					.findByOrgIdAndFinYearAndBranchCodeAndSourceScreenCodeAndScreenCode(paymentVO.getOrgId(),
							paymentVO.getFinYear(), paymentVO.getBranchCode(), sourceScreenCode, screenCode1);
			multipleDocIdGenerationDetailsVO.setLastno(multipleDocIdGenerationDetailsVO.getLastno() + 1);
			multipleDocIdGenerationDetailsRepo.save(multipleDocIdGenerationDetailsVO);

			AccountsVO accountsVO = new AccountsVO();
			accountsVO.setDocId(generatedDocId);
			accountsVO.setDocDate(generatedDocDate);
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
//			accountsVO.setExRate(dtlsVO.getExRate());
			accountsVO.setRemarks(paymentVO.getCancelRemarks());
			accountsVO.setFinYear(paymentVO.getFinYear());

			accountsVO.setTotalDebitAmount(paymentVO.getAmount());
			accountsVO.setTotalCreditAmount(paymentVO.getAmount());
//			accountsVO.setCreditDays(taxInvoiceVO.getCreditDays());
//			accountsVO.setAmountInWords(savedReceiptVO.getAmountInWords());
//			accountsVO.setStTaxAmount(taxInvoiceVO.getTotalTaxableAmountLc());
//			accountsVO.setChargeableAmount(taxInvoiceVO.getTotalChargeAmountLc());

			// Create AccountsDetailsVO list and populate it
			List<AccountsDetailsVO> accountsDetailsVOs = new ArrayList<>();

			// RECEIVABLE A/C entry (Credit)
			AccountsDetailsVO accountsDetailsVO = new AccountsDetailsVO();
			accountsDetailsVO.setNDebitAmount(paymentVO.getAmount());
			accountsDetailsVO.setACategory("PAYABLE A/C");
			accountsDetailsVO.setAccountName("PAYABLE A/C");
			accountsDetailsVO.setSubLedgerCode(paymentVO.getSubLedgerCode());
			accountsDetailsVO.setDebitAmount(paymentVO.getAmount());
			accountsDetailsVO.setNCreditAmount(BigDecimal.ZERO);
			accountsDetailsVO.setCreditAmount(BigDecimal.ZERO);
			accountsDetailsVO.setArapFlag(true);
			accountsDetailsVO.setArapAmount(paymentVO.getAmount().multiply(BigDecimal.valueOf(-1)));
			accountsDetailsVO.setBDebitAmount(paymentVO.getAmount());
			accountsDetailsVO.setBCrAmount(BigDecimal.ZERO);
			accountsDetailsVO.setBArapAmount(paymentVO.getAmount().multiply(BigDecimal.valueOf(-1)));
			accountsDetailsVO.setACurrency(paymentVO.getCurrency());
			accountsDetailsVO.setSubledgerName(paymentVO.getSubLedgerName());
			accountsDetailsVO.setNArapAmount(paymentVO.getAmount().multiply(BigDecimal.valueOf(-1)));
			accountsDetailsVO.setGstflag(2);
			accountsDetailsVO.setAccountsVO(accountsVO);
			accountsDetailsVOs.add(accountsDetailsVO);

			// BANK/CASH entry (Debit)
			AccountsDetailsVO accountsDetailsVO1 = new AccountsDetailsVO();
			accountsDetailsVO1.setNDebitAmount(BigDecimal.ZERO);
//			accountsDetailsVO1.setAccountName(paymentVO.getBankCashAcc());
			accountsDetailsVO1.setSubLedgerCode("None");
			accountsDetailsVO1.setDebitAmount(BigDecimal.ZERO);
			accountsDetailsVO1.setNCreditAmount(paymentVO.getAmount());
			accountsDetailsVO1.setCreditAmount(paymentVO.getAmount());
			accountsDetailsVO1.setArapFlag(false);
			accountsDetailsVO1.setArapAmount(BigDecimal.ZERO);
			accountsDetailsVO1.setBDebitAmount(BigDecimal.ZERO);
			accountsDetailsVO1.setBCrAmount(paymentVO.getAmount());
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
			arapDetailsVO.setActive(savedAccountsVO.isActive());
			arapDetailsVO.setAccName(accountsDetailsVOs2.getAccountName());
			arapDetailsVO.setGstFlag(accountsDetailsVOs2.getGstflag());
			arapDetailsVO.setSubLedgerName(accountsDetailsVOs2.getSubledgerName());
			arapDetailsVO.setSalesType(savedAccountsVO.getSalesType());
			arapDetailsVO.setNativeAmt(accountsDetailsVOs2.getArapAmount());
			arapDetailsRepo.save(arapDetailsVO);

			// Update approval status
			paymentVO.setApproveStatus(action);
			paymentVO.setApproveBy(actionBy);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
			paymentVO.setApproveOn(LocalDateTime.now().format(formatter).toUpperCase());

			return apAdjustmentOffSetRepo.save(paymentVO);
		}

		return paymentVO;

	}

}
