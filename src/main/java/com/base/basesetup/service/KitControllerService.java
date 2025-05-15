package com.base.basesetup.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.base.basesetup.dto.AssetCategoryDTO;
import com.base.basesetup.dto.AssetDTO;
import com.base.basesetup.dto.KitDTO;
import com.base.basesetup.entity.AssetCategoryVO;
import com.base.basesetup.entity.AssetTypeDTO;
import com.base.basesetup.entity.AssetTypeVO;
import com.base.basesetup.entity.AssetVO;
import com.base.basesetup.entity.KitVO;
import com.base.basesetup.exception.ApplicationException;

@Service
public interface KitControllerService {
	
	//ASSET TYPE

	Map<String, Object> updateCreateAssetType(@Valid AssetTypeDTO assetTypeDTO) throws ApplicationException;

	List<AssetTypeVO> getAssetTypeByOrgId(Long orgid);

	Optional<AssetTypeVO> getAssetTypeById(Long id);
	
	//AssetCategory

	Map<String, Object> updateCreateAssetCategory(@Valid AssetCategoryDTO assetCategoryDTO) throws ApplicationException;

	List<AssetCategoryVO> getAssetCategoryByOrgId(Long orgid);

	Optional<AssetCategoryVO> getAssetCategoryById(Long id);

	//ASSETS
	
	Map<String, Object> updateCreateAsset(@Valid AssetDTO assetDTO) throws ApplicationException;

	List<AssetVO> getAssetByOrgId(Long orgid);

	Optional<AssetVO> getAssetById(Long id);
	
	//KIT

	Map<String, Object> updateCreateKit(@Valid KitDTO kitDTO) throws ApplicationException;

	List<KitVO> getKitByOrgId(Long orgid);

	Optional<KitVO> getKitById(Long id);

	List<Map<String, Object>> getAssetCategoeyByAsset(Long orgId, String category);

	List<Map<String, Object>> getAssetDescriptionByAssetCode(Long orgId, String assetCategory, String assetType);

}
