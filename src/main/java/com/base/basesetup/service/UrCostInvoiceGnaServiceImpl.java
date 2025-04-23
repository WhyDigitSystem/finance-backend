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

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.basesetup.dto.ChargesUrCostInvoiceGnaDTO;
import com.base.basesetup.dto.TdsUrCostInvoiceGnaDTO;
import com.base.basesetup.dto.UrCostInvoiceGnaDTO;
import com.base.basesetup.entity.AccountsDetailsVO;
import com.base.basesetup.entity.AccountsVO;
import com.base.basesetup.entity.ArapDetailsVO;
import com.base.basesetup.entity.ChargesUrCostInvoiceGnaVO;
import com.base.basesetup.entity.DocumentTypeMappingDetailsVO;
import com.base.basesetup.entity.MultipleDocIdGenerationDetailsVO;
import com.base.basesetup.entity.PartyMasterVO;
import com.base.basesetup.entity.TdsUrCostInvoiceGnaVO;
import com.base.basesetup.entity.UrCostInvoiceGnaVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.AccountsDetailsRepo;
import com.base.basesetup.repo.AccountsRepo;
import com.base.basesetup.repo.ArapDetailsRepo;
import com.base.basesetup.repo.ChargeTypeRequestRepo;
import com.base.basesetup.repo.ChargesUrCostInvoiceGnaRepo;
import com.base.basesetup.repo.CostInvoiceRepo;
import com.base.basesetup.repo.DocumentTypeMappingDetailsRepo;
import com.base.basesetup.repo.GroupLedgerRepo;
import com.base.basesetup.repo.MultipleDocIdGenerationDetailsRepo;
import com.base.basesetup.repo.PartyMasterRepo;
import com.base.basesetup.repo.TdsUrCostInvoiceGnaRepo;
import com.base.basesetup.repo.UrCostInvoiceGnaRepo;

@Service
public class UrCostInvoiceGnaServiceImpl implements UrCostInvoiceGnaService {

	public static final Logger LOGGER = LoggerFactory.getLogger(UrCostInvoiceGnaServiceImpl.class);
	
	@Autowired
	CostInvoiceRepo costInvoiceRepo;

	@Autowired
	UrCostInvoiceGnaRepo urCostInvoiceGnaRepo;

	@Autowired
	ChargesUrCostInvoiceGnaRepo chargesUrCostInvoiceGnaRepo;

	@Autowired
	TdsUrCostInvoiceGnaRepo tdsUrCostInvoiceGnaRepo;

	@Autowired
	GroupLedgerRepo groupLedgerRepo;

	@Autowired
	AmountInWordsConverterService amountInWordsConverterService;

	@Autowired
	PartyMasterRepo partyMasterRepo;

	@Autowired
	ArapDetailsRepo arapDetailsRepo;

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

	@Override
	public Map<String, Object> updateCreateUrCostInvoiceGna(UrCostInvoiceGnaDTO urCostInvoiceGnaDTO)
			throws ApplicationException {
		String screenCode = "URCI";
		UrCostInvoiceGnaVO urCostInvoiceGnaVO = new UrCostInvoiceGnaVO();
		String message;

		if (ObjectUtils.isNotEmpty(urCostInvoiceGnaDTO.getId())) {
			urCostInvoiceGnaVO = urCostInvoiceGnaRepo.findById(urCostInvoiceGnaDTO.getId())
					.orElseThrow(() -> new ApplicationException("UR CostInvoice GNA not found"));

			urCostInvoiceGnaVO.setUpdatedBy(urCostInvoiceGnaDTO.getCreatedBy());
			createUpdateUrCostInvoiceGnaVOByUrCostInvoiceGnaDTO(urCostInvoiceGnaDTO, urCostInvoiceGnaVO);
			message = "UR CostInvoice GNA Updated Successfully";
		} else {
			// GETDOCID API
			String docId = urCostInvoiceGnaRepo.getUrCostInvoiceGnaDocId(urCostInvoiceGnaDTO.getOrgId(),
					urCostInvoiceGnaDTO.getFinYear(), urCostInvoiceGnaDTO.getBranchCode(), screenCode);
			urCostInvoiceGnaVO.setDocId(docId);

			// GETDOCID LASTNO +1
			DocumentTypeMappingDetailsVO documentTypeMappingDetailsVO = documentTypeMappingDetailsRepo
					.findByOrgIdAndFinYearAndBranchCodeAndScreenCode(urCostInvoiceGnaDTO.getOrgId(),
							urCostInvoiceGnaDTO.getFinYear(), urCostInvoiceGnaDTO.getBranchCode(), screenCode);
			documentTypeMappingDetailsVO.setLastno(documentTypeMappingDetailsVO.getLastno() + 1);
			documentTypeMappingDetailsRepo.save(documentTypeMappingDetailsVO);

			urCostInvoiceGnaVO.setCreatedBy(urCostInvoiceGnaDTO.getCreatedBy());
			urCostInvoiceGnaVO.setUpdatedBy(urCostInvoiceGnaDTO.getCreatedBy());
			createUpdateUrCostInvoiceGnaVOByUrCostInvoiceGnaDTO(urCostInvoiceGnaDTO, urCostInvoiceGnaVO);
			message = "UR CostInvoice GNA Created Successfully";
		}
		urCostInvoiceGnaRepo.save(urCostInvoiceGnaVO);
		Map<String, Object> response = new HashMap<>();
		response.put("urCostInvoiceGnaVO", urCostInvoiceGnaVO);
		response.put("message", message);
		return response;
	}

	private void createUpdateUrCostInvoiceGnaVOByUrCostInvoiceGnaDTO(UrCostInvoiceGnaDTO urCostInvoiceGnaDTO,
			UrCostInvoiceGnaVO urCostInvoiceGnaVO) throws ApplicationException {

		urCostInvoiceGnaVO.setOrgId(urCostInvoiceGnaDTO.getOrgId());
		urCostInvoiceGnaVO.setBranch(urCostInvoiceGnaDTO.getBranch());
		urCostInvoiceGnaVO.setBranchCode(urCostInvoiceGnaDTO.getBranchCode());
		urCostInvoiceGnaVO.setFinYear(urCostInvoiceGnaDTO.getFinYear());
		urCostInvoiceGnaVO.setCreatedBy(urCostInvoiceGnaDTO.getCreatedBy());
		urCostInvoiceGnaVO.setSupplierType(urCostInvoiceGnaDTO.getSupplierType());
		urCostInvoiceGnaVO.setSupplierCode(urCostInvoiceGnaDTO.getSupplierCode());
		urCostInvoiceGnaVO.setSupplierName(urCostInvoiceGnaDTO.getSupplierName().toUpperCase());
		urCostInvoiceGnaVO.setSupplierPlace(urCostInvoiceGnaDTO.getSupplierPlace().toUpperCase());
		urCostInvoiceGnaVO.setSupplierBillNo(urCostInvoiceGnaDTO.getSupplierBillNo());
		urCostInvoiceGnaVO.setSupplierBillDate(urCostInvoiceGnaDTO.getSupplierBillDate());
		urCostInvoiceGnaVO.setSupplierCode(urCostInvoiceGnaDTO.getSupplierCode());
		urCostInvoiceGnaVO.setCreditDays(urCostInvoiceGnaDTO.getCreditDays());
		urCostInvoiceGnaVO.setCurrency(urCostInvoiceGnaDTO.getCurrency());
		urCostInvoiceGnaVO.setExRate(urCostInvoiceGnaDTO.getExRate());
		urCostInvoiceGnaVO.setSupplierGstIn(urCostInvoiceGnaDTO.getSupplierGstIn());
		urCostInvoiceGnaVO.setSupplierGstInCode(urCostInvoiceGnaDTO.getSupplierGstInCode());
		urCostInvoiceGnaVO.setRemarks(urCostInvoiceGnaDTO.getRemarks());
		urCostInvoiceGnaVO.setAddress(urCostInvoiceGnaDTO.getAddress());
		urCostInvoiceGnaVO.setOtherInfo(urCostInvoiceGnaDTO.getOtherInfo());
		urCostInvoiceGnaVO.setShipperRefNo(urCostInvoiceGnaDTO.getShipperRefNo());
		urCostInvoiceGnaVO.setGstType(urCostInvoiceGnaDTO.getGstType());
		urCostInvoiceGnaVO.setDueDate(urCostInvoiceGnaDTO.getDueDate());
		urCostInvoiceGnaVO.setVId(urCostInvoiceGnaDTO.getVId());
		urCostInvoiceGnaVO.setVDate(urCostInvoiceGnaDTO.getVDate());
		urCostInvoiceGnaVO.setState(urCostInvoiceGnaDTO.getState());
		urCostInvoiceGnaVO.setMode(urCostInvoiceGnaDTO.getMode());
		urCostInvoiceGnaVO.setAddressType(urCostInvoiceGnaDTO.getAddressType());
//		urCostInvoiceGnaVO.setStatus(urCostInvoiceGnaDTO.getStatus());

		if (ObjectUtils.isNotEmpty(urCostInvoiceGnaDTO.getId())) {
			List<ChargesUrCostInvoiceGnaVO> chargesUrCostInvoiceGnaVO1 = chargesUrCostInvoiceGnaRepo
					.findByUrCostInvoiceGnaVO(urCostInvoiceGnaVO);
			chargesUrCostInvoiceGnaRepo.deleteAll(chargesUrCostInvoiceGnaVO1);

			List<TdsUrCostInvoiceGnaVO> tdsUrCostInvoiceGnaVO1 = tdsUrCostInvoiceGnaRepo
					.findByUrCostInvoiceGnaVO(urCostInvoiceGnaVO);
			tdsUrCostInvoiceGnaRepo.deleteAll(tdsUrCostInvoiceGnaVO1);
		}

		BigDecimal sumOfLcAmount = BigDecimal.ZERO;
		BigDecimal tdsAmount = BigDecimal.ZERO;
		BigDecimal taxAmount = BigDecimal.ZERO;
		BigDecimal totaltdsAmount = BigDecimal.ZERO;

		Map<String, BigDecimal> igstCategorySumMap = new HashMap<>();
		Map<String, BigDecimal> cgstCategorySumMap = new HashMap<>();

		List<ChargesUrCostInvoiceGnaVO> chargesUrCostInvoiceGnaVOs = new ArrayList<>();
		for (ChargesUrCostInvoiceGnaDTO chargesUrCostInvoiceGnaDTO : urCostInvoiceGnaDTO
				.getChargesUrCostInvoiceGnaDTO()) {

			ChargesUrCostInvoiceGnaVO chargesUrCostInvoiceGnaVO = new ChargesUrCostInvoiceGnaVO();
			chargesUrCostInvoiceGnaVO.setChargeLedger(chargesUrCostInvoiceGnaDTO.getChargeLedger());
			chargesUrCostInvoiceGnaVO.setChargeAccount(chargesUrCostInvoiceGnaDTO.getChargeAccount());
			chargesUrCostInvoiceGnaVO.setRate(chargesUrCostInvoiceGnaDTO.getRate());
			chargesUrCostInvoiceGnaVO.setCurrency(chargesUrCostInvoiceGnaDTO.getCurrency());
			chargesUrCostInvoiceGnaVO.setExRate(chargesUrCostInvoiceGnaDTO.getExRate());
			chargesUrCostInvoiceGnaVO.setGSTPercent(chargesUrCostInvoiceGnaDTO.getGSTPercent());

			BigDecimal fcAmount;
			BigDecimal lcAmount;
			BigDecimal billAmount;
			BigDecimal gstPercent = BigDecimal.valueOf(chargesUrCostInvoiceGnaDTO.getGSTPercent());
			BigDecimal gstAmount = BigDecimal.ZERO;

			if (!chargesUrCostInvoiceGnaDTO.getCurrency().equals("INR")) {

				chargesUrCostInvoiceGnaVO.setFcAmount(chargesUrCostInvoiceGnaDTO.getRate());
			} else {
				fcAmount = BigDecimal.valueOf(0.00);
				chargesUrCostInvoiceGnaVO.setFcAmount(fcAmount);
			}

			lcAmount = chargesUrCostInvoiceGnaDTO.getExRate().multiply(chargesUrCostInvoiceGnaDTO.getRate());
			chargesUrCostInvoiceGnaVO.setLcAmount(lcAmount);
			sumOfLcAmount = sumOfLcAmount.add(lcAmount);
			billAmount = lcAmount;
			chargesUrCostInvoiceGnaVO.setBillAmount(billAmount);

			gstAmount = lcAmount.multiply(gstPercent).divide(BigDecimal.valueOf(100));

			if (urCostInvoiceGnaDTO.getGstType().equalsIgnoreCase("INTER")
					&& gstPercent.compareTo(BigDecimal.ZERO) > 0) {
				String igstCategoryKey = gstPercent.toString();
				igstCategorySumMap.put(igstCategoryKey,
						igstCategorySumMap.getOrDefault(igstCategoryKey, BigDecimal.ZERO).add(gstAmount));
			}
			if (urCostInvoiceGnaDTO.getGstType().equalsIgnoreCase("INTRA")
					&& gstPercent.compareTo(BigDecimal.ZERO) > 0) {
				String gstCategoryKey = gstPercent.toString();
				cgstCategorySumMap.put(gstCategoryKey,
						cgstCategorySumMap.getOrDefault(gstCategoryKey, BigDecimal.ZERO).add(gstAmount));
			}

			chargesUrCostInvoiceGnaVO.setUrCostInvoiceGnaVO(urCostInvoiceGnaVO);
			chargesUrCostInvoiceGnaVOs.add(chargesUrCostInvoiceGnaVO);
		}

		if ("INTER".equalsIgnoreCase(urCostInvoiceGnaDTO.getGstType())) {
			for (Map.Entry<String, BigDecimal> entry : igstCategorySumMap.entrySet()) {
				String gstType = "INTER";
				Double gstPercent1 = Double.parseDouble(entry.getKey());
				BigDecimal igstLcAmount = entry.getValue();
				taxAmount = taxAmount.add(igstLcAmount);

				Set<Object[]> groupLedgerVOsOutput = urCostInvoiceGnaRepo.findInterDetailsForUrCostInvoiceGnaPosting(
						urCostInvoiceGnaDTO.getOrgId(), gstType, gstPercent1);
				for (Object[] ch : groupLedgerVOsOutput) {
					ChargesUrCostInvoiceGnaVO outputSummaryVO = new ChargesUrCostInvoiceGnaVO();

					String chargeDesc = ch[0].toString();
					float gstPer = Float.parseFloat(ch[1].toString());
					String currency = ch[2].toString();

					outputSummaryVO.setCurrency(currency);
					outputSummaryVO.setGSTPercent(gstPer);
					outputSummaryVO.setChargeLedger(ch[0].toString());
					outputSummaryVO.setChargeAccount(chargeDesc);
					outputSummaryVO.setRate(BigDecimal.ZERO);
					outputSummaryVO.setExRate(BigDecimal.ZERO);
					outputSummaryVO.setLcAmount(igstLcAmount);
					outputSummaryVO.setFcAmount(BigDecimal.ZERO);
					outputSummaryVO.setBillAmount(igstLcAmount);
					outputSummaryVO.setUrCostInvoiceGnaVO(urCostInvoiceGnaVO);
					chargesUrCostInvoiceGnaVOs.add(outputSummaryVO);
				}

			}
		}

		if ("INTRA".equalsIgnoreCase(urCostInvoiceGnaDTO.getGstType())) {
			for (Map.Entry<String, BigDecimal> entry : cgstCategorySumMap.entrySet()) {
				String gstType = "INTRA";
				Double gstPercent = Double.parseDouble(entry.getKey()) / 2;
				BigDecimal totalTaxAmount = entry.getValue();

				BigDecimal cgstAmount = totalTaxAmount.divide(BigDecimal.valueOf(2));
				taxAmount = taxAmount.add(cgstAmount);

				BigDecimal sgstAmount = totalTaxAmount.divide(BigDecimal.valueOf(2));
				taxAmount = taxAmount.add(sgstAmount);

				Set<Object[]> groupLedgerVOs = urCostInvoiceGnaRepo.findIntraDetailsForUrCostInvoiceGnaPosting(
						urCostInvoiceGnaDTO.getOrgId(), gstType, gstPercent);

				for (Object[] entry1 : groupLedgerVOs) {
					ChargesUrCostInvoiceGnaVO cgstSummaryVO = new ChargesUrCostInvoiceGnaVO();
					cgstSummaryVO.setGSTPercent(Float.parseFloat(entry1[1].toString()));
					cgstSummaryVO.setCurrency(entry1[2].toString());
					cgstSummaryVO.setChargeLedger(entry1[0].toString());
					cgstSummaryVO.setChargeAccount(entry1[0].toString());
					cgstSummaryVO.setExRate(BigDecimal.ZERO);
					cgstSummaryVO.setRate(BigDecimal.ZERO);
					cgstSummaryVO.setLcAmount(cgstAmount);
					cgstSummaryVO.setFcAmount(BigDecimal.ZERO);
					cgstSummaryVO.setBillAmount(cgstAmount);
					cgstSummaryVO.setUrCostInvoiceGnaVO(urCostInvoiceGnaVO);
					chargesUrCostInvoiceGnaVOs.add(cgstSummaryVO);
				}
			}
		}

		urCostInvoiceGnaVO.setChargesUrCostInvoiceGnaVO(chargesUrCostInvoiceGnaVOs);

		List<TdsUrCostInvoiceGnaVO> tdsUrCostInvoiceGnaVOs = new ArrayList<>();
		for (TdsUrCostInvoiceGnaDTO tdsUrCostInvoiceGnaDTO : urCostInvoiceGnaDTO.getTdsUrCostInvoiceGnaDTO()) {
			TdsUrCostInvoiceGnaVO tdsUrCostInvoiceGnaVO = new TdsUrCostInvoiceGnaVO();

			tdsUrCostInvoiceGnaVO.setTdsWithHolding(tdsUrCostInvoiceGnaDTO.getTdsWithHolding());
			tdsUrCostInvoiceGnaVO.setTdsWithHoldingPer(tdsUrCostInvoiceGnaDTO.getTdsWithHoldingPer());
			tdsUrCostInvoiceGnaVO.setSection(tdsUrCostInvoiceGnaDTO.getSection());
			tdsUrCostInvoiceGnaVO.setAccountName(tdsUrCostInvoiceGnaDTO.getAccountName());

			BigDecimal totTdsWhAmt = BigDecimal.ZERO;
			BigDecimal tdsWhPercent = tdsUrCostInvoiceGnaDTO.getTdsWithHoldingPer();
			totTdsWhAmt = sumOfLcAmount.multiply(tdsWhPercent.divide(BigDecimal.valueOf(100)));
			tdsUrCostInvoiceGnaVO.setTotTdsWithAmt(totTdsWhAmt);
			tdsUrCostInvoiceGnaVO.setFcTdsAmt(totTdsWhAmt);
			tdsAmount = totTdsWhAmt;
			tdsUrCostInvoiceGnaVO.setUrCostInvoiceGnaVO(urCostInvoiceGnaVO);
			tdsUrCostInvoiceGnaVOs.add(tdsUrCostInvoiceGnaVO);

		}
		urCostInvoiceGnaVO.setTdsUrCostInvoiceGnaVO(tdsUrCostInvoiceGnaVOs);
		totaltdsAmount = totaltdsAmount.add(tdsAmount);

		BigDecimal netAmountLc = sumOfLcAmount.subtract(totaltdsAmount).add(taxAmount);
		BigDecimal actBillAmtLc = sumOfLcAmount.add(taxAmount);
		BigDecimal roundedValue = netAmountLc.setScale(0, RoundingMode.HALF_UP);
		BigDecimal roundOff = roundedValue.subtract(netAmountLc);
		
		BigDecimal totalTaxAmount = BigDecimal.ZERO;
		
		 totalTaxAmount=totalTaxAmount.add(taxAmount);
		

		urCostInvoiceGnaVO.setTotChargeAmtLc(sumOfLcAmount);
		urCostInvoiceGnaVO.setActBillAmtLc(actBillAmtLc);
		urCostInvoiceGnaVO.setRoundOff(roundOff);
		urCostInvoiceGnaVO.setInput(taxAmount);
		urCostInvoiceGnaVO.setOutput(taxAmount);
		urCostInvoiceGnaVO.setNetamountBillCurr(netAmountLc);
		urCostInvoiceGnaVO.setTotalGstAmount(totalTaxAmount);
		

	}

	@Override
	public List<UrCostInvoiceGnaVO> getUrCostInvoiceGnaById(Long id) {

		List<UrCostInvoiceGnaVO> urCostInvoiceGnaVOList = new ArrayList<>();

		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received  CostInvoice BY Id : {}", id);
			urCostInvoiceGnaVOList = urCostInvoiceGnaRepo.getUrCostInvoiceGnaById(id);

			for (UrCostInvoiceGnaVO urCostInvoiceGnaVO : urCostInvoiceGnaVOList) {
				List<ChargesUrCostInvoiceGnaVO> gstLines = new ArrayList<>();
				List<ChargesUrCostInvoiceGnaVO> normalCharges = new ArrayList<>();

				// Iterate through the chargerCostInvoiceVO list and split charges
				for (ChargesUrCostInvoiceGnaVO charge : urCostInvoiceGnaVO.getChargesUrCostInvoiceGnaVO()) {
					if (isGstCharge(charge)) {
						gstLines.add(charge); // Add GST related charges to gstLines
					} else {
						normalCharges.add(charge); // Add normal charges to normalCharges
					}
				}

				urCostInvoiceGnaVO.setGstLines(gstLines);
				urCostInvoiceGnaVO.setNormalCharges(normalCharges);
			}
		}
		return urCostInvoiceGnaVOList;
	}

	private boolean isGstCharge(ChargesUrCostInvoiceGnaVO charge) {
		// Check if chargeName contains "CGST", "SGST" or "IGST" to identify GST charges
		return charge.getChargeLedger() != null && (charge.getChargeLedger().contains("CGST")
				|| charge.getChargeLedger().contains("SGST") || charge.getChargeLedger().contains("IGST"));

	}

	@Override
	public List<UrCostInvoiceGnaVO> getAllUrCostInvoiceGnaByOrgId(Long orgId, String finYear, String branchCode) {

		return urCostInvoiceGnaRepo.getAllUrCostInvoiceGnaByOrgId(orgId, finYear, branchCode);

	}

	@Override
	public String getUrCostInvoiceGnaDocId(Long orgId, String finYear, String branch, String branchCode) {
		String ScreenCode = "URCI";
		String result = urCostInvoiceGnaRepo.getUrCostInvoiceGnaDocId(orgId, finYear, branchCode, ScreenCode);
		return result;
	}

	@Override
	public List<PartyMasterVO> getAllVendorFromPartyMaster(Long orgId, String partyType) {

		return urCostInvoiceGnaRepo.getAllVendorFromPartyMaster(orgId, partyType);
	}

	@Override
	public List<Map<String, Object>> getChargeLedgerFromGroup(Long orgId) {
		Set<Object[]> chCode = urCostInvoiceGnaRepo.getChargeLedgerFromGroup(orgId);
		return getChargeLedger(chCode);
	}

	private List<Map<String, Object>> getChargeLedger(Set<Object[]> chCode) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chCode) {
			Map<String, Object> map = new HashMap<>();
			map.put("chargeLedger", ch[0] != null ? ch[0].toString() : ""); // Empty string if null
			List1.add(map);
		}
		return List1;
	}

	@Override
	public List<Map<String, Object>> getCurrencyAndExrateFromParty(Long orgId, String supplierCode) {
		Set<Object[]> chCode = urCostInvoiceGnaRepo.getCurrencyAndExrateFromParty(orgId, supplierCode);
		return getCurrencyAndExrate(chCode);
	}

	private List<Map<String, Object>> getCurrencyAndExrate(Set<Object[]> chCode) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chCode) {
			Map<String, Object> map = new HashMap<>();
			map.put("currency", ch[0] != null ? ch[0].toString() : "");
			map.put("buyingexrate", ch[1] != null ? ch[1].toString() : "");
			map.put("sellingexrate", ch[2] != null ? ch[2].toString() : "");
			List1.add(map);
		}
		return List1;
	}

	@Override
	public List<Map<String, Object>> getVendorAddressFromPartyMaster(Long orgId, String supplierCode) {
		Set<Object[]> stateDetails = urCostInvoiceGnaRepo.getVendorAddressFromPartyMaster(orgId, supplierCode);
		return getStateDetails(stateDetails);
	}

	private List<Map<String, Object>> getStateDetails(Set<Object[]> stateDetails) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : stateDetails) {
			Map<String, Object> map = new HashMap<>();
			map.put("stateCode", ch[0] != null ? ch[0].toString() : ""); // Empty string if null
			map.put("state", ch[1] != null ? ch[1].toString() : "");
			map.put("gstin", ch[2] != null ? ch[2].toString() : "");
			map.put("city", ch[3] != null ? ch[3].toString() : "");
			map.put("address", ch[4] != null ? ch[4].toString() : "");
			List1.add(map);
		}
		return List1;
	}

	@Override
	public List<Map<String, Object>> getSectionNameFromMaster(Long orgId, String section) {
		Set<Object[]> sectionName = urCostInvoiceGnaRepo.getSectionNameFromMaster(orgId, section);
		return getSectionName(sectionName);
	}

	private List<Map<String, Object>> getSectionName(Set<Object[]> sectionName) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : sectionName) {
			Map<String, Object> map = new HashMap<>();
			map.put("sectionName", ch[0] != null ? ch[0].toString() : "");
			map.put("tcsPercentage", ch[1] != null ? ch[1].toString() : "");

			List1.add(map);
		}
		return List1;
	}

	@Override
	public UrCostInvoiceGnaVO approveUrCostInvoiceGnaVO(Long orgId, Long id, String docId, String action,
			String actionBy) throws ApplicationException {

		UrCostInvoiceGnaVO urCostInvoiceGnaVO = urCostInvoiceGnaRepo.findByOrgIdAndIdAndDocId(orgId, id, docId);
		String screenCode = "AC";
		String sourceScreenCode = urCostInvoiceGnaVO.getScreenCode();

		if (urCostInvoiceGnaVO.getApproveStatus() == null
				|| (!urCostInvoiceGnaVO.getApproveStatus().equalsIgnoreCase("Approved")
						&& !urCostInvoiceGnaVO.getApproveStatus().equalsIgnoreCase("Rejected"))) {

			String accountsDocId = accountsRepo.geturCostInvoiceGnaDocId(urCostInvoiceGnaVO.getOrgId(),
					urCostInvoiceGnaVO.getFinYear(), urCostInvoiceGnaVO.getBranchCode(), sourceScreenCode, screenCode);
			urCostInvoiceGnaVO.setDocId(docId);

			// GETDOCID LASTNO +1
			MultipleDocIdGenerationDetailsVO mulDocId = multipleDocIdGenerationDetailsRepo
					.findByOrgIdAndFinYearAndBranchCodeAndSourceScreenCodeAndScreenCode(urCostInvoiceGnaVO.getOrgId(),
							urCostInvoiceGnaVO.getFinYear(), urCostInvoiceGnaVO.getBranchCode(), sourceScreenCode,
							screenCode);
			mulDocId.setLastno(mulDocId.getLastno() + 1);
			multipleDocIdGenerationDetailsRepo.save(mulDocId);

			AccountsVO accountsVO = new AccountsVO();
			accountsVO.setDocId(accountsDocId);
			accountsVO.setSourceId(urCostInvoiceGnaVO.getId());
			accountsVO.setCreatedBy(urCostInvoiceGnaVO.getCreatedBy());
			accountsVO.setModifiedon(
					urCostInvoiceGnaVO.getCommonDate() != null ? urCostInvoiceGnaVO.getCommonDate().getModifiedon()
							: null);
			accountsVO.setCreatedon(
					urCostInvoiceGnaVO.getCommonDate() != null ? urCostInvoiceGnaVO.getCommonDate().getModifiedon()
							: null);

			accountsVO.setCancelRemarks(urCostInvoiceGnaVO.getCancelRemarks());
			accountsVO.setFinYear(urCostInvoiceGnaVO.getFinYear());
			accountsVO.setBranch(urCostInvoiceGnaVO.getBranch());
			accountsVO.setBranchCode(urCostInvoiceGnaVO.getBranchCode());
			accountsVO.setRefNo(urCostInvoiceGnaVO.getDocId());
			accountsVO.setRefDate(urCostInvoiceGnaVO.getDocDate());
			accountsVO.setCurrency(urCostInvoiceGnaVO.getCurrency());
			accountsVO.setExRate(urCostInvoiceGnaVO.getExRate());

			BigDecimal totalDebitAmount = urCostInvoiceGnaVO.getNetamountBillCurr();
			accountsVO.setTotalDebitAmount(totalDebitAmount);
			accountsVO.setTotalCreditAmount(totalDebitAmount);
			accountsVO.setDueDate(urCostInvoiceGnaVO.getDueDate());
			accountsVO.setSupplierRefNo(urCostInvoiceGnaVO.getSupplierBillNo());
			accountsVO.setCreditDays(urCostInvoiceGnaVO.getCreditDays());
			accountsVO.setSourceScreen(urCostInvoiceGnaVO.getScreenName());
			accountsVO.setSourceScreenCode(urCostInvoiceGnaVO.getScreenCode());
			accountsVO.setModifiedBy(urCostInvoiceGnaVO.getUpdatedBy());
			accountsVO.setOrgId(urCostInvoiceGnaVO.getOrgId());
			accountsVO.setRemarks(urCostInvoiceGnaVO.getRemarks());
			accountsVO.setChargeableAmount(urCostInvoiceGnaVO.getTotChargeAmtLc());

			List<AccountsDetailsVO> accountsDetailsVOs = new ArrayList<>();

			AccountsDetailsVO accountsDetailsVO = new AccountsDetailsVO();
			BigDecimal totalGstAmount = urCostInvoiceGnaVO.getTotalGstAmount();
			accountsDetailsVO.setNDebitAmount(BigDecimal.ZERO);
			accountsDetailsVO.setACategory("PAYABLE A/C");
			accountsDetailsVO.setAccountName("PAYABLE A/C");
			accountsDetailsVO.setDebitAmount(BigDecimal.ZERO);
			accountsDetailsVO.setNCreditAmount(totalDebitAmount.subtract(totalGstAmount));
			accountsDetailsVO.setCreditAmount(totalDebitAmount.subtract(totalGstAmount));
			accountsDetailsVO.setArapFlag(true);
			accountsDetailsVO.setArapAmount(totalDebitAmount.subtract(totalGstAmount));
			accountsDetailsVO.setBDebitAmount(BigDecimal.ZERO);
			accountsDetailsVO.setBCrAmount(BigDecimal.ZERO);
			accountsDetailsVO.setBArapAmount(BigDecimal.ZERO);
			accountsDetailsVO.setACurrency(urCostInvoiceGnaVO.getCurrency());
			accountsDetailsVO.setAExRate(urCostInvoiceGnaVO.getExRate());
			accountsDetailsVO.setSubledgerName(urCostInvoiceGnaVO.getSupplierName());
			accountsDetailsVO.setSubLedgerCode(urCostInvoiceGnaVO.getSupplierCode());
			accountsDetailsVO.setNArapAmount(BigDecimal.ZERO);
			accountsDetailsVO.setGstflag(6);
			accountsDetailsVO.setAccountsVO(accountsVO);
			accountsDetailsVOs.add(accountsDetailsVO);	
			
			for (TdsUrCostInvoiceGnaVO tdsUrCostInvoiceGnaVO : urCostInvoiceGnaVO.getTdsUrCostInvoiceGnaVO()) {
				 Set<Object[]> tdsLedgers = costInvoiceRepo.getTdsLedgerFromAccount(urCostInvoiceGnaVO.getOrgId());

			        for (Object[] ledger : tdsLedgers) {
				AccountsDetailsVO accountsDetailsVO1 = new AccountsDetailsVO();
				accountsDetailsVO1.setNDebitAmount(BigDecimal.ZERO);
				accountsDetailsVO1.setACategory(ledger[1].toString());
				accountsDetailsVO1.setAccountName(ledger[0].toString());
				accountsDetailsVO1.setDebitAmount(BigDecimal.ZERO);
				accountsDetailsVO1.setNCreditAmount(tdsUrCostInvoiceGnaVO.getTotTdsWithAmt());
				accountsDetailsVO1.setCreditAmount(tdsUrCostInvoiceGnaVO.getTotTdsWithAmt());
				accountsDetailsVO1.setArapFlag(false);
				accountsDetailsVO1.setArapAmount(BigDecimal.ZERO);
				accountsDetailsVO1.setBDebitAmount(BigDecimal.ZERO);
				accountsDetailsVO1.setBCrAmount(BigDecimal.ZERO);
				accountsDetailsVO1.setBArapAmount(BigDecimal.ZERO);
				accountsDetailsVO1.setSubledgerName("None");
				accountsDetailsVO1.setSubLedgerCode("None");
				accountsDetailsVO1.setACurrency(urCostInvoiceGnaVO.getCurrency());
				accountsDetailsVO1.setAExRate(urCostInvoiceGnaVO.getExRate());
				accountsDetailsVO1.setNArapAmount(BigDecimal.ZERO);
				accountsDetailsVO1.setGstflag(3);
				accountsDetailsVO1.setAccountsVO(accountsVO);
				accountsDetailsVOs.add(accountsDetailsVO1);
				
				
			        }

			}

			Map<String, BigDecimal> ledgerAmountMap = new HashMap<>();

			List<ChargesUrCostInvoiceGnaVO> gstChargeList = urCostInvoiceGnaVO.getChargesUrCostInvoiceGnaVO();

			for (ChargesUrCostInvoiceGnaVO gstVO : gstChargeList) {
				String chargeLedger = gstVO.getChargeLedger();
				String gstType = urCostInvoiceGnaVO.getGstType();
				Long orgId1 = urCostInvoiceGnaVO.getOrgId();

				Set<Object[]> outputPosting = urCostInvoiceGnaRepo.getOuputPosting(orgId1, gstType, chargeLedger);
				if (!outputPosting.isEmpty())
					continue;

				Set<Object[]> inputPosting = urCostInvoiceGnaRepo.getInputPosting(orgId1, gstType, chargeLedger);
				if (!inputPosting.isEmpty())
					continue;

				ledgerAmountMap.merge(chargeLedger, gstVO.getBillAmount(), BigDecimal::add);
			}

			for (Map.Entry<String, BigDecimal> entry : ledgerAmountMap.entrySet()) {
				String ledger = entry.getKey();
				BigDecimal totalAmount = entry.getValue();

				Set<Object[]> ledgerPosting = urCostInvoiceGnaRepo.getLedgerPosting(ledger);
				if (ledgerPosting.isEmpty())
					continue;

				for (Object[] ch : ledgerPosting) {
					AccountsDetailsVO accountsDetailsVO1 = new AccountsDetailsVO();
					accountsDetailsVO1.setNDebitAmount(totalAmount);
					accountsDetailsVO1.setACategory(ch[1].toString());
					accountsDetailsVO1.setAccountName(ch[0].toString());
					accountsDetailsVO1.setDebitAmount(totalAmount);
					accountsDetailsVO1.setNCreditAmount(BigDecimal.ZERO);
					accountsDetailsVO1.setCreditAmount(BigDecimal.ZERO);
					accountsDetailsVO1.setArapFlag(false);
					accountsDetailsVO1.setArapAmount(BigDecimal.ZERO);
					accountsDetailsVO1.setBDebitAmount(totalAmount);
					accountsDetailsVO1.setBCrAmount(BigDecimal.ZERO);
					accountsDetailsVO1.setBArapAmount(BigDecimal.ZERO);
					accountsDetailsVO1.setSubledgerName("None");
					accountsDetailsVO1.setSubLedgerCode("None");
					accountsDetailsVO1.setNArapAmount(BigDecimal.ZERO);
					accountsDetailsVO1.setGstflag(3);
					accountsDetailsVO1.setAccountsVO(accountsVO);
					accountsDetailsVOs.add(accountsDetailsVO1);
					break;
				}
			}

			for (ChargesUrCostInvoiceGnaVO gstVO : gstChargeList) {
				String chargeLedger = gstVO.getChargeLedger();
				String gstType = urCostInvoiceGnaVO.getGstType();
				Long orgId1 = urCostInvoiceGnaVO.getOrgId();

				Set<Object[]> outputPosting = urCostInvoiceGnaRepo.getOuputPosting(orgId1, gstType, chargeLedger);
				if (!outputPosting.isEmpty()) {
					for (Object[] ch : outputPosting) {
						AccountsDetailsVO accountsDetailsVO1 = new AccountsDetailsVO();
						accountsDetailsVO1.setNDebitAmount(BigDecimal.ZERO);
						accountsDetailsVO1.setACategory(ch[1].toString());
						accountsDetailsVO1.setAccountName(ch[0].toString());
						accountsDetailsVO1.setDebitAmount(BigDecimal.ZERO);
						accountsDetailsVO1.setNCreditAmount(gstVO.getBillAmount());
						accountsDetailsVO1.setCreditAmount(gstVO.getBillAmount());
						accountsDetailsVO1.setArapFlag(false);
						accountsDetailsVO1.setArapAmount(BigDecimal.ZERO);
						accountsDetailsVO1.setBDebitAmount(BigDecimal.ZERO);
						accountsDetailsVO1.setBCrAmount(BigDecimal.ZERO);
						accountsDetailsVO1.setBArapAmount(BigDecimal.ZERO);
						accountsDetailsVO1.setSubledgerName("None");
						accountsDetailsVO1.setSubLedgerCode("None");
						accountsDetailsVO1.setNArapAmount(BigDecimal.ZERO);
						accountsDetailsVO1.setGstflag(3);
						accountsDetailsVO1.setAccountsVO(accountsVO);
						accountsDetailsVOs.add(accountsDetailsVO1);
					}
					continue;
				}

				Set<Object[]> inputPosting = urCostInvoiceGnaRepo.getInputPosting(orgId1, gstType, chargeLedger);
				if (!inputPosting.isEmpty()) {
					for (Object[] ch : inputPosting) {
						AccountsDetailsVO accountsDetailsVO1 = new AccountsDetailsVO();
						accountsDetailsVO1.setNDebitAmount(gstVO.getBillAmount());
						accountsDetailsVO1.setACategory(ch[1].toString());
						accountsDetailsVO1.setAccountName(ch[0].toString());
						accountsDetailsVO1.setDebitAmount(gstVO.getBillAmount());
						accountsDetailsVO1.setNCreditAmount(BigDecimal.ZERO);
						accountsDetailsVO1.setCreditAmount(BigDecimal.ZERO);
						accountsDetailsVO1.setArapFlag(false);
						accountsDetailsVO1.setArapAmount(BigDecimal.ZERO);
						accountsDetailsVO1.setBDebitAmount(gstVO.getBillAmount());
						accountsDetailsVO1.setBCrAmount(BigDecimal.ZERO);
						accountsDetailsVO1.setBArapAmount(BigDecimal.ZERO);
						accountsDetailsVO1.setSubledgerName("None");
						accountsDetailsVO1.setSubLedgerCode("None");
						accountsDetailsVO1.setNArapAmount(BigDecimal.ZERO);
						accountsDetailsVO1.setGstflag(3);
						accountsDetailsVO1.setAccountsVO(accountsVO);
						accountsDetailsVOs.add(accountsDetailsVO1);
					}
				}
			}

			accountsVO.setAccountsDetailsVO(accountsDetailsVOs);
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
			arapDetailsVO.setSubLedgerName(accountsDetailsVOs2.getAccountName());
			arapDetailsVO.setSalesType(savedAccountsVO.getSalesType());
			arapDetailsVO.setNativeAmt(accountsDetailsVOs2.getArapAmount());
			arapDetailsRepo.save(arapDetailsVO);
			urCostInvoiceGnaVO.setPurVoucherNo(savedAccountsVO.getDocId());
			urCostInvoiceGnaVO.setPurVoucherDate(savedAccountsVO.getDocDate());

			System.out.println("Accounts details saved successfully with Doc ID: " + savedAccountsVO.getDocId());

			urCostInvoiceGnaVO.setApproveStatus(action);
			urCostInvoiceGnaVO.setApproveBy(actionBy);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
			urCostInvoiceGnaVO.setApproveOn(LocalDateTime.now().format(formatter).toUpperCase());

			return urCostInvoiceGnaRepo.save(urCostInvoiceGnaVO);

		} else if (urCostInvoiceGnaVO.getApproveStatus().equals("Approved")) {
			throw new ApplicationException("This Invoice Already Approved,");
		} else {
			throw new ApplicationException("This Invoice Already Rejected");
		}
	}

}
