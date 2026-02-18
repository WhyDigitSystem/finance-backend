package com.base.basesetup.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

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
import com.base.basesetup.entity.BudgetLoansOutStandingVO;
import com.base.basesetup.entity.PyLoansOutStandingVO;

@Service
public interface BudgetService {
	
	List<Map<String,Object>>getSubGroupDetails(Long orgId,String mainGroup);

	
	Map<String,Object>createUpdateBudget(List<BudgetDTO> budgetDTO);
	
	Map<String,Object>createUpdateBudgetOB(List<OrderBookingDTO> orderBookingDTO);

	List<Map<String, Object>> getGroupLedgersDetails(Long orgId, String year, String clientCode, String mainGroup,
			String subGroupCode);


	Map<String, Object> createUpdatePreviousYear(List<PreviousYearDTO> budgetDTO);
	
	


	List<Map<String, Object>> getPreviousYearGroupLedgersDetails(Long orgId, String year, String clientCode,
			String mainGroup, String subGroupCode);


	List<Map<String, Object>> getActualGroupLedgersDetails(Long orgId, String year, String clientCode, String mainGroup,
			String subGroupCode);


	List<Map<String, Object>> getOrderBookingBudgetDetail(Long orgId, String year, String clientCode, String type);


	Map<String, Object> createUpdatePYActulaOB(List<OrderBookingDTO> orderBookingDTO);


	List<Map<String, Object>> getPYActualOBDetails(Long orgId, String year, String clientCode, String type);


	List<Map<String, Object>> getBudgetDetailsAutomatic(Long orgId, String year, String clientCode, String mainGroup,String yearType);
	
	

	
	Map<String,Object>createUpdateBudgetHeadCount(List<BudgetHeadCountDTO> budgetHeadCountDTO);
	

	List<Map<String, Object>> getGroupLedgersDetailsForHeadCount(Long orgId, String year, String clientCode);


	Map<String, Object> createUpdatePreviousYearHeadCount(List<PyHeadCountDTO> pyHeadCountDTO);
	
	List<Map<String, Object>> getGroupLedgersDetailsPYForHeadCount(Long orgId, String year, String clientCode);
	
	Map<String,Object>createUpdateBudgetAccountPayable(List<BudgetACPDTO> budgetACPDTO);


	List<Map<String, Object>> getBudgetACPDetails(Long orgId, String year, String month, String clientCode,String type);
	
	Map<String,Object>createUpdatePYAccountPayable(List<BudgetACPDTO> budgetACPDTO);
	
	List<Map<String, Object>> getPYACPDetails(Long orgId, String year, String month, String clientCode,String type);
	
	
	Map<String,Object>createUpdateBudgetUnitWise(List<BudgetUnitWiseDTO> budgetUnitWiseDTO);


	List<Map<String, Object>> getUnitDetails(Long orgId, String clientCode);



	List<Map<String, Object>> getUnitLedgerDetails(Long orgId, String year, String clientCode, String mainGroup,
			String accountCode, String unit);

	List<Map<String, Object>> getSegmentDetails(Long orgId, String clientCode, String segmentType);
	
	
	Map<String,Object>createUpdatePYUnitWise(List<BudgetUnitWiseDTO> budgetUnitWiseDTO);
	
	List<Map<String, Object>> getPYUnitLedgerDetails(Long orgId, String year, String clientCode, String mainGroup,
			String accountCode, String unit);


	List<Map<String, Object>> getRatioAnalysisPYGroupLedgersDetails(Long orgId, String year, String clientCode,
			String mainGroup, String subGroupCode);


	Map<String, Object> createUpdateBudgetRatioAnalysis(List<BudgetRatioAnalysisDTO> budgetRatioAnalysisDTO);


	List<Map<String, Object>> getRatioAnalysisBudgetGroupLedgersDetails(Long orgId, String year, String clientCode,
			String mainGroup, String subGroupCode);


	Map<String, Object> createUpdatePYRatioAnalysis(List<BudgetRatioAnalysisDTO> budgetRatioAnalysisDTO);


	List<Map<String, Object>> getLedgerDetailsForPL(Long orgId, String mainGroupName);


	List<Map<String, Object>> getSubGroupDetailsForPL(Long orgId, String mainGroupName);



	List<Map<String, Object>> getLedgerDetailsForSubGroupPL(Long orgId, String mainGroupName, String subGroupName);


	List<Map<String, Object>> getPYDetailsAutomatic(Long orgId, String year, String clientCode, String mainGroup);


	Map<String, Object> createUpdateIncrementalProfitBudget(List<IncrementalProfitDTO> budgetDTO);


	Map<String, Object> createUpdateIncrementalProfitPY(List<IncrementalProfitDTO> budgetDTO);


	List<Map<String, Object>> getBudgetIncrementalGroupLedgersDetails(Long orgId, String year, String clientCode,
			String mainGroup,String subGroup);


	List<Map<String, Object>> getPYIncrementalGroupLedgersDetails(Long orgId, String year, String clientCode,
			String mainGroup,String subGroup);
	
	
	// Advance Payment
	
	Map<String, Object> createUpdateAdvancePaymentPY(List<PyAdvancePaymentReceiptDTO> advancePaymentReceiptDTO);


	List<Map<String, Object>> getAdvancePaymentReceiptDetails(Long orgId, String year, String clientCode, String type);

	List<BudgetLoansOutStandingVO> BudgetLoanOutStandingLedger(Long orgId, String year, String clientCode);

	Map<String, Object> createUpdateBudgetLoanOutStanding(List<LoanOutstandingDTO> loanOutstandingDTO);

	Map<String, Object> createUpdatePyLoanOutStanding(List<LoanOutstandingDTO> loanOutstandingDTO);

	List<PyLoansOutStandingVO> PyLoanOutStandingLedger(Long orgId, String year, String clientCode);
	
	
	Map<String, Object> createUpdateBudgetSalesPurchaseAnalysis(List<SalesPurchaseDTO> salesPurchaseDTO);


	Map<String, Object> createUpdatePySalesPurchaseAnalysis(List<SalesPurchaseDTO> salesPurchaseDTO);
	
	Map<String, Object> createUpdateBudgetSalesPurchaseItemAnalysis(List<SalesPurchaseItemDTO> salesPurchaseItemDTO);


	Map<String, Object> createUpdatePySalesPurchaseItemAnalysis(List<SalesPurchaseItemDTO>salesPurchaseItemDTO);


	List<Map<String, Object>> getBudgetSalesPurchaseDetails(Long orgId, String finYear, String clientCode, String type);


	List<Map<String, Object>> getPySalesPurchaseDetails(Long orgId, String finYear, String clientCode, String type);


	List<Map<String, Object>> getBudgetSalesPurchaseItemDetails(Long orgId, String finYear, String clientCode,
			String type);


	List<Map<String, Object>> getPySalesPurchaseItemDetails(Long orgId, String finYear, String clientCode, String type);


	List<Map<String, Object>> getActualIncrementalGroupLedgersDetails(Long orgId, String year, String clientCode,
			String mainGroup, String subGroup, String month);


	
	

}
