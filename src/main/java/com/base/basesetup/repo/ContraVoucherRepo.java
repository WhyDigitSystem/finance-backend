package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.ContraVoucherVO;

@Repository
public interface ContraVoucherRepo extends JpaRepository<ContraVoucherVO, Long>{

	@Query(nativeQuery = true, value = "select * from contravoucher where orgid=?1")
	List<ContraVoucherVO> getAllContraVoucherByOrgId(Long orgId);

	@Query(nativeQuery = true, value = "select * from contravoucher where contravoucherid=?1")
	List<ContraVoucherVO> getAllContraVoucherById(Long id);

	@Query(nativeQuery = true,value="select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
	String getContraVoucherDocId(Long orgId, String finYear, String branchCode, String screenCode);

	@Query(nativeQuery = true,value ="select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
	String getContraVoucherByDocId(Long orgId, String finYear, String branchCode, String screenCode);

	@Query(nativeQuery = true,value ="select  a0.accountgroupname, a0.accountcode, a0.category , a0.currency from groupledger a0 where orgid=?1 and a0.type='ACCOUNT' and a0.Active=1 and a0.category in ('BANK','CASH')")
	Set<Object[]> findAccountNamefromGroupLedgerforCV(Long orgId);


}
