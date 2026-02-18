package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.base.basesetup.entity.ClientCompanyVO;
import com.base.basesetup.entity.ClientUnitVO;

public interface ClientUnitRepo extends JpaRepository<ClientUnitVO, Long> {

	List<ClientUnitVO> findByClientCompanyVO(ClientCompanyVO clientCompanyVO);

}
