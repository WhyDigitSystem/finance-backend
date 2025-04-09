package com.base.basesetup.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class Views {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void initialize() {
        try {
            executeQueries();
        } catch (Exception e) {
            throw new RuntimeException("Error initializing database", e);
        }
    }

    private void executeQueries() {
    	
    	// Exrates View
    	jdbcTemplate.execute("create or replace view vw_exrates as\r\n"
    			+ "SELECT \r\n"
    			+ "    c.dailymonthlyexratesdtlid,\r\n"
    			+ "    g.orgid,\r\n"
    			+ "    g.date,\r\n"
    			+ "    g.month,\r\n"
    			+ "    c.currency, \r\n"
    			+ "    c.currencydescripition, \r\n"
    			+ "    c.buyingexrate, \r\n"
    			+ "    c.sellingexrate\r\n"
    			+ "FROM \r\n"
    			+ "    dailymonthlyexratesdtl c\r\n"
    			+ "JOIN \r\n"
    			+ "    dailymonthlyexrates g\r\n"
    			+ "ON \r\n"
    			+ "    c.dailymonthlyexratesid = g.dailymonthlyexratesid\r\n"
    			+ "JOIN (\r\n"
    			+ "    SELECT \r\n"
    			+ "        a.currency,\r\n"
    			+ "        MAX(a.dailymonthlyexratesdtlid) AS dailymonthlyexratesdtlid\r\n"
    			+ "    FROM \r\n"
    			+ "        dailymonthlyexratesdtl a\r\n"
    			+ "    JOIN \r\n"
    			+ "        dailymonthlyexrates b\r\n"
    			+ "    ON \r\n"
    			+ "        a.dailymonthlyexratesid = b.dailymonthlyexratesid\r\n"
    			+ "    GROUP BY \r\n"
    			+ "        a.currency\r\n"
    			+ ") d \r\n"
    			+ "ON c.dailymonthlyexratesdtlid = d.dailymonthlyexratesdtlid");
    	
    	jdbcTemplate.execute("create or replace view vw_REVENUE AS\r\n"
    			+ "SELECT \r\n"
    			+ "    h.orgid, \r\n"
    			+ "    h.billmonth, \r\n"
    			+ "    h.finyear,\r\n"
    			+ "    h.branch,\r\n"
    			+ "        h.branchcode,\r\n"
    			+ "    h.docid, \r\n"
    			+ "    h.docdate, \r\n"
    			+ "    (h.totalcreditamount - COALESCE(j.totalcreditamount, 0)) AS amount\r\n"
    			+ "FROM \r\n"
    			+ "    (SELECT \r\n"
    			+ "        a.orgid, \r\n"
    			+ "        DATE_FORMAT(a.vdate, '%M') AS billmonth, \r\n"
    			+ "        a.finyear, \r\n"
    			+ "        a.branch,\r\n"
    			+ "        a.branchcode,\r\n"
    			+ "        b.vid docid, \r\n"
    			+ "        b.vdate docdate, \r\n"
    			+ "        a.totalcreditamount \r\n"
    			+ "     FROM accounts a\r\n"
    			+ "     JOIN taxinvoice b ON a.orgid = b.orgid AND a.refno = b.docid\r\n"
    			+ "     WHERE b.cancel = 0\r\n"
    			+ "    ) h\r\n"
    			+ "LEFT JOIN \r\n"
    			+ "    (SELECT \r\n"
    			+ "        a.orgid, \r\n"
    			+ "        DATE_FORMAT(a.vdate, '%M') AS billmonth, \r\n"
    			+ "        a.finyear, \r\n"
    			+ "        a.branch,\r\n"
    			+ "        a.branchcode,\r\n"
    			+ "        c.vid docid, \r\n"
    			+ "        c.vdate docdate, \r\n"
    			+ "        a.totalcreditamount \r\n"
    			+ "     FROM accounts a\r\n"
    			+ "     JOIN irncreditnote b ON a.orgid = b.orgid AND b.docid = a.refno\r\n"
    			+ "     JOIN taxinvoice c ON b.orgid = c.orgid AND c.docid = b.originbillno\r\n"
    			+ "     WHERE  b.cancel = 0\r\n"
    			+ "    ) j\r\n"
    			+ "ON h.orgid = j.orgid AND h.docid = j.docid");
    	
    	jdbcTemplate.execute("create or replace view vw_cost as\r\n"
    			+ "SELECT \r\n"
    			+ "    h.orgid, \r\n"
    			+ "    h.billmonth, \r\n"
    			+ "    h.finyear,\r\n"
    			+ "    h.branch, \r\n"
    			+ "        h.branchcode,\r\n"
    			+ "    h.docid, \r\n"
    			+ "    h.docdate, \r\n"
    			+ "    (h.totalcreditamount - COALESCE(j.totalcreditamount, 0)) AS amount\r\n"
    			+ "FROM \r\n"
    			+ "    (SELECT \r\n"
    			+ "        a.orgid, \r\n"
    			+ "        DATE_FORMAT(a.vdate, '%M') AS billmonth, \r\n"
    			+ "        a.finyear, \r\n"
    			+ "        a.branch,\r\n"
    			+ "        a.branchcode,\r\n"
    			+ "        b.vid docid, \r\n"
    			+ "        b.vdate docdate, \r\n"
    			+ "        a.totalcreditamount \r\n"
    			+ "     FROM accounts a\r\n"
    			+ "     JOIN costinvoice b ON a.orgid = b.orgid AND a.refno = b.docid\r\n"
    			+ "     WHERE b.cancel = 0\r\n"
    			+ "    ) h\r\n"
    			+ "LEFT JOIN \r\n"
    			+ "    (SELECT \r\n"
    			+ "        a.orgid, \r\n"
    			+ "        DATE_FORMAT(a.vdate, '%M') AS billmonth, \r\n"
    			+ "        a.finyear, \r\n"
    			+ "        a.branch,\r\n"
    			+ "        a.branchcode,\r\n"
    			+ "        c.vid docid, \r\n"
    			+ "        c.vdate docdate, \r\n"
    			+ "        a.totalcreditamount \r\n"
    			+ "     FROM accounts a\r\n"
    			+ "     JOIN costdebitnote b ON a.orgid = b.orgid AND b.docid = a.refno\r\n"
    			+ "     JOIN costinvoice c ON b.orgid = c.orgid AND c.docid = b.orginbill\r\n"
    			+ "     WHERE  b.cancel = 0\r\n"
    			+ "    ) j\r\n"
    			+ "ON h.orgid = j.orgid AND h.docid = j.docid");
   }
    
    

}
