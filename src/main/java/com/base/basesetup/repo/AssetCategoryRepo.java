package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.AssetCategoryVO;

@Repository
public interface AssetCategoryRepo extends JpaRepository<AssetCategoryVO, Long> {

	@Query(value = "select a.* from assetcategory a where orgid=?1", nativeQuery = true)
	List<AssetCategoryVO> getByOrgId(Long orgid);

	boolean existsByCategoryAndOrgId(String category, Long orgId);

	boolean existsByCategoryCodeAndOrgId(String categoryCode, Long orgId);

}
