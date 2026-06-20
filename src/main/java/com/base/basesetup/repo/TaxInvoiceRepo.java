package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.base.basesetup.entity.TaxInvoiceVO;

public interface TaxInvoiceRepo extends JpaRepository<TaxInvoiceVO, Long> {

	boolean existsByDocIdAndOrgId(String docId, Long orgId);

	boolean existsByInvoiceNoAndOrgId(String invoiceNo, Long orgId);

	boolean existsByDocIdAndOrgIdAndId(String docId, Long orgId, Long id);

	boolean existsByInvoiceNoAndOrgIdAndId(String invoiceNo, Long orgId, Long id);

	@Query(value = "select a from TaxInvoiceVO a where a.orgId=?1 and a.finYear=?2 and a.branchCode=?3 order by a.docId desc")
	List<TaxInvoiceVO> getAllTaxInvoiceByOrgId(Long orgId, String finYear, String branchCode);

	@Query(nativeQuery = true, value = "select * from taxinvoice where orgid=?1 and docid=?2")
	TaxInvoiceVO findAllTaxInvoiceByDocId(Long orgId, String docId);

	@Query(value = "select a from TaxInvoiceVO a where a.id=?1")
	TaxInvoiceVO getTaxInvoiceById(Long id);

//	@Query(nativeQuery = true, value = "select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
//	String getTaxInvoiceDocId(Long orgId, String finYear, String branchCode, String screenCode);

	@Query(value = "SELECT " + "CONCAT(d.prefixfield, LPAD(d.lastno, 5, '0')) AS docid, " + "CASE "
			+ "   WHEN CURDATE() BETWEEN f.startdate AND f.enddate " + "   THEN CURDATE() " + "   ELSE f.enddate "
			+ "END AS docdate " + "FROM documenttypemappingdetails d " + "JOIN financialyear f "
			+ "ON d.finyear = f.finyear AND d.orgid = f.orgid " + "WHERE d.orgid = ?1 " + "AND d.finyear = ?2 "
			+ "AND d.branchcode = ?3 " + "AND d.screencode = ?4", nativeQuery = true)
	List<Object[]> getTaxInvoiceDocId(Long orgId, String finYear, String branchCode, String screenCode);

	@Query(nativeQuery = true, value = "select currency,currencydescripition,buyingexrate,sellingexrate,ROW_NUMBER() OVER (ORDER BY currency) AS id from vw_exrates where orgid=?1")
	Set<Object[]> getCurrencyAndExrateDetails(Long orgId);

	@Query(nativeQuery = true, value = "select a.statecode,a.gstin,concat(a.stateno,' - ',a.state)stateno from partystate a,partymaster b where a.partymasterid=b.partymasterid and b.orgid=?1 and b.partymasterid=?2\r\n"
			+ "group by a.statecode,a.gstin,concat(a.stateno,' - ',a.state)")
	Set<Object[]> getStateCodeDetails(Long orgId, Long id);

	@Query(nativeQuery = true, value = "SELECT a.businessplace FROM partyaddress a,partymaster b,state c where  a.partymasterid=b.partymasterid and a.state=c.state and b.orgid=?1 and a.partymasterid=?2 and c.statecode=?3\r\n"
			+ "group by businessplace")
	Set<Object[]> getPlaceOfSupplyDetails(Long orgId, Long id, String stateCode);

	@Query(nativeQuery = true, value = "SELECT a.addresstype,concat(a.addressline1,',',a.addressline2,',',a.addressline3) address,a.pincode FROM partyaddress a,partymaster b,state c where  a.partymasterid=b.partymasterid and a.state=c.state and b.orgid=?1 and a.partymasterid=?2 and c.statecode=?3 and a.businessplace=?4\r\n"
			+ "group by a.addresstype,concat(a.addressline1,',',a.addressline2,',',a.addressline3),a.pincode")
	Set<Object[]> getAddressDetails(Long orgId, Long id, String stateCode, String placeOfSupply);

	@Query(nativeQuery = true, value = "SELECT \r\n" + "       CASE \r\n"
			+ "           WHEN statecode = ?3 THEN 'INTRA'\r\n" + "           ELSE 'INTER'\r\n"
			+ "       END AS transactionType\r\n" + "FROM branch\r\n" + "WHERE orgid = ?1 \r\n"
			+ "  AND branchcode = ?2")
	Set<Object[]> getGstType(Long orgId, String branchCode, String stateCode);

	@Query(value = "select a from TaxInvoiceVO a where a.orgId=?1 and a.partyName=?2 and a.branchCode=?3 and a.approveStatus='Approved' order by a.docId desc")
	List<TaxInvoiceVO> findPartyInvoiceDetails(Long orgId, String party, String branchCode);

	TaxInvoiceVO findByOrgIdAndIdAndDocId(Long orgId, Long id, String docId);

	@Query(nativeQuery = true, value = "select a.creditdays from partymaster a where a.orgid=?1 and partycode=?2 and a.active=1 ")
	Set<Object[]> findCreditDaysFromCustomer(Long orgId, String customerCode);

	@Query(nativeQuery = true, value = "select a.jobno from jobcard a where a.orgid=?1 and a.closed=0 and a.active=1 ")
	Set<Object[]> getAllJobNoByActice(Long orgId);

	@Query(nativeQuery = true, value = "select * from taxinvoice a where a.orgid=?1 and docid=?2 ")
	TaxInvoiceVO findByOrgIdAndDocId(Long orgId, String originBillNo);

	@Query(nativeQuery = true, value = "select a.jobno from jobcard a where a.orgid=?1 and a.customercode=?2 and active=1 ")
	Set<Object[]> getJobCardForTaxInvoice(Long orgId, String partyCode);

	boolean existsByvIdAndOrgId(String vId, Long orgId);

	@Query(nativeQuery = true, value = "SELECT \r\n" + "    a.orgid,\r\n" + "    a.branchcode,\r\n" + "    a.vid,\r\n"
			+ "    a.vdate,\r\n" + "    a.joborderno,\r\n" + "    c.docid AS voucherno,\r\n"
			+ "    c.docdate AS voucherdate,\r\n" + "    e.partyshortname AS billtoparty,\r\n"
			+ "    e.controllingoff,\r\n" + "    a.billcurr,\r\n" + "    a.billcurrrate,\r\n"
			+ "    a.totalinvamountbc,\r\n" + "    a.totalinvamountlc,\r\n" + "    a.totaltaxableamountlc,\r\n"
			+ "    a.gsttype,\r\n" + "    a.totaltaxamountlc,\r\n" + "    a.totaltaxamountbc,\r\n"
			+ "    a.roundoffamountlc,\r\n" + "    SUM(b.fcamount) AS fcamt,\r\n" + "    SUM(b.lcamount) AS lcamt,\r\n"
			+ "    SUM(b.rate) AS rate,\r\n" + "    SUM(b.billamount) AS billamount,\r\n"
			+ "    a.partytype,a.docid,a.docdate,a.screencode\r\n" + "FROM\r\n" + "    taxinvoice a\r\n" + "JOIN\r\n"
			+ "    taxinvoicedetails b ON a.taxinvoiceid = b.taxinvoiceid\r\n" + "JOIN\r\n"
			+ "    accounts c ON a.docid = c.refno\r\n" + "JOIN\r\n"
			+ "    partymaster e ON a.partycode = e.partycode\r\n" + "WHERE\r\n" + "   (\r\n"
			+ "        (?1 IS NULL OR ?1 = '' OR ?2 IS NULL OR ?2 = '') \r\n"
			+ "        OR c.vdate BETWEEN STR_TO_DATE(?1, '%Y-%m-%d') AND STR_TO_DATE(?2, '%Y-%m-%d')\r\n" + "    )\r\n"
			+ "    AND a.orgid = ?3\r\n" + "    AND (a.branchcode = ?4 OR ?4 = 'ALL')\r\n"
			+ "    AND (e.partycode = ?5 OR ?5 = 'ALL') and a.finyear=?6\r\n" + "GROUP BY\r\n"
			+ "    a.orgid, a.branchcode, a.vid, a.vdate, a.joborderno, c.docid, c.docdate, \r\n"
			+ "    e.partyshortname, e.controllingoff, a.billcurr, a.billcurrrate, a.totalinvamountbc, \r\n"
			+ "    a.totalinvamountlc, a.totaltaxableamountlc, a.gsttype, a.totaltaxamountlc, \r\n"
			+ "    a.totaltaxamountbc, a.roundoffamountlc, a.partytype,a.docid,a.docdate,a.screencode\r\n" + "\r\n"
			+ "UNION\r\n" + "\r\n" + "SELECT\r\n" + "    a.orgid,\r\n" + "    a.branchcode,\r\n" + "    a.vid,\r\n"
			+ "    a.vdate,\r\n" + "    a.jobno AS joborderno,\r\n" + "    c.docid AS voucherno,\r\n"
			+ "    c.docdate AS voucherdate,\r\n" + "    e.partyshortname AS billtoparty,\r\n"
			+ "    e.controllingoff,\r\n" + "    a.billcurr,\r\n" + "    a.billcurrrate,\r\n"
			+ "    a.totalinvamountbc*-1,\r\n" + "    a.totalinvamountlc*-1,\r\n" + "    a.totaltaxableamountlc,\r\n"
			+ "    a.gsttype,\r\n" + "    a.totaltaxamountlc*-1,\r\n" + "    0 AS totaltaxamountbc,\r\n"
			+ "    a.roundoffamountlc,\r\n" + "    SUM(b.fcamount) AS fcamt,\r\n"
			+ "    SUM(b.lcamount)*-1 AS lcamt,\r\n" + "    SUM(b.rate) AS rate,\r\n"
			+ "    SUM(b.billamount)*-1 AS billamount,\r\n" + "    a.partytype,a.docid,a.docdate,a.screencode\r\n"
			+ "FROM\r\n" + "    irncreditnote a\r\n" + "JOIN\r\n"
			+ "    irncreditnotedetails b ON a.irncreditnoteid = b.irncreditnoteid\r\n" + "JOIN\r\n"
			+ "    accounts c ON a.docid = c.refno\r\n" + "JOIN\r\n"
			+ "    partymaster e ON a.partycode = e.partycode\r\n" + "WHERE\r\n" + "   (\r\n"
			+ "        (?1 IS NULL OR ?1 = '' OR ?2 IS NULL OR ?2 = '') \r\n"
			+ "        OR c.vdate BETWEEN STR_TO_DATE(?1, '%Y-%m-%d') AND STR_TO_DATE(?2, '%Y-%m-%d')\r\n" + "    )\r\n"
			+ "    AND a.orgid = ?3\r\n" + "    AND (a.branchcode = ?4 OR ?4 = 'ALL')\r\n"
			+ "    AND (e.partycode = ?5 OR ?5 = 'ALL') and a.finyear=?6\r\n" + "GROUP BY\r\n"
			+ "    a.orgid, a.branchcode, a.vid, a.vdate, a.jobno, e.partyshortname, e.controllingoff, \r\n"
			+ "    a.billcurr, a.billcurrrate, a.totalinvamountbc, a.totalinvamountlc, \r\n"
			+ "    a.totaltaxableamountlc, a.gsttype, a.totaltaxamountlc, a.roundoffamountlc, \r\n"
			+ "    c.docid, c.docdate, a.partytype,a.docid,a.docdate,a.screencode\r\n" + "\r\n" + "ORDER BY\r\n"
			+ "    vid, vdate")
	Set<Object[]> getReportDetailsForSalesRegister(String fromDate, String toDate, Long orgId, String branchCode,
			String partyCode, String finYear);

	@Query(nativeQuery = true, value = "SELECT SUM(a.amount) \r\n" + "FROM (\r\n"
			+ "    -- Current Month Revenue (only if type = 'MONTH')\r\n" + "    SELECT SUM(v.amount) AS amount\r\n"
			+ "    FROM vw_revenue v \r\n" + "    JOIN financialyear f ON v.finyear = f.finyear\r\n"
			+ "    WHERE v.docdate BETWEEN f.startdate AND f.enddate \r\n"
			+ "      AND MONTH(v.docdate) = MONTH(CURDATE()) \r\n" + "      AND ?2 = 'MONTH' \r\n"
			+ "      AND CAST(v.finyear AS SIGNED) = ?3\r\n" + "      AND v.orgid = ?1\r\n" + "\r\n"
			+ "    UNION ALL\r\n" + "\r\n" + "    -- Current Financial Year Revenue (only if type = 'YEAR')\r\n"
			+ "    SELECT v.amount \r\n" + "    FROM vw_revenue v \r\n" + "    WHERE (\r\n"
			+ "        (MONTH(v.docdate) >= 4 AND CAST(v.finyear AS SIGNED) = ?3) \r\n" + "        OR \r\n"
			+ "        (MONTH(v.docdate) < 4 AND CAST(v.finyear AS SIGNED) = (?3 + 1))\r\n" + "    )\r\n"
			+ "    AND v.orgid = ?1 \r\n" + "    AND ?2 = 'YEAR'\r\n" + ") a")
	Set<Object[]> getDsahboardRevenue(Long orgId, String billMonth, String finYear);

//	@Query(nativeQuery =true,value="SELECT \r\n"
//			+ "    v.orgid, \r\n"
//			+ "    SUM(v.amount) AS curmnth, \r\n"
//			+ "    0 AS premonth\r\n"
//			+ "FROM vw_revenue v\r\n"
//			+ "INNER JOIN financialyear f \r\n"
//			+ "    ON v.finyear = f.finyear \r\n"
//			+ "    AND v.docdate BETWEEN f.startdate AND f.enddate\r\n"
//			+ "WHERE \r\n"
//			+ "    f.finyear = ?2\r\n"
//			+ "    AND MONTH(v.docdate) =  MONTH(CURDATE()) \r\n"
//			+ "    AND v.orgid = ?1\r\n"
//			+ "    AND ?3 = 'MONTH'\r\n"
//			+ "GROUP BY v.orgid\r\n"
//			+ "\r\n"
//			+ "UNION\r\n"
//			+ "\r\n"
//			+ "SELECT \r\n"
//			+ "    v.orgid, \r\n"
//			+ "    0 AS curmnth, \r\n"
//			+ "    SUM(v.amount) AS premonth\r\n"
//			+ "FROM vw_revenue v\r\n"
//			+ "INNER JOIN financialyear f \r\n"
//			+ "    ON v.finyear = f.finyear \r\n"
//			+ "    AND v.docdate BETWEEN f.startdate AND f.enddate\r\n"
//			+ "WHERE \r\n"
//			+ "    f.finyear = ?2\r\n"
//			+ "    AND MONTH(v.docdate) =\r\n"
//			+ " CASE \r\n"
//			+ "            WHEN MONTH(CURDATE()) =4  THEN NULL \r\n"
//			+ "            ELSE MONTH(CURDATE()) - 1\r\n"
//			+ "         END\r\n"
//			+ "    AND v.orgid = ?1\r\n"
//			+ "    AND ?3 = 'MONTH'\r\n"
//			+ "GROUP BY v.orgid")

	@Query(nativeQuery = true, value = "SELECT SUM(a.curmnth) AS currentmonth, SUM(a.premonth) AS previousmonth, SUM(a.curyear) AS currentyear, SUM(a.preyear) AS previousyear\r\n"
			+ "FROM (\r\n"
			+ "    SELECT v.orgid, SUM(v.amount) AS curmnth, 0 AS premonth, 0 AS curyear, 0 AS preyear\r\n"
			+ "    FROM vw_revenue v\r\n" + "    INNER JOIN financialyear f \r\n"
			+ "        ON v.finyear = f.finyear \r\n" + "        AND v.docdate BETWEEN f.startdate AND f.enddate\r\n"
			+ "    WHERE \r\n" + "        CAST(f.finyear AS SIGNED) =?2\r\n"
			+ "        AND MONTH(v.docdate) = MONTH(CURDATE())\r\n" + "        AND v.orgid =?1 and v.branchcode=?5\r\n"
			+ "        AND  ?3 = 'MONTH'\r\n" + "    GROUP BY v.orgid\r\n" + "    UNION\r\n"
			+ "    SELECT v.orgid, 0 AS curmnth, SUM(v.amount) AS premonth, 0 AS curyear, 0 AS preyear\r\n"
			+ "    FROM vw_revenue v\r\n" + "    WHERE \r\n" + "        v.finyear = (\r\n" + "            CASE \r\n"
			+ "                WHEN MONTH(CURDATE()) = 4 AND YEAR(CURDATE()) =?2 THEN (?2 - 1)\r\n"
			+ "                ELSE ?2\r\n" + "            END\r\n" + "        )\r\n"
			+ "        AND MONTH(v.docdate) = (\r\n" + "            CASE \r\n"
			+ "                WHEN MONTH(CURDATE()) = 4 AND YEAR(CURDATE()) =?2 THEN 3\r\n"
			+ "                ELSE (MONTH(CURDATE()) - 1)\r\n" + "            END\r\n" + "        )\r\n"
			+ "        AND v.orgid =?1 and v.branchcode=?5\r\n" + "        AND ?3 = 'MONTH'\r\n"

			+ "    GROUP BY v.orgid\r\n" + "    UNION\r\n"
			+ "    SELECT v.orgid, 0 AS curmnth, 0 AS premonth, SUM(v.amount) AS curyear, 0 AS preyear\r\n"
			+ "    FROM vw_revenue v\r\n" + "    WHERE \r\n" + "        v.finyear =?2\r\n"
			+ "        AND v.orgid =?1 and v.branchcode=?5\r\n" + "        AND ?4 = 'YEAR'\r\n"
			+ "    GROUP BY v.orgid\r\n" + "    UNION\r\n"
			+ "    SELECT v.orgid, 0 AS curmnth, 0 AS premonth, 0 AS curyear, SUM(v.amount) AS preyear\r\n"
			+ "    FROM vw_revenue v\r\n" + "    WHERE \r\n" + "        v.finyear = (?2 - 1)\r\n"
			+ "        AND v.orgid =?1 and v.branchcode=?5\r\n" + "        AND ?4 = 'YEAR'\r\n"
			+ "    GROUP BY v.orgid\r\n" + ") a")
	Set<Object[]> getPercentageDiffFromRevenue(Long orgId, Long finYear, String Month, String Year, String branchCode);

	@Query(nativeQuery = true, value = "SELECT \r\n" + "    SUM(a.curyear) AS currentyear,\r\n"
			+ "    SUM(a.preyear) AS previousyear\r\n" + "FROM (\r\n" + "    SELECT \r\n"
			+ "        SUM(v.amount) AS curyear,\r\n" + "        0 AS preyear,\r\n"
			+ "        MONTH(v.docdate) AS month,\r\n" + "        v.finyear,\r\n" + "        v.orgid\r\n"
			+ "    FROM vw_revenue v\r\n" + "    WHERE v.finyear =?2\r\n" + "    AND v.orgid = ?1\r\n"
			+ "    GROUP BY MONTH(v.docdate), v.finyear, v.orgid\r\n" + "    UNION\r\n" + "    SELECT  \r\n"
			+ "        0 AS curyear,\r\n" + "        SUM(v.amount) AS preyear,\r\n"
			+ "        MONTH(v.docdate) AS month,\r\n" + "        v.finyear,\r\n" + "        v.orgid\r\n"
			+ "    FROM vw_revenue v\r\n" + "    WHERE v.finyear =(?2 -1)\r\n" + "    AND v.orgid = ?1\r\n"
			+ "    GROUP BY MONTH(v.docdate), v.finyear, v.orgid\r\n" + ") a")
	Set<Object[]> getPercentageDiffFromYear(Long orgId, Long finYear);

	@Query(nativeQuery = true, value = "SELECT \r\n" + "    SUM(a.curyear) AS currentyear,\r\n"
			+ "    SUM(a.preyear) AS previousyear,\r\n" + "    SUM(a.curmonth) AS currentmonth,\r\n"
			+ "    SUM(a.premonth) AS previousmonth \r\n" + "FROM (\r\n" + "    SELECT \r\n"
			+ "        SUM(r.paymentamt) AS curyear,\r\n" + "        0 AS preyear,\r\n" + "        0 AS curmonth,\r\n"
			+ "        0 AS premonth,\r\n" + "        r.orgid \r\n" + "    FROM payment r\r\n"
			+ "    WHERE r.finyear =?2\r\n" + "        AND r.orgid =?1\r\n" + "        AND ?3 = 'YEAR'\r\n"
			+ "    GROUP BY r.orgid\r\n" + "\r\n" + "    UNION\r\n" + "\r\n" + "    SELECT \r\n"
			+ "        0 AS curyear,\r\n" + "        SUM(r.paymentamt) AS preyear,\r\n" + "        0 AS curmonth,\r\n"
			+ "        0 AS premonth,\r\n" + "        r.orgid \r\n" + "    FROM payment r\r\n"
			+ "    WHERE r.finyear = (?2 - 1)\r\n" + "        AND r.orgid =?1\r\n" + "        AND ?3 = 'YEAR'\r\n"
			+ "    GROUP BY r.orgid\r\n" + "\r\n" + "    UNION\r\n" + "\r\n" + "    SELECT \r\n"
			+ "        0 AS curyear,\r\n" + "        0 AS preyear,\r\n" + "        SUM(r.paymentamt) AS curmonth,\r\n"
			+ "        0 AS premonth,\r\n" + "        r.orgid \r\n" + "    FROM payment r\r\n"
			+ "    JOIN financialyear f ON f.finyear = r.finyear \r\n"
			+ "    WHERE r.docdate BETWEEN f.startdate AND f.enddate \r\n"
			+ "        AND MONTH(r.docdate) = MONTH(CURDATE()) \r\n" + "        AND CAST(r.finyear AS SIGNED) =?2\r\n"
			+ "        AND r.orgid =?1\r\n" + "         AND ?3 = 'MONTH'\r\n" + "    GROUP BY r.orgid\r\n" + "\r\n"
			+ "    UNION \r\n" + "SELECT \r\n" + "    0 AS curyear,\r\n" + "    0 AS preyear,\r\n"
			+ "    0 AS curmonth,\r\n" + "    SUM(r.paymentamt) AS premonth,\r\n" + "    r.orgid \r\n"
			+ "FROM payment r\r\n" + "JOIN financialyear f ON f.finyear = r.finyear \r\n"
			+ "WHERE r.docdate BETWEEN f.startdate AND f.enddate \r\n" + "  AND r.orgid =?1\r\n"
			+ "  AND r.finyear = (\r\n" + "      CASE \r\n" + "          WHEN MONTH(CURDATE()) = 4 THEN ?2 - 1 \r\n"
			+ "          ELSE ?2\r\n" + "      END\r\n" + "  )\r\n" + "  AND MONTH(r.docdate) = (\r\n"
			+ "      CASE \r\n"
			+ "          WHEN MONTH(CURDATE()) = 4 THEN  MONTH(CURDATE()) - 1 else    MONTH(CURDATE())    \r\n"
			+ "                          \r\n" + "      END\r\n" + "  )\r\n" + "GROUP BY r.orgid) a")
	Set<Object[]> getPercentageFromPayment(Long orgId, Long finYear, String month);

	@Query(nativeQuery = true, value = "SELECT \r\n" + "    p.partyname,\r\n" + "    p.partyshortname,\r\n"
			+ "    SUM(d.arapamount),\r\n" + "    MONTH(t.vdate)\r\n" + "FROM taxinvoice t\r\n"
			+ "JOIN accounts a ON t.vid = a.vid\r\n" + "JOIN accountsdetails d ON d.accountsid = a.accountsid\r\n"
			+ "JOIN partymaster p ON t.partycode = p.partycode\r\n" + "WHERE \r\n" + "    a.orgid = ?1\r \n"
			+ "    AND a.finyear =?2 \r\n" + "    AND a.branchcode =?3 \r\n" + "    AND t.partytype='CUSTOMER'\r\n"
			+ "GROUP BY \r\n" + "    p.partyname, \r\n" + "    MONTH(t.vdate), \r\n" + "    p.partyshortname\r\n"
			+ "ORDER BY \r\n" + "    MONTH(t.vdate)\r\n" + "")
	Set<Object[]> getSalesMonthWiseData(Long orgId, Long finYear, String branchCode);

	@Query(nativeQuery = true, value = "select monthnumber,monthname,totalamount from (\r\n" + "SELECT \r\n"
			+ "    MONTH(docdate) AS monthnumber,\r\n" + "    DATE_FORMAT(docdate, '%M') AS monthname,\r\n"
			+ "    SUM(amount) AS totalamount\r\n" + "FROM vw_revenue \r\n" + "WHERE  finyear =?2\r\n"
			+ "and orgid =?1\r\n" + "GROUP BY MONTH(docdate), DATE_FORMAT(docdate, '%M')\r\n"
			+ "ORDER BY MONTH(docdate)\r\n" + ")a  where monthname =?3")
	Set<Object[]> getRevenueMonthWiseRevenue(Long orgId, Long finYear, String monthName);

	@Query(nativeQuery = true, value = "select monthnumber,monthname,totalamount from (\r\n" + "SELECT \r\n"
			+ "    MONTH(docdate) AS monthnumber,\r\n" + "    DATE_FORMAT(docdate, '%M') AS monthname,\r\n"
			+ "    SUM(amount) AS totalamount\r\n" + "FROM vw_cost\r\n" + "WHERE  finyear =?2\r\n" + "and orgid =?1\r\n"
			+ "GROUP BY MONTH(docdate), DATE_FORMAT(docdate, '%M')\r\n" + "ORDER BY MONTH(docdate)\r\n"
			+ ")a  where monthname =?3")
	Set<Object[]> getRevenueMonthWiseCost(Long orgId, Long finYear, String monthName);

	@Query(nativeQuery = true, value = "select monthnumber,monthname,totalamount from (\r\n" + "SELECT \r\n"
			+ "    MONTH(docdate) AS monthnumber,\r\n" + "    DATE_FORMAT(docdate, '%M') AS monthname,\r\n"
			+ "    SUM(receiptamt) AS totalamount\r\n" + "FROM receipt\r\n" + "WHERE  finyear =?2\r\n"
			+ "and orgid =?1\r\n" + "GROUP BY MONTH(docdate), DATE_FORMAT(docdate, '%M')\r\n"
			+ "ORDER BY MONTH(docdate)\r\n" + ")a  where monthname =?3")
	Set<Object[]> getRevenueMonthWiseRecepit(Long orgId, Long finYear, String monthName);

	@Query(nativeQuery = true, value = "select monthnumber,monthname,totalamount from (\r\n" + "SELECT \r\n"
			+ "    MONTH(docdate) AS monthnumber,\r\n" + "    DATE_FORMAT(docdate, '%M') AS monthname,\r\n"
			+ "    SUM(paymentamt) AS totalamount\r\n" + "FROM payment\r\n" + "WHERE  finyear =?2\r\n"
			+ "and orgid =?1\r\n" + "GROUP BY MONTH(docdate), DATE_FORMAT(docdate, '%M')\r\n"
			+ "ORDER BY MONTH(docdate)\r\n" + ")a  where monthname =?3")
	Set<Object[]> getRevenueMonthWisePayment(Long orgId, Long finYear, String monthName);

//	@Query(nativeQuery = true,value="select * from taxinvoice a,irncreditnote a1  where a.orgid=a1.orgid and a.partycode=a1.partycode and a.docid=a1.originbillno\r\n"
//			+ " and a.approvestatus=a1.approvestatus and a.orgid=?1 and a.partyname=?2")
//	List<TaxInvoiceVO> getCheck(Long orgId, String party);

	@Query(nativeQuery = true, value = "SELECT \r\n" + "    a.transactionno,\r\n" + "    a.transactiondate,\r\n"
			+ "    m.kitid,\r\n" + "    m.kitname,\r\n" + "    m.kitqty\r\n" + "FROM \r\n" + "    mim a\r\n"
			+ "LEFT JOIN \r\n" + "    mimdetails m ON a.mimid = m.mimid\r\n" + "WHERE \r\n"
			+ "    a.transactionno NOT IN (\r\n" + "        SELECT transno FROM taxinvoiceannexure\r\n" + "    )\r\n"
			+ "    AND a.cancel = 0\r\n" + "    AND a.orgid = ?1\r\n" + "")
	Set<Object[]> getFillGridForTaxInvoice(Long orgId);

	@Query(nativeQuery = true, value = "SELECT transactionno\r\n" + "FROM taxinvoice\r\n"
			+ "WHERE (?3 IS NOT NULL AND docid = ?3)\r\n" + "\r\n" + "UNION\r\n" + "\r\n" + "SELECT a.transactionno\r\n"
			+ "FROM mim a\r\n" + "WHERE (\r\n" + "        ?3 IS NULL \r\n" + "        OR NOT EXISTS (\r\n"
			+ "            SELECT 1 \r\n" + "            FROM taxinvoice b \r\n" + "            WHERE b.docid = ?3\r\n"
			+ "        )\r\n" + "    )\r\n" + "AND a.cancel = 0\r\n" + "AND a.receiver = ?2\r\n"
			+ "AND a.orgid = ?1\r\n" + "AND a.transactionno NOT IN (\r\n" + "    SELECT ax.transno\r\n"
			+ "    FROM taxinvoiceannexure ax\r\n" + "    JOIN taxinvoice b ON ax.taxinvoiceid = b.taxinvoiceid\r\n"
			+ "    WHERE b.approvestatus = 'Approved'\r\n" + ")\r\n" + "")
	Set<Object[]> getMimFillGridgettransaction(Long orgId, String Receiver, String docId);

	@Query(nativeQuery = true, value = "SELECT a.transactionno, a.transactiondate, m.kitid, m.kitname, m.kitqty \r\n"
			+ "FROM mim a\r\n" + "LEFT JOIN mimdetails m ON a.mimid = m.mimid\r\n"
			+ "WHERE a.transactionno NOT IN (select  transno from taxinvoiceannexure a, taxinvoice b where a.taxinvoiceid = b.taxinvoiceid and approvestatus ='Approved')\r\n"
			+ "AND a.cancel = 0\r\n" + "AND a.orgid = ?1\r\n" + "AND FIND_IN_SET(a.transactionno, ?2)\r\n"
			+ "GROUP BY a.transactionno, a.transactiondate, m.kitid, m.kitname, m.kitqty, a.cancel")
	Set<Object[]> getMimFillGridgetKitDetails(Long orgId, String transactionNo);

	@Query(nativeQuery = true, value = "SELECT originbillno, vid, totalinvamountlc  FROM irncreditnote WHERE orgid =?1 AND originbillno =?2 AND approvestatus is Null")
	Set<Object[]> getOrginBillNoBased(Long orgId, String orginBillNo);

	@Query(nativeQuery = true, value = "select * from taxinvoice where screencode=?1 and docid=?2")
	TaxInvoiceVO getTaxInvoiceByDocIdandScreenCode(String screenCode, String docId);

//	@Query(nativeQuery = true, value = "SELECT \r\n" + "    a.finyear,\r\n" + "    a.Vid,\r\n" + "    a.Vdate,\r\n"
//			+ "    a.docid,\r\n" + "    a.docdate,\r\n" + "    a.invoiceno,\r\n" + "    a.invoicedate,\r\n"
//			+ "    a.gsttype,\r\n" + "    a.partyname,\r\n" + "    a.placeofsupply,\r\n"
//			+ "    a.totalchargeamountlc,\r\n" + "    a.totalinvamountlc,\r\n" + "    a.totaltaxamountlc,\r\n"
//			+ "    b.chargetype,\r\n" + "    b.chargecode,\r\n" + "    b.chargename,\r\n" + "    b.description,\r\n"
//			+ "    b.currency,\r\n" + "    b.gstpercent,\r\n" + "    b.qty,\r\n" + "    b.rate,\r\n"
//			+ "    b.taxable,\r\n" + "    b.billamount,\r\n" + "    b.gstamount,\r\n"
//			+ "    b.billamount + b.gstamount AS totalLcAmount,\r\n" + "    CASE \r\n"
//			+ "        WHEN a.approvestatus IS NULL THEN 'Not Appproved' \r\n" + "        ELSE a.approvestatus \r\n"
//			+ "    END AS approvestatus\r\n" + "FROM \r\n" + "    taxinvoice a,\r\n" + "    taxinvoicedetails b\r\n"
//			+ "WHERE \r\n" + "    a.taxinvoiceid = b.taxinvoiceid\r\n" + "    AND a.orgid = ?1\r\n"
//			+ "    AND (a.partyname = ?3 OR ?3 = 'ALL')\r\n" + "    AND a.finyear = ?2\r\n"
//			+ "    AND (?4 IS NULL OR a.vdate >= ?4)\r\n" + "    AND (?5 IS NULL OR a.vdate <= ?5)\r\n"
//			+ "    AND (a.branchcode = ?6 OR ?6 = 'ALL')\r\n" + "ORDER BY \r\n" + "    a.createdon DESC")
//	Set<Object[]> getTaxinvoiceDetails(Long orgId, String finYear, String partyname, String fromDate, String toDate,
//			String branchCode);
	
	@Query(nativeQuery = true, value = "select *\r\n"
			+ "from (\r\n"
			+ "    select \r\n"
			+ "        a.finyear,\r\n"
			+ "        a.vid,\r\n"
			+ "        a.vdate,\r\n"
			+ "        a.docid,\r\n"
			+ "        a.docdate,\r\n"
			+ "        a.invoiceno,\r\n"
			+ "        a.invoicedate,\r\n"
			+ "        a.gsttype,\r\n"
			+ "        a.partyname,\r\n"
			+ "        a.placeofsupply,\r\n"
			+ "\r\n"
			+ "        a.totalchargeamountlc,\r\n"
			+ "        a.totalinvamountlc,\r\n"
			+ "        a.totaltaxamountlc,\r\n"
			+ "\r\n"
			+ "        b.chargetype,\r\n"
			+ "        b.chargecode,\r\n"
			+ "        b.chargename,\r\n"
			+ "        b.description,\r\n"
			+ "        b.currency,\r\n"
			+ "        b.gstpercent,\r\n"
			+ "        b.qty,\r\n"
			+ "        b.rate,\r\n"
			+ "        b.taxable,\r\n"
			+ "        b.billamount,\r\n"
			+ "        b.gstamount,\r\n"
			+ "        (b.billamount + b.gstamount) AS totalLcAmount,\r\n"
			+ "\r\n"
			+ "        CASE \r\n"
			+ "            WHEN a.approvestatus IS null THEN 'Not Approved'\r\n"
			+ "            ELSE a.approvestatus\r\n"
			+ "        END AS approvestatus,\r\n"
			+ "\r\n"
			+ "        1 AS sNo,\r\n"
			+ "        a.createdon\r\n"
			+ "\r\n"
			+ "    from taxinvoice a\r\n"
			+ "    INNER JOIN taxinvoicedetails b \r\n"
			+ "        ON a.taxinvoiceid = b.taxinvoiceid\r\n"
			+ "\r\n"
			+ "    WHERE a.orgid = ?1\r\n"
			+ "      AND a.finyear = ?2\r\n"
			+ "    AND (a.partyname = ?3 OR ?3 = 'ALL')\r\n"
			+ "    AND (?4 IS NULL OR a.vdate >= ?4)\r\n"
			+ "    AND (?5 IS NULL OR a.vdate <= ?5)\r\n"
			+ "    AND (a.branchcode = ?6 OR ?6 = 'ALL')\r\n"
			+ "    UNION ALL\r\n"
			+ "    select \r\n"
			+ "        null AS finyear,\r\n"
			+ "        null AS vid,\r\n"
			+ "        null AS vdate,\r\n"
			+ "        'Total Amount' AS docid,\r\n"
			+ "        null AS docdate,\r\n"
			+ "        null AS invoiceno,\r\n"
			+ "        null AS invoicedate,\r\n"
			+ "        null AS gsttype,\r\n"
			+ "        null AS partyname,\r\n"
			+ "        null AS placeofsupply,\r\n"
			+ "\r\n"
			+ "        SUM(a.totalchargeamountlc),\r\n"
			+ "        SUM(a.totalinvamountlc),\r\n"
			+ "        SUM(a.totaltaxamountlc),\r\n"
			+ "\r\n"
			+ "        null AS chargetype,\r\n"
			+ "        null AS chargecode,\r\n"
			+ "        null AS chargename,\r\n"
			+ "        null AS description,\r\n"
			+ "        null AS currency,\r\n"
			+ "        null AS gstpercent,\r\n"
			+ "        null AS qty,\r\n"
			+ "        null AS rate,\r\n"
			+ "        null AS taxable,\r\n"
			+ "        SUM(b.billamount),\r\n"
			+ "        SUM(b.gstamount),\r\n"
			+ "        SUM(b.billamount + b.gstamount),\r\n"
			+ "\r\n"
			+ "        null AS approvestatus,\r\n"
			+ "\r\n"
			+ "        2 AS sNo,\r\n"
			+ "        null AS createdon\r\n"
			+ "\r\n"
			+ "    from taxinvoice a\r\n"
			+ "    INNER JOIN taxinvoicedetails b \r\n"
			+ "        ON a.taxinvoiceid = b.taxinvoiceid\r\n"
			+ "    WHERE a.orgid = ?1\r\n"
			+ "        AND a.finyear = ?2\r\n"
			+ "    AND (a.partyname = ?3 OR ?3 = 'ALL')\r\n"
			+ "    AND (?4 IS NULL OR a.vdate >= ?4)\r\n"
			+ "    AND (?5 IS NULL OR a.vdate <= ?5)\r\n"
			+ "    AND (a.branchcode = ?6 OR ?6 = 'ALL')\r\n"
			+ ") x\r\n"
			+ "ORDER BY \r\n"
			+ "    x.sNo,\r\n"
			+ "    x.createdon DESC")
	Set<Object[]> getTaxinvoiceDetails(Long orgId, String finYear, String partyname, String fromDate, String toDate,
			String branchCode);

//	@Query(nativeQuery = true, value = "SELECT \r\n" + "    a.finyear, \r\n" + "    a.Vid, \r\n" + "    a.Vdate, \r\n"
//			+ "    a.docid, \r\n" + "    a.docdate, \r\n" + "    a.invoiceno, \r\n" + "    a.invoicedate, \r\n"
//			+ "    a.gsttype,\r\n" + "    a.partyname, \r\n" + "    a.placeofsupply, \r\n"
//			+ "    a.totalchargeamountlc, \r\n" + "    a.totalinvamountlc, \r\n"
//			+ "   cast( case when a.gsttype='INTER' then a.totaltaxamountlc else 0 end as decimal(18,2)) as igst,\r\n"
//			+ "   cast( case when a.gsttype='INTRA' then a.totaltaxamountlc/2  else 0 end as decimal(18,2) ) as cgst,\r\n"
//			+ "	cast(case when a.gsttype='INTRA' then a.totaltaxamountlc/2 else 0  end as decimal(18,2) ) as sgst ,\r\n"
//			+ "    case when\r\n"
//			+ "    a.approvestatus is null then 'Not Appproved' else a.approvestatus end as approvestatus\r\n"
//			+ "FROM \r\n" + "    taxinvoice a\r\n" + "WHERE \r\n" + "    a.orgid = ?1\r\n"
//			+ "    AND (a.partyname = ?3 OR ?3 = 'ALL')\r\n" + "    AND a.finyear = ?2\r\n"
//			+ "    AND (?4 IS NULL OR a.vdate >= ?4)\r\n" + "    AND (?5 IS NULL OR a.vdate <= ?5)\r\n"
//			+ "    AND (a.branchcode = ?6 OR ?6 = 'ALL')\r\n" + "ORDER BY \r\n" + "    a.createdon DESC")
//	Set<Object[]> getTaxinvoiceSummary(Long orgId, String finYear, String partyname, String fromDate, String toDate,
//			String branchCode);
	
	@Query(nativeQuery = true, value = "SELECT *\r\n"
			+ "FROM (\r\n"
			+ "\r\n"
			+ "    /* ================= DETAIL ================= */\r\n"
			+ "    SELECT \r\n"
			+ "        a.finyear,\r\n"
			+ "        a.vid,\r\n"
			+ "        a.vdate,\r\n"
			+ "        a.docid,\r\n"
			+ "        a.docdate,\r\n"
			+ "        a.invoiceno,\r\n"
			+ "        a.invoicedate,\r\n"
			+ "        a.gsttype,\r\n"
			+ "        a.partyname,\r\n"
			+ "        a.placeofsupply,\r\n"
			+ "\r\n"
			+ "        a.totalchargeamountlc,\r\n"
			+ "        a.totalinvamountlc,\r\n"
			+ "\r\n"
			+ "        CAST(CASE \r\n"
			+ "                WHEN a.gsttype = 'INTER' \r\n"
			+ "                THEN a.totaltaxamountlc \r\n"
			+ "                ELSE 0 \r\n"
			+ "             END AS DECIMAL(18,2)) AS igst,\r\n"
			+ "\r\n"
			+ "        CAST(CASE \r\n"
			+ "                WHEN a.gsttype = 'INTRA' \r\n"
			+ "                THEN a.totaltaxamountlc / 2 \r\n"
			+ "                ELSE 0 \r\n"
			+ "             END AS DECIMAL(18,2)) AS cgst,\r\n"
			+ "\r\n"
			+ "        CAST(CASE \r\n"
			+ "                WHEN a.gsttype = 'INTRA' \r\n"
			+ "                THEN a.totaltaxamountlc / 2 \r\n"
			+ "                ELSE 0 \r\n"
			+ "             END AS DECIMAL(18,2)) AS sgst,\r\n"
			+ "\r\n"
			+ "        CASE \r\n"
			+ "            WHEN a.approvestatus IS NULL THEN 'Not Approved'\r\n"
			+ "            ELSE a.approvestatus\r\n"
			+ "        END AS approvestatus,\r\n"
			+ "\r\n"
			+ "        1 AS sNo,\r\n"
			+ "        a.createdon\r\n"
			+ "\r\n"
			+ "    FROM taxinvoice a\r\n"
			+ "    WHERE \r\n"
			+ "        a.orgid = ?1\r\n"
			+ "        AND (a.partyname = ?3 OR ?3 = 'ALL')\r\n"
			+ "        AND a.finyear = ?2\r\n"
			+ "        AND (?4 IS NULL OR a.vdate >= ?4)\r\n"
			+ "        AND (?5 IS NULL OR a.vdate <= ?5)\r\n"
			+ "        AND (a.branchcode = ?6 OR ?6 = 'ALL')\r\n"
			+ "\r\n"
			+ "\r\n"
			+ "    UNION ALL\r\n"
			+ "\r\n"
			+ "\r\n"
			+ "    /* ================= TOTAL ================= */\r\n"
			+ "    SELECT \r\n"
			+ "        NULL AS finyear,\r\n"
			+ "        NULL AS vid,\r\n"
			+ "        NULL AS vdate,\r\n"
			+ "        'Total Amount' AS docid,\r\n"
			+ "        NULL AS docdate,\r\n"
			+ "        NULL AS invoiceno,\r\n"
			+ "        NULL AS invoicedate,\r\n"
			+ "        NULL AS gsttype,\r\n"
			+ "        NULL AS partyname,\r\n"
			+ "        NULL AS placeofsupply,\r\n"
			+ "\r\n"
			+ "        SUM(a.totalchargeamountlc),\r\n"
			+ "        SUM(a.totalinvamountlc),\r\n"
			+ "\r\n"
			+ "        /* IGST TOTAL */\r\n"
			+ "        CAST(SUM(CASE \r\n"
			+ "                    WHEN a.gsttype = 'INTER' \r\n"
			+ "                    THEN a.totaltaxamountlc \r\n"
			+ "                    ELSE 0 \r\n"
			+ "                 END) AS DECIMAL(18,2)),\r\n"
			+ "\r\n"
			+ "        /* CGST TOTAL */\r\n"
			+ "        CAST(SUM(CASE \r\n"
			+ "                    WHEN a.gsttype = 'INTRA' \r\n"
			+ "                    THEN a.totaltaxamountlc / 2 \r\n"
			+ "                    ELSE 0 \r\n"
			+ "                 END) AS DECIMAL(18,2)),\r\n"
			+ "\r\n"
			+ "        /* SGST TOTAL */\r\n"
			+ "        CAST(SUM(CASE \r\n"
			+ "                    WHEN a.gsttype = 'INTRA' \r\n"
			+ "                    THEN a.totaltaxamountlc / 2 \r\n"
			+ "                    ELSE 0 \r\n"
			+ "                 END) AS DECIMAL(18,2)),\r\n"
			+ "\r\n"
			+ "        NULL AS approvestatus,\r\n"
			+ "\r\n"
			+ "        2 AS sNo,\r\n"
			+ "        NULL AS createdon\r\n"
			+ "\r\n"
			+ "    FROM taxinvoice a\r\n"
			+ "    WHERE \r\n"
			+ "        a.orgid = ?1\r\n"
			+ "        AND (a.partyname = ?3 OR ?3 = 'ALL')\r\n"
			+ "        AND a.finyear = ?2\r\n"
			+ "        AND (?4 IS NULL OR a.vdate >= ?4)\r\n"
			+ "        AND (?5 IS NULL OR a.vdate <= ?5)\r\n"
			+ "        AND (a.branchcode = ?6 OR ?6 = 'ALL')\r\n"
			+ "\r\n"
			+ ") x\r\n"
			+ "\r\n"
			+ "ORDER BY \r\n"
			+ "    x.sNo,\r\n"
			+ "    x.createdon DESC")
	Set<Object[]> getTaxinvoiceSummary(Long orgId, String finYear, String partyname, String fromDate, String toDate,
			String branchCode);

	@Query(nativeQuery = true, value = "select p.currency,d.sellingexrate from partymaster p ,vw_exrates d where \r\n"
			+ " p.currency=d.currency  and p.orgid=d.orgid and p.orgid=?1\r\n" + " and  p.partycode=?2")
	Set<Object[]> getCurrencyFromPartyMaster(Long orgId, String partyCode);

	@Query(nativeQuery = true, value = "select 1 as sno, a.vid,a.vdate,a.refno,a.refdate,c.partytype,c.partycode,c.partyname,c.recipientgstin,d.currency,d.exrate,d.gstpercent,sum(d.gstamount) as gstAmount,sum(lcamount) as chargeAmount ,sum(d.billamount) as billAmount,c.totalinvamountlc as totalAmountLc from accountsdetails b, accounts a,taxinvoice c,taxinvoicedetails d where \r\n"
			+ " a.accountsid=b.accountsid and a.refno=c.docid and c.cancel=0  and b.acategory='TAX' and c.taxinvoiceid=d.taxinvoiceid and a.sourcescreencode in('TI') and c.finyear=?3 and  a.docdate between ?4 and ?5 and (c.partyname=?2 or ?2='ALL') and c.orgid=?1\r\n"
			+ "group by a.vid,a.vdate,a.refno,a.refdate,c.partytype,c.partycode,c.partyname,c.recipientgstin,d.currency,d.exrate,d.gstpercent,c.totalinvamountlc \r\n"
			+ " union\r\n"
			+ "select 2 as sno,a.vid,a.vdate,a.refno,a.refdate,c.partytype,c.partycode,c.partyname,c.recipientgstin,d.currency,d.exrate,d.gstpercent,sum(d.gstamount) as gstAmount,sum(lcamount) as chargeAmount ,sum(d.billamount) as billAmount,c.totalinvamountlc as totalAmountLc from accountsdetails b, accounts a,irncreditnote c,irncreditnotedetails d where \r\n"
			+ " a.accountsid=b.accountsid and a.refno=c.docid and c.cancel=0  and b.acategory='TAX' and c.irncreditnoteid=d.irncreditnoteid and a.sourcescreencode in('ICN') and c.finyear=?3 and  a.docdate between ?4 and ?5 and (c.partyname=?2 or ?2='ALL') and c.orgid=?1\r\n"
			+ "group by a.vid,a.vdate,a.refno,a.refdate,c.partytype,c.partycode,c.partyname,c.recipientgstin,d.currency,d.exrate,d.gstpercent,c.totalinvamountlc\r\n"
			+ "union\r\n" + "SELECT \r\n" + "    3 as sno,\r\n" + "    'TotalAmount' as vid,\r\n"
			+ "    '' as vdate,\r\n" + "    '' as refno,\r\n" + "    '' as refdate,\r\n" + "    '' as partytype,\r\n"
			+ "    '' as partycode,\r\n" + "    '' as partyname,\r\n" + "    '' as recipientgstin,\r\n"
			+ "    '' as currency,\r\n" + "    '' as exrate,\r\n" + "    '' as gstpercent,\r\n"
			+ "    SUM(gstAmount),\r\n" + "    SUM(chargeAmount),\r\n" + "    SUM(billAmount),\r\n"
			+ "    SUM(totalAmountLc)\r\n" + "FROM (\r\n"
			+ "    select 1 as sno, a.vid,a.vdate,a.refno,a.refdate,c.partytype,c.partycode,c.partyname,\r\n"
			+ "           c.recipientgstin,d.currency,d.exrate,d.gstpercent,\r\n"
			+ "           sum(d.gstamount) as gstAmount,\r\n" + "           sum(lcamount) as chargeAmount,\r\n"
			+ "           sum(d.billamount) as billAmount,\r\n" + "           c.totalinvamountlc as totalAmountLc\r\n"
			+ "    from accountsdetails b, accounts a,taxinvoice c,taxinvoicedetails d \r\n"
			+ "    where a.accountsid=b.accountsid \r\n" + "      and a.refno=c.docid \r\n"
			+ "      and c.cancel=0  \r\n" + "      and b.acategory='TAX' \r\n"
			+ "      and c.taxinvoiceid=d.taxinvoiceid \r\n" + "      and a.sourcescreencode in('TI') \r\n"
			+ "      and c.finyear=?3 \r\n" + "      and a.docdate between ?4 and ?5 \r\n"
			+ "      and (c.partyname=?2 or ?2='ALL')\r\n" + "      and c.orgid=?1\r\n"
			+ "    group by a.vid,a.vdate,a.refno,a.refdate,c.partytype,c.partycode,\r\n"
			+ "             c.partyname,c.recipientgstin,d.currency,d.exrate,d.gstpercent,\r\n"
			+ "             c.totalinvamountlc \r\n" + "\r\n" + "    UNION ALL\r\n" + "\r\n"
			+ "    select 2 as sno, a.vid,a.vdate,a.refno,a.refdate,c.partytype,c.partycode,c.partyname,\r\n"
			+ "           c.recipientgstin,d.currency,d.exrate,d.gstpercent,\r\n"
			+ "           sum(d.gstamount) as gstAmount,\r\n" + "           sum(lcamount) as chargeAmount,\r\n"
			+ "           sum(d.billamount) as billAmount,\r\n" + "           c.totalinvamountlc as totalAmountLc\r\n"
			+ "    from accountsdetails b, accounts a,irncreditnote c,irncreditnotedetails d \r\n"
			+ "    where a.accountsid=b.accountsid \r\n" + "      and a.refno=c.docid \r\n"
			+ "      and c.cancel=0  \r\n" + "      and b.acategory='TAX' \r\n"
			+ "      and c.irncreditnoteid=d.irncreditnoteid \r\n" + "      and a.sourcescreencode in('ICN') \r\n"
			+ "      and c.finyear=?3 \r\n" + "      and a.docdate between ?4 and ?5 \r\n"
			+ "      and (c.partyname=?2 or ?2='ALL')\r\n" + "      and c.orgid=?1\r\n"
			+ "    group by a.vid,a.vdate,a.refno,a.refdate,c.partytype,c.partycode,\r\n"
			+ "             c.partyname,c.recipientgstin,d.currency,d.exrate,d.gstpercent,\r\n"
			+ "             c.totalinvamountlc \r\n" + ") s\r\n" + " order by 1,2,3")
	Set<Object[]> getRevenueGstReport(Long orgId, String partyName, String finYear, String fromDate, String toDate);

	@Query(nativeQuery = true, value = "WITH income_data AS (\r\n" + "    SELECT g.accountgroupname,\r\n"
			+ "           n.groupname,\r\n" + "           COALESCE(SUM(a.creditamount), 0) AS totalamount\r\n"
			+ "    FROM groupledger g\r\n" + "    JOIN accountsdetails a ON a.accountname = g.accountgroupname\r\n"
			+ "    JOIN accounts a1 ON a1.accountsid = a.accountsid\r\n"
			+ "    JOIN taxinvoice t ON t.docid = a1.refno AND t.cancel = 0\r\n"
			+ "    JOIN groupledger n ON g.accountgroupname = n.accountgroupname\r\n"
			+ "    WHERE g.coalist = 'INCOME'\r\n" + "      AND g.type = 'ACCOUNT'\r\n"
			+ "      AND a1.docdate BETWEEN ?2 AND ?3\r\n" + "      AND a1.orgid =?1\r\n"
			+ "    GROUP BY g.accountgroupname, n.groupname\r\n" + "),\r\n" + "expense_data AS (\r\n"
			+ "    SELECT g.accountgroupname,\r\n" + "           n.groupname,\r\n"
			+ "           COALESCE(SUM(a.debitamount), 0) AS totalamount\r\n" + "    FROM groupledger g\r\n"
			+ "    JOIN accountsdetails a ON a.accountname = g.accountgroupname\r\n"
			+ "    JOIN accounts a1 ON a1.accountsid = a.accountsid\r\n"
			+ "    LEFT JOIN costinvoice t ON t.docid = a1.refno\r\n"
			+ "    LEFT JOIN urcostinvoicegna u ON u.docid = a1.refno\r\n"
			+ "    LEFT JOIN rcostinvoicegna r ON r.docid = a1.refno\r\n"
			+ "    JOIN groupledger n ON g.accountgroupname = n.accountgroupname\r\n"
			+ "    WHERE g.coalist = 'EXPENSE'\r\n" + "      AND g.type = 'ACCOUNT'\r\n"
			+ "      AND a1.docdate BETWEEN ?2 AND ?3\r\n" + "      AND a1.orgid = ?1\r\n"
			+ "      AND t.cancel = 0\r\n" + "    GROUP BY g.accountgroupname, n.groupname\r\n" + ")\r\n" + "(\r\n"
			+ "    -- Heading: Income\r\n" + "    SELECT 'Income' AS groupname, NULL AS totalamount FROM dual\r\n"
			+ "    \r\n" + "    UNION ALL\r\n" + "    \r\n" + "    -- Income groups\r\n"
			+ "    SELECT groupname, SUM(totalamount) FROM income_data GROUP BY groupname\r\n" + "    \r\n"
			+ "    UNION ALL\r\n" + "    \r\n" + "    -- Total Income\r\n"
			+ "    SELECT 'totalIncome', SUM(totalamount) FROM income_data\r\n" + "    \r\n" + "    UNION ALL\r\n"
			+ "    \r\n" + "    -- Empty line\r\n" + "    SELECT NULL, NULL FROM dual\r\n" + "    \r\n"
			+ "    UNION ALL\r\n" + "    \r\n" + "    -- Heading: Expense\r\n"
			+ "    SELECT 'Expense', NULL FROM dual\r\n" + "    \r\n" + "    UNION ALL\r\n" + "    \r\n"
			+ "    -- Expense groups\r\n"
			+ "    SELECT groupname, SUM(totalamount) FROM expense_data GROUP BY groupname\r\n" + "    \r\n"
			+ "    UNION ALL\r\n" + "    \r\n" + "    -- Total Expense\r\n"
			+ "    SELECT 'totalExpense', SUM(totalamount) FROM expense_data\r\n" + "    \r\n" + "    UNION ALL\r\n"
			+ "    \r\n" + "    -- Net Profit (Total Income - Total Expense)\r\n" + "    SELECT 'netProfit',\r\n"
			+ "           (SELECT COALESCE(SUM(totalamount),0) FROM income_data)\r\n"
			+ "           - (SELECT COALESCE(SUM(totalamount),0) FROM expense_data))")
	Set<Object[]> getProfitAndLossReport(Long orgId, String fromDate, String toDate);

	@Query(nativeQuery = true, value = "WITH income AS (\r\n" + "    SELECT g.accountgroupname,\r\n"
			+ "           SUM(a.creditamount) AS totalamount\r\n" + "    FROM groupledger g\r\n"
			+ "    JOIN accountsdetails a ON a.accountname = g.accountgroupname\r\n"
			+ "    JOIN accounts a1 ON a1.accountsid = a.accountsid\r\n"
			+ "    JOIN taxinvoice t ON t.docid = a1.refno AND t.cancel = 0\r\n" + "    WHERE g.coalist = 'INCOME'\r\n"
			+ "      AND g.type = 'ACCOUNT'\r\n" + "      AND a1.docdate BETWEEN ?2 AND ?3\r\n"
			+ "      AND a1.orgid =?1\r\n" + "    GROUP BY g.accountgroupname\r\n" + "),\r\n" + "expense AS (\r\n"
			+ "    SELECT g.accountgroupname,\r\n" + "           SUM(a.debitamount) AS totalamount\r\n"
			+ "    FROM groupledger g\r\n" + "    JOIN accountsdetails a ON a.accountname = g.accountgroupname\r\n"
			+ "    JOIN accounts a1 ON a1.accountsid = a.accountsid\r\n"
			+ "    LEFT JOIN costinvoice t ON t.docid = a1.refno\r\n"
			+ "    LEFT JOIN urcostinvoicegna u ON u.docid = a1.refno\r\n"
			+ "    LEFT JOIN rcostinvoicegna r ON r.docid = a1.refno\r\n" + "    WHERE g.coalist = 'EXPENSE'\r\n"
			+ "      AND g.type = 'ACCOUNT'\r\n" + "      AND a1.docdate BETWEEN ?2 AND ?3\r\n"
			+ "      AND a1.orgid =?1\r\n" + "      AND t.cancel = 0\r\n" + "    GROUP BY g.accountgroupname\r\n"
			+ "),\r\n" + "totals AS (\r\n"
			+ "    SELECT 'Income Total' AS accountgroupname, SUM(totalamount) AS totalamount FROM income\r\n"
			+ "    UNION ALL\r\n" + "    SELECT 'Expense Total', SUM(totalamount) FROM expense\r\n" + "),\r\n"
			+ "profit AS (\r\n" + "    SELECT 'Profit/Loss' AS accountgroupname,\r\n"
			+ "           COALESCE((SELECT SUM(totalamount) FROM income),0) -\r\n"
			+ "           COALESCE((SELECT SUM(totalamount) FROM expense),0) AS totalamount\r\n" + ")\r\n"
			+ "SELECT  \r\n" + "       l.totalamount,\r\n" + "	n.groupname,\r\n" + "l.accountgroupname\r\n"
			+ "FROM (\r\n" + "    SELECT * FROM income\r\n" + "    UNION ALL\r\n" + "    SELECT * FROM expense\r\n"
			+ "    UNION ALL\r\n" + "    SELECT * FROM totals\r\n" + "    UNION ALL\r\n"
			+ "    SELECT * FROM profit\r\n" + ") l\r\n"
			+ "JOIN groupledger n ON l.accountgroupname = n.accountgroupname\r\n" + "WHERE n.groupname =?4\r\n"
			+ "ORDER BY l.accountgroupname")
	Set<Object[]> getNetProfit(Long orgId, String fromDate, String toDate, String groupName);

	@Query(nativeQuery = true, value = "SELECT \r\n" + "    groupname,\r\n" + "    accountcode,\r\n"
			+ "    accountgroupname,\r\n" + "    partycode,\r\n" + "    partyname,\r\n" + "\r\n"
			+ "    -- Opening Debit / Credit\r\n" + "    CASE \r\n"
			+ "        WHEN (SUM(odbamount) - SUM(ocramount)) > 0 \r\n"
			+ "        THEN SUM(odbamount) - SUM(ocramount) \r\n" + "        ELSE 0 \r\n" + "    END AS odbamount,\r\n"
			+ "\r\n" + "    CASE \r\n" + "        WHEN (SUM(odbamount) - SUM(ocramount)) > 0 \r\n"
			+ "        THEN 0 \r\n" + "        ELSE ABS(SUM(odbamount) - SUM(ocramount)) \r\n"
			+ "    END AS ocramount,\r\n" + "\r\n" + "    -- Transaction Debit / Credit\r\n"
			+ "    SUM(tdbamount) AS tdbamount,\r\n" + "    SUM(tcramount) AS tcramount,\r\n" + "\r\n"
			+ "    -- Closing Debit / Credit\r\n" + "    CASE \r\n"
			+ "        WHEN (SUM(odbamount) - SUM(ocramount) + SUM(tdbamount) - SUM(tcramount)) > 0 \r\n"
			+ "        THEN (SUM(odbamount) - SUM(ocramount) + SUM(tdbamount) - SUM(tcramount)) \r\n"
			+ "        ELSE 0 \r\n" + "    END AS cdbamount,\r\n" + "\r\n" + "    CASE \r\n"
			+ "        WHEN (SUM(odbamount) - SUM(ocramount) + SUM(tdbamount) - SUM(tcramount)) > 0 \r\n"
			+ "        THEN 0 \r\n"
			+ "        ELSE ABS(SUM(odbamount) - SUM(ocramount) + SUM(tdbamount) - SUM(tcramount)) \r\n"
			+ "    END AS ccramount\r\n" + "\r\n" + "FROM (\r\n" + "    -- Opening Balances\r\n" + "    SELECT \r\n"
			+ "        ac.accountgroupname AS groupname,\r\n" + "        c.accountcode,\r\n"
			+ "        c.accountgroupname,\r\n"
			+ "        CASE WHEN UPPER(?6) = 'YES' THEN d.partycode ELSE 'None' END AS partycode,\r\n"
			+ "        CASE WHEN UPPER(?6) = 'YES' THEN d.partyname ELSE 'None' END AS partyname,\r\n"
			+ "        SUM(b.bdebitamount) AS odbamount,\r\n" + "        SUM(b.bcreditamount) AS ocramount,\r\n"
			+ "        0 AS tdbamount,\r\n" + "        0 AS tcramount\r\n" + "    FROM accounts a\r\n"
			+ "    JOIN accountsdetails b ON a.accountsid = b.accountsid\r\n"
			+ "    JOIN groupledger c ON b.accountname = c.accountgroupname\r\n"
			+ "    JOIN partymaster d ON b.subledgercode = d.partycode\r\n"
			+ "    JOIN branch e ON a.branch = e.branch\r\n" + "    JOIN financialyear f ON a.finyear = f.finyear\r\n"
			+ "    JOIN groupledger ac ON c.groupname = ac.groupname\r\n" + "    WHERE \r\n"
			+ "       a.cancel = 'F'\r\n" + "      AND a.docdate < ?3\r\n" + "      AND e.branch =?1\r\n"
			+ "       and a.orgid=?5\r\n"
			+ "    GROUP BY ac.accountgroupname, c.accountcode, c.accountgroupname, d.partycode, d.partyname\r\n"
			+ "\r\n" + "    UNION ALL\r\n" + "\r\n" + "    -- Current Transactions\r\n" + "    SELECT \r\n"
			+ "        ac.accountgroupname AS groupname,\r\n" + "        c.accountcode,\r\n"
			+ "        c.accountgroupname,\r\n"
			+ "        CASE WHEN UPPER(?6) = 'YES' THEN d.partycode ELSE 'None' END AS partycode,\r\n"
			+ "        CASE WHEN UPPER(?6) = 'YES' THEN d.partyname ELSE 'None' END AS partyname,\r\n"
			+ "        0 AS odbamount,\r\n" + "        0 AS ocramount,\r\n"
			+ "        SUM(b.bdebitamount) AS tdbamount,\r\n" + "        SUM(b.bcreditamount) AS tcramount\r\n"
			+ "    FROM accounts a\r\n" + "    JOIN accountsdetails b ON a.accountsid = b.accountsid\r\n"
			+ "    JOIN groupledger c ON b.accountname = c.accountgroupname\r\n"
			+ "    JOIN partymaster d ON b.subledgercode = d.partycode\r\n"
			+ "    JOIN branch e ON a.branch = e.branch\r\n" + "    JOIN financialyear f ON a.finyear = f.finyear\r\n"
			+ "    JOIN groupledger ac ON c.groupname = ac.groupname\r\n" + "    WHERE\r\n"
			+ "       a.cancel = 'F'\r\n" + "      AND a.docdate BETWEEN ?3 AND ?4\r\n" + "      AND a.finyear =?2 \r\n"
			+ "      AND e.branch =?1\r\n" + "      and a.orgid=?5\r\n" + "\r\n"
			+ "    GROUP BY ac.accountgroupname, c.accountcode, c.accountgroupname, d.partycode, d.partyname\r\n"
			+ ") t\r\n" + "GROUP BY groupname, accountcode, accountgroupname, partycode, partyname\r\n"
			+ "ORDER BY groupname, accountcode")
	Set<Object[]> getTrailBalance(String branch, String finYear, String fromDate, String toDate, Long orgId,
			String details);

	@Query(nativeQuery = true, value = " select sum(t.complete) complete, sum(t.Approved)Approved,sum(t.Pending)Pending,sum(t.Reject)Reject from(\r\n"
			+ "select count(*) complete, 0 Approved, 0 Pending,0 Reject from taxinvoice where orgid= ?1 and finyear=?2 and branchcode=?3  and cancel=0 \r\n"
			+ "union  all\r\n"
			+ "select 0 complete, count(*) Approved,0  Pending,0 Reject from taxinvoice where orgid= ?1 and finyear=?2  and branchcode=?3 and  cancel=0 and\r\n"
			+ " approvestatus='APPROVED'\r\n" + " union all\r\n"
			+ " select 0 complete, 0 Approved, count(*) Pending,0 Reject from taxinvoice where orgid= ?1 and finyear=?2  and branchcode=?3 and cancel=0 and\r\n"
			+ " status='PROFOMA'\r\n" + " union all\r\n"
			+ "select 0 complete, 0 Approved, 0 Pending,count(*) Reject from taxinvoice where orgid= ?1 and finyear=?2  and branchcode=?3 and  cancel=0 and\r\n"
			+ " approvestatus='REJECTED')t")
	Set<Object[]> getTaxInvoiceCount(Long orgId, String finYear, String branchCode);

	@Query(nativeQuery = true, value = "select startdate,enddate  from financialyear where orgid=?1  and finyear=?2 and active=1 and closed =0")
	Set<Object[]> getFinYearDetails(Long orgId, Long finYear);

}
