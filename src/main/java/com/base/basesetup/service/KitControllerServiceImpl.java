package com.base.basesetup.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.basesetup.dto.AssetCategoryDTO;
import com.base.basesetup.dto.AssetDTO;
import com.base.basesetup.dto.AssetItemDTO;
import com.base.basesetup.dto.DailyMonthlyExRatesDtlDTO;
import com.base.basesetup.entity.AssetCategoryVO;
import com.base.basesetup.entity.AssetItemVO;
import com.base.basesetup.entity.AssetTypeDTO;
import com.base.basesetup.entity.AssetTypeVO;
import com.base.basesetup.entity.AssetVO;
import com.base.basesetup.entity.DailyMonthlyExRatesDtlVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.AssetCategoryRepo;
import com.base.basesetup.repo.AssetRepo;
import com.base.basesetup.repo.AssetTypeRepo;

@Service
public class KitControllerServiceImpl implements KitControllerService {

	public static final Logger LOGGER = LoggerFactory.getLogger(KitControllerServiceImpl.class);

	@Autowired
	AssetTypeRepo assetTypeRepo;

	@Autowired
	AssetCategoryRepo assetCategoryRepo;

	@Autowired
	AssetRepo assetRepo;

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
				assetTypeVO.setAssetType(assetTypeDTO.getAssetType());
			}

			if (!assetTypeVO.getTypeCode().equalsIgnoreCase(assetTypeDTO.getTypeCode())) {
				if (assetTypeRepo.existsByTypeCode(assetTypeDTO.getTypeCode())) {
					throw new ApplicationException(
							"This Asset Type Code already exists in this organization: " + assetTypeDTO.getTypeCode());
				}
				assetTypeVO.setTypeCode(assetTypeDTO.getTypeCode());
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
		assetTypeVO.setAssetType(assetTypeDTO.getAssetType());
		assetTypeVO.setTypeCode(assetTypeDTO.getTypeCode());
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

		assetCategoryVO.setAssetType(assetCategoryDTO.getAssetType());
		assetCategoryVO.setCategory(assetCategoryDTO.getCategory());
		assetCategoryVO.setCategoryCode(assetCategoryDTO.getCategoryCode());
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
						"This Asset already exists in this organization: " + assetDTO.getAssetCodeId());
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
			if (assetRepo.existsByAssetNameAndOrgId(assetDTO.getAssetName(), assetDTO.getOrgId())) {
				throw new ApplicationException(
						"This Asset already exists in this organization: " + assetDTO.getAssetCodeId());
			}

			if (assetRepo.existsByAssetCodeIdAndOrgId(assetDTO.getAssetCodeId(), assetDTO.getOrgId())) {
				throw new ApplicationException(
						"This AssetCode already exists in this organization: " + assetDTO.getAssetCodeId());
			}

			assetVO.setUpdatedBy(assetDTO.getCreatedBy());

			message = "AssetCategory updated successfully";
		}

		assetVO = getAssetVOFromAssetDTO(assetVO, assetDTO);
		assetRepo.save(assetVO);

		Map<String, Object> response = new HashMap<>();
		response.put("assetVO", assetVO);
		response.put("message", message);
		return response;

	}

	private AssetVO getAssetVOFromAssetDTO(AssetVO assetVO, @Valid AssetDTO assetDTO) {

		assetVO.setOrgId(assetDTO.getOrgId());
		assetVO.setCategory(assetDTO.getCategory());
		assetVO.setCategoryCode(assetDTO.getCategoryCode());
		assetVO.setAssetCodeId(assetDTO.getAssetCodeId());
		assetVO.setAssetName(assetDTO.getAssetName());
		assetVO.setBelongsTo(assetDTO.getBelongsTo());
		assetVO.setMaterialIdentification(assetDTO.getMaterialIdentification());
		assetVO.setManufacturePartCode(assetDTO.getManufacturePartCode());
		assetVO.setDesign(assetDTO.getDesign());
		assetVO.setLength(assetDTO.getLength());
		assetVO.setBreath(assetDTO.getBreath());
		assetVO.setHeight(assetDTO.getHeight());
		assetVO.setWeight(assetDTO.getWeight());
		assetVO.setQuantity(assetDTO.getQuantity());
		assetVO.setDimUnit(assetDTO.getDimUnit());
		assetVO.setManufacturer(assetDTO.getManufacturer());
		assetVO.setChargableWeight(assetDTO.getChargableWeight());
		assetVO.setBrand(assetDTO.getBrand());
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
		assetVO.setCancel(assetDTO.isCancel());
		assetVO.setCancelremarks(assetDTO.getCancelremarks());
		assetVO.setPoNo(assetDTO.getPoNo());
		assetVO.setPoDate(assetDTO.getPoDate());
		assetVO.setActive(assetDTO.isActive());

		List<AssetItemVO> assetItemVOs = new ArrayList<>();
		long skuId = assetVO.getSkuFrom();

		if (assetDTO.getAssetItemDTO() != null) {
		    for (AssetItemDTO assetItemDTO : assetDTO.getAssetItemDTO()) {
		        AssetItemVO assetItemVO = new AssetItemVO();

		        // Set AssetVO association
		        assetItemVO.setAssetVO(assetVO);

		        // SKU: generate with increment
		        assetItemVO.setSkuId(new StringBuilder(assetDTO.getAssetCodeId())
		            .append("-")
		            .append(skuId)
		            .toString());

		        // Set asset name from VO or DTO
		        assetItemVO.setAssetName(assetVO.getAssetName());

		        // Set status (if available from DTO, use that instead)
		        assetItemVO.setStatus(MasterConstant.ASSET_ITEM_STATUS_INSTOCK);

		        // Add to list
		        assetItemVO.setAssetVO(assetVO);
		        assetItemVOs.add(assetItemVO);

		        skuId++;
		    }
		}
		assetVO.setAssetItemVO(assetItemVOs);


		return assetVO;
	}
}
