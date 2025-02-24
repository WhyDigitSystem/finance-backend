package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.PartyMasterVO;
import com.base.basesetup.entity.UrCostInvoiceGnaVO;

@Repository
public interface UrCostInvoiceGnaRepo extends JpaRepository<UrCostInvoiceGnaVO, Long> {

	@Query(value = "select a from UrCostInvoiceGnaVO a where a.orgId=?1 and a.finYear=?2 and a.branchCode=?3 order by a.docId desc")
	List<UrCostInvoiceGnaVO> getAllUrCostInvoiceGnaByOrgId(Long orgId, String finYear, String branchCode);

	@Query(value = "select a from UrCostInvoiceGnaVO a where a.id=?1")
	UrCostInvoiceGnaVO getUrCostInvoiceGnaById(Long id);

	@Query(nativeQuery = true, value = "select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
	String getUrCostInvoiceGnaDocId(Long orgId, String finYear, String branchCode, String screenCode);

	@Query(value = "select a from PartyMasterVO a where a.orgId=?1 and a.partyType=?2 and a.active=true")
	List<PartyMasterVO> getAllVendorFromPartyMaster(Long orgId, String partyType);

	@Query(nativeQuery = true, value = "SELECT b.statecode, b.state, a.gstin, c.city,CONCAT(c.addressline1, ', ', c.addressline2, ', ', c.addressline3) AS address FROM  partymaster a JOIN  partystate b ON a.partymasterid = b.partymasterid\r\n"
			+ "JOIN   partyaddress c ON a.partymasterid = c.partymasterid AND b.state = c.state WHERE  a.orgid =?1 AND a.partytype = 'VENDOR' AND partycode =?2 AND a.active = 1\r\n"
			+ "ORDER BY b.statecode, b.state, a.gstin, c.city, address")
	Set<Object[]> getVendorAddressFromPartyMaster(Long orgId, String supplierCode);

	@Query(nativeQuery = true, value = "select currency,buyingexrate,sellingexrate from vw_exrates where orgid=?1 order by currency")
	Set<Object[]> getCurrencyAndExrateFromParty(Long orgId);

	@Query(nativeQuery = true, value = "select accountgroupname from groupledger where orgid=?1 and category in ('OTHERS','TAX') and active = true  order by accountgroupname")
	Set<Object[]> getChargeLedgerFromGroup(Long orgId);

	@Query(nativeQuery = true, value = "select a.sectionname ,b.tcspercentage  from tdsmaster a , tdsmaster2 b where a.tdsmasterid=b.tdsmaster2id and a.orgid=?1 and a.section=?2 and a.active = 1  order by sectionname")
	Set<Object[]> getSectionNameFromMaster(Long orgId, String section);

	@Query(nativeQuery = true, value = "select  accountgroupname,gstpercentage,currency from groupledger where orgid=?1 and gsttaxflag!='NA' and category='TAX' and gsttaxflag IN ('INPUT TAX','OUTPUT TAX') and gsttype=?2 and\r\n"
			+ " gstpercentage IN(?3) group by  accountgroupname,gstpercentage,currency order by gstpercentage desc")
	Set<Object[]> findInterDetailsForUrCostInvoiceGnaPosting(Long orgId, String gtsType, Double gstPercent1);

	@Query(nativeQuery = true, value = "select  accountgroupname,gstpercentage,currency from groupledger where orgid=?1 and gsttaxflag!='NA' and category='TAX' and gsttaxflag IN ('INPUT TAX','OUTPUT TAX') and gsttype=?2 and\r\n"
			+ " gstpercentage IN(?3) group by  accountgroupname,gstpercentage,currency order by gstpercentage desc")
	Set<Object[]> findIntraDetailsForUrCostInvoiceGnaPosting(Long orgId, String gtsType, Double gstPercent);

}
