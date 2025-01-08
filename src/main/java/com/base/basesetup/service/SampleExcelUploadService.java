package com.base.basesetup.service;


import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.base.basesetup.dto.SampleExcelUploadDTO;
import com.base.basesetup.entity.SampleExcelUploadVO;
import com.base.basesetup.exception.ApplicationException;

@Service
public interface SampleExcelUploadService {

	 void ExcelUploadForSample(MultipartFile[] files) throws ApplicationException;
	    int getTotalRows();
	    int getSuccessfulUploads();
	 
	Map<String, Object> updateCreateSampleExcelUpload(@Valid SampleExcelUploadDTO sampleExcelUploadDTO) throws ApplicationException;
	
	List<SampleExcelUploadVO> getAllSampleExcelById(Long id);
	
	List<SampleExcelUploadVO> getAllSampleExcelUpload();
	
	
}
