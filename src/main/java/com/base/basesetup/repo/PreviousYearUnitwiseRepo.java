package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.base.basesetup.entity.PreviousYearUnitwiseVO;

public interface PreviousYearUnitwiseRepo extends JpaRepository<PreviousYearUnitwiseVO, Long> {
	
	@Query(nativeQuery = true, value = "WITH Months AS (\r\n"
			+ "    SELECT 'April' AS month UNION ALL\r\n"
			+ "    SELECT 'May' UNION ALL\r\n"
			+ "    SELECT 'June' UNION ALL\r\n"
			+ "    SELECT 'July' UNION ALL\r\n"
			+ "    SELECT 'August' UNION ALL\r\n"
			+ "    SELECT 'September' UNION ALL\r\n"
			+ "    SELECT 'October' UNION ALL\r\n"
			+ "    SELECT 'November' UNION ALL\r\n"
			+ "    SELECT 'December' UNION ALL\r\n"
			+ "    SELECT 'January' UNION ALL\r\n"
			+ "    SELECT 'February' UNION ALL\r\n"
			+ "    SELECT 'March'\r\n"
			+ "),\r\n"
			+ "Accounts AS (\r\n"
			+ "    SELECT \r\n"
			+ "        a.accountname,\r\n"
			+ "        a.accountcode,\r\n"
			+ "        b.natureofaccount,\r\n"
			+ "        CAST(a.displayseq AS UNSIGNED) AS displayseq\r\n"
			+ "    FROM groupledgers a\r\n"
			+ "    LEFT JOIN coa b ON a.accountname = b.accountgroupname\r\n"
			+ "    WHERE a.groupname = ?4 \r\n"
			+ "      AND a.orgid = ?1\r\n"
			+ "      AND (a.subgroupname = ?5 OR ?5 = 'ALL') \r\n"
			+ "      AND a.active = 1\r\n"
			+ ")\r\n"
			+ "SELECT \r\n"
			+ "    a.accountname, \r\n"
			+ "    a.accountcode, \r\n"
			+ "    a.natureofaccount, \r\n"
			+ "    m.month, \r\n"
			+ "    COALESCE(SUM(b.amount), 0) AS totalamount\r\n"
			+ "FROM Accounts a\r\n"
			+ "CROSS JOIN Months m\r\n"
			+ "LEFT JOIN pyunit b \r\n"
			+ "    ON a.accountname = b.accountname\r\n"
			+ "    AND b.orgid = ?1 and b.unit=?6\r\n"
			+ "    AND b.year = ?2\r\n"
			+ "    AND b.clientcode = ?3\r\n"
			+ "    AND b.maingroup = ?4\r\n"
			+ "    AND (b.subgroup = ?5 OR ?5 = 'ALL')\r\n"
			+ "    AND b.month = m.month\r\n"
			+ "GROUP BY \r\n"
			+ "    a.accountname, \r\n"
			+ "    a.accountcode, \r\n"
			+ "    a.natureofaccount, \r\n"
			+ "    m.month, \r\n"
			+ "    a.displayseq,\r\n"
			+ "    FIELD(m.month, 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December', 'January', 'February', 'March')\r\n"
			+ "ORDER BY \r\n"
			+ "    cast(a.displayseq as unsigned),\r\n"
			+ "    FIELD(m.month, 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December', 'January', 'February', 'March')")
	Set<Object[]> getPYUnitWiseLedgersDetails(Long orgId,String year,String clientCode, String mainGroup,String accountCode,String unit);
	
	@Query(nativeQuery = true, value = "select a.* from pyunit a where a.orgid=?1 and a.clientcode=?2 and a.year=?3 and a.unit=?4 ")
	List<PreviousYearUnitwiseVO> getPYUnitDetails(Long org, String clientcode, String yr, String unit);

	


}
