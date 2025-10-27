package com.base.basesetup.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.base.basesetup.dto.ElMfrDTO;
import com.base.basesetup.entity.ElMfrVO;
import com.base.basesetup.exception.ApplicationException;

@Service
public interface ELReportService {

	Map<String, Object> createUpdateElMfr(ElMfrDTO elMfrDTO) throws ApplicationException;

	List<ElMfrVO> getAllElMfr(Long orgId);

	int getTotalRows();

	int getSuccessfulUploads();

	void excelUploadForElMfr(MultipartFile[] files, String createdBy, Long orgId) throws ApplicationException, IOException;

	List<Map<String, Object>> getMisMatchClientTb(Long orgId, String clientCode);
	
	List<Map<String, Object>> getClientBudgetDetails(Long orgId,String year, String client,String clientCode);
	
	List<Map<String, Object>> getClientPreviousYearActualDetails(Long orgId,String year, String client,String clientCode);
	
	List<Map<String, Object>> getElevateYTDTBDetails(Long orgId,String clientCode, String finyear,String month);

	List<Map<String, Object>> getMonthlyProcess(Long orgId, String clientCode, String finyear, String month,
			String yearType, String mainGroupName, String subGroupCode);
	
	List<Map<String, Object>> getELBudgetReport(Long orgId, String clientCode, String finyear,
			String yearType, String mainGroupName, String subGroupCode);

	List<Map<String, Object>> getELPYReport(Long orgId, String finyear,String clientCode, 
			String mainGroupName, String subGroupCode,String month);

	List<Map<String, Object>> getELActualReport(Long orgId, String clientCode, String finyear, String yearType,
			String month, String mainGroupName, String subGroupCode);

	List<Map<String, Object>> getELActualQuaterReport(Long orgId, String clientCode, String finyear, String yearType,
			String month, String mainGroupName, String subGroupCode);

	List<Map<String, Object>> getELActualAutomaticReport(Long orgId, String finyear, String clientCode,
			String mainGroupName, String month, String yearType);

	List<Map<String, Object>> getELActualIncrementalProfitReport(Long orgId, String clientCode, String finyear,
			String yearType);

	List<Map<String, Object>> getELActualHeadCountReport(Long orgId, String clientCode, String finyear, String yearType,
			String month);


	List<Map<String, Object>> getELActualARAPReport(Long orgId, String clientCode, String finyear, String yearType,
			String month, String type);

	List<Map<String, Object>> getELActualRatioAnalysisReport(Long orgId, String finyear, String clientCode,
			String mainGroupName, String month, String yearType,String subGroup);

	List<Map<String, Object>> getELActualSalesPurchaseAnalysisReport(Long orgId, String finyear, String clientCode,
			String type, String month, String yearType);

	List<Map<String, Object>> getELBudgetAutomaticReport(Long orgId, String clientCode, String finyear, String yearType,
			String mainGroupName);

	List<Map<String, Object>> getELPyAutomaticReport(Long orgId, String clientCode, String finyear, String yearType,
			String mainGroupName,String month);

	List<Map<String, Object>> getELPyRatioAnalaysisReport(Long orgId, String clientCode, String finyear,
			String yearType, String mainGroupName, String subGroupName, String month);

	List<Map<String, Object>> getELBudgetHCReport(Long orgId, String finyear, String clientCode, String PreviousYear);

	List<Map<String, Object>> getELBudgetOBReport(Long orgId, String finYear, String clientCode, String previousYear);

	List<Map<String, Object>> getELBudgetRatioAnalysisDetails(Long orgId, String finyear, String clientCode,
			String PreviousYear);

	List<Map<String, Object>> getELBudgetSalesPurchaseAnalysisDetails(Long orgId, String finyear, String clientCode,
			String PreviousYear, String type);
	
	List<Map<String, Object>> getELPyOBReport(Long orgId,String previousYear, String clientCode,String month);

	List<Map<String, Object>> getELPyHCReport(Long orgId, String previousYear, String clientCode,String month);

	List<Map<String, Object>> getELPySalesPurchaseAnalysisDetails(Long orgId, String previousYear, String clientCode,
			String type,String month);

	List<Map<String, Object>> getELActualOBReport(Long orgId, String clientCode, String finYear, String month,
			String previousYear, String type);
}
