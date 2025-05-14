package com.base.basesetup.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.base.basesetup.dto.AssetCategoryDTO;
import com.base.basesetup.dto.AssetDTO;
import com.base.basesetup.entity.AssetCategoryVO;
import com.base.basesetup.entity.AssetTypeDTO;
import com.base.basesetup.entity.AssetTypeVO;
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

}
