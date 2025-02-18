package com.base.basesetup.service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.base.basesetup.dto.CustomerCurrencyMappingDTO;
import com.base.basesetup.dto.CustomerSalesPersonDTO;
import com.base.basesetup.dto.CustomersAddressDTO;
import com.base.basesetup.dto.CustomersDTO;
import com.base.basesetup.dto.CustomersStateDTO;
import com.base.basesetup.dto.PartyTypeDTO;
import com.base.basesetup.dto.VendorCurrencyMappingDTO;
import com.base.basesetup.entity.PartyAddressVO;
import com.base.basesetup.entity.PartyCurrencyMappingVO;
import com.base.basesetup.entity.PartyMasterVO;
import com.base.basesetup.entity.PartySalesPersonTaggingVO;
import com.base.basesetup.entity.PartySpecialTDSVO;
import com.base.basesetup.entity.PartyStateVO;
import com.base.basesetup.entity.PartyTypeVO;
import com.base.basesetup.entity.SpecialTdsDTO;
import com.base.basesetup.entity.VendorDTO;
import com.base.basesetup.entity.VendorsAddressDTO;
import com.base.basesetup.entity.VendorsStateDTO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.PartyAddressRepo;
import com.base.basesetup.repo.PartyCurrencyMappingRepo;
import com.base.basesetup.repo.PartyMasterRepo;
import com.base.basesetup.repo.PartySalesPersonTaggingRepo;
import com.base.basesetup.repo.PartySpecialTDSRepo;
import com.base.basesetup.repo.PartyStateRepo;
import com.base.basesetup.repo.PartyTypeRepo;

@Service
public class PartyTypeServiceImpl implements PartyTypeService {
	public static final Logger LOGGER = LoggerFactory.getLogger(MasterServiceImpl.class);

	@Autowired
	PartyTypeRepo partyTypeRepo;

	@Autowired
	PartyMasterRepo partyMasterRepo;

	@Autowired
	PartyAddressRepo partyAddressRepo;

	@Autowired
	PartyStateRepo partyStateRepo;

	@Autowired
	PartySpecialTDSRepo partySpecialTDSRepo;

	@Autowired
	PartySalesPersonTaggingRepo partySalesPersonTaggingRepo;

	@Autowired
	PartyCurrencyMappingRepo partyCurrencyMappingRepo;

	// PartyType

	@Override
	public PartyTypeVO createUpdatePartyType(@Valid PartyTypeDTO partyTypeDTO) throws ApplicationException {
		PartyTypeVO partyTypeVO = new PartyTypeVO();
		boolean isUpdate = false;
		if (ObjectUtils.isNotEmpty(partyTypeDTO.getId())) {
			isUpdate = true;
			partyTypeVO = partyTypeRepo.findById(partyTypeDTO.getId())
					.orElseThrow(() -> new ApplicationException("Invalid PartyType details"));
			partyTypeVO.setUpdatedBy(partyTypeDTO.getCreatedBy());
		} else {
			if (partyTypeRepo.existsByPartyTypeAndOrgId(partyTypeDTO.getPartyType(), partyTypeDTO.getOrgId())) {
				throw new ApplicationException("The given value PartyType already exists.");
			}
			if (partyTypeRepo.existsByPartyTypeCodeAndOrgId(partyTypeDTO.getPartyTypeCode(), partyTypeDTO.getOrgId())) {
				throw new ApplicationException("The given value PartyTypeCode already exists.");
			}
			partyTypeVO.setUpdatedBy(partyTypeDTO.getCreatedBy());
			partyTypeVO.setCreatedBy(partyTypeDTO.getCreatedBy());
		}
		if (isUpdate) {
			PartyTypeVO partyType = partyTypeRepo.findById(partyTypeDTO.getId()).orElse(null);
			if (!partyType.getPartyType().equalsIgnoreCase(partyTypeDTO.getPartyType())) {
				if (partyTypeRepo.existsByPartyTypeAndOrgId(partyTypeDTO.getPartyType(), partyTypeDTO.getOrgId())) {
					throw new ApplicationException("The given value PartyType already exists.");
				}
			}
			if (!partyType.getPartyTypeCode().equalsIgnoreCase(partyTypeDTO.getPartyTypeCode())) {
				if (partyTypeRepo.existsByPartyTypeCodeAndOrgId(partyTypeDTO.getPartyTypeCode(),
						partyTypeDTO.getOrgId())) {
					throw new ApplicationException("The given value PartyTypeCode already exists.");
				}
			}
		}
		getPartyTypeVOFromPartyTypeDTO(partyTypeDTO, partyTypeVO);
		return partyTypeRepo.save(partyTypeVO);
	}

	private void getPartyTypeVOFromPartyTypeDTO(@Valid PartyTypeDTO partyTypeDTO, PartyTypeVO partyTypeVO) {
//		partyTypeVO.setPartyType(partyTypeDTO.getPartyType());
		String partyType = partyTypeDTO.getPartyType();
		if (partyType != null) {
			partyTypeVO.setPartyType(partyType.toUpperCase()); // Set field to uppercase
		}
//		partyTypeVO.setPartyTypeCode(partyTypeDTO.getPartyTypeCode());
		String partyTypeCode = partyTypeDTO.getPartyTypeCode();
		if (partyTypeCode != null) {
			partyTypeVO.setPartyTypeCode(partyTypeCode.toUpperCase()); // Set field to uppercase
		}
		partyTypeVO.setCancel(partyTypeDTO.isCancel());
		partyTypeVO.setOrgId(partyTypeDTO.getOrgId());
		partyTypeVO.setActive(partyTypeDTO.isActive());
		partyTypeVO.setCancelRemarks(partyTypeDTO.getCancelRemarks());

	}

	@Override
	public List<PartyTypeVO> getPartyTypeById(Long id) {
		List<PartyTypeVO> partyTypeVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received  PartyType BY Id : {}", id);
			partyTypeVO = partyTypeRepo.findPartyTypeVOById(id);
		} else {
			LOGGER.info("Successfully Received  PartyType For All Id.");
			partyTypeVO = partyTypeRepo.findAll();
		}
		return partyTypeVO;
	}

	@Override
	public List<PartyTypeVO> getAllPartyTypeByOrgId(Long orgid) {
		List<PartyTypeVO> partyTypeVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(orgid)) {
			LOGGER.info("Successfully Received  PartyType BY OrgId : {}", orgid);
			partyTypeVO = partyTypeRepo.findAllPartyTypeVOByOrgId(orgid);
		} else {
			LOGGER.info("Successfully Received  PartyType For All OrgId.");
			partyTypeVO = partyTypeRepo.findAll();
		}
		return partyTypeVO;
	}

	@Override
	@Transactional
	public List<Map<String, Object>> getPartyCodeByOrgIdAndPartyType(Long orgid, String partytype) {

		Set<Object[]> result = partyTypeRepo.findPartyCodeByOrgIdAndPartyType(orgid, partytype);
		return getPartyCodeByOrgIdAndPartyType(result);
	}

	private List<Map<String, Object>> getPartyCodeByOrgIdAndPartyType(Set<Object[]> result) {
		List<Map<String, Object>> details1 = new ArrayList<>();
		for (Object[] fs : result) {
			Map<String, Object> part = new HashMap<>();
			part.put("partCode", fs[0] != null ? fs[0].toString() : "");
			details1.add(part);
		}
		return details1;
	}

	@Override
	@Transactional
	public void uploadCustomerData(MultipartFile file, Long orgId, String createdBy) throws Exception {
		List<CustomersDTO> customersDTOList = new ArrayList<>();

		try (InputStream inputStream = file.getInputStream(); Workbook workbook = WorkbookFactory.create(inputStream)) {
			processCustomerSheet(workbook.getSheetAt(0), customersDTOList, orgId, createdBy);
			processStateSheet(workbook.getSheetAt(1), customersDTOList);
			processAddressSheet(workbook.getSheetAt(2), customersDTOList);
			processSalesPersonSheet(workbook.getSheetAt(3), customersDTOList);
			processCurrencySheet(workbook.getSheetAt(4), customersDTOList);

			for (CustomersDTO customer : customersDTOList) {
				createUpdateCustomer(customer);
			}
		}
	}

	private void processCustomerSheet(Sheet sheet, List<CustomersDTO> customersDTOList, Long orgId, String createdBy) {
		for (Row row : sheet) {
			if (row.getRowNum() == 0)
				continue;
			CustomersDTO customer = new CustomersDTO();
			customer.setCustomerName(getStringCellValue(row.getCell(0)));
//			customer.setCustomerCode(getStringCellValue(row.getCell(1)));
			customer.setGstIn(getStringCellValue(row.getCell(1)));
			customer.setPanNo(getStringCellValue(row.getCell(2)));
			customer.setCreditLimit(getBigDecimalValue(row.getCell(3)));
			Long creditDays = getLongCellValue(row.getCell(4));
			customer.setCreditDays(creditDays != null ? creditDays : 0);
			customer.setCreditTerms(getStringCellValue(row.getCell(5)));
			customer.setTaxRegistered(getStringCellValue(row.getCell(6)));
			customer.setBussinessType(getStringCellValue(row.getCell(7)));
			customer.setBussinessCategory(getStringCellValue(row.getCell(8)));
			customer.setAccountsType(getStringCellValue(row.getCell(9)));
			customer.setCurrency(getStringCellValue(row.getCell(10)));
			customer.setCreatedBy(createdBy);
			customer.setOrgId(orgId);
			customer.setActive(true);
			customersDTOList.add(customer);
		}
	}

	private void processStateSheet(Sheet sheet, List<CustomersDTO> customersDTOList) {
		for (Row row : sheet) {
			if (row.getRowNum() == 0)
				continue;
			String customerName = getStringCellValue(row.getCell(7));
			customersDTOList.stream().filter(c -> c.getCustomerName().equals(customerName)).findFirst()
					.ifPresent(customer -> {
						CustomersStateDTO state = new CustomersStateDTO();
						state.setState(getStringCellValue(row.getCell(0)));
						state.setStateCode(getStringCellValue(row.getCell(1)));
						state.setStateNo(getLongCellValue(row.getCell(2)));
						state.setGstIn(getStringCellValue(row.getCell(3)));
						state.setContactPerson(getStringCellValue(row.getCell(4)));
						state.setPhoneNo(getStringCellValue(row.getCell(5)));
						state.setEMail(getStringCellValue(row.getCell(6)));
						customer.getCustomersStateDTO().add(state);
					});
		}
	}

	private void processAddressSheet(Sheet sheet, List<CustomersDTO> customersDTOList) {
		for (Row row : sheet) {
			if (row.getRowNum() == 0)
				continue;
			String customerName = getStringCellValue(row.getCell(10));
			customersDTOList.stream().filter(c -> c.getCustomerName().equals(customerName)).findFirst()
					.ifPresent(customer -> {
						CustomersAddressDTO address = new CustomersAddressDTO();
						address.setState(getStringCellValue(row.getCell(0)));
						address.setCity(getStringCellValue(row.getCell(1)));
						address.setBussinesPlace(getStringCellValue(row.getCell(3)));
						address.setGstnIn(getStringCellValue(row.getCell(2)));
						address.setAddressType(getStringCellValue(row.getCell(4)));
						address.setAddressLane1(getStringCellValue(row.getCell(5)));
						address.setAddressLane2(getStringCellValue(row.getCell(6)));
						address.setAddressLane3(getStringCellValue(row.getCell(7)));
						address.setPinCode(getLongCellValue(row.getCell(8)));
						String no = getStringCellValue(row.getCell(9));
						System.out.println(no);
						address.setContact(getStringCellValue(row.getCell(9)));
						customer.getCustomersAddressDTO().add(address);
					});
		}
	}

	private void processSalesPersonSheet(Sheet sheet, List<CustomersDTO> customersDTOList) {
		for (Row row : sheet) {
			if (row.getRowNum() == 0)
				continue;
			String customerName = getStringCellValue(row.getCell(5));
			customersDTOList.stream().filter(c -> c.getCustomerName().equals(customerName)).findFirst()
					.ifPresent(customer -> {
						CustomerSalesPersonDTO salesPerson = new CustomerSalesPersonDTO();
						salesPerson.setSalesPerson(getStringCellValue(row.getCell(0)));
						salesPerson.setEmpCode(getStringCellValue(row.getCell(1)));
						salesPerson.setSalesBranch(getStringCellValue(row.getCell(2)));
						salesPerson.setEffectiveFrom(getLocalDateCellValue(row.getCell(3)));
						salesPerson.setEffectiveTill(getLocalDateCellValue(row.getCell(4)));
						customer.getCustomerSalesPersonDTO().add(salesPerson);
					});
		}
	}

	private void processCurrencySheet(Sheet sheet, List<CustomersDTO> customersDTOList) {
		for (Row row : sheet) {
			if (row.getRowNum() == 0)
				continue;
			String customerName = getStringCellValue(row.getCell(1));
			customersDTOList.stream().filter(c -> c.getCustomerName().equals(customerName)).findFirst()
					.ifPresent(customer -> {
						CustomerCurrencyMappingDTO currency = new CustomerCurrencyMappingDTO();
						currency.setTransCurrency(getStringCellValue(row.getCell(0)));
						customer.getCustomerCurrencyMappingDTO().add(currency);
					});
		}
	}

//	private String getStringCellValue(Cell cell) {
//		return (cell == null) ? "" : cell.toString().trim();
//	}

	private String getStringCellValue(Cell cell) {
	    if (cell == null) return "";

	    switch (cell.getCellType()) {
	        case STRING:
	            return cell.getStringCellValue().trim();
	        case NUMERIC:
	            double numericValue = cell.getNumericCellValue();
	            if (numericValue == Math.floor(numericValue)) {
	                // If it's a whole number, convert without decimal point
	                return String.valueOf((long) numericValue);
	            }
	            return String.valueOf(numericValue);
	        default:
	            return "";
	    }
	}

	private Long getLongCellValue(Cell cell) {
		return (cell == null || cell.getCellType() != CellType.NUMERIC) ? null : (long) cell.getNumericCellValue();
	}

	private BigDecimal getBigDecimalValue(Cell cell) {
		return (cell == null || cell.getCellType() == CellType.BLANK) ? BigDecimal.ZERO
				: (cell.getCellType() == CellType.NUMERIC) ? BigDecimal.valueOf(cell.getNumericCellValue())
						: new BigDecimal(cell.getStringCellValue().trim());
	}

	private LocalDate getLocalDateCellValue(Cell cell) {
		return (cell == null || !DateUtil.isCellDateFormatted(cell)) ? null
				: cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	@Override
	public Map<String, Object> createUpdateCustomer(@Valid CustomersDTO customersDTO) throws ApplicationException {

		PartyMasterVO partyMasterVO = new PartyMasterVO();
		String message = null;

		if (ObjectUtils.isEmpty(customersDTO.getId())) {
			String partyType = "CUSTOMER";
			if (partyMasterRepo.existsByPartyNameAndOrgIdAndPartyType(customersDTO.getCustomerName(), customersDTO.getOrgId(),partyType)) {
				String errorMessage = String.format("This CustomerName: %s Already Exists in This Organization",
						customersDTO.getCustomerName());
				throw new ApplicationException(errorMessage);
			}

		
			// PARTCODE DOCID API
			String partyTypeDocId = partyTypeRepo.getPartyTypeDocId(customersDTO.getOrgId(), partyType);
			partyMasterVO.setPartyCode(partyTypeDocId);

			// UPDATE PARTCODE DOCID LASTNO +1
			PartyTypeVO partyTypeVO = partyTypeRepo.findByOrgIdAndPartyType(customersDTO.getOrgId(), partyType);
			partyTypeVO.setLastNo(partyTypeVO.getLastNo() + 1);
			partyTypeRepo.save(partyTypeVO);

			partyMasterVO.setCreatedBy(customersDTO.getCreatedBy());
			partyMasterVO.setUpdatedBy(customersDTO.getCreatedBy());

			message = "Customers creation Successfully";
		} else {

			partyMasterVO = partyMasterRepo.findById(customersDTO.getId())
					.orElseThrow(() -> new ApplicationException("Invalid PartyMaster Details"));
			partyMasterVO.setUpdatedBy(customersDTO.getCreatedBy());

			if (!partyMasterVO.getPartyName().equalsIgnoreCase(customersDTO.getCustomerName())) {
				String partyType = "CUSTOMER";
				if (partyMasterRepo.existsByPartyNameAndOrgIdAndPartyType(customersDTO.getCustomerName(),
						customersDTO.getOrgId(),partyType)) {
					String errorMessage = String.format("This CustomerName: %s Already Exists in This Organization",
							customersDTO.getCustomerName());
					throw new ApplicationException(errorMessage);
				}
				partyMasterVO.setPartyName(customersDTO.getCustomerName());
			}

			message = "Customer Updation Successfully";
		}
		partyMasterVO = partyMasterRepo.save(partyMasterVO);
		if (ObjectUtils.isNotEmpty(customersDTO.getId())) {
			List<PartyStateVO> partyStateVOList = partyStateRepo.findByPartyMasterVO(partyMasterVO);
			partyStateRepo.deleteAll(partyStateVOList);
		}
		List<PartyStateVO> partyStateVOs = new ArrayList<>();
		for (CustomersStateDTO partyStateDTO : customersDTO.getCustomersStateDTO()) {
			PartyStateVO partyStateVO = new PartyStateVO();
			partyStateVO.setState(partyStateDTO.getState());
			partyStateVO.setGstIn(partyStateDTO.getGstIn());
			partyStateVO.setStateNo(partyStateDTO.getStateNo());
			partyStateVO.setContactPerson(partyStateDTO.getContactPerson());
			partyStateVO.setStateCode(partyStateDTO.getStateCode());
			partyStateVO.setEmail(partyStateDTO.getEMail());
			partyStateVO.setContactPhoneNo(partyStateDTO.getPhoneNo());
			// partyStateVO.setPartyName(partyStateDTO.getCustomerName());
			partyStateVO.setPartyMasterVO(partyMasterVO);
			partyStateVOs.add(partyStateVO);
		}

		if (ObjectUtils.isNotEmpty(customersDTO.getId())) {
			List<PartyAddressVO> partyAddressVOList = partyAddressRepo.findByPartyMasterVO(partyMasterVO);
			partyAddressRepo.deleteAll(partyAddressVOList);
		}
		List<PartyAddressVO> partyAddressVOs = new ArrayList<>();
		for (CustomersAddressDTO partyAddressDTO : customersDTO.getCustomersAddressDTO()) {
			PartyAddressVO partyAddressVO = new PartyAddressVO();
			partyAddressVO.setState(partyAddressDTO.getState());
			partyAddressVO.setStateGstIn(partyAddressDTO.getGstnIn());
			partyAddressVO.setBusinessPlace(partyAddressDTO.getBussinesPlace());
			partyAddressVO.setCity(partyAddressDTO.getCity()); // Changed from cityName to city
			partyAddressVO.setAddressType(partyAddressDTO.getAddressType());
			partyAddressVO.setAddressLine1(partyAddressDTO.getAddressLane1());
			partyAddressVO.setAddressLine2(partyAddressDTO.getAddressLane2());
			partyAddressVO.setAddressLine3(partyAddressDTO.getAddressLane3());
			partyAddressVO.setPincode(partyAddressDTO.getPinCode());
			partyAddressVO.setContact(partyAddressDTO.getContact());
//			partyAddressVO.setSez(partyAddressDTO.isSez());
//			partyAddressVO.setContactPerson(partyAddressDTO.getContactPerson());
//			partyAddressVO.setContactNo(partyAddressDTO.getContactNo());
			// Changed from contactPerson to contact
			// partyAddressVO.setPartyName(partyAddressDTO.getCustomerName());
			partyAddressVO.setPartyMasterVO(partyMasterVO);
			partyAddressVOs.add(partyAddressVO);
		}

		if (ObjectUtils.isNotEmpty(customersDTO.getId())) {
			List<PartyCurrencyMappingVO> partyCurrencyMappingVO1 = partyCurrencyMappingRepo
					.findByPartyMasterVO(partyMasterVO);
			partyCurrencyMappingRepo.deleteAll(partyCurrencyMappingVO1);
		}
		List<PartyCurrencyMappingVO> partyCurrencyMappingVOs = new ArrayList<>();
		for (CustomerCurrencyMappingDTO customerCurrencyMappingDTO : customersDTO.getCustomerCurrencyMappingDTO()) {
			PartyCurrencyMappingVO partyCurrencyMappingVO = new PartyCurrencyMappingVO();
			partyCurrencyMappingVO.setTransCurrency(customerCurrencyMappingDTO.getTransCurrency());
			partyCurrencyMappingVO.setPartyMasterVO(partyMasterVO);
			partyCurrencyMappingVOs.add(partyCurrencyMappingVO);

		}
		if (ObjectUtils.isNotEmpty(customersDTO.getId())) {
			List<PartySalesPersonTaggingVO> partySalesPersonTaggingVOList = partySalesPersonTaggingRepo
					.findByPartyMasterVO(partyMasterVO);
			partySalesPersonTaggingRepo.deleteAll(partySalesPersonTaggingVOList);
		}
		List<PartySalesPersonTaggingVO> partySalesPersonTaggingVOs = new ArrayList<>();
		for (CustomerSalesPersonDTO partySalesPersonTaggingDTO : customersDTO.getCustomerSalesPersonDTO()) {
			PartySalesPersonTaggingVO partySalesPersonTaggingVO = new PartySalesPersonTaggingVO();
			partySalesPersonTaggingVO.setSalesPerson(partySalesPersonTaggingDTO.getSalesPerson());
			partySalesPersonTaggingVO.setEmpCode(partySalesPersonTaggingDTO.getEmpCode());
			partySalesPersonTaggingVO.setSalesBranch(partySalesPersonTaggingDTO.getSalesBranch());
			partySalesPersonTaggingVO.setEffectiveFrom(partySalesPersonTaggingDTO.getEffectiveFrom());
			partySalesPersonTaggingVO.setEffectiveTill(partySalesPersonTaggingDTO.getEffectiveTill());
			// partySalesPersonTaggingVO.setPartyName(partySalesPersonTaggingDTO.getCustomerName());
			partySalesPersonTaggingVO.setPartyMasterVO(partyMasterVO);
			partySalesPersonTaggingVOs.add(partySalesPersonTaggingVO);

		}

		getPartyMasterVOFromCustomersDTO(customersDTO, partyMasterVO);
		partyMasterVO.setPartyStateVO(partyStateVOs);
		partyMasterVO.setPartyAddressVO(partyAddressVOs);
		partyMasterVO.setPartyCurrencyMappingVO(partyCurrencyMappingVOs);
		partyMasterVO.setPartySalesPersonTaggingVO(partySalesPersonTaggingVOs);

		partyMasterRepo.save(partyMasterVO);
		Map<String, Object> response = new HashMap<>();
		response.put("partyMasterVO", partyMasterVO);
		response.put("message", message);
		return response;
	}

	private void getPartyMasterVOFromCustomersDTO(@Valid CustomersDTO customerDTO, PartyMasterVO partyMasterVO) {
		partyMasterVO.setPartyType("CUSTOMER");
		partyMasterVO.setPartyName(customerDTO.getCustomerName());
		partyMasterVO.setGstPartyName(customerDTO.getCustomerName());
		partyMasterVO.setGstIn(customerDTO.getGstIn());
		partyMasterVO.setPanNo(customerDTO.getPanNo());
		partyMasterVO.setPanName(customerDTO.getCustomerName());
		partyMasterVO.setActive(customerDTO.isActive());
		partyMasterVO.setOrgId(customerDTO.getOrgId());
		Long creditDays = customerDTO.getCreditDays();
		partyMasterVO.setCreditDays(creditDays != null ? creditDays : 0);
//		partyMasterVO.setCreditDays(customerDTO.getCreditDays());
		partyMasterVO.setCreditLimit(customerDTO.getCreditLimit());
		partyMasterVO.setCreditTerms(customerDTO.getCreditTerms());
		partyMasterVO.setGstRegistered(customerDTO.getTaxRegistered());
		partyMasterVO.setBussinessType(customerDTO.getBussinessType());
		partyMasterVO.setBussinessCate(customerDTO.getBussinessCategory());
		partyMasterVO.setAccountType(customerDTO.getAccountsType());
//		partyMasterVO.setPartyCode(customerDTO.getCustomerCode());
		partyMasterVO.setCurrency(customerDTO.getCurrency());

		if (customerDTO.isApproved()) {
			partyMasterVO.setActive(true);
		}

	}

	@Override
	public Optional<PartyMasterVO> getCustomersById(Long id) {
		return partyMasterRepo.findById(id);
	}

	@Override
	public List<PartyMasterVO> getAllCustomers(Long orgId) {
		return partyMasterRepo.getAllCustomers(orgId);
	}

	@Override
	public Map<String, Object> createUpdateVendor(@Valid VendorDTO vendorDTO) throws ApplicationException {
		PartyMasterVO partyMasterVO = new PartyMasterVO();
		String message = null;

		if (ObjectUtils.isEmpty(vendorDTO.getId())) {
			String partyType = "VENDOR";
			if (partyMasterRepo.existsByPartyNameAndOrgIdAndPartyType(vendorDTO.getVendorName(), vendorDTO.getOrgId(),partyType)) {
				String errorMessage = String.format("This CustomerName: %s Already Exists in This Organization",
						vendorDTO.getVendorName());
				throw new ApplicationException(errorMessage);
			}

			
			// PARTCODE DOCID API
			String partyTypeDocId = partyTypeRepo.getPartyTypeVendorDocId(vendorDTO.getOrgId(), partyType);
			partyMasterVO.setPartyCode(partyTypeDocId);

			// UPDATE PARTCODE DOCID LASTNO +1
			PartyTypeVO partyTypeVO = partyTypeRepo.findByOrgIdAndPartyType(vendorDTO.getOrgId(), partyType);
			partyTypeVO.setLastNo(partyTypeVO.getLastNo() + 1);
			partyTypeRepo.save(partyTypeVO);

			partyMasterVO.setCreatedBy(vendorDTO.getCreatedBy());
			partyMasterVO.setUpdatedBy(vendorDTO.getCreatedBy());

			message = "vendor Creation Successfully";
		} else {
			partyMasterVO = partyMasterRepo.findById(vendorDTO.getId()).orElseThrow(
					() -> new ApplicationException("vendor Order Not Found with id: " + vendorDTO.getId()));

			if (!partyMasterVO.getPartyName().equalsIgnoreCase(vendorDTO.getVendorName())) {
				String partyType = "VENDOR";
				if (partyMasterRepo.existsByPartyNameAndOrgIdAndPartyType(vendorDTO.getVendorName(), vendorDTO.getOrgId(),partyType)) {
					String errorMessage = String.format("This VendorName: %s Already Exists in This Organization",
							vendorDTO.getVendorName());
					throw new ApplicationException(errorMessage);
				}
				partyMasterVO.setPartyName(vendorDTO.getVendorName());
			}

			partyMasterVO.setUpdatedBy(vendorDTO.getCreatedBy());

			message = "vendor  Updation Successfully";
		}

		partyMasterVO = getpartyMasterVOFromVendorDTO(partyMasterVO, vendorDTO);
		partyMasterRepo.save(partyMasterVO);

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("partyMasterVO", partyMasterVO);
		return response;
	}

	private PartyMasterVO getpartyMasterVOFromVendorDTO(PartyMasterVO partyMasterVO, @Valid VendorDTO vendorDTO) {

		partyMasterVO.setPartyName(vendorDTO.getVendorName());
		partyMasterVO.setGstIn(vendorDTO.getGstIn());
		partyMasterVO.setPanNo(vendorDTO.getPanNo());
		partyMasterVO.setOrgId(vendorDTO.getOrgId());
		partyMasterVO.setPartyType("VENDOR");
		Long creditDays = vendorDTO.getCreditDays();
		partyMasterVO.setCreditDays(creditDays != null ? creditDays : 0);
		partyMasterVO.setCreditLimit(vendorDTO.getCreditLimit());
		partyMasterVO.setCreditTerms(vendorDTO.getCreditTerms());
		partyMasterVO.setGstRegistered(vendorDTO.getTaxRegistered());
		partyMasterVO.setBussinessType(vendorDTO.getBussinessType());
		partyMasterVO.setBussinessCate(vendorDTO.getBussinessCategory());
		partyMasterVO.setAccountType(vendorDTO.getAccountsType());

		if (vendorDTO.isApproved()) {
			partyMasterVO.setActive(true);
		}

		if (vendorDTO.getId() != null) {
			// Clear previous items from the database
			List<PartyStateVO> partyStateVOs = partyStateRepo.findByPartyMasterVO(partyMasterVO);
			partyStateRepo.deleteAll(partyStateVOs);

			List<PartyAddressVO> partyAddressVOs = partyAddressRepo.findByPartyMasterVO(partyMasterVO);
			partyAddressRepo.deleteAll(partyAddressVOs);

			List<PartySpecialTDSVO> partySpecialTDSVO1 = partySpecialTDSRepo.findByPartyMasterVO(partyMasterVO);
			partySpecialTDSRepo.deleteAll(partySpecialTDSVO1);
			
			List<PartyCurrencyMappingVO> partyCurrencyMappingVO1 = partyCurrencyMappingRepo
					.findByPartyMasterVO(partyMasterVO);
			partyCurrencyMappingRepo.deleteAll(partyCurrencyMappingVO1);

		}

		List<PartyStateVO> stateVOs = new ArrayList<>();
		for (VendorsStateDTO vendorsStateDTO : vendorDTO.getVendorStateDTO()) {

			PartyStateVO partyStateVO = new PartyStateVO();

			partyStateVO.setState(vendorsStateDTO.getState());
			partyStateVO.setStateCode(vendorsStateDTO.getStateCode());
			partyStateVO.setStateNo(vendorsStateDTO.getStateNo());
			partyStateVO.setGstIn(vendorsStateDTO.getGstIn());
			partyStateVO.setContactPerson(vendorsStateDTO.getContactPerson());
			partyStateVO.setContactPhoneNo(vendorsStateDTO.getPhoneNo());
			partyStateVO.setEmail(vendorsStateDTO.getEMail());
			partyStateVO.setPartyMasterVO(partyMasterVO);
			// Set the SalesVO reference
			stateVOs.add(partyStateVO);

			// Add the SalesItemParticularsVO to the list
			partyMasterVO.setPartyStateVO(stateVOs);
		}

		List<PartyAddressVO> addressVOs = new ArrayList<>();
		for (VendorsAddressDTO vendorsAddressDTO : vendorDTO.getVendorAddressDTO()) {
			PartyAddressVO partyAddressVO = new PartyAddressVO();

			partyAddressVO.setState(vendorsAddressDTO.getState());
			partyAddressVO.setCity(vendorsAddressDTO.getCity());
			partyAddressVO.setBusinessPlace(vendorsAddressDTO.getBussinesPlace());
			partyAddressVO.setStateGstIn(vendorsAddressDTO.getGstnIn());
			partyAddressVO.setAddressType(vendorsAddressDTO.getAddressType());
			partyAddressVO.setAddressLine1(vendorsAddressDTO.getAddressLane1());
			partyAddressVO.setAddressLine2(vendorsAddressDTO.getAddressLane2());
			partyAddressVO.setAddressLine3(vendorsAddressDTO.getAddressLane3());
			partyAddressVO.setPincode(vendorsAddressDTO.getPinCode());
			partyAddressVO.setContact(vendorsAddressDTO.getContact());
//			partyAddressVO.setSez(vendorsAddressDTO.isSez());
//			partyAddressVO.setContactPerson(vendorsAddressDTO.getContactPerson());
//			partyAddressVO.setContactNo(vendorsAddressDTO.getContactNo());
			partyAddressVO.setPartyMasterVO(partyMasterVO);
			// Set the SalesVO reference
			addressVOs.add(partyAddressVO);

			// Add the SalesItemParticularsVO to the list
			partyMasterVO.setPartyAddressVO(addressVOs);
		}

		List<PartySpecialTDSVO> specialTDSVOs = new ArrayList<>();
		for (SpecialTdsDTO specialTdsDTO : vendorDTO.getSpecialTdsDTO()) {

			PartySpecialTDSVO PartySpecialTDSVO = new PartySpecialTDSVO();
			PartySpecialTDSVO.setTdsWithSec(specialTdsDTO.getWhSection());
			PartySpecialTDSVO.setSection(specialTdsDTO.getSection());
			PartySpecialTDSVO.setRateFrom(specialTdsDTO.getRateFrom());
			PartySpecialTDSVO.setRateTo(specialTdsDTO.getRateTo());
			PartySpecialTDSVO.setTdsWithPer(specialTdsDTO.getWhPercentage());
			PartySpecialTDSVO.setSurchargePer(specialTdsDTO.getSurPercentage());
			PartySpecialTDSVO.setEdPercentage(specialTdsDTO.getEdPercentage());
			PartySpecialTDSVO.setTdsCertifiNo(specialTdsDTO.getTdsCertificateNo());

			PartySpecialTDSVO.setPartyMasterVO(partyMasterVO);
			// Set the SalesVO reference
			specialTDSVOs.add(PartySpecialTDSVO);

			// Add the SalesItemParticularsVO to the list
			partyMasterVO.setPartySpecialTDSVO(specialTDSVOs);
		}
	
		
		List<PartyCurrencyMappingVO> partyCurrencyMappingVOs = new ArrayList<>();
		for (VendorCurrencyMappingDTO vendorCurrencyMappingDTO : vendorDTO.getVendorCurrencyMappingDTO()) {
			PartyCurrencyMappingVO partyCurrencyMappingVO = new PartyCurrencyMappingVO();
			partyCurrencyMappingVO.setTransCurrency(vendorCurrencyMappingDTO.getTransCurrency());
			partyCurrencyMappingVO.setPartyMasterVO(partyMasterVO);
			partyCurrencyMappingVOs.add(partyCurrencyMappingVO);
			  partyMasterVO.setPartyCurrencyMappingVO(partyCurrencyMappingVOs);
		}	
      
		return partyMasterVO;
	}

	@Override
	public Optional<PartyMasterVO> getVendorsById(Long id) {
		// TODO Auto-generated method stub
		return partyMasterRepo.findById(id);
	}

	@Override
	public List<PartyMasterVO> getAllVendors(Long orgId) {
		return partyMasterRepo.getAllVendors(orgId);
	}



	@Override
	public List<Map<String, Object>> getSectionNameFromTds(Long orgId, String section) {
		Set<Object[]> chType = partyTypeRepo.getSectionNameFromTds(orgId, section);
		return getSectionName(chType);
	}

	public List<Map<String, Object>> getSectionName(Set<Object[]> chType) {
		List<Map<String, Object>> list1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();
			map.put("sectionName", ch[0] != null ? ch[0].toString() : "");
			list1.add(map);
		}
		return list1;
	}

//Vendor file upload

	@Override
	@Transactional
	public void vendorUpload(MultipartFile files, Long orgId, String createdBy) throws Exception {
		List<VendorDTO> vendorDTOList = new ArrayList<>();

		try (InputStream inputStream = files.getInputStream(); Workbook workbook = WorkbookFactory.create(inputStream)) {

			processVendorSheet(workbook.getSheetAt(0), vendorDTOList, orgId, createdBy);
			processStateSheets(workbook.getSheetAt(1), vendorDTOList);
			processAddressSheets(workbook.getSheetAt(2), vendorDTOList);
			processSpecialTds(workbook.getSheetAt(3), vendorDTOList);

			for (VendorDTO vendor : vendorDTOList) {
				createUpdateVendor(vendor);
			}
		}
	}

	private void processVendorSheet(Sheet sheet, List<VendorDTO> vendorDTOList, Long orgId, String createdBy) {
		for (Row row : sheet) {
			if (row.getRowNum() == 0)
				continue;
			VendorDTO vendor = new VendorDTO();

			vendor.setVendorName(getStringCellValue(row.getCell(0)));
			vendor.setGstIn(getStringCellValue(row.getCell(1)));
			vendor.setPanNo(getStringCellValue(row.getCell(2)));
			vendor.setCreditLimit(getBigDecimalValue(row.getCell(3)));
			Long creditDays = getLongCellValue(row.getCell(4));
			vendor.setCreditDays(creditDays != null ? creditDays : 0);
			vendor.setCreditTerms(getStringCellValue(row.getCell(5)));
			vendor.setTaxRegistered(getStringCellValue(row.getCell(6)));
			vendor.setBussinessType(getStringCellValue(row.getCell(7)));
			vendor.setBussinessCategory(getStringCellValue(row.getCell(8)));
			vendor.setAccountsType(getStringCellValue(row.getCell(9)));
			vendor.setCreatedBy(createdBy);
			vendor.setOrgId(orgId);
			vendor.setActive(true);

			vendorDTOList.add(vendor);
		}
	}

	private void processStateSheets(Sheet sheet, List<VendorDTO> vendorDTOList) {
		for (Row row : sheet) {
			if (row.getRowNum() == 0)
				continue;
			String vendorName = getStringCellValue(row.getCell(7));

			vendorDTOList.stream().filter(v -> v.getVendorName().equals(vendorName)).findFirst().ifPresent(vendor -> {
				if (vendor.getVendorStateDTO() == null) {
					vendor.setVendorStateDTO(new ArrayList<>());
				}
				VendorsStateDTO state = new VendorsStateDTO();
				state.setState(getStringCellValue(row.getCell(0)));
				state.setStateCode(getStringCellValue(row.getCell(1)));
				state.setStateNo(getLongCellValue(row.getCell(2)));
				state.setGstIn(getStringCellValue(row.getCell(3)));
				state.setContactPerson(getStringCellValue(row.getCell(4)));
				state.setPhoneNo(getStringCellValue(row.getCell(5)));
				state.setEMail(getStringCellValue(row.getCell(6)));

				vendor.getVendorStateDTO().add(state);
			});
		}
	}

	private void processAddressSheets(Sheet sheet, List<VendorDTO> vendorDTOList) {
		for (Row row : sheet) {
			if (row.getRowNum() == 0)
				continue;
			String vendorName = getStringCellValue(row.getCell(10));

			vendorDTOList.stream().filter(v -> v.getVendorName().equals(vendorName)).findFirst().ifPresent(vendor -> {
				if (vendor.getVendorAddressDTO() == null) {
					vendor.setVendorAddressDTO(new ArrayList<>());
				}
				VendorsAddressDTO address = new VendorsAddressDTO();
				address.setState(getStringCellValue(row.getCell(0)));
				address.setCity(getStringCellValue(row.getCell(1)));
				address.setBussinesPlace(getStringCellValue(row.getCell(3)));
				address.setGstnIn(getStringCellValue(row.getCell(2)));
				address.setAddressType(getStringCellValue(row.getCell(4)));
				address.setAddressLane1(getStringCellValue(row.getCell(5)));
				address.setAddressLane2(getStringCellValue(row.getCell(6)));
				address.setAddressLane3(getStringCellValue(row.getCell(7)));
				address.setPinCode(getLongCellValue(row.getCell(8)));
				address.setContact(getStringCellValue(row.getCell(9)));

				vendor.getVendorAddressDTO().add(address);
			});
		}
	}

	private void processSpecialTds(Sheet sheet, List<VendorDTO> vendorDTOList) {
		for (Row row : sheet) {
			if (row.getRowNum() == 0)
				continue;
			String vendorName = getStringCellValue(row.getCell(8));

			vendorDTOList.stream().filter(v -> v.getVendorName().equals(vendorName)).findFirst().ifPresent(vendor -> {
				if (vendor.getSpecialTdsDTO() == null) {
					vendor.setSpecialTdsDTO(new ArrayList<>());
				}
				SpecialTdsDTO tds = new SpecialTdsDTO();
				tds.setSection(getStringCellValue(row.getCell(0)));
				tds.setWhSection(getStringCellValue(row.getCell(1)));
				tds.setRateFrom(getLongCellValue(row.getCell(3)));
				tds.setRateTo(getLongCellValue(row.getCell(2)));
				tds.setWhPercentage(getBigDecimalCellValues(row.getCell(4)));
				tds.setSurPercentage(getBigDecimalCellValues(row.getCell(5)));
				tds.setEdPercentage(getBigDecimalCellValues(row.getCell(6)));
				tds.setTdsCertificateNo(getStringCellValue(row.getCell(7)));

				vendor.getSpecialTdsDTO().add(tds);
			});
		}
	}

	private BigDecimal getBigDecimalCellValues(Cell cell) {
		if (cell == null || cell.getCellType() == CellType.BLANK) {
			return BigDecimal.ZERO;
		}

		if (cell.getCellType() == CellType.NUMERIC) {
			return BigDecimal.valueOf(cell.getNumericCellValue());
		}

		try {
			return new BigDecimal(cell.getStringCellValue().trim());
		} catch (NumberFormatException e) {
			return BigDecimal.ZERO; // Return zero if the value cannot be converted
		}
	}

}
