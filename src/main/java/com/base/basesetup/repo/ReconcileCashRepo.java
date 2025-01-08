package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.ReconcileCashVO;

@Repository
public interface ReconcileCashRepo extends JpaRepository<ReconcileCashVO, Long> {

	@Query(nativeQuery = true, value = "select * from reconcilecash where orgid=?1")
	List<ReconcileCashVO> getAllReconcileCashByOrgId(Long orgId);

	@Query(nativeQuery = true, value = "select * from reconcilecash where reconcilecashid=?1")
	List<ReconcileCashVO> getAllReconcileCashById(Long id);


	@Query(nativeQuery = true, value = "select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
	String getReconcileCashDocId(Long orgId, String finYear, String branchCode, String screenCode);

}
