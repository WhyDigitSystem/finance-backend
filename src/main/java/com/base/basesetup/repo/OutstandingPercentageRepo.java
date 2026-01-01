package com.base.basesetup.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.base.basesetup.entity.OutstandingPercentageVO;

public interface OutstandingPercentageRepo extends JpaRepository<OutstandingPercentageVO, Long> {

}
