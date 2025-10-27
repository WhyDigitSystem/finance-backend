package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.base.basesetup.entity.ClientCompanyReportAccessVO;
import com.base.basesetup.entity.ClientCompanyVO;

public interface ClientCompanyReportAccessRepo extends JpaRepository<ClientCompanyReportAccessVO, Long> {

	List<ClientCompanyReportAccessVO> findByClientCompanyVO(ClientCompanyVO clientCompanyVO);

}
