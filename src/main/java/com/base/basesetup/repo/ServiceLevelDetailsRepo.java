package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.ServiceLevelDetailsVO;
import com.base.basesetup.entity.ServiceLevelVO;
@Repository
public interface ServiceLevelDetailsRepo extends JpaRepository<ServiceLevelDetailsVO, Long>{

	List<ServiceLevelDetailsVO> findByServiceLevelVO(ServiceLevelVO serviceLevelVO);

}
