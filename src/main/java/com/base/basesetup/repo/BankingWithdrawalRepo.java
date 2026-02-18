package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.BankingWithdrawalVO;

@Repository
public interface BankingWithdrawalRepo extends JpaRepository<BankingWithdrawalVO, Long> {

	@Query(nativeQuery = true, value = "select * from bankingwithdrawal where orgid=?1")
	List<BankingWithdrawalVO> getAllBankingWithdrawalByOrgId(Long orgId);

	@Query(nativeQuery = true, value = "select * from bankingwithdrawal where bankingwithdrawalid=?1")
	List<BankingWithdrawalVO> getBankingWithdrawalById(Long id);

	@Query(nativeQuery = true,value="select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
	String getBankingWithdrawalDocId(Long orgId, String finYear, String branchCode, String screenCode);

	@Query(nativeQuery = true,value ="select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
	String getBankingWithdrawalByDocId(Long orgId, String finYear, String branchCode, String screenCode);


}
