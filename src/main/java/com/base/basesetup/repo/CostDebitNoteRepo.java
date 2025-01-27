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

	@Query(value = "SELECT * FROM costdebitnote where orgid=?1", nativeQuery = true)
	List<CostDebitNoteVO> getByCostDebitNoteByOrgId(Long orgId);

	@Query(value = "SELECT * FROM costdebitnote where costdebitnoteid=?1", nativeQuery = true)
	List<CostDebitNoteVO> getByCostDebitNoteById(Long id);

	@Query(value = "SELECT * FROM costdebitnote where active=1", nativeQuery = true)
	List<CostDebitNoteVO> getActiveCostDebitNote();

	@Query(nativeQuery = true, value = "select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
	String getCostDebitNoteDocId(Long orgId, String finYear, String branchCode, String screenCode);

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

	@Query(value = "select a from CostDebitNoteVO a where a.orgId=?1 and a.supplierName=?2 and a.branchCode=?3")
	List<CostDebitNoteVO> findOrginBillNoByParty(Long orgId, String party, String branchCode);

	@Query(nativeQuery = true, value = "SELECT a.partytype\r\n" + "FROM partymaster a\r\n" + "WHERE a.orgId = ?1\r\n"
			+ "AND a.branch = ?2\r\n" + "AND a.finyear =?3 and partytype='VENDOR'GROUP BY a.partytype")
	Set<Object[]> partyTypeForCostDebitNote(Long orgId, String branch, String finYear);

	@Query(nativeQuery = true, value = "SELECT chargedescription,chargecode,govtsac,taxable,serviceaccountcode,gsttax FROM chargetyperequest WHERE chargedescription LIKE '%INTER%' AND chargecode LIKE '%gst%' AND orgid=?1 AND gsttax IN (?2) GROUP BY chargedescription,chargecode,govtsac,taxable,serviceaccountcode,gsttax")
	Set<Object[]> findChargeNameAndChargeCodeForIgstPosting(Long orgId, String gstPercent);

	@Query(nativeQuery = true, value = "SELECT chargedescription,chargecode,govtsac,taxable,serviceaccountcode,gsttax FROM chargetyperequest WHERE chargedescription LIKE '%INTRA%' AND chargecode LIKE '%gst%' AND orgid=?1 AND gsttax IN (?2) GROUP BY chargedescription,chargecode,govtsac,taxable,serviceaccountcode,gsttax")
	Set<Object[]> findChargeNameAndChargeCodeForCgstAndSgtsPosting(Long orgId, BigDecimal gstPercent);

	CostDebitNoteVO findByOrgIdAndIdAndDocId(Long orgId, Long id, String docId);

	@Query(nativeQuery = true, value = "select accountgroupname,gstpercentage,currency from groupledger where orgid=?1 and gsttaxflag!='NA' and category='TAX' and gsttaxflag='INPUT TAX' and gsttype=?2 and gstpercentage IN(?3) order by gstpercentage desc")
	Set<Object[]> findInterAndIntraDetailsForCostInvoicePosting(Long orgId, String gstType, String gstPercent);

	@Query(nativeQuery = true, value = "select accountgroupname,gstpercentage,currency from groupledger where orgid=?1 and gsttaxflag!='NA' and category='TAX' and gsttaxflag='INPUT TAX' and gsttype=?2 and gstpercentage IN(?3) order by gstpercentage desc")
	Set<Object[]> findInterAndIntraDetailsForCostInvoice(Long orgId, String gstType, List<String> gstPercent);

}
