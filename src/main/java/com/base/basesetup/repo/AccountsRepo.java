package com.base.basesetup.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.base.basesetup.entity.AccountsVO;

public interface AccountsRepo extends JpaRepository<AccountsVO, Long> {

	@Query(nativeQuery = true, value = "select concat(prefixfield,lpad(lastno,5,0)) AS docid from multipledocidgendetails where orgid=?1  and finyear=?2  and branchcode=?3 and sourcescreencode=?4 and screencode=?5 ")
	String getApproveDocId(Long orgId, String finYear, String branchCode, String sourceScreenCode,
			String screenCode);


	@Query(nativeQuery = true, value = "select concat(prefixfield,lpad(lastno,5,0)) AS docid from multipledocidgendetails where orgid=?1  and finyear=?2  and branchcode=?3 and sourcescreencode=?4 and screencode=?5 ")
	String getCostInvoiceDocId(Long orgId, String finYear, String branchCode, String sourceScreenCode,
			String screenCode);


}
