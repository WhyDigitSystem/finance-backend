package com.base.basesetup.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.base.basesetup.dto.FirstDataDTO;
import com.base.basesetup.entity.FirstDataVO;
import com.base.basesetup.exception.ApplicationException;

@Service
public interface ExcelFileUploadService {

	void ExcelUploadForSample(MultipartFile[] files, String createdBy) throws ApplicationException;

	int getTotalRows();

	int getSuccessfulUploads();

	List<FirstDataVO> getAllFirstData();

	Map<String, Object> updateCreateFirstData(@Valid FirstDataDTO firstDataDTO) throws ApplicationException;

	Optional<FirstDataVO> getFirstDataById(Long id);




}
