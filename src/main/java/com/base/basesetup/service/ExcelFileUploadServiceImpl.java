package com.base.basesetup.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;
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

import com.base.basesetup.dto.FirstDataDTO;
import com.base.basesetup.entity.FirstDataVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.FirstDataRepo;

import io.jsonwebtoken.io.IOException;

@Service
public class ExcelFileUploadServiceImpl implements ExcelFileUploadService {

	public static final Logger LOGGER = LoggerFactory.getLogger(ExcelFileUploadServiceImpl.class);

	@Autowired
	FirstDataRepo firstDataRepo;

	
    private int totalRows=0;
    private int successfulUploads=0;

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
    public void ExcelUploadForSample(MultipartFile[] files, String createdBy)
            throws ApplicationException {
        List<FirstDataVO> dataToSave = new ArrayList<>();

        // Reset counters at the start of each upload
        totalRows = 0;
        successfulUploads = 0;

        for (MultipartFile file : files) {
			try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
				Sheet sheet = workbook.getSheetAt(0); // Assuming only one sheet
				List<String> errorMessages = new ArrayList<>();
				System.out.println("Processing file: " + file.getOriginalFilename()); // Debug statement
				Row headerRow = sheet.getRow(0);
				if (!isHeaderValid(headerRow)) {
					throw new ApplicationException("Invalid Excel format. Please refer to the sample file.");
				}

				// Check all rows for validity first
				for (Row row : sheet) {
					if (row.getRowNum() == 0 || isRowEmpty(row)) {
						continue; // Skip header row and empty rows
					}

					totalRows++; // Increment totalRows
					System.out.println("Validating row: " + (row.getRowNum() + 1)); // Debug statement

                    try {
                        // Retrieve cell values based on the provided order
                        LocalDate docDate = parseDate(getStringCellValue(row.getCell(0)));
                        Long accountNumber = parseLong(getStringCellValue(row.getCell(1)));
                        String accountName = getStringCellValue(row.getCell(2));
                        String description = getStringCellValue(row.getCell(3));
                        String category = getStringCellValue(row.getCell(4));
                        BigDecimal debit = parseBigDecimal(getStringCellValue(row.getCell(5)));
                        BigDecimal credit = parseBigDecimal(getStringCellValue(row.getCell(6)));
                        String currency = getStringCellValue(row.getCell(7));
                        BigDecimal balance = parseBigDecimal(getStringCellValue(row.getCell(8)));
                        String transactionType = getStringCellValue(row.getCell(9));

                        // Create FirstDataVO and add to list for batch saving
                        FirstDataVO dataVO = new FirstDataVO();
                        dataVO.setDocDate(docDate);
                        dataVO.setAccountNumber(accountNumber);
                        dataVO.setAccountName(accountName.toUpperCase());
                        dataVO.setDescription(description);
                        dataVO.setCategory(category.toUpperCase());
                        dataVO.setDebit(debit);
                        dataVO.setCredit(credit);
                        dataVO.setCurrency(currency.toUpperCase());
                        dataVO.setBalance(balance);
                        dataVO.setTransactionType(transactionType.toUpperCase());
                        dataVO.setCreatedBy(createdBy);
                        dataVO.setUpdatedBy(createdBy);

                        dataToSave.add(dataVO);
                        successfulUploads++; // Increment successfulUploads

                    } catch (Exception e) {
                        errorMessages.add("Error processing row " + (row.getRowNum() + 1) + ": " + e.getMessage());
                    }
                }

                // Save all valid rows
                firstDataRepo.saveAll(dataToSave);

                if (!errorMessages.isEmpty()) {
                    throw new ApplicationException(
                            "Excel upload validation failed. Errors: " + String.join(", ", errorMessages));
                }

            } catch (IOException e) {
                throw new ApplicationException(
                        "Failed to process file: " + file.getOriginalFilename() + " - " + e.getMessage());
            } catch (EncryptedDocumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (java.io.IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
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
        int expectedColumnCount = 10; // Adjust based on the actual number of columns
        return "date".equalsIgnoreCase(getStringCellValue(headerRow.getCell(0)))
                && "accountnumber".equalsIgnoreCase(getStringCellValue(headerRow.getCell(1)))
                && "accountname".equalsIgnoreCase(getStringCellValue(headerRow.getCell(2)))
                && "description".equalsIgnoreCase(getStringCellValue(headerRow.getCell(3)))
                && "category".equalsIgnoreCase(getStringCellValue(headerRow.getCell(4)))
                && "debit".equalsIgnoreCase(getStringCellValue(headerRow.getCell(5)))
                && "credit".equalsIgnoreCase(getStringCellValue(headerRow.getCell(6)))
                && "currency".equalsIgnoreCase(getStringCellValue(headerRow.getCell(7)))
                && "balance".equalsIgnoreCase(getStringCellValue(headerRow.getCell(8)))
                && "transactiontype".equalsIgnoreCase(getStringCellValue(headerRow.getCell(9)));
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
	public List<FirstDataVO> getAllFirstData() {
		return firstDataRepo.findAll();
	}

	@Override
	public Map<String, Object> updateCreateFirstData(@Valid FirstDataDTO firstDataDTO) throws ApplicationException {
		
		FirstDataVO firstDataVO=null;
		String message=null;
		
		if(ObjectUtils.isEmpty(firstDataDTO.getId())) {
			
			firstDataVO=new FirstDataVO();
			
			firstDataVO.setCreatedBy(firstDataDTO.getCreatedBy());
			firstDataVO.setUpdatedBy(firstDataDTO.getCreatedBy());
			
			message = "First Data Creation Successfully";
		}
		else {
			firstDataVO = firstDataRepo.findById(firstDataDTO.getId()).orElseThrow(
					() -> new ApplicationException("Cost Invoice Not Found with id: " + firstDataDTO.getId()));
			firstDataVO.setUpdatedBy(firstDataDTO.getCreatedBy());

			message = "First Data Updation Successfully";
		}
		
		firstDataVO=getFirstDataVOFromFirstDataDTO(firstDataVO,firstDataDTO);
		
		firstDataRepo.save(firstDataVO);
		
		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("firstDataVO", firstDataVO);
		return response;
		
	}

	private FirstDataVO getFirstDataVOFromFirstDataDTO(FirstDataVO firstDataVO, @Valid FirstDataDTO firstDataDTO) {
		
		firstDataVO.setDocDate(firstDataDTO.getDocDate());
	    firstDataVO.setAccountNumber(firstDataDTO.getAccountNumber());
	    firstDataVO.setAccountName(firstDataDTO.getAccountName().toUpperCase());
	    firstDataVO.setDescription(firstDataDTO.getDescription());
	    firstDataVO.setCategory(firstDataDTO.getCategory().toUpperCase());
	    firstDataVO.setDebit(firstDataDTO.getDebit());
	    firstDataVO.setCredit(firstDataDTO.getCredit());
	    firstDataVO.setCurrency(firstDataDTO.getCurrency().toUpperCase());
	    firstDataVO.setBalance(firstDataDTO.getBalance());
	    firstDataVO.setTransactionType(firstDataDTO.getTransactionType().toUpperCase());
	    firstDataVO.setCreatedBy(firstDataDTO.getCreatedBy());
	    
	    return firstDataVO;
	}

	@Override
	public Optional<FirstDataVO> getFirstDataById(Long id) {
		return firstDataRepo.findById(id);
	}
    
}

