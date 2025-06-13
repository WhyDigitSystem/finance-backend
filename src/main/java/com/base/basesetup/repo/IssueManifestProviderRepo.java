package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.IssueManifestProviderVO;

@Repository
public interface IssueManifestProviderRepo extends JpaRepository<IssueManifestProviderVO, Long> {

	boolean existsByOrgIdAndTransactionNo(Long orgId, String transactionNo);

	@Query(nativeQuery = true, value = "select * from mim where orgid=?1")
	List<IssueManifestProviderVO> findAllIssueManifeasrProvider(Long orgId);

	@Query(nativeQuery = true, value = "select * from mim where orgid=?1 and finyear=?2")
	List<IssueManifestProviderVO> getAllIssueManifestProvider(Long orgId, Long finYear);

	@Query(nativeQuery = true, value = "select transactionno,transactiondate,transportername,sender,receiver,amount,hsncode,b.assetcode,b.asset,b.assetqty,kitid,kitname,kitqty from \r\n"
			+ "finance_aip.mim a,finance_aip.mimdetails b where a.mimid = b.mimid and 'MIM' =?1 and a.orgid =?2 and (a.receiver=?3 or 'ALL'=?3) and a.finyear=?4 and (?5 is null or a.transactiondate >= ?5)  and (?6 is null or a.transactiondate <= ?6)\r\n"
			+ "union all \r\n"
			+ "select transactionno,transactiondate,transportername,sender,receiver,0 amount,hsncode,b.assetcode,b.asset,b.assetqty,kitid,kitname,kitqty from \r\n"
			+ "finance_aip.rim a,finance_aip.rimdetails b where a.rimid = b.rimid and  'RIM' =?1 and a.orgid =?2 and (a.sender=?3  or 'ALL'=?3) and a.finyear=?4\r\n"
			+ "and (?5 is null or a.transactiondate >= ?5)  and (?6 is null or a.transactiondate <= ?6)\r\n"
			+ "order by transactiondate")
	Set<Object[]> getMimReportDetails(String type, Long orgId, String customerName, String finYear, String toDate,
			String fromDate);

//	@Query(value = "SELECT \r\n" + "  m.*, \r\n" + "  ?1 AS type\r\n" + "FROM \r\n" + "  mim m\r\n" + "WHERE \r\n"
//			+ "  m.orgid = ?2\r\n" + "  AND m.finyear = ?4\r\n" + "  AND (\r\n" + "    (\r\n"
//			+ "      ?3 IS NOT NULL \r\n" + "      AND ?5 IS NOT NULL \r\n" + "      AND ?6 IS NOT NULL\r\n"
//			+ "      AND m.receiver = ?3\r\n" + "      AND m.transactiondate BETWEEN ?6 AND ?5\r\n" + "    )\r\n"
//			+ "    OR (\r\n" + "      ?3 IS NULL OR ?5 IS NULL OR ?6 IS NULL\r\n" + "    )\r\n"
//			+ "  )", nativeQuery = true)
//	List<IssueManifestProviderVO> findMIMReports(String type, Long orgId, String customerName, String finYear,
//			String toDate, String fromDate);

	
	@Query(value = "SELECT *\r\n"
			+ "FROM finance_aip.mim a\r\n"
			+ "WHERE ?1 = 'MIM'\r\n"
			+ "  AND a.orgid =?2\r\n"
			+ "  AND (a.receiver =?3 OR ?3 = 'ALL')\r\n"
			+ "  AND a.finyear = ?4\r\n"
			+ "  AND (?6 IS NULL OR a.transactiondate >=?6)\r\n"
			+ "  AND (?5 IS NULL OR a.transactiondate <= ?5)\r\n"
			+ "  order by  a.transactionno ,a.transactiondate", nativeQuery = true)
	List<IssueManifestProviderVO> findMIMReports(String type, Long orgId, String customerName, String finYear,
			String toDate, String fromDate);
	
//	@Query(value = "SELECT \r\n" + "  a.*, \r\n" + "  ?1 AS type\r\n" + "FROM \r\n" + "  rim a\r\n" + "WHERE \r\n"
//			+ "  a.orgid = ?2\r\n" + "  AND a.finyear = ?4\r\n" + "  AND (\r\n" + "    (\r\n"
//			+ "      ?3 IS NOT NULL \r\n" + "      AND ?5 IS NOT NULL \r\n" + "      AND ?6 IS NOT NULL\r\n"
//			+ "      AND a.sender = ?3\r\n" + "      AND a.transactiondate BETWEEN ?6 AND ?5\r\n" + "    )\r\n"
//			+ "    OR (\r\n" + "      ?3 IS NULL OR ?5 IS NULL OR ?6 IS NULL\r\n" + "    )\r\n"
//			+ "  )",
//    nativeQuery = true)
//List<RetrievalManifestProviderVO> findRIMReports(String type, Long orgId, String sender, String finYear, String toDate, String fromDate);


	@Query(nativeQuery = true, value = "select transactionno,transactiondate,transportername,receiver,amount,hsncode,SUM(kitqty) as sum from (\r\n"
			+ "			      select  transactionno,transactiondate,transportername,receiver,amount,hsncode,kitqty,orgid,finyear  from mim a,mimdetails m where a.mimid =m.mimid \r\n"
			+ "			     group by transactionno,transactiondate,transportername,receiver,kitid,amount,hsncode,kitqty,orgid,finyear\r\n"
			+ "			      order by transactionno,transactiondate) A  where \r\n"
			+ "                  ?1 = 'MIM'\r\n"
			+ "  AND a.orgid =?2\r\n"
			+ "  AND (a.receiver =?3 OR ?3 = 'ALL')\r\n"
			+ "  AND a.finyear =?4\r\n"
			+ "  AND (?5 IS NULL OR a.transactiondate >=?5)\r\n"
			+ "  AND (?6  IS NULL OR a.transactiondate <=?6) \r\n"
			+ " group by transactionno,transactiondate,transportername,receiver,amount,hsncode")
	Set<Object[]> findMimSummaryReport(String type, Long orgId, String customerName, String finYear, String fromDate,
			String toDate);
	
	@Query(nativeQuery = true, value = "select transactionno,transactiondate,transportername,sender,0 as  amount,hsncode,SUM(kitqty)  from (\r\n"
			+ "			      select  transactionno,transactiondate,transportername,sender,hsncode,kitqty,orgid,finyear  from rim a,rimdetails m where a.rimid =m.rimid \r\n"
			+ "			     group by transactionno,transactiondate,transportername,sender,kitid,hsncode,kitqty,orgid,finyear\r\n"
			+ "			      order by transactionno,transactiondate) A  where \r\n"
			+ "                  ?1 = 'RIM'\r\n"
			+ "  AND a.orgid =?2\r\n"
			+ "  AND (a.sender =?3 OR ?3 = 'ALL')\r\n"
			+ "  AND a.finyear =?4\r\n"
			+ "  AND (?5 IS NULL OR a.transactiondate >=?5)\r\n"
			+ "  AND (?6  IS NULL OR a.transactiondate <=?6) \r\n"
			+ " group by transactionno,transactiondate,transportername,sender,hsncode")
	Set<Object[]> findRimSummaryReport(String type, Long orgId, String customerName, String finYear, String fromDate,
			String toDate);
}
