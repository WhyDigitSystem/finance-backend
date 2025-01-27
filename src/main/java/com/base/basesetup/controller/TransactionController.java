package com.base.basesetup.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.base.basesetup.common.CommonConstant;
import com.base.basesetup.common.UserConstants;
import com.base.basesetup.dto.AdjustmentJournalDTO;
import com.base.basesetup.dto.ArApAdjustmentOffSetDTO;
import com.base.basesetup.dto.BankingDepositDTO;
import com.base.basesetup.dto.BankingWithdrawalDTO;
import com.base.basesetup.dto.BrsOpeningDTO;
import com.base.basesetup.dto.ChartCostCenterDTO;
import com.base.basesetup.dto.ContraVoucherDTO;
import com.base.basesetup.dto.DailyMonthlyExRatesDTO;
import com.base.basesetup.dto.DebitNoteDTO;
import com.base.basesetup.dto.FundTransferDTO;
import com.base.basesetup.dto.GeneralJournalDTO;
import com.base.basesetup.dto.GlOpeningBalanceDTO;
import com.base.basesetup.dto.GstSalesVoucherDTO;
import com.base.basesetup.dto.PaymentReversalDTO;
import com.base.basesetup.dto.PaymentVoucherDTO;
import com.base.basesetup.dto.ReceiptReversalDTO;
import com.base.basesetup.dto.ReconcileBankDTO;
import com.base.basesetup.dto.ReconcileCashDTO;
import com.base.basesetup.dto.ReconcileCorpBankDTO;
import com.base.basesetup.dto.ResponseDTO;
import com.base.basesetup.dto.TmsJobCardDTO;
import com.base.basesetup.entity.AdjustmentJournalVO;
import com.base.basesetup.entity.ArApAdjustmentOffSetVO;
import com.base.basesetup.entity.BankingDepositVO;
import com.base.basesetup.entity.BankingWithdrawalVO;
import com.base.basesetup.entity.BrsExcelUploadVO;
import com.base.basesetup.entity.BrsOpeningVO;
import com.base.basesetup.entity.ChartCostCenterVO;
import com.base.basesetup.entity.ContraVoucherVO;
import com.base.basesetup.entity.DailyMonthlyExRatesVO;
import com.base.basesetup.entity.DebitNoteVO;
import com.base.basesetup.entity.FundTransferVO;
import com.base.basesetup.entity.GeneralJournalVO;
import com.base.basesetup.entity.GlOpeningBalanceVO;
import com.base.basesetup.entity.GstSalesVoucherVO;
import com.base.basesetup.entity.JobCardVO;
import com.base.basesetup.entity.PaymentReversalVO;
import com.base.basesetup.entity.PaymentVoucherVO;
import com.base.basesetup.entity.ReceiptReversalVO;
import com.base.basesetup.entity.ReconcileBankVO;
import com.base.basesetup.entity.ReconcileCashVO;
import com.base.basesetup.entity.ReconcileCorpBankVO;
import com.base.basesetup.service.TransactionService;

@CrossOrigin
@RestController
@RequestMapping("/api/transaction")
public class TransactionController extends BaseController {

	public static final Logger LOGGER = LoggerFactory.getLogger(TransactionController.class);

	@Autowired
	TransactionService transactionService;
	// IrnCredit

//	DailyMonthlyExRates
	@GetMapping("/getAllDailyMonthlyExRatesByOrgId")
	public ResponseEntity<ResponseDTO> getAllDailyMonthlyExRatesByOrgId(@RequestParam(required = false) Long orgId) {
		String methodName = "getAllDailyMonthlyExRatesByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<DailyMonthlyExRatesVO> dailyMonthlyExRatesVO = new ArrayList<>();
		try {
			dailyMonthlyExRatesVO = transactionService.getAllDailyMonthlyExRatesByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"DailyMonthlyExRates information get successfully ByOrgId");
			responseObjectsMap.put("dailyMonthlyExRatesVO", dailyMonthlyExRatesVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"DailyMonthlyExRates information receive failedByOrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getAllDailyMonthlyExRatesById")
	public ResponseEntity<ResponseDTO> getAllDailyMonthlyExRatesById(@RequestParam(required = false) Long id) {
		String methodName = "getAllDailyMonthlyExRatesById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<DailyMonthlyExRatesVO> dailyMonthlyExRatesVO = new ArrayList<>();
		try {
			dailyMonthlyExRatesVO = transactionService.getAllDailyMonthlyExRatesById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"DailyMonthlyExRates information get successfully By id");
			responseObjectsMap.put("dailyMonthlyExRatesVO", dailyMonthlyExRatesVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"DailyMonthlyExRates information receive failedByOrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/updateCreateDailyMonthlyExRates")
	public ResponseEntity<ResponseDTO> updateCreateDailyMonthlyExRates(
			@Valid @RequestBody DailyMonthlyExRatesDTO dailyMonthlyExRatesDTO) {
		String methodName = "updateCreateDailyMonthlyExRates()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
			DailyMonthlyExRatesVO dailyMonthlyExRatesVO = transactionService
					.updateCreateDailyMonthlyExRates(dailyMonthlyExRatesDTO);
			boolean isUpdate = dailyMonthlyExRatesDTO.getId() != null;

			if (dailyMonthlyExRatesVO != null) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
						isUpdate ? "DailyMonthlyExRates updated successfully"
								: "DailyMonthlyExRates created successfully");
				responseObjectsMap.put("dailyMonthlyExRatesVO", dailyMonthlyExRatesVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				errorMsg = isUpdate ? "DailyMonthlyExRates not found for ID: " + dailyMonthlyExRatesDTO.getId()
						: "DailyMonthlyExRates creation failed";
				responseDTO = createServiceResponseError(responseObjectsMap,
						isUpdate ? "DailyMonthlyExRates update failed" : "DailyMonthlyExRates creation failed",
						errorMsg);
			}
		} catch (Exception e) {
			errorMsg = e.getMessage();
			boolean isUpdate = dailyMonthlyExRatesDTO.getId() != null;
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap,
					isUpdate ? "DailyMonthlyExRates update failed" : "DailyMonthlyExRates creation failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

//	BrsOpening
	@GetMapping("/getAllBrsOpeningByOrgId")
	public ResponseEntity<ResponseDTO> getAllBrsOpeningByOrgId(@RequestParam Long orgId) {
		String methodName = "getAllBrsOpeningByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<BrsOpeningVO> brsOpeningVO = new ArrayList<>();
		try {
			brsOpeningVO = transactionService.getAllBrsOpeningByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "BrsOpening information get successfully ByOrgId");
			responseObjectsMap.put("brsOpeningVO", brsOpeningVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "BrsOpening information receive failedByOrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getAllBrsOpeningById")
	public ResponseEntity<ResponseDTO> getAllBrsOpeningById(@RequestParam Long id) {
		String methodName = "getAllBrsOpeningById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<BrsOpeningVO> brsOpeningVO = new ArrayList<>();
		try {
			brsOpeningVO = transactionService.getAllBrsOpeningById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "BrsOpening information get successfully By id");
			responseObjectsMap.put("brsOpeningVO", brsOpeningVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "BrsOpening information receive failedByOrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/updateCreateBrsOpening")
	public ResponseEntity<ResponseDTO> updateCreateBrsOpening(@Valid @RequestBody BrsOpeningDTO brsOpeningDTO) {
		String methodName = "updateCreateBrsOpening()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
			Map<String, Object> brsOpeningVO = transactionService.updateCreateBrsOpening(brsOpeningDTO);
			boolean isUpdate = brsOpeningDTO.getId() != null;

			if (brsOpeningVO != null) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
						isUpdate ? "BrsOpening updated successfully" : "BrsOpening created successfully");
				responseObjectsMap.put("brsOpeningVO", brsOpeningVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				errorMsg = isUpdate ? "BrsOpening not found for ID: " + brsOpeningDTO.getId()
						: "BrsOpening creation failed";
				responseDTO = createServiceResponseError(responseObjectsMap,
						isUpdate ? "BrsOpening update failed" : "BrsOpening creation failed", errorMsg);
			}
		} catch (Exception e) {
			errorMsg = e.getMessage();
			boolean isUpdate = brsOpeningDTO.getId() != null;
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap,
					isUpdate ? "BrsOpening update failed" : "BrsOpening creation failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getBrsOpeningByActive")
	public ResponseEntity<ResponseDTO> getBrsOpeningByActive() {
		String methodName = "getBrsOpeningByActive()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<BrsOpeningVO> brsOpeningVO = new ArrayList<>();
		try {
			brsOpeningVO = transactionService.getBrsOpeningByActive();
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "BrsOpening information get successfully By Active");
			responseObjectsMap.put("brsOpeningVO", brsOpeningVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"BrsOpening information receive failed By Active", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getBranchForBrsOpening")
	public ResponseEntity<ResponseDTO> getBranchForBrsOpening(@RequestParam Long orgId) {

		String methodName = "getBranchForBrsOpening()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> branch = new ArrayList<>();

		try {
			branch = transactionService.getBranchForBrsOpening(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Branch retrieved successfully");
			responseObjectsMap.put("branch", branch);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve Branch information",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

//	@PostMapping("/brsExcelUpload")
//	public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file) throws Exception {
//		transactionService.saveBrsExcelData(file);
//		return new ResponseEntity<>("File uploaded successfully", HttpStatus.OK);
//	}

	@PostMapping("/excelUploadForBrs")
	public ResponseEntity<ResponseDTO> ExcelUploadForBrs(@RequestParam MultipartFile[] files,
			@RequestParam Long orgId, @RequestParam String createdBy, String branch,
			String branchCode) {
		String methodName = "ExcelUploadForBrs()";
		int totalRows = 0;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		int successfulUploads = 0;
		ResponseDTO responseDTO = null;
		try {
			// Call service method to process Excel upload
			transactionService.ExcelUploadForBrs(files, orgId, createdBy, branch, branchCode);

			// Retrieve the counts after processing
			totalRows = transactionService.getTotalRows(); // Get total rows processed
			successfulUploads = transactionService.getSuccessfulUploads(); // Get successful uploads count
			// Construct success response
			responseObjectsMap.put("statusFlag", "Ok");
			responseObjectsMap.put("status", true);
			responseObjectsMap.put("totalRows", totalRows);
			responseObjectsMap.put("successfulUploads", successfulUploads);
			Map<String, Object> paramObjectsMap = new HashMap<>();
			paramObjectsMap.put("message", "Excel Upload For brs successful");
			responseObjectsMap.put("paramObjectsMap", paramObjectsMap);
			responseDTO = createServiceResponse(responseObjectsMap);

		} catch (Exception e) {

			String errorMsg = e.getMessage();
			LOGGER.error(CommonConstant.EXCEPTION, methodName, e);
			responseObjectsMap.put("statusFlag", "Error");
			responseObjectsMap.put("status", false);
			responseObjectsMap.put("errorMessage", errorMsg);

			responseDTO = createServiceResponseError(responseObjectsMap, "Excel Upload For Brs Failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getAllBrsExcelByOrgId")
	public ResponseEntity<ResponseDTO> getAllBrsExcelByOrgId(@RequestParam Long orgId) {
		String methodName = "getAllBrsExcelByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<BrsExcelUploadVO> brsExcelVO = new ArrayList<>();
		try {
			brsExcelVO = transactionService.getAllBrsExcelByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Brs Excel information get successfully ByOrgId");
			responseObjectsMap.put("brsExcelVO", brsExcelVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Brs Excel information receive failedByOrgId",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

//	ChartCostCenter
	@GetMapping("/getAllChartCostCenterByOrgId")
	public ResponseEntity<ResponseDTO> getAllChartCostCenterByOrgId(@RequestParam Long orgId) {
		String methodName = "getAllChartCostCenterByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ChartCostCenterVO> chartCostCenterVO = new ArrayList<>();
		try {
			chartCostCenterVO = transactionService.getAllChartCostCenterByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ChartCostCenter information get successfully ByOrgId");
			responseObjectsMap.put("chartCostCenterVO", chartCostCenterVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ChartCostCenter information receive failedByOrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getChartCostCenterById")
	public ResponseEntity<ResponseDTO> getChartCostCenterById(@RequestParam Long id) {
		String methodName = "getChartCostCenterById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ChartCostCenterVO> chartCostCenterVO = new ArrayList<>();
		try {
			chartCostCenterVO = transactionService.getChartCostCenterById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ChartCostCenter information get successfully By id");
			responseObjectsMap.put("chartCostCenterVO", chartCostCenterVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ChartCostCenter information receive failedByOrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/updateCreateChartCostCenter")
	public ResponseEntity<ResponseDTO> updateCreateChartCostCenter(
			@Valid @RequestBody List<ChartCostCenterDTO> chartCostCenterDTOList) {
		String methodName = "updateCreateChartCostCenter()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO;

		try {
			// Assuming the service method returns a list of maps containing messages and VO
			// objects
			List<Map<String, Object>> chartCostCenterResponses = transactionService
					.updateCreateChartCostCenterList(chartCostCenterDTOList);

			responseObjectsMap.put("chartCostCenterResponses", chartCostCenterResponses); // List of responses for each
																							// DTO
			responseDTO = createServiceResponse(responseObjectsMap);

		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getChartCostCenterByActive")
	public ResponseEntity<ResponseDTO> getChartCostCenterByActive() {
		String methodName = "getChartCostCenterByActive()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ChartCostCenterVO> chartCostCenterVO = new ArrayList<>();
		try {
			chartCostCenterVO = transactionService.getChartCostCenterByActive();
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ChartCostCenter information get successfully By Active");
			responseObjectsMap.put("chartCostCenterVO", chartCostCenterVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ChartCostCenter information receive failed By Active", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getChartCostCenterDocId")
	public ResponseEntity<ResponseDTO> getChartCostCenterDocId(@RequestParam Long orgId, @RequestParam String finYear,
			@RequestParam String branch, @RequestParam String branchCode) {

		String methodName = "getChartCostCenterDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		String mapp = "";

		try {
			mapp = transactionService.getChartCostCenterDocId(orgId, finYear, branch, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ChartCostCenter DocId information retrieved successfully");
			responseObjectsMap.put("chartCostCenterDocId", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Failed to retrieve ChartCostCenter Docid information", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

//	FundTransfer
	@GetMapping("/getAllFundTransferByOrgId")
	public ResponseEntity<ResponseDTO> getAllFundTransferByOrgId(@RequestParam(required = false) Long orgId) {
		String methodName = "getAllFundTransferByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<FundTransferVO> fundTransferVO = new ArrayList<>();
		try {
			fundTransferVO = transactionService.getAllFundTransferByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "FundTransfer information get successfully ByOrgId");
			responseObjectsMap.put("fundTransferVO", fundTransferVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"FundTransfer information receive failedByOrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getAllFundTransferById")
	public ResponseEntity<ResponseDTO> getAllFundTransferById(@RequestParam(required = false) Long id) {
		String methodName = "getAllFundTransferById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<FundTransferVO> fundTransferVO = new ArrayList<>();
		try {
			fundTransferVO = transactionService.getAllFundTransferById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "FundTransfer information get successfully By id");
			responseObjectsMap.put("fundTransferVO", fundTransferVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"FundTransfer information receive failedByOrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/updateCreateFundTransfer")
	public ResponseEntity<ResponseDTO> updateCreateFundTransfer(@Valid @RequestBody FundTransferDTO fundTransferDTO) {
		String methodName = "updateCreateFundTransfer()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
			Map<String, Object> fundTransferVO = transactionService.updateCreateFundTransfer(fundTransferDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, fundTransferVO.get("message"));
			responseObjectsMap.put("fundTransferVO", fundTransferVO.get("fundTransferVO")); // Corrected key
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getFundTransferByActive")
	public ResponseEntity<ResponseDTO> getFundTransferByActive() {
		String methodName = "getFundTransferByActive()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<FundTransferVO> fundTransferVO = new ArrayList<>();
		try {
			fundTransferVO = transactionService.getFundTransferByActive();
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"FundTransfer information get successfully By Active");
			responseObjectsMap.put("fundTransferVO", fundTransferVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"FundTransfer information receive failed By Active", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getFundTranferByDocId")
	public ResponseEntity<ResponseDTO> getFundTranferByDocId(@RequestParam Long orgId, @RequestParam String docId) {
		String methodName = "getFundTranferByDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		FundTransferVO fundTransferVO = new FundTransferVO();
		try {
			fundTransferVO = transactionService.getFundTranferByDocId(orgId, docId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Fund Tranfer Docid information get successfully");
			responseObjectsMap.put("fundTransferVO", fundTransferVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Fund Tranfer Docid information receive failed By docid", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getFundTranferDocId")
	public ResponseEntity<ResponseDTO> getFundTranferDocId(@RequestParam Long orgId, @RequestParam String finYear,
			@RequestParam String branch, @RequestParam String branchCode) {

		String methodName = "getFundTranferDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		String mapp = "";

		try {
			mapp = transactionService.getFundTranferDocId(orgId, finYear, branch, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"Fund Tranfer Docid information retrieved successfully");
			responseObjectsMap.put("fundTransferDocId", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Failed to retrieve Fund Tranfer Docid information", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

//	GeneralJournal
	@GetMapping("/getAllGeneralJournalByOrgId")
	public ResponseEntity<ResponseDTO> getAllGeneralJournalByOrgId(@RequestParam Long orgId) {
		String methodName = "getAllGeneralJournalByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<GeneralJournalVO> generalJournalVO = new ArrayList<>();
		try {
			generalJournalVO = transactionService.getAllGeneralJournalByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"GeneralJournal information get successfully ByOrgId");
			responseObjectsMap.put("generalJournalVO", generalJournalVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"GeneralJournal information receive failedByOrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getGeneralJournalById")
	public ResponseEntity<ResponseDTO> getGeneralJournalById(@RequestParam Long id) {
		String methodName = "getGeneralJournalById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<GeneralJournalVO> generalJournalVO = new ArrayList<>();
		try {
			generalJournalVO = transactionService.getGeneralJournalById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "GeneralJournal information get successfully By id");
			responseObjectsMap.put("generalJournalVO", generalJournalVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"GeneralJournal information receive failedByOrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/updateCreateGeneralJournal")
	public ResponseEntity<ResponseDTO> updateCreateGeneralJournal(
			@Valid @RequestBody GeneralJournalDTO generalJournalDTO) {
		String methodName = "updateCreateGeneralJournal()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> generalJournalVO = transactionService.updateCreateGeneralJournal(generalJournalDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, generalJournalVO.get("message"));
			responseObjectsMap.put("generalJournalVO", generalJournalVO.get("generalJournalVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getGeneralJournalByActive")
	public ResponseEntity<ResponseDTO> getGeneralJournalByActive() {
		String methodName = "getGeneralJournalByActive()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<GeneralJournalVO> generalJournalVO = new ArrayList<>();
		try {
			generalJournalVO = transactionService.getGeneralJournalByActive();
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"GeneralJournal information get successfully By Active");
			responseObjectsMap.put("generalJournalVO", generalJournalVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"GeneralJournal information receive failed By Active", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getGeneralJournalDocId")
	public ResponseEntity<ResponseDTO> getGeneralJournalDocId(@RequestParam Long orgId, @RequestParam String finYear,
			@RequestParam String branch, @RequestParam String branchCode) {

		String methodName = "getGeneralJournalDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		String mapp= null;

		try {
			mapp = transactionService.getGeneralJournalDocId(orgId, finYear, branch, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"GeneralJournal DocId information retrieved successfully");
			responseObjectsMap.put("generalJournalDocId", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Failed to retrieve GeneralJournal Docid information", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getAccountNameFromGroup")
	public ResponseEntity<ResponseDTO> getAccountNameFromGroup(
			@RequestParam Long orgId) {

		String methodName = "getAccountNameFromGroup()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mov = new ArrayList<>();
		try {
			mov = transactionService.getAccountNameFromGroup(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					" AccountName from Group information retrieved successfully");
			responseObjectsMap.put("generalJournalVO", mov);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Failed to retrieve AccountName from Group information", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	
	// DebitNote

	@GetMapping("/getAllDebitNoteByOrgId")
	public ResponseEntity<ResponseDTO> getAllDebitNoteByOrgId(@RequestParam(required = false) Long orgId) {
		String methodName = "getAllDebitNoteByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<DebitNoteVO> debitNoteVO = new ArrayList<>();
		try {
			debitNoteVO = transactionService.getAllDebitNoteByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "DebitNote information get successfully By OrgId");
			responseObjectsMap.put("debitNoteVO", debitNoteVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"DebitNote information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getAllDebitNoteById")
	public ResponseEntity<ResponseDTO> getAllDebitNoteById(@RequestParam(required = false) Long id) {
		String methodName = "getAllDebitNoteById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<DebitNoteVO> debitNoteVO = new ArrayList<>();
		try {
			debitNoteVO = transactionService.getAllDebitNoteById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "DebitNote information get successfully By id");
			responseObjectsMap.put("debitNoteVO", debitNoteVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"DebitNote information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/updateCreateDebitNote")
	public ResponseEntity<ResponseDTO> updateCreateDebitNote(@Valid @RequestBody DebitNoteDTO debitNoteDTO) {
		String methodName = "updateCreateDebitNote()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
			DebitNoteVO debitNoteVO = transactionService.updateCreateDebitNote(debitNoteDTO);
			boolean isUpdate = debitNoteDTO.getId() != null;

			if (debitNoteVO != null) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
						isUpdate ? "DebitNote updated successfully" : "DebitNote created successfully");
				responseObjectsMap.put("debitNoteVO", debitNoteVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				errorMsg = isUpdate ? "DebitNote not found for ID: " + debitNoteDTO.getId()
						: "DebitNote creation failed";
				responseDTO = createServiceResponseError(responseObjectsMap,
						isUpdate ? "DebitNote update failed" : "DebitNote creation failed", errorMsg);
			}
		} catch (Exception e) {
			errorMsg = e.getMessage();
			boolean isUpdate = debitNoteDTO.getId() != null;
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap,
					isUpdate ? "DebitNote update failed" : "DebitNote creation failed", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getDebitNoteByActive")
	public ResponseEntity<ResponseDTO> getDebitNoteByActive() {
		String methodName = "getDebitNoteByActive()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<DebitNoteVO> debitNoteVO = new ArrayList<>();
		try {
			debitNoteVO = transactionService.getDebitNoteByActive();
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "DebitNote information get successfully By Active");
			responseObjectsMap.put("debitNoteVO", debitNoteVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"DebitNote information receive failed By Active", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	// GstSalesVoucher

	@GetMapping("/getAllGstSalesVoucherByOrgId")
	public ResponseEntity<ResponseDTO> getAllGstSalesVoucherByOrgId(@RequestParam(required = false) Long orgId) {
		String methodName = "getAllGstSalesVoucherByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<GstSalesVoucherVO> gstSalesVoucherVO = new ArrayList<>();
		try {
			gstSalesVoucherVO = transactionService.getAllGstSalesVoucherByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"GstSalesVoucher information get successfully By OrgId");
			responseObjectsMap.put("gstSalesVoucherVO", gstSalesVoucherVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"GstSalesVoucher information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getAllGstSalesVoucherById")
	public ResponseEntity<ResponseDTO> getAllGstSalesVoucherById(@RequestParam(required = false) Long id) {
		String methodName = "getAllGstSalesVoucherById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<GstSalesVoucherVO> gstSalesVoucherVO = new ArrayList<>();
		try {
			gstSalesVoucherVO = transactionService.getAllGstSalesVoucherById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "GstSalesVoucher information get successfully By id");
			responseObjectsMap.put("gstSalesVoucherVO", gstSalesVoucherVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"GstSalesVoucher information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/updateCreateGstSalesVoucher")
	public ResponseEntity<ResponseDTO> updateCreateGstSalesVoucher(
			@Valid @RequestBody GstSalesVoucherDTO gstSalesVoucherDTO) {
		String methodName = "updateCreateGstSalesVoucher()";

		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
			GstSalesVoucherVO gstSalesVoucherVO = transactionService.updateCreateGstSalesVoucher(gstSalesVoucherDTO);
			boolean isUpdate = gstSalesVoucherDTO.getId() != null;

			if (gstSalesVoucherVO != null) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
						isUpdate ? "GstSalesVoucher updated successfully" : "GstSalesVoucher created successfully");
				responseObjectsMap.put("gstSalesVoucherVO", gstSalesVoucherVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				errorMsg = isUpdate ? "GstSalesVoucher not found for ID: " + gstSalesVoucherDTO.getId()
						: "GstSalesVoucher creation failed";
				responseDTO = createServiceResponseError(responseObjectsMap,
						isUpdate ? "GstSalesVoucher update failed" : "GstSalesVoucher creation failed", errorMsg);
			}
		} catch (Exception e) {
			errorMsg = e.getMessage();
			boolean isUpdate = gstSalesVoucherDTO.getId() != null;
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap,
					isUpdate ? "GstSalesVoucher update failed" : "GstSalesVoucher creation failed", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getGstSalesVoucherByActive")
	public ResponseEntity<ResponseDTO> getGstSalesVoucherByActive() {
		String methodName = "getGstSalesVoucherByActive()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<GstSalesVoucherVO> gstSalesVoucherVO = new ArrayList<>();
		try {
			gstSalesVoucherVO = transactionService.getGstSalesVoucherByActive();
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"GstSalesVoucher information get successfully By Active");
			responseObjectsMap.put("gstSalesVoucherVO", gstSalesVoucherVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"GstSalesVoucher information receive failed By Active", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	// PaymentVoucher

	@GetMapping("/getAllPaymentVoucherByOrgId")
	public ResponseEntity<ResponseDTO> getAllPaymentVoucherByOrgId(@RequestParam Long orgId) {
		String methodName = "getAllPaymentVoucherByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<PaymentVoucherVO> paymentVoucherVO = new ArrayList<>();
		try {
			paymentVoucherVO = transactionService.getAllPaymentVoucherByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"PaymentVoucher information get successfully By OrgId");
			responseObjectsMap.put("paymentVoucherVO", paymentVoucherVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"PaymentVoucher information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getPaymentVoucherById")
	public ResponseEntity<ResponseDTO> getPaymentVoucherById(@RequestParam Long id) {
		String methodName = "getPaymentVoucherById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<PaymentVoucherVO> paymentVoucherVO = new ArrayList<>();
		try {
			paymentVoucherVO = transactionService.getPaymentVoucherById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "PaymentVoucher information get successfully By id");
			responseObjectsMap.put("paymentVoucherVO", paymentVoucherVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"PaymentVoucher information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/updateCreatePaymentVoucher")
	public ResponseEntity<ResponseDTO> updateCreatePaymentVoucher(
			@Valid @RequestBody PaymentVoucherDTO paymentVoucherDTO) {
		String methodName = "updateCreatePaymentVoucher()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
			Map<String, Object> paymentVoucherVO = transactionService.updateCreatePaymentVoucher(paymentVoucherDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, paymentVoucherVO.get("message"));
			responseObjectsMap.put("paymentVoucherVO", paymentVoucherVO.get("paymentVoucherVO")); // Corrected key
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getPaymentVoucherByActive")
	public ResponseEntity<ResponseDTO> getPaymentVoucherByActive() {
		String methodName = "getPaymentVoucherByActive()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<PaymentVoucherVO> paymentVoucherVO = new ArrayList<>();
		try {
			paymentVoucherVO = transactionService.getPaymentVoucherByActive();
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"PaymentVoucher information get successfully By Active");
			responseObjectsMap.put("paymentVoucherVO", paymentVoucherVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"PaymentVoucher information receive failed By Active", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getpaymentVoucherByDocId")
	public ResponseEntity<ResponseDTO> getpaymentVoucherByDocId(@RequestParam Long orgId, @RequestParam String docId) {
		String methodName = "getpaymentVoucherByDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		PaymentVoucherVO paymentVoucherVO = new PaymentVoucherVO();
		try {
			paymentVoucherVO = transactionService.getpaymentVoucherByDocId(orgId, docId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"PaymentVoucher information get successfully By docid");
			responseObjectsMap.put("paymentVoucherVO", paymentVoucherVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"PaymentVoucher information receive failed By docid", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getpaymentVoucherDocId")
	public ResponseEntity<ResponseDTO> getpaymentVoucherDocId(@RequestParam Long orgId, @RequestParam String finYear,
			@RequestParam String branch, @RequestParam String branchCode) {

		String methodName = "getpaymentVoucherDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		String mapp = "";

		try {
			mapp = transactionService.getpaymentVoucherDocId(orgId, finYear, branch, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"paymentVoucher DocId information retrieved successfully");
			responseObjectsMap.put("paymentVoucherDocId", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Failed to retrieve  paymentVoucher DocId information", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	
	@GetMapping("/getCurrencyAndExrateDetails")
	public ResponseEntity<ResponseDTO> getCurrencyAndExrateDetails(@RequestParam Long orgId) {
		String methodName = "getCurrencyAndExrateDetails()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String,Object>> mapp = new ArrayList<>();

		try {
			mapp = transactionService.getCurrencyAndExrates(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Currency Details retrieved successfully");
			responseObjectsMap.put("currencyVO", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Failed to retrieve Currency Details", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	
	// ReceiptReversal

	@GetMapping("/getAllReceiptReversalByOrgId")
	public ResponseEntity<ResponseDTO> getAllReceiptReversalByOrgId(@RequestParam(required = false) Long orgId) {
		String methodName = "getAllReceiptReversalByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ReceiptReversalVO> receiptReversalVO = new ArrayList<>();
		try {
			receiptReversalVO = transactionService.getAllReceiptReversalByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ReceiptReversal information get successfully By OrgId");
			responseObjectsMap.put("receiptReversalVO", receiptReversalVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ReceiptReversal information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getAllReceiptReversalById")
	public ResponseEntity<ResponseDTO> getAllReceiptReversalById(@RequestParam(required = false) Long id) {
		String methodName = "getAllReceiptReversalById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ReceiptReversalVO> receiptReversalVO = new ArrayList<>();
		try {
			receiptReversalVO = transactionService.getAllReceiptReversalById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ReceiptReversal information get successfully By id");
			responseObjectsMap.put("receiptReversalVO", receiptReversalVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ReceiptReversal information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/updateCreateReceiptReversal")
	public ResponseEntity<ResponseDTO> updateCreateReceiptReversal(
			@Valid @RequestBody ReceiptReversalDTO receiptReversalDTO) {
		String methodName = "updateCreateReceiptReversal()";

		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
			ReceiptReversalVO receiptReversalVO = transactionService.updateCreateReceiptReversal(receiptReversalDTO);
			boolean isUpdate = receiptReversalDTO.getId() != null;

			if (receiptReversalVO != null) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
						isUpdate ? "ReceiptReversal updated successfully" : "ReceiptReversal created successfully");
				responseObjectsMap.put("receiptReversalVO", receiptReversalVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				errorMsg = isUpdate ? "ReceiptReversal not found for ID: " + receiptReversalDTO.getId()
						: "ReceiptReversal creation failed";
				responseDTO = createServiceResponseError(responseObjectsMap,
						isUpdate ? "ReceiptReversal update failed" : "ReceiptReversal creation failed", errorMsg);
			}
		} catch (Exception e) {
			errorMsg = e.getMessage();
			boolean isUpdate = receiptReversalDTO.getId() != null;
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap,
					isUpdate ? "ReceiptReversal update failed" : "ReceiptReversal creation failed", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getReceiptReversalByActive")
	public ResponseEntity<ResponseDTO> getReceiptReversalByActive() {
		String methodName = "getReceiptReversalByActive()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ReceiptReversalVO> receiptReversalVO = new ArrayList<>();
		try {
			receiptReversalVO = transactionService.getReceiptReversalByActive();
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ReceiptReversal information get successfully By Active");
			responseObjectsMap.put("receiptReversalVO", receiptReversalVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ReceiptReversal information receive failed By Active", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	// PaymentReversal

	@GetMapping("/getAllPaymentReversalByOrgId")
	public ResponseEntity<ResponseDTO> getAllPaymentReversalByOrgId(@RequestParam(required = false) Long orgId) {
		String methodName = "getAllPaymentReversalByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<PaymentReversalVO> paymentReversalVO = new ArrayList<>();
		try {
			paymentReversalVO = transactionService.getAllPaymentReversalByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"PaymentReversal information get successfully By OrgId");
			responseObjectsMap.put("PaymentReversalVO", paymentReversalVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"PaymentReversal information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getAllPaymentReversalById")
	public ResponseEntity<ResponseDTO> getAllPaymentReversalById(@RequestParam(required = false) Long id) {
		String methodName = "getAllPaymentReversalById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<PaymentReversalVO> paymentReversalVO = new ArrayList<>();
		try {
			paymentReversalVO = transactionService.getAllPaymentReversalById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "PaymentReversal information get successfully By id");
			responseObjectsMap.put("paymentReversalVO", paymentReversalVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"PaymentReversal information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/updateCreatePaymentReversal")
	public ResponseEntity<ResponseDTO> updateCreatePaymentReversal(
			@Valid @RequestBody PaymentReversalDTO paymentReversalDTO) {
		String methodName = "updateCreatePaymentReversal()";

		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
			PaymentReversalVO paymentReversalVO = transactionService.updateCreatePaymentReversal(paymentReversalDTO);
			boolean isUpdate = paymentReversalDTO.getId() != null;

			if (paymentReversalVO != null) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
						isUpdate ? "PaymentReversal updated successfully" : "PaymentReversal created successfully");
				responseObjectsMap.put("paymentReversalVO", paymentReversalVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				errorMsg = isUpdate ? "PaymentReversal not found for ID: " + paymentReversalDTO.getId()
						: "PaymentReversal creation failed";
				responseDTO = createServiceResponseError(responseObjectsMap,
						isUpdate ? "PaymentReversal update failed" : "PaymentReversal creation failed", errorMsg);
			}
		} catch (Exception e) {
			errorMsg = e.getMessage();
			boolean isUpdate = paymentReversalDTO.getId() != null;
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap,
					isUpdate ? "PaymentReversal update failed" : "PaymentReversal creation failed", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getPaymentReversalByActive")
	public ResponseEntity<ResponseDTO> getPaymentReversalByActive() {
		String methodName = "getPaymentReversalByActive()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<PaymentReversalVO> paymentReversalVO = new ArrayList<>();
		try {
			paymentReversalVO = transactionService.getPaymentReversalByActive();
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"PaymentReversal information get successfully By Active");
			responseObjectsMap.put("paymentReversalVO", paymentReversalVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"PaymentReversal information receive failed By Active", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	// ArApAdjustmentOffSet

	@GetMapping("/getAllArApAdjustmentOffSetByOrgId")
	public ResponseEntity<ResponseDTO> getAllArApAdjustmentOffSetByOrgId(@RequestParam(required = false) Long orgId) {
		String methodName = "getAllArApAdjustmentOffSetByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ArApAdjustmentOffSetVO> arApAdjustmentOffSetVO = new ArrayList<>();
		try {
			arApAdjustmentOffSetVO = transactionService.getAllArApAdjustmentOffSetByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ArApAdjustmentOffSet information get successfully By OrgId");
			responseObjectsMap.put("arApAdjustmentOffSetVO", arApAdjustmentOffSetVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ArApAdjustmentOffSet information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getAllArApAdjustmentOffSetById")
	public ResponseEntity<ResponseDTO> getAllArApAdjustmentOffSetById(@RequestParam(required = false) Long id) {
		String methodName = "getAllArApAdjustmentOffSetById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ArApAdjustmentOffSetVO> arApAdjustmentOffSetVO = new ArrayList<>();
		try {
			arApAdjustmentOffSetVO = transactionService.getAllArApAdjustmentOffSetById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ArApAdjustmentOffSet information get successfully By id");
			responseObjectsMap.put("arApAdjustmentOffSetVO", arApAdjustmentOffSetVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ArApAdjustmentOffSet information receive failed By Id", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/updateCreateArApAdjustmentOffSet")
	public ResponseEntity<ResponseDTO> updateCreateArApAdjustmentOffSet(
			@Valid @RequestBody ArApAdjustmentOffSetDTO arApAdjustmentOffSetDTO) {
		String methodName = "updateCreateArApAdjustmentOffSet()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object>arApAdjustmentOffSetVO  = transactionService.updateCreateArApAdjustmentOffSet(arApAdjustmentOffSetDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, arApAdjustmentOffSetVO.get("message"));
			responseObjectsMap.put("arApAdjustmentOffSetVO", arApAdjustmentOffSetVO.get("arApAdjustmentOffSetVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	
	}
	@GetMapping("/getArApAdjustmentOffSetDocId")
	public ResponseEntity<ResponseDTO> getArApAdjustmentOffSetDocId(@RequestParam Long orgId, @RequestParam String finYear,
			@RequestParam String branch, @RequestParam String branchCode) {

		String methodName = "getArApAdjustmentOffSetDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		String mapp = "";

		try {
			mapp = transactionService.getArApAdjustmentOffSetDocId(orgId, finYear, branch, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ArApAdjustmentOffSetDocId information retrieved successfully");
			responseObjectsMap.put("arApAdjustmentOffSetDocId", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Failed to retrieve ArApAdjustmentOffSetDocId information", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	// GlOpeningBalance

	@GetMapping("/getAllGlOpeningBalanceByOrgId")
	public ResponseEntity<ResponseDTO> getAllGlOpeningBalanceByOrgId(@RequestParam(required = false) Long orgId) {
		String methodName = "getAllGlOpeningBalanceByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<GlOpeningBalanceVO> glOpeningBalanceVO = new ArrayList<>();
		try {
			glOpeningBalanceVO = transactionService.getAllGlOpeningBalanceByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"GlOpeningBalance information get successfully By OrgId");
			responseObjectsMap.put("glOpeningBalanceVO", glOpeningBalanceVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"GlOpeningBalance information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getAllGlOpeningBalanceById")
	public ResponseEntity<ResponseDTO> getAllGlOpeningBalanceById(@RequestParam(required = false) Long id) {
		String methodName = "getAllGlOpeningBalanceById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<GlOpeningBalanceVO> glOpeningBalanceVO = new ArrayList<>();
		try {
			glOpeningBalanceVO = transactionService.getAllGlOpeningBalanceById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"GlOpeningBalance information get successfully By id");
			responseObjectsMap.put("glOpeningBalanceVO", glOpeningBalanceVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"GlOpeningBalance information receive failedByOrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/updateCreateGlOpeningBalance")
	public ResponseEntity<ResponseDTO> updateCreateGlOpeningBalance(
			@Valid @RequestBody GlOpeningBalanceDTO glOpeningBalanceDTO) {
		String methodName = "updateCreateGlOpeningBalance()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
			Map<String, Object> glOpeningBalanceVO = transactionService
					.updateCreateGlOpeningBalance(glOpeningBalanceDTO);
			boolean isUpdate = glOpeningBalanceDTO.getId() != null;

			if (glOpeningBalanceVO != null) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
						isUpdate ? "GlOpeningBalance updated successfully" : "GlOpeningBalance created successfully");
				responseObjectsMap.put("glOpeningBalanceVO", glOpeningBalanceVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				errorMsg = isUpdate ? "GlOpeningBalance not found for ID: " + glOpeningBalanceDTO.getId()
						: "GlOpeningBalance creation failed";
				responseDTO = createServiceResponseError(responseObjectsMap,
						isUpdate ? "GlOpeningBalance update failed" : "GlOpeningBalance creation failed", errorMsg);
			}
		} catch (Exception e) {
			errorMsg = e.getMessage();
			boolean isUpdate = glOpeningBalanceDTO.getId() != null;
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap,
					isUpdate ? "GlOpeningBalance update failed" : "GlOpeningBalance creation failed", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getGlOpeningBalanceByActive")
	public ResponseEntity<ResponseDTO> getGlOpeningBalanceByActive() {
		String methodName = "getGlOpeningBalanceByActive()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<GlOpeningBalanceVO> glOpeningBalanceVO = new ArrayList<>();
		try {
			glOpeningBalanceVO = transactionService.getGlOpeningBalanceByActive();
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"GlOpeningBalance information get successfully By Active");
			responseObjectsMap.put("glOpeningBalanceVO", glOpeningBalanceVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"GlOpeningBalance information receive failed By Active", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getGlOpeningBalanceDocId")
	public ResponseEntity<ResponseDTO> getGlOpeningBalanceDocId(@RequestParam Long orgId, @RequestParam String finYear,
			@RequestParam String branch, @RequestParam String branchCode) {

		String methodName = "getGlOpeningBalanceDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		String mapp = "";

		try {
			mapp = transactionService.getGlOpeningBalanceDocId(orgId, finYear, branch, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"GlOpeningBalanceDocId information retrieved successfully");
			responseObjectsMap.put("GlOpeningBalanceDocId", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Failed to retrieve GlOpeningBalanceDocId information", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getGlOpeningBalanceByDocId")
	public ResponseEntity<ResponseDTO> getGlOpeningBalanceByDocId(@RequestParam Long orgId,
			@RequestParam String docId) {
		String methodName = "getGlOpeningBalanceByDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		GlOpeningBalanceVO glOpeningBalanceVO = new GlOpeningBalanceVO();
		try {
			glOpeningBalanceVO = transactionService.getGlOpeningBalanceByDocId(orgId, docId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"GlOpeningBalance information get successfully By docid");
			responseObjectsMap.put("glOpeningBalanceVO", glOpeningBalanceVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"GlOpeningBalance information receive failed By docid", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	// ReconcileBank

	@GetMapping("/getAllReconcileBankByOrgId")
	public ResponseEntity<ResponseDTO> getAllReconcileBankByOrgId(@RequestParam(required = false) Long orgId) {
		String methodName = "getAllReconcileBankByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ReconcileBankVO> reconcileBankVO = new ArrayList<>();
		try {
			reconcileBankVO = transactionService.getAllReconcileBankByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ReconcileBank information get successfully By OrgId");
			responseObjectsMap.put("reconcileBankVO", reconcileBankVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ReconcileBank information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getAllReconcileBankById")
	public ResponseEntity<ResponseDTO> getAllReconcileBankById(@RequestParam(required = false) Long id) {
		String methodName = "getAllReconcileBankById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ReconcileBankVO> reconcileBankVO = new ArrayList<>();
		try {
			reconcileBankVO = transactionService.getAllReconcileBankById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ReconcileBank information get successfully By id");
			responseObjectsMap.put("reconcileBankVO", reconcileBankVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ReconcileBank information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/updateCreateReconcileBank")
	public ResponseEntity<ResponseDTO> updateCreateReconcileBank(
			@Valid @RequestBody ReconcileBankDTO reconcileBankDTO) {
		String methodName = "updateCreateReconcileBank()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
			Map<String, Object> reconcileBankVO = transactionService.updateCreateReconcileBank(reconcileBankDTO);
			boolean isUpdate = reconcileBankDTO.getId() != null;

			if (reconcileBankVO != null) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
						isUpdate ? "ReconcileBank updated successfully" : "ReconcileBank created successfully");
				responseObjectsMap.put("reconcileBankVO", reconcileBankVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				errorMsg = isUpdate ? "ReconcileBank not found for ID: " + reconcileBankDTO.getId()
						: "ReconcileBank creation failed";
				responseDTO = createServiceResponseError(responseObjectsMap,
						isUpdate ? "ReconcileBank update failed" : "ReconcileBank creation failed", errorMsg);
			}
		} catch (Exception e) {
			errorMsg = e.getMessage();
			boolean isUpdate = reconcileBankDTO.getId() != null;
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap,
					isUpdate ? "ReconcileBank update failed" : "ReconcileBank creation failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getReconcileBankDocId")
	public ResponseEntity<ResponseDTO> getReconcileBankDocId(@RequestParam Long orgId, @RequestParam String finYear,
			@RequestParam String branch, @RequestParam String branchCode) {

		String methodName = "getReconcileBankDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		String mapp = "";

		try {
			mapp = transactionService.getReconcileBankDocId(orgId, finYear, branch, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ReconcileBankDocId information retrieved successfully");
			responseObjectsMap.put("reconcileBankDocId", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Failed to retrieve ReconcileBankDocId information", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getBankNameForGroupLedger")
	public ResponseEntity<ResponseDTO> getBankNameForGroupLedger(@RequestParam Long orgId) {
		String methodName = "getBankNameForGroupLedger()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> accountName = new ArrayList<>();
		try {
			accountName = transactionService.getBankNameForGroupLedger(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, " AccountName from Group information retrieved successfully");
			responseObjectsMap.put("accountName", accountName);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Failed to retrieve AccountName from Group information", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	// reconcileCorpBank

	@GetMapping("/getAllReconcileCorpBankByOrgId")
	public ResponseEntity<ResponseDTO> getAllReconcileCorpBankByOrgId(@RequestParam(required = false) Long orgId) {
		String methodName = "getAllReconcileCorpBankByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ReconcileCorpBankVO> reconcileCorpBankVO = new ArrayList<>();
		try {
			reconcileCorpBankVO = transactionService.getAllReconcileCorpBankByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ReconcileCorpBank information get successfully By OrgId");
			responseObjectsMap.put("reconcileCorpBankVO", reconcileCorpBankVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ReconcileCorpBank information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getAllReconcileCorpBankById")
	public ResponseEntity<ResponseDTO> getAllReconcileCorpBankById(@RequestParam(required = false) Long id) {
		String methodName = "getAllReconcileCorpBankById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ReconcileCorpBankVO> reconcileCorpBankVO = new ArrayList<>();
		try {
			reconcileCorpBankVO = transactionService.getAllReconcileCorpBankById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ReconcileCorpBank information get successfully By id");
			responseObjectsMap.put("reconcileCorpBankVO", reconcileCorpBankVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ReconcileCorpBank information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/updateCreateReconcileCorpBank")
	public ResponseEntity<ResponseDTO> updateCreateReconcileCorpBank(
			@Valid @RequestBody ReconcileCorpBankDTO reconcileCorpBankDTO) {
		String methodName = "updateCreateReconcileCorpBank()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
			Map<String, Object> reconcileCorpBankVO = transactionService
					.updateCreateReconcileCorpBank(reconcileCorpBankDTO);
			boolean isUpdate = reconcileCorpBankDTO.getId() != null;

			if (reconcileCorpBankVO != null) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
						isUpdate ? "ReconcileCorpBank updated successfully" : "ReconcileCorpBank created successfully");
				responseObjectsMap.put("reconcileCorpBankVO", reconcileCorpBankVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				errorMsg = isUpdate ? "ReconcileCorpBank not found for ID: " + reconcileCorpBankDTO.getId()
						: "ReconcileCorpBank creation failed";
				responseDTO = createServiceResponseError(responseObjectsMap,
						isUpdate ? "ReconcileCorpBank update failed" : "ReconcileCorpBank creation failed", errorMsg);
			}
		} catch (Exception e) {
			errorMsg = e.getMessage();
			boolean isUpdate = reconcileCorpBankDTO.getId() != null;
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap,
					isUpdate ? "ReconcileCorpBank update failed" : "ReconcileCorpBank creation failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	

	@GetMapping("/getReconcileCorpBankDocId")
	public ResponseEntity<ResponseDTO> getReconcileCorpBankDocId(@RequestParam Long orgId, @RequestParam String finYear,
			@RequestParam String branch, @RequestParam String branchCode) {

		String methodName = "getReconcileCorpBankDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		String mapp = "";

		try {
			mapp = transactionService.getReconcileCorpBankDocId(orgId, finYear, branch, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ReconcileBankDocId information retrieved successfully");
			responseObjectsMap.put("reconcileCorpBankDocId", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Failed to retrieve ReconcileCorpBankDocId information", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	
	// ReconcileCash

	@GetMapping("/getAllReconcileCashByOrgId")
	public ResponseEntity<ResponseDTO> getAllReconcileCashByOrgId(@RequestParam(required = false) Long orgId) {
		String methodName = "getAllReconcileCashByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ReconcileCashVO> reconcileCashVO = new ArrayList<>();
		try {
			reconcileCashVO = transactionService.getAllReconcileCashByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ReconcileCash information get successfully By OrgId");
			responseObjectsMap.put("reconcileCashVO", reconcileCashVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ReconcileCash information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getAllReconcileCashById")
	public ResponseEntity<ResponseDTO> getAllReconcileCashById(@RequestParam(required = false) Long id) {
		String methodName = "getAllReconcileCashById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ReconcileCashVO> reconcileCashVO = new ArrayList<>();
		try {
			reconcileCashVO = transactionService.getAllReconcileCashById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ReconcileCash information get successfully By id");
			responseObjectsMap.put("reconcileCashVO", reconcileCashVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ReconcileCash information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/updateCreateReconcileCash")
	public ResponseEntity<ResponseDTO> updateCreateReconcileCash(
			@Valid @RequestBody ReconcileCashDTO reconcileCashDTO) {
		String methodName = "updateCreateReconcileCash()";

		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;

		try {
			Map<String, Object> reconcileCashVO = transactionService.updateCreateReconcileCash(reconcileCashDTO);
			boolean isUpdate = reconcileCashDTO.getId() != null;

			if (reconcileCashVO != null) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
						isUpdate ? "ReconcileCash updated successfully" : "ReconcileCash created successfully");
				responseObjectsMap.put("reconcileCashVO", reconcileCashVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				errorMsg = isUpdate ? "ReconcileCash not found for ID: " + reconcileCashDTO.getId()
						: "ReconcileCash creation failed";
				responseDTO = createServiceResponseError(responseObjectsMap,
						isUpdate ? "ReconcileCash update failed" : "ReconcileCash creation failed", errorMsg);
			}
		} catch (Exception e) {
			errorMsg = e.getMessage();
			boolean isUpdate = reconcileCashDTO.getId() != null;
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap,
					isUpdate ? "ReconcileCash update failed" : "ReconcileCash creation failed", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}


	@GetMapping("/getReconcileCashDocId")
	public ResponseEntity<ResponseDTO> getReconcileCashDocId(@RequestParam Long orgId, @RequestParam String finYear,
			@RequestParam String branch, @RequestParam String branchCode) {
		String methodName = "getReconcileCashDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		String mapp = "";

		try {
			mapp = transactionService.getReconcileCashDocId(orgId, finYear, branch, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ReconcileCashDocId information retrieved successfully");
			responseObjectsMap.put("reconcileCashDocId", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Failed to retrieve ReconcileCashDocId information", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	

//	AdjustmentJournal
	@GetMapping("/getAllAdjustmentJournalByOrgId")
	public ResponseEntity<ResponseDTO> getAllAdjustmentJournalByOrgId(@RequestParam Long orgId) {
		String methodName = "getAllAdjustmentJournalByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<AdjustmentJournalVO> adjustmentJournalVO = new ArrayList<>();
		try {
			adjustmentJournalVO = transactionService.getAllAdjustmentJournalByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"AdjustmentJournal information get successfully ByOrgId");
			responseObjectsMap.put("adjustmentJournalVO", adjustmentJournalVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"AdjustmentJournal information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getAdjustmentJournalById")
	public ResponseEntity<ResponseDTO> getAdjustmentJournalById(@RequestParam Long id) {
		String methodName = "getAdjustmentJournalById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<AdjustmentJournalVO> adjustmentJournalVO = new ArrayList<>();
		try {
			adjustmentJournalVO = transactionService.getAdjustmentJournalById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"AdjustmentJournal information get successfully By id");
			responseObjectsMap.put("adjustmentJournalVO", adjustmentJournalVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"AdjustmentJournal information receive failedByOrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/updateCreateAdjustmentJournal")
	public ResponseEntity<ResponseDTO> updateCreateAdjustmentJournal(
			@Valid @RequestBody AdjustmentJournalDTO adjustmentJournalDTO) {
		String methodName = "updateCreateAdjustmentJournal()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> adjustmentJournalVO = transactionService
					.updateCreateAdjustmentJournal(adjustmentJournalDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, adjustmentJournalVO.get("message"));
			responseObjectsMap.put("adjustmentJournalVO", adjustmentJournalVO.get("adjustmentJournalVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getAdjustmentJournalDocId")
	public ResponseEntity<ResponseDTO> getAdjustmentJournalDocId(@RequestParam Long orgId, @RequestParam String finYear,
			@RequestParam String branch, @RequestParam String branchCode) {

		String methodName = "getAdjustmentJournalDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		String mapp = "";

		try {
			mapp = transactionService.getAdjustmentJournalDocId(orgId, finYear, branch, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"AdjustmentJournal DocId information retrieved successfully");
			responseObjectsMap.put("adjustmentJournalDocId", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Failed to retrieve AdjustmentJournal Docid information", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	// TMS-TT-JobCard

	@GetMapping("/getAllTmsJobCardByOrgId")
	public ResponseEntity<ResponseDTO> getAllJobCardByOrgId(@RequestParam(required = false) Long orgId) {
		String methodName = "getAllTmsJobCardByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<JobCardVO> jobCardVO = new ArrayList<>();
		try {
			jobCardVO = transactionService.getAllJobCardByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "TmsJobCard information get successfully By OrgId");
			responseObjectsMap.put("jobCardVO", jobCardVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"TmsJobCard information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}

	@GetMapping("/getAllTmsJobCardById")
	public ResponseEntity<ResponseDTO> getAllJobCardById(@RequestParam(required = false) Long id) {
		String methodName = "getAllTmsJobCardById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<JobCardVO> jobCardVO = new ArrayList<>();
		try {
			jobCardVO = transactionService.getAllJobCardById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "TmsJobCard information get successfully By id");
			responseObjectsMap.put("jobCardVO", jobCardVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"TmsJobCard information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/updateCreateTmsJobCard")
	public ResponseEntity<ResponseDTO> updateCreateJobCard(@RequestBody TmsJobCardDTO tmsJobCardDTO) {
		String methodName = "updateCreateTmsJobCard()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> tmsJobCardVO = transactionService.updateCreateJobCard(tmsJobCardDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, tmsJobCardVO.get("message"));
			responseObjectsMap.put("jobCardVO", tmsJobCardVO.get("tmsJobCardVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	

	@GetMapping("/getSalesPersonFromPartyMaster")
	public ResponseEntity<ResponseDTO> getSalesPersonFromPartyMaster(@RequestParam Long orgId, @RequestParam String partyName) {
		String methodName = "getSalesPersonFromPartyMaster()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String,Object>> sales = new ArrayList<>();

		try {
			sales = transactionService.getSalesPersonFromPartyMaster(orgId,partyName);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "SalesPerson retrieved successfully");
			responseObjectsMap.put("salesperson", sales);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Failed to retrieve SalesPerson", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getAllCustomersFromPartyMaster")
	public ResponseEntity<ResponseDTO> getAllCustomersFromPartyMaster(@RequestParam Long orgId) {
		String methodName = "getAllCustomersFromPartyMaster()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> customer = new ArrayList<>();
		try {
			customer = transactionService.getAllCustomersFromPartyMaster(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "CustomerName information get successfully");
			responseObjectsMap.put("customer", customer);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Failed to CustomerName information", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getTmsJobCardDocId")
	public ResponseEntity<ResponseDTO> getJobCardDocId(@RequestParam Long orgId, @RequestParam String finYear,
			@RequestParam String branch, @RequestParam String branchCode) {

		String methodName = "getTmsJobCardDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		String mapp = "";

		try {
			mapp = transactionService.getJobCardDocId(orgId, finYear, branch, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "TmsJobCardDocId information retrieved successfully");
			responseObjectsMap.put("tmsJobCardDocId", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Failed to retrieve TmsJobCardDocId information", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	//BANKINGDEPOSIT
	
			@GetMapping("/getAllBankingDepositByOrgId")
			public ResponseEntity<ResponseDTO> getAllBankingDepositByOrgId(@RequestParam Long orgId) {
				String methodName = "getAllBankingDepositByOrgId()";
				LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
				String errorMsg = null;
				Map<String, Object> responseObjectsMap = new HashMap<>();
				ResponseDTO responseDTO = null;
				List<BankingDepositVO> bankingDepositVO = new ArrayList<>();
				try {
					bankingDepositVO = transactionService.getAllBankingDepositByOrgId(orgId);
				} catch (Exception e) {
					errorMsg = e.getMessage();
					LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
				}
				if (StringUtils.isBlank(errorMsg)) {
					responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
							"BankingDeposit information get successfully ByOrgId");
					responseObjectsMap.put("bankingDepositVO", bankingDepositVO);
					responseDTO = createServiceResponse(responseObjectsMap);
				} else {
					responseDTO = createServiceResponseError(responseObjectsMap,
							"BankingDeposit information receive failed By OrgId", errorMsg);
				}
				LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
				return ResponseEntity.ok().body(responseDTO);

			}
			
			
			@GetMapping("/getBankingDepositById")
			public ResponseEntity<ResponseDTO> getBankingDepositById(@RequestParam Long id) {
				String methodName = "getBankingDepositById()";
				LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
				String errorMsg = null;
				Map<String, Object> responseObjectsMap = new HashMap<>();
				ResponseDTO responseDTO = null;
				List<BankingDepositVO> bankingDepositVO = new ArrayList<>();
				try {
					bankingDepositVO = transactionService.getBankingDepositById(id);
				} catch (Exception e) {
					errorMsg = e.getMessage();
					LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
				}
				if (StringUtils.isBlank(errorMsg)) {
					responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
							"BankingDeposit information get successfully By Id");
					responseObjectsMap.put("bankingDepositVO", bankingDepositVO);
					responseDTO = createServiceResponse(responseObjectsMap);
				} else {
					responseDTO = createServiceResponseError(responseObjectsMap,
							"BankingDeposit information receive failed By Id", errorMsg);
				}
				LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
				return ResponseEntity.ok().body(responseDTO);

			}
			
			@PutMapping("/updateCreateBankingDeposit")
			public ResponseEntity<ResponseDTO> updateCreateBankingDeposit(
					@Valid @RequestBody BankingDepositDTO bankingDepositDTO) {
				String methodName = "updateCreateBankingDeposit()";
				LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
				String errorMsg = null;
				Map<String, Object> responseObjectsMap = new HashMap<>();
				ResponseDTO responseDTO = null;
				try {
					Map<String, Object> bankingDepositVO = transactionService.updateCreateBankingDeposit(bankingDepositDTO);
					responseObjectsMap.put(CommonConstant.STRING_MESSAGE, bankingDepositVO.get("message"));
					responseObjectsMap.put("bankingDepositVO", bankingDepositVO.get("bankingDepositVO"));
					responseDTO = createServiceResponse(responseObjectsMap);
				} catch (Exception e) {
					errorMsg = e.getMessage();
					LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
					responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
				}
				LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
				return ResponseEntity.ok().body(responseDTO);
			}
			
			@GetMapping("/getBankingDepositDocId")
			public ResponseEntity<ResponseDTO> getBankingDepositDocId(@RequestParam Long orgId, @RequestParam String finYear,
					@RequestParam String branch, @RequestParam String branchCode) {

				String methodName = "getBankingDepositDocId()";
				LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
				String errorMsg = null;
				Map<String, Object> responseObjectsMap = new HashMap<>();
				ResponseDTO responseDTO = null;
				String mapp = "";

				try {
					mapp = transactionService.getBankingDepositDocId(orgId, finYear, branch, branchCode);
				} catch (Exception e) {
					errorMsg = e.getMessage();
					LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
				}

				if (StringUtils.isBlank(errorMsg)) {
					responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
							"BankingDeposit DocId information retrieved successfully");
					responseObjectsMap.put("bankingDepositDocId", mapp);
					responseDTO = createServiceResponse(responseObjectsMap);
				} else {
					responseDTO = createServiceResponseError(responseObjectsMap,
							"Failed to retrieve BankingDeposit Docid information", errorMsg);
				}

				LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
				return ResponseEntity.ok().body(responseDTO);
			}

			@GetMapping("/getBankNameFromGroupforBankingDeposit")
			public ResponseEntity<ResponseDTO> getBankNameFromGroupforBankingDeposit(
					@RequestParam Long orgId) {

				String methodName = "getBankNameFromGroupforBankingDeposit()";
				LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
				String errorMsg = null;
				Map<String, Object> responseObjectsMap = new HashMap<>();
				ResponseDTO responseDTO = null;
				List<Map<String, Object>> mov = new ArrayList<>();
				try {
					mov = transactionService.getBankNameFromGroupforBankingDeposit(orgId);
				} catch (Exception e) {
					errorMsg = e.getMessage();
					LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
				}

				if (StringUtils.isBlank(errorMsg)) {
					responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
							" BankName from Group for BankingDeposit information retrieved successfully");
					responseObjectsMap.put("BankingDeposit", mov);
					responseDTO = createServiceResponse(responseObjectsMap);
				} else {
					responseDTO = createServiceResponseError(responseObjectsMap,
							"Failed to retrieve BankName from Group for BankingDeposit information", errorMsg);
				}

				LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
				return ResponseEntity.ok().body(responseDTO);
			}
	
	//BANKINGWITHDRAWAL
	
	@GetMapping("/getAllBankingWithdrawalByOrgId")
	public ResponseEntity<ResponseDTO> getAllBankingWithdrawalByOrgId(@RequestParam Long orgId) {
		String methodName = "getAllBankingWithdrawalByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<BankingWithdrawalVO> bankingWithdrawalVO = new ArrayList<>();
		try {
			bankingWithdrawalVO = transactionService.getAllBankingWithdrawalByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"BankingWithdrawal information get successfully ByOrgId");
			responseObjectsMap.put("bankingWithdrawalVO", bankingWithdrawalVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"BankingWithdrawal information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}
	
	
	@GetMapping("/getBankingWithdrawalById")
	public ResponseEntity<ResponseDTO> getBankingWithdrawalById(@RequestParam Long id) {
		String methodName = "getBankingWithdrawalById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<BankingWithdrawalVO> bankingWithdrawalVO = new ArrayList<>();
		try {
			bankingWithdrawalVO = transactionService.getBankingWithdrawalById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"BankingWithdrawal information get successfully By Id");
			responseObjectsMap.put("bankingWithdrawalVO", bankingWithdrawalVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"BankingWithdrawal information receive failed By Id", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}
	
	@PutMapping("/updateCreateBankingWithdrawal")
	public ResponseEntity<ResponseDTO> updateCreateBankingWithdrawal(
			@Valid @RequestBody BankingWithdrawalDTO bankingWithdrawalDTO) {
		String methodName = "updateCreateBankingWithdrawal()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> bankingWithdrawalVO = transactionService.updateCreateBankingWithdrawal(bankingWithdrawalDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, bankingWithdrawalVO.get("message"));
			responseObjectsMap.put("bankingWithdrawalVO", bankingWithdrawalVO.get("bankingWithdrawalVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getBankingWithdrawalDocId")
	public ResponseEntity<ResponseDTO> getBankingWithdrawalDocId(@RequestParam Long orgId, @RequestParam String finYear,
			@RequestParam String branch, @RequestParam String branchCode) {

		String methodName = "getBankingWithdrawalDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		String mapp = "";

		try {
			mapp = transactionService.getBankingWithdrawalDocId(orgId, finYear, branch, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"BankingWithdrawal DocId information retrieved successfully");
			responseObjectsMap.put("bankingWithdrawalDocId", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Failed to retrieve BankingWithdrawal Docid information", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	//ContraVoucher
	
	@GetMapping("/getAllContraVoucherByOrgId")
	public ResponseEntity<ResponseDTO> getAllContraVoucherByOrgId(@RequestParam Long orgId) {
		String methodName = "getAllContraVoucherByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ContraVoucherVO> contraVoucherVO = new ArrayList<>();
		try {
			contraVoucherVO = transactionService.getAllContraVoucherByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ContraVoucher information get successfully ByOrgId");
			responseObjectsMap.put("contraVoucherVO", contraVoucherVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ContraVoucher information receive failed By OrgId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}
	
	@GetMapping("/getContraVoucherById")
	public ResponseEntity<ResponseDTO> getContraVoucherById(@RequestParam Long id) {
		String methodName = "getContraVoucherById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ContraVoucherVO> contraVoucherVO = new ArrayList<>();
		try {
			contraVoucherVO = transactionService.getContraVoucherById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ContraVoucher information get successfully By Id");
			responseObjectsMap.put("contraVoucherVO", contraVoucherVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"ContraVoucher information receive failed By Id", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);

	}
	
	@GetMapping("/getContraVoucherDocId")
	public ResponseEntity<ResponseDTO> getContraVoucherDocId(@RequestParam Long orgId, @RequestParam String finYear,
			@RequestParam String branch, @RequestParam String branchCode) {

		String methodName = "getContraVoucherDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		String mapp = "";

		try {
			mapp = transactionService.getContraVoucherDocId(orgId, finYear, branch, branchCode);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					"ContraVoucher DocId information retrieved successfully");
			responseObjectsMap.put("contraVoucherDocId", mapp);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Failed to retrieve ContraVoucher Docid information", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@PutMapping("/updateCreateContraVoucher")
	public ResponseEntity<ResponseDTO> updateCreateContraVoucher(
			@Valid @RequestBody ContraVoucherDTO contraVoucherDTO) {
		String methodName = "updateCreateContraVoucher()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> bankingWithdrawalVO = transactionService.updateCreateContraVoucher(contraVoucherDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, bankingWithdrawalVO.get("message"));
			responseObjectsMap.put("contraVoucherVO", bankingWithdrawalVO.get("contraVoucherVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getAccountNamefromGroupLedgerforCV")
	public ResponseEntity<ResponseDTO> getAccountNamefromGroupLedgerforCV(
			@RequestParam Long OrgId	) {

		String methodName = "getAccountNamefromGroupLedgerforCV()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> mov = new ArrayList<>();
		try {
			mov = transactionService.getAccountNamefromGroupLedgerforCV(OrgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
					" AccountName from GroupLedger for Contravoucher information retrieved successfully");
			responseObjectsMap.put("ContraVoucherVO", mov);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Failed to retrieve AccountName from GroupLedger for Contravoucher information", errorMsg);
		}

		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}


}