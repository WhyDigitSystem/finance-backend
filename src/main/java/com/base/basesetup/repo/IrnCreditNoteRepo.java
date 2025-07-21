package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.base.basesetup.entity.IrnCreditNoteVO;

public interface IrnCreditNoteRepo extends JpaRepository<IrnCreditNoteVO, Long> {

	@Query(nativeQuery = true, value = "select * from irncreditnote where orgid=?1 and finyear=?2 and branchcode=?3")
	List<IrnCreditNoteVO> getAllIrnCreditByOrgId(Long orgId, String finYear, String branchCode);

	@Query(nativeQuery = true, value = "select * from irncreditnote where irncreditnoteid=?1")
	List<IrnCreditNoteVO> getAllIrnCreditById(Long id);

	@Query(nativeQuery = true, value = "select * from irncreditnote where active=1")
	List<IrnCreditNoteVO> findIrnCreditByActive();

	@Query(nativeQuery = true, value = "select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
	String getIrnCreditDocId(Long orgId, String finYear, String branchCode, String screenCode);

	@Query(nativeQuery = true, value = "SELECT partyname,partycode,partytype from partymaster where orgid=?1 and active=1 ")
	Set<Object[]> findPartyNameAndPartyCodeAndPartyTypeForIrn(Long orgId);

	IrnCreditNoteVO findByOrgIdAndIdAndDocId(Long orgId, Long id, String docId);

	@Query(nativeQuery = true, value = "SELECT \r\n" + "    SUM(\r\n" + "        CASE \r\n"
			+ "            WHEN type = 'TaxInvoice' THEN amount \r\n" + "            ELSE -amount \r\n"
			+ "        END\r\n" + "    ) AS amount\r\n" + "FROM (\r\n" + "    SELECT \r\n"
			+ "        'TaxInvoice' AS type, \r\n" + "        SUM(a.totalcreditamount) AS amount \r\n"
			+ "    FROM accounts a\r\n" + "    JOIN taxinvoice t ON a.refno = t.docid \r\n"
			+ "    WHERE t.docid =?1 AND t.cancel = 'F'\r\n" + "\r\n" + "    UNION ALL\r\n" + "\r\n" + "    SELECT \r\n"
			+ "        'CreditNote' AS type, \r\n" + "        SUM(a.totalcreditamount) AS amount \r\n"
			+ "    FROM accounts a\r\n" + "    JOIN irncreditnote i ON a.refno = i.docid \r\n"
			+ "    WHERE i.originbillno =?1 AND i.cancel = 'F'\r\n" + "\r\n" + "    UNION ALL\r\n" + "\r\n"
			+ "    SELECT \r\n" + "        'Receipt' AS type, \r\n" + "        SUM(a.totalcreditamount) AS amount \r\n"
			+ "    FROM accounts a\r\n" + "    JOIN receipt r ON a.refno = r.docid \r\n"
			+ "    JOIN receiptinvdetails r1 ON r.receiptid = r1.receiptid \r\n"
			+ "    WHERE r.cancel = 'F' AND r1.refno =?1\r\n" + "    \r\n" + "      UNION ALL\r\n" + "\r\n"
			+ "    SELECT \r\n" + "        'ArAdjusments' AS type, \r\n"
			+ "        SUM(a.totalcreditamount) AS amount \r\n" + "    FROM accounts a\r\n"
			+ "    JOIN aradjustmentoffset r ON a.refno = r.docid \r\n"
			+ "    JOIN aroffsetinvoicedetails r1 ON r.aradjustmentoffsetid = r1.aradjustmentoffsetid \r\n"
			+ "    WHERE r.cancel = 'F' AND r1.refno =?1\r\n" + ") AS sub")
	Set<Object[]> getByAmount(String docId);

	@Query(nativeQuery = true, value = "select * from irncreditnote where screencode=?1 and docid=?2")
	IrnCreditNoteVO getCreditNoteByDocIdandScreenCode(String screenCode, String docId);

	@Query(nativeQuery = true, value = "SELECT \r\n" + "    income_data.partyname,\r\n" + "    income_data.docid,\r\n"
			+ "    income_data.income,\r\n" + "    COALESCE(expense_data.expense, 0) AS expense,\r\n"
			+ "    income_data.income - COALESCE(expense_data.expense, 0) AS amount\r\n" + "FROM (\r\n"
			+ "    SELECT \r\n" + "        a.partyname, \r\n" + "        a.docid, \r\n"
			+ "        SUM(a.totalinvamountlc) AS income\r\n" + "    FROM \r\n" + "        taxinvoice a \r\n"
			+ "    WHERE \r\n" + "        a.orgid =?1 and a.docid=?2\r\n" + "    GROUP BY \r\n"
			+ "        a.partyname,   a.docid\r\n" + ") AS income_data\r\n" + "LEFT JOIN (\r\n" + "    SELECT \r\n"
			+ "        b.partyname, \r\n" + "        b.originbillno, \r\n" + "        case when\r\n"
			+ "        SUM(b.totalinvamountlc)  is null then 0 else SUM(b.totalinvamountlc)  end AS expense\r\n"
			+ "    FROM \r\n" + "        irncreditnote   b\r\n" + "    WHERE \r\n"
			+ "        b.orgid =?1 and b.originbillno=?2\r\n" + "    GROUP BY \r\n" + "         b.partyname, \r\n"
			+ "        b.originbillno\r\n" + ") AS expense_data\r\n" + "ON \r\n"
			+ "    income_data.partyname = expense_data.partyname \r\n"
			+ "    AND income_data.docid = expense_data.originbillno")
	Set<Object[]> findByOrginBillBased(Long orgId, String orginbillNo);

}
