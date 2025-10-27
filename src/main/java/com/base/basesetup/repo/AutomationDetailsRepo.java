package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.base.basesetup.entity.AutomationDetailsVO;
import com.base.basesetup.entity.AutomationVO;

public interface AutomationDetailsRepo extends JpaRepository<AutomationDetailsVO, Long> {

	List<AutomationDetailsVO> findByAutomationVO(AutomationVO automationVO);

}
