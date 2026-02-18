package com.base.basesetup.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.base.basesetup.dto.MonthlyProcessDTO;
import com.base.basesetup.entity.MonthlyProcessVO;
import com.base.basesetup.exception.ApplicationException;

@Service
public interface MonthlyProcessService {

	Map<String, Object> createUpdateMonthlyProcess(MonthlyProcessDTO monthlyProcessDTO) throws ApplicationException;
	
	List<MonthlyProcessVO>getAllMonthlyProcessByClientCode(Long orgId,String clientCode,String mainGroup,String subGroupCode, String finYear);

	MonthlyProcessVO getAllMonthlyProcessById(Long id);
}
