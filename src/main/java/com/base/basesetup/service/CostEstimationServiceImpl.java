package com.base.basesetup.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.base.basesetup.dto.CostEstimationDTO;
import com.base.basesetup.dto.CostEstimationDetailsDTO;
import com.base.basesetup.entity.AccountsDetailsVO;
import com.base.basesetup.entity.AccountsVO;
import com.base.basesetup.entity.ArapDetailsVO;
import com.base.basesetup.entity.CostEstimationDetailsVO;
import com.base.basesetup.entity.CostEstimationVO;
import com.base.basesetup.entity.DocumentTypeMappingDetailsVO;
import com.base.basesetup.entity.MultipleDocIdGenerationDetailsVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.AccountsDetailsRepo;
import com.base.basesetup.repo.AccountsRepo;
import com.base.basesetup.repo.ArapDetailsRepo;
import com.base.basesetup.repo.CostEstimationDetailsRepo;
import com.base.basesetup.repo.CostEstimationRepo;
import com.base.basesetup.repo.DocumentTypeMappingDetailsRepo;
import com.base.basesetup.repo.MultipleDocIdGenerationDetailsRepo;

@Service
public class CostEstimationServiceImpl implements CostEstimationService {

	public static final Logger LOGGER = LoggerFactory.getLogger(CostEstimationServiceImpl.class);

	@Autowired
	CostEstimationRepo costEstimationRepo;

	@Autowired
	DocumentTypeMappingDetailsRepo documentTypeMappingDetailsRepo;

	@Autowired
	CostEstimationDetailsRepo costEstimationDetailsRepo;

	@Autowired
	AccountsRepo accountsRepo;

	@Autowired
	AccountsDetailsRepo accountsDetailsRepo;

	@Autowired
	MultipleDocIdGenerationDetailsRepo multipleDocIdGenerationDetailsRepo;

	@Autowired
	ArapDetailsRepo arapDetailsRepo;

	@Autowired
	AmountInWordsConverterService amountInWordsConverterService;

	// CostEstimation

	@Override
	public List<CostEstimationVO> getAllCostEstimationByOrgId(Long orgId,String finYear, String branchCode) {

		return costEstimationRepo.getAllCostEstimationByOrgId(orgId, finYear,  branchCode);
	}

	@Override
	public CostEstimationVO getAllCostEstimationById(Long id) {

		return costEstimationRepo.getAllCostEstimationById(id);
	}

	@Override
	public Map<String, Object> updateCreateCostEstimation(@Valid CostEstimationDTO costEstimationDTO)
			throws ApplicationException {
		String screenCode = "CE";
		CostEstimationVO costEstimationVO = new CostEstimationVO();
		String message;
		if (ObjectUtils.isNotEmpty(costEstimationDTO.getId())) {
			costEstimationVO = costEstimationRepo.findById(costEstimationDTO.getId())
					.orElseThrow(() -> new ApplicationException("CostEstimation Not Found!"));
			costEstimationVO.setUpdatedBy(costEstimationDTO.getCreatedBy());
			createUpdateCostEstimationVOByCostEstimationDTO(costEstimationDTO, costEstimationVO);
			message = "CostEstimation Updated Successfully";
		} else {
			// GETDOCID API
			createUpdateCostEstimationVOByCostEstimationDTO(costEstimationDTO, costEstimationVO);
			String docId = costEstimationRepo.getCostEstimationDocId(costEstimationDTO.getOrgId(),
					costEstimationDTO.getFinYear(), costEstimationDTO.getBranchCode(), screenCode);
			costEstimationVO.setDocId(docId);

			// GETDOCID LASTNO +1
			DocumentTypeMappingDetailsVO documentTypeMappingDetailsVO = documentTypeMappingDetailsRepo
					.findByOrgIdAndFinYearAndBranchCodeAndScreenCode(costEstimationDTO.getOrgId(),
							costEstimationDTO.getFinYear(), costEstimationDTO.getBranchCode(), screenCode);
			documentTypeMappingDetailsVO.setLastno(documentTypeMappingDetailsVO.getLastno() + 1);
			documentTypeMappingDetailsRepo.save(documentTypeMappingDetailsVO);

			costEstimationVO.setUpdatedBy(costEstimationDTO.getCreatedBy());
			costEstimationVO.setCreatedBy(costEstimationDTO.getCreatedBy());
//			createUpdateCostEstimationVOByCostEstimationDTO(costEstimationDTO, costEstimationVO);
			message = "CostEstimation Created Successfully";
		}

		costEstimationRepo.save(costEstimationVO);
		Map<String, Object> response = new HashMap<>();
		response.put("costEstimationVO", costEstimationVO);
		response.put("message", message);
		return response;
	}

	private void createUpdateCostEstimationVOByCostEstimationDTO(@Valid CostEstimationDTO costEstimationDTO,
			CostEstimationVO costEstimationVO) throws ApplicationException {
		costEstimationVO.setBranch(costEstimationDTO.getBranch());
		costEstimationVO.setBranchCode(costEstimationDTO.getBranchCode());
		costEstimationVO.setEmployeeName(costEstimationDTO.getEmployeeName());
		costEstimationVO.setEmployeeCode(costEstimationDTO.getEmployeeCode());
		costEstimationVO.setCreatedBy(costEstimationDTO.getCreatedBy());
		costEstimationVO.setActive(costEstimationDTO.isActive());
		costEstimationVO.setFinYear(costEstimationDTO.getFinYear());
		costEstimationVO.setBranchCode(costEstimationDTO.getBranchCode());
		costEstimationVO.setOrgId(costEstimationDTO.getOrgId());
		costEstimationVO.setStatus(costEstimationDTO.getStatus());
		costEstimationVO.setDepartment(costEstimationDTO.getDepartment());
		costEstimationVO.setToDate(costEstimationDTO.getToDate());
		costEstimationVO.setFromDate(costEstimationDTO.getFromDate());
		costEstimationVO.setApprovalRemarks(costEstimationDTO.getApprovalRemarks());

		if (ObjectUtils.isNotEmpty(costEstimationVO.getId())) {
			List<CostEstimationDetailsVO> costEstimationDetailsVO1 = costEstimationDetailsRepo
					.findByCostEstimationVO(costEstimationVO);
			costEstimationDetailsRepo.deleteAll(costEstimationDetailsVO1);
		}

		BigDecimal totalAmount = BigDecimal.ZERO;

		List<CostEstimationDetailsVO> costEstimationDetailsVOs = new ArrayList<>();
		for (CostEstimationDetailsDTO costEstimationDetailsDTO : costEstimationDTO.getCostEstimationDetailsDTO()) {
			CostEstimationDetailsVO costEstimationDetailsVO = new CostEstimationDetailsVO();

			costEstimationDetailsVO.setCategory(costEstimationDetailsDTO.getCategory());
			costEstimationDetailsVO.setParticulars(costEstimationDetailsDTO.getParticulars());
			costEstimationDetailsVO.setRemarks(costEstimationDetailsDTO.getRemarks());
			costEstimationDetailsVO.setAmount(costEstimationDetailsDTO.getAmount());

			totalAmount = totalAmount.add(costEstimationDetailsVO.getAmount());
			costEstimationDetailsVO.setCostEstimationVO(costEstimationVO);

			costEstimationDetailsVOs.add(costEstimationDetailsVO);

		}

		costEstimationVO.setTotalAmount(totalAmount);
		costEstimationVO.setAmountInWords(amountInWordsConverterService.convert(costEstimationVO.getTotalAmount()));
		costEstimationVO.setCostEstimationDetailsVO(costEstimationDetailsVOs);

	}

	@Override
	public List<Map<String, Object>> getAllEmployees(Long orgId) {
		Set<Object[]> customerName = costEstimationRepo.getAllEmployees(orgId);
		return getAllEmployees(customerName);
	}

	private List<Map<String, Object>> getAllEmployees(Set<Object[]> customer) {
		List<Map<String, Object>> doctypeMappingDetails = new ArrayList<>();
		for (Object[] sup : customer) {
			Map<String, Object> doctype = new HashMap<>();
			doctype.put("employeeName", sup[0] != null ? sup[0].toString() : "");
			doctype.put("employeeCode", sup[1] != null ? sup[1].toString() : "");
			doctypeMappingDetails.add(doctype);
		}

		return doctypeMappingDetails;
	}

	@Override
	public String getCostEstimationDocId(Long orgId, String finYear, String branch, String branchCode) {
		String ScreenCode = "CE";
		return costEstimationRepo.getCostEstimationDocId(orgId, finYear, branchCode, ScreenCode);

	}

	@Override
	public CostEstimationVO approveCostEstimation(Long orgId, Long id, String docId, String action, String actionBy)
			throws ApplicationException {

		CostEstimationVO costEstimationVO = costEstimationRepo.findByOrgIdAndIdAndDocId(orgId, id, docId);
		String screenCode = "AC";
		String sourceScreenCode = costEstimationVO.getScreenCode();

		if ("Approved".equalsIgnoreCase(costEstimationVO.getApproveStatus())) {
			throw new ApplicationException("This Invoice is already approved.");
		} else if ("Rejected".equalsIgnoreCase(costEstimationVO.getApproveStatus())) {
			throw new ApplicationException("This Invoice is already rejected.");
		}

		AccountsVO accountsVO = accountsRepo.findByRefNoAndRefDate(costEstimationVO.getDocId(),
				costEstimationVO.getDocDate());

		if (accountsVO != null) {
			List<AccountsDetailsVO> oldDetails = accountsDetailsRepo.findByAccountsVO(accountsVO);
			if (!oldDetails.isEmpty()) {
				accountsDetailsRepo.deleteAll(oldDetails);
			}
		} else {

			accountsVO = new AccountsVO();
			String accountsDocId = accountsRepo.getCostEstimationDocId(costEstimationVO.getOrgId(),
					costEstimationVO.getFinYear(), costEstimationVO.getBranchCode(), sourceScreenCode, screenCode);

			MultipleDocIdGenerationDetailsVO mulDocId = multipleDocIdGenerationDetailsRepo
					.findByOrgIdAndFinYearAndBranchCodeAndSourceScreenCodeAndScreenCode(costEstimationVO.getOrgId(),
							costEstimationVO.getFinYear(), costEstimationVO.getBranchCode(), sourceScreenCode,
							screenCode);

			mulDocId.setLastno(mulDocId.getLastno() + 1);
			multipleDocIdGenerationDetailsRepo.save(mulDocId);
			accountsVO.setDocId(accountsDocId);
		}

		// Populate AccountsVO
		accountsVO.setSourceId(costEstimationVO.getId());
		accountsVO.setCreatedBy(costEstimationVO.getCreatedBy());
		accountsVO.setModifiedBy(costEstimationVO.getUpdatedBy());
		accountsVO.setCreatedon(costEstimationVO.getCommonDate().getModifiedon().toUpperCase());
		accountsVO.setModifiedon(costEstimationVO.getCommonDate().getModifiedon().toUpperCase());
		accountsVO.setFinYear(costEstimationVO.getFinYear());
		accountsVO.setBranch(costEstimationVO.getBranch());
		accountsVO.setBranchCode(costEstimationVO.getBranchCode());
		accountsVO.setOrgId(costEstimationVO.getOrgId());
		accountsVO.setRefNo(costEstimationVO.getDocId());
		accountsVO.setRefDate(costEstimationVO.getDocDate());
//	    accountsVO.setVId(costInvoiceVO.getVId());
//	    accountsVO.setVDate(costInvoiceVO.getVDate());
//	    accountsVO.setDueDate(costInvoiceVO.getDueDate());
		accountsVO.setAmountInWords(costEstimationVO.getAmountInWords());
//	    accountsVO.setChargeableAmount(costInvoiceVO.getTotChargesLcAmt());
//	    accountsVO.setSupplierRefNo(costInvoiceVO.getSupplierBillNo());
//	    accountsVO.setCreditDays(costInvoiceVO.getCreditDays());
		accountsVO.setSourceScreen(costEstimationVO.getScreenName());
		accountsVO.setSourceScreenCode(costEstimationVO.getScreenCode());
//	    accountsVO.setRemarks(costEstimationVO.getRemarks());
		accountsVO.setTotalDebitAmount(costEstimationVO.getTotalAmount());
		accountsVO.setTotalCreditAmount(costEstimationVO.getTotalAmount());

		List<AccountsDetailsVO> accountsDetailsVOs = new ArrayList<>();

		// PAYABLE A/C Entry
		AccountsDetailsVO payableDetail = new AccountsDetailsVO();
		payableDetail.setAccountName("PAYABLE A/C");
		payableDetail.setACategory("PAYABLE A/C");
		payableDetail.setDebitAmount(BigDecimal.ZERO);
		payableDetail.setCreditAmount(costEstimationVO.getTotalAmount());
		payableDetail.setNDebitAmount(BigDecimal.ZERO);
		payableDetail.setNCreditAmount(costEstimationVO.getTotalAmount());
		payableDetail.setBDebitAmount(BigDecimal.ZERO);
		payableDetail.setBCrAmount(costEstimationVO.getTotalAmount());
		payableDetail.setArapAmount(costEstimationVO.getTotalAmount());
		payableDetail.setBArapAmount(costEstimationVO.getTotalAmount());
		payableDetail.setNArapAmount(costEstimationVO.getTotalAmount());
		payableDetail.setArapFlag(true);
		payableDetail.setSubledgerName(costEstimationVO.getEmployeeName());
		payableDetail.setSubLedgerCode(costEstimationVO.getEmployeeCode());
		payableDetail.setGstflag(6);
		payableDetail.setAccountsVO(accountsVO);
		accountsDetailsVOs.add(payableDetail);

		Map<String, BigDecimal> ledgerSumMap = new HashMap<>();
		for (CostEstimationDetailsVO charge : costEstimationVO.getCostEstimationDetailsVO()) {
			ledgerSumMap.merge(charge.getCategory(), charge.getAmount(), BigDecimal::add);
		}

		for (Map.Entry<String, BigDecimal> entry : ledgerSumMap.entrySet()) {
			AccountsDetailsVO gstDetail = new AccountsDetailsVO();
			gstDetail.setAccountName(entry.getKey());
			gstDetail.setACategory(entry.getKey());
			gstDetail.setDebitAmount(entry.getValue());
			gstDetail.setNDebitAmount(entry.getValue());
			gstDetail.setCreditAmount(BigDecimal.ZERO);
			gstDetail.setNCreditAmount(BigDecimal.ZERO);
			gstDetail.setBDebitAmount(entry.getValue());
			gstDetail.setBCrAmount(BigDecimal.ZERO);
			gstDetail.setArapAmount(BigDecimal.ZERO);
			gstDetail.setBArapAmount(BigDecimal.ZERO);
			gstDetail.setNArapAmount(BigDecimal.ZERO);
			gstDetail.setArapFlag(false);
			gstDetail.setSubledgerName("None");
			gstDetail.setSubLedgerCode("None");
//	        gstDetail.setACurrency(costInvoiceVO.getCurrency());
//	        gstDetail.setAExRate(costInvoiceVO.getExRate());
			gstDetail.setGstflag(3);
			gstDetail.setAccountsVO(accountsVO);
			accountsDetailsVOs.add(gstDetail);
		}

		accountsVO.setAccountsDetailsVO(accountsDetailsVOs);
		AccountsVO savedAccountsVO = accountsRepo.save(accountsVO);

		List<ArapDetailsVO> existingAraps = arapDetailsRepo.findByRefNo(savedAccountsVO.getRefNo());
		if (!existingAraps.isEmpty()) {
			arapDetailsRepo.deleteAll(existingAraps);
		}

		AccountsDetailsVO payableEntry = accountsDetailsRepo.findByAccountsVOAndGstflag(savedAccountsVO, 6);
		ArapDetailsVO arapDetailsVO = new ArapDetailsVO();
		arapDetailsVO.setSourceTransid(payableEntry.getId());
		arapDetailsVO.setCreatedBy(savedAccountsVO.getCreatedBy());
		arapDetailsVO.setUpdatedBy(savedAccountsVO.getModifiedBy());
		arapDetailsVO.setBranch(savedAccountsVO.getBranch());
		arapDetailsVO.setBranchCode(savedAccountsVO.getBranchCode());
		arapDetailsVO.setFinYear(savedAccountsVO.getFinYear());
		arapDetailsVO.setRefNo(savedAccountsVO.getRefNo());
		arapDetailsVO.setRefDate(savedAccountsVO.getRefDate());
		arapDetailsVO.setSubLedgerCode(payableEntry.getSubLedgerCode());
		arapDetailsVO.setCurrency(payableEntry.getACurrency());
		arapDetailsVO.setExRate(payableEntry.getAExRate());
		arapDetailsVO.setAmount(payableEntry.getArapAmount());
		arapDetailsVO.setBaseAmt(payableEntry.getArapAmount());
		arapDetailsVO.setDueDate(savedAccountsVO.getDueDate());
		arapDetailsVO.setCreditDays(savedAccountsVO.getCreditDays());
		arapDetailsVO.setDocId(savedAccountsVO.getDocId());
		arapDetailsVO.setDocDate(savedAccountsVO.getDocDate());
		arapDetailsVO.setAccCurrency(savedAccountsVO.getCurrency());
		arapDetailsVO.setExRate(savedAccountsVO.getExRate());
		arapDetailsVO.setAccName(payableEntry.getAccountName());
		arapDetailsVO.setActive(savedAccountsVO.isActive());
		arapDetailsVO.setGstFlag(payableEntry.getGstflag());
		arapDetailsVO.setSubLedgerName(payableEntry.getSubledgerName());
		arapDetailsVO.setSalesType(savedAccountsVO.getSalesType());
		arapDetailsVO.setNativeAmt(payableEntry.getArapAmount());
		arapDetailsVO.setOrgId(savedAccountsVO.getOrgId());
		arapDetailsRepo.save(arapDetailsVO);

		// Final invoice updates
		costEstimationVO.setPurVoucherNo(savedAccountsVO.getDocId());
		costEstimationVO.setPurVoucherDate(savedAccountsVO.getDocDate());

		costEstimationVO.setApproveStatus(action);
		costEstimationVO.setApproveBy(actionBy);
		costEstimationVO.setApproveOn(
				LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a")).toUpperCase());

		return costEstimationRepo.save(costEstimationVO);
	}

	
//	@Override
//	@Transactional
//	public String uploadImageCostEstimationDetail(List<MultipartFile> files, Long costEstimationId,List<Long>detailsId ) throws IOException {
//		
//		for(MultipartFile file:files) {
//		
//	    CostEstimationVO costEstimationVO = costEstimationRepo.findById(costEstimationId)
//	        .orElseThrow(() -> new RuntimeException("CostEstimation not found"));
//
//	    List<CostEstimationDetailsVO> detail = costEstimationDetailsRepo.findBycostEstimationVO(costEstimationVO);
//
//	    if (!detail.getCostEstimationVO().getId().equals(costEstimationVO.getId())) {
//	        throw new IllegalArgumentException("Detail does not belong to the specified cost estimation.");
//	    }
//
//	    detail.setImage(file.getBytes());
//	    costEstimationDetailsRepo.save(detail);
//		}
//
//	    return "File Uploaded Sucessfully";
//	}
	
	@Override
	public String uploadImageCostEstimationDetail(List<MultipartFile> files, Long costEstimationId, List<Long> detailsId) throws IOException {

	    if (files.size() != detailsId.size()) {
	        throw new IllegalArgumentException("Mismatch between number of files and detail IDs.");
	    }

	    CostEstimationVO costEstimationVO = costEstimationRepo.findById(costEstimationId)
	            .orElseThrow(() -> new RuntimeException("CostEstimation not found"));

	    for (int i = 0; i < files.size(); i++) {
	        MultipartFile file = files.get(i);
	        Long detailId = detailsId.get(i);

	        CostEstimationDetailsVO detail = costEstimationDetailsRepo.findById(detailId)
	                .orElseThrow(() -> new RuntimeException("CostEstimationDetail not found with ID: " + detailId));

	        if (!detail.getCostEstimationVO().getId().equals(costEstimationVO.getId())) {
	            throw new IllegalArgumentException("Detail with ID " + detailId + " does not belong to the specified cost estimation.");
	        }

	        detail.setImage(file.getBytes());
	        costEstimationDetailsRepo.save(detail);
	    }

	    return "Files uploaded successfully";
	}


	
	@Override
	public List<Map<String, Object>> getCostEstimationDetails(Long orgId, String finYear, String employeeName, String fromDate,
			String toDate, String branchCode) {
		Set<Object[]> chType = costEstimationRepo.getCostEstimationDetails(orgId, finYear, employeeName, fromDate, toDate,
				branchCode);
		return getCostEstimationDetails(chType);
	}

	private List<Map<String, Object>> getCostEstimationDetails(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();
			map.put("finyear", ch[0] != null ? ch[0].toString() : ""); 
			map.put("docId", ch[1] != null ? ch[1].toString() : ""); 
			map.put("docdate", ch[2] != null ? ch[2].toString() : ""); 
			map.put("employeeName", ch[3] != null ? ch[3].toString() : ""); 
			map.put("employeeCode", ch[4] != null ? ch[4].toString() : ""); // 4
			map.put("fromDate", ch[5] != null ? ch[5].toString() : ""); // 5
			map.put("toDate", ch[6] != null ? ch[6].toString() : ""); // 6
			map.put("approvalremarks", ch[7] != null ? ch[7].toString() : ""); // 7
			map.put("totalAmount", ch[8] != null ? new BigDecimal(ch[8].toString()) : BigDecimal.ZERO); // 8
			map.put("particulars", ch[9] != null ? ch[9].toString() : ""); // 9
			map.put("category", ch[10] != null ?  ch[10].toString() : ""); // 10
//			map.put("category", ch[11] != null ?  ch[11].toString() : ""); // 11
			map.put("remarks", ch[11] != null ?  ch[11].toString() : ""); // 12
			map.put("amount", ch[12] != null ? new BigDecimal(ch[12].toString()) : BigDecimal.ZERO); // 13
			 if (ch[13] != null && ch[13] instanceof byte[]) {
		            byte[] imageBytes = (byte[]) ch[13];
		            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
		            map.put("image", "data:image/jpeg;base64," + base64Image); 
		        } else {
		            map.put("image", "");
		        }
			map.put("approvestatus", ch[14] != null ? ch[14].toString() : ""); 		
			List1.add(map);
		}
		return List1;
	}

	@Override
	public List<Map<String, Object>> getCostEstimationSummary(Long orgId, String finYear, String employeeName, String fromDate,
			String toDate, String branchCode) {
		Set<Object[]> chType = costEstimationRepo.getCostEstimationSummary(orgId, finYear, employeeName, fromDate, toDate,
				branchCode);
		return getCostEstimationSummary(chType);
	}

	private List<Map<String, Object>> getCostEstimationSummary(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();
			map.put("finyear", ch[0] != null ? ch[0].toString() : "");
			map.put("docId", ch[1] != null ? ch[1].toString() : "");
			map.put("docDate", ch[2] != null ? ch[2].toString() : "");
			map.put("employeeName", ch[3] != null ? ch[3].toString() : "");
			map.put("employeeCode", ch[4] != null ? ch[4].toString() : "");
			map.put("fromDate", ch[5] != null ? ch[5].toString() : ""); // 5
			map.put("toDate", ch[6] != null ? ch[6].toString() : ""); // 6
			map.put("approvalremarks", ch[7] != null ? ch[7].toString() : ""); // 7
			map.put("totalAmount", ch[8] != null ? new BigDecimal(ch[8].toString()) : BigDecimal.ZERO); // 8
			map.put("approvestatus", ch[9] != null ? ch[9].toString() : "");
//			map.put("totalchargeamountlc", ch[10] != null ? new BigDecimal(ch[10].toString()) : BigDecimal.ZERO);
//			map.put("totalinvamountlc", ch[11] != null ? new BigDecimal(ch[11].toString()) : BigDecimal.ZERO);
//			map.put("totaltaxamountlc", ch[12] != null ? new BigDecimal(ch[12].toString()) : BigDecimal.ZERO);
//			map.put("approvestatus", ch[13] != null ? ch[13].toString() : "");

			List1.add(map);
		}
		return List1;
	}

//	@Override
//	public CostEstimationVO uploadMultipleImagesToCostEstimationDetails(MultipartFile[] files, Long costEstimationId,
//	        List<Long> costEstmationDetailsId) {
//
//	    CostEstimationVO costEstimationVO = costEstimationRepo.findById(costEstimationId)
//	            .orElseThrow(() -> new RuntimeException("CostEstimation not found with ID: " + costEstimationId));
//
//	    if (files.length != costEstmationDetailsId.size()) {
//	        throw new IllegalArgumentException("Each file must have a corresponding detail ID.");
//	    }
//
//	    for (int i = 0; i < files.length; i++) {
//	        MultipartFile file = files[i];
//	        Long detailId = costEstmationDetailsId.get(i);
//
//	        CostEstimationDetailsVO detail = costEstimationDetailsRepo.findById(detailId)
//	                .orElseThrow(() -> new RuntimeException("CostEstimationDetail not found with ID: " + detailId));
//
//	        try {
//	            detail.setImage(file.getBytes());
//	        } catch (IOException e) {
//	            throw new RuntimeException("Failed to read image file for detail ID: " + detailId, e);
//	        }
//
//	        detail.setCostEstimationVO(costEstimationVO);
//	        costEstimationDetailsRepo.save(detail);
//	    }
//
//	    return costEstimationVO;
//	}
//

}
