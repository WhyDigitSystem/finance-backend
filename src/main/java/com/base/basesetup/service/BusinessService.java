package com.base.basesetup.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.EncryptedDocumentException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.base.basesetup.dto.CCoaDTO;
import com.base.basesetup.dto.CoaDTO;
import com.base.basesetup.dto.ExcelUploadResultDTO;
import com.base.basesetup.dto.LedgerMappingDTO;
import com.base.basesetup.dto.ServiceLevelDTO;
import com.base.basesetup.entity.CCoaVO;
import com.base.basesetup.entity.CoaVO;
import com.base.basesetup.entity.LedgerMappingVO;
import com.base.basesetup.entity.ServiceLevelVO;
import com.base.basesetup.exception.ApplicationException;

@Service
public interface BusinessService {

	Map<String, Object> createUpdateCoa(CoaDTO coaDTO) throws ApplicationException;

	List<CoaVO> getAllCao(Long orgId);

	Optional<CoaVO> getCaoById(Long id);

	List<Map<String, Object>> getGroupName(Long orgId);
	
	void excelUploadForCoa(MultipartFile[] files, String createdBy,Long orgId) throws ApplicationException, EncryptedDocumentException, IOException;
	
	int getTotalRows();

	int getSuccessfulUploads();
	
	//CCoa

	Map<String, Object> createUpdateCCoa(CCoaDTO cCoaDTO) throws ApplicationException;

	List<CCoaVO> getAllCCao(Long orgId, String clientCode);

	Optional<CCoaVO> getCCaoById(Long id);

	List<Map<String, Object>> getGroupNameForCCoa();


	//LEDGER MAPPING
	
	Map<String, Object> createUpdateLedgerMapping(LedgerMappingDTO ledgerMappingDTO) throws ApplicationException;

	List<Map<String, Object>> getCOAForLedgerMapping(Long orgId);

	List<Map<String, Object>> getLedgerMap(Long orgId);

	List<Map<String, Object>> getFillGridForLedgerMapping(String clientCode,Long orgId);

	Optional<LedgerMappingVO> getLedgerMappingbyId(Long id);

	List<LedgerMappingVO> getAllLedgerMapping(Long orgId, String clientCode);

	ExcelUploadResultDTO excelUploadForCCoa(MultipartFile[] files, String createdBy, String clientCode,
			String clientName, Long orgId)
			throws EncryptedDocumentException, io.jsonwebtoken.io.IOException, ApplicationException, IOException;

	ExcelUploadResultDTO excelUploadForLedgerMapping(MultipartFile[] files, String createdBy, String clientCode,Long orgId,String clientName) throws EncryptedDocumentException, IOException, ApplicationException;

	//SERVICELEVEL
	
	Map<String, Object> createUpdateServiceLevel(ServiceLevelDTO serviceLevelDTO) throws ApplicationException;

	Optional<ServiceLevelVO> getServiceLevelbyId(Long id);

	List<ServiceLevelVO> getAllServiceLevel(Long orgId);

	Optional<CoaVO> getCaoByCode(String code);

	

	


	

}
