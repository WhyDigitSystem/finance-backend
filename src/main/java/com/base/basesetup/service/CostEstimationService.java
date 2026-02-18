package com.base.basesetup.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.base.basesetup.dto.CostEstimationDTO;
import com.base.basesetup.entity.CostEstimationVO;
import com.base.basesetup.exception.ApplicationException;

@Service
public interface CostEstimationService {

	List<CostEstimationVO> getAllCostEstimationByOrgId(Long orgId,String finYear, String branchCode);

	CostEstimationVO getAllCostEstimationById(Long id);

	Map<String, Object> updateCreateCostEstimation(@Valid CostEstimationDTO costEstimationDTO)
			throws ApplicationException;

	List<Map<String, Object>> getAllEmployees(Long orgId);

	String getCostEstimationDocId(Long orgId, String finYear, String branch, String branchCode);

	CostEstimationVO approveCostEstimation(Long orgId, Long id, String docId, String action, String actionBy)
			throws ApplicationException;



	List<Map<String, Object>> getCostEstimationDetails(Long orgId, String finYear, String employeeName, String fromDate,
			String toDate, String branchCode,String category);

	List<Map<String, Object>> getCostEstimationSummary(Long orgId, String finYear, String employeeName, String fromDate,
			String toDate, String branchCode,String category);


	String uploadImageCostEstimationDetail(List<MultipartFile> files, Long costEstimationId, List<Long> detailsId)
			throws IOException;

	List<Map<String, Object>> getCostEstimationCount(Long orgId, String finYear, String branchCode);

	
//	CostEstimationVO uploadMultipleImagesToCostEstimationDetails(MultipartFile[] files, Long costEstimationId, List<Long> costEstmationDetailsId);

}
