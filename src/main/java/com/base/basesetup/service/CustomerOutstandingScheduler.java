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
	@Scheduled(cron = "0 41 10 * * ?", zone = "Asia/Kolkata")
	public void sendDailyCreditRiskMail() throws MessagingException {

		List<CustomerOutstandingVO> customers = repository.findCustomersExceeding80Percent();

		if (customers.isEmpty())
			return;

		int totalCustomers = customers.size();
		int c80 = 0, c100 = 0, c120 = 0;
		BigDecimal totalOutstanding = BigDecimal.ZERO;

		for (CustomerOutstandingVO c : customers) {
			int p = c.getOutpercentage();
			if (p >= 80 && p <= 99)
				c80++;
			if (p >= 100 && p <= 119)
				c100++;
			if (p >= 120)
				c120++;
			totalOutstanding = totalOutstanding.add(c.getTotaldue());
		}

		String rows = buildCustomerRows(customers);

		String html = loadOutstandingReportTemplate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")),
				totalCustomers, c80, c100, c120, totalOutstanding, rows);
		mailService.sendCreditRiskMail(html);
	}

	public String loadOutstandingReportTemplate(String reportDate, int totalCustomers, int customers80,
			int customers100, int customers120, BigDecimal totalOutstanding, String customerRows) {

		try {
			ClassPathResource resource = new ClassPathResource("templates/outstandingreport.html");

			String html = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

			return html.replace("${reportDate}", reportDate)
					.replace("${totalCustomers}", String.valueOf(totalCustomers))
					.replace("${customers80}", String.valueOf(customers80))
					.replace("${customers100}", String.valueOf(customers100))
					.replace("${customers120}", String.valueOf(customers120))
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

	        if (percent >= 120) {
	            textColor = "#8b0000";
	            status = "Extreme Breach";
	        } else if (percent >= 100) {
	            textColor = "#ff6600";
	            status = "Critical Breach";
	        } else if (percent >= 80) {
	            textColor = "#ffcc00";
	            status = "Credit Breached";
	        } else {
	            textColor = "#339933";
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
