package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.base.basesetup.entity.EmployeeVO;

public interface EmployeeRepo extends JpaRepository<EmployeeVO,Long>{

	@Query(value = "SELECT e.employeeCode , e.employeeName FROM EmployeeVO e WHERE e.orgId=?1")
	Set<Object[]> findAllNameAndEmployeeCodeByOrgId(Long orgId);

	@Query(nativeQuery = true,value = "select * from employee where orgid=?1")
	List<EmployeeVO> findAllEmployeeByOrgId(Long orgId);

	boolean existsByEmployeeCodeAndOrgId(String employeeCode, Long orgId);

	@Query(nativeQuery = true,value = "SELECT departmentname FROM department  WHERE orgid=?1 and active=1")
	Set<Object[]> findDepartmentNameForEmployee(Long orgId);

	@Query(nativeQuery = true,value = "SELECT designationname FROM designation  WHERE orgid=?1 and active=1")
	Set<Object[]> findDesignationNameForEmployee(Long orgId);

}
