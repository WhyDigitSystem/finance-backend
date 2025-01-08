package com.base.basesetup.service;

import java.io.IOException;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.EncryptedDocumentException;
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

import com.base.basesetup.dto.SampleExcelUploadDTO;
import com.base.basesetup.entity.SampleExcelUploadVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.SampleExcelUploadRepo;

@Service
public class SampleExcelUploadServiceImpl implements SampleExcelUploadService {

	public static final Logger LOGGER = LoggerFactory.getLogger(SampleExcelUploadServiceImpl.class);

	@Autowired
	SampleExcelUploadRepo sampleExcelUploadRepo;

	private int totalRows = 0;
	private int successfulUploads = 0;

	@Override
	public int getTotalRows() {
		return totalRows;
	}

	@Override
	public int getSuccessfulUploads() {
		return successfulUploads;
	}

	@Transactional
	@Override
	public void ExcelUploadForSample(MultipartFile[] files) throws ApplicationException {
	    List<SampleExcelUploadVO> sampleExcelUploadVOToSave = new ArrayList<>();

	    totalRows = 0;
	    successfulUploads = 0;

	    for (MultipartFile file : files) {
	        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
	            Sheet sheet = workbook.getSheetAt(0); 
	            List<String> errorMessages = new ArrayList<>();
	            System.out.println("Processing file: " + file.getOriginalFilename()); 

	            Row headerRow = sheet.getRow(0);
	            if (!isHeaderValid(headerRow)) {
	                throw new ApplicationException("Invalid Excel format. Please refer to the sample file.");
	            }

	            for (Row row : sheet) {
	                if (row.getRowNum() == 0 || isRowEmpty(row)) {
	                    continue; 
	                }

	                totalRows++; 
	                System.out.println("Validating row: " + (row.getRowNum() + 1)); 

	                try {
	                    String name = getStringCellValue(row.getCell(0));
	                    String email = getStringCellValue(row.getCell(1));
	                    String address = getStringCellValue(row.getCell(2));
	                    Long mobile = parseLong(getStringCellValue(row.getCell(3)));
	                    LocalDate dob = parseDate(getStringCellValue(row.getCell(4)));

	                    if (dob != null && !isAgeValid(dob)) {
	                        errorMessages.add("User must be 18 years or older at row " + (row.getRowNum() + 1));
	                        continue; 
	                    }

	                    SampleExcelUploadVO sampleExcelUploadVO = new SampleExcelUploadVO();
	                    sampleExcelUploadVO.setName(name);
	                    sampleExcelUploadVO.setEmail(email);
	                    sampleExcelUploadVO.setAddress(address);
	                    sampleExcelUploadVO.setMobile(mobile);
	                    sampleExcelUploadVO.setDob(dob);

	                    sampleExcelUploadVOToSave.add(sampleExcelUploadVO);
	                    successfulUploads++;

	                } catch (Exception e) {
	                    errorMessages.add("Error processing row " + (row.getRowNum() + 1) + ": " + e.getMessage());
	                }
	            }

	            if (!sampleExcelUploadVOToSave.isEmpty()) {
	                sampleExcelUploadRepo.saveAll(sampleExcelUploadVOToSave);
	            }

	            if (!errorMessages.isEmpty()) {
	                throw new ApplicationException(
	                        "Excel upload validation failed. Errors: " + String.join(", ", errorMessages));
	            }

	        } catch (IOException e) {
	            throw new ApplicationException("Failed to process file: " + file.getOriginalFilename() + " - " + e.getMessage());
	        } catch (EncryptedDocumentException e1) {
	            throw new ApplicationException("Error reading the encrypted Excel file. Please ensure the file is unencrypted.");
	        } catch (Exception e) {
	            throw new ApplicationException("An error occurred while processing the Excel file. Please try again.");
	        }
	    }
	}

	public boolean isAgeValid(LocalDate dob) {
	    if (dob == null) return false;

	    LocalDate today = LocalDate.now();
	    
	    int age = today.getYear() - dob.getYear();
	    
	   
	    if (dob.getMonthValue() > today.getMonthValue() || 
	        (dob.getMonthValue() == today.getMonthValue() && dob.getDayOfMonth() > today.getDayOfMonth())) {
	        age--;
	    }

	    return age >= 18;
	}

	private LocalDate parseDate(String stringCellValue) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			return LocalDate.parse(stringCellValue, formatter);
		} catch (Exception e) {
			System.err.println("Error parsing date: " + stringCellValue);
			return null;
		}
	}

	private boolean isRowEmpty(Row row) {
		for (Cell cell : row) {
			if (cell.getCellType() != CellType.BLANK) {
				return false;
			}
		}
		return true;
	}

	private boolean isHeaderValid(Row headerRow) {
		if (headerRow == null) {
			return false;
		}
		int expectedColumnCount = 5; 
		return "name".equalsIgnoreCase(getStringCellValue(headerRow.getCell(0)))
				&& "email".equalsIgnoreCase(getStringCellValue(headerRow.getCell(1)))
				&& "address".equalsIgnoreCase(getStringCellValue(headerRow.getCell(2)))
				&& "mobile".equalsIgnoreCase(getStringCellValue(headerRow.getCell(3)))
				&& "dob".equalsIgnoreCase(getStringCellValue(headerRow.getCell(4)));
	}

	private String getStringCellValue(Cell cell) {
		if (cell == null) {
			return "";
		}

		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue().trim();
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				return new SimpleDateFormat("dd-MM-yyyy").format(cell.getDateCellValue());
			} else {
				double numericValue = cell.getNumericCellValue();
				if (numericValue == (int) numericValue) {
					return String.valueOf((int) numericValue);
				} else {
					return BigDecimal.valueOf(numericValue).toPlainString();
				}
			}
		case BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		case FORMULA:
			return cell.getCellFormula();
		default:
			return "";
		}
	}

	private Long parseLong(String stringCellValue) {
		try {
			return Long.parseLong(stringCellValue);
		} catch (NumberFormatException e) {
			System.err.println("Error parsing long: " + stringCellValue);
			return null;
		}
	}

	private BigDecimal parseBigDecimal(String stringCellValue) {
		try {
			return new BigDecimal(stringCellValue);
		} catch (NumberFormatException e) {
			System.err.println("Error parsing BigDecimal: " + stringCellValue);
			return null;
		}
	}

	@Override
	public Map<String, Object> updateCreateSampleExcelUpload(@Valid SampleExcelUploadDTO sampleExcelUploadDTO)
			throws ApplicationException {
		SampleExcelUploadVO sampleExcelUploadVO = new SampleExcelUploadVO();
		String message;
		if (ObjectUtils.isNotEmpty(sampleExcelUploadDTO.getId())) {
			sampleExcelUploadVO = sampleExcelUploadRepo.findById(sampleExcelUploadDTO.getId())
					.orElseThrow(() -> new ApplicationException("Invalid SampleDetails details"));
			createUpdateSampleExcelUploadVOBySampleExcelUploadDTO(sampleExcelUploadDTO, sampleExcelUploadVO);
			message = "ExcelUpload Updated Successfully";
		} else {
			createUpdateSampleExcelUploadVOBySampleExcelUploadDTO(sampleExcelUploadDTO, sampleExcelUploadVO);
			message = "ExcelUpload Created Successfully";
		}

		sampleExcelUploadRepo.save(sampleExcelUploadVO);
		Map<String, Object> response = new HashMap<>();
		response.put("sampleExcelUploadVO", sampleExcelUploadVO);
		response.put("message", message);
		return response;
	}

	private void createUpdateSampleExcelUploadVOBySampleExcelUploadDTO(@Valid SampleExcelUploadDTO sampleExcelUploadDTO,
			SampleExcelUploadVO sampleExcelUploadVO) throws ApplicationException {
		sampleExcelUploadVO.setName(sampleExcelUploadDTO.getName());
		sampleExcelUploadVO.setAddress(sampleExcelUploadDTO.getAddress());
		sampleExcelUploadVO.setMobile(sampleExcelUploadDTO.getMobile());
		sampleExcelUploadVO.setEmail(sampleExcelUploadDTO.getEmail());

        LocalDate dob = sampleExcelUploadDTO.getDob();
		
		if (dob != null && !isAgeValid(dob)) {
			throw new ApplicationException("Date MissMatching !");
		}

		sampleExcelUploadVO.setDob(dob);
}



	@Override
	public List<SampleExcelUploadVO> getAllSampleExcelById(Long id) {
		List<SampleExcelUploadVO> sampleExcelUploadVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received DailyMonthlyExRates BY OrgId : {}", id);
			sampleExcelUploadVO = sampleExcelUploadRepo.findSampleExcelUploadById(id);
		}
		return sampleExcelUploadVO;
	}

	@Override
	public List<SampleExcelUploadVO> getAllSampleExcelUpload() {

		return sampleExcelUploadRepo.findAll();

	}

}
