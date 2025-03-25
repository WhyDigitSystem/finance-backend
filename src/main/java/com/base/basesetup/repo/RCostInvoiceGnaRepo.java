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
	List<RCostInvoiceGnaVO> getAllRCostInvoiceGnaById(Long id);

	@Query(nativeQuery = true, value = "select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
	String getRCostInvoiceGnaDocId(Long orgId, String finYear, String branchCode, String screenCode);

//	@Query(nativeQuery = true, value = "SELECT * FROM finance.partymaster WHERE partytype = ?1 and gstregistered='YES'  AND active = 1")
//	List<PartyMasterVO> getPartyDetailsForRCostInvoice(String partyType);

	@Query(value = "select a from PartyMasterVO a where a.orgId=?1 and a.partyType=?2 and a.active=true and a.gstRegistered='YES'")
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

//	@Query(nativeQuery = true,value=" SELECT b.statecode, b.state, a.gstin, c.city,\r\n"
//			+ " CONCAT(c.addressline1, ', ', c.addressline2, ', ', c.addressline3) AS address  ,ROW_NUMBER() OVER (ORDER BY currency) AS id \r\n"
//			+ " FROM partymaster a JOIN partystate b ON a.partymasterid = b.partymasterid JOIN \r\n"
//			+ " partyaddress c ON a.partymasterid = c.partymasterid AND b.state = c.state\r\n"
//			+ " WHERE a.orgid = ?1 AND a.partytype ='VENDOR' and partycode=?2 AND a.active = 1 ORDER BY b.statecode, \r\n"
//			+ " b.state, a.gstin, c.city, address")
//	Set<Object[]> getStatedetailsFromPartyMaster(Long orgId, String partyCode);

	@Query(nativeQuery = true,value=" SELECT b.statecode  , b.state, a.gstin ,ROW_NUMBER() OVER (ORDER BY currency) AS id \r\n"
			+ "			FROM partymaster a JOIN partystate b ON a.partymasterid = b.partymasterid "
			+ "			 WHERE a.orgid = ?1 AND a.partytype ='VENDOR' and partycode=?2  "
			+ "			 AND a.active = 1 ORDER BY b.statecode, \r\n"
			+ "		b.state, a.gstin")
	Set<Object[]> getStatedetailsFromPartyMaster(Long orgId, String partyCode);
	
	@Query(nativeQuery = true,value="SELECT  a1.businessplace,CONCAT(a1.addressline1, ',', a1.addressline2, ',', a1.addressline3) AS address FROM partymaster a,partyaddress a1, partystate a2\r\n"
			+ "where a.partymasterid=a1.partymasterid and a.partymasterid=a2.partymasterid  and a1.state = a2.state and  a.orgid =?1 AND a.partycode =?2\r\n"
			+ " and a1.state=?3 and a1.addresstype=?4 AND  a.active = 1 ORDER BY  a1.businessplace, address")
	Set<Object[]> getCitydetailsFromPartyMaster(Long orgId, String partyCode,String state,String addressType);
	
	@Query(nativeQuery = true, value = "select a.addresstype from partyaddress a , partystate a1,partymaster a2 where  a2.orgid=?1 and  a.state=?2 and a.state=a1.state and a.partymasterid=a2.partymasterid and \r\n"
			+ "a2.partymasterid=a1.partymasterid and a2.partycode=?3")
	Set<Object[]> findByAddressTypeFromPartyAddress(Long orgId, String state, String partyCode);

	@Query(nativeQuery = true, value = "select accountgroupname,currency,gstpercentage from groupledger where orgid=?1 and gsttaxflag!='NA' and category='TAX' and gsttaxflag='INPUT TAX' and gsttype=?2 and gstpercentage=?3  order by gstpercentage desc")
	Set<Object[]> findInterDetailsForrCostInvoiceGnaPosting(Long orgId, String gstType, Double gstPercent);

	@Query(nativeQuery = true, value = "select accountgroupname,currency,gstpercentage from groupledger where orgid=?1 and gsttaxflag!='NA' and category='TAX' and gsttaxflag='INPUT TAX' and gsttype=?2 and gstpercentage=?3  order by gstpercentage desc")
	Set<Object[]> findIntraDetailsForrCostInvoiceGnaPosting(Long orgId, String gstType, Double gstPercent);

	RCostInvoiceGnaVO findByOrgIdAndIdAndDocId(Long orgId, Long id, String docId);

	
	@Query(nativeQuery = true, value = "SELECT \r\n"
			+ "    branchcode,\r\n"
			+ "    evid,\r\n"
			+ "    evdate,\r\n"
			+ "    suppliername,\r\n"
			+ "    suppliergstin,\r\n"
			+ "    gsttype,\r\n"
			+ "    SUM(charges) AS charges,\r\n"
			+ "    SUM(OIGST) AS Output_IGST,\r\n"
			+ "    SUM(OCGST) AS Output_CGST,\r\n"
			+ "    SUM(OSGST) AS Output_SGST,\r\n"
			+ "    MAX(gstpercent) AS gstpercent\r\n"
			+ "FROM (\r\n"
			+ "    SELECT \r\n"
			+ "        branchcode,\r\n"
			+ "        evid,\r\n"
			+ "        evdate,\r\n"
			+ "        suppliername,\r\n"
			+ "        suppliergstin,\r\n"
			+ "        gsttype,\r\n"
			+ "        charges,\r\n"
			+ "        0 AS OIGST,\r\n"
			+ "        0 AS OCGST,\r\n"
			+ "        0 AS OSGST,\r\n"
			+ "        gstpercent\r\n"
			+ "    FROM (\r\n"
			+ "        SELECT \r\n"
			+ "            a.branchcode,\r\n"
			+ "            c.docid,\r\n"
			+ "            c.docdate,\r\n"
			+ "            a.vid AS evid,\r\n"
			+ "            a.vdate AS evdate,\r\n"
			+ "            a.suppliername,\r\n"
			+ "            a.suppliergstin,\r\n"
			+ "            a.gsttype,\r\n"
			+ "            a.netbilllcamt AS charges,\r\n"
			+ "            gstpercent AS gstpercent\r\n"
			+ "        FROM \r\n"
			+ "            costinvoice a\r\n"
			+ "            JOIN chargercostinvoice b ON a.costinvoiceid = b.costinvoiceid\r\n"
			+ "            JOIN accounts c ON a.docid = c.refno AND a.vid = c.vid\r\n"
			+ "        WHERE \r\n"
			+ "            a.cancel = 'F'\r\n"
			+ "            AND a.orgid = ?1\r\n"
			+ "            AND (a.branchcode = ?2 OR ?2 = 'ALL')\r\n"
			+ "            AND (c.vdate BETWEEN date(?3) AND date(?4) OR (?3 IS NULL AND ?4 IS NULL))\r\n"
			+ "            AND (a.suppliercode = ?5 OR ?5 = 'ALL')\r\n"
			+ "        GROUP BY \r\n"
			+ "            a.branchcode,\r\n"
			+ "            c.docid,\r\n"
			+ "            c.docdate,\r\n"
			+ "            a.vid,\r\n"
			+ "            a.vdate,\r\n"
			+ "            a.suppliername,\r\n"
			+ "            a.suppliergstin,\r\n"
			+ "            a.gsttype,\r\n"
			+ "            a.netbilllcamt,\r\n"
			+ "            gstpercent\r\n"
			+ "    ) AS a\r\n"
			+ ") AS b\r\n"
			+ "GROUP BY \r\n"
			+ "    branchcode, \r\n"
			+ "    evid, \r\n"
			+ "    evdate,  \r\n"
			+ "    suppliername, \r\n"
			+ "    suppliergstin, \r\n"
			+ "    gsttype\r\n"
			+ "ORDER BY \r\n"
			+ "    evid, evdate")
	Set<Object[]> findRegisterCostInvoiceReport(Long orgId, String branchCode, String fromDate,
			String toDate,String partyCode);
}
	