package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.IrnCreditNoteDetailsVO;
import com.base.basesetup.entity.IrnCreditNoteVO;

@Repository
public interface IrnCreditNoteDetailsRepo extends JpaRepository<IrnCreditNoteDetailsVO, Long>{

	List<IrnCreditNoteDetailsVO> findByIrnCreditNoteVO(IrnCreditNoteVO irnCreditVO);


}
