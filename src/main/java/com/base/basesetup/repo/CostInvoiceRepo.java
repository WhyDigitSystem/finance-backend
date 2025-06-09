package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.base.basesetup.entity.CostDebitNoteVO;
import com.base.basesetup.entity.CostInvoiceVO;
import com.base.basesetup.entity.DebitNoteVO;
import com.base.basesetup.entity.PartyMasterVO;

public interface CostInvoiceRepo extends JpaRepository<CostInvoiceVO, Long> {

	@Query(nativeQuery = true, value = "select * from costinvoice where orgid=?1 and finyear=?2 and branchcode=?3")
	List<CostInvoiceVO> getAllCostInvoiceByOrgId(Long orgId,String finYear, String branchCode);

	@Query(nativeQuery = true, value = "select * from costinvoice where costinvoiceid=?1")
	List<CostInvoiceVO> getAllCostInvoiceById(Long id);
	
	@Query(value = "select a from CostInvoiceVO a where a.id=?1")
	CostInvoiceVO getCostInvoiceById(Long id);

	@Query(nativeQuery = true, value = "select * from costinvoice where active = 1")
	List<CostInvoiceVO> findCostInvoiceByActive();

	@Query(value = "select * from costInvoice a where a.orgid=?1 and docId=?2", nativeQuery = true)
	CostInvoiceVO findAllCostInvoiceByDocId(Long orgId, String docId);

	@Query(nativeQuery = true, value = "select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
	String getCostInvoiceDocId(Long orgId, String finYear, String branchCode, String screenCode);

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

	@Query(nativeQuery = true,value = "select * from costinvoice a where a.orgId=?1 and a.supplierName=?2 and a.branchCode=?3 and a.approvestatus='Approved' order by a.docId desc")
	List<CostInvoiceVO> findOrginBillNoByParty(Long orgId, String party, String branchCode);

	@Query(nativeQuery = true, value = "select  accountgroupname,gstpercentage,currency from groupledger where orgid=?1 and gsttaxflag!='NA' and category='TAX' and gsttaxflag='INPUT TAX' and gsttype=?2 and gstpercentage IN(?3) group by  accountgroupname,gstpercentage,currency order by gstpercentage desc")
	Set<Object[]> findInterDetailsForCostInvoicePosting(Long orgId, String gtsType, Double gstPercent);

	@Query(nativeQuery = true, value = "select accountgroupname,gstpercentage,currency from groupledger where orgid=?1 and gsttaxflag!='NA' and category='TAX' and gsttaxflag='INPUT TAX' and gsttype=?2 and gstpercentage IN(?3) group by  accountgroupname,gstpercentage,currency order by gstpercentage desc")
	Set<Object[]> findIntraDetailsForCostInvoicePosting(Long orgId, String gtsType, Double intraPercent);

	CostInvoiceVO findByOrgIdAndIdAndDocId(Long orgId, Long id, String docId);

	@Query(nativeQuery = true,value = "select a.creditdays from partymaster a where a.orgId=?1 and partycode=?2 and a.active=1 ")
	Set<Object[]> findCreditDaysFromVendor(Long orgId, String supplierCode);

	@Query(nativeQuery = true, value = "select accountgroupname,gstpercentage,currency from groupledger where orgid=?1 and gsttaxflag!='NA' and category='TAX' and gsttaxflag='INPUT TAX' and gsttype=?2 and gstpercentage IN(?3) order by gstpercentage desc")
	Set<Object[]> findInterAndIntraDetailsForCostInvoice(Long orgId, String gstType, List<String> gstPercent);

	@Query(nativeQuery = true, value = "select * from costinvoice where orgid=?1 and docid=?2")
	CostInvoiceVO findByOrgIdAndDocId(Long orgId, String orginBill);

	boolean existsByvIdAndOrgId(String vId, Long orgId);

	@Query(nativeQuery = true,value = "select sum(amount) as totalAmount from vw_cost where orgid=?1 and (billmonth=?2 or 'ALL'=?2) and finyear=?3")
	Set<Object[]> getDsahboardCost(Long orgId, String billMonth, String finYear);

	@Query(nativeQuery = true,value = "SELECT \r\n"
			+ "    suppliername,\r\n"
			+ "    SUM(total_amount) AS total_amount,\r\n"
			+ "    SUM(TDS_4) AS TDS_4,\r\n"
			+ "    SUM(TDS_9) AS TDS_9,\r\n"
			+ "    SUM(TDS_10) AS TDS_10,\r\n"
			+ "    financial_year,\r\n"
			+ "    partyshortname\r\n"
			+ "FROM (\r\n"
			+ "    -- ✅ MONTH block (runs only when ?2 = 'MONTH')\r\n"
			+ "    SELECT \r\n"
			+ "        a.suppliername,\r\n"
			+ "        SUM(b.totaltds) AS total_amount,\r\n"
			+ "        SUM(CASE WHEN b.tdswithholdingper = 4 THEN b.totaltds ELSE 0 END) AS TDS_4,\r\n"
			+ "        SUM(CASE WHEN b.tdswithholdingper = 9 THEN b.totaltds ELSE 0 END) AS TDS_9,\r\n"
			+ "        SUM(CASE WHEN b.tdswithholdingper = 10 THEN b.totaltds ELSE 0 END) AS TDS_10,\r\n"
			+ "        a.finyear AS financial_year,\r\n"
			+ "        p.partyshortname\r\n"
			+ "    FROM costinvoice a \r\n"
			+ "    JOIN tdscostinvoice b ON a.costinvoiceid = b.costinvoiceid\r\n"
			+ "    JOIN partymaster p ON p.partyname = a.suppliername\r\n"
			+ "    WHERE \r\n"
			+ "        a.orgid = ?1\r\n"
			+ "        AND ?2 = 'MONTH'\r\n"
			+ "        AND CAST(a.finyear AS SIGNED) = ?3\r\n"
			+ "        AND MONTH(a.vdate) = MONTH(CURDATE())\r\n"
			+ "    GROUP BY a.suppliername, a.finyear, p.partyshortname\r\n"
			+ "\r\n"
			+ "    UNION \r\n"
			+ "\r\n"
			+ "    -- ✅ YEAR block (runs only when ?2 = 'YEAR')\r\n"
			+ "    SELECT \r\n"
			+ "        a.suppliername,\r\n"
			+ "        SUM(b.totaltds) AS total_amount,\r\n"
			+ "        SUM(CASE WHEN b.tdswithholdingper = 4 THEN b.totaltds ELSE 0 END) AS TDS_4,\r\n"
			+ "        SUM(CASE WHEN b.tdswithholdingper = 9 THEN b.totaltds ELSE 0 END) AS TDS_9,\r\n"
			+ "        SUM(CASE WHEN b.tdswithholdingper = 10 THEN b.totaltds ELSE 0 END) AS TDS_10,\r\n"
			+ "        a.finyear AS financial_year,\r\n"
			+ "        p.partyshortname\r\n"
			+ "    FROM costinvoice a \r\n"
			+ "    JOIN tdscostinvoice b ON a.costinvoiceid = b.costinvoiceid\r\n"
			+ "    JOIN partymaster p ON p.partyname = a.suppliername\r\n"
			+ "    WHERE \r\n"
			+ "        a.orgid = ?1\r\n"
			+ "        AND ?2 = 'YEAR'\r\n"
			+ "        AND (\r\n"
			+ "            (MONTH(a.vdate) >= 4 AND CAST(a.finyear AS SIGNED) = ?3)\r\n"
			+ "            OR\r\n"
			+ "            (MONTH(a.vdate) < 4 AND CAST(a.finyear AS SIGNED) = (?3 + 1))\r\n"
			+ "        )\r\n"
			+ "    GROUP BY a.suppliername, a.finyear, p.partyshortname\r\n"
			+ ") t\r\n"
			+ "GROUP BY suppliername, financial_year, partyshortname\r\n"
			+ "")
	Set<Object[]> getTdsSummary(Long orgId, String month, Long finYear);

	@Query(nativeQuery = true,value ="SELECT \r\n"
			+ "    SUM(a.curmnth) AS currentmonth,\r\n"
			+ "    SUM(a.premonth) AS previousmonth,\r\n"
			+ "    SUM(a.curyear) AS currentyear,\r\n"
			+ "    SUM(a.preyear) AS previousyear\r\n"
			+ "FROM (\r\n"
			+ "    -- Current Month Revenue\r\n"
			+ "  \r\n"
			+ "SELECT \r\n"
			+ "        v.orgid, \r\n"
			+ "        SUM(v.amount) AS curmnth, \r\n"
			+ "        0 AS premonth,\r\n"
			+ "        0 AS curyear,\r\n"
			+ "        0 AS preyear\r\n"
			+ "    FROM vw_cost v\r\n"
			+ "    INNER JOIN financialyear f \r\n"
			+ "        ON v.finyear = f.finyear \r\n"
			+ "        AND v.docdate BETWEEN f.startdate AND f.enddate\r\n"
			+ "    WHERE \r\n"
			+ "        CAST(f.finyear AS SIGNED) = ?2\r\n"
			+ "        AND MONTH(v.docdate) = MONTH(CURDATE()) \r\n"
			+ "        AND v.orgid = ?1\r\n"
			+ "        AND ?3 = 'MONTH'\r\n"
			+ "    GROUP BY v.orgid\r\n"
			+ "\r\n"
			+ "    UNION\r\n"
			+ "\r\n"
			+ "    -- Previous Month Revenue\r\n"
			+ "    SELECT \r\n"
			+ "        v.orgid, \r\n"
			+ "        0 AS curmnth, \r\n"
			+ "        SUM(v.amount) AS premonth,\r\n"
			+ "        0 AS curyear,\r\n"
			+ "        0 AS preyear\r\n"
			+ "    FROM vw_cost v\r\n"
			+ "    INNER JOIN financialyear f \r\n"
			+ "        ON v.finyear = f.finyear \r\n"
			+ "        AND v.docdate BETWEEN f.startdate AND f.enddate\r\n"
			+ "    WHERE \r\n"
			+ "        v.finyear=( case \r\n"
			+ "      	when MONTH(CURDATE()) = 4 and year(curdate())=?2  then (?2-1) else ?2 end) \r\n"
			+ "        	and month(v.docdate) = (case when MONTH(CURDATE()) = 4 and year(curdate())=?2  then 3 else (MONTH(CURDATE())-1) end )\r\n"
			+ "  and ?3='MONTH'\r\n"
			+ "    GROUP BY v.orgid\r\n"
			+ "\r\n"
			+ "    UNION\r\n"
			+ "\r\n"
			+ "    -- Current Financial Year Revenue\r\n"
			+ "    SELECT \r\n"
			+ "        v.orgid,\r\n"
			+ "        0 AS curmnth, \r\n"
			+ "        0 AS premonth, \r\n"
			+ "        SUM(v.amount) AS curyear, \r\n"
			+ "        0 AS preyear\r\n"
			+ "    FROM vw_cost v\r\n"
			+ "    WHERE \r\n"
			+ "        v.finyear =?2\r\n"
			+ "        AND v.orgid = ?1\r\n"
			+ "        AND ?4 = 'YEAR'\r\n"
			+ "    GROUP BY v.orgid\r\n"
			+ "\r\n"
			+ "    UNION \r\n"
			+ "\r\n"
			+ "    -- Previous Financial Year Revenue\r\n"
			+ "    SELECT  \r\n"
			+ "        v.orgid,\r\n"
			+ "        0 AS curmnth, \r\n"
			+ "        0 AS premonth, \r\n"
			+ "        0 AS curyear, \r\n"
			+ "        SUM(v.amount) AS preyear\r\n"
			+ "    FROM vw_cost v\r\n"
			+ "    WHERE \r\n"
			+ "        v.finyear =(?2 -1)\r\n"
			+ "        AND v.orgid = ?1\r\n"
			+ "        AND ?4 = 'YEAR'\r\n"
			+ "    GROUP BY v.orgid\r\n"
			+ "    \r\n"
			+ "    \r\n"
			+ ") a")
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
	
	@Query(nativeQuery =true,value="SELECT \r\n"
			+ "    SUM(a.curyear) AS currentyear,\r\n"
			+ "    SUM(a.preyear) AS previousyear,\r\n"
			+ "    SUM(a.curmonth) AS currentmonth,\r\n"
			+ "    SUM(a.premonth) AS previousmonth \r\n"
			+ "FROM (\r\n"
			+ "    SELECT \r\n"
			+ "        SUM(r.receiptamt) AS curyear,\r\n"
			+ "        0 AS preyear,\r\n"
			+ "        0 AS curmonth,\r\n"
			+ "        0 AS premonth,\r\n"
			+ "        r.orgid \r\n"
			+ "    FROM receipt r\r\n"
			+ "    WHERE r.finyear = ?2\r\n"
			+ "        AND r.orgid = ?1\r\n"
			+ "        AND ?3 = 'YEAR'\r\n"
			+ "    GROUP BY r.orgid\r\n"
			+ "\r\n"
			+ "    UNION\r\n"
			+ "\r\n"
			+ "    SELECT \r\n"
			+ "        0 AS curyear,\r\n"
			+ "        SUM(r.receiptamt) AS preyear,\r\n"
			+ "        0 AS curmonth,\r\n"
			+ "        0 AS premonth,\r\n"
			+ "        r.orgid \r\n"
			+ "    FROM receipt r\r\n"
			+ "    WHERE r.finyear = (?2 - 1)\r\n"
			+ "        AND r.orgid = ?1\r\n"
			+ "        AND ?3 = 'YEAR'\r\n"
			+ "    GROUP BY r.orgid\r\n"
			+ "\r\n"
			+ "    UNION\r\n"
			+ "\r\n"
			+ "    SELECT \r\n"
			+ "        0 AS curyear,\r\n"
			+ "        0 AS preyear,\r\n"
			+ "        SUM(r.receiptamt) AS curmonth,\r\n"
			+ "        0 AS premonth,\r\n"
			+ "        r.orgid \r\n"
			+ "    FROM receipt r\r\n"
			+ "    JOIN financialyear f ON f.finyear = r.finyear \r\n"
			+ "    WHERE r.docdate BETWEEN f.startdate AND f.enddate \r\n"
			+ "        AND MONTH(r.docdate) = MONTH(CURDATE()) \r\n"
			+ "        AND CAST(r.finyear AS SIGNED) = ?2\r\n"
			+ "        AND r.orgid = ?1\r\n"
			+ "         AND ?3 = 'MONTH'\r\n"
			+ "    GROUP BY r.orgid\r\n"
			+ "\r\n"
			+ "    UNION \r\n"
			+ "SELECT \r\n"
			+ "    0 AS curyear,\r\n"
			+ "    0 AS preyear,\r\n"
			+ "    0 AS curmonth,\r\n"
			+ "    SUM(r.receiptamt) AS premonth,\r\n"
			+ "    r.orgid \r\n"
			+ "FROM receipt r\r\n"
			+ "JOIN financialyear f ON f.finyear = r.finyear \r\n"
			+ "WHERE r.docdate BETWEEN f.startdate AND f.enddate \r\n"
			+ "  AND r.orgid = ?1\r\n"
			+ "  AND r.finyear = (\r\n"
			+ "      CASE \r\n"
			+ "          WHEN MONTH(CURDATE()) = 4 THEN ?2 - 1 \r\n"
			+ "          ELSE ?2\r\n"
			+ "      END\r\n"
			+ "  )\r\n"
			+ "  AND MONTH(r.docdate) = (\r\n"
			+ "      CASE \r\n"
			+ "          WHEN MONTH(CURDATE()) = 4 THEN  MONTH(CURDATE()) - 1 else    MONTH(CURDATE())    \r\n"
			+ "                           -- e.g., May -> April\r\n"
			+ "      END\r\n"
			+ "  )\r\n"
			+ "GROUP BY r.orgid) a")

	Set<Object[]> getPercentageFromReceipt(Long orgId, Long finYear, String month);

	@Query(nativeQuery=true,value="select c.suppliername,p.partyshortname,sum(d.totaltds) as totaltds,c.finyear  from tdscostinvoice d join costinvoice c  on d.costinvoiceid=c.costinvoiceid\r\n"
			+ "join accounts a on a.vid=c.vid join partymaster p on c.suppliercode=p.partycode where c.finyear=?2 and c.orgid=?1 and c.branchcode=?3 \r\n"
			+ " group by c.suppliername,p.partyshortname,c.finyear")
	Set<Object[]> getTotaltdsFromCustomer(Long orgId, Long finYear, String branchCode);

	@Query(nativeQuery=true,value="select c.vid,c.vdate, c.suppliername,p.partyshortname,d.totaltds,c.finyear  from tdscostinvoice d join costinvoice c  on d.costinvoiceid=c.costinvoiceid\r\n"
			+ "join accounts a on a.vid=c.vid join partymaster p on c.suppliercode=p.partycode where c.suppliername=?4 \r\n"
			+ "and c.finyear=?2 and c.orgid=?1 and c.branchcode=?3 \r\n"
			+ "group by c.vid,c.vdate, c.suppliername,p.partyshortname,d.totaltds,c.finyear")
	Set<Object[]> getTotaltdsFromCustomerBillWise(Long orgId, Long finYear, String branchCode, String partyName);
	
	
	//cost invoice hyperlink
	@Query(nativeQuery=true,value="select * from costinvoice where docid=?1 and screencode=?2")
	CostInvoiceVO getCostByDocIdandScreenCode(String screenCode, String docId);

	@Query(nativeQuery=true,value="select * from costdebitnote where docid=?1 and screencode=?2")
	CostDebitNoteVO getDebitNoteByDocIdandScreenCode(String screenCode, String docId);
	
	
	
//
//	@Query(nativeQuery = true,value="select * from costinvoice a,costdebitnote a1  where a.orgid=a1.orgid and a.suppliercode=a1.suppliercode and a.docid=a1.orginbill\r\n"
//			+ " and a.approvestatus=a1.approvestatus and a.orgid=?1 and a.suppliername=?2")
//	List<CostInvoiceVO> getCheck(Long orgId, String party);



}
