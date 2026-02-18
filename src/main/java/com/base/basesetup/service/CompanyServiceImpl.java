package com.base.basesetup.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.basesetup.dto.CompanyEmployeeDTO;
import com.base.basesetup.dto.EltCompanyDTO;
import com.base.basesetup.entity.CompanyEmployeeVO;
import com.base.basesetup.entity.EltCompanyVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.BranchRepo;
import com.base.basesetup.repo.CompanyEmployeeRepo;
import com.base.basesetup.repo.EltCompanyRepo;

@Service
public class CompanyServiceImpl implements CompanyService {

	@Autowired
	EltCompanyRepo eltCompanyRepo;

	@Autowired
	CompanyEmployeeRepo companyEmployeeRepo;

	
	@Autowired
	BranchRepo branchRepo;
	
	public static final Logger LOGGER = LoggerFactory.getLogger(CompanyServiceImpl.class);

	@Override
	public Map<String, Object> updateCreateCompany(@Valid EltCompanyDTO eltCompanyDTO) throws ApplicationException {

		EltCompanyVO eltCompanyVO;

		String message = null;

		if (ObjectUtils.isEmpty(eltCompanyDTO.getId())) {

			eltCompanyVO = new EltCompanyVO();

			if (eltCompanyRepo.existsByCompanyCodeAndId(eltCompanyDTO.getCompanyCode(), eltCompanyDTO.getId())) {
				String errorMessage = String.format("The CompanyCode: %s  already exists This Organization.",
						eltCompanyDTO.getCompanyCode());
				throw new ApplicationException(errorMessage);
			}

			if (eltCompanyRepo.existsByCompanyNameAndId(eltCompanyDTO.getCompanyName(), eltCompanyDTO.getId())) {
				String errorMessage = String.format("The CompanyName: %s  already exists This Organization.",
						eltCompanyDTO.getCompanyName());
				throw new ApplicationException(errorMessage);
			}

			if (eltCompanyRepo.existsByEmailAndId(eltCompanyDTO.getEmail(), eltCompanyDTO.getId())) {
				String errorMessage = String.format("The CompanyMail: %s  already exists This Organization.",
						eltCompanyDTO.getEmail());
				throw new ApplicationException(errorMessage);
			}

			if (eltCompanyRepo.existsByPhoneAndId(eltCompanyDTO.getPhone(), eltCompanyDTO.getId())) {
				String errorMessage = String.format("The PhoneNo: %s  already exists This Organization.",
						eltCompanyDTO.getPhone());
				throw new ApplicationException(errorMessage);
			}

			if (eltCompanyRepo.existsByWebSiteAndId(eltCompanyDTO.getWebSite(), eltCompanyDTO.getId())) {
				String errorMessage = String.format("The WebSite: %s  already exists This Organization.",
						eltCompanyDTO.getWebSite());
				throw new ApplicationException(errorMessage);
			}

			eltCompanyVO.setCreatedBy(eltCompanyDTO.getCreatedBy());

			eltCompanyVO.setUpdatedBy(eltCompanyDTO.getCreatedBy());

			message = "Elt Company Successfully";
		} else {

			eltCompanyVO = eltCompanyRepo.findById(eltCompanyDTO.getId()).orElseThrow(
					() -> new ApplicationException("Elt CompanyVO  Not Found with id: " + eltCompanyDTO.getId()));
			eltCompanyVO.setUpdatedBy(eltCompanyDTO.getCreatedBy());

			if (!eltCompanyVO.getCompanyCode().equalsIgnoreCase(eltCompanyDTO.getCompanyCode())) {
				if (eltCompanyRepo.existsByCompanyCodeAndId(eltCompanyDTO.getCompanyCode(), eltCompanyDTO.getId())) {
					String errorMessage = String.format("The CompanyCode: %s  already exists This Organization.",
							eltCompanyDTO.getCompanyCode());
					throw new ApplicationException(errorMessage);
				}
				eltCompanyVO.setCompanyCode(eltCompanyDTO.getCompanyCode());
			}

			if (!eltCompanyVO.getCompanyName().equalsIgnoreCase(eltCompanyDTO.getCompanyName())) {

				if (eltCompanyRepo.existsByCompanyNameAndId(eltCompanyDTO.getCompanyName(), eltCompanyDTO.getId())) {
					String errorMessage = String.format("The CompanyName: %s  already exists This Organization.",
							eltCompanyDTO.getCompanyName());
					throw new ApplicationException(errorMessage);
				}
				eltCompanyVO.setCompanyName(eltCompanyDTO.getCompanyName());
			}
			if (!eltCompanyVO.getEmail().equalsIgnoreCase(eltCompanyDTO.getEmail())) {

				if (eltCompanyRepo.existsByEmailAndId(eltCompanyDTO.getEmail(), eltCompanyDTO.getId())) {
					String errorMessage = String.format("The CompanyMail: %s  already exists This Organization.",
							eltCompanyDTO.getEmail());
					throw new ApplicationException(errorMessage);
				}

				eltCompanyVO.setEmail(eltCompanyDTO.getEmail());

			}
			
			if (!eltCompanyVO.getPhone().equalsIgnoreCase(eltCompanyDTO.getPhone())) {

			if (eltCompanyRepo.existsByPhoneAndId(eltCompanyDTO.getPhone(), eltCompanyDTO.getId())) {
				String errorMessage = String.format("The PhoneNo: %s  already exists This Organization.",
						eltCompanyDTO.getPhone());
				throw new ApplicationException(errorMessage);
			}

			eltCompanyVO.setPhone(eltCompanyDTO.getPhone());

			}
			
			if (!eltCompanyVO.getWebSite().equalsIgnoreCase(eltCompanyDTO.getWebSite())) {
			
			if (eltCompanyRepo.existsByWebSiteAndId(eltCompanyDTO.getWebSite(), eltCompanyDTO.getId())) {
				String errorMessage = String.format("The WebSite: %s  already exists This Organization.",
						eltCompanyDTO.getWebSite());
				throw new ApplicationException(errorMessage);
			}

			eltCompanyVO.setWebSite(eltCompanyDTO.getWebSite());
			}
			message = "Elt Company Updation Successfully";

		}
		eltCompanyVO = getEltCompanyVOFromEltCompanyDTO(eltCompanyVO, eltCompanyDTO);
		eltCompanyRepo.save(eltCompanyVO);

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("eltCompanyVO", eltCompanyVO);
		return response;

	}

	private EltCompanyVO getEltCompanyVOFromEltCompanyDTO(EltCompanyVO eltCompanyVO,
			@Valid EltCompanyDTO eltCompanyDTO) {

		eltCompanyVO.setCompanyCode(eltCompanyDTO.getCompanyCode());
		eltCompanyVO.setCompanyName(eltCompanyDTO.getCompanyName());
		eltCompanyVO.setEmail(eltCompanyDTO.getEmail());
		eltCompanyVO.setPhone(eltCompanyDTO.getPhone());
		eltCompanyVO.setWebSite(eltCompanyDTO.getWebSite());
		eltCompanyVO.setActive(eltCompanyDTO.isActive());
		return eltCompanyVO;
	}

	@Override
	public Optional<EltCompanyVO> getEltCompanyById(Long id) {

		return eltCompanyRepo.findEltCompanyById(id);
	}

	@Override
	public List<EltCompanyVO> getAllEltCompany() {

		return eltCompanyRepo.findAll();
	}

//CompanyEmployee

	@Override
	public Map<String, Object> updateCreateCompanyEmployee(@Valid CompanyEmployeeDTO companyEmployeeDTO)
	        throws ApplicationException {

	    CompanyEmployeeVO companyEmployeeVO;

	    String message = null;

	    if (ObjectUtils.isEmpty(companyEmployeeDTO.getId())) {
	        companyEmployeeVO = new CompanyEmployeeVO();

	        if (companyEmployeeRepo.existsByEmployeeCodeAndOrgId(companyEmployeeDTO.getEmployeeCode(),
	                companyEmployeeDTO.getOrgId())) {
	            String errorMessage = String.format("The EmployeeCode: %s already exists in this Organization.",
	                    companyEmployeeDTO.getEmployeeCode());
	            throw new ApplicationException(errorMessage);
	        }
	        companyEmployeeVO.setEmployeeCode(companyEmployeeDTO.getEmployeeCode());

	        if (companyEmployeeRepo.existsByEmailAndOrgId(companyEmployeeDTO.getEmail(),
	                companyEmployeeDTO.getOrgId())) {
	            String errorMessage = String.format("The CompanyMail: %s already exists in this Organization.",
	                    companyEmployeeDTO.getEmail());
	            throw new ApplicationException(errorMessage);
	        }
	        companyEmployeeVO.setEmail(companyEmployeeDTO.getEmail());
	        companyEmployeeVO.setCreatedBy(companyEmployeeDTO.getCreatedBy());
	        companyEmployeeVO.setUpdatedBy(companyEmployeeDTO.getCreatedBy());

	        message = "Company Employee Creation Successfully";
	    } else {
	        companyEmployeeVO = companyEmployeeRepo.findById(companyEmployeeDTO.getId()).orElseThrow(
	                () -> new ApplicationException("CompanyEmployeeVO not found with ID: " + companyEmployeeDTO.getId()));

	        companyEmployeeVO.setUpdatedBy(companyEmployeeDTO.getCreatedBy());

	        if (!companyEmployeeVO.getEmployeeCode().equalsIgnoreCase(companyEmployeeDTO.getEmployeeCode())) {
	            if (companyEmployeeRepo.existsByEmployeeCodeAndOrgId(companyEmployeeDTO.getEmployeeCode(),
	                    companyEmployeeDTO.getOrgId())) {
	                String errorMessage = String.format("The EmployeeCode: %s already exists in this Organization.",
	                        companyEmployeeDTO.getEmployeeCode());
	                throw new ApplicationException(errorMessage);
	            }
	            companyEmployeeVO.setEmployeeCode(companyEmployeeDTO.getEmployeeCode());
	        }

	        if (!companyEmployeeVO.getEmail().equalsIgnoreCase(companyEmployeeDTO.getEmail())) {
	            if (companyEmployeeRepo.existsByEmailAndOrgId(companyEmployeeDTO.getEmail(),
	                    companyEmployeeDTO.getOrgId())) {
	                String errorMessage = String.format("The CompanyMail: %s already exists in this Organization.",
	                        companyEmployeeDTO.getEmail());
	                throw new ApplicationException(errorMessage);
	            }
	            companyEmployeeVO.setEmail(companyEmployeeDTO.getEmail());
	            companyEmployeeVO.setUpdatedBy(companyEmployeeDTO.getCreatedBy());
	        }
	        message = "Company Employee Updation Successfully";
	    }

	    companyEmployeeVO = getCompanyEmployeeVOFromCompanyEmployeeDTO(companyEmployeeVO, companyEmployeeDTO);
	    companyEmployeeRepo.save(companyEmployeeVO);

	    Map<String, Object> response = new HashMap<>();
	    response.put("message", message);
	    response.put("companyEmployeeVO", companyEmployeeVO);
	    return response;
	}

	private CompanyEmployeeVO getCompanyEmployeeVOFromCompanyEmployeeDTO(CompanyEmployeeVO companyEmployeeVO,
	        @Valid CompanyEmployeeDTO companyEmployeeDTO) {

	    companyEmployeeVO.setEmployeeName(companyEmployeeDTO.getEmployeeName());
	    companyEmployeeVO.setPhone(companyEmployeeDTO.getPhone());
	    companyEmployeeVO.setOrgId(companyEmployeeDTO.getOrgId());
	    companyEmployeeVO.setActive(companyEmployeeDTO.isActive());
	    return companyEmployeeVO;
	}


	@Override
	public Optional<CompanyEmployeeVO> getCompanyEmployeeById(Long id) {
		return companyEmployeeRepo.findById(id);
	}

	@Override
	public List<CompanyEmployeeVO> getAllCompanyEmployeeByOrgId(Long orgId) {
		return companyEmployeeRepo.getAllCompanyEmployee(orgId);
	}


}
