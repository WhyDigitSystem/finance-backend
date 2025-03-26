package com.base.basesetup.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	}
	
	
