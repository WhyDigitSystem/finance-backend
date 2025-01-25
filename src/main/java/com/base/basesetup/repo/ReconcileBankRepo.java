package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.ReconcileBankVO;

@Repository
public interface ReconcileBankRepo extends JpaRepository<ReconcileBankVO, Long> {

	@Query(nativeQuery = true, value = "select * from reconcilebank where orgid=?1")
	List<ReconcileBankVO> getAllReconcileBankByOrgId(Long orgId);

	@Query(nativeQuery = true, value = "select * from reconcilebank where reconcilebankid=?1")
	ReconcileBankVO getAllReconcileBankById(Long id);

	@Query(nativeQuery = true, value = "select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
	String getReconcileBankDocId(Long orgId, String finYear, String branchCode, String screenCode);

	@Query(nativeQuery = true, value = "select accountgroupname  from  groupledger where orgid=?1 and category='BANK' and active=1 group by accountgroupname")
	Set<Object[]> findByAccountNameForBank(Long orgId);

}