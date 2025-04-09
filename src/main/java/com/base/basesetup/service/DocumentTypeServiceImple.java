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

import com.base.basesetup.dto.DocumentTypeDTO;
import com.base.basesetup.dto.DocumentTypeMappingDTO;
import com.base.basesetup.dto.DocumentTypeMappingDetailsDTO;
import com.base.basesetup.entity.DocumentTypeMappingDetailsVO;
import com.base.basesetup.entity.DocumentTypeMappingVO;
import com.base.basesetup.entity.DocumentTypeVO;
import com.base.basesetup.entity.FinancialYearVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.DocumentTypeMappingDetailsRepo;
import com.base.basesetup.repo.DocumentTypeMappingRepo;
import com.base.basesetup.repo.DocumentTypeRepo;
import com.base.basesetup.repo.FinancialYearRepo;


@Service
public class DocumentTypeServiceImple  implements DocumentTypeService{
	
	public static final Logger LOGGER = LoggerFactory.getLogger(DocumentTypeServiceImple.class);
	
	@Autowired
	DocumentTypeRepo documentTypeRepo;
	
	@Autowired
	DocumentTypeMappingRepo documentTypeMappingRepo;
	
	@Autowired
	DocumentTypeMappingDetailsRepo documentTypeMappingDetailsRepo;
	
	@Autowired
	FinancialYearRepo financialYearRepo;
	
	// Document Type
	@Override
	public Map<String, Object> createUpdateDocumentType(DocumentTypeDTO documentTypeDTO) throws ApplicationException {
		DocumentTypeVO documentTypeVO = new DocumentTypeVO();
		String message;
		if (ObjectUtils.isEmpty(documentTypeDTO.getId())) {
			if (documentTypeRepo.existsByOrgIdAndScreenCode(documentTypeDTO.getOrgId(),
					documentTypeDTO.getScreenCode())) {
				throw new ApplicationException("ScreenCode already exist ");
			}

			if (documentTypeRepo.existsByOrgIdAndDocCode(documentTypeDTO.getOrgId(), documentTypeDTO.getDocCode())) {
				throw new ApplicationException("Doc Code already exist ");
			}
			documentTypeVO.setDocCode(documentTypeDTO.getDocCode());
			documentTypeVO.setScreenCode(documentTypeDTO.getScreenCode());
			documentTypeVO.setCreatedBy(documentTypeDTO.getCreatedBy());
			documentTypeVO.setUpdatedBy(documentTypeDTO.getCreatedBy());
			mapDocumentTypeDTOToDocumentTypeVO(documentTypeDTO, documentTypeVO);
			message = "Document Type Created successfully";
		} else {
			documentTypeVO = documentTypeRepo.findById(documentTypeDTO.getId()).orElse(null);

			if (!documentTypeVO.getScreenCode().equalsIgnoreCase(documentTypeDTO.getScreenCode())) {
				if (documentTypeRepo.existsByOrgIdAndScreenCode(documentTypeDTO.getOrgId(),
						documentTypeDTO.getScreenCode())) {
					throw new ApplicationException("ScreenCode already exist ");
				}
				documentTypeVO.setScreenCode(documentTypeDTO.getScreenCode());
			}

			if (!documentTypeVO.getDocCode().equalsIgnoreCase(documentTypeDTO.getDocCode())) {
				if (documentTypeRepo.existsByOrgIdAndDocCode(documentTypeDTO.getOrgId(),
						documentTypeDTO.getDocCode())) {
					throw new ApplicationException("Doc Code already exist ");
				}
				documentTypeVO.setDocCode(documentTypeDTO.getDocCode());
			}
			documentTypeVO.setUpdatedBy(documentTypeDTO.getCreatedBy());
			// Update the remaining fields from carrierDTO to carrierVO
			mapDocumentTypeDTOToDocumentTypeVO(documentTypeDTO, documentTypeVO);
			message = "Document Type Updated successfully";

		}
		documentTypeRepo.save(documentTypeVO);
		Map<String, Object> response = new HashMap<>();
		response.put("documentTypeVO", documentTypeVO);
		response.put("message", message);
		return response;
	}

	private void mapDocumentTypeDTOToDocumentTypeVO(DocumentTypeDTO documentTypeDTO, DocumentTypeVO documentTypeVO) {

		documentTypeVO.setDescription(documentTypeDTO.getDescription());
		documentTypeVO.setOrgId(documentTypeDTO.getOrgId());
		documentTypeVO.setScreenName(documentTypeDTO.getScreenName());
	}

	@Override
	public DocumentTypeVO getDocumentTypeById(Long id) throws ApplicationException {
		if (ObjectUtils.isEmpty(id)) {
			throw new ApplicationException("Invalid DocumentType Id");
		}
		DocumentTypeVO documentTypeVO = documentTypeRepo.findById(id)
				.orElseThrow(() -> new ApplicationException("Document Type not found for Id: " + id));

		return documentTypeVO;
	}

	@Override
	public List<DocumentTypeVO> getAllDocumentTypeByOrgId(Long orgId) {

		return documentTypeRepo.findAllByOrgId(orgId);
	}

	@Override
	public Map<String, Object> createDocumentTypeMapping(DocumentTypeMappingDTO documentTypeMappingDTO)
			throws ApplicationException {
		String message;
		DocumentTypeMappingVO documentTypeMappingVO = new DocumentTypeMappingVO();
		documentTypeMappingVO.setBranch(documentTypeMappingDTO.getBranch());
		documentTypeMappingVO.setBranchCode(documentTypeMappingDTO.getBranchCode());
		documentTypeMappingVO.setFinYear(documentTypeMappingDTO.getFinYear());
		documentTypeMappingVO.setFinYearIdentifier(documentTypeMappingDTO.getFinYearIdentifier());
		documentTypeMappingVO.setOrgId(documentTypeMappingDTO.getOrgId());
		documentTypeMappingVO.setCreatedBy(documentTypeMappingDTO.getCreatedBy());
		documentTypeMappingVO.setUpdatedBy(documentTypeMappingDTO.getCreatedBy());

		List<DocumentTypeMappingDetailsVO> documentTypeMappingDetailsVO = new ArrayList<>();

		if (documentTypeMappingDTO.getDocumentTypeMappingDetailsDTO() != null) {
			for (DocumentTypeMappingDetailsDTO documentTypeMappingDetailsDTO : documentTypeMappingDTO
					.getDocumentTypeMappingDetailsDTO()) {
				DocumentTypeMappingDetailsVO documentTypeMappingDetailsVO1 = new DocumentTypeMappingDetailsVO();
				documentTypeMappingDetailsVO1.setScreenCode(documentTypeMappingDetailsDTO.getScreenCode());
				documentTypeMappingDetailsVO1.setScreenName(documentTypeMappingDetailsDTO.getScreenName());
				documentTypeMappingDetailsVO1.setDocCode(documentTypeMappingDetailsDTO.getDocCode());
				documentTypeMappingDetailsVO1.setBranch(documentTypeMappingDetailsDTO.getBranch());
				documentTypeMappingDetailsVO1.setBranchCode(documentTypeMappingDetailsDTO.getBranchCode());
				documentTypeMappingDetailsVO1.setPrefixField(documentTypeMappingDetailsDTO.getPrefixField());
				documentTypeMappingDetailsVO1.setFinYear(documentTypeMappingDetailsDTO.getFinYear());
				documentTypeMappingDetailsVO1
						.setFinYearIdentifier(documentTypeMappingDetailsDTO.getFinYearIdentifier());
				documentTypeMappingDetailsVO1.setConcatenation(documentTypeMappingDetailsDTO.getScreenCode()
						+ documentTypeMappingDetailsDTO.getDocCode());
				documentTypeMappingDetailsVO1.setOrgId(documentTypeMappingDTO.getOrgId());
				documentTypeMappingDetailsVO1.setDocumentTypeMappingVO(documentTypeMappingVO);
				documentTypeMappingDetailsVO.add(documentTypeMappingDetailsVO1);
			}
		}
		documentTypeMappingVO.setDocumentTypeMappingDetailsVO(documentTypeMappingDetailsVO);
		documentTypeMappingRepo.save(documentTypeMappingVO);
		message = "Document Type created Successfully";
		Map<String, Object> response = new HashMap<>();
		response.put("documentTypeMappingVO", documentTypeMappingVO);
		response.put("message", message);
		return response;

	}

	@Override
	public List<Map<String, Object>> getPendingDocumentTypeMapping(Long orgId, String branch, String branchCode,
			String finYear, String finYearIdentifier) {
		int finyear= Integer.parseInt(finYear.toString());
		FinancialYearVO financialYearVO= financialYearRepo.findByOrgIdAndFinYear(orgId,finyear);
		String finYearIden=financialYearVO.getFinYearId().toString();
		Set<Object[]> pendingDocTypeDetails = documentTypeMappingRepo.getPendingDoctypeMapping(orgId, branch,
				branchCode, finYear, finYearIden);
		return getPendingDocType(pendingDocTypeDetails);
	}

	private List<Map<String, Object>> getPendingDocType(Set<Object[]> pendingDocTypeDetails) {
		List<Map<String, Object>> doctypeMappingDetails = new ArrayList<>();
		for (Object[] sup : pendingDocTypeDetails) {
			Map<String, Object> doctype = new HashMap<>();
			doctype.put("screenName", sup[0] != null ? sup[0].toString() : "");
			doctype.put("screenCode", sup[1] != null ? sup[1].toString() : "");
			doctype.put("docCode", sup[2] != null ? sup[2].toString() : "");
			doctype.put("finYear", sup[3] != null ? sup[3].toString() : "");
			doctype.put("branch", sup[4] != null ? sup[4].toString() : "");
			doctype.put("branchCode", sup[5] != null ? sup[5].toString() : "");
			doctype.put("finYearIdentifier", sup[6] != null ? sup[6].toString() : "");
			doctype.put("prefixField", sup[7] != null ? sup[7].toString() : "");
			doctypeMappingDetails.add(doctype);
		}

		return doctypeMappingDetails;
	}
	
	@Override
	public List<DocumentTypeMappingVO> getAllDocumentTypeMapping(Long orgId) {

		return documentTypeMappingRepo.findByOrgId(orgId);
	}

	@Override
	public DocumentTypeMappingVO getDocumentTypeMappingById(Long id) throws ApplicationException {
		if (ObjectUtils.isEmpty(id)) {
			throw new ApplicationException("Invalid DocumentTypeMapping Id");
		}
		DocumentTypeMappingVO documentTypeMappingVO = documentTypeMappingRepo.findById(id)
				.orElseThrow(() -> new ApplicationException("Document Type Mapping not found for Id: " + id));

		return documentTypeMappingVO;
	}

}
