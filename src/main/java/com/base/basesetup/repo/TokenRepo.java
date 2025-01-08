package com.base.basesetup.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.TokenVO;

@Repository
public interface TokenRepo extends JpaRepository<TokenVO, String>{

}
