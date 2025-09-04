package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.CostEstimationVO;

@Repository
public interface CostEstimationRepo extends JpaRepository<CostEstimationVO, Long> {
	
	@Query(nativeQuery = true,value = "select * from costestimation where orgid=?1 and finyear=?2 and branchcode=?3")
	List<CostEstimationVO> getAllCostEstimationByOrgId(Long orgId,String finYear, String branchCode);
	
	@Query(nativeQuery = true, value = "select * from costestimation where costestimationid=?1")
	CostEstimationVO getAllCostEstimationById(Long id);
	
	@Query(nativeQuery = true, value = "select employee,employeecode from employee where orgid=?1 and active=1")
   Set<Object []> getAllEmployees(Long orgId);
   
   @Query(nativeQuery = true, value = "select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
	String getCostEstimationDocId(Long orgId, String finYear, String branchCode, String screenCode);

CostEstimationVO findByOrgIdAndIdAndDocId(Long orgId, Long id, String docId);


@Query(nativeQuery = true, value = "SELECT \r\n"
		+ "    a.finyear,\r\n"
		+ "    a.docid,\r\n"
		+ "    a.docdate,\r\n"
		+ "    a.employeename,\r\n"
		+ "    a.employeecode,\r\n"
		+ "    a.fromdate,\r\n"
		+ "    a.todate,\r\n"
		+ "    a.approvalremarks,\r\n"
		+ "    a.totalamount,\r\n"
		+ "    b.particulars,\r\n"
		+ "    b.category,\r\n"
		+ "    b.remarks,\r\n"
		+ "    b.amount,\r\n"
		+ "    b.image,\r\n"
		+ "    CASE \r\n"
		+ "        WHEN a.approvestatus IS NULL THEN 'Not Approved'\r\n"
		+ "        ELSE a.approvestatus\r\n"
		+ "    END AS approvestatus\r\n"
		+ "FROM costestimation a\r\n"
		+ "INNER JOIN costestimationdetails b \r\n"
		+ "        ON a.costestimationid = b.costestimationid\r\n"
		+ "WHERE a.orgid = ?1\r\n"
		+ "  AND a.finyear = ?2\r\n"
		+ "  AND (a.employeename = ?3 OR ?3 = 'ALL')\r\n"
		+ "  AND (?4 IS NULL OR a.docdate >= ?4)\r\n"
		+ "  AND (?5 IS NULL OR a.docdate <= ?5)\r\n"
		+ "  AND (a.branchcode = ?6 OR ?6 = 'ALL')\r\n"
		+ "  AND (b.category = ?7 OR ?7 = 'ALL')\r\n"
		+ "ORDER BY a.createdon DESC")
Set<Object[]> getCostEstimationDetails(Long orgId, String finYear, String employeeName, String fromDate, String toDate,String branchCode,String category);

@Query(nativeQuery = true, value = "SELECT \r\n"
		+ "    a.finyear,\r\n"
		+ "    a.docid,\r\n"
		+ "    a.docdate,\r\n"
		+ "    a.employeename,\r\n"
		+ "    a.employeecode,\r\n"
		+ "    a.fromdate,\r\n"
		+ "    a.todate,\r\n"
		+ "    a.approvalremarks,\r\n"
		+ "    a.totalamount,\r\n"
		+ "    CASE \r\n"
		+ "        WHEN a.approvestatus IS NULL THEN 'Not Approved'\r\n"
		+ "        ELSE a.approvestatus\r\n"
		+ "    END AS approvestatus\r\n"
		+ "FROM costestimation a\r\n"
		+ "JOIN costestimationdetails b \r\n"
		+ "     ON a.costestimationid = b.costestimationid\r\n"
		+ "WHERE a.orgid = ?1\r\n"
		+ "  AND a.finyear = ?2\r\n"
		+ "  AND (a.employeename = ?3 OR ?3 = 'ALL')\r\n"
		+ "  AND (?4 IS NULL OR a.docdate >= ?4)\r\n"
		+ "  AND (?5 IS NULL OR a.docdate <= ?5)\r\n"
		+ "  AND (a.branchcode = ?6 OR ?6 = 'ALL')\r\n"
		+ "  AND (b.category = ?7 OR ?7 = 'ALL')\r\n"
		+ "ORDER BY a.createdon DESC")
Set<Object[]> getCostEstimationSummary(Long orgId, String finYear, String employeeName, String fromDate, String toDate,String branchCode,String category);
}
