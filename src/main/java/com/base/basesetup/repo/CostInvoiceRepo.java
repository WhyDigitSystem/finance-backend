package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.base.basesetup.entity.CostInvoiceVO;
import com.base.basesetup.entity.PartyMasterVO;

public interface CostInvoiceRepo extends JpaRepository<CostInvoiceVO, Long> {

	@Query(nativeQuery = true, value = "select * from costinvoice where orgid=?1 and finyear=?2 and branchcode=?3")
	List<CostInvoiceVO> getAllCostInvoiceByOrgId(Long orgId, String finYear, String branchCode);

	@Query(nativeQuery = true, value = "select * from costinvoice where costinvoiceid=?1")
	List<CostInvoiceVO> getAllCostInvoiceById(Long id);

	@Query(value = "select a from CostInvoiceVO a where a.id=?1")
	CostInvoiceVO getCostInvoiceById(Long id);

	@Query(nativeQuery = true, value = "select * from costinvoice where active = 1")
	List<CostInvoiceVO> findCostInvoiceByActive();

	@Query(value = "select * from costInvoice a where a.orgid=?1 and docId=?2", nativeQuery = true)
	CostInvoiceVO findAllCostInvoiceByDocId(Long orgId, String docId);

//	@Query(nativeQuery = true, value = "select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
//	String getCostInvoiceDocId(Long orgId, String finYear, String branchCode, String screenCode);
	
	@Query(value = "SELECT " + "CONCAT(d.prefixfield, LPAD(d.lastno, 5, '0')) AS docid, " + "CASE "
			+ "   WHEN CURDATE() BETWEEN f.startdate AND f.enddate " + "   THEN CURDATE() " + "   ELSE f.enddate "
			+ "END AS docdate " + "FROM documenttypemappingdetails d " + "JOIN financialyear f "
			+ "ON d.finyear = f.finyear AND d.orgid = f.orgid " + "WHERE d.orgid = ?1 " + "AND d.finyear = ?2 "
			+ "AND d.branchcode = ?3 " + "AND d.screencode = ?4", nativeQuery = true)
	List<Object[]> getCostInvoiceDocId(Long orgId, String finYear, String branchCode, String screenCode);

	@Query(value = "select a.chargeType from ChargeTypeRequestVO a where a.orgId=?1 and a.active=true group by a.chargeType")
	Set<Object[]> getActiveChargType(Long orgId);

	@Query(nativeQuery = true, value = "select chargecode,govtsac,chargedescription,taxable,ccfeeapplicable,excempted,serviceaccountcode,gsttax,salesaccount from chargetyperequest\r\n"
			+ " where orgid=?1 and chargetype=?2 and active=1 group by chargecode,govtsac,chargedescription,taxable,ccfeeapplicable,excempted,serviceaccountcode,gsttax,salesaccount")
	Set<Object[]> getActiveChargCodeByOrgIdAndChargeTypeIgnoreCase(Long orgId, String chargeType);

	@Query(nativeQuery = true, value = "select c.orgid,c.date,c.month,c.currency,c.currencydescripition,c.buyingexrate,c.sellingexrate from  partymaster a,partycurrencymapping b, vw_exrates c where a.partymasterid=b.partymasterid\r\n"
			+ "and a.orgid=c.orgid and a.orgid=?1 and b.transcurrency=c.currency and a.partycode=?2\r\n"
			+ " group by c.orgid,c.date,c.month,c.currency,c.currencydescripition,c.buyingexrate,c.sellingexrate ")
	Set<Object[]> getCurrencyAndExratesForMatchingParties(Long orgId, String partyCode);

	@Query(value = "select a from PartyMasterVO a where a.orgId=?1 and a.partyType=?2 and a.active=true and a.gstRegistered='YES'")
	List<PartyMasterVO> findByOrgIdAndPartyTypeIgnoreCase(Long orgId, String partyType);

	@Query(nativeQuery = true, value = "select a.statecode,a.gstin,concat(a.stateno,' - ',a.state)stateno from partystate a,partymaster b where a.partymasterid=b.partymasterid and b.orgid=?1 and b.partymasterid=?2\r\n"
			+ "group by a.statecode,a.gstin,concat(a.stateno,' - ',a.state)")
	Set<Object[]> getStateCodeDetails(Long orgId, Long id);

	@Query(nativeQuery = true, value = "SELECT a.addresstype,concat(a.addressline1,',',a.addressline2,',',a.addressline3) address,a.pincode FROM partyaddress a,partymaster b,state c where  a.partymasterid=b.partymasterid and a.state=c.state and b.orgid=?1 and a.partymasterid=?2 and c.statecode=?3 and a.businessplace=?4\r\n"
			+ "group by a.addresstype,concat(a.addressline1,',',a.addressline2,',',a.addressline3),a.pincode")
	Set<Object[]> getAddressDetails(Long orgId, Long id, String stateCode, String placeOfSupply);

	@Query(nativeQuery = true, value = "SELECT \r\n" + "       CASE \r\n"
			+ "           WHEN statecode = ?3 THEN 'INTRA'\r\n" + "           ELSE    'INTER'\r\n"
			+ "       END AS transactionType\r\n" + "FROM branch\r\n" + "WHERE orgid = ?1 \r\n"
			+ "  AND branchcode = ?2")
	Set<Object[]> getGstType(Long orgId, String branchCode, String stateCode);

	@Query(nativeQuery = true, value = "SELECT a.businessplace FROM partyaddress a,partymaster b,state c where  a.partymasterid=b.partymasterid and a.state=c.state and b.orgid=?1 and a.partymasterid=?2 and c.statecode=?3\r\n"
			+ "group by businessplace")
	Set<Object[]> getPlaceOfSupplyDetails(Long orgId, Long id, String stateCode);

	@Query(nativeQuery = true, value = "SELECT j.jobno,j.customer,j.partyshortname FROM jobcard j WHERE orgid=?1 AND closed = 0 AND active=1 group by j.jobno,j.customer,partyshortname")
	Set<Object[]> getJobNoFromTmsJobCard(Long orgId);

	@Query(nativeQuery = true, value = "select accountgroupname,category from groupledger where orgid=?1 and gsttaxflag='NA' and category='PAYABLE A/C' and type='ACCOUNT'  and groupname='TDS'")
	Set<Object[]> getTdsLedgerFromAccount(Long orgId);

	@Query(nativeQuery = true, value = "SELECT a.tdswithsec,a.tdswithper FROM partyspecialtds a, partymaster b WHERE a.partymasterid = b.partymasterid AND b.orgid=?1 AND b.partycode=?2")
	Set<Object[]> findTdsDetailsFromPartyMasterSpecialTDS(Long orgId, String partyCode);

	@Query(nativeQuery = true, value = "select * from costinvoice a where a.orgId=?1 and a.supplierName=?2 and a.branchCode=?3 and a.approvestatus='Approved' order by a.docId desc")
	List<CostInvoiceVO> findOrginBillNoByParty(Long orgId, String party, String branchCode);

	@Query(nativeQuery = true, value = "select  accountgroupname,gstpercentage,currency from groupledger where orgid=?1 and gsttaxflag!='NA' and category='TAX' and gsttaxflag='INPUT TAX' and gsttype=?2 and gstpercentage IN(?3) group by  accountgroupname,gstpercentage,currency order by gstpercentage desc")
	Set<Object[]> findInterDetailsForCostInvoicePosting(Long orgId, String gtsType, Double gstPercent);

	@Query(nativeQuery = true, value = "select accountgroupname,gstpercentage,currency from groupledger where orgid=?1 and gsttaxflag!='NA' and category='TAX' and gsttaxflag='INPUT TAX' and gsttype=?2 and gstpercentage IN(?3) group by  accountgroupname,gstpercentage,currency order by gstpercentage desc")
	Set<Object[]> findIntraDetailsForCostInvoicePosting(Long orgId, String gtsType, Double intraPercent);

	CostInvoiceVO findByOrgIdAndIdAndDocId(Long orgId, Long id, String docId);

	@Query(nativeQuery = true, value = "select a.creditdays from partymaster a where a.orgId=?1 and partycode=?2 and a.active=1 ")
	Set<Object[]> findCreditDaysFromVendor(Long orgId, String supplierCode);

	@Query(nativeQuery = true, value = "select accountgroupname,gstpercentage,currency from groupledger where orgid=?1 and gsttaxflag!='NA' and category='TAX' and gsttaxflag='INPUT TAX' and gsttype=?2 and gstpercentage IN(?3) order by gstpercentage desc")
	Set<Object[]> findInterAndIntraDetailsForCostInvoice(Long orgId, String gstType, List<String> gstPercent);

	@Query(nativeQuery = true, value = "select * from costinvoice where orgid=?1 and docid=?2")
	CostInvoiceVO findByOrgIdAndDocId(Long orgId, String orginBill);

	boolean existsByvIdAndOrgId(String vId, Long orgId);

	@Query(nativeQuery = true, value = "select sum(amount) as totalAmount from vw_cost where orgid=?1 and (billmonth=?2 or 'ALL'=?2) and finyear=?3")
	Set<Object[]> getDsahboardCost(Long orgId, String billMonth, String finYear);

	@Query(nativeQuery = true, value = "SELECT \r\n" + "    suppliername,\r\n"
			+ "    SUM(total_amount) AS total_amount,\r\n" + "    SUM(TDS_4) AS TDS_4,\r\n"
			+ "    SUM(TDS_9) AS TDS_9,\r\n" + "    SUM(TDS_10) AS TDS_10,\r\n" + "    financial_year,\r\n"
			+ "    partyshortname\r\n" + "FROM (\r\n" + "    -- ✅ MONTH block (runs only when ?2 = 'MONTH')\r\n"
			+ "    SELECT \r\n" + "        a.suppliername,\r\n" + "        SUM(b.totaltds) AS total_amount,\r\n"
			+ "        SUM(CASE WHEN b.tdswithholdingper = 4 THEN b.totaltds ELSE 0 END) AS TDS_4,\r\n"
			+ "        SUM(CASE WHEN b.tdswithholdingper = 9 THEN b.totaltds ELSE 0 END) AS TDS_9,\r\n"
			+ "        SUM(CASE WHEN b.tdswithholdingper = 10 THEN b.totaltds ELSE 0 END) AS TDS_10,\r\n"
			+ "        a.finyear AS financial_year,\r\n" + "        p.partyshortname\r\n"
			+ "    FROM costinvoice a \r\n" + "    JOIN tdscostinvoice b ON a.costinvoiceid = b.costinvoiceid\r\n"
			+ "    JOIN partymaster p ON p.partyname = a.suppliername\r\n" + "    WHERE \r\n"
			+ "        a.orgid = ?1\r\n" + "        AND ?2 = 'MONTH'\r\n"
			+ "        AND CAST(a.finyear AS SIGNED) = ?3\r\n" + "        AND MONTH(a.supplierbilldate) = MONTH(CURDATE())\r\n"
			+ "    GROUP BY a.suppliername, a.finyear, p.partyshortname\r\n" + "\r\n" + "    UNION \r\n" + "\r\n"
			+ "    -- ✅ YEAR block (runs only when ?2 = 'YEAR')\r\n" + "    SELECT \r\n" + "        a.suppliername,\r\n"
			+ "        SUM(b.totaltds) AS total_amount,\r\n"
			+ "        SUM(CASE WHEN b.tdswithholdingper = 4 THEN b.totaltds ELSE 0 END) AS TDS_4,\r\n"
			+ "        SUM(CASE WHEN b.tdswithholdingper = 9 THEN b.totaltds ELSE 0 END) AS TDS_9,\r\n"
			+ "        SUM(CASE WHEN b.tdswithholdingper = 10 THEN b.totaltds ELSE 0 END) AS TDS_10,\r\n"
			+ "        a.finyear AS financial_year,\r\n" + "        p.partyshortname\r\n"
			+ "    FROM costinvoice a \r\n" + "    JOIN tdscostinvoice b ON a.costinvoiceid = b.costinvoiceid\r\n"
			+ "    JOIN partymaster p ON p.partyname = a.suppliername\r\n" + "    WHERE \r\n"
			+ "        a.orgid = ?1\r\n" + "        AND ?2 = 'YEAR'\r\n" + "        AND (\r\n"
			+ "            (MONTH(a.supplierbilldate) >= 4 AND CAST(a.finyear AS SIGNED) = ?3)\r\n" + "            OR\r\n"
			+ "            (MONTH(a.supplierbilldate) < 4 AND CAST(a.finyear AS SIGNED) = (?3 + 1))\r\n" + "        )\r\n"
			+ "    GROUP BY a.suppliername, a.finyear, p.partyshortname\r\n" + ") t\r\n"
			+ "GROUP BY suppliername, financial_year, partyshortname\r\n" + "")
	Set<Object[]> getTdsSummary(Long orgId, String month, Long finYear);

	@Query(nativeQuery = true, value = "SELECT \r\n" + "    SUM(a.curmnth) AS currentmonth,\r\n"
			+ "    SUM(a.premonth) AS previousmonth,\r\n" + "    SUM(a.curyear) AS currentyear,\r\n"
			+ "    SUM(a.preyear) AS previousyear\r\n" + "FROM (\r\n" + "    -- Current Month Revenue\r\n" + "  \r\n"
			+ "SELECT \r\n" + "        v.orgid, \r\n" + "        SUM(v.amount) AS curmnth, \r\n"
			+ "        0 AS premonth,\r\n" + "        0 AS curyear,\r\n" + "        0 AS preyear\r\n"
			+ "    FROM vw_cost v\r\n" + "    INNER JOIN financialyear f \r\n" + "        ON v.finyear = f.finyear \r\n"
			+ "        AND v.docdate BETWEEN f.startdate AND f.enddate\r\n" + "    WHERE \r\n"
			+ "        CAST(f.finyear AS SIGNED) = ?2\r\n" + "        AND MONTH(v.docdate) = MONTH(CURDATE()) \r\n"
			+ "        AND v.orgid = ?1\r\n" + "        AND ?3 = 'MONTH'\r\n" + "    GROUP BY v.orgid\r\n" + "\r\n"
			+ "    UNION\r\n" + "\r\n" + "    -- Previous Month Revenue\r\n" + "    SELECT \r\n"
			+ "        v.orgid, \r\n" + "        0 AS curmnth, \r\n" + "        SUM(v.amount) AS premonth,\r\n"
			+ "        0 AS curyear,\r\n" + "        0 AS preyear\r\n" + "    FROM vw_cost v\r\n"
			+ "    INNER JOIN financialyear f \r\n" + "        ON v.finyear = f.finyear \r\n"
			+ "        AND v.docdate BETWEEN f.startdate AND f.enddate\r\n" + "    WHERE \r\n"
			+ "        v.finyear=( case \r\n"
			+ "      	when MONTH(CURDATE()) = 4 and year(curdate())=?2  then (?2-1) else ?2 end) \r\n"
			+ "        	and month(v.docdate) = (case when MONTH(CURDATE()) = 4 and year(curdate())=?2  then 3 else (MONTH(CURDATE())-1) end )\r\n"
			+ "  and ?3='MONTH'\r\n" + "    GROUP BY v.orgid\r\n" + "\r\n" + "    UNION\r\n" + "\r\n"
			+ "    -- Current Financial Year Revenue\r\n" + "    SELECT \r\n" + "        v.orgid,\r\n"
			+ "        0 AS curmnth, \r\n" + "        0 AS premonth, \r\n" + "        SUM(v.amount) AS curyear, \r\n"
			+ "        0 AS preyear\r\n" + "    FROM vw_cost v\r\n" + "    WHERE \r\n" + "        v.finyear =?2\r\n"
			+ "        AND v.orgid = ?1\r\n" + "        AND ?4 = 'YEAR'\r\n" + "    GROUP BY v.orgid\r\n" + "\r\n"
			+ "    UNION \r\n" + "\r\n" + "    -- Previous Financial Year Revenue\r\n" + "    SELECT  \r\n"
			+ "        v.orgid,\r\n" + "        0 AS curmnth, \r\n" + "        0 AS premonth, \r\n"
			+ "        0 AS curyear, \r\n" + "        SUM(v.amount) AS preyear\r\n" + "    FROM vw_cost v\r\n"
			+ "    WHERE \r\n" + "        v.finyear =(?2 -1)\r\n" + "        AND v.orgid = ?1\r\n"
			+ "        AND ?4 = 'YEAR'\r\n" + "    GROUP BY v.orgid\r\n" + "    \r\n" + "    \r\n" + ") a")
	Set<Object[]> getPercentageDiffFromCost(Long orgId, Long finYear, String month, String year);

//	@Query(nativeQuery =true,value = "SELECT \r\n"
//			+ "    SUM(a.curyear) AS currentyear,\r\n"
//			+ "    SUM(a.preyear) AS previousyear,\r\n"
//			+ "    SUM(a.curmonth) AS currentmonth,\r\n"
//			+ "    SUM(a.premonth) AS previousmonth\r\n"
//			+ "FROM (\r\n"
//			+ "    -- Current Financial Year: Apr finYear - Mar (finYear + 1)\r\n"
//			+ "    SELECT \r\n"
//			+ "        SUM(r.receiptamt) AS curyear,\r\n"
//			+ "        0 AS preyear,\r\n"
//			+ "        0 AS curmonth,\r\n"
//			+ "        0 AS premonth,\r\n"
//			+ "        r.orgid\r\n"
//			+ "    FROM receipt r\r\n"
//			+ "    JOIN financialyear f ON f.finyear = r.finyear \r\n"
//			+ "    WHERE (\r\n"
//			+ "        (MONTH(r.docdate) >= 4 AND CAST(r.finyear AS SIGNED) = ?2) \r\n"
//			+ "        OR \r\n"
//			+ "        (MONTH(r.docdate) < 4 AND CAST(r.finyear AS SIGNED) = (?2 + 1))\r\n"
//			+ "    )\r\n"
//			+ "    AND r.orgid = ?1\r\n"
//			+ "    AND ?3 = 'YEAR'\r\n"
//			+ "    GROUP BY r.orgid\r\n"
//			+ "\r\n"
//			+ "    UNION\r\n"
//			+ "\r\n"
//			+ "    -- Previous Financial Year: Apr (finYear - 1) - Mar finYear\r\n"
//			+ "    SELECT \r\n"
//			+ "        0 AS curyear,\r\n"
//			+ "        SUM(r.receiptamt) AS preyear,\r\n"
//			+ "        0 AS curmonth,\r\n"
//			+ "        0 AS premonth,\r\n"
//			+ "        r.orgid\r\n"
//			+ "    FROM receipt r\r\n"
//			+ "    JOIN financialyear f ON f.finyear = r.finyear \r\n"
//			+ "    WHERE (\r\n"
//			+ "        (MONTH(r.docdate) >= 4 AND CAST(r.finyear AS SIGNED) = (?2 - 1)) \r\n"
//			+ "        OR \r\n"
//			+ "        (MONTH(r.docdate) < 4 AND CAST(r.finyear AS SIGNED) = ?2)\r\n"
//			+ "    )\r\n"
//			+ "    AND r.orgid = ?1\r\n"
//			+ "    AND ?3 = 'YEAR'\r\n"
//			+ "    GROUP BY r.orgid\r\n"
//			+ "\r\n"
//			+ "    UNION\r\n"
//			+ "\r\n"
//			+ "    -- Current Month\r\n"
//			+ "    SELECT \r\n"
//			+ "        0 AS curyear,\r\n"
//			+ "        0 AS preyear,\r\n"
//			+ "        SUM(r.receiptamt) AS curmonth,\r\n"
//			+ "        0 AS premonth,\r\n"
//			+ "        r.orgid\r\n"
//			+ "    FROM receipt r\r\n"
//			+ "    JOIN financialyear f ON f.finyear = r.finyear \r\n"
//			+ "    WHERE r.docdate BETWEEN f.startdate AND f.enddate \r\n"
//			+ "    AND MONTH(r.docdate) = MONTH(CURDATE())\r\n"
//			+ "    AND CAST(r.finyear AS SIGNED) = ?2\r\n"
//			+ "    AND r.orgid = ?1\r\n"
//			+ "    AND ?3 = 'MONTH'\r\n"
//			+ "    GROUP BY r.orgid\r\n"
//			+ "\r\n"
//			+ "    UNION\r\n"
//			+ "\r\n"
//			+ "    -- Previous Month\r\n"
//			+ "    SELECT \r\n"
//			+ "        0 AS curyear,\r\n"
//			+ "        0 AS preyear,\r\n"
//			+ "        0 AS curmonth,\r\n"
//			+ "        SUM(r.receiptamt) AS premonth,\r\n"
//			+ "        r.orgid\r\n"
//			+ "    FROM receipt r\r\n"
//			+ "    JOIN financialyear f ON f.finyear = r.finyear \r\n"
//			+ "    WHERE r.docdate BETWEEN f.startdate AND f.enddate \r\n"
//			+ "    AND r.orgid = ?1\r\n"
//			+ "    AND ?3 = 'MONTH'\r\n"
//			+ "    AND (\r\n"
//			+ "        (MONTH(CURDATE()) = 1 AND MONTH(r.docdate) = 12 AND CAST(r.finyear AS SIGNED) = (?2 - 1))\r\n"
//			+ "        OR\r\n"
//			+ "        (MONTH(CURDATE()) != 1 AND MONTH(r.docdate) = MONTH(CURDATE()) - 1 AND CAST(r.finyear AS SIGNED) = ?2)\r\n"
//			+ "    )\r\n"
//			+ "    GROUP BY r.orgid\r\n"
//			+ ") a"
//			+ " \r\n"
//			+ "")

	@Query(nativeQuery = true, value = "SELECT \r\n" + "    SUM(a.curyear) AS currentyear,\r\n"
			+ "    SUM(a.preyear) AS previousyear,\r\n" + "    SUM(a.curmonth) AS currentmonth,\r\n"
			+ "    SUM(a.premonth) AS previousmonth \r\n" + "FROM (\r\n" + "    SELECT \r\n"
			+ "        SUM(r.receiptamt) AS curyear,\r\n" + "        0 AS preyear,\r\n" + "        0 AS curmonth,\r\n"
			+ "        0 AS premonth,\r\n" + "        r.orgid \r\n" + "    FROM receipt r\r\n"
			+ "    WHERE r.finyear = ?2\r\n" + "        AND r.orgid = ?1\r\n" + "        AND ?3 = 'YEAR'\r\n"
			+ "    GROUP BY r.orgid\r\n" + "\r\n" + "    UNION\r\n" + "\r\n" + "    SELECT \r\n"
			+ "        0 AS curyear,\r\n" + "        SUM(r.receiptamt) AS preyear,\r\n" + "        0 AS curmonth,\r\n"
			+ "        0 AS premonth,\r\n" + "        r.orgid \r\n" + "    FROM receipt r\r\n"
			+ "    WHERE r.finyear = (?2 - 1)\r\n" + "        AND r.orgid = ?1\r\n" + "        AND ?3 = 'YEAR'\r\n"
			+ "    GROUP BY r.orgid\r\n" + "\r\n" + "    UNION\r\n" + "\r\n" + "    SELECT \r\n"
			+ "        0 AS curyear,\r\n" + "        0 AS preyear,\r\n" + "        SUM(r.receiptamt) AS curmonth,\r\n"
			+ "        0 AS premonth,\r\n" + "        r.orgid \r\n" + "    FROM receipt r\r\n"
			+ "    JOIN financialyear f ON f.finyear = r.finyear \r\n"
			+ "    WHERE r.docdate BETWEEN f.startdate AND f.enddate \r\n"
			+ "        AND MONTH(r.docdate) = MONTH(CURDATE()) \r\n" + "        AND CAST(r.finyear AS SIGNED) = ?2\r\n"
			+ "        AND r.orgid = ?1\r\n" + "         AND ?3 = 'MONTH'\r\n" + "    GROUP BY r.orgid\r\n" + "\r\n"
			+ "    UNION \r\n" + "SELECT \r\n" + "    0 AS curyear,\r\n" + "    0 AS preyear,\r\n"
			+ "    0 AS curmonth,\r\n" + "    SUM(r.receiptamt) AS premonth,\r\n" + "    r.orgid \r\n"
			+ "FROM receipt r\r\n" + "JOIN financialyear f ON f.finyear = r.finyear \r\n"
			+ "WHERE r.docdate BETWEEN f.startdate AND f.enddate \r\n" + "  AND r.orgid = ?1\r\n"
			+ "  AND r.finyear = (\r\n" + "      CASE \r\n" + "          WHEN MONTH(CURDATE()) = 4 THEN ?2 - 1 \r\n"
			+ "          ELSE ?2\r\n" + "      END\r\n" + "  )\r\n" + "  AND MONTH(r.docdate) = (\r\n"
			+ "      CASE \r\n"
			+ "          WHEN MONTH(CURDATE()) = 4 THEN  MONTH(CURDATE()) - 1 else    MONTH(CURDATE())    \r\n"
			+ "                           -- e.g., May -> April\r\n" + "      END\r\n" + "  )\r\n"
			+ "GROUP BY r.orgid) a")

	Set<Object[]> getPercentageFromReceipt(Long orgId, Long finYear, String month);

	@Query(nativeQuery = true, value = "select c.suppliername,p.partyshortname,sum(d.totaltds) as totaltds,c.finyear  from tdscostinvoice d join costinvoice c  on d.costinvoiceid=c.costinvoiceid\r\n"
			+ "join accounts a on a.supplierbillno=c.supplierbillno join partymaster p on c.suppliercode=p.partycode where c.finyear=?2 and c.orgid=?1 and c.branchcode=?3 \r\n"
			+ " group by c.suppliername,p.partyshortname,c.finyear")
	Set<Object[]> getTotaltdsFromCustomer(Long orgId, Long finYear, String branchCode);

	@Query(nativeQuery = true, value = "select c.supplierbillno,c.supplierbilldate, c.suppliername,p.partyshortname,d.totaltds,c.finyear  from tdscostinvoice d join costinvoice c  on d.costinvoiceid=c.costinvoiceid\r\n"
			+ "join accounts a on a.supplierbillno=c.supplierbillno join partymaster p on c.suppliercode=p.partycode where c.suppliername=?4 \r\n"
			+ "and c.finyear=?2 and c.orgid=?1 and c.branchcode=?3 \r\n"
			+ "group by c.supplierbillno,c.supplierbilldate, c.suppliername,p.partyshortname,d.totaltds,c.finyear")
	Set<Object[]> getTotaltdsFromCustomerBillWise(Long orgId, Long finYear, String branchCode, String partyName);

	// cost invoice hyperlink
	@Query(nativeQuery = true, value = "select * from costinvoice where docid=?2 and screencode=?1")
	CostInvoiceVO getCostByDocIdandScreenCode(String screenCode, String docId);

//	@Query(nativeQuery=true,value="select * from costdebitnote where docid=?2 and screencode=?1")
//	CostDebitNoteVO getDebitNoteByDocIdandScreenCode(String screenCode, String docId);

	@Query(nativeQuery = true, value = "select accountgroupname,category from groupledger where orgid=?1 and gsttaxflag='NA' and category='RECEIVABLE A/C' and type='ACCOUNT'  and groupname='TDS'")
	Set<Object[]> getTdsLedgerFromAccountReceivable(Long orgId);

//	@Query(nativeQuery = true, value = "SELECT \r\n"
//			+ "    a.finyear,\r\n"
//			+ "    a.docid,\r\n"
//			+ "    a.docdate,\r\n"
//			+ "    a.supplierbillno,\r\n"
//			+ "    a.purvoucherno,\r\n"
//			+ "    a.purvoucherdate,\r\n"
//			+ "    a.suppliercode,\r\n"
//			+ "    a.suppliername,\r\n"
//			+ "    a.supplierplace,\r\n"
//			+ "    a.gsttype,\r\n"
//			+ "    a.totchargeslcamt,\r\n"
//			+ "    a.payment,\r\n"
//			+ "    a.supplierbilldate,\r\n"
//			+ "    a.gstinputlcamt,\r\n"
//			+ "    a.netbilllcamt,\r\n"
//			+ "    a.totchargeslcamt + a.gstinputlcamt AS totalAmount,\r\n"
//			+ "    a1.totaltds,\r\n"
//			+ "    case when\r\n"
//			+ "    a.approvestatus is null then 'Not Appproved' else a.approvestatus end as approvestatus\r\n"
//			+ "FROM \r\n"
//			+ "    costinvoice a,\r\n"
//			+ "    tdscostinvoice a1\r\n"
//			+ "WHERE \r\n"
//			+ "    a.costinvoiceid = a1.costinvoiceid\r\n"
//			+ "    AND a.orgid = ?1\r\n"
//			+ "    AND a.finyear = ?4\r\n"
//			+ "    AND (?2 IS NULL OR a.supplierbilldate >= ?2)\r\n"
//			+ "    AND (?3 IS NULL OR a.supplierbilldate <= ?3)\r\n"
//			+ "    AND (?5 IS NULL OR ?5 = 'ALL' OR a.suppliername = ?5)\r\n"
//			+ "    AND (a.branchcode = ?6 OR ?6 = 'ALL')\r\n"
//			+ "ORDER BY \r\n"
//			+ "    a.createdon DESC")
//	Set<Object[]> getCostInvoiceSummary(Long orgId, String fromDate, String toDate, String finYear, String partyName,
//			String branchCode);
	
	
	@Query(nativeQuery = true, value = "SELECT *\r\n"
			+ "FROM (\r\n"
			+ "    SELECT \r\n"
			+ "        a.finyear,\r\n"
			+ "        a.docid,\r\n"
			+ "        a.docdate,\r\n"
			+ "        a.supplierbillno,\r\n"
			+ "        a.purvoucherno,\r\n"
			+ "        a.purvoucherdate,\r\n"
			+ "        a.suppliercode,\r\n"
			+ "        a.suppliername,\r\n"
			+ "        a.supplierplace,\r\n"
			+ "        a.gsttype,\r\n"
			+ "\r\n"
			+ "        a.totchargeslcamt,\r\n"
			+ "        a.payment,\r\n"
			+ "        a.supplierbilldate,\r\n"
			+ "        a.gstinputlcamt,\r\n"
			+ "        a.netbilllcamt,\r\n"
			+ "        (a.totchargeslcamt + a.gstinputlcamt) AS totalAmount,\r\n"
			+ "        a1.totaltds,\r\n"
			+ "        CASE \r\n"
			+ "            WHEN a.approvestatus IS NULL THEN 'Not Approved'\r\n"
			+ "            ELSE a.approvestatus\r\n"
			+ "        END AS approvestatus,\r\n"
			+ "        1 AS sNo,\r\n"
			+ "        a.createdon\r\n"
			+ "    FROM costinvoice a\r\n"
			+ "    JOIN tdscostinvoice a1 \r\n"
			+ "        ON a.costinvoiceid = a1.costinvoiceid\r\n"
			+ "    WHERE \r\n"
			+ "        a.orgid = ?1\r\n"
			+ "        AND a.finyear = ?4\r\n"
			+ "        AND (?2 IS NULL OR a.supplierbilldate >= ?2)\r\n"
			+ "        AND (?3 IS NULL OR a.supplierbilldate <= ?3)\r\n"
			+ "        AND (?5 IS NULL OR ?5 = 'ALL' OR a.suppliername = ?5)\r\n"
			+ "        AND (a.branchcode = ?6 OR ?6 = 'ALL')\r\n"
			+ "    UNION ALL\r\n"
			+ "    SELECT \r\n"
			+ "        NULL AS finyear,\r\n"
			+ "        'Total Amount' AS docid,\r\n"
			+ "        NULL AS docdate,\r\n"
			+ "        NULL AS supplierbillno,\r\n"
			+ "        NULL AS purvoucherno,\r\n"
			+ "        NULL AS purvoucherdate,\r\n"
			+ "        NULL AS suppliercode,\r\n"
			+ "        NULL AS suppliername,\r\n"
			+ "        NULL AS supplierplace,\r\n"
			+ "        NULL AS gsttype,\r\n"
			+ "        SUM(a.totchargeslcamt),\r\n"
			+ "        SUM(a.payment),\r\n"
			+ "        NULL AS supplierbilldate,\r\n"
			+ "        SUM(a.gstinputlcamt),\r\n"
			+ "        SUM(a.netbilllcamt),\r\n"
			+ "        SUM(a.totchargeslcamt + a.gstinputlcamt),\r\n"
			+ "        SUM(a1.totaltds),\r\n"
			+ "        NULL AS approvestatus,\r\n"
			+ "        2 AS sNo,\r\n"
			+ "        NULL AS createdon\r\n"
			+ "\r\n"
			+ "    FROM costinvoice a\r\n"
			+ "    JOIN tdscostinvoice a1 \r\n"
			+ "        ON a.costinvoiceid = a1.costinvoiceid\r\n"
			+ "    WHERE \r\n"
			+ "        a.orgid = ?1\r\n"
			+ "        AND a.finyear = ?4\r\n"
			+ "        AND (?2 IS NULL OR a.supplierbilldate >= ?2)\r\n"
			+ "        AND (?3 IS NULL OR a.supplierbilldate <= ?3)\r\n"
			+ "        AND (?5 IS NULL OR ?5 = 'ALL' OR a.suppliername = ?5)\r\n"
			+ "        AND (a.branchcode = ?6 OR ?6 = 'ALL')\r\n"
			+ ") x\r\n"
			+ "ORDER BY \r\n"
			+ "    x.sNo,\r\n"
			+ "    x.createdon DESC")
	Set<Object[]> getCostInvoiceSummary(Long orgId, String fromDate, String toDate, String finYear, String partyName,
			String branchCode);

//	@Query(nativeQuery = true, value = "SELECT \r\n"
//			+ "    a.finyear,\r\n"
//			+ "    a.docid,\r\n"
//			+ "    a.docdate,\r\n"
//			+ "    a.supplierbillno,\r\n"
//			+ "    a.purvoucherno,\r\n"
//			+ "    a.purvoucherdate,\r\n"
//			+ "    a.suppliercode,\r\n"
//			+ "    c.party,\r\n"
//			+ "    c.description,\r\n"
//			+ "    a.suppliername,\r\n"
//			+ "    a.supplierplace,\r\n"
//			+ "    a.gsttype,\r\n"
//			+ "    a.mode,\r\n"
//			+ "    a.totchargeslcamt,\r\n"
//			+ "    a.payment,\r\n"
//			+ "    b.section,\r\n"
//			+ "    b.totaltds,\r\n"
//			+ "    c.jobno,\r\n"
//			+ "    c.chargecode,\r\n"
//			+ "    c.chargername,\r\n"
//			+ "    c.ledger,\r\n"
//			+ "    c.lcamt,\r\n"
//			+ "    c.gst,\r\n"
//			+ "    c.qty,\r\n"
//			+ "    c.rate,\r\n"
//			+ "    c.lcamt + c.gst AS totalLcAmount,\r\n"
//			+ "    a.supplierbilldate,\r\n"
//			+ "    a.netbilllcamt,\r\n"
//			+ "    c.gstpercent,\r\n"
//			+ "    CASE \r\n"
//			+ "        WHEN a.approvestatus IS NULL THEN 'Not Appproved' \r\n"
//			+ "        ELSE a.approvestatus \r\n"
//			+ "    END AS approvestatus\r\n"
//			+ "FROM \r\n"
//			+ "    costinvoice a\r\n"
//			+ "JOIN \r\n"
//			+ "    tdscostinvoice b ON a.costinvoiceid = b.costinvoiceid\r\n"
//			+ "JOIN \r\n"
//			+ "    chargercostinvoice c ON a.costinvoiceid = c.costinvoiceid\r\n"
//			+ "WHERE \r\n"
//			+ "    a.orgid = ?1\r\n"
//			+ "    AND c.ledger NOT LIKE ('%GST%')\r\n"
//			+ "    AND a.finyear = ?4\r\n"
//			+ "    AND (?2 IS NULL OR a.supplierbilldate >= ?2)\r\n"
//			+ "    AND (?3 IS NULL OR a.supplierbilldate <= ?3)\r\n"
//			+ "    AND (?5 IS NULL OR ?5 = 'ALL' OR a.suppliername = ?5)\r\n"
//			+ "    AND (a.branchcode = ?6 OR ?6 = 'ALL')\r\n"
//			+ "ORDER BY \r\n"
//			+ "    a.createdon DESC")
//	Set<Object[]> getCostInvoiceDetails(Long orgId, String fromDate, String toDate, String finYear, String partyName,
//			String branchCode);
	
	
	@Query(nativeQuery = true, value = "SELECT *\r\n"
			+ "FROM (\r\n"
			+ "    SELECT \r\n"
			+ "        a.finyear,\r\n"
			+ "        a.docid,\r\n"
			+ "        a.docdate,\r\n"
			+ "        a.supplierbillno,\r\n"
			+ "        a.purvoucherno,\r\n"
			+ "        a.purvoucherdate,\r\n"
			+ "        a.suppliercode,\r\n"
			+ "        c.party,\r\n"
			+ "        c.description,\r\n"
			+ "        a.suppliername,\r\n"
			+ "        a.supplierplace,\r\n"
			+ "        a.gsttype,\r\n"
			+ "        a.mode,\r\n"
			+ "\r\n"
			+ "        a.totchargeslcamt,\r\n"
			+ "        a.payment,\r\n"
			+ "        b.section,\r\n"
			+ "        b.totaltds,\r\n"
			+ "\r\n"
			+ "        c.jobno,\r\n"
			+ "        c.chargecode,\r\n"
			+ "        c.chargername,\r\n"
			+ "        c.ledger,\r\n"
			+ "\r\n"
			+ "        c.lcamt,\r\n"
			+ "        round(c.gst,2) as gst,\r\n"
			+ "        c.qty,\r\n"
			+ "        c.rate,\r\n"
			+ "       round((c.lcamt + c.gst),2) AS totalLcAmount,\r\n"
			+ "\r\n"
			+ "        a.supplierbilldate,\r\n"
			+ "        a.netbilllcamt,\r\n"
			+ "        c.gstpercent,\r\n"
			+ "\r\n"
			+ "        CASE \r\n"
			+ "            WHEN a.approvestatus IS NULL THEN 'Not Approved'\r\n"
			+ "            ELSE a.approvestatus\r\n"
			+ "        END AS approvestatus,\r\n"
			+ "\r\n"
			+ "        1 AS sNo,\r\n"
			+ "        a.createdon\r\n"
			+ "\r\n"
			+ "    FROM costinvoice a\r\n"
			+ "    JOIN tdscostinvoice b \r\n"
			+ "        ON a.costinvoiceid = b.costinvoiceid\r\n"
			+ "    JOIN chargercostinvoice c \r\n"
			+ "        ON a.costinvoiceid = c.costinvoiceid\r\n"
			+ "    WHERE \r\n"
			+ "        a.orgid = ?1\r\n"
			+ "         AND c.ledger NOT LIKE '%GST%'\r\n"
			+ "        AND a.finyear = ?4\r\n"
			+ "        AND (?2 IS NULL OR a.supplierbilldate >= ?2)\r\n"
			+ "        AND (?3 IS NULL OR a.supplierbilldate <= ?3)\r\n"
			+ "        AND (?5 IS NULL OR ?5 = 'ALL' OR a.suppliername = ?5)\r\n"
			+ "        AND (a.branchcode = ?6 OR ?6 = 'ALL')\r\n"
			+ "    UNION ALL\r\n"
			+ "    SELECT \r\n"
			+ "        NULL,\r\n"
			+ "        'Total Amount',\r\n"
			+ "        NULL,\r\n"
			+ "        NULL,\r\n"
			+ "        NULL,\r\n"
			+ "        NULL,\r\n"
			+ "        NULL,\r\n"
			+ "        NULL,\r\n"
			+ "        NULL,\r\n"
			+ "        NULL,\r\n"
			+ "        NULL,\r\n"
			+ "        NULL,\r\n"
			+ "        NULL,\r\n"
			+ "        SUM(DISTINCT a.totchargeslcamt),\r\n"
			+ "        SUM(DISTINCT a.payment),\r\n"
			+ "        NULL,\r\n"
			+ "        SUM(DISTINCT b.totaltds),\r\n"
			+ "        NULL,\r\n"
			+ "        NULL,\r\n"
			+ "        NULL,\r\n"
			+ "        NULL,\r\n"
			+ "        SUM(c.lcamt),\r\n"
			+ "        round(SUM(c.gst),2),\r\n"
			+ "        SUM(c.qty),\r\n"
			+ "        SUM(c.rate),\r\n"
			+ "      round(SUM(c.lcamt + c.gst),2),\r\n"
			+ "\r\n"
			+ "        NULL,\r\n"
			+ "        SUM(DISTINCT a.netbilllcamt),\r\n"
			+ "        NULL,\r\n"
			+ "\r\n"
			+ "        NULL,\r\n"
			+ "        2,\r\n"
			+ "        NULL\r\n"
			+ "\r\n"
			+ "    FROM costinvoice a\r\n"
			+ "    JOIN tdscostinvoice b \r\n"
			+ "        ON a.costinvoiceid = b.costinvoiceid\r\n"
			+ "    JOIN chargercostinvoice c \r\n"
			+ "        ON a.costinvoiceid = c.costinvoiceid\r\n"
			+ "\r\n"
			+ "    WHERE \r\n"
			+ "        a.orgid =?1\r\n"
			+ "        AND c.ledger NOT LIKE '%GST%'\r\n"
			+ "       AND a.finyear = ?4\r\n"
			+ "      AND (?2 IS NULL OR a.supplierbilldate >= ?2)\r\n"
			+ "       AND (?3 IS NULL OR a.supplierbilldate <= ?3)\r\n"
			+ "        AND (?5 IS NULL OR ?5 = 'ALL' OR a.suppliername = ?5)\r\n"
			+ "	 AND (a.branchcode = ?6 OR ?6 = 'ALL')\r\n"
			+ "\r\n"
			+ ") x\r\n"
			+ "\r\n"
			+ "ORDER BY \r\n"
			+ "    x.sNo,\r\n"
			+ "    x.createdon DESC")
	Set<Object[]> getCostInvoiceDetails(Long orgId, String fromDate, String toDate, String finYear, String partyName,
			String branchCode);
	
	
	@Query(nativeQuery = true, value = "select 1 as sno, a.supplierbillno,a.supplierbilldate,a.refno,a.refdate,c.supplietype,c.suppliercode,c.suppliername,c.suppliergstin,d.currency,d.exrate,d.gstpercent,sum(d.gstamount) as gstAmount,sum(d.lcamt) as chargeAmount ,sum(d.billamt) as billAmount,c.actbillcurramt as totalAmountLc from accountsdetails b, accounts a,costinvoice c,chargercostinvoice d where \r\n"
			+ " a.accountsid=b.accountsid and a.refno=c.docid and c.cancel=0  and b.acategory='TAX' and c.costinvoiceid=d.costinvoiceid and a.sourcescreencode in('CI') and c.finyear=?3 and d.exrate <> 0.00 and  a.docdate between ?4 and ?5 and (c.suppliername=?2 or ?2='ALL') and c.orgid=?1\r\n"
			+ "group by a.supplierbillno,a.supplierbilldate,a.refno,a.refdate,c.supplietype,c.suppliercode,c.suppliername,c.suppliergstin,d.currency,d.exrate,d.gstpercent,c.actbillcurramt\r\n"
			+ " union\r\n"
			+ "select 2 as sno, a.vid,a.vdate,a.refno,a.refdate,c.supplietype,c.suppliercode,c.suppliername,c.suppliergstin,d.currency,d.exrate,d.gstpercent,sum(d.gstamount) as gstAmount,sum(d.lcamt) as chargeAmount ,sum(d.billamt) as billAmount,c.actbillcurramt as totalAmountLc from accountsdetails b, accounts a,costdebitnote c,chargercostdebitnote d where \r\n"
			+ " a.accountsid=b.accountsid and a.refno=c.docid and c.cancel=0  and b.acategory='TAX' and c.costdebitnoteid=d.costdebitnoteid and a.sourcescreencode in('CDN') and c.finyear=?3 and   d.exrate <> 0.00  and  a.docdate between ?4 and ?5 and (c.suppliername=?2 or ?2='ALL') and c.orgid=?1\r\n"
			+ "group by a.vid,a.vdate,a.refno,a.refdate,c.supplietype,c.suppliercode,c.suppliername,c.suppliergstin,d.currency,d.exrate,d.gstpercent,c.actbillcurramt\r\n"
			+ "union\r\n"
			+ "SELECT \r\n"
			+ "    3 as sno,\r\n"
			+ "    '' as vid,\r\n"
			+ "    '' as vdate,\r\n"
			+ "    '' as refno,\r\n"
			+ "    '' as refdate,\r\n"
			+ "    '' as partytype,\r\n"
			+ "    '' as partycode,\r\n"
			+ "    '' as partyname,\r\n"
			+ "    '' as recipientgstin,\r\n"
			+ "    '' as currency,\r\n"
			+ "    '' as exrate,\r\n"
			+ "    '' as gstpercent,\r\n"
			+ "    SUM(gstAmount) AS totalGstAmount,\r\n"
			+ "    SUM(chargeAmount) AS totalChargeAmount,\r\n"
			+ "    SUM(billAmount) AS totalBillAmount,\r\n"
			+ "    SUM(totalAmountLc) AS totalLcAmount\r\n"
			+ "FROM (\r\n"
			+ "    SELECT \r\n"
			+ "        1 AS sno, \r\n"
			+ "        a.supplierbillno,\r\n"
			+ "        a.supplierbilldate,\r\n"
			+ "        a.refno,\r\n"
			+ "        a.refdate,\r\n"
			+ "        c.supplietype,\r\n"
			+ "        c.suppliercode,\r\n"
			+ "        c.suppliername,\r\n"
			+ "        c.suppliergstin,\r\n"
			+ "        d.currency,\r\n"
			+ "        d.exrate,\r\n"
			+ "        d.gstpercent,\r\n"
			+ "        SUM(d.gstamount) AS gstAmount,\r\n"
			+ "        SUM(d.lcamt) AS chargeAmount,\r\n"
			+ "        SUM(d.billamt) AS billAmount,\r\n"
			+ "        c.actbillcurramt AS totalAmountLc\r\n"
			+ "    FROM \r\n"
			+ "        accounts a\r\n"
			+ "        JOIN accountsdetails b ON a.accountsid = b.accountsid\r\n"
			+ "        JOIN costinvoice c ON a.refno = c.docid\r\n"
			+ "        JOIN chargercostinvoice d ON c.costinvoiceid = d.costinvoiceid\r\n"
			+ "    WHERE \r\n"
			+ "        c.cancel = 0\r\n"
			+ "        AND b.acategory = 'TAX'\r\n"
			+ "        AND a.sourcescreencode IN ('CI')\r\n"
			+ "        AND c.finyear =?3\r\n"
			+ "        AND d.exrate <> 0.00\r\n"
			+ "        AND a.docdate BETWEEN ?4 AND ?5\r\n"
			+ "        AND (c.suppliername =?2 OR ?2= 'ALL')\r\n"
			+ "        AND c.orgid =?1\r\n"
			+ "    GROUP BY \r\n"
			+ "        a.supplierbillno, a.supplierbilldate, a.refno, a.refdate,\r\n"
			+ "        c.supplietype, c.suppliercode, c.suppliername, c.suppliergstin,\r\n"
			+ "        d.currency, d.exrate, d.gstpercent, c.actbillcurramt\r\n"
			+ "\r\n"
			+ "    UNION\r\n"
			+ "\r\n"
			+ "    SELECT \r\n"
			+ "        2 AS sno, \r\n"
			+ "        a.vid,\r\n"
			+ "        a.vdate,\r\n"
			+ "        a.refno,\r\n"
			+ "        a.refdate,\r\n"
			+ "        c.supplietype,\r\n"
			+ "        c.suppliercode,\r\n"
			+ "        c.suppliername,\r\n"
			+ "        c.suppliergstin,\r\n"
			+ "        d.currency,\r\n"
			+ "        d.exrate,\r\n"
			+ "        d.gstpercent,\r\n"
			+ "        SUM(d.gstamount) AS gstAmount,\r\n"
			+ "        SUM(d.lcamt) AS chargeAmount,\r\n"
			+ "        SUM(d.billamt) AS billAmount,\r\n"
			+ "        c.actbillcurramt AS totalAmountLc\r\n"
			+ "    FROM \r\n"
			+ "        accounts a\r\n"
			+ "        JOIN accountsdetails b ON a.accountsid = b.accountsid\r\n"
			+ "        JOIN costdebitnote c ON a.refno = c.docid\r\n"
			+ "        JOIN chargercostdebitnote d ON c.costdebitnoteid = d.costdebitnoteid\r\n"
			+ "    WHERE \r\n"
			+ "        c.cancel = 0\r\n"
			+ "        AND b.acategory = 'TAX'\r\n"
			+ "        AND a.sourcescreencode IN ('CDN')\r\n"
			+ "        AND c.finyear = ?3\r\n"
			+ "        AND d.exrate <> 0.00\r\n"
			+ "        AND a.docdate BETWEEN ?4 AND ?5\r\n"
			+ "        AND (c.suppliername =?2 OR ?2= 'ALL')\r\n"
			+ "        AND c.orgid =?1\r\n"
			+ "    GROUP BY \r\n"
			+ "        a.vid, a.vdate, a.refno, a.refdate,\r\n"
			+ "        c.supplietype, c.suppliercode, c.suppliername, c.suppliergstin,\r\n"
			+ "        d.currency, d.exrate, d.gstpercent, c.actbillcurramt\r\n"
			+ ")  combined_data\r\n"
			+ " order by 1,2,3")
	Set<Object[]> getCostGstReport(Long orgId,String partyName,String finYear,String fromDate,String toDate);

//	@Query(nativeQuery = true, 
//		       value = "SELECT * FROM costdebitnote WHERE screencode = ?1 AND docid = ?2")
//		CostDebitNoteVO getDebitNoteByDocIdandScreenCode(String screenCode, String docId);

//
//	@Query(nativeQuery = true,value="select * from costinvoice a,costdebitnote a1  where a.orgid=a1.orgid and a.suppliercode=a1.suppliercode and a.docid=a1.orginbill\r\n"
//			+ " and a.approvestatus=a1.approvestatus and a.orgid=?1 and a.suppliername=?2")
//	List<CostInvoiceVO> getCheck(Long orgId, String party);
	
	@Query(nativeQuery = true, value = "select \r\n"
			+ "    sum(t.complete) as complete, \r\n"
			+ "    sum(t.Approved) as Approved, \r\n"
			+ "    sum(t.Pending) as Pending, \r\n"
			+ "    sum(t.Reject) as Reject\r\n"
			+ "from (\r\n"
			+ "    select count(*) as complete, 0 as Approved, 0 as Pending, 0 as Reject\r\n"
			+ "    from costinvoice \r\n"
			+ "    where orgid = ?1 and finyear = ?2 and branchcode = ?3 and cancel = 0\r\n"
			+ "\r\n"
			+ "    union all\r\n"
			+ "\r\n"
			+ "    select 0 as complete, count(*) as Approved, 0 as Pending, 0 as Reject\r\n"
			+ "    from costinvoice \r\n"
			+ "    where orgid = ?1 and finyear = ?2 and branchcode = ?3 and cancel = 0 \r\n"
			+ "      and approvestatus = 'APPROVED'\r\n"
			+ "\r\n"
			+ "    union all\r\n"
			+ "\r\n"
			+ "    select 0 as complete, 0 as Approved, count(*) as Pending, 0 as Reject\r\n"
			+ "    from costinvoice \r\n"
			+ "    where orgid = ?1 and finyear = ?2 and branchcode = ?3 and cancel = 0 \r\n"
			+ "      and mode = 'EDIT'\r\n"
			+ "\r\n"
			+ "    union all\r\n"
			+ "\r\n"
			+ "    select 0 as complete, 0 as Approved, 0 as Pending, count(*) as Reject\r\n"
			+ "    from costinvoice \r\n"
			+ "    where orgid = ?1 and finyear = ?2 and branchcode = ?3 and cancel = 0 \r\n"
			+ "      and approvestatus = 'REJECTED'\r\n"
			+ ") t")
	Set<Object[]> getCostInvoiceCount(Long orgId,String finYear, String branchCode);

}
