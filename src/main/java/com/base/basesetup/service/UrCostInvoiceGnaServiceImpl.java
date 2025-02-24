package com.base.basesetup.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import com.base.basesetup.entity.ChargesUrCostInvoiceGnaVO;
import com.base.basesetup.entity.DocumentTypeMappingDetailsVO;
import com.base.basesetup.entity.PartyMasterVO;
import com.base.basesetup.entity.TdsUrCostInvoiceGnaVO;
import com.base.basesetup.entity.UrCostInvoiceGnaVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.AccountsDetailsRepo;
import com.base.basesetup.repo.AccountsRepo;
import com.base.basesetup.repo.ArapDetailsRepo;
import com.base.basesetup.repo.ChargeTypeRequestRepo;
import com.base.basesetup.repo.ChargesUrCostInvoiceGnaRepo;
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
			UrCostInvoiceGnaVO urCostInvoiceGnaVO) {

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

		BigDecimal netAmountLc = sumOfLcAmount.subtract(totaltdsAmount);
		BigDecimal actBillAmtLc = sumOfLcAmount.subtract(totaltdsAmount);
		BigDecimal roundedValue = netAmountLc.setScale(0, RoundingMode.HALF_UP);
		BigDecimal roundOff = roundedValue.subtract(netAmountLc);
		urCostInvoiceGnaVO.setTotChargeAmtLc(sumOfLcAmount);
		urCostInvoiceGnaVO.setActBillAmtLc(actBillAmtLc);
		urCostInvoiceGnaVO.setRoundOff(roundOff);
		urCostInvoiceGnaVO.setInput(taxAmount);
		urCostInvoiceGnaVO.setOutput(taxAmount);
		urCostInvoiceGnaVO.setNetamountBillCurr(roundedValue);

	}

	@Override
	public UrCostInvoiceGnaVO getUrCostInvoiceGnaById(Long id) {

		return urCostInvoiceGnaRepo.getUrCostInvoiceGnaById(id);

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
	public List<Map<String, Object>> getCurrencyAndExrateFromParty(Long orgId) {
		Set<Object[]> chCode = urCostInvoiceGnaRepo.getCurrencyAndExrateFromParty(orgId);
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
			map.put("state", ch[0] != null ? ch[0].toString() : ""); // Empty string if null
			map.put("stateCode", ch[1] != null ? ch[1].toString() : "");
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

}
