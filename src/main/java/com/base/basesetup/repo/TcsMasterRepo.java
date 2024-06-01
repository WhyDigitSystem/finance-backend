package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.base.basesetup.entity.TcsMasterVO;

public interface TcsMasterRepo extends JpaRepository<TcsMasterVO, Long> {

	
	@Query(value="Select * from tcsmaster where orgid=?1",nativeQuery = true)
	List<TcsMasterVO> getAllTcsMasterByOrgId(Long orgId);
 
	@Query(value="Select * from tcsmaster where tcsmasterid=?1",nativeQuery = true)
	List<TcsMasterVO> getAllTcsMasterById(Long id);

	@Query(nativeQuery = true,value = "select * from tcsmaster where active=1")
	List<TcsMasterVO> findTcsMasterByActive();

	boolean existsBySectionAndOrgId(String section, Long orgId);

	boolean existsBySectionNameAndOrgId(String sectionName, Long orgId);

	boolean existsBySectionNameAndOrgIdAndId(String sectionName, Long orgId, Long id);

	boolean existsBySectionAndOrgIdAndId(String section, Long orgId, Long id);

}
