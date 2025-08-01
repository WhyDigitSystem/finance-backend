package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.GroupLedgerVO;
import com.base.basesetup.entity.PartyMasterVO;
import com.base.basesetup.entity.UrCostInvoiceGnaVO;

@Repository
public interface UrCostInvoiceGnaRepo extends JpaRepository<UrCostInvoiceGnaVO, Long> {

	@Query(value = "select a from UrCostInvoiceGnaVO a where a.orgId=?1 and a.finYear=?2 and a.branchCode=?3 order by a.docId desc")
	List<UrCostInvoiceGnaVO> getAllUrCostInvoiceGnaByOrgId(Long orgId, String finYear, String branchCode);

	@Query(value = "select a from UrCostInvoiceGnaVO a where a.id=?1")
	List<UrCostInvoiceGnaVO> getUrCostInvoiceGnaById(Long id);

	@Query(nativeQuery = true, value = "select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
	String getUrCostInvoiceGnaDocId(Long orgId, String finYear, String branchCode, String screenCode);

	@Query(value = "select a from PartyMasterVO a where a.orgId=?1 and a.partyType=?2 and a.active=true and a.gstRegistered='NO'")
	List<PartyMasterVO> getAllVendorFromPartyMaster(Long orgId, String partyType);

	@Query(nativeQuery = true, value = "SELECT b.statecode, b.state, a.gstin, c.city,CONCAT(c.addressline1, ', ', c.addressline2, ', ', c.addressline3) AS address FROM  \r\n"
			+ "partymaster a,partyaddress c, partystate b WHERE \r\n"
			+ "a.partymasterid = b.partymasterid  AND  a.partymasterid = c.partymasterid AND \r\n"
			+ "b.state = c.state and   a.orgid =?1 AND a.partytype = 'VENDOR' AND partycode =?2 AND a.active = 1\r\n"
			+ "ORDER BY b.statecode, b.state, a.gstin, c.city, address")
	Set<Object[]> getVendorAddressFromPartyMaster(Long orgId, String supplierCode);

	@Query(nativeQuery = true, value = "select a.currency,a.buyingexrate,a.sellingexrate from vw_exrates a, partymaster a1,partycurrencymapping b where\r\n"
			+ "           a1.orgid=?1  and a1.partymasterid=b.partymasterid and a.orgid=a1.orgid and a1.partycode=?2  order by a.currency")
	Set<Object[]> getCurrencyAndExrateFromParty(Long orgId, String supplierCode);

	@Query(nativeQuery = true, value = "select accountgroupname from groupledger where orgid=?1 and category in ('OTHERS','TAX','BANK') and active = true  order by accountgroupname")
	Set<Object[]> getChargeLedgerFromGroup(Long orgId);

	@Query(nativeQuery = true, value = "select a.sectionname ,b.tcspercentage  from tdsmaster a , tdsmaster2 b where a.tdsmasterid=b.tdsmaster2id and a.orgid=?1 and a.section=?2 and a.active = 1  order by sectionname")
	Set<Object[]> getSectionNameFromMaster(Long orgId, String section);

	@Query(nativeQuery = true, value = "select  accountgroupname,gstpercentage,currency from groupledger where orgid=?1 and gsttaxflag!='NA' and category='TAX' and gsttaxflag IN ('INPUT TAX','OUTPUT TAX') and gsttype=?2 and\r\n"
			+ " gstpercentage IN(?3) group by  accountgroupname,gstpercentage,currency order by gstpercentage desc")
	Set<Object[]> findInterDetailsForUrCostInvoiceGnaPosting(Long orgId, String gtsType, Double gstPercent1);

	@Query(nativeQuery = true, value = "select  accountgroupname,gstpercentage,currency from groupledger where orgid=?1 and gsttaxflag!='NA' and category='TAX' and gsttaxflag IN ('INPUT TAX','OUTPUT TAX') and gsttype=?2 and\r\n"
			+ " gstpercentage IN(?3) group by  accountgroupname,gstpercentage,currency order by gstpercentage desc")
	Set<Object[]> findIntraDetailsForUrCostInvoiceGnaPosting(Long orgId, String gtsType, Double gstPercent);

	@Query(nativeQuery = true, value = "SELECT * FROM chargesurcostinvoicegna WHERE LOWER(chargeLedger) LIKE '%input%' AND  urcostinvoicegnaid =?1")
	Set<Object[]> findAccountsInputPostinInvoiceGnaPosting(Long id);

	@Query(nativeQuery = true, value = "SELECT * FROM chargesurcostinvoicegna a, urcostinvoicegna a1 WHERE \r\n"
			+ "LOWER(chargeLedger) LIKE '%output%' AND a.urcostinvoicegnaid =?1 and a1.orgid=?2 and a.urcostinvoicegnaid=a1.urcostinvoicegnaid")
	Set<Object[]> findAccountsOutputPostinInvoiceGnaPosting(Long id);
	
	@Query(nativeQuery = true, value = "SELECT * FROM chargesurcostinvoicegna WHERE  urcostinvoicegnaid =?1 and chargeledger=?2")
	Set<Object[]> getUrChargeLedgerDetails(Long id,String chargeLedger);

	UrCostInvoiceGnaVO findByOrgIdAndIdAndDocId(Long orgId, Long id, String docId);
	
	@Query(nativeQuery = true, value = "select  accountgroupname,category from groupledger where orgid=?1 and gsttaxflag!='NA' and \r\n"
			+ "category='TAX' and gsttaxflag IN ('OUTPUT TAX') and gsttype=?2  and accountgroupname=?3\r\n"
			+ " group by  accountgroupname")
	Set<Object[]>  getOuputPosting(Long orgId, String gstType, String  accountName);
	
	@Query(nativeQuery = true, value = "select  accountgroupname,category from groupledger where orgid=?1 and gsttaxflag!='NA' and \r\n"
			+ "category='TAX' and gsttaxflag IN ('INPUT TAX') and gsttype=?2  and accountgroupname=?3\r\n"
			+ " group by  accountgroupname")
	Set<Object[]>  getInputPosting(Long orgId, String gstType, String  accountName);
	
	@Query(nativeQuery = true, value = "select accountgroupname,category from groupledger where accountgroupname=?1")
	Set<Object[]>  getLedgerPosting( String  accountName);

	
	@Query(nativeQuery = true, value = "select * from urcostinvoicegna where screencode=?1 and docid=?2")
	UrCostInvoiceGnaVO getUrCostInvoiceByDocIdandScreenCode(String screenCode, String docId);

	@Query(nativeQuery = true,value = "select accountgroupname from groupledger where orgid=?1 and groupname=?2  and active = true  order by accountgroupname")
	Set<Object[]> getChargeAccountFromChargeLedger(Long orgId, String chargeLedger);


}
