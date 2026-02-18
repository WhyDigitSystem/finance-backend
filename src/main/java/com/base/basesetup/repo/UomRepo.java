package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.UomVO;

@Repository
public interface UomRepo extends JpaRepository<UomVO, Long>{

	@Query(nativeQuery = true, value = "select * from uommast where orgid=?1")
	List<UomVO> getUomByOrgId(Long orgId);

	@Query(nativeQuery = true, value = "select * from uommast where uommastid=?1")
	List<UomVO> getUomById(Long id);

	//boolean existsByUomCodeAndOrgId(String uomCode, Long orgId);


	boolean existsByUomCodeAndOrgId(String uomCode, Long orgId);
}
