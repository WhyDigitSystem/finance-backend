package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.PartyMasterVO;
import com.base.basesetup.entity.RCostInvoiceGnaVO;

@Repository
public interface RCostInvoiceGnaRepo extends JpaRepository<RCostInvoiceGnaVO, Long> {

	@Query(nativeQuery = true, value = "select * from rcostinvoicegna where orgid=?1 and active=1 ")
	List<RCostInvoiceGnaVO> getAllCostInvoiceByOrgId(Long orgId);

	@Query(nativeQuery = true, value = "select * from rcostinvoicegna where rcostinvoicegnaid=?1 and active=1 ")
	RCostInvoiceGnaVO getAllRCostInvoiceGnaById(Long id);

	@Query(nativeQuery = true, value = "select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
	String getRCostInvoiceGnaDocId(Long orgId, String finYear, String branchCode, String screenCode);

	@Query(nativeQuery = true, value = "SELECT * FROM partymaster WHERE partytype = ?1 AND active = 1")
	List<PartyMasterVO> getPartyDetailsForRCostInvoice(String partyType);

	@Query(value = "select a from PartyMasterVO a where a.orgId=?1 and a.partyType=?2 and a.active=true")
	List<PartyMasterVO> getAllVendorFromPartyMaster(Long orgId, String partyType);
	
	@Query(nativeQuery = true,value = "select accountgroupname from groupledger where orgid=?1 and category in ('OTHERS','TAX') and active = 1  order by accountgroupname")
	Set<Object[]> getChargeLedgerFromGroup(Long orgId);

	@Query(nativeQuery = true,value = "select a.sectionname ,b.tcspercentage  from tdsmaster a , tdsmaster2 b where a.tdsmasterid=b.tdsmaster2id and a.orgid=?1 and a.section=?2 and a.active = 1  order by sectionname")
	Set<Object[]> getSectionNameFromTDSMaster(Long orgId, String section);

	@Query(nativeQuery = true,value = "SELECT \r\n"
			+ "       CASE \r\n"
			+ "           WHEN statecode = ?3 THEN 'INTRA'\r\n"
			+ "           ELSE 'INTER'\r\n"
			+ "       END AS transactionType\r\n"
			+ "FROM branch\r\n"
			+ "WHERE orgid = ?1 \r\n"
			+ "  AND branchcode = ?2")
	Set<Object[]> getGstType(Long orgId, String branchCode,String stateCode);

	@Query(nativeQuery = true,value="select currency,currencydescripition,buyingexrate,sellingexrate,ROW_NUMBER() OVER (ORDER BY currency) AS id from vw_exrates where orgid=?1 ")
	Set<Object[]> getCurrencyAndExrateDetails(Long orgId);

	@Query(nativeQuery = true,value=" SELECT b.statecode, b.state, a.gstin, c.city,\r\n"
			+ " CONCAT(c.addressline1, ', ', c.addressline2, ', ', c.addressline3) AS address  ,ROW_NUMBER() OVER (ORDER BY currency) AS id \r\n"
			+ " FROM partymaster a JOIN partystate b ON a.partymasterid = b.partymasterid JOIN \r\n"
			+ " partyaddress c ON a.partymasterid = c.partymasterid AND b.state = c.state\r\n"
			+ " WHERE a.orgid = ?1 AND a.partytype ='VENDOR' and partycode=?2 AND a.active = 1 ORDER BY b.statecode, \r\n"
			+ " b.state, a.gstin, c.city, address")
	Set<Object[]> getStatedetailsFromPartyMaster(Long orgId, String partyCode);

}
	