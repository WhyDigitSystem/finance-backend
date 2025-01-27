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

import com.base.basesetup.dto.ChargerCostDebitNoteDTO;
import com.base.basesetup.dto.CostDebitNoteDTO;
import com.base.basesetup.dto.TdsCostDebitNoteDTO;
import com.base.basesetup.entity.AccountsDetailsVO;
import com.base.basesetup.entity.AccountsVO;
import com.base.basesetup.entity.ChargerCostDebitNoteVO;
import com.base.basesetup.entity.CostDebitNoteVO;
import com.base.basesetup.entity.DocumentTypeMappingDetailsVO;
import com.base.basesetup.entity.GroupLedgerVO;
import com.base.basesetup.entity.MultipleDocIdGenerationDetailsVO;
import com.base.basesetup.entity.TdsCostDebitNoteVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.AccountsRepo;
import com.base.basesetup.repo.ChargesCostDebitNoteRepo;
import com.base.basesetup.repo.CostDebitNoteRepo;
import com.base.basesetup.repo.DocumentTypeMappingDetailsRepo;
import com.base.basesetup.repo.GroupLedgerRepo;
import com.base.basesetup.repo.MultipleDocIdGenerationDetailsRepo;
import com.base.basesetup.repo.TdsCostDebitNoteRepo;

@Service
public class CostDebitNoteServiceImpl implements CostDebitNoteService {

	public static final Logger LOGGER = LoggerFactory.getLogger(CostDebitNoteServiceImpl.class);

	@Autowired
	CostDebitNoteRepo costDebitNoteRepo;

	@Autowired
	ChargesCostDebitNoteRepo chargesCostDebitNoteRepo;

	@Autowired
	TdsCostDebitNoteRepo tdsCostDebitNoteRepo;

	@Autowired
	DocumentTypeMappingDetailsRepo documentTypeMappingDetailsRepo;

	@Autowired
	AccountsRepo accountsRepo;

	@Autowired
	GroupLedgerRepo groupLedgerRepo;

	@Autowired
	MultipleDocIdGenerationDetailsRepo multipleDocIdGenerationDetailsRepo;

	@Override
	public Map<String, Object> updateCreateCostDebitNote(@Valid CostDebitNoteDTO costDebitNoteDTO)
			throws ApplicationException {

		String message = null;
		String screenCode = "CDN";
		CostDebitNoteVO costDebitNoteVO;

		if (ObjectUtils.isEmpty(costDebitNoteDTO.getId())) {
			costDebitNoteVO = new CostDebitNoteVO();

			// GETDOCID API
			String docId = costDebitNoteRepo.getCostDebitNoteDocId(costDebitNoteDTO.getOrgId(),
					costDebitNoteDTO.getFinYear(), costDebitNoteDTO.getBranchCode(), screenCode);
			costDebitNoteVO.setDocId(docId);

			// GETDOCID LASTNO +1
			DocumentTypeMappingDetailsVO documentTypeMappingDetailsVO = documentTypeMappingDetailsRepo
					.findByOrgIdAndFinYearAndBranchCodeAndScreenCode(costDebitNoteDTO.getOrgId(),
							costDebitNoteDTO.getFinYear(), costDebitNoteDTO.getBranchCode(), screenCode);
			documentTypeMappingDetailsVO.setLastno(documentTypeMappingDetailsVO.getLastno() + 1);
			documentTypeMappingDetailsRepo.save(documentTypeMappingDetailsVO);

			costDebitNoteVO.setCreatedBy(costDebitNoteDTO.getCreatedBy());
			costDebitNoteVO.setUpdatedBy(costDebitNoteDTO.getCreatedBy());

			message = "CostDebitNote Creation Successfully";

		} else {
			costDebitNoteVO = costDebitNoteRepo.findById(costDebitNoteDTO.getId()).orElseThrow(
					() -> new ApplicationException("Cost DebitNote Not Found with id: " + costDebitNoteDTO.getId()));
			costDebitNoteVO.setUpdatedBy(costDebitNoteDTO.getCreatedBy());

			message = "CostDebitNote Updation Successfully";
		}

		costDebitNoteVO = getCostDebitNoteVOFromCostDebitNoteDTO(costDebitNoteVO, costDebitNoteDTO);
		costDebitNoteRepo.save(costDebitNoteVO);

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("costDebitNoteVO", costDebitNoteVO);
		return response;
	}

	private CostDebitNoteVO getCostDebitNoteVOFromCostDebitNoteDTO(CostDebitNoteVO costDebitNoteVO,
			@Valid CostDebitNoteDTO costDebitNoteDTO) {

		costDebitNoteVO.setProduct(costDebitNoteDTO.getProduct());
		costDebitNoteVO.setSupplierBillNo(costDebitNoteDTO.getSupplierBillNo());
		costDebitNoteVO.setSupplierType(costDebitNoteDTO.getSupplierType());
		costDebitNoteVO.setSupplierCode(costDebitNoteDTO.getSupplierCode());
		costDebitNoteVO.setCreditDays(costDebitNoteDTO.getCreditDays());
		costDebitNoteVO.setDueDate(costDebitNoteDTO.getDueDate());
		costDebitNoteVO.setSupplierName(costDebitNoteDTO.getSupplierName());
		costDebitNoteVO.setSupplierPlace(costDebitNoteDTO.getSupplierPlace());
		costDebitNoteVO.setCurrency(costDebitNoteDTO.getCurrency());
		costDebitNoteVO.setExRate(costDebitNoteDTO.getExRate());
		costDebitNoteVO.setSupplierGstIn(costDebitNoteDTO.getSupplierGstIn());
		costDebitNoteVO.setSupplierGstInCode(costDebitNoteDTO.getSupplierGstInCode());
		costDebitNoteVO.setRemarks(costDebitNoteDTO.getRemarks());
		costDebitNoteVO.setAddress(costDebitNoteDTO.getAddress());
		costDebitNoteVO.setOtherInfo(costDebitNoteDTO.getOtherInfo());
		costDebitNoteVO.setShipperRefNo(costDebitNoteDTO.getShipperRefNo());
		costDebitNoteVO.setGstType(costDebitNoteDTO.getGstType());
		costDebitNoteVO.setOrgId(costDebitNoteDTO.getOrgId());
		costDebitNoteVO.setCreatedBy(costDebitNoteDTO.getCreatedBy());
		costDebitNoteVO.setCancelRemarks(costDebitNoteDTO.getCancelRemarks());
		costDebitNoteVO.setBranch(costDebitNoteDTO.getBranch());
		costDebitNoteVO.setBranchCode(costDebitNoteDTO.getBranchCode());
		costDebitNoteVO.setCustomer(costDebitNoteDTO.getCustomer());
		costDebitNoteVO.setClient(costDebitNoteDTO.getClient());
		costDebitNoteVO.setFinYear(costDebitNoteDTO.getFinYear());
		costDebitNoteVO.setPayment(costDebitNoteDTO.getPayment());
		costDebitNoteVO.setAccuralid(costDebitNoteDTO.getAccuralid());
		costDebitNoteVO.setUtrRef(costDebitNoteDTO.getUtrRef());
		costDebitNoteVO.setCostType(costDebitNoteDTO.getCostType());
		costDebitNoteVO.setOrginBill(costDebitNoteDTO.getOrginBill());
		costDebitNoteVO.setOrginBillDate(costDebitNoteDTO.getOriginBillDate());

		// Set individual fields from DTO to VO

		// Deleting existing entries if updating
		if (costDebitNoteDTO.getId() != null) {

			List<TdsCostDebitNoteVO> costDebitNoteTaxPrtculVOs = tdsCostDebitNoteRepo
					.findByCostDebitNoteVO(costDebitNoteVO);
			tdsCostDebitNoteRepo.deleteAll(costDebitNoteTaxPrtculVOs);

			List<ChargerCostDebitNoteVO> costDebitChargesVOs = chargesCostDebitNoteRepo
					.findByCostDebitNoteVO(costDebitNoteVO);
			chargesCostDebitNoteRepo.deleteAll(costDebitChargesVOs);
		}

		BigDecimal sumBillAmount = BigDecimal.ZERO;
		BigDecimal sumLcAmount = BigDecimal.ZERO;

		BigDecimal taxAmount = BigDecimal.ZERO;
		BigDecimal tdsAmount = BigDecimal.ZERO;

		List<ChargerCostDebitNoteVO> chargerCostDebitVOs = new ArrayList<>();

		// Map to store IGST sums by GST percentage
		Map<String, BigDecimal> igstCategorySumMap = new HashMap<>();
		Map<String, BigDecimal> cgstCategorySumMap = new HashMap<>();

		for (ChargerCostDebitNoteDTO chargerCostDebitDTO : costDebitNoteDTO.getCostDebitChargesDTO()) {
			ChargerCostDebitNoteVO chargerCostDebitVO = new ChargerCostDebitNoteVO();

			chargerCostDebitVO.setQty(chargerCostDebitDTO.getQty());
			chargerCostDebitVO.setRate(chargerCostDebitDTO.getRate());
			chargerCostDebitVO.setJobNo(chargerCostDebitDTO.getJobNo());
			chargerCostDebitVO.setChargeName(chargerCostDebitDTO.getChargeName());
			chargerCostDebitVO.setChargeCode(chargerCostDebitDTO.getChargeCode());
			chargerCostDebitVO.setChargeLedger(chargerCostDebitDTO.getChargeLedger());
			chargerCostDebitVO.setLedger(chargerCostDebitDTO.getLedger());
			chargerCostDebitVO.setSac(chargerCostDebitDTO.getSac());
			chargerCostDebitVO.setCurrency(chargerCostDebitDTO.getCurrency());
			chargerCostDebitVO.setExRate(chargerCostDebitDTO.getExRate());
			chargerCostDebitVO.setGst(chargerCostDebitDTO.getGst());
			chargerCostDebitVO.setGovChargeCode(chargerCostDebitDTO.getGovChargeCode());
			chargerCostDebitVO.setExempted(chargerCostDebitDTO.getExempted());
			chargerCostDebitVO.setTaxable(chargerCostDebitDTO.getTaxable());
			chargerCostDebitVO.setGSTPercent(chargerCostDebitDTO.getGstPercent());

//			FIELD DECLARATION
			BigDecimal fcAmount = BigDecimal.ZERO;
			BigDecimal lcAmount;
			BigDecimal billAmount;
			BigDecimal gstAmount = BigDecimal.ZERO;

//			TO CHECK THE CURRENCY 
			if (!chargerCostDebitDTO.getCurrency().equals("INR")) {
				BigDecimal rate = chargerCostDebitDTO.getRate(); // BigDecimal type is expected here
				BigDecimal qty = BigDecimal.valueOf(chargerCostDebitDTO.getQty()); // Convert qty to BigDecimal
				fcAmount = rate.multiply(qty);
				chargerCostDebitVO.setFcAmt(fcAmount);
			} else {
				fcAmount = BigDecimal.valueOf(0.00);
				chargerCostDebitVO.setFcAmt(fcAmount);
			}

//			FIELD DECLARATION
			BigDecimal exRate = chargerCostDebitDTO.getExRate();
			BigDecimal qty = BigDecimal.valueOf(chargerCostDebitDTO.getQty());
			BigDecimal rate = chargerCostDebitDTO.getRate();
			BigDecimal gstPercent = BigDecimal.valueOf(chargerCostDebitDTO.getGstPercent());

//			LC AMOUNT CALCULATION
			lcAmount = exRate.multiply(qty.multiply(rate));
			chargerCostDebitVO.setLcAmt(lcAmount);
			sumLcAmount = sumLcAmount.add(lcAmount); // TDS Purpose

//			BILL AMOUNT CALCULATION
			billAmount = lcAmount.divide(exRate, RoundingMode.HALF_UP);
			chargerCostDebitVO.setBillAmt(billAmount);
			sumBillAmount = sumBillAmount.add(billAmount);

//			GST AMOUNT CALCULATION
			gstAmount = lcAmount.multiply(gstPercent).divide(BigDecimal.valueOf(100));
			chargerCostDebitVO.setGstAmount(gstAmount);

//			AGGREGATE IGST SUMS BY GST PERCENTAGE
			if (costDebitNoteDTO.getGstType().equalsIgnoreCase("INTER") && gstPercent.compareTo(BigDecimal.ZERO) > 0) {
				String igstCategoryKey = gstPercent.toString();
				igstCategorySumMap.put(igstCategoryKey,
						igstCategorySumMap.getOrDefault(igstCategoryKey, BigDecimal.ZERO).add(gstAmount));
			}
			if (costDebitNoteDTO.getGstType().equalsIgnoreCase("INTRA") && gstPercent.compareTo(BigDecimal.ZERO) > 0) {
				String gstCategoryKey = gstPercent.toString();
				cgstCategorySumMap.put(gstCategoryKey,
						cgstCategorySumMap.getOrDefault(gstCategoryKey, BigDecimal.ZERO).add(gstAmount));
			}

			chargerCostDebitVO.setCostDebitNoteVO(costDebitNoteVO);
			chargerCostDebitVOs.add(chargerCostDebitVO);
		}

//		ADD IGST ROWS FOR EACH IGST PERCENTAGE IN igstCategorySumMap
		if ("INTER".equalsIgnoreCase(costDebitNoteDTO.getGstType())) {
			for (Map.Entry<String, BigDecimal> entry : igstCategorySumMap.entrySet()) {
				ChargerCostDebitNoteVO igstSummaryVO = new ChargerCostDebitNoteVO();

				String gstPercent = entry.getKey();
				BigDecimal igstLcAmount = entry.getValue();
				taxAmount = taxAmount.add(igstLcAmount);

				Set<Object[]> chargeVO = costDebitNoteRepo.findInterAndIntraDetailsForCostInvoicePosting(
						costDebitNoteDTO.getOrgId(), costDebitNoteDTO.getGstType(), gstPercent);

				if (!chargeVO.isEmpty()) {
					Object[] chargeVOSet = chargeVO.iterator().next(); // Get the first element in the set
					String chargeDesc = (String) chargeVOSet[0];
					Float gstPer = ((Double) chargeVOSet[1]).floatValue();
					String currency = (String) chargeVOSet[2];
					String ledger = (String) chargeVOSet[0];
					igstSummaryVO.setChargeName(chargeDesc);
					igstSummaryVO.setCurrency(currency);
					igstSummaryVO.setLedger(ledger);
					igstSummaryVO.setGSTPercent(gstPer);
				}
				igstSummaryVO.setQty(Integer.valueOf(1));
				igstSummaryVO.setRate(BigDecimal.ONE);
				igstSummaryVO.setExRate(BigDecimal.ONE);
				igstSummaryVO.setFcAmt(BigDecimal.ONE);
				igstSummaryVO.setLcAmt(igstLcAmount);
				igstSummaryVO.setFcAmt(BigDecimal.ONE);
				igstSummaryVO.setBillAmt(BigDecimal.ONE);
				igstSummaryVO.setGstAmount(BigDecimal.ONE);
				igstSummaryVO.setCostDebitNoteVO(costDebitNoteVO);

				chargerCostDebitVOs.add(igstSummaryVO);
			}
		}

//		ADD CGST and SGST ROWS FOR EACH GST PERCENTAGE IN cgstCategorySumMap
		if ("INTRA".equalsIgnoreCase(costDebitNoteDTO.getGstType())) {
			for (Map.Entry<String, BigDecimal> entry : cgstCategorySumMap.entrySet()) {

				String gstPercent = entry.getKey();
				BigDecimal intraPercent = new BigDecimal(gstPercent).divide(BigDecimal.valueOf(2));
				System.out.println("PARAM" + intraPercent);
				BigDecimal totalTaxAmount = entry.getValue();

				BigDecimal cgstAmount = totalTaxAmount.divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP);
				taxAmount = taxAmount.add(cgstAmount);
				BigDecimal sgstAmount = totalTaxAmount.divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP);
				taxAmount = taxAmount.add(sgstAmount);

				Set<Object[]> chargeVO = costDebitNoteRepo
						.findChargeNameAndChargeCodeForCgstAndSgtsPosting(costDebitNoteDTO.getOrgId(), intraPercent);

				if (!chargeVO.isEmpty()) {

//					TO HOLD THE FIRST TWO LIST
					Object[] cgstRecord = null;
					Object[] sgstRecord = null;

					// Iterate through the query results
					for (Object[] chargeVOSet : chargeVO) {
						String chargeCode = (String) chargeVOSet[1];

						// Determine CGST and SGST records based on the charge code
						if (chargeCode.contains("CGST")) {
							cgstRecord = chargeVOSet; // Set CGST record
						} else if (chargeCode.contains("SGST")) {
							sgstRecord = chargeVOSet; // Set SGST record
						}

						if (cgstRecord != null && sgstRecord != null) {

							ChargerCostDebitNoteVO cgstSummaryVO = new ChargerCostDebitNoteVO();
							cgstSummaryVO.setChargeName((String) cgstRecord[0]);
							cgstSummaryVO.setGSTPercent((Float) cgstRecord[1]);
							cgstSummaryVO.setCurrency((String) cgstRecord[2]);
							cgstSummaryVO.setLedger((String) cgstRecord[0]);
							cgstSummaryVO.setQty(Integer.valueOf(1));
							cgstSummaryVO.setRate(BigDecimal.ONE);
							cgstSummaryVO.setExRate(BigDecimal.ONE);
							cgstSummaryVO.setFcAmt(BigDecimal.ONE);
							cgstSummaryVO.setLcAmt(cgstAmount);
							cgstSummaryVO.setBillAmt(BigDecimal.ONE);
							cgstSummaryVO.setGstAmount(BigDecimal.ONE);
							cgstSummaryVO.setCostDebitNoteVO(costDebitNoteVO);
							chargerCostDebitVOs.add(cgstSummaryVO);

							ChargerCostDebitNoteVO sgstSummaryVO = new ChargerCostDebitNoteVO();
							sgstSummaryVO.setChargeName((String) sgstRecord[0]);
							sgstSummaryVO.setGSTPercent((Float) sgstRecord[1]);
							sgstSummaryVO.setCurrency((String) sgstRecord[2]);
							sgstSummaryVO.setLedger((String) sgstRecord[0]);
							sgstSummaryVO.setQty(Integer.valueOf(1));
							sgstSummaryVO.setRate(BigDecimal.ONE);
							sgstSummaryVO.setExRate(BigDecimal.ONE);
							sgstSummaryVO.setFcAmt(BigDecimal.ONE);
							sgstSummaryVO.setLcAmt(sgstAmount);
							sgstSummaryVO.setBillAmt(BigDecimal.ONE);
							sgstSummaryVO.setGstAmount(BigDecimal.ONE);
							sgstSummaryVO.setCostDebitNoteVO(costDebitNoteVO);

							chargerCostDebitVOs.add(sgstSummaryVO);

							break;
						}
					}
				}
			}
		}

		Map<String, BigDecimal> ledgerSumMap = new HashMap<>();
		for (ChargerCostDebitNoteVO detailsVO : chargerCostDebitVOs) {
			String ledger = detailsVO.getLedger();
			BigDecimal lcAmount = detailsVO.getLcAmt();
			ledgerSumMap.put(ledger, ledgerSumMap.getOrDefault(ledger, BigDecimal.ZERO).add(lcAmount));
		}

		costDebitNoteVO.setCostDebitChargesVO(chargerCostDebitVOs);

		// TDS table
		List<TdsCostDebitNoteVO> tdsCostDebitVOs = new ArrayList<>();
		for (TdsCostDebitNoteDTO tdsCostDebitDTO : costDebitNoteDTO.getCostDebitNoteTaxPrtculDTO()) {
			TdsCostDebitNoteVO tdsCostDebitVO = new TdsCostDebitNoteVO();

			tdsCostDebitVO.setTdsWithHolding(tdsCostDebitDTO.getTdsWithHolding());
			tdsCostDebitVO.setTdsWithHoldingPer(tdsCostDebitDTO.getTdsWithHoldingPer());
			tdsCostDebitVO.setSection(tdsCostDebitDTO.getSection());
//			tdsCostInvoiceVO.setTotTdsWhAmnt(tdsCostInvoiceDTO.getTotTdsWhAmnt());

			BigDecimal totTdsWhAmt = BigDecimal.ZERO;
			BigDecimal tdsWhPercent = tdsCostDebitDTO.getTdsWithHoldingPer();
			System.out.println("TOTAL LC AMOUNT IS :" + sumLcAmount);
			totTdsWhAmt = sumLcAmount.multiply(tdsWhPercent.divide(BigDecimal.valueOf(100)));
			tdsCostDebitVO.setTotTdsWhAmnt(totTdsWhAmt);
			tdsAmount = totTdsWhAmt;

			tdsCostDebitVO.setCostDebitNoteVO(costDebitNoteVO);
			tdsCostDebitVOs.add(tdsCostDebitVO);
		}
		costDebitNoteVO.setTdsCostDebitNoteVO(tdsCostDebitVOs);

		// SUMMARY CALCULATION
		BigDecimal totChargeAmtBillCurr = sumBillAmount;
		BigDecimal totChargeAmtLc = sumLcAmount;
		BigDecimal netAmountBillCurr = sumBillAmount.subtract(tdsAmount).add(taxAmount);
		BigDecimal netAmountLc = taxAmount.subtract(tdsAmount).add(sumLcAmount);
		BigDecimal actBillAmtLc = sumLcAmount.subtract(tdsAmount).add(taxAmount);
		BigDecimal actBillAmtBillCurr = sumBillAmount.add(taxAmount);
		BigDecimal sumDebitAmount = sumLcAmount.add(taxAmount);

		costDebitNoteVO.setTotChargesBillCurrAmt(totChargeAmtBillCurr);
		costDebitNoteVO.setTotChargesLcAmt(totChargeAmtLc);
		costDebitNoteVO.setNetBillCurrAmt(netAmountBillCurr);
		costDebitNoteVO.setNetBillLcAmt(netAmountLc);
		costDebitNoteVO.setActBillCurrAmt(actBillAmtBillCurr);
		costDebitNoteVO.setActBillLcAmt(actBillAmtLc);
		costDebitNoteVO.setSumLcAmt(sumDebitAmount);
		costDebitNoteVO.setCostDebitChargesVO(chargerCostDebitVOs);

		return costDebitNoteVO;
	}

	@Override
	public List<CostDebitNoteVO> getCostDebitNoteByOrgId(Long orgId) {

		return costDebitNoteRepo.getByCostDebitNoteByOrgId(orgId);
	}

	@Override
	public List<CostDebitNoteVO> getCostDebitNoteById(Long id) {
		return costDebitNoteRepo.getByCostDebitNoteById(id);
	}

	@Override
	public List<CostDebitNoteVO> getActiveCostDebitNote() {
		return costDebitNoteRepo.getActiveCostDebitNote();
	}

	@Override
	public CostDebitNoteVO getAllCostDebitNoteByDocId(Long orgId, String docId) {
		return costDebitNoteRepo.findAllCostDebitNoteByDocId(orgId, docId);
	}

	// 8778426636
	@Override
	public String getCostDebitNoteDocId(Long orgId, String finYear, String branch, String branchCode) {
		String ScreenCode = "CDN";
		String result = costDebitNoteRepo.getCostDebitNoteDocId(orgId, finYear, branchCode, ScreenCode);
		return result;
	}

	@Override
	public List<Map<String, Object>> chargeTypeDetailsForCostDebitNote(Long orgId) {
		Set<Object[]> getChargeTypeDetails = costDebitNoteRepo.getChareDetails(orgId);
		return getChargeType(getChargeTypeDetails); // Call the correct method
	}

	private List<Map<String, Object>> getChargeType(Set<Object[]> getCharge) {
		List<Map<String, Object>> gridDetails = new ArrayList<>();
		for (Object[] grid : getCharge) {
			Map<String, Object> details = new HashMap<>();
			details.put("chargeType", grid[0] != null ? grid[0].toString() : "");
			details.put("chargeCode", grid[1] != null ? grid[1].toString() : "");
			details.put("govtSac", grid[2] != null ? grid[2].toString() : "");
			details.put("serviceAccountCode", grid[3] != null ? grid[3].toString() : ""); // Updated to match
																							// "serviceAccountCode"

			gridDetails.add(details);
		}
		return gridDetails;
	}

	@Override
	public List<Map<String, Object>> partyDetailsForCostDebitNote(Long orgId, String branch, String finYear) {
		// Fetch party details from the repository
		Set<Object[]> getPartyTypeDetails = costDebitNoteRepo.getParty(orgId, branch, finYear);
		// Return processed party details
		return getPartyDetails(getPartyTypeDetails);
	}

	private List<Map<String, Object>> getPartyDetails(Set<Object[]> getPartyInformation) {
		List<Map<String, Object>> gridDetails = new ArrayList<>();
		// Iterate through the result set
		for (Object[] grid : getPartyInformation) {

			Map<String, Object> details = new HashMap<>();
			details.put("partyName", grid[0] != null ? grid[0].toString() : "");
			details.put("partyCode", grid[1] != null ? grid[1].toString() : "");
			details.put("addressType", grid[2] != null ? grid[2].toString() : "");

			gridDetails.add(details);

		}
		return gridDetails; // Return the list of party details
	}

	@Override
	public List<Map<String, Object>> getAllDocIdForCostInvoice(Long orgId) {
		Set<Object[]> getAllDocId = costDebitNoteRepo.getDocIdForCI(orgId);
		// Return processed party details
		return getAllCIDocId(getAllDocId);
	}

	private List<Map<String, Object>> getAllCIDocId(Set<Object[]> getDocIdInformation) {
		List<Map<String, Object>> gridDetails = new ArrayList<>();
		// Iterate through the result set
		for (Object[] grid : getDocIdInformation) {

			Map<String, Object> details = new HashMap<>();
			details.put("docId", grid[0] != null ? grid[0].toString() : "");
			gridDetails.add(details);

		}
		return gridDetails; // Return the list of party details
	}

	@Override
	public List<Map<String, Object>> getCurrencyAndExrates(Long orgId) {
		Set<Object[]> currency = costDebitNoteRepo.getCurrencyAndExrateDetails(orgId);
		return getCurrency(currency);
	}

	private List<Map<String, Object>> getCurrency(Set<Object[]> currency) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : currency) {
			Map<String, Object> map = new HashMap<>();
			map.put("currency", ch[0] != null ? ch[0].toString() : ""); // Empty string if null
			map.put("currencyDescription", ch[1] != null ? ch[1].toString() : "");
			map.put("buyingExRate", ch[2] != null ? ch[2].toString() : "");
			map.put("sellingExRate", ch[3] != null ? ch[3].toString() : "");
			List1.add(map);
		}
		return List1;
	}

	@Override
	public List<CostDebitNoteVO> getOrginBillNoByParty(Long orgId, String party, String branchCode) {
		return costDebitNoteRepo.findOrginBillNoByParty(orgId, party, branchCode);
	}

	@Override
	public List<Map<String, Object>> partyTypeForCostDebitNote(Long orgId, String branch, String finYear) {
		// Fetch party details from the repository
		Set<Object[]> getAllPartyType = costDebitNoteRepo.partyTypeForCostDebitNote(orgId, branch, finYear);
		// Return processed party details
		return getPartyType(getAllPartyType);
	}

	private List<Map<String, Object>> getPartyType(Set<Object[]> getPartyInformation) {
		List<Map<String, Object>> gridDetails = new ArrayList<>();
		// Iterate through the result set
		for (Object[] grid : getPartyInformation) {

			Map<String, Object> details = new HashMap<>();
			details.put("partyType", grid[0] != null ? grid[0].toString() : "");
			gridDetails.add(details);

		}
		return gridDetails; // Return the list of party details
	}

	@Override
	public CostDebitNoteVO approveCostDebitNote(Long orgId, Long id, String docId, String action, String actionBy)
			throws ApplicationException {
		// Fetch the TaxInvoiceVO from the repository
		CostDebitNoteVO costDebitNoteVO = costDebitNoteRepo.findByOrgIdAndIdAndDocId(orgId, id, docId);
		String screenCode = "AC";
		String sourceScreenCode = costDebitNoteVO.getScreenCode();

		// Validate the approval status of the invoice
		if (costDebitNoteVO.getApproveStatus() == null || (!costDebitNoteVO.getApproveStatus().equals("Approved")
				&& !costDebitNoteVO.getApproveStatus().equals("Rejected"))) {

			String accountsDocId = accountsRepo.getApproveDocId(costDebitNoteVO.getOrgId(),
					costDebitNoteVO.getFinYear(), costDebitNoteVO.getBranchCode(), sourceScreenCode, screenCode);
			costDebitNoteVO.setDocId(docId);

			System.out.println(accountsDocId);
			// GETDOCID LASTNO +1
			MultipleDocIdGenerationDetailsVO multipleDocIdGenerationDetailsVO = multipleDocIdGenerationDetailsRepo
					.findByOrgIdAndFinYearAndBranchCodeAndSourceScreenCodeAndScreenCode(costDebitNoteVO.getOrgId(),
							costDebitNoteVO.getFinYear(), costDebitNoteVO.getBranchCode(), sourceScreenCode,
							screenCode);
			System.out.println(multipleDocIdGenerationDetailsVO.getLastno());
			multipleDocIdGenerationDetailsVO.setLastno(multipleDocIdGenerationDetailsVO.getLastno() + 1);
			multipleDocIdGenerationDetailsRepo.save(multipleDocIdGenerationDetailsVO);

			// Create AccountsVO object and populate its fields
			AccountsVO accountsVO = new AccountsVO();
			accountsVO.setDocId(accountsDocId);
			accountsVO.setSourceScreen(costDebitNoteVO.getScreenName());
			accountsVO.setSourceScreenCode(costDebitNoteVO.getScreenCode());
			accountsVO.setSourceId(costDebitNoteVO.getId());
			accountsVO.setCreatedBy(costDebitNoteVO.getCreatedBy());
			accountsVO.setModifiedBy(costDebitNoteVO.getUpdatedBy());
			accountsVO.setOrgId(costDebitNoteVO.getOrgId());
			accountsVO.setBranch(costDebitNoteVO.getBranch());
			accountsVO.setBranchCode(costDebitNoteVO.getBranchCode());
			accountsVO.setModifiedon(costDebitNoteVO.getCommonDate().getModifiedon().toUpperCase());
			accountsVO.setCreatedon(costDebitNoteVO.getCommonDate().getModifiedon().toUpperCase());
			accountsVO.setRefNo(costDebitNoteVO.getDocId());
			accountsVO.setRefDate(costDebitNoteVO.getDocDate());
			accountsVO.setCurrency(costDebitNoteVO.getCurrency());
			accountsVO.setExRate(costDebitNoteVO.getExRate());
			accountsVO.setRemarks(costDebitNoteVO.getRemarks());
//			accountsVO.setBillMonth(costDebitNoteVO.getbil());
			accountsVO.setFinYear(costDebitNoteVO.getFinYear());

			// Calculate total debit/credit amounts
			BigDecimal totalDebitAmount = costDebitNoteVO.getTotChargesLcAmt().add(costDebitNoteVO.getGstInputLcAmt());
			accountsVO.setTotalDebitAmount(totalDebitAmount);
			accountsVO.setTotalCreditAmount(totalDebitAmount);
			accountsVO.setCreditDays(costDebitNoteVO.getCreditDays());
//			accountsVO.setAmountInWords(costDebitNoteVO.getAmountInWords());
			accountsVO.setStTaxAmount(costDebitNoteVO.getGstInputLcAmt());
			accountsVO.setChargeableAmount(costDebitNoteVO.getTotChargesLcAmt());
//			accountsVO.setSalesType(costDebitNoteVO.getSalesType());

			// Create AccountsDetailsVO list and populate it
			List<AccountsDetailsVO> accountsDetailsVOs = new ArrayList<>();

			// Add PAYABLE A/C entry
			AccountsDetailsVO accountsDetailsVO = new AccountsDetailsVO();
			accountsDetailsVO.setNDebitAmount(costDebitNoteVO.getSumLcAmt());
			accountsDetailsVO.setACategory("PAYABLE A/C");
			accountsDetailsVO.setDebitAmount(costDebitNoteVO.getSumLcAmt());
			accountsDetailsVO.setNCreditAmount(BigDecimal.ZERO);
			accountsDetailsVO.setCreditAmount(BigDecimal.ZERO);
			accountsDetailsVO.setArapFlag(true);
			accountsDetailsVO.setArapAmount(costDebitNoteVO.getSumLcAmt().multiply(BigDecimal.valueOf(-1)));
			accountsDetailsVO.setBDebitAmount(costDebitNoteVO.getSumLcAmt());
			accountsDetailsVO.setBCrAmount(BigDecimal.ZERO);
			accountsDetailsVO.setBArapAmount(costDebitNoteVO.getSumLcAmt().multiply(BigDecimal.valueOf(-1)));
			accountsDetailsVO.setACurrency(costDebitNoteVO.getCurrency());
			accountsDetailsVO.setAExRate(costDebitNoteVO.getExRate());
			accountsDetailsVO.setSubledgerName(costDebitNoteVO.getSupplierName());
			accountsDetailsVO.setSubLedgerCode(costDebitNoteVO.getSupplierCode());
			accountsDetailsVO.setNArapAmount(costDebitNoteVO.getSumLcAmt().multiply(BigDecimal.valueOf(-1)));
			accountsDetailsVO.setGstflag(6);
			accountsDetailsVO.setAccountsVO(accountsVO);
			accountsDetailsVOs.add(accountsDetailsVO);

			if (costDebitNoteVO.getRoundOff() < 0) {
				accountsDetailsVO.setNDebitAmount(BigDecimal.valueOf(costDebitNoteVO.getRoundOff()));
				accountsDetailsVO.setACategory("PAYABLE A/C");
				accountsDetailsVO.setSubLedgerCode("None");
				accountsDetailsVO.setDebitAmount(BigDecimal.valueOf(costDebitNoteVO.getRoundOff()));
				accountsDetailsVO.setNCreditAmount(BigDecimal.ZERO);
				accountsDetailsVO.setCreditAmount(BigDecimal.ZERO);
				accountsDetailsVO.setArapFlag(false);
				accountsDetailsVO.setArapAmount(BigDecimal.valueOf(costDebitNoteVO.getRoundOff()));
				accountsDetailsVO.setBDebitAmount(BigDecimal.valueOf(costDebitNoteVO.getRoundOff()));
				accountsDetailsVO.setBCrAmount(BigDecimal.ZERO);
				accountsDetailsVO.setBArapAmount(BigDecimal.valueOf(costDebitNoteVO.getRoundOff()));
				accountsDetailsVO.setACurrency(costDebitNoteVO.getCurrency());
				accountsDetailsVO.setAExRate(costDebitNoteVO.getExRate());
				accountsDetailsVO.setSubledgerName("None");
				accountsDetailsVO.setNArapAmount(BigDecimal.valueOf(costDebitNoteVO.getRoundOff()));
				accountsDetailsVO.setGstflag(3);
				accountsDetailsVO.setAccountsVO(accountsVO);
				accountsDetailsVOs.add(accountsDetailsVO);
			}

			if (costDebitNoteVO.getRoundOff() > 0) {
				accountsDetailsVO.setNDebitAmount(BigDecimal.ZERO);
				accountsDetailsVO.setACategory("PAYABLE A/C");
				accountsDetailsVO.setSubLedgerCode("None");
				accountsDetailsVO.setDebitAmount(BigDecimal.valueOf(costDebitNoteVO.getRoundOff()));
				accountsDetailsVO.setNCreditAmount(BigDecimal.ZERO);
				accountsDetailsVO.setCreditAmount(BigDecimal.ZERO);
				accountsDetailsVO.setArapFlag(false);
				accountsDetailsVO.setArapAmount(BigDecimal.valueOf(costDebitNoteVO.getRoundOff()));
				accountsDetailsVO.setBDebitAmount(BigDecimal.valueOf(costDebitNoteVO.getRoundOff()));
				accountsDetailsVO.setBCrAmount(BigDecimal.ZERO);
				accountsDetailsVO.setBArapAmount(BigDecimal.valueOf(costDebitNoteVO.getRoundOff()));
				accountsDetailsVO.setACurrency(costDebitNoteVO.getCurrency());
				accountsDetailsVO.setAExRate(costDebitNoteVO.getExRate());
				accountsDetailsVO.setSubledgerName("None");
				accountsDetailsVO.setNArapAmount(BigDecimal.valueOf(costDebitNoteVO.getRoundOff()));
				accountsDetailsVO.setGstflag(3);
				accountsDetailsVO.setAccountsVO(accountsVO);
				accountsDetailsVOs.add(accountsDetailsVO);
			}

			// Group and process GST-related ledgers
			Map<String, BigDecimal> ledgerSumMap = new HashMap<>();
			for (ChargerCostDebitNoteVO gstVO : costDebitNoteVO.getCostDebitChargesVO()) {
				String ledger = gstVO.getChargeLedger();
				BigDecimal lcAmount = gstVO.getLcAmt();

				ledgerSumMap.put(ledger, ledgerSumMap.getOrDefault(ledger, BigDecimal.ZERO).add(lcAmount));
			}

			// Add GST ledger entries
			for (Map.Entry<String, BigDecimal> entry : ledgerSumMap.entrySet()) {
				GroupLedgerVO groupLedgerVO = groupLedgerRepo.findByAccountGroupName(entry.getKey());

				AccountsDetailsVO gstAccountDetailsVO = new AccountsDetailsVO();
				gstAccountDetailsVO.setACategory(groupLedgerVO.getCategory());
				gstAccountDetailsVO.setNDebitAmount(BigDecimal.ZERO);
				gstAccountDetailsVO.setDebitAmount(BigDecimal.ZERO);
				gstAccountDetailsVO.setNCreditAmount(entry.getValue());
				gstAccountDetailsVO.setCreditAmount(entry.getValue());
				gstAccountDetailsVO.setArapFlag(false);
				gstAccountDetailsVO.setArapAmount(BigDecimal.ZERO);
				gstAccountDetailsVO.setBDebitAmount(BigDecimal.ZERO);
				gstAccountDetailsVO.setBCrAmount(entry.getValue());
				gstAccountDetailsVO.setBArapAmount(BigDecimal.ZERO);
				gstAccountDetailsVO.setAccountName(groupLedgerVO.getAccountGroupName());
				gstAccountDetailsVO.setACurrency(costDebitNoteVO.getCurrency());
				gstAccountDetailsVO.setAExRate(costDebitNoteVO.getExRate());
				gstAccountDetailsVO.setSubledgerName("None");
				gstAccountDetailsVO.setSubLedgerCode("None");
				gstAccountDetailsVO.setNArapAmount(BigDecimal.ZERO);
				gstAccountDetailsVO.setGstflag(3);
				gstAccountDetailsVO.setAccountsVO(accountsVO);
				accountsDetailsVOs.add(gstAccountDetailsVO);
			}

			accountsVO.setAccountsDetailsVO(accountsDetailsVOs);

			// Save AccountsVO and update TaxInvoiceVO
			AccountsVO savedAccountsVO = accountsRepo.save(accountsVO);
			costDebitNoteVO.setPurVoucherNo(savedAccountsVO.getDocId());
			costDebitNoteVO.setPurVoucherDate(savedAccountsVO.getDocDate());
			costDebitNoteVO.setApproveStatus(action);
			costDebitNoteVO.setApproveBy(actionBy);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
			costDebitNoteVO.setApproveOn(LocalDateTime.now().format(formatter).toUpperCase());

			return costDebitNoteRepo.save(costDebitNoteVO);

		} else if (costDebitNoteVO.getApproveStatus().equals("Approved")) {
			throw new ApplicationException("This Invoice Already Approved,");
		} else {
			throw new ApplicationException("This Invoice Already Rejected");
		}
	}

	@Override
	public List<Map<String, Object>> getInterAndIntraDetailsForCostInvoice(Long orgId, String gstType,
			List<String> gstPercent) {
		Set<Object[]> chargeDetails = costDebitNoteRepo.findInterAndIntraDetailsForCostInvoice(orgId, gstType,
				gstPercent);
		return getChargeDe(chargeDetails);
	}

	private List<Map<String, Object>> getChargeDe(Set<Object[]> chDetails) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chDetails) {
			Map<String, Object> map = new HashMap<>();
			map.put("taxDesc", ch[0] != null ? ch[0].toString() : ""); // Empty string if null
			map.put("taxPercent", ch[1] != null ? ch[1].toString() : "");
			map.put("currency", ch[2] != null ? ch[2].toString() : "");

			List1.add(map);
		}
		return List1;

	}
}
