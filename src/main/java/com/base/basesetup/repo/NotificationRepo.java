package com.base.basesetup.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.NotificationVO;


@Repository
public interface NotificationRepo extends JpaRepository<NotificationVO, Long>{

}

