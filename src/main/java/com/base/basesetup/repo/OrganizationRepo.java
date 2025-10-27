package com.base.basesetup.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.OrganizationVO;

@Repository
public interface OrganizationRepo extends JpaRepository<OrganizationVO, Long>{

}
