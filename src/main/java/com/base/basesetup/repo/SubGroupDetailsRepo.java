package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.base.basesetup.entity.GroupMappingVO;
import com.base.basesetup.entity.SubGroupDetailsVO;

public interface SubGroupDetailsRepo  extends JpaRepository<SubGroupDetailsVO, Long>{

	List<SubGroupDetailsVO> findByGroupMappingVO(GroupMappingVO groupMappingVO);

}
