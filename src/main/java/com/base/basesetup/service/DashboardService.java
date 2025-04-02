package com.base.basesetup.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface DashboardService {

	List<Map<String, Object>> getReceiptAmont(Long orgId, String month, String year);

	List<Map<String, Object>> getPaymentAmont(Long orgId, String month, String year);

	List<Map<String, Object>> getTdsSummary(Long orgId, String month, String finYear);

	List<Map<String, Object>> getPercentageDiffFromRevenue(Long orgId,String finYear,String choose);

}
