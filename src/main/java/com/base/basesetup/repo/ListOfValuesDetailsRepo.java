package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.base.basesetup.entity.ListOfValuesDetailsVO;
import com.base.basesetup.entity.ListOfValuesVO;

public interface ListOfValuesDetailsRepo extends JpaRepository<ListOfValuesDetailsVO, Long>{


	List<ListOfValuesDetailsVO> findByListOfValuesVO(ListOfValuesVO listOfValuesVO);

}
