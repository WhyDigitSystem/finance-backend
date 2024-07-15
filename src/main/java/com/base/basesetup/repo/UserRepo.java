package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.base.basesetup.entity.UserVO;

public interface UserRepo extends JpaRepository<UserVO, Long> {

	boolean existsByUserNameOrEmail(String userName, String email);

	UserVO findByUserName(String userName);

	@Query(value = "select u from UserVO u where u.id =?1")
	UserVO getUserById(Long userId);

	UserVO findByUserNameAndUserId(String userName, Long userId);

	boolean existsByUserName(String employeeCode);

	List<UserVO> findByOrgId(Long orgId);
	
	@Query(nativeQuery = true,value = "select a.companyname from company a,users b where a.companyid=b.orgid and b.userid=?1")
	Set<Object[]> getUserCompany(Long userId);

}