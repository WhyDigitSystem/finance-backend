package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.ReconcileCashVO;

@Repository
public interface ReconcileCashRepo extends JpaRepository<ReconcileCashVO, Long> {

	@Query(nativeQuery = true, value = "select * from reconcilecash where orgid=?1")
	List<ReconcileCashVO> getAllReconcileCashByOrgId(Long orgId);

	@Query(nativeQuery = true, value = "select * from reconcilecash where reconcilecashid=?1")
	ReconcileCashVO getAllReconcileCashById(Long id);

//	@Query(nativeQuery = true, value = "select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
//	String getReconcileCashDocId(Long orgId, String finYear, String branchCode, String screenCode);
	
	
	@Query(value = "SELECT " + "CONCAT(d.prefixfield, LPAD(d.lastno, 5, '0')) AS docid, " + "CASE "
			+ "   WHEN CURDATE() BETWEEN f.startdate AND f.enddate " + "   THEN CURDATE() " + "   ELSE f.enddate "
			+ "END AS docdate " + "FROM documenttypemappingdetails d " + "JOIN financialyear f "
			+ "ON d.finyear = f.finyear AND d.orgid = f.orgid " + "WHERE d.orgid = ?1 " + "AND d.finyear = ?2 "
			+ "AND d.branchcode = ?3 " + "AND d.screencode = ?4", nativeQuery = true)
	List<Object[]> getReconcileCashDocId(Long orgId, String finYear, String branchCode, String screenCode);

	@Query(nativeQuery = true, value = "select accountgroupname  from  groupledger where orgid=?1 and category='BANK' and active=1 group by accountgroupname")
	Set<Object[]> findByAccountNameForBank(Long orgId);

	

}
