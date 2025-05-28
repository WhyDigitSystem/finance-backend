package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.IssueManifestProviderVO;

@Repository
public interface IssueManifestProviderRepo extends JpaRepository<IssueManifestProviderVO, Long>{

	boolean existsByOrgIdAndTransactionNo(Long orgId, String transactionNo);

	
	@Query(nativeQuery =true,value ="select * from mim where orgid=?1")
	List<IssueManifestProviderVO> findAllIssueManifeasrProvider(Long orgId);

	@Query(nativeQuery =true,value ="select * from mim where orgid=?1 and finyear=?2")
	List<IssueManifestProviderVO> getAllIssueManifestProvider(Long orgId, Long finYear);

}
