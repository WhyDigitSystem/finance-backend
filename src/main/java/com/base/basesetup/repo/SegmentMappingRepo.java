package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.base.basesetup.entity.SegmentMappingVO;

public interface SegmentMappingRepo extends JpaRepository<SegmentMappingVO, Long> {

	List<SegmentMappingVO> findByOrgId(Long orgId);
	
	
	@Query(nativeQuery = true, value = "select b.value from segmentmapping a, segmentmappingdetails b where a.segmentmappingid=b.segmentmappingid   and a.orgid=?1 and a.active=1 and b.active=1\r\n"
			+ "group by b.value")
	Set<Object[]>getSegmentDetailsByClient(Long orgId);


}
