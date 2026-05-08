package com.base.basesetup.repo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.CostDebitNoteVO;

@Repository
public interface CostDebitNoteRepo extends JpaRepository<CostDebitNoteVO, Long> {

	@Query(value = "SELECT * FROM costdebitnote where orgid=?1 and finyear=?2 and branchcode=?3", nativeQuery = true)
	List<CostDebitNoteVO> getByCostDebitNoteByOrgId(Long orgId, String finYear, String branchCode);

	@Query(value = "SELECT * FROM costdebitnote where costdebitnoteid=?1", nativeQuery = true)
	List<CostDebitNoteVO> getByCostDebitNoteById(Long id);

	@Query(value = "SELECT * FROM costdebitnote where active=1", nativeQuery = true)
	List<CostDebitNoteVO> getActiveCostDebitNote();

//	@Query(nativeQuery = true, value = "select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
//	String getCostDebitNoteDocId(Long orgId, String finYear, String branchCode, String screenCode);

	@Query(value = "SELECT " + "CONCAT(d.prefixfield, LPAD(d.lastno, 5, '0')) AS docid, " + "CASE "
			+ "   WHEN CURDATE() BETWEEN f.startdate AND f.enddate " + "   THEN CURDATE() " + "   ELSE f.enddate "
			+ "END AS docdate " + "FROM documenttypemappingdetails d " + "JOIN financialyear f "
			+ "ON d.finyear = f.finyear AND d.orgid = f.orgid " + "WHERE d.orgid = ?1 " + "AND d.finyear = ?2 "
			+ "AND d.branchcode = ?3 " + "AND d.screencode = ?4", nativeQuery = true)
	List<Object[]> getCostDebitNoteDocId(Long orgId, String finYear, String branchCode, String screenCode);

	@Query(nativeQuery = true, value = "select * from CostDebitNoteVO where orgId=?1 and docId=?2")
	CostDebitNoteVO findAllCostDebitNoteByDocId(Long orgId, String docId);

	@Query(value = "SELECT c.chargeType, c.chargeCode, c.govtSac, c.serviceAccountCode FROM ChargeTypeRequest c WHERE c.orgId =?1 AND c.purchaseAccount IS NOT NULL", nativeQuery = true)
	Set<Object[]> getChareDetails(Long orgId);

	@Query(nativeQuery = true, value = "SELECT a.partyname, a.partycode, b.addresstype\r\n" + "FROM partymaster a\r\n"
			+ "JOIN partyaddress b ON a.partymasterid = b.partymasterid  \r\n" + "WHERE a.orgId = ?1\r\n"
			+ "AND a.branch = ?2\r\n"
			+ "AND a.finyear =?3 and partytype='Vendor'GROUP BY a.partyname, a.partycode, b.addresstype")
	Set<Object[]> getParty(Long orgId, String branch, String finYear);

	@Query(value = "select docid from costinvoice where orgid=?1", nativeQuery = true)
	Set<Object[]> getDocIdForCI(Long orgId);

	@Query(nativeQuery = true, value = "select currency,currencydescripition,buyingexrate,sellingexrate from vw_exrates where orgid=?1")
	Set<Object[]> getCurrencyAndExrateDetails(Long orgId);

	@Query(nativeQuery = true, value = "SELECT a.partytype\r\n" + "FROM partymaster a\r\n" + "WHERE a.orgId = ?1\r\n"
			+ "AND a.branch = ?2\r\n" + "AND a.finyear =?3 and partytype='VENDOR'GROUP BY a.partytype")
	Set<Object[]> partyTypeForCostDebitNote(Long orgId, String branch, String finYear);

	@Query(nativeQuery = true, value = "SELECT chargedescription,chargecode,govtsac,taxable,serviceaccountcode,gsttax FROM chargetyperequest WHERE chargedescription LIKE '%INTER%' AND chargecode LIKE '%gst%' AND orgid=?1 AND gsttax IN (?2) GROUP BY chargedescription,chargecode,govtsac,taxable,serviceaccountcode,gsttax")
	Set<Object[]> findChargeNameAndChargeCodeForIgstPosting(Long orgId, String gstPercent);

	@Query(nativeQuery = true, value = "SELECT chargedescription,chargecode,govtsac,taxable,serviceaccountcode,gsttax FROM chargetyperequest WHERE chargedescription LIKE '%INTRA%' AND chargecode LIKE '%gst%' AND orgid=?1 AND gsttax IN (?2) GROUP BY chargedescription,chargecode,govtsac,taxable,serviceaccountcode,gsttax")
	Set<Object[]> findChargeNameAndChargeCodeForCgstAndSgtsPosting(Long orgId, BigDecimal gstPercent);

	CostDebitNoteVO findByOrgIdAndIdAndDocId(Long orgId, Long id, String docId);

//	@Query(nativeQuery = true, value = "select accountgroupname,gstpercentage,currency from groupledger where orgid=?1 and gsttaxflag!='NA' and category='TAX' and gsttaxflag='INPUT TAX' and gsttype=?2 and gstpercentage IN(?3) order by gstpercentage desc")
//	Set<Object[]> findInterAndIntraDetailsForCostInvoicePosting(Long orgId, String gstType, String gstPercent);
//
	@Query(nativeQuery = true, value = "select accountgroupname,gstpercentage,currency from groupledger where orgid=?1 and gsttaxflag!='NA' and category='TAX' and gsttaxflag='INPUT TAX' and gsttype=?2 and gstpercentage IN(?3) order by gstpercentage desc")
	Set<Object[]> findInterAndIntraDetailsForCostInvoice(Long orgId, String gstType, List<String> gstPercent);

	@Query(nativeQuery = true, value = "select  accountgroupname,gstpercentage,currency from groupledger where orgid=?1 and gsttaxflag!='NA' and category='TAX' and gsttaxflag='INPUT TAX' and gsttype=?2 and gstpercentage IN(?3) group by  accountgroupname,gstpercentage,currency order by gstpercentage desc")
	Set<Object[]> findInterDetailsForCostDebitNotePosting(Long orgId, String gtsType, Double gstPercent);

	@Query(nativeQuery = true, value = "select accountgroupname,gstpercentage,currency from groupledger where orgid=?1 and gsttaxflag!='NA' and category='TAX' and gsttaxflag='INPUT TAX' and gsttype=?2 and gstpercentage IN(?3) group by  accountgroupname,gstpercentage,currency order by gstpercentage desc")
	Set<Object[]> findIntraDetailsForCostDebitNotePosting(Long orgId, String gtsType, Double intraPercent);

//	@Query(nativeQuery = true, value = "select accountgroupname,category from groupledger where orgid=?1 and gsttaxflag='NA' and category='RECEIVABLE A/C' and type='ACCOUNT'  and groupname='TDS'")
//	Set<Object[]> getAccountNameFromTDSLedger(Long orgId);

	@Query(nativeQuery = true, value = "select accountgroupname,category from groupledger where orgid=?1 and gsttaxflag='NA' and category='PAYABLE A/C' and type='ACCOUNT'  and groupname='TDS'")
	Set<Object[]> getAccountNameFromTDSLedger(Long orgId);

	@Query(nativeQuery = true, value = "SELECT \r\n" + "    SUM(\r\n" + "        CASE \r\n"
			+ "            WHEN type = 'CostInvoice' THEN amount \r\n" + "            ELSE -amount \r\n"
			+ "        END\r\n" + "    ) AS amount\r\n" + "FROM (\r\n" + "    SELECT \r\n"
			+ "        'CostInvoice' AS type, \r\n" + "        SUM(a.totalcreditamount) AS amount \r\n"
			+ "    FROM accounts a\r\n" + "    JOIN costinvoice t ON a.refno = t.docid \r\n"
			+ "    WHERE t.docid = ?1 AND t.cancel = 'F'\r\n" + "\r\n" + "    UNION ALL\r\n" + "\r\n"
			+ "    SELECT \r\n" + "        'DebitNote' AS type, \r\n"
			+ "        SUM(a.totalcreditamount) AS amount \r\n" + "    FROM accounts a\r\n"
			+ "    JOIN costdebitnote i ON a.refno = i.docid \r\n" + "    WHERE i.orginbill = ?1 AND i.cancel = 'F'\r\n"
			+ "\r\n" + "    UNION ALL\r\n" + "\r\n" + "    SELECT \r\n" + "        'Payment' AS type, \r\n"
			+ "        SUM(a.totalcreditamount) AS amount \r\n" + "    FROM accounts a\r\n"
			+ "    JOIN payment r ON a.refno = r.docid \r\n"
			+ "    JOIN paymentinvdtls r1 ON r.paymentid = r1.paymentid \r\n"
			+ "    WHERE r.cancel = 'F' AND r1.refno = ?1\r\n" + "\r\n" + "    UNION ALL\r\n" + "\r\n"
			+ "    SELECT \r\n" + "        'APAdjustmentsOffSet' AS type, \r\n"
			+ "        SUM(a.totalcreditamount) AS amount \r\n" + "    FROM accounts a\r\n"
			+ "    JOIN apadjustmentoffset r ON a.refno = r.docid \r\n"
			+ "    JOIN apoffsetinvoicedetails r1 ON r.apadjustmentoffsetid = r1.apadjustmentoffsetid \r\n"
			+ "    WHERE r.cancel = 'F' AND r1.refno = ?1\r\n" + ") AS sub")
	Set<Object[]> getByAmount(String docId);

	@Query(nativeQuery = true, value = "SELECT * FROM costdebitnote WHERE screencode = ?1 AND docid = ?2")
	CostDebitNoteVO getDebitNoteByDocIdandScreenCode(String screenCode, String docId);

	@Query(nativeQuery = true, value = "SELECT \r\n" + "    income_data.suppliername,\r\n"
			+ "    income_data.docid,\r\n" + "    income_data.income,\r\n"
			+ "    COALESCE(expense_data.expense, 0) AS expense,\r\n"
			+ "    income_data.income - COALESCE(expense_data.expense, 0) AS amount\r\n" + "FROM (\r\n"
			+ "    SELECT \r\n" + "        a.suppliername, \r\n" + "        a.docid, \r\n"
			+ "        SUM(a.netbilllcamt) AS income\r\n" + "    FROM \r\n" + "        costinvoice a \r\n"
			+ "    WHERE \r\n" + "        a.orgid =?1\r\n" + "        AND a.docid =?2\r\n" + "    GROUP BY \r\n"
			+ "        a.suppliername, a.docid\r\n" + ") AS income_data\r\n" + "LEFT JOIN (\r\n" + "    SELECT \r\n"
			+ "        b.suppliername, \r\n" + "        b.orginbill, \r\n"
			+ "        COALESCE(SUM(b.netbilllcamt), 0) AS expense\r\n" + "    FROM \r\n"
			+ "        costdebitnote b\r\n" + "    WHERE \r\n" + "        b.orgid =?1\r\n"
			+ "        AND b.orginbill =?2\r\n" + "    GROUP BY \r\n" + "        b.suppliername, b.orginbill\r\n"
			+ ") AS expense_data\r\n" + "ON \r\n" + "    income_data.suppliername = expense_data.suppliername \r\n"
			+ "    AND income_data.docid = expense_data.orginbill")
	Set<Object[]> findByOrginBillBased(Long orgId, String orginbillNo);

	@Query(nativeQuery = true, value = "select \r\n" + "    sum(t.complete) as complete, \r\n"
			+ "    sum(t.Approved) as Approved, \r\n" + "    sum(t.Pending) as Pending, \r\n"
			+ "    sum(t.Reject) as Reject\r\n" + "from (\r\n"
			+ "    select count(*) as complete, 0 as Approved, 0 as Pending, 0 as Reject\r\n"
			+ "    from costdebitnote \r\n"
			+ "    where orgid =?1 and finyear = ?2 and branchcode = ?3 and cancel = 0\r\n" + "\r\n"
			+ "    union all\r\n" + "\r\n"
			+ "    select 0 as complete, count(*) as Approved, 0 as Pending, 0 as Reject\r\n"
			+ "    from costdebitnote \r\n"
			+ "    where orgid =?1 and finyear = ?2 and branchcode = ?3 and cancel = 0 \r\n"
			+ "      and approvestatus = 'APPROVED'\r\n" + "\r\n" + "    union all\r\n" + "\r\n"
			+ "    select 0 as complete, 0 as Approved, count(*) as Pending, 0 as Reject\r\n"
			+ "    from costdebitnote \r\n"
			+ "    where orgid =?1 and finyear = ?2 and branchcode = ?3 and cancel = 0 \r\n"
			+ "      and mode = 'EDIT'\r\n" + "\r\n" + "    union all\r\n" + "\r\n"
			+ "    select 0 as complete, 0 as Approved, 0 as Pending, count(*) as Reject\r\n"
			+ "    from costdebitnote \r\n"
			+ "    where orgid =?1 and finyear = ?2 and branchcode = ?3 and cancel = 0 \r\n"
			+ "      and approvestatus = 'REJECTED'\r\n" + ") t")
	Set<Object[]> getCostDebitNoteCount(Long orgId, String finYear, String branchCode);

}
