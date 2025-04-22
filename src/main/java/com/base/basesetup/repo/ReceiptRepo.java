package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.ReceiptVO;

@Repository
public interface ReceiptRepo extends JpaRepository<ReceiptVO, Long> {

	@Query(nativeQuery = true, value = "select * from receipt where orgid=?1 and finyear=?2 and branchcode=?3")
	List<ReceiptVO> getAllReceiptReceivableByOrgId(Long orgId,String finYear, String branchCode);

	@Query(nativeQuery = true, value = "select * from receipt where receiptid=?1")
	List<ReceiptVO> getAllReceiptReceivableById(Long id);

	@Query(nativeQuery = true, value = "select * from receipt where active=1")
	List<ReceiptVO> findReceiptReceivablesByActive();

	@Query(nativeQuery = true, value = "select partyname,partycode from partymaster where orgid=?1 and active=1 and partytype='CUSTOMER'")
	Set<Object[]> getCustomerNameAndCodeForReceipt(Long orgId);

	@Query(nativeQuery = true, value = "select a.docid,a.docdate,a.customername,a.bankcashacc,a.receiptamt,a.bankchargeacc,a.taxamt,a.tdsamt,b.invno,b.invdate,b.refno,b.refdate,a.chequebank,a.chequeutino,b.amount,b.outstanding,b.settled,a.createdon,a.createdby from receipt a, receiptinvdetails b where a.receiptid=b.receiptid and a.orgid=?1 and a.docdate BETWEEN ?2 AND ?3 and a.customername =?4")
	Set<Object[]> findAllReceiptRegister(Long orgId, String fromDate, String toDate, String subLedgerName);

	@Query(nativeQuery = true, value = "select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
	String getReceiptDocId(Long orgId, String finYear, String branchCode, String screenCode);

	@Query(nativeQuery = true, value = "select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
	String getArBillBalanceDocId(Long orgId, String finYear, String branchCode, String screenCode);

	@Query(nativeQuery = true, value = "WITH b AS (\r\n"
			+ "    SELECT \r\n"
			+ "        subledgercode, \r\n"
			+ "        docid, \r\n"
			+ "        SUM(amount) AS settled\r\n"
			+ "    FROM arapadjustments\r\n"
			+ "    WHERE CANCEL = 'F'\r\n"
			+ "    GROUP BY subledgercode, docid\r\n"
			+ ")\r\n"
			+ "SELECT \r\n"
			+ "    a.arapdetailsid,\r\n"
			+ "    a.branch,\r\n"
			+ "    a.subledgercode,\r\n"
			+ "    c.vid,\r\n"
			+ "    c.vdate,\r\n"
			+ "    a.refno,\r\n"
			+ "    a.refdate,\r\n"
			+ "    a.supprefno,\r\n"
			+ "    a.suprefdate,\r\n"
			+ "    a.acccurrency,\r\n"
			+ "    a.exrate,\r\n"
			+ "    SUM(t.billamount) AS totalAmount,\r\n"
			+ "    IFNULL(b.settled, 0) AS arapsettled,\r\n"
			+ "    a.chargableamt,\r\n"
			+ "    a.tdsamt,\r\n"
			+ "    t.gstpercent,\r\n"
			+ "    SUM(t.gstamount) AS gstamount\r\n"
			+ "FROM arapdetails a\r\n"
			+ "LEFT JOIN b \r\n"
			+ "    ON a.subledgercode = b.subledgercode \r\n"
			+ "    AND a.docid = b.docid\r\n"
			+ "JOIN taxinvoice d \r\n"
			+ "    ON a.refno = d.docid \r\n"
			+ "JOIN taxinvoicedetails t \r\n"
			+ "    ON d.taxinvoiceid = t.taxinvoiceid \r\n"
			+ "JOIN accounts c \r\n"
			+ "    ON a.docid = c.docid\r\n"
			+ "WHERE \r\n"
			+ "    a.CANCEL = 'F'\r\n"
			+ "    AND a.subledgercode =?2\r\n"
			+ "    AND a.orgid =?1\r\n"
			+ "GROUP BY \r\n"
			+ "    a.arapdetailsid,\r\n"
			+ "    a.branch,\r\n"
			+ "    a.subledgercode,\r\n"
			+ "    c.vid,\r\n"
			+ "    c.vdate,\r\n"
			+ "    a.refno,\r\n"
			+ "    a.refdate,\r\n"
			+ "    a.supprefno,\r\n"
			+ "    a.suprefdate,\r\n"
			+ "    a.acccurrency,\r\n"
			+ "    a.exrate,\r\n"
			+ "    b.settled,\r\n"
			+ "    a.chargableamt,\r\n"
			+ "    a.tdsamt,\r\n"
			+ "    t.gstpercent\r\n"
			+ "ORDER BY \r\n"
			+ "    a.refdate,\r\n"
			+ "    c.vid")
	Set<Object[]> findReciptFillGrid(Long orgId, String partyCode);

	@Query(nativeQuery = true, value = "select * from receipt r,receiptinvdetails r1 where r.orgid=?1 and r.customercode=?2 and r.cancel=0 and r.receiptid=r1.receiptid  and r1.issettled=false")
	List<ReceiptVO> getAllReceiptByOrgIdAndBranchCode(Long orgId, String customerCode);

	@Query(nativeQuery =true,value ="SELECT r.orgid, r.branchcode, r.finyear, \r\n"
			+ "    r.createdby, \r\n"
			+ "    r.createdon, \r\n"
			+ "    r.docid, \r\n"
			+ "    r.docdate, \r\n"
			+ "    r.receipt_type AS subtypecode, \r\n"
			+ "    r.chequebank, \r\n"
			+ "    r.chequeutino AS chqnumber, \r\n"
			+ "    r.customercode AS subledgercode, \r\n"
			+ "    r.customername AS subledgername, \r\n"
			+ "    r.receiptamt AS receiptamount, \r\n"
			+ "    r.bankcharges AS bankchargesamt, \r\n"
			+ "    CASE \r\n"
			+ "        WHEN tds_rank = 1 THEN r.tdsamt ELSE 0 \r\n"
			+ "    END AS tdsamount, \r\n"
			+ "    rd.invno AS invoiceno, \r\n"
			+ "    rd.invdate AS invoicedate, \r\n"
			+ "    rd.refno, \r\n"
			+ "    rd.refdate, \r\n"
			+ "    adj.amount AS arapamount, \r\n"
			+ "    adj.chargeableamt, \r\n"
			+ "    rd.outstanding AS arapoutstanding, \r\n"
			+ "    rd.settled AS arapsettled, \r\n"
			+ "    ?4 AS fyr, \r\n"
			+ "    ?5 AS stdt, \r\n"
			+ "    ?6 AS eddt \r\n"
			+ "FROM (\r\n"
			+ "    SELECT r.receiptid,\r\n"

			+ "        r.orgid, \r\n"
			+ "        r.branchcode, \r\n"
			+ "        r.finyear, \r\n"
			+ "        r.createdby, \r\n"
			+ "        r.createdon, \r\n"
			+ "        r.docid, \r\n"
			+ "        r.docdate, \r\n"
			+ "        r.receipt_type, \r\n"
			+ "        r.chequebank, \r\n"
			+ "        r.chequeutino, \r\n"
			+ "        r.customercode, \r\n"
			+ "        r.customername, \r\n"
			+ "        r.receiptamt, \r\n"
			+ "        r.bankcharges, \r\n"
			+ "        r.tdsamt, \r\n"
			+ "        ROW_NUMBER() OVER (PARTITION BY r.docid ORDER BY r.docid) AS tds_rank \r\n"
			+ "    FROM receipt r \r\n"
			+ "    WHERE \r\n"
			+ "        COALESCE(r.cancel, 0) = 1  \r\n"
			+ "        AND (LOWER(COALESCE(r.customername, '')) = LOWER(?2) OR 'ALL' = ?2) \r\n"
			+ "        AND (r.docdate BETWEEN date(?5) AND date(?6) OR ( 'NULL'=?5 AND 'NULL'=?6))\r\n"
			+ "        AND (LOWER(COALESCE(r.branchcode, '')) = LOWER(?3) OR 'ALL' = ?3)\r\n"
			+ ") r\r\n"
			+ "LEFT JOIN receiptinvdetails rd ON r.receiptid = rd.receiptid\r\n"
			+ "LEFT JOIN arapadjustments adj ON adj.docid = r.docid \r\n"
			+ "WHERE r.orgid = ?1\r\n"
			+ "AND (r.finyear = ?4 OR 'ALL' = ?4) \r\n"
			+ "ORDER BY r.orgid, r.branchcode, r.finyear, \r\n"

			+ "    r.createdon, \r\n"
			+ "    r.createdby, \r\n"
			+ "    r.docdate, \r\n"
			+ "    r.docid, \r\n"
			+ "    r.chequebank, \r\n"
			+ "    r.chequeutino, \r\n"

			+ "    r.customername\r\n"
			+ "")

	Set<Object[]> getReceiptRegisterReport(Long orgId, String partyName, String branchCode, String finYear,
			String fromDate, String toDate);

	
	@Query(nativeQuery = true, value = "SELECT \r\n"
			+ "    a.createdby,\r\n"
			+ "    a.createdon,\r\n"
			+ "    a.docid,\r\n"
			+ "    a.docdate,\r\n"
			+ "    a.subtypecode,\r\n"
			+ "    a.subtypename,\r\n"
			+ "    a.chequebank,\r\n"
			+ "    a.chequeno,\r\n"
			+ "    a.subledgercode,\r\n"
			+ "    a.subledgername,\r\n"
			+ "    m.accountname,\r\n"
			+ "    a.receiptamt AS receiptamount,\r\n"
			+ "    a.bankcharges AS bankchargesamt,\r\n"
			+ "    CASE WHEN rnk = 1 THEN a.tdsamt ELSE 0 END AS tdsamt,\r\n"
			+ "    a.servicetaxamt AS staxamount,\r\n"
			+ "    a.invno AS invoiceno,\r\n"
			+ "    a.invdate AS invoicedate,\r\n"
			+ "    a.refno,\r\n"
			+ "    a.refdate,\r\n"
			+ "    a.amount AS arapamount,\r\n"
			+ "    a.outstanding AS arapoutstanding,\r\n"
			+ "    a.settled AS arapsettled\r\n"
			+ "FROM  \r\n"
			+ "    (SELECT \r\n"
			+ "        p.createdby,\r\n"
			+ "        p.createdon,\r\n"
			+ "        p.docid,\r\n"
			+ "        p.docdate,\r\n"
			+ "        p.paymenttype AS subtypecode,\r\n"
			+ "        p.type AS subtypename,\r\n"
			+ "        p.chequebank,\r\n"
			+ "        p.chequeno,\r\n"
			+ "        p.partycode AS subledgercode,\r\n"
			+ "        p.partyname AS subledgername,\r\n"
			+ "        p.receiptamt,\r\n"
			+ "        p.bankcharges,\r\n"
			+ "        p.tdsamt,\r\n"
			+ "        p.servicetaxamt,\r\n"
			+ "        pi.invno,\r\n"
			+ "        pi.invdate,\r\n"
			+ "        pi.refno,\r\n"
			+ "        pi.refdate,\r\n"
			+ "        pi.amount,\r\n"
			+ "        pi.outstanding,\r\n"
			+ "        pi.settled,\r\n"
			+ "        ROW_NUMBER() OVER (PARTITION BY p.docid ORDER BY p.docid) AS rnk\r\n"
			+ "    FROM payment p\r\n"
			+ "    LEFT JOIN paymentinvdtls pi ON p.paymentid = pi.paymentid\r\n"
			+ "    LEFT JOIN account m ON m.accountid = p.bankcashacc\r\n"
			+ "    LEFT JOIN branch br ON p.branch = br.branch\r\n"
			+ "    WHERE p.cancel = 0\r\n"
			+ "      AND (p.partycode=?2 OR ?2 = 'ALL')\r\n"
			+ "      AND (p.docdate BETWEEN date(?5) AND date(?6) OR (?5 is null AND ?6 is null))\r\n"
			+ "      AND (br.branchcode =?3 OR ?3 = 'ALL')\r\n"
			+ "      AND  p.orgid =?1\r\n"
			+ "     AND (p.finyear =?4 OR ?4 = 'ALL')\r\n"
			+ "    ) AS a \r\n"
			+ "LEFT JOIN account m ON a.subledgercode = m.accountcode\r\n"
			+ "ORDER BY \r\n"
			+ "    a.createdon, \r\n"
			+ "    a.createdby, \r\n"
			+ "    a.docdate, \r\n"
			+ "    a.docid, \r\n"
			+ "    rnk, \r\n"
			+ "    a.subtypename, \r\n"
			+ "    a.chequebank, \r\n"
			+ "    a.chequeno, \r\n"
			+ "    a.subledgername")
	Set<Object[]> getPaymentRegisterReport(Long orgId, String partyCode, String branchCode, String finYear,
	                                        String fromDate, String toDate);

	@Query(nativeQuery =true,value ="SELECT SUM(r.receiptamt) AS receiptAmnt\r\n"
			+ "FROM receipt r\r\n"
			+ "WHERE r.orgid = ?1 and r.finyear=?3 and ((month(docdate)=month(current_date()) and ?2='Month')or ?2 is null )")
	Set<Object[]> getReceiptAmont(Long orgId, String month, String year);
	
	@Query(nativeQuery = true, value = "select * from receipt r,receiptinvdetails r1 where r.orgid=?1  and r.cancel=0 and r.receiptid=r1.receiptid  and r1.issettled=false")
	List<ReceiptVO> getAllReceiptByCode(Long orgId);


}
