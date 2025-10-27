package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.base.basesetup.entity.GroupLedgersVO;
import com.base.basesetup.entity.GroupMappingVO;

public interface GroupLedgersRepo extends JpaRepository<GroupLedgersVO, Long> {

	List<GroupLedgersVO> findByGroupMappingVO(GroupMappingVO groupMappingVO);
	
	@Query(nativeQuery = true, value = "SELECT h.ledgers, h.groupcode\r\n"
			+ "FROM (\r\n"
			+ "    SELECT accountgroupname AS ledgers, accountcode AS groupcode\r\n"
			+ "    FROM coa\r\n"
			+ "    WHERE orgid = ?1 AND active = 1\r\n"
			+ "    GROUP BY accountgroupname, accountcode\r\n"
			+ "\r\n"
			+ "    UNION\r\n"
			+ "\r\n"
			+ "    SELECT a.valuesdescription AS ledgers, null AS groupcode\r\n"
			+ "    FROM listofvaluesdetails a\r\n"
			+ "    JOIN listofvalues b ON a.listofvaluesid = b.listofvaluesid\r\n"
			+ "    WHERE b.orgid = ?1\r\n"
			+ "      AND b.name = ?2\r\n"
			+ "      AND a.active = 1\r\n"
			+ "    GROUP BY a.valuesdescription\r\n"
			+ ") h\r\n"
			+ "GROUP BY h.ledgers, h.groupcode")
	Set<Object []>ledgersDetails(Long orgId, String segment);



	GroupLedgersVO findByOrgIdAndAccountNameAndMainGroupName(Long orgId, String accountName, String mainGroup);

}
