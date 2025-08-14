package com.base.basesetup.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.base.basesetup.dto.CustomerAttachmentType;
import com.base.basesetup.dto.QrBarCodeDTO;
import com.base.basesetup.dto.QrBarCodeDetailsDTO;
import com.base.basesetup.dto.SingleQrBarCodeDTO;
import com.base.basesetup.entity.DocumentTypeMappingDetailsVO;
import com.base.basesetup.entity.QrBarCodeDetailsVO;
import com.base.basesetup.entity.QrBarCodeVO;
import com.base.basesetup.entity.QrBarExcelUploadVO;
import com.base.basesetup.entity.SingleQrBarCodeVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.DocumentTypeMappingDetailsRepo;
import com.base.basesetup.repo.QrBarCodeDetailsRepo;
import com.base.basesetup.repo.QrBarCodeRepo;
import com.base.basesetup.repo.QrBarExcelUploadRepo;
import com.base.basesetup.repo.SingleQrBarCodeRepo;

@Service
public class QrBarCodeServiceImpl implements QrBarCodeService {

	public static final Logger LOGGER = LoggerFactory.getLogger(QrBarCodeServiceImpl.class);

	@Autowired
	QrBarCodeRepo qrBarCodeRepo;

	@Autowired
	QrBarCodeDetailsRepo qrBarCodeDetailsRepo;

	@Autowired
	QrBarExcelUploadRepo qrBarExcelUploadRepo;

	@Autowired
	SingleQrBarCodeRepo singleQrBarCodeRepo;

	@Autowired
	DocumentTypeMappingDetailsRepo documentTypeMappingDetailsRepo;

	// QrBarCdoe

	@Override
	public List<QrBarCodeVO> getAllQrBarCodeByOrgId(Long orgId, String finyear, String branchCode) {

		return qrBarCodeRepo.getAllQrBarCodeByOrgId(orgId, finyear, branchCode);
	}

	@Override
	public QrBarCodeVO getQrBarCodeById(Long id) {

		return qrBarCodeRepo.getQrBarCodeById(id);
	}

	@Override
	public Map<String, Object> createUpdateQrBarCode(@Valid QrBarCodeDTO qrBarCodeDTO) throws ApplicationException {
		String screenCode = "QRBAR";
		QrBarCodeVO qrBarCodeVO = new QrBarCodeVO();
		String message;
		if (ObjectUtils.isNotEmpty(qrBarCodeDTO.getId())) {
			qrBarCodeVO = qrBarCodeRepo.findById(qrBarCodeDTO.getId())
					.orElseThrow(() -> new ApplicationException("Invalid QrBarCode details"));
			qrBarCodeVO.setUpdatedBy(qrBarCodeDTO.getCreatedBy());
			createUpdateQrBarCodeVOByQrBarCodeDTO(qrBarCodeDTO, qrBarCodeVO);
			message = "QrBarCode Updated Successfully";
		} else {
			// GETDOCID API
			createUpdateQrBarCodeVOByQrBarCodeDTO(qrBarCodeDTO, qrBarCodeVO);
			String docId = qrBarCodeRepo.getQrBarCodeDocId(qrBarCodeDTO.getOrgId(), qrBarCodeDTO.getFinYear(),
					qrBarCodeDTO.getBranchCode(), screenCode);
			qrBarCodeVO.setDocId(docId);

			// GETDOCID LASTNO +1
			DocumentTypeMappingDetailsVO documentTypeMappingDetailsVO = documentTypeMappingDetailsRepo
					.findByOrgIdAndFinYearAndBranchCodeAndScreenCode(qrBarCodeDTO.getOrgId(), qrBarCodeDTO.getFinYear(),
							qrBarCodeDTO.getBranchCode(), screenCode);
			documentTypeMappingDetailsVO.setLastno(documentTypeMappingDetailsVO.getLastno() + 1);
			documentTypeMappingDetailsRepo.save(documentTypeMappingDetailsVO);

			qrBarCodeVO.setUpdatedBy(qrBarCodeDTO.getCreatedBy());
			qrBarCodeVO.setCreatedBy(qrBarCodeDTO.getCreatedBy());
//				createUpdateCostEstimationVOByCostEstimationDTO(costEstimationDTO, costEstimationVO);
			message = "QrBarCode Created Successfully";
		}

		qrBarCodeRepo.save(qrBarCodeVO);
		Map<String, Object> response = new HashMap<>();
		response.put("qrBarCodeVO", qrBarCodeVO);
		response.put("message", message);
		return response;
	}

	private void createUpdateQrBarCodeVOByQrBarCodeDTO(@Valid QrBarCodeDTO qrBarCodeDTO, QrBarCodeVO qrBarCodeVO)
			throws ApplicationException {
		qrBarCodeVO.setBranch(qrBarCodeDTO.getBranch());
		qrBarCodeVO.setBranchCode(qrBarCodeDTO.getBranchCode());
		qrBarCodeVO.setCreatedBy(qrBarCodeDTO.getCreatedBy());
		qrBarCodeVO.setFinYear(qrBarCodeDTO.getFinYear());
		qrBarCodeVO.setEntryNo(qrBarCodeDTO.getEntryNo());
		qrBarCodeVO.setCount(qrBarCodeDTO.getCount());
		qrBarCodeVO.setOrgId(qrBarCodeDTO.getOrgId());
		qrBarCodeVO.setCancel(qrBarCodeDTO.isCancel());
		qrBarCodeVO.setCancelRemarks(qrBarCodeDTO.getCancelRemarks());
		qrBarCodeVO.setActive(qrBarCodeDTO.isActive());

		if (ObjectUtils.isNotEmpty(qrBarCodeVO.getId())) {
			List<QrBarCodeDetailsVO> qrBarCodeDetailsVO1 = qrBarCodeDetailsRepo.findByQrBarCodeVO(qrBarCodeVO);
			qrBarCodeDetailsRepo.deleteAll(qrBarCodeDetailsVO1);
		}

		List<QrBarCodeDetailsVO> qrBarCodeDetailsVOs = new ArrayList<>();
		for (QrBarCodeDetailsDTO qrBarCodeDetailsDTO : qrBarCodeDTO.getQrBarCodeDetailsDTO()) {

			QrBarCodeDetailsVO qrBarCodeDetailsVO = new QrBarCodeDetailsVO();
			qrBarCodeDetailsVO.setPartNo(qrBarCodeDetailsDTO.getPartNo());
			qrBarCodeDetailsVO.setPartDescription(qrBarCodeDetailsDTO.getPartDescription());
			qrBarCodeDetailsVO.setBatchNo(qrBarCodeDetailsDTO.getBatchNo());
			qrBarCodeDetailsVO.setBarCodeValue(qrBarCodeDetailsDTO.getBarCodeValue());
			qrBarCodeDetailsVO.setQrCodeValue(qrBarCodeDetailsDTO.getQrCodeValue());

			qrBarCodeDetailsVO.setQrBarCodeVO(qrBarCodeVO);
			qrBarCodeDetailsVOs.add(qrBarCodeDetailsVO);

		}
		qrBarCodeVO.setQrBarCodeDetailsVO(qrBarCodeDetailsVOs);
	}

	@Override
	public String getQrBarCodeDocId(Long orgId, String finYear, String branch, String branchCode) {
		String ScreenCode = "QRBAR";
		return qrBarCodeRepo.getQrBarCodeDocId(orgId, finYear, branchCode, ScreenCode);

	}

	private int totalRows = 0; // Instance variable to keep track of total rows
	private int successfulUploads = 0; // Instance variable to keep track of successful uploads

	private final DataFormatter dataFormatter = new DataFormatter();

	@Transactional
	public void ExcelUploadForQrBarCode(MultipartFile[] files, CustomerAttachmentType type, String createdBy)
			throws ApplicationException {
		List<QrBarExcelUploadVO> qrBarExcelUploadVOsToSave = new ArrayList<>();
		totalRows = 0;
		successfulUploads = 0;

		for (MultipartFile file : files) {
			if (file.isEmpty()) {
				throw new ApplicationException(
						"The supplied file '" + file.getOriginalFilename() + "' is empty (zero bytes long).");
			}

			try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
				Sheet sheet = workbook.getSheetAt(0);
				List<String> errorMessages = new ArrayList<>();
				System.out.println("Processing file: " + file.getOriginalFilename());

				Row headerRow = sheet.getRow(0);
				if (!isHeaderValid(headerRow)) {
					throw new ApplicationException("Invalid Excel format in file '" + file.getOriginalFilename()
							+ "'. Expected headers are: Type, From location, From location type, Location pick, partno, partdesc, sku, Grn No, GRN date, Batch No, Exp date, Entry no, From Status, To Status");
				}

				for (Row row : sheet) {
					if (row.getRowNum() == 0 || isRowEmpty(row)) {
						continue;
					}

					totalRows++;
					System.out.println("Validating row: " + (row.getRowNum() + 1));

					try {

						String entryno = getStringCellValue(row.getCell(0));
						String partno = getStringCellValue(row.getCell(1));
						String partdescription = getStringCellValue(row.getCell(2));
						String batchno = getStringCellValue(row.getCell(3));

//	                        if (qrBarExcelUploadRepo.existsByEntryNoAndPartNo(entryno, partno)) {
//	                            // If duplicate is found, throw an ApplicationException
//	                            String errorMessage = String.format("This PartNo: %s already exists for EntryNo: %s.", partno, entryno);
//	                            throw new ApplicationException(errorMessage);
//	                        }else {

						// Create and populate SrsExcelUploadVO object
						QrBarExcelUploadVO qrBarExcelUploadVO = new QrBarExcelUploadVO();
						qrBarExcelUploadVO.setEntryNo(entryno);
						qrBarExcelUploadVO.setPartNo(partno);
						qrBarExcelUploadVO.setPartDescription(partdescription);
						qrBarExcelUploadVO.setBatchNo(batchno);

						qrBarExcelUploadVO.setCreatedBy(createdBy);
						qrBarExcelUploadVO.setUpdatedBy(createdBy);
						qrBarExcelUploadVO.setActive(true);
						qrBarExcelUploadVO.setCancel(false);
						qrBarExcelUploadVO.setCancelRemarks("");

						qrBarExcelUploadVOsToSave.add(qrBarExcelUploadVO);
						successfulUploads++;

					} catch (Exception e) {
						System.err.println("Application error at row " + (row.getRowNum() + 1) + ": " + e.getMessage());
						throw e;
					}
				}

				qrBarExcelUploadRepo.saveAll(qrBarExcelUploadVOsToSave);
			} catch (IOException e) {
				throw new ApplicationException(
						"Failed to process file: " + file.getOriginalFilename() + " - " + e.getMessage());
			}
		}
	}

	private LocalDate parseDate(Cell cell) {
		if (cell == null) {
			return null;
		}

		try {
			if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
				return cell.getLocalDateTimeCellValue().toLocalDate();
			} else if (cell.getCellType() == CellType.STRING) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Adjusted to dd/MM/yyyy
				return LocalDate.parse(cell.getStringCellValue(), formatter);
			}
		} catch (Exception e) {
			System.err.println("Date parsing error for cell value: " + getStringCellValue(cell));
		}
		return null;
	}

	private String getStringCellValue(Cell cell) {
		if (cell == null) {
			return ""; // Return empty string if cell is null
		}

		// Use DataFormatter to get the cell value as a string
		return dataFormatter.formatCellValue(cell);
	}

	private boolean isRowEmpty(Row row) {
		for (Cell cell : row) {
			if (cell.getCellType() != CellType.BLANK) {
				return false;
			}
		}
		return true;
	}

	private boolean isHeaderValid(Row headerRow) throws ApplicationException {
	    if (headerRow == null) return false;

	    List<String> expectedHeaders = Arrays.asList("entryno", "partno", "partdescription", "batchno");

	    List<String> actualHeaders = new ArrayList<>();
	    for (Cell cell : headerRow) {
	        actualHeaders.add(getStringCellValue(cell).trim().toLowerCase());
	    }

	    if (!expectedHeaders.equals(actualHeaders)) {
	        throw new ApplicationException("Invalid Excel format. Expected headers: " 
	                + expectedHeaders + ", Found headers: " + actualHeaders);
	    }
	    return true;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public int getSuccessfulUploads() {
		return successfulUploads;
	}

	@Override
	@Transactional
	public List<Map<String, Object>> getFillGridFromQrBarExcelUpload(String entryNo) {

		Set<Object[]> result = qrBarCodeRepo.findFillGridFromQrBarExcelUpload(entryNo);
		return getFillGridForQrBarCode(result);
	}

	private List<Map<String, Object>> getFillGridForQrBarCode(Set<Object[]> result) {
		List<Map<String, Object>> details1 = new ArrayList<>();
		for (Object[] fs : result) {
			Map<String, Object> part = new HashMap<>();
			part.put("partNo", fs[0] != null ? fs[0].toString() : "");
			part.put("partDesc", fs[1] != null ? fs[1].toString() : "");
			part.put("batchNo", fs[2] != null ? fs[2].toString() : "");
			part.put("id", fs[3] != null ? Integer.parseInt(fs[3].toString()) : 0);

			details1.add(part);
		}
		return details1;
	}

	// SingleQrBarCode

	@Override
	public List<SingleQrBarCodeVO> getAllSingleQrBarCode(Long orgId) {
		return singleQrBarCodeRepo.findAllSingleQrBarCode(orgId);

	}

	@Override
	public SingleQrBarCodeVO getSingleQrBarCodeById(Long id) {
			return singleQrBarCodeRepo.findSingleQrBarCodeById(id);
	}

	@Override
	public Map<String, Object> createUpdateSingleQrBarCode(SingleQrBarCodeDTO singleQrBarCodeDTO)
			throws ApplicationException {
		SingleQrBarCodeVO singleQrBarCodeVO = new SingleQrBarCodeVO();
		String message;

		if (ObjectUtils.isNotEmpty(singleQrBarCodeDTO.getId())) {
			singleQrBarCodeVO = singleQrBarCodeRepo.findById(singleQrBarCodeDTO.getId())
					.orElseThrow(() -> new ApplicationException("Invalid QrBarCode details"));

			
			singleQrBarCodeVO.setUpdatedBy(singleQrBarCodeDTO.getCreatedBy());
			
			if (!singleQrBarCodeVO.getQrBarCodeValue().equalsIgnoreCase(singleQrBarCodeDTO.getQrBarCodeValue())) {
				if (singleQrBarCodeRepo.existsByQrBarCodeValueAndOrgId(singleQrBarCodeDTO.getQrBarCodeValue(), singleQrBarCodeDTO.getOrgId())) {
					String errorMessage = String.format("This QrBarCodeValue: %s Already Exists in This Organization",
							singleQrBarCodeDTO.getQrBarCodeValue());
					throw new ApplicationException(errorMessage);
				}
				singleQrBarCodeVO.setQrBarCodeValue(singleQrBarCodeDTO.getQrBarCodeValue().toUpperCase());
			}

			createUpdateSingleQrBarCodeVOBySingleQrBarCodeDTO(singleQrBarCodeDTO, singleQrBarCodeVO);

			message = "SingleQrBarCode updated Successfully";

		} else {

			if (singleQrBarCodeRepo.existsByQrBarCodeValueAndOrgId(singleQrBarCodeDTO.getQrBarCodeValue(), singleQrBarCodeDTO.getOrgId())) {
				String errorMessage = String.format("This QrBarCode: %s Already Exists in This Organization",
						singleQrBarCodeDTO.getQrBarCodeValue());
				throw new ApplicationException(errorMessage);
			}
			createUpdateSingleQrBarCodeVOBySingleQrBarCodeDTO(singleQrBarCodeDTO, singleQrBarCodeVO);

			singleQrBarCodeVO.setUpdatedBy(singleQrBarCodeDTO.getCreatedBy());
			singleQrBarCodeVO.setCreatedBy(singleQrBarCodeDTO.getCreatedBy());
			message = "SingleQrBarCode created Successfully";

		}

	  singleQrBarCodeRepo.save(singleQrBarCodeVO);
		Map<String, Object> response = new HashMap<>();
		response.put("singleQrBarCodeVO", singleQrBarCodeVO);
		response.put("message", message);
		return response;

	}

	private void createUpdateSingleQrBarCodeVOBySingleQrBarCodeDTO(@Valid SingleQrBarCodeDTO singleQrBarCodeDTO,
			SingleQrBarCodeVO singleQrBarCodeVO) throws ApplicationException {

		singleQrBarCodeVO.setQrBarCodeValue(singleQrBarCodeDTO.getQrBarCodeValue().toUpperCase());
		singleQrBarCodeVO.setCount(singleQrBarCodeDTO.getCount());
		singleQrBarCodeVO.setOrgId(singleQrBarCodeDTO.getOrgId());
		singleQrBarCodeVO.setCancel(singleQrBarCodeDTO.isCancel());
		singleQrBarCodeVO.setCancelRemarks(singleQrBarCodeDTO.getCancelRemarks());
		singleQrBarCodeVO.setActive(singleQrBarCodeDTO.isActive());

	}
}
