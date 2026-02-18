package com.base.basesetup.service;

import java.util.List;
import java.util.Map;

import com.base.basesetup.dto.AllotmentDTO;
import com.base.basesetup.entity.AllotmentVO;
import com.base.basesetup.responseDTO.AllotmentResponseDTO;

public interface AllotmentService {

	Map<String, Object> createUpdateAllotment(AllotmentDTO allotmentDTO);

	List<AllotmentVO> getAllAllotmentByOrgId(Long orgId);

	AllotmentResponseDTO getAllotmentById(Long id);

}
