package com.base.basesetup.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.base.basesetup.dto.StockBranchDTO;
import com.base.basesetup.dto.WarehouseDTO;
import com.base.basesetup.entity.AssetVO;
import com.base.basesetup.entity.CityVO;
import com.base.basesetup.entity.StateVO;
import com.base.basesetup.entity.StockBranchVO;
import com.base.basesetup.entity.WarehouseVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.AssetRepo;
import com.base.basesetup.repo.CityRepo;
import com.base.basesetup.repo.StateRepo;
import com.base.basesetup.repo.StockBranchRepo;
import com.base.basesetup.repo.WarehouseRepo;

import io.jsonwebtoken.io.IOException;

@Service
public class WareHouseServiceImpl implements WareHouseService {

	public static final Logger LOGGER = LoggerFactory.getLogger(CommonMasterServiceImpl.class);

	@Autowired
	StockBranchRepo stockBranchRepo;

	@Autowired
	WarehouseRepo warehouseRepo;
	
	@Autowired
	CityRepo cityRepo;
	
	@Autowired
	StateRepo stateRepo;
	
	@Autowired
	AssetRepo assetRepo;

	@Override
	public Map<String, Object> createupdateStockBranch(StockBranchDTO stockBranchDTO) throws ApplicationException {
		StockBranchVO stockBranchVO;
		String message;

		if (ObjectUtils.isEmpty(stockBranchDTO.getId())) {
			if (stockBranchRepo.existsByBranchAndOrgId(stockBranchDTO.getBranch(), stockBranchDTO.getOrgId())) {
				String errorMessage = String.format("The StockBranch: %s already exists in this organization.",
						stockBranchDTO.getBranch());
				throw new ApplicationException(errorMessage);
			}
			if (stockBranchRepo.existsBybranchCodeAndOrgId(stockBranchDTO.getBranchCode(), stockBranchDTO.getOrgId())) {
				String errorMessage = String.format("The BranchCode: %s already exists in this organization.",
						stockBranchDTO.getBranchCode());
				throw new ApplicationException(errorMessage);
			}

			stockBranchVO = new StockBranchVO();
			stockBranchVO.setCreatedBy(stockBranchDTO.getCreatedBy());
			stockBranchVO.setUpdatedBy(stockBranchDTO.getCreatedBy());
			message = "StockBranch Created Successfully";
		} else {

			stockBranchVO = stockBranchRepo.findById(stockBranchDTO.getId()).orElseThrow(
					() -> new ApplicationException("StockBranch not found with id: " + stockBranchDTO.getId()));
			stockBranchVO.setUpdatedBy(stockBranchDTO.getCreatedBy());

			if (!stockBranchVO.getBranch().equalsIgnoreCase(stockBranchDTO.getBranch())) {
				if (stockBranchRepo.existsByBranchAndOrgId(stockBranchDTO.getBranch(), stockBranchDTO.getOrgId())) {
					String errorMessage = String.format("The Branch: %s already exists in this organization.",
							stockBranchDTO.getBranch());
					throw new ApplicationException(errorMessage);
				}
				stockBranchVO.setBranch(stockBranchDTO.getBranch().toUpperCase());
			}

			if (!stockBranchVO.getBranchCode().equalsIgnoreCase(stockBranchDTO.getBranchCode())) {
				if (stockBranchRepo.existsBybranchCodeAndOrgId(stockBranchDTO.getBranchCode(),
						stockBranchDTO.getOrgId())) {
					String errorMessage = String.format("The BranchCode: %s already exists in this organization.",
							stockBranchDTO.getBranchCode());
					throw new ApplicationException(errorMessage);
				}
				stockBranchVO.setBranchCode(stockBranchDTO.getBranchCode().toUpperCase());
			}
			message = "StockBranch Updated Successfully";
		}

		getStockBranchVOFromStockBranchDTO(stockBranchVO, stockBranchDTO);
		stockBranchRepo.save(stockBranchVO);

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("stockBranchVO", stockBranchVO);
		return response;
	}

	private void getStockBranchVOFromStockBranchDTO(StockBranchVO stockBranchVO, StockBranchDTO stockBranchDTO) {
		stockBranchVO.setOrgId(stockBranchDTO.getOrgId());
		stockBranchVO.setActive(stockBranchDTO.isActive());
		stockBranchVO.setBranch(stockBranchDTO.getBranch().toUpperCase());
		stockBranchVO.setBranchCode(stockBranchDTO.getBranchCode().toUpperCase());
	}

	@Override
	public List<StockBranchVO> getAllStockBranchByOrgId(Long orgId) {

		return stockBranchRepo.getAllStockBranchByOrgId(orgId);
	}

	@Override
	public StockBranchVO getStockBranchById(Long id) {

		return stockBranchRepo.getStockBranchById(id);
	}

	// WareHouse

	@Override
	public Map<String, Object> createupdateWarehouse(WarehouseDTO warehouseDTO) throws ApplicationException {
		WarehouseVO warehouseVO;
		String message;

		String concatName = warehouseDTO.getLocationName().toUpperCase() + "-"
				+ warehouseDTO.getLocationUnit().toUpperCase();

		if (ObjectUtils.isEmpty(warehouseDTO.getId())) {

			if (warehouseRepo.existsByLocationUnitAndOrgId(warehouseDTO.getLocationUnit(), warehouseDTO.getOrgId())) {
				String errorMessage = String.format(
						"The LocationName: %s and LocationUnit: %s already exist for this Organization",
						warehouseDTO.getLocationName(), warehouseDTO.getLocationUnit());
				throw new ApplicationException(errorMessage);
			}

			if (warehouseRepo.existsByNameAndOrgId(concatName, warehouseDTO.getOrgId())) {
				String errorMessage = String.format("The Name: %s already exists for this Organization", concatName);
				throw new ApplicationException(errorMessage);
			}

			warehouseVO = new WarehouseVO();
			warehouseVO.setCreatedBy(warehouseDTO.getCreatedBy());
			warehouseVO.setUpdatedBy(warehouseDTO.getCreatedBy());
			message = "Warehouse Created Successfully";
		} else {
			warehouseVO = warehouseRepo.findById(warehouseDTO.getId()).orElseThrow(
					() -> new ApplicationException("StockBranch not found with id: " + warehouseDTO.getId()));
			warehouseVO.setUpdatedBy(warehouseDTO.getCreatedBy());

			boolean isLocationChanged =!warehouseVO.getLocationUnit().equalsIgnoreCase(warehouseDTO.getLocationUnit());

			if (isLocationChanged) {
				if (warehouseRepo.existsByLocationUnitAndOrgId(warehouseVO.getLocationUnit(), warehouseDTO.getOrgId())) {
					String errorMessage = String.format(
							"The LocationUnit: %s already exist for this Organization",
							warehouseDTO.getLocationName(), warehouseDTO.getLocationUnit());
					throw new ApplicationException(errorMessage);
				}

				warehouseVO.setLocationName(warehouseDTO.getLocationName().toUpperCase());
				warehouseVO.setLocationUnit(warehouseDTO.getLocationUnit().toUpperCase());
			}

			if (!warehouseVO.getName().equalsIgnoreCase(concatName)) {
				if (warehouseRepo.existsByNameAndOrgId(concatName, warehouseDTO.getOrgId())) {
					String errorMessage = String.format("The Name: %s already exists for this Organization",
							concatName);
					throw new ApplicationException(errorMessage);
				}
				warehouseVO.setName(concatName);
			}

			message = "Warehouse Updated Successfully";
		}

		getWarehouseVOFromWarehouseDTO(warehouseVO, warehouseDTO);
		warehouseRepo.save(warehouseVO);

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("warehouseVO", warehouseVO);
		return response;
	}

	private void getWarehouseVOFromWarehouseDTO(WarehouseVO warehouseVO, WarehouseDTO warehouseDTO) {
		warehouseVO.setOrgId(warehouseDTO.getOrgId());
		warehouseVO.setLocationName(warehouseDTO.getLocationName().toUpperCase());
		warehouseVO.setLocationUnit(warehouseDTO.getLocationUnit().toUpperCase());
		warehouseVO.setActive(warehouseDTO.isActive());
		warehouseVO.setName(warehouseDTO.getLocationName().toUpperCase() + "-"+ warehouseDTO.getLocationUnit().toUpperCase());
		warehouseVO.setAddress(warehouseDTO.getAddress().toUpperCase());
		warehouseVO.setCreatedBy(warehouseDTO.getCreatedBy());
		warehouseVO.setState(warehouseDTO.getState());
		warehouseVO.setPincode(warehouseDTO.getPincode());
		warehouseVO.setCode(warehouseDTO.getCode().toUpperCase());
		warehouseVO.setStockBranch(warehouseDTO.getStockBranch());
		warehouseVO.setCity(warehouseDTO.getCity());
		warehouseVO.setCountry(warehouseDTO.getCountry());
		warehouseVO.setGst(warehouseDTO.getGst());
	}

	@Override
	public List<WarehouseVO> getAllWarehouseByOrgId(Long orgId) {

		return warehouseRepo.getAllWarehouseByOrgId(orgId);
	}

	@Override
	public WarehouseVO getWarehouseById(Long id) {

		return warehouseRepo.getWarehouseById(id);
	}

	@Override
	public List<StockBranchVO> getStockBranchName(Long orgId)  {
		return warehouseRepo.getStockBranchName(orgId);
	}
	
	private int totalRows = 0;
	private int successfulUploads = 0;

	@Transactional
	@Override
	public void excelUploadForWarehouse(MultipartFile[] files, String createdBy, Long orgId)
	        throws EncryptedDocumentException, ApplicationException, IOException, java.io.IOException {
		totalRows = 0;
		successfulUploads = 0;

		for (MultipartFile file : files) {
		    try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
		        Sheet sheet = workbook.getSheetAt(0);
		        List<String> errorMessages = new ArrayList<>();
		        List<WarehouseVO> validWarehouseList = new ArrayList<>();

		        Set<String> uniqueLocationUnit = new HashSet<>();
		        Set<String> uniqueNameUnit = new HashSet<>();

		        for (Row row : sheet) {
		            if (row.getRowNum() == 0 || isRowEmpty(row)) continue;
		            totalRows++;

		            try {
		                String locationName = getStringCellValue(row.getCell(0));
		                String locationUnit = getStringCellValue(row.getCell(1));
		                String code = getStringCellValue(row.getCell(2));
		                String address = getStringCellValue(row.getCell(3));
		                String country = getStringCellValue(row.getCell(4));
		                String state = getStringCellValue(row.getCell(5));
		                String city = getStringCellValue(row.getCell(6));
		                Long pincode = getLongCellValue(row.getCell(7));
		                String gst = getStringCellValue(row.getCell(8));
		                String stockBranch = getStringCellValue(row.getCell(9));
		                boolean active = getActiveBooleanValue(row.getCell(10), row.getRowNum() + 1);

		                String concatName = locationName.toUpperCase() + "-" + locationUnit.toUpperCase();

		                // In-memory duplicate check (within file)
		                String inFileUnitKey = locationUnit.toUpperCase() + "_" + orgId;
		                String inFileNameUnitKey = concatName + "_" + orgId;

		                if (!uniqueLocationUnit.add(inFileUnitKey)) {
		                    errorMessages.add("Row " + (row.getRowNum() + 1) + ": Duplicate LocationUnit within Excel file");
		                }
		                if (!uniqueNameUnit.add(inFileNameUnitKey)) {
		                    errorMessages.add("Row " + (row.getRowNum() + 1) + ": Duplicate LocationName + LocationUnit within Excel file");
		                }

		                if (warehouseRepo.existsByLocationUnitAndOrgId(locationUnit, orgId)) {
		                    errorMessages.add("Row " + (row.getRowNum() + 1) + ": Duplicate LocationUnit in DB");
		                }
		                if (warehouseRepo.existsByNameAndOrgId(concatName, orgId)) {
		                    errorMessages.add("Row " + (row.getRowNum() + 1) + ": Duplicate Warehouse Name in DB");
		                }

		                // Prepare for save (but don't save yet)
		                WarehouseVO warehouseVO = new WarehouseVO();
		                warehouseVO.setLocationName(locationName.toUpperCase());
		                warehouseVO.setLocationUnit(locationUnit.toUpperCase());
		                warehouseVO.setName(concatName);
		                warehouseVO.setCode(code.toUpperCase());
		                warehouseVO.setAddress(address.toUpperCase());
		                warehouseVO.setCountry(country.toUpperCase());
		                warehouseVO.setState(state.toUpperCase());
		                warehouseVO.setCity(city.toUpperCase());
		                warehouseVO.setPincode(pincode);
		                warehouseVO.setGst(gst);
		                warehouseVO.setStockBranch(stockBranch.toUpperCase());
		                warehouseVO.setActive(active);
		                warehouseVO.setOrgId(orgId);
		                warehouseVO.setCreatedBy(createdBy);
		                warehouseVO.setUpdatedBy(createdBy);

		                validWarehouseList.add(warehouseVO);

		            } catch (Exception e) {
		                errorMessages.add("Row " + (row.getRowNum() + 1) + ": " + e.getMessage());
		            }
		        }

		        if (!errorMessages.isEmpty()) {
		            throw new ApplicationException("Excel validation errors:\n" + String.join("\n", errorMessages));
		        }

		        // If all rows valid, save all in bulk
		        warehouseRepo.saveAll(validWarehouseList);
		        successfulUploads += validWarehouseList.size();

		    } catch (IOException e) {
		        throw new ApplicationException("Failed to process file: " + file.getOriginalFilename() + " - " + e.getMessage());
		    }
		}
	}

	@Override
	public int getTotalRows() {
	    return totalRows;
	}

	@Override
	public int getSuccessfulUploads() {
	    return successfulUploads;
	}

	private boolean isRowEmpty(Row row) {
	    if (row == null) return true;
	    for (Cell cell : row) {
	        if (cell != null && cell.getCellType() != CellType.BLANK && !cell.toString().trim().isEmpty()) {
	            return false;
	        }
	    }
	    return true;
	}

	private String getStringCellValue(Cell cell) {
	    return cell == null ? "" : cell.toString().trim();
	}

	private Long getLongCellValue(Cell cell) {
	    try {
	        return (cell == null || cell.getCellType() == CellType.BLANK) ? null : (long) cell.getNumericCellValue();
	    } catch (Exception e) {
	        throw new IllegalArgumentException("Invalid numeric value in 'pincode' column.");
	    }
	}

	private BigDecimal getBigDecimalValue(Cell cell) {
	    try {
	        if (cell == null || cell.getCellType() == CellType.BLANK) return BigDecimal.ZERO;
	        return new BigDecimal(cell.toString().trim());
	    } catch (Exception e) {
	        throw new IllegalArgumentException("Invalid GST value.");
	    }
	}

	private boolean getActiveBooleanValue(Cell cell, int rowNumber) throws ApplicationException {
	    if (cell == null || cell.getCellType() == CellType.BLANK) {
	        throw new ApplicationException("Missing 'active' value at row " + rowNumber);
	    }

	    switch (cell.getCellType()) {
	        case STRING:
	            String val = cell.getStringCellValue().trim();
	            if ("1".equals(val)) return true;
	            if ("0".equals(val)) return false;
	            break;
	        case NUMERIC:
	            int numVal = (int) cell.getNumericCellValue();
	            if (numVal == 1) return true;
	            if (numVal == 0) return false;
	            break;
	    }

	    throw new ApplicationException("Invalid 'active' value at row " + rowNumber);
	}

	
	@Override
	public List<CityVO> getAllCitiesByStateAndCountry(String state, String country, Long orgId) {

		return cityRepo.findAllByStateAndCountryAndOrgId(state, country, orgId);
	}
	
	@Override
	public List<StateVO> getAllStatesByCountry(String Country, Long orgId) {
		return stateRepo.findAllStateByCountryAndOrgId(Country, orgId);
	}
	
	
	//ASSET FILEUPLOAD
	
	@Transactional
	@Override
	public void excelUploadForAsset(MultipartFile[] files, String createdBy, Long orgId)
	        throws EncryptedDocumentException, ApplicationException, IOException, java.io.IOException {

	    totalRows = 0;
	    successfulUploads = 0;

	    for (MultipartFile file : files) {
	        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
	            Sheet sheet = workbook.getSheetAt(0);
	            List<String> errorMessages = new ArrayList<>();
	            List<AssetVO> validAssetList = new ArrayList<>();

	            for (Row row : sheet) {
	                if (row.getRowNum() == 0 || isRowEmpty(row)) continue;
	                totalRows++;

	                try {
	                    String type = getStringCellValue(row.getCell(0));
	                    String category = getStringCellValue(row.getCell(1));
	                    String categoryCode = getStringCellValue(row.getCell(2));
	                    String assetCode = getStringCellValue(row.getCell(3));
	                    String assetDescription = getStringCellValue(row.getCell(4));
	                    String belongsTo = getStringCellValue(row.getCell(5));
	                    String materialIdentification = getStringCellValue(row.getCell(6));
	                    String design = getStringCellValue(row.getCell(7));
	                    String hsnCode = getStringCellValue(row.getCell(8));
	                    String costPrice = getStringCellValue(row.getCell(9));
	                    boolean active = getActiveBooleanValue(row.getCell(10), row.getRowNum() + 1);

	                    // DB duplicate checks
	                    if (assetRepo.existsByAssetCodeIdAndOrgId(assetCode, orgId)) {
	                        throw new ApplicationException(
	                            String.format("Row %d: Asset Code '%s' already exists in the system.", row.getRowNum() + 1, assetCode));
	                    }

	                    if (assetRepo.existsByAssetNameAndOrgId(assetDescription, orgId)) {
	                        throw new ApplicationException(
	                            String.format("Row %d: Asset Name '%s' already exists in the system.", row.getRowNum() + 1, assetDescription));
	                    }

	                    // Prepare the AssetVO object
	                    AssetVO assetVO = new AssetVO();
	                    assetVO.setAssetType(type.toUpperCase());
	                    assetVO.setCategory(category.toUpperCase());
	                    assetVO.setCategoryCode(categoryCode);
	                    assetVO.setAssetCodeId(assetCode.toUpperCase());
	                    assetVO.setAssetName(assetDescription.toUpperCase());
	                    assetVO.setBelongsTo(belongsTo.toUpperCase());
	                    assetVO.setMaterialIdentification(materialIdentification);
	                    assetVO.setDesign(design);
	                    assetVO.setHsnCode(hsnCode);
	                    assetVO.setCostPrice(costPrice);
	                    assetVO.setActive(active);
	                    assetVO.setOrgId(orgId);
	                    assetVO.setCreatedBy(createdBy);
	                    assetVO.setUpdatedBy(createdBy);

	                    validAssetList.add(assetVO);

	                } catch (Exception e) {
	                    errorMessages.add("Row " + (row.getRowNum() + 1) + ": " + e.getMessage());
	                }
	            }

	            if (!errorMessages.isEmpty()) {
	                throw new ApplicationException("Excel validation errors:\n" + String.join("\n", errorMessages));
	            }

	            assetRepo.saveAll(validAssetList);
	            successfulUploads += validAssetList.size();

	        } catch (IOException e) {
	            throw new ApplicationException("Failed to process file: " + file.getOriginalFilename() + " - " + e.getMessage());
	        }
	    }
	}		
	
}
	

