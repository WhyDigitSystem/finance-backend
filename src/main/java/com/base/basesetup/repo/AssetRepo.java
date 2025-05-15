package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.AssetVO;

@Repository
public interface AssetRepo extends JpaRepository<AssetVO, Long>{

	boolean existsByAssetNameAndOrgId(String assetName, Long orgId);

	boolean existsByAssetCodeIdAndOrgId(String assetCodeId, Long orgId);

	@Query(nativeQuery = true,value = "select a.* from asset a where orgid=?1")
	List<AssetVO> findByOrgId(Long orgid);

	@Query(nativeQuery = true,value="select a.asset from asset a where a.assetcode=?2 and a.orgid=?1 and active=1 and cancel=0")
	Set<Object[]> getAssetDescriptionByAsset(Long orgId, String assetCode);

}
