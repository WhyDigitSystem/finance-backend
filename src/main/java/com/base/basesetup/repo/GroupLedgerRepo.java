package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.base.basesetup.entity.GroupLedgerVO;

public interface GroupLedgerRepo extends JpaRepository<GroupLedgerVO, Long>{
	
@Query(nativeQuery =true,value = "select * from groupledger where orgid=?1 order by type,groupledgerid asc")
	List<GroupLedgerVO> getAllGroupLedgerByOrgId(Long orgId);

@Query(nativeQuery =true,value = "select * from groupledger where groupledgerid=?1")
List<GroupLedgerVO> getAllGroupLedgerById(Long id);

@Query(nativeQuery = true,value = "select * from groupledger where active=1")
List<GroupLedgerVO> findGroupLedgerByActive();

boolean existsByAccountGroupNameAndOrgId(String accountGroupName, Long orgId);


@Query(nativeQuery = true, value = "select accountgroupname from groupledger where orgid=?1 and type='GROUP' and active=1 group by accountgroupname")
Set<Object[]> getGroupDetails(Long orgId);


GroupLedgerVO findByAccountGroupName(String key);


@Query(nativeQuery = true, value = "select * from groupledger where orgid=?1 and gsttaxflag!='NA' and category='TAX' and gsttaxflag='OUTPUT TAX' and gsttype=?2 and gstpercentage=?3  order by gstpercentage desc")
List<GroupLedgerVO> getTaxLedgerDetails(Long orgId, String gstType, Double key);

boolean existsByAccountCodeAndOrgId(String accountCode, Long orgId);

@Query(nativeQuery =true,value ="select * from  groupledger where active=1 and orgid=?1 and accountgroupname=?2  and type='group'")
GroupLedgerVO getOrgIdAndMainAccountGroupName(Long orgId, String groupName);


@Query(nativeQuery =true,value ="select * from  groupledger where active=1 and orgid=?1 and accountgroupname=?2  and type='group' and groupname is not null")
GroupLedgerVO getOrgIdAndSubAccountGroupName(Long orgId, String groupName);

@Query(nativeQuery =true,value ="select * from  groupledger where active=1 and orgid=?1 and accountcode=?2  and type='group' and groupname is null")
GroupLedgerVO getOrgIdAndMainAccountCode(Long orgId, String parentCode);

@Query(nativeQuery =true,value ="select * from  groupledger where active=1 and orgid=?1 and accountcode=?2  and type='group' and groupname is not null")
GroupLedgerVO getOrgIdAndSubAccountCode(Long orgId, String parentCode);

@Query(nativeQuery =true,value =" SELECT \r\n"
		+ "    a.accountgroupname AS maingroup,\r\n"
		+ "    a.accountcode AS maingroupaccountcode,\r\n"
		+ "    b.accountgroupname AS subgroup,\r\n"
		+ "    b.accountcode AS subgroupaccountcode,\r\n"
		+ "    c.accountgroupname AS account,\r\n"
		+ "    c.accountcode AS accountcode\r\n"
		+ "FROM \r\n"
		+ "    groupledger a\r\n"
		+ "LEFT JOIN \r\n"
		+ "    groupledger b ON b.parentcode = a.accountcode AND b.orgid = a.orgid\r\n"
		+ "LEFT JOIN \r\n"
		+ "    groupledger c ON c.parentcode = b.accountcode AND c.orgid = b.orgid\r\n"
		+ "WHERE \r\n"
		+ "    a.orgid = 202502 AND a.accountcode < 100\r\n"
		+ "ORDER BY \r\n"
		+ "    CAST(a.accountcode AS UNSIGNED) ASC,\r\n"
		+ "    CAST(b.accountcode AS UNSIGNED) ASC,\r\n"
		+ "    CAST(c.accountcode as unsigned) asc")
Set<Object[]> findgetGroupLedgerexcelDetails(Long orgId);

@Query(nativeQuery =true,value = "select * from groupledger where accountcode=?1")
List<GroupLedgerVO> findAllGroupLedgerByAccountCode(String  accountCode);



}