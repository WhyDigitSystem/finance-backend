package com.base.basesetup.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.basesetup.dto.OffSetDTO;
import com.base.basesetup.dto.OffSetDetailsDTO;
import com.base.basesetup.entity.DocumentTypeMappingDetailsVO;
import com.base.basesetup.entity.OffSetDetailsVO;
import com.base.basesetup.entity.OffSetVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.DocumentTypeMappingDetailsRepo;
import com.base.basesetup.repo.OffSetDetailsRepo;
import com.base.basesetup.repo.OffSetRepo;

@Service
public class OffSetServiceImpl implements OffSetService{
	
	public static final Logger LOGGER = LoggerFactory.getLogger(OffSetServiceImpl.class);
	
	@Autowired
	OffSetRepo offSetRepo;
	
	@Autowired
	OffSetDetailsRepo offSetDetailsRepo;
	
	@Autowired
	DocumentTypeMappingDetailsRepo documentTypeMappingDetailsRepo;

	@Override
	public List<OffSetVO> getAllOffSet(Long orgId, String finYear, String branchCode) {
		
		return offSetRepo.findByOrgIdAndFinYearAndBranchCode(orgId,finYear,branchCode);
	}

	@Override
	public OffSetVO getOffSetById(Long Id) {
		
		return offSetRepo.findById(Id).get();
	}

	@Override
	public Map<String, Object> updateCreateOffSet(OffSetDTO offSetDTO) throws ApplicationException {
		
		String screenCode = "OFS";
		OffSetVO offSetVO = new OffSetVO();
		String message;
		if (ObjectUtils.isNotEmpty(offSetDTO.getId())) {
			offSetVO = offSetRepo.findById(offSetDTO.getId())
					.orElseThrow(() -> new ApplicationException("OffSet not found"));

			offSetVO.setUpdatedBy(offSetDTO.getCreatedBy());
			createUpdateOffSetVOByOffSetDTO(offSetDTO, offSetVO);
			message = "Off Set Updated Successfully";
		} else {
			// GETDOCID API
			String docId = offSetRepo.getOffSetDocId(offSetDTO.getOrgId(), offSetDTO.getFinYear(),
					offSetDTO.getBranchCode(), screenCode);
			offSetVO.setDocId(docId);

			// GETDOCID LASTNO +1
			DocumentTypeMappingDetailsVO documentTypeMappingDetailsVO = documentTypeMappingDetailsRepo
					.findByOrgIdAndFinYearAndBranchCodeAndScreenCode(offSetDTO.getOrgId(), offSetDTO.getFinYear(),
							offSetDTO.getBranchCode(), screenCode);
			documentTypeMappingDetailsVO.setLastno(documentTypeMappingDetailsVO.getLastno() + 1);
			documentTypeMappingDetailsRepo.save(documentTypeMappingDetailsVO);

			offSetVO.setCreatedBy(offSetDTO.getCreatedBy());
			offSetVO.setUpdatedBy(offSetDTO.getCreatedBy());
			createUpdateOffSetVOByOffSetDTO(offSetDTO, offSetVO);
			message = "Off Set Created Successfully";
		}

		offSetRepo.save(offSetVO);
		Map<String, Object> response = new HashMap<>();
		response.put("offSetVO", offSetVO);
		response.put("message", message);
		return response;
	}

	private void createUpdateOffSetVOByOffSetDTO(OffSetDTO offSetDTO, OffSetVO offSetVO) {

		// Map fields from DTO to VO
		offSetVO.setBranch(offSetDTO.getBranch());
		offSetVO.setBranchCode(offSetDTO.getBranchCode());
        offSetVO.setFinYear(offSetDTO.getFinYear());
        offSetVO.setRpDocId(offSetDTO.getRpDocId());
        offSetVO.setRpDocDate(offSetDTO.getRpDocDate());
        offSetVO.setSubLedgerType(offSetDTO.getSubLedgerType());
        offSetVO.setSubLedgername(offSetDTO.getSubLedgername());
        offSetVO.setSubLedgerCode(offSetDTO.getSubLedgerCode());
        offSetVO.setAccountName(offSetDTO.getAccountName());
        offSetVO.setCurrency(offSetDTO.getCurrency());
        offSetVO.setExRate(offSetDTO.getExRate());
        offSetVO.setOrgId(offSetDTO.getOrgId());
        offSetVO.setCreatedBy(offSetDTO.getCreatedBy());

		if (ObjectUtils.isNotEmpty(offSetVO.getId())) {
			List<OffSetDetailsVO> detailsVOs = offSetDetailsRepo.findByOffSetVO(offSetVO);
			offSetDetailsRepo.deleteAll(detailsVOs);
		}
		BigDecimal totOutStandingAmount = BigDecimal.ZERO;
		BigDecimal totGainOrLossAmount = BigDecimal.ZERO;
		

		List<OffSetDetailsVO> offSetDetailsVO = new ArrayList<>();
		for (OffSetDetailsDTO taxInvoiceDetailsDTO : offSetDTO.getOffSetDetailsDTO()) {

			OffSetDetailsVO taxInvoiceDetailsVO = new OffSetDetailsVO();
			taxInvoiceDetailsVO.setInvoiceNo(taxInvoiceDetailsDTO.getInvoiceNo());
			taxInvoiceDetailsVO.setInvoiceDate(taxInvoiceDetailsDTO.getInvoiceDate());
			taxInvoiceDetailsVO.setRefNo(taxInvoiceDetailsDTO.getRefNo());
			taxInvoiceDetailsVO.setInvCurrency(taxInvoiceDetailsDTO.getInvCurrency());
			taxInvoiceDetailsVO.setInvExRate(taxInvoiceDetailsDTO.getInvExRate());
			taxInvoiceDetailsVO.setArApAmount(taxInvoiceDetailsDTO.getArApAmount());
			taxInvoiceDetailsVO.setArApOutStanding(taxInvoiceDetailsDTO.getArApOutStanding());
			taxInvoiceDetailsVO.setArApSettled(taxInvoiceDetailsDTO.getArApSettled());
			taxInvoiceDetailsVO.setSettledExRate(taxInvoiceDetailsDTO.getSettledExRate());
			taxInvoiceDetailsVO.setGainOrLoss(taxInvoiceDetailsDTO.getGainOrLoss());

			totOutStandingAmount = totOutStandingAmount.add(taxInvoiceDetailsDTO.getArApOutStanding());
			totGainOrLossAmount=totGainOrLossAmount.add(taxInvoiceDetailsDTO.getGainOrLoss());
			taxInvoiceDetailsVO.setOffSetVO(offSetVO);
			offSetDetailsVO.add(taxInvoiceDetailsVO);
		}
		offSetVO.setAmount(totOutStandingAmount.multiply(BigDecimal.valueOf(-1)));
		offSetVO.setTotalSettled(totOutStandingAmount);
		offSetVO.setFxGaOrLoss(totGainOrLossAmount);
	}
	
	@Override
	public String getOffSetDocId(Long orgId, String finYear, String branch, String branchCode) {
		String ScreenCode = "OFS";
		String result = offSetRepo.getOffSetDocId(orgId, finYear, branchCode, ScreenCode);
		return result;
	}

}
