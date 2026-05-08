package com.base.basesetup.service;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.base.basesetup.dto.ApAdjustmentOffSetDTO;
import com.base.basesetup.dto.ArAdjustmentOffSetDTO;
import com.base.basesetup.entity.ApAdjustmentOffSetVO;
import com.base.basesetup.entity.ArAdjustmentOffSetVO;
import com.base.basesetup.entity.PaymentVO;
import com.base.basesetup.exception.ApplicationException;

@Service
public interface ArAdjustmentOffSetService {

	//AR ADJUSTMENT OFFSET
	
	List<ArAdjustmentOffSetVO> getAllArAdjustmentOffSetByOrgId(Long orgId);

	List<ArAdjustmentOffSetVO> getArAdjustmentOffSetById(Long id);

	Map<String, Object> updateCreateArAdjustmentOffSet(@Valid ArAdjustmentOffSetDTO arAdjustmentOffSetDTO) throws ApplicationException;

	Map<String, Object> getArAdjustmentOffSetDocId(Long orgId, String finYear, String branch, String branchCode);

	List<Map<String, Object>> getAllCustomerReceiptByOrgIdAndBranchCode(Long orgId, String branchCode,String customerName);

	//AP ADJUSTMENT OFFSET
	
	List<ApAdjustmentOffSetVO> getAllApAdjustmentOffSetByOrgId(Long orgId);

	List<ApAdjustmentOffSetVO> getApAdjustmentOffSetById(Long id);

	String getApAdjustmentOffSetDocId(Long orgId, String finYear, String branch, String branchCode);

	List<PaymentVO> getAllVendorPaymentByOrgIdAndBranchCode(Long orgId, String branchCode);

	Map<String, Object> updateCreateApAdjustmentOffSet(@Valid ApAdjustmentOffSetDTO apAdjustmentOffSetDTO) throws ApplicationException;

	List<Map<String, Object>> getArOffsetFillgrid(Long orgId, String subLedgerCode, String docId, String branch,
			String docDate);


	ArAdjustmentOffSetVO approveArAdjustmentOffSet(Long orgId, Long id, String docId, String action, String actionBy)
			throws ApplicationException;

	ApAdjustmentOffSetVO approveApAdjustmentOffSet(Long orgId, Long id, String docId, String action, String actionBy)
			throws ApplicationException;

}
