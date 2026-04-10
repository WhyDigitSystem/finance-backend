package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.HSNSacCodeVO;
import com.base.basesetup.entity.SacCodeVO;

@Repository
public interface SacCodeRepo extends JpaRepository<SacCodeVO, Long> {

	@Query(nativeQuery = true, name = "select * from saccode where saccodeid=?1")
	List<SacCodeVO> getAllSacCodeById(Long id);

	@Query(nativeQuery = true, name = "select * from saccode where orgid=?1")
	List<SacCodeVO> getAllSacCodeByOrgId(Long orgId);

	boolean existsByOrgIdAndServiceAccountCodeIgnoreCase(Long orgId, String serviceAccountCode);

	boolean existsByOrgIdAndSacDescriptionIgnoreCase(Long orgId, String sacDescription);

	@Query("SELECT s FROM HSNSacCodeVO s WHERE s.orgId = :orgId AND s.active = true")
	List<HSNSacCodeVO> getAllActiveSacCodeByOrgId( Long orgId);



//	@Query(nativeQuery = true,name = "select * from saccode where active=1")
//	List<SacCodeVO> findSacCodeByActive();
}
