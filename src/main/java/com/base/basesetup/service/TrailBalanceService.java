package com.base.basesetup.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.base.basesetup.dto.ExcelUploadResultDTO;
import com.base.basesetup.dto.TbHeaderDTO;
import com.base.basesetup.entity.TbHeaderVO;
import com.base.basesetup.exception.ApplicationException;

@Repository
public interface TrailBalanceService {


	int getTotalRows();

	int getSuccessfulUploads();


	Map<String, Object> createUpdateTrailBalance(TbHeaderDTO tbHeaderDTO) throws ApplicationException;

	ExcelUploadResultDTO excelUploadForTb(MultipartFile[] files, String createdBy, String clientCode, String finYear, String month,
			String clientName, Long orgId) throws ApplicationException, IOException;

	String getTBDocId(Long orgId, String finYear,String clientCode);

	List<Map<String, Object>> getFillGridForTB(Long orgId, String finYear,String tbMonth, String client,String clientCode);

	List<TbHeaderVO> getAllTbByClient(Long orgId, String finYear, String client);
	
	TbHeaderVO getTrialBalanceVOById(Long Id);
	
	ExcelUploadResultDTO excelUploadForBudget(MultipartFile[] files, String createdBy, String clientCode,
			String clientName, Long orgId) throws ApplicationException, IOException;

	ExcelUploadResultDTO excelUploadForPreviousYear(MultipartFile[] files, String createdBy, String clientCode,
			String clientName, Long orgId)throws ApplicationException, IOException;
	
	

}
