package com.base.basesetup.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.basesetup.dto.InvoiceDTO;
import com.base.basesetup.dto.InvoiceProductLinesDTO;
import com.base.basesetup.entity.InvoiceProductLinesVO;
import com.base.basesetup.entity.InvoiceVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.InvoiceProductLinesRepo;
import com.base.basesetup.repo.InvoiceRepo;

@Service
public class ReportServiceImpl implements ReportService{

	public static final Logger LOGGER = LoggerFactory.getLogger(ReportService.class);
	
	@Autowired
	InvoiceRepo invoiceRepo;
	
	@Autowired
	InvoiceProductLinesRepo invoiceProductLinesRepo;
	
	// Invoice
		@Override
		public Map<String, Object> createUpdateInvoice(InvoiceDTO invoiceDTO) throws ApplicationException {
			InvoiceVO invoiceVO = new InvoiceVO();
			String message;
			if (ObjectUtils.isEmpty(invoiceDTO.getId())) {
				List<InvoiceProductLinesVO> invoiceProductLinesVO = new ArrayList<>();
				if (invoiceDTO.getItems() != null) {
					for (InvoiceProductLinesDTO invoiceProductLinesDTO : invoiceDTO.getItems()) {
						InvoiceProductLinesVO invoiceProductLinesVO1 = new InvoiceProductLinesVO();
						invoiceProductLinesVO1.setDescription(invoiceProductLinesDTO.getDescription());
						invoiceProductLinesVO1.setQuantity(invoiceProductLinesDTO.getQuantity());
						invoiceProductLinesVO1.setRate(invoiceProductLinesDTO.getRate());
						invoiceProductLinesVO1.setAmount(invoiceProductLinesDTO.getAmount());
						invoiceProductLinesVO1.setInvoiceVO(invoiceVO);
						invoiceProductLinesVO.add(invoiceProductLinesVO1);
					}
				}
				if (invoiceRepo.existsByOrgIdAndPoNumber(invoiceDTO.getOrgId(), invoiceDTO.getPoNumber())) {
					throw new ApplicationException("Po No already Exists");
				}
				invoiceVO.setPoNumber(invoiceDTO.getPoNumber());
				invoiceVO.setProductLines(invoiceProductLinesVO);
				invoiceVO.setCreatedBy(invoiceDTO.getCreatedBy());
				invoiceVO.setModifiedBy(invoiceDTO.getCreatedBy());
//				String base64Image = invoiceDTO.getLogo();
//				if (base64Image != null && base64Image.startsWith("data:image/")) {
//					base64Image = base64Image.substring(base64Image.indexOf(",") + 1);
//					byte[] imageBytes = Base64.getDecoder().decode(base64Image);
//					invoiceVO.setLogo(imageBytes);
//				}
				mapInvoiceDTOToInvoiceVO(invoiceDTO, invoiceVO);

				message = "Invoice Created successfully";
			} else {

				invoiceVO = invoiceRepo.findById(invoiceDTO.getId()).get();
				if (!invoiceVO.getPoNumber().equals(invoiceDTO.getPoNumber())) {
					if (invoiceRepo.existsByOrgIdAndPoNumber(invoiceDTO.getOrgId(), invoiceDTO.getPoNumber())) {
						throw new ApplicationException("Po No already Exists");
					}
					invoiceVO.setPoNumber(invoiceDTO.getPoNumber());
				}
				List<InvoiceProductLinesVO> invoiceProductLinesVO2 = invoiceProductLinesRepo.findByInvoiceVO(invoiceVO);
				invoiceProductLinesRepo.deleteAll(invoiceProductLinesVO2);

				List<InvoiceProductLinesVO> invoiceProductLinesVO = new ArrayList<>();
				if (invoiceDTO.getItems() != null) {
					for (InvoiceProductLinesDTO invoiceProductLinesDTO : invoiceDTO.getItems()) {
						InvoiceProductLinesVO invoiceProductLinesVO1 = new InvoiceProductLinesVO();
						invoiceProductLinesVO1.setDescription(invoiceProductLinesDTO.getDescription());
						invoiceProductLinesVO1.setQuantity(invoiceProductLinesDTO.getQuantity());
						invoiceProductLinesVO1.setRate(invoiceProductLinesDTO.getRate());
						invoiceProductLinesVO1.setAmount(invoiceProductLinesDTO.getAmount());
						invoiceProductLinesVO1.setInvoiceVO(invoiceVO);
						invoiceProductLinesVO.add(invoiceProductLinesVO1);
					}
				}
				invoiceVO.setModifiedBy(invoiceDTO.getCreatedBy());
				invoiceVO.setProductLines(invoiceProductLinesVO);
				mapInvoiceDTOToInvoiceVO(invoiceDTO, invoiceVO);
//				String base64Image = invoiceDTO.getLogo();
//				if (base64Image != null && base64Image.startsWith("data:image/")) {
//					base64Image = base64Image.substring(base64Image.indexOf(",") + 1);
//					byte[] imageBytes = Base64.getDecoder().decode(base64Image);
//					invoiceVO.setLogo(imageBytes);
//				}
				message = "Invoice Updated successfully";

			}
			String pono = invoiceDTO.getPoNumber();
			if (pono == null) {
				throw new ApplicationException("Field cannot be Empty");
			}
			invoiceRepo.save(invoiceVO);
			Map<String, Object> response = new HashMap<>();
			response.put("invoiceVO", invoiceVO);
			response.put("message", message);
			return response;
		}

		private void mapInvoiceDTOToInvoiceVO(InvoiceDTO invoiceDTO, InvoiceVO invoiceVO) {

			invoiceVO.setPoDate(invoiceDTO.getPoDate());
			invoiceVO.setPoNumber(invoiceDTO.getPoNumber());
			invoiceVO.setCompanyAddress(invoiceDTO.getCompanyAddress());
			invoiceVO.setVendorAddress(invoiceDTO.getVendorAddress());
			invoiceVO.setDeliveryAddress(invoiceDTO.getDeliveryAddress());
			invoiceVO.setTermsAndConditions(invoiceDTO.getTermsAndConditions());
			invoiceVO.setSubtotal(invoiceDTO.getSubtotal());
			invoiceVO.setSgst(invoiceDTO.getSgst());
			invoiceVO.setCgst(invoiceDTO.getCgst());
			invoiceVO.setTotal(invoiceDTO.getTotal());
			invoiceVO.setGstType(invoiceDTO.getGstType());
			invoiceVO.setIgst(invoiceDTO.getIgst());

			invoiceVO.setOrgId(invoiceDTO.getOrgId());
		}

		@Override
		public List<InvoiceVO> getAllInvoice(Long orgId) {

			return invoiceRepo.findAllByOrgId(orgId);
		}

		@Override
		public InvoiceVO getInvoiceById(Long id) {
			// TODO Auto-generated method stub
			return invoiceRepo.findById(id).get();
		}
	
	
	
}
