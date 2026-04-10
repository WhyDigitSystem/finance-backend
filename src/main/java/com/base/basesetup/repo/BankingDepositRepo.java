package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.BankingDepositVO;

@Repository
public interface BankingDepositRepo extends JpaRepository<BankingDepositVO, Long> {

	@Query(nativeQuery = true, value = "select * from bankingdeposit where orgid=?1")
	List<BankingDepositVO> getAllBankingDepositByOrgId(Long orgId);

	@Query(nativeQuery = true, value = "select * from bankingdeposit where bankingdepositid=?1")
	List<BankingDepositVO> getBankingDepositById(Long id);

//	@Query(nativeQuery = true,value ="select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
//	String getBankingDepositDocId(Long orgId, String finYear, String branchCode, String screenCode);

	@Query(value = "SELECT " + "CONCAT(d.prefixfield, LPAD(d.lastno, 5, '0')) AS docid, " + "CASE "
			+ "   WHEN CURDATE() BETWEEN f.startdate AND f.enddate " + "   THEN CURDATE() " + "   ELSE f.enddate "
			+ "END AS docdate " + "FROM documenttypemappingdetails d " + "JOIN financialyear f "
			+ "ON d.finyear = f.finyear AND d.orgid = f.orgid " + "WHERE d.orgid = ?1 " + "AND d.finyear = ?2 "
			+ "AND d.branchcode = ?3 " + "AND d.screencode = ?4", nativeQuery = true)
	List<Object[]> getBankingDepositDocId(Long orgId, String finYear, String branchCode, String screenCode);

//	@Query(nativeQuery = true, value = "select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
//	String getBankingDepositByDocId(Long orgId, String finYear, String branchCode, String screenCode);

	@Query(nativeQuery = true, value = "select accountgroupname from groupledger where orgid=?1 and category='bank'  and  active=1")
	Set<Object[]> findBankNameFromGroupforBankingDeposit(Long orgId);

}
