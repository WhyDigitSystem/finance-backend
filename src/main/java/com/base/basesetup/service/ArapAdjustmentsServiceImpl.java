package com.base.basesetup.service;

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

import com.base.basesetup.controller.ArapAdjustmentsController;
import com.base.basesetup.dto.ArapAdjustmentsDTO;
import com.base.basesetup.entity.ArapAdjustmentsVO;
import com.base.basesetup.entity.DocumentTypeMappingDetailsVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.ArapAdjustmentsRepo;
import com.base.basesetup.repo.DocumentTypeMappingDetailsRepo;

@Service
public class ArapAdjustmentsServiceImpl implements ArapAdjustmentsService{

	public static final Logger LOGGER = LoggerFactory.getLogger(ArapAdjustmentsController.class);
	
	@Autowired
	ArapAdjustmentsRepo arapAdjustmentsRepo;
	
	@Autowired
	DocumentTypeMappingDetailsRepo documentTypeMappingDetailsRepo;
	
	
	
	@Override
	public List<ArapAdjustmentsVO> getAllArapAdjustmentsByOrgId(Long orgId) {
		List<ArapAdjustmentsVO> arapAdjustmentsVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(orgId)) {
			LOGGER.info("Successfully Received ArapAdjustments BY OrgId : {}", orgId);
			arapAdjustmentsVO = arapAdjustmentsRepo.getAllArapAdjustmentsByOrgId(orgId);
		} else {
			LOGGER.info("Successfully Received ArapAdjustments For All OrgId.");
			arapAdjustmentsVO = arapAdjustmentsRepo.findAll();
		}
		return arapAdjustmentsVO;
	}

	@Override
	public List<ArapAdjustmentsVO> getAllArapAdjustmentsById(Long id) {
		List<ArapAdjustmentsVO> arapAdjustmentsVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received ArapAdjustments BY Id : {}", id);
			arapAdjustmentsVO = arapAdjustmentsRepo.getAllArapAdjustmentsById(id);
		} else {
			LOGGER.info("Successfully Received ArapAdjustments For All Id.");
			arapAdjustmentsVO = arapAdjustmentsRepo.findAll();
		}
		return arapAdjustmentsVO;
	}

	

	@Override
	public List<ArapAdjustmentsVO> getArapAdjustmentsByActive() {
		return arapAdjustmentsRepo.findArapAdjustmentsByActive();
	}

	@Override
	public Map<String, Object> createUpdateArapAdjustments(@Valid ArapAdjustmentsDTO arapAdjustmentsDTO) throws ApplicationException {
		ArapAdjustmentsVO arapAdjustmentsVO;
		String message = null;
		String screenCode="AA";
		if (ObjectUtils.isEmpty(arapAdjustmentsDTO.getId())) {

			arapAdjustmentsVO = new ArapAdjustmentsVO();
			
			// GETDOCID API
						String docId = arapAdjustmentsRepo.getArapAdjustmentsDocId(arapAdjustmentsDTO.getOrgId(), arapAdjustmentsDTO.getFinYear(),
								arapAdjustmentsDTO.getBranchCode(), screenCode);
						arapAdjustmentsVO.setDocId(docId);

						// GETDOCID LASTNO +1
						DocumentTypeMappingDetailsVO documentTypeMappingDetailsVO = documentTypeMappingDetailsRepo
								.findByOrgIdAndFinYearAndBranchCodeAndScreenCode(arapAdjustmentsDTO.getOrgId(),
										arapAdjustmentsDTO.getFinYear(), arapAdjustmentsDTO.getBranchCode(), screenCode);
						documentTypeMappingDetailsVO.setLastno(documentTypeMappingDetailsVO.getLastno() + 1);
						documentTypeMappingDetailsRepo.save(documentTypeMappingDetailsVO);
			
			arapAdjustmentsVO.setCreatedBy(arapAdjustmentsDTO.getCreatedBy());
			arapAdjustmentsVO.setUpdatedBy(arapAdjustmentsDTO.getCreatedBy());

			message = "ArapAdjustments Creation Successfull";
		} else {

			arapAdjustmentsVO = arapAdjustmentsRepo.findById(arapAdjustmentsDTO.getId()).orElseThrow(
					() -> new ApplicationException("Cost Invoice Not Found with id: " + arapAdjustmentsDTO.getId()));

			arapAdjustmentsVO.setUpdatedBy(arapAdjustmentsDTO.getCreatedBy());
			message = "ArapAdjustments Updation Successfull";
		}
		
		arapAdjustmentsVO=getArapAdjustmentsVOFromArapAdjustmentsDTO(arapAdjustmentsVO,arapAdjustmentsDTO);
		arapAdjustmentsRepo.save(arapAdjustmentsVO);

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("arapAdjustmentsVO", arapAdjustmentsVO);
		return response;
		
	}

	private ArapAdjustmentsVO getArapAdjustmentsVOFromArapAdjustmentsDTO(ArapAdjustmentsVO arapAdjustmentsVO,
			@Valid ArapAdjustmentsDTO arapAdjustmentsDTO) {
		
		arapAdjustmentsVO.setBranch(arapAdjustmentsDTO.getBranch());
	    arapAdjustmentsVO.setFinYear(arapAdjustmentsDTO.getFinYear());
	    arapAdjustmentsVO.setSourceId(arapAdjustmentsDTO.getSourceId());
	    arapAdjustmentsVO.setRefNo(arapAdjustmentsDTO.getRefNo());
	    arapAdjustmentsVO.setAccountName(arapAdjustmentsDTO.getAccountName());
	    arapAdjustmentsVO.setCurrency(arapAdjustmentsDTO.getCurrency());
	    arapAdjustmentsVO.setAccCurrency(arapAdjustmentsDTO.getAccCurrency());
	    arapAdjustmentsVO.setBaseAmt(arapAdjustmentsDTO.getBaseAmt());
	    arapAdjustmentsVO.setNativeAmt(arapAdjustmentsDTO.getNativeAmt());
	    arapAdjustmentsVO.setOffDocId(arapAdjustmentsDTO.getOffDocId());
	    arapAdjustmentsVO.setVoucherType(arapAdjustmentsDTO.getVoucherType());
	    arapAdjustmentsVO.setRefDate(arapAdjustmentsDTO.getRefDate());
	    arapAdjustmentsVO.setSubLedgerCode(arapAdjustmentsDTO.getSubLedgerCode());
	    arapAdjustmentsVO.setExRate(arapAdjustmentsDTO.getExRate());
	    arapAdjustmentsVO.setCreditDays(arapAdjustmentsDTO.getCreditDays());
	    arapAdjustmentsVO.setDueDate(arapAdjustmentsDTO.getDueDate());
	    arapAdjustmentsVO.setOrgId(arapAdjustmentsDTO.getOrgId());
	    arapAdjustmentsVO.setCreatedBy(arapAdjustmentsDTO.getCreatedBy());
	    arapAdjustmentsVO.setActive(arapAdjustmentsDTO.isActive());
	    arapAdjustmentsVO.setBranchCode(arapAdjustmentsDTO.getBranchCode());
	    arapAdjustmentsVO.setIpNo(arapAdjustmentsDTO.getIpNo());
	    arapAdjustmentsVO.setLatitude(arapAdjustmentsDTO.getLatitude());
	    arapAdjustmentsVO.setTransId(arapAdjustmentsDTO.getTransId());
	    arapAdjustmentsVO.setChargeableAmt(arapAdjustmentsDTO.getChargeableAmt());
	    arapAdjustmentsVO.setTdsAmt(arapAdjustmentsDTO.getTdsAmt());
	    arapAdjustmentsVO.setSubLedgerName(arapAdjustmentsDTO.getSubLedgerName());
	    arapAdjustmentsVO.setGstFlag(arapAdjustmentsDTO.isGstFlag());
	    arapAdjustmentsVO.setAmount(arapAdjustmentsDTO.getAmount());
		
	    
        return arapAdjustmentsVO;
	}

	@Override
	public ArapAdjustmentsVO getArapAdjustmentsByDocId(Long orgId, String docId) {
		return arapAdjustmentsRepo.findArapAdjustmentsByDocId(orgId, docId);
	}

	@Override
	public String getArapAdjustmentsDocId(Long orgId, String finYear, String branch, String branchCode) {
		String ScreenCode = "AA";
		String result = arapAdjustmentsRepo.getArapAdjustmentsDocId(orgId, finYear, branchCode, ScreenCode);
		return result;
	}

	@Override
	public List<Map<String, Object>> GetArapAgeing(String asondate, String pdate, String partyname, Long orgId) {
		Set<Object[]> mapp = arapAdjustmentsRepo.findArapAgeing(asondate, pdate, partyname,orgId);
		return GetArapAgeing(mapp);
	}

	private List<Map<String, Object>> GetArapAgeing(Set<Object[]> mapp) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : mapp) {
			Map<String, Object> map = new HashMap<>();
			map.put("arapdetailsid", ch[0] != null ? ch[0].toString() : ""); // Empty string if null
			map.put("orgid", ch[1] != null ? ch[1].toString() : "");
			map.put("branch", ch[2] != null ? ch[2].toString() : "");
			map.put("subledgercode", ch[3] != null ? ch[3].toString() : "");
			map.put("partyname", ch[4] != null ? ch[4].toString() : "");
			map.put("subledgername", ch[5] != null ? ch[5].toString() : "");
			map.put("partytype", ch[6] != null ? ch[6].toString() : ""); // Empty string if null
			map.put("branchcode", ch[7] != null ? ch[7].toString() : "");
			map.put("subledgerdivision", ch[8] != null ? ch[8].toString() : "");
			map.put("currency", ch[9] != null ? ch[9].toString() : "");
			map.put("auser", ch[10] != null ? ch[10].toString() : "");
			map.put("docid", ch[11] != null ? ch[11].toString() : "");
			map.put("docdate", ch[12] != null ? ch[12].toString() : ""); // Empty string if null
			map.put("supprefno", ch[13] != null ? ch[13].toString() : "");
			map.put("duedate", ch[14] != null ? ch[14].toString() : "");
			map.put("refno", ch[15] != null ? ch[15].toString() : "");
			map.put("refdate", ch[16] != null ? ch[16].toString() : "");
			map.put("amount", ch[17] != null ? ch[17].toString() : "");
			map.put("outstanding", ch[18] != null ? ch[18].toString() : ""); // Empty string if null
			map.put("totaldue", ch[19] != null ? ch[19].toString() : "");
			map.put("unadjusted", ch[20] != null ? ch[20].toString() : "");
			map.put("ddays", ch[21] != null ? ch[21].toString() : "");
			map.put("mslab1", ch[22] != null ? ch[22].toString() : "");
			map.put("mslab2", ch[23] != null ? ch[23].toString() : "");
			map.put("mslab3", ch[24] != null ? ch[24].toString() : ""); // Empty string if null
			map.put("mslab4", ch[25] != null ? ch[25].toString() : "");
			map.put("mslab5", ch[26] != null ? ch[26].toString() : "");
			map.put("product", ch[27] != null ? ch[27].toString() : "");
			map.put("creditlimit", ch[28] != null ? ch[28].toString() : "");
			map.put("creditdays", ch[29] != null ? ch[29].toString() : "");
			map.put("doctypecode", ch[30] != null ? ch[30].toString() : "");
			
			List1.add(map);
		}
		return List1;
	}
	
	
	@Override
	public List<Map<String, Object>> GetArapAdjustments(String asondt,String partyName, String branch, Long orgId) {
		Set<Object[]> mapp = arapAdjustmentsRepo.findArapAdjustments(asondt,partyName,branch,orgId);
		return GetArapAdjustments(mapp);
	}

	private List<Map<String, Object>> GetArapAdjustments(Set<Object[]> mapp) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : mapp) {
			Map<String, Object> map = new HashMap<>();
			map.put("orgid", ch[0] != null ? ch[0].toString() : ""); // Empty string if null
			map.put("subledgercode", ch[1] != null ? ch[1].toString() : "");
			map.put("subledgername", ch[2] != null ? ch[2].toString() : "");
			map.put("partytype", ch[3] != null ? ch[3].toString() : "");
			map.put("branch", ch[4] != null ? ch[4].toString() : "");
			map.put("jobbranch", ch[5] != null ? ch[5].toString() : "");
			map.put("currency", ch[6] != null ? ch[6].toString() : ""); // Empty string if null
			map.put("creditdays", ch[7] != null ? ch[7].toString() : "");
			map.put("creditlimit", ch[8] != null ? ch[8].toString() : "");
			map.put("amount", ch[9] != null ? ch[9].toString() : "");
			map.put("outstanding", ch[10] != null ? ch[10].toString() : "");
			map.put("unadjusted", ch[11] != null ? ch[11].toString() : "");
			map.put("totaldue", ch[12] != null ? ch[12].toString() : ""); // Empty string if null
			map.put("mslab1", ch[13] != null ? ch[13].toString() : "");
			map.put("mslab2", ch[14] != null ? ch[14].toString() : "");
			map.put("mslab3", ch[15] != null ? ch[15].toString() : "");
			map.put("mslab4", ch[16] != null ? ch[16].toString() : "");
			map.put("mslab5", ch[17] != null ? ch[17].toString() : "");
			map.put("partyshortname", ch[18] != null ? ch[18].toString() : "");
			
			
			List1.add(map);
		}
		return List1;
	}
	
	
	
	}
	
	
