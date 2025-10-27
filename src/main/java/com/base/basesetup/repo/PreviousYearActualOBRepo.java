package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.base.basesetup.entity.PreviousYearActualOBVO;

public interface PreviousYearActualOBRepo extends JpaRepository<PreviousYearActualOBVO, Long> {

	@Query("select a from PreviousYearActualOBVO a where a.orgId=?1 and a.clientCode=?2 and a.year=?3 and a.type=?4")
	List<PreviousYearActualOBVO> getBudgetListDetails(Long orgId, String clientCode, String year, String type);

	@Query(nativeQuery = true,value = "WITH Months AS (\r\n"
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
			+ ")\r\n"
			+ "SELECT \r\n"
			+ "    a.segment, \r\n"
			+ "    a.customername, \r\n"
			+ "    a.ordervalue,\r\n"
			+ "    a.balanceordervalue,\r\n"
			+ "    a.classification,\r\n"
			+ "    a.existingorderplan,\r\n"
			+ "    a.paymentreceived,\r\n"
			+ "    a.yettoreceive,\r\n"
			+ "    a.month, \r\n"
			+ "    COALESCE(SUM(a.amount), 0) AS totalamount\r\n"
			+ "FROM previousyearob a\r\n"
			+ "join Months m\r\n"
			+ "    ON a.month = m.month\r\n"
			+ "    AND a.orgid = ?1\r\n"
			+ "    AND a.year = ?2\r\n"
			+ "    AND a.clientcode = ?3\r\n"
			+ "    AND a.type = ?4\r\n"
			+ "GROUP BY \r\n"
			+ "    a.segment,\r\n"
			+ "    a.customername,\r\n"
			+ "    a.ordervalue,\r\n"
			+ "    a.balanceordervalue,\r\n"
			+ "    a.classification,\r\n"
			+ "    a.existingorderplan,\r\n"
			+ "    a.paymentreceived,\r\n"
			+ "    a.yettoreceive,\r\n"
			+ "    a.month,\r\n"
			+ "    FIELD(m.month, 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December', 'January', 'February', 'March')\r\n"
			+ "ORDER BY \r\n"
			+ "    FIELD(m.month, 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December', 'January', 'February', 'March')")
	Set<Object[]> getOrderBookingPYDetails(Long orgId, String year, String clientCode, String type);

	

}
