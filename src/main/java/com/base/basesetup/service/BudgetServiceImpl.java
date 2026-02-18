package com.base.basesetup.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.basesetup.dto.BudgetACPDTO;
import com.base.basesetup.dto.BudgetDTO;
import com.base.basesetup.dto.BudgetHeadCountDTO;
import com.base.basesetup.dto.BudgetRatioAnalysisDTO;
import com.base.basesetup.dto.BudgetUnitWiseDTO;
import com.base.basesetup.dto.IncrementalProfitDTO;
import com.base.basesetup.dto.LoanOutstandingDTO;
import com.base.basesetup.dto.OrderBookingDTO;
import com.base.basesetup.dto.PreviousYearDTO;
import com.base.basesetup.dto.PyAdvancePaymentReceiptDTO;
import com.base.basesetup.dto.PyHeadCountDTO;
import com.base.basesetup.dto.SalesPurchaseDTO;
import com.base.basesetup.dto.SalesPurchaseItemDTO;
import com.base.basesetup.entity.BudgetACPVO;
import com.base.basesetup.entity.BudgetHeadCountVO;
import com.base.basesetup.entity.BudgetIncrementalProfitVO;
import com.base.basesetup.entity.BudgetLoansOutStandingVO;
import com.base.basesetup.entity.BudgetSalesPurchaseItemVO;
import com.base.basesetup.entity.BudgetSalesPurchaseVO;
import com.base.basesetup.entity.BudgetUnitWiseVO;
import com.base.basesetup.entity.BudgetVO;
import com.base.basesetup.entity.FinancialYearVO;
import com.base.basesetup.entity.GroupLedgersVO;
import com.base.basesetup.entity.OrderBookingVO;
import com.base.basesetup.entity.PYHeadCountVO;
import com.base.basesetup.entity.PreviousYearARAPVO;
import com.base.basesetup.entity.PreviousYearActualOBVO;
import com.base.basesetup.entity.PreviousYearActualVO;
import com.base.basesetup.entity.PreviousYearIncrementalProfitVO;
import com.base.basesetup.entity.PreviousYearUnitwiseVO;
import com.base.basesetup.entity.PyAdvancePaymentReceiptVO;
import com.base.basesetup.entity.PyLoansOutStandingVO;
import com.base.basesetup.entity.PySalesPurchaseItemVO;
import com.base.basesetup.entity.PySalesPurchaseVO;
import com.base.basesetup.repo.BudgetACPRepo;
import com.base.basesetup.repo.BudgetHeadCountRepo;
import com.base.basesetup.repo.BudgetIncrementalProfitRepo;
import com.base.basesetup.repo.BudgetLoansOutStandingRepo;
import com.base.basesetup.repo.BudgetRepo;
import com.base.basesetup.repo.BudgetSalesPurchaseItemRepo;
import com.base.basesetup.repo.BudgetSalesPurchaseRepo;
import com.base.basesetup.repo.BudgetUnitWiseRepo;
import com.base.basesetup.repo.FinancialYearRepo;
import com.base.basesetup.repo.GroupLedgersRepo;
import com.base.basesetup.repo.GroupMappingRepo;
import com.base.basesetup.repo.OrderBookingRepo;
import com.base.basesetup.repo.PreviousYearARAPRepo;
import com.base.basesetup.repo.PreviousYearActualOBRepo;
import com.base.basesetup.repo.PreviousYearActualRepo;
import com.base.basesetup.repo.PreviousYearIncrementalProfitRepo;
import com.base.basesetup.repo.PreviousYearUnitwiseRepo;
import com.base.basesetup.repo.PyAdvancePaymentReceiptRepo;
import com.base.basesetup.repo.PyHeadCountRepo;
import com.base.basesetup.repo.PyLoansOutStandingRepo;
import com.base.basesetup.repo.PySalesPurchaseItemRepo;
import com.base.basesetup.repo.PySalesPurchaseRepo;
import com.base.basesetup.repo.SubGroupDetailsRepo;

@Service
public class BudgetServiceImpl implements BudgetService {

	public static final Logger LOGGER = LoggerFactory.getLogger(BudgetServiceImpl.class);

	@Autowired
	GroupMappingRepo groupMappingRepo;

	@Autowired
	QuaterMonthService quaterMonthService;

	@Autowired
	FinancialYearRepo financialYearRepo;

	@Autowired
	PreviousYearActualOBRepo previousYearActualOBRepo;

	@Autowired
	BudgetUnitWiseRepo budgetUnitWiseRepo;

	@Autowired
	PreviousYearUnitwiseRepo previousYearUnitwiseRepo;

	@Autowired
	BudgetACPRepo budgetACPRepo;

	@Autowired
	PreviousYearARAPRepo previousYearARAPRepo;

	@Autowired
	SubGroupDetailsRepo subGroupDetailsRepo;

	@Autowired
	GroupLedgersRepo groupLedgersRepo;

	@Autowired
	BudgetHeadCountRepo budgetHeadCountRepo;

	@Autowired
	PyHeadCountRepo pyHeadCountRepo;

	@Autowired
	OrderBookingRepo orderBookingRepo;

	@Autowired
	BudgetRepo budgetRepo;

	@Autowired
	PreviousYearActualRepo previousYearActualRepo;

	@Autowired
	BudgetIncrementalProfitRepo budgetIncrementalProfitRepo;

	@Autowired
	PreviousYearIncrementalProfitRepo previousYearIncrementalProfitRepo;

	@Autowired
	PyAdvancePaymentReceiptRepo pyAdvancePaymentReceiptRepo;

	@Autowired
	BudgetLoansOutStandingRepo budgetLoansOutStandingRepo;

	@Autowired
	PyLoansOutStandingRepo pyLoansOutStandingRepo;

	@Autowired
	BudgetSalesPurchaseRepo budgetSalesPurchaseRepo;

	@Autowired
	PySalesPurchaseRepo pySalesPurchaseRepo;

	@Autowired
	BudgetSalesPurchaseItemRepo budgetSalesPurchaseItemRepo;

	@Autowired
	PySalesPurchaseItemRepo pySalesPurchaseItemRepo;

	@Override
	public List<Map<String, Object>> getSubGroupDetails(Long orgId, String mainGroup) {

		Set<Object[]> subGroupDetails = groupMappingRepo.getSubGroupDetails(orgId, mainGroup);
		return getSubGroupDetails(subGroupDetails);
	}

	private List<Map<String, Object>> getSubGroupDetails(Set<Object[]> subGroupDetails) {
		List<Map<String, Object>> subgroup = new ArrayList<>();
		for (Object[] sub : subGroupDetails) {
			Map<String, Object> mp = new HashMap<>();
			mp.put("subGroupName", sub[0] != null ? sub[0].toString() : "");
			mp.put("subGroupCode", sub[1] != null ? sub[1].toString() : null);
			subgroup.add(mp);
		}
		return subgroup;
	}

	@Override
	public List<Map<String, Object>> getGroupLedgersDetails(Long orgId, String year, String clientCode,
			String mainGroup, String subGroupCode) {

		Set<Object[]> subGroupDetails = groupMappingRepo.getGroupLedgersDetails(orgId, year, clientCode, mainGroup,
				subGroupCode);
		return getGroupLedgerDetails(subGroupDetails);
	}

	private List<Map<String, Object>> getGroupLedgerDetails(Set<Object[]> subGroupDetails) {
		List<Map<String, Object>> subgroup = new ArrayList<>();
		for (Object[] sub : subGroupDetails) {
			Map<String, Object> mp = new HashMap<>();
			mp.put("subGroupName", sub[0] != null ? sub[0].toString() : "");
			mp.put("subGroupCode", sub[1] != null ? sub[1].toString() : "");
			mp.put("natureOfAccount", sub[2] != null ? sub[2].toString() : "");
			mp.put("month", sub[3] != null ? sub[3].toString() : "");
			mp.put("amount", sub[4] != null ? new BigDecimal(sub[4].toString()) : BigDecimal.ZERO);
			subgroup.add(mp);
		}
		return subgroup;
	}

	@Override
	@Transactional
	public Map<String, Object> createUpdateBudget(List<BudgetDTO> budgetDTOList) {
		Map<String, Object> response = new HashMap<>();
		if (budgetDTOList == null || budgetDTOList.isEmpty()) {
			response.put("message", "No data to save");
			return response;
		}

		String createdBy = null;
		String client = null;
		String yearType = null;

		BudgetDTO firstDto = budgetDTOList.get(0);
		Long orgId = firstDto.getOrgId();
		String clientCode = firstDto.getClientCode();
		String year = firstDto.getYear();
		String mainGroup = firstDto.getMainGroup();
		String subGroupname = firstDto.getSubGroup();

		Set<String> distinctMonths = budgetDTOList.stream().map(BudgetDTO::getMonth).filter(Objects::nonNull)
				.collect(Collectors.toSet());


		List<BudgetDTO> positiveAmountList = budgetDTOList.stream()
				.filter(dto -> dto.getAmount() != null || dto.getAmount() !=BigDecimal.ZERO )
				.collect(Collectors.toList());
		Set<String> distinctSubgroup = new HashSet<>();
		
		for (BudgetDTO pto : positiveAmountList) {
			GroupLedgersVO groupVO = groupLedgersRepo.findByOrgIdAndAccountNameAndMainGroupName(pto.getOrgId(),
					pto.getAccountName(), pto.getMainGroup());
			distinctSubgroup.add(groupVO.getGroupName());
		}

		for (String subg : distinctSubgroup) {
			// ✅ Delete only once per distinct month
			for (String month : distinctMonths) {
				budgetRepo.deleteByOrgIdAndClientAndYearAndMainGroupAndSubGroupAndMonth(orgId, clientCode,
						year, mainGroup, subg, month);
			}
		}

		// Delete existing records
		for (BudgetDTO dto : positiveAmountList) {
			try {
				GroupLedgersVO groupVO = groupLedgersRepo.findByOrgIdAndAccountNameAndMainGroupName(dto.getOrgId(),
						dto.getAccountName(), dto.getMainGroup());

			} catch (Exception e) {
				System.out.println("Error fetching group: " + dto.getAccountName() + " | " + dto.getMainGroup());
			}

			createdBy = dto.getCreatedBy();
			year = dto.getYear();
			orgId = dto.getOrgId();
			clientCode = dto.getClientCode();
			client = dto.getClient();
			yearType = dto.getYearType();
		}

		for (BudgetDTO dto : positiveAmountList) {
			BudgetVO vo = new BudgetVO();
			vo.setOrgId(dto.getOrgId());
			vo.setClient(dto.getClient());
			vo.setClientCode(dto.getClientCode());
			vo.setCreatedBy(dto.getCreatedBy());
			vo.setUpdatedBy(dto.getCreatedBy());
			vo.setAccountName(dto.getAccountName());
			vo.setAccountCode(dto.getAccountCode());
			vo.setNatureOfAccount(dto.getNatureOfAccount());
			vo.setYear(dto.getYear());
			vo.setMonth(dto.getMonth());
			vo.setAmount(dto.getAmount());
			vo.setMainGroup(dto.getMainGroup());

			try {
				GroupLedgersVO groupVO = groupLedgersRepo.findByOrgIdAndAccountNameAndMainGroupName(dto.getOrgId(),
						dto.getAccountName(), dto.getMainGroup());
				vo.setSubGroup(groupVO.getGroupName());
			} catch (Exception e) {
				System.out.println("Group fetch failed: " + dto.getAccountName() + " | " + dto.getMainGroup());
			}

			vo.setSubGroupCode(dto.getSubGroupCode());
			vo.setActive(true);

			int quarter = quaterMonthService.getQuaterMonthDetails(dto.getYearType(), dto.getMonth());
			int monthSeq = quaterMonthService.getMonthNumber(dto.getYearType(), dto.getMonth());

			vo.setQuater(String.valueOf(quarter));
			vo.setMonthsequence(monthSeq);

			budgetRepo.save(vo);
		}

		// 🔁 Handle "Closing Stock" logic once
		boolean containsClosingStock = budgetDTOList.stream()
				.anyMatch(dto -> "Closing Stock".equalsIgnoreCase(dto.getMainGroup()));

		if (containsClosingStock) {
			String mGroup = "Balance Sheet Schedule";
			String subGroup = "Stock";

			// 🔁 Delete old Balance Sheet entries
			List<BudgetVO> bsList = budgetRepo.findStockDetails(orgId, clientCode, year, mGroup, subGroup);
			if (bsList != null && !bsList.isEmpty())
				budgetRepo.deleteAll(bsList);

			// 🔁 Closing Stock Monthly Summary
			List<Object[]> closingStockSums = budgetRepo.getMonthWiseSumAmountForClosingStock(orgId, clientCode, year);
			for (Object[] row : closingStockSums) {
				saveStockEntryBudget(orgId, clientCode, client, year, yearType, createdBy, (String) row[0],
						(BigDecimal) row[1], mGroup, subGroup, "Stock", "4000", "Cr");
			}

			String mg = "Drawing Power";
			String raw = "Raw Material";
			List<BudgetVO> rawMaterial = budgetRepo.findOldDetails(orgId, clientCode, year, mg, mg, raw);
			if (rawMaterial != null && !rawMaterial.isEmpty())
				budgetRepo.deleteAll(rawMaterial);
			// 🔁 Drawing Power (Raw Material)
			List<String> dpCodes = Arrays.asList("4001", "4002", "4003", "4004", "4005", "4006", "4007", "4008", "4012",
					"4013");
			List<Object[]> dpSums = budgetRepo.getDrawingPowerMonthWiseSum(orgId, clientCode, year, dpCodes);
			for (Object[] row : dpSums) {
				saveStockEntryBudget(orgId, clientCode, client, year, yearType, createdBy, (String) row[0],
						(BigDecimal) row[1], "Drawing Power", "Drawing Power", "Raw Material", null, null);
			}

			String wip = "WIP";
			List<BudgetVO> wipdetails = budgetRepo.findOldDetails(orgId, clientCode, year, mg, mg, wip);
			if (wipdetails != null && !wipdetails.isEmpty())
				budgetRepo.deleteAll(wipdetails);
			// 🔁 WIP
			List<Object[]> wipSums = budgetRepo.getMonthWiseSumForWIP(orgId, clientCode, year);
			for (Object[] row : wipSums) {
				saveStockEntryBudget(orgId, clientCode, client, year, yearType, createdBy, (String) row[0],
						(BigDecimal) row[1], "Drawing Power", "Drawing Power", "WIP", null, "Db");
			}

			String finishedGoods = "Finished Goods";
			List<BudgetVO> finishedGoodsdetails = budgetRepo.findOldDetails(orgId, clientCode, year, mg, mg,
					finishedGoods);
			if (finishedGoodsdetails != null && !finishedGoodsdetails.isEmpty())
				budgetRepo.deleteAll(finishedGoodsdetails);
			// 🔁 Finished Goods
			List<Object[]> fgSums = budgetRepo.getMonthWiseSumForFinishedGoods(orgId, clientCode, year,
					Arrays.asList("4010", "4011"));
			for (Object[] row : fgSums) {
				saveStockEntryBudget(orgId, clientCode, client, year, yearType, createdBy, (String) row[0],
						(BigDecimal) row[1], "Drawing Power", "Drawing Power", "Finished Goods", null, "Db");
			}
		}

		response.put("message", "Successfully Saved");
		return response;
	}

	private void saveStockEntryBudget(Long orgId, String clientCode, String client, String year, String yearType,
			String createdBy, String month, BigDecimal amount, String mainGroup, String subGroup, String accountName,
			String accountCode, String natureOfAccount) {
		BudgetVO vo = new BudgetVO();
		vo.setOrgId(orgId);
		vo.setClientCode(clientCode);
		vo.setClient(client);
		vo.setYear(year);
		vo.setMonth(month);
		vo.setAmount(amount);
		vo.setMainGroup(mainGroup);
		vo.setSubGroup(subGroup);
		vo.setActive(true);
		vo.setCreatedBy(createdBy);
		vo.setUpdatedBy(createdBy);
		vo.setAccountName(accountName);
		vo.setAccountCode(accountCode);
		vo.setNatureOfAccount(natureOfAccount);

		int quarter = quaterMonthService.getQuaterMonthDetails(yearType, month);
		int monthSeq = quaterMonthService.getMonthNumber(yearType, month);
		vo.setQuater(String.valueOf(quarter));
		vo.setMonthsequence(monthSeq);

		budgetRepo.save(vo);
	}

	@Override
	public Map<String, Object> createUpdatePreviousYear(List<PreviousYearDTO> budgetDTOList) {
		Map<String, Object> response = new HashMap<>();
		if (budgetDTOList == null || budgetDTOList.isEmpty()) {
			response.put("message", "No data to save");
			return response;
		}

		String createdBy = null;
		String client = null;
		String yearType = null;

		

		PreviousYearDTO firstDto = budgetDTOList.get(0);
		Long orgId = firstDto.getOrgId();
		String clientCode = firstDto.getClientCode();
		String year = firstDto.getYear();
		String mainGroup = firstDto.getMainGroup();

		

		Set<String> distinctMonths = budgetDTOList.stream().map(PreviousYearDTO::getMonth).filter(Objects::nonNull)
				.collect(Collectors.toSet());

		List<PreviousYearDTO> positiveAmountList = budgetDTOList.stream()
				.filter(dto -> dto.getAmount() != null  || dto.getAmount() != BigDecimal.ZERO )
				.collect(Collectors.toList());
		Set<String> distinctSubgroup = new HashSet<>();
		
		for (PreviousYearDTO pto : positiveAmountList) {
			GroupLedgersVO groupVO = groupLedgersRepo.findByOrgIdAndAccountNameAndMainGroupName(pto.getOrgId(),
					pto.getAccountName(), pto.getMainGroup());
			distinctSubgroup.add(groupVO.getGroupName());
		}

		for (String subg : distinctSubgroup) {
			// ✅ Delete only once per distinct month
			for (String month : distinctMonths) {
				previousYearActualRepo.deleteByOrgIdAndClientAndYearAndMainGroupAndSubGroupAndMonth(orgId, clientCode,
						year, mainGroup, subg, month);
			}
		}

		// Delete existing records
		for (PreviousYearDTO dto : positiveAmountList) {
			try {
				GroupLedgersVO groupVO = groupLedgersRepo.findByOrgIdAndAccountNameAndMainGroupName(dto.getOrgId(),
						dto.getAccountName(), dto.getMainGroup());

			} catch (Exception e) {
				System.out.println("Error fetching group: " + dto.getAccountName() + " | " + dto.getMainGroup());
			}

			createdBy = dto.getCreatedBy();
			year = dto.getYear();
			orgId = dto.getOrgId();
			clientCode = dto.getClientCode();
			client = dto.getClient();
			yearType = dto.getYearType();
		}

		for (PreviousYearDTO dto : positiveAmountList) {

			PreviousYearActualVO vo = new PreviousYearActualVO();
			vo.setOrgId(dto.getOrgId());
			vo.setClient(dto.getClient());
			vo.setClientCode(dto.getClientCode());
			vo.setCreatedBy(dto.getCreatedBy());
			vo.setUpdatedBy(dto.getCreatedBy());
			vo.setAccountName(dto.getAccountName());
			vo.setAccountCode(dto.getAccountCode());
			vo.setNatureOfAccount(dto.getNatureOfAccount());
			vo.setYear(dto.getYear());
			vo.setMonth(dto.getMonth());
			vo.setAmount(dto.getAmount());
			vo.setMainGroup(dto.getMainGroup());

			try {
				GroupLedgersVO groupVO = groupLedgersRepo.findByOrgIdAndAccountNameAndMainGroupName(dto.getOrgId(),
						dto.getAccountName(), dto.getMainGroup());
				vo.setSubGroup(groupVO.getGroupName());
			} catch (Exception e) {
				System.out.println("Group fetch failed: " + dto.getAccountName() + " | " + dto.getMainGroup());
			}

			vo.setSubGroupCode(dto.getSubGroupCode());
			vo.setActive(true);

			int quarter = quaterMonthService.getQuaterMonthDetails(dto.getYearType(), dto.getMonth());
			int monthSeq = quaterMonthService.getMonthNumber(dto.getYearType(), dto.getMonth());

			vo.setQuater(String.valueOf(quarter));
			vo.setMonthsequence(monthSeq);

			previousYearActualRepo.save(vo);
		}

		// 🔁 Handle "Closing Stock" logic once
		boolean containsClosingStock = budgetDTOList.stream()
				.anyMatch(dto -> "Closing Stock".equalsIgnoreCase(dto.getMainGroup()));

		if (containsClosingStock) {
			String mGroup = "Balance Sheet Schedule";
			String subGroup = "Stock";

			// 🔁 Delete old Balance Sheet entries
			List<PreviousYearActualVO> bsList = previousYearActualRepo.findStockDetails(orgId, clientCode, year, mGroup,
					subGroup);
			if (bsList != null && !bsList.isEmpty())
				previousYearActualRepo.deleteAll(bsList);

			// 🔁 Closing Stock Monthly Summary
			List<Object[]> closingStockSums = previousYearActualRepo.getMonthWiseSumAmountForClosingStock(orgId,
					clientCode, year);
			for (Object[] row : closingStockSums) {
				saveStockEntry(orgId, clientCode, client, year, yearType, createdBy, (String) row[0],
						(BigDecimal) row[1], mGroup, subGroup, "Stock", "4000", "Cr");
			}

			String mg = "Drawing Power";
			String raw = "Raw Material";
			List<PreviousYearActualVO> rawMaterial = previousYearActualRepo.findOldDetails(orgId, clientCode, year, mg,
					mg, raw);
			if (rawMaterial != null && !rawMaterial.isEmpty())
				previousYearActualRepo.deleteAll(rawMaterial);
			// 🔁 Drawing Power (Raw Material)
			List<String> dpCodes = Arrays.asList("4001", "4002", "4003", "4004", "4005", "4006", "4007", "4008", "4012",
					"4013");
			List<Object[]> dpSums = previousYearActualRepo.getDrawingPowerMonthWiseSum(orgId, clientCode, year,
					dpCodes);
			for (Object[] row : dpSums) {
				saveStockEntry(orgId, clientCode, client, year, yearType, createdBy, (String) row[0],
						(BigDecimal) row[1], "Drawing Power", "Drawing Power", "Raw Material", null, null);
			}

			String wip = "WIP";
			List<PreviousYearActualVO> wipdetails = previousYearActualRepo.findOldDetails(orgId, clientCode, year, mg,
					mg, wip);
			if (wipdetails != null && !wipdetails.isEmpty())
				previousYearActualRepo.deleteAll(wipdetails);
			// 🔁 WIP
			List<Object[]> wipSums = previousYearActualRepo.getMonthWiseSumForWIP(orgId, clientCode, year);
			for (Object[] row : wipSums) {
				saveStockEntry(orgId, clientCode, client, year, yearType, createdBy, (String) row[0],
						(BigDecimal) row[1], "Drawing Power", "Drawing Power", "WIP", null, "Db");
			}

			String finishedGoods = "Finished Goods";
			List<PreviousYearActualVO> finishedGoodsdetails = previousYearActualRepo.findOldDetails(orgId, clientCode,
					year, mg, mg, finishedGoods);
			if (finishedGoodsdetails != null && !finishedGoodsdetails.isEmpty())
				previousYearActualRepo.deleteAll(finishedGoodsdetails);
			// 🔁 Finished Goods
			List<Object[]> fgSums = previousYearActualRepo.getMonthWiseSumForFinishedGoods(orgId, clientCode, year,
					Arrays.asList("4010", "4011"));
			for (Object[] row : fgSums) {
				saveStockEntry(orgId, clientCode, client, year, yearType, createdBy, (String) row[0],
						(BigDecimal) row[1], "Drawing Power", "Drawing Power", "Finished Goods", null, "Db");
			}
		}

		response.put("message", "Successfully Saved");
		return response;
	}

	private void saveStockEntry(Long orgId, String clientCode, String client, String year, String yearType,
			String createdBy, String month, BigDecimal amount, String mainGroup, String subGroup, String accountName,
			String accountCode, String natureOfAccount) {
		PreviousYearActualVO vo = new PreviousYearActualVO();
		vo.setOrgId(orgId);
		vo.setClientCode(clientCode);
		vo.setClient(client);
		vo.setYear(year);
		vo.setMonth(month);
		vo.setAmount(amount);
		vo.setMainGroup(mainGroup);
		vo.setSubGroup(subGroup);
		vo.setActive(true);
		vo.setCreatedBy(createdBy);
		vo.setUpdatedBy(createdBy);
		vo.setAccountName(accountName);
		vo.setAccountCode(accountCode);
		vo.setNatureOfAccount(natureOfAccount);

		int quarter = quaterMonthService.getQuaterMonthDetails(yearType, month);
		int monthSeq = quaterMonthService.getMonthNumber(yearType, month);
		vo.setQuater(String.valueOf(quarter));
		vo.setMonthsequence(monthSeq);

		previousYearActualRepo.save(vo);
	}

	@Override
	public List<Map<String, Object>> getPreviousYearGroupLedgersDetails(Long orgId, String year, String clientCode,
			String mainGroup, String subGroupCode) {

		Set<Object[]> subGroupDetails = groupMappingRepo.PreviousYearGroupLedgersDetails(orgId, year, clientCode,
				mainGroup, subGroupCode);
		return getGroupLedgerDetails(subGroupDetails);
	}

	@Override
	public List<Map<String, Object>> getActualGroupLedgersDetails(Long orgId, String year, String clientCode,
			String mainGroup, String subGroupCode) {

		Set<Object[]> subGroupDetails = groupMappingRepo.ActualGroupLedgersDetails(orgId, year, clientCode, mainGroup,
				subGroupCode);
		return getGroupLedgerDetails(subGroupDetails);
	}

	@Override
	public Map<String, Object> createUpdateBudgetOB(List<OrderBookingDTO> orderBookingDTO) {
		OrderBookingVO budgetVO = new OrderBookingVO();
		for (OrderBookingDTO budgetDTO2 : orderBookingDTO) {
			List<OrderBookingVO> ordDetails = orderBookingRepo.getBudgetListDetails(budgetDTO2.getOrgId(),
					budgetDTO2.getClientCode(), budgetDTO2.getYear(), budgetDTO2.getType());
			orderBookingRepo.deleteAll(ordDetails);
		}
		for (OrderBookingDTO budgetDTO2 : orderBookingDTO) {

			budgetVO = new OrderBookingVO();
			budgetVO.setOrgId(budgetDTO2.getOrgId());
			budgetVO.setClient(budgetDTO2.getClient());
			budgetVO.setClientCode(budgetDTO2.getClientCode());
			budgetVO.setCreatedBy(budgetDTO2.getCreatedBy());
			budgetVO.setUpdatedBy(budgetDTO2.getCreatedBy());
			budgetVO.setYear(budgetDTO2.getYear()); // Extract year
			budgetVO.setMonth(budgetDTO2.getMonth()); // Extract month (first three letters)
			budgetVO.setAmount(budgetDTO2.getAmount());
			budgetVO.setBalanceOrderValue(budgetDTO2.getBalanceOrderValue());
			budgetVO.setClassification(budgetDTO2.getClassification());
			budgetVO.setCustomerName(budgetDTO2.getCustomerName());
			budgetVO.setExistingOrderPlan(budgetDTO2.getExistingOrderPlan());
			budgetVO.setOrderValue(budgetDTO2.getOrderValue());
			budgetVO.setPaymentReceived(budgetDTO2.getPaymentReceived());
			budgetVO.setSegment(budgetDTO2.getSegment());
			budgetVO.setType(budgetDTO2.getType());
			budgetVO.setYetToReceive(budgetDTO2.getYetToReceive());

			budgetVO.setActive(true);

			int quater = quaterMonthService.getQuaterMonthDetails(budgetDTO2.getYearType(), budgetDTO2.getMonth());
			budgetVO.setQuater(String.valueOf(quater));

			int monthseq = quaterMonthService.getMonthNumber(budgetDTO2.getYearType(), budgetDTO2.getMonth());
			budgetVO.setMonthsequence(monthseq);

			orderBookingRepo.save(budgetVO);
		}

		Map<String, Object> response = new HashMap<>();
		response.put("message", "Successfully Saved");
		return response;
	}

	@Override
	public List<Map<String, Object>> getOrderBookingBudgetDetail(Long orgId, String year, String clientCode,
			String type) {

		Set<Object[]> subGroupDetails = orderBookingRepo.getOrderBookingBudDetails(orgId, year, clientCode, type);
		return getOrderBookingBudgetDetails(subGroupDetails);
	}

	private List<Map<String, Object>> getOrderBookingBudgetDetails(Set<Object[]> subGroupDetails) {
		List<Map<String, Object>> subgroup = new ArrayList<>();
		for (Object[] sub : subGroupDetails) {
			Map<String, Object> mp = new HashMap<>();
			mp.put("segment", sub[0] != null ? sub[0].toString() : "");
			mp.put("customerName", sub[1] != null ? sub[1].toString() : "");
			mp.put("orderValue", sub[2] != null ? new BigDecimal(sub[2].toString()) : BigDecimal.ZERO);
			mp.put("balanceOrderValue", sub[3] != null ? new BigDecimal(sub[3].toString()) : BigDecimal.ZERO);
			mp.put("classification", sub[4] != null ? sub[4].toString() : "");
			mp.put("existingOrderPlan", sub[5] != null ? new BigDecimal(sub[5].toString()) : BigDecimal.ZERO);
			mp.put("paymentReceived", sub[6] != null ? new BigDecimal(sub[6].toString()) : BigDecimal.ZERO);
			mp.put("yetToReceived", sub[7] != null ? new BigDecimal(sub[7].toString()) : BigDecimal.ZERO);
			mp.put("month", sub[8] != null ? sub[8].toString() : "");
			mp.put("amount", sub[9] != null ? new BigDecimal(sub[9].toString()) : BigDecimal.ZERO);
			subgroup.add(mp);
		}
		return subgroup;
	}

	@Override
	public Map<String, Object> createUpdatePYActulaOB(List<OrderBookingDTO> orderBookingDTO) {
		PreviousYearActualOBVO budgetVO = new PreviousYearActualOBVO();
		for (OrderBookingDTO budgetDTO2 : orderBookingDTO) {
			List<PreviousYearActualOBVO> ordDetails = previousYearActualOBRepo.getBudgetListDetails(
					budgetDTO2.getOrgId(), budgetDTO2.getClientCode(), budgetDTO2.getYear(), budgetDTO2.getType());
			if (ordDetails != null) {
				previousYearActualOBRepo.deleteAll(ordDetails);
			}
		}
		for (OrderBookingDTO budgetDTO2 : orderBookingDTO) {

			budgetVO = new PreviousYearActualOBVO();
			budgetVO.setOrgId(budgetDTO2.getOrgId());
			budgetVO.setClient(budgetDTO2.getClient());
			budgetVO.setClientCode(budgetDTO2.getClientCode());
			budgetVO.setCreatedBy(budgetDTO2.getCreatedBy());
			budgetVO.setUpdatedBy(budgetDTO2.getCreatedBy());
			budgetVO.setYear(budgetDTO2.getYear()); // Extract year
			budgetVO.setMonth(budgetDTO2.getMonth()); // Extract month (first three letters)
			budgetVO.setAmount(budgetDTO2.getAmount());
			budgetVO.setBalanceOrderValue(budgetDTO2.getBalanceOrderValue());
			budgetVO.setClassification(budgetDTO2.getClassification());
			budgetVO.setCustomerName(budgetDTO2.getCustomerName());
			budgetVO.setExistingOrderPlan(budgetDTO2.getExistingOrderPlan());
			budgetVO.setOrderValue(budgetDTO2.getOrderValue());
			budgetVO.setPaymentReceived(budgetDTO2.getPaymentReceived());
			budgetVO.setSegment(budgetDTO2.getSegment());
			budgetVO.setType(budgetDTO2.getType());
			budgetVO.setYetToReceive(budgetDTO2.getYetToReceive());

			budgetVO.setActive(true);

			int quater = quaterMonthService.getQuaterMonthDetails(budgetDTO2.getYearType(), budgetDTO2.getMonth());
			budgetVO.setQuater(String.valueOf(quater));

			int monthseq = quaterMonthService.getMonthNumber(budgetDTO2.getYearType(), budgetDTO2.getMonth());
			budgetVO.setMonthsequence(monthseq);

			previousYearActualOBRepo.save(budgetVO);
		}

		Map<String, Object> response = new HashMap<>();
		response.put("message", "Successfully Saved");
		return response;
	}

	@Override
	public List<Map<String, Object>> getPYActualOBDetails(Long orgId, String year, String clientCode, String type) {
		Set<Object[]> subGroupDetails = previousYearActualOBRepo.getOrderBookingPYDetails(orgId, year, clientCode,
				type);
		return getOrderBookingPYDetails(subGroupDetails);
	}

	private List<Map<String, Object>> getOrderBookingPYDetails(Set<Object[]> subGroupDetails) {
		List<Map<String, Object>> subgroup = new ArrayList<>();
		for (Object[] sub : subGroupDetails) {
			Map<String, Object> mp = new HashMap<>();
			mp.put("segment", sub[0] != null ? sub[0].toString() : "");
			mp.put("customerName", sub[1] != null ? sub[1].toString() : "");
			mp.put("orderValue", sub[2] != null ? new BigDecimal(sub[2].toString()) : BigDecimal.ZERO);
			mp.put("balanceOrderValue", sub[3] != null ? new BigDecimal(sub[3].toString()) : BigDecimal.ZERO);
			mp.put("classification", sub[4] != null ? sub[4].toString() : "");
			mp.put("existingOrderPlan", sub[5] != null ? new BigDecimal(sub[5].toString()) : BigDecimal.ZERO);
			mp.put("paymentReceived", sub[6] != null ? new BigDecimal(sub[6].toString()) : BigDecimal.ZERO);
			mp.put("yetToReceived", sub[7] != null ? new BigDecimal(sub[7].toString()) : BigDecimal.ZERO);
			mp.put("month", sub[8] != null ? sub[8].toString() : "");
			mp.put("amount", sub[9] != null ? new BigDecimal(sub[9].toString()) : BigDecimal.ZERO);
			subgroup.add(mp);
		}
		return subgroup;
	}

	@Override
	public List<Map<String, Object>> getBudgetDetailsAutomatic(Long orgId, String year, String clientCode,
			String mainGroup, String clientYear) {

		Set<Object[]> subGroupDetails = new HashSet<Object[]>();
		String previousYear = null;
		if (clientYear.equals("FY")) {
			FinancialYearVO financialYearVO = financialYearRepo.findByOrgIdAndFinYearIdentifierAndYearType(orgId, year,
					clientYear);
			int preYear = financialYearVO.getFinYear() - 1;
			FinancialYearVO financialYearVO2 = financialYearRepo.findByOrgIdAndFinYearAndYearType(orgId, preYear,
					clientYear);
			previousYear = financialYearVO2.getFinYearIdentifier();

		} else if (clientYear.equals("CY")) {
			FinancialYearVO financialYearVO = financialYearRepo.findByOrgIdAndFinYearIdentifierAndYearType(orgId, year,
					clientYear);
			int preYear = financialYearVO.getFinYear() - 1;
			FinancialYearVO financialYearVO2 = financialYearRepo.findByOrgIdAndFinYearAndYearType(orgId, preYear,
					clientYear);
			previousYear = financialYearVO2.getFinYearIdentifier();
		}
		if (mainGroup.equals("Profit And Loss")) {
			subGroupDetails = budgetRepo.getProfitAndLossBudgetDetailsAuto(orgId, year, clientCode, mainGroup,
					clientYear, previousYear);
		} else {
			subGroupDetails = budgetRepo.getBudgetDetailsAuto(orgId, year, clientCode, mainGroup);
		}

		return getBudgetAuto(subGroupDetails);
	}

	private List<Map<String, Object>> getBudgetAuto(Set<Object[]> subGroupDetails) {
		List<Map<String, Object>> subgroup = new ArrayList<>();
		for (Object[] sub : subGroupDetails) {
			Map<String, Object> mp = new HashMap<>();
			mp.put("subGroupCode", sub[0] != null ? sub[0].toString() : "");
			mp.put("subGroupName", sub[1] != null ? sub[1].toString() : "");
			mp.put("natureOfAccount", null);
			mp.put("month", sub[2]);
			mp.put("amount", sub[3] != null ? new BigDecimal(sub[3].toString()) : BigDecimal.ZERO);
			subgroup.add(mp);
		}
		return subgroup;
	}

	@Override
	public List<Map<String, Object>> getPYDetailsAutomatic(Long orgId, String year, String clientCode,
			String mainGroup) {

		Set<Object[]> subGroupDetails = budgetRepo.getPYDetailsAuto(orgId, year, clientCode, mainGroup);
		return getPYAuto(subGroupDetails);
	}

	private List<Map<String, Object>> getPYAuto(Set<Object[]> subGroupDetails) {
		List<Map<String, Object>> subgroup = new ArrayList<>();
		for (Object[] sub : subGroupDetails) {
			Map<String, Object> mp = new HashMap<>();
			mp.put("subGroupCode", sub[0] != null ? sub[0].toString() : "");
			mp.put("subGroupName", sub[1] != null ? sub[1].toString() : "");
			mp.put("natureOfAccount", null);
			mp.put("month", sub[2]);
			mp.put("amount", sub[3] != null ? new BigDecimal(sub[3].toString()) : BigDecimal.ZERO);
			subgroup.add(mp);
		}
		return subgroup;
	}

	@Override
	public Map<String, Object> createUpdateBudgetHeadCount(List<BudgetHeadCountDTO> budgetHeadCountDTO) {

		BudgetHeadCountVO budgetVO = new BudgetHeadCountVO();
		Long org = null;
		String clientcode = null;
		String yr = null;
		for (BudgetHeadCountDTO budgetDTO2 : budgetHeadCountDTO) {
			org = budgetDTO2.getOrgId();
			clientcode = budgetDTO2.getClientCode();
			yr = budgetDTO2.getYear();
		}
		List<BudgetHeadCountVO> vo = budgetHeadCountRepo.getClientBudgetDls(org, clientcode, yr);
		if (vo != null) {
			budgetHeadCountRepo.deleteAll(vo);
		}

		for (BudgetHeadCountDTO budgetDTO2 : budgetHeadCountDTO) {

			budgetVO = new BudgetHeadCountVO();
			budgetVO.setOrgId(budgetDTO2.getOrgId());
			budgetVO.setClient(budgetDTO2.getClient());
			budgetVO.setClientCode(budgetDTO2.getClientCode());
			budgetVO.setCreatedBy(budgetDTO2.getCreatedBy());
			budgetVO.setUpdatedBy(budgetDTO2.getCreatedBy());
			budgetVO.setYear(budgetDTO2.getYear()); // Extract year
			budgetVO.setMonth(budgetDTO2.getMonth()); // Extract month (first three letters)
			budgetVO.setDepartment(budgetDTO2.getDepartment());
			budgetVO.setEmploymentType(budgetDTO2.getEmploymentType());
			budgetVO.setCategory(budgetDTO2.getCategory());
			budgetVO.setHeadcount(budgetDTO2.getHeadcount());
			budgetVO.setCtc(budgetDTO2.getCtc());
			budgetVO.setActive(true);

			int quater = quaterMonthService.getQuaterMonthDetails(budgetDTO2.getYearType(), budgetDTO2.getMonth());
			budgetVO.setQuater(String.valueOf(quater));

			int monthseq = quaterMonthService.getMonthNumber(budgetDTO2.getYearType(), budgetDTO2.getMonth());
			budgetVO.setMonthsequence(monthseq);

			budgetHeadCountRepo.save(budgetVO);
		}

		Map<String, Object> response = new HashMap<>();
		response.put("message", "Successfully Saved");
		return response;
	}

	@Override
	public List<Map<String, Object>> getGroupLedgersDetailsForHeadCount(Long orgId, String year, String clientCode) {
		Set<Object[]> Details = budgetHeadCountRepo.getDetails(orgId, year, clientCode);
		return getDetails(Details);
	}

	private List<Map<String, Object>> getDetails(Set<Object[]> Details) {
		List<Map<String, Object>> subgroup = new ArrayList<>();
		for (Object[] sub : Details) {
			Map<String, Object> mp = new HashMap<>();
			mp.put("department", sub[0] != null ? sub[0].toString() : "");
			mp.put("employmentType", sub[1] != null ? sub[1].toString() : "");
			mp.put("category", sub[2] != null ? sub[2].toString() : "");
			mp.put("month", sub[3] != null ? sub[3].toString() : "");
			mp.put("headCount", sub[4] != null ? Integer.parseInt(sub[4].toString()) : 0);
			mp.put("ctc", sub[5] != null ? new BigDecimal(sub[5].toString()) : BigDecimal.ZERO);
			subgroup.add(mp);
		}
		return subgroup;
	}

	@Override
	public Map<String, Object> createUpdatePreviousYearHeadCount(List<PyHeadCountDTO> pyHeadCountDTO) {

		PYHeadCountVO budgetVO = new PYHeadCountVO();
		Long org = null;
		String clientcode = null;
		String yr = null;
		for (PyHeadCountDTO budgetDTO2 : pyHeadCountDTO) {
			org = budgetDTO2.getOrgId();
			clientcode = budgetDTO2.getClientCode();
			yr = budgetDTO2.getYear();
		}
		List<PYHeadCountVO> vo = pyHeadCountRepo.getClientBudgetDls(org, clientcode, yr);

		if (vo != null) {
			pyHeadCountRepo.deleteAll(vo);
		}

		for (PyHeadCountDTO budgetDTO2 : pyHeadCountDTO) {

			budgetVO = new PYHeadCountVO();
			budgetVO.setOrgId(budgetDTO2.getOrgId());
			budgetVO.setClient(budgetDTO2.getClient());
			budgetVO.setClientCode(budgetDTO2.getClientCode());
			budgetVO.setCreatedBy(budgetDTO2.getCreatedBy());
			budgetVO.setUpdatedBy(budgetDTO2.getCreatedBy());
			budgetVO.setYear(budgetDTO2.getYear()); // Extract year
			budgetVO.setMonth(budgetDTO2.getMonth()); // Extract month (first three letters)
			budgetVO.setDepartment(budgetDTO2.getDepartment());
			budgetVO.setEmploymentType(budgetDTO2.getEmploymentType());
			budgetVO.setCategory(budgetDTO2.getCategory());
			budgetVO.setHeadcount(budgetDTO2.getHeadcount());
			budgetVO.setCtc(budgetDTO2.getCtc());
			budgetVO.setActive(true);

			int quater = quaterMonthService.getQuaterMonthDetails(budgetDTO2.getYearType(), budgetDTO2.getMonth());
			budgetVO.setQuater(String.valueOf(quater));

			int monthseq = quaterMonthService.getMonthNumber(budgetDTO2.getYearType(), budgetDTO2.getMonth());
			budgetVO.setMonthsequence(monthseq);

			pyHeadCountRepo.save(budgetVO);
		}

		Map<String, Object> response = new HashMap<>();
		response.put("message", "Successfully Saved");
		return response;
	}

	@Override
	public Map<String, Object> createUpdateBudgetAccountPayable(List<BudgetACPDTO> budgetACPDTO) {

		BudgetACPVO budgetVO = new BudgetACPVO();
		Long org = null;
		String clientcode = null;
		String yr = null;
		String month = null;
		String type = null;
		for (BudgetACPDTO budgetDTO2 : budgetACPDTO) {
			org = budgetDTO2.getOrgId();
			clientcode = budgetDTO2.getClientCode();
			yr = budgetDTO2.getYear();
			month = budgetDTO2.getMonth();
			type = budgetDTO2.getType();
		}
		List<BudgetACPVO> vo = budgetACPRepo.getClientBudgetDls(org, clientcode, yr, month, type);
		if (vo != null) {
			budgetACPRepo.deleteAll(vo);
		}

		for (BudgetACPDTO budgetDTO2 : budgetACPDTO) {

			budgetVO = new BudgetACPVO();
			budgetVO.setOrgId(budgetDTO2.getOrgId());
			budgetVO.setClient(budgetDTO2.getClient());
			budgetVO.setClientCode(budgetDTO2.getClientCode());
			budgetVO.setCreatedBy(budgetDTO2.getCreatedBy());
			budgetVO.setUpdatedBy(budgetDTO2.getCreatedBy());
			budgetVO.setYear(budgetDTO2.getYear()); // Extract year
			budgetVO.setMonth(budgetDTO2.getMonth()); // Extract month (first three letters)
			budgetVO.setSupplier(budgetDTO2.getSupplier());
			budgetVO.setPaymentTerms(budgetDTO2.getPaymentTerms());
			budgetVO.setGrossPurchase(budgetDTO2.getGrossPurchase());
			budgetVO.setNoOfMonthPurchase(budgetDTO2.getNoOfMonthPurchase());
			budgetVO.setOutStanding(budgetDTO2.getOutStanding());
			budgetVO.setPaymentPeriod(budgetDTO2.getPaymentPeriod());
			budgetVO.setSlab1(budgetDTO2.getSlab1());
			budgetVO.setSlab2(budgetDTO2.getSlab2());
			budgetVO.setSlab3(budgetDTO2.getSlab3());
			budgetVO.setSlab4(budgetDTO2.getSlab4());
			budgetVO.setSlab5(budgetDTO2.getSlab5());
			budgetVO.setSlab6(budgetDTO2.getSlab6());
			budgetVO.setType(budgetDTO2.getType());
			budgetVO.setActive(true);

			int quater = quaterMonthService.getQuaterMonthDetails(budgetDTO2.getYearType(), budgetDTO2.getMonth());
			budgetVO.setQuater(String.valueOf(quater));

			int monthseq = quaterMonthService.getMonthNumber(budgetDTO2.getYearType(), budgetDTO2.getMonth());
			budgetVO.setMonthsequence(monthseq);

			budgetACPRepo.save(budgetVO);
		}

		Map<String, Object> response = new HashMap<>();
		response.put("message", "Successfully Saved");
		return response;
	}

	@Override
	public List<Map<String, Object>> getBudgetACPDetails(Long orgId, String year, String month, String clientCode,
			String type) {
		Set<Object[]> Details = budgetACPRepo.getBudgetACPDetails(orgId, year, month, clientCode, type);
		return getACPDetails(Details);
	}

	private List<Map<String, Object>> getACPDetails(Set<Object[]> Details) {
		List<Map<String, Object>> subgroup = new ArrayList<>();
		for (Object[] sub : Details) {
			Map<String, Object> mp = new HashMap<>();
			mp.put("supplier", sub[0] != null ? sub[0].toString() : "");
			mp.put("paymentTerms", sub[1] != null ? sub[1].toString() : "");
			mp.put("grossPurchase", sub[2] != null ? new BigDecimal(sub[2].toString()) : BigDecimal.ZERO);
			mp.put("noOfMonthPurhcase", sub[3] != null ? Integer.parseInt(sub[3].toString()) : 0);
			mp.put("outStanding", sub[4] != null ? new BigDecimal(sub[4].toString()) : BigDecimal.ZERO);
			mp.put("paymentPeriod", sub[5] != null ? Integer.parseInt(sub[5].toString()) : 0);
			mp.put("slab1", sub[6] != null ? new BigDecimal(sub[6].toString()) : BigDecimal.ZERO);
			mp.put("slab2", sub[7] != null ? new BigDecimal(sub[7].toString()) : BigDecimal.ZERO);
			mp.put("slab3", sub[8] != null ? new BigDecimal(sub[8].toString()) : BigDecimal.ZERO);
			mp.put("slab4", sub[9] != null ? new BigDecimal(sub[9].toString()) : BigDecimal.ZERO);
			mp.put("slab5", sub[10] != null ? new BigDecimal(sub[10].toString()) : BigDecimal.ZERO);
			mp.put("slab6", sub[11] != null ? new BigDecimal(sub[11].toString()) : BigDecimal.ZERO);
			subgroup.add(mp);
		}
		return subgroup;
	}

	@Override
	public Map<String, Object> createUpdateBudgetUnitWise(List<BudgetUnitWiseDTO> budgetUnitWiseDTO) {
		BudgetUnitWiseVO budgetVO = new BudgetUnitWiseVO();
		String maingroup = null;
		String subgroup = null;
		Long org = null;
		String clientcode = null;
		String yr = null;
		String unit = null;
		for (BudgetUnitWiseDTO budgetDTO2 : budgetUnitWiseDTO) {
			maingroup = budgetDTO2.getMainGroup();

			GroupLedgersVO groupLedgersVO = groupLedgersRepo.findByOrgIdAndAccountNameAndMainGroupName(
					budgetDTO2.getOrgId(), budgetDTO2.getAccountName(), budgetDTO2.getMainGroup());
			subgroup = groupLedgersVO.getGroupName();
			org = budgetDTO2.getOrgId();
			unit = budgetDTO2.getUnit();
			clientcode = budgetDTO2.getClientCode();
			yr = budgetDTO2.getYear();

		}

		List<BudgetUnitWiseVO> vo = budgetUnitWiseRepo.getBudgetDls(org, clientcode, yr, unit);
		if (vo != null) {
			budgetUnitWiseRepo.deleteAll(vo);
		}

		for (BudgetUnitWiseDTO budgetDTO2 : budgetUnitWiseDTO) {

			if (!budgetDTO2.getAmount().equals(BigDecimal.ZERO)) {
				budgetVO = new BudgetUnitWiseVO();
				budgetVO.setOrgId(budgetDTO2.getOrgId());
				budgetVO.setClient(budgetDTO2.getClient());
				budgetVO.setClientCode(budgetDTO2.getClientCode());
				budgetVO.setCreatedBy(budgetDTO2.getCreatedBy());
				budgetVO.setUpdatedBy(budgetDTO2.getCreatedBy());
				budgetVO.setAccountName(budgetDTO2.getAccountName());
				budgetVO.setAccountCode(budgetDTO2.getAccountCode());
				budgetVO.setUnit(unit);
				budgetVO.setNatureOfAccount(budgetDTO2.getNatureOfAccount());
				budgetVO.setYear(budgetDTO2.getYear()); // Extract year
				budgetVO.setMonth(budgetDTO2.getMonth()); // Extract month (first three letters)
				budgetVO.setAmount(budgetDTO2.getAmount());
				budgetVO.setMainGroup(budgetDTO2.getMainGroup());

				GroupLedgersVO groupLedgersVO = groupLedgersRepo.findByOrgIdAndAccountNameAndMainGroupName(
						budgetDTO2.getOrgId(), budgetDTO2.getAccountName(), budgetDTO2.getMainGroup());

				budgetVO.setSubGroupCode(budgetDTO2.getSubGroupCode());
				budgetVO.setSubGroup(groupLedgersVO.getGroupName());
				budgetVO.setActive(true);

				int quater = quaterMonthService.getQuaterMonthDetails(budgetDTO2.getYearType(), budgetDTO2.getMonth());
				budgetVO.setQuater(String.valueOf(quater));

				int monthseq = quaterMonthService.getMonthNumber(budgetDTO2.getYearType(), budgetDTO2.getMonth());
				budgetVO.setMonthsequence(monthseq);

				budgetUnitWiseRepo.save(budgetVO);
			}
		}

		Map<String, Object> response = new HashMap<>();
		response.put("message", "Successfully Saved");
		return response;
	}

	@Override
	public List<Map<String, Object>> getUnitDetails(Long orgId, String clientCode) {
		Set<Object[]> Details = budgetACPRepo.getUnitDetails(orgId, clientCode);
		return getUnitDetails(Details);
	}

	private List<Map<String, Object>> getUnitDetails(Set<Object[]> Details) {
		List<Map<String, Object>> subgroup = new ArrayList<>();
		for (Object[] sub : Details) {
			Map<String, Object> mp = new HashMap<>();
			mp.put("supplier", sub[0] != null ? sub[0].toString() : "");
			subgroup.add(mp);
		}
		return subgroup;
	}

	@Override
	public List<Map<String, Object>> getSegmentDetails(Long orgId, String clientCode, String segmentType) {
		Set<Object[]> Details = budgetUnitWiseRepo.getSegmentDetails(orgId, clientCode, segmentType);
		return getSegmentDetails(Details);
	}

	private List<Map<String, Object>> getSegmentDetails(Set<Object[]> Details) {
		List<Map<String, Object>> subgroup = new ArrayList<>();
		for (Object[] sub : Details) {
			Map<String, Object> mp = new HashMap<>();
			mp.put("segmentDetails", sub[0] != null ? sub[0].toString() : "");
			subgroup.add(mp);
		}
		return subgroup;
	}

	@Override
	public List<Map<String, Object>> getUnitLedgerDetails(Long orgId, String year, String clientCode, String mainGroup,
			String accountCode, String unit) {
		Set<Object[]> Details = budgetUnitWiseRepo.getUnitWiseLedgersDetails(orgId, year, clientCode, mainGroup,
				accountCode, unit);
		return getUnitLedgerDetails(Details);
	}

	private List<Map<String, Object>> getUnitLedgerDetails(Set<Object[]> Details) {
		List<Map<String, Object>> subgroup = new ArrayList<>();
		for (Object[] sub : Details) {
			Map<String, Object> mp = new HashMap<>();
			mp.put("subGroupName", sub[0] != null ? sub[0].toString() : "");
			mp.put("subGroupCode", sub[1] != null ? sub[1].toString() : "");
			mp.put("natureOfAccount", sub[2] != null ? sub[2].toString() : "");
			mp.put("month", sub[3] != null ? sub[3].toString() : "");
			mp.put("amount", sub[4] != null ? new BigDecimal(sub[4].toString()) : BigDecimal.ZERO);
			subgroup.add(mp);
		}
		return subgroup;
	}

	@Override
	public List<Map<String, Object>> getGroupLedgersDetailsPYForHeadCount(Long orgId, String year, String clientCode) {
		Set<Object[]> Details = pyHeadCountRepo.getPYHCDetails(orgId, year, clientCode);
		return getDetails(Details);
	}

	@Override
	public Map<String, Object> createUpdatePYAccountPayable(List<BudgetACPDTO> budgetACPDTO) {

		PreviousYearARAPVO budgetVO = new PreviousYearARAPVO();
		Long org = null;
		String clientcode = null;
		String yr = null;
		String month = null;
		String type = null;
		for (BudgetACPDTO budgetDTO2 : budgetACPDTO) {
			org = budgetDTO2.getOrgId();
			clientcode = budgetDTO2.getClientCode();
			yr = budgetDTO2.getYear();
			month = budgetDTO2.getMonth();
			type = budgetDTO2.getType();
		}
		List<PreviousYearARAPVO> vo = previousYearARAPRepo.getClientPYDetails(org, clientcode, yr, month, type);
		if (vo != null) {
			previousYearARAPRepo.deleteAll(vo);
		}

		for (BudgetACPDTO budgetDTO2 : budgetACPDTO) {

			budgetVO = new PreviousYearARAPVO();
			budgetVO.setOrgId(budgetDTO2.getOrgId());
			budgetVO.setClient(budgetDTO2.getClient());
			budgetVO.setClientCode(budgetDTO2.getClientCode());
			budgetVO.setCreatedBy(budgetDTO2.getCreatedBy());
			budgetVO.setUpdatedBy(budgetDTO2.getCreatedBy());
			budgetVO.setYear(budgetDTO2.getYear()); // Extract year
			budgetVO.setMonth(budgetDTO2.getMonth()); // Extract month (first three letters)
			budgetVO.setSupplier(budgetDTO2.getSupplier());
			budgetVO.setPaymentTerms(budgetDTO2.getPaymentTerms());
			budgetVO.setGrossPurchase(budgetDTO2.getGrossPurchase());
			budgetVO.setNoOfMonthPurchase(budgetDTO2.getNoOfMonthPurchase());
			budgetVO.setOutStanding(budgetDTO2.getOutStanding());
			budgetVO.setPaymentPeriod(budgetDTO2.getPaymentPeriod());
			budgetVO.setSlab1(budgetDTO2.getSlab1());
			budgetVO.setSlab2(budgetDTO2.getSlab2());
			budgetVO.setSlab3(budgetDTO2.getSlab3());
			budgetVO.setSlab4(budgetDTO2.getSlab4());
			budgetVO.setSlab5(budgetDTO2.getSlab5());
			budgetVO.setSlab6(budgetDTO2.getSlab6());
			budgetVO.setType(budgetDTO2.getType());
			budgetVO.setActive(true);

			int quater = quaterMonthService.getQuaterMonthDetails(budgetDTO2.getYearType(), budgetDTO2.getMonth());
			budgetVO.setQuater(String.valueOf(quater));

			int monthseq = quaterMonthService.getMonthNumber(budgetDTO2.getYearType(), budgetDTO2.getMonth());
			budgetVO.setMonthsequence(monthseq);

			previousYearARAPRepo.save(budgetVO);
		}

		Map<String, Object> response = new HashMap<>();
		response.put("message", "Successfully Saved");
		return response;
	}

	@Override
	public List<Map<String, Object>> getPYACPDetails(Long orgId, String year, String month, String clientCode,
			String type) {
		Set<Object[]> Details = previousYearARAPRepo.getPYARAPDetails(orgId, year, month, clientCode, type);
		return getACPDetails(Details);
	}

	@Override
	public Map<String, Object> createUpdatePYUnitWise(List<BudgetUnitWiseDTO> budgetUnitWiseDTO) {
		PreviousYearUnitwiseVO budgetVO = new PreviousYearUnitwiseVO();
		String maingroup = null;
		String subgroup = null;
		Long org = null;
		String clientcode = null;
		String yr = null;
		String unit = null;
		for (BudgetUnitWiseDTO budgetDTO2 : budgetUnitWiseDTO) {
			maingroup = budgetDTO2.getMainGroup();

			GroupLedgersVO groupLedgersVO = groupLedgersRepo.findByOrgIdAndAccountNameAndMainGroupName(
					budgetDTO2.getOrgId(), budgetDTO2.getAccountName(), budgetDTO2.getMainGroup());
			subgroup = groupLedgersVO.getGroupName();
			org = budgetDTO2.getOrgId();
			unit = budgetDTO2.getUnit();
			clientcode = budgetDTO2.getClientCode();
			yr = budgetDTO2.getYear();

		}

		List<PreviousYearUnitwiseVO> vo = previousYearUnitwiseRepo.getPYUnitDetails(org, clientcode, yr, unit);
		if (vo != null) {
			previousYearUnitwiseRepo.deleteAll(vo);
		}

		for (BudgetUnitWiseDTO budgetDTO2 : budgetUnitWiseDTO) {

			if (!budgetDTO2.getAmount().equals(BigDecimal.ZERO)) {
				budgetVO = new PreviousYearUnitwiseVO();
				budgetVO.setOrgId(budgetDTO2.getOrgId());
				budgetVO.setClient(budgetDTO2.getClient());
				budgetVO.setClientCode(budgetDTO2.getClientCode());
				budgetVO.setCreatedBy(budgetDTO2.getCreatedBy());
				budgetVO.setUpdatedBy(budgetDTO2.getCreatedBy());
				budgetVO.setAccountName(budgetDTO2.getAccountName());
				budgetVO.setAccountCode(budgetDTO2.getAccountCode());
				budgetVO.setUnit(unit);
				budgetVO.setNatureOfAccount(budgetDTO2.getNatureOfAccount());
				budgetVO.setYear(budgetDTO2.getYear()); // Extract year
				budgetVO.setMonth(budgetDTO2.getMonth()); // Extract month (first three letters)
				budgetVO.setAmount(budgetDTO2.getAmount());
				budgetVO.setMainGroup(budgetDTO2.getMainGroup());

				GroupLedgersVO groupLedgersVO = groupLedgersRepo.findByOrgIdAndAccountNameAndMainGroupName(
						budgetDTO2.getOrgId(), budgetDTO2.getAccountName(), budgetDTO2.getMainGroup());

				budgetVO.setSubGroupCode(budgetDTO2.getSubGroupCode());
				budgetVO.setSubGroup(groupLedgersVO.getGroupName());
				budgetVO.setActive(true);

				int quater = quaterMonthService.getQuaterMonthDetails(budgetDTO2.getYearType(), budgetDTO2.getMonth());
				budgetVO.setQuater(String.valueOf(quater));

				int monthseq = quaterMonthService.getMonthNumber(budgetDTO2.getYearType(), budgetDTO2.getMonth());
				budgetVO.setMonthsequence(monthseq);

				previousYearUnitwiseRepo.save(budgetVO);
			}
		}

		Map<String, Object> response = new HashMap<>();
		response.put("message", "Successfully Saved");
		return response;
	}

	@Override
	public List<Map<String, Object>> getPYUnitLedgerDetails(Long orgId, String year, String clientCode,
			String mainGroup, String accountCode, String unit) {
		Set<Object[]> Details = previousYearUnitwiseRepo.getPYUnitWiseLedgersDetails(orgId, year, clientCode, mainGroup,
				accountCode, unit);
		return getUnitLedgerDetails(Details);
	}

	@Override
	public List<Map<String, Object>> getRatioAnalysisBudgetGroupLedgersDetails(Long orgId, String year,
			String clientCode, String mainGroup, String subGroupCode) {

		Set<Object[]> subGroupDetails = groupMappingRepo.getRatioAnalysisBudgetGroupLedgersDetails(orgId, year,
				clientCode, mainGroup, subGroupCode);
		return getRatioAnalysisGroupLedgerDetails(subGroupDetails);
	}

	private List<Map<String, Object>> getRatioAnalysisGroupLedgerDetails(Set<Object[]> subGroupDetails) {
		List<Map<String, Object>> subgroup = new ArrayList<>();
		for (Object[] sub : subGroupDetails) {
			Map<String, Object> mp = new HashMap<>();
			mp.put("subGroupName", sub[0] != null ? sub[0].toString() : "");
			mp.put("subGroupCode", sub[1] != null ? sub[1].toString() : "");
			mp.put("natureOfAccount", sub[2] != null ? sub[2].toString() : "");
			mp.put("month", sub[3] != null ? sub[3].toString() : "");
			mp.put("amount", sub[4] != null ? new BigDecimal(sub[4].toString()) : BigDecimal.ZERO);
			subgroup.add(mp);
		}
		return subgroup;
	}

	@Override
	public Map<String, Object> createUpdateBudgetRatioAnalysis(List<BudgetRatioAnalysisDTO> budgetRatioAnalysisDTO) {
		BudgetVO budgetVO = new BudgetVO();
		String maingroup = null;
		String subgroup = null;
		Long org = null;
		String clientcode = null;
		String yr = null;
		for (BudgetRatioAnalysisDTO budgetDTO2 : budgetRatioAnalysisDTO) {
			maingroup = budgetDTO2.getMainGroup();
			subgroup = budgetDTO2.getSubGroup();
			org = budgetDTO2.getOrgId();
			clientcode = budgetDTO2.getClientCode();
			yr = budgetDTO2.getYear();

			List<BudgetVO> vo = budgetRepo.getClientBudgetDls(org, clientcode, yr, maingroup, subgroup);
			if (vo != null) {
				budgetRepo.deleteAll(vo);
			}
		}

		for (BudgetRatioAnalysisDTO budgetDTO2 : budgetRatioAnalysisDTO) {

			if (!budgetDTO2.getAmount().equals(BigDecimal.ZERO)) {
				budgetVO = new BudgetVO();
				budgetVO.setOrgId(budgetDTO2.getOrgId());
				budgetVO.setClient(budgetDTO2.getClient());
				budgetVO.setClientCode(budgetDTO2.getClientCode());
				budgetVO.setCreatedBy(budgetDTO2.getCreatedBy());
				budgetVO.setUpdatedBy(budgetDTO2.getCreatedBy());
				budgetVO.setAccountName(budgetDTO2.getAccountName());
				budgetVO.setYear(budgetDTO2.getYear()); // Extract year
				budgetVO.setMonth(budgetDTO2.getMonth()); // Extract month (first three letters)
				budgetVO.setAmount(budgetDTO2.getAmount());
				budgetVO.setMainGroup(budgetDTO2.getMainGroup());

				budgetVO.setSubGroup(budgetDTO2.getSubGroup());
				budgetVO.setActive(true);

				int quater = quaterMonthService.getQuaterMonthDetails(budgetDTO2.getYearType(), budgetDTO2.getMonth());
				budgetVO.setQuater(String.valueOf(quater));

				int monthseq = quaterMonthService.getMonthNumber(budgetDTO2.getYearType(), budgetDTO2.getMonth());
				budgetVO.setMonthsequence(monthseq);

				budgetRepo.save(budgetVO);
			}
		}

		Map<String, Object> response = new HashMap<>();
		response.put("message", "Successfully Saved");
		return response;
	}

	@Override
	public List<Map<String, Object>> getRatioAnalysisPYGroupLedgersDetails(Long orgId, String year, String clientCode,
			String mainGroup, String subGroupCode) {
		Set<Object[]> subGroupDetails = groupMappingRepo.getRatioAnalysisPYGroupLedgersDetails(orgId, year, clientCode,
				mainGroup, subGroupCode);
		return getRatioAnalysisPYGroupLedgerDetails(subGroupDetails);
	}

	private List<Map<String, Object>> getRatioAnalysisPYGroupLedgerDetails(Set<Object[]> subGroupDetails) {
		List<Map<String, Object>> subgroup = new ArrayList<>();
		for (Object[] sub : subGroupDetails) {
			Map<String, Object> mp = new HashMap<>();
			mp.put("subGroupName", sub[0] != null ? sub[0].toString() : "");
			mp.put("subGroupCode", sub[1] != null ? sub[1].toString() : "");
			mp.put("natureOfAccount", sub[2] != null ? sub[2].toString() : "");
			mp.put("month", sub[3] != null ? sub[3].toString() : "");
			mp.put("amount", sub[4] != null ? new BigDecimal(sub[4].toString()) : BigDecimal.ZERO);
			subgroup.add(mp);
		}
		return subgroup;
	}

	@Override
	public Map<String, Object> createUpdatePYRatioAnalysis(List<BudgetRatioAnalysisDTO> budgetRatioAnalysisDTO) {
		PreviousYearActualVO budgetVO = new PreviousYearActualVO();
		String maingroup = null;
		String subgroup = null;
		Long org = null;
		String clientcode = null;
		String yr = null;
		for (BudgetRatioAnalysisDTO budgetDTO2 : budgetRatioAnalysisDTO) {
			maingroup = budgetDTO2.getMainGroup();
			subgroup = budgetDTO2.getSubGroup();
			org = budgetDTO2.getOrgId();
			clientcode = budgetDTO2.getClientCode();
			yr = budgetDTO2.getYear();

			List<PreviousYearActualVO> vo = previousYearActualRepo.getClientBudgetDls(org, clientcode, yr, maingroup,
					subgroup);
			if (vo != null) {
				previousYearActualRepo.deleteAll(vo);
			}
		}

		for (BudgetRatioAnalysisDTO budgetDTO2 : budgetRatioAnalysisDTO) {

			if (!budgetDTO2.getAmount().equals(BigDecimal.ZERO)) {
				budgetVO = new PreviousYearActualVO();
				budgetVO.setOrgId(budgetDTO2.getOrgId());
				budgetVO.setClient(budgetDTO2.getClient());
				budgetVO.setClientCode(budgetDTO2.getClientCode());
				budgetVO.setCreatedBy(budgetDTO2.getCreatedBy());
				budgetVO.setUpdatedBy(budgetDTO2.getCreatedBy());
				budgetVO.setAccountName(budgetDTO2.getAccountName());
				budgetVO.setYear(budgetDTO2.getYear()); // Extract year
				budgetVO.setMonth(budgetDTO2.getMonth()); // Extract month (first three letters)
				budgetVO.setAmount(budgetDTO2.getAmount());
				budgetVO.setMainGroup(budgetDTO2.getMainGroup());

				budgetVO.setSubGroup(budgetDTO2.getSubGroup());
				budgetVO.setActive(true);

				int quater = quaterMonthService.getQuaterMonthDetails(budgetDTO2.getYearType(), budgetDTO2.getMonth());
				budgetVO.setQuater(String.valueOf(quater));

				int monthseq = quaterMonthService.getMonthNumber(budgetDTO2.getYearType(), budgetDTO2.getMonth());
				budgetVO.setMonthsequence(monthseq);

				previousYearActualRepo.save(budgetVO);
			}
		}

		Map<String, Object> response = new HashMap<>();
		response.put("message", "Successfully Saved");
		return response;
	}

	@Override
	public List<Map<String, Object>> getLedgerDetailsForPL(Long orgId, String mainGroupName) {
		Set<Object[]> subGroupDetails = groupMappingRepo.getLedgerDetailsForPL(orgId, mainGroupName);
		return getLedgerDetailsForPL(subGroupDetails);
	}

	private List<Map<String, Object>> getLedgerDetailsForPL(Set<Object[]> subGroupDetails) {
		List<Map<String, Object>> subgroup = new ArrayList<>();
		for (Object[] sub : subGroupDetails) {
			Map<String, Object> mp = new HashMap<>();
			mp.put("subGroupName", sub[1] != null ? sub[1].toString() : "");
			subgroup.add(mp);
		}
		return subgroup;
	}

	@Override
	public List<Map<String, Object>> getSubGroupDetailsForPL(Long orgId, String mainGroupName) {
		Set<Object[]> subGroupDetails = groupMappingRepo.getSubGroupDetailsForPL(orgId, mainGroupName);
		return getSubGroupDetailsForPL(subGroupDetails);
	}

	private List<Map<String, Object>> getSubGroupDetailsForPL(Set<Object[]> subGroupDetails) {
		List<Map<String, Object>> subgroup = new ArrayList<>();
		for (Object[] sub : subGroupDetails) {
			Map<String, Object> mp = new HashMap<>();
			mp.put("subGroupName", sub[0] != null ? sub[0].toString() : "");
			subgroup.add(mp);
		}
		return subgroup;
	}

	@Override
	public List<Map<String, Object>> getLedgerDetailsForSubGroupPL(Long orgId, String mainGroupName,
			String subGroupName) {
		Set<Object[]> subGroupDetails = groupMappingRepo.getLedgerDetailsForSubGroupPL(orgId, mainGroupName,
				subGroupName);
		return getLedgerDetailsForSubGroupPL(subGroupDetails);
	}

	private List<Map<String, Object>> getLedgerDetailsForSubGroupPL(Set<Object[]> subGroupDetails) {
		List<Map<String, Object>> subgroup = new ArrayList<>();
		for (Object[] sub : subGroupDetails) {
			Map<String, Object> mp = new HashMap<>();
			mp.put("accountName", sub[0] != null ? sub[0].toString() : "");
			mp.put("accountCode", sub[1] != null ? sub[1].toString() : "");
			mp.put("mainGroup", sub[2] != null ? sub[2].toString() : "");
			mp.put("subGroup", sub[3] != null ? sub[3].toString() : "");
			subgroup.add(mp);
		}
		return subgroup;
	}

	@Override
	public Map<String, Object> createUpdateIncrementalProfitBudget(List<IncrementalProfitDTO> budgetDTO) {
		BudgetIncrementalProfitVO budgetVO = new BudgetIncrementalProfitVO();
		Long org = null;
		String clientcode = null;
		String yr = null;
		String subGroup = null;
		for (IncrementalProfitDTO budgetDTO2 : budgetDTO) {

			GroupLedgersVO groupLedgersVO = new GroupLedgersVO();

			try {
				groupLedgersVO = groupLedgersRepo.findByOrgIdAndAccountNameAndMainGroupName(budgetDTO2.getOrgId(),
						budgetDTO2.getAccountName(), budgetDTO2.getMainGroup());
			} catch (Exception e) {
				System.out.println(budgetDTO2.getAccountName() + budgetDTO2.getMainGroup());
			}
			subGroup = groupLedgersVO.getGroupName();

			org = budgetDTO2.getOrgId();
			clientcode = budgetDTO2.getClientCode();
			yr = budgetDTO2.getYear();

			List<BudgetIncrementalProfitVO> vo = budgetIncrementalProfitRepo.getClientBudgetDls(org, clientcode, yr,
					subGroup);
			if (vo != null) {
				budgetIncrementalProfitRepo.deleteAll(vo);
			}
		}

		for (IncrementalProfitDTO budgetDTO2 : budgetDTO) {

			if (!budgetDTO2.getAmount().equals(BigDecimal.ZERO)) {
				budgetVO = new BudgetIncrementalProfitVO();
				budgetVO.setOrgId(budgetDTO2.getOrgId());
				budgetVO.setClient(budgetDTO2.getClient());
				budgetVO.setClientCode(budgetDTO2.getClientCode());
				budgetVO.setCreatedBy(budgetDTO2.getCreatedBy());
				budgetVO.setUpdatedBy(budgetDTO2.getCreatedBy());
				budgetVO.setAccountName(budgetDTO2.getAccountName());
				budgetVO.setAccountCode(budgetDTO2.getAccountCode());
				budgetVO.setYear(budgetDTO2.getYear()); // Extract year
				budgetVO.setMonth(budgetDTO2.getMonth()); // Extract month (first three letters)
				budgetVO.setAmount(budgetDTO2.getAmount());
				budgetVO.setActive(true);
				GroupLedgersVO gr = new GroupLedgersVO();

				try {
					gr = groupLedgersRepo.findByOrgIdAndAccountNameAndMainGroupName(budgetDTO2.getOrgId(),
							budgetDTO2.getAccountName(), budgetDTO2.getMainGroup());
				} catch (Exception e) {
					System.out.println(budgetDTO2.getAccountName() + budgetDTO2.getMainGroup());
				}

				budgetVO.setSubGroup(gr.getGroupName());
				budgetIncrementalProfitRepo.save(budgetVO);
			}
		}

		Map<String, Object> response = new HashMap<>();
		response.put("message", "Successfully Saved");
		return response;
	}

	@Override
	public Map<String, Object> createUpdateIncrementalProfitPY(List<IncrementalProfitDTO> budgetDTO) {
		PreviousYearIncrementalProfitVO budgetVO = new PreviousYearIncrementalProfitVO();
		Long org = null;
		String clientcode = null;
		String yr = null;
		String subGroup = null;
		for (IncrementalProfitDTO budgetDTO2 : budgetDTO) {

			GroupLedgersVO groupLedgersVO = new GroupLedgersVO();

			try {
				groupLedgersVO = groupLedgersRepo.findByOrgIdAndAccountNameAndMainGroupName(budgetDTO2.getOrgId(),
						budgetDTO2.getAccountName(), budgetDTO2.getMainGroup());
			} catch (Exception e) {
				System.out.println(budgetDTO2.getAccountName() + budgetDTO2.getMainGroup());
			}
			subGroup = groupLedgersVO.getGroupName();

			org = budgetDTO2.getOrgId();
			clientcode = budgetDTO2.getClientCode();
			yr = budgetDTO2.getYear();

			List<PreviousYearIncrementalProfitVO> vo = previousYearIncrementalProfitRepo.getClientBudgetDls(org,
					clientcode, yr, subGroup);
			if (vo != null) {
				previousYearIncrementalProfitRepo.deleteAll(vo);
			}
		}

		for (IncrementalProfitDTO budgetDTO2 : budgetDTO) {

			if (!budgetDTO2.getAmount().equals(BigDecimal.ZERO)) {
				budgetVO = new PreviousYearIncrementalProfitVO();
				budgetVO.setOrgId(budgetDTO2.getOrgId());
				budgetVO.setClient(budgetDTO2.getClient());
				budgetVO.setClientCode(budgetDTO2.getClientCode());
				budgetVO.setCreatedBy(budgetDTO2.getCreatedBy());
				budgetVO.setUpdatedBy(budgetDTO2.getCreatedBy());
				budgetVO.setAccountName(budgetDTO2.getAccountName());
				budgetVO.setAccountCode(budgetDTO2.getAccountCode());
				budgetVO.setYear(budgetDTO2.getYear()); // Extract year
				budgetVO.setMonth(budgetDTO2.getMonth()); // Extract month (first three letters)
				budgetVO.setAmount(budgetDTO2.getAmount());
				budgetVO.setActive(true);
				GroupLedgersVO gr = new GroupLedgersVO();

				try {
					gr = groupLedgersRepo.findByOrgIdAndAccountNameAndMainGroupName(budgetDTO2.getOrgId(),
							budgetDTO2.getAccountName(), budgetDTO2.getMainGroup());
				} catch (Exception e) {
					System.out.println(budgetDTO2.getAccountName() + budgetDTO2.getMainGroup());
				}

				budgetVO.setSubGroup(gr.getGroupName());
				previousYearIncrementalProfitRepo.save(budgetVO);
			}
		}

		Map<String, Object> response = new HashMap<>();
		response.put("message", "Successfully Saved");
		return response;
	}

	@Override
	public List<Map<String, Object>> getBudgetIncrementalGroupLedgersDetails(Long orgId, String year, String clientCode,
			String mainGroup, String subGroup) {

		Set<Object[]> particularDetails = groupMappingRepo.getBudgetIncrementalLedgersDetails(orgId, year, clientCode,
				mainGroup, subGroup);
		return getIncrementalProfitLedgerDetails(particularDetails);
	}

	private List<Map<String, Object>> getIncrementalProfitLedgerDetails(Set<Object[]> subGroupDetails) {
		List<Map<String, Object>> subgroup = new ArrayList<>();
		for (Object[] sub : subGroupDetails) {
			Map<String, Object> mp = new HashMap<>();
			mp.put("accountCode", sub[0] != null ? sub[0].toString() : null);
			mp.put("accountName", sub[1] != null ? sub[1].toString() : "");
			mp.put("amount", sub[2] != null ? new BigDecimal(sub[2].toString()) : BigDecimal.ZERO);
			subgroup.add(mp);
		}
		return subgroup;
	}

	@Override
	public List<Map<String, Object>> getPYIncrementalGroupLedgersDetails(Long orgId, String year, String clientCode,
			String mainGroup, String subGroup) {

		Set<Object[]> particularDetails = groupMappingRepo.getPYIncrementalLedgersDetails(orgId, year, clientCode,
				mainGroup, subGroup);
		return getIncrementalProfitLedgerDetails(particularDetails);
	}

	@Override
	public Map<String, Object> createUpdateAdvancePaymentPY(
			List<PyAdvancePaymentReceiptDTO> pyAdvancePaymentReceiptDTO) {
		PyAdvancePaymentReceiptVO budgetVO = new PyAdvancePaymentReceiptVO();
		Long org = null;
		String clientcode = null;
		String yr = null;
		String type = null;
		for (PyAdvancePaymentReceiptDTO budgetDTO2 : pyAdvancePaymentReceiptDTO) {
			org = budgetDTO2.getOrgId();
			clientcode = budgetDTO2.getClientCode();
			yr = budgetDTO2.getYear();
			type = budgetDTO2.getType();

			List<PyAdvancePaymentReceiptVO> vo = pyAdvancePaymentReceiptRepo.getClientBudgetDls(org, clientcode, yr,
					type);
			if (vo != null) {
				pyAdvancePaymentReceiptRepo.deleteAll(vo);
			}
		}

		for (PyAdvancePaymentReceiptDTO budgetDTO2 : pyAdvancePaymentReceiptDTO) {

			if (!budgetDTO2.getAmount().equals(BigDecimal.ZERO)) {
				budgetVO = new PyAdvancePaymentReceiptVO();
				budgetVO.setOrgId(budgetDTO2.getOrgId());
				budgetVO.setClient(budgetDTO2.getClient());
				budgetVO.setClientCode(budgetDTO2.getClientCode());
				budgetVO.setCreatedBy(budgetDTO2.getCreatedBy());
				budgetVO.setUpdatedBy(budgetDTO2.getCreatedBy());
				budgetVO.setType(budgetDTO2.getType());
				budgetVO.setParty(budgetDTO2.getParty());
				;
				budgetVO.setYear(budgetDTO2.getYear()); // Extract year
				budgetVO.setMonth(budgetDTO2.getMonth()); // Extract month (first three letters)
				budgetVO.setAmount(budgetDTO2.getAmount());
				budgetVO.setActive(true);

				int quater = quaterMonthService.getQuaterMonthDetails(budgetDTO2.getYearType(), budgetDTO2.getMonth());
				budgetVO.setQuater(String.valueOf(quater));

				int monthseq = quaterMonthService.getMonthNumber(budgetDTO2.getYearType(), budgetDTO2.getMonth());
				budgetVO.setMonthsequence(monthseq);

				pyAdvancePaymentReceiptRepo.save(budgetVO);
			}
		}

		Map<String, Object> response = new HashMap<>();
		response.put("message", "Successfully Saved");
		return response;

	}

	@Override
	public List<Map<String, Object>> getAdvancePaymentReceiptDetails(Long orgId, String year, String clientCode,
			String type) {

		Set<Object[]> paymentReceiptDetails = pyAdvancePaymentReceiptRepo.getPaymentReceiptDetails(orgId, year,
				clientCode, type);
		return getPRDetails(paymentReceiptDetails);
	}

	private List<Map<String, Object>> getPRDetails(Set<Object[]> paymentReceiptDetails) {
		List<Map<String, Object>> subgroup = new ArrayList<>();
		for (Object[] sub : paymentReceiptDetails) {
			Map<String, Object> mp = new HashMap<>();
			mp.put("party", sub[0] != null ? sub[0].toString() : null);
			mp.put("month", sub[1] != null ? sub[1].toString() : "");
			mp.put("amount", sub[2] != null ? new BigDecimal(sub[2].toString()) : BigDecimal.ZERO);
			subgroup.add(mp);
		}
		return subgroup;
	}

	@Override
	public List<BudgetLoansOutStandingVO> BudgetLoanOutStandingLedger(Long orgId, String year, String clientCode) {

		return budgetLoansOutStandingRepo.findByOrgIdAndYearAndClientCode(orgId, year, clientCode);
	}

	@Override
	@Transactional
	public Map<String, Object> createUpdateBudgetLoanOutStanding(List<LoanOutstandingDTO> loanOutstandingDTO) {
		BudgetLoansOutStandingVO budgetVO = new BudgetLoansOutStandingVO();
		Long org = null;
		String clientcode = null;
		String yr = null;
		for (LoanOutstandingDTO budgetDTO2 : loanOutstandingDTO) {
			org = budgetDTO2.getOrgId();
			clientcode = budgetDTO2.getClientCode();
			yr = budgetDTO2.getYear();

			List<BudgetLoansOutStandingVO> vo = budgetLoansOutStandingRepo.getClientBudgetDls(org, clientcode, yr);
			if (vo != null) {
				budgetLoansOutStandingRepo.deleteAll(vo);
			}
		}

		for (LoanOutstandingDTO budgetDTO2 : loanOutstandingDTO) {

			if (!budgetDTO2.getAmount().equals(BigDecimal.ZERO)) {
				budgetVO = new BudgetLoansOutStandingVO();
				budgetVO.setOrgId(budgetDTO2.getOrgId());
				budgetVO.setClient(budgetDTO2.getClient());
				budgetVO.setClientCode(budgetDTO2.getClientCode());
				budgetVO.setCreatedBy(budgetDTO2.getCreatedBy());
				budgetVO.setUpdatedBy(budgetDTO2.getCreatedBy());
				budgetVO.setAccountName(budgetDTO2.getAccountName());
				budgetVO.setAccountCode(budgetDTO2.getAccountCode());
				budgetVO.setYear(budgetDTO2.getYear()); // Extract year
				budgetVO.setMonth(budgetDTO2.getMonth()); // Extract month (first three letters)
				budgetVO.setAmount(budgetDTO2.getAmount());
				budgetVO.setBanckName(budgetDTO2.getBanckName());
				budgetVO.setSanctionAmount(budgetDTO2.getSanctionAmount());
				budgetVO.setOutstanding(budgetDTO2.getOutstanding());
				budgetVO.setInterestRate(budgetDTO2.getInterestRate());
				budgetVO.setTenure(budgetDTO2.getTenure());
				budgetVO.setInterest(budgetDTO2.getInterest());
				budgetVO.setPrincipal(budgetDTO2.getPrincipal());
				budgetVO.setEmiTotal(budgetDTO2.getEmiTotal());
				budgetVO.setTenureDate(budgetDTO2.getTenureDate());
				budgetVO.setBalanceMonth(budgetDTO2.getBalanceMonth());
				budgetVO.setActive(true);

				int quater = quaterMonthService.getQuaterMonthDetails(budgetDTO2.getYearType(), budgetDTO2.getMonth());
				budgetVO.setQuater(String.valueOf(quater));

				int monthseq = quaterMonthService.getMonthNumber(budgetDTO2.getYearType(), budgetDTO2.getMonth());
				budgetVO.setMonthsequence(monthseq);

				budgetLoansOutStandingRepo.save(budgetVO);
			}
		}

		Map<String, Object> response = new HashMap<>();
		response.put("message", "Successfully Saved");
		return response;
	}

	@Override
	@Transactional
	public Map<String, Object> createUpdatePyLoanOutStanding(List<LoanOutstandingDTO> loanOutstandingDTO) {

		PyLoansOutStandingVO budgetVO = new PyLoansOutStandingVO();
		Long org = null;
		String clientcode = null;
		String yr = null;
		for (LoanOutstandingDTO budgetDTO2 : loanOutstandingDTO) {
			org = budgetDTO2.getOrgId();
			clientcode = budgetDTO2.getClientCode();
			yr = budgetDTO2.getYear();

			List<PyLoansOutStandingVO> vo = pyLoansOutStandingRepo.getClientBudgetDls(org, clientcode, yr);
			if (vo != null) {
				pyLoansOutStandingRepo.deleteAll(vo);
			}
		}

		for (LoanOutstandingDTO budgetDTO2 : loanOutstandingDTO) {

			if (!budgetDTO2.getAmount().equals(BigDecimal.ZERO)) {
				budgetVO = new PyLoansOutStandingVO();
				budgetVO.setOrgId(budgetDTO2.getOrgId());
				budgetVO.setClient(budgetDTO2.getClient());
				budgetVO.setClientCode(budgetDTO2.getClientCode());
				budgetVO.setCreatedBy(budgetDTO2.getCreatedBy());
				budgetVO.setUpdatedBy(budgetDTO2.getCreatedBy());
				budgetVO.setAccountName(budgetDTO2.getAccountName());
				budgetVO.setAccountCode(budgetDTO2.getAccountCode());
				budgetVO.setYear(budgetDTO2.getYear()); // Extract year
				budgetVO.setMonth(budgetDTO2.getMonth()); // Extract month (first three letters)
				budgetVO.setAmount(budgetDTO2.getAmount());
				budgetVO.setBanckName(budgetDTO2.getBanckName());
				budgetVO.setSanctionAmount(budgetDTO2.getSanctionAmount());
				budgetVO.setOutstanding(budgetDTO2.getOutstanding());
				budgetVO.setInterestRate(budgetDTO2.getInterestRate());
				budgetVO.setTenure(budgetDTO2.getTenure());
				budgetVO.setInterest(budgetDTO2.getInterest());
				budgetVO.setPrincipal(budgetDTO2.getPrincipal());
				budgetVO.setEmiTotal(budgetDTO2.getEmiTotal());
				budgetVO.setTenureDate(budgetDTO2.getTenureDate());
				budgetVO.setBalanceMonth(budgetDTO2.getBalanceMonth());
				budgetVO.setActive(true);

				int quater = quaterMonthService.getQuaterMonthDetails(budgetDTO2.getYearType(), budgetDTO2.getMonth());
				budgetVO.setQuater(String.valueOf(quater));

				int monthseq = quaterMonthService.getMonthNumber(budgetDTO2.getYearType(), budgetDTO2.getMonth());
				budgetVO.setMonthsequence(monthseq);

				pyLoansOutStandingRepo.save(budgetVO);
			}
		}

		Map<String, Object> response = new HashMap<>();
		response.put("message", "Successfully Saved");
		return response;
	}

	@Override
	public List<PyLoansOutStandingVO> PyLoanOutStandingLedger(Long orgId, String year, String clientCode) {
		// TODO Auto-generated method stub
		return pyLoansOutStandingRepo.findByOrgIdAndYearAndClientCode(orgId, year, clientCode);
	}

	@Override
	@Transactional
	public Map<String, Object> createUpdateBudgetSalesPurchaseAnalysis(List<SalesPurchaseDTO> salesPurchaseDTO) {
		Map<String, Object> response = new HashMap<>();
		if (salesPurchaseDTO == null || salesPurchaseDTO.isEmpty()) {
			response.put("message", "No data to save");
			return response;
		}
		SalesPurchaseDTO firstDto = salesPurchaseDTO.get(0);
		Long orgId = firstDto.getOrgId();
		String clientCode = firstDto.getClientCode();
		String year = firstDto.getYear();
		String type = firstDto.getType();
		String month = firstDto.getMonth();

		// Delete existing records
		List<BudgetSalesPurchaseVO> existing = budgetSalesPurchaseRepo.getClientBudgetSalesPurchaseDtls(orgId,
				clientCode, year, type, month);

		if (existing != null && !existing.isEmpty()) {
			budgetSalesPurchaseRepo.deleteAll(existing);
		}
		for (SalesPurchaseDTO dto : salesPurchaseDTO) {
			if (dto.getAmount() == null || dto.getAmount().compareTo(BigDecimal.ZERO) == 0)
				continue;

			BudgetSalesPurchaseVO vo = new BudgetSalesPurchaseVO();
			vo.setOrgId(dto.getOrgId());
			vo.setClient(dto.getClient());
			vo.setClientCode(dto.getClientCode());
			vo.setCreatedBy(dto.getCreatedBy());
			vo.setUpdatedBy(dto.getCreatedBy());
			vo.setDescription(dto.getDescription());
			vo.setNatureOfAccount(dto.getNatureOfAccount());
			vo.setYear(dto.getYear());
			vo.setMonth(dto.getMonth());
			vo.setAmount(dto.getAmount());
			vo.setType(dto.getType());
			vo.setActive(true);

			int quarter = quaterMonthService.getQuaterMonthDetails(dto.getYearType(), dto.getMonth());
			int monthSeq = quaterMonthService.getMonthNumber(dto.getYearType(), dto.getMonth());

			vo.setQuater(String.valueOf(quarter));
			vo.setMonthsequence(monthSeq);

			budgetSalesPurchaseRepo.save(vo);
		}
		response.put("message", "Successfully Saved");
		return response;
	}

	@Override
	@Transactional
	public Map<String, Object> createUpdatePySalesPurchaseAnalysis(List<SalesPurchaseDTO> salesPurchaseDTO) {
		Map<String, Object> response = new HashMap<>();
		if (salesPurchaseDTO == null || salesPurchaseDTO.isEmpty()) {
			response.put("message", "No data to save");
			return response;
		}
		SalesPurchaseDTO firstDto = salesPurchaseDTO.get(0);
		Long orgId = firstDto.getOrgId();
		String clientCode = firstDto.getClientCode();
		String year = firstDto.getYear();
		String type = firstDto.getType();
		String month = firstDto.getMonth();

		// Delete existing records

		List<PySalesPurchaseVO> existing = pySalesPurchaseRepo.getClientPySalesPurchaseDtls(orgId, clientCode, year,
				type, month);

		if (existing != null && !existing.isEmpty()) {
			pySalesPurchaseRepo.deleteAll(existing);
		}

		for (SalesPurchaseDTO dto : salesPurchaseDTO) {
			if (dto.getAmount() == null || dto.getAmount().compareTo(BigDecimal.ZERO) == 0)
				continue;

			PySalesPurchaseVO vo = new PySalesPurchaseVO();
			vo.setOrgId(dto.getOrgId());
			vo.setClient(dto.getClient());
			vo.setClientCode(dto.getClientCode());
			vo.setCreatedBy(dto.getCreatedBy());
			vo.setUpdatedBy(dto.getCreatedBy());
			vo.setDescription(dto.getDescription());
			vo.setNatureOfAccount(dto.getNatureOfAccount());
			vo.setYear(dto.getYear());
			vo.setMonth(dto.getMonth());
			vo.setAmount(dto.getAmount());
			vo.setType(dto.getType());
			vo.setActive(true);

			int quarter = quaterMonthService.getQuaterMonthDetails(dto.getYearType(), dto.getMonth());
			int monthSeq = quaterMonthService.getMonthNumber(dto.getYearType(), dto.getMonth());

			vo.setQuater(String.valueOf(quarter));
			vo.setMonthsequence(monthSeq);

			pySalesPurchaseRepo.save(vo);
		}
		response.put("message", "Successfully Saved");
		return response;
	}

	@Override
	public Map<String, Object> createUpdateBudgetSalesPurchaseItemAnalysis(
			List<SalesPurchaseItemDTO> salesPurchaseItemDTO) {

		Map<String, Object> response = new HashMap<>();
		if (salesPurchaseItemDTO == null || salesPurchaseItemDTO.isEmpty()) {
			response.put("message", "No data to save");
			return response;
		}
		SalesPurchaseItemDTO firstDto = salesPurchaseItemDTO.get(0);
		Long orgId = firstDto.getOrgId();
		String clientCode = firstDto.getClientCode();
		String year = firstDto.getYear();
		String type = firstDto.getType();
		String month = firstDto.getMonth();

		// Delete existing records

		List<BudgetSalesPurchaseItemVO> existing = budgetSalesPurchaseItemRepo
				.getClientBudgetSalesPurchaseItemDtls(orgId, clientCode, year, type, month);

		if (existing != null && !existing.isEmpty()) {
			budgetSalesPurchaseItemRepo.deleteAll(existing);
		}

		for (SalesPurchaseItemDTO dto : salesPurchaseItemDTO) {
			if (dto.getValue() == null || dto.getValue().compareTo(BigDecimal.ZERO) == 0 && dto.getQty() == null
					|| dto.getQty().compareTo(BigDecimal.ZERO) == 0)
				continue;

			BudgetSalesPurchaseItemVO vo = new BudgetSalesPurchaseItemVO();
			vo.setOrgId(dto.getOrgId());
			vo.setClient(dto.getClient());
			vo.setClientCode(dto.getClientCode());
			vo.setCreatedBy(dto.getCreatedBy());
			vo.setUpdatedBy(dto.getCreatedBy());
			vo.setDescription(dto.getDescription());
			vo.setYear(dto.getYear());
			vo.setMonth(dto.getMonth());
			vo.setValue(dto.getValue());
			vo.setQty(dto.getQty());
			vo.setType(dto.getType());
			vo.setActive(true);

			int quarter = quaterMonthService.getQuaterMonthDetails(dto.getYearType(), dto.getMonth());
			int monthSeq = quaterMonthService.getMonthNumber(dto.getYearType(), dto.getMonth());

			vo.setQuater(String.valueOf(quarter));
			vo.setMonthsequence(monthSeq);

			budgetSalesPurchaseItemRepo.save(vo);
		}
		response.put("message", "Successfully Saved");
		return response;
	}

	@Override
	public Map<String, Object> createUpdatePySalesPurchaseItemAnalysis(
			List<SalesPurchaseItemDTO> salesPurchaseItemDTO) {
		Map<String, Object> response = new HashMap<>();
		if (salesPurchaseItemDTO == null || salesPurchaseItemDTO.isEmpty()) {
			response.put("message", "No data to save");
			return response;
		}
		SalesPurchaseItemDTO firstDto = salesPurchaseItemDTO.get(0);
		Long orgId = firstDto.getOrgId();
		String clientCode = firstDto.getClientCode();
		String year = firstDto.getYear();
		String type = firstDto.getType();
		String month = firstDto.getMonth();

		// Delete existing records

		List<PySalesPurchaseItemVO> existing = pySalesPurchaseItemRepo.getClientPySalesPurchaseItemDtls(orgId,
				clientCode, year, type, month);

		if (existing != null && !existing.isEmpty()) {
			pySalesPurchaseItemRepo.deleteAll(existing);
		}

		for (SalesPurchaseItemDTO dto : salesPurchaseItemDTO) {
			if (dto.getValue() == null || dto.getValue().compareTo(BigDecimal.ZERO) == 0 && dto.getQty() == null
					|| dto.getQty().compareTo(BigDecimal.ZERO) == 0)
				continue;

			PySalesPurchaseItemVO vo = new PySalesPurchaseItemVO();
			vo.setOrgId(dto.getOrgId());
			vo.setClient(dto.getClient());
			vo.setClientCode(dto.getClientCode());
			vo.setCreatedBy(dto.getCreatedBy());
			vo.setUpdatedBy(dto.getCreatedBy());
			vo.setDescription(dto.getDescription());
			vo.setYear(dto.getYear());
			vo.setMonth(dto.getMonth());
			vo.setValue(dto.getValue());
			vo.setQty(dto.getQty());
			vo.setType(dto.getType());
			vo.setActive(true);

			int quarter = quaterMonthService.getQuaterMonthDetails(dto.getYearType(), dto.getMonth());
			int monthSeq = quaterMonthService.getMonthNumber(dto.getYearType(), dto.getMonth());

			vo.setQuater(String.valueOf(quarter));
			vo.setMonthsequence(monthSeq);

			pySalesPurchaseItemRepo.save(vo);
		}
		response.put("message", "Successfully Saved");
		return response;
	}

	@Override
	public List<Map<String, Object>> getBudgetSalesPurchaseDetails(Long orgId, String finYear, String clientCode,
			String type) {

		Set<Object[]> subGroupDetails = budgetSalesPurchaseRepo.getBudgetDetails(orgId, finYear, clientCode, type);
		return getBudgetDetails(subGroupDetails);
	}

	private List<Map<String, Object>> getBudgetDetails(Set<Object[]> subGroupDetails) {
		List<Map<String, Object>> subgroup = new ArrayList<>();
		for (Object[] sub : subGroupDetails) {
			Map<String, Object> mp = new HashMap<>();
			mp.put("description", sub[0] != null ? sub[0].toString() : "");
			mp.put("month", sub[1] != null ? sub[1].toString() : "");
			mp.put("amount", sub[2] != null ? new BigDecimal(sub[2].toString()) : BigDecimal.ZERO);
			mp.put("natureOfAccount", sub[3] != null ? sub[3].toString() : "");
			subgroup.add(mp);
		}
		return subgroup;
	}

	@Override
	public List<Map<String, Object>> getPySalesPurchaseDetails(Long orgId, String finYear, String clientCode,
			String type) {

		Set<Object[]> subGroupDetails = pySalesPurchaseRepo.getPyDetails(orgId, finYear, clientCode, type);
		return getBudgetDetails(subGroupDetails);
	}

	@Override
	public List<Map<String, Object>> getBudgetSalesPurchaseItemDetails(Long orgId, String finYear, String clientCode,
			String type) {

		Set<Object[]> subGroupDetails = budgetSalesPurchaseItemRepo.getBudgetItemDetails(orgId, finYear, clientCode,
				type);
		return getBudgetItemDetails(subGroupDetails);
	}

	private List<Map<String, Object>> getBudgetItemDetails(Set<Object[]> subGroupDetails) {
		List<Map<String, Object>> subgroup = new ArrayList<>();
		for (Object[] sub : subGroupDetails) {
			Map<String, Object> mp = new HashMap<>();
			mp.put("description", sub[0] != null ? sub[0].toString() : "");
			mp.put("month", sub[1] != null ? sub[1].toString() : "");
			mp.put("qty", sub[2] != null ? new BigDecimal(sub[2].toString()) : BigDecimal.ZERO);
			mp.put("value", sub[3] != null ? new BigDecimal(sub[3].toString()) : BigDecimal.ZERO);
			subgroup.add(mp);
		}
		return subgroup;
	}

	@Override
	public List<Map<String, Object>> getPySalesPurchaseItemDetails(Long orgId, String finYear, String clientCode,
			String type) {

		Set<Object[]> subGroupDetails = pySalesPurchaseItemRepo.getPyItemDetails(orgId, finYear, clientCode, type);
		return getBudgetItemDetails(subGroupDetails);
	}

	@Override
	public List<Map<String, Object>> getActualIncrementalGroupLedgersDetails(Long orgId, String year, String clientCode,
			String mainGroup, String subGroup, String month) {
		Set<Object[]> particularDetails = groupMappingRepo.getActualIncrementalLedgersDetails(orgId, year, clientCode,
				mainGroup, subGroup, month);
		return getIncrementalProfitLedgerDetails(particularDetails);
	}
}
