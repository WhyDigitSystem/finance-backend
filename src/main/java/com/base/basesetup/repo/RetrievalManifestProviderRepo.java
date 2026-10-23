package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.RetrievalManifestProviderVO;

@Repository
public interface RetrievalManifestProviderRepo extends JpaRepository<RetrievalManifestProviderVO, Long> {

	boolean existsByOrgIdAndTransactionNo(Long orgId, String transactionNo);

	@Query(nativeQuery = true, value = "select a.* from rim a where a.orgid=?1 and a.transactionno not in(\r\n"
			+ "select docid from binretrieval where orgid=?1 group by docid)")
	List<RetrievalManifestProviderVO> findPendingBinRetrievalDetails(Long orgId);

//	@Query(value = 
//			  "SELECT a.*\r\n"
//			  + "FROM rim a\r\n"
//			  + "WHERE a.orgid = ?2\r\n"
//			  + "  AND a.finyear = ?4\r\n"
//			  + "  AND 'RIM' = ?1\r\n"
//			  + "  AND (\r\n"
//			  + "    (\r\n"
//			  + "      ?3 IS NOT NULL\r\n"
//			  + "      AND ?5 IS NOT NULL\r\n"
//			  + "      AND ?6 IS NOT NULL\r\n"
//			  + "      AND a.sender = ?3\r\n"
//			  + "      AND a.transactiondate BETWEEN ?6 AND ?5\r\n"
//			  + "    )\r\n"
//			  + "    OR (\r\n"
//			  + "      ?3 IS NULL OR ?5 IS NULL OR ?6 IS NULL\r\n"
//			  + "    )\r\n"
//			  + "  )\r\n"
//			  + "",
//			  nativeQuery = true)
//List<RetrievalManifestProviderVO> findRIMReports(String type, Long orgId, String sender, String finYear, String toDate, String fromDate);

//	@Query(value = "SELECT \r\n" + "  m.*, \r\n" + "  ?1 AS type\r\n" + "FROM \r\n" + "  mim m\r\n" + "WHERE \r\n"
//			+ "  m.orgid = ?2\r\n" + "  AND m.finyear = ?4\r\n" + "  AND (\r\n" + "    (\r\n"
//			+ "      ?3 IS NOT NULL \r\n" + "      AND ?5 IS NOT NULL \r\n" + "      AND ?6 IS NOT NULL\r\n"
//			+ "      AND m.receiver = ?3\r\n" + "      AND m.transactiondate BETWEEN ?6 AND ?5\r\n" + "    )\r\n"
//			+ "    OR (\r\n" + "      ?3 IS NULL OR ?5 IS NULL OR ?6 IS NULL\r\n" + "    )\r\n"
//			+ "  )", nativeQuery = true)
//	List<IssueManifestProviderVO> findMIMReports(String type, Long orgId, String customerName, String finYear,
//			String toDate, String fromDate);

	@Query(value = """
			SELECT b.company, a.*
			FROM rim a
			JOIN warehouse b
			    ON a.receiver = CONCAT(
			        COALESCE(b.locationname, ''),
			        '-',
			        COALESCE(b.locationunit, '')
			    )
			WHERE ?1 = 'RIM'
			  AND a.orgid = ?2
			  AND (a.sender = ?3 OR ?3 = 'ALL')
			  AND (b.company = ?4 OR ?4 = 'ALL')
			  AND a.finyear = ?5
			  AND (?6 IS NULL OR a.transactiondate >= ?6)
			  AND (?7 IS NULL OR a.transactiondate <= ?7)
			ORDER BY a.transactionno, a.transactiondate
			""", nativeQuery = true)
	List<Object[]> findRIMReports(String type, Long orgId, String customer, String company, String finyear,
			String fromDate, String toDate);

	@Query(nativeQuery = true, value = "select concat(prefixfield,lpad(lastno,4,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode='BLR' and screencode=?3")
	String getRetrievalManifestProviderDocId(Long orgId, String finYear, String screenCode);

	@Query(nativeQuery = true, value = "select * from rim where orgid=?1 and finyear=?2  order by transactionno desc")
	List<RetrievalManifestProviderVO> getAllRetrievalManifestProvider(Long orgId, Long finYear);

	@Query(value = """
			SELECT DISTINCT w.company
			FROM warehouse w
			WHERE w.orgid = ?1
			  AND w.company IS NOT NULL
			ORDER BY w.company
			""", nativeQuery = true)
	List<String> findCompanyDropdownByOrgId(Long orgId);

	@Query(value = """
			      SELECT suppliername, docid, docdate, a.supplierbillno, a.supplierbilldate,
			             a.totchargeslcamt, a.gstinputlcamt, a.netbillcurramt,
			             CASE
			                 WHEN approvestatus = 'Approved' THEN 'Approved'
			                 ELSE 'Pending'
			             END AS status,
			             purvoucherno, purvoucherdate,
			             'AIPACKS TRAX' AS entity,
			             a.finyear,
			             DATE_FORMAT(docdate, '%M %Y') AS billedmonthyear,
			             b.bussinesscate,
			             NOW() AS Report_Time
			      FROM finance_aip_trax.costinvoice a
			      JOIN finance_aip_trax.partymaster b
			          ON a.suppliercode = b.partycode
			      WHERE a.cancel = 0
			        AND ('ALL' = ?1 OR a.suppliername = ?1 ) and (b.bussinesscate=?3 or 'ALL'=?3)
			AND (?2 IS NULL OR a.docdate <= ?2)

			      UNION

			      SELECT suppliername, docid, docdate, c.supplierbillno, c.supplierbilldate,
			             c.totchargeslcamt, c.gstinputlcamt, c.netbillcurramt,
			             CASE
			                 WHEN approvestatus = 'Approved' THEN 'Approved'
			                 ELSE 'Pending'
			             END AS status,
			             purvoucherno, purvoucherdate,
			             'SCM AIPACKS' AS entity,
			             c.finyear,
			             DATE_FORMAT(docdate, '%M %Y') AS billedmonthyear,
			             d.bussinesscate,
			             NOW() AS Report_Time
			      FROM finance_aip.costinvoice c
			      JOIN finance_aip.partymaster d
			          ON c.suppliercode = d.partycode
			      WHERE c.cancel = 0
			        AND ('ALL' = ?1 OR c.suppliername = ?1) and (d.bussinesscate=?3 or 'ALL'=?3)
			     AND (?2 IS NULL OR c.docdate <=?2)
			      ORDER BY bussinesscate, suppliername, billedmonthyear
			      """, nativeQuery = true)
	List<Object[]> findConsolidationCostInvoiceReports(String supplier, String asonDate, String type);

	@Query(value = """
	        SELECT DISTINCT TRIM(bussinesscate) AS bussinesscate
	        FROM finance_aip_trax.partymaster
	        WHERE bussinesscate IS NOT NULL
	          AND TRIM(bussinesscate) <> '' and partytype='VENDOR'

	        UNION

	        SELECT DISTINCT TRIM(bussinesscate) AS bussinesscate
	        FROM finance_aip.partymaster
	        WHERE bussinesscate IS NOT NULL
	          AND TRIM(bussinesscate) <> '' and partytype='VENDOR'
	        """, nativeQuery = true)
	List<Object> getAllCategoryList();

	@Query(nativeQuery = true, value = "select partyname from finance_aip_trax.partymaster where (bussinesscate=?1 or 'ALL'=?1)  and partytype='VENDOR' group by partyname\r\n"
			+ "union\r\n"
			+ " select  partyname from finance_aip.partymaster where (bussinesscate=?1 or 'ALL'=?1)  and partytype='VENDOR' group by partyname")
	Set<Object[]> getAllVendorListCostReport(String type);

	@Query(value = """
			SELECT a.partyname, a.docid, a.docdate, a.purvoucherno,
			       a.purvoucherdate, a.paymentamt, a.chequeno, a.chequedate,
			       a.payto, a.onaccount,
			       CASE
			           WHEN a.approvestatus = 'Approved' THEN 'Approved'
			           ELSE 'Pending'
			       END AS status,
			       'AIPACKS TRAX' AS entity,
			       a.finyear,
			       DATE_FORMAT(a.docdate, '%M %Y') AS billedmonthyear,
			       b.bussinesscate
			FROM finance_aip_trax.payment a
			JOIN finance_aip_trax.partymaster b
			    ON a.partycode = b.partycode
			WHERE a.cancel = 0
			  AND (?1 = 'ALL' OR FIND_IN_SET(a.partyname, ?1) > 0)
			  AND (?2 IS NULL OR a.docdate <= ?2) and  (b.bussinesscate=?3 or ?3='ALL') 
			UNION
			SELECT c.partyname, c.docid, c.docdate, c.purvoucherno,
			       c.purvoucherdate, c.paymentamt, c.chequeno, c.chequedate,
			       c.payto, c.onaccount,
			       CASE
			           WHEN c.approvestatus = 'Approved' THEN 'Approved'
			           ELSE 'Pending'
			       END AS status,
			       'SCM AIPACKS' AS entity,
			       c.finyear,
			       DATE_FORMAT(c.docdate, '%M %Y') AS billedmonthyear,
			       d.bussinesscate
			FROM finance_aip.payment c
			JOIN finance_aip.partymaster d
			    ON c.partycode = d.partycode
			WHERE c.cancel = 0
			  AND (?1 = 'ALL' OR FIND_IN_SET(c.partyname, ?1) > 0)
			  AND (?2 IS NULL OR c.docdate <= ?2) and (d.bussinesscate=?3 or ?3='ALL')
			ORDER BY entity, docdate, billedmonthyear
			""", nativeQuery = true)
	List<Object[]> findConsolidationPaymentReports(String partyName, String asonDate,String type);

}