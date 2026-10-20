package com.base.basesetup.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.base.basesetup.dto.QuotationNewDTO;
import com.base.basesetup.entity.QuotationNewVO;
import com.base.basesetup.exception.ApplicationException;

@Service
public interface QuotationService {


	List<QuotationNewVO> getQuotationByorgId(Long orgId);

	Optional<QuotationNewVO> getQutationById(Long id);

	Map<String, Object> createUpdateQuotation(QuotationNewDTO quotationDTO) throws ApplicationException;

}
