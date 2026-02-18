package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.CCoaVO;

@Repository
public interface CCoaRepo extends JpaRepository<CCoaVO, Long>{


	CCoaVO findByAccountName(String groupName);

	@Query(nativeQuery =true,value ="select accountgroupname from  ccoa where active=1 and type='group'")
	Set<Object[]> findGroups();



	CCoaVO findByOrgIdAndAccountCode(Long orgId, String clientAccountCodes);


	@Query(value = "select a from CCoaVO a where a.orgId=?1 and a.clientCode=?2")
	List<CCoaVO> findAllByOrgIdAndClientCode(Long orgId, String clientCode);


	boolean existsByOrgIdAndClientCodeAndAccountNameIgnoreCase(Long orgId, String clientCode, String accountName);

	boolean existsByOrgIdAndClientCodeAndAccountCode(Long orgId, String clientCode, String accountCode);

	List<CCoaVO> findByOrgIdAndClientCode(Long orgId, String clientCode);

	CCoaVO findByOrgIdAndClientCodeAndAccountNameIgnoreCase(Long orgId, String clientCode, String clientAccountName);

}
