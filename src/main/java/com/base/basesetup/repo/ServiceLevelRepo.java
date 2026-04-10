package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.ServiceLevelVO;
@Repository
public interface ServiceLevelRepo extends JpaRepository<ServiceLevelVO, Long>{


	boolean existsByLevelCodeAndOrgId(String levelCode, Long orgId);

	boolean existsByLevelNameAndOrgId(String levelName, Long orgId);

	@Query(value ="select * from servicelevel where orgid=?1",nativeQuery =true)
	List<ServiceLevelVO> getAllServiceLevel(Long orgId);
}
