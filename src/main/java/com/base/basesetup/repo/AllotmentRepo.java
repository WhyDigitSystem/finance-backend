package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.AllotmentVO;

@Repository
public interface AllotmentRepo extends JpaRepository<AllotmentVO, Long>{

	@Query(nativeQuery = true, value = "select * from allotment  where orgid=?1")
	List<AllotmentVO> getAllAllotmentByOrgId(Long orgId);

}
