package com.base.basesetup.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.AssetVO;

@Repository
public interface AssetRepo extends JpaRepository<AssetVO, Long>{

	boolean existsByAssetNameAndOrgId(String assetName, Long orgId);

	boolean existsByAssetCodeIdAndOrgId(String assetCodeId, Long orgId);

}
