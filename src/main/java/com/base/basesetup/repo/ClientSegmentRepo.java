package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.base.basesetup.entity.ClientCompanyVO;
import com.base.basesetup.entity.ClientSegmentVO;

public interface ClientSegmentRepo extends JpaRepository<ClientSegmentVO, Long> {

	List<ClientSegmentVO> findByClientCompanyVO(ClientCompanyVO clientCompanyVO);

}
