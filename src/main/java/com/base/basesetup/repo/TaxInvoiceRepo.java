package com.base.basesetup.repo;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.base.basesetup.entity.TaxInvoiceVO;

public interface TaxInvoiceRepo extends JpaRepository<TaxInvoiceVO, Long> {

	boolean existsByDocIdAndOrgId(String docId, Long orgId);

	boolean existsByInvoiceNoAndOrgId(String invoiceNo, Long orgId);

	boolean existsByDocIdAndOrgIdAndId(String docId, Long orgId, Long id);

	boolean existsByInvoiceNoAndOrgIdAndId(String invoiceNo, Long orgId, Long id);

	@Query(value = "select a from TaxInvoiceVO a where a.orgId=?1 and a.finYear=?2 and a.branchCode=?3 order by a.docId desc")
	List<TaxInvoiceVO> getAllTaxInvoiceByOrgId(Long orgId, String finYear, String branchCode);																																										

	@Query(nativeQuery = true, value = "select * from taxinvoice where orgid=?1 and docid=?2")
	TaxInvoiceVO findAllTaxInvoiceByDocId(Long orgId, String docId);

	@Query(value = "select a from TaxInvoiceVO a where a.id=?1")
	TaxInvoiceVO getTaxInvoiceById(Long id);

	@Query(nativeQuery = true,value="select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
	String getTaxInvoiceDocId(Long orgId, String finYear, String branchCode, String screenCode);

	@Query(nativeQuery = true,value="select currency,currencydescripition,buyingexrate,sellingexrate,ROW_NUMBER() OVER (ORDER BY currency) AS id from vw_exrates where orgid=?1")
	Set<Object[]> getCurrencyAndExrateDetails(Long orgId);

	@Query(nativeQuery = true,value = "select a.statecode,a.gstin,concat(a.stateno,' - ',a.state)stateno from partystate a,partymaster b where a.partymasterid=b.partymasterid and b.orgid=?1 and b.partymasterid=?2\r\n"
			+ "group by a.statecode,a.gstin,concat(a.stateno,' - ',a.state)")
	Set<Object[]> getStateCodeDetails(Long orgId, Long id);

	@Query(nativeQuery = true,value = "SELECT a.businessplace FROM partyaddress a,partymaster b,state c where  a.partymasterid=b.partymasterid and a.state=c.state and b.orgid=?1 and a.partymasterid=?2 and c.statecode=?3\r\n"
			+ "group by businessplace")
	Set<Object[]> getPlaceOfSupplyDetails(Long orgId, Long id,String stateCode);

	@Query(nativeQuery = true,value = "SELECT a.addresstype,concat(a.addressline1,',',a.addressline2,',',a.addressline3) address,a.pincode FROM partyaddress a,partymaster b,state c where  a.partymasterid=b.partymasterid and a.state=c.state and b.orgid=?1 and a.partymasterid=?2 and c.statecode=?3 and a.businessplace=?4\r\n"
			+ "group by a.addresstype,concat(a.addressline1,',',a.addressline2,',',a.addressline3),a.pincode")
	Set<Object[]> getAddressDetails(Long orgId, Long id,String stateCode,String placeOfSupply);
	
	@Query(nativeQuery = true,value = "SELECT \r\n"
			+ "       CASE \r\n"
			+ "           WHEN statecode = ?3 THEN 'INTRA'\r\n"
			+ "           ELSE 'INTER'\r\n"
			+ "       END AS transactionType\r\n"
			+ "FROM branch\r\n"
			+ "WHERE orgid = ?1 \r\n"
			+ "  AND branchcode = ?2")
	Set<Object[]> getGstType(Long orgId, String branchCode,String stateCode);

	@Query(value = "select a from TaxInvoiceVO a where a.orgId=?1 and a.partyName=?2 and a.branchCode=?3 and a.approveStatus='Approved' order by a.docId desc")
	List<TaxInvoiceVO> findPartyInvoiceDetails(Long orgId, String party, String branchCode);

	TaxInvoiceVO findByOrgIdAndIdAndDocId(Long orgId, Long id, String docId);

	@Query(nativeQuery = true,value = "select a.creditdays from partymaster a where a.orgid=?1 and partycode=?2 and a.active=1 ")
	Set<Object[]> findCreditDaysFromCustomer(Long orgId, String customerCode);

	@Query(nativeQuery = true,value = "select a.jobno from jobcard a where a.orgid=?1 and a.closed=0 and a.active=1 ")
	Set<Object[]> getAllJobNoByActice(Long orgId);

	@Query(nativeQuery = true,value = "select * from taxinvoice a where a.orgid=?1 and docid=?2 ")
	TaxInvoiceVO findByOrgIdAndDocId(Long orgId, String originBillNo);

	@Query(nativeQuery = true,value = "select a.jobno from jobcard a where a.orgid=?1 and a.customercode=?2 and active=1 ")
	Set<Object[]> getJobCardForTaxInvoice(Long orgId, String partyCode);

	boolean existsByvIdAndOrgId(String vId, Long orgId);

	@Query(nativeQuery = true,value = "SELECT \r\n"
			+ "    a.orgid, \r\n"
			+ "    a.branchcode, \r\n"
			+ "    a.docid, \r\n"
			+ "    a.docdate, \r\n"
			+ "    a.joborderno, \r\n"
			+ "    c.docid AS voucherno, \r\n"
			+ "    c.docdate AS voucherdate, \r\n"
			+ "    e.partyname AS billtoparty, \r\n"
			+ "    e.controllingoff, \r\n"
			+ "    a.billcurr, \r\n"
			+ "    a.billcurrrate, \r\n"
			+ "    a.totalinvamountbc, \r\n"
			+ "    a.totalinvamountlc, \r\n"
			+ "    a.totaltaxableamountlc, \r\n"
			+ "    a.gsttype, \r\n"
			+ "    a.totaltaxamountlc, \r\n"
			+ "    a.totaltaxamountbc, \r\n"
			+ "    a.roundoffamountlc, \r\n"
			+ "    SUM(b.fcamount) AS fcamt, \r\n"
			+ "    SUM(b.lcamount) AS lcamt, \r\n"
			+ "    SUM(b.rate) AS rate, \r\n"
			+ "    SUM(b.billamount) AS billamount, \r\n"
			+ "    a.partytype\r\n"
			+ "FROM \r\n"
			+ "    taxinvoice a\r\n"
			+ "JOIN \r\n"
			+ "    taxinvoicedetails b ON a.taxinvoiceid = b.taxinvoiceid\r\n"
			+ "JOIN \r\n"
			+ "    accounts c ON a.docid = c.refno\r\n"
			+ "JOIN \r\n"
			+ "    partymaster e ON a.partycode = e.partycode\r\n"
			+ "WHERE \r\n"
			+ "    c.finyear = ?1\r\n"
			+ "    AND (\r\n"
			+ "        (?2 IS NULL OR ?2 = '' OR ?3 IS NULL OR ?3 = '')\r\n"
			+ "        OR c. vdate BETWEEN STR_TO_DATE(?2, '%Y-%m-%d') AND STR_TO_DATE(?3, '%Y-%m-%d')\r\n"
			+ "    )\r\n"
			+ "    AND a.orgid = ?4\r\n"
			+ "    AND (a.branchcode = ?5 OR ?5 = 'ALL')\r\n"
			+ "    AND (e.partycode = ?6 OR ?6 = 'ALL')\r\n"
			+ "GROUP BY \r\n"
			+ "    a.orgid, \r\n"
			+ "    a.branchcode, \r\n"
			+ "    a.docid, \r\n"
			+ "    a.docdate, \r\n"
			+ "    a.joborderno, \r\n"
			+ "    c.docid, \r\n"
			+ "    c.docdate, \r\n"
			+ "    e.partyname, \r\n"
			+ "    e.controllingoff, \r\n"
			+ "    a.billcurr, \r\n"
			+ "    a.billcurrrate, \r\n"
			+ "    a.totalinvamountbc, \r\n"
			+ "    a.totalinvamountlc, \r\n"
			+ "    a.totaltaxableamountlc, \r\n"
			+ "    a.gsttype, \r\n"
			+ "    a.totaltaxamountlc, \r\n"
			+ "    a.totaltaxamountbc, \r\n"
			+ "    a.roundoffamountlc, \r\n"
			+ "    a.partytype\r\n"
			+ "\r\n"
			+ "UNION\r\n"
			+ "\r\n"
			+ "SELECT \r\n"
			+ "    a.orgid, \r\n"
			+ "    a.branchcode, \r\n"
			+ "    a.docid, \r\n"
			+ "    a.docdate, \r\n"
			+ "    a.jobno AS joborderno, \r\n"
			+ "    c.docid AS voucherno, \r\n"
			+ "    c.docdate AS voucherdate, \r\n"
			+ "    e.partyname AS billtoparty, \r\n"
			+ "    e.controllingoff, \r\n"
			+ "    a.billcurr, \r\n"
			+ "    a.billcurrrate, \r\n"
			+ "    a.totalinvamountbc, \r\n"
			+ "    a.totalinvamountlc, \r\n"
			+ "    a.totaltaxableamountlc, \r\n"
			+ "    a.gsttype, \r\n"
			+ "    a.totaltaxamountlc, \r\n"
			+ "    0 AS totaltaxamountbc, \r\n"
			+ "    a.roundoffamountlc, \r\n"
			+ "    SUM(b.fcamount) AS fcamt, \r\n"
			+ "    SUM(b.lcamount) AS lcamt, \r\n"
			+ "    SUM(b.rate) AS rate, \r\n"
			+ "    SUM(b.billamount) AS billamount, \r\n"
			+ "    a.partytype\r\n"
			+ "FROM \r\n"
			+ "    irncreditnote a\r\n"
			+ "JOIN \r\n"
			+ "    irncreditnotedetails b ON a.irncreditnoteid = b.irncreditnoteid\r\n"
			+ "JOIN \r\n"
			+ "    accounts c ON a.docid = c.refno\r\n"
			+ "JOIN \r\n"
			+ "    partymaster e ON a.partycode = e.partycode\r\n"
			+ "WHERE \r\n"
			+ "    c.finyear = ?1\r\n"
			+ "    AND (\r\n"
			+ "        (?2 IS NULL OR ?2 = '' OR ?3 IS NULL OR ?3 = '')\r\n"
			+ "        OR c.vdate BETWEEN STR_TO_DATE(?2, '%Y-%m-%d') AND STR_TO_DATE(?3, '%Y-%m-%d')\r\n"
			+ "    )\r\n"
			+ "    AND a.orgid = ?4\r\n"
			+ "    AND (a.branchcode = ?5 OR ?5 = 'ALL')\r\n"
			+ "    AND (e.partycode = ?6 OR ?6 = 'ALL')\r\n"
			+ "GROUP BY \r\n"
			+ "    a.orgid, \r\n"
			+ "    a.branchcode, \r\n"
			+ "    a.docid, \r\n"
			+ "    a.docdate, \r\n"
			+ "    a.jobno, \r\n"
			+ "    e.partyname, \r\n"
			+ "    e.controllingoff, \r\n"
			+ "    a.billcurr, \r\n"
			+ "    a.billcurrrate, \r\n"
			+ "    a.totalinvamountbc, \r\n"
			+ "    a.totalinvamountlc, \r\n"
			+ "    a.totaltaxableamountlc, \r\n"
			+ "    a.gsttype, \r\n"
			+ "    a.totaltaxamountlc, \r\n"
			+ "    a.roundoffamountlc, \r\n"
			+ "    c.docid, \r\n"
			+ "    c.docdate, \r\n"
			+ "    a.partytype\r\n"
			+ "\r\n"
			+ "ORDER BY \r\n"
			+ "    docdate, docid")
	Set<Object[]> getReportDetailsForSalesRegister(String finyear, String fromDate, String toDate, Long orgId,
			String branchCode, String partyCode);

	@Query(nativeQuery = true,value = "select sum(amount) as totalAmount from vw_revenue where orgid=?1 and billmonth=?2 or 'ALL'= ?2 and finyear=?3 ")
	Set<Object[]> getDsahboardRevenue(Long orgId, String billMonth, String finYear);




	
}