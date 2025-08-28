package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.PaymentVO;

@Repository
public interface PaymentRepo extends JpaRepository<PaymentVO, Long> {

	@Query(value = "Select * from payment where paymentid=?1", nativeQuery = true)
	List<PaymentVO> getPaymentById(Long id);

	@Query(value = "select * from payment  where orgid=?1  and finyear=?2 and branchcode=?3", nativeQuery = true)
	List<PaymentVO> getAllPaymentByOrgId(Long orgId,String finYear, String branchCode);

	@Query(nativeQuery = true, value = "select a.docid,a.docdate,a.partyname,a.bankcashacc,a.receiptamt,a.bankcharges,a.tdsamt,a.chequebank,a.chequeno,b.invno,b.invdate,b.refno,b.refdate,b.amount,b.outstanding,b.settled,a.createdon,a.createdby from payment a, paymentinvdtls b where a.paymentid=b.paymentid and a.orgid=?1 and a.docdate BETWEEN ?2 AND ?3 and a.partyname =?4")
	Set<Object[]> findAllPaymentRegister(Long orgId, String fromDate, String toDate, String subLedgerName);

	@Query(nativeQuery = true, value = "SELECT p.partyname,p.partycode,c.transcurrency,s.statecode,s.gstin FROM partymaster p,partystate s ,partycurrencymapping c\r\n"
			+ "WHERE p.partymasterid = s.partymasterid and c.partymasterid = p.partymasterid  and p.orgid =?1\r\n"
			+ "AND p.active = 1 and p.partytype='VENDOR' and p.partyname=?2")
	Set<Object[]> findPartyNameAndCodeForPayment(Long orgId, String partyName);

	@Query(nativeQuery = true, value = "SELECT a.currency AS incurrency FROM partymaster a WHERE a.orgid = ?1 AND a.branch = ?2 AND a.branchcode = ?3  AND a.finyear = ?4 \r\n"
			+ "  AND a.partyname = ?5 AND a.active = 1 UNION SELECT b.transcurrency AS incurrency FROM partymaster a JOIN partycurrencymapping b \r\n"
			+ "  ON a.partymasterid = b.partymasterid WHERE a.orgid = ?1 AND a.branch = ?2  AND a.branchcode = ?3 \r\n"
			+ "  AND a.finyear = ?4 AND a.partyname = ?5 AND a.active = 1")
	Set<Object[]> findCurrencyAndTransCurrencyForPayment(Long orgId, String branch, String branchCode, String finYear,
			String partyName);

	@Query(nativeQuery = true, value = "SELECT CONCAT(statecode, ' - ', state) AS statecode FROM state WHERE orgid =?1 and active=1 ")
	Set<Object[]> findStateCodeByOrgIdForPayment(Long orgId);

	@Query(nativeQuery = true, value = "SELECT accountgroupname FROM groupledger WHERE accountgroupname LIKE '%TDS%' and orgid =?1 and active=1 ")
	Set<Object[]> findAccountGroupNameByOrgIdForPayment(Long orgId);

	@Query(nativeQuery = true, value = "select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
	String getPaymentDocId(Long orgId, String finYear, String branchCode, String screenCode);


	@Query(nativeQuery =true,value ="select p.partyname,p.partycode from partymaster p where orgid=?1  and p.partytype='VENDOR' group by p.partyname,p.partycode")
	Set<Object[]> findPartyNameAndPartyCode(Long orgId);

	@Query(nativeQuery = true, value = "select * from payment where orgid=?1 and branchcode=?2 and cancel=0")
	List<PaymentVO> getAllVendorPaymentByOrgIdAndBranchCode(Long orgId, String branchCode);

	@Query(nativeQuery =true,value ="SELECT SUM(r.paymentamt) AS paymentamt\r\n"
			+ "FROM payment r\r\n"
			+ "WHERE r.orgid = ?1 and r.finyear=?3 and ((month(docdate)=month(current_date()) and '?2'='Month')or ?2 is null )")
	Set<Object[]> getPaymentAmont(Long orgId, String month, String year);
	
	
	@Query(nativeQuery =true,value = "with n AS (\r\n"
			+ "    SELECT \r\n"
			+ "        orgid, \r\n"
			+ "        refno, \r\n"
			+ "        SUM(amount) AS settamt \r\n"
			+ "    FROM arapadjustments \r\n"
			+ "    WHERE amount > 0 \r\n"
			+ "    GROUP BY orgid, refno\r\n"
			+ "),\r\n"
			+ " b AS (\r\n"
			+ "    SELECT \r\n"
			+ "        subledgercode, \r\n"
			+ "        refno AS docid, \r\n"
			+ "        SUM(amount) AS settled\r\n"
			+ "    FROM arapadjustments\r\n"
			+ "    WHERE CANCEL = 'F'\r\n"
			+ "    GROUP BY subledgercode, refno \r\n"
			+ "    HAVING SUM(amount) > 0\r\n"
			+ ")\r\n"
			+ "select ROW_NUMBER() OVER () AS id,\r\n"
			+ "    a.branch,\r\n"
			+ "    a.subledgercode,\r\n"
			+ "    c.vid,\r\n"
			+ "    c.vdate,\r\n"
			+ "    a.refno,\r\n"
			+ "    a.refdate,\r\n"
			+ "    a.supprefno,\r\n"
			+ "    a.suprefdate,\r\n"
			+ "    a.acccurrency,\r\n"
			+ "    d.exrate,\r\n"
			+ "    SUM(d.totchargeslcamt - COALESCE(h.totchargeslcamt, 0)) AS totalAmount,\r\n"
			+ "    SUM(d.actbillcurramt - COALESCE(h.actbillcurramt, 0)) - COALESCE(n.settamt, 0) AS invamount,\r\n"
			+ "    a.chargableamt,\r\n"
			+ "    SUM(d.gstinputlcamt - COALESCE(h.gstinputlcamt, 0)) AS gstamount from arapdetails a JOIN costinvoice d \r\n"
			+ "    ON a.refno = d.docid  LEFT JOIN costdebitnote h \r\n"
			+ "    ON d.docid = h.orginbill and h.approvestatus='Approved' JOIN accounts c \r\n"
			+ "    ON a.docid = c.docid left JOIN n \r\n"
			+ "    ON n.orgid = a.orgid \r\n"
			+ "    AND n.refno = c.vid LEFT JOIN b \r\n"
			+ "    ON a.subledgercode = b.subledgercode \r\n"
			+ "    AND a.docid = b.docid WHERE \r\n"
			+ "    d.branchcode = ?3 \r\n"
			+ "    AND a.CANCEL = 'F'\r\n"
			+ "    AND a.subledgercode = ?2\r\n"
			+ "    AND a.orgid = ?1  group by\r\n"
			+ "    a.branch,\r\n"
			+ "    a.subledgercode,\r\n"
			+ "    c.vid,\r\n"
			+ "    c.vdate,\r\n"
			+ "    a.refno,\r\n"
			+ "    a.refdate,\r\n"
			+ "    a.supprefno,\r\n"
			+ "    a.suprefdate,\r\n"
			+ "    a.acccurrency,\r\n"
			+ "    d.exrate,\r\n"
			+ "    a.chargableamt,\r\n"
			+ "    a.tdsamt,n.settamt having SUM(d.actbillcurramt - COALESCE(h.actbillcurramt, 0)) - COALESCE(n.settamt, 0)>0")
	Set<Object[]> getPaymentFillGrid(Long orgId, String partyCode,String branchCode);
	
	@Query(nativeQuery = true, value = "SELECT p.onaccount - COALESCE(SUM(a2.settled), 0) AS netAmount,p.docid,p.docdate FROM payment p LEFT JOIN apadjustmentoffset a1 ON p.docid = a1.paymentdocid \r\n"
			+ "LEFT JOIN apoffsetinvoicedetails a2 ON a2.apadjustmentoffsetid = a1.apadjustmentoffsetid\r\n"
			+ "		WHERE p.orgid =?1 AND p.branchcode =?2 AND p.cancel = 0 AND p.partyname =?3 AND p.approvestatus = 'Approved' AND \r\n"
			+ "		p.onaccount > 0 GROUP BY p.onaccount,  p.docid,p.docdate  HAVING p.onaccount - COALESCE(SUM(a2.settled), 0) > 0")
	Set<Object[]> getAllPaymentByOrgIdAndBranchCode(Long orgId, String branchCode,String partyName);

	PaymentVO findByOrgIdAndIdAndDocId(Long orgId, Long id, String docId);

	
	@Query(nativeQuery = true, value = "SELECT   \r\n"
			+ "    a.finyear,  \r\n"
			+ "    b.invno,  \r\n"
			+ "    b.invdate,  \r\n"
			+ "    a.docid,  \r\n"
			+ "    a.docdate,  \r\n"
			+ "    b.refno,  \r\n"
			+ "    b.refdate,  \r\n"
			+ "    a.partyname,  \r\n"
			+ "    a.partycode,  \r\n"
			+ "    a.paymentamt,  \r\n"
			+ "    a.netamount,  \r\n"
			+ "    a.onaccount,  \r\n"
			+ "    a.chequeno,  \r\n"
			+ "    a.chequedate,  \r\n"
			+ "    a.tdsamt,  \r\n"
			+ "    b.amount,  \r\n"
			+ "    b.gstamount,  \r\n"
			+ "    b.chargeamt,  \r\n"
			+ "    b.settled,  \r\n"
			+ "    b.outstanding,\r\n"
			+ "    a.status,\r\n"
			+ "    case when a.approvestatus is null then 'Not Appproved' else a.approvestatus end as approvestatus  \r\n"
			+ "FROM   \r\n"
			+ "    payment a,  \r\n"
			+ "    paymentinvdtls b  \r\n"
			+ "WHERE   \r\n"
			+ "    a.paymentid = b.paymentid  \r\n"
			+ "    AND a.orgid = ?1 \r\n"
			+ "    AND (a.partyname = ?3 OR ?3= 'ALL')  \r\n"
			+ "    AND a.finyear = ?2  \r\n"
			+ "    AND (?4 IS NULL OR a.docdate >=?4)  \r\n"
			+ "    AND (?5 IS NULL OR a.docdate <=?5)  \r\n"
			+ "    and (a.branchcode =?6  OR ?6 = 'ALL')\r\n"
			+ "ORDER BY   \r\n"
			+ "    a.createdon DESC")
	Set<Object[]> getPaymentDetails(Long orgId, String finYear, String partyname, String fromDate, String toDate, String branchCode);

	
	@Query(nativeQuery = true, value = "SELECT   \r\n"
			+ "    a.finyear,   \r\n"
			+ "    a.docid,   \r\n"
			+ "    a.docdate,   \r\n"
			+ "    a.partyname,  \r\n"
			+ "    a.partycode,  \r\n"
			+ "    a.chequeno,   \r\n"
			+ "    a.chequedate,   \r\n"
			+ "    a.paymentamt,   \r\n"
			+ "    a.netamount,  \r\n"
			+ "    a.tdsamt,   \r\n"
			+ "    a.onaccount,   \r\n"
			+ "    a.bankcashacc,\r\n"
			+ "    a.status,\r\n"
			+ "    case when\r\n"
			+ "    a.approvestatus is null then 'Not Appproved' else a.approvestatus end as approvestatus\r\n"
			+ "FROM   \r\n"
			+ "    payment a  \r\n"
			+ "WHERE   \r\n"
			+ "    a.orgid = ?1  \r\n"
			+ "    AND (a.partyname = ?3 OR ?3 = 'ALL')  \r\n"
			+ "    AND a.finyear = ?2  \r\n"
			+ "    AND (?4 IS NULL OR a.docdate >= ?4)  \r\n"
			+ "    AND (?5 IS NULL OR a.docdate <= ?5)  \r\n"
			+ "    and (a.branchcode = ?6 OR ?6 = 'ALL')\r\n"
			+ "ORDER BY   \r\n"
			+ "    a.createdon DESC")
	Set<Object[]> getPaymentSummary(Long orgId, String finYear, String partyname, String fromDate, String toDate,String branchCode);

	@Query(nativeQuery = true, value = "select * from payment where orgid=?1 and docid=?2")
	PaymentVO findAllPaymentByDocId(Long orgId, String docId);
	
	@Query(nativeQuery = true, value = "select 1 as sno, a.docid,a.docdate,a.refno,a.refdate,a1.accountname,c.suppliername,p.partytype,p.gstin,p.panno,p.tanno,c1.totaltds,sum(c2.gstamount) as gstAmount,sum(c2.lcamt) as chargeAmount ,sum(c2.billamt) as billAmount,c.actbillcurramt as totalAmountLc,c1.tdswithholdingper,c2.gstpercent  from accounts a,accountsdetails a1,costinvoice c,tdscostinvoice c1,chargercostinvoice c2,  partymaster p,branch b\r\n"
			+ " where b.branch=c.branch and c.costinvoiceid=c2.costinvoiceid  and c.costinvoiceid=c1.costinvoiceid and  p.partyname=c.suppliername  and  a.accountsid=a1.accountsid and a.refno=c.docid and a.sourcescreencode in('CI') and a1.accountname='TDS PAYABLE' and c2.billamt <> 0.00 and (b.branch=?6 or ?6='ALL')\r\n"
			+ " and (c.suppliername=?2 or ?2='ALL') and a.docdate between ?4 and ?5 and c.finyear=?3 and c.orgid=?1\r\n"
			+ " group by a.docid,a.docdate,a.refno,a.refdate,a1.accountname,c.suppliername,p.partytype,p.gstin,p.panno,p.tanno,c1.totaltds,c.actbillcurramt,c1.tdswithholdingper,c2.gstpercent\r\n"
			+ " union\r\n"
			+ "select 2 as sno, a.docid,a.docdate,a.refno,a.refdate,a1.accountname,c.suppliername,p.partytype,p.gstin,p.panno,p.tanno,c1.totaltds,sum(c2.gstamount) as gstAmount,sum(c2.lcamt) as chargeAmount ,sum(c2.billamt) as billAmount,c.actbillcurramt as totalAmountLc,c1.tdswithholdingper,c2.gstpercent  from accounts a,accountsdetails a1,costdebitnote c,costdebitnotetaxprtcul c1,chargercostdebitnote c2,  partymaster p,branch b\r\n"
			+ " where b.branch=c.branch and c.costdebitnoteid=c2.costdebitnoteid  and c.costdebitnoteid=c1.costdebitnoteid and  p.partyname=c.suppliername  and  a.accountsid=a1.accountsid and a.refno=c.docid and a.sourcescreencode in('CDN') and a1.accountname='TDS PAYABLE' and c2.billamt <> 0.00 and (b.branch=?6 or ?6='ALL')\r\n"
			+ " and (c.suppliername=?2 or ?2='ALL') and a.docdate between ?4 and ?5 and c.finyear=?3 and c.orgid=?1\r\n"
			+ " group by a.docid,a.docdate,a.refno,a.refdate,a1.accountname,c.suppliername,p.partytype,p.gstin,p.panno,p.tanno,c1.totaltds,c.actbillcurramt,c1.tdswithholdingper,c2.gstpercent\r\n"
			+ " union\r\n"
			+ " select 3 as sno, a.docid,a.docdate,a.refno,a.refdate,a1.accountname,c.partyname,p.partytype,p.gstin,p.panno,p.tanno,c1.totaltdsamt,sum(c2.gstamt) as gstAmount,sum(c2.lcamt) as chargeAmount ,sum(c2.billamt) as billAmount,c.actbillamtlc as totalAmountLc,c1.tdsper,c2.gstper  from accounts a,accountsdetails a1,rcostinvoicegna c,tdsrcostinvoicegna c1,chargercostinvoicegna c2,  partymaster p,branch b\r\n"
			+ " where b.branch=c.branch and c.rcostinvoicegnaid=c2.rcostinvoicegnaid  and c.rcostinvoicegnaid=c1.rcostinvoicegnaid and  p.partyname=c.partyname  and  a.accountsid=a1.accountsid and a.refno=c.docid and a.sourcescreencode in('RCI') and a1.accountname='TDS PAYABLE' and c2.billamt <> 0.00 and (b.branch=?6 or ?6='ALL')\r\n"
			+ " and (c.partyname=?2 or ?2='ALL') and a.docdate between ?4 and ?5 and c.finyear=?3 and c.orgid=?1\r\n"
			+ " group by a.docid,a.docdate,a.refno,a.refdate,a1.accountname,c.partyname,p.partytype,p.gstin,p.panno,p.tanno,c1.totaltdsamt,c.actbillamtlc,c1.tdsper,c2.gstper\r\n"
			+ "union\r\n"
			+ " select 4 as sno, a.docid,a.docdate,a.refno,a.refdate,a1.accountname,c.suppliername,p.partytype,p.gstin,p.panno,p.tanno,c1.totaltdswithamt,sum(c.totalgstamount) as gstAmount,sum(c2.lcamount) as chargeAmount ,sum(c2.billamount) as billAmount,c.actbillamtlc as totalAmountLc,c1.tdswithholdingper,c2.gstpercent  from accounts a,accountsdetails a1,urcostinvoicegna c,tdsurcostinvoicegna c1,chargesurcostinvoicegna c2,  partymaster p,branch b\r\n"
			+ " where b.branch=c.branch and c.urcostinvoicegnaid=c2.urcostinvoicegnaid  and c.urcostinvoicegnaid=c1.urcostinvoicegnaid and  p.partyname=c.suppliername  and  a.accountsid=a1.accountsid and a.refno=c.docid and a.sourcescreencode in('URCI') and a1.accountname='TDS PAYABLE' and c2.billamount <> 0.00 and (b.branch=?6 or ?6='ALL')\r\n"
			+ " and (c.suppliername=?2 or ?2='ALL') and a.docdate between ?4 and ?5 and c.finyear=?3 and c.orgid=?1\r\n"
			+ " group by a.docid,a.docdate,a.refno,a.refdate,a1.accountname,c.suppliername,p.partytype,p.gstin,p.panno,p.tanno,c1.totaltdswithamt,c.actbillamtlc,c1.tdswithholdingper,c2.gstpercent\r\n"
			+ " union\r\n"
			+ " SELECT \r\n"
			+ "     5 AS sno,\r\n"
			+ "    NULL AS docid,\r\n"
			+ "    NULL AS docdate,\r\n"
			+ "    NULL AS refno,\r\n"
			+ "    NULL AS refdate,\r\n"
			+ "    NULL AS accountname,\r\n"
			+ "    NULL AS suppliername,\r\n"
			+ "    NULL AS partytype,\r\n"
			+ "    NULL AS gstin,\r\n"
			+ "    NULL AS panno,\r\n"
			+ "    NULL AS tanno,\r\n"
			+ "    SUM(totaltds) AS totaltds,\r\n"
			+ "    SUM(gstAmount) AS gstAmount,\r\n"
			+ "    SUM(chargeAmount) AS chargeAmount,\r\n"
			+ "    SUM(billAmount) AS billAmount,\r\n"
			+ "    SUM(totalAmountLc) AS totalAmountLc,\r\n"
			+ "    NULL AS tdswithholdingper,\r\n"
			+ "    NULL AS gstpercent\r\n"
			+ "FROM (\r\n"
			+ "select 1 as sno, a.docid,a.docdate,a.refno,a.refdate,a1.accountname,c.suppliername,p.partytype,p.gstin,p.panno,p.tanno,c1.totaltds,sum(c2.gstamount) as gstAmount,sum(c2.lcamt) as chargeAmount ,sum(c2.billamt) as billAmount,c.actbillcurramt as totalAmountLc,c1.tdswithholdingper,c2.gstpercent  from accounts a,accountsdetails a1,costinvoice c,tdscostinvoice c1,chargercostinvoice c2,  partymaster p,branch b\r\n"
			+ " where b.branch=c.branch and c.costinvoiceid=c2.costinvoiceid  and c.costinvoiceid=c1.costinvoiceid and  p.partyname=c.suppliername  and  a.accountsid=a1.accountsid and a.refno=c.docid and a.sourcescreencode in('CI') and a1.accountname='TDS PAYABLE' and c2.billamt <> 0.00 and (b.branch=?6 or ?6='ALL')\r\n"
			+ " and (c.suppliername=?2 or ?2='ALL') and a.docdate between ?4 and ?5 and c.finyear=?3 and c.orgid=?1\r\n"
			+ " group by a.docid,a.docdate,a.refno,a.refdate,a1.accountname,c.suppliername,p.partytype,p.gstin,p.panno,p.tanno,c1.totaltds,c.actbillcurramt,c1.tdswithholdingper,c2.gstpercent\r\n"
			+ " union all\r\n"
			+ "select 2 as sno, a.docid,a.docdate,a.refno,a.refdate,a1.accountname,c.suppliername,p.partytype,p.gstin,p.panno,p.tanno,c1.totaltds,sum(c2.gstamount) as gstAmount,sum(c2.lcamt) as chargeAmount ,sum(c2.billamt) as billAmount,c.actbillcurramt as totalAmountLc,c1.tdswithholdingper,c2.gstpercent  from accounts a,accountsdetails a1,costdebitnote c,costdebitnotetaxprtcul c1,chargercostdebitnote c2,  partymaster p,branch b\r\n"
			+ " where b.branch=c.branch and c.costdebitnoteid=c2.costdebitnoteid  and c.costdebitnoteid=c1.costdebitnoteid and  p.partyname=c.suppliername  and  a.accountsid=a1.accountsid and a.refno=c.docid and a.sourcescreencode in('CDN') and a1.accountname='TDS PAYABLE' and c2.billamt <> 0.00 and (b.branch=?6 or ?6='ALL')\r\n"
			+ " and (c.suppliername=?2 or ?2='ALL') and a.docdate between ?4 and ?5 and c.finyear=?3 and c.orgid=?1\r\n"
			+ " group by a.docid,a.docdate,a.refno,a.refdate,a1.accountname,c.suppliername,p.partytype,p.gstin,p.panno,p.tanno,c1.totaltds,c.actbillcurramt,c1.tdswithholdingper,c2.gstpercent\r\n"
			+ " union all\r\n"
			+ " select 3 as sno, a.docid,a.docdate,a.refno,a.refdate,a1.accountname,c.partyname,p.partytype,p.gstin,p.panno,p.tanno,c1.totaltdsamt,sum(c2.gstamt) as gstAmount,sum(c2.lcamt) as chargeAmount ,sum(c2.billamt) as billAmount,c.actbillamtlc as totalAmountLc,c1.tdsper,c2.gstper  from accounts a,accountsdetails a1,rcostinvoicegna c,tdsrcostinvoicegna c1,chargercostinvoicegna c2,  partymaster p,branch b\r\n"
			+ " where b.branch=c.branch and c.rcostinvoicegnaid=c2.rcostinvoicegnaid  and c.rcostinvoicegnaid=c1.rcostinvoicegnaid and  p.partyname=c.partyname  and  a.accountsid=a1.accountsid and a.refno=c.docid and a.sourcescreencode in('RCI') and a1.accountname='TDS PAYABLE' and c2.billamt <> 0.00 and (b.branch=?6 or ?6='ALL')\r\n"
			+ " and (c.partyname=?2 or ?2='ALL') and a.docdate between ?4 and ?5 and c.finyear=?3 and c.orgid=?1\r\n"
			+ " group by a.docid,a.docdate,a.refno,a.refdate,a1.accountname,c.partyname,p.partytype,p.gstin,p.panno,p.tanno,c1.totaltdsamt,c.actbillamtlc,c1.tdsper,c2.gstper\r\n"
			+ "union all\r\n"
			+ " select 4 as sno, a.docid,a.docdate,a.refno,a.refdate,a1.accountname,c.suppliername,p.partytype,p.gstin,p.panno,p.tanno,c1.totaltdswithamt,sum(c.totalgstamount) as gstAmount,sum(c2.lcamount) as chargeAmount ,sum(c2.billamount) as billAmount,c.actbillamtlc as totalAmountLc,c1.tdswithholdingper,c2.gstpercent  from accounts a,accountsdetails a1,urcostinvoicegna c,tdsurcostinvoicegna c1,chargesurcostinvoicegna c2,  partymaster p,branch b\r\n"
			+ " where b.branch=c.branch and c.urcostinvoicegnaid=c2.urcostinvoicegnaid  and c.urcostinvoicegnaid=c1.urcostinvoicegnaid and  p.partyname=c.suppliername  and  a.accountsid=a1.accountsid and a.refno=c.docid and a.sourcescreencode in('URCI') and a1.accountname='TDS PAYABLE' and c2.billamount <> 0.00 and (b.branch=?6 or ?6='ALL')\r\n"
			+ " and (c.suppliername=?2 or ?2='ALL') and a.docdate between ?4 and ?5 and c.finyear=?3 and c.orgid=?1\r\n"
			+ " group by a.docid,a.docdate,a.refno,a.refdate,a1.accountname,c.suppliername,p.partytype,p.gstin,p.panno,p.tanno,c1.totaltdswithamt,c.actbillamtlc,c1.tdswithholdingper,c2.gstpercent\r\n"
			+ "   \r\n"
			+ ") AS summary\r\n"
			+ "ORDER BY sno, docid, docdate, refno")
	Set<Object[]> getPaybaleTdsDetailsReport(Long orgId,String partyName,String finYear,String fromDate,String toDate,String branchName);


}
