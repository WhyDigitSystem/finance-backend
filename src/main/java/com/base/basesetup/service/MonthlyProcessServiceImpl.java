package com.base.basesetup.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.basesetup.dto.MonthlyProcessDTO;
import com.base.basesetup.entity.MonthlyProcessDetailsVO;
import com.base.basesetup.entity.MonthlyProcessVO;
import com.base.basesetup.entity.PreviousYearActualVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.MonthlyProcessDetailsRepo;
import com.base.basesetup.repo.MonthlyProcessRepo;
import com.base.basesetup.repo.PreviousYearActualRepo;

@Service
public class MonthlyProcessServiceImpl implements MonthlyProcessService {

	public static final Logger LOGGER = LoggerFactory.getLogger(MonthlyProcessServiceImpl.class);

	@Autowired
	MonthlyProcessRepo monthlyProcessRepo;

	@Autowired
	MonthlyProcessDetailsRepo monthlyProcessDetailsRepo;
	
	@Autowired
	QuaterMonthService quaterMonthService;

	@Autowired
	PreviousYearActualRepo previousYearActualRepo;

	@Override
	public Map<String, Object> createUpdateMonthlyProcess(MonthlyProcessDTO monthlyProcessDTO)
			throws ApplicationException {
		MonthlyProcessVO monthlyProcessVO = new MonthlyProcessVO();
		String message;
		MonthlyProcessVO monthly = monthlyProcessRepo.getOrgIdAndClientCodeAndYearAndMonthAndMainGroupAndSubGroup(
				monthlyProcessDTO.getOrgId(), monthlyProcessDTO.getClientCode(), monthlyProcessDTO.getYear(),
				monthlyProcessDTO.getMonth(), monthlyProcessDTO.getMainGroup(), monthlyProcessDTO.getSubGroup());
		if (monthly != null) {
			monthlyProcessVO = monthlyProcessRepo.findByOrgIdAndClientCodeAndYearAndMonthAndMainGroupAndSubGroup(
					monthlyProcessDTO.getOrgId(), monthlyProcessDTO.getClientCode(), monthlyProcessDTO.getYear(),
					monthlyProcessDTO.getMonth(), monthlyProcessDTO.getMainGroup(), monthlyProcessDTO.getSubGroup())
					.orElseThrow(() -> new ApplicationException("Monthly Process not found"));

			monthlyProcessVO.setUpdatedBy(monthlyProcessDTO.getCreatedBy());
			createUpdateMonthlyProcessVOByMonthlyProcessDTO(monthlyProcessDTO, monthlyProcessVO);
			message = "Monthly Process Updated Successfully";
		} else {
			monthlyProcessVO.setCreatedBy(monthlyProcessDTO.getCreatedBy());
			monthlyProcessVO.setUpdatedBy(monthlyProcessDTO.getCreatedBy());
			createUpdateMonthlyProcessVOByMonthlyProcessDTO(monthlyProcessDTO, monthlyProcessVO);
			message = "Monthly Process Created Successfully";
		}

		monthlyProcessVO = monthlyProcessRepo.save(monthlyProcessVO);
		List<MonthlyProcessDetailsVO> monthlyProcessDetailsVO = monthlyProcessVO.getMonthlyProcessDetailsVO();
		for (MonthlyProcessDetailsVO monthlyProcessDetailsVO2 : monthlyProcessDetailsVO) {
			if (monthlyProcessDetailsVO2.isApproveStatus()) {
				
				PreviousYearActualVO previousYearActualVO = previousYearActualRepo.getPreviousYearDetails(
						monthlyProcessDetailsVO2.getOrgId(), monthlyProcessDetailsVO2.getClientCode(),
						monthlyProcessDetailsVO2.getYear(), monthlyProcessDetailsVO2.getMonth(),
						monthlyProcessDetailsVO2.getMainGroup(), monthlyProcessDetailsVO2.getSegmentCode(),
						monthlyProcessDetailsVO2.getElGlCode());

				if (previousYearActualVO == null) {
					previousYearActualVO = new PreviousYearActualVO();
					previousYearActualVO.setActive(true);
					previousYearActualVO.setCancel(false);
					previousYearActualVO.setCreatedBy(monthlyProcessVO.getCreatedBy());
					previousYearActualVO.setUpdatedBy(monthlyProcessVO.getUpdatedBy());
					previousYearActualVO.setOrgId(monthlyProcessDetailsVO2.getOrgId());
					previousYearActualVO.setClient(monthlyProcessDetailsVO2.getClient());
					previousYearActualVO.setClientCode(monthlyProcessDetailsVO2.getClientCode());
					previousYearActualVO.setYear(monthlyProcessDetailsVO2.getYear());
					previousYearActualVO.setMonth(monthlyProcessDetailsVO2.getMonth());
					previousYearActualVO.setMainGroup(monthlyProcessDetailsVO2.getMainGroup());
					previousYearActualVO.setSubGroup(monthlyProcessDetailsVO2.getSegment());
					previousYearActualVO.setSubGroupCode(monthlyProcessDetailsVO2.getSegmentCode());
					previousYearActualVO.setAccountCode(monthlyProcessDetailsVO2.getElGlCode());
					previousYearActualVO.setAccountName(monthlyProcessDetailsVO2.getElGl());
					previousYearActualVO.setNatureOfAccount(monthlyProcessDetailsVO2.getNatureOfAccount());
					if (monthlyProcessDetailsVO2.getNatureOfAccount().equals("Cr")) {
						previousYearActualVO.setAmount(
								monthlyProcessDetailsVO2.getClosingBalance().multiply(BigDecimal.valueOf(-1)));
					}
					else
					{
						previousYearActualVO.setAmount(monthlyProcessDetailsVO2.getClosingBalance());
					}
					
					int quater=quaterMonthService.getQuaterMonthDetails(monthlyProcessDTO.getYearType(), monthlyProcessDTO.getMonth());
					previousYearActualVO.setQuater(String.valueOf(quater));
					int monthseq= quaterMonthService.getMonthNumber(monthlyProcessDTO.getYearType(), monthlyProcessDTO.getMonth());
					previousYearActualVO.setMonthsequence(monthseq);
					
					previousYearActualRepo.save(previousYearActualVO);

				}
				else
				{
					previousYearActualVO.setActive(true);
					previousYearActualVO.setCancel(false);
					previousYearActualVO.setCreatedBy(monthlyProcessVO.getCreatedBy());
					previousYearActualVO.setUpdatedBy(monthlyProcessVO.getUpdatedBy());
					previousYearActualVO.setOrgId(monthlyProcessDetailsVO2.getOrgId());
					previousYearActualVO.setClient(monthlyProcessDetailsVO2.getClient());
					previousYearActualVO.setClientCode(monthlyProcessDetailsVO2.getClientCode());
					previousYearActualVO.setYear(monthlyProcessDetailsVO2.getYear());
					previousYearActualVO.setMonth(monthlyProcessDetailsVO2.getMonth());
					previousYearActualVO.setMainGroup(monthlyProcessDetailsVO2.getMainGroup());
					previousYearActualVO.setSubGroup(monthlyProcessDetailsVO2.getSegment());
					previousYearActualVO.setSubGroupCode(monthlyProcessDetailsVO2.getSegmentCode());
					previousYearActualVO.setAccountCode(monthlyProcessDetailsVO2.getElGlCode());
					previousYearActualVO.setAccountName(monthlyProcessDetailsVO2.getElGl());
					previousYearActualVO.setNatureOfAccount(monthlyProcessDetailsVO2.getNatureOfAccount());
					if (monthlyProcessDetailsVO2.getNatureOfAccount().equals("Cr")) {
						previousYearActualVO.setAmount(
								monthlyProcessDetailsVO2.getClosingBalance().multiply(BigDecimal.valueOf(-1)));
					}
					else
					{
						previousYearActualVO.setAmount(monthlyProcessDetailsVO2.getClosingBalance());
					}
					int quater=quaterMonthService.getQuaterMonthDetails(monthlyProcessDTO.getYearType(), monthlyProcessDTO.getMonth());
					previousYearActualVO.setQuater(String.valueOf(quater));
					int monthseq= quaterMonthService.getMonthNumber(monthlyProcessDTO.getYearType(), monthlyProcessDTO.getMonth());
					previousYearActualVO.setMonthsequence(monthseq);
					
					previousYearActualRepo.save(previousYearActualVO);
				}
			}

		}
		Map<String, Object> response = new HashMap<>();
		response.put("monthlyProcessVO", monthlyProcessVO);
		response.put("message", message);
		return response;
	}

	private void createUpdateMonthlyProcessVOByMonthlyProcessDTO(MonthlyProcessDTO monthlyProcessDTO,
			MonthlyProcessVO monthlyProcessVO) {

		monthlyProcessVO.setOrgId(monthlyProcessDTO.getOrgId());
		monthlyProcessVO.setMainGroup(monthlyProcessDTO.getMainGroup());
		monthlyProcessVO.setSubGroup(monthlyProcessDTO.getSubGroup());
		monthlyProcessVO.setSubGroupCode(monthlyProcessDTO.getSubGroupCode());
		monthlyProcessVO.setYear(monthlyProcessDTO.getYear());
		monthlyProcessVO.setMonth(monthlyProcessDTO.getMonth());
		monthlyProcessVO.setClient(monthlyProcessDTO.getClient());
		monthlyProcessVO.setClientCode(monthlyProcessDTO.getClientCode());

		if (ObjectUtils.isNotEmpty(monthlyProcessVO.getId())) {
			List<MonthlyProcessDetailsVO> monthlyProcessDetailsVOs1 = monthlyProcessDetailsRepo
					.findByMonthlyProcessVO(monthlyProcessVO);
			monthlyProcessDetailsRepo.deleteAll(monthlyProcessDetailsVOs1);
		}

		List<MonthlyProcessDetailsVO> monthlyProcessDetailsVOs = monthlyProcessDTO.getMonthlyProcessDetailsDTO()
				.stream().map(dto -> {
					MonthlyProcessDetailsVO vo = new MonthlyProcessDetailsVO();
					vo.setElGlCode(dto.getElGlCode());
					vo.setElGl(dto.getElGl());
					vo.setNatureOfAccount(dto.getNatureOfAccount());
					vo.setSegment(monthlyProcessDTO.getSubGroup());
					vo.setSegmentCode(monthlyProcessDTO.getSubGroupCode());
					vo.setMainGroup(monthlyProcessDTO.getMainGroup());
					vo.setCurrentMonthDebit(dto.getCurrentMonthDebit());
					vo.setOrgId(monthlyProcessDTO.getOrgId());
					vo.setCurrentMonthCredit(dto.getCurrentMonthCredit());
					vo.setCurrentMonthClosing(dto.getCurrentMonthDebit().subtract(dto.getCurrentMonthCredit()));
					vo.setPrevioustMonthDebit(dto.getPrevioustMonthDebit());
					vo.setPrevioustMonthCredit(dto.getPrevioustMonthCredit());

					BigDecimal currentMonthClosing = dto.getCurrentMonthDebit().subtract(dto.getCurrentMonthCredit());
					BigDecimal preMonthClosing = dto.getPrevioustMonthDebit().subtract(dto.getPrevioustMonthCredit());

					vo.setPrevioustMonthClosing(dto.getPrevioustMonthDebit().subtract(dto.getPrevioustMonthCredit()));
					vo.setForTheMonthActDebit(dto.getCurrentMonthDebit().subtract(dto.getPrevioustMonthDebit()));
					vo.setForTheMonthActCredit(dto.getCurrentMonthCredit().subtract(dto.getPrevioustMonthCredit()));
					BigDecimal forMonthActDebit = dto.getCurrentMonthDebit().subtract(dto.getPrevioustMonthDebit());
					BigDecimal forMonthActCredit = dto.getCurrentMonthCredit().subtract(dto.getPrevioustMonthCredit());
					vo.setForTheMonthActClosing(forMonthActDebit.subtract(forMonthActCredit));

					vo.setProvisionDebit(dto.getProvisionDebit());
					vo.setProvisionCredit(dto.getProvisionCredit());
					vo.setProvisionClosing(dto.getProvisionDebit().subtract(dto.getProvisionCredit()));

					vo.setForTheMonthDebit(forMonthActDebit.add(dto.getProvisionDebit()));
					vo.setForTheMonthCredit(forMonthActCredit.add(dto.getProvisionCredit()));
					BigDecimal forTheMonthDebit = forMonthActDebit.add(dto.getProvisionDebit());
					BigDecimal forTheMonthCredit = forMonthActCredit.add(dto.getProvisionCredit());
					vo.setForTheMonthClosing(forTheMonthDebit.subtract(forTheMonthCredit));
					vo.setMismatch(dto.isMismatch());
					vo.setProvisionRemarks(dto.getProvisionRemarks());
					vo.setYear(monthlyProcessDTO.getYear());
					vo.setMonth(monthlyProcessDTO.getMonth());
					vo.setClient(monthlyProcessDTO.getClient());
					vo.setClientCode(monthlyProcessDTO.getClientCode());
					vo.setApproveStatus(dto.isApproveStatus());
					vo.setMonthlyProcessVO(monthlyProcessVO);
					if (dto.isApproveStatus()) {
						vo.setClosingBalance(forTheMonthDebit.subtract(forTheMonthCredit));
					} else {
						vo.setClosingBalance(BigDecimal.ZERO);
					}
					return vo;
				}).collect(Collectors.toList());

		monthlyProcessVO.setMonthlyProcessDetailsVO(monthlyProcessDetailsVOs);
	}

	@Override
	public List<MonthlyProcessVO> getAllMonthlyProcessByClientCode(Long orgId, String clientCode, String mainGroup,
			String subGroupCode, String finYear) {

		List<MonthlyProcessVO> monthlyProcessVO = monthlyProcessRepo
				.findByOrgIdAndClientCodeAndMainGroupAndSubGroupCodeAndYear(orgId, clientCode, mainGroup, subGroupCode,
						finYear);
		return monthlyProcessVO;

	}

	@Override
	public MonthlyProcessVO getAllMonthlyProcessById(Long id) {
		MonthlyProcessVO monthlyProcessVO = monthlyProcessRepo.findById(id).get();
		return monthlyProcessVO;
	}

}
