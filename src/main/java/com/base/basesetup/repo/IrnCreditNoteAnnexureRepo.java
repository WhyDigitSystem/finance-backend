package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.base.basesetup.entity.IrnCreditNoteAnnexureVO;
import com.base.basesetup.entity.IrnCreditNoteVO;

public interface IrnCreditNoteAnnexureRepo extends JpaRepository<IrnCreditNoteAnnexureVO, Long>{

	List<IrnCreditNoteAnnexureVO> findByIrnCreditNoteVO(IrnCreditNoteVO irnCreditNoteVO);

}
