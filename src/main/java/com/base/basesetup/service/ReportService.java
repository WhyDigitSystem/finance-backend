package com.base.basesetup.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.base.basesetup.dto.InvoiceDTO;
import com.base.basesetup.entity.InvoiceVO;
import com.base.basesetup.exception.ApplicationException;

@Service
public interface ReportService {

	Map<String, Object> createUpdateInvoice(InvoiceDTO invoiceDTO) throws ApplicationException;

	InvoiceVO getInvoiceById(Long id);

	List<InvoiceVO> getAllInvoice(Long orgId);

}
