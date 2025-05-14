package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.AssetTypeVO;

@Repository
public interface AssetTypeRepo extends JpaRepository<AssetTypeVO, Long> {

	boolean existsByAssetType(String assetType);

	boolean existsByTypeCode(String typeCode);

	@Query(value = "select a.* from assettype a where a.orgid=?1 ", nativeQuery = true)
	List<AssetTypeVO> findByOrgId(Long orgid);

}
