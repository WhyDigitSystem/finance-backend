package com.base.basesetup.service;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.base.basesetup.dto.CostEstimationDTO;
import com.base.basesetup.entity.CostEstimationVO;
import com.base.basesetup.exception.ApplicationException;

@Service
public interface CostEstimationService {

	List<CostEstimationVO> getAllCostEstimationByOrgId(Long orgId);

	CostEstimationVO getAllCostEstimationById(Long id);

	Map<String, Object> updateCreateCostEstimation(@Valid CostEstimationDTO costEstimationDTO)
			throws ApplicationException;

	List<Map<String, Object>> getAllEmployees(Long orgId,String department);

	String getCostEstimationDocId(Long orgId, String finYear, String branch, String branchCode);

}
