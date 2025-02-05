package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.TaxMasterVO;

@Repository
public interface TaxMasterRepo extends JpaRepository<TaxMasterVO,Long>{

	@Query(value="Select * from taxmaster where orgid=?1",nativeQuery = true)
	List<TaxMasterVO> getAllTaxMasterByOrgId(Long orgId);

	@Query(value="Select * from taxmaster where taxmasterid=?1",nativeQuery = true)
	List<TaxMasterVO> getAllTaxMasterById(Long id);

	@Query(nativeQuery = true,value = "select * from taxmaster where active=1")
	List<TaxMasterVO> findTaxMasterByActive();

	@Query(nativeQuery = true,value = "select serviceaccountcode from saccode where orgid=?1 and active=1")
	Set<Object[]> findServiceAccountCodeForTaxMaster(Long orgId);

	@Query(nativeQuery = true,value = "select accountgroupname from groupledger where orgid=?1 and type in ('ACCOUNT') and category in ('RECEIVABLE A/C') and gsttaxflag in ('OUTPUT TAX') and active=1")
	Set<Object[]> findRevenueLegderForTaxMaster(Long orgId);

	@Query(nativeQuery = true,value = "select accountgroupname from groupledger where orgid=?1 and type in ('ACCOUNT') and category in ('PAYABLE A/C') and gsttaxflag in ('OUTPUT TAX') and active=1")
	Set<Object[]> findCostLedgerForTaxMaster(Long orgId);


}
