package com.base.basesetup.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.AiapVO;

@Repository
public interface AiapRepo extends JpaRepository<AiapVO, Long>{

}
