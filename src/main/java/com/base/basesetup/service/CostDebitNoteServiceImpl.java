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

import com.base.basesetup.dto.ChargerCostDebitNoteDTO;
import com.base.basesetup.dto.CostDebitNoteDTO;
import com.base.basesetup.dto.TdsCostDebitNoteDTO;
import com.base.basesetup.entity.AccountsDetailsVO;
import com.base.basesetup.entity.AccountsVO;
import com.base.basesetup.entity.ArapDetailsVO;
import com.base.basesetup.entity.ChargerCostDebitNoteVO;
import com.base.basesetup.entity.CostDebitNoteVO;
import com.base.basesetup.entity.CostInvoiceVO;
import com.base.basesetup.entity.DocumentTypeMappingDetailsVO;
import com.base.basesetup.entity.GroupLedgerVO;
import com.base.basesetup.entity.MultipleDocIdGenerationDetailsVO;
import com.base.basesetup.entity.TdsCostDebitNoteVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.AccountsDetailsRepo;
import com.base.basesetup.repo.AccountsRepo;
import com.base.basesetup.repo.ArapDetailsRepo;
import com.base.basesetup.repo.ChargesCostDebitNoteRepo;
import com.base.basesetup.repo.CostDebitNoteRepo;
import com.base.basesetup.repo.CostInvoiceRepo;
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
	AccountsDetailsRepo accountsDetailsRepo;

	@Autowired
	GroupLedgerRepo groupLedgerRepo;

	@Autowired
	ArapDetailsRepo arapDetailsRepo;

	@Autowired
	CostInvoiceRepo costInvoiceRepo;

	@Autowired
	AmountInWordsConverterService amountInWordsConverterService;

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

			getCostDebitNoteVOFromCostDebitNoteDTO(costDebitNoteVO, costDebitNoteDTO);
			// GETDOCID API
//			String docId = costDebitNoteRepo.getCostDebitNoteDocId(costDebitNoteDTO.getOrgId(),
//					costDebitNoteDTO.getFinYear(), costDebitNoteDTO.getBranchCode(), screenCode);
//			costDebitNoteVO.setDocId(docId);

			List<Object[]> taxInvoiceDoc = costDebitNoteRepo.getCostDebitNoteDocId(costDebitNoteDTO.getOrgId(),
					costDebitNoteDTO.getFinYear(), costDebitNoteDTO.getBranchCode(), screenCode);

			if (taxInvoiceDoc != null && !taxInvoiceDoc.isEmpty()) {

				Object[] row = taxInvoiceDoc.get(0);

				// ✅ Set docId
				costDebitNoteVO.setDocId((String) row[0]);

				// ✅ Convert java.sql.Date → LocalDate
				if (row[1] != null) {
					costDebitNoteVO.setDocDate(((java.sql.Date) row[1]).toLocalDate());
				}
			}

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
			getCostDebitNoteVOFromCostDebitNoteDTO(costDebitNoteVO, costDebitNoteDTO);
			message = "CostDebitNote Updation Successfully";
		}
//
//		 getCostDebitNoteVOFromCostDebitNoteDTO(costDebitNoteVO, costDebitNoteDTO);
		costDebitNoteRepo.save(costDebitNoteVO);

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("costDebitNoteVO", costDebitNoteVO);
		return response;
	}

	private CostDebitNoteVO getCostDebitNoteVOFromCostDebitNoteDTO(CostDebitNoteVO costDebitNoteVO,
			@Valid CostDebitNoteDTO costDebitNoteDTO) throws ApplicationException {

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
//		System.out.println("Test    ....:" + costDebitNoteDTO.getOrgId());
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
//		costDebitNoteVO.setCostType(costDebitNoteDTO.getCostType());
		costDebitNoteVO.setOrginBill(costDebitNoteDTO.getOrginBill());
//		System.out.println("Test    ....:" + costDebitNoteDTO.getOrginBill());
		costDebitNoteVO.setOrginBillDate(costDebitNoteDTO.getOriginBillDate());
//		costDebitNoteVO.setApproved(costDebitNoteDTO.isApproved());
		costDebitNoteVO.setMode(costDebitNoteDTO.getMode());
//		costDebitNoteVO.setPurVoucherNo(costDebitNoteDTO.getPurVoucherNo());
//		costDebitNoteVO.setPurVoucherDate(costDebitNoteDTO.getPurVoucherDate());
		costDebitNoteVO.setVId(costDebitNoteDTO.getVId());
		costDebitNoteVO.setVDate(costDebitNoteDTO.getVDate());

		// Set individual fields from DTO to VO

		// Deleting existing entries if updating
		if (costDebitNoteDTO.getId() != null) {

			List<ChargerCostDebitNoteVO> chargerCostDebitNoteVO1 = chargesCostDebitNoteRepo
					.findByCostDebitNoteVO(costDebitNoteVO);
			chargesCostDebitNoteRepo.deleteAll(chargerCostDebitNoteVO1);

			List<TdsCostDebitNoteVO> tdsCostDebitNoteVO1 = tdsCostDebitNoteRepo.findByCostDebitNoteVO(costDebitNoteVO);
			tdsCostDebitNoteRepo.deleteAll(tdsCostDebitNoteVO1);
		}

		BigDecimal sumBillAmount = BigDecimal.ZERO;
		BigDecimal sumLcAmount = BigDecimal.ZERO;

		BigDecimal taxAmount = BigDecimal.ZERO;
		BigDecimal tdsAmount = BigDecimal.ZERO;

		BigDecimal gstInputAmount = BigDecimal.ZERO;

		List<ChargerCostDebitNoteVO> chargerCostDebitVOs = new ArrayList<>();

		// Map to store IGST sums by GST percentage
		Map<String, BigDecimal> igstCategorySumMap = new HashMap<>();
		Map<String, BigDecimal> cgstCategorySumMap = new HashMap<>();

		for (ChargerCostDebitNoteDTO chargerCostDebitDTO : costDebitNoteDTO.getChargerCostDebitNoteDTO()) {
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
//			chargerCostDebitVO.setExempted(chargerCostDebitDTO.getExempted());
			chargerCostDebitVO.setTaxable(chargerCostDebitDTO.getTaxable());
			chargerCostDebitVO.setDescription(chargerCostDebitDTO.getDescription());
			chargerCostDebitVO.setGSTPercent(chargerCostDebitDTO.getGSTPercent());

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
			BigDecimal gstPercent = BigDecimal.valueOf(chargerCostDebitDTO.getGSTPercent());

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

			gstInputAmount = gstInputAmount.add(gstAmount);

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

		if ("INTER".equalsIgnoreCase(costDebitNoteDTO.getGstType())) {
			for (Map.Entry<String, BigDecimal> entry : igstCategorySumMap.entrySet()) {
				ChargerCostDebitNoteVO igstSummaryVO = new ChargerCostDebitNoteVO();

				String gstType = "INTER";
				Double gstPercent = Double.parseDouble(entry.getKey());
				BigDecimal igstLcAmount = entry.getValue();
//				if (igstSummaryVO.getGSTPercent() != 0) {
				taxAmount = taxAmount.add(igstLcAmount);

				Set<Object[]> groupLedgerVOs = costDebitNoteRepo
						.findInterDetailsForCostDebitNotePosting(costDebitNoteDTO.getOrgId(), gstType, gstPercent);
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
					igstSummaryVO.setCostDebitNoteVO(costDebitNoteVO);
					chargerCostDebitVOs.add(igstSummaryVO);
				}
			}
		}

//		ADD CGST and SGST ROWS FOR EACH GST PERCENTAGE IN cgstCategorySumMap
		if ("INTRA".equalsIgnoreCase(costDebitNoteDTO.getGstType())) {
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

				Set<Object[]> groupLedgerVOs = costDebitNoteRepo
						.findIntraDetailsForCostDebitNotePosting(costDebitNoteDTO.getOrgId(), gstType, gstPercent);

				for (Object[] entry1 : groupLedgerVOs) {
					ChargerCostDebitNoteVO cgstSummaryVO = new ChargerCostDebitNoteVO();
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
					cgstSummaryVO.setCostDebitNoteVO(costDebitNoteVO);
					chargerCostDebitVOs.add(cgstSummaryVO);

				}
			}
		}

		costDebitNoteVO.setChargerCostDebitNoteVO(chargerCostDebitVOs);

		// TDS table
		List<TdsCostDebitNoteVO> tdsCostDebitVOs = new ArrayList<>();
		for (TdsCostDebitNoteDTO tdsCostDebitDTO : costDebitNoteDTO.getTdsCostDebitNoteDTO()) {
			TdsCostDebitNoteVO tdsCostDebitVO = new TdsCostDebitNoteVO();

			tdsCostDebitVO.setTdsWithHolding(tdsCostDebitDTO.getTdsWithHolding());
			tdsCostDebitVO.setTdsWithHoldingPer(tdsCostDebitDTO.getTdsWithHoldingPer());
			tdsCostDebitVO.setSection(tdsCostDebitDTO.getSection());

			BigDecimal totTdsWhAmt = BigDecimal.ZERO;
			BigDecimal tdsWhPercent = tdsCostDebitDTO.getTdsWithHoldingPer();

			if (tdsWhPercent != null) {
				System.out.println("TOTAL LC AMOUNT IS :" + sumLcAmount);
				totTdsWhAmt = sumLcAmount.multiply(tdsWhPercent.divide(BigDecimal.valueOf(100)));
			} else {
				System.out.println("TDS Withholding Percentage is null, defaulting to ZERO.");
			}

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
		BigDecimal roundedValue = totChargeAmtLc.setScale(0, RoundingMode.HALF_UP);
		System.out.println(roundedValue);
		BigDecimal sumDebitAmount = sumLcAmount.add(taxAmount);
		BigDecimal roundOff = roundedValue.subtract(totChargeAmtLc);

		costDebitNoteVO.setTotChargesBillCurrAmt(totChargeAmtBillCurr);
//		costDebitNoteVO.setNetBillCurrAmt(netAmountBillCurr);
		costDebitNoteVO.setNetBillLcAmt(netAmountLc);
		costDebitNoteVO.setTotChargesLcAmt(totChargeAmtLc);

		System.out.println("orgid :" + costDebitNoteDTO.getOrgId() + "orginbill" + costDebitNoteDTO.getOrginBill());

		CostInvoiceVO costInvoiceVO = costInvoiceRepo.findByOrgIdAndDocId(costDebitNoteDTO.getOrgId(),
				costDebitNoteDTO.getOrginBill());
		if (costInvoiceVO == null) {
			System.out.println("DEBUG: No CostInvoice found for OrgId: " + costDebitNoteDTO.getOrgId() + " and DocId: "
					+ costDebitNoteDTO.getOrginBill());
			throw new ApplicationException("No CostInvoice found for given orgId and docId");
		}

		BigDecimal sumLcAmounts = costInvoiceVO.getNetBillCurrAmt();

		System.out.println(costInvoiceVO.getNetBillCurrAmt());
//		System.out.println(roundedValue);

//		if (netAmountBillCurr.compareTo(sumLcAmounts) <= 0) {
//			costDebitNoteVO.setNetBillCurrAmt(netAmountBillCurr);
//
//		} else {
//			throw new IllegalArgumentException("COSTDEBITNOTE " + netAmountBillCurr
//					+ " must be less than or equal to COSTINVOICE  " + sumLcAmounts);
//		}

//		
//		if (netAmountBillCurr.compareTo(sumLcAmounts) <= 0) {
//			costDebitNoteVO.setNetBillCurrAmt(netAmountBillCurr);
//
//		} else {
//			throw new IllegalArgumentException("COSTDEBITNOTE " + netAmountBillCurr
//					+ " must be less than or equal to COSTINVOICE  " + sumLcAmounts);
//		}

//		Set<Object[]> byOrginBillBased = costDebitNoteRepo.findByOrginBillBased(costDebitNoteDTO.getOrgId(),
//				costDebitNoteDTO.getOrginBill());
//		
		Set<Object[]> byOrginBillBased =
			    costDebitNoteRepo.findByOrginBillBased(
			        costDebitNoteDTO.getOrgId(),
			        costDebitNoteDTO.getOrginBill(),
			        costDebitNoteDTO.getId()
			    );

		for (Object[] ledger : byOrginBillBased) {
			BigDecimal remainingAmount = (BigDecimal) ledger[4];

			if (netAmountBillCurr.compareTo(remainingAmount) <= 0) {
				if (netAmountBillCurr.compareTo(sumLcAmounts) <= 0) {
					costDebitNoteVO.setNetBillCurrAmt(netAmountBillCurr);
				} else {
					throw new IllegalArgumentException("COSTDEBITNOTE" + netAmountBillCurr
							+ " must be less than or equal to COSTINVOICE " + sumLcAmounts);
				}
			} else {
				throw new IllegalArgumentException("CREDIT NOTE " + netAmountBillCurr
						+ " must be less than or equal to REMAINING amount " + remainingAmount);
			}
		}

		costDebitNoteVO.setActBillCurrAmt(actBillAmtBillCurr);
		costDebitNoteVO.setActBillLcAmt(actBillAmtLc);
		costDebitNoteVO.setRoundOff(roundOff);
		costDebitNoteVO.setGstInputLcAmt(gstInputAmount);
		costDebitNoteVO.setSumLcAmt(sumDebitAmount);
		costDebitNoteVO.setAmountInWords(amountInWordsConverterService.convert(costInvoiceVO.getNetBillCurrAmt()));

//		costDebitNoteVO.setChargerCostDebitNoteVO(chargerCostDebitVOs);

		return costDebitNoteVO;
	}

	@Override
	public List<CostDebitNoteVO> getCostDebitNoteByOrgId(Long orgId, String finYear, String branchCode) {

		return costDebitNoteRepo.getByCostDebitNoteByOrgId(orgId, finYear, branchCode);
	}

	@Override
	public List<CostDebitNoteVO> getCostDebitNoteById(Long id) {

		List<CostDebitNoteVO> costInvoiceVOList = new ArrayList<>();

		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received  CostInvoice BY Id : {}", id);
			costInvoiceVOList = costDebitNoteRepo.getByCostDebitNoteById(id);

			for (CostDebitNoteVO costDebitNoteVO : costInvoiceVOList) {
				List<ChargerCostDebitNoteVO> gstLines = new ArrayList<>();
				List<ChargerCostDebitNoteVO> normalCharges = new ArrayList<>();

				// Iterate through the chargerCostInvoiceVO list and split charges
				for (ChargerCostDebitNoteVO charge : costDebitNoteVO.getChargerCostDebitNoteVO()) {
					if (isGstCharge(charge)) {
						gstLines.add(charge); // Add GST related charges to gstLines
					} else {
						normalCharges.add(charge); // Add normal charges to normalCharges
					}
				}

				costDebitNoteVO.setGstLines(gstLines);
				costDebitNoteVO.setNormalCharges(normalCharges);
			}
		}
		return costInvoiceVOList;
	}

	private boolean isGstCharge(ChargerCostDebitNoteVO charge) {
		// Check if chargeName contains "CGST", "SGST" or "IGST" to identify GST charges
		return charge.getChargeName() != null && (charge.getChargeName().contains("CGST")
				|| charge.getChargeName().contains("SGST") || charge.getChargeName().contains("IGST"));
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
//	@Override
//	public String getCostDebitNoteDocId(Long orgId, String finYear, String branch, String branchCode) {
//		String ScreenCode = "CDN";
//		String result = costDebitNoteRepo.getCostDebitNoteDocId(orgId, finYear, branchCode, ScreenCode);
//		return result;
//	}

	@Override
	public Map<String, Object> getCostDebitNoteDocId(Long orgId, String finYear, String branch, String branchCode) {

		String screenCode = "CDN";

		List<Object[]> results = costDebitNoteRepo.getCostDebitNoteDocId(orgId, finYear, branchCode, screenCode);

		Map<String, Object> map = new HashMap<>();

		if (results != null && !results.isEmpty()) {
			Object[] row = results.get(0);

			map.put("docId", row[0]);
			map.put("docDate", row.length > 1 ? row[1] : null);
		}

		return map;
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
	public List<CostInvoiceVO> getOriginBillNofromCostInvoiceByParty(Long orgId, String party, String branchCode) {
//		 List<CostInvoiceVO> existingInvoices = costInvoiceRepo.getCheck(orgId, party);
//		    List<CostInvoiceVO> allPartyInvoices = costInvoiceRepo.findOrginBillNoByParty(orgId, party, branchCode);
//
//		    if (existingInvoices == null || existingInvoices.isEmpty()) {
//		        return allPartyInvoices; 
//		    }
//
//		    Set<String> existingInvoiceNumbers = existingInvoices.stream()
//		            .map(CostInvoiceVO::getDocId) 
//		            .collect(Collectors.toSet());
//
//		    return allPartyInvoices.stream()
//		            .filter(invoice -> !existingInvoiceNumbers.contains(invoice.getDocId())) 
//		            .collect(Collectors.toList());

		return costInvoiceRepo.findOrginBillNoByParty(orgId, party, branchCode);

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

//			String accountsDocId = accountsRepo.getApproveDocId(costDebitNoteVO.getOrgId(),
//					costDebitNoteVO.getFinYear(), costDebitNoteVO.getBranchCode(), sourceScreenCode, screenCode);
////			costDebitNoteVO.setDocId(docId);

			List<Object[]> taxInvoiceDoc = accountsRepo.getApproveDocId(costDebitNoteVO.getOrgId(),
					costDebitNoteVO.getFinYear(), costDebitNoteVO.getBranchCode(), sourceScreenCode, screenCode);

			String generatedDocId = null;
			LocalDate generatedDocDate = null;

			if (taxInvoiceDoc != null && !taxInvoiceDoc.isEmpty()) {
				Object[] row = taxInvoiceDoc.get(0);
				generatedDocId = (String) row[0];
				if (row[1] != null) {
					generatedDocDate = ((java.sql.Date) row[1]).toLocalDate();
				}
			}
			costDebitNoteVO.setPurVoucherNo(generatedDocId);
			costDebitNoteVO.setPurVoucherDate(generatedDocDate);

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
			accountsVO.setDocId(generatedDocId);
			accountsVO.setDocDate(generatedDocDate);
			accountsVO.setSourceScreen(costDebitNoteVO.getScreenName());
			accountsVO.setSourceScreenCode(costDebitNoteVO.getScreenCode());
			accountsVO.setSourceId(costDebitNoteVO.getId());
			accountsVO.setVId(costDebitNoteVO.getVId());
			accountsVO.setVDate(costDebitNoteVO.getVDate());
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
//						accountsVO.setBillMonth(costDebitNoteVO.getbil());
			accountsVO.setFinYear(costDebitNoteVO.getFinYear());

			// Calculate total debit/credit amounts
			BigDecimal totalDebitAmount = costDebitNoteVO.getNetBillCurrAmt();
			accountsVO.setTotalDebitAmount(totalDebitAmount);
			accountsVO.setTotalCreditAmount(totalDebitAmount);
			accountsVO.setCreditDays(costDebitNoteVO.getCreditDays());
			accountsVO.setAmountInWords(costDebitNoteVO.getAmountInWords());
			accountsVO.setStTaxAmount(costDebitNoteVO.getGstInputLcAmt());
			accountsVO.setChargeableAmount(costDebitNoteVO.getTotChargesLcAmt());
//						accountsVO.setSalesType(costDebitNoteVO.getSalesType());

			// Create AccountsDetailsVO list and populate it
			List<AccountsDetailsVO> accountsDetailsVOs = new ArrayList<>();

			// Add PAYABLE A/C entry
			AccountsDetailsVO accountsDetailsVO = new AccountsDetailsVO();
			accountsDetailsVO.setNDebitAmount(costDebitNoteVO.getNetBillCurrAmt());
			accountsDetailsVO.setACategory("PAYABLE A/C");
			accountsDetailsVO.setAccountName("PAYABLE A/C");
			accountsDetailsVO.setDebitAmount(costDebitNoteVO.getNetBillCurrAmt());
			accountsDetailsVO.setNCreditAmount(BigDecimal.ZERO);
			accountsDetailsVO.setCreditAmount(BigDecimal.ZERO);
			accountsDetailsVO.setArapFlag(true);
			accountsDetailsVO.setArapAmount(costDebitNoteVO.getNetBillCurrAmt().multiply(new BigDecimal(-1)));
			accountsDetailsVO.setBDebitAmount(costDebitNoteVO.getNetBillCurrAmt());
			accountsDetailsVO.setBCrAmount(BigDecimal.ZERO);
			accountsDetailsVO.setBArapAmount(costDebitNoteVO.getNetBillCurrAmt().multiply(new BigDecimal(-1)));
			accountsDetailsVO.setACurrency(costDebitNoteVO.getCurrency());
			accountsDetailsVO.setAExRate(costDebitNoteVO.getExRate());
			accountsDetailsVO.setSubledgerName(costDebitNoteVO.getSupplierName());
			accountsDetailsVO.setSubLedgerCode(costDebitNoteVO.getSupplierCode());
			accountsDetailsVO.setNArapAmount(costDebitNoteVO.getNetBillCurrAmt().multiply(new BigDecimal(-1)));
			accountsDetailsVO.setGstflag(6);
			accountsDetailsVO.setAccountsVO(accountsVO);
			accountsDetailsVOs.add(accountsDetailsVO);

			for (TdsCostDebitNoteVO tdsCostDebitVO : costDebitNoteVO.getTdsCostDebitNoteVO()) {

				Set<Object[]> ch = costDebitNoteRepo.getAccountNameFromTDSLedger(costDebitNoteVO.getOrgId());

				for (Object[] ch1 : ch) {

					AccountsDetailsVO accountsDetailsVO1 = new AccountsDetailsVO();
					accountsDetailsVO1.setNDebitAmount(tdsCostDebitVO.getTotTdsWhAmnt());
					accountsDetailsVO1.setACategory(ch1[1].toString());
					accountsDetailsVO1.setAccountName(ch1[0].toString());
					accountsDetailsVO1.setDebitAmount(tdsCostDebitVO.getTotTdsWhAmnt());
					accountsDetailsVO1.setNCreditAmount(BigDecimal.ZERO);
					accountsDetailsVO1.setCreditAmount(BigDecimal.ZERO);
					accountsDetailsVO1.setArapFlag(false);
					accountsDetailsVO1.setArapAmount(BigDecimal.ZERO);
					accountsDetailsVO1.setBDebitAmount(tdsCostDebitVO.getTotTdsWhAmnt());
					accountsDetailsVO1.setBCrAmount(BigDecimal.ZERO);
					accountsDetailsVO1.setBArapAmount(BigDecimal.ZERO);
					accountsDetailsVO1.setSubledgerName("None");
					accountsDetailsVO1.setSubLedgerCode("None");
					accountsDetailsVO1.setACurrency(costDebitNoteVO.getCurrency());
					accountsDetailsVO1.setAExRate(costDebitNoteVO.getExRate());
					accountsDetailsVO1.setNArapAmount(BigDecimal.ZERO);
					accountsDetailsVO1.setGstflag(3);
					accountsDetailsVO1.setAccountsVO(accountsVO);
					accountsDetailsVOs.add(accountsDetailsVO1);

				}

			}

//						for (ChargerCostDebitNoteVO gstVO : costDebitNoteVO.getChargerCostDebitNoteVO()) {
////							String ledger = gstVO.getLedger();
////							BigDecimal lcAmount = gstVO.getLcAmt();
//			//
////							ledgerSumMap.put(ledger, ledgerSumMap.getOrDefault(ledger, BigDecimal.ZERO).add(lcAmount));
////						}
//			//
////						// Add GST ledger entries
////						for (Map.Entry<String, BigDecimal> entry : ledgerSumMap.entrySet()) {
//							GroupLedgerVO groupLedgerVO = groupLedgerRepo.findByAccountGroupName(gstVO.getLedger());

			Map<String, BigDecimal> ledgerSumMap = new HashMap<>();
			for (ChargerCostDebitNoteVO gstVO : costDebitNoteVO.getChargerCostDebitNoteVO()) {
				String ledger = gstVO.getLedger();
				BigDecimal lcAmount = gstVO.getLcAmt();

				ledgerSumMap.put(ledger, ledgerSumMap.getOrDefault(ledger, BigDecimal.ZERO).add(lcAmount));
			}

			// Add GST ledger entries
			for (Map.Entry<String, BigDecimal> entry : ledgerSumMap.entrySet()) {

				GroupLedgerVO groupLedgerVO = groupLedgerRepo.findByAccountGroupName(entry.getKey());

				if(groupLedgerVO == null) {
				    throw new ApplicationException(
				        "Ledger not found : " + entry.getKey()
				    );
				}
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
			int gstflag = 6;
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
			arapDetailsVO.setRefDate(savedAccountsVO.getRefDate());
			arapDetailsVO.setSubLedgerCode(accountsDetailsVOs2.getSubLedgerCode());
			arapDetailsVO.setCurrency(accountsDetailsVOs2.getACurrency());
			arapDetailsVO.setExRate(accountsDetailsVOs2.getAExRate());
			arapDetailsVO.setAmount(accountsDetailsVOs2.getArapAmount().multiply(new BigDecimal(1)));
			arapDetailsVO.setBaseAmt(accountsDetailsVOs2.getArapAmount().multiply(new BigDecimal(1)));
			arapDetailsVO.setDueDate(savedAccountsVO.getDueDate());
			arapDetailsVO.setCreditDays(savedAccountsVO.getCreditDays());
			arapDetailsVO.setDocId(savedAccountsVO.getDocId());
			arapDetailsVO.setDocDate(savedAccountsVO.getDocDate());
			arapDetailsVO.setAccCurrency(savedAccountsVO.getCurrency());
			arapDetailsVO.setExRate(savedAccountsVO.getExRate());
			arapDetailsVO.setOrgId(savedAccountsVO.getOrgId());
			arapDetailsVO.setActive(savedAccountsVO.isActive());
			arapDetailsVO.setAccName(accountsDetailsVOs2.getAccountName());
			arapDetailsVO.setGstFlag(accountsDetailsVOs2.getGstflag());
			arapDetailsVO.setSubLedgerName(accountsDetailsVOs2.getSubledgerName());
			arapDetailsVO.setSalesType(savedAccountsVO.getSalesType());
			arapDetailsVO.setNativeAmt(accountsDetailsVOs2.getArapAmount().multiply(new BigDecimal(1)));
			arapDetailsRepo.save(arapDetailsVO);
			costDebitNoteVO.setPurVoucherNo(savedAccountsVO.getDocId());
			costDebitNoteVO.setPurVoucherDate(savedAccountsVO.getDocDate());

			LocalDate vDate = costDebitNoteVO.getVDate() != null ? costDebitNoteVO.getVDate()
					: costDebitNoteVO.getDocDate();
			int creditDays = costDebitNoteVO.getCreditDays();
			LocalDate dueDate = vDate.plusDays(creditDays);
			// Save dueDate in your entity
			savedAccountsVO.setDueDate(dueDate);
			costDebitNoteVO.setDueDate(dueDate);
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

	@Override
	public List<Map<String, Object>> getCostDebitNoteCount(Long orgId, String finYear, String branchCode) {
		Set<Object[]> chType = costDebitNoteRepo.getCostDebitNoteCount(orgId, finYear, branchCode);
		return getCostDebitNoteCount(chType);
	}

	private List<Map<String, Object>> getCostDebitNoteCount(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();
			map.put("Complete", ch[0] != null ? ch[0].toString() : "");
			map.put("Approved", ch[1] != null ? ch[1].toString() : "");
			map.put("Pending", ch[2] != null ? ch[2].toString() : "");
			map.put("Reject", ch[3] != null ? ch[3].toString() : "");

			List1.add(map);
		}
		return List1;
	}
}
