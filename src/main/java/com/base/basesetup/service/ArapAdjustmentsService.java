package com.base.basesetup.service;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.base.basesetup.dto.ArapAdjustmentsDTO;
import com.base.basesetup.entity.ArapAdjustmentsVO;
import com.base.basesetup.exception.ApplicationException;

@Service
public interface ArapAdjustmentsService {

//	ArapAdjustments
	List<ArapAdjustmentsVO> getAllArapAdjustmentsByOrgId(Long orgId);

	List<ArapAdjustmentsVO> getAllArapAdjustmentsById(Long id);

	List<ArapAdjustmentsVO> getArapAdjustmentsByActive();

	Map<String, Object> createUpdateArapAdjustments(@Valid ArapAdjustmentsDTO arapAdjustmentsDTO) throws ApplicationException;

	ArapAdjustmentsVO getArapAdjustmentsByDocId(Long orgId, String docId);

	String getArapAdjustmentsDocId(Long orgId, String finYear, String branch, String branchCode);
	
	
	List<Map<String, Object>> GetArapAgeing(String asondate , String pdate,String partyname,Long orgId);
	
	List<Map<String, Object>> GetArapAdjustments(@RequestParam(required = true) String Asondate, 
			@RequestParam(required = true) String partyname,@RequestParam(required = true) String branch,  
			@RequestParam(required = true)  Long orgId, 
			@RequestParam(required = false) String pdate, 
			@RequestParam(required = true) Long finyear	);
}
