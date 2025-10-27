package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.base.basesetup.entity.PreviousYearActualVO;

public interface PreviousYearActualRepo extends JpaRepository<PreviousYearActualVO, Long>{

	@Query(nativeQuery = true, value = "SELECT \r\n"
			+ "    ROW_NUMBER() OVER (ORDER BY accountcode) AS `ELGLS.No`,\r\n"
			+ "    accountname AS `ELGL`,\r\n"
			+ "    accountcode AS `ELGLCode`,\r\n"
			+ "    natureofaccount AS `NLDN/CR`,\r\n"
			+ "    MAX(CASE WHEN month = 'January' THEN amount ELSE 0 END) AS 'January',\r\n"
			+ "    MAX(CASE WHEN month = 'February' THEN amount ELSE 0 END) AS 'February',\r\n"
			+ "    MAX(CASE WHEN month = 'March' THEN amount ELSE 0 END) AS 'March',\r\n"
			+ "    MAX(CASE WHEN month = 'April' THEN amount ELSE 0 END) AS 'April',\r\n"
			+ "    MAX(CASE WHEN month = 'May' THEN amount ELSE 0 END) AS 'May',\r\n"
			+ "    MAX(CASE WHEN month = 'June' THEN amount ELSE 0 END) AS 'June',\r\n"
			+ "    MAX(CASE WHEN month = 'July' THEN amount ELSE 0 END) AS 'July',\r\n"
			+ "    MAX(CASE WHEN month = 'August' THEN amount ELSE 0 END) AS 'August',\r\n"
			+ "    MAX(CASE WHEN month = 'September' THEN amount ELSE 0 END) AS 'September',\r\n"
			+ "    MAX(CASE WHEN month = 'October' THEN amount ELSE 0 END) AS 'October',\r\n"
			+ "    MAX(CASE WHEN month = 'November' THEN amount ELSE 0 END) AS 'November',\r\n"
			+ "    MAX(CASE WHEN month = 'December' THEN amount ELSE 0 END) AS 'December'\r\n"
			+ "FROM previousyearactual where year=?2 and orgid=?1 and client=?3 and clientcode=?4 \r\n"
			+ "GROUP BY accountname, accountcode, natureofaccount\r\n"
			+ "ORDER BY accountcode")
	Set<Object[]> getClientPreviousYearActualDetails(Long orgId, String year, String client, String clientCode);

	@Query(nativeQuery = true,value = "select * from previousyearactual where orgid=?1 and clientcode=?2 and year=?3 and month=?4 and maingroup=?5 and subgroupcode=?6 and accountcode=?7")
	List<PreviousYearActualVO> getPreviousYearDetails1(Long orgId, String clientCode, String year, String month,
			String mainGroup, String subGroupCode, String accountCode);

	@Query(nativeQuery = true,value = "SELECT \r\n"
			+ "    a.maingroup, \r\n"
			+ "    a.subgroup, \r\n"
			+ "    a.subgroupcode, \r\n"
			+ "    a.accountcode, \r\n"
			+ "    a.accountname,\r\n"
			+ "    a.natureofaccount, \r\n"
			+ "    a.quater, \r\n"
			+ "    SUM(a.previousYear) AS previousYear,gl.displayseq\r\n"
			+ "FROM (\r\n"
			+ "    SELECT \r\n"
			+ "        maingroup, \r\n"
			+ "        subgroup, \r\n"
			+ "        subgroupcode, \r\n"
			+ "        accountcode, \r\n"
			+ "        accountname, \r\n"
			+ "        natureofaccount, \r\n"
			+ "        quater,\r\n"
			+ "        SUM(amount) AS previousYear\r\n"
			+ "    FROM previousyearactual\r\n"
			+ "    WHERE orgid = ?1\r\n"
			+ "      AND clientcode = ?3\r\n"
			+ "      AND year = ?2\r\n"
			+ "    GROUP BY \r\n"
			+ "        maingroup, \r\n"
			+ "        subgroup, \r\n"
			+ "        natureofaccount, \r\n"
			+ "        subgroupcode, \r\n"
			+ "        accountcode, \r\n"
			+ "        accountname, \r\n"
			+ "        quater\r\n"
			+ "\r\n"
			+ "    UNION\r\n"
			+ "\r\n"
			+ "    SELECT \r\n"
			+ "        maingroup, \r\n"
			+ "        subgroup, \r\n"
			+ "        subgroupcode, \r\n"
			+ "        accountcode, \r\n"
			+ "        accountname, \r\n"
			+ "        natureofaccount, \r\n"
			+ "        'YTD' AS quater,\r\n"
			+ "        SUM(amount) AS previousYear\r\n"
			+ "    FROM previousyearactual\r\n"
			+ "    WHERE orgid = ?1\r\n"
			+ "      AND clientcode = ?3\r\n"
			+ "      AND year = ?2\r\n"
			+ "      AND monthsequence <= (\r\n"
			+ "          SELECT MAX(monthsequence)\r\n"
			+ "          FROM previousyearactual\r\n"
			+ "          WHERE year = ?2\r\n"
			+ "            AND month = ?6\r\n"
			+ "            AND clientcode = ?3\r\n"
			+ "      )\r\n"
			+ "    GROUP BY \r\n"
			+ "        maingroup, \r\n"
			+ "        subgroup, \r\n"
			+ "        natureofaccount, \r\n"
			+ "        subgroupcode, \r\n"
			+ "        accountcode, \r\n"
			+ "        accountname\r\n"
			+ ") AS a JOIN groupledgers gl ON a.accountname = gl.accountname and gl.groupname=?4 and \r\n"
			+ " a.maingroup = ?4 \r\n"
			+ "  AND  (a.subgroup=?5 or 'ALL'=?5)\r\n"
			+ "GROUP BY \r\n"
			+ "    a.maingroup, \r\n"
			+ "    a.subgroup, \r\n"
			+ "    a.subgroupcode, \r\n"
			+ "    a.accountcode, \r\n"
			+ "    a.accountname, \r\n"
			+ "    a.natureofaccount, \r\n"
			+ "    a.quater,gl.displayseq ORDER BY cast(gl.displayseq as unsigned)")
	Set<Object[]> getELPYDetails(Long orgId, String finyear, String clientCode, String mainGroupName,
			String subGroupCode,String month);
	
	
	
	@Query(nativeQuery = true,value = "select a.maingroup,a.subgroup,a.subgroupcode,a.accountcode,a.accountname,a.natureofaccount,quater,sum(a.budget) budget,sum(a.actual)actual,sum(a.PY)PY from(\r\n"
			+ "select maingroup,subgroup,subgroupcode,accountcode,accountname,natureofaccount,quater,sum(amount) budget,0 actual,0 PY from budget where  orgid=?1 and clientcode=?2 and year=?3 and month=?5\r\n"
			+ "group by maingroup,subgroup,subgroupcode,accountcode,natureofaccount,accountname,quater \r\n"
			+ "union\r\n"
			+ "select maingroup,subgroup,subgroupcode,accountcode,accountname,natureofaccount,quater,0 budget,sum(amount)actual,0 PY from previousyearactual where orgid=?1 and clientcode=?2 and year=?3 and month=?5  group by maingroup,subgroup,natureofaccount,subgroupcode,accountcode,accountname,quater\r\n"
			+ "union\r\n"
			+ "select maingroup,subgroup,subgroupcode,accountcode,accountname,natureofaccount,quater,0 budget,0 actual,sum(amount)PY from previousyearactual where orgid=?1 and clientcode=?2 and year=?4 and month=?5  group by maingroup,subgroup,natureofaccount,subgroupcode,accountcode,accountname,quater) a\r\n"
			+ " JOIN groupledgers gl ON a.accountname = gl.accountname and gl.groupname=?6 and a.maingroup=?6 and (a.subgroup=?7 or ?7='ALL')group by a.maingroup,a.subgroup,a.subgroupcode,a.accountcode,a.accountname,a.natureofaccount,quater ORDER BY cast(gl.displayseq as unsigned)")
	Set<Object[]> getELActualQuaterDetails(Long orgId, String clientCode, String finyear, String previousYear, String month,
			String mainGroupName, String subGroupCode);

	
	
	@Query(nativeQuery = true,value = "select * from previousyearactual where orgid=?1 and clientcode=?2 and year=?3 and month=?4 and maingroup=?5 and subgroupcode=?6 and accountcode=?7 ")
	PreviousYearActualVO getPreviousYearDetails(Long orgId, String clientCode, String year, String month,
			String mainGroup, String subGroupCode, String accountCode);

	@Query(nativeQuery = true,value = "select a.* from previousyearactual a where a.orgid=?1 and a.clientcode=?2 and a.year=?3 and a.maingroup=?4 and a.subgroup=?5 ")
	List<PreviousYearActualVO> getClientBudgetDls(Long org, String clientcode, String yr, String maingroup,
			String subgroup);
	
	@Modifying
	@Transactional
	@Query(value = "delete from previousyearactual a where a.orgid = ?1 and a.clientcode = ?2 and a.year = ?3 and a.maingroup = ?4 and a.subgroup = ?5", nativeQuery = true)
	void deleteClientBudgetDls(Long orgId, String clientCode, String year, String mainGroup, String subGroup);

	@Query(nativeQuery = true,value = "SELECT p.month, SUM(p.amount) FROM previousyearactual p \r\n"
			+ "		       WHERE p.orgid = ?1 AND p.clientcode = ?2 AND p.year = ?3 \r\n"
			+ "		       AND p.maingroup = 'Closing Stock' GROUP BY p.month")
	List<Object[]> getMonthWiseSumAmountForClosingStock(Long orgId, String clientcode, String yr);

	@Query(nativeQuery = true, value = "select * from previousyearactual where orgid=?1 and clientcode=?2 and year=?3 and maingroup=?4 and subgroup=?5")
	List<PreviousYearActualVO> findStockDetails(Long org, String clientcode, String yr, String mGroup, String subGroup);

	@Query(nativeQuery = true,value ="SELECT p.month, SUM(p.amount) FROM previousyearactual p " +
		       "WHERE p.orgId = ?1 AND p.clientCode = ?2 AND p.year = ?3 " +
		       "AND p.mainGroup = 'Closing Stock' AND p.accountCode IN (?4) "+
		       "GROUP BY p.month")
	List<Object[]> getDrawingPowerMonthWiseSum(Long org, String clientcode, String yr, List<String> accountCodeList);

	@Query(nativeQuery = true,value ="SELECT p.month, SUM(p.amount) FROM previousyearactual p " +
		       "WHERE p.orgId = ?1 AND p.clientCode = ?2 AND p.year = ?3 " +
		       "AND p.mainGroup = 'Closing Stock' AND p.accountCode = '4009' " +
		       "GROUP BY p.month")
	List<Object[]> getMonthWiseSumForWIP(Long org, String clientcode, String yr);

	@Query(nativeQuery = true,value ="SELECT p.month, SUM(p.amount) FROM previousyearactual p " +
		       "WHERE p.orgId = ?1 AND p.clientCode = ?2 AND p.year = ?3 " +
		       "AND p.mainGroup = 'Closing Stock' AND p.accountCode IN (?4) "+
		       "GROUP BY p.month")
	List<Object[]> getMonthWiseSumForFinishedGoods(Long org, String clientcode, String yr,
			List<String> finishedGoodsCodes);
	
	@Query(nativeQuery = true, value = "select * from previousyearactual where orgid=?1 and clientcode=?2 and year=?3 and maingroup=?4 and subgroup=?5 and accountname=?6 ")
	List<PreviousYearActualVO> findOldDetails(Long orgId, String clientCode, String year, String mg, String mg2, String raw);

	@Modifying
    @Transactional
    @Query(value = "DELETE FROM previousyearactual " +
                   "WHERE orgid = ?1 " +
                   "AND clientcode = ?2 " +
                   "AND year = ?3 " +
                   "AND maingroup = ?4 " +
                   "AND subgroup = ?5 " +
                   "AND month = ?6", nativeQuery = true)
	void deleteByOrgIdAndClientAndYearAndMainGroupAndSubGroupAndMonth(Long orgId, String clientCode, String year,
			String mainGroup, String subGroup, String month);

	
	

}
