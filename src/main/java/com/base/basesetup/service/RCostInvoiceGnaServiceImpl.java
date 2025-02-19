package com.base.basesetup.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import com.base.basesetup.entity.ChargeRCostInvoiceGnaVO;
import com.base.basesetup.entity.DocumentTypeMappingDetailsVO;
import com.base.basesetup.entity.PartyMasterVO;
import com.base.basesetup.entity.RCostInvoiceGnaVO;
import com.base.basesetup.entity.TdsRCostInvoiceGnaVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.ChargeRCostInvoiceGnaRepo;
import com.base.basesetup.repo.DocumentTypeMappingDetailsRepo;
import com.base.basesetup.repo.RCostInvoiceGnaRepo;
import com.base.basesetup.repo.TdsRCostInvoiceGnaRepo;

@Service
public class RCostInvoiceGnaServiceImpl implements RCostInvoiceGnaService {

	public static final Logger LOGGER = LoggerFactory.getLogger(RCostInvoiceGnaServiceImpl.class);

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

	@Override
	public List<RCostInvoiceGnaVO> getAllRCostInvoiceGnaByOrgId(Long orgId) {
		List<RCostInvoiceGnaVO> rCostInvoiceGnaVO = new ArrayList<>();
		rCostInvoiceGnaVO = rCostInvoiceGnaRepo.getAllCostInvoiceByOrgId(orgId);
		return rCostInvoiceGnaVO;
	}
	
	@Override
	public RCostInvoiceGnaVO getAllRCostInvoiceGnaById(Long id) {
		RCostInvoiceGnaVO rCostInvoiceGnaVO = new RCostInvoiceGnaVO();

		rCostInvoiceGnaVO = rCostInvoiceGnaRepo.getAllRCostInvoiceGnaById(id);

		return rCostInvoiceGnaVO;
	}
	
	@Override
	public String getRCostInvoiceGnaDocId(Long orgId, String finYear, String branch, String branchCode) {
		String ScreenCode = "RCI";
		String result = rCostInvoiceGnaRepo.getRCostInvoiceGnaDocId(orgId, finYear, branchCode, ScreenCode);
		return result;
	}
	
	
	 @Override
		public Map<String, Object> updateCreateRCostInvoiceGna(RCostInvoiceGnaDTO rCostInvoiceGnaDTO)
				throws ApplicationException {
			String screenCode = "RCI";
			RCostInvoiceGnaVO rCostInvoiceGnaVO = new RCostInvoiceGnaVO();
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

				List<TdsRCostInvoiceGnaVO> tdsRCostInvoiceGnaVOs = tdsRCostInvoiceGnaRepo.findByrCostInvoiceGnaVO(rCostInvoiceGnaVO);
				tdsRCostInvoiceGnaRepo.deleteAll(tdsRCostInvoiceGnaVOs);

			}
		    
			BigDecimal sumOfLcAmount = BigDecimal.ZERO;
			BigDecimal sumOfBillAmount= BigDecimal.ZERO;
			BigDecimal gstAmt = BigDecimal.ZERO;
			BigDecimal totalGstAmt = BigDecimal.ZERO;
			
			String Currency ="";

		    List<ChargeRCostInvoiceGnaVO> chargeRCostInvoiceGnaVOs = new ArrayList<>();
			for (ChargeRCostInvoiceGnaDTO chargeRCostInvoiceGnaDTO : rCostInvoiceGnaDTO.getChargeRCostInvoiceGnaDTO()) {
				ChargeRCostInvoiceGnaVO chargeRCostInvoiceGnaVO = new ChargeRCostInvoiceGnaVO();
				chargeRCostInvoiceGnaVO.setChargeName(chargeRCostInvoiceGnaDTO.getChargeName());
				chargeRCostInvoiceGnaVO.setTdsApplicable(chargeRCostInvoiceGnaDTO.isTdsApplicable());
				chargeRCostInvoiceGnaVO.setCurrency(chargeRCostInvoiceGnaDTO.getCurrency());
				Currency =chargeRCostInvoiceGnaDTO.getCurrency();
				chargeRCostInvoiceGnaVO.setExRate(chargeRCostInvoiceGnaDTO.getExRate());
				chargeRCostInvoiceGnaVO.setRate(chargeRCostInvoiceGnaDTO.getRate());
				chargeRCostInvoiceGnaVO.setGstPer(chargeRCostInvoiceGnaDTO.getGstPer());
				chargeRCostInvoiceGnaVO.setGtaamount(chargeRCostInvoiceGnaDTO.getGtaamount());

				BigDecimal fcAmount;
				BigDecimal lcAmount;
				BigDecimal billAmount;

				if (!chargeRCostInvoiceGnaDTO.getCurrency().equals("INR")) {
					BigDecimal rate = chargeRCostInvoiceGnaDTO.getRate();

					fcAmount = rate;

				} else {
					fcAmount = BigDecimal.valueOf(0.00);
					
				}

				lcAmount = chargeRCostInvoiceGnaDTO.getExRate().multiply(chargeRCostInvoiceGnaDTO.getRate());
				
//				System.out.println(lcAmount);
//				System.out.println(chargeRCostInvoiceGnaDTO.getGstPer());

				 gstAmt = lcAmount.multiply(BigDecimal.valueOf(chargeRCostInvoiceGnaDTO.getGstPer()))
                        .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
				 
//					System.out.println(gstAmt);

				totalGstAmt = totalGstAmt.add(gstAmt);
//				System.out.println(totalGstAmt);

				chargeRCostInvoiceGnaVO.setFcAmt(fcAmount);
				chargeRCostInvoiceGnaVO.setLcAmt(lcAmount);
				billAmount = chargeRCostInvoiceGnaDTO.getExRate().multiply(chargeRCostInvoiceGnaDTO.getRate());
				chargeRCostInvoiceGnaVO.setBillAmt(billAmount);

				sumOfLcAmount = sumOfLcAmount.add(lcAmount);
				sumOfBillAmount = sumOfBillAmount.add(billAmount);
				
				chargeRCostInvoiceGnaVO.setRCostInvoiceGnaVO(rCostInvoiceGnaVO);
				chargeRCostInvoiceGnaVOs.add(chargeRCostInvoiceGnaVO);
			}
			rCostInvoiceGnaVO.setChargeRCostInvoiceGnaVO(chargeRCostInvoiceGnaVOs);
			
			BigDecimal tdsAmount = BigDecimal.ZERO;
			BigDecimal totaltdsAmount =BigDecimal.ZERO;
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

				
				BigDecimal actBillAmtBC = sumOfBillAmount;
				BigDecimal netAmtBillCurr = BigDecimal.ZERO;
				if(Currency.equals("INR")) {
					netAmtBillCurr = sumOfLcAmount.subtract(totaltdsAmount);
				}else {
					netAmtBillCurr = sumOfBillAmount;
				}
				
				BigDecimal netAmtBillLc = sumOfLcAmount.subtract(totaltdsAmount);
				
				BigDecimal actBillAmtLc =  sumOfBillAmount.subtract(totaltdsAmount);
				
				BigDecimal roundedValue = netAmtBillLc.setScale(0, RoundingMode.HALF_UP);
				BigDecimal roundOff = roundedValue.subtract(netAmtBillLc);
			    rCostInvoiceGnaVO.setActBillAmtBc(actBillAmtBC);
			    rCostInvoiceGnaVO.setActBillAmtLc(actBillAmtLc);
			    rCostInvoiceGnaVO.setNetAmtBc(netAmtBillCurr);
			    rCostInvoiceGnaVO.setNetAmtLc(netAmtBillLc);
			    rCostInvoiceGnaVO.setRoundOff(roundOff);
			    rCostInvoiceGnaVO.setGstAmtLc(sumOfLcAmount.subtract(totalGstAmt));	
			    rCostInvoiceGnaVO.setAmountInWords(
						amountInWordsConverterService.convert(rCostInvoiceGnaVO.getActBillAmtLc().longValue()));
			return rCostInvoiceGnaVO;
		    
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
		Set<Object[]> sectionName = rCostInvoiceGnaRepo.getSectionNameFromTDSMaster(orgId,section);
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
	public List<Map<String, Object>> getStateFromPartyMaster(Long orgId,String partyCode) {
		Set<Object[]> stateDetails = rCostInvoiceGnaRepo.getStatedetailsFromPartyMaster(orgId,partyCode);
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
			map.put("id", ch[5] != null ? ch[5].toString() : "");

			List1.add(map);
		}
		return List1;
	}


}
