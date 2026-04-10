package com.base.basesetup.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.base.basesetup.entity.AccountsVO;

public interface AccountsRepo extends JpaRepository<AccountsVO, Long> {

//	@Query(nativeQuery = true, value = "select concat(prefixfield,lpad(lastno,5,0)) AS docid from multipledocidgendetails where orgid=?1  and finyear=?2  and branchcode=?3 and sourcescreencode=?4 and screencode=?5 ")
//	String getApproveDocId(Long orgId, String finYear, String branchCode, String sourceScreenCode, String screenCode);

	@Query(nativeQuery = true, value = "SELECT \r\n" + "    CONCAT(d.prefixfield, LPAD(d.lastno, 5, '0')) AS docid,\r\n"
			+ "    CASE \r\n" + "        WHEN CURDATE() BETWEEN f.startdate AND f.enddate \r\n"
			+ "        THEN CURDATE()\r\n" + "        ELSE f.enddate\r\n" + "    END AS docdate\r\n"
			+ "FROM multipledocidgendetails d\r\n" + "JOIN financialyear f \r\n" + "    ON d.finyear = f.finyear \r\n"
			+ "    AND d.orgid = f.orgid\r\n" + "WHERE d.orgid = ?1\r\n"
			+ "AND d.finyear = ?2  AND d.branchcode = ?3 and d.sourcescreencode=?4\r\n" + "AND d.screencode = ?5")
	List<Object[]> getApproveDocId(Long orgId, String finYear, String branchCode, String sourceScreenCode,
			String screenCode);

	@Query(nativeQuery = true, value = "select concat(prefixfield,lpad(lastno,5,0)) AS docid from multipledocidgendetails where orgid=?1  and finyear=?2  and branchcode=?3 and sourcescreencode=?4 and screencode=?5 ")
	String getCostInvoiceDocId(Long orgId, String finYear, String branchCode, String sourceScreenCode,
			String screenCode);

	@Query(nativeQuery = true, value = "select concat(prefixfield,lpad(lastno,5,0)) AS docid from multipledocidgendetails where orgid=?1  and finyear=?2  and branchcode=?3 and sourcescreencode=?4 and screencode=?5 ")
	String getrCostInvoiceGnaDocId(Long orgId, String finYear, String branchCode, String screenCode,
			String accountsScreenCode);

	@Query(nativeQuery = true, value = "select concat(prefixfield,lpad(lastno,5,0)) AS docid from multipledocidgendetails where orgid=?1  and finyear=?2  and branchcode=?3 and sourcescreencode=?4 and screencode=?5 ")
	String geturCostInvoiceGnaDocId(Long orgId, String finYear, String branchCode, String screenCode,
			String accountsScreenCode);

	@Query(nativeQuery = true, value = "select concat(prefixfield,lpad(lastno,5,0)) AS docid from multipledocidgendetails where orgid=?1  and finyear=?2  and branchcode=?3 and sourcescreencode=?4 and screencode=?5 ")
	String getRCostInvoiceGnaDocId(Long orgId, String finYear, String branchCode, String sourceScreenCode,
			String screenCode);

	@Query(nativeQuery = true, value = "select * from accounts where refno=?1 and vid=?2 and vdate=?3")
	AccountsVO findByRefNoAndVIdAndVDate(String docId, String vId, LocalDate vDate);

	@Query(nativeQuery = true, value = "select * from accounts where refno=?1 and refdate=?2")
	AccountsVO findByRefNoAndRefDate(String docId, LocalDate docDate);

	@Query(nativeQuery = true, value = "select concat(prefixfield,lpad(lastno,5,0)) AS docid from multipledocidgendetails where orgid=?1  and finyear=?2  and branchcode=?3 and sourcescreencode=?4 and screencode=?5 ")
	String getCostEstimationDocId(Long orgId, String finYear, String branchCode, String sourceScreenCode,
			String screenCode);

}
