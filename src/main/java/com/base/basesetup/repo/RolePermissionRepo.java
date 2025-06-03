package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.base.basesetup.entity.RolesPermissionHeaderVO;
import com.base.basesetup.entity.RolesPermissionVO;

public interface RolePermissionRepo extends JpaRepository<RolesPermissionVO, Long> {

	List<RolesPermissionVO> findByRolesPermissionHeaderVO(RolesPermissionHeaderVO vo);


}
