package com.base.basesetup.service;

import java.math.BigDecimal;
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

import com.base.basesetup.dto.CostEstimationDTO;
import com.base.basesetup.dto.CostEstimationDetailsDTO;
import com.base.basesetup.entity.CostEstimationDetailsVO;
import com.base.basesetup.entity.CostEstimationVO;
import com.base.basesetup.entity.DocumentTypeMappingDetailsVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.CostEstimationDetailsRepo;
import com.base.basesetup.repo.CostEstimationRepo;
import com.base.basesetup.repo.DocumentTypeMappingDetailsRepo;

@Service
public class CostEstimationServiceImpl implements CostEstimationService {

	public static final Logger LOGGER = LoggerFactory.getLogger(CostEstimationServiceImpl.class);

	@Autowired
	CostEstimationRepo costEstimationRepo;

	@Autowired
	DocumentTypeMappingDetailsRepo documentTypeMappingDetailsRepo;

	@Autowired
	CostEstimationDetailsRepo costEstimationDetailsRepo;

	// CostEstimation

	@Override
	public List<CostEstimationVO> getAllCostEstimationByOrgId(Long orgId) {

		return costEstimationRepo.getAllCostEstimationByOrgId(orgId);
	}

	@Override
	public CostEstimationVO getAllCostEstimationById(Long id) {

		return costEstimationRepo.getAllCostEstimationById(id);
	}

	@Override
	public Map<String, Object> updateCreateCostEstimation(@Valid CostEstimationDTO costEstimationDTO)
			throws ApplicationException {
		String screenCode = "CE";
		CostEstimationVO costEstimationVO = new CostEstimationVO();
		String message;
		if (ObjectUtils.isNotEmpty(costEstimationDTO.getId())) {
			costEstimationVO = costEstimationRepo.findById(costEstimationDTO.getId())
					.orElseThrow(() -> new ApplicationException("CostEstimation Not Found!"));
			costEstimationVO.setUpdatedBy(costEstimationDTO.getCreatedBy());
			createUpdateCostEstimationVOByCostEstimationDTO(costEstimationDTO, costEstimationVO);
			message = "CostEstimation Updated Successfully";
		} else {
			// GETDOCID API
			createUpdateCostEstimationVOByCostEstimationDTO(costEstimationDTO, costEstimationVO);
			String docId = costEstimationRepo.getCostEstimationDocId(costEstimationDTO.getOrgId(),
					costEstimationDTO.getFinYear(), costEstimationDTO.getBranchCode(), screenCode);
			costEstimationVO.setDocId(docId);

			// GETDOCID LASTNO +1
			DocumentTypeMappingDetailsVO documentTypeMappingDetailsVO = documentTypeMappingDetailsRepo
					.findByOrgIdAndFinYearAndBranchCodeAndScreenCode(costEstimationDTO.getOrgId(),
							costEstimationDTO.getFinYear(), costEstimationDTO.getBranchCode(), screenCode);
			documentTypeMappingDetailsVO.setLastno(documentTypeMappingDetailsVO.getLastno() + 1);
			documentTypeMappingDetailsRepo.save(documentTypeMappingDetailsVO);

			costEstimationVO.setUpdatedBy(costEstimationDTO.getCreatedBy());
			costEstimationVO.setCreatedBy(costEstimationDTO.getCreatedBy());
//			createUpdateCostEstimationVOByCostEstimationDTO(costEstimationDTO, costEstimationVO);
			message = "CostEstimation Created Successfully";
		}

		costEstimationRepo.save(costEstimationVO);
		Map<String, Object> response = new HashMap<>();
		response.put("costEstimationVO", costEstimationVO);
		response.put("message", message);
		return response;
	}

	private void createUpdateCostEstimationVOByCostEstimationDTO(@Valid CostEstimationDTO costEstimationDTO,
			CostEstimationVO costEstimationVO) throws ApplicationException {
		costEstimationVO.setBranch(costEstimationDTO.getBranch());
		costEstimationVO.setBranchCode(costEstimationDTO.getBranchCode());
		costEstimationVO.setEmployeeName(costEstimationDTO.getEmployeeName());
		costEstimationVO.setEmployeeCode(costEstimationDTO.getEmployeeCode());
		costEstimationVO.setCreatedBy(costEstimationDTO.getCreatedBy());
		costEstimationVO.setActive(costEstimationDTO.isActive());
		costEstimationVO.setFinYear(costEstimationDTO.getFinYear());
		costEstimationVO.setBranchCode(costEstimationDTO.getBranchCode());
		costEstimationVO.setOrgId(costEstimationDTO.getOrgId());
		costEstimationVO.setDepartment(costEstimationDTO.getDepartment());

		if (ObjectUtils.isNotEmpty(costEstimationVO.getId())) {
			List<CostEstimationDetailsVO> costEstimationDetailsVO1 = costEstimationDetailsRepo
					.findByCostEstimationVO(costEstimationVO);
			costEstimationDetailsRepo.deleteAll(costEstimationDetailsVO1);
		}

		BigDecimal totalAmount = BigDecimal.ZERO;

		List<CostEstimationDetailsVO> costEstimationDetailsVOs = new ArrayList<>();
		for (CostEstimationDetailsDTO costEstimationDetailsDTO : costEstimationDTO.getCostEstimationDetailsDTO()) {
			CostEstimationDetailsVO costEstimationDetailsVO = new CostEstimationDetailsVO();

			costEstimationDetailsVO.setCategory(costEstimationDetailsDTO.getCategory());
			costEstimationDetailsVO.setParticulars(costEstimationDetailsDTO.getParticulars());
			costEstimationDetailsVO.setRemarks(costEstimationDetailsDTO.getRemarks());
			costEstimationDetailsVO.setAmount(costEstimationDetailsDTO.getAmount());

			totalAmount = totalAmount.add(costEstimationDetailsVO.getAmount());
			costEstimationDetailsVO.setCostEstimationVO(costEstimationVO);

			costEstimationDetailsVOs.add(costEstimationDetailsVO);

		}

		costEstimationVO.setTotalAmount(totalAmount);

		costEstimationVO.setCostEstimationDetailsVO(costEstimationDetailsVOs);

	}

	@Override
	public List<Map<String, Object>> getAllEmployees(Long orgId,String department) {
		Set<Object[]> customerName = costEstimationRepo.getAllEmployees(orgId,department);
		return getAllEmployees(customerName);
	}

	private List<Map<String, Object>> getAllEmployees(Set<Object[]> customer) {
		List<Map<String, Object>> doctypeMappingDetails = new ArrayList<>();
		for (Object[] sup : customer) {
			Map<String, Object> doctype = new HashMap<>();
			doctype.put("employeeName", sup[0] != null ? sup[0].toString() : "");
			doctype.put("employeeCode", sup[1] != null ? sup[1].toString() : "");
			doctypeMappingDetails.add(doctype);
		}

		return doctypeMappingDetails;
	}

	
	@Override
	public String getCostEstimationDocId(Long orgId, String finYear, String branch, String branchCode) {
		String ScreenCode = "CE";
		return  costEstimationRepo.getCostEstimationDocId(orgId, finYear, branchCode, ScreenCode);
		
	}

	
}
