package com.base.basesetup.service;

import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.basesetup.dto.PartyTypeDTO;
import com.base.basesetup.entity.PartyTypeVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.PartyTypeRepo;

@Service
public class PartyTypeServiceImpl implements PartyTypeService {
	public static final Logger LOGGER = LoggerFactory.getLogger(MasterServiceImpl.class);

	@Autowired
	PartyTypeRepo partyTypeRepo;
	
	//PartyType
	
	@Override
	public PartyTypeVO createUpdatePartyType(@Valid PartyTypeDTO partyTypeDTO) throws ApplicationException {
		PartyTypeVO partyTypeVO = new PartyTypeVO();
		boolean isUpdate = false;
		if (ObjectUtils.isNotEmpty(partyTypeDTO.getId())) {
			isUpdate = true;
			partyTypeVO = partyTypeRepo.findById(partyTypeDTO.getId())
					.orElseThrow(() -> new ApplicationException("Invalid PartyType details"));
			partyTypeVO.setUpdatedBy(partyTypeDTO.getCreatedBy());
		} else {
			if (partyTypeRepo.existsByPartyTypeAndOrgId(partyTypeDTO.getPartyType(), partyTypeDTO.getOrgId())) {
				throw new ApplicationException("The given value PartyType already exists.");
			}
			if (partyTypeRepo.existsByPartyTypeCodeAndOrgId(partyTypeDTO.getPartyTypeCode(),
					partyTypeDTO.getOrgId())) {
				throw new ApplicationException("The given value PartyTypeCode already exists.");
			}
			partyTypeVO.setUpdatedBy(partyTypeDTO.getCreatedBy());
			partyTypeVO.setCreatedBy(partyTypeDTO.getCreatedBy());
		}
		if (isUpdate) {
			PartyTypeVO partyType = partyTypeRepo.findById(partyTypeDTO.getId()).orElse(null);
			if (!partyType.getPartyType().equalsIgnoreCase(partyTypeDTO.getPartyType())) {
				if (partyTypeRepo.existsByPartyTypeAndOrgId(partyTypeDTO.getPartyType(), partyTypeDTO.getOrgId())) {
					throw new ApplicationException("The given value PartyType already exists.");
				}
			}
			if (!partyType.getPartyTypeCode().equalsIgnoreCase(partyTypeDTO.getPartyTypeCode())) {
				if (partyTypeRepo.existsByPartyTypeCodeAndOrgId(partyTypeDTO.getPartyTypeCode(),
						partyTypeDTO.getOrgId())) {
					throw new ApplicationException("The given value PartyTypeCode already exists.");
				}
			}
		}
		getPartyTypeVOFromPartyTypeDTO(partyTypeDTO, partyTypeVO);
		return partyTypeRepo.save(partyTypeVO);
	}

	private void getPartyTypeVOFromPartyTypeDTO(@Valid PartyTypeDTO partyTypeDTO, PartyTypeVO partyTypeVO) {
//		partyTypeVO.setPartyType(partyTypeDTO.getPartyType());
		String partyType = partyTypeDTO.getPartyType(); 
        if (partyType != null) {
        	partyTypeVO.setPartyType(partyType.toUpperCase()); // Set field to uppercase
        }
//		partyTypeVO.setPartyTypeCode(partyTypeDTO.getPartyTypeCode());
		String partyTypeCode = partyTypeDTO.getPartyTypeCode(); 
        if (partyTypeCode != null) {
        	partyTypeVO.setPartyTypeCode(partyTypeCode.toUpperCase()); // Set field to uppercase
        }
		partyTypeVO.setCancel(partyTypeDTO.isCancel());
		partyTypeVO.setOrgId(partyTypeDTO.getOrgId());
		partyTypeVO.setActive(partyTypeDTO.isActive());
		partyTypeVO.setCancelRemarks(partyTypeDTO.getCancelRemarks());


	}

}
