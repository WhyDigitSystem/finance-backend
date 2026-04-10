package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.base.basesetup.entity.SegmentMappingDetailsVO;
import com.base.basesetup.entity.SegmentMappingVO;

public interface SegmentMappingDetailsRepo extends JpaRepository<SegmentMappingDetailsVO, Long> {

	List<SegmentMappingDetailsVO> findBySegmentMappingVO(SegmentMappingVO segmentMappingVO);

}
