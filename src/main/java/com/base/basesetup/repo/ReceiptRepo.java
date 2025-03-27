package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.ReceiptVO;

@Repository
public interface ReceiptRepo extends JpaRepository<ReceiptVO, Long> {

	@Query(nativeQuery = true, value = "select * from receipt where orgid=?1")
	List<ReceiptVO> getAllReceiptReceivableByOrgId(Long orgId);

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

	@Query(nativeQuery = true, value = "WITH b AS (\r\n" + "    SELECT subledgercode, docid, SUM(amount) AS settled\r\n"
			+ "    FROM arapadjustments\r\n" + "    WHERE CANCEL = 'F'\r\n" + "    GROUP BY subledgercode, docid\r\n"
			+ ")\r\n" + "SELECT \r\n" + "    a.arapdetailsid,\r\n" + "    a.branch,\r\n" + "    a.subledgercode,\r\n"
			+ "    c.vid,\r\n" + "    c.vdate,\r\n" + "    a.refno,\r\n" + "    a.refdate,\r\n" + "    a.supprefno,\r\n"
			+ "    a.suprefdate,\r\n" + "    a.acccurrency,\r\n" + "    a.exrate,\r\n" + "    a.amount,\r\n"
			+ "   IFNULL(b.settled, 0) AS arapsettled, \r\n" + "    a.chargableamt,\r\n" + "    a.tdsamt\r\n"
			+ "FROM arapdetails a\r\n" + "LEFT JOIN b \r\n" + "    ON a.subledgercode = b.subledgercode \r\n"
			+ "    AND a.docid = b.docid join accounts c\r\n" + "WHERE  a.docid=c.docid and\r\n"
			+ "    a.CANCEL = 'F' and a.subledgercode=?2 and a.orgid=?1\r\n" + "ORDER BY a.docdate, a.docid")
	Set<Object[]> findReciptFillGrid(Long orgId, String partyCode);

	@Query(nativeQuery = true, value = "select * from receipt where orgid=?1 and branchcode=?2 and cancel=0")
	List<ReceiptVO> getAllReceiptByOrgIdAndBranchCode(Long orgId, String branchCode);

}
