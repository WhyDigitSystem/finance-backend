package com.base.basesetup.repo;

import java.util.List;
import java.util.Map;
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

	@Query(nativeQuery = true,value="select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
	String getTaxInvoiceDocId(Long orgId, String finYear, String branchCode, String screenCode);

	@Query(nativeQuery = true,value="select currency,currencydescripition,buyingexrate,sellingexrate,ROW_NUMBER() OVER (ORDER BY currency) AS id from vw_exrates where orgid=?1")
	Set<Object[]> getCurrencyAndExrateDetails(Long orgId);

	@Query(nativeQuery = true,value = "select a.statecode,a.gstin,concat(a.stateno,' - ',a.state)stateno from partystate a,partymaster b where a.partymasterid=b.partymasterid and b.orgid=?1 and b.partymasterid=?2\r\n"
			+ "group by a.statecode,a.gstin,concat(a.stateno,' - ',a.state)")
	Set<Object[]> getStateCodeDetails(Long orgId, Long id);

	@Query(nativeQuery = true,value = "SELECT a.businessplace FROM partyaddress a,partymaster b,state c where  a.partymasterid=b.partymasterid and a.state=c.state and b.orgid=?1 and a.partymasterid=?2 and c.statecode=?3\r\n"
			+ "group by businessplace")
	Set<Object[]> getPlaceOfSupplyDetails(Long orgId, Long id,String stateCode);

	@Query(nativeQuery = true,value = "SELECT a.addresstype,concat(a.addressline1,',',a.addressline2,',',a.addressline3) address,a.pincode FROM partyaddress a,partymaster b,state c where  a.partymasterid=b.partymasterid and a.state=c.state and b.orgid=?1 and a.partymasterid=?2 and c.statecode=?3 and a.businessplace=?4\r\n"
			+ "group by a.addresstype,concat(a.addressline1,',',a.addressline2,',',a.addressline3),a.pincode")
	Set<Object[]> getAddressDetails(Long orgId, Long id,String stateCode,String placeOfSupply);
	
	@Query(nativeQuery = true,value = "SELECT \r\n"
			+ "       CASE \r\n"
			+ "           WHEN statecode = ?3 THEN 'INTRA'\r\n"
			+ "           ELSE 'INTER'\r\n"
			+ "       END AS transactionType\r\n"
			+ "FROM branch\r\n"
			+ "WHERE orgid = ?1 \r\n"
			+ "  AND branchcode = ?2")
	Set<Object[]> getGstType(Long orgId, String branchCode,String stateCode);

	@Query(value = "select a from TaxInvoiceVO a where a.orgId=?1 and a.partyName=?2 and a.branchCode=?3 and a.approveStatus='Approved' order by a.docId desc")
	List<TaxInvoiceVO> findPartyInvoiceDetails(Long orgId, String party, String branchCode);

	TaxInvoiceVO findByOrgIdAndIdAndDocId(Long orgId, Long id, String docId);

	@Query(nativeQuery = true,value = "select a.creditdays from partymaster a where a.orgid=?1 and partycode=?2 and a.active=1 ")
	Set<Object[]> findCreditDaysFromCustomer(Long orgId, String customerCode);

	@Query(nativeQuery = true,value = "select a.jobno from jobcard a where a.orgid=?1 and a.closed=0 and a.active=1 ")
	Set<Object[]> getAllJobNoByActice(Long orgId);

	@Query(nativeQuery = true,value = "select * from taxinvoice a where a.orgid=?1 and docid=?2 ")
	TaxInvoiceVO findByOrgIdAndDocId(Long orgId, String originBillNo);

	@Query(nativeQuery = true,value = "select a.jobno from jobcard a where a.orgid=?1 and a.customercode=?2 and active=1 ")
	Set<Object[]> getJobCardForTaxInvoice(Long orgId, String partyCode);

	boolean existsByvIdAndOrgId(String vId, Long orgId);

	@Query(nativeQuery = true,value = "SELECT \r\n"
			+ "    a.orgid,\r\n"
			+ "    a.branchcode,\r\n"
			+ "    a.vid,\r\n"
			+ "    a.vdate,\r\n"
			+ "    a.joborderno,\r\n"
			+ "    c.docid AS voucherno,\r\n"
			+ "    c.docdate AS voucherdate,\r\n"
			+ "    e.partyshortname AS billtoparty,\r\n"
			+ "    e.controllingoff,\r\n"
			+ "    a.billcurr,\r\n"
			+ "    a.billcurrrate,\r\n"
			+ "    a.totalinvamountbc,\r\n"
			+ "    a.totalinvamountlc,\r\n"
			+ "    a.totaltaxableamountlc,\r\n"
			+ "    a.gsttype,\r\n"
			+ "    a.totaltaxamountlc,\r\n"
			+ "    a.totaltaxamountbc,\r\n"
			+ "    a.roundoffamountlc,\r\n"
			+ "    SUM(b.fcamount) AS fcamt,\r\n"
			+ "    SUM(b.lcamount) AS lcamt,\r\n"
			+ "    SUM(b.rate) AS rate,\r\n"
			+ "    SUM(b.billamount) AS billamount,\r\n"
			+ "    a.partytype,a.docid,a.docdate,a.screencode\r\n"
			+ "FROM\r\n"
			+ "    taxinvoice a\r\n"
			+ "JOIN\r\n"
			+ "    taxinvoicedetails b ON a.taxinvoiceid = b.taxinvoiceid\r\n"
			+ "JOIN\r\n"
			+ "    accounts c ON a.docid = c.refno\r\n"
			+ "JOIN\r\n"
			+ "    partymaster e ON a.partycode = e.partycode\r\n"
			+ "WHERE\r\n"
			+ "   (\r\n"
			+ "        (?1 IS NULL OR ?1 = '' OR ?2 IS NULL OR ?2 = '') \r\n"
			+ "        OR c.vdate BETWEEN STR_TO_DATE(?1, '%Y-%m-%d') AND STR_TO_DATE(?2, '%Y-%m-%d')\r\n"
			+ "    )\r\n"
			+ "    AND a.orgid = ?3\r\n"
			+ "    AND (a.branchcode = ?4 OR ?4 = 'ALL')\r\n"
			+ "    AND (e.partycode = ?5 OR ?5 = 'ALL') and a.finyear=?6\r\n"
			+ "GROUP BY\r\n"
			+ "    a.orgid, a.branchcode, a.vid, a.vdate, a.joborderno, c.docid, c.docdate, \r\n"
			+ "    e.partyshortname, e.controllingoff, a.billcurr, a.billcurrrate, a.totalinvamountbc, \r\n"
			+ "    a.totalinvamountlc, a.totaltaxableamountlc, a.gsttype, a.totaltaxamountlc, \r\n"
			+ "    a.totaltaxamountbc, a.roundoffamountlc, a.partytype,a.docid,a.docdate,a.screencode\r\n"
			+ "\r\n"
			+ "UNION\r\n"
			+ "\r\n"
			+ "SELECT\r\n"
			+ "    a.orgid,\r\n"
			+ "    a.branchcode,\r\n"
			+ "    a.vid,\r\n"
			+ "    a.vdate,\r\n"
			+ "    a.jobno AS joborderno,\r\n"
			+ "    c.docid AS voucherno,\r\n"
			+ "    c.docdate AS voucherdate,\r\n"
			+ "    e.partyshortname AS billtoparty,\r\n"
			+ "    e.controllingoff,\r\n"
			+ "    a.billcurr,\r\n"
			+ "    a.billcurrrate,\r\n"
			+ "    a.totalinvamountbc*-1,\r\n"
			+ "    a.totalinvamountlc*-1,\r\n"
			+ "    a.totaltaxableamountlc,\r\n"
			+ "    a.gsttype,\r\n"
			+ "    a.totaltaxamountlc*-1,\r\n"
			+ "    0 AS totaltaxamountbc,\r\n"
			+ "    a.roundoffamountlc,\r\n"
			+ "    SUM(b.fcamount) AS fcamt,\r\n"
			+ "    SUM(b.lcamount)*-1 AS lcamt,\r\n"
			+ "    SUM(b.rate) AS rate,\r\n"
			+ "    SUM(b.billamount)*-1 AS billamount,\r\n"
			+ "    a.partytype,a.docid,a.docdate,a.screencode\r\n"
			+ "FROM\r\n"
			+ "    irncreditnote a\r\n"
			+ "JOIN\r\n"
			+ "    irncreditnotedetails b ON a.irncreditnoteid = b.irncreditnoteid\r\n"
			+ "JOIN\r\n"
			+ "    accounts c ON a.docid = c.refno\r\n"
			+ "JOIN\r\n"
			+ "    partymaster e ON a.partycode = e.partycode\r\n"
			+ "WHERE\r\n"
			+ "   (\r\n"
			+ "        (?1 IS NULL OR ?1 = '' OR ?2 IS NULL OR ?2 = '') \r\n"
			+ "        OR c.vdate BETWEEN STR_TO_DATE(?1, '%Y-%m-%d') AND STR_TO_DATE(?2, '%Y-%m-%d')\r\n"
			+ "    )\r\n"
			+ "    AND a.orgid = ?3\r\n"
			+ "    AND (a.branchcode = ?4 OR ?4 = 'ALL')\r\n"
			+ "    AND (e.partycode = ?5 OR ?5 = 'ALL') and a.finyear=?6\r\n"
			+ "GROUP BY\r\n"
			+ "    a.orgid, a.branchcode, a.vid, a.vdate, a.jobno, e.partyshortname, e.controllingoff, \r\n"
			+ "    a.billcurr, a.billcurrrate, a.totalinvamountbc, a.totalinvamountlc, \r\n"
			+ "    a.totaltaxableamountlc, a.gsttype, a.totaltaxamountlc, a.roundoffamountlc, \r\n"
			+ "    c.docid, c.docdate, a.partytype,a.docid,a.docdate,a.screencode\r\n"
			+ "\r\n"
			+ "ORDER BY\r\n"
			+ "    vid, vdate")
	Set<Object[]> getReportDetailsForSalesRegister( String fromDate, String toDate, Long orgId,
			String branchCode, String partyCode, String finYear);

	@Query(nativeQuery = true,value = "SELECT SUM(a.amount) \r\n"
			+ "FROM (\r\n"
			+ "    -- Current Month Revenue (only if type = 'MONTH')\r\n"
			+ "    SELECT SUM(v.amount) AS amount\r\n"
			+ "    FROM vw_revenue v \r\n"
			+ "    JOIN financialyear f ON v.finyear = f.finyear\r\n"
			+ "    WHERE v.docdate BETWEEN f.startdate AND f.enddate \r\n"
			+ "      AND MONTH(v.docdate) = MONTH(CURDATE()) \r\n"
			+ "      AND ?2 = 'MONTH' \r\n"
			+ "      AND CAST(v.finyear AS SIGNED) = ?3\r\n"
			+ "      AND v.orgid = ?1\r\n"
			+ "\r\n"
			+ "    UNION ALL\r\n"
			+ "\r\n"
			+ "    -- Current Financial Year Revenue (only if type = 'YEAR')\r\n"
			+ "    SELECT v.amount \r\n"
			+ "    FROM vw_revenue v \r\n"
			+ "    WHERE (\r\n"
			+ "        (MONTH(v.docdate) >= 4 AND CAST(v.finyear AS SIGNED) = ?3) \r\n"
			+ "        OR \r\n"
			+ "        (MONTH(v.docdate) < 4 AND CAST(v.finyear AS SIGNED) = (?3 + 1))\r\n"
			+ "    )\r\n"
			+ "    AND v.orgid = ?1 \r\n"
			+ "    AND ?2 = 'YEAR'\r\n"
			+ ") a")
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
	
	@Query(nativeQuery = true,value ="SELECT SUM(a.curmnth) AS currentmonth, SUM(a.premonth) AS previousmonth, SUM(a.curyear) AS currentyear, SUM(a.preyear) AS previousyear\r\n"
			+ "FROM (\r\n"
			+ "    SELECT v.orgid, SUM(v.amount) AS curmnth, 0 AS premonth, 0 AS curyear, 0 AS preyear\r\n"
			+ "    FROM vw_revenue v\r\n"
			+ "    INNER JOIN financialyear f \r\n"
			+ "        ON v.finyear = f.finyear \r\n"
			+ "        AND v.docdate BETWEEN f.startdate AND f.enddate\r\n"
			+ "    WHERE \r\n"
			+ "        CAST(f.finyear AS SIGNED) =?2\r\n"
			+ "        AND MONTH(v.docdate) = MONTH(CURDATE())\r\n"
			+ "        AND v.orgid =?1 and v.branchcode=?5\r\n"
			+ "        AND  ?3 = 'MONTH'\r\n"
			+ "    GROUP BY v.orgid\r\n"
			+ "    UNION\r\n"
			+ "    SELECT v.orgid, 0 AS curmnth, SUM(v.amount) AS premonth, 0 AS curyear, 0 AS preyear\r\n"
			+ "    FROM vw_revenue v\r\n"
			+ "    WHERE \r\n"
			+ "        v.finyear = (\r\n"
			+ "            CASE \r\n"
			+ "                WHEN MONTH(CURDATE()) = 4 AND YEAR(CURDATE()) =?2 THEN (?2 - 1)\r\n"
			+ "                ELSE ?2\r\n"
			+ "            END\r\n"
			+ "        )\r\n"
			+ "        AND MONTH(v.docdate) = (\r\n"
			+ "            CASE \r\n"
			+ "                WHEN MONTH(CURDATE()) = 4 AND YEAR(CURDATE()) =?2 THEN 3\r\n"
			+ "                ELSE (MONTH(CURDATE()) - 1)\r\n"
			+ "            END\r\n"
			+ "        )\r\n"
			+ "        AND v.orgid =?1 and v.branchcode=?5\r\n"
			+ "        AND ?3 = 'MONTH'\r\n"

			+ "    GROUP BY v.orgid\r\n"
			+ "    UNION\r\n"
			+ "    SELECT v.orgid, 0 AS curmnth, 0 AS premonth, SUM(v.amount) AS curyear, 0 AS preyear\r\n"
			+ "    FROM vw_revenue v\r\n"
			+ "    WHERE \r\n"
			+ "        v.finyear =?2\r\n"
			+ "        AND v.orgid =?1 and v.branchcode=?5\r\n"
			+ "        AND ?4 = 'YEAR'\r\n"
			+ "    GROUP BY v.orgid\r\n"
			+ "    UNION\r\n"
			+ "    SELECT v.orgid, 0 AS curmnth, 0 AS premonth, 0 AS curyear, SUM(v.amount) AS preyear\r\n"
			+ "    FROM vw_revenue v\r\n"
			+ "    WHERE \r\n"
			+ "        v.finyear = (?2 - 1)\r\n"
			+ "        AND v.orgid =?1 and v.branchcode=?5\r\n"
			+ "        AND ?4 = 'YEAR'\r\n"
			+ "    GROUP BY v.orgid\r\n"
			+ ") a" )
	Set<Object[]> getPercentageDiffFromRevenue(Long orgId,Long finYear,String Month,String Year,String branchCode);


	@Query(nativeQuery =true,value = "SELECT \r\n"
			+ "    SUM(a.curyear) AS currentyear,\r\n"
			+ "    SUM(a.preyear) AS previousyear\r\n"
			+ "FROM (\r\n"
			+ "    SELECT \r\n"
			+ "        SUM(v.amount) AS curyear,\r\n"
			+ "        0 AS preyear,\r\n"
			+ "        MONTH(v.docdate) AS month,\r\n"
			+ "        v.finyear,\r\n"
			+ "        v.orgid\r\n"
			+ "    FROM vw_revenue v\r\n"
			+ "    WHERE v.finyear =?2\r\n"
			+ "    AND v.orgid = ?1\r\n"
			+ "    GROUP BY MONTH(v.docdate), v.finyear, v.orgid\r\n"
			+ "    UNION\r\n"
			+ "    SELECT  \r\n"
			+ "        0 AS curyear,\r\n"
			+ "        SUM(v.amount) AS preyear,\r\n"
			+ "        MONTH(v.docdate) AS month,\r\n"
			+ "        v.finyear,\r\n"
			+ "        v.orgid\r\n"
			+ "    FROM vw_revenue v\r\n"
			+ "    WHERE v.finyear =(?2 -1)\r\n"
			+ "    AND v.orgid = ?1\r\n"
			+ "    GROUP BY MONTH(v.docdate), v.finyear, v.orgid\r\n"
			+ ") a")
	Set<Object[]> getPercentageDiffFromYear(Long orgId, Long finYear);
	
    @Query(nativeQuery =true,value = "SELECT \r\n"
    		+ "    SUM(a.curyear) AS currentyear,\r\n"
    		+ "    SUM(a.preyear) AS previousyear,\r\n"
    		+ "    SUM(a.curmonth) AS currentmonth,\r\n"
    		+ "    SUM(a.premonth) AS previousmonth \r\n"
    		+ "FROM (\r\n"
    		+ "    SELECT \r\n"
    		+ "        SUM(r.paymentamt) AS curyear,\r\n"
    		+ "        0 AS preyear,\r\n"
    		+ "        0 AS curmonth,\r\n"
    		+ "        0 AS premonth,\r\n"
    		+ "        r.orgid \r\n"
    		+ "    FROM payment r\r\n"
    		+ "    WHERE r.finyear =?2\r\n"
    		+ "        AND r.orgid =?1\r\n"
    		+ "        AND ?3 = 'YEAR'\r\n"
    		+ "    GROUP BY r.orgid\r\n"
    		+ "\r\n"
    		+ "    UNION\r\n"
    		+ "\r\n"
    		+ "    SELECT \r\n"
    		+ "        0 AS curyear,\r\n"
    		+ "        SUM(r.paymentamt) AS preyear,\r\n"
    		+ "        0 AS curmonth,\r\n"
    		+ "        0 AS premonth,\r\n"
    		+ "        r.orgid \r\n"
    		+ "    FROM payment r\r\n"
    		+ "    WHERE r.finyear = (?2 - 1)\r\n"
    		+ "        AND r.orgid =?1\r\n"
    		+ "        AND ?3 = 'YEAR'\r\n"
    		+ "    GROUP BY r.orgid\r\n"
    		+ "\r\n"
    		+ "    UNION\r\n"
    		+ "\r\n"
    		+ "    SELECT \r\n"
    		+ "        0 AS curyear,\r\n"
    		+ "        0 AS preyear,\r\n"
    		+ "        SUM(r.paymentamt) AS curmonth,\r\n"
    		+ "        0 AS premonth,\r\n"
    		+ "        r.orgid \r\n"
    		+ "    FROM payment r\r\n"
    		+ "    JOIN financialyear f ON f.finyear = r.finyear \r\n"
    		+ "    WHERE r.docdate BETWEEN f.startdate AND f.enddate \r\n"
    		+ "        AND MONTH(r.docdate) = MONTH(CURDATE()) \r\n"
    		+ "        AND CAST(r.finyear AS SIGNED) =?2\r\n"
    		+ "        AND r.orgid =?1\r\n"
    		+ "         AND ?3 = 'MONTH'\r\n"
    		+ "    GROUP BY r.orgid\r\n"
    		+ "\r\n"
    		+ "    UNION \r\n"
    		+ "SELECT \r\n"
    		+ "    0 AS curyear,\r\n"
    		+ "    0 AS preyear,\r\n"
    		+ "    0 AS curmonth,\r\n"
    		+ "    SUM(r.paymentamt) AS premonth,\r\n"
    		+ "    r.orgid \r\n"
    		+ "FROM payment r\r\n"
    		+ "JOIN financialyear f ON f.finyear = r.finyear \r\n"
    		+ "WHERE r.docdate BETWEEN f.startdate AND f.enddate \r\n"
    		+ "  AND r.orgid =?1\r\n"
    		+ "  AND r.finyear = (\r\n"
    		+ "      CASE \r\n"
    		+ "          WHEN MONTH(CURDATE()) = 4 THEN ?2 - 1 \r\n"
    		+ "          ELSE ?2\r\n"
    		+ "      END\r\n"
    		+ "  )\r\n"
    		+ "  AND MONTH(r.docdate) = (\r\n"
    		+ "      CASE \r\n"
    		+ "          WHEN MONTH(CURDATE()) = 4 THEN  MONTH(CURDATE()) - 1 else    MONTH(CURDATE())    \r\n"
    		+ "                          \r\n"
    		+ "      END\r\n"
    		+ "  )\r\n"
    		+ "GROUP BY r.orgid) a")
	Set<Object[]> getPercentageFromPayment(Long orgId, Long finYear, String month);

	
	@Query(nativeQuery =true,value = "SELECT \r\n"
			+ "    p.partyname,\r\n"
			+ "    p.partyshortname,\r\n"
			+ "    SUM(d.arapamount),\r\n"
			+ "    MONTH(t.vdate)\r\n"
			+ "FROM taxinvoice t\r\n"
			+ "JOIN accounts a ON t.vid = a.vid\r\n"
			+ "JOIN accountsdetails d ON d.accountsid = a.accountsid\r\n"
			+ "JOIN partymaster p ON t.partycode = p.partycode\r\n"
			+ "WHERE \r\n"
			+ "    a.orgid = ?1\r \n"
			+ "    AND a.finyear =?2 \r\n"
			+ "    AND a.branchcode =?3 \r\n"
			+ "    AND t.partytype='CUSTOMER'\r\n"
			+ "GROUP BY \r\n"
			+ "    p.partyname, \r\n"
			+ "    MONTH(t.vdate), \r\n"
			+ "    p.partyshortname\r\n"
			+ "ORDER BY \r\n"
			+ "    MONTH(t.vdate)\r\n"
			+ "")
	Set<Object[]> getSalesMonthWiseData(Long orgId, Long finYear, String branchCode);
	
	@Query(nativeQuery =true,value = "select monthnumber,monthname,totalamount from (\r\n"
			+ "SELECT \r\n"
			+ "    MONTH(docdate) AS monthnumber,\r\n"
			+ "    DATE_FORMAT(docdate, '%M') AS monthname,\r\n"
			+ "    SUM(amount) AS totalamount\r\n"
			+ "FROM vw_revenue \r\n"
			+ "WHERE  finyear =?2\r\n"
			+ "and orgid =?1\r\n"
			+ "GROUP BY MONTH(docdate), DATE_FORMAT(docdate, '%M')\r\n"
			+ "ORDER BY MONTH(docdate)\r\n"
			+ ")a  where monthname =?3")
	Set<Object[]> getRevenueMonthWiseRevenue(Long orgId, Long finYear, String monthName);
	
	@Query(nativeQuery =true,value = "select monthnumber,monthname,totalamount from (\r\n"
			+ "SELECT \r\n"
			+ "    MONTH(docdate) AS monthnumber,\r\n"
			+ "    DATE_FORMAT(docdate, '%M') AS monthname,\r\n"
			+ "    SUM(amount) AS totalamount\r\n"
			+ "FROM vw_cost\r\n"
			+ "WHERE  finyear =?2\r\n"
			+ "and orgid =?1\r\n"
			+ "GROUP BY MONTH(docdate), DATE_FORMAT(docdate, '%M')\r\n"
			+ "ORDER BY MONTH(docdate)\r\n"
			+ ")a  where monthname =?3")
	Set<Object[]> getRevenueMonthWiseCost(Long orgId, Long finYear, String monthName);
	
	
	@Query(nativeQuery =true,value = "select monthnumber,monthname,totalamount from (\r\n"
			+ "SELECT \r\n"
			+ "    MONTH(docdate) AS monthnumber,\r\n"
			+ "    DATE_FORMAT(docdate, '%M') AS monthname,\r\n"
			+ "    SUM(receiptamt) AS totalamount\r\n"
			+ "FROM receipt\r\n"
			+ "WHERE  finyear =?2\r\n"
			+ "and orgid =?1\r\n"
			+ "GROUP BY MONTH(docdate), DATE_FORMAT(docdate, '%M')\r\n"
			+ "ORDER BY MONTH(docdate)\r\n"
			+ ")a  where monthname =?3")
	Set<Object[]> getRevenueMonthWiseRecepit(Long orgId, Long finYear, String monthName);
	
	
	@Query(nativeQuery =true,value = "select monthnumber,monthname,totalamount from (\r\n"
			+ "SELECT \r\n"
			+ "    MONTH(docdate) AS monthnumber,\r\n"
			+ "    DATE_FORMAT(docdate, '%M') AS monthname,\r\n"
			+ "    SUM(paymentamt) AS totalamount\r\n"
			+ "FROM payment\r\n"
			+ "WHERE  finyear =?2\r\n"
			+ "and orgid =?1\r\n"
			+ "GROUP BY MONTH(docdate), DATE_FORMAT(docdate, '%M')\r\n"
			+ "ORDER BY MONTH(docdate)\r\n"
			+ ")a  where monthname =?3")
	Set<Object[]> getRevenueMonthWisePayment(Long orgId, Long finYear, String monthName);	
	

//	@Query(nativeQuery = true,value="select * from taxinvoice a,irncreditnote a1  where a.orgid=a1.orgid and a.partycode=a1.partycode and a.docid=a1.originbillno\r\n"
//			+ " and a.approvestatus=a1.approvestatus and a.orgid=?1 and a.partyname=?2")
//	List<TaxInvoiceVO> getCheck(Long orgId, String party);

	
	@Query(nativeQuery = true, value = "SELECT \r\n"
			+ "    a.transactionno,\r\n"
			+ "    a.transactiondate,\r\n"
			+ "    m.kitid,\r\n"
			+ "    m.kitname,\r\n"
			+ "    m.kitqty\r\n"
			+ "FROM \r\n"
			+ "    mim a\r\n"
			+ "LEFT JOIN \r\n"
			+ "    mimdetails m ON a.mimid = m.mimid\r\n"
			+ "WHERE \r\n"
			+ "    a.transactionno NOT IN (\r\n"
			+ "        SELECT transno FROM taxinvoiceannexure\r\n"
			+ "    )\r\n"
			+ "    AND a.cancel = 0\r\n"
			+ "    AND a.orgid = ?1\r\n"
			+ "")
		Set<Object[]> getFillGridForTaxInvoice(Long orgId);
		
		
		@Query(nativeQuery = true, value = "SELECT transactionno\r\n"
				+ "FROM taxinvoice\r\n"
				+ "WHERE (?3 IS NOT NULL AND docid = ?3)\r\n"
				+ "\r\n"
				+ "UNION\r\n"
				+ "\r\n"
				+ "SELECT a.transactionno\r\n"
				+ "FROM mim a\r\n"
				+ "WHERE (\r\n"
				+ "        ?3 IS NULL \r\n"
				+ "        OR NOT EXISTS (\r\n"
				+ "            SELECT 1 \r\n"
				+ "            FROM taxinvoice b \r\n"
				+ "            WHERE b.docid = ?3\r\n"
				+ "        )\r\n"
				+ "    )\r\n"
				+ "AND a.cancel = 0\r\n"
				+ "AND a.receiver = ?2\r\n"
				+ "AND a.orgid = ?1\r\n"
				+ "AND a.transactionno NOT IN (\r\n"
				+ "    SELECT ax.transno\r\n"
				+ "    FROM taxinvoiceannexure ax\r\n"
				+ "    JOIN taxinvoice b ON ax.taxinvoiceid = b.taxinvoiceid\r\n"
				+ "    WHERE b.approvestatus = 'Approved'\r\n"
				+ ")\r\n"
				+ "")
	Set<Object[]> getMimFillGridgettransaction(Long orgId,String Receiver,String docId);


@Query(nativeQuery = true, value = "SELECT a.transactionno, a.transactiondate, m.kitid, m.kitname, m.kitqty \r\n"
		+ "FROM mim a\r\n"
		+ "LEFT JOIN mimdetails m ON a.mimid = m.mimid\r\n"
		+ "WHERE a.transactionno NOT IN (select  transno from taxinvoiceannexure a, taxinvoice b where a.taxinvoiceid = b.taxinvoiceid and approvestatus ='Approved')\r\n"
		+ "AND a.cancel = 0\r\n"
		+ "AND a.orgid = ?1\r\n"
		+ "AND FIND_IN_SET(a.transactionno, ?2)\r\n"
		+ "GROUP BY a.transactionno, a.transactiondate, m.kitid, m.kitname, m.kitqty, a.cancel")
Set<Object[]> getMimFillGridgetKitDetails(Long orgId, String transactionNo);

@Query(nativeQuery = true, value = "SELECT originbillno, vid, totalinvamountlc  FROM irncreditnote WHERE orgid =?1 AND originbillno =?2 AND approvestatus is Null")
Set<Object[]> getOrginBillNoBased(Long orgId, String orginBillNo);


@Query(nativeQuery = true, value = "select * from taxinvoice where screencode=?1 and docid=?2")
TaxInvoiceVO getTaxInvoiceByDocIdandScreenCode(String screenCode, String docId);

@Query(nativeQuery = true, value = "SELECT \r\n"
		+ "    a.finyear,\r\n"
		+ "    a.Vid,\r\n"
		+ "    a.Vdate,\r\n"
		+ "    a.docid,\r\n"
		+ "    a.docdate,\r\n"
		+ "    a.invoiceno,\r\n"
		+ "    a.invoicedate,\r\n"
		+ "    a.gsttype,\r\n"
		+ "    a.partyname,\r\n"
		+ "    a.placeofsupply,\r\n"
		+ "    a.totalchargeamountlc,\r\n"
		+ "    a.totalinvamountlc,\r\n"
		+ "    a.totaltaxamountlc,\r\n"
		+ "    b.chargetype,\r\n"
		+ "    b.chargecode,\r\n"
		+ "    b.chargename,\r\n"
		+ "    b.description,\r\n"
		+ "    b.currency,\r\n"
		+ "    b.gstpercent,\r\n"
		+ "    b.qty,\r\n"
		+ "    b.rate,\r\n"
		+ "    b.taxable,\r\n"
		+ "    b.billamount,\r\n"
		+ "    b.gstamount,\r\n"
		+ "    b.billamount + b.gstamount AS totalLcAmount\r\n"
		+ "FROM \r\n"
		+ "    taxinvoice a,\r\n"
		+ "    taxinvoicedetails b\r\n"
		+ "WHERE \r\n"
		+ "    a.taxinvoiceid = b.taxinvoiceid\r\n"
		+ "    AND a.orgid = ?1\r\n"
		+ "    AND (a.partyname = ?2 OR ?2 = 'ALL')\r\n"
		+ "    AND (?3 IS NULL OR a.vdate >= ?3)\r\n"
		+ "    AND (?4 IS NULL OR a.vdate <= ?4)\r\n"
		+ "    AND (a.branchcode = ?5 OR ?5 = 'ALL')\r\n"
		+ "ORDER BY \r\n"
		+ "    a.createdon DESC")
Set<Object[]> getTaxinvoiceDetails(Long orgId, String partyname, String fromDate, String toDate,String branchCode);

@Query(nativeQuery = true, value = "SELECT \r\n"
		+ "    a.finyear, \r\n"
		+ "    a.Vid, \r\n"
		+ "    a.Vdate, \r\n"
		+ "    a.docid, \r\n"
		+ "    a.docdate, \r\n"
		+ "    a.invoiceno, \r\n"
		+ "    a.invoicedate, \r\n"
		+ "    a.gsttype,\r\n"
		+ "    a.partyname, \r\n"
		+ "    a.placeofsupply, \r\n"
		+ "    a.totalchargeamountlc, \r\n"
		+ "    a.totalinvamountlc, \r\n"
		+ "    a.totaltaxamountlc,\r\n"
		+ "    a.approvestatus\r\n"
		+ "FROM \r\n"
		+ "    taxinvoice a\r\n"
		+ "WHERE \r\n"
		+ "    a.orgid = ?1\r\n"
		+ "    AND (a.partyname = ?2 OR ?2 = 'ALL')\r\n"
		+ "    AND (?3 IS NULL OR a.vdate >= ?3)\r\n"
		+ "    AND (?4 IS NULL OR a.vdate <= ?4)\r\n"
		+ "    AND (a.branchcode = ?5 OR ?5 = 'ALL')\r\n"
		+ "ORDER BY \r\n"
		+ "    a.createdon DESC")
Set<Object[]> getTaxinvoiceSummary(Long orgId, String partyname, String fromDate, String toDate,String branchCode);
 


	
}
