package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.KitAssetVO;
import com.base.basesetup.entity.KitVO;

@Repository
public interface KitAssetRepo extends JpaRepository<KitAssetVO, Long>{

	List<KitAssetVO> findByKitVO(KitVO kitVO);

}
