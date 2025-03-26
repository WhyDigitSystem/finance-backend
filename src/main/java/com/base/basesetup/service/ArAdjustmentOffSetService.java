package com.base.basesetup.service;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.base.basesetup.dto.ArAdjustmentOffSetDTO;
import com.base.basesetup.entity.ArAdjustmentOffSetVO;
import com.base.basesetup.entity.ReceiptVO;
import com.base.basesetup.exception.ApplicationException;

@Service
public interface ArAdjustmentOffSetService {

	List<ArAdjustmentOffSetVO> getAllArAdjustmentOffSetByOrgId(Long orgId);

	List<ArAdjustmentOffSetVO> getArAdjustmentOffSetById(Long id);

	Map<String, Object> updateCreateArAdjustmentOffSet(@Valid ArAdjustmentOffSetDTO arAdjustmentOffSetDTO) throws ApplicationException;

	String getArAdjustmentOffSetDocId(Long orgId, String finYear, String branch, String branchCode);

	List<ReceiptVO> getAllCustomerReceiptByOrgIdAndBranchCode(Long orgId, String branchCode);

}
