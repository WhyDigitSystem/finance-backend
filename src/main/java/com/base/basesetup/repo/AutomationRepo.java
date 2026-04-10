package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.base.basesetup.entity.AutomationVO;

public interface AutomationRepo extends JpaRepository<AutomationVO, Long> {

	List<AutomationVO> findByOrgId(Long orgId);

}
