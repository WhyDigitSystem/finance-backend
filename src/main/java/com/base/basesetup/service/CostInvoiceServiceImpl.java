
package com.base.basesetup.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

import com.base.basesetup.dto.ChargerCostInvoiceDTO;
import com.base.basesetup.dto.CostInvoiceDTO;
import com.base.basesetup.dto.TdsCostInvoiceDTO;
import com.base.basesetup.entity.AccountsDetailsVO;
import com.base.basesetup.entity.AccountsVO;
import com.base.basesetup.entity.ArapDetailsVO;
import com.base.basesetup.entity.ChargerCostInvoiceVO;
import com.base.basesetup.entity.CostInvoiceVO;
import com.base.basesetup.entity.DocumentTypeMappingDetailsVO;
import com.base.basesetup.entity.GroupLedgerVO;
import com.base.basesetup.entity.MultipleDocIdGenerationDetailsVO;
import com.base.basesetup.entity.PartyMasterVO;
import com.base.basesetup.entity.TdsCostInvoiceVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.AccountsDetailsRepo;
import com.base.basesetup.repo.AccountsRepo;
import com.base.basesetup.repo.ArapDetailsRepo;
import com.base.basesetup.repo.ChargeTypeRequestRepo;
import com.base.basesetup.repo.ChargerCostInvoiceRepo;
import com.base.basesetup.repo.CostInvoiceRepo;
import com.base.basesetup.repo.DocumentTypeMappingDetailsRepo;
import com.base.basesetup.repo.GroupLedgerRepo;
import com.base.basesetup.repo.MultipleDocIdGenerationDetailsRepo;
import com.base.basesetup.repo.TdsCostInvoiceRepo;

@Service
public class CostInvoiceServiceImpl implements CostInvoiceService {

	public static final Logger LOGGER = LoggerFactory.getLogger(CostInvoiceServiceImpl.class);

	@Autowired
	CostInvoiceRepo costInvoiceRepo;

	@Autowired
	TdsCostInvoiceRepo tdsCostInvoiceRepo;

	@Autowired
	ArapDetailsRepo arapDetailsRepo;

	@Autowired
	ChargerCostInvoiceRepo chargerCostInvoiceRepo;

	@Autowired
	DocumentTypeMappingDetailsRepo documentTypeMappingDetailsRepo;

	@Autowired
	ChargeTypeRequestRepo chargeTypeRequestRepo;

	@Autowired
	AccountsRepo accountsRepo;

	@Autowired
	AccountsDetailsRepo accountsDetailsRepo;

	@Autowired
	MultipleDocIdGenerationDetailsRepo multipleDocIdGenerationDetailsRepo;

	@Autowired
	GroupLedgerRepo groupLedgerRepo;

	@Autowired
	AmountInWordsConverterService amountInWordsConverterService;

	// costInvoice

	@Override
	public List<CostInvoiceVO> getAllCostInvoiceByOrgId(Long orgId, String finYear, String branchCode) {

		List<CostInvoiceVO> costInvoiceVO = new ArrayList<>();
		costInvoiceVO = costInvoiceRepo.getAllCostInvoiceByOrgId(orgId, finYear, branchCode);

		return costInvoiceVO;
	}

	@Override
	public List<CostInvoiceVO> getAllCostInvoiceById(Long id) {
		List<CostInvoiceVO> costInvoiceVOList = new ArrayList<>();

		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received  CostInvoice BY Id : {}", id);
			costInvoiceVOList = costInvoiceRepo.getAllCostInvoiceById(id);

			for (CostInvoiceVO costInvoiceVO : costInvoiceVOList) {
				List<ChargerCostInvoiceVO> gstLines = new ArrayList<>();
				List<ChargerCostInvoiceVO> normalCharges = new ArrayList<>();

				// Iterate through the chargerCostInvoiceVO list and split charges
				for (ChargerCostInvoiceVO charge : costInvoiceVO.getChargerCostInvoiceVO()) {
					if (isGstCharge(charge)) {
						gstLines.add(charge); // Add GST related charges to gstLines
					} else {
						normalCharges.add(charge); // Add normal charges to normalCharges
					}
				}

				costInvoiceVO.setGstLines(gstLines);
				costInvoiceVO.setNormalCharges(normalCharges);
			}
		}
		return costInvoiceVOList;
	}

	private boolean isGstCharge(ChargerCostInvoiceVO charge) {
		// Check if chargeName contains "CGST", "SGST" or "IGST" to identify GST charges
		return charge.getChargeName() != null && (charge.getChargeName().contains("CGST")
				|| charge.getChargeName().contains("SGST") || charge.getChargeName().contains("IGST"));
	}

	@Override
	public List<CostInvoiceVO> getCostInvoiceByActive() {
		return costInvoiceRepo.findCostInvoiceByActive();
	}

	@Override
	public Map<String, Object> updateCreateCostInvoice(@Valid CostInvoiceDTO costInvoiceDTO)
			throws ApplicationException {
		String screenCode = "CI";
		CostInvoiceVO costInvoiceVO;
		String message = null;

		if (ObjectUtils.isEmpty(costInvoiceDTO.getId())) {

			costInvoiceVO = new CostInvoiceVO();

			if (costInvoiceRepo.existsByvIdAndOrgId(costInvoiceDTO.getVId(), costInvoiceDTO.getOrgId())) {

				String errorMessage = String.format("This VId: %s already exists for this organization.",
						costInvoiceDTO.getVId());
				throw new ApplicationException(errorMessage);
			}

			String docId = costInvoiceRepo.getCostInvoiceDocId(costInvoiceDTO.getOrgId(), costInvoiceDTO.getFinYear(),
					costInvoiceDTO.getBranchCode(), screenCode);
			costInvoiceVO.setDocId(docId);

			// GETDOCID LASTNO +1
			DocumentTypeMappingDetailsVO documentTypeMappingDetailsVO = documentTypeMappingDetailsRepo
					.findByOrgIdAndFinYearAndBranchCodeAndScreenCode(costInvoiceDTO.getOrgId(),
							costInvoiceDTO.getFinYear(), costInvoiceDTO.getBranchCode(), screenCode);
			documentTypeMappingDetailsVO.setLastno(documentTypeMappingDetailsVO.getLastno() + 1);
			documentTypeMappingDetailsRepo.save(documentTypeMappingDetailsVO);

			getCostInvoiceVOFromCostInvoiceDTO(costInvoiceVO, costInvoiceDTO);
			costInvoiceVO.setCreatedBy(costInvoiceDTO.getCreatedBy());
			costInvoiceVO.setUpdatedBy(costInvoiceDTO.getCreatedBy());

			message = "CostInvoice Creation Successfully";
		}

		else {

			costInvoiceVO = costInvoiceRepo.findById(costInvoiceDTO.getId()).orElseThrow(
					() -> new ApplicationException("Cost Invoice Not Found with id: " + costInvoiceDTO.getId()));
			costInvoiceVO.setUpdatedBy(costInvoiceDTO.getCreatedBy());

			if (!costInvoiceVO.getVId().equals(costInvoiceDTO.getVId())) {
				if (costInvoiceRepo.existsByvIdAndOrgId(costInvoiceDTO.getVId(), costInvoiceDTO.getOrgId())) {
					String errorMessage = String.format("This VId: %s already exists for this organization.",
							costInvoiceDTO.getVId());
					throw new ApplicationException(errorMessage);
				}
				costInvoiceVO.setVId(costInvoiceDTO.getVId());
			}

			getCostInvoiceVOFromCostInvoiceDTO(costInvoiceVO, costInvoiceDTO);
			message = "CostInvoice Updation Successfully";
		}

//	 getCostInvoiceVOFromCostInvoiceDTO(costInvoiceVO, costInvoiceDTO);
		costInvoiceRepo.save(costInvoiceVO);

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("costInvoiceVO", costInvoiceVO);
		return response;

	}

	private CostInvoiceVO getCostInvoiceVOFromCostInvoiceDTO(CostInvoiceVO costInvoiceVO,
			@Valid CostInvoiceDTO costInvoiceDTO) {

		costInvoiceVO.setMode(costInvoiceDTO.getMode());
		costInvoiceVO.setProduct(costInvoiceDTO.getProduct());
		costInvoiceVO.setSupplierBillNo(costInvoiceDTO.getSupplierBillNo());
		costInvoiceVO.setSupplierType(costInvoiceDTO.getSupplierType());
		costInvoiceVO.setSupplierCode(costInvoiceDTO.getSupplierCode());
		costInvoiceVO.setCreditDays(costInvoiceDTO.getCreditDays());
//		costInvoiceVO.setDueDate(costInvoiceDTO.getDueDate());
		costInvoiceVO.setSupplierName(costInvoiceDTO.getSupplierName());
		costInvoiceVO.setSupplierPlace(costInvoiceDTO.getSupplierPlace());
		costInvoiceVO.setCurrency(costInvoiceDTO.getCurrency());
		costInvoiceVO.setExRate(costInvoiceDTO.getExRate());
		costInvoiceVO.setSupplierGstIn(costInvoiceDTO.getSupplierGstIn());
		costInvoiceVO.setSupplierGstInCode(costInvoiceDTO.getSupplierGstInCode());
		costInvoiceVO.setRemarks(costInvoiceDTO.getRemarks());
		costInvoiceVO.setAddress(costInvoiceDTO.getAddress());
		costInvoiceVO.setOtherInfo(costInvoiceDTO.getOtherInfo());
		costInvoiceVO.setShipperRefNo(costInvoiceDTO.getShipperRefNo());
		costInvoiceVO.setGstType(costInvoiceDTO.getGstType());
		costInvoiceVO.setOrgId(costInvoiceDTO.getOrgId());
//		costInvoiceVO.setCreatedBy(costInvoiceDTO.getCreatedBy());
		costInvoiceVO.setCancelRemarks(costInvoiceDTO.getCancelRemarks());
		costInvoiceVO.setBranch(costInvoiceDTO.getBranch());
		costInvoiceVO.setBranchCode(costInvoiceDTO.getBranchCode());
		costInvoiceVO.setCustomer(costInvoiceDTO.getCustomer());
		costInvoiceVO.setClient(costInvoiceDTO.getClient());
		costInvoiceVO.setFinYear(costInvoiceDTO.getFinYear());
		costInvoiceVO.setPayment(costInvoiceDTO.getPayment());
		costInvoiceVO.setAccuralid(costInvoiceDTO.getAccuralid());
		costInvoiceVO.setUtrRef(costInvoiceDTO.getUtrRef());
		costInvoiceVO.setCostType(costInvoiceDTO.getCostType());
		costInvoiceVO.setSupplierId(costInvoiceDTO.getSupplierId());
		costInvoiceVO.setJobOrderNo(costInvoiceDTO.getJobOrderNo());
		costInvoiceVO.setVId(costInvoiceDTO.getVId());
		costInvoiceVO.setVDate(costInvoiceDTO.getVDate());

		if (costInvoiceDTO.getId() != null) {

			List<ChargerCostInvoiceVO> chargerCostInvoiceVOs = chargerCostInvoiceRepo
					.findByCostInvoiceVO(costInvoiceVO);
			chargerCostInvoiceRepo.deleteAll(chargerCostInvoiceVOs);

			List<TdsCostInvoiceVO> tdsCostInvoiceVOs = tdsCostInvoiceRepo.findByCostInvoiceVO(costInvoiceVO);
			tdsCostInvoiceRepo.deleteAll(tdsCostInvoiceVOs);

		}

		BigDecimal sumBillAmount = BigDecimal.ZERO;
		BigDecimal sumLcAmount = BigDecimal.ZERO;
		BigDecimal taxAmount = BigDecimal.ZERO;
		BigDecimal tdsAmount = BigDecimal.ZERO;

		List<ChargerCostInvoiceVO> chargerCostInvoiceVOs = new ArrayList<>();

		// Map to store IGST sums by GST percentage
		Map<String, BigDecimal> igstCategorySumMap = new HashMap<>();
		Map<String, BigDecimal> cgstCategorySumMap = new HashMap<>();

		for (ChargerCostInvoiceDTO chargerCostInvoiceDTO : costInvoiceDTO.getChargerCostInvoiceDTO()) {
			ChargerCostInvoiceVO chargerCostInvoiceVO = new ChargerCostInvoiceVO();

			chargerCostInvoiceVO.setQty(chargerCostInvoiceDTO.getQty());
			chargerCostInvoiceVO.setRate(chargerCostInvoiceDTO.getRate());
			chargerCostInvoiceVO.setJobNo(chargerCostInvoiceDTO.getJobNo());
			chargerCostInvoiceVO.setChargeName(chargerCostInvoiceDTO.getChargeName());
			chargerCostInvoiceVO.setChargeCode(chargerCostInvoiceDTO.getChargeCode());
			chargerCostInvoiceVO.setChargeLedger(chargerCostInvoiceDTO.getChargeLedger());
			chargerCostInvoiceVO.setSac(chargerCostInvoiceDTO.getSac());
			chargerCostInvoiceVO.setCurrency(chargerCostInvoiceDTO.getCurrency());
			chargerCostInvoiceVO.setExRate(chargerCostInvoiceDTO.getExRate());
			chargerCostInvoiceVO.setGst(chargerCostInvoiceDTO.getGst());
			chargerCostInvoiceVO.setGovChargeCode(chargerCostInvoiceDTO.getGovChargeCode());
			chargerCostInvoiceVO.setExempted(chargerCostInvoiceDTO.getExempted());
			chargerCostInvoiceVO.setTaxable(chargerCostInvoiceDTO.getTaxable());
			chargerCostInvoiceVO.setDescription(chargerCostInvoiceDTO.getDescription());
			chargerCostInvoiceVO.setGSTPercent(chargerCostInvoiceDTO.getGstPercent());
			chargerCostInvoiceVO.setLedger(chargerCostInvoiceDTO.getLedger());
			chargerCostInvoiceVO.setParty(chargerCostInvoiceDTO.getParty());

//			FIELD DECLARATION
			BigDecimal fcAmount = BigDecimal.ZERO;
			BigDecimal lcAmount;
			BigDecimal billAmount;
			BigDecimal gstAmount = BigDecimal.ZERO;

//			TO CHECK THE CURRENCY 
			if (!chargerCostInvoiceDTO.getCurrency().equals("INR")) {
				BigDecimal rate = chargerCostInvoiceDTO.getRate(); // BigDecimal type is expected here
				BigDecimal qty = BigDecimal.valueOf(chargerCostInvoiceDTO.getQty()); // Convert qty to BigDecimal
				fcAmount = rate.multiply(qty);
				chargerCostInvoiceVO.setFcAmt(fcAmount);
			} else {
				fcAmount = BigDecimal.valueOf(0.00);
				chargerCostInvoiceVO.setFcAmt(fcAmount);
			}

//			FIELD DECLARATION
			BigDecimal exRate = chargerCostInvoiceDTO.getExRate();
			BigDecimal qty = BigDecimal.valueOf(chargerCostInvoiceDTO.getQty());
			BigDecimal rate = chargerCostInvoiceDTO.getRate();
			BigDecimal gstPercent = BigDecimal.valueOf(chargerCostInvoiceDTO.getGstPercent());

//			LC AMOUNT CALCULATION
			lcAmount = exRate.multiply(qty.multiply(rate));
			chargerCostInvoiceVO.setLcAmt(lcAmount);
			sumLcAmount = sumLcAmount.add(lcAmount); // TDS Purpose

//			BILL AMOUNT CALCULATION
			billAmount = lcAmount.divide(exRate);
			chargerCostInvoiceVO.setBillAmt(billAmount);
			sumBillAmount = sumBillAmount.add(billAmount);

//			GST AMOUNT CALCULATION
			gstAmount = lcAmount.multiply(gstPercent).divide(BigDecimal.valueOf(100));
			chargerCostInvoiceVO.setGstAmount(gstAmount);

//			AGGREGATE IGST SUMS BY GST PERCENTAGE
			if (costInvoiceDTO.getGstType().equalsIgnoreCase("INTER") && gstPercent.compareTo(BigDecimal.ZERO) > 0) {
				String igstCategoryKey = gstPercent.toString();
				igstCategorySumMap.put(igstCategoryKey,
						igstCategorySumMap.getOrDefault(igstCategoryKey, BigDecimal.ZERO).add(gstAmount));
			}
			if (costInvoiceDTO.getGstType().equalsIgnoreCase("INTRA") && gstPercent.compareTo(BigDecimal.ZERO) > 0) {
				String gstCategoryKey = gstPercent.toString();
				cgstCategorySumMap.put(gstCategoryKey,
						cgstCategorySumMap.getOrDefault(gstCategoryKey, BigDecimal.ZERO).add(gstAmount));
			}

			chargerCostInvoiceVO.setCostInvoiceVO(costInvoiceVO);
			chargerCostInvoiceVOs.add(chargerCostInvoiceVO);
		}

//		ADD IGST ROWS FOR EACH IGST PERCENTAGE IN igstCategorySumMap
		if ("INTER".equalsIgnoreCase(costInvoiceDTO.getGstType())) {
			for (Map.Entry<String, BigDecimal> entry : igstCategorySumMap.entrySet()) {
				ChargerCostInvoiceVO igstSummaryVO = new ChargerCostInvoiceVO();

				String gstType = "INTER";
				Double gstPercent = Double.parseDouble(entry.getKey());
				BigDecimal igstLcAmount = entry.getValue();
//				if (igstSummaryVO.getGSTPercent() != 0) {
				taxAmount = taxAmount.add(igstLcAmount);

				Set<Object[]> groupLedgerVOs = costInvoiceRepo
						.findInterDetailsForCostInvoicePosting(costInvoiceDTO.getOrgId(), gstType, gstPercent);
				for (Object[] ch : groupLedgerVOs) {
					String chargeDesc = ch[0].toString();
					double gstPer = Double.parseDouble(ch[1].toString());
					String currency = ch[2].toString();
					String ledger = ch[0].toString();
					igstSummaryVO.setChargeName(chargeDesc);
					igstSummaryVO.setCurrency(currency);
					igstSummaryVO.setGSTPercent(gstPer);
					igstSummaryVO.setLedger(ledger);
					igstSummaryVO.setQty(0);
					igstSummaryVO.setRate(BigDecimal.ZERO);
					igstSummaryVO.setExRate(BigDecimal.ZERO);
					igstSummaryVO.setFcAmt(BigDecimal.ZERO);
					igstSummaryVO.setLcAmt(igstLcAmount);
					igstSummaryVO.setFcAmt(BigDecimal.ZERO);
					igstSummaryVO.setBillAmt(BigDecimal.ZERO);
					igstSummaryVO.setGstAmount(BigDecimal.ZERO);
					igstSummaryVO.setCostInvoiceVO(costInvoiceVO);
					chargerCostInvoiceVOs.add(igstSummaryVO);
				}
			}
		}

//		ADD CGST and SGST ROWS FOR EACH GST PERCENTAGE IN cgstCategorySumMap
		if ("INTRA".equalsIgnoreCase(costInvoiceDTO.getGstType())) {
			for (Map.Entry<String, BigDecimal> entry : cgstCategorySumMap.entrySet()) {
				String gstType = "INTRA";
				Double gstPercent = Double.parseDouble(entry.getKey()) / 2;
				BigDecimal totalTaxAmount = entry.getValue();

				BigDecimal cgstAmount = totalTaxAmount.divide(BigDecimal.valueOf(2));
//				if (igstSummaryVO.getGSTPercent() != 0) {
				taxAmount = taxAmount.add(cgstAmount);

				BigDecimal sgstAmount = totalTaxAmount.divide(BigDecimal.valueOf(2));
//				if (igstSummaryVO.getGSTPercent() != 0) {
				taxAmount = taxAmount.add(sgstAmount);

				Set<Object[]> groupLedgerVOs = costInvoiceRepo
						.findIntraDetailsForCostInvoicePosting(costInvoiceDTO.getOrgId(), gstType, gstPercent);

				for (Object[] entry1 : groupLedgerVOs) {
					ChargerCostInvoiceVO cgstSummaryVO = new ChargerCostInvoiceVO();
					cgstSummaryVO.setChargeName(entry1[0].toString());
					cgstSummaryVO.setGSTPercent(Double.parseDouble(entry1[1].toString()));
					cgstSummaryVO.setCurrency(entry1[2].toString());
					cgstSummaryVO.setLedger(entry1[0].toString());
					cgstSummaryVO.setQty(0);
					cgstSummaryVO.setRate(BigDecimal.ZERO);
					cgstSummaryVO.setExRate(BigDecimal.ZERO);
					cgstSummaryVO.setFcAmt(BigDecimal.ZERO);
					cgstSummaryVO.setLcAmt(cgstAmount);
					cgstSummaryVO.setBillAmt(BigDecimal.ZERO);
					cgstSummaryVO.setGstAmount(BigDecimal.ZERO);
					cgstSummaryVO.setCostInvoiceVO(costInvoiceVO);
					chargerCostInvoiceVOs.add(cgstSummaryVO);
				}
			}
		}

		costInvoiceVO.setChargerCostInvoiceVO(chargerCostInvoiceVOs);

		// TDS table
		List<TdsCostInvoiceVO> tdsCostInvoiceVOs = new ArrayList<>();
		for (TdsCostInvoiceDTO tdsCostInvoiceDTO : costInvoiceDTO.getTdsCostInvoiceDTO()) {
			TdsCostInvoiceVO tdsCostInvoiceVO = new TdsCostInvoiceVO();

			tdsCostInvoiceVO.setTdsWithHolding(tdsCostInvoiceDTO.getTdsWithHolding());
			tdsCostInvoiceVO.setTdsWithHoldingPer(tdsCostInvoiceDTO.getTdsWithHoldingPer());
			tdsCostInvoiceVO.setSection(tdsCostInvoiceDTO.getSection());
//			tdsCostInvoiceVO.setTotTdsWhAmnt(tdsCostInvoiceDTO.getTotTdsWhAmnt());

			BigDecimal totTdsWhAmt = BigDecimal.ZERO;
			BigDecimal tdsWhPercent = tdsCostInvoiceDTO.getTdsWithHoldingPer();
			System.out.println("TOTAL LC AMOUNT IS :" + sumLcAmount);
			totTdsWhAmt = sumLcAmount.multiply(tdsWhPercent.divide(BigDecimal.valueOf(100)));
			tdsCostInvoiceVO.setTotTdsWhAmnt(totTdsWhAmt);
			tdsAmount = totTdsWhAmt;

			tdsCostInvoiceVO.setCostInvoiceVO(costInvoiceVO);
			tdsCostInvoiceVOs.add(tdsCostInvoiceVO);
		}
		costInvoiceVO.setTdsCostInvoiceVO(tdsCostInvoiceVOs);

		// SUMMARY CALCULATION
		BigDecimal totChargeAmtBillCurr = sumBillAmount;
		BigDecimal totChargeAmtLc = sumLcAmount;
		BigDecimal netAmountBillCurr = sumBillAmount.subtract(tdsAmount).add(taxAmount);
		BigDecimal netAmountLc = taxAmount.subtract(tdsAmount).add(sumLcAmount);
		BigDecimal actBillAmtLc = sumLcAmount.subtract(tdsAmount).add(taxAmount);
		BigDecimal actBillAmtBillCurr = sumBillAmount.add(taxAmount);
		BigDecimal roundedValue = netAmountLc.setScale(0, RoundingMode.HALF_UP);
		BigDecimal sumDebitAmount = sumLcAmount.add(taxAmount);
		Long roundOff = netAmountLc.subtract(roundedValue).longValue();

		costInvoiceVO.setTotChargesBillCurrAmt(totChargeAmtBillCurr);
		costInvoiceVO.setTotChargesLcAmt(totChargeAmtLc);
		costInvoiceVO.setNetBillCurrAmt(netAmountBillCurr);
		costInvoiceVO.setNetBillLcAmt(netAmountLc);
		costInvoiceVO.setActBillCurrAmt(actBillAmtBillCurr);
		costInvoiceVO.setActBillLcAmt(actBillAmtLc);
		costInvoiceVO.setRoundOff(roundOff);
		costInvoiceVO.setGstInputLcAmt(taxAmount);
		costInvoiceVO.setSumLcAmt(sumDebitAmount);

		costInvoiceVO.setAmountInWords(amountInWordsConverterService.convert(costInvoiceVO.getNetBillCurrAmt()));

		return costInvoiceVO;

	}

	@Override
	public CostInvoiceVO getCostInvoiceByDocId(Long orgId, String docId) {
		return costInvoiceRepo.findAllCostInvoiceByDocId(orgId, docId);
	}

	@Override
	public String getCostInvoiceDocId(Long orgId, String finYear, String branch, String branchCode) {
		String ScreenCode = "CI";
		String result = costInvoiceRepo.getCostInvoiceDocId(orgId, finYear, branchCode, ScreenCode);
		return result;
	}

	@Override
	public List<Map<String, Object>> getChargeType(Long orgId) {
		Set<Object[]> chType = costInvoiceRepo.getActiveChargType(orgId);
		return getChargeType(chType);
	}

	private List<Map<String, Object>> getChargeType(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();
			map.put("chargeType", ch[0].toString());
			List1.add(map);
		}
		return List1;

	}

	@Override
	public List<Map<String, Object>> getChargeCodeByChargeType(Long orgId, String chargeType) {
		Set<Object[]> chCode = costInvoiceRepo.getActiveChargCodeByOrgIdAndChargeTypeIgnoreCase(orgId, chargeType);
		return getChargeCode(chCode);
	}

	private List<Map<String, Object>> getChargeCode(Set<Object[]> chCode) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chCode) {
			Map<String, Object> map = new HashMap<>();
			map.put("chargeCode", ch[0] != null ? ch[0].toString() : ""); // Empty string if null
			map.put("govChargeCode", ch[1] != null ? ch[1].toString() : "");
			map.put("chargeName", ch[2] != null ? ch[2].toString() : "");
			map.put("taxable", ch[3] != null ? ch[3].toString() : "");
			map.put("ccFeeApplicable", ch[4] != null ? ch[4].toString() : "");
			map.put("exempted", ch[5] != null ? ch[5].toString() : "");
			map.put("sac", ch[6] != null ? ch[6].toString() : "");
			map.put("GSTPercent", ch[7] != null ? ch[7].toString() : ""); // Handle as string, empty if null
			map.put("ledger", ch[8] != null ? ch[8].toString() : "");

			List1.add(map);
		}
		return List1;

	}

	@Override
	public List<Map<String, Object>> getCurrencyAndExratesForMatchingParties(Long orgId, String partyCode) {
		Set<Object[]> currency = costInvoiceRepo.getCurrencyAndExratesForMatchingParties(orgId, partyCode);
		return getCurrency(currency);
	}

	private List<Map<String, Object>> getCurrency(Set<Object[]> currency) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : currency) {
			Map<String, Object> map = new HashMap<>();
			map.put("orgId", ch[0] != null ? ch[0].toString() : ""); // Empty string if null
			map.put("date", ch[1] != null ? ch[1].toString() : "");
			map.put("month", ch[2] != null ? ch[2].toString() : "");
			map.put("currency", ch[3] != null ? ch[3].toString() : "");
			map.put("currencyDescripition", ch[4] != null ? ch[4].toString() : "");
			map.put("buyingExRate", ch[5] != null ? ch[5].toString() : "");
			map.put("sellingExRate", ch[6] != null ? ch[6].toString() : "");

			List1.add(map);
		}
		return List1;
	}

	@Override
	public List<PartyMasterVO> getAllPartyByPartyType(Long orgId, String partyType) {

		return costInvoiceRepo.findByOrgIdAndPartyTypeIgnoreCase(orgId, partyType);

	}

	@Override
	public List<Map<String, Object>> getPartyStateCodeDetails(Long orgId, Long id) {
		Set<Object[]> getStateDetails = costInvoiceRepo.getStateCodeDetails(orgId, id);
		return getState(getStateDetails);
	}

	private List<Map<String, Object>> getState(Set<Object[]> getStateDetails) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : getStateDetails) {
			Map<String, Object> map = new HashMap<>();
			map.put("stateCode", ch[0] != null ? ch[0].toString() : ""); // Empty string if null
			map.put("recipientGSTIN", ch[1] != null ? ch[1].toString() : "");
			map.put("stateNo", ch[2] != null ? ch[2].toString() : "");
			List1.add(map);
		}
		return List1;
	}

	@Override
	public List<Map<String, Object>> getPartyAddressDetails(Long orgId, Long id, String stateCode,
			String placeOfSupply) {
		Set<Object[]> getAddressDetails = costInvoiceRepo.getAddressDetails(orgId, id, stateCode, placeOfSupply);
		return getAddress(getAddressDetails);
	}

	private List<Map<String, Object>> getAddress(Set<Object[]> getAddressDetails) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : getAddressDetails) {
			Map<String, Object> map = new HashMap<>();
			map.put("addressType", ch[0] != null ? ch[0].toString() : "");
			map.put("address", ch[1] != null ? ch[1].toString() : ""); // Empty string if null
			map.put("pinCode", ch[2] != null ? ch[2].toString() : "");
			List1.add(map);
		}
		return List1;
	}

	@Override
	public List<Map<String, Object>> getGstTypeDetails(Long orgId, String branchCode, String stateCode) {
		Set<Object[]> getGSTTypeDetails = costInvoiceRepo.getGstType(orgId, branchCode, stateCode);
		return getGstType(getGSTTypeDetails);
	}

	private List<Map<String, Object>> getGstType(Set<Object[]> getGSTTypeDetails) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : getGSTTypeDetails) {
			Map<String, Object> map = new HashMap<>();
			map.put("gstType", ch[0] != null ? ch[0].toString() : "");
			List1.add(map);
		}
		return List1;
	}

	@Override
	public List<Map<String, Object>> getPlaceOfSupplyDetails(Long orgId, Long id, String stateCode) {
		Set<Object[]> getPOSDetails = costInvoiceRepo.getPlaceOfSupplyDetails(orgId, id, stateCode);
		return getPOS(getPOSDetails);
	}

	private List<Map<String, Object>> getPOS(Set<Object[]> getPOSDetails) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : getPOSDetails) {
			Map<String, Object> map = new HashMap<>();
			map.put("placeOfSupply", ch[0] != null ? ch[0].toString() : ""); // Empty string if null
			List1.add(map);
		}
		return List1;
	}

	@Override
	public List<Map<String, Object>> getJobNoFromTmsJobCard(Long orgId) {
		Set<Object[]> getJobNo = costInvoiceRepo.getJobNoFromTmsJobCard(orgId);
		return getJobDetails(getJobNo);
	}

	private List<Map<String, Object>> getJobDetails(Set<Object[]> getJob) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : getJob) {
			Map<String, Object> map = new HashMap<>();
			map.put("jobNo", ch[0] != null ? ch[0].toString() : ""); // Empty string if null
			map.put("customerName", ch[1] != null ? ch[1].toString() : "");
			List1.add(map);
		}
		return List1;
	}

	@Override
	public List<Map<String, Object>> getChargeDetailsFromChargeType(Long orgId) {
		Set<Object[]> chDetails = chargeTypeRequestRepo.getActiveChargeDetailsFromChargeType(orgId);
		return getChargeDetails(chDetails);
	}

	private List<Map<String, Object>> getChargeDetails(Set<Object[]> chCode) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chCode) {
			Map<String, Object> map = new HashMap<>();
			map.put("chargeCode", ch[0] != null ? ch[0].toString() : ""); // Empty string if null
			map.put("govChargeCode", ch[1] != null ? ch[1].toString() : "");
			map.put("chargeName", ch[2] != null ? ch[2].toString() : "");
			map.put("taxable", ch[3] != null ? ch[3].toString() : "");
			map.put("ccFeeApplicable", ch[4] != null ? ch[4].toString() : "");
			map.put("exempted", ch[5] != null ? ch[5].toString() : "");
			map.put("sac", ch[6] != null ? ch[6].toString() : "");
			map.put("GSTPercent", ch[7] != null ? ch[7].toString() : ""); // Handle as string, empty if null
			map.put("ledger", ch[8] != null ? ch[8].toString() : "");

			List1.add(map);
		}
		return List1;

	}

	@Override
	public List<Map<String, Object>> getTdsDetailsFromPartyMasterSpecialTDS(Long orgId, String partyCode) {
		Set<Object[]> specialTds = costInvoiceRepo.findTdsDetailsFromPartyMasterSpecialTDS(orgId, partyCode);
		return getTds(specialTds);
	}

	private List<Map<String, Object>> getTds(Set<Object[]> chCode) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chCode) {
			Map<String, Object> map = new HashMap<>();
			map.put("section", ch[0] != null ? ch[0].toString() : ""); // Empty string if null
			map.put("tdsWhPercent", ch[1] != null ? ch[1].toString() : "");

			List1.add(map);
		}
		return List1;

	}

	@Override
	public List<Map<String, Object>> getInterAndIntraDetailsForCostInvoice(Long orgId, String gstType,
			List<String> gstPercent) {
		Set<Object[]> chargeDetails = costInvoiceRepo.findInterAndIntraDetailsForCostInvoice(orgId, gstType,
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

//	@Override
//	public CostInvoiceVO approveCostInvoice(Long orgId, Long id, String docId, String action, String actionBy)
//			throws ApplicationException {
//		// Fetch the CostInvoice details from Cost Invoice
//		CostInvoiceVO costInvoiceVO = costInvoiceRepo.findByOrgIdAndIdAndDocId(orgId, id, docId);
//		String screenCode = "AC";
//		String sourceScreenCode = costInvoiceVO.getScreenCode();
//
//		// Validate the approval status of the invoice
//		if (costInvoiceVO.getApproveStatus() == null || (!costInvoiceVO.getApproveStatus().equalsIgnoreCase("Approved")
//				&& !costInvoiceVO.getApproveStatus().equalsIgnoreCase("Rejected"))) {
//
//			String accountsDocId = accountsRepo.getCostInvoiceDocId(costInvoiceVO.getOrgId(),
//					costInvoiceVO.getFinYear(), costInvoiceVO.getBranchCode(), sourceScreenCode, screenCode);
//
//			// GETDOCID LASTNO +1
//			MultipleDocIdGenerationDetailsVO mulDocId = multipleDocIdGenerationDetailsRepo
//					.findByOrgIdAndFinYearAndBranchCodeAndSourceScreenCodeAndScreenCode(costInvoiceVO.getOrgId(),
//							costInvoiceVO.getFinYear(), costInvoiceVO.getBranchCode(), sourceScreenCode, screenCode);
//			mulDocId.setLastno(mulDocId.getLastno() + 1);
//			multipleDocIdGenerationDetailsRepo.save(mulDocId);
//
//			AccountsVO accountsVO = new AccountsVO();
//			accountsVO.setDocId(accountsDocId);
//			accountsVO.setSourceId(costInvoiceVO.getId());
//			accountsVO.setModifiedon(costInvoiceVO.getCommonDate().getModifiedon().toUpperCase());
//			accountsVO.setCreatedBy(costInvoiceVO.getCreatedBy());
//			accountsVO.setCreatedon(costInvoiceVO.getCommonDate().getModifiedon().toUpperCase());
//			accountsVO.setCancelRemarks(costInvoiceVO.getCancelRemarks());
//			accountsVO.setFinYear(costInvoiceVO.getFinYear());
//			accountsVO.setBranch(costInvoiceVO.getBranch());
//			accountsVO.setBranchCode(costInvoiceVO.getBranchCode());
//			accountsVO.setAmountInWords(costInvoiceVO.getAmountInWords());
//			accountsVO.setRefNo(costInvoiceVO.getDocId());
//			accountsVO.setRefDate(costInvoiceVO.getDocDate());
//			accountsVO.setVId(costInvoiceVO.getVId());
//			accountsVO.setVDate(costInvoiceVO.getVDate());
//			accountsVO.setCurrency(costInvoiceVO.getCurrency());
//			accountsVO.setExRate(costInvoiceVO.getExRate());
//
//			// Calculate total debit/credit amounts
//			BigDecimal totalDebitAmount = costInvoiceVO.getNetBillCurrAmt();// tax
//			accountsVO.setTotalDebitAmount(totalDebitAmount);
//			accountsVO.setTotalCreditAmount(totalDebitAmount);
//			accountsVO.setDueDate(costInvoiceVO.getDueDate());
//			accountsVO.setSupplierRefNo(costInvoiceVO.getSupplierBillNo());
//			accountsVO.setCreditDays(costInvoiceVO.getCreditDays());
//			accountsVO.setSourceScreen(costInvoiceVO.getScreenName());
//			accountsVO.setSourceScreenCode(costInvoiceVO.getScreenCode());
//			accountsVO.setModifiedBy(costInvoiceVO.getUpdatedBy());
//			accountsVO.setOrgId(costInvoiceVO.getOrgId());
//			accountsVO.setRemarks(costInvoiceVO.getRemarks());
//			accountsVO.setChargeableAmount(costInvoiceVO.getTotChargesLcAmt());
//
////			accountsVO.setAmountInWords(costInvoiceVO.getAmountInWords());
////			accountsVO.setStTaxAmount(costInvoiceVO.getTotalTaxableAmountLc());
////			accountsVO.setSalesType(costInvoiceVO.getSalesType());
//
//			List<AccountsDetailsVO> accountsDetailsVOs = new ArrayList<>();
//			AccountsDetailsVO accountsDetailsVO = new AccountsDetailsVO();
//			accountsDetailsVO.setNDebitAmount(BigDecimal.ZERO);
//			accountsDetailsVO.setACategory("PAYABLE A/C");
//			accountsDetailsVO.setAccountName("PAYABLE A/C");
//			accountsDetailsVO.setDebitAmount(BigDecimal.ZERO);
//			accountsDetailsVO.setNCreditAmount(costInvoiceVO.getNetBillCurrAmt());
//			accountsDetailsVO.setCreditAmount(costInvoiceVO.getNetBillCurrAmt());
//			accountsDetailsVO.setArapFlag(true);
//			accountsDetailsVO.setArapAmount(costInvoiceVO.getNetBillCurrAmt());
//			accountsDetailsVO.setBDebitAmount(BigDecimal.ZERO);
//			accountsDetailsVO.setBCrAmount(costInvoiceVO.getNetBillCurrAmt());
//			accountsDetailsVO.setBArapAmount(costInvoiceVO.getNetBillCurrAmt());
//			accountsDetailsVO.setACurrency(costInvoiceVO.getCurrency());
//			accountsDetailsVO.setAExRate(costInvoiceVO.getExRate());
//			accountsDetailsVO.setSubledgerName(costInvoiceVO.getSupplierName());
//			accountsDetailsVO.setSubLedgerCode(costInvoiceVO.getSupplierCode());
//			accountsDetailsVO.setNArapAmount(costInvoiceVO.getNetBillCurrAmt());
//			accountsDetailsVO.setGstflag(6);
//			accountsDetailsVO.setAccountsVO(accountsVO);
//			accountsDetailsVOs.add(accountsDetailsVO);
//
//			for (TdsCostInvoiceVO tdsCostInvoiceVO : costInvoiceVO.getTdsCostInvoiceVO()) {
//
//				Set<Object[]> ch = costInvoiceRepo.getTdsLedgerFromAccount(costInvoiceVO.getOrgId());
//
//				for (Object[] ch1 : ch) {
//
//					AccountsDetailsVO accountsDetailsVO1 = new AccountsDetailsVO();
//					accountsDetailsVO1.setNDebitAmount(BigDecimal.ZERO);
//					accountsDetailsVO1.setACategory(ch1[1].toString());
//					accountsDetailsVO1.setAccountName(ch1[0].toString());
//					accountsDetailsVO1.setDebitAmount(BigDecimal.ZERO);
//					accountsDetailsVO1.setNCreditAmount(tdsCostInvoiceVO.getTotTdsWhAmnt());
//					accountsDetailsVO1.setCreditAmount(tdsCostInvoiceVO.getTotTdsWhAmnt());
//					accountsDetailsVO1.setArapFlag(false);
//					accountsDetailsVO1.setArapAmount(BigDecimal.ZERO);
//					accountsDetailsVO1.setBDebitAmount(BigDecimal.ZERO);
//					accountsDetailsVO1.setBCrAmount(tdsCostInvoiceVO.getTotTdsWhAmnt());
//					accountsDetailsVO1.setBArapAmount(BigDecimal.ZERO);
//					accountsDetailsVO1.setSubledgerName("None");
//					accountsDetailsVO1.setSubLedgerCode("None");
//					accountsDetailsVO1.setACurrency(costInvoiceVO.getCurrency());
//					accountsDetailsVO1.setAExRate(costInvoiceVO.getExRate());
//					accountsDetailsVO1.setNArapAmount(BigDecimal.ZERO);
//					accountsDetailsVO1.setGstflag(3);
//					accountsDetailsVO1.setAccountsVO(accountsVO);
//					accountsDetailsVOs.add(accountsDetailsVO1);
//
//				}
//
//			}
//
////			for (ChargerCostInvoiceVO tdsCostInvoiceVO : costInvoiceVO.getChargerCostInvoiceVO()) {
////				GroupLedgerVO groupLedgerVO = groupLedgerRepo.findByAccountGroupName(tdsCostInvoiceVO.getLedger());
//
//			// Group and process GST-related ledgers
//			Map<String, BigDecimal> ledgerSumMap = new HashMap<>();
//			for (ChargerCostInvoiceVO tdsCostInvoiceVO : costInvoiceVO.getChargerCostInvoiceVO()) {
//				String ledger = tdsCostInvoiceVO.getLedger();
//				BigDecimal lcAmount = tdsCostInvoiceVO.getLcAmt();
//
//				ledgerSumMap.put(ledger, ledgerSumMap.getOrDefault(ledger, BigDecimal.ZERO).add(lcAmount));
//			}
//
//			// Add GST ledger entries
//			for (Map.Entry<String, BigDecimal> entry : ledgerSumMap.entrySet()) {
//				GroupLedgerVO groupLedgerVO = groupLedgerRepo.findByAccountGroupName(entry.getKey());
//
//				AccountsDetailsVO accountsDetailsVO1 = new AccountsDetailsVO();
//				accountsDetailsVO1.setNDebitAmount(BigDecimal.ZERO);
//				accountsDetailsVO1.setACategory(groupLedgerVO.getCategory());
//				accountsDetailsVO1.setAccountName(groupLedgerVO.getAccountGroupName());
////				accountsDetailsVO1.setDebitAmount(tdsCostInvoiceVO.getBillAmt().add(tdsCostInvoiceVO.getGstAmount()));
//				accountsDetailsVO1.setDebitAmount(entry.getValue());
//				accountsDetailsVO1.setNDebitAmount(entry.getValue());
//				accountsDetailsVO1.setNCreditAmount(BigDecimal.ZERO);
//				accountsDetailsVO1.setCreditAmount(BigDecimal.ZERO);
//				accountsDetailsVO1.setACurrency(costInvoiceVO.getCurrency());
//				accountsDetailsVO1.setAExRate(costInvoiceVO.getExRate());
//				accountsDetailsVO1.setArapFlag(false);
//				accountsDetailsVO1.setArapAmount(BigDecimal.ZERO);
////				accountsDetailsVO1.setBDebitAmount(tdsCostInvoiceVO.getBillAmt().add(tdsCostInvoiceVO.getGstAmount()));
//				accountsDetailsVO1.setBDebitAmount(entry.getValue());
//				accountsDetailsVO1.setBCrAmount(BigDecimal.ZERO);
//				accountsDetailsVO1.setBArapAmount(BigDecimal.ZERO);
//				accountsDetailsVO1.setSubledgerName("None");
//				accountsDetailsVO1.setSubLedgerCode("None");
//				accountsDetailsVO1.setNArapAmount(BigDecimal.ZERO);
//				accountsDetailsVO1.setGstflag(3);
//				accountsDetailsVO1.setAccountsVO(accountsVO);
//				accountsDetailsVOs.add(accountsDetailsVO1);
//
//			}
//
////
//
//			// Save AccountsVO and update TaxInvoiceVO
//			accountsVO.setAccountsDetailsVO(accountsDetailsVOs);
//			AccountsVO savedAccountsVO = accountsRepo.save(accountsVO);
//
//			int gstflag = 6;
//			AccountsDetailsVO accountsDetailsVOs2 = accountsDetailsRepo.findByAccountsVOAndGstflag(savedAccountsVO,
//					gstflag);
//			ArapDetailsVO arapDetailsVO = new ArapDetailsVO();
//			arapDetailsVO.setSourceTransid(accountsDetailsVOs2.getId());
//			arapDetailsVO.setCreatedBy(savedAccountsVO.getCreatedBy());
//			arapDetailsVO.setUpdatedBy(savedAccountsVO.getModifiedBy());
//			arapDetailsVO.setBranch(savedAccountsVO.getBranch());
//			arapDetailsVO.setBranchCode(savedAccountsVO.getBranchCode());
//			arapDetailsVO.setFinYear(savedAccountsVO.getFinYear());
//			arapDetailsVO.setRefNo(savedAccountsVO.getRefNo());
//			arapDetailsVO.setRefDate(savedAccountsVO.getRefDate());
//			arapDetailsVO.setSubLedgerCode(accountsDetailsVOs2.getSubLedgerCode());
//			arapDetailsVO.setCurrency(accountsDetailsVOs2.getACurrency());
//			arapDetailsVO.setExRate(accountsDetailsVOs2.getAExRate());
//			arapDetailsVO.setAmount(accountsDetailsVOs2.getArapAmount());
//			arapDetailsVO.setBaseAmt(accountsDetailsVOs2.getArapAmount());
//			arapDetailsVO.setDueDate(savedAccountsVO.getDueDate());
//			arapDetailsVO.setCreditDays(savedAccountsVO.getCreditDays());
//			arapDetailsVO.setDocId(savedAccountsVO.getDocId());
//			arapDetailsVO.setDocDate(savedAccountsVO.getDocDate());
//			arapDetailsVO.setAccCurrency(savedAccountsVO.getCurrency());
//			arapDetailsVO.setExRate(savedAccountsVO.getExRate());
//			arapDetailsVO.setAccName(accountsDetailsVOs2.getAccountName());
//			arapDetailsVO.setGstFlag(accountsDetailsVOs2.getGstflag());
//			arapDetailsVO.setSubLedgerName(accountsDetailsVOs2.getSubledgerName());
//			arapDetailsVO.setSalesType(savedAccountsVO.getSalesType());
//			arapDetailsVO.setNativeAmt(accountsDetailsVOs2.getArapAmount());
//			arapDetailsRepo.save(arapDetailsVO);
//			costInvoiceVO.setPurVoucherNo(savedAccountsVO.getDocId());
//			costInvoiceVO.setPurVoucherDate(savedAccountsVO.getDocDate());
//
////			LocalDate purVouDate = savedAccountsVO.getDocDate();
////			int creditDays = costInvoiceVO.getCreditDays();
////			LocalDate dueDate = purVouDate.plusDays(creditDays);
////			// Save dueDate in your entity
////			accountsVO.setDueDate(dueDate);
////			costInvoiceVO.setDueDate(dueDate);
//
//			costInvoiceVO.setApproveStatus(action);
//			costInvoiceVO.setApproveBy(actionBy);
//			costInvoiceVO.setPurVoucherNo(accountsVO.getDocId());
//			costInvoiceVO.setPurVoucherDate(accountsVO.getDocDate());
//			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
//			costInvoiceVO.setApproveOn(LocalDateTime.now().format(formatter).toUpperCase());
//
//			return costInvoiceRepo.save(costInvoiceVO);
//
//		} else if (costInvoiceVO.getApproveStatus().equals("Approved")) {
//			throw new ApplicationException("This Invoice Already Approved,");
//		} else {
//			throw new ApplicationException("This Invoice Already Rejected");
//		}
//	}

	
	
	
	@Override
	public CostInvoiceVO approveCostInvoice(Long orgId, Long id, String docId, String action, String actionBy)
	        throws ApplicationException {

	    // Fetch cost invoice details
	    CostInvoiceVO costInvoiceVO = costInvoiceRepo.findByOrgIdAndIdAndDocId(orgId, id, docId);
	    String screenCode = "AC";
	    String sourceScreenCode = costInvoiceVO.getScreenCode();

	    // Validate approval status
	    if ("Approved".equalsIgnoreCase(costInvoiceVO.getApproveStatus())) {
	        throw new ApplicationException("This Invoice is already approved.");
	    } else if ("Rejected".equalsIgnoreCase(costInvoiceVO.getApproveStatus())) {
	        throw new ApplicationException("This Invoice is already rejected.");
	    }

	    // Fetch existing AccountsVO (if any)
	    AccountsVO accountsVO = accountsRepo.findByRefNoAndVIdAndVDate(
	        costInvoiceVO.getDocId(), costInvoiceVO.getVId(), costInvoiceVO.getVDate()
	    );

	    // Delete existing details if found
	    if (accountsVO != null) {
	        List<AccountsDetailsVO> oldDetails = accountsDetailsRepo.findByAccountsVO(accountsVO);
	        if (!oldDetails.isEmpty()) {
	            accountsDetailsRepo.deleteAll(oldDetails);
	        }
	    } else {
	        // Create new AccountsVO with docId generation
	        accountsVO = new AccountsVO();
	        String accountsDocId = accountsRepo.getCostInvoiceDocId(
	            costInvoiceVO.getOrgId(), costInvoiceVO.getFinYear(),
	            costInvoiceVO.getBranchCode(), sourceScreenCode, screenCode
	        );

	        MultipleDocIdGenerationDetailsVO mulDocId = multipleDocIdGenerationDetailsRepo
	                .findByOrgIdAndFinYearAndBranchCodeAndSourceScreenCodeAndScreenCode(
	                        costInvoiceVO.getOrgId(), costInvoiceVO.getFinYear(),
	                        costInvoiceVO.getBranchCode(), sourceScreenCode, screenCode);

	        mulDocId.setLastno(mulDocId.getLastno() + 1);
	        multipleDocIdGenerationDetailsRepo.save(mulDocId);
	        accountsVO.setDocId(accountsDocId);
	    }

	    // Populate AccountsVO
	    accountsVO.setSourceId(costInvoiceVO.getId());
	    accountsVO.setCreatedBy(costInvoiceVO.getCreatedBy());
	    accountsVO.setModifiedBy(costInvoiceVO.getUpdatedBy());
	    accountsVO.setCreatedon(costInvoiceVO.getCommonDate().getModifiedon().toUpperCase());
	    accountsVO.setModifiedon(costInvoiceVO.getCommonDate().getModifiedon().toUpperCase());
	    accountsVO.setFinYear(costInvoiceVO.getFinYear());
	    accountsVO.setBranch(costInvoiceVO.getBranch());
	    accountsVO.setBranchCode(costInvoiceVO.getBranchCode());
	    accountsVO.setCurrency(costInvoiceVO.getCurrency());
	    accountsVO.setOrgId(costInvoiceVO.getOrgId());
	    accountsVO.setRefNo(costInvoiceVO.getDocId());
	    accountsVO.setRefDate(costInvoiceVO.getDocDate());
	    accountsVO.setVId(costInvoiceVO.getVId());
	    accountsVO.setVDate(costInvoiceVO.getVDate());
	    accountsVO.setDueDate(costInvoiceVO.getDueDate());
	    accountsVO.setAmountInWords(costInvoiceVO.getAmountInWords());
	    accountsVO.setChargeableAmount(costInvoiceVO.getTotChargesLcAmt());
	    accountsVO.setSupplierRefNo(costInvoiceVO.getSupplierBillNo());
	    accountsVO.setCreditDays(costInvoiceVO.getCreditDays());
	    accountsVO.setSourceScreen(costInvoiceVO.getScreenName());
	    accountsVO.setSourceScreenCode(costInvoiceVO.getScreenCode());
	    accountsVO.setRemarks(costInvoiceVO.getRemarks());
	    accountsVO.setTotalDebitAmount(costInvoiceVO.getNetBillCurrAmt());
	    accountsVO.setTotalCreditAmount(costInvoiceVO.getNetBillCurrAmt());

	    List<AccountsDetailsVO> accountsDetailsVOs = new ArrayList<>();

	    // PAYABLE A/C Entry
	    AccountsDetailsVO payableDetail = new AccountsDetailsVO();
	    payableDetail.setAccountName("PAYABLE A/C");
	    payableDetail.setACategory("PAYABLE A/C");
	    payableDetail.setDebitAmount(BigDecimal.ZERO);
	    payableDetail.setCreditAmount(costInvoiceVO.getNetBillCurrAmt());
	    payableDetail.setNDebitAmount(BigDecimal.ZERO);
	    payableDetail.setNCreditAmount(costInvoiceVO.getNetBillCurrAmt());
	    payableDetail.setBDebitAmount(BigDecimal.ZERO);
	    payableDetail.setBCrAmount(costInvoiceVO.getNetBillCurrAmt());
	    payableDetail.setArapAmount(costInvoiceVO.getNetBillCurrAmt());
	    payableDetail.setBArapAmount(costInvoiceVO.getNetBillCurrAmt());
	    payableDetail.setNArapAmount(costInvoiceVO.getNetBillCurrAmt());
	    payableDetail.setArapFlag(true);
	    payableDetail.setACurrency(costInvoiceVO.getCurrency());
	    payableDetail.setAExRate(costInvoiceVO.getExRate());
	    payableDetail.setSubledgerName(costInvoiceVO.getSupplierName());
	    payableDetail.setSubLedgerCode(costInvoiceVO.getSupplierCode());
	    payableDetail.setGstflag(6);
	    payableDetail.setAccountsVO(accountsVO);
	    accountsDetailsVOs.add(payableDetail);

	    // TDS entries
	    for (TdsCostInvoiceVO tds : costInvoiceVO.getTdsCostInvoiceVO()) {
	        Set<Object[]> tdsLedgers = costInvoiceRepo.getTdsLedgerFromAccount(costInvoiceVO.getOrgId());

	        for (Object[] ledger : tdsLedgers) {
	            AccountsDetailsVO tdsDetail = new AccountsDetailsVO();
	            tdsDetail.setAccountName(ledger[0].toString());
	            tdsDetail.setACategory(ledger[1].toString());
	            tdsDetail.setDebitAmount(BigDecimal.ZERO);
	            tdsDetail.setCreditAmount(tds.getTotTdsWhAmnt());
	            tdsDetail.setNDebitAmount(BigDecimal.ZERO);
	            tdsDetail.setNCreditAmount(tds.getTotTdsWhAmnt());
	            tdsDetail.setBDebitAmount(BigDecimal.ZERO);
	            tdsDetail.setBCrAmount(tds.getTotTdsWhAmnt());
	            tdsDetail.setArapAmount(BigDecimal.ZERO);
	            tdsDetail.setBArapAmount(BigDecimal.ZERO);
	            tdsDetail.setNArapAmount(BigDecimal.ZERO);
	            tdsDetail.setArapFlag(false);
	            tdsDetail.setACurrency(costInvoiceVO.getCurrency());
	            tdsDetail.setAExRate(costInvoiceVO.getExRate());
	            tdsDetail.setSubledgerName("None");
	            tdsDetail.setSubLedgerCode("None");
	            tdsDetail.setGstflag(3);
	            tdsDetail.setAccountsVO(accountsVO);
	            accountsDetailsVOs.add(tdsDetail);
	        }
	    }

	    // GST ledger entries (grouped by ledger)
	    Map<String, BigDecimal> ledgerSumMap = new HashMap<>();
	    for (ChargerCostInvoiceVO charge : costInvoiceVO.getChargerCostInvoiceVO()) {
	        ledgerSumMap.merge(charge.getLedger(), charge.getLcAmt(), BigDecimal::add);
	    }

	    for (Map.Entry<String, BigDecimal> entry : ledgerSumMap.entrySet()) {
	        GroupLedgerVO groupLedger = groupLedgerRepo.findByAccountGroupName(entry.getKey());

	        AccountsDetailsVO gstDetail = new AccountsDetailsVO();
	        gstDetail.setAccountName(groupLedger.getAccountGroupName());
	        gstDetail.setACategory(groupLedger.getCategory());
	        gstDetail.setDebitAmount(entry.getValue());
	        gstDetail.setNDebitAmount(entry.getValue());
	        gstDetail.setCreditAmount(BigDecimal.ZERO);
	        gstDetail.setNCreditAmount(BigDecimal.ZERO);
	        gstDetail.setBDebitAmount(entry.getValue());
	        gstDetail.setBCrAmount(BigDecimal.ZERO);
	        gstDetail.setArapAmount(BigDecimal.ZERO);
	        gstDetail.setBArapAmount(BigDecimal.ZERO);
	        gstDetail.setNArapAmount(BigDecimal.ZERO);
	        gstDetail.setArapFlag(false);
	        gstDetail.setSubledgerName("None");
	        gstDetail.setSubLedgerCode("None");
	        gstDetail.setACurrency(costInvoiceVO.getCurrency());
	        gstDetail.setAExRate(costInvoiceVO.getExRate());
	        gstDetail.setGstflag(3);
	        gstDetail.setAccountsVO(accountsVO);
	        accountsDetailsVOs.add(gstDetail);
	    }

	    accountsVO.setAccountsDetailsVO(accountsDetailsVOs);
	    AccountsVO savedAccountsVO = accountsRepo.save(accountsVO);

	    // Remove and recreate ARAP
	    List<ArapDetailsVO> existingAraps = arapDetailsRepo.findByRefNo(savedAccountsVO.getRefNo());
	    if (!existingAraps.isEmpty()) {
	        arapDetailsRepo.deleteAll(existingAraps);
	    }

	    AccountsDetailsVO payableEntry = accountsDetailsRepo.findByAccountsVOAndGstflag(savedAccountsVO, 6);
	    ArapDetailsVO arapDetailsVO = new ArapDetailsVO();
	    arapDetailsVO.setSourceTransid(payableEntry.getId());
	    arapDetailsVO.setCreatedBy(savedAccountsVO.getCreatedBy());
	    arapDetailsVO.setUpdatedBy(savedAccountsVO.getModifiedBy());
	    arapDetailsVO.setBranch(savedAccountsVO.getBranch());
	    arapDetailsVO.setBranchCode(savedAccountsVO.getBranchCode());
	    arapDetailsVO.setFinYear(savedAccountsVO.getFinYear());
	    arapDetailsVO.setRefNo(savedAccountsVO.getRefNo());
	    arapDetailsVO.setRefDate(savedAccountsVO.getRefDate());
	    arapDetailsVO.setSubLedgerCode(payableEntry.getSubLedgerCode());
	    arapDetailsVO.setCurrency(payableEntry.getACurrency());
	    arapDetailsVO.setExRate(payableEntry.getAExRate());
	    arapDetailsVO.setAmount(payableEntry.getArapAmount());
	    arapDetailsVO.setBaseAmt(payableEntry.getArapAmount());
	    arapDetailsVO.setDueDate(savedAccountsVO.getDueDate());
	    arapDetailsVO.setCreditDays(savedAccountsVO.getCreditDays());
	    arapDetailsVO.setDocId(savedAccountsVO.getDocId());
	    arapDetailsVO.setDocDate(savedAccountsVO.getDocDate());
	    arapDetailsVO.setAccCurrency(savedAccountsVO.getCurrency());
	    arapDetailsVO.setExRate(savedAccountsVO.getExRate());
	    arapDetailsVO.setAccName(payableEntry.getAccountName());
	    arapDetailsVO.setGstFlag(payableEntry.getGstflag());
	    arapDetailsVO.setSubLedgerName(payableEntry.getSubledgerName());
	    arapDetailsVO.setSalesType(savedAccountsVO.getSalesType());
	    arapDetailsVO.setNativeAmt(payableEntry.getArapAmount());
	    arapDetailsVO.setOrgId(savedAccountsVO.getOrgId());
	    arapDetailsRepo.save(arapDetailsVO);

	    // Final invoice updates
	    costInvoiceVO.setPurVoucherNo(savedAccountsVO.getDocId());
	    costInvoiceVO.setPurVoucherDate(savedAccountsVO.getDocDate());
	    
		LocalDate vDate = costInvoiceVO.getVDate()!=null?costInvoiceVO.getVDate():costInvoiceVO.getDocDate();
		int creditDays = costInvoiceVO.getCreditDays();
		LocalDate dueDate = vDate.plusDays(creditDays);
		// Save dueDate in your entity
		savedAccountsVO.setDueDate(dueDate);
		costInvoiceVO.setDueDate(dueDate);
	    costInvoiceVO.setApproveStatus(action);
	    costInvoiceVO.setApproveBy(actionBy);
	    costInvoiceVO.setApproveOn(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a")).toUpperCase());

	    return costInvoiceRepo.save(costInvoiceVO);
	}


	
	
	
	
	@Override
	public List<Map<String, Object>> getCreditDaysFromVendor(Long orgId, String supplierCode) {
		Set<Object[]> chDetails = costInvoiceRepo.findCreditDaysFromVendor(orgId, supplierCode);
		return getCreditDaysFromVendor(chDetails);
	}

	private List<Map<String, Object>> getCreditDaysFromVendor(Set<Object[]> chCode) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chCode) {
			Map<String, Object> map = new HashMap<>();
			map.put("creditDays", ch[0] != null ? ch[0].toString() : ""); // Empty string if null
			List1.add(map);
		}
		return List1;

	}

	@Override
	public CostInvoiceVO getCostInvoiceById(Long id) {
		CostInvoiceVO costInvoiceVO = new CostInvoiceVO();

		costInvoiceVO = costInvoiceRepo.getCostInvoiceById(id);

		return costInvoiceVO;
	}

	@Override
	public List<Map<String, Object>> getDsahboardCost(Long orgId, String billMonth, String finYear) {
		Set<Object[]> chType = costInvoiceRepo.getDsahboardCost(orgId, billMonth, finYear);
		return getDash(chType);
	}

	private List<Map<String, Object>> getDash(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			if (ch != null) {
				Map<String, Object> map = new HashMap<>();
				map.put("amount", ch[0] != null ? ch[0].toString() : "0");
				List1.add(map);
			}
		}
		return List1;

	}
}
