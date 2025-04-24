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
	
	
	@Query(nativeQuery =true,value = "WITH b AS (\r\n"
			+ "    SELECT subledgercode, refno AS docid, SUM(amount) AS settled\r\n"
			+ "    FROM arapadjustments\r\n"
			+ "    WHERE CANCEL = 'F'\r\n"
			+ "    GROUP BY subledgercode, refno\r\n"
			+ "    HAVING SUM(amount) > 0\r\n"
			+ "),\r\n"
			+ "n AS (\r\n"
			+ "    SELECT orgid, refno, SUM(amount) AS settamt\r\n"
			+ "    FROM arapadjustments\r\n"
			+ "    WHERE amount > 0\r\n"
			+ "    GROUP BY orgid, refno\r\n"
			+ ")\r\n"
			+ "SELECT ROW_NUMBER() OVER () AS id, a.branch, a.subledgercode, c.vid, c.vdate, a.refno, a.refdate, a.supprefno, a.suprefdate, a.acccurrency, a.exrate, \r\n"
			+ "    CASE \r\n"
			+ "        WHEN h.totchargeslcamt IS NULL THEN d.totchargeslcamt \r\n"
			+ "        ELSE d.totchargeslcamt - h.totchargeslcamt \r\n"
			+ "    END AS totalAmount,\r\n"
			+ "   ( CASE \r\n"
			+ "        WHEN h.totchargeslcamt IS NULL THEN d.totchargeslcamt \r\n"
			+ "        ELSE d.totchargeslcamt - h.totchargeslcamt \r\n"
			+ "    end)\r\n"
			+ " - COALESCE(n.settamt, 0) AS invamount,\r\n"
			+ "-- SUM(d.totchargeslcamt - h.totchargeslcamt) AS totalAmount, \r\n"
			+ "   --  SUM(d.totchargeslcamt - h.totchargeslcamt) - COALESCE(n.settamt, 0) AS invamount, \r\n"
			+ "    a.chargableamt, a.tdsamt, t.gstpercent, \r\n"
			+ "    SUM(d.gstinputlcamt - h.gstinputlcamt) AS gstamount\r\n"
			+ "FROM arapdetails a\r\n"
			+ "LEFT JOIN b ON a.subledgercode = b.subledgercode AND a.docid = b.docid\r\n"
			+ "join partymaster p on a.subledgercode = partycode \r\n"
			+ "join partystate p2  on p.partymasterid = p2.partymasterid\r\n"
			+ " JOIN costinvoice d ON a.refno = d.docid\r\n"
			+ "JOIN chargercostinvoice t ON d.costinvoiceid = t.costinvoiceid\r\n"
			+ " JOIN accounts c ON a.docid = c.docid\r\n"
			+ "left JOIN costdebitnote h ON d.docid = h.orginbill\r\n"
			+ " LEFT JOIN n ON n.orgid = a.orgid AND n.refno = c.vid\r\n"
			+ " WHERE d.branchcode = ?3 AND a.CANCEL = 'F' AND a.subledgercode = ?2 AND a.orgid = ?1 and p2.statecode =?4\r\n"
			+ " GROUP BY a.branch, a.subledgercode, c.vid, c.vdate, a.refno, a.refdate, a.supprefno,h.totchargeslcamt,d.totchargeslcamt,\r\n"
			+ "a.suprefdate, a.acccurrency, a.exrate, b.settled, a.chargableamt, a.tdsamt, t.gstpercent, n.settamt\r\n"
			+ " HAVING sum( CASE \r\n"
			+ "        WHEN h.totchargeslcamt IS NULL THEN d.totchargeslcamt \r\n"
			+ "        ELSE d.totchargeslcamt - h.totchargeslcamt \r\n"
			+ "    END) - COALESCE(n.settamt, 0) > 0\r\n"
			+ "ORDER BY a.refdate, c.vid")
	Set<Object[]> getPaymentFillGrid(Long orgId, String partyCode,String branchCode,String stateCode);


}
