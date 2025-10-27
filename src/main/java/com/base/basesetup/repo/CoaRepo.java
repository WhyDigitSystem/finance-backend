package com.base.basesetup.repo;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.base.basesetup.entity.CoaVO;

public interface CoaRepo extends JpaRepository<CoaVO, Long>{



	@Query(nativeQuery =true,value ="select accountgroupname from  coa where active=1 and orgid=?1 and type='group'")
	Set<Object[]> findGroups(Long orgId);

	@Query(nativeQuery = true,value = "SELECT \r\n"
			+ "    a.accountgroupname AS maingroup,\r\n"
			+ "    a.accountcode AS maingroupaccountcode,\r\n"
			+ "    b.accountgroupname AS subgroup,\r\n"
			+ "    b.accountcode AS subgroupaccountcode,\r\n"
			+ "    c.accountgroupname AS account,\r\n"
			+ "    c.accountcode AS accountcode\r\n"
			+ "FROM \r\n"
			+ "    coa a\r\n"
			+ "LEFT JOIN \r\n"
			+ "    coa b ON b.parentcode = a.accountcode AND b.orgid = a.orgid\r\n"
			+ "LEFT JOIN \r\n"
			+ "    coa c ON c.parentcode = b.accountcode AND c.orgid = b.orgid\r\n"
			+ "WHERE \r\n"
			+ "    a.orgid = ?1 and a.accountcode<1000\r\n"
			+ "ORDER BY \r\n"
			+ "    CAST(a.accountcode AS UNSIGNED) ASC,\r\n"
			+ "    CAST(b.accountcode AS UNSIGNED) ASC,\r\n"
			+ "    CAST(c.accountcode AS UNSIGNED) ASC")
	Set<Object[]> findAccountMap(Long orgId);

	CoaVO findByOrgIdAndAccountCode(Long orgId, String elAccountCode);



	@Query(value = "select * from coa a where a.orgid=?1",nativeQuery = true)
	List<CoaVO> getAllCaoByOrgId(Long orgId);

	@Query(nativeQuery =true,value ="select * from  coa where active=1 and orgid=?1 and accountgroupname=?2  and type='group' and groupname is null")
	CoaVO getOrgIdAndMainAccountGroupName(Long orgId, String groupName);

	@Query(nativeQuery =true,value ="select * from  coa where active=1 and orgid=?1 and accountgroupname=?2  and type='group' and groupname is not null")
	CoaVO getOrgIdAndSubAccountGroupName(Long orgId, String groupName);

	Optional<CoaVO> findByAccountCode(String code);

	boolean existsByOrgIdAndAccountGroupNameIgnoreCase(Long orgId, String accountName);

	boolean existsByOrgIdAndAccountCodeIgnoreCase(Long orgId, String accountCode);
	
	@Query(nativeQuery = true,value = "select a.* from coa a where a.orgid=?1 and a.type='Group' and groupname is not null ")
	List<CoaVO> getOrgIdAndSubGroupName(Long orgId);

	@Query(nativeQuery = true,value = "select a.* from coa a where a.orgid=?1 and a.type='Account' and parentcode in(?2)")
	List<CoaVO> getOrgIdAndSubGroupName(Long orgId, List<String> accountCode);

}
