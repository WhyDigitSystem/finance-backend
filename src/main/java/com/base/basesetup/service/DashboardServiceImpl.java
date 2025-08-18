package com.base.basesetup.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.basesetup.repo.CostInvoiceRepo;
import com.base.basesetup.repo.PaymentRepo;
import com.base.basesetup.repo.ReceiptRepo;
import com.base.basesetup.repo.TaxInvoiceRepo;

@Service
public class DashboardServiceImpl implements DashboardService {

	@Autowired
	ReceiptRepo receiptRepo;

	@Autowired
	PaymentRepo paymentRepo;

	@Autowired
	CostInvoiceRepo costInvoiceRepo;

	@Autowired
	TaxInvoiceRepo taxInvoiceRepo;

	public static final Logger LOGGER = LoggerFactory.getLogger(DashboardServiceImpl.class);

	@Override
	public List<Map<String, Object>> getReceiptAmont(Long orgId, String month, String year) {
		Set<Object[]> chType = receiptRepo.getReceiptAmont(orgId, month, year);
		return getReceiptAmt(chType);
	}

	private List<Map<String, Object>> getReceiptAmt(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();

			map.put("receiptAmt", (ch != null && ch.length > 0 && ch[0] != null) ? new BigDecimal(ch[0].toString())
					: BigDecimal.ZERO);

			List1.add(map);
		}
		return List1;

	}

	@Override
	public List<Map<String, Object>> getPaymentAmont(Long orgId, String month, String year) {
		Set<Object[]> chType = paymentRepo.getPaymentAmont(orgId, month, year);
		return getPaymentAmt(chType);
	}

	private List<Map<String, Object>> getPaymentAmt(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();

			map.put("paymentAmt", (ch != null && ch.length > 0 && ch[0] != null) ? new BigDecimal(ch[0].toString())
					: BigDecimal.ZERO);

			List1.add(map);
		}
		return List1;

	}

	@Override
	public List<Map<String, Object>> getTdsSummary(Long orgId, String month, Long finYear) {
		Set<Object[]> chType = costInvoiceRepo.getTdsSummary(orgId, month, finYear);
		return getTds(chType);
	}

	private List<Map<String, Object>> getTds(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();

			map.put("supplierName", ch[0] != null ? ch[0].toString() : "");
			map.put("tdsAmount", ch[1] != null ? new BigDecimal(ch[1].toString()) : BigDecimal.ZERO);
			map.put("tds4", ch[2] != null ? new BigDecimal(ch[2].toString()) : BigDecimal.ZERO);
			map.put("tds9", ch[3] != null ? new BigDecimal(ch[3].toString()) : BigDecimal.ZERO);
			map.put("tds10", ch[4] != null ? new BigDecimal(ch[4].toString()) : BigDecimal.ZERO);
			map.put("shortName", ch[6] != null ? ch[6].toString() : "");

			List1.add(map);
		}
		return List1;

	}

	@Override
	public List<Map<String, Object>> getPercentageDiffFromRevenue(Long orgId, Long finYear, String Month, String Year,
			String branchCode) {
		Set<Object[]> chType = taxInvoiceRepo.getPercentageDiffFromRevenue(orgId, finYear, Month, Year, branchCode);
		return getPercentage(chType);
	}

	private List<Map<String, Object>> getPercentage(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();

			map.put("curMonth", ch[0] != null ? new BigDecimal(ch[0].toString()) : BigDecimal.ZERO);
			map.put("preMonth", ch[1] != null ? new BigDecimal(ch[1].toString()) : BigDecimal.ZERO);
			map.put("curYear", ch[2] != null ? new BigDecimal(ch[2].toString()) : BigDecimal.ZERO);
			map.put("preYear", ch[3] != null ? new BigDecimal(ch[3].toString()) : BigDecimal.ZERO);

			List1.add(map);
		}
		return List1;

	}

	@Override
	public List<Map<String, Object>> getPercentageDiffFromYear(Long orgId, Long finYear) {
		Set<Object[]> chType = taxInvoiceRepo.getPercentageDiffFromYear(orgId, finYear);
		return getPercentage1(chType);
	}

	private List<Map<String, Object>> getPercentage1(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();

			map.put("curyear", ch[0] != null ? new BigDecimal(ch[0].toString()) : BigDecimal.ZERO);
			map.put("preyear", ch[1] != null ? new BigDecimal(ch[1].toString()) : BigDecimal.ZERO);

			List1.add(map);
		}
		return List1;

	}

	@Override
	public List<Map<String, Object>> getPercentageDiffFromCost(Long orgId, Long finYear, String month, String year) {
		Set<Object[]> chType = costInvoiceRepo.getPercentageDiffFromCost(orgId, finYear, month, year);
		return getPercentageCost(chType);
	}

	private List<Map<String, Object>> getPercentageCost(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();

			map.put("curMonth", ch[0] != null ? new BigDecimal(ch[0].toString()) : BigDecimal.ZERO);
			map.put("preMonth", ch[1] != null ? new BigDecimal(ch[1].toString()) : BigDecimal.ZERO);
			map.put("curYear", ch[2] != null ? new BigDecimal(ch[2].toString()) : BigDecimal.ZERO);
			map.put("preYear", ch[3] != null ? new BigDecimal(ch[3].toString()) : BigDecimal.ZERO);

			List1.add(map);
		}
		return List1;

	}

	@Override
	public List<Map<String, Object>> getPercentageFromReceipt(Long orgId, Long finYear, String month) {
		Set<Object[]> chType = costInvoiceRepo.getPercentageFromReceipt(orgId, finYear, month);
		return getPercentageReceipt(chType);
	}

	private List<Map<String, Object>> getPercentageReceipt(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();

			map.put("curYear", ch[0] != null ? new BigDecimal(ch[0].toString()) : BigDecimal.ZERO);
			map.put("preYear", ch[1] != null ? new BigDecimal(ch[1].toString()) : BigDecimal.ZERO);
			map.put("curMonth", ch[2] != null ? new BigDecimal(ch[2].toString()) : BigDecimal.ZERO);
			map.put("preMonth", ch[3] != null ? new BigDecimal(ch[3].toString()) : BigDecimal.ZERO);

			List1.add(map);
		}
		return List1;

	}

	@Override
	public List<Map<String, Object>> getPercentageFromPayment(Long orgId, Long finYear, String month) {
		Set<Object[]> chType = taxInvoiceRepo.getPercentageFromPayment(orgId, finYear, month);
		return getPercentagePayment(chType);
	}

	private List<Map<String, Object>> getPercentagePayment(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();

			map.put("curYear", ch[0] != null ? new BigDecimal(ch[0].toString()) : BigDecimal.ZERO);
			map.put("preYear", ch[1] != null ? new BigDecimal(ch[1].toString()) : BigDecimal.ZERO);
			map.put("curMonth", ch[2] != null ? new BigDecimal(ch[2].toString()) : BigDecimal.ZERO);
			map.put("preMonth", ch[3] != null ? new BigDecimal(ch[3].toString()) : BigDecimal.ZERO);

			List1.add(map);
		}
		return List1;

	}

	@Override
	public List<Map<String, Object>> getSalesMonthWiseData(Long orgId, Long finYear, String branchCode) {
		Set<Object[]> chType = taxInvoiceRepo.getSalesMonthWiseData(orgId, finYear, branchCode);
		return getSalesMonthWise(chType);
	}

	private List<Map<String, Object>> getSalesMonthWise(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();

			map.put("partyName", ch[0] != null ? ch[0].toString() : "");
			map.put("partyShortName", ch[1] != null ? ch[1].toString() : "");
			map.put("amount", ch[2] != null ? new BigDecimal(ch[2].toString()) : BigDecimal.ZERO);
			map.put("month", ch[3] != null ? ((Number) ch[3]).intValue() : 0);

			List1.add(map);
		}
		return List1;

	}

	@Override
	public List<Map<String, Object>> getTotaltdsFromCustomer(Long orgId, Long finYear, String branchCode) {
		Set<Object[]> chType = costInvoiceRepo.getTotaltdsFromCustomer(orgId, finYear, branchCode);
		return getTotaltds(chType);
	}

	private List<Map<String, Object>> getTotaltds(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();

			map.put("partyName", ch[0] != null ? ch[0].toString() : "");
			map.put("partyShortName", ch[1] != null ? ch[1].toString() : "");
			map.put("totalTds", ch[2] != null ? new BigDecimal(ch[2].toString()) : BigDecimal.ZERO);
			map.put("finYear", ch[3] != null ? ch[1].toString() : "");

			List1.add(map);
		}
		return List1;

	}

	@Override
	public List<Map<String, Object>> getTotaltdsFromCustomerBillWise(Long orgId, Long finYear, String branchCode,
			String partyName) {
		Set<Object[]> chType = costInvoiceRepo.getTotaltdsFromCustomerBillWise(orgId, finYear, branchCode, partyName);
		return getTotaltdsFromCustomer(chType);
	}

	private List<Map<String, Object>> getTotaltdsFromCustomer(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();

			map.put("docId", ch[0] != null ? ch[0].toString() : "");
			map.put("docDate", ch[1] != null ? ch[1].toString() : "");
			map.put("partyName", ch[2] != null ? ch[2].toString() : "");
			map.put("shortName", ch[3] != null ? ch[3].toString() : "");
			map.put("tdsAmt", ch[4] != null ? new BigDecimal(ch[4].toString()) : BigDecimal.ZERO);
			map.put("finYear", ch[5] != null ? ch[5].toString() : "");

			List1.add(map);
		}
		return List1;

	}

	@Override
	public List<Map<String, Object>> getRevenueMonthWiseRevenue(Long orgId, Long finYear, String monthName) {
		Set<Object[]> chType = taxInvoiceRepo.getRevenueMonthWiseRevenue(orgId, finYear, monthName);
		return getRevenueMonthWiseRevenue(chType);
	}

	private List<Map<String, Object>> getRevenueMonthWiseRevenue(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();

			map.put("monthnumber", ch[0] != null ? ch[0].toString() : "");
			map.put("monthname", ch[1] != null ? ch[1].toString() : "");
			map.put("totalamount", ch[2] != null ? new BigDecimal(ch[2].toString()) : BigDecimal.ZERO);

			List1.add(map);
		}
		return List1;
	}

	@Override
	public List<Map<String, Object>> getRevenueMonthWiseCost(Long orgId, Long finYear, String monthName) {
		Set<Object[]> chType = taxInvoiceRepo.getRevenueMonthWiseCost(orgId, finYear, monthName);
		return getRevenueMonthWiseCost(chType);
	}

	private List<Map<String, Object>> getRevenueMonthWiseCost(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();

			map.put("monthnumber", ch[0] != null ? ch[0].toString() : "");
			map.put("monthname", ch[1] != null ? ch[1].toString() : "");
			map.put("totalamount", ch[2] != null ? new BigDecimal(ch[2].toString()) : BigDecimal.ZERO);

			List1.add(map);
		}
		return List1;
	}

	@Override
	public List<Map<String, Object>> getRevenueMonthWiseRecepit(Long orgId, Long finYear, String monthName) {
		Set<Object[]> chType = taxInvoiceRepo.getRevenueMonthWiseRecepit(orgId, finYear, monthName);
		return getRevenueMonthWiseRecepit(chType);
	}

	private List<Map<String, Object>> getRevenueMonthWiseRecepit(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();

			map.put("monthnumber", ch[0] != null ? ch[0].toString() : "");
			map.put("monthname", ch[1] != null ? ch[1].toString() : "");
			map.put("totalamount", ch[2] != null ? new BigDecimal(ch[2].toString()) : BigDecimal.ZERO);

			List1.add(map);
		}
		return List1;
	}

	@Override
	public List<Map<String, Object>> getRevenueMonthWisePayment(Long orgId, Long finYear, String monthName) {
		Set<Object[]> chType = taxInvoiceRepo.getRevenueMonthWisePayment(orgId, finYear, monthName);
		return getRevenueMonthWisePayment(chType);
	}

	private List<Map<String, Object>> getRevenueMonthWisePayment(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();

			map.put("monthnumber", ch[0] != null ? ch[0].toString() : "");
			map.put("monthname", ch[1] != null ? ch[1].toString() : "");
			map.put("totalamount", ch[2] != null ? new BigDecimal(ch[2].toString()) : BigDecimal.ZERO);

			List1.add(map);
		}
		return List1;
	}

	@Override
	public List<Map<String, Object>> getTrailBalanceReport(String startDate, String endDate, String branch,String message,String finYear) {
		Set<Object[]> chType = receiptRepo.getTrailBalanceReport(startDate,endDate, branch, message, finYear);
		return getTrailBalanceReport(chType);
	}

	private List<Map<String, Object>> getTrailBalanceReport(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();
			map.put("groupName", ch[0] != null ? ch[0].toString() : "");
			map.put("accountCode", ch[1] != null ? ch[1].toString() : "");
			map.put("subledgerCode", ch[2] != null ? ch[2].toString() : "");
			map.put("subledgerName", ch[3] != null ? ch[3].toString() : "");
			map.put("odbamount", ch[4] != null ? new BigDecimal(ch[4].toString()) : BigDecimal.ZERO);
			map.put("ocramount", ch[5] != null ? new BigDecimal(ch[5].toString()) : BigDecimal.ZERO);
			map.put("tdbamount", ch[6] != null ? new BigDecimal(ch[6].toString()) : BigDecimal.ZERO);
			map.put("tcramount", ch[7] != null ? new BigDecimal(ch[7].toString()) : BigDecimal.ZERO);
			map.put("cdbamount", ch[8] != null ? new BigDecimal(ch[8].toString()) : BigDecimal.ZERO);
			map.put("ccramount", ch[9] != null ? new BigDecimal(ch[9].toString()) : BigDecimal.ZERO);
			List1.add(map);
		}
		return List1;
	}
}
