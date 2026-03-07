package com.base.basesetup.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.basesetup.dto.AllotmentDTO;
import com.base.basesetup.dto.AllotmentDetailsDTO;
import com.base.basesetup.entity.AllotmentDetailsVO;
import com.base.basesetup.entity.AllotmentVO;
import com.base.basesetup.entity.ListOfValuesVO;
import com.base.basesetup.repo.AllotmentDetailsRepo;
import com.base.basesetup.repo.AllotmentRepo;
import com.base.basesetup.repo.ListOfValuesRepo;
import com.base.basesetup.repo.PartyMasterRepo;
import com.base.basesetup.responseDTO.AllotmentDetailsResponseDTO;
import com.base.basesetup.responseDTO.AllotmentResponseDTO;


@Service
public class AllotmentServiceImpl implements AllotmentService{
	
	public static final Logger LOGGER = LoggerFactory.getLogger(AllotmentServiceImpl.class);

	
	@Autowired
	AllotmentRepo allotmentRepo;
	
	@Autowired
	AllotmentDetailsRepo allotmentDetailsRepo;
	
	@Autowired
	PartyMasterRepo partymasterRepo;
	
	@Autowired
	ListOfValuesRepo listOfValuesRepo;

	@Transactional(rollbackOn = Exception.class)
	@Override
	public Map<String, Object> createUpdateAllotment(AllotmentDTO dto) {

	    AllotmentVO allotmentVO;
	    String message;

	    // ---------- UPDATE ----------
	    if (dto.getId() != null) {

	        allotmentVO = allotmentRepo.findById(dto.getId())
	                .orElseThrow(() ->
	                        new RuntimeException("Invalid Allotment ID"));

	        allotmentVO.setUpdatedBy(dto.getCreatedBy());

	        // delete old child rows
	        List<AllotmentDetailsVO> allotmentDetailsVOs = allotmentDetailsRepo.findByAllotmentVO(allotmentVO);
	        allotmentDetailsRepo.deleteAll(allotmentDetailsVOs);
	        message = "Allotment Updated Successfully";

	    } else {

	        // ---------- CREATE ----------
	        allotmentVO = new AllotmentVO();
	        allotmentVO.setCreatedBy(dto.getCreatedBy());
	        allotmentVO.setUpdatedBy(dto.getCreatedBy());

	        message = "Allotment Created Successfully";
	    }

	    // ---------- MAP HEADER ----------
	    mapAllotmentDTOAndAllotmentVO(dto, allotmentVO);

	    // ---------- SAVE ----------
	    allotmentRepo.save(allotmentVO);

	    AllotmentResponseDTO responseDTO =
	            mapToAllotmentResponseDTO(allotmentVO);

	    Map<String, Object> response = new HashMap<>();
	    response.put("allotmentVO", responseDTO);
	    response.put("message", message);

	    return response;
	}

	private void mapAllotmentDTOAndAllotmentVO(
	        AllotmentDTO dto,
	        AllotmentVO allotmentVO) {

	    // ---------- HEADER ----------
	    allotmentVO.setSupplier(dto.getSupplier());
	    allotmentVO.setCustomer(dto.getCustomer());
	    allotmentVO.setMode(dto.getMode());

	    allotmentVO.setStartDate(dto.getStartDate());
	    allotmentVO.setEndDate(dto.getEndDate());

	    allotmentVO.setActive(dto.isActive());
	    allotmentVO.setOrgId(dto.getOrgId());
	    allotmentVO.setCancel(dto.isCancel());

	    allotmentVO.setBranchCode(dto.getBranchCode());
	    allotmentVO.setBranchName(dto.getBranchName());

	    // ---------- DETAILS ----------
	    List<AllotmentDetailsVO> allotmentDetails =
	            new ArrayList<>();

	    if (dto.getAllotmentDetailsDTO() != null) {

	        for (AllotmentDetailsDTO t :
	                dto.getAllotmentDetailsDTO()) {

	            AllotmentDetailsVO td =
	                    new AllotmentDetailsVO();

	            td.setProjectCode(t.getProjectCode());
	            td.setPart(t.getPart());
	            td.setInventory(t.getInventory());
	            td.setSchedule(t.getSchedule());
	            td.setMonth(t.getMonth());
	            td.setDay(t.getDay());

	            td.setBoxesReq(t.getBoxesReq());
	            td.setShortage(t.getShortage());
	            td.setShorted(t.getShorted());
	            td.setAdherence(t.getAdherence());
	            td.setAllot(t.getAllot());
	            td.setKitNo(t.getKitNo());
	            td.setKitDesc(t.getKitDesc());
	            td.setPartNo(t.getPartNo());

	            // 🔥 VERY IMPORTANT
	            td.setAllotmentVO(allotmentVO);

	            allotmentDetails.add(td);
	        }
	    }

	    allotmentVO.setAllotmentDetailsVO(allotmentDetails);
	}

	
	private AllotmentResponseDTO mapToAllotmentResponseDTO(
	        AllotmentVO allotmentVO) {

	    AllotmentResponseDTO dto =
	            new AllotmentResponseDTO();

	    // ---------- HEADER ----------
	    dto.setId(allotmentVO.getId());
	    dto.setSupplier(allotmentVO.getSupplier());
	    dto.setCustomer(allotmentVO.getCustomer());
	    dto.setMode(allotmentVO.getMode());

	    dto.setStartDate(allotmentVO.getStartDate());
	    dto.setEndDate(allotmentVO.getEndDate());

	    dto.setActive(allotmentVO.isActive());
	    dto.setCreatedBy(allotmentVO.getCreatedBy());
	    dto.setUpdatedBy(allotmentVO.getUpdatedBy());

	    dto.setOrgId(allotmentVO.getOrgId());
	    dto.setCancel(allotmentVO.isCancel());

	    dto.setBranchCode(allotmentVO.getBranchCode());
	    dto.setBranchName(allotmentVO.getBranchName());

	    // ---------- DETAILS ----------
	    List<AllotmentDetailsResponseDTO> detailList =
	            new ArrayList<>();

	    if (allotmentVO.getAllotmentDetailsVO() != null) {

	        for (AllotmentDetailsVO d :
	                allotmentVO.getAllotmentDetailsVO()) {

	        	AllotmentDetailsResponseDTO det =
	        	        new AllotmentDetailsResponseDTO();

	            det.setProjectCode(d.getProjectCode());
	            det.setPart(d.getPart());
	            det.setInventory(d.getInventory());
	            det.setSchedule(d.getSchedule());
	            det.setMonth(d.getMonth());
	            det.setDay(d.getDay());

	            det.setBoxesReq(d.getBoxesReq());
	            det.setShortage(d.getShortage());
	            det.setShorted(d.getShorted());
	            det.setAdherence(d.getAdherence());
	            det.setAllot(d.getAllot());
	            det.setKitNo(d.getKitNo());
	            det.setKitDesc(d.getKitDesc());
	            det.setPartNo(d.getPartNo());
	            detailList.add(det);
	        }
	    }

	    dto.setAllotmentDetailsResponseDTO(detailList);

	    return dto;
	}

	@Override
	public List<AllotmentVO> getAllAllotmentByOrgId(Long orgId) {
		return allotmentRepo.getAllAllotmentByOrgId(orgId);

	}
	
	@Override
	public AllotmentResponseDTO getAllotmentById(Long id) {
		AllotmentVO allotmentVO = allotmentRepo.findById(id).orElseThrow();
		AllotmentResponseDTO allotmentResponseDTO = mapToAllotmentResponseDTO(allotmentVO);
		return allotmentResponseDTO;
	}
	
	@Override
	public List<ListOfValuesVO> getOemDetails(Long orgId) {
		return listOfValuesRepo.getOemDetails(orgId);

	}


	@Override
	public List<Map<String, Object>> getCustomer(Long orgId) {
		Set<Object[]> chType = partymasterRepo.getCustomer(orgId);
		return getTds(chType);
	}

	private List<Map<String, Object>> getTds(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();

			map.put("customerShortName", ch[0] != null ? ch[0].toString() : "");
			map.put("customer", ch[1] != null ? ch[1].toString() : "");
			map.put("customerCode", ch[2] != null ? ch[2].toString() : "");

			List1.add(map);
		}
		return List1;

	}
}
