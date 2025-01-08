package com.base.basesetup.service;

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

import com.base.basesetup.dto.MultipleDocIdGenerationDTO;
import com.base.basesetup.dto.MultipleDocIdGenerationDetailsDTO;
import com.base.basesetup.entity.DocumentTypeVO;
import com.base.basesetup.entity.MultipleDocIdGenerationDetailsVO;
import com.base.basesetup.entity.MultipleDocIdGenerationVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.MultipleDocIdGenerationRepo;

@Service
public class MultipleDocIdGenerationServiceImpl implements MultipleDocIdGenerationService {

	public static final Logger LOGGER = LoggerFactory.getLogger(DocumentTypeServiceImple.class);

	@Autowired
	MultipleDocIdGenerationRepo multipleDocIdGenerationRepo;
	
	@Override
	public Map<String, Object> createUpdateMultipleDocIdGeneration(MultipleDocIdGenerationDTO multipleDocIdGenerationDTO)
			throws ApplicationException {
		String message;
		MultipleDocIdGenerationVO multipleDocIdGenerationVO = new MultipleDocIdGenerationVO();
		multipleDocIdGenerationVO.setBranch(multipleDocIdGenerationDTO.getBranch());
		multipleDocIdGenerationVO.setBranchCode(multipleDocIdGenerationDTO.getBranchCode());
		multipleDocIdGenerationVO.setFinYear(multipleDocIdGenerationDTO.getFinYear());
		multipleDocIdGenerationVO.setFinYearIdentifier(multipleDocIdGenerationDTO.getFinYearIdentifier());
		multipleDocIdGenerationVO.setOrgId(multipleDocIdGenerationDTO.getOrgId());
		multipleDocIdGenerationVO.setScreenName(multipleDocIdGenerationDTO.getScreenName());
		multipleDocIdGenerationVO.setScreenCode(multipleDocIdGenerationDTO.getScreenCode());
		multipleDocIdGenerationVO.setDocCode(multipleDocIdGenerationDTO.getDocCode());
		multipleDocIdGenerationVO.setCreatedBy(multipleDocIdGenerationDTO.getCreatedBy());
		multipleDocIdGenerationVO.setUpdatedBy(multipleDocIdGenerationDTO.getCreatedBy());

		List<MultipleDocIdGenerationDetailsVO> multipleDocIdGenerationDetailsVO = new ArrayList<>();

		if (multipleDocIdGenerationDTO.getMultipleDocIdGenerationDetailsDTO() != null) {
			for (MultipleDocIdGenerationDetailsDTO multipleDocIdGenerationDetailsDTO : multipleDocIdGenerationDTO.
					getMultipleDocIdGenerationDetailsDTO()) {
				MultipleDocIdGenerationDetailsVO multipleDocIdGenerationDetailsVO1 = new MultipleDocIdGenerationDetailsVO();
				multipleDocIdGenerationDetailsVO1.setScreenCode(multipleDocIdGenerationVO.getScreenCode());
				multipleDocIdGenerationDetailsVO1.setScreenName(multipleDocIdGenerationVO.getScreenName());
				multipleDocIdGenerationDetailsVO1.setDocCode(multipleDocIdGenerationVO.getDocCode());
				multipleDocIdGenerationDetailsVO1.setBranch(multipleDocIdGenerationDetailsDTO.getBranch());
				multipleDocIdGenerationDetailsVO1.setBranchCode(multipleDocIdGenerationDetailsDTO.getBranchCode());
				multipleDocIdGenerationDetailsVO1.setPrefixField(multipleDocIdGenerationDetailsDTO.getPrefixField());
				multipleDocIdGenerationDetailsVO1.setFinYear(multipleDocIdGenerationDetailsDTO.getFinYear());
				multipleDocIdGenerationDetailsVO1
						.setFinYearIdentifier(multipleDocIdGenerationDetailsDTO.getFinYearIdentifier());
				multipleDocIdGenerationDetailsVO1.setConcatenation(multipleDocIdGenerationDetailsDTO.getScreenCode()
						+ multipleDocIdGenerationDetailsDTO.getDocCode());
				multipleDocIdGenerationDetailsVO1.setSourceDocCode(multipleDocIdGenerationDetailsDTO.getDocCode());
				multipleDocIdGenerationDetailsVO1.setSourceScreenCode(multipleDocIdGenerationDetailsDTO.getScreenCode());
				multipleDocIdGenerationDetailsVO1.setSourceScreenName(multipleDocIdGenerationDetailsDTO.getScreenName());
				multipleDocIdGenerationDetailsVO1.setOrgId(multipleDocIdGenerationDTO.getOrgId());
				multipleDocIdGenerationDetailsVO1.setMultipleDocIdGenerationVO(multipleDocIdGenerationVO);
				multipleDocIdGenerationDetailsVO.add(multipleDocIdGenerationDetailsVO1);
			}
		}
		multipleDocIdGenerationVO.setMultipleDocIdGenerationDetailsVO(multipleDocIdGenerationDetailsVO);
		multipleDocIdGenerationRepo.save(multipleDocIdGenerationVO);
		message = "MultipleDocIdGeneration created Successfully";
		Map<String, Object> response = new HashMap<>();
		response.put("multipleDocIdGenerationVO", multipleDocIdGenerationVO);
		response.put("message", message);
		return response;

	}
	
	@Override
	public MultipleDocIdGenerationVO getMultipleDocIdGenerationById(Long id) throws ApplicationException {
		if (ObjectUtils.isEmpty(id)) {
			throw new ApplicationException("Invalid MultipleDocIdGeneration Id");
		}
		MultipleDocIdGenerationVO multipleDocIdGenerationVO = multipleDocIdGenerationRepo.findById(id)
				.orElseThrow(() -> new ApplicationException("MultipleDocIdGeneration not found for Id: " + id));

		return multipleDocIdGenerationVO;
	}
	
	@Override
	public List<MultipleDocIdGenerationVO> getMultipleDocIdGenerationByOrgId(Long orgId) {

		return multipleDocIdGenerationRepo.findMultipleDocIdGenerationByOrgId(orgId);
	}
	
	
	@Override
	public List<Map<String, Object>> getPendingMultipleDocIdGeneration(Long orgId, String branch, String branchCode,
			String finYear, String finYearIdentifier,String docCode) {
		Set<Object[]> pendingMultipleDocIdGenDetails = multipleDocIdGenerationRepo.getPendingMultipleDocIdGeneration(orgId, branch,
				branchCode, finYear, finYearIdentifier,docCode);
		return getPendingMultipleDocIdGeneration(pendingMultipleDocIdGenDetails);
	}

	private List<Map<String, Object>> getPendingMultipleDocIdGeneration(Set<Object[]> pendingMultipleDocIdDetails) {
		List<Map<String, Object>> multipleDocIdDetails = new ArrayList<>();
		for (Object[] sup : pendingMultipleDocIdDetails) {
			Map<String, Object> doctype = new HashMap<>();
			doctype.put("screenName", sup[0] != null ? sup[0].toString() : "");
			doctype.put("screenCode", sup[1] != null ? sup[1].toString() : "");
			doctype.put("docCode", sup[2] != null ? sup[2].toString() : "");
			doctype.put("finYear", sup[3] != null ? sup[3].toString() : "");
			doctype.put("branch", sup[4] != null ? sup[4].toString() : "");
			doctype.put("branchCode", sup[5] != null ? sup[5].toString() : "");
			doctype.put("finYearIdentifier", sup[6] != null ? sup[6].toString() : "");
			doctype.put("prefixField", sup[7] != null ? sup[7].toString() : "");
			multipleDocIdDetails.add(doctype);
		}

		return multipleDocIdDetails;
	}
	
}
