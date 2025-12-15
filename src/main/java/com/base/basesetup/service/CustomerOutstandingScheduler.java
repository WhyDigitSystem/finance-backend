package com.base.basesetup.service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.base.basesetup.entity.CustomerOutstandingVO;
import com.base.basesetup.repo.CustomerOutstandingRepo;
import com.base.basesetup.util.AmountUtil;

@Service
public class CustomerOutstandingScheduler {

	@Autowired
	private CustomerOutstandingRepo repository;

	@Autowired
	private EmailServiceAutoImpl mailService;

	// Runs every day at 8 AM
	@Scheduled(cron = "0 33 12 * * ?", zone = "Asia/Kolkata")
	public void sendDailyCreditRiskMail() throws MessagingException {

		List<CustomerOutstandingVO> customers = repository.findCustomersExceeding80Percent();

		if (customers.isEmpty())
			return;

		int totalCustomers = customers.size();
		int c80 = 0, c120 = 0, c150 = 0;
		BigDecimal totalOutstanding = BigDecimal.ZERO;

		for (CustomerOutstandingVO c : customers) {
			int p = c.getOutpercentage();
			if (p >= 80 && p <= 119)
				c80++;
			if (p >= 120 && p <= 149)
				c120++;
			if (p >= 150)
				c150++;
			totalOutstanding = totalOutstanding.add(c.getTotaldue());
		}

		String rows = buildCustomerRows(customers);

		String html = loadOutstandingReportTemplate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")),
				totalCustomers, c80, c120, c150, totalOutstanding, rows);
		mailService.sendCreditRiskMail(html);
	}

	public String loadOutstandingReportTemplate(String reportDate, int totalCustomers, int customers80,
			int customers120, int customers150, BigDecimal totalOutstanding, String customerRows) {

		try {
			ClassPathResource resource = new ClassPathResource("templates/outstandingreport.html");

			String html = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

			return html.replace("${reportDate}", reportDate)
					.replace("${totalCustomers}", String.valueOf(totalCustomers))
					.replace("${customers80}", String.valueOf(customers80))
					.replace("${customers120}", String.valueOf(customers120))
					.replace("${customers150}", String.valueOf(customers150))
					.replace("${totalOutstanding}", AmountUtil.formatInteger(totalOutstanding))
					.replace("${customerRows}", customerRows);

		} catch (Exception e) {
			e.printStackTrace();
			return "<p>Error loading report</p>";
		}
	}
	
	private String buildCustomerRows(List<CustomerOutstandingVO> customers) {

	    StringBuilder rows = new StringBuilder();
	    boolean evenRow = false;

	    for (CustomerOutstandingVO c : customers) {

	        evenRow = !evenRow;

	        String rowBg = evenRow ? "#f5f7fb" : "#ffffff";

	        int percent = c.getOutpercentage();
	        String textColor;
	        String status;

	        if (percent >= 150) {
	            textColor = "#8B0000";
	            status = "Extreme Breach";
	        } else if (percent >= 120) {
	            textColor = "#D32F2F";
	            status = "Critical Breach";
	        } else if (percent >= 100) {
	            textColor = "#F57C00";
	            status = "Credit Breached";
	        } else {
	            textColor = "#FFA000";
	            status = "Near Credit Limit";
	        }

	        rows.append(
	            "<tr style='background:" + rowBg + ";'>" +
	            "<td>" + c.getSubledgercode() + "</td>" +
	            "<td>" + c.getSubledgername() + "</td>" +
	            "<td align='right'>" + AmountUtil.formatInteger(c.getCreditlimit()) + "</td>" +
	            "<td align='right'>" + AmountUtil.formatInteger(c.getTotaldue()) + "</td>" +
	            "<td align='right' style='color:" + textColor + ";'><b>" + percent + "%</b></td>" +
	            "<td style='color:" + textColor + "; font-weight:600;'>" + status + "</td>" +
	            "</tr>"
	        );
	    }

	    return rows.toString();
	}


}
