package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.KitVO;

@Repository
public interface KitRepo extends JpaRepository<KitVO, Long>{

	boolean existsByKitNoAndOrgId(String kitNo, Long orgId);

	boolean existsByKitDescAndOrgId(String kitDesc, Long orgId);

	@Query(nativeQuery =true,value = "select a.* from kit a where a.orgid=?1")
	List<KitVO> findAllKit(Long orgid);


}
