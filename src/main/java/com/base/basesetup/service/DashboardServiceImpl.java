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

@Service
public class DashboardServiceImpl implements DashboardService {

	@Autowired
	ReceiptRepo receiptRepo;
	
	@Autowired
	PaymentRepo paymentRepo;
	
	@Autowired
	CostInvoiceRepo costInvoiceRepo;

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
		
			map.put("receiptAmt", (ch != null && ch.length > 0 && ch[0] != null) 
                    ? new BigDecimal(ch[0].toString()) 
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
		
			map.put("paymentAmt", (ch != null && ch.length > 0 && ch[0] != null) 
                    ? new BigDecimal(ch[0].toString()) 
                    : BigDecimal.ZERO);

			List1.add(map);
		}
		return List1;

	}

	@Override
	public List<Map<String, Object>> getTdsSummary(Long orgId, String month, String finYear) {
		Set<Object[]> chType = costInvoiceRepo.getTdsSummary(orgId, month,finYear);
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
		     

			List1.add(map);
		}
		return List1;

	}
}
