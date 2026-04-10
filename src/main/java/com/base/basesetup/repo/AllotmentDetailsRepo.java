package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.AllotmentDetailsVO;
import com.base.basesetup.entity.AllotmentVO;

@Repository
public interface AllotmentDetailsRepo extends JpaRepository<AllotmentDetailsVO, Long>{


	List<AllotmentDetailsVO> findByAllotmentVO(AllotmentVO allotmentVO);

}
