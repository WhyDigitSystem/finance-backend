package com.base.basesetup.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.base.basesetup.dto.ClientCompanyDTO;
import com.base.basesetup.entity.ClientCompanyReportAccessVO;
import com.base.basesetup.entity.ClientCompanyVO;
import com.base.basesetup.entity.ClientSegmentVO;
import com.base.basesetup.entity.ClientUnitVO;
import com.base.basesetup.entity.UserVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.ClientCompanyRepo;
import com.base.basesetup.repo.ClientCompanyReportAccessRepo;
import com.base.basesetup.repo.ClientSegmentRepo;
import com.base.basesetup.repo.ClientUnitRepo;
import com.base.basesetup.util.CryptoUtils;

@Service
public class ClientCompanyServiceImpl implements ClientCompanyService{

	public static final Logger LOGGER = LoggerFactory.getLogger(ClientCompanyServiceImpl.class);

	@Autowired
	ClientCompanyRepo clientCompanyRepo;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	ClientCompanyReportAccessRepo clientCompanyReportAccessRepo;
	
	@Autowired
	ClientUnitRepo clientUnitRepo;
	
	@Autowired
	ClientSegmentRepo clientSegmentRepo;
	
	
	@Override
	public List<ClientCompanyVO> getClientCompanyByOrgId(Long orgId) {
		return clientCompanyRepo.findClientCompanyByOrgId(orgId);
	}


	@Override
	public Optional<ClientCompanyVO> getClientCompanyById(Long companyid) {
		return clientCompanyRepo.findByClientCompanyById(companyid);
	}
	
	@Override
	public Map<String, Object> updateCreateClientCompany(@Valid ClientCompanyDTO clientCompanyDTO) throws Exception {

		ClientCompanyVO clientCompanyVO;

		String message = null;
		
		int count= clientCompanyRepo.getClientCount(clientCompanyDTO.getOrgId());
		

		if (ObjectUtils.isEmpty(clientCompanyDTO.getId())) {

			clientCompanyVO = new ClientCompanyVO();

			if (clientCompanyRepo.existsByClientCodeAndOrgId(clientCompanyDTO.getClientCode(), clientCompanyDTO.getOrgId())) {
				String errorMessage = String.format("The ClientCode: %s  already exists This Organization.",
						clientCompanyDTO.getClientCode());
				throw new ApplicationException(errorMessage);
			}

			if (clientCompanyRepo.existsByClientNameAndOrgId(clientCompanyDTO.getClientName(), clientCompanyDTO.getOrgId())) {
				String errorMessage = String.format("The ClientName: %s  already exists This Organization.",
						clientCompanyDTO.getClientName());
				throw new ApplicationException(errorMessage);
			}

			if (clientCompanyRepo.existsByEmailAndOrgId(clientCompanyDTO.getEmail(), clientCompanyDTO.getOrgId())) {
				String errorMessage = String.format("The CompanyMail: %s  already exists This Organization.",
						clientCompanyDTO.getEmail());
				throw new ApplicationException(errorMessage);
			}

			if (clientCompanyRepo.existsByPhoneAndOrgId(clientCompanyDTO.getPhone(), clientCompanyDTO.getOrgId())) {
				String errorMessage = String.format("The PhoneNo: %s  already exists This Organization.",
						clientCompanyDTO.getPhone());
				throw new ApplicationException(errorMessage);
			}

			if (clientCompanyRepo.existsByWebSiteAndOrgId(clientCompanyDTO.getWebSite(), clientCompanyDTO.getOrgId())) {
				String errorMessage = String.format("The WebSite: %s  already exists This Organization.",
						clientCompanyDTO.getWebSite());
				throw new ApplicationException(errorMessage);
			}

			clientCompanyVO.setCreatedBy(clientCompanyDTO.getCreatedBy());

			clientCompanyVO.setUpdatedBy(clientCompanyDTO.getCreatedBy());

			message = "Client Company Successfully";
		} else {

			clientCompanyVO = clientCompanyRepo.findById(clientCompanyDTO.getId()).orElseThrow(
					() -> new ApplicationException("Elt CompanyVO  Not Found with id: " + clientCompanyDTO.getOrgId()));
			clientCompanyVO.setUpdatedBy(clientCompanyDTO.getCreatedBy());

			if (!clientCompanyVO.getClientCode().equalsIgnoreCase(clientCompanyDTO.getClientCode())) {
			if (clientCompanyRepo.existsByClientCodeAndOrgId(clientCompanyDTO.getClientCode(), clientCompanyDTO.getOrgId())) {
				String errorMessage = String.format("The ClientCode: %s  already exists This Organization.",
						clientCompanyDTO.getClientCode());
				throw new ApplicationException(errorMessage);
			}
			}
			if (!clientCompanyVO.getClientName().equalsIgnoreCase(clientCompanyDTO.getClientName())) {
			if (clientCompanyRepo.existsByClientNameAndOrgId(clientCompanyDTO.getClientName(), clientCompanyDTO.getOrgId())) {
				String errorMessage = String.format("The ClientName: %s  already exists This Organization.",
						clientCompanyDTO.getClientName());
				throw new ApplicationException(errorMessage);
			}}
			clientCompanyVO.setClientName(clientCompanyDTO.getClientName());

			if (!clientCompanyVO.getEmail().equalsIgnoreCase(clientCompanyDTO.getEmail())) {
			if (clientCompanyRepo.existsByEmailAndOrgId(clientCompanyDTO.getEmail(), clientCompanyDTO.getOrgId())) {
				String errorMessage = String.format("The CompanyMail: %s  already exists This Organization.",
						clientCompanyDTO.getEmail());
				throw new ApplicationException(errorMessage);
			}}

			clientCompanyVO.setEmail(clientCompanyDTO.getEmail());

			if (!clientCompanyVO.getPhone().equalsIgnoreCase(clientCompanyDTO.getPhone())) {
			if (clientCompanyRepo.existsByPhoneAndOrgId(clientCompanyDTO.getPhone(), clientCompanyDTO.getOrgId())) {
				String errorMessage = String.format("The PhoneNo: %s  already exists This Organization.",
						clientCompanyDTO.getPhone());
				throw new ApplicationException(errorMessage);
			}}

			clientCompanyVO.setPhone(clientCompanyDTO.getPhone());

			if (!clientCompanyVO.getWebSite().equalsIgnoreCase(clientCompanyDTO.getWebSite())) {
			if (clientCompanyRepo.existsByWebSiteAndOrgId(clientCompanyDTO.getWebSite(), clientCompanyDTO.getOrgId())) {
				String errorMessage = String.format("The WebSite: %s  already exists This Organization.",
						clientCompanyDTO.getWebSite());
				throw new ApplicationException(errorMessage);
			}}

			clientCompanyVO.setWebSite(clientCompanyDTO.getWebSite());

			message = "Client Company Updation Successfully";

		}
		ClientCompanyVO clientCompanyVOs = getClientCompanyVOFromClientCompanyDTO(clientCompanyVO, clientCompanyDTO);
		clientCompanyRepo.save(clientCompanyVOs);
		
		UserVO userVO= new UserVO();
		userVO.setOrgId(clientCompanyVOs.getOrgId());
		userVO.setActive(true);
		userVO.setClient(clientCompanyVOs.getClientName());
		userVO.setClientId(clientCompanyVOs.getId());
		userVO.setEmail(clientCompanyVOs.getUserName());
		userVO.setUserName(clientCompanyVOs.getUserName());
		userVO.setUserType("GUEST");
		userVO.setPassword(passwordEncoder.encode(CryptoUtils.getDecrypt(clientCompanyDTO.getPassword())));

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("clientCompanyVO", clientCompanyVOs);
		return response;

	}

	private ClientCompanyVO getClientCompanyVOFromClientCompanyDTO(ClientCompanyVO clientCompanyVO,
			@Valid ClientCompanyDTO clientCompanyDTO) {

		clientCompanyVO.setClientCode(clientCompanyDTO.getClientCode());
		clientCompanyVO.setClientName(clientCompanyDTO.getClientName());
		clientCompanyVO.setEmail(clientCompanyDTO.getEmail());
		clientCompanyVO.setPhone(clientCompanyDTO.getPhone());
		clientCompanyVO.setWebSite(clientCompanyDTO.getWebSite());
		clientCompanyVO.setOrgId(clientCompanyDTO.getOrgId());
		clientCompanyVO.setClientYear(clientCompanyDTO.getClientYear());
		clientCompanyVO.setActive(clientCompanyDTO.isActive());
		clientCompanyVO.setBussinessType(clientCompanyDTO.getBussinessType());
		clientCompanyVO.setLevelOfService(clientCompanyDTO.getLevelOfService());
		clientCompanyVO.setRepPerson(clientCompanyDTO.getRepPerson());
		clientCompanyVO.setTurnOver(clientCompanyDTO.getTurnOver());
		clientCompanyVO.setUserName(clientCompanyDTO.getUserName());
		clientCompanyVO.setPassword(clientCompanyDTO.getPassword());
		clientCompanyVO.setCurrency(clientCompanyDTO.getCurrency());
		clientCompanyVO.setYearStartDate(clientCompanyDTO.getYearStartDate());
		clientCompanyVO.setYearEndDate(clientCompanyDTO.getYearEndDate());
		
		
	 	
		if(ObjectUtils.isNotEmpty(clientCompanyDTO.getId()))
		{
			List<ClientCompanyReportAccessVO> clientCompanyReportAccessVO= clientCompanyReportAccessRepo.findByClientCompanyVO(clientCompanyVO);
			clientCompanyReportAccessRepo.deleteAll(clientCompanyReportAccessVO);
			
			List<ClientUnitVO>clientUnitVOs= clientUnitRepo.findByClientCompanyVO(clientCompanyVO);
			clientUnitRepo.deleteAll(clientUnitVOs);
			
			List<ClientSegmentVO>clientSegment= clientSegmentRepo.findByClientCompanyVO(clientCompanyVO);
			clientSegmentRepo.deleteAll(clientSegment);
		}
		
		if (!ObjectUtils.isEmpty(clientCompanyDTO.getClientCompanyReportAccessDTO())) {
			List<ClientCompanyReportAccessVO>accessList= clientCompanyDTO.getClientCompanyReportAccessDTO().stream()
					.map(accessDTO -> {
						ClientCompanyReportAccessVO clientCompanyReportAccessVOs= new ClientCompanyReportAccessVO();
						clientCompanyReportAccessVOs.setElCode(accessDTO.getElCode());
						clientCompanyReportAccessVOs.setDescription(accessDTO.getDescription());
						clientCompanyReportAccessVOs.setAccess(accessDTO.isAccess());
						clientCompanyReportAccessVOs.setClientCompanyVO(clientCompanyVO);
						return clientCompanyReportAccessVOs;
					}).collect(Collectors.toList());
			clientCompanyVO.setClientCompanyReportAccessVO(accessList);
		}	
		
		if (!ObjectUtils.isEmpty(clientCompanyDTO.getClientUnitDTO())) {
			List<ClientUnitVO>unitList= clientCompanyDTO.getClientUnitDTO().stream()
					.map(accessDTO -> {
						ClientUnitVO clientUnitVOs= new ClientUnitVO();
						clientUnitVOs.setUnit(accessDTO.getUnit());
						clientUnitVOs.setLocation(accessDTO.getLocation());
						clientUnitVOs.setActive(accessDTO.isActive());
						clientUnitVOs.setClientCompanyVO(clientCompanyVO);
						return clientUnitVOs;
					}).collect(Collectors.toList());
			clientCompanyVO.setClientUnitVO(unitList);
		}
		
		if (!ObjectUtils.isEmpty(clientCompanyDTO.getClientSegmentDTO())) {
			List<ClientSegmentVO>segmentList= clientCompanyDTO.getClientSegmentDTO().stream()
					.map(accessDTO -> {
						ClientSegmentVO clientSegmentVOs= new ClientSegmentVO();
						clientSegmentVOs.setUnit(accessDTO.getUnit());
						clientSegmentVOs.setSegment(accessDTO.getSegment());
						clientSegmentVOs.setActive(accessDTO.isActive());
						clientSegmentVOs.setClientCompanyVO(clientCompanyVO);
						return clientSegmentVOs;
					}).collect(Collectors.toList());
			clientCompanyVO.setClientSegmentVO(segmentList);
		}
		return clientCompanyVO;
	}

	
	
}
