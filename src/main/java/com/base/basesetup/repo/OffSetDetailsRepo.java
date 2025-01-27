package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.base.basesetup.entity.OffSetDetailsVO;
import com.base.basesetup.entity.OffSetVO;

public interface OffSetDetailsRepo extends JpaRepository<OffSetDetailsVO, Long> {

	List<OffSetDetailsVO> findByOffSetVO(OffSetVO offSetVO);

}
