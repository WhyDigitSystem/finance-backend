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

	@Query(value = "select * from payment  where orgid=?1 ", nativeQuery = true)
	List<PaymentVO> getAllPaymentByOrgId(Long orgId);

	@Query(nativeQuery = true, value = "select a.docid,a.docdate,a.partyname,a.bankcashacc,a.receiptamt,a.bankcharges,a.tdsamt,a.chequebank,a.chequeno,b.invno,b.invdate,b.refno,b.refdate,b.amount,b.outstanding,b.settled,a.createdon,a.createdby from payment a, paymentinvdtls b where a.paymentid=b.paymentid and a.orgid=?1 and a.docdate BETWEEN ?2 AND ?3 and a.partyname =?4")
	Set<Object[]> findAllPaymentRegister(Long orgId, String fromDate, String toDate, String subLedgerName);

	@Query(nativeQuery = true, value = "select partyname,partycode from partymaster where orgid=?1 and active=1 and partytype='VENDOR'")
	Set<Object[]> findPartyNameAndCodeForPayment(Long orgId);

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

	@Query(nativeQuery = true, value = "select * from payment where orgid=?1 and branchcode=?2 ")
	List<PaymentVO> getAllVendorPaymentByOrgIdAndBranchCode(Long orgId, String branchCode);

}
