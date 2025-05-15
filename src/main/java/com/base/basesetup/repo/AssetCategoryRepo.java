package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.AssetCategoryVO;

@Repository
public interface AssetCategoryRepo extends JpaRepository<AssetCategoryVO, Long> {

	@Query(value = "select a.* from assetcategory a where orgid=?1  and cancel=0", nativeQuery = true)
	List<AssetCategoryVO> getByOrgId(Long orgid);

	boolean existsByCategoryAndOrgId(String category, Long orgId);

	boolean existsByCategoryCodeAndOrgId(String categoryCode, Long orgId);

	@Query(nativeQuery = true,value = "select a.category,a.categorycode from assetcategory a where a.category=?2 and a.orgid=?1  and a.active=1 and a.cancel=0")
	Set<Object[]> getAssetData(Long orgId, String assetType);

}
