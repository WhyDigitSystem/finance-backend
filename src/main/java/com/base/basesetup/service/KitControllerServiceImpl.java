package com.base.basesetup.service;

import java.io.IOException;
import java.math.BigDecimal;
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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.base.basesetup.dto.AssetCategoryDTO;
import com.base.basesetup.dto.AssetDTO;
import com.base.basesetup.dto.KitAssetDTO;
import com.base.basesetup.dto.KitDTO;
import com.base.basesetup.entity.AssetCategoryVO;
import com.base.basesetup.entity.AssetTypeDTO;
import com.base.basesetup.entity.AssetTypeVO;
import com.base.basesetup.entity.AssetVO;
import com.base.basesetup.entity.KitAssetVO;
import com.base.basesetup.entity.KitVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.AssetCategoryRepo;
import com.base.basesetup.repo.AssetItemRepo;
import com.base.basesetup.repo.AssetRepo;
import com.base.basesetup.repo.AssetTypeRepo;
import com.base.basesetup.repo.KitAssetRepo;
import com.base.basesetup.repo.KitRepo;

@Service
public class KitControllerServiceImpl implements KitControllerService {

	public static final Logger LOGGER = LoggerFactory.getLogger(KitControllerServiceImpl.class);

	@Autowired
	AssetTypeRepo assetTypeRepo;

	@Autowired
	AssetCategoryRepo assetCategoryRepo;

	@Autowired
	AssetRepo assetRepo;

	@Autowired
	AssetItemRepo assetItemRepo;

	@Autowired
	KitRepo kitRepo;

	@Autowired
	KitAssetRepo kitAssetRepo;

	@Override
	public Map<String, Object> updateCreateAssetType(@Valid AssetTypeDTO assetTypeDTO) throws ApplicationException {

		AssetTypeVO assetTypeVO;
		String message;

		if (ObjectUtils.isEmpty(assetTypeDTO.getId())) {
			// Create flow

			if (assetTypeRepo.existsByAssetType(assetTypeDTO.getAssetType())) {
				throw new ApplicationException(
						"This Asset Type already exists in this organization: " + assetTypeDTO.getAssetType());
			}

			if (assetTypeRepo.existsByTypeCode(assetTypeDTO.getTypeCode())) {
				throw new ApplicationException(
						"This Asset Type Code already exists in this organization: " + assetTypeDTO.getTypeCode());
			}

			assetTypeVO = new AssetTypeVO();
			assetTypeVO.setCreatedBy(assetTypeDTO.getCreatedBy());
			assetTypeVO.setUpdatedBy(assetTypeDTO.getCreatedBy());

			message = "Asset Type created successfully";
		} else {
			// Update flow

			assetTypeVO = assetTypeRepo.findById(assetTypeDTO.getId())
					.orElseThrow(() -> new ApplicationException("The ID was not found. Please provide a valid one."));

			if (!assetTypeVO.getAssetType().equalsIgnoreCase(assetTypeDTO.getAssetType())) {
				if (assetTypeRepo.existsByAssetType(assetTypeDTO.getAssetType())) {
					throw new ApplicationException(
							"This Asset Type already exists in this organization: " + assetTypeDTO.getAssetType());
				}
				assetTypeVO.setAssetType(assetTypeDTO.getAssetType().toUpperCase());
			}

			if (!assetTypeVO.getTypeCode().equalsIgnoreCase(assetTypeDTO.getTypeCode())) {
				if (assetTypeRepo.existsByTypeCode(assetTypeDTO.getTypeCode())) {
					throw new ApplicationException(
							"This Asset Type Code already exists in this organization: " + assetTypeDTO.getTypeCode());
				}
				assetTypeVO.setTypeCode(assetTypeDTO.getTypeCode().toUpperCase());
			}

			assetTypeVO.setUpdatedBy(assetTypeDTO.getCreatedBy());

			message = "Asset Type updated successfully";
		}

		assetTypeVO = getAssetTypeVOFromDTO(assetTypeVO, assetTypeDTO);
		assetTypeRepo.save(assetTypeVO);

		Map<String, Object> response = new HashMap<>();
		response.put("assetTypeVO", assetTypeVO);
		response.put("message", message);
		return response;
	}

	private AssetTypeVO getAssetTypeVOFromDTO(AssetTypeVO assetTypeVO, @Valid AssetTypeDTO assetTypeDTO) {
		assetTypeVO.setOrgId(assetTypeDTO.getOrgId());
		assetTypeVO.setAssetType(assetTypeDTO.getAssetType().toUpperCase());
		assetTypeVO.setTypeCode(assetTypeDTO.getTypeCode().toUpperCase());
		assetTypeVO.setCancelremarks(assetTypeDTO.getCancelremarks());
		assetTypeVO.setCancel(assetTypeDTO.isCancel());
		assetTypeVO.setActive(assetTypeDTO.isActive());
		return assetTypeVO;
	}

	@Override
	public List<AssetTypeVO> getAssetTypeByOrgId(Long orgid) {
		return assetTypeRepo.findByOrgId(orgid);
	}

	@Override
	public Optional<AssetTypeVO> getAssetTypeById(Long id) {
		return assetTypeRepo.findById(id);
	}

	@Override
	public Map<String, Object> updateCreateAssetCategory(@Valid AssetCategoryDTO assetCategoryDTO)
			throws ApplicationException {

		AssetCategoryVO assetCategoryVO;

		String message;

		if (ObjectUtils.isEmpty(assetCategoryDTO.getId())) {

			if (assetCategoryRepo.existsByCategoryAndOrgId(assetCategoryDTO.getCategory(),
					assetCategoryDTO.getOrgId())) {
				throw new ApplicationException(
						"This Category already exists in this organization: " + assetCategoryDTO.getCategoryCode());
			}

			if (assetCategoryRepo.existsByCategoryCodeAndOrgId(assetCategoryDTO.getCategoryCode(),
					assetCategoryDTO.getOrgId())) {
				throw new ApplicationException("This Category Code already exists in this organization: "
						+ assetCategoryDTO.getCategoryCode());
			}

			assetCategoryVO = new AssetCategoryVO();

			assetCategoryVO.setCreatedBy(assetCategoryDTO.getCreatedBy());
			assetCategoryVO.setUpdatedBy(assetCategoryDTO.getCreatedBy());

			message = "AssetCategory Creation SuccessFully";

		} else {
			// Update flow

			assetCategoryVO = assetCategoryRepo.findById(assetCategoryDTO.getId())
					.orElseThrow(() -> new ApplicationException("The ID was not found. Please provide a valid one."));
			if (!assetCategoryVO.getCategory().equalsIgnoreCase(assetCategoryDTO.getCategory())) {

				if (assetCategoryRepo.existsByCategoryAndOrgId(assetCategoryDTO.getCategory(),
						assetCategoryDTO.getOrgId())) {
					throw new ApplicationException(
							"This Category already exists in this organization: " + assetCategoryDTO.getCategoryCode());
				}

				assetCategoryVO.setCategory(assetCategoryDTO.getCategory().toUpperCase());

			}

			if (!assetCategoryVO.getCategoryCode().equalsIgnoreCase(assetCategoryDTO.getCategoryCode())) {

				if (assetCategoryRepo.existsByCategoryCodeAndOrgId(assetCategoryDTO.getCategoryCode(),
						assetCategoryDTO.getOrgId())) {
					throw new ApplicationException("This Category Code already exists in this organization: "
							+ assetCategoryDTO.getCategoryCode());
				}

				assetCategoryVO.setCategoryCode(assetCategoryDTO.getCategoryCode().toUpperCase());

			}
			assetCategoryVO.setUpdatedBy(assetCategoryDTO.getCreatedBy());

			message = "AssetCategory updated successfully";
		}

		assetCategoryVO = getAssetCategoryVOFromAssetCategoryDTO(assetCategoryVO, assetCategoryDTO);
		assetCategoryRepo.save(assetCategoryVO);

		Map<String, Object> response = new HashMap<>();
		response.put("assetCategoryVO", assetCategoryVO);
		response.put("message", message);
		return response;
	}

	private AssetCategoryVO getAssetCategoryVOFromAssetCategoryDTO(AssetCategoryVO assetCategoryVO,
			@Valid AssetCategoryDTO assetCategoryDTO) {

		assetCategoryVO.setAssetType(assetCategoryDTO.getAssetType().toUpperCase());
		assetCategoryVO.setCategory(assetCategoryDTO.getCategory().toUpperCase());
		assetCategoryVO.setCategoryCode(assetCategoryDTO.getCategoryCode().toUpperCase());
		assetCategoryVO.setActive(assetCategoryDTO.isActive());
		assetCategoryVO.setLength(assetCategoryDTO.getLength());
		assetCategoryVO.setBreath(assetCategoryDTO.getBreath());
		assetCategoryVO.setHeight(assetCategoryDTO.getHeight());
		assetCategoryVO.setDimUnit(assetCategoryDTO.getDimUnit());
		assetCategoryVO.setCancel(assetCategoryDTO.isCancel());
		assetCategoryVO.setCancelremarks(assetCategoryDTO.getCancelremarks());
		assetCategoryVO.setOrgId(assetCategoryDTO.getOrgId());

		return assetCategoryVO;
	}

	@Override
	public List<AssetCategoryVO> getAssetCategoryByOrgId(Long orgid) {
		return assetCategoryRepo.getByOrgId(orgid);
	}

	@Override
	public Optional<AssetCategoryVO> getAssetCategoryById(Long id) {
		return assetCategoryRepo.findById(id);
	}

	// ASSETS

	@Override
	public Map<String, Object> updateCreateAsset(@Valid AssetDTO assetDTO) throws ApplicationException {

		AssetVO assetVO;

		String message;

		if (ObjectUtils.isEmpty(assetDTO.getId())) {

			if (assetRepo.existsByAssetNameAndOrgId(assetDTO.getAssetName(), assetDTO.getOrgId())) {
				throw new ApplicationException(
						"This Asset already exists in this organization: " + assetDTO.getAssetName());
			}

			if (assetRepo.existsByAssetCodeIdAndOrgId(assetDTO.getAssetCodeId(), assetDTO.getOrgId())) {
				throw new ApplicationException(
						"This AssetCode already exists in this organization: " + assetDTO.getAssetCodeId());
			}

			assetVO = new AssetVO();

			assetVO.setCreatedBy(assetDTO.getCreatedBy());
			assetVO.setUpdatedBy(assetDTO.getCreatedBy());

			message = "Asset Creation SuccessFully";

		} else {
			// Update flow

			assetVO = assetRepo.findById(assetDTO.getId())
					.orElseThrow(() -> new ApplicationException("The ID was not found. Please provide a valid one."));

			if (!assetVO.getAssetName().equalsIgnoreCase(assetDTO.getAssetName())) {

				if (assetRepo.existsByAssetNameAndOrgId(assetDTO.getAssetName(), assetDTO.getOrgId())) {
					throw new ApplicationException(
							"This Asset already exists in this organization: " + assetDTO.getAssetName());
				}

				assetVO.setAssetName(assetDTO.getAssetName().toUpperCase());

			}

			if (!assetVO.getAssetCodeId().equalsIgnoreCase(assetDTO.getAssetCodeId())) {

				if (assetRepo.existsByAssetCodeIdAndOrgId(assetDTO.getAssetCodeId(), assetDTO.getOrgId())) {
					throw new ApplicationException(
							"This AssetCode already exists in this organization: " + assetDTO.getAssetCodeId());
				}

				assetVO.setAssetCodeId(assetDTO.getAssetCodeId().toUpperCase());
			}
			assetVO.setUpdatedBy(assetDTO.getCreatedBy());

			message = "Asset updated successfully";
		}

		assetVO = getAssetVOFromAssetDTO(assetVO, assetDTO);
		assetRepo.save(assetVO);

		Map<String, Object> response = new HashMap<>();
		response.put("assetVO", assetVO);
		response.put("message", message);
		return response;

	}

	private AssetVO getAssetVOFromAssetDTO(AssetVO assetVO, @Valid AssetDTO assetDTO) throws ApplicationException {

		assetVO.setOrgId(assetDTO.getOrgId());
		assetVO.setCategory(assetDTO.getCategory().toUpperCase());
		assetVO.setCategoryCode(assetDTO.getCategoryCode().toUpperCase());
		assetVO.setAssetCodeId(assetDTO.getAssetCodeId().toUpperCase());
		assetVO.setAssetName(assetDTO.getAssetName().toUpperCase());
		assetVO.setBelongsTo(assetDTO.getBelongsTo());
		assetVO.setMaterialIdentification(assetDTO.getMaterialIdentification());
		assetVO.setManufacturePartCode(assetDTO.getManufacturePartCode());
		assetVO.setDesign(assetDTO.getDesign());
		assetVO.setLength(assetDTO.getLength());
		assetVO.setBreath(assetDTO.getBreath());
		assetVO.setHeight(assetDTO.getHeight());
		assetVO.setWeight(assetDTO.getWeight());
		assetVO.setQuantity(assetDTO.getQuantity());
		// assetVO.setDimUnit(assetDTO.getDimUnit());
		assetVO.setManufacturer(assetDTO.getManufacturer());
		assetVO.setChargableWeight(assetDTO.getChargableWeight());
		// assetVO.setBrand(assetDTO.getBrand());
		assetVO.setEanUpc(assetDTO.getEanUpc());
		assetVO.setAssetType(assetDTO.getAssetType());
		assetVO.setExpectedLife(assetDTO.getExpectedLife());
		assetVO.setMaintanencePeriod(assetDTO.getMaintanencePeriod());
		assetVO.setExpectedTrips(assetDTO.getExpectedTrips());
		assetVO.setHsnCode(assetDTO.getHsnCode());
		assetVO.setTaxRate(assetDTO.getTaxRate());
		assetVO.setSkuFrom(assetDTO.getSkuFrom());
		assetVO.setSkuTo(assetDTO.getSkuTo());
		assetVO.setCostPrice(assetDTO.getCostPrice());
		assetVO.setSellPrice(assetDTO.getSellPrice());
		assetVO.setScrapValue(assetDTO.getScrapValue());
		//assetVO.setCancel(assetDTO.isCancel());
		assetVO.setCancelremarks(assetDTO.getCancelremarks());
		assetVO.setPoNo(assetDTO.getPoNo());
		assetVO.setPoDate(assetDTO.getPoDate());
		assetVO.setActive(assetDTO.isActive());

//		Set<String> skuIdSet = new HashSet<>();
//		List<AssetItemVO> assetItemVOs = new ArrayList<>();
//
//		if (assetDTO.getAssetItemDTO() != null) {
//			for (AssetItemDTO assetDTO1 : assetDTO.getAssetItemDTO()) {
//				String skuId = new StringBuilder(assetDTO.getAssetCodeId()).append("-").append(assetDTO1.getSkuId())
//						.toString();
//
//				AssetItemVO assetItemVO = new AssetItemVO();
//
//				if (!assetItemVO.getSkuId().equalsIgnoreCase(skuId)) {
//
//					// Optional: Check for duplicates in database
//					if (assetItemRepo.existsBySkuId(skuId)) {
//						throw new ApplicationException("SKU ID already exists in database: " + skuId);
//					}
//				}
//
//				// Check for duplicates in current request
//				if (!skuIdSet.add(skuId)) {
//					throw new ApplicationException("Duplicate SKU ID in request: " + skuId);
//				}
//
//				assetItemVO.setSkuId(skuId);
//				assetItemVO.setAssetName(assetDTO1.getAssetName());
//				assetItemVO.setStatus(assetDTO1.getStatus());
//				assetItemVO.setAssetVO(assetVO);
//				assetItemVOs.add(assetItemVO);
//			}
//		}
//
//		assetVO.setAssetItemVO(assetItemVOs);

		return assetVO;
	}

	@Override
	public List<AssetVO> getAssetByOrgId(Long orgid) {
		return assetRepo.findByOrgId(orgid);
	}

	@Override
	public Optional<AssetVO> getAssetById(Long id) {
		return assetRepo.findById(id);
	}

	@Override
	public Map<String, Object> updateCreateKit(@Valid KitDTO kitDTO) throws ApplicationException {

		KitVO kitVO;

		String message;
		if (ObjectUtils.isEmpty(kitDTO.getId())) {

			if (kitRepo.existsByKitNoAndOrgId(kitDTO.getKitNo(), kitDTO.getOrgId())) {
				throw new ApplicationException("This KitId already exists in this organization: " + kitDTO.getKitNo());
			}

			if (kitRepo.existsByKitDescAndOrgId(kitDTO.getKitDesc(), kitDTO.getOrgId())) {
				throw new ApplicationException(
						"This KitDEsc already exists in this organization: " + kitDTO.getKitDesc());
			}

			kitVO = new KitVO();

			kitVO.setCreatedBy(kitDTO.getCreatedBy());
			kitVO.setUpdatedBy(kitDTO.getCreatedBy());

			message = "Kit Creation SuccessFully";

		} else {
			// Update flow

			kitVO = kitRepo.findById(kitDTO.getId())
					.orElseThrow(() -> new ApplicationException("The ID was not found. Please provide a valid one."));

			if (!kitVO.getKitNo().equalsIgnoreCase(kitDTO.getKitNo())) {

				if (kitRepo.existsByKitNoAndOrgId(kitDTO.getKitNo(), kitDTO.getOrgId())) {
					throw new ApplicationException(
							"This KitId already exists in this organization: " + kitDTO.getKitNo());
				}

				kitVO.setKitNo(kitDTO.getKitNo().toUpperCase());

			}

			if (!kitVO.getKitDesc().equalsIgnoreCase(kitDTO.getKitDesc())) {

				if (kitRepo.existsByKitDescAndOrgId(kitDTO.getKitDesc(), kitDTO.getOrgId())) {
					throw new ApplicationException(
							"This KitDEsc already exists in this organization: " + kitDTO.getKitDesc());
				}

				kitVO.setKitDesc(kitDTO.getKitDesc().toUpperCase());
			}
			kitVO.setUpdatedBy(kitDTO.getCreatedBy());

			message = "Kit updated successfully";
		}

		kitVO = getKitVOFromKitDTO(kitVO, kitDTO);
		kitRepo.save(kitVO);

		Map<String, Object> response = new HashMap<>();
		response.put("kitVO", kitVO);
		response.put("message", message);
		return response;

	}

	private KitVO getKitVOFromKitDTO(KitVO kitVO, @Valid KitDTO kitDTO) {

		kitVO.setOrgId(kitDTO.getOrgId());
		kitVO.setActive(kitDTO.isActive());
		kitVO.setKitNo(kitDTO.getKitNo());
		kitVO.setKitDesc(kitDTO.getKitDesc());
		kitVO.setCancel(kitDTO.isCancel());
		kitVO.setFinyr(kitDTO.getFinyr());
		kitVO.setCreatedBy(kitDTO.getCreatedBy());
		kitVO.setCancelRemarks(kitDTO.getCancelRemarks());
		kitVO.setPartQty(kitDTO.getPartQty());
		kitVO.setBlock(kitDTO.isBlock());
		kitVO.setEflag(kitDTO.isEflag());
		kitVO.setPartNo(kitDTO.getPartNo());

		if (ObjectUtils.isNotEmpty(kitVO.getId())) {
			List<KitAssetVO> kitAssetVOs = kitAssetRepo.findByKitVO(kitVO);
			kitAssetRepo.deleteAll(kitAssetVOs);

		}

		List<KitAssetVO> kitAssetVOs = new ArrayList<KitAssetVO>();

		if (kitVO != null) {

			for (KitAssetDTO kitAssetDTO : kitDTO.getKitAssetDTO()) {

				KitAssetVO kitAssetVO = new KitAssetVO();

				kitAssetVO.setAssetType(kitAssetDTO.getAssetType());
				kitAssetVO.setBelongsTo(kitAssetDTO.getBelongsTo());
				kitAssetVO.setManufacturePartCode(kitAssetDTO.getManufacturePartCode());
				kitAssetVO.setAssetCategory(kitAssetDTO.getAssetCategory());
				kitAssetVO.setCategoryCode(kitAssetDTO.getCategoryCode());
				kitAssetVO.setAssetCodeId(kitAssetDTO.getAssetCodeId());
				kitAssetVO.setAssetName(kitAssetDTO.getAssetName());
				kitAssetVO.setQuantity(kitAssetDTO.getQuantity());

				kitAssetVO.setKitVO(kitVO);
				kitAssetVOs.add(kitAssetVO);

			}

			kitVO.setKitAssetVO(kitAssetVOs);
		}

		return kitVO;
	}

	@Override
	public List<KitVO> getKitByOrgId(Long orgid) {
		return kitRepo.findAllKit(orgid);
	}

	@Override
	public Optional<KitVO> getKitById(Long id) {
		return kitRepo.findById(id);
	}

	@Override
	public List<Map<String, Object>> getAssetCategoeyByAsset(Long orgId,String assetType) {
		Set<Object[]> chType = assetCategoryRepo.getAssetData(orgId,assetType);
		return getAssetCategoey(chType);
	}

	private List<Map<String, Object>> getAssetCategoey(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();
			map.put("category", ch[0] != null ? ch[0].toString() : "");
			map.put("categoryCode", ch[1] != null ? ch[1].toString() : "");
			List1.add(map);
		}
		return List1;

	}

	@Override
	public List<Map<String, Object>> getAssetDescriptionByAssetCode(Long orgId, String assetCategory,String assetType) {
		Set<Object[]> chType = assetRepo.getAssetDescriptionByAsset(orgId, assetCategory,assetType);
		return getAssetDescription(chType);
	}

	private List<Map<String, Object>> getAssetDescription(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();
			map.put("asset", ch[0] != null ? ch[0].toString() : "");
			map.put("assetCode", ch[1] != null ? ch[1].toString() : "");
			List1.add(map);
		}
		return List1;

	}

	// Excel File Uploads

		private int totalRows = 0; // Initialize totalRows

		private int successfulUploads = 0; // Initialize successfulUploads

		@Override
		@Transactional
		public void ExcelUploadForAssetCategory(MultipartFile[] files, Long orgId,
				String createdBy) throws ApplicationException {
			List<AssetCategoryVO> assetCategoryVOsToSave = new ArrayList<>();
			totalRows = 0; // Reset totalRows for each execution
			successfulUploads = 0; // Reset successfulUploads for each execution

			// Process each uploaded file
			for (MultipartFile file : files) {
				try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
					Sheet sheet = workbook.getSheetAt(0); // Assuming only one sheet
					List<String> errorMessages = new ArrayList<>();
					System.out.println("Processing file: " + file.getOriginalFilename()); // Debug statement
					Row headerRow = sheet.getRow(0);
					if (!isHeaderValidAssetCategory(headerRow)) {
						throw new ApplicationException("Invalid Excel format.Please Refer The Sample File");
					}

					// Check all rows for validity first
					for (Row row : sheet) {
						if (row.getRowNum() == 0) {
							continue; // Skip header row
						}

						totalRows++; // Increment totalRows

						String assetType = row.getCell(0).getStringCellValue();
						String category = row.getCell(1).getStringCellValue();
						String categoryCode = row.getCell(2).getStringCellValue();

						// Validate each row
						try {
							if (assetCategoryRepo.existsByCategoryAndOrgId(category, orgId)) {
								errorMessages.add("Category " + category + " Already exists for this Organization. Row: "
										+ (row.getRowNum() + 1));
							}
							if (assetCategoryRepo.existsByCategoryCodeAndOrgId(categoryCode, orgId)) {
								errorMessages.add("Category Code " + categoryCode
										+ " Already exists for this Organization. Row: " + (row.getRowNum() + 1));
							}
							AssetTypeVO assetTypeVO = assetTypeRepo.findByOrgIdAndAssetType(orgId, assetType);
							if (assetTypeVO == null) {
								errorMessages.add("Asset Type " + assetType + " not found for orgId: " + orgId
										+ " and assetType: " + assetType + ". Row: " + (row.getRowNum() + 1));
							}
						} catch (Exception e) {
							errorMessages.add("Error processing row " + (row.getRowNum() + 1) + ": " + e.getMessage());
						}
					}

					// If there are errors, throw ApplicationException and do not save any rows
					if (!errorMessages.isEmpty()) {
						throw new ApplicationException(
								"Excel upload validation failed. Errors: " + String.join(", ", errorMessages));
					}

					// No errors found, now save all rows
					for (Row row : sheet) {
						if (row.getRowNum() == 0) {
							continue; // Skip header row
						}

						String assetType = row.getCell(0).getStringCellValue();
						String category = row.getCell(1).getStringCellValue();
						String categoryCode = row.getCell(2).getStringCellValue();

						// Create AssetCategoryVO and add to list for batch saving
						AssetCategoryVO assetCategoryVO = new AssetCategoryVO();
						assetCategoryVO.setOrgId(orgId);
						assetCategoryVO.setActive(true);
						assetCategoryVO.setCreatedBy(createdBy);
						assetCategoryVO.setUpdatedBy(createdBy);
						assetCategoryVO.setAssetType(assetType.toUpperCase());
						assetCategoryVO.setCategory(category.toUpperCase());
						assetCategoryVO.setCategoryCode(categoryCode.toUpperCase());
						assetCategoryVOsToSave.add(assetCategoryVO);
						successfulUploads++; // Increment successfulUploads
					}
				} catch (IOException e) {
					// Handle IO exceptions specific to the file
					throw new ApplicationException(
							"Failed to process file: " + file.getOriginalFilename() + " - " + e.getMessage());
				}
			}

			// Batch save all AssetCategoryVOs
			assetCategoryRepo.saveAll(assetCategoryVOsToSave);
		}

		private boolean isHeaderValidAssetCategory(Row headerRow) {
			if (headerRow == null) {
				return false;
			}
			int expectedColumnCount = 3;
			if (headerRow.getPhysicalNumberOfCells() != expectedColumnCount) {
				return false;
			}
			return "assetType".equalsIgnoreCase(getStringCellValue(headerRow.getCell(0)))
					&& "category".equalsIgnoreCase(getStringCellValue(headerRow.getCell(1)))
					&& "categoryCode".equalsIgnoreCase(getStringCellValue(headerRow.getCell(2)));
		}

		private boolean isRowEmpty(Row row) {
			for (Cell cell : row) {
				if (cell.getCellType() != CellType.BLANK) {
					return false;
				}
			}
			return true;
		}

		private String getStringCellValue(Cell cell) {
			if (cell == null) {
				return "";
			}
			switch (cell.getCellType()) {
			case STRING:
				return cell.getStringCellValue();
			case NUMERIC:
				return BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();
			case BOOLEAN:
				return String.valueOf(cell.getBooleanCellValue());
			case FORMULA:
				return cell.getCellFormula();
			default:
				return "";
			}
		}

		// Method to retrieve total rows processed
		public int getTotalRows() {
			return totalRows;
		}

		// Method to retrieve successful uploads count
		public int getSuccessfulUploads() {
			return successfulUploads;
		}

}
