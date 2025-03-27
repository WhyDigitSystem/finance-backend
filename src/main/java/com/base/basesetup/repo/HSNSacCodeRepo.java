package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.HSNSacCodeVO;

@Repository
public interface HSNSacCodeRepo extends JpaRepository<HSNSacCodeVO, Long> {

	@Query(value = "Select * from hsnsaccode where orgid=?1", nativeQuery = true)
	List<HSNSacCodeVO> getAllHSNSacCodeByOrgId(Long orgId);

	@Query(value = "Select * from hsnsaccode where hsnsaccodeid=?1", nativeQuery = true)
	HSNSacCodeVO getAllHSNSacCodeById(Long id);

	@Query(nativeQuery = true, value = "select * from hsnsaccode where active=1")
	List<HSNSacCodeVO> findHSNSacCodeByActive();

	boolean existsByCode(String code);


}
