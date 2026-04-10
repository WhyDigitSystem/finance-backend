package com.base.basesetup.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import com.base.basesetup.dto.ClientCompanyDTO;
import com.base.basesetup.entity.ClientCompanyVO;
import com.base.basesetup.exception.ApplicationException;

public interface ClientCompanyService {

	List<ClientCompanyVO> getClientCompanyByOrgId(Long orgId);

	Optional<ClientCompanyVO> getClientCompanyById(Long id);

	Map<String, Object> updateCreateClientCompany(@Valid ClientCompanyDTO clientCompanyDTO) throws ApplicationException, Exception;

}
