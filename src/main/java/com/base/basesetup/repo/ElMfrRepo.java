package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.ElMfrVO;

@Repository
public interface ElMfrRepo extends JpaRepository<ElMfrVO, Long> {

	boolean existsByDescriptionAndOrgId(String description, Long orgId);

	boolean existsByElCodeAndOrgId(String elCode, Long orgId);

	@Query(nativeQuery = true, value = "select * FROM elmfr where orgid=?1")
	List<ElMfrVO> getAllElMfr(Long orgId);

	
	@Query(nativeQuery =true,value ="SELECT ROW_NUMBER() OVER() AS sno, th.accountcode, th.accountname, th.action, th.screen \r\n"
			+ "FROM (\r\n"
			+ "    SELECT \r\n"
			+ "        tb.accountcode,\r\n"
			+ "        tb.accountname,\r\n"
			+ "        'Pending Client COA Creation' AS action,\r\n"
			+ "        1 AS screen\r\n"
			+ "    FROM tbexcelupload tb\r\n"
			+ "    WHERE tb.clientcode = ?2\r\n"
			+ "      AND tb.orgid = ?1\r\n"
			+ "      AND tb.accountname NOT IN (\r\n"
			+ "        SELECT accountname\r\n"
			+ "        FROM ccoa \r\n"
			+ "        WHERE clientcode = ?2\r\n"
			+ "          AND orgid = ?1\r\n"
			+ "        GROUP BY accountname\r\n"
			+ "      )\r\n"
			+ "      AND tb.accountname NOT IN (\r\n"
			+ "        SELECT l.clientcoa\r\n"
			+ "        FROM ledgermapping l\r\n"
			+ "        WHERE l.clientcode = ?2\r\n"
			+ "          AND l.orgid = ?1\r\n"
			+ "        GROUP BY l.clientcoa\r\n"
			+ "      )\r\n"
			+ "    GROUP BY tb.accountcode, tb.accountname, action, screen\r\n"
			+ "    UNION\r\n"
			+ "    SELECT \r\n"
			+ "        tb.accountcode,\r\n"
			+ "        tb.accountname,\r\n"
			+ "        'Pending Ledger Mapping' AS action,\r\n"
			+ "        2 AS screen\r\n"
			+ "    FROM tbexcelupload tb\r\n"
			+ "    WHERE tb.clientcode = ?2\r\n"
			+ "      AND tb.orgid = ?1\r\n"
			+ "      AND tb.accountname IN (\r\n"
			+ "        SELECT accountname \r\n"
			+ "        FROM ccoa \r\n"
			+ "        WHERE clientcode = ?2\r\n"
			+ "          AND orgid = ?1\r\n"
			+ "        GROUP BY accountname\r\n"
			+ "      )\r\n"
			+ "      AND tb.accountname NOT IN (\r\n"
			+ "        SELECT l.clientcoa\r\n"
			+ "        FROM ledgermapping l\r\n"
			+ "        WHERE l.clientcode = ?2\r\n"
			+ "          AND l.orgid = ?1\r\n"
			+ "        GROUP BY l.clientcoacode\r\n"
			+ "      )\r\n"
			+ "    GROUP BY tb.accountcode, tb.accountname, action, screen\r\n"
			+ ") th")
	Set<Object[]> getMisMatchClientTb(Long orgId, String clientCode);

}
