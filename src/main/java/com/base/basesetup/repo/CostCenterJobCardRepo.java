package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.CostCenterJobCardVO;
import com.base.basesetup.entity.JobCardVO;
@Repository
public interface CostCenterJobCardRepo extends JpaRepository<CostCenterJobCardVO, Long> {

	List<CostCenterJobCardVO> findByJobCardVO(JobCardVO jobCardVO);

}
