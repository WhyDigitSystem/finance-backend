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

import com.base.basesetup.dto.ChargeRCostInvoiceGnaDTO;
import com.base.basesetup.dto.RCostInvoiceGnaDTO;
import com.base.basesetup.dto.TdsRCostInvoiceGnaDTO;
import com.base.basesetup.entity.AccountsDetailsVO;
import com.base.basesetup.entity.AccountsVO;
import com.base.basesetup.entity.ArapDetailsVO;
import com.base.basesetup.entity.ChargeRCostInvoiceGnaVO;
import com.base.basesetup.entity.DocumentTypeMappingDetailsVO;
import com.base.basesetup.entity.GroupLedgerVO;
import com.base.basesetup.entity.MultipleDocIdGenerationDetailsVO;
import com.base.basesetup.entity.PartyMasterVO;
import com.base.basesetup.entity.RCostInvoiceGnaVO;
import com.base.basesetup.entity.TdsRCostInvoiceGnaVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.AccountsDetailsRepo;
import com.base.basesetup.repo.AccountsRepo;
import com.base.basesetup.repo.ArapDetailsRepo;
import com.base.basesetup.repo.ChargeRCostInvoiceGnaRepo;
import com.base.basesetup.repo.CostInvoiceRepo;
import com.base.basesetup.repo.DocumentTypeMappingDetailsRepo;
import com.base.basesetup.repo.GroupLedgerRepo;
import com.base.basesetup.repo.MultipleDocIdGenerationDetailsRepo;
import com.base.basesetup.repo.RCostInvoiceGnaRepo;
import com.base.basesetup.repo.TdsRCostInvoiceGnaRepo;

@Service
public class RCostInvoiceGnaServiceImpl implements RCostInvoiceGnaService {

	public static final Logger LOGGER = LoggerFactory.getLogger(RCostInvoiceGnaServiceImpl.class);
	
	@Autowired
	CostInvoiceRepo costInvoiceRepo;

	@Autowired
	RCostInvoiceGnaRepo rCostInvoiceGnaRepo;

	@Autowired
	DocumentTypeMappingDetailsRepo documentTypeMappingDetailsRepo;

	@Autowired
	AmountInWordsConverterService amountInWordsConverterService;

	@Autowired
	ChargeRCostInvoiceGnaRepo chargeRCostInvoiceGnaRepo;

	@Autowired
	TdsRCostInvoiceGnaRepo tdsRCostInvoiceGnaRepo;

	@Autowired
	GroupLedgerRepo groupLedgerRepo;

	@Autowired
	AccountsRepo accountsRepo;
	

	@Autowired
	ArapDetailsRepo arapDetailsRepo;
	
	@Autowired
	AccountsDetailsRepo accountsDetailsRepo;

	@Autowired
	MultipleDocIdGenerationDetailsRepo multipleDocIdGenerationDetailsRepo;

	@Override
	public List<RCostInvoiceGnaVO> getAllRCostInvoiceGnaByOrgId(Long orgId) {
		List<RCostInvoiceGnaVO> rCostInvoiceGnaVO = new ArrayList<>();
		rCostInvoiceGnaVO = rCostInvoiceGnaRepo.getAllCostInvoiceByOrgId(orgId);
		return rCostInvoiceGnaVO;
	}

	@Override
	public List<RCostInvoiceGnaVO> getAllRCostInvoiceGnaById(Long id) {
		List<RCostInvoiceGnaVO> rCostInvoiceGnaVOList = new ArrayList<>();

		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received  RCostInvoiceGna BY Id : {}", id);
			rCostInvoiceGnaVOList = rCostInvoiceGnaRepo.getAllRCostInvoiceGnaById(id);

			for (RCostInvoiceGnaVO rCostInvoiceGnaVO : rCostInvoiceGnaVOList) {
				List<ChargeRCostInvoiceGnaVO> gstLines = new ArrayList<>();
				List<ChargeRCostInvoiceGnaVO> normalCharges = new ArrayList<>();

				for (ChargeRCostInvoiceGnaVO charge : rCostInvoiceGnaVO.getChargeRCostInvoiceGnaVO()) {
					if (isGstCharge(charge)) {
						gstLines.add(charge); // Add GST related charges to gstLines
					} else {
						normalCharges.add(charge); // Add normal charges to normalCharges
					}
				}

				rCostInvoiceGnaVO.setGstLines(gstLines);
				rCostInvoiceGnaVO.setNormalCharges(normalCharges);
			}
		}
		return rCostInvoiceGnaVOList;
	}

	private boolean isGstCharge(ChargeRCostInvoiceGnaVO charge) {
		// Check if chargeName contains "CGST", "SGST" or "IGST" to identify GST charges
		return charge.getChargeName() != null && (charge.getChargeName().contains("CGST")
				|| charge.getChargeName().contains("SGST") || charge.getChargeName().contains("IGST"));
	}

	@Override
	public String getRCostInvoiceGnaDocId(Long orgId, String finYear, String branch, String branchCode) {
		String ScreenCode = "RCI";
		String result = rCostInvoiceGnaRepo.getRCostInvoiceGnaDocId(orgId, finYear, branchCode, ScreenCode);
		return result;
	}

	@Override
	public List<PartyMasterVO> getAllVendorFromPartyMaster(Long orgId, String partyType) {

		return rCostInvoiceGnaRepo.getAllVendorFromPartyMaster(orgId, partyType);
	}

	@Override
	public List<Map<String, Object>> getChargeLedgerFromGroup(Long orgId) {
		Set<Object[]> chCode = rCostInvoiceGnaRepo.getChargeLedgerFromGroup(orgId);
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
	public List<Map<String, Object>> getCurrencyAndExrates(Long orgId) {
		Set<Object[]> currency = rCostInvoiceGnaRepo.getCurrencyAndExrateDetails(orgId);
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
			map.put("id", ch[4] != null ? ch[4].toString() : "");

			List1.add(map);
		}
		return List1;
	}

	@Override
	public List<Map<String, Object>> getSectionNameFromTDSMaster(Long orgId, String section) {
		Set<Object[]> sectionName = rCostInvoiceGnaRepo.getSectionNameFromTDSMaster(orgId, section);
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
	public List<Map<String, Object>> getGstTypeDetails(Long orgId, String branchCode, String stateCode) {
		Set<Object[]> getGSTTypeDetails = rCostInvoiceGnaRepo.getGstType(orgId, branchCode, stateCode);
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
	public List<Map<String, Object>> getStateFromPartyMaster(Long orgId, String partyCode) {
		Set<Object[]> stateDetails = rCostInvoiceGnaRepo.getStatedetailsFromPartyMaster(orgId, partyCode);
		return getStateDetails(stateDetails);
	}

	private List<Map<String, Object>> getStateDetails(Set<Object[]> stateDetails) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : stateDetails) {
			Map<String, Object> map = new HashMap<>();
			map.put("stateCode", ch[0] != null ? ch[0].toString() : ""); // Empty string if null
			map.put("state", ch[1] != null ? ch[1].toString() : "");
			map.put("gstin", ch[2] != null ? ch[2].toString() : "");
//			map.put("city", ch[3] != null ? ch[3].toString() : "");
//			map.put("address", ch[4] != null ? ch[4].toString() : "");
			map.put("id", ch[3] != null ? ch[3].toString() : "");

			List1.add(map);
		}
		return List1;
	}

	@Override
	public Map<String, Object> updateCreateRCostInvoiceGna(RCostInvoiceGnaDTO rCostInvoiceGnaDTO)
			throws ApplicationException {
		String screenCode = "RCI";
		String accountsScreenCode = "AC";
		RCostInvoiceGnaVO rCostInvoiceGnaVO = new RCostInvoiceGnaVO();
//		AccountsVO accountsVO =new AccountsVO();

		String message;
		if (ObjectUtils.isNotEmpty(rCostInvoiceGnaDTO.getId())) {
			rCostInvoiceGnaVO = rCostInvoiceGnaRepo.findById(rCostInvoiceGnaDTO.getId())
					.orElseThrow(() -> new ApplicationException("R CostInvoice GNA not found"));

			rCostInvoiceGnaVO.setUpdatedBy(rCostInvoiceGnaDTO.getCreatedBy());
			createUpdateRCostInvoiceGnaVOByRCostInvoiceGnaDTO(rCostInvoiceGnaDTO, rCostInvoiceGnaVO);
			message = "R CostInvoice GNA Updated Successfully";
		} else {
			// GETDOCID API
			String docId = rCostInvoiceGnaRepo.getRCostInvoiceGnaDocId(rCostInvoiceGnaDTO.getOrgId(),
					rCostInvoiceGnaDTO.getFinYear(), rCostInvoiceGnaDTO.getBranchCode(), screenCode);
			rCostInvoiceGnaVO.setDocId(docId);

			// GETDOCID LASTNO +1
			DocumentTypeMappingDetailsVO documentTypeMappingDetailsVO = documentTypeMappingDetailsRepo
					.findByOrgIdAndFinYearAndBranchCodeAndScreenCode(rCostInvoiceGnaDTO.getOrgId(),
							rCostInvoiceGnaDTO.getFinYear(), rCostInvoiceGnaDTO.getBranchCode(), screenCode);
			documentTypeMappingDetailsVO.setLastno(documentTypeMappingDetailsVO.getLastno() + 1);
			documentTypeMappingDetailsRepo.save(documentTypeMappingDetailsVO);

			rCostInvoiceGnaVO.setCreatedBy(rCostInvoiceGnaDTO.getCreatedBy());
			rCostInvoiceGnaVO.setUpdatedBy(rCostInvoiceGnaDTO.getCreatedBy());
			createUpdateRCostInvoiceGnaVOByRCostInvoiceGnaDTO(rCostInvoiceGnaDTO, rCostInvoiceGnaVO);
			message = "R CostInvoice GNA Created Successfully";
		}

		rCostInvoiceGnaRepo.save(rCostInvoiceGnaVO);
		Map<String, Object> response = new HashMap<>();
		response.put("rCostInvoiceGnaVO", rCostInvoiceGnaVO);
		response.put("message", message);
		return response;
	}

	private RCostInvoiceGnaVO createUpdateRCostInvoiceGnaVOByRCostInvoiceGnaDTO(RCostInvoiceGnaDTO rCostInvoiceGnaDTO,
			@Valid RCostInvoiceGnaVO rCostInvoiceGnaVO) {

		rCostInvoiceGnaVO.setPartyType(rCostInvoiceGnaDTO.getPartyType());
		rCostInvoiceGnaVO.setPartyCode(rCostInvoiceGnaDTO.getPartyCode());
		rCostInvoiceGnaVO.setSupplierBillNo(rCostInvoiceGnaDTO.getSupplierBillNo());
		rCostInvoiceGnaVO.setSupplierBillDate(rCostInvoiceGnaDTO.getSupplierBillDate());
		rCostInvoiceGnaVO.setPartyName(rCostInvoiceGnaDTO.getPartyName());
		rCostInvoiceGnaVO.setCreditDays(rCostInvoiceGnaDTO.getCreditDays());
		rCostInvoiceGnaVO.setDueDate(rCostInvoiceGnaDTO.getDueDate());
		rCostInvoiceGnaVO.setSupplierGstIn(rCostInvoiceGnaDTO.getSupplierGstIn());
		rCostInvoiceGnaVO.setSupplierGstInCode(rCostInvoiceGnaDTO.getSupplierGstInCode());
		rCostInvoiceGnaVO.setCurrency(rCostInvoiceGnaDTO.getCurrency());
		rCostInvoiceGnaVO.setExRate(rCostInvoiceGnaDTO.getExRate());
		rCostInvoiceGnaVO.setPlace(rCostInvoiceGnaDTO.getPlace());
		rCostInvoiceGnaVO.setAddress(rCostInvoiceGnaDTO.getAddress());
		rCostInvoiceGnaVO.setRemarks(rCostInvoiceGnaDTO.getRemarks());
		rCostInvoiceGnaVO.setGstType(rCostInvoiceGnaDTO.getGstType());
		rCostInvoiceGnaVO.setVId(rCostInvoiceGnaDTO.getVId());
		rCostInvoiceGnaVO.setVDate(rCostInvoiceGnaDTO.getVDate());
		rCostInvoiceGnaVO.setMode(rCostInvoiceGnaDTO.getMode());
		rCostInvoiceGnaVO.setAddressType(rCostInvoiceGnaDTO.getAddressType());
		rCostInvoiceGnaVO.setState(rCostInvoiceGnaDTO.getState());

		// Default fields
		rCostInvoiceGnaVO.setOrgId(rCostInvoiceGnaDTO.getOrgId());
		rCostInvoiceGnaVO.setActive(rCostInvoiceGnaDTO.isActive());
		rCostInvoiceGnaVO.setCreatedBy(rCostInvoiceGnaDTO.getCreatedBy());
		rCostInvoiceGnaVO.setBranch(rCostInvoiceGnaDTO.getBranch());
		rCostInvoiceGnaVO.setBranchCode(rCostInvoiceGnaDTO.getBranchCode());
		rCostInvoiceGnaVO.setFinYear(rCostInvoiceGnaDTO.getFinYear());

		if (rCostInvoiceGnaDTO.getId() != null) {

			List<ChargeRCostInvoiceGnaVO> chargeRCostInvoiceGnaVOs = chargeRCostInvoiceGnaRepo
					.findByrCostInvoiceGnaVO(rCostInvoiceGnaVO);
			chargeRCostInvoiceGnaRepo.deleteAll(chargeRCostInvoiceGnaVOs);

			List<TdsRCostInvoiceGnaVO> tdsRCostInvoiceGnaVOs = tdsRCostInvoiceGnaRepo
					.findByrCostInvoiceGnaVO(rCostInvoiceGnaVO);
			tdsRCostInvoiceGnaRepo.deleteAll(tdsRCostInvoiceGnaVOs);

		}

		BigDecimal sumOfLcAmount = BigDecimal.ZERO;
		BigDecimal sumOfBillAmount = BigDecimal.ZERO;
		BigDecimal gstAmt = BigDecimal.ZERO;
		BigDecimal totalGstAmt = BigDecimal.ZERO;
		BigDecimal rate = BigDecimal.ZERO;
		BigDecimal exrate = BigDecimal.ZERO;
		BigDecimal sumOfRate = BigDecimal.ZERO;
		BigDecimal lcAmt = BigDecimal.ZERO;
		BigDecimal gstAmount1 = BigDecimal.ZERO;
		BigDecimal gstAmount2 = BigDecimal.ZERO;
		BigDecimal gstPer = BigDecimal.ZERO;
		BigDecimal gtaAmount = BigDecimal.ZERO;

		String Currency = "";

		List<ChargeRCostInvoiceGnaVO> chargeRCostInvoiceGnaVOs = new ArrayList<>();

		Map<String, BigDecimal> igstCategorySumMap = new HashMap<>();
		Map<String, BigDecimal> cgstCategorySumMap = new HashMap<>();

		for (ChargeRCostInvoiceGnaDTO chargeRCostInvoiceGnaDTO : rCostInvoiceGnaDTO.getChargeRCostInvoiceGnaDTO()) {
			ChargeRCostInvoiceGnaVO chargeRCostInvoiceGnaVO = new ChargeRCostInvoiceGnaVO();
			chargeRCostInvoiceGnaVO.setChargeName(chargeRCostInvoiceGnaDTO.getChargeName());
			chargeRCostInvoiceGnaVO.setTdsApplicable(chargeRCostInvoiceGnaDTO.isTdsApplicable());
			chargeRCostInvoiceGnaVO.setCurrency(chargeRCostInvoiceGnaDTO.getCurrency());
			Currency = chargeRCostInvoiceGnaDTO.getCurrency();
			chargeRCostInvoiceGnaVO.setExRate(chargeRCostInvoiceGnaDTO.getExRate());
			chargeRCostInvoiceGnaVO.setRate(chargeRCostInvoiceGnaDTO.getRate());
			chargeRCostInvoiceGnaVO.setGstPer(chargeRCostInvoiceGnaDTO.getGstPer());
			gstPer = BigDecimal.valueOf(chargeRCostInvoiceGnaDTO.getGstPer());
			chargeRCostInvoiceGnaVO.setGtaAmount(chargeRCostInvoiceGnaDTO.getGtaAmount());
			gtaAmount=gtaAmount.add(chargeRCostInvoiceGnaDTO.getGtaAmount());			

			BigDecimal fcAmount;
			BigDecimal billAmount;
			rate = chargeRCostInvoiceGnaDTO.getRate();
			sumOfRate = sumOfRate.add(rate);
			if (!chargeRCostInvoiceGnaDTO.getCurrency().equals("INR")) {
				fcAmount = rate;
				billAmount= rate;
				

			} else {
				fcAmount = BigDecimal.valueOf(0.00);
				
				billAmount = chargeRCostInvoiceGnaDTO.getExRate().multiply(chargeRCostInvoiceGnaDTO.getRate());
			chargeRCostInvoiceGnaVO.setBillAmt(billAmount);	

			}
			exrate = chargeRCostInvoiceGnaDTO.getExRate();

			lcAmt = chargeRCostInvoiceGnaDTO.getExRate().multiply(chargeRCostInvoiceGnaDTO.getRate());

			gstAmt = lcAmt.multiply(BigDecimal.valueOf(chargeRCostInvoiceGnaDTO.getGstPer()))
					.divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
			chargeRCostInvoiceGnaVO.setGstAmt(gstAmt);
			gstAmount1=billAmount.multiply(BigDecimal.valueOf(chargeRCostInvoiceGnaDTO.getGstPer()))
					.divide(BigDecimal.valueOf(100));
			
			System.out.println(gstAmount1);
			totalGstAmt = totalGstAmt.add(gstAmt);
			gstAmount2=gstAmount2.add(gstAmount1);
			chargeRCostInvoiceGnaVO.setFcAmt(fcAmount);
			chargeRCostInvoiceGnaVO.setLcAmt(lcAmt);
////			billAmt = chargeRCostInvoiceGnaDTO.getExRate().multiply(chargeRCostInvoiceGnaDTO.getRate());
			chargeRCostInvoiceGnaVO.setBillAmt(billAmount);

			sumOfLcAmount = sumOfLcAmount.add(lcAmt);
			sumOfBillAmount = sumOfBillAmount.add(billAmount);

//			AGGREGATE IGST SUMS BY GST PERCENTAGE
			if (rCostInvoiceGnaDTO.getGstType().equalsIgnoreCase("INTER") && gstPer.compareTo(BigDecimal.ZERO) > 0) {
				String igstCategoryKey = gstPer.toString();
				igstCategorySumMap.put(igstCategoryKey,
						igstCategorySumMap.getOrDefault(igstCategoryKey, BigDecimal.ZERO).add(gstAmt));
			}
			if (rCostInvoiceGnaDTO.getGstType().equalsIgnoreCase("INTRA") && gstPer.compareTo(BigDecimal.ZERO) > 0) {
				String gstCategoryKey = gstPer.toString();
				cgstCategorySumMap.put(gstCategoryKey,
						cgstCategorySumMap.getOrDefault(gstCategoryKey, BigDecimal.ZERO).add(gstAmt));
			}

			chargeRCostInvoiceGnaVO.setRCostInvoiceGnaVO(rCostInvoiceGnaVO);
			chargeRCostInvoiceGnaVOs.add(chargeRCostInvoiceGnaVO);
		}

//		ADD IGST ROWS FOR EACH IGST PERCENTAGE IN igstCategorySumMap
		if ("INTER".equalsIgnoreCase(rCostInvoiceGnaDTO.getGstType())) {
			for (Map.Entry<String, BigDecimal> entry : igstCategorySumMap.entrySet()) {
				ChargeRCostInvoiceGnaVO igstSummaryVO = new ChargeRCostInvoiceGnaVO();

				String gstType = "INTER";
				Double gstPercent = Double.parseDouble(entry.getKey());
				BigDecimal igstLcAmount = entry.getValue();

				Set<Object[]> groupLedgerVOs = rCostInvoiceGnaRepo
						.findInterDetailsForrCostInvoiceGnaPosting(rCostInvoiceGnaDTO.getOrgId(), gstType, gstPercent);
				for (Object[] ch : groupLedgerVOs) {
					String chargeDesc = ch[0].toString();
					float gstPerc = (float) Double.parseDouble(ch[2].toString());
//					String currency = ch[1].toString();
					igstSummaryVO.setChargeName(chargeDesc);
					igstSummaryVO.setCurrency(Currency);
					igstSummaryVO.setExRate(exrate);
					igstSummaryVO.setTdsApplicable(true);
//					igstSummaryVO.setGstPer(gstPerc);
//					igstSummaryVO.setRate(sumOfRate);
//
//					// Foreign currency handling
//					if (Currency.equals("INR")) {
//						igstSummaryVO.setFcAmt(BigDecimal.ZERO);
//					} else {
//						igstSummaryVO.setFcAmt(sumOfRate);
//					}
//					igstSummaryVO.setLcAmt(igstLcAmount);
//					igstSummaryVO.setBillAmt(igstLcAmount);
//					igstSummaryVO.setGstAmt(gstAmt);

					igstSummaryVO.setRate(BigDecimal.ZERO);
					igstSummaryVO.setExRate(BigDecimal.ZERO);
					igstSummaryVO.setFcAmt(BigDecimal.ZERO);
					igstSummaryVO.setLcAmt(igstLcAmount);
					igstSummaryVO.setFcAmt(BigDecimal.ZERO);
					igstSummaryVO.setBillAmt(BigDecimal.ZERO);
					igstSummaryVO.setGstAmt(BigDecimal.ZERO);
					igstSummaryVO.setRCostInvoiceGnaVO(rCostInvoiceGnaVO);
					chargeRCostInvoiceGnaVOs.add(igstSummaryVO);
				}
			}
		}

//		ADD CGST and SGST ROWS FOR EACH GST PERCENTAGE IN cgstCategorySumMap
		if ("INTRA".equalsIgnoreCase(rCostInvoiceGnaDTO.getGstType())) {
			for (Map.Entry<String, BigDecimal> entry : cgstCategorySumMap.entrySet()) {
				String gstType = "INTRA";
				Double gstPercent = Double.parseDouble(entry.getKey()) / 2;
				BigDecimal totalTaxAmount = entry.getValue();

				BigDecimal cgstAmount = totalTaxAmount.divide(BigDecimal.valueOf(2));
//				if (igstSummaryVO.getGSTPercent() != 0) {
//				totalGstAmt = totalGstAmt.add(cgstAmount);

				BigDecimal sgstAmount = totalTaxAmount.divide(BigDecimal.valueOf(2));
//				if (igstSummaryVO.getGSTPercent() != 0) {
//				totalGstAmt = totalGstAmt.add(sgstAmount);

				Set<Object[]> groupLedgerVOs = rCostInvoiceGnaRepo
						.findIntraDetailsForrCostInvoiceGnaPosting(rCostInvoiceGnaDTO.getOrgId(), gstType, gstPercent);

				for (Object[] entry1 : groupLedgerVOs) {
					ChargeRCostInvoiceGnaVO cgstSummaryVO = new ChargeRCostInvoiceGnaVO();
					cgstSummaryVO.setChargeName(entry1[0].toString());
					cgstSummaryVO.setGstPer(new BigDecimal(entry1[2].toString()).floatValue());
					cgstSummaryVO.setCurrency(Currency);
					cgstSummaryVO.setTdsApplicable(true);
//					cgstSummaryVO.setExRate(exrate);
//					cgstSummaryVO.setRate(sumOfRate);
//
//					// Foreign currency handling
//					if (Currency.equals("INR")) {
//						cgstSummaryVO.setFcAmt(BigDecimal.ZERO);
//					} else {
//						cgstSummaryVO.setFcAmt(sumOfRate);
//					}
//					cgstSummaryVO.setLcAmt(cgstAmount);
//					cgstSummaryVO.setBillAmt(cgstAmount);
//					cgstSummaryVO.setGstAmt(gstAmt);

					cgstSummaryVO.setRate(BigDecimal.ZERO);
					cgstSummaryVO.setExRate(BigDecimal.ZERO);
					cgstSummaryVO.setFcAmt(BigDecimal.ZERO);
					cgstSummaryVO.setLcAmt(cgstAmount);
					cgstSummaryVO.setBillAmt(BigDecimal.ZERO);
					cgstSummaryVO.setGstAmt(BigDecimal.ZERO);

					cgstSummaryVO.setRCostInvoiceGnaVO(rCostInvoiceGnaVO);
					chargeRCostInvoiceGnaVOs.add(cgstSummaryVO);
				}
			}
		}

		rCostInvoiceGnaVO.setChargeRCostInvoiceGnaVO(chargeRCostInvoiceGnaVOs);

//		rCostInvoiceGnaVO.setChargeRCostInvoiceGnaVO(chargeRCostInvoiceGnaVOs);

		BigDecimal tdsAmount = BigDecimal.ZERO;
		BigDecimal totaltdsAmount = BigDecimal.ZERO;
		List<TdsRCostInvoiceGnaVO> tdsRCostInvoiceGnaVOs = new ArrayList<>();
		for (TdsRCostInvoiceGnaDTO tdsRCostInvoiceGnaDTO : rCostInvoiceGnaDTO.getTdsRCostInvoiceGnaDTO()) {
			TdsRCostInvoiceGnaVO tdsRCostInvoiceGnaVO = new TdsRCostInvoiceGnaVO();
			tdsRCostInvoiceGnaVO.setTds(tdsRCostInvoiceGnaDTO.getTds());
			tdsRCostInvoiceGnaVO.setTdsPer(tdsRCostInvoiceGnaDTO.getTdsPer());
			tdsRCostInvoiceGnaVO.setSection(tdsRCostInvoiceGnaDTO.getSection());

			BigDecimal totTdsAmt = BigDecimal.ZERO;
			BigDecimal tdsPercent = tdsRCostInvoiceGnaDTO.getTdsPer();
			totTdsAmt = sumOfLcAmount.multiply(tdsPercent.divide(BigDecimal.valueOf(100)));
			tdsRCostInvoiceGnaVO.setTdsPerAmt(totTdsAmt);
			tdsRCostInvoiceGnaVO.setTotalTdsAmt(totTdsAmt);
			tdsAmount = totTdsAmt;

			tdsRCostInvoiceGnaVO.setRCostInvoiceGnaVO(rCostInvoiceGnaVO);
			tdsRCostInvoiceGnaVOs.add(tdsRCostInvoiceGnaVO);
		}
		rCostInvoiceGnaVO.setTdsRCostInvoiceGnaVO(tdsRCostInvoiceGnaVOs);
		totaltdsAmount = totaltdsAmount.add(tdsAmount);

		// Determine the net amount in the currency of the bill (netAmtBillCurr)
		BigDecimal netAmtBillCurr;
		if ("INR".equalsIgnoreCase(Currency)) {
		    netAmtBillCurr = sumOfLcAmount.subtract(totaltdsAmount);  
		    rCostInvoiceGnaVO.setActBillAmtLc(netAmtBillCurr);  
//		    rCostInvoiceGnaVO.setActBillAmtBc(sumOfBillAmount.subtract(totaltdsAmount));
		} else {
		    netAmtBillCurr = sumOfLcAmount.subtract(totaltdsAmount); 
			rCostInvoiceGnaVO.setActBillAmtLc(sumOfLcAmount);  
		
		}

		// Compute net and actual bill amounts in Local Currency (LC)
		BigDecimal netAmtBillLc = netAmtBillCurr.add(totalGstAmt);
		BigDecimal actBillAmtLc = netAmtBillLc.add(totaltdsAmount);  // Re-adding TDS to get actual LC value

		// Set values in VO
//		rCostInvoiceGnaVO.setActBillAmtBc(sumOfBillAmount);  // Actual bill in bill currency
//		rCostInvoiceGnaVO.setActBillAmtLc(sumOfLcAmount);     // Actual bill in local currency
		rCostInvoiceGnaVO.setNetAmtBc(sumOfBillAmount.add(gstAmount2));  
		rCostInvoiceGnaVO.setActBillAmtBc(sumOfBillAmount);
		BigDecimal unroundedNetAmtLc = netAmtBillLc.add(gtaAmount);

		BigDecimal netAmtLc = unroundedNetAmtLc.setScale(0, RoundingMode.HALF_UP);

		BigDecimal roundOff =unroundedNetAmtLc.subtract(netAmtLc).abs().setScale(2, RoundingMode.HALF_UP);

		rCostInvoiceGnaVO.setNetAmtLc(netAmtLc);
		rCostInvoiceGnaVO.setRoundOff(roundOff);

		// Set GST, TDS, sum amounts
		rCostInvoiceGnaVO.setGstAmtLc(totalGstAmt);          
		rCostInvoiceGnaVO.setTotalTdsAmt(totaltdsAmount);    
		rCostInvoiceGnaVO.setSumLcAmt(sumOfLcAmount);        
		rCostInvoiceGnaVO.setSumBillAmt(sumOfBillAmount);    

		rCostInvoiceGnaVO.setAmountInWords(
		    amountInWordsConverterService.convert(netAmtLc)
		);


		return rCostInvoiceGnaVO;


	}

	@Override
	public List<Map<String, Object>> getCityFromPartyMaster(Long orgId, String partyCode, String state,
			String addressType) {
		Set<Object[]> stateDetails = rCostInvoiceGnaRepo.getCitydetailsFromPartyMaster(orgId, partyCode, state,
				addressType);
		return getCityDetails(stateDetails);

	}

	private List<Map<String, Object>> getCityDetails(Set<Object[]> stateDetails) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : stateDetails) {
			Map<String, Object> map = new HashMap<>();
			map.put("city", ch[0] != null ? ch[0].toString() : "");
			map.put("address", ch[1] != null ? ch[1].toString() : "");
			List1.add(map);
		}
		return List1;
	}

	@Override
	public RCostInvoiceGnaVO approveRCostInvoiceGna(Long orgId, Long id, String docId, String action, String actionBy)
			throws ApplicationException {
		// Fetch the CostInvoice details from Cost Invoice
		RCostInvoiceGnaVO rCostInvoiceGnaVO = rCostInvoiceGnaRepo.findByOrgIdAndIdAndDocId(orgId, id, docId);
		String screenCode = "AC";
		String sourceScreenCode = rCostInvoiceGnaVO.getScreenCode();

		// Validate the approval status of the invoice
		if (rCostInvoiceGnaVO.getApproveStatus() == null
				|| (!rCostInvoiceGnaVO.getApproveStatus().equalsIgnoreCase("Approved")
						&& !rCostInvoiceGnaVO.getApproveStatus().equalsIgnoreCase("Rejected"))) {

			String accountsDocId = accountsRepo.getRCostInvoiceGnaDocId(rCostInvoiceGnaVO.getOrgId(),
					rCostInvoiceGnaVO.getFinYear(), rCostInvoiceGnaVO.getBranchCode(), sourceScreenCode, screenCode);
			rCostInvoiceGnaVO.setDocId(docId);

			// GETDOCID LASTNO +1
			MultipleDocIdGenerationDetailsVO mulDocId = multipleDocIdGenerationDetailsRepo
					.findByOrgIdAndFinYearAndBranchCodeAndSourceScreenCodeAndScreenCode(rCostInvoiceGnaVO.getOrgId(),
							rCostInvoiceGnaVO.getFinYear(), rCostInvoiceGnaVO.getBranchCode(), sourceScreenCode,
							screenCode);
			mulDocId.setLastno(mulDocId.getLastno() + 1);
			multipleDocIdGenerationDetailsRepo.save(mulDocId);

			AccountsVO accountsVO = new AccountsVO();
			accountsVO.setDocId(accountsDocId);
			accountsVO.setSourceId(rCostInvoiceGnaVO.getId());
			accountsVO.setCreatedBy(rCostInvoiceGnaVO.getCreatedBy());
			if (rCostInvoiceGnaVO.getCommonDate() != null
					&& rCostInvoiceGnaVO.getCommonDate().getModifiedon() != null) {
				accountsVO.setModifiedon(rCostInvoiceGnaVO.getCommonDate().getModifiedon().toUpperCase());
				accountsVO.setCreatedon(rCostInvoiceGnaVO.getCommonDate().getModifiedon().toUpperCase());
			} else {
				accountsVO.setModifiedon(null); // or set a default value
				accountsVO.setCreatedon(null); // or set a default value
			}
			accountsVO.setCancelRemarks(rCostInvoiceGnaVO.getCancelRemarks());
			accountsVO.setFinYear(rCostInvoiceGnaVO.getFinYear());
			accountsVO.setBranch(rCostInvoiceGnaVO.getBranch());
			accountsVO.setBranchCode(rCostInvoiceGnaVO.getBranchCode());
			accountsVO.setRefNo(rCostInvoiceGnaVO.getDocId());
			accountsVO.setRefDate(rCostInvoiceGnaVO.getDocDate());
			accountsVO.setCurrency(rCostInvoiceGnaVO.getCurrency());
			accountsVO.setExRate(rCostInvoiceGnaVO.getExRate());

			// Calculate total debit/credit amounts
//			BigDecimal totalDebitAmount = rCostInvoiceGnaVO.getTotChargesLcAmt().add(costInvoiceVO.getGstInputLcAmt());// tax
			accountsVO.setTotalDebitAmount(rCostInvoiceGnaVO.getNetAmtLc());
			accountsVO.setTotalCreditAmount(rCostInvoiceGnaVO.getNetAmtLc());
			accountsVO.setDueDate(rCostInvoiceGnaVO.getDueDate());
			accountsVO.setSupplierRefNo(rCostInvoiceGnaVO.getSupplierBillNo());
			accountsVO.setCreditDays(rCostInvoiceGnaVO.getCreditDays());
			accountsVO.setSourceScreen(rCostInvoiceGnaVO.getScreenName());
			accountsVO.setSourceScreenCode(rCostInvoiceGnaVO.getScreenCode());
			accountsVO.setModifiedBy(rCostInvoiceGnaVO.getUpdatedBy());
			accountsVO.setOrgId(rCostInvoiceGnaVO.getOrgId());
			accountsVO.setRemarks(rCostInvoiceGnaVO.getRemarks());
			accountsVO.setChargeableAmount(rCostInvoiceGnaVO.getSumLcAmt());

			accountsVO.setAmountInWords(rCostInvoiceGnaVO.getAmountInWords());
//			accountsVO.setStTaxAmount(costInvoiceVO.getTotalTaxableAmountLc());
//			accountsVO.setSalesType(costInvoiceVO.getSalesType());
			//
			List<AccountsDetailsVO> accountsDetailsVOs = new ArrayList<>();
			AccountsDetailsVO accountsDetailsVO = new AccountsDetailsVO();
			accountsDetailsVO.setNDebitAmount(BigDecimal.ZERO);
			accountsDetailsVO.setACategory("PAYABLE A/C");
			accountsDetailsVO.setAccountName("PAYABLE A/C");
			accountsDetailsVO.setDebitAmount(BigDecimal.ZERO);
			accountsDetailsVO.setNCreditAmount(rCostInvoiceGnaVO.getNetAmtLc());
			accountsDetailsVO.setCreditAmount(rCostInvoiceGnaVO.getNetAmtLc());
			accountsDetailsVO.setArapFlag(true);
			accountsDetailsVO.setArapAmount(rCostInvoiceGnaVO.getNetAmtLc());
			accountsDetailsVO.setBDebitAmount(BigDecimal.ZERO);
			accountsDetailsVO.setBCrAmount(BigDecimal.ZERO);
			accountsDetailsVO.setBArapAmount(BigDecimal.ZERO);
			accountsDetailsVO.setACurrency(rCostInvoiceGnaVO.getCurrency());
			accountsDetailsVO.setAExRate(rCostInvoiceGnaVO.getExRate());
			accountsDetailsVO.setSubledgerName(rCostInvoiceGnaVO.getPartyName());
			accountsDetailsVO.setSubLedgerCode(rCostInvoiceGnaVO.getPartyName());
			accountsDetailsVO.setNArapAmount(BigDecimal.ZERO);
			accountsDetailsVO.setGstflag(6);
			accountsDetailsVO.setTdsAmount(rCostInvoiceGnaVO.getTotalTdsAmt());
			accountsDetailsVO.setAccountsVO(accountsVO);
			accountsDetailsVOs.add(accountsDetailsVO);

			for (TdsRCostInvoiceGnaVO tdsRCostInvoiceGnaVO : rCostInvoiceGnaVO.getTdsRCostInvoiceGnaVO()) {
		        Set<Object[]> tdsLedgers = costInvoiceRepo.getTdsLedgerFromAccount(rCostInvoiceGnaVO.getOrgId());

		        for (Object[] ledger : tdsLedgers) {
				AccountsDetailsVO accountsDetailsVO1 = new AccountsDetailsVO();
				accountsDetailsVO1.setNDebitAmount(BigDecimal.ZERO);
				accountsDetailsVO1.setAccountName(ledger[0].toString());
				accountsDetailsVO1.setACategory(ledger[1].toString());
				accountsDetailsVO1.setDebitAmount(BigDecimal.ZERO);
				accountsDetailsVO1.setACurrency(rCostInvoiceGnaVO.getCurrency());
				accountsDetailsVO1.setAExRate(rCostInvoiceGnaVO.getExRate());
				accountsDetailsVO1.setNCreditAmount(tdsRCostInvoiceGnaVO.getTotalTdsAmt());
				accountsDetailsVO1.setCreditAmount(tdsRCostInvoiceGnaVO.getTotalTdsAmt());
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

			}

			// Group and process GST-related ledgers
			Map<String, BigDecimal> ledgerSumMap = new HashMap<>();
			for (ChargeRCostInvoiceGnaVO gstVO : rCostInvoiceGnaVO.getChargeRCostInvoiceGnaVO()) {
				String ledger = gstVO.getChargeName();
				BigDecimal lcAmount = gstVO.getLcAmt();

				ledgerSumMap.put(ledger, ledgerSumMap.getOrDefault(ledger, BigDecimal.ZERO).add(lcAmount));
			}

			// Add GST ledger entries
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
				gstAccountDetailsVO.setACurrency(rCostInvoiceGnaVO.getCurrency());
				gstAccountDetailsVO.setAExRate(rCostInvoiceGnaVO.getExRate());
				gstAccountDetailsVO.setSubledgerName("None");
				gstAccountDetailsVO.setSubLedgerCode("None");
				gstAccountDetailsVO.setNArapAmount(BigDecimal.ZERO);
				gstAccountDetailsVO.setGstflag(3);
				gstAccountDetailsVO.setAccountsVO(accountsVO);
				accountsDetailsVOs.add(gstAccountDetailsVO);
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
			arapDetailsVO.setSubLedgerName(accountsDetailsVOs2.getSubledgerName());
			arapDetailsVO.setSalesType(savedAccountsVO.getSalesType());
			arapDetailsVO.setNativeAmt(accountsDetailsVOs2.getArapAmount());
			arapDetailsRepo.save(arapDetailsVO);
			rCostInvoiceGnaVO.setPurVoucherNo(savedAccountsVO.getDocId());
			rCostInvoiceGnaVO.setPurVoucherDate(savedAccountsVO.getDocDate());

			LocalDate vDate = rCostInvoiceGnaVO.getVDate()!=null?rCostInvoiceGnaVO.getVDate():rCostInvoiceGnaVO.getDocDate();
			int creditDays = rCostInvoiceGnaVO.getCreditDays();
			LocalDate dueDate = vDate.plusDays(creditDays);
			// Save dueDate in your entity
			savedAccountsVO.setDueDate(dueDate);
			rCostInvoiceGnaVO.setDueDate(dueDate);
			rCostInvoiceGnaVO.setApproveStatus(action);
			rCostInvoiceGnaVO.setApproveBy(actionBy);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
			rCostInvoiceGnaVO.setApproveOn(LocalDateTime.now().format(formatter).toUpperCase());

			return rCostInvoiceGnaRepo.save(rCostInvoiceGnaVO);

		} else if (rCostInvoiceGnaVO.getApproveStatus().equals("Approved")) {
			throw new ApplicationException("This Invoice Already Approved,");
		} else {
			throw new ApplicationException("This Invoice Already Rejected");
		}
	}

	@Override
	public List<Map<String, Object>> findByAddressTypeFromPartyAddress(Long orgId, String state, String partyCode) {
		Set<Object[]> chCode = rCostInvoiceGnaRepo.findByAddressTypeFromPartyAddress(orgId, state, partyCode);
		return findByAddressType(chCode);
	}

	private List<Map<String, Object>> findByAddressType(Set<Object[]> chCode) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chCode) {
			Map<String, Object> map = new HashMap<>();
			map.put("addressType", ch[0] != null ? ch[0].toString() : "");
			List1.add(map);
		}
		return List1;

	}

	@Override
	public List<Map<String, Object>> getRegisterCostInvoiceReport(Long orgId, String branchCode,
			String fromDate, String toDate,String partyCode) {
		Set<Object[]> chCode = rCostInvoiceGnaRepo.findRegisterCostInvoiceReport(orgId, branchCode,  fromDate,
				toDate,partyCode);
		return findRegisterCostInvoice(chCode);
	}

	private List<Map<String, Object>> findRegisterCostInvoice(Set<Object[]> chCode) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chCode) {
			Map<String, Object> map = new HashMap<>();

			map.put("BranchCode", ch[0] != null ? ch[0].toString() : "");
			map.put("Vid", ch[1] != null ? ch[1].toString() : "");
			map.put("Vdate", ch[2] != null ? ch[2].toString() : "");
			map.put("SupplierName", ch[3] != null ? ch[3].toString() : "");
			map.put("SupplierGstin", ch[4] != null ? ch[4].toString() : "");
			map.put("GstType", ch[5] != null ?  ch[5].toString() : "");
			map.put("BillAmount", ch[6] != null ?  new BigDecimal(ch[6].toString()) : BigDecimal.ZERO);
			map.put("Tax", ch[7] != null ? new BigDecimal(ch[7].toString()) : BigDecimal.ZERO);
			map.put("TotalAmount", ch[8] != null ? new BigDecimal(ch[8].toString()) : BigDecimal.ZERO);
			map.put("Tds", ch[9] != null ? new BigDecimal(ch[9].toString()) : BigDecimal.ZERO);
			map.put("PartyPayable", ch[10] != null ? new BigDecimal(ch[10].toString()) : BigDecimal.ZERO);
			 map.put("OutputIgst", ch[11] != null ? new BigDecimal(ch[11].toString()) : BigDecimal.ZERO);
			 map.put("OutputCgst", ch[12] != null ? new BigDecimal(ch[12].toString()) : BigDecimal.ZERO);
			 map.put("OutputSgst", ch[13] != null ? new BigDecimal(ch[13].toString()) : BigDecimal.ZERO);
			 map.put("GstPercent", ch[14] != null ? new BigDecimal(ch[14].toString()) : BigDecimal.ZERO);
			 map.put("DocId", ch[15] != null ?  ch[15].toString() : "");
			 map.put("DocDate", ch[16] != null ?  ch[16].toString() : "");
			 map.put("ScreenCode", ch[17] != null ?  ch[17].toString() : "");
			List1.add(map);
		}
		return List1;

	}
}
