package com.base.basesetup.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.base.basesetup.dto.StockBranchDTO;
import com.base.basesetup.dto.WarehouseDTO;
import com.base.basesetup.entity.CityVO;
import com.base.basesetup.entity.StateVO;
import com.base.basesetup.entity.StockBranchVO;
import com.base.basesetup.entity.WarehouseVO;
import com.base.basesetup.exception.ApplicationException;

@Service
public interface WareHouseService {
	
	//StockBranch
	
	Map<String, Object>  createupdateStockBranch(StockBranchDTO stockBranchDTO) throws ApplicationException;

	List<StockBranchVO> getAllStockBranchByOrgId(Long orgId);
	
	StockBranchVO getStockBranchById(Long id);
	
	//WareHouse
	
	Map<String, Object>  createupdateWarehouse(WarehouseDTO warehouseDTO) throws ApplicationException;

	List<WarehouseVO> getAllWarehouseByOrgId(Long orgId);
	
	WarehouseVO getWarehouseById(Long id);

	List<StockBranchVO> getStockBranchName(Long orgId);

	void excelUploadForWarehouse(MultipartFile[] files, String createdBy, Long orgId)
			throws EncryptedDocumentException, ApplicationException, IOException;

	int getTotalRows();
	int getSuccessfulUploads();

	List<CityVO> getAllCitiesByStateAndCountry(String state, String country, Long orgId);

	List<StateVO> getAllStatesByCountry(String Country, Long orgId);

	void excelUploadForAsset(MultipartFile[] files, String createdBy, Long orgId)
			throws EncryptedDocumentException, ApplicationException, IOException;

	List<Map<String, Object>> getAllWarehouseNames(Long orgId);


}
