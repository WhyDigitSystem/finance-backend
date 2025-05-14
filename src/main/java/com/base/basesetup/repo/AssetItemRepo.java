package com.base.basesetup.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.AssetItemVO;

@Repository
public interface AssetItemRepo extends JpaRepository<AssetItemVO, Long>{

}
