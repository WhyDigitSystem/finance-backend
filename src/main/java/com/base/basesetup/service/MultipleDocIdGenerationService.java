package com.base.basesetup.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.base.basesetup.dto.MultipleDocIdGenerationDTO;
import com.base.basesetup.entity.MultipleDocIdGenerationVO;
import com.base.basesetup.exception.ApplicationException;

@Service
public interface MultipleDocIdGenerationService {

	MultipleDocIdGenerationVO getMultipleDocIdGenerationById(Long id) throws ApplicationException;

	Map<String, Object> createUpdateMultipleDocIdGeneration(MultipleDocIdGenerationDTO multipleDocIdGenerationDTO) throws ApplicationException;

	List<MultipleDocIdGenerationVO> getMultipleDocIdGenerationByOrgId(Long orgId) throws ApplicationException;

	List<Map<String, Object>> getPendingMultipleDocIdGeneration(Long orgId, String branch, String branchCode,
			String finYear, String finYearIdentifier,String docCode);

}
