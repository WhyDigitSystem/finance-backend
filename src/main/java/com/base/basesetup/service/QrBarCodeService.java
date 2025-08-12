package com.base.basesetup.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.base.basesetup.dto.CustomerAttachmentType;
import com.base.basesetup.dto.QrBarCodeDTO;
import com.base.basesetup.dto.SingleQrBarCodeDTO;
import com.base.basesetup.entity.QrBarCodeVO;
import com.base.basesetup.entity.SingleQrBarCodeVO;
import com.base.basesetup.exception.ApplicationException;

@Service
public interface QrBarCodeService {

	//QrBarCode
	
	List<QrBarCodeVO> getAllQrBarCodeByOrgId(Long orgId, String finyear, String branchCode);

	QrBarCodeVO getQrBarCodeById(Long id);
	
	Map<String, Object> createUpdateQrBarCode(QrBarCodeDTO qrBarCodeDTO) throws ApplicationException;
	
	String getQrBarCodeDocId(Long orgId, String finYear, String branch, String branchCode);

	int getTotalRows();

	int getSuccessfulUploads();

	void ExcelUploadForQrBarCode(MultipartFile[] files, CustomerAttachmentType type, String createdBy)
			throws ApplicationException;

	List<Map<String, Object>> getFillGridFromQrBarExcelUpload(String entryNo);
	
	
	//SingleBarCode


	SingleQrBarCodeVO getSingleQrBarCodeById(Long id);

	Map<String, Object> createUpdateSingleQrBarCode(SingleQrBarCodeDTO singleQrBarCodeDTO) throws ApplicationException;

	List<SingleQrBarCodeVO> getAllSingleQrBarCode(Long orgId);

}
