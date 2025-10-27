package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.base.basesetup.entity.CompanyEmployeeVO;

public interface CompanyEmployeeRepo extends JpaRepository<CompanyEmployeeVO, Long>{

	//boolean existsByEmployeeCodeAndOrgId(String employeeCode, Long orgId);

	boolean existsByEmailAndOrgId(String email, Long orgId);

	boolean existsByPhoneAndOrgId(String phone, Long orgId);

//	boolean existsByWebSiteAndOrgId(String webSite, Long orgId);

	@Query(nativeQuery =true,value ="SELECT * FROM companyemployee  where orgid=?1")
	List<CompanyEmployeeVO> getAllCompanyEmployee(Long orgId);

	boolean existsByEmployeeCodeAndOrgId(String employeeCode, Long orgId);

	boolean existsByOrgIdAndEmployeeCode(Long id, String employeeCode);

}
