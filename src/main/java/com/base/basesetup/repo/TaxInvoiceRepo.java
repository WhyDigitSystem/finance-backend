package com.base.basesetup.repo;

import java.util.List;
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

	@Query(nativeQuery = true,value = "SELECT      \r\n"
			+ "    a.orgid, a.branchcode, a.docid, a.docdate, a.joborderno, \r\n"
			+ "    c.docid AS vourcharno, c.docdate AS vourchardate, \r\n"
			+ "    e.partyname AS billtoparty, e.controllingoff, \r\n"
			+ "    a.billcurr, a.billcurrrate, a.totalinvamountbc, a.totalinvamountlc, \r\n"
			+ "    a.totaltaxableamountlc, a.gsttype, a.totaltaxamountlc, a.totaltaxamountbc, \r\n"
			+ "    a.roundoffamountlc, \r\n"
			+ "    SUM(b.fcamount) AS fcamt, SUM(b.lcamount) AS lcamt, \r\n"
			+ "    SUM(b.RATE) AS rate, SUM(b.BILLAMOUNT) AS billamount \r\n"
			+ "FROM taxinvoice a \r\n"
			+ "JOIN taxinvoicedetails b ON a.taxinvoiceid = b.taxinvoiceid   \r\n"
			+ "JOIN accounts c ON a.docid = c.refno  \r\n"
			+ "JOIN partymaster e   \r\n"
			+ "WHERE c.finyear = ?1 \r\n"
			+ "AND c.docdate BETWEEN STR_TO_DATE(?2, '%Y-%m-%d') \r\n"
			+ "                       AND STR_TO_DATE(?3, '%Y-%m-%d')\r\n"
			+ "    AND a.orgid = ?4\r\n"
			+ "    AND (a.branchcode = ?5 OR ?5 = 'ALL') \r\n"
			+ "    AND (e.partycode = ?6 OR ?6 = 'ALL') \r\n"
			+ "GROUP BY a.orgid, a.branchcode, a.docid, a.docdate, a.joborderno, \r\n"
			+ "    c.docid, c.docdate,  e.partyname, e.controllingoff, \r\n"
			+ "    a.billcurr, a.billcurrrate, a.totalinvamountbc, a.totalinvamountlc, \r\n"
			+ "    a.totaltaxableamountlc, a.gsttype, a.totaltaxamountlc, a.totaltaxamountbc, \r\n"
			+ "    a.roundoffamountlc  \r\n"
			+ "\r\n"
			+ "UNION  \r\n"
			+ "\r\n"
			+ "SELECT      \r\n"
			+ "    a.orgid, a.branchcode, a.docid, a.docdate,a.joborderno, \r\n"
			+ "    c.docid AS vourcharno, c.docdate AS vourchardate, \r\n"
			+ "     e.partyname AS billtoparty, e.controllingoff, \r\n"
			+ "    a.billcurr, a.billcurrrate, a.totalinvamountbc, a.totalinvamountlc, \r\n"
			+ "    a.totaltaxableamountlc, a.gsttype, a.totaltaxamountlc, a.totaltaxamountbc, \r\n"
			+ "    a.roundoffamountlc, \r\n"
			+ "    SUM(b.fcamount) AS fcamt, SUM(b.lcamount) AS lcamt, \r\n"
			+ "    SUM(b.RATE) AS rate, SUM(b.BILLAMOUNT) AS billamount \r\n"
			+ "FROM taxinvoice a \r\n"
			+ "JOIN taxinvoicedetails b ON a.taxinvoiceid = b.taxinvoiceid   \r\n"
			+ "JOIN accounts c ON a.docid = c.refno  \r\n"
			+ "JOIN partymaster e  \r\n"
			+ "WHERE c.finyear = ?1 \r\n"
			+ "AND c.docdate BETWEEN STR_TO_DATE(?2, '%Y-%m-%d') \r\n"
			+ "                       AND STR_TO_DATE(?3, '%Y-%m-%d')\r\n"
			+ "    AND a.orgid = ?4\r\n"
			+ "    AND (a.branchcode = ?5 OR ?5 = 'ALL')  \r\n"
			+ "    AND (e.partycode = ?6 OR ?6 = 'ALL') \r\n"
			+ "GROUP BY a.orgid, a.branchcode, a.docid, a.docdate, a.joborderno, \r\n"
			+ "    c.docid, c.docdate,  e.partyname, e.controllingoff, \r\n"
			+ "    a.billcurr, a.billcurrrate, a.totalinvamountbc, a.totalinvamountlc, \r\n"
			+ "    a.totaltaxableamountlc, a.gsttype, a.totaltaxamountlc, a.totaltaxamountbc, \r\n"
			+ "    a.roundoffamountlc  \r\n"
			+ "\r\n"
			+ "UNION  \r\n"
			+ "\r\n"
			+ "SELECT      \r\n"
			+ "    a.orgid, a.branchcode, a.docid, a.docdate, a.joborderno, \r\n"
			+ "    c.docid AS vourcharno, c.docdate AS vourchardate, \r\n"
			+ "    e.partyname AS billtoparty, e.controllingoff, \r\n"
			+ "    a.billcurr, a.billcurrrate, a.totalinvamountbc, a.totalinvamountlc, \r\n"
			+ "    0 AS taxableamt, 'NONE' AS gsttype, 0 AS gstamtlc, 0 AS gstamtfc, \r\n"
			+ "    a.roundoffamountlc, \r\n"
			+ "    SUM(b.fcamount) AS fcamt, SUM(b.lcamount) AS lcamt, \r\n"
			+ "    SUM(b.RATE) AS rate, SUM(b.BILLAMOUNT) AS billamount \r\n"
			+ "FROM taxinvoice a \r\n"
			+ "JOIN taxinvoicedetails b ON a.taxinvoiceid = b.taxinvoiceid   \r\n"
			+ "JOIN accounts c ON a.docid = c.refno  \r\n"
			+ "JOIN partymaster e   \r\n"
			+ "WHERE c.finyear = ?1 \r\n"
			+ "AND c.docdate BETWEEN STR_TO_DATE(?2, '%Y-%m-%d') \r\n"
			+ "                       AND STR_TO_DATE(?3, '%Y-%m-%d')\r\n"
			+ "    AND a.orgid = ?4\r\n"
			+ "    AND (a.branchcode = ?5 OR ?5 = 'ALL') \r\n"
			+ "    AND (e.partycode = ?6 OR ?6 = 'ALL') \r\n"
			+ "GROUP BY a.orgid, a.branchcode, a.docid, a.docdate, a.joborderno, \r\n"
			+ "    e.partyname, e.controllingoff, \r\n"
			+ "    a.billcurr, a.billcurrrate, a.totalinvamountbc, a.totalinvamountlc, \r\n"
			+ "    a.roundoffamountlc, c.docid, c.docdate \r\n"
			+ "\r\n"
			+ "ORDER BY 7, 6  \r\n"
			+ "")
	Set<Object[]> getReportDetailsForSalesRegister(String finyear, String fromDate, String toDate, Long orgId,
			String branchCode, String partyCode);




	
}