package com.base.basesetup.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
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

import com.base.basesetup.dto.ElMfrDTO;
import com.base.basesetup.entity.ElMfrVO;
import com.base.basesetup.entity.FinancialYearVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.BudgetHeadCountRepo;
import com.base.basesetup.repo.BudgetRepo;
import com.base.basesetup.repo.ElMfrRepo;
import com.base.basesetup.repo.FinancialYearRepo;
import com.base.basesetup.repo.OrderBookingRepo;
import com.base.basesetup.repo.PreviousYearActualRepo;
import com.base.basesetup.repo.TrialBalanceRepo;

import io.jsonwebtoken.io.IOException;

@Service
public class ELReportServiceImpl implements ELReportService {

	public static final Logger LOGGER = LoggerFactory.getLogger(ELReportServiceImpl.class);

	@Autowired
	ElMfrRepo elMfrRepo;

	@Autowired
	TrialBalanceRepo trialBalanceRepo;

	@Autowired
	FinancialYearRepo financialYearRepo;

	@Autowired
	BudgetRepo budgetRepo;

	@Autowired
	PreviousYearActualRepo previousYearActualRepo;

	@Autowired
	BudgetHeadCountRepo budgetHeadCountRepo;

	@Autowired
	OrderBookingRepo orderBookingRepo;

	@Override
	public Map<String, Object> createUpdateElMfr(ElMfrDTO elMfrDTO) throws ApplicationException {

		ElMfrVO elMfrVO = null;
		String message = null;

		if (ObjectUtils.isEmpty(elMfrDTO.getId())) {

			if (elMfrRepo.existsByDescriptionAndOrgId(elMfrDTO.getDescription(), elMfrDTO.getOrgId())) {

				String errorMessage = String.format("This Description: %s Already Exists", elMfrDTO.getDescription());
				throw new ApplicationException(errorMessage);

			}

			if (elMfrRepo.existsByElCodeAndOrgId(elMfrDTO.getElCode(), elMfrDTO.getOrgId())) {

				String errorMessage = String.format("This ElCode: %s Already Exists", elMfrDTO.getElCode());
				throw new ApplicationException(errorMessage);

			}

			elMfrVO = new ElMfrVO();
			elMfrVO.setCreatedBy(elMfrDTO.getCreatedBy());
			elMfrVO.setUpdatedBy(elMfrDTO.getCreatedBy());

			message = "El MFR Report Creation Successfully";

		} else {

			elMfrVO = elMfrRepo.findById(elMfrDTO.getId())
					.orElseThrow(() -> new ApplicationException("El MFR not found with id: " + elMfrDTO.getId()));
			elMfrVO.setUpdatedBy(elMfrDTO.getCreatedBy());

			if (!elMfrVO.getDescription().equalsIgnoreCase(elMfrDTO.getDescription())) {

				if (elMfrRepo.existsByDescriptionAndOrgId(elMfrDTO.getDescription(), elMfrDTO.getOrgId())) {

					String errorMessage = String.format("This Description: %s Already Exists",
							elMfrDTO.getDescription());
					throw new ApplicationException(errorMessage);

				}
				elMfrVO.setDescription(elMfrDTO.getDescription());
			}

			if (!elMfrVO.getElCode().equalsIgnoreCase(elMfrDTO.getElCode())) {

				if (elMfrRepo.existsByElCodeAndOrgId(elMfrDTO.getElCode(), elMfrDTO.getOrgId())) {

					String errorMessage = String.format("This ElCode: %s Already Exists", elMfrDTO.getElCode());
					throw new ApplicationException(errorMessage);

				}
				elMfrVO.setElCode(elMfrDTO.getElCode());
			}

			message = "El MFR Report Updation Successfully";
		}

		elMfrVO = getElMfrVOFromElMfrDTO(elMfrVO, elMfrDTO);
		elMfrRepo.save(elMfrVO);

		Map<String, Object> response = new HashMap<String, Object>();
		response.put("message", message);
		response.put("elMfrVO", elMfrVO);
		return response;
	}

	private ElMfrVO getElMfrVOFromElMfrDTO(ElMfrVO elMfrVO, ElMfrDTO elMfrDTO) {

		elMfrVO.setDescription(elMfrDTO.getDescription());
		elMfrVO.setElCode(elMfrDTO.getElCode());
		elMfrVO.setOrgId(elMfrDTO.getOrgId());

		return elMfrVO;
	}

	@Override
	public List<ElMfrVO> getAllElMfr(Long orgId) {
		return elMfrRepo.getAllElMfr(orgId);
	}

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
	public void excelUploadForElMfr(MultipartFile[] files, String createdBy, Long orgId)
			throws ApplicationException, java.io.IOException {
		List<ElMfrVO> dataToSave = new ArrayList<>();

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
						String description = getStringCellValue(row.getCell(0));
						String elCode = getStringCellValue(row.getCell(1));

						// Check if the description already exists in the database
						if (elMfrRepo.existsByDescriptionAndOrgId(description, orgId)) {
							String errorMessage = String.format("This Description: %s Already Exists", description);
							errorMessages.add(errorMessage);
							continue; // Skip saving this row if it already exists
						}

						// Check if the elCode already exists in the database
						if (elMfrRepo.existsByElCodeAndOrgId(elCode, orgId)) {
							String errorMessage = String.format("This ElCode: %s Already Exists", elCode);
							errorMessages.add(errorMessage);
							continue; // Skip saving this row if it already exists
						}

						// Create ElMfrVO and add to list for batch saving
						ElMfrVO dataVO = new ElMfrVO();
						dataVO.setDescription(description);
						dataVO.setElCode(elCode);
						dataVO.setCreatedBy(createdBy);
						dataVO.setOrgId(orgId);
						dataVO.setUpdatedBy(createdBy);

						dataToSave.add(dataVO);
						successfulUploads++; // Increment successfulUploads

					} catch (Exception e) {
						errorMessages.add("Error processing row " + (row.getRowNum() + 1) + ": " + e.getMessage());
					}
				}

				// Save all valid rows
				if (!dataToSave.isEmpty()) {
					elMfrRepo.saveAll(dataToSave);
				}

				if (!errorMessages.isEmpty()) {
					throw new ApplicationException(
							"Excel upload validation failed. Errors: " + String.join(", ", errorMessages));
				}

			} catch (IOException | EncryptedDocumentException e) {
				throw new ApplicationException(
						"Failed to process file: " + file.getOriginalFilename() + " - " + e.getMessage());
			}
		}
	}

	private boolean isRowEmpty(Row row) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean isHeaderValid(Row headerRow) {
		if (headerRow == null) {
			return false;
		}
		// Validate header columns for expected field names
		return "Description".equalsIgnoreCase(getStringCellValue(headerRow.getCell(0)))
				&& "ElCode".equalsIgnoreCase(getStringCellValue(headerRow.getCell(1)));
	}

	private String getStringCellValue(Cell cell) {
		if (cell == null) {
			return ""; // Return empty string if cell is null
		}

		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue().trim(); // Remove leading/trailing whitespaces
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				return new SimpleDateFormat("dd-MM-yyyy").format(cell.getDateCellValue()); // Format date cells
			} else {
				double numericValue = cell.getNumericCellValue();
				if (numericValue == (int) numericValue) {
					return String.valueOf((int) numericValue); // Return as integer if the number has no decimal part
				} else {
					return BigDecimal.valueOf(numericValue).toPlainString(); // Return the full numeric value
				}
			}
		case BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		case FORMULA:
			return cell.getCellFormula(); // Return the formula if it's a formula cell
		default:
			return ""; // Return empty string for other cell types
		}
	}

	private LocalDate parseDate(String stringCellValue) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			return LocalDate.parse(stringCellValue, formatter); // Convert date from string format
		} catch (Exception e) {
			System.err.println("Error parsing date: " + stringCellValue);
			return null; // Return null if parsing fails
		}
	}

	private Long parseLong(String stringCellValue) {
		try {
			return Long.parseLong(stringCellValue); // Parse string to long
		} catch (NumberFormatException e) {
			System.err.println("Error parsing long: " + stringCellValue);
			return null; // Return null if parsing fails
		}
	}

	private BigDecimal parseBigDecimal(String stringCellValue) {
		try {
			return new BigDecimal(stringCellValue); // Parse string to BigDecimal
		} catch (NumberFormatException e) {
			System.err.println("Error parsing BigDecimal: " + stringCellValue);
			return null; // Return null if parsing fails
		}
	}

	@Override
	public List<Map<String, Object>> getMisMatchClientTb(Long orgId, String clientCode) {
		// TODO Auto-generated method stub
		Set<Object[]> getTb = elMfrRepo.getMisMatchClientTb(orgId, clientCode);
		return getMisMatch(getTb);
	}

	private List<Map<String, Object>> getMisMatch(Set<Object[]> chCode) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chCode) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", Integer.parseInt(ch[0].toString()));
			map.put("accountCode", ch[1] != null ? ch[1].toString() : "");
			map.put("accountName", ch[2] != null ? ch[2].toString() : "");
			map.put("action", ch[3] != null ? ch[3].toString() : "");
			map.put("screen", Integer.parseInt(ch[4].toString()));
			List1.add(map);
		}
		return List1;

	}

	@Override
	public List<Map<String, Object>> getClientBudgetDetails(Long orgId, String year, String client, String clientCode) {
		Set<Object[]> budget = budgetRepo.getClientBudgetDetails(orgId, year, client, clientCode);
		return budgetDetails(budget);
	}

	private List<Map<String, Object>> budgetDetails(Set<Object[]> budget) {
		List<Map<String, Object>> budgets = new ArrayList<>();
		for (Object[] bud : budget) {
			Map<String, Object> b = new HashMap<>();
			b.put("id", Integer.parseInt(bud[0].toString()));
			b.put("accountName", bud[1] != null ? bud[1].toString() : "");
			b.put("accountCode", bud[2] != null ? bud[2].toString() : "");
			b.put("natureOfAccount", bud[3] != null ? bud[3].toString() : "");
			b.put("january", ((Number) bud[4]).intValue());
			b.put("february", ((Number) bud[5]).intValue());
			b.put("march", ((Number) bud[6]).intValue());
			b.put("april", ((Number) bud[7]).intValue());
			b.put("may", ((Number) bud[8]).intValue());
			b.put("june", ((Number) bud[9]).intValue());
			b.put("july", ((Number) bud[10]).intValue());
			b.put("august", ((Number) bud[11]).intValue());
			b.put("september", ((Number) bud[12]).intValue());
			b.put("october", ((Number) bud[13]).intValue());
			b.put("november", ((Number) bud[14]).intValue());
			b.put("december", ((Number) bud[15]).intValue());

			budgets.add(b);
		}
		return budgets;
	}

	@Override
	public List<Map<String, Object>> getClientPreviousYearActualDetails(Long orgId, String year, String client,
			String clientCode) {
		Set<Object[]> actual = previousYearActualRepo.getClientPreviousYearActualDetails(orgId, year, client,
				clientCode);
		return actualDetails(actual);
	}

	private List<Map<String, Object>> actualDetails(Set<Object[]> actual) {
		List<Map<String, Object>> actuals = new ArrayList<>();
		for (Object[] bud : actual) {
			Map<String, Object> b = new HashMap<>();
			b.put("id", Integer.parseInt(bud[0].toString()));
			b.put("accountName", bud[1] != null ? bud[1].toString() : "");
			b.put("accountCode", bud[2] != null ? bud[2].toString() : "");
			b.put("natureOfAccount", bud[3] != null ? bud[3].toString() : "");
			b.put("january", ((Number) bud[4]).intValue());
			b.put("february", ((Number) bud[5]).intValue());
			b.put("march", ((Number) bud[6]).intValue());
			b.put("april", ((Number) bud[7]).intValue());
			b.put("may", ((Number) bud[8]).intValue());
			b.put("june", ((Number) bud[9]).intValue());
			b.put("july", ((Number) bud[10]).intValue());
			b.put("august", ((Number) bud[11]).intValue());
			b.put("september", ((Number) bud[12]).intValue());
			b.put("october", ((Number) bud[13]).intValue());
			b.put("november", ((Number) bud[14]).intValue());
			b.put("december", ((Number) bud[15]).intValue());

			actuals.add(b);
		}
		return actuals;
	}

	@Override
	public List<Map<String, Object>> getElevateYTDTBDetails(Long orgId, String clientCode, String finyear,
			String month) {

		Set<Object[]> ElTBdetails = trialBalanceRepo.getElYTDTbdetails(orgId, clientCode, finyear, month);
		return getELYTDTB(ElTBdetails);
	}

	private List<Map<String, Object>> getELYTDTB(Set<Object[]> elTBdetails) {

		List<Map<String, Object>> YTDTB = new ArrayList<>();
		for (Object[] bud : elTBdetails) {
			Map<String, Object> b = new HashMap<>();
			b.put("id", Integer.parseInt(bud[0].toString()));
			b.put("clientGlCode", bud[1] != null ? bud[1].toString() : "");
			b.put("clientGlLedger", bud[2] != null ? bud[2].toString() : "");
			b.put("elglCode", bud[3] != null ? bud[3].toString() : "");
			b.put("elglLedger", bud[4] != null ? bud[4].toString() : "");
			b.put("opBalDebit", bud[5] != null ? new BigDecimal(bud[5].toString()) : BigDecimal.ZERO);
			b.put("opBalCredit", bud[6] != null ? new BigDecimal(bud[6].toString()) : BigDecimal.ZERO);
			b.put("transDebit", bud[7] != null ? new BigDecimal(bud[7].toString()) : BigDecimal.ZERO);
			b.put("transCredit", bud[8] != null ? new BigDecimal(bud[8].toString()) : BigDecimal.ZERO);
			b.put("clBalDebit", bud[9] != null ? new BigDecimal(bud[9].toString()) : BigDecimal.ZERO);
			b.put("clBalCredit", bud[10] != null ? new BigDecimal(bud[10].toString()) : BigDecimal.ZERO);
			b.put("closingBalance", bud[11] != null ? new BigDecimal(bud[11].toString()) : BigDecimal.ZERO);
			b.put("natureOfAccount", bud[12] != null ? bud[12].toString() : "");
			b.put("segment", bud[13] != null ? bud[13].toString() : "");
			YTDTB.add(b);
		}
		return YTDTB;
	}

	@Override
	public List<Map<String, Object>> getMonthlyProcess(Long orgId, String clientCode, String finyear, String month,
			String yearType, String mainGroupName, String subGroupCode) {
		String previousYear = finyear;
		if (yearType.equals("FY") && month.equals("April")) {
			FinancialYearVO financialYearVO = financialYearRepo.findByOrgIdAndFinYearIdentifierAndYearType(orgId,
					finyear, yearType);
			int preYear = financialYearVO.getFinYear() - 1;
			FinancialYearVO financialYearVO2 = financialYearRepo.findByOrgIdAndFinYearAndYearType(orgId, preYear,
					yearType);
			previousYear = financialYearVO2.getFinYearIdentifier();

		} else if (yearType.equals("CY") && month.equals("January")) {
			FinancialYearVO financialYearVO = financialYearRepo.findByOrgIdAndFinYearIdentifierAndYearType(orgId,
					finyear, yearType);
			int preYear = financialYearVO.getFinYear() - 1;
			FinancialYearVO financialYearVO2 = financialYearRepo.findByOrgIdAndFinYearAndYearType(orgId, preYear,
					yearType);
			previousYear = financialYearVO2.getFinYearIdentifier();
		} else {
			previousYear = finyear;
		}

		Set<Object[]> ElTBdetails = trialBalanceRepo.getElYTDTbdetailsforMonthlyProcess(orgId, clientCode, finyear,
				month, previousYear, mainGroupName, subGroupCode);
		return getMonthlyProcess(ElTBdetails);
	}

	private List<Map<String, Object>> getMonthlyProcess(Set<Object[]> elTBdetails) {

		List<Map<String, Object>> YTDTB = new ArrayList<>();
		for (Object[] bud : elTBdetails) {
			Map<String, Object> b = new HashMap<>();
			b.put("id", Integer.parseInt(bud[0].toString()));
			b.put("elGlCode", bud[1] != null ? bud[1].toString() : "");
			b.put("elGl", bud[2] != null ? bud[2].toString() : "");
			b.put("natureOfAccount", bud[3] != null ? bud[3].toString() : "");
			b.put("segment", bud[4] != null ? bud[4].toString() : "");
			b.put("currentMonthDebit", bud[5] != null ? new BigDecimal(bud[5].toString()) : BigDecimal.ZERO);
			b.put("currentMonthCredit", bud[6] != null ? new BigDecimal(bud[6].toString()) : BigDecimal.ZERO);
			b.put("currentMonthClosing", bud[7] != null ? new BigDecimal(bud[7].toString()) : BigDecimal.ZERO);
			b.put("previousMonthDebit", bud[8] != null ? new BigDecimal(bud[8].toString()) : BigDecimal.ZERO);
			b.put("previousMonthCredit", bud[9] != null ? new BigDecimal(bud[9].toString()) : BigDecimal.ZERO);
			b.put("previousMonthClosing", bud[10] != null ? new BigDecimal(bud[10].toString()) : BigDecimal.ZERO);
			b.put("forTheMonthActDebit", bud[11] != null ? new BigDecimal(bud[11].toString()) : BigDecimal.ZERO);
			b.put("forTheMonthActCredit", bud[12] != null ? new BigDecimal(bud[12].toString()) : BigDecimal.ZERO);
			b.put("forTheMonthActClosing", bud[13] != null ? new BigDecimal(bud[13].toString()) : BigDecimal.ZERO);
			b.put("mismatch", "Yes".equalsIgnoreCase(bud[14] != null ? bud[14].toString() : "No"));
			b.put("provisionDebit", bud[15] != null ? new BigDecimal(bud[15].toString()) : BigDecimal.ZERO);
			b.put("provisionCredit", bud[16] != null ? new BigDecimal(bud[16].toString()) : BigDecimal.ZERO);
			b.put("provisionClosing", bud[17] != null ? new BigDecimal(bud[17].toString()) : BigDecimal.ZERO);
			b.put("forTheMonthDebit", bud[18] != null ? new BigDecimal(bud[18].toString()) : BigDecimal.ZERO);
			b.put("forTheMonthCredit", bud[19] != null ? new BigDecimal(bud[19].toString()) : BigDecimal.ZERO);
			b.put("forTheMonthClosing", bud[20] != null ? new BigDecimal(bud[20].toString()) : BigDecimal.ZERO);
			b.put("approveStatus", "Yes".equalsIgnoreCase(bud[21] != null ? bud[21].toString() : "No"));
			YTDTB.add(b);
		}
		return YTDTB;
	}

	@Override
	public List<Map<String, Object>> getELBudgetReport(Long orgId, String clientCode, String finyear, String yearType,
			String mainGroupName, String subGroupCode) {
		String previousYear = null;
		if (yearType.equals("FY")) {
			FinancialYearVO financialYearVO = financialYearRepo.findByOrgIdAndFinYearIdentifierAndYearType(orgId,
					finyear, yearType);
			int preYear = financialYearVO.getFinYear() - 1;
			FinancialYearVO financialYearVO2 = financialYearRepo.findByOrgIdAndFinYearAndYearType(orgId, preYear,
					yearType);
			previousYear = financialYearVO2.getFinYearIdentifier();

		} else if (yearType.equals("CY")) {
			FinancialYearVO financialYearVO = financialYearRepo.findByOrgIdAndFinYearIdentifierAndYearType(orgId,
					finyear, yearType);
			int preYear = financialYearVO.getFinYear() - 1;
			FinancialYearVO financialYearVO2 = financialYearRepo.findByOrgIdAndFinYearAndYearType(orgId, preYear,
					yearType);
			previousYear = financialYearVO2.getFinYearIdentifier();
		}
		System.out.println(previousYear);

		Set<Object[]> ELBudgetDetails = budgetRepo.getELBudgetDetails(orgId, finyear, previousYear, clientCode,
				mainGroupName, subGroupCode);
		return budgetReport(ELBudgetDetails);
	}

	private List<Map<String, Object>> budgetReport(Set<Object[]> eLBudgetDetails) {

		List<Map<String, Object>> YTDTB = new ArrayList<>();
		for (Object[] bud : eLBudgetDetails) {
			Map<String, Object> b = new HashMap<>();
			b.put("mainGroup", bud[0] != null ? bud[0].toString() : "");
			b.put("subGroup", bud[1] != null ? bud[1].toString() : "");
			b.put("subGroupCode", bud[2] != null ? bud[2].toString() : "");
			b.put("ElGlCode", bud[3] != null ? bud[3].toString() : "");
			b.put("ElGl", bud[4] != null ? bud[4].toString() : "");
			b.put("natureOfAccount", bud[5] != null ? bud[5].toString() : "");
			b.put("quater", bud[6] != null ? bud[6].toString() : "");
			b.put("currentYear", bud[7] != null ? new BigDecimal(bud[7].toString()) : BigDecimal.ZERO);
			b.put("previousYear", bud[8] != null ? new BigDecimal(bud[8].toString()) : BigDecimal.ZERO);
			YTDTB.add(b);
		}
		return YTDTB;
	}

	@Override
	public List<Map<String, Object>> getELPYReport(Long orgId, String finyear, String clientCode, String mainGroupName,
			String subGroupCode, String month) {

		Set<Object[]> ELPYDetails = previousYearActualRepo.getELPYDetails(orgId, finyear, clientCode, mainGroupName,
				subGroupCode, month);
		return PyReport(ELPYDetails);
	}

	private List<Map<String, Object>> PyReport(Set<Object[]> eLPYDetails) {
		List<Map<String, Object>> YTDTB = new ArrayList<>();
		for (Object[] bud : eLPYDetails) {
			Map<String, Object> b = new HashMap<>();
			b.put("mainGroup", bud[0] != null ? bud[0].toString() : "");
			b.put("subGroup", bud[1] != null ? bud[1].toString() : "");
			b.put("subGroupCode", bud[2] != null ? bud[2].toString() : "");
			b.put("ElGlCode", bud[3] != null ? bud[3].toString() : "");
			b.put("ElGl", bud[4] != null ? bud[4].toString() : "");
			b.put("natureOfAccount", bud[5] != null ? bud[5].toString() : "");
			b.put("quater", bud[6] != null ? bud[6].toString() : "");
			b.put("amount", bud[7] != null ? new BigDecimal(bud[7].toString()) : BigDecimal.ZERO);
			YTDTB.add(b);
		}
		return YTDTB;
	}

	@Override
	public List<Map<String, Object>> getELActualReport(Long orgId, String clientCode, String finyear, String yearType,
			String month, String mainGroupName, String subGroupCode) {
		String previousYear = null;
		if (yearType.equals("FY")) {
			FinancialYearVO financialYearVO = financialYearRepo.findByOrgIdAndFinYearIdentifierAndYearType(orgId,
					finyear, yearType);
			int preYear = financialYearVO.getFinYear() - 1;
			FinancialYearVO financialYearVO2 = financialYearRepo.findByOrgIdAndFinYearAndYearType(orgId, preYear,
					yearType);
			previousYear = financialYearVO2.getFinYearIdentifier();

		} else if (yearType.equals("CY")) {
			FinancialYearVO financialYearVO = financialYearRepo.findByOrgIdAndFinYearIdentifierAndYearType(orgId,
					finyear, yearType);
			int preYear = financialYearVO.getFinYear() - 1;
			FinancialYearVO financialYearVO2 = financialYearRepo.findByOrgIdAndFinYearAndYearType(orgId, preYear,
					yearType);
			previousYear = financialYearVO2.getFinYearIdentifier();
		}
		System.out.println(previousYear);

		Set<Object[]> ELactualDetails = budgetRepo.getELActualDetails(orgId, clientCode, finyear, previousYear, month,
				mainGroupName, subGroupCode);
		return ElActual(ELactualDetails);
	}

	private List<Map<String, Object>> ElActual(Set<Object[]> ELactualDetails) {

		List<Map<String, Object>> YTDTB = new ArrayList<>();
		for (Object[] bud : ELactualDetails) {
			Map<String, Object> b = new HashMap<>();
			b.put("elGlCode", bud[3] != null ? bud[3].toString() : "");
			b.put("elGl", bud[4] != null ? bud[4].toString() : "");
			b.put("natureOfAccount", bud[5] != null ? bud[5].toString() : "");
			b.put("budget", bud[6] != null ? new BigDecimal(bud[6].toString()) : BigDecimal.ZERO);
			b.put("actual", bud[7] != null ? new BigDecimal(bud[7].toString()) : BigDecimal.ZERO);
			b.put("py", bud[8] != null ? new BigDecimal(bud[8].toString()) : BigDecimal.ZERO);
			b.put("budgetYTD", bud[9] != null ? new BigDecimal(bud[9].toString()) : BigDecimal.ZERO);
			b.put("ActualYTD", bud[10] != null ? new BigDecimal(bud[10].toString()) : BigDecimal.ZERO);
			b.put("pyYTD", bud[11] != null ? new BigDecimal(bud[11].toString()) : BigDecimal.ZERO);
			b.put("fullBudget", bud[12] != null ? new BigDecimal(bud[12].toString()) : BigDecimal.ZERO);
			b.put("estimate", bud[13] != null ? new BigDecimal(bud[13].toString()) : BigDecimal.ZERO);
			b.put("fullPy", bud[14] != null ? new BigDecimal(bud[14].toString()) : BigDecimal.ZERO);
			YTDTB.add(b);
		}
		return YTDTB;
	}

	@Override
	public List<Map<String, Object>> getELActualQuaterReport(Long orgId, String clientCode, String finyear,
			String yearType, String month, String mainGroupName, String subGroupCode) {
		String previousYear = null;
		if (yearType.equals("FY")) {
			FinancialYearVO financialYearVO = financialYearRepo.findByOrgIdAndFinYearIdentifierAndYearType(orgId,
					finyear, yearType);
			int preYear = financialYearVO.getFinYear() - 1;
			FinancialYearVO financialYearVO2 = financialYearRepo.findByOrgIdAndFinYearAndYearType(orgId, preYear,
					yearType);
			previousYear = financialYearVO2.getFinYearIdentifier();

		} else if (yearType.equals("CY")) {
			FinancialYearVO financialYearVO = financialYearRepo.findByOrgIdAndFinYearIdentifierAndYearType(orgId,
					finyear, yearType);
			int preYear = financialYearVO.getFinYear() - 1;
			FinancialYearVO financialYearVO2 = financialYearRepo.findByOrgIdAndFinYearAndYearType(orgId, preYear,
					yearType);
			previousYear = financialYearVO2.getFinYearIdentifier();
		}
		System.out.println(previousYear);

		Set<Object[]> ELactualDetails = previousYearActualRepo.getELActualQuaterDetails(orgId, clientCode, finyear,
				previousYear, month, mainGroupName, subGroupCode);
		return ElActualQuater(ELactualDetails);
	}

	private List<Map<String, Object>> ElActualQuater(Set<Object[]> ELactualDetails) {

		List<Map<String, Object>> YTDTB = new ArrayList<>();
		for (Object[] bud : ELactualDetails) {
			Map<String, Object> b = new HashMap<>();
			b.put("elGlCode", bud[3] != null ? bud[3].toString() : "");
			b.put("elGl", bud[4] != null ? bud[4].toString() : "");
			b.put("natureOfAccount", bud[5] != null ? bud[5].toString() : "");
			b.put("quater", bud[6] != null ? bud[6].toString() : "");
			b.put("budgetYTD", bud[7] != null ? new BigDecimal(bud[7].toString()) : BigDecimal.ZERO);
			b.put("ActualYTD", bud[8] != null ? new BigDecimal(bud[8].toString()) : BigDecimal.ZERO);
			b.put("pyYTD", bud[9] != null ? new BigDecimal(bud[9].toString()) : BigDecimal.ZERO);
			YTDTB.add(b);
		}
		return YTDTB;
	}

	@Override
	public List<Map<String, Object>> getELActualAutomaticReport(Long orgId, String finyear, String clientCode,
			String mainGroupName, String month, String yearType) {
		String previousYear = null;
		if (yearType.equals("FY")) {
			FinancialYearVO financialYearVO = financialYearRepo.findByOrgIdAndFinYearIdentifierAndYearType(orgId,
					finyear, yearType);
			int preYear = financialYearVO.getFinYear() - 1;
			FinancialYearVO financialYearVO2 = financialYearRepo.findByOrgIdAndFinYearAndYearType(orgId, preYear,
					yearType);
			previousYear = financialYearVO2.getFinYearIdentifier();

		} else if (yearType.equals("CY")) {
			FinancialYearVO financialYearVO = financialYearRepo.findByOrgIdAndFinYearIdentifierAndYearType(orgId,
					finyear, yearType);
			int preYear = financialYearVO.getFinYear() - 1;
			FinancialYearVO financialYearVO2 = financialYearRepo.findByOrgIdAndFinYearAndYearType(orgId, preYear,
					yearType);
			previousYear = financialYearVO2.getFinYearIdentifier();
		}
		System.out.println(previousYear);

		Set<Object[]> ELactualDetails = budgetRepo.getELActualDetailsAutomatic(orgId, finyear, clientCode,
				mainGroupName, month, previousYear);
		return ElActualAutomatic(ELactualDetails);
	}

	private List<Map<String, Object>> ElActualAutomatic(Set<Object[]> ELactualDetails) {

		List<Map<String, Object>> YTDTB = new ArrayList<>();
		for (Object[] bud : ELactualDetails) {
			Map<String, Object> b = new HashMap<>();
			b.put("elGlCode", bud[0] != null ? bud[0].toString() : "");
			b.put("elGl", bud[1] != null ? bud[1].toString() : "");
			b.put("natureOfAccount", "");
			b.put("budget", bud[2] != null ? new BigDecimal(bud[2].toString()) : BigDecimal.ZERO);
			b.put("actual", bud[3] != null ? new BigDecimal(bud[3].toString()) : BigDecimal.ZERO);
			b.put("py", bud[4] != null ? new BigDecimal(bud[4].toString()) : BigDecimal.ZERO);
			b.put("budgetYTD", bud[5] != null ? new BigDecimal(bud[5].toString()) : BigDecimal.ZERO);
			b.put("ActualYTD", bud[6] != null ? new BigDecimal(bud[6].toString()) : BigDecimal.ZERO);
			b.put("pyYTD", bud[7] != null ? new BigDecimal(bud[7].toString()) : BigDecimal.ZERO);
			b.put("fullBudget", bud[8] != null ? new BigDecimal(bud[8].toString()) : BigDecimal.ZERO);
			b.put("estimate", bud[9] != null ? new BigDecimal(bud[9].toString()) : BigDecimal.ZERO);
			b.put("fullPy", bud[10] != null ? new BigDecimal(bud[10].toString()) : BigDecimal.ZERO);
			YTDTB.add(b);
		}
		return YTDTB;
	}

	@Override
	public List<Map<String, Object>> getELActualIncrementalProfitReport(Long orgId, String clientCode, String finyear,
			String yearType) {
		String previousYear = null;
		if (yearType.equals("FY")) {
			FinancialYearVO financialYearVO = financialYearRepo.findByOrgIdAndFinYearIdentifierAndYearType(orgId,
					finyear, yearType);
			int preYear = financialYearVO.getFinYear() - 1;
			FinancialYearVO financialYearVO2 = financialYearRepo.findByOrgIdAndFinYearAndYearType(orgId, preYear,
					yearType);
			previousYear = financialYearVO2.getFinYearIdentifier();

		} else if (yearType.equals("CY")) {
			FinancialYearVO financialYearVO = financialYearRepo.findByOrgIdAndFinYearIdentifierAndYearType(orgId,
					finyear, yearType);
			int preYear = financialYearVO.getFinYear() - 1;
			FinancialYearVO financialYearVO2 = financialYearRepo.findByOrgIdAndFinYearAndYearType(orgId, preYear,
					yearType);
			previousYear = financialYearVO2.getFinYearIdentifier();
		}
		System.out.println(previousYear);

		Set<Object[]> ELactualIncrementalDetails = budgetRepo.getELActualIncrementalProfitReport(orgId, clientCode,
				finyear);
		return ElActualIncrement(ELactualIncrementalDetails);
	}

	private List<Map<String, Object>> ElActualIncrement(Set<Object[]> ELactualIncrementalDetails) {

		List<Map<String, Object>> YTDTB = new ArrayList<>();
		for (Object[] bud : ELactualIncrementalDetails) {
			Map<String, Object> b = new HashMap<>();
			b.put("elGlCode", bud[0] != null ? bud[0].toString() : "");
			b.put("elGl", bud[1] != null ? bud[1].toString() : "");
			b.put("budgetYTD", bud[2] != null ? new BigDecimal(bud[2].toString()) : BigDecimal.ZERO);
			b.put("ActualYTD", bud[3] != null ? new BigDecimal(bud[3].toString()) : BigDecimal.ZERO);
			YTDTB.add(b);
		}
		return YTDTB;
	}

	@Override
	public List<Map<String, Object>> getELActualHeadCountReport(Long orgId, String clientCode, String finyear,
			String yearType, String month) {
		String previousYear = null;
		if (yearType.equals("FY")) {
			FinancialYearVO financialYearVO = financialYearRepo.findByOrgIdAndFinYearIdentifierAndYearType(orgId,
					finyear, yearType);
			int preYear = financialYearVO.getFinYear() - 1;
			FinancialYearVO financialYearVO2 = financialYearRepo.findByOrgIdAndFinYearAndYearType(orgId, preYear,
					yearType);
			previousYear = financialYearVO2.getFinYearIdentifier();

		} else if (yearType.equals("CY")) {
			FinancialYearVO financialYearVO = financialYearRepo.findByOrgIdAndFinYearIdentifierAndYearType(orgId,
					finyear, yearType);
			int preYear = financialYearVO.getFinYear() - 1;
			FinancialYearVO financialYearVO2 = financialYearRepo.findByOrgIdAndFinYearAndYearType(orgId, preYear,
					yearType);
			previousYear = financialYearVO2.getFinYearIdentifier();
		}
		System.out.println(previousYear);

		Set<Object[]> ELactualHeadCountlDetails = budgetRepo.getELActualHeadCountReport(orgId, clientCode, finyear,
				previousYear, month);
		return ElActualHeadCount(ELactualHeadCountlDetails);
	}

	private List<Map<String, Object>> ElActualHeadCount(Set<Object[]> ELactualHeadCountlDetails) {

		List<Map<String, Object>> YTDTB = new ArrayList<>();
		for (Object[] bud : ELactualHeadCountlDetails) {
			Map<String, Object> b = new HashMap<>();
			b.put("department", bud[0] != null ? bud[0].toString() : "");
			b.put("category", bud[1] != null ? bud[1].toString() : "");
			b.put("budgetYTD", bud[2] != null ? new BigDecimal(bud[2].toString()) : BigDecimal.ZERO);
			b.put("ActualYTD", bud[3] != null ? new BigDecimal(bud[3].toString()) : BigDecimal.ZERO);
			b.put("pyYTD", bud[4] != null ? new BigDecimal(bud[4].toString()) : BigDecimal.ZERO);
			YTDTB.add(b);
		}
		return YTDTB;
	}

	@Override
	public List<Map<String, Object>> getELActualARAPReport(Long orgId, String clientCode, String finyear,
			String yearType, String month, String type) {
		String previousYear = null;
		if (yearType.equals("FY")) {
			FinancialYearVO financialYearVO = financialYearRepo.findByOrgIdAndFinYearIdentifierAndYearType(orgId,
					finyear, yearType);
			int preYear = financialYearVO.getFinYear() - 1;
			FinancialYearVO financialYearVO2 = financialYearRepo.findByOrgIdAndFinYearAndYearType(orgId, preYear,
					yearType);
			previousYear = financialYearVO2.getFinYearIdentifier();

		} else if (yearType.equals("CY")) {
			FinancialYearVO financialYearVO = financialYearRepo.findByOrgIdAndFinYearIdentifierAndYearType(orgId,
					finyear, yearType);
			int preYear = financialYearVO.getFinYear() - 1;
			FinancialYearVO financialYearVO2 = financialYearRepo.findByOrgIdAndFinYearAndYearType(orgId, preYear,
					yearType);
			previousYear = financialYearVO2.getFinYearIdentifier();
		}
		System.out.println(previousYear);

		Set<Object[]> ELactualARAPlDetails = budgetRepo.getELActualARAPReport(orgId, clientCode, finyear, type, month);
		return ElActualArAp(ELactualARAPlDetails);
	}

	private List<Map<String, Object>> ElActualArAp(Set<Object[]> ELactualARAPlDetails) {

		List<Map<String, Object>> YTDTB = new ArrayList<>();
		for (Object[] bud : ELactualARAPlDetails) {
			Map<String, Object> b = new HashMap<>();
			b.put("supplier", bud[0] != null ? bud[0].toString() : "");
			b.put("slab1", bud[1] != null ? new BigDecimal(bud[1].toString()) : BigDecimal.ZERO);
			b.put("slab2", bud[2] != null ? new BigDecimal(bud[2].toString()) : BigDecimal.ZERO);
			b.put("slab3", bud[3] != null ? new BigDecimal(bud[3].toString()) : BigDecimal.ZERO);
			b.put("slab4", bud[4] != null ? new BigDecimal(bud[4].toString()) : BigDecimal.ZERO);
			b.put("slab5", bud[5] != null ? new BigDecimal(bud[5].toString()) : BigDecimal.ZERO);
			b.put("slab6", bud[6] != null ? new BigDecimal(bud[6].toString()) : BigDecimal.ZERO);
			YTDTB.add(b);
		}
		return YTDTB;
	}

	@Override
	public List<Map<String, Object>> getELActualRatioAnalysisReport(Long orgId, String finyear, String clientCode,
			String mainGroupName, String month, String yearType, String subGroup) {
		String previousYear = null;
		if (yearType.equals("FY")) {
			FinancialYearVO financialYearVO = financialYearRepo.findByOrgIdAndFinYearIdentifierAndYearType(orgId,
					finyear, yearType);
			int preYear = financialYearVO.getFinYear() - 1;
			FinancialYearVO financialYearVO2 = financialYearRepo.findByOrgIdAndFinYearAndYearType(orgId, preYear,
					yearType);
			previousYear = financialYearVO2.getFinYearIdentifier();

		} else if (yearType.equals("CY")) {
			FinancialYearVO financialYearVO = financialYearRepo.findByOrgIdAndFinYearIdentifierAndYearType(orgId,
					finyear, yearType);
			int preYear = financialYearVO.getFinYear() - 1;
			FinancialYearVO financialYearVO2 = financialYearRepo.findByOrgIdAndFinYearAndYearType(orgId, preYear,
					yearType);
			previousYear = financialYearVO2.getFinYearIdentifier();
		}
		System.out.println(previousYear);

		Set<Object[]> ELactualRatioAnalysisDetails = budgetRepo.getELActualRatioAnalysisReport(orgId, clientCode,
				finyear, previousYear, month, mainGroupName, subGroup);
		return ElActualRatio(ELactualRatioAnalysisDetails);
	}

	private List<Map<String, Object>> ElActualRatio(Set<Object[]> ELactualRatioAnalysisDetails) {

		List<Map<String, Object>> YTDTB = new ArrayList<>();
		for (Object[] bud : ELactualRatioAnalysisDetails) {
			Map<String, Object> b = new HashMap<>();
			b.put("elGl", bud[0] != null ? bud[0].toString() : "");
			b.put("budgetYTD", bud[1] != null ? new BigDecimal(bud[1].toString()) : BigDecimal.ZERO);
			b.put("ActualYTD", bud[2] != null ? new BigDecimal(bud[2].toString()) : BigDecimal.ZERO);
			b.put("pyYTD", bud[3] != null ? new BigDecimal(bud[3].toString()) : BigDecimal.ZERO);
			YTDTB.add(b);
		}
		return YTDTB;
	}

	@Override
	public List<Map<String, Object>> getELActualSalesPurchaseAnalysisReport(Long orgId, String finyear,
			String clientCode, String type, String month, String yearType) {
		String previousYear = null;
		if (yearType.equals("FY")) {
			FinancialYearVO financialYearVO = financialYearRepo.findByOrgIdAndFinYearIdentifierAndYearType(orgId,
					finyear, yearType);
			int preYear = financialYearVO.getFinYear() - 1;
			FinancialYearVO financialYearVO2 = financialYearRepo.findByOrgIdAndFinYearAndYearType(orgId, preYear,
					yearType);
			previousYear = financialYearVO2.getFinYearIdentifier();

		} else if (yearType.equals("CY")) {
			FinancialYearVO financialYearVO = financialYearRepo.findByOrgIdAndFinYearIdentifierAndYearType(orgId,
					finyear, yearType);
			int preYear = financialYearVO.getFinYear() - 1;
			FinancialYearVO financialYearVO2 = financialYearRepo.findByOrgIdAndFinYearAndYearType(orgId, preYear,
					yearType);
			previousYear = financialYearVO2.getFinYearIdentifier();
		}
		System.out.println(previousYear);

		Set<Object[]> ELactualSalesPurchaseAnalysisDetails = budgetRepo.getELActualSalesPurchaseAnalysisReport(orgId,
				clientCode, finyear, previousYear, month, type);
		return ElActualSalesPurchase(ELactualSalesPurchaseAnalysisDetails);
	}

	private List<Map<String, Object>> ElActualSalesPurchase(Set<Object[]> ELactualSalesPurchaseAnalysisDetails) {

		List<Map<String, Object>> YTDTB = new ArrayList<>();
		for (Object[] bud : ELactualSalesPurchaseAnalysisDetails) {
			Map<String, Object> b = new HashMap<>();
			b.put("descrption", bud[0] != null ? bud[0].toString() : "");
			b.put("budgetYTD", bud[1] != null ? new BigDecimal(bud[1].toString()) : BigDecimal.ZERO);
			b.put("actualYTD", bud[2] != null ? new BigDecimal(bud[2].toString()) : BigDecimal.ZERO);
			b.put("pyYTD", bud[3] != null ? new BigDecimal(bud[3].toString()) : BigDecimal.ZERO);
			YTDTB.add(b);
		}
		return YTDTB;
	}

	@Override
	public List<Map<String, Object>> getELBudgetAutomaticReport(Long orgId, String clientCode, String finyear,
			String yearType, String mainGroupName) {
		String previousYear = null;
		if (yearType.equals("FY")) {
			FinancialYearVO financialYearVO = financialYearRepo.findByOrgIdAndFinYearIdentifierAndYearType(orgId,
					finyear, yearType);
			int preYear = financialYearVO.getFinYear() - 1;
			FinancialYearVO financialYearVO2 = financialYearRepo.findByOrgIdAndFinYearAndYearType(orgId, preYear,
					yearType);
			previousYear = financialYearVO2.getFinYearIdentifier();

		} else if (yearType.equals("CY")) {
			FinancialYearVO financialYearVO = financialYearRepo.findByOrgIdAndFinYearIdentifierAndYearType(orgId,
					finyear, yearType);
			int preYear = financialYearVO.getFinYear() - 1;
			FinancialYearVO financialYearVO2 = financialYearRepo.findByOrgIdAndFinYearAndYearType(orgId, preYear,
					yearType);
			previousYear = financialYearVO2.getFinYearIdentifier();
		}
		System.out.println(previousYear);

		Set<Object[]> ELBudgetAutomaticDetails = budgetRepo.getELBudgetAutomaticDetails(orgId, finyear, previousYear,
				clientCode, mainGroupName);
		return budgetAutomaticReport(ELBudgetAutomaticDetails);
	}

	private List<Map<String, Object>> budgetAutomaticReport(Set<Object[]> ELBudgetAutomaticDetails) {

		List<Map<String, Object>> YTDTB = new ArrayList<>();
		for (Object[] bud : ELBudgetAutomaticDetails) {
			Map<String, Object> b = new HashMap<>();
			b.put("elGlCode", bud[0] != null ? bud[0].toString() : "");
			b.put("elGl", bud[1] != null ? bud[1].toString() : "");
			b.put("quater", bud[2] != null ? bud[2].toString() : "");
			b.put("currentYear", bud[4] != null ? new BigDecimal(bud[4].toString()) : BigDecimal.ZERO);
			b.put("previousYear", bud[5] != null ? new BigDecimal(bud[5].toString()) : BigDecimal.ZERO);
			YTDTB.add(b);
		}
		return YTDTB;
	}

	@Override
	public List<Map<String, Object>> getELPyAutomaticReport(Long orgId, String clientCode, String finyear,
			String yearType, String mainGroupName, String month) {
		Set<Object[]> ELPYAutomaticDetails = budgetRepo.getELPyAutomaticDetails(orgId, finyear, clientCode,
				mainGroupName, month);
		return pyAutomaticReport(ELPYAutomaticDetails);
	}

	private List<Map<String, Object>> pyAutomaticReport(Set<Object[]> ELPYAutomaticDetails) {

		List<Map<String, Object>> YTDTB = new ArrayList<>();
		for (Object[] bud : ELPYAutomaticDetails) {
			Map<String, Object> b = new HashMap<>();
			b.put("elGlCode", bud[0] != null ? bud[0].toString() : "");
			b.put("elGl", bud[1] != null ? bud[1].toString() : "");
			b.put("quater", bud[2] != null ? bud[2].toString() : "");
			b.put("previousYear", bud[4] != null ? new BigDecimal(bud[4].toString()) : BigDecimal.ZERO);
			YTDTB.add(b);
		}
		return YTDTB;
	}

	@Override
	public List<Map<String, Object>> getELPyRatioAnalaysisReport(Long orgId, String clientCode, String finyear,
			String yearType, String mainGroupName, String subGroupName, String month) {
		Set<Object[]> ELPYRatioAnalysisDetails = budgetRepo.getELPyRatioAnalysisDetails(orgId, finyear, clientCode,
				mainGroupName, subGroupName, month);
		return pyRatioAnalysisReport(ELPYRatioAnalysisDetails);
	}

	private List<Map<String, Object>> pyRatioAnalysisReport(Set<Object[]> ELPYRatioAnalysisDetails) {

		List<Map<String, Object>> YTDTB = new ArrayList<>();
		for (Object[] bud : ELPYRatioAnalysisDetails) {
			Map<String, Object> b = new HashMap<>();
			b.put("elGl", bud[0] != null ? bud[0].toString() : "");
			b.put("quater", bud[1] != null ? bud[1].toString() : "");
			b.put("previousYear", bud[2] != null ? new BigDecimal(bud[2].toString()) : BigDecimal.ZERO);
			YTDTB.add(b);
		}
		return YTDTB;
	}

	@Override
	public List<Map<String, Object>> getELBudgetHCReport(Long orgId, String finyear, String clientCode,
			String PreviousYear) {
		Set<Object[]> ELBudgetHCDetails = budgetHeadCountRepo.getELBudgetHCDetails(orgId, finyear, clientCode,
				PreviousYear);
		return budgetHCAnalysisReport(ELBudgetHCDetails);
	}

	private List<Map<String, Object>> budgetHCAnalysisReport(Set<Object[]> ELBudgetHCDetails) {

		List<Map<String, Object>> YTDTB = new ArrayList<>();
		for (Object[] bud : ELBudgetHCDetails) {
			Map<String, Object> b = new HashMap<>();
			b.put("department", bud[0] != null ? bud[0].toString() : "");
			b.put("category", bud[1] != null ? bud[1].toString() : "");
			b.put("quater", bud[2] != null ? bud[2].toString() : "");
			b.put("type", bud[3] != null ? bud[3].toString() : "");
			b.put("budget", bud[4] != null ? new BigDecimal(bud[4].toString()) : BigDecimal.ZERO);
			b.put("py", bud[5] != null ? new BigDecimal(bud[5].toString()) : BigDecimal.ZERO);
			YTDTB.add(b);
		}
		return YTDTB;
	}

	@Override
	public List<Map<String, Object>> getELBudgetOBReport(Long orgId, String finYear, String clientCode,
			String previousYear) {
		Set<Object[]> ELBudgetOBDetails = orderBookingRepo.getElBudgetOrderBookingDetails(orgId, finYear, clientCode,
				previousYear);
		return budgetOBReport(ELBudgetOBDetails);
	}

	private List<Map<String, Object>> budgetOBReport(Set<Object[]> ELBudgetOBDetails) {

		List<Map<String, Object>> YTDTB = new ArrayList<>();
		for (Object[] bud : ELBudgetOBDetails) {
			Map<String, Object> b = new HashMap<>();
			b.put("segment", bud[0] != null ? bud[0].toString() : "");
			b.put("quater", bud[1] != null ? bud[1].toString() : "");
			b.put("budget", bud[2] != null ? new BigDecimal(bud[2].toString()) : BigDecimal.ZERO);
			b.put("py", bud[3] != null ? new BigDecimal(bud[3].toString()) : BigDecimal.ZERO);
			YTDTB.add(b);
		}
		return YTDTB;
	}
	
	@Override
	public List<Map<String, Object>> getELBudgetRatioAnalysisDetails(Long orgId, String finyear, String clientCode,
			String PreviousYear) {
		Set<Object[]> ELBudgetRatioDetails = budgetRepo.getELBudgetRatioAnalysisDetails(orgId, finyear, clientCode,
				PreviousYear);
		return budgetRatioAnalysisReport(ELBudgetRatioDetails);
	}

	private List<Map<String, Object>> budgetRatioAnalysisReport(Set<Object[]> ELBudgetRatioDetails) {

		List<Map<String, Object>> YTDTB = new ArrayList<>();
		for (Object[] bud : ELBudgetRatioDetails) {
			Map<String, Object> b = new HashMap<>();
			b.put("accountName", bud[0] != null ? bud[0].toString() : "");
			b.put("quater", bud[1] != null ? bud[1].toString() : "");
			b.put("budget", bud[2] != null ? new BigDecimal(bud[2].toString()) : BigDecimal.ZERO);
			b.put("py", bud[3] != null ? new BigDecimal(bud[3].toString()) : BigDecimal.ZERO);
			YTDTB.add(b);
		}
		return YTDTB;
	}
	
	@Override
	public List<Map<String, Object>> getELBudgetSalesPurchaseAnalysisDetails(Long orgId, String finyear, String clientCode,
			String PreviousYear,String type) {
		Set<Object[]> ELBudgetSalesPurchaseDetails = budgetRepo.getELBudgetSalesPurchaseAnalysisDetails(orgId, finyear, clientCode,
				PreviousYear,type);
		return budgetSalesPurchaseAnalysisReport(ELBudgetSalesPurchaseDetails);
	}

	private List<Map<String, Object>> budgetSalesPurchaseAnalysisReport(Set<Object[]> ELBudgetSalesPurchaseDetails) {

		List<Map<String, Object>> YTDTB = new ArrayList<>();
		for (Object[] bud : ELBudgetSalesPurchaseDetails) {
			Map<String, Object> b = new HashMap<>();
			b.put("description", bud[0] != null ? bud[0].toString() : "");
			b.put("natureOfAccount", bud[1] != null ? bud[1].toString() : "");
			b.put("quater", bud[2] != null ? bud[2].toString() : "");
			b.put("budget", bud[3] != null ? new BigDecimal(bud[3].toString()) : BigDecimal.ZERO);
			b.put("py", bud[4] != null ? new BigDecimal(bud[4].toString()) : BigDecimal.ZERO);
			YTDTB.add(b);
		}
		return YTDTB;
	}

	@Override
	public List<Map<String, Object>> getELPyOBReport(Long orgId, String previousYear, String clientCode,String month) {
		Set<Object[]> ELPyOBDetails = orderBookingRepo.getElPyOrderBookingDetails(orgId, previousYear, clientCode,month);
		return pyOBReport(ELPyOBDetails);
	}

	private List<Map<String, Object>> pyOBReport(Set<Object[]> ELPyOBDetails) {

		List<Map<String, Object>> YTDTB = new ArrayList<>();
		for (Object[] bud : ELPyOBDetails) {
			Map<String, Object> b = new HashMap<>();
			b.put("segment", bud[0] != null ? bud[0].toString() : "");
			b.put("quater", bud[1] != null ? bud[1].toString() : "");
			b.put("py", bud[2] != null ? new BigDecimal(bud[2].toString()) : BigDecimal.ZERO);
			YTDTB.add(b);
		}
		return YTDTB;
	}
	
	@Override
	public List<Map<String, Object>> getELPyHCReport(Long orgId, String PreviousYear, String clientCode,String month) {
		Set<Object[]> getELPyHCReport = budgetHeadCountRepo.getELPyHCDetails(orgId, PreviousYear, clientCode,month);
		return pyHCAnalysisReport(getELPyHCReport);
	}

	private List<Map<String, Object>> pyHCAnalysisReport(Set<Object[]> getELPyHCReport) {

		List<Map<String, Object>> YTDTB = new ArrayList<>();
		for (Object[] bud : getELPyHCReport) {
			Map<String, Object> b = new HashMap<>();
			b.put("department", bud[0] != null ? bud[0].toString() : "");
			b.put("category", bud[1] != null ? bud[1].toString() : "");
			b.put("quater", bud[2] != null ? bud[2].toString() : "");
			b.put("type", bud[3] != null ? bud[3].toString() : "");
			b.put("py", bud[4] != null ? new BigDecimal(bud[4].toString()) : BigDecimal.ZERO);
			YTDTB.add(b);
		}
		return YTDTB;
	}
	
	@Override
	public List<Map<String, Object>> getELPySalesPurchaseAnalysisDetails(Long orgId, String PreviousYear, String clientCode,String type,String month) {
		Set<Object[]> ELPySalesPurchaseDetails = budgetRepo.getELPySalesPurchaseAnalysisDetails(orgId, PreviousYear, clientCode,type,month);
		return pySalesPurchaseAnalysisReport(ELPySalesPurchaseDetails);
	}

	private List<Map<String, Object>> pySalesPurchaseAnalysisReport(Set<Object[]> ELPySalesPurchaseDetails) {

		List<Map<String, Object>> YTDTB = new ArrayList<>();
		for (Object[] bud : ELPySalesPurchaseDetails) {
			Map<String, Object> b = new HashMap<>();
			b.put("description", bud[0] != null ? bud[0].toString() : "");
			b.put("natureOfAccount", bud[1] != null ? bud[1].toString() : "");
			b.put("quater", bud[2] != null ? bud[2].toString() : "");
			b.put("py", bud[3] != null ? new BigDecimal(bud[3].toString()) : BigDecimal.ZERO);
			YTDTB.add(b);
		}
		return YTDTB;
	}

	@Override
	public List<Map<String, Object>> getELActualOBReport(Long orgId, String clientCode, String finYear, String month,
			String previousYear, String type) {
		Set<Object[]>obdetails=budgetRepo.getELActualOrderBooking(orgId,clientCode,finYear,month,previousYear,type);
		return ELActualOB(obdetails);
	}
	
	private List<Map<String, Object>> ELActualOB(Set<Object[]> ELactualDetails) {

		List<Map<String, Object>> YTDTB = new ArrayList<>();
		for (Object[] bud : ELactualDetails) {
			Map<String, Object> b = new HashMap<>();
			b.put("customerName", bud[0] != null ? bud[0].toString() : "");
			b.put("budget", bud[1] != null ? new BigDecimal(bud[1].toString()) : BigDecimal.ZERO);
			b.put("actual", bud[2] != null ? new BigDecimal(bud[2].toString()) : BigDecimal.ZERO);
			b.put("py", bud[3] != null ? new BigDecimal(bud[3].toString()) : BigDecimal.ZERO);
			b.put("budgetYTD", bud[4] != null ? new BigDecimal(bud[4].toString()) : BigDecimal.ZERO);
			b.put("ActualYTD", bud[5] != null ? new BigDecimal(bud[5].toString()) : BigDecimal.ZERO);
			b.put("pyYTD", bud[6] != null ? new BigDecimal(bud[6].toString()) : BigDecimal.ZERO);
			b.put("fullBudget", bud[7] != null ? new BigDecimal(bud[7].toString()) : BigDecimal.ZERO);
			b.put("fullActual", bud[8] != null ? new BigDecimal(bud[8].toString()) : BigDecimal.ZERO);
			b.put("fullPy", bud[9] != null ? new BigDecimal(bud[9].toString()) : BigDecimal.ZERO);
			YTDTB.add(b);
		}
		return YTDTB;
	}

}
