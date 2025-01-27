package com.base.basesetup.repo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.base.basesetup.entity.CostInvoiceVO;
import com.base.basesetup.entity.PartyMasterVO;

public interface CostInvoiceRepo extends JpaRepository<CostInvoiceVO, Long> {

	@Query(nativeQuery = true, value = "select * from costinvoice where orgid=?1")
	List<CostInvoiceVO> getAllCostInvoiceByOrgId(Long orgId);

	@Query(nativeQuery = true, value = "select * from costinvoice where costinvoiceid=?1")
	List<CostInvoiceVO> getAllCostInvoiceById(Long id);

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

	@Query(value = "select a from PartyMasterVO a where a.orgId=?1 and a.partyType=?2 and a.active=true")
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

	@Query(nativeQuery = true, value = "SELECT jobno FROM jobcard WHERE orgid=?1 AND closed = 0 AND active=1")
	Set<Object[]> getJobNoFromTmsJobCard(Long orgId);

	@Query(nativeQuery = true, value = "SELECT a.tdswithsec,a.tdswithper FROM partyspecialtds a, partymaster b WHERE a.partymasterid = b.partymasterid AND b.orgid=?1 AND b.partycode=?2")
	Set<Object[]> findTdsDetailsFromPartyMasterSpecialTDS(Long orgId, String partyCode);

	@Query(value = "select a from CostInvoiceVO a where a.orgId=?1 and a.supplierName=?2 and a.branchCode=?3")
	List<CostInvoiceVO> findOrginBillNoByParty(Long orgId, String party, String branchCode);

	@Query(nativeQuery = true, value = "select accountgroupname,gstpercentage,currency from groupledger where orgid=?1 and gsttaxflag!='NA' and category='TAX' and gsttaxflag='INPUT TAX' and gsttype=?2 and gstpercentage IN(?3) order by gstpercentage desc")
	Set<Object[]> findInterAndIntraDetailsForCostInvoice(Long orgId, String gtsType, List<String> gstPercent);

	@Query(nativeQuery = true, value = "select accountgroupname,gstpercentage,currency from groupledger where orgid=?1 and gsttaxflag!='NA' and category='TAX' and gsttaxflag='INPUT TAX' and gsttype=?2 and gstpercentage IN(?3) order by gstpercentage desc")
	Set<Object[]> findInterAndIntraDetailsForCostInvoicePosting(Long orgId, String gtsType, String intraPercent);

	CostInvoiceVO findByOrgIdAndIdAndDocId(Long orgId, Long id, String docId);

}
