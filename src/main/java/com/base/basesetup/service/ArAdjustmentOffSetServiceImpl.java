package com.base.basesetup.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.basesetup.dto.ApAdjustmentOffSetDTO;
import com.base.basesetup.dto.ApOffSetInvoiceDetailsDTO;
import com.base.basesetup.dto.ArAdjustmentOffSetDTO;
import com.base.basesetup.dto.ArOffSetInvoiceDetailsDTO;
import com.base.basesetup.entity.ApAdjustmentOffSetVO;
import com.base.basesetup.entity.ApOffSetInvoiceDetailsVO;
import com.base.basesetup.entity.ArAdjustmentOffSetVO;
import com.base.basesetup.entity.ArOffSetInvoiceDetailsVO;
import com.base.basesetup.entity.DocumentTypeMappingDetailsVO;
import com.base.basesetup.entity.PaymentVO;
import com.base.basesetup.entity.ReceiptVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.ApAdjustmentOffSetRepo;
import com.base.basesetup.repo.ApOffSetInvoiceDetailsRepo;
import com.base.basesetup.repo.ArAdjustmentOffSetRepo;
import com.base.basesetup.repo.ArOffSetInvoiceDetailsRepo;
import com.base.basesetup.repo.DocumentTypeMappingDetailsRepo;
import com.base.basesetup.repo.PaymentRepo;
import com.base.basesetup.repo.ReceiptRepo;

@Service
public class ArAdjustmentOffSetServiceImpl implements ArAdjustmentOffSetService {

	public static final Logger LOGGER = LoggerFactory.getLogger(ArAdjustmentOffSetServiceImpl.class);

	@Autowired
	ArAdjustmentOffSetRepo arAdjustmentOffSetRepo;
	
	@Autowired
	ArOffSetInvoiceDetailsRepo arOffSetInvoiceDetailsRepo;
	
	@Autowired
	ApAdjustmentOffSetRepo apAdjustmentOffSetRepo;
	
	@Autowired
	ReceiptRepo receiptRepo;

	@Autowired
	PaymentRepo paymentRepo;
	
	@Autowired
	DocumentTypeMappingDetailsRepo documentTypeMappingDetailsRepo;
	
	@Autowired
	ApOffSetInvoiceDetailsRepo apOffSetInvoiceDetailsRepo;

	@Override
	public List<ArAdjustmentOffSetVO> getAllArAdjustmentOffSetByOrgId(Long orgId) {
		List<ArAdjustmentOffSetVO> arAdjustmentOffSetVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(orgId)) {
			LOGGER.info("Successfully Received  ArAdjustmentOffSet BY OrgId: {}", orgId);
			arAdjustmentOffSetVO = arAdjustmentOffSetRepo.getAllArAdjustmentOffSetByOrgId(orgId);
		}
		return arAdjustmentOffSetVO;
	}

	@Override
	public List<ArAdjustmentOffSetVO> getArAdjustmentOffSetById(Long id) {
		List<ArAdjustmentOffSetVO> arAdjustmentOffSetVO = new ArrayList<>();

		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received ArAdjustmentOffSet BY Id : {}", id);
			arAdjustmentOffSetVO = arAdjustmentOffSetRepo.getArAdjustmentOffSetById(id);
		}
		return arAdjustmentOffSetVO;
	}

	@Override
	public Map<String, Object> updateCreateArAdjustmentOffSet(@Valid ArAdjustmentOffSetDTO arAdjustmentOffSetDTO)
			throws ApplicationException {
		String screenCode = "ARA";
		ArAdjustmentOffSetVO arAdjustmentOffSetVO = new ArAdjustmentOffSetVO();
		String message;
		if (ObjectUtils.isNotEmpty(arAdjustmentOffSetDTO.getId())) {
			arAdjustmentOffSetVO = arAdjustmentOffSetRepo.findById(arAdjustmentOffSetDTO.getId())
					.orElseThrow(() -> new ApplicationException("ArAdjustmentOffSet not found"));

			arAdjustmentOffSetVO.setUpdatedBy(arAdjustmentOffSetDTO.getCreatedBy());
			createUpdateArAdjustmentOffSetVOByArAdjustmentOffSetDTO(arAdjustmentOffSetDTO, arAdjustmentOffSetVO);
			message = "ArAdjustmentOffSet Updated Successfully";
		} else {
			// GETDOCID API
			String docId = arAdjustmentOffSetRepo.getArAdjustmentOffSetByDocId(arAdjustmentOffSetDTO.getOrgId(),
					arAdjustmentOffSetDTO.getFinYear(), arAdjustmentOffSetDTO.getBranchCode(), screenCode);

			arAdjustmentOffSetVO.setDocId(docId);

//						// GETDOCID LASTNO +1
			DocumentTypeMappingDetailsVO documentTypeMappingDetailsVO = documentTypeMappingDetailsRepo
					.findByOrgIdAndFinYearAndBranchCodeAndScreenCode(arAdjustmentOffSetDTO.getOrgId(),
							arAdjustmentOffSetDTO.getFinYear(), arAdjustmentOffSetDTO.getBranchCode(), screenCode);
			documentTypeMappingDetailsVO.setLastno(documentTypeMappingDetailsVO.getLastno() + 1);
			documentTypeMappingDetailsRepo.save(documentTypeMappingDetailsVO);

			arAdjustmentOffSetVO.setCreatedBy(arAdjustmentOffSetDTO.getCreatedBy());
			arAdjustmentOffSetVO.setUpdatedBy(arAdjustmentOffSetDTO.getCreatedBy());
			createUpdateArAdjustmentOffSetVOByArAdjustmentOffSetDTO(arAdjustmentOffSetDTO, arAdjustmentOffSetVO);
			message = "ArAdjustmentOffSet Created Successfully";
		}

		arAdjustmentOffSetRepo.save(arAdjustmentOffSetVO);
		Map<String, Object> response = new HashMap<>();
		response.put("arAdjustmentOffSetVO", arAdjustmentOffSetVO);
		response.put("message", message);
		return response;
	}

	private void createUpdateArAdjustmentOffSetVOByArAdjustmentOffSetDTO(
	        @Valid ArAdjustmentOffSetDTO arAdjustmentOffSetDTO, ArAdjustmentOffSetVO arAdjustmentOffSetVO)
	        throws ApplicationException {

	    arAdjustmentOffSetVO.setReceiptDocId(arAdjustmentOffSetDTO.getReceiptDocId());
	    arAdjustmentOffSetVO.setReceiptDocDate(arAdjustmentOffSetDTO.getReceiptDocDate());
	    arAdjustmentOffSetVO.setSubLedgerType(arAdjustmentOffSetDTO.getSubLedgerType());
	    arAdjustmentOffSetVO.setSubLedgerName(arAdjustmentOffSetDTO.getSubLedgerName());
	    arAdjustmentOffSetVO.setSubLedgerCode(arAdjustmentOffSetDTO.getSubLedgerCode());
	    arAdjustmentOffSetVO.setCurrency(arAdjustmentOffSetDTO.getCurrency());
	    arAdjustmentOffSetVO.setExRate(arAdjustmentOffSetDTO.getExRate());
	    arAdjustmentOffSetVO.setSupplierRefNo(arAdjustmentOffSetDTO.getSupplierRefNo());
	    arAdjustmentOffSetVO.setNarration(arAdjustmentOffSetDTO.getNarration());
	    arAdjustmentOffSetVO.setAmount(arAdjustmentOffSetDTO.getAmount());


	    arAdjustmentOffSetVO.setBranch(arAdjustmentOffSetDTO.getBranch());
	    arAdjustmentOffSetVO.setBranchCode(arAdjustmentOffSetDTO.getBranchCode());
	    arAdjustmentOffSetVO.setFinYear(arAdjustmentOffSetDTO.getFinYear());
	    arAdjustmentOffSetVO.setOrgId(arAdjustmentOffSetDTO.getOrgId());
	    arAdjustmentOffSetVO.setActive(arAdjustmentOffSetDTO.isActive());

	    if (ObjectUtils.isNotEmpty(arAdjustmentOffSetDTO.getId())) {
	        List<ArOffSetInvoiceDetailsVO> arOffSetInvoiceDetailsVOList = arOffSetInvoiceDetailsRepo
	                .findByArAdjustmentOffSetVO(arAdjustmentOffSetVO);
	        arOffSetInvoiceDetailsRepo.deleteAll(arOffSetInvoiceDetailsVOList);
	    }

	    List<ArOffSetInvoiceDetailsVO> arOffSetInvoiceDetailsVOs = new ArrayList<>();
	    BigDecimal totalSettledAmount = BigDecimal.ZERO;
	    BigDecimal totalForexGainOrLoss = BigDecimal.ZERO;

	    for (ArOffSetInvoiceDetailsDTO arOffSetInvoiceDetailsDTO : arAdjustmentOffSetDTO.getArOffSetInvoiceDetailsDTO()) {
	        ArOffSetInvoiceDetailsVO arOffSetInvoiceDetailsVO = new ArOffSetInvoiceDetailsVO();

	        arOffSetInvoiceDetailsVO.setInvoiceNo(arOffSetInvoiceDetailsDTO.getInvoiceNo());
	        arOffSetInvoiceDetailsVO.setInvoiceDate(arOffSetInvoiceDetailsDTO.getInvoiceDate());
	        arOffSetInvoiceDetailsVO.setRefNo(arOffSetInvoiceDetailsDTO.getRefNo());
	        arOffSetInvoiceDetailsVO.setRefDate(arOffSetInvoiceDetailsDTO.getRefDate());
	        arOffSetInvoiceDetailsVO.setCurr(arOffSetInvoiceDetailsDTO.getCurr());
	        arOffSetInvoiceDetailsVO.setExRate(arOffSetInvoiceDetailsDTO.getExRate());
	        arOffSetInvoiceDetailsVO.setInvAmount(arOffSetInvoiceDetailsDTO.getInvAmount());
	        arOffSetInvoiceDetailsVO.setOutStanding(arOffSetInvoiceDetailsDTO.getOutStanding());
	        arOffSetInvoiceDetailsVO.setSettled(arOffSetInvoiceDetailsDTO.getSettled());
	        arOffSetInvoiceDetailsVO.setSetExRate(arOffSetInvoiceDetailsDTO.getSetExRate());
	        arOffSetInvoiceDetailsVO.setTnxSettled(arOffSetInvoiceDetailsDTO.getTnxSettled());
	        arOffSetInvoiceDetailsVO.setGainOrLoss(arOffSetInvoiceDetailsDTO.getGainOrLoss());
	        arOffSetInvoiceDetailsVO.setRemarks(arOffSetInvoiceDetailsDTO.getRemarks());

	        arOffSetInvoiceDetailsVO.setArAdjustmentOffSetVO(arAdjustmentOffSetVO);
	        arOffSetInvoiceDetailsVOs.add(arOffSetInvoiceDetailsVO);

	        if (arOffSetInvoiceDetailsDTO.getSettled() != null) {
	            totalSettledAmount = totalSettledAmount.subtract(arOffSetInvoiceDetailsDTO.getSettled());
	        }
	        if (arOffSetInvoiceDetailsDTO.getGainOrLoss() != null) {
	            totalForexGainOrLoss = totalForexGainOrLoss.add(arOffSetInvoiceDetailsDTO.getGainOrLoss());
	        }
	    }

	    BigDecimal roundedTotalSettled = totalSettledAmount.setScale(2, RoundingMode.HALF_UP);
	    BigDecimal roundOffAmount = totalSettledAmount.subtract(roundedTotalSettled).setScale(2, RoundingMode.HALF_UP);

	    arAdjustmentOffSetVO.setTotalSettled(roundedTotalSettled);
	    arAdjustmentOffSetVO.setRoundOffAmount(roundOffAmount);

	    if (arAdjustmentOffSetDTO.getAmount() != null) {
	        arAdjustmentOffSetVO.setOnAccount(roundedTotalSettled.add(arAdjustmentOffSetDTO.getAmount()));
	    }

	    arAdjustmentOffSetVO.setForexGainOrLoss(totalForexGainOrLoss.setScale(2, RoundingMode.HALF_UP));
	    
	    arAdjustmentOffSetVO.setArOffSetInvoiceDetailsVO(arOffSetInvoiceDetailsVOs);
	}

	@Override
	public String getArAdjustmentOffSetDocId(Long orgId, String finYear, String branch, String branchCode) {
		String ScreenCode = "ARA";
		String result = arAdjustmentOffSetRepo.getArAdjustmentOffSetDocId(orgId, finYear, branchCode, ScreenCode);	
		return result;
	}
	
	
	@Override
	public List<ReceiptVO> getAllCustomerReceiptByOrgIdAndBranchCode(Long orgId, String branchCode) {
	    List<ReceiptVO> receiptVO = new ArrayList<>();
	    if (ObjectUtils.isNotEmpty(orgId) && ObjectUtils.isNotEmpty(branchCode)) {
	        LOGGER.info("Successfully Received receipt BY OrgId: {} and BranchCode: {}", orgId, branchCode);
	        receiptVO = receiptRepo.getAllReceiptByOrgIdAndBranchCode(orgId, branchCode);
	    }
	    return receiptVO;
	}

	//AP ADJUSTMENT OFFSET
	
	@Override
	public List<ApAdjustmentOffSetVO> getAllApAdjustmentOffSetByOrgId(Long orgId) {
		List<ApAdjustmentOffSetVO> apAdjustmentOffSetVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(orgId)) {
			LOGGER.info("Successfully Received  ApAdjustmentOffSet BY OrgId: {}", orgId);
			apAdjustmentOffSetVO = apAdjustmentOffSetRepo.getAllApAdjustmentOffSetByOrgId(orgId);
		}
		return apAdjustmentOffSetVO;
	}
	
	
	@Override
	public List<ApAdjustmentOffSetVO> getApAdjustmentOffSetById(Long id) {
		List<ApAdjustmentOffSetVO> apAdjustmentOffSetVO = new ArrayList<>();

		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received ApAdjustmentOffSet BY Id : {}", id);
			apAdjustmentOffSetVO = apAdjustmentOffSetRepo.getApAdjustmentOffSetById(id);
		}
		return apAdjustmentOffSetVO;
	}
	
	
	@Override
	public String getApAdjustmentOffSetDocId(Long orgId, String finYear, String branch, String branchCode) {
		String ScreenCode = "APA";
		String result = apAdjustmentOffSetRepo.getApAdjustmentOffSetDocId(orgId, finYear, branchCode, ScreenCode);	
		return result;
	}
	
	
	@Override
	public List<PaymentVO> getAllVendorPaymentByOrgIdAndBranchCode(Long orgId, String branchCode) {
	    List<PaymentVO> paymentVO = new ArrayList<>();
	    if (ObjectUtils.isNotEmpty(orgId) && ObjectUtils.isNotEmpty(branchCode)) {
	        LOGGER.info("Successfully Received payment BY OrgId: {} and BranchCode: {}", orgId, branchCode);
	        paymentVO = paymentRepo.getAllVendorPaymentByOrgIdAndBranchCode(orgId, branchCode);
	    }
	    return paymentVO;
	}
	
	
	
	@Override
	public Map<String, Object> updateCreateApAdjustmentOffSet(@Valid ApAdjustmentOffSetDTO apAdjustmentOffSetDTO)
			throws ApplicationException {
		String screenCode = "APA";
		ApAdjustmentOffSetVO apAdjustmentOffSetVO = new ApAdjustmentOffSetVO();
		String message;
		if (ObjectUtils.isNotEmpty(apAdjustmentOffSetDTO.getId())) {
			apAdjustmentOffSetVO = apAdjustmentOffSetRepo.findById(apAdjustmentOffSetDTO.getId())
					.orElseThrow(() -> new ApplicationException("ApAdjustmentOffSet not found"));

			apAdjustmentOffSetVO.setUpdatedBy(apAdjustmentOffSetDTO.getCreatedBy());
			createUpdateApAdjustmentOffSetVOByApAdjustmentOffSetDTO(apAdjustmentOffSetDTO, apAdjustmentOffSetVO);
			message = "ApAdjustmentOffSet Updated Successfully";
		} else {
			// GETDOCID API
			String docId = apAdjustmentOffSetRepo.getApAdjustmentOffSetByDocId(apAdjustmentOffSetDTO.getOrgId(),
					apAdjustmentOffSetDTO.getFinYear(), apAdjustmentOffSetDTO.getBranchCode(), screenCode);

			apAdjustmentOffSetVO.setDocId(docId);

//						// GETDOCID LASTNO +1
			DocumentTypeMappingDetailsVO documentTypeMappingDetailsVO = documentTypeMappingDetailsRepo
					.findByOrgIdAndFinYearAndBranchCodeAndScreenCode(apAdjustmentOffSetDTO.getOrgId(),
							apAdjustmentOffSetDTO.getFinYear(), apAdjustmentOffSetDTO.getBranchCode(), screenCode);
			documentTypeMappingDetailsVO.setLastno(documentTypeMappingDetailsVO.getLastno() + 1);
			documentTypeMappingDetailsRepo.save(documentTypeMappingDetailsVO);

			apAdjustmentOffSetVO.setCreatedBy(apAdjustmentOffSetDTO.getCreatedBy());
			apAdjustmentOffSetVO.setUpdatedBy(apAdjustmentOffSetDTO.getCreatedBy());
			createUpdateApAdjustmentOffSetVOByApAdjustmentOffSetDTO(apAdjustmentOffSetDTO, apAdjustmentOffSetVO);
			message = "ApAdjustmentOffSet Created Successfully";
		}

		apAdjustmentOffSetRepo.save(apAdjustmentOffSetVO);
		Map<String, Object> response = new HashMap<>();
		response.put("apAdjustmentOffSetVO", apAdjustmentOffSetVO);
		response.put("message", message);
		return response;
	}

	private void createUpdateApAdjustmentOffSetVOByApAdjustmentOffSetDTO(
	        @Valid ApAdjustmentOffSetDTO apAdjustmentOffSetDTO, ApAdjustmentOffSetVO apAdjustmentOffSetVO)
	        throws ApplicationException {

	    apAdjustmentOffSetVO.setPaymentDocId(apAdjustmentOffSetDTO.getPaymentDocId());
	    apAdjustmentOffSetVO.setPaymentDocDate(apAdjustmentOffSetDTO.getPaymentDocDate());
	    apAdjustmentOffSetVO.setSubLedgerType(apAdjustmentOffSetDTO.getSubLedgerType());
	    apAdjustmentOffSetVO.setSubLedgerName(apAdjustmentOffSetDTO.getSubLedgerName());
	    apAdjustmentOffSetVO.setSubLedgerCode(apAdjustmentOffSetDTO.getSubLedgerCode());
	    apAdjustmentOffSetVO.setCurrency(apAdjustmentOffSetDTO.getCurrency());
	    apAdjustmentOffSetVO.setExRate(apAdjustmentOffSetDTO.getExRate());
	    apAdjustmentOffSetVO.setSupplierRefNo(apAdjustmentOffSetDTO.getSupplierRefNo());
	    apAdjustmentOffSetVO.setNarration(apAdjustmentOffSetDTO.getNarration());
	    apAdjustmentOffSetVO.setAmount(apAdjustmentOffSetDTO.getAmount());


	    apAdjustmentOffSetVO.setBranch(apAdjustmentOffSetDTO.getBranch());
	    apAdjustmentOffSetVO.setBranchCode(apAdjustmentOffSetDTO.getBranchCode());
	    apAdjustmentOffSetVO.setFinYear(apAdjustmentOffSetDTO.getFinYear());
	    apAdjustmentOffSetVO.setOrgId(apAdjustmentOffSetDTO.getOrgId());
	    apAdjustmentOffSetVO.setActive(apAdjustmentOffSetDTO.isActive());

	    if (ObjectUtils.isNotEmpty(apAdjustmentOffSetDTO.getId())) {
	        List<ApOffSetInvoiceDetailsVO> apOffSetInvoiceDetailsVOList = apOffSetInvoiceDetailsRepo
	                .findByApAdjustmentOffSetVO(apAdjustmentOffSetVO);
	        apOffSetInvoiceDetailsRepo.deleteAll(apOffSetInvoiceDetailsVOList);
	    }

	    List<ApOffSetInvoiceDetailsVO> apOffSetInvoiceDetailsVOs = new ArrayList<>();
	    BigDecimal totalSettledAmount = BigDecimal.ZERO;
	    BigDecimal totalForexGainOrLoss = BigDecimal.ZERO;

	    for (ApOffSetInvoiceDetailsDTO apOffSetInvoiceDetailsDTO : apAdjustmentOffSetDTO.getApOffSetInvoiceDetailsDTO()) {
	        ApOffSetInvoiceDetailsVO apOffSetInvoiceDetailsVO = new ApOffSetInvoiceDetailsVO();

	        apOffSetInvoiceDetailsVO.setInvoiceNo(apOffSetInvoiceDetailsDTO.getInvoiceNo());
	        apOffSetInvoiceDetailsVO.setInvoiceDate(apOffSetInvoiceDetailsDTO.getInvoiceDate());
	        apOffSetInvoiceDetailsVO.setRefNo(apOffSetInvoiceDetailsDTO.getRefNo());
	        apOffSetInvoiceDetailsVO.setRefDate(apOffSetInvoiceDetailsDTO.getRefDate());
	        apOffSetInvoiceDetailsVO.setCurr(apOffSetInvoiceDetailsDTO.getCurr());
	        apOffSetInvoiceDetailsVO.setExRate(apOffSetInvoiceDetailsDTO.getExRate());
	        apOffSetInvoiceDetailsVO.setInvAmount(apOffSetInvoiceDetailsDTO.getInvAmount());
	        apOffSetInvoiceDetailsVO.setOutStanding(apOffSetInvoiceDetailsDTO.getOutStanding());
	        apOffSetInvoiceDetailsVO.setSettled(apOffSetInvoiceDetailsDTO.getSettled());
	        apOffSetInvoiceDetailsVO.setSetExRate(apOffSetInvoiceDetailsDTO.getSetExRate());
	        apOffSetInvoiceDetailsVO.setTnxSettled(apOffSetInvoiceDetailsDTO.getTnxSettled());
	        apOffSetInvoiceDetailsVO.setGainOrLoss(apOffSetInvoiceDetailsDTO.getGainOrLoss());
	        apOffSetInvoiceDetailsVO.setRemarks(apOffSetInvoiceDetailsDTO.getRemarks());

	        apOffSetInvoiceDetailsVO.setApAdjustmentOffSetVO(apAdjustmentOffSetVO);
	        apOffSetInvoiceDetailsVOs.add(apOffSetInvoiceDetailsVO);

	        if (apOffSetInvoiceDetailsDTO.getSettled() != null) {
	            totalSettledAmount = totalSettledAmount.subtract(apOffSetInvoiceDetailsDTO.getSettled());
	        }
	        if (apOffSetInvoiceDetailsDTO.getGainOrLoss() != null) {
	            totalForexGainOrLoss = totalForexGainOrLoss.add(apOffSetInvoiceDetailsDTO.getGainOrLoss());
	        }
	    }

	    BigDecimal roundedTotalSettled = totalSettledAmount.setScale(2, RoundingMode.HALF_UP);
	    BigDecimal roundOffAmount = totalSettledAmount.subtract(roundedTotalSettled).setScale(2, RoundingMode.HALF_UP);

	    apAdjustmentOffSetVO.setTotalSettled(roundedTotalSettled);
	    apAdjustmentOffSetVO.setRoundOffAmount(roundOffAmount);

	    if (apAdjustmentOffSetDTO.getAmount() != null) {
	        apAdjustmentOffSetVO.setOnAccount(roundedTotalSettled.add(apAdjustmentOffSetDTO.getAmount()));
	    }

	    apAdjustmentOffSetVO.setForexGainOrLoss(totalForexGainOrLoss.setScale(2, RoundingMode.HALF_UP));
	    
	    apAdjustmentOffSetVO.setApOffSetInvoiceDetailsVO(apOffSetInvoiceDetailsVOs);
	}
}
