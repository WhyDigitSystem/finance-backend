package com.base.basesetup.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.basesetup.dto.QuotationDetailsNewDTO;
import com.base.basesetup.dto.QuotationNewDTO;
import com.base.basesetup.entity.QuotationDetailsNewVO;
import com.base.basesetup.entity.QuotationNewVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.QuotationDetailsNewRepo;
import com.base.basesetup.repo.QuotationNewRepo;

@Service
public class QuotationServiceImpl implements QuotationService {

	public static final Logger LOGGER = LoggerFactory.getLogger(QuotationServiceImpl.class);

	@Autowired
	QuotationNewRepo quotationRepo;

	@Autowired
	QuotationDetailsNewRepo quotationDetailsRepo;

	@Override
	public Map<String, Object> createUpdateQuotation(QuotationNewDTO quotationDTO) throws ApplicationException {

	    QuotationNewVO quotationVO;
	    String message = null;

	    // Check if quotationDTO has an id
	    if (ObjectUtils.isEmpty(quotationDTO.getId())) {
	        // If no id, create a new quotation
	        quotationVO = new QuotationNewVO();
	        quotationVO.setCreatedBy(quotationDTO.getCreatedBy());
	        quotationVO.setUpdatedBy(quotationDTO.getCreatedBy());
	        message = "Quotation Created Successfully";
	    } else {
	        // If id exists, update the existing quotation
	        quotationVO = quotationRepo.findById(quotationDTO.getId()).orElseThrow(
	                () -> new ApplicationException("Quotation Not Found with id: " + quotationDTO.getId()));
	        quotationVO.setUpdatedBy(quotationDTO.getCreatedBy());
	        message = "Quotation Updation Successfully";
	    }

	    // Convert DTO to entity and set additional fields
	    quotationVO = getQuotationVOFromQuotationDTO(quotationVO, quotationDTO);

	    // Save or update the quotation in the database
	    quotationRepo.save(quotationVO);

	    // Prepare response
	    Map<String, Object> response = new HashMap<>();
	    response.put("message", message);
	    response.put("quotationVO", quotationVO);
	    return response;
	}

	private QuotationNewVO getQuotationVOFromQuotationDTO(QuotationNewVO quotationVO, QuotationNewDTO quotationDTO) {

	    // Set the basic details
	    quotationVO.setQuotationTo(quotationDTO.getQuotationTo());
	    quotationVO.setShippingAddress(quotationDTO.getShippingAddress());
	    quotationVO.setCustomerAddress(quotationDTO.getCustomerAddress());
//	    quotationVO.setFinYear(quotationDTO.getFinYear());
	    quotationVO.setOrgId(quotationDTO.getOrgId());
	    quotationVO.setCode(quotationDTO.getCode());

	    // Build the code using the prefix, financial year, date, and constant
//	    String code = quotationVO.getPrefix() + quotationDTO.getFinYear() + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMM")) + "-1";
//	    quotationVO.setCode(code);
	    
	    
	    if (quotationDTO.getId() != null) {
			// Clear previous items from the database
			List<QuotationDetailsNewVO> quotationDetailsVOs = quotationDetailsRepo.findByQuotationNewVO(quotationVO);
			quotationDetailsRepo.deleteAll(quotationDetailsVOs);

		}

	    // Set the list of quotation details
	    List<QuotationDetailsNewVO> quotationDetailsVOs = new ArrayList<>();
	    for (QuotationDetailsNewDTO quotationDetailsDTO : quotationDTO.getQuotationDetailsDTO()) {

	        QuotationDetailsNewVO quotationDetailsVO = new QuotationDetailsNewVO();

	        // Map the details to the entity
	        quotationDetailsVO.setDescription(quotationDetailsDTO.getDescription());
	        quotationDetailsVO.setPricre(quotationDetailsDTO.getPricre());
	        quotationDetailsVO.setUnit(quotationDetailsDTO.getUnit());
	        quotationDetailsVO.setTotal(quotationDetailsDTO.getTotal());

	        // Link back to the main quotation
	        quotationDetailsVO.setQuotationNewVO(quotationVO);
	        quotationDetailsVOs.add(quotationDetailsVO);
	    }

	    // Set the quotation details list in the main quotation entity
	    quotationVO.setQuotationDetailsVO(quotationDetailsVOs);

	    return quotationVO;
	}

	@Override
	public List<QuotationNewVO> getQuotationByorgId(Long orgId) {
		return quotationRepo.getQuotationByorgId(orgId);
	}

	@Override
	public Optional<QuotationNewVO> getQutationById(Long id) {
		return quotationRepo.findById(id);
	}


}
