package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.IrnCreditNoteGstVO;
import com.base.basesetup.entity.IrnCreditNoteVO;
@Repository
public interface IrnCreditNoteGstRepo extends JpaRepository<IrnCreditNoteGstVO,Long>{


	List<IrnCreditNoteGstVO> findByIrnCreditNoteVO(IrnCreditNoteVO irnCreditNoteVO);

}
