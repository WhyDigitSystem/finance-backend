package com.base.basesetup.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import com.base.basesetup.dto.CustomerSalesPersonDTO;
import com.base.basesetup.dto.CustomersAddressDTO;
import com.base.basesetup.dto.CustomersDTO;
import com.base.basesetup.dto.CustomersStateDTO;
import com.base.basesetup.dto.PartyTypeDTO;
import com.base.basesetup.entity.PartyAddressVO;
import com.base.basesetup.entity.PartyMasterVO;
import com.base.basesetup.entity.PartySalesPersonTaggingVO;
import com.base.basesetup.entity.PartySpecialTDSVO;
import com.base.basesetup.entity.PartyStateVO;
import com.base.basesetup.entity.PartyTypeVO;
import com.base.basesetup.entity.SpecialTdsDTO;
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

			// Reading the customer sheet (CustomersDTO)
			Sheet customerSheet = workbook.getSheetAt(0); // Assuming customer sheet is the first one
			List<CustomersDTO> customersDTOList = new ArrayList<>();

			// Loop through the customer sheet and create CustomersDTO entries
			for (Row row : customerSheet) {
				if (row.getRowNum() == 0) { // Skipping header
					continue;
				}

				// Mapping Excel row data to CustomersDTO
				CustomersDTO customersDTO = new CustomersDTO();
				customersDTO.setCustomerName(getStringCellValue(row.getCell(0))); // Customer Name
				customersDTO.setGstIn(getStringCellValue(row.getCell(2))); // GSTIN
				customersDTO.setPanNo(getStringCellValue(row.getCell(3))); // Pan No
				customersDTO.setCreatedBy(createdBy); // Created By
				customersDTO.setOrgId(orgId);
				customersDTO.setActive(true); // Assuming it's active

				// Add CustomersDTO to the list
				customersDTOList.add(customersDTO);
			}

			// Reading the state sheet (PartyStateDTO)
			Sheet stateSheet = workbook.getSheetAt(1); // Assuming state sheet is the second one
			List<CustomersStateDTO> partyStateDTOList = new ArrayList<>();

			// Loop through the state sheet and create PartyStateDTO entries
			for (Row row : stateSheet) {
				if (row.getRowNum() == 0) { // Skipping header
					continue;
				}

				String customerName = row.getCell(7).getStringCellValue();
				CustomersDTO customersDTO = customersDTOList.stream()
						.filter(c -> c.getCustomerName().equals(customerName)).findFirst().orElseThrow(
								() -> new RuntimeException("No customer found for Customer name: " + customerName));

				// Mapping Excel row data to PartyStateDTO
				CustomersStateDTO customersStateDTO = new CustomersStateDTO();
				customersStateDTO.setState(getStringCellValue(row.getCell(0))); // State
				customersStateDTO.setStateCode(getStringCellValue(row.getCell(1))); // State Code
				customersStateDTO.setStateNo(getLongCellValue(row.getCell(2))); // State No
				customersStateDTO.setGstIn(getStringCellValue(row.getCell(3))); // GSTIN
				customersStateDTO.setContactPerson(getStringCellValue(row.getCell(4))); // Contact Person
				customersStateDTO.setPhoneNo(getStringCellValue(row.getCell(5))); // Contact Phone No
				customersStateDTO.setEMail(getStringCellValue(row.getCell(6))); // Contact Email

				partyStateDTOList.add(customersStateDTO);
				customersDTO.setCustomersStateDTO(partyStateDTOList);
			}

			// Reading the address sheet (PartyAddressDTO)
			Sheet addressSheet = workbook.getSheetAt(2); // Assuming address sheet is the third one
			List<CustomersAddressDTO> partyAddressDTOList = new ArrayList<>();

			// Loop through the address sheet and create PartyAddressDTO entries
			for (Row row : addressSheet) {
				if (row.getRowNum() == 0) { // Skipping header
					continue;
				}

				String customerName = row.getCell(11).getStringCellValue();
				CustomersDTO customersDTO = customersDTOList.stream()
						.filter(c -> c.getCustomerName().equals(customerName)).findFirst().orElseThrow(
								() -> new RuntimeException("No customer found for Customer name: " + customerName));

				// Mapping Excel row data to PartyAddressDTO
				CustomersAddressDTO partyAddressDTO = new CustomersAddressDTO();
				partyAddressDTO.setState(getStringCellValue(row.getCell(0))); // State
				partyAddressDTO.setCity(getStringCellValue(row.getCell(1))); // City
				partyAddressDTO.setBussinesPlace(getStringCellValue(row.getCell(2))); // Business Place
				partyAddressDTO.setGstnIn(getStringCellValue(row.getCell(3))); // GST IN
				partyAddressDTO.setAddressType(getStringCellValue(row.getCell(4))); // Address Type
				partyAddressDTO.setAddressLane1(getStringCellValue(row.getCell(5))); // Address Line 1
				partyAddressDTO.setAddressLane2(getStringCellValue(row.getCell(6))); // Address Line 2
				partyAddressDTO.setAddressLane3(getStringCellValue(row.getCell(7))); // Address Line 3
				partyAddressDTO.setPinCode(getLongCellValue(row.getCell(8))); // PinCode
				partyAddressDTO.setContact(getStringCellValue(row.getCell(9))); // Contact

				partyAddressDTOList.add(partyAddressDTO);
				customersDTO.setCustomersAddressDTO(partyAddressDTOList);

			}

			// Reading the sales person tagging sheet (PartySalesPersonTaggingDTO)
			Sheet salesPersonSheet = workbook.getSheetAt(3); // Assuming sales person tagging sheet is the fourth one
			List<CustomerSalesPersonDTO> partySalesPersonTaggingDTOList = new ArrayList<>();

			// Loop through the sales person sheet and create PartySalesPersonTaggingDTO
			// entries
			for (Row row : salesPersonSheet) {
				if (row.getRowNum() == 0) { // Skipping header
					continue;
				}

				String customerName = row.getCell(5).getStringCellValue();
				CustomersDTO customersDTO = customersDTOList.stream()
						.filter(c -> c.getCustomerName().equals(customerName)).findFirst().orElseThrow(
								() -> new RuntimeException("No customer found for Customer name: " + customerName));

				// Mapping Excel row data to PartySalesPersonTaggingDTO
				CustomerSalesPersonDTO partySalesPersonTaggingDTO = new CustomerSalesPersonDTO();
				partySalesPersonTaggingDTO.setSalesPerson(getStringCellValue(row.getCell(0))); // Sales Person
				partySalesPersonTaggingDTO.setEmpCode(getStringCellValue(row.getCell(1))); // Emp Code
				partySalesPersonTaggingDTO.setSalesBranch(getStringCellValue(row.getCell(2))); // Branch
				partySalesPersonTaggingDTO.setEffectiveFrom(getLocalDateCellValue(row.getCell(3))); // Effective From
				partySalesPersonTaggingDTO.setEffectiveTill(getLocalDateCellValue(row.getCell(4))); // Effective Till

				partySalesPersonTaggingDTOList.add(partySalesPersonTaggingDTO);
				customersDTO.setCustomerSalesPersonDTO(partySalesPersonTaggingDTOList);

			}

			for (CustomersDTO customer : customersDTOList) {
				createUpdateCustomer(customer); // Assuming this method takes a single CustomersDTO
			}

		}
	}

	// Helper method to get string value from a cell, ensuring the cell isn't null
	private String getStringCellValue(Cell cell) {
		if (cell == null) {
			return ""; // Return empty string if cell is null
		}
		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue();
		case NUMERIC:
			return String.valueOf(cell.getNumericCellValue());
		case BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		default:
			return ""; // Return empty string for unsupported cell types or unknown cases
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

	// Helper method to get LocalDate value from a cell (date)
	private LocalDate getLocalDateCellValue(Cell cell) {
		if (cell == null) {
			return null;
		}
		if (cell.getCellType() == CellType.NUMERIC) {
			if (DateUtil.isCellDateFormatted(cell)) {
				// Excel stores dates as serial numbers, convert that to LocalDate
				return cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			}
		}
		return null; // Return null if it's not a date or is an invalid cell type
	}

	@Override
	public Map<String, Object> createUpdateCustomer(@Valid CustomersDTO customersDTO) throws ApplicationException {

		PartyMasterVO partyMasterVO = new PartyMasterVO();
		String message = null;

		if (ObjectUtils.isNotEmpty(customersDTO.getId())) {

			partyMasterVO = partyMasterRepo.findById(customersDTO.getId())
					.orElseThrow(() -> new ApplicationException("Invalid PartyMaster Details"));
			partyMasterVO.setUpdatedBy(customersDTO.getCreatedBy());

			message = "Customers Updated Successfully";
		} else {

			String partyType = "CUSTOMER";
			// PARTCODE DOCID API
			String partyTypeDocId = partyTypeRepo.getPartyTypeDocId(customersDTO.getOrgId(), partyType);
			partyMasterVO.setPartyCode(partyTypeDocId);

			// UPDATE PARTCODE DOCID LASTNO +1
			PartyTypeVO partyTypeVO = partyTypeRepo.findByOrgIdAndPartyType(customersDTO.getOrgId(), partyType);
			partyTypeVO.setLastNo(partyTypeVO.getLastNo() + 1);
			partyTypeRepo.save(partyTypeVO);

			partyMasterVO.setCreatedBy(customersDTO.getCreatedBy());
			partyMasterVO.setUpdatedBy(customersDTO.getCreatedBy());
			message = "Customer Created Successfully";
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
			partyAddressVO.setContact(partyAddressDTO.getContact()); // Changed from contactPerson to contact

			partyAddressVO.setPartyMasterVO(partyMasterVO);
			partyAddressVOs.add(partyAddressVO);
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
			partySalesPersonTaggingVO.setPartyMasterVO(partyMasterVO);
			partySalesPersonTaggingVOs.add(partySalesPersonTaggingVO);

		}
		getPartyMasterVOFromCustomerDTO(customersDTO, partyMasterVO);
		partyMasterVO.setPartyStateVO(partyStateVOs);
		partyMasterVO.setPartyAddressVO(partyAddressVOs);
		partyMasterVO.setPartySalesPersonTaggingVO(partySalesPersonTaggingVOs);

		partyMasterRepo.save(partyMasterVO);
		Map<String, Object> response = new HashMap<>();
		response.put("partyMasterVO", partyMasterVO);
		response.put("message", message);
		return response;
	}

	private void getPartyMasterVOFromCustomerDTO(@Valid CustomersDTO customerDTO, PartyMasterVO partyMasterVO) {
		partyMasterVO.setPartyType("CUSTOMER");
		partyMasterVO.setPartyName(customerDTO.getCustomerName());
		partyMasterVO.setGstPartyName(customerDTO.getCustomerName());
		partyMasterVO.setGstIn(customerDTO.getGstIn());
		partyMasterVO.setPanNo(customerDTO.getPanNo());
		partyMasterVO.setPanName(customerDTO.getCustomerName());
		partyMasterVO.setActive(customerDTO.isActive());
		partyMasterVO.setOrgId(customerDTO.getOrgId());
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

	private PartyMasterVO getpartyMasterVOFromVendorDTO(PartyMasterVO partyMasterVO, @Valid VendorDTO vendorDTO) {

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
			// partyStateVO.setStateNo(vendorsStateDTO.getStateNo());
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
