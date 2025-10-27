package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.base.basesetup.entity.MonthlyProcessDetailsVO;
import com.base.basesetup.entity.MonthlyProcessVO;

public interface MonthlyProcessDetailsRepo extends JpaRepository<MonthlyProcessDetailsVO, Long>{

	List<MonthlyProcessDetailsVO> findByMonthlyProcessVO(MonthlyProcessVO monthlyProcessVO);
	
	
	

}
