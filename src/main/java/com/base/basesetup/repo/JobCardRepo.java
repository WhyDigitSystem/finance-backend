package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.JobCardVO;

@Repository
public interface JobCardRepo extends JpaRepository<JobCardVO, Long> {

	@Query(nativeQuery = true, value = "select * from jobcard where orgid=?1")
	List<JobCardVO> getAllJobCardByOrgId(Long orgId);

	@Query(nativeQuery = true, value = "select * from jobcard where jobcardid=?1")
	JobCardVO getAllJobCardById(Long id);

	@Query(nativeQuery = true, value = "select  a1.salesperson from partymaster a,partysalespersontagging a1 where active=1 and  a.partymasterid=a1.partymasterid\r\n"
			+ "	and a.orgid=?1  and a.partyname=?2 group by a1.salesperson")
	Set<Object[]> findBySalesPreson(Long orgId, String partyName);

	@Query(nativeQuery = true, value = "select partyname , partycode,partyshortname from partymaster where orgid=?1 and active=1 and partytype='CUSTOMER' group by partyname,partycode,partyshortname")
	Set<Object[]> findAllCustomers(Long orgId);

	@Query(nativeQuery = true, value = "select a.totalinvamountlc from taxinvoice a where a.orgid=?1 and a.partyname=?2 and a.approvestatus='Approved'\r\n"
			+ "  order by  a.totalinvamountlc")
	Set<Object[]> getIncomeByTaxInvoice(Long orgId, String customerName);

	@Query(nativeQuery = true, value = "select a.netbillcurramt from costinvoice a  where a.orgid=?1  and a.suppliername=?2 \r\n"
			+ "   and a.approvestatus='Approved' order by a.netbillcurramt")
	Set<Object[]> getExponesByCostInvoice(Long orgId, String customerName);

	@Query(nativeQuery = true, value = "SELECT \r\n" + "    income_data.partyname,\r\n"
			+ "    income_data.joborderno,\r\n" + "    income_data.income,\r\n"
			+ "    COALESCE(expense_data.expense, 0) AS expense,\r\n"
			+ "    income_data.income - COALESCE(expense_data.expense, 0) AS profit\r\n" + "FROM \r\n" + "    (\r\n"
			+ "        SELECT a.partyname, a.joborderno, SUM(a1.billamount) AS income\r\n"
			+ "        FROM taxinvoice a \r\n"
			+ "        JOIN taxinvoicedetails a1 ON a.taxinvoiceid = a1.taxinvoiceid\r\n"
			+ "        WHERE a.orgid =?1 AND a.partyname =?2 and a.joborderno=?3\r\n"
			+ "        GROUP BY a.partyname, a.joborderno\r\n" + "    ) AS income_data\r\n" + "LEFT JOIN \r\n"
			+ "    (\r\n" + "        SELECT b1.party, b1.jobno, SUM(b1.lcamt) AS expense\r\n"
			+ "        FROM costinvoice b\r\n"
			+ "        JOIN chargercostinvoice b1 ON b.costinvoiceid = b1.costinvoiceid\r\n"
			+ "        WHERE b.orgid =?1 AND b1.party =?2 and b1.jobno=?3\r\n"
			+ "        GROUP BY b1.party, b1.jobno\r\n" + "    ) AS expense_data\r\n"
			+ "ON income_data.partyname = expense_data.party \r\n" + "AND income_data.joborderno = expense_data.jobno")
	Set<Object[]> getIncomeAndExponseAndProfit(Long orgId, String partyName, String jobNo);

//	@Query(nativeQuery = true, value = "select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
//	String getJobCardDocId(Long orgId, String finYear, String branchCode, String screenCode);

	@Query(value = "SELECT " + "CONCAT(d.prefixfield, LPAD(d.lastno, 5, '0')) AS docid, " + "CASE "
			+ "   WHEN CURDATE() BETWEEN f.startdate AND f.enddate " + "   THEN CURDATE() " + "   ELSE f.enddate "
			+ "END AS docdate " + "FROM documenttypemappingdetails d " + "JOIN financialyear f "
			+ "ON d.finyear = f.finyear AND d.orgid = f.orgid " + "WHERE d.orgid = ?1 " + "AND d.finyear = ?2 "
			+ "AND d.branchcode = ?3 " + "AND d.screencode = ?4", nativeQuery = true)
	List<Object[]> getJobCardDocId(Long orgId, String finYear, String branchCode, String screenCode);

	boolean existsByrefNoAndOrgId(String refNo, Long orgId);

	@Query(nativeQuery = true, value = "select sum(income) income,sum(expence) expence,case when sum(income) - sum(expence) > 0 then (sum(income) - sum(expence)) else\r\n"
			+ " 0 end as profit ,\r\n"
			+ " case when sum(income) - sum(expence) < 0 then  abs(sum(income) - sum(expence)) else 0 end as loss\r\n"
			+ "     from(\r\n"
			+ "select sum(a.amount) as income,0 expence from vw_revenue a join taxinvoice t on a.docid=t.vid\r\n"
			+ "  join jobcard j on t.joborderno=j.jobno \r\n"
			+ " where a.orgid=?1 and j.customer=?2 and a.finyear=?3 and t.branch=?4\r\n" + " union\r\n"
			+ "  select 0 as income,sum(a.amount) expence from vw_cost a join costinvoice t on \r\n"
			+ " a.docid=t.vid join chargercostinvoice c on t.costinvoiceid=c.costinvoiceid\r\n"
			+ "   join jobcard j on c.jobno=j.jobno and j.customer=c.party\r\n"
			+ " where a.orgid=?1 and j.customer=?2 and a.finyear=?3 and t.branch=?4\r\n" + " ) a1")
	Set<Object[]> getIncomeAndExponseAndProfitDetails(Long orgId, String partyName, String finYear, String branch);

}
