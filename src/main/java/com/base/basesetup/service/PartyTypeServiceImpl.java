package com.base.basesetup.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.base.basesetup.dto.CustomerSalesPersonDTO;
import com.base.basesetup.dto.CustomersAddressDTO;
import com.base.basesetup.dto.CustomersDTO;
import com.base.basesetup.dto.CustomersStateDTO;
import com.base.basesetup.dto.PartyTypeDTO;
import com.base.basesetup.entity.PartyAddressVO;
import com.base.basesetup.entity.PartyMasterVO;
import com.base.basesetup.entity.PartySalesPersonTaggingVO;
import com.base.basesetup.entity.PartyStateVO;
import com.base.basesetup.entity.PartyTypeVO;
import com.base.basesetup.entity.VendorDTO;
import com.base.basesetup.entity.VendorsAddressDTO;
import com.base.basesetup.entity.VendorsSalesPersonTaggingDTO;
import com.base.basesetup.entity.VendorsStateDTO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.PartyAddressRepo;
import com.base.basesetup.repo.PartyMasterRepo;
import com.base.basesetup.repo.PartySalesPersonTaggingRepo;
import com.base.basesetup.repo.PartyStateRepo;
import com.base.basesetup.repo.PartyTypeRepo;

import io.jsonwebtoken.io.IOException;

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
	PartySalesPersonTaggingRepo partySalesPersonTaggingRepo;

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
	public List<PartyTypeVO> getAllPartyTypeByOrgId(Long orgid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PartyTypeVO> getPartyTypeById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> getPartyCodeByOrgIdAndPartyType(Long orgid, String partytype) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processExcelFile(MultipartFile excelFile, String creatdBy) throws IOException, java.io.IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void uploadCustomerData(MultipartFile file, Long orgId, String createdBy) throws Exception {
		try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {

			// Reading the customer sheet (Customer Data)
			Sheet customerSheet = workbook.getSheetAt(0); // Assuming customer sheet is the first one
			List<PartyMasterVO> customersVOList = new ArrayList<>();

			for (Row row : customerSheet) {
				if (row.getRowNum() == 0) { // Skipping header
					continue;
				}

				PartyMasterVO partyMasterVO = new PartyMasterVO();
				partyMasterVO.setOrgId(orgId);

				// Customer details mapping from the sheet
				partyMasterVO.setPartyName(getStringCellValue(row.getCell(0))); // Customer Name
				partyMasterVO.setPartyCode(getStringCellValue(row.getCell(1))); // Customer Code
				partyMasterVO.setGstIn(getStringCellValue(row.getCell(2))); // GSTIN
				partyMasterVO.setPanNo(getStringCellValue(row.getCell(3))); // Pan No

				partyMasterVO.setCreatedBy(createdBy);

				customersVOList.add(partyMasterVO);
			}

			// Reading the state sheet (State Data)
			Sheet stateSheet = workbook.getSheetAt(1); // Assuming state sheet is the second one
			List<PartyStateVO> statesVOList = new ArrayList<>();

			for (Row row : stateSheet) {
				if (row.getRowNum() == 0) { // Skipping header
					continue;
				}

				PartyStateVO partyStateVO = new PartyStateVO();
				partyStateVO.setState(getStringCellValue(row.getCell(0))); // State
				partyStateVO.setStateCode(getStringCellValue(row.getCell(1))); // State Code
				partyStateVO.setStateNo(getLongCellValue(row.getCell(2))); // State No
				partyStateVO.setGstIn(getStringCellValue(row.getCell(3))); // GSTIN
				partyStateVO.setContactPerson(getStringCellValue(row.getCell(4))); // Contact Person
				partyStateVO.setContactPhoneNo(getStringCellValue(row.getCell(5))); // Contact Phone No
				partyStateVO.setEmail(getStringCellValue(row.getCell(6))); // Contact Email
				// partyStateVO.setPartyCode(getStringCellValue(row.getCell(7))); // Customer
				// Code

				// Lookup Customer for State using customerCode
				// PartyMasterVO customer = findCustomerByCode(partyStateVO.getPartyCode());
				// partyStateVO.setPartyMasterVO(customer);

				statesVOList.add(partyStateVO);
			}

			// Reading the address sheet (Address Data)
			Sheet addressSheet = workbook.getSheetAt(2); // Assuming address sheet is the third one
			List<PartyAddressVO> addressesVOList = new ArrayList<>();

			for (Row row : addressSheet) {
				if (row.getRowNum() == 0) { // Skipping header
					continue;
				}

				PartyAddressVO partyAddressVO = new PartyAddressVO();
				partyAddressVO.setState(getStringCellValue(row.getCell(0))); // State
				partyAddressVO.setCity(getStringCellValue(row.getCell(1))); // City
				partyAddressVO.setBusinessPlace(getStringCellValue(row.getCell(2))); // Business Place
				partyAddressVO.setStateGstIn(getStringCellValue(row.getCell(3))); // State GSTIN
				partyAddressVO.setAddressType(getStringCellValue(row.getCell(4))); // Address Type
				partyAddressVO.setAddressLine1(getStringCellValue(row.getCell(5))); // Address Line 1
				partyAddressVO.setAddressLine2(getStringCellValue(row.getCell(6))); // Address Line 2
				partyAddressVO.setAddressLine3(getStringCellValue(row.getCell(7))); // Address Line 3
				partyAddressVO.setPincode(getLongCellValue(row.getCell(8))); // Pincode
				partyAddressVO.setContact(getStringCellValue(row.getCell(9))); // Contact
				// partyAddressVO.setPartyCode(getStringCellValue(row.getCell(10))); // Customer
				// Code

				// Lookup Customer for Address using customerCode
				// PartyMasterVO customer = findCustomerByCode(partyAddressVO.getPartyCode());
				// partyAddressVO.setPartyMasterVO(customer);

				addressesVOList.add(partyAddressVO);
			}

			// Reading the salesperson sheet (Salesperson Tagging Data)
			Sheet salesPersonSheet = workbook.getSheetAt(3); // Assuming salesperson sheet is the fourth one
			List<PartySalesPersonTaggingVO> salesPersonsVOList = new ArrayList<>();

			for (Row row : salesPersonSheet) {
				if (row.getRowNum() == 0) { // Skipping header
					continue;
				}

				PartySalesPersonTaggingVO salesPersonTaggingVO = new PartySalesPersonTaggingVO();
				salesPersonTaggingVO.setSalesPerson(getStringCellValue(row.getCell(0))); // Sales Person Name
				salesPersonTaggingVO.setEmpCode(getStringCellValue(row.getCell(1))); // Emp Code
				salesPersonTaggingVO.setSalesBranch(getStringCellValue(row.getCell(2))); // Sales Branch

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

				String effectiveFromString = getStringCellValue(row.getCell(3)); // Effective From
				String effectiveTillString = getStringCellValue(row.getCell(4)); // Effective Till

				LocalDate effectiveFrom = effectiveFromString.isEmpty() ? null
						: LocalDate.parse(effectiveFromString, formatter);
				LocalDate effectiveTill = effectiveTillString.isEmpty() ? null
						: LocalDate.parse(effectiveTillString, formatter);

				salesPersonTaggingVO.setEffectiveFrom(effectiveFrom);
				salesPersonTaggingVO.setEffectiveTill(effectiveTill);
				// salesPersonTaggingVO.setPartyCode(getStringCellValue(row.getCell(5))); //
				// Customer Code

				// Lookup Customer for Salesperson using customerCode
				// PartyMasterVO customer =
				// findCustomerByCode(salesPersonTaggingVO.getPartyCode());
				// salesPersonTaggingVO.setPartyMasterVO(customer);

				salesPersonsVOList.add(salesPersonTaggingVO);
			}

			// Save all data to the respective repositories
			savePartyMasterVOData(customersVOList);
			savePartyStateVOData(statesVOList);
			savePartyAddressVOData(addressesVOList);
			savePartySalesPersonTaggingVOData(salesPersonsVOList);

		}
	}

	// Helper method to get string value from a cell
	private String getStringCellValue(Cell cell) {
		if (cell == null) {
			return "";
		}
		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue();
		case NUMERIC:
			return String.valueOf(cell.getNumericCellValue());
		default:
			return "";
		}
	}

	// Helper method to get Long value from a cell (numeric or string)
	private Long getLongCellValue(Cell cell) {
		if (cell == null) {
			return null;
		}
		switch (cell.getCellType()) {
		case NUMERIC:
			double numericValue = cell.getNumericCellValue();
			return numericValue == (long) numericValue ? (long) numericValue : null;
		case STRING:
			try {
				return Long.parseLong(cell.getStringCellValue());
			} catch (NumberFormatException e) {
				return null;
			}
		default:
			return null;
		}
	}

	// Method to find PartyMasterVO by Customer Code (custom implementation)
	private PartyMasterVO findCustomerByCode(String customerCode) {
		// Implement logic to find the PartyMasterVO by customerCode, maybe through a
		// simple lookup
		return partyMasterRepo.findByPartyCode(customerCode);
	}

	// Save PartyMasterVO data to the database
	private void savePartyMasterVOData(List<PartyMasterVO> customers) {
		partyMasterRepo.saveAll(customers);
	}

	// Save PartyStateVO data to the database
	private void savePartyStateVOData(List<PartyStateVO> states) {
		partyStateRepo.saveAll(states);
	}

	// Save PartyAddressVO data to the database
	private void savePartyAddressVOData(List<PartyAddressVO> addresses) {
		partyAddressRepo.saveAll(addresses);
	}

	// Save PartySalesPersonTaggingVO data to the database
	private void savePartySalesPersonTaggingVOData(List<PartySalesPersonTaggingVO> salesPersons) {
		partySalesPersonTaggingRepo.saveAll(salesPersons);
	}

	@Override
	public Map<String, Object> createUpdateCustomer(@Valid CustomersDTO customersDTO) throws ApplicationException {

		PartyMasterVO partyMasterVO;
		String message = null;

		if (ObjectUtils.isEmpty(customersDTO.getId())) {

			partyMasterVO = new PartyMasterVO();
			partyMasterVO.setCreatedBy(customersDTO.getCreatedBy());
			partyMasterVO.setUpdatedBy(customersDTO.getCreatedBy());

			message = "Customers Creation Successfully";
		} else {

			partyMasterVO = partyMasterRepo.findById(customersDTO.getId()).orElseThrow(
					() -> new ApplicationException("Customers Order Not Found with id: " + customersDTO.getId()));
			partyMasterVO.setUpdatedBy(customersDTO.getCreatedBy());
			message = "Customers  Updation Successfully";
		}

		partyMasterVO = getpartyMasterVOFromCustomersDTO(partyMasterVO, customersDTO);
		partyMasterRepo.save(partyMasterVO);

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("partyMasterVO", partyMasterVO);
		return response;
	}

	private PartyMasterVO getpartyMasterVOFromCustomersDTO(PartyMasterVO partyMasterVO,
			@Valid CustomersDTO customersDTO) {

		partyMasterVO.setPartyName(customersDTO.getCustomerName());
		partyMasterVO.setPartyCode(customersDTO.getCustomerCode());
		partyMasterVO.setGstIn(customersDTO.getGstIn());
		partyMasterVO.setPanNo(customersDTO.getPanNo());
		partyMasterVO.setOrgId(customersDTO.getOrgId());
		partyMasterVO.setPartyType("CUSTOMER");

		if (customersDTO.getId() != null) {
			// Clear previous items from the database
			List<PartyStateVO> partyStateVOs = partyStateRepo.findByPartyMasterVO(partyMasterVO);
			partyStateRepo.deleteAll(partyStateVOs);

			List<PartyAddressVO> partyAddressVOs = partyAddressRepo.findByPartyMasterVO(partyMasterVO);
			partyAddressRepo.deleteAll(partyAddressVOs);

			List<PartySalesPersonTaggingVO> personTaggingVOs = partySalesPersonTaggingRepo
					.findByPartyMasterVO(partyMasterVO);
			partySalesPersonTaggingRepo.deleteAll(personTaggingVOs);

		}

		List<PartyStateVO> stateVOs = new ArrayList<>();
		for (CustomersStateDTO customersStateDTO : customersDTO.getCustomersStateDTO()) {

			PartyStateVO partyStateVO = new PartyStateVO();

			partyStateVO.setState(customersStateDTO.getState());
			partyStateVO.setStateCode(customersStateDTO.getStateCode());
			partyStateVO.setStateNo(customersStateDTO.getStateNo());
			partyStateVO.setGstIn(customersStateDTO.getGstIn());
			partyStateVO.setContactPerson(customersStateDTO.getContactPerson());
			partyStateVO.setContactPhoneNo(customersStateDTO.getPhoneNo());
			partyStateVO.setEmail(customersStateDTO.getEMail());
			partyStateVO.setCustomerCode(customersStateDTO.getCustomerCode());
			partyStateVO.setPartyMasterVO(partyMasterVO);
			// Set the SalesVO reference
			stateVOs.add(partyStateVO);

			// Add the SalesItemParticularsVO to the list
			partyMasterVO.setPartyStateVO(stateVOs);
		}

		List<PartyAddressVO> addressVOs = new ArrayList<>();
		for (CustomersAddressDTO customersAddressDTO : customersDTO.getCustomersAddressDTO()) {

			PartyAddressVO partyAddressVO = new PartyAddressVO();

			partyAddressVO.setState(customersAddressDTO.getState());
			partyAddressVO.setCity(customersAddressDTO.getCity());
			partyAddressVO.setBusinessPlace(customersAddressDTO.getBussinesPlace());
			partyAddressVO.setStateGstIn(customersAddressDTO.getGstnIn());
			partyAddressVO.setAddressType(customersAddressDTO.getAddressType());
			partyAddressVO.setAddressLine1(customersAddressDTO.getAddressLane1());
			partyAddressVO.setAddressLine2(customersAddressDTO.getAddressLane2());
			partyAddressVO.setAddressLine3(customersAddressDTO.getAddressLane3());
			partyAddressVO.setPincode(customersAddressDTO.getPinCode());
			partyAddressVO.setContact(customersAddressDTO.getContact());
			partyAddressVO.setCustomerCode(customersAddressDTO.getCustomerCode());
			partyAddressVO.setPartyMasterVO(partyMasterVO);
			// Set the SalesVO reference
			addressVOs.add(partyAddressVO);

			// Add the SalesItemParticularsVO to the list
			partyMasterVO.setPartyAddressVO(addressVOs);
		}

		List<PartySalesPersonTaggingVO> personTaggingVOs = new ArrayList<>();
		for (CustomerSalesPersonDTO customerSalesPersonDTO : customersDTO.getCustomerSalesPersonDTO()) {

			PartySalesPersonTaggingVO partySalesPersonTaggingVO = new PartySalesPersonTaggingVO();

			partySalesPersonTaggingVO.setSalesPerson(customerSalesPersonDTO.getSalesPerson());
			partySalesPersonTaggingVO.setEmpCode(customerSalesPersonDTO.getEmpCode());
			partySalesPersonTaggingVO.setSalesBranch(customerSalesPersonDTO.getSalesBranch());
			partySalesPersonTaggingVO.setEffectiveFrom(customerSalesPersonDTO.getEffectiveFrom());
			partySalesPersonTaggingVO.setEffectiveTill(customerSalesPersonDTO.getEffectiveTill());
			partySalesPersonTaggingVO.setCustomerCode(customerSalesPersonDTO.getCustomerCode());
			partySalesPersonTaggingVO.setPartyMasterVO(partyMasterVO);
			// Set the SalesVO reference
			personTaggingVOs.add(partySalesPersonTaggingVO);

			// Add the SalesItemParticularsVO to the list
			partyMasterVO.setPartySalesPersonTaggingVO(personTaggingVOs);
		}

		return partyMasterVO;
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
		PartyMasterVO partyMasterVO;
		String message = null;

		if (ObjectUtils.isEmpty(vendorDTO.getId())) {

			partyMasterVO = new PartyMasterVO();
			partyMasterVO.setCreatedBy(vendorDTO.getCreatedBy());
			partyMasterVO.setUpdatedBy(vendorDTO.getCreatedBy());

			message = "vendor Creation Successfully";
		} else {

			partyMasterVO = partyMasterRepo.findById(vendorDTO.getId()).orElseThrow(
					() -> new ApplicationException("vendor Order Not Found with id: " + vendorDTO.getId()));
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

	private PartyMasterVO getpartyMasterVOFromVendorDTO(PartyMasterVO partyMasterVO,
			@Valid VendorDTO vendorDTO) {

		partyMasterVO.setPartyName(vendorDTO.getVendorName());
		partyMasterVO.setPartyCode(vendorDTO.getVendorCode());
		partyMasterVO.setGstIn(vendorDTO.getGstIn());
		partyMasterVO.setPanNo(vendorDTO.getPanNo());
		partyMasterVO.setOrgId(vendorDTO.getOrgId());
		partyMasterVO.setPartyType("VENDOR");

		if (vendorDTO.getId() != null) {
			// Clear previous items from the database
			List<PartyStateVO> partyStateVOs = partyStateRepo.findByPartyMasterVO(partyMasterVO);
			partyStateRepo.deleteAll(partyStateVOs);

			List<PartyAddressVO> partyAddressVOs = partyAddressRepo.findByPartyMasterVO(partyMasterVO);
			partyAddressRepo.deleteAll(partyAddressVOs);

			List<PartySalesPersonTaggingVO> personTaggingVOs = partySalesPersonTaggingRepo
					.findByPartyMasterVO(partyMasterVO);
			partySalesPersonTaggingRepo.deleteAll(personTaggingVOs);

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
			partyStateVO.setCustomerCode(vendorsStateDTO.getVendorCode());
			partyStateVO.setPartyMasterVO(partyMasterVO);
			// Set the SalesVO reference
			stateVOs.add(partyStateVO);

			// Add the SalesItemParticularsVO to the list
			partyMasterVO.setPartyStateVO(stateVOs);
		}

		List<PartyAddressVO> addressVOs = new ArrayList<>();
		for (VendorsAddressDTO vendorsAddressDTO :vendorDTO.getVendorAddressDTO()) {
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
			partyAddressVO.setCustomerCode(vendorsAddressDTO.getVendorCode());
			partyAddressVO.setPartyMasterVO(partyMasterVO);
			// Set the SalesVO reference
			addressVOs.add(partyAddressVO);

			// Add the SalesItemParticularsVO to the list
			partyMasterVO.setPartyAddressVO(addressVOs);
		}

		List<PartySalesPersonTaggingVO> personTaggingVOs = new ArrayList<>();
		for (VendorsSalesPersonTaggingDTO vendorsSalesPersonTaggingDTO : vendorDTO.getVendorSalesPersonDTO()) {

			PartySalesPersonTaggingVO partySalesPersonTaggingVO = new PartySalesPersonTaggingVO();

			partySalesPersonTaggingVO.setSalesPerson(vendorsSalesPersonTaggingDTO.getSalesPerson());
			partySalesPersonTaggingVO.setEmpCode(vendorsSalesPersonTaggingDTO.getEmpCode());
			partySalesPersonTaggingVO.setSalesBranch(vendorsSalesPersonTaggingDTO.getSalesBranch());
			partySalesPersonTaggingVO.setEffectiveFrom(vendorsSalesPersonTaggingDTO.getEffectiveFrom());
			partySalesPersonTaggingVO.setEffectiveTill(vendorsSalesPersonTaggingDTO.getEffectiveTill());
			partySalesPersonTaggingVO.setCustomerCode(vendorsSalesPersonTaggingDTO.getVendorCode());
			partySalesPersonTaggingVO.setPartyMasterVO(partyMasterVO);
			// Set the SalesVO reference
			personTaggingVOs.add(partySalesPersonTaggingVO);

			// Add the SalesItemParticularsVO to the list
			partyMasterVO.setPartySalesPersonTaggingVO(personTaggingVOs);
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


}
