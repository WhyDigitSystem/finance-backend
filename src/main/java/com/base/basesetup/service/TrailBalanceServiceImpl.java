package com.base.basesetup.service;

import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

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

import com.base.basesetup.dto.ExcelUploadResultDTO;
import com.base.basesetup.dto.TbDetailsDTO;
import com.base.basesetup.dto.TbHeaderDTO;
import com.base.basesetup.entity.BudgetVO;
import com.base.basesetup.entity.DocumentTypeMappingDetailsVO;
import com.base.basesetup.entity.PreviousYearActualVO;
import com.base.basesetup.entity.TbDetailsVO;
import com.base.basesetup.entity.TbHeaderVO;
import com.base.basesetup.entity.TrialBalanceVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.BudgetRepo;
import com.base.basesetup.repo.DocumentTypeMappingDetailsRepo;
import com.base.basesetup.repo.PreviousYearActualRepo;
import com.base.basesetup.repo.TbHeaderRepo;
import com.base.basesetup.repo.TrialBalanceRepo;

import io.jsonwebtoken.io.IOException;

@Service
public class TrailBalanceServiceImpl implements TrailBalanceService {

	public static final Logger LOGGER = LoggerFactory.getLogger(TrailBalanceServiceImpl.class);
	@Autowired
	TrialBalanceRepo trialBalanceRepo;

	@Autowired
	TbHeaderRepo tbHeaderRepo;

	@Autowired
	BudgetRepo budgetRepo;
	
	@Autowired
	PreviousYearActualRepo previousYearActualRepo;

	@Autowired
	DocumentTypeMappingDetailsRepo documentTypeMappingDetailsRepo;

	private int totalRows = 0;
	private int successfulUploads = 0;
	
	@Override
	public int getTotalRows() {
		return totalRows;
	}

	@Override
	public int getSuccessfulUploads() {
		// TODO Auto-generated method stub
		return successfulUploads;
	}

	@Override
	public ExcelUploadResultDTO excelUploadForTb(MultipartFile[] files, String createdBy, String clientCode,
			String finYear, String month, String clientName, Long orgId)
			throws ApplicationException, java.io.IOException {
		totalRows = 0;
		successfulUploads = 0;
		ExcelUploadResultDTO result = new ExcelUploadResultDTO(); // Result object
		List<TrialBalanceVO> dataToSave = new ArrayList<>();
		result.setTotalExcelRows(0);
		result.setSuccessfulUploads(0);
		result.setUnsuccessfulUploads(0);

		for (MultipartFile file : files) {
			try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
				Sheet sheet = workbook.getSheetAt(0); // Assuming only one sheet
				Row headerRow = sheet.getRow(0);
				List<String> errorMessages = new ArrayList<>();
				// Validate header
				for (Row row : sheet) {

					if (row.getRowNum() == 0) {
						continue; // Skip this iteration
					}
					totalRows++;
					result.setTotalExcelRows(result.getTotalExcelRows() + 1); // Increment total rows

					try {
						// Parse cell values
						String accountName = getStringCellValue(row.getCell(0)); // Account Name is in column 1
						BigDecimal opBalanceDb = parseBigDecimal(getStringCellValue(row.getCell(1)));
						BigDecimal opBalanceCr = parseBigDecimal(getStringCellValue(row.getCell(2)));
						BigDecimal transDebit = parseBigDecimal(getStringCellValue(row.getCell(3))); // Debit Amount in
						BigDecimal transCredit = parseBigDecimal(getStringCellValue(row.getCell(4))); // Credit in
						BigDecimal clBalanceDb = parseBigDecimal(getStringCellValue(row.getCell(5)));
						BigDecimal clBalanceCr = parseBigDecimal(getStringCellValue(row.getCell(6)));
						String year = getStringCellValue(row.getCell(7));
						String processMonth = getStringCellValue(row.getCell(8));
						// Debit in column 3

						// Create and populate TrailBalanceVO object
						TrialBalanceVO dataVO = new TrialBalanceVO();
						dataVO.setAccountName(accountName);
//						dataVO.setAccountCode(accountCode);
						dataVO.setClientCode(clientCode);
						dataVO.setOpBalanceDb(opBalanceDb);
						dataVO.setOpBalanceCr(opBalanceCr);
						dataVO.setTransCredit(transCredit);
						dataVO.setTransDebit(transDebit);
						dataVO.setClBalanceDb(clBalanceDb);
						dataVO.setClBalanceCr(clBalanceCr);
						dataVO.setFinYear(year);
						dataVO.setMonth(processMonth);
						dataVO.setOrgId(orgId);
						dataVO.setClient(clientName);
						dataVO.setCreatedBy(createdBy);
						dataVO.setUpdatedBy(createdBy);
						dataToSave.add(dataVO);
						result.setSuccessfulUploads(result.getSuccessfulUploads() + 1); // Increment successful uploads
						successfulUploads++;
					} catch (Exception e) {
						errorMessages.add("Row No " + (row.getRowNum() + 1) + ": " + e.getMessage());
						result.setUnsuccessfulUploads(result.getUnsuccessfulUploads() + 1);
						String error = String.format("Row %d: %s", row.getRowNum() + 1, e.getMessage());

						result.addFailureReason(error); // Capture failure reason
					}
				}if (!errorMessages.isEmpty()) {
					throw new ApplicationException(
						    "Excel upload failed. " + String.join(", ", errorMessages) + ". Except for these lines, all other data has been uploaded.");
				}

			} catch (IOException e) {
				throw new ApplicationException(
						"Failed to process file: " + file.getOriginalFilename() + " - " + e.getMessage());
			}
		}

		// Save all valid rows
		if (!dataToSave.isEmpty()) {
			trialBalanceRepo.saveAll(dataToSave);
		}

		return result; // Return the result summary
	}

	private BigDecimal parseBigDecimal(String value) throws ApplicationException {
		if (value == null || value.trim().isEmpty()) {
			return BigDecimal.ZERO; // Return 0.00 for empty or null cells
		}
		try {
			BigDecimal parsedValue = new BigDecimal(value.trim());
			return parsedValue.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : parsedValue; // Ensure zero is set as
																								// 0.00
		} catch (NumberFormatException e) {
			throw new ApplicationException("Invalid number format: " + value, e);
		}
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

	private boolean isRowEmpty(Row row) {
		for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
			Cell cell = row.getCell(cellNum);
			if (cell != null && cell.getCellType() != CellType.BLANK) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Map<String, Object> createUpdateTrailBalance(TbHeaderDTO tbHeaderDTO) throws ApplicationException {

		String screenCode = "TB";
		TbHeaderVO tbHeaderVO = new TbHeaderVO();

		String message = null;

		if (ObjectUtils.isEmpty(tbHeaderDTO.getId())) {

			String docId = tbHeaderRepo.getTBDocId(tbHeaderDTO.getOrgId(), tbHeaderDTO.getFinYear(), screenCode,
					tbHeaderDTO.getClientCode());
			tbHeaderVO.setDocId(docId);

			// GETDOCID LASTNO +1

			tbHeaderVO.setCreatedBy(tbHeaderDTO.getCreatedBy());
			tbHeaderVO.setUpdatedBy(tbHeaderDTO.getCreatedBy());

			message = "TrailBalance Creation SuccessFully";

		} else {

			tbHeaderVO = tbHeaderRepo.findById(tbHeaderDTO.getId())
					.orElseThrow(() -> new ApplicationException("HeaderDTO not found with id: " + tbHeaderDTO.getId()));
			tbHeaderVO.setUpdatedBy(tbHeaderDTO.getCreatedBy());

			message = "TrailBalance Updation SuccessFully";
		}

		tbHeaderVO = getTbHeaderVOromTbHeaderDTO(tbHeaderVO, tbHeaderDTO);
		tbHeaderRepo.save(tbHeaderVO);

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("tbHeaderVO", tbHeaderVO); // Return the list of saved records
		return response;
	}

	private TbHeaderVO getTbHeaderVOromTbHeaderDTO(TbHeaderVO tbHeaderVO, TbHeaderDTO tbHeaderDTO) {

		tbHeaderVO.setClientCode(tbHeaderDTO.getClientCode());
		tbHeaderVO.setClient(tbHeaderDTO.getClient());
		tbHeaderVO.setOrgId(tbHeaderDTO.getOrgId());
		tbHeaderVO.setClientCode(tbHeaderDTO.getClientCode());
		tbHeaderVO.setTbMonth(tbHeaderDTO.getTbMonth());
		tbHeaderVO.setFinYear(tbHeaderDTO.getFinYear());

		List<TbDetailsVO> tbDetailsVO = new ArrayList<>();
		List<TbDetailsDTO> tbDetailsDTO = tbHeaderDTO.getTbDetailsDTO();
		for (TbDetailsDTO detailsDTO : tbDetailsDTO) {
			TbDetailsVO tbDetailsVOs = new TbDetailsVO();
			tbDetailsVOs.setOrgId(tbHeaderDTO.getOrgId());
			tbDetailsVOs.setClient(tbHeaderDTO.getClient());
			tbDetailsVOs.setClientCode(tbHeaderDTO.getClientCode());
			tbDetailsVOs.setTbMonth(tbHeaderDTO.getTbMonth());
			tbDetailsVOs.setFinYear(tbHeaderDTO.getFinYear());
			tbDetailsVOs.setCoa(detailsDTO.getCoa());
			tbDetailsVOs.setCoaCode(detailsDTO.getCoaCode());
			tbDetailsVOs.setClientAccountName(detailsDTO.getClientAccountName());
			tbDetailsVOs.setClientAccountCode(detailsDTO.getClientAccountCode());
			tbDetailsVOs.setOpeningBalanceDb(detailsDTO.getOpeningBalanceDb());
			tbDetailsVOs.setOpeningBalanceCr(detailsDTO.getOpeningBalanceCr());
			tbDetailsVOs.setClosingBalanceDb(detailsDTO.getClosingBalanceDb());
			tbDetailsVOs.setClosingBalanceCr(detailsDTO.getClosingBalanceCr());
			tbDetailsVOs.setTransDebit(detailsDTO.getTransDebit());
			tbDetailsVOs.setTransCredit(detailsDTO.getTransCredit());
			tbDetailsVOs.setRemarks(detailsDTO.getRemarks());
			tbDetailsVOs.setTbHeaderVO(tbHeaderVO);
			tbDetailsVO.add(tbDetailsVOs);
		}
		tbHeaderVO.setTbDetailsVO(tbDetailsVO);
		return tbHeaderVO;
	}

	@Override
	public String getTBDocId(Long orgId, String finYear, String clientCode) {
		String ScreenCode = "TB";
		String result = tbHeaderRepo.getTBDocId(orgId, finYear, ScreenCode, clientCode);
		return result;
	}

	@Override
	public List<Map<String, Object>> getFillGridForTB(Long orgId, String finYear, String tbMonth, String client,
			String clientCode) {

		Set<Object[]> getDetails = trialBalanceRepo.getFillGridForTbExcelUpload(orgId, finYear, tbMonth, client,
				clientCode);
		return getFillGrid(getDetails);
	}

	private List<Map<String, Object>> getFillGrid(Set<Object[]> getDetails) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : getDetails) {
			Map<String, Object> map = new HashMap<>();
			map.put("clientAccountCode", ch[0] != null ? ch[0].toString() : "");
			map.put("clientAccountName", ch[1] != null ? ch[1].toString() : "");
			map.put("coaCode", ch[2] != null ? ch[2].toString() : "");
			map.put("coa", ch[3] != null ? ch[3].toString() : "");
			map.put("openingBalanceDb", ch[4] != null ? new BigDecimal(ch[4].toString()) : BigDecimal.ZERO);
			map.put("openingBalanceCr", ch[5] != null ? new BigDecimal(ch[5].toString()) : BigDecimal.ZERO);
			map.put("transDebit", ch[6] != null ? new BigDecimal(ch[6].toString()) : BigDecimal.ZERO);
			map.put("transCredit", ch[7] != null ? new BigDecimal(ch[7].toString()) : BigDecimal.ZERO);
			map.put("closingBalanceDb", ch[8] != null ? new BigDecimal(ch[8].toString()) : BigDecimal.ZERO);
			map.put("closingBalanceCr", ch[9] != null ? new BigDecimal(ch[9].toString()) : BigDecimal.ZERO);
			map.put("id", Integer.parseInt(ch[10].toString()));

			List1.add(map);
		}
		return List1;
	}

	@Override
	public List<TbHeaderVO> getAllTbByClient(Long orgId, String finYear, String client) {
		List<TbHeaderVO> headerVO = tbHeaderRepo.getAllTrialBalance(orgId, finYear, client);
		return headerVO;
	}

	@Override
	public TbHeaderVO getTrialBalanceVOById(Long Id) {

		return tbHeaderRepo.findById(Id).get();
	}

	@Override
	@Transactional(rollbackOn = ApplicationException.class)
	public ExcelUploadResultDTO excelUploadForBudget(MultipartFile[] files, String createdBy, String clientCode,
	        String clientName, Long orgId) throws ApplicationException, IOException, java.io.IOException {

		totalRows = 0;
		successfulUploads = 0;
	    ExcelUploadResultDTO result = new ExcelUploadResultDTO(); // Result object
	    List<BudgetVO> budgets = new ArrayList<>();
	    result.setTotalExcelRows(0);
	    result.setSuccessfulUploads(0);
	    result.setUnsuccessfulUploads(0);

	    for (MultipartFile file : files) {
	        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
	            Sheet sheet = workbook.getSheetAt(0); // Assuming only one sheet

	            for (Row row : sheet) {
	                if (row.getRowNum() == 0 || isRowEmpty(row)) {
	                	
	                	continue;
	                }
	                

	               
	                totalRows++;
	                result.setTotalExcelRows(result.getTotalExcelRows() + 1); // Increment total rows

	                try {
	                    // Parse cell values
	                    String accountName = getStringCellValue(row.getCell(1)); // Account Name
	                    String accountCode = getStringCellValue(row.getCell(2)); // Account Code
	                    String natureOfAccount = getStringCellValue(row.getCell(3)); // Nature of Account

	                    // Loop through months from April-24 to March-25
	                    for (int colIndex = 4; colIndex <= 15; colIndex++) {
	                    	String months=sheet.getRow(0).getCell(colIndex).toString();
		                	String[] parts = months.split("-");

		                    // Extract the month and year
		                    String abbreviatedMonth = parts[1]; // Apr
		                    String year = parts[2]; // 2024

		                    // Convert abbreviated month to full month name
		                    String fullMonth = getFullMonthName(abbreviatedMonth);

		                    // Print results
	                        BigDecimal amount = parseBigDecimal(getStringCellValue(row.getCell(colIndex))); // Amount

	                        // Create and populate BudgetVO object
	                        BudgetVO monthlyBudget = new BudgetVO();
	                        monthlyBudget.setOrgId(orgId);
	                        monthlyBudget.setClient(clientName);
	                        monthlyBudget.setClientCode(clientCode);
	                        monthlyBudget.setCreatedBy(createdBy);
	                        monthlyBudget.setUpdatedBy(createdBy);
	                        monthlyBudget.setAccountName(accountName);
	                        monthlyBudget.setAccountCode(accountCode);
	                        monthlyBudget.setNatureOfAccount(natureOfAccount);
	                        monthlyBudget.setYear(year); // Extract year
	                        monthlyBudget.setMonth(fullMonth); // Extract month (first three letters)
	                        monthlyBudget.setAmount(amount);
	                        monthlyBudget.setActive(true);
	                        budgets.add(monthlyBudget);
	                    }
	                    successfulUploads++;
	                    result.setSuccessfulUploads(result.getSuccessfulUploads() + 1); // Increment successful uploads
	                } catch (Exception e) {
	                    result.setUnsuccessfulUploads(result.getUnsuccessfulUploads() + 1);
	                    String error = String.format("Row %d: %s", row.getRowNum() + 1, e.getMessage());
	                    result.addFailureReason(error); // Capture failure reason
	                }
	            }
	        } catch (IOException | EncryptedDocumentException e) {
	            throw new ApplicationException(
	                    "Failed to process file: " + file.getOriginalFilename() + " - " + e.getMessage());
	        }
	    }

	    // Save all valid budget entries
	    if (!budgets.isEmpty()) {
	        budgetRepo.saveAll(budgets);
	    }

	    return result; // Return the result summary
	}

	private String getFullMonthName(String abbreviatedMonth) {
		 String[] months = new DateFormatSymbols().getMonths(); // Full month names
         String[] shortMonths = new DateFormatSymbols().getShortMonths(); // Abbreviated names
         
         for (int i = 0; i < shortMonths.length; i++) {
             if (shortMonths[i].equalsIgnoreCase(abbreviatedMonth)) {
                 return months[i]; // Return the full month name
             }
         }
         return null;
	}

	
	
	@Override
	@Transactional(rollbackOn = ApplicationException.class)
	public ExcelUploadResultDTO excelUploadForPreviousYear(MultipartFile[] files, String createdBy, String clientCode,
			String clientName, Long orgId) throws ApplicationException, java.io.IOException {

		totalRows = 0;
		successfulUploads = 0;
	    ExcelUploadResultDTO result = new ExcelUploadResultDTO(); // Result object
	    List<PreviousYearActualVO> budgets = new ArrayList<>();
	    result.setTotalExcelRows(0);
	    result.setSuccessfulUploads(0);
	    result.setUnsuccessfulUploads(0);

	    for (MultipartFile file : files) {
	        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
	            Sheet sheet = workbook.getSheetAt(0); // Assuming only one sheet

	            for (Row row : sheet) {
	                if (row.getRowNum() == 0 || isRowEmpty(row)) {
	                	
	                	continue;
	                }
	                totalRows++;
	                result.setTotalExcelRows(result.getTotalExcelRows() + 1); // Increment total rows

	                try {
	                    // Parse cell values
	                    String accountName = getStringCellValue(row.getCell(1)); // Account Name
	                    String accountCode = getStringCellValue(row.getCell(2)); // Account Code
	                    String natureOfAccount = getStringCellValue(row.getCell(3)); // Nature of Account

	                    // Loop through months from April-24 to March-25
	                    for (int colIndex = 4; colIndex <= 15; colIndex++) {
	                    	String months=sheet.getRow(0).getCell(colIndex).toString();
		                	String[] parts = months.split("-");

		                    // Extract the month and year
		                    String abbreviatedMonth = parts[1]; // Apr
		                    String year = parts[2]; // 2024

		                    // Convert abbreviated month to full month name
		                    String fullMonth = getFullMonthName(abbreviatedMonth);

		                    // Print results
	                        BigDecimal amount = parseBigDecimal(getStringCellValue(row.getCell(colIndex))); // Amount

	                        // Create and populate BudgetVO object
	                        PreviousYearActualVO monthlyBudget = new PreviousYearActualVO();
	                        monthlyBudget.setOrgId(orgId);
	                        monthlyBudget.setClient(clientName);
	                        monthlyBudget.setClientCode(clientCode);
	                        monthlyBudget.setCreatedBy(createdBy);
	                        monthlyBudget.setUpdatedBy(createdBy);
	                        monthlyBudget.setAccountName(accountName);
	                        monthlyBudget.setAccountCode(accountCode);
	                        monthlyBudget.setNatureOfAccount(natureOfAccount);
	                        monthlyBudget.setYear(year); // Extract year
	                        monthlyBudget.setMonth(fullMonth); // Extract month (first three letters)
	                        monthlyBudget.setAmount(amount);
	                        monthlyBudget.setActive(true);

	                        budgets.add(monthlyBudget);
	                    }

	                    successfulUploads++;
	                    result.setSuccessfulUploads(result.getSuccessfulUploads() + 1); // Increment successful uploads
	                } catch (Exception e) {
	                    result.setUnsuccessfulUploads(result.getUnsuccessfulUploads() + 1);
	                    String error = String.format("Row %d: %s", row.getRowNum() + 1, e.getMessage());
	                    result.addFailureReason(error); // Capture failure reason
	                }
	            }
	        } catch (IOException | EncryptedDocumentException e) {
	            throw new ApplicationException(
	                    "Failed to process file: " + file.getOriginalFilename() + " - " + e.getMessage());
	        }
	    }

	    // Save all valid budget entries
	    if (!budgets.isEmpty()) {
	        previousYearActualRepo.saveAll(budgets);
	    }

	    return result; // Return the result summary
	}

}
