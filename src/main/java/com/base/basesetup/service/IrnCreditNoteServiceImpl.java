package com.base.basesetup.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.basesetup.dto.IrnCreditNoteDTO;
import com.base.basesetup.dto.IrnCreditNoteDetailsDTO;
import com.base.basesetup.entity.AccountsDetailsVO;
import com.base.basesetup.entity.AccountsVO;
import com.base.basesetup.entity.DocumentTypeMappingDetailsVO;
import com.base.basesetup.entity.GroupLedgerVO;
import com.base.basesetup.entity.IrnCreditNoteDetailsVO;
import com.base.basesetup.entity.IrnCreditNoteGstVO;
import com.base.basesetup.entity.IrnCreditNoteVO;
import com.base.basesetup.entity.MultipleDocIdGenerationDetailsVO;
import com.base.basesetup.entity.PartyMasterVO;
import com.base.basesetup.entity.TaxInvoiceDetailsVO;
import com.base.basesetup.entity.TaxInvoiceGstVO;
import com.base.basesetup.entity.TaxInvoiceVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.AccountsRepo;
import com.base.basesetup.repo.DocumentTypeMappingDetailsRepo;
import com.base.basesetup.repo.GroupLedgerRepo;
import com.base.basesetup.repo.IrnCreditNoteDetailsRepo;
import com.base.basesetup.repo.IrnCreditNoteGstRepo;
import com.base.basesetup.repo.IrnCreditNoteRepo;
import com.base.basesetup.repo.MultipleDocIdGenerationDetailsRepo;
import com.base.basesetup.repo.PartyMasterRepo;
import com.base.basesetup.repo.TaxInvoiceRepo;

@Service
public class IrnCreditNoteServiceImpl implements IrnCreditNoteService {
	public static final Logger LOGGER = LoggerFactory.getLogger(IrnCreditNoteServiceImpl.class);
	@Autowired
	IrnCreditNoteRepo irnCreditRepo;

	@Autowired
	IrnCreditNoteDetailsRepo irnCreditChargesRepo;

	@Autowired
	IrnCreditNoteGstRepo irnCreditGstRepo;
	
	@Autowired
	AccountsRepo accountsRepo;
	
	@Autowired
	GroupLedgerRepo groupLedgerRepo;
	
	@Autowired
	MultipleDocIdGenerationDetailsRepo multipleDocIdGenerationDetailsRepo;
	
	@Autowired
	AmountInWordsConverterService amountInWordsConverterService;
	
	@Autowired
	PartyMasterRepo partyMasterRepo;
	
	@Autowired
	TaxInvoiceRepo taxInvoiceRepo;

	@Autowired
	DocumentTypeMappingDetailsRepo documentTypeMappingDetailsRepo;

	@Override
	public List<IrnCreditNoteVO> getAllIrnCreditByOrgId(Long orgId) {
		List<IrnCreditNoteVO> irnCreditVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(orgId)) { 
			LOGGER.info("Successfully Received  IrnCredit BY OrgId : {}", orgId);
			irnCreditVO = irnCreditRepo.getAllIrnCreditByOrgId(orgId);
		} 
		return irnCreditVO;
	} 

	@Override
	public List<IrnCreditNoteVO> getAllIrnCreditById(Long id) {
		List<IrnCreditNoteVO> irnCreditVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received  IrnCredit BY Id : {}", id);
			irnCreditVO = irnCreditRepo.getAllIrnCreditById(id);
		} 
		return irnCreditVO;
	}

	@Override
	public Map<String, Object> updateCreateIrnCreditNote(IrnCreditNoteDTO irnCreditNoteDTO) throws ApplicationException {
		String screenCode = "ICN";
		IrnCreditNoteVO irnCreditNoteVO = new IrnCreditNoteVO();
		String message;
		if (ObjectUtils.isNotEmpty(irnCreditNoteDTO.getId())) {
			irnCreditNoteVO = irnCreditRepo.findById(irnCreditNoteDTO.getId())
					.orElseThrow(() -> new ApplicationException("IRN Credit Note not found"));

			irnCreditNoteVO.setModifiedBy(irnCreditNoteDTO.getCreatedBy());
			createUpdateIrnCreditNoteVOByIrnCreditNoteDTO(irnCreditNoteDTO, irnCreditNoteVO);
			message = "IRN Credit Note Updated Successfully";
		} else {
			// GETDOCID API
			String docId = irnCreditRepo.getIrnCreditDocId(irnCreditNoteDTO.getOrgId(), irnCreditNoteDTO.getFinYear(),
					irnCreditNoteDTO.getBranchCode(), screenCode);
			irnCreditNoteVO.setDocId(docId);
			
			System.out.println(docId);

			// GETDOCID LASTNO +1
			DocumentTypeMappingDetailsVO documentTypeMappingDetailsVO = documentTypeMappingDetailsRepo
					.findByOrgIdAndFinYearAndBranchCodeAndScreenCode(irnCreditNoteDTO.getOrgId(),
							irnCreditNoteDTO.getFinYear(), irnCreditNoteDTO.getBranchCode(), screenCode);
			documentTypeMappingDetailsVO.setLastno(documentTypeMappingDetailsVO.getLastno() + 1);
			documentTypeMappingDetailsRepo.save(documentTypeMappingDetailsVO);

			irnCreditNoteVO.setCreatedBy(irnCreditNoteDTO.getCreatedBy());
			irnCreditNoteVO.setModifiedBy(irnCreditNoteDTO.getCreatedBy());
			createUpdateIrnCreditNoteVOByIrnCreditNoteDTO(irnCreditNoteDTO, irnCreditNoteVO);
			message = "Tax Invoice Created Successfully";
		}

		irnCreditRepo.save(irnCreditNoteVO);
		Map<String, Object> response = new HashMap<>();
		response.put("irnCreditNoteVO", irnCreditNoteVO);
		response.put("message", message);
		return response;
	}

	private void createUpdateIrnCreditNoteVOByIrnCreditNoteDTO(IrnCreditNoteDTO irnCreditNoteDTO, IrnCreditNoteVO irnCreditNoteVO) {

		// Map fields from DTO to VO
		irnCreditNoteVO.setOrgId(irnCreditNoteDTO.getOrgId());
	    irnCreditNoteVO.setBranch(irnCreditNoteDTO.getBranch());
	    irnCreditNoteVO.setBranchCode(irnCreditNoteDTO.getBranchCode());
	    irnCreditNoteVO.setFinYear(irnCreditNoteDTO.getFinYear());
	    irnCreditNoteVO.setCreatedBy(irnCreditNoteDTO.getCreatedBy());
	    irnCreditNoteVO.setBizType(irnCreditNoteDTO.getBizType());
	    irnCreditNoteVO.setBizMode(irnCreditNoteDTO.getBizMode());
	    irnCreditNoteVO.setPartyName(irnCreditNoteDTO.getPartyName());
	    irnCreditNoteVO.setPartyCode(irnCreditNoteDTO.getPartyCode());
	    irnCreditNoteVO.setPartyType(irnCreditNoteDTO.getPartyType());
	    irnCreditNoteVO.setStateNo(irnCreditNoteDTO.getStateNo());
	    irnCreditNoteVO.setStateCode(irnCreditNoteDTO.getStateCode());
	    irnCreditNoteVO.setRecipientGSTIN(irnCreditNoteDTO.getRecipientGSTIN());
	    irnCreditNoteVO.setPlaceOfSupply(irnCreditNoteDTO.getPlaceOfSupply());
	    irnCreditNoteVO.setAddressType(irnCreditNoteDTO.getAddressType());
	    irnCreditNoteVO.setAddress(irnCreditNoteDTO.getAddress());
	    irnCreditNoteVO.setPinCode(irnCreditNoteDTO.getPinCode());
	    irnCreditNoteVO.setStatus(irnCreditNoteDTO.getStatus());
	    irnCreditNoteVO.setGstType(irnCreditNoteDTO.getGstType());
	    irnCreditNoteVO.setOriginBillNo(irnCreditNoteDTO.getOriginBillNo());
	    irnCreditNoteVO.setOriginBillDate(irnCreditNoteDTO.getOriginBillDate());
	    irnCreditNoteVO.setSupplierRefNo(irnCreditNoteDTO.getSupplierRefNo());
	    irnCreditNoteVO.setSupplierRefDate(irnCreditNoteDTO.getSupplierRefDate());
	    irnCreditNoteVO.setBillCurr(irnCreditNoteDTO.getBillCurr());
	    irnCreditNoteVO.setBillCurrRate(irnCreditNoteDTO.getBillCurrRate());
	    irnCreditNoteVO.setExAmount(irnCreditNoteDTO.getExAmount());
	    irnCreditNoteVO.setCreditDays(irnCreditNoteDTO.getCreditDays());
	    irnCreditNoteVO.setShipperRefNo(irnCreditNoteDTO.getShipperRefNo());
	    irnCreditNoteVO.setBillMonth(irnCreditNoteDTO.getBillMonth());
	    irnCreditNoteVO.setSalesType(irnCreditNoteDTO.getSalesType());
	    irnCreditNoteVO.setCreditRemarks(irnCreditNoteDTO.getCreditRemarks());
	    
		if (ObjectUtils.isNotEmpty(irnCreditNoteVO.getId())) {
			List<IrnCreditNoteDetailsVO> irnCreditNoteDetailsVO1 = irnCreditChargesRepo.findByIrnCreditNoteVO(irnCreditNoteVO);
			irnCreditChargesRepo.deleteAll(irnCreditNoteDetailsVO1);
		}
		BigDecimal totalChargeAmountLC = BigDecimal.ZERO;
		BigDecimal totalChargeAmountBC = BigDecimal.ZERO;
		BigDecimal totalTaxAmountLC = BigDecimal.ZERO;
		BigDecimal totalTaxAmountBC = BigDecimal.ZERO;
		BigDecimal totalInvAmountLC = BigDecimal.ZERO;
		BigDecimal totalInvAmountBC = BigDecimal.ZERO;

		List<IrnCreditNoteDetailsVO> irnCreditNoteDetailsVOs = new ArrayList<>();
		for (IrnCreditNoteDetailsDTO irnCreditNoteDetailsDTO : irnCreditNoteDTO.getIrnCreditNoteDetailsDTO()) {

			IrnCreditNoteDetailsVO irnCreditNoteDetailsVO = new IrnCreditNoteDetailsVO();
			irnCreditNoteDetailsVO.setChargeType(irnCreditNoteDetailsDTO.getChargeType());
			irnCreditNoteDetailsVO.setChargeCode(irnCreditNoteDetailsDTO.getChargeCode());
			irnCreditNoteDetailsVO.setGovChargeCode(irnCreditNoteDetailsDTO.getGovChargeCode());
			irnCreditNoteDetailsVO.setLedger(irnCreditNoteDetailsDTO.getLedger());
			irnCreditNoteDetailsVO.setChargeName(irnCreditNoteDetailsDTO.getChargeName());
			irnCreditNoteDetailsVO.setTaxable(irnCreditNoteDetailsDTO.getTaxable());
			irnCreditNoteDetailsVO.setQty(irnCreditNoteDetailsDTO.getQty());
			irnCreditNoteDetailsVO.setRate(irnCreditNoteDetailsDTO.getRate());
			irnCreditNoteDetailsVO.setCurrency(irnCreditNoteDetailsDTO.getCurrency());
			irnCreditNoteDetailsVO.setExRate(irnCreditNoteDetailsDTO.getExRate());
			irnCreditNoteDetailsVO.setExempted(irnCreditNoteDetailsDTO.getExempted());
			irnCreditNoteDetailsVO.setSac(irnCreditNoteDetailsDTO.getSac());
			irnCreditNoteDetailsVO.setGSTPercent(irnCreditNoteDetailsDTO.getGSTPercent());

			BigDecimal fcAmount;
			BigDecimal lcAmount;
			BigDecimal tlcAmount;
			BigDecimal billAmount;
			BigDecimal gstAmount;

			if (!irnCreditNoteDetailsDTO.getCurrency().equals("INR")) {
				BigDecimal rate = irnCreditNoteDetailsDTO.getRate(); // BigDecimal type is expected here
				BigDecimal qty = BigDecimal.valueOf(irnCreditNoteDetailsDTO.getQty()); // Convert qty to BigDecimal

				fcAmount = rate.multiply(qty);

				irnCreditNoteDetailsVO.setFcAmount(fcAmount);

			} else {
				fcAmount = BigDecimal.valueOf(0.00);
				irnCreditNoteDetailsVO.setFcAmount(fcAmount);
			}

			BigDecimal exRate = irnCreditNoteDetailsDTO.getExRate();// Assuming getExRate() returns BigDecimal
			BigDecimal qty = BigDecimal.valueOf(irnCreditNoteDetailsDTO.getQty());
			BigDecimal rate = irnCreditNoteDetailsDTO.getRate();
			lcAmount = exRate.multiply(qty.multiply(rate));
			irnCreditNoteDetailsVO.setLcAmount(lcAmount);
			totalChargeAmountLC = totalChargeAmountLC.add(lcAmount);

			BigDecimal gstPercent = BigDecimal.valueOf(irnCreditNoteDetailsDTO.getGSTPercent()); // Convert GSTPercent to
																								// BigDecimal
			tlcAmount = lcAmount.multiply(gstPercent).divide(BigDecimal.valueOf(100));
			irnCreditNoteDetailsVO.setTlcAmount(tlcAmount);

			billAmount = lcAmount.divide(exRate, RoundingMode.HALF_UP); // Ensure you specify a RoundingMode when
																		// dividing
			irnCreditNoteDetailsVO.setBillAmount(billAmount);
			totalChargeAmountBC = totalChargeAmountBC.add(billAmount);

			gstAmount = lcAmount.multiply(gstPercent).divide(BigDecimal.valueOf(100));
			irnCreditNoteDetailsVO.setGstAmount(gstAmount);
			totalTaxAmountLC = totalTaxAmountLC.add(gstAmount);
			totalTaxAmountBC = totalTaxAmountBC.add(gstAmount);

			irnCreditNoteDetailsVO.setIrnCreditNoteVO(irnCreditNoteVO);
			irnCreditNoteDetailsVOs.add(irnCreditNoteDetailsVO);
		}
		Map<Integer, BigDecimal> gstSumMap = new HashMap<>();
		for (IrnCreditNoteDetailsVO detailsVO : irnCreditNoteDetailsVOs) {
			int gst = detailsVO.getGSTPercent();
			BigDecimal gstAmount = detailsVO.getGstAmount();
			gstSumMap.put(gst, gstSumMap.getOrDefault(gst, BigDecimal.ZERO).add(gstAmount));
		}
		
		Map<Double, BigDecimal> updatedGstSumMap = new HashMap<>();
		if(irnCreditNoteDTO.getGstType().equals("INTRA"))
		{
		for (Map.Entry<Integer, BigDecimal> entry : gstSumMap.entrySet()) {
		    double halfGst = entry.getKey() / 2; // Divide GST by 2
		    BigDecimal halfGstAmount = entry.getValue().divide(BigDecimal.valueOf(2), 2, RoundingMode.UNNECESSARY); // Divide GST Amount by 2
		    updatedGstSumMap.put(halfGst, halfGstAmount);
		}
		}
		else
		{
			for (Map.Entry<Integer, BigDecimal> entry : gstSumMap.entrySet()) {
				double gst = entry.getKey(); // Keep the original GST percentage
			    BigDecimal gstAmount = entry.getValue(); // Keep the original GST amount
			    updatedGstSumMap.put(gst, gstAmount); //
			}
			
		}
		
		Map<String,BigDecimal>taxDetailsMap= new HashMap<>();
		for (Map.Entry<Double, BigDecimal> entry : updatedGstSumMap.entrySet())
		{
			List<GroupLedgerVO> groupLedgerVOs= groupLedgerRepo.getTaxLedgerDetails(irnCreditNoteDTO.getOrgId(),irnCreditNoteDTO.getGstType(),entry.getKey());
			for(GroupLedgerVO groupLedgerVO:groupLedgerVOs)
			{
				taxDetailsMap.put(groupLedgerVO.getAccountGroupName(), entry.getValue());
			}
		}
		List<IrnCreditNoteGstVO> irnCreditNoteGstVOList = new ArrayList<>();
		for (Map.Entry<String, BigDecimal> entry : taxDetailsMap.entrySet()) {
			IrnCreditNoteGstVO irnCreditNoteGstVO = new IrnCreditNoteGstVO();
			irnCreditNoteGstVO.setGstChargeAcc(entry.getKey());
			irnCreditNoteGstVO.setGstCrLcAmount(BigDecimal.ZERO);
			irnCreditNoteGstVO.setGstDbBillAmount(entry.getValue());
			irnCreditNoteGstVO.setGstDbLcAmount(entry.getValue());
			irnCreditNoteGstVO.setGstSubledgerCode("None");
			irnCreditNoteGstVO.setGstCrBillAmount(BigDecimal.ZERO);
			irnCreditNoteGstVO.setIrnCreditNoteVO(irnCreditNoteVO);
			irnCreditNoteGstVOList.add(irnCreditNoteGstVO);
		}

		Map<String, BigDecimal> ledgerSumMap = new HashMap<>();
		for (IrnCreditNoteDetailsVO detailsVO : irnCreditNoteDetailsVOs) {
			String ledger = detailsVO.getLedger();
			BigDecimal lcAmount = detailsVO.getLcAmount();

			ledgerSumMap.put(ledger, ledgerSumMap.getOrDefault(ledger, BigDecimal.ZERO).add(lcAmount));
		}
		if (ObjectUtils.isNotEmpty(irnCreditNoteVO.getId())) {
			List<IrnCreditNoteGstVO> irnCreditNoteGstVO = irnCreditGstRepo.findByIrnCreditNoteVO(irnCreditNoteVO);
			irnCreditGstRepo.deleteAll(irnCreditNoteGstVO);
		}
		
		for (Map.Entry<String, BigDecimal> entry : ledgerSumMap.entrySet()) {
			IrnCreditNoteGstVO irnCreditNoteGstVO = new IrnCreditNoteGstVO();
			irnCreditNoteGstVO.setGstChargeAcc(entry.getKey());
			irnCreditNoteGstVO.setGstCrLcAmount(BigDecimal.ZERO);
			irnCreditNoteGstVO.setGstDbBillAmount(entry.getValue());
			irnCreditNoteGstVO.setGstDbLcAmount(entry.getValue());
			irnCreditNoteGstVO.setGstSubledgerCode("None");
			irnCreditNoteGstVO.setGstCrBillAmount(BigDecimal.ZERO);
			irnCreditNoteGstVO.setIrnCreditNoteVO(irnCreditNoteVO);
			irnCreditNoteGstVOList.add(irnCreditNoteGstVO);
		}
		irnCreditNoteVO.setIrnCreditNoteGstVO(irnCreditNoteGstVOList);

		totalInvAmountLC = totalChargeAmountLC.add(totalTaxAmountLC);
		totalInvAmountBC = totalChargeAmountBC.add(totalTaxAmountBC);

		irnCreditNoteVO.setTotalChargeAmountLc(totalChargeAmountLC);
		irnCreditNoteVO.setTotalChargeAmountBc(totalChargeAmountBC);
		irnCreditNoteVO.setTotalTaxAmountLc(totalTaxAmountLC);
		irnCreditNoteVO.setTotalTaxAmountBc(totalTaxAmountBC);

		BigDecimal originalTotalInvAmountLC = totalChargeAmountLC.add(totalTaxAmountLC);
		BigDecimal roundedTotalInvAmountLC = totalInvAmountLC.setScale(0, RoundingMode.HALF_UP);
		BigDecimal roundOffAmountLC = roundedTotalInvAmountLC.subtract(originalTotalInvAmountLC);
		irnCreditNoteVO.setTotalInvAmountLc(roundedTotalInvAmountLC);
		irnCreditNoteVO.setAmountInWords(amountInWordsConverterService.convert(irnCreditNoteVO.getTotalInvAmountLc().longValue()));
		irnCreditNoteVO.setRoundOffAmountLc(roundOffAmountLC);

		BigDecimal roundedTotalInvAmountBC = totalInvAmountBC.setScale(0, RoundingMode.HALF_UP);
		irnCreditNoteVO.setTotalInvAmountBc(roundedTotalInvAmountBC);

		irnCreditNoteVO.setIrnCreditNoteDetailsVO(irnCreditNoteDetailsVOs);

	}

	@Override
	public String getIrnCreditNoteDocId(Long orgId, String finYear, String branch, String branchCode) {
		String ScreenCode = "ICN";
		String result = irnCreditRepo.getIrnCreditDocId(orgId, finYear, branchCode, ScreenCode);
		return result;
	}
	
	@Override
	public List<PartyMasterVO> getAllPartyByPartyType(Long orgId, String partyType) {
		
		return partyMasterRepo.findByOrgIdAndPartyTypeIgnoreCase(orgId,partyType);

	}

	@Override
	public List<TaxInvoiceVO> getOriginBillNofromTaxInvoiceByParty(Long orgId, String party,String branchCode) {
		return taxInvoiceRepo.findPartyInvoiceDetails(orgId,party,branchCode);
	}

	@Override
	public IrnCreditNoteVO approveIrnCreditNote(Long orgId, Long id, String docId, String action, String actionBy)
			throws ApplicationException {
		 // Fetch the TaxInvoiceVO from the repository
		IrnCreditNoteVO irnCreditNoteVO = irnCreditRepo.findByOrgIdAndIdAndDocId(orgId, id, docId);
	    String screenCode="AC";
	    String sourceScreenCode = irnCreditNoteVO.getScreenCode();
	    

	    // Validate the approval status of the invoice
	    if (irnCreditNoteVO.getApproveStatus() == null || 
	       (!irnCreditNoteVO.getApproveStatus().equals("Approved") && !irnCreditNoteVO.getApproveStatus().equals("Rejected"))) {

	    	String accountsDocId = accountsRepo.getApproveDocId(irnCreditNoteVO.getOrgId(), irnCreditNoteVO.getFinYear(),
	    			irnCreditNoteVO.getBranchCode(),sourceScreenCode,screenCode);
			irnCreditNoteVO.setDocId(docId);

			// GETDOCID LASTNO +1
			MultipleDocIdGenerationDetailsVO multipleDocIdGenerationDetailsVO = multipleDocIdGenerationDetailsRepo
					.findByOrgIdAndFinYearAndBranchCodeAndSourceScreenCodeAndScreenCode(irnCreditNoteVO.getOrgId(), irnCreditNoteVO.getFinYear(),
			    			irnCreditNoteVO.getBranchCode(),sourceScreenCode,screenCode);
			multipleDocIdGenerationDetailsVO.setLastno(multipleDocIdGenerationDetailsVO.getLastno() + 1);
			multipleDocIdGenerationDetailsRepo.save(multipleDocIdGenerationDetailsVO);
			
	        // Create AccountsVO object and populate its fields
	        AccountsVO accountsVO = new AccountsVO();
	        accountsVO.setDocId(accountsDocId);
	        accountsVO.setSourceScreen(irnCreditNoteVO.getScreenName());
	        accountsVO.setSourceScreenCode(irnCreditNoteVO.getScreenCode());
	        accountsVO.setSourceId(irnCreditNoteVO.getId());
	        accountsVO.setCreatedBy(irnCreditNoteVO.getCreatedBy());
	        accountsVO.setModifiedBy(irnCreditNoteVO.getModifiedBy());
	        accountsVO.setOrgId(irnCreditNoteVO.getOrgId());
	        accountsVO.setBranch(irnCreditNoteVO.getBranch());
	        accountsVO.setBranchCode(irnCreditNoteVO.getBranchCode());
	        accountsVO.setModifiedon(irnCreditNoteVO.getCommonDate().getModifiedon().toUpperCase());
	        accountsVO.setCreatedon(irnCreditNoteVO.getCommonDate().getModifiedon().toUpperCase());
	        accountsVO.setRefNo(irnCreditNoteVO.getDocId());
	        accountsVO.setRefDate(irnCreditNoteVO.getDocDate());
	        accountsVO.setCurrency(irnCreditNoteVO.getBillCurr());
	        accountsVO.setExRate(irnCreditNoteVO.getBillCurrRate());
	        accountsVO.setRemarks(irnCreditNoteVO.getCreditRemarks());
	        accountsVO.setSupplierRefNo(irnCreditNoteVO.getSupplierRefNo());
	        accountsVO.setSupplierRefDate(irnCreditNoteVO.getSupplierRefDate());
	        accountsVO.setBillMonth(irnCreditNoteVO.getBillMonth());
	        accountsVO.setFinYear(irnCreditNoteVO.getFinYear());

//	        // Calculate total debit/credit amounts
	        BigDecimal totalDebitAmount = irnCreditNoteVO.getTotalChargeAmountLc().add(irnCreditNoteVO.getTotalTaxAmountLc());
	        accountsVO.setTotalDebitAmount(totalDebitAmount);
	        accountsVO.setTotalCreditAmount(totalDebitAmount);
	        accountsVO.setCreditDays(irnCreditNoteVO.getCreditDays());
	        accountsVO.setAmountInWords(irnCreditNoteVO.getAmountInWords());
	        accountsVO.setStTaxAmount(irnCreditNoteVO.getTotalTaxableAmountLc());
	        accountsVO.setChargeableAmount(irnCreditNoteVO.getTotalChargeAmountLc());
	        accountsVO.setSalesType(irnCreditNoteVO.getSalesType());

//	        // Create AccountsDetailsVO list and populate it
	        List<AccountsDetailsVO> accountsDetailsVOs = new ArrayList<>();

//	        // Add RECEIVABLE A/C entry
	        AccountsDetailsVO accountsDetailsVO = new AccountsDetailsVO();
	        accountsDetailsVO.setNDebitAmount(BigDecimal.ZERO);
	        accountsDetailsVO.setACategory("RECEIVABLE A/C");
	        accountsDetailsVO.setSubLedgerCode(irnCreditNoteVO.getPartyCode());
	        accountsDetailsVO.setDebitAmount(BigDecimal.ZERO);
	        accountsDetailsVO.setNCreditAmount(irnCreditNoteVO.getTotalInvAmountLc());
	        accountsDetailsVO.setCreditAmount(irnCreditNoteVO.getTotalInvAmountLc());
	        accountsDetailsVO.setArapFlag(true);
	        accountsDetailsVO.setArapAmount(irnCreditNoteVO.getTotalInvAmountLc().multiply(BigDecimal.valueOf(-1)));
	        accountsDetailsVO.setBDebitAmount(BigDecimal.ZERO);
	        accountsDetailsVO.setBCrAmount(irnCreditNoteVO.getTotalInvAmountLc());
	        accountsDetailsVO.setBArapAmount(irnCreditNoteVO.getTotalInvAmountLc().multiply(BigDecimal.valueOf(-1)));
	        accountsDetailsVO.setACurrency(irnCreditNoteVO.getBillCurr());
	        accountsDetailsVO.setAExRate(irnCreditNoteVO.getBillCurrRate());
	        accountsDetailsVO.setSubledgerName(irnCreditNoteVO.getPartyName());
	        accountsDetailsVO.setSubLedgerCode(irnCreditNoteVO.getPartyCode());
	        accountsDetailsVO.setNArapAmount(irnCreditNoteVO.getTotalInvAmountLc().multiply(BigDecimal.valueOf(-1)));
	        accountsDetailsVO.setGstflag(1);
	        accountsDetailsVO.setAccountsVO(accountsVO);
	        accountsDetailsVOs.add(accountsDetailsVO);

//	        // Group and process GST-related ledgers
	        Map<String, BigDecimal> ledgerSumMap = new HashMap<>();
	        for (IrnCreditNoteGstVO gstVO : irnCreditNoteVO.getIrnCreditNoteGstVO()) {
	            String ledger = gstVO.getGstChargeAcc();
	            BigDecimal lcAmount = gstVO.getGstDbLcAmount();

	            ledgerSumMap.put(ledger, ledgerSumMap.getOrDefault(ledger, BigDecimal.ZERO).add(lcAmount));
	        }

//	        // Add GST ledger entries
	        for (Map.Entry<String, BigDecimal> entry : ledgerSumMap.entrySet()) {
	            GroupLedgerVO groupLedgerVO = groupLedgerRepo.findByAccountGroupName(entry.getKey());
	            
	            AccountsDetailsVO gstAccountDetailsVO = new AccountsDetailsVO();
	            gstAccountDetailsVO.setACategory(groupLedgerVO.getCategory());
	            gstAccountDetailsVO.setNDebitAmount(entry.getValue());
	            gstAccountDetailsVO.setDebitAmount(entry.getValue());
	            gstAccountDetailsVO.setNCreditAmount(BigDecimal.ZERO);
	            gstAccountDetailsVO.setCreditAmount(BigDecimal.ZERO);
	            gstAccountDetailsVO.setArapFlag(false);
	            gstAccountDetailsVO.setArapAmount(BigDecimal.ZERO);
	            gstAccountDetailsVO.setBDebitAmount(entry.getValue());
	            gstAccountDetailsVO.setBCrAmount(BigDecimal.ZERO);
	            gstAccountDetailsVO.setBArapAmount(BigDecimal.ZERO);
	            gstAccountDetailsVO.setAccountName(groupLedgerVO.getAccountGroupName());
	            gstAccountDetailsVO.setACurrency(irnCreditNoteVO.getBillCurr());
	            gstAccountDetailsVO.setAExRate(irnCreditNoteVO.getBillCurrRate());
	            gstAccountDetailsVO.setSubledgerName("None");
	            gstAccountDetailsVO.setSubLedgerCode("None");
	            gstAccountDetailsVO.setNArapAmount(BigDecimal.ZERO);
	            gstAccountDetailsVO.setGstflag(3);
	            gstAccountDetailsVO.setAccountsVO(accountsVO);
	            accountsDetailsVOs.add(gstAccountDetailsVO);
	        }

	        accountsVO.setAccountsDetailsVO(accountsDetailsVOs);

//	        // Save AccountsVO and update TaxInvoiceVO
	        AccountsVO savedAccountsVO = accountsRepo.save(accountsVO);
	        irnCreditNoteVO.setVoucherNo(savedAccountsVO.getDocId());
	        irnCreditNoteVO.setVoucherDate(savedAccountsVO.getDocDate());
	        irnCreditNoteVO.setApproveStatus(action);
	        irnCreditNoteVO.setApproveBy(actionBy);
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
	        irnCreditNoteVO.setApproveOn(LocalDateTime.now().format(formatter).toUpperCase());
	        
	        return irnCreditRepo.save(irnCreditNoteVO);

	    } else if (irnCreditNoteVO.getApproveStatus().equals("Approved")) {
	        throw new ApplicationException("This Credit Note Already Approved,");
	    } else {
	        throw new ApplicationException("This Credit Note Already Rejected");
	    }
	    }
}
