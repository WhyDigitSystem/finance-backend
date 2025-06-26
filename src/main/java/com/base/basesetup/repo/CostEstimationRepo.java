package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.CostEstimationVO;

@Repository
public interface CostEstimationRepo extends JpaRepository<CostEstimationVO, Long> {
	
	@Query(nativeQuery = true,value = "select * from costestimation where orgid=?1")
	List<CostEstimationVO> getAllCostEstimationByOrgId(Long orgId);
	
	@Query(nativeQuery = true, value = "select * from costestimation where costestimationid=?1")
	CostEstimationVO getAllCostEstimationById(Long id);
	
	@Query(nativeQuery = true, value = "select employee,employeecode from employee where orgid=?1 and active=1 and department=?2")
   Set<Object []> getAllEmployees(Long orgId,String department);
   
   @Query(nativeQuery = true, value = "select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
	String getCostEstimationDocId(Long orgId, String finYear, String branchCode, String screenCode);
}
