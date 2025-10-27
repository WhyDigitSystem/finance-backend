package com.base.basesetup.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.base.basesetup.dto.CompanyEmployeeDTO;
import com.base.basesetup.dto.EltCompanyDTO;
import com.base.basesetup.entity.CompanyEmployeeVO;
import com.base.basesetup.entity.EltCompanyVO;
import com.base.basesetup.exception.ApplicationException;

@Service
public interface CompanyService {

	Map<String, Object> updateCreateCompany(@Valid EltCompanyDTO eltCompanyDTO) throws ApplicationException;

	Optional<EltCompanyVO> getEltCompanyById(Long id);
	
	List<EltCompanyVO> getAllEltCompany();

	//CompanyEmployee
	
	Map<String, Object> updateCreateCompanyEmployee(@Valid CompanyEmployeeDTO companyEmployeeDTO) throws ApplicationException;

	Optional<CompanyEmployeeVO> getCompanyEmployeeById(Long id);

	List<CompanyEmployeeVO> getAllCompanyEmployeeByOrgId(Long orgId);


	


}
