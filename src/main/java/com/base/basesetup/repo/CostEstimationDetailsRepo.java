package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.CostEstimationDetailsVO;
import com.base.basesetup.entity.CostEstimationVO;

@Repository
public interface CostEstimationDetailsRepo extends JpaRepository<CostEstimationDetailsVO, Long> {

	List<CostEstimationDetailsVO> findByCostEstimationVO(CostEstimationVO costEstimationVO);

}
