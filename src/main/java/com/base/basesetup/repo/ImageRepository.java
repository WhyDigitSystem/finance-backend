package com.base.basesetup.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.base.basesetup.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

}

