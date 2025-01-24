package com.base.basesetup.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.base.basesetup.dto.OffSetDTO;
import com.base.basesetup.entity.OffSetVO;
import com.base.basesetup.exception.ApplicationException;

@Service
public interface OffSetService {
	
	List<OffSetVO>getAllOffSet(Long orgId,String finYear,String branchCode);
	
	OffSetVO getOffSetById(Long Id);
	
	Map<String, Object> updateCreateOffSet( OffSetDTO offSetDTO) throws ApplicationException;

	String getOffSetDocId(Long orgId, String finYear, String branch, String branchCode);

}
