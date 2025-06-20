package com.base.basesetup.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.basesetup.dto.InvoiceDTO;
import com.base.basesetup.dto.InvoiceProductLinesDTO;
import com.base.basesetup.dto.IssueManifestProviderDTO;
import com.base.basesetup.dto.IssueManifestProviderDetailsDTO;
import com.base.basesetup.dto.QuotationDTO;
import com.base.basesetup.dto.QuotationDetailsDTO;
import com.base.basesetup.dto.RetrievalManifestProviderDTO;
import com.base.basesetup.dto.RetrievalManifestProviderDetailsDTO;
import com.base.basesetup.entity.DeclarationAndNotesVO;
import com.base.basesetup.entity.InvoiceProductLinesVO;
import com.base.basesetup.entity.InvoiceVO;
import com.base.basesetup.entity.IssueManifestProviderDetailsVO;
import com.base.basesetup.entity.IssueManifestProviderVO;
import com.base.basesetup.entity.QuotationDetailsVO;
import com.base.basesetup.entity.QuotationVO;
import com.base.basesetup.entity.RetrievalManifestProviderDetailsVO;
import com.base.basesetup.entity.RetrievalManifestProviderVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.InvoiceProductLinesRepo;
import com.base.basesetup.repo.InvoiceRepo;
import com.base.basesetup.repo.IssueManifestProviderDetailsRepo;
import com.base.basesetup.repo.IssueManifestProviderRepo;
import com.base.basesetup.repo.QuotationDetailsRepo;
import com.base.basesetup.repo.QuotationRepo;
import com.base.basesetup.repo.ReceiptRepo;
import com.base.basesetup.repo.RetrievalManifestProviderDetailsRepo;
import com.base.basesetup.repo.RetrievalManifestProviderRepo;
import com.base.basesetup.repo.TaxInvoiceRepo;

@Service
public class ReportServiceImpl implements ReportService {

	public static final Logger LOGGER = LoggerFactory.getLogger(ReportService.class);

	@Autowired
	InvoiceRepo invoiceRepo;

	@Autowired
	TaxInvoiceRepo taxInvoiceRepo;

	@Autowired
	InvoiceProductLinesRepo invoiceProductLinesRepo;

	@Autowired
	IssueManifestProviderRepo issueManifestProviderRepo;

	@Autowired
	IssueManifestProviderDetailsRepo issueManifestProviderDetailsRepo;

	@Autowired
	RetrievalManifestProviderRepo retrievalManifestProviderRepo;

	@Autowired
	RetrievalManifestProviderDetailsRepo retrievalManifestProviderDetailsRepo;

	@Autowired
	declarationAndNotesRepo declarationAndNotesRepo;

	@Autowired
	ReceiptRepo receiptRepo;

	@Autowired
	QuotationRepo quotationRepo;

	@Autowired
	QuotationDetailsRepo quotationDetailsRepo;

	// Invoice
	@Override
	public Map<String, Object> createUpdateInvoice(InvoiceDTO invoiceDTO) throws ApplicationException {
		InvoiceVO invoiceVO = new InvoiceVO();
		String message = null;

		if (ObjectUtils.isEmpty(invoiceDTO.getId())) {
			if (invoiceRepo.existsByOrgIdAndPoNumber(invoiceDTO.getOrgId(), invoiceDTO.getPoNumber())) {
				String errorMessage = String.format("The PoNumber: %s already exists This Organization.",
						invoiceDTO.getPoNumber());
				throw new ApplicationException(errorMessage);
			}
			invoiceVO = new InvoiceVO();
			invoiceVO.setCreatedBy(invoiceDTO.getCreatedBy());
			invoiceVO.setModifiedBy(invoiceDTO.getCreatedBy());
			message = "Invoice Creation SuccessFully";
		} else {

			invoiceVO = invoiceRepo.findById(invoiceDTO.getId())
					.orElseThrow(() -> new ApplicationException("Invoice not found with id: " + invoiceDTO.getId()));
			invoiceVO.setModifiedBy(invoiceDTO.getCreatedBy());
			if (!invoiceVO.getPoNumber().equalsIgnoreCase(invoiceDTO.getPoNumber())) {
				if (invoiceRepo.existsByOrgIdAndPoNumber(invoiceDTO.getOrgId(), invoiceDTO.getPoNumber())) {
					String errorMessage = String.format("The PoNumber: %s already exists This Organization.",
							invoiceDTO.getPoNumber());
					throw new ApplicationException(errorMessage);
				}
				invoiceVO.setPoNumber(invoiceDTO.getPoNumber().toUpperCase());
			}
			message = "Invoice Update Successfully";
		}

		mapInvoiceDTOToInvoiceVO(invoiceDTO, invoiceVO);
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
		invoiceVO.setVendorName(invoiceDTO.getVendorName());
		invoiceVO.setGstIn(invoiceDTO.getGstIn());
		invoiceVO.setTermsAndConditions(invoiceDTO.getTermsAndConditions());
		invoiceVO.setFinYear(invoiceDTO.getFinYear());
		invoiceVO.setRemarks(invoiceDTO.getRemarks());
		invoiceVO.setOrgId(invoiceDTO.getOrgId());

		if (ObjectUtils.isNotEmpty(invoiceDTO.getId())) {
			List<InvoiceProductLinesVO> invoiceProductLinesVO2 = invoiceProductLinesRepo.findByInvoiceVO(invoiceVO);
			invoiceProductLinesRepo.deleteAll(invoiceProductLinesVO2);
		}

		BigDecimal subToatl = BigDecimal.ZERO;
		BigDecimal taxAmount = BigDecimal.ZERO;
		BigDecimal totalTaxAmount = BigDecimal.ZERO;
		List<InvoiceProductLinesVO> invoiceProductLinesVOs = new ArrayList<>();
		if (invoiceDTO.getItems() != null) {
			for (InvoiceProductLinesDTO invoiceProductLinesDTO : invoiceDTO.getItems()) {
				InvoiceProductLinesVO invoiceProductLinesVO1 = new InvoiceProductLinesVO();
				invoiceProductLinesVO1.setDescription(invoiceProductLinesDTO.getDescription());
				invoiceProductLinesVO1.setQuantity(invoiceProductLinesDTO.getQuantity());
				invoiceProductLinesVO1.setRate(invoiceProductLinesDTO.getRate());
				invoiceProductLinesVO1.setTax(invoiceProductLinesDTO.getTax());				
				invoiceProductLinesVO1.setAmount(invoiceProductLinesDTO.getQuantity().multiply(invoiceProductLinesDTO.getRate()));
				taxAmount = invoiceProductLinesDTO.getTax().multiply(invoiceProductLinesVO1.getAmount())
						.divide(BigDecimal.valueOf(100));
				invoiceProductLinesVO1.setTaxValue(taxAmount);
				subToatl = subToatl.add(invoiceProductLinesVO1.getAmount());
				totalTaxAmount=totalTaxAmount.add(invoiceProductLinesVO1.getTaxValue());
				invoiceProductLinesVO1.setInvoiceVO(invoiceVO);
				invoiceProductLinesVOs.add(invoiceProductLinesVO1);
			}
		}

		invoiceVO.setSubTotal(subToatl);
		invoiceVO.setTotalTaxAmount(totalTaxAmount);
		BigDecimal totalAmount=subToatl.add(totalTaxAmount);
		invoiceVO.setTotal(totalAmount);
				
		invoiceVO.setProductLines(invoiceProductLinesVOs);

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

	// Issue Manifest

	@Override
	public Map<String, Object> createUpdateIssuemanifest(IssueManifestProviderDTO issueManifestProviderDTO)
			throws ApplicationException {
//		IssueManifestProviderVO issueManifestProviderVO = new IssueManifestProviderVO();
//		String message = null;
//		if (issueManifestProviderDTO.getId() != null) {
//			// Update existing entity
//			issueManifestProviderVO = issueManifestProviderRepo.findById(issueManifestProviderDTO.getId())
//					.orElseThrow(() -> new ApplicationException(
//							"This Id Not Found Any Information, Invalid Id: " + issueManifestProviderDTO.getId()));
//			issueManifestProviderVO.setUpdatedBy(issueManifestProviderDTO.getCreatedBy());
//			if (!issueManifestProviderVO.getTransactionNo().equals(issueManifestProviderDTO.getTransactionNo())) {
//				if (issueManifestProviderRepo.existsByOrgIdAndTransactionNo(issueManifestProviderDTO.getOrgId(),
//						issueManifestProviderDTO.getTransactionNo())) {
//					throw new ApplicationException("TransactionNo already Exists");
//				}
//				issueManifestProviderVO.setTransactionNo(issueManifestProviderDTO.getTransactionNo());
//
//			}
//			message = "IssueManifestProvider Updation Sucessfully";
//
//		} else {
//
//			issueManifestProviderVO = new IssueManifestProviderVO();
//			issueManifestProviderVO.setCreatedBy(issueManifestProviderDTO.getCreatedBy());
//			issueManifestProviderVO.setUpdatedBy(issueManifestProviderDTO.getCreatedBy());
//			if (issueManifestProviderRepo.existsByOrgIdAndTransactionNo(issueManifestProviderDTO.getOrgId(),
//					issueManifestProviderDTO.getTransactionNo())) {
//				throw new ApplicationException("TransactionNo already Exists");
//			}
//			issueManifestProviderVO.setTransactionNo(issueManifestProviderDTO.getTransactionNo());
//			message = "IssueManifestProvider Creatrion Sucessfully";
//		}
//		getIssueManifestProviderVOFromIssueManifestProviderDTO(issueManifestProviderVO, issueManifestProviderDTO);
//		issueManifestProviderRepo.save(issueManifestProviderVO);
//
//		// Prepare the response
//		Map<String, Object> response = new HashMap<>();
//		response.put("message", message);
//		response.put("issueManifestProviderVO", issueManifestProviderVO);
//		return response;

		IssueManifestProviderVO issueManifestProviderVO = new IssueManifestProviderVO();
		String message = null;
		if (ObjectUtils.isEmpty(issueManifestProviderDTO.getId())) {
			if (issueManifestProviderRepo.existsByOrgIdAndTransactionNo(issueManifestProviderDTO.getOrgId(),
					issueManifestProviderDTO.getTransactionNo())) {
				String errorMessage = String.format("The TransactionNo: %s already exists This Organization.",
						issueManifestProviderDTO.getTransactionNo());
				throw new ApplicationException(errorMessage);
			}
			issueManifestProviderVO = new IssueManifestProviderVO();
			issueManifestProviderVO.setCreatedBy(issueManifestProviderDTO.getCreatedBy());
			issueManifestProviderVO.setUpdatedBy(issueManifestProviderDTO.getCreatedBy());
			message = "IssueManifestProvider Creation SuccessFully";
		} else {

			issueManifestProviderVO = issueManifestProviderRepo.findById(issueManifestProviderDTO.getId())
					.orElseThrow(() -> new ApplicationException(
							"IssueManifestProvider not found with id: " + issueManifestProviderDTO.getId()));
			issueManifestProviderVO.setUpdatedBy(issueManifestProviderDTO.getCreatedBy());
			if (!issueManifestProviderVO.getTransactionNo().equals(issueManifestProviderDTO.getTransactionNo())) {
				if (issueManifestProviderRepo.existsByOrgIdAndTransactionNo(issueManifestProviderDTO.getOrgId(),
						issueManifestProviderDTO.getTransactionNo())) {
					String errorMessage = String.format("The TransactionNo: %s already exists This Organization.",
							issueManifestProviderDTO.getTransactionNo());
					throw new ApplicationException(errorMessage);
				}
				issueManifestProviderVO.setTransactionNo(issueManifestProviderDTO.getTransactionNo());
			}
			message = "IssueManifestProvider Update Successfully";
		}

		getIssueManifestProviderVOFromIssueManifestProviderDTO(issueManifestProviderVO, issueManifestProviderDTO);
		issueManifestProviderRepo.save(issueManifestProviderVO);
		Map<String, Object> response = new HashMap<>();
		response.put("issueManifestProviderVO", issueManifestProviderVO);
		response.put("message", message);
		return response;

	}

	private IssueManifestProviderVO getIssueManifestProviderVOFromIssueManifestProviderDTO(
			IssueManifestProviderVO issueManifestProviderVO, IssueManifestProviderDTO issueManifestProviderDTO)
			throws ApplicationException {
		issueManifestProviderVO.setTransactionDate(issueManifestProviderDTO.getTransactionDate());
		issueManifestProviderVO.setDispatchDate(issueManifestProviderDTO.getDispatchDate());
		issueManifestProviderVO.setTransactionType(issueManifestProviderDTO.getTransactionType());
		issueManifestProviderVO.setFromWarehouse(issueManifestProviderDTO.getFromWarehouse());
		issueManifestProviderVO.setWarehouseAddress(issueManifestProviderDTO.getWarehouseAddress());
		issueManifestProviderVO.setSender(issueManifestProviderDTO.getSender());
		issueManifestProviderVO.setSenderAddress(issueManifestProviderDTO.getSenderAddress());
		issueManifestProviderVO.setReceiver(issueManifestProviderDTO.getReceiver());
		issueManifestProviderVO.setReceiverAddress(issueManifestProviderDTO.getReceiverAddress());
		issueManifestProviderVO.setReceiverName(issueManifestProviderDTO.getReceiverName());
		issueManifestProviderVO.setReceiverGst(issueManifestProviderDTO.getReceiverGst());
		issueManifestProviderVO.setAmountInWords(issueManifestProviderDTO.getAmountInWords());
		issueManifestProviderVO.setAmount(issueManifestProviderDTO.getAmount());
		issueManifestProviderVO.setTransporterName(issueManifestProviderDTO.getTransporterName());
		issueManifestProviderVO.setVehicleNo(issueManifestProviderDTO.getVehicleNo());
		issueManifestProviderVO.setDriverPhoneNo(issueManifestProviderDTO.getDriverPhoneNo());
		issueManifestProviderVO.setLocationUnit(issueManifestProviderDTO.getLocationUnit());
		issueManifestProviderVO.setTransactionNo(issueManifestProviderDTO.getTransactionNo());
		issueManifestProviderVO.setActive(issueManifestProviderDTO.isActive());
		issueManifestProviderVO.setCancel(issueManifestProviderDTO.isCancel());
		issueManifestProviderVO.setOrgId(issueManifestProviderDTO.getOrgId());
		issueManifestProviderVO.setFinYear(issueManifestProviderDTO.getFinYear());

		if (ObjectUtils.isNotEmpty(issueManifestProviderDTO.getId())) {

			List<IssueManifestProviderDetailsVO> issueManifestProviderDetailsVOs = issueManifestProviderDetailsRepo
					.findByIssueManifestProviderVO(issueManifestProviderVO);
			issueManifestProviderDetailsRepo.deleteAll(issueManifestProviderDetailsVOs);
		}

		List<IssueManifestProviderDetailsVO> detailsVOs = new ArrayList<>();

		for (IssueManifestProviderDetailsDTO detailsDTO : issueManifestProviderDTO
				.getIssueManifestProviderDetailsDTO()) {

			IssueManifestProviderDetailsVO issueManifestProviderDetailsVO = new IssueManifestProviderDetailsVO();

			issueManifestProviderDetailsVO.setAsset(detailsDTO.getAsset());
			issueManifestProviderDetailsVO.setAssetCode(detailsDTO.getAssetCode());
			issueManifestProviderDetailsVO.setAssetQty(detailsDTO.getAssetQty());
			issueManifestProviderDetailsVO.setActualQty(detailsDTO.getActualQty());
			issueManifestProviderDetailsVO.setShortTageQty(detailsDTO.getAssetQty() - detailsDTO.getActualQty());
			issueManifestProviderDetailsVO.setKitId(detailsDTO.getKitId());
			issueManifestProviderDetailsVO.setKitName(detailsDTO.getKitName());
			issueManifestProviderDetailsVO.setKitQty(detailsDTO.getKitQty());
			issueManifestProviderDetailsVO.setHsnCode(detailsDTO.getHsnCode());
			issueManifestProviderDetailsVO.setIssueManifestProviderVO(issueManifestProviderVO);
			detailsVOs.add(issueManifestProviderDetailsVO);

		}
		issueManifestProviderVO.setIssueManifestProviderDetailsVOs(detailsVOs);
		return issueManifestProviderVO;

	}

	@Override
	public List<IssueManifestProviderVO> getAllIssueManifestProvider(Long orgId, Long finYear) {

		return issueManifestProviderRepo.getAllIssueManifestProvider(orgId, finYear);
	}

	@Override
	public List<IssueManifestProviderVO> getAllIssueManifestProviderForPendingIssueRequest(Long orgId) {

		return issueManifestProviderRepo.findAllIssueManifeasrProvider(orgId);
	}

	@Override
	public Optional<IssueManifestProviderVO> getAllIssueManifestProviderById(Long id) {

		return issueManifestProviderRepo.findById(id);
	}

	@Override
	public Map<String, Object> createUpdateRetrievalManifest(RetrievalManifestProviderDTO retrievalManifestProviderDTO)
			throws ApplicationException {
		RetrievalManifestProviderVO retrievalManifestProviderVO = null;
		String message = null;
		if (retrievalManifestProviderDTO.getId() != null) {
			// Update existing entity
			retrievalManifestProviderVO = retrievalManifestProviderRepo.findById(retrievalManifestProviderDTO.getId())
					.orElseThrow(() -> new ApplicationException(
							"This Id Not Found Any Information, Invalid Id: " + retrievalManifestProviderDTO.getId()));
			retrievalManifestProviderVO.setUpdatedBy(retrievalManifestProviderDTO.getCreatedBy());

			if (!retrievalManifestProviderVO.getTransactionNo()
					.equals(retrievalManifestProviderDTO.getTransactionNo())) {
				if (retrievalManifestProviderRepo.existsByOrgIdAndTransactionNo(retrievalManifestProviderDTO.getOrgId(),
						retrievalManifestProviderDTO.getTransactionNo())) {
					throw new ApplicationException("TransactionNo already Exists");
				}
				retrievalManifestProviderVO.setTransactionNo(retrievalManifestProviderDTO.getTransactionNo());

			}
			message = "IssueManifestProvider Updation Sucessfully";

		} else {

			retrievalManifestProviderVO = new RetrievalManifestProviderVO();
			retrievalManifestProviderVO.setCreatedBy(retrievalManifestProviderDTO.getCreatedBy());
			retrievalManifestProviderVO.setUpdatedBy(retrievalManifestProviderDTO.getCreatedBy());
			if (retrievalManifestProviderRepo.existsByOrgIdAndTransactionNo(retrievalManifestProviderDTO.getOrgId(),
					retrievalManifestProviderDTO.getTransactionNo())) {
				throw new ApplicationException("TransactionNo already Exists");
			}
			retrievalManifestProviderVO.setTransactionNo(retrievalManifestProviderDTO.getTransactionNo());
			message = "IssueManifestProvider Creatrion Sucessfully";
		}
		getRetrievalManifestProviderVOFromRetrievalManifestProviderDTO(retrievalManifestProviderVO,
				retrievalManifestProviderDTO);
		retrievalManifestProviderRepo.save(retrievalManifestProviderVO);

		// Prepare the response
		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("retrievalManifestProviderVO", retrievalManifestProviderVO);
		return response;
	}

	private RetrievalManifestProviderVO getRetrievalManifestProviderVOFromRetrievalManifestProviderDTO(
			RetrievalManifestProviderVO retrievalManifestProviderVO,
			RetrievalManifestProviderDTO retrievalManifestProviderDTO) throws ApplicationException {

		retrievalManifestProviderVO.setTransactionDate(retrievalManifestProviderDTO.getTransactionDate());
		retrievalManifestProviderVO.setDispatchDate(retrievalManifestProviderDTO.getDispatchDate());
		retrievalManifestProviderVO.setTransactionType(retrievalManifestProviderDTO.getTransactionType());
		retrievalManifestProviderVO.setSender(retrievalManifestProviderDTO.getSender());
		retrievalManifestProviderVO.setSenderAddress(retrievalManifestProviderDTO.getSenderAddress());
		retrievalManifestProviderVO.setReceiver(retrievalManifestProviderDTO.getReceiver());
		retrievalManifestProviderVO.setReceiverAddress(retrievalManifestProviderDTO.getReceiverAddress());
		retrievalManifestProviderVO.setSenderGst(retrievalManifestProviderDTO.getSenderGst());
		retrievalManifestProviderVO.setTransporterName(retrievalManifestProviderDTO.getTransporterName());
		retrievalManifestProviderVO.setVehicleeNo(retrievalManifestProviderDTO.getVechileNo());
		retrievalManifestProviderVO.setDriverPhoneNo(retrievalManifestProviderDTO.getDriverPhoneNo());
		retrievalManifestProviderVO.setActive(retrievalManifestProviderDTO.isActive());
		retrievalManifestProviderVO.setCancel(retrievalManifestProviderDTO.isCancel());
		retrievalManifestProviderVO.setOrgId(retrievalManifestProviderDTO.getOrgId());
		retrievalManifestProviderVO.setFinYear(retrievalManifestProviderDTO.getFinYear());

		if (retrievalManifestProviderDTO.getId() != null) {

			List<RetrievalManifestProviderDetailsVO> retrievalManifestProviderDetailsVOs = retrievalManifestProviderDetailsRepo
					.findByRetrievalManifestProviderVO(retrievalManifestProviderVO);
			retrievalManifestProviderDetailsRepo.deleteAll(retrievalManifestProviderDetailsVOs);
		}

		List<RetrievalManifestProviderDetailsVO> detailsVOs = new ArrayList<RetrievalManifestProviderDetailsVO>();

		for (RetrievalManifestProviderDetailsDTO detailsDTO : retrievalManifestProviderDTO
				.getRetrievalManifestProviderDetailsDTO()) {

			RetrievalManifestProviderDetailsVO retrievalManifestProviderDetailsVO = new RetrievalManifestProviderDetailsVO();

			retrievalManifestProviderDetailsVO.setAsset(detailsDTO.getAsset());
			retrievalManifestProviderDetailsVO.setAssetCode(detailsDTO.getAssetCode());
			retrievalManifestProviderDetailsVO.setAssetQty(detailsDTO.getAssetQty());
			retrievalManifestProviderDetailsVO.setKitId(detailsDTO.getKitId());
			retrievalManifestProviderDetailsVO.setKitName(detailsDTO.getKitName());
			retrievalManifestProviderDetailsVO.setKitQty(detailsDTO.getKitQty());
			retrievalManifestProviderDetailsVO.setHsnCode(detailsDTO.getHsnCode());
			retrievalManifestProviderDetailsVO.setRetrievalManifestProviderVO(retrievalManifestProviderVO);
			detailsVOs.add(retrievalManifestProviderDetailsVO);

		}
		retrievalManifestProviderVO.setRetrievalManifestProviderDetailsVOs(detailsVOs);
		return retrievalManifestProviderVO;

	}

	@Override
	public List<RetrievalManifestProviderVO> getAllRetrievalManifestProvider() {
		return retrievalManifestProviderRepo.findAll();
	}

	@Override
	public Optional<RetrievalManifestProviderVO> getRetrievalManifestProviderById(Long id) {
		return retrievalManifestProviderRepo.findById(id);
	}

	// DECLARATION PART

	@Override
	public DeclarationAndNotesVO createDeclarationAndNotes(DeclarationAndNotesVO declarationAndNotesVO) {
		declarationAndNotesVO = new DeclarationAndNotesVO();
		StringBuilder builder = new StringBuilder();
		builder.append(
				"The packaging products given on hire shall always remain the property of SCM AI-PACKS Private Limited and shall not be used for the purpose otherwise agreed upon. ");
		builder.append("same shall be returned at the address notified by SCM AI-PACKS Private Limited.");
		String builder1 = builder.toString().replace(",", " ");
		declarationAndNotesVO.setDeclaration(builder1.toString());
		declarationAndNotesVO.setNote1(
				"1. The goods listed in the above manifest are used empty packaging issued to customer on a daily hire basis. The service is packaging on."
						.replace(",", " "));
		declarationAndNotesVO.setNote1Bold("rental model and not sale to customer.".replace(",", " "));
		declarationAndNotesVO.setNote2(
				"2. No E-Way Bill is required for Empty Cargo Containers. Refer, Rule 14 of Central Goods and Services Tax (Second Amendment) Rules, 2018."
						.replace(",", " "));

		return declarationAndNotesRepo.save(declarationAndNotesVO);
	}

	@Override
	public List<DeclarationAndNotesVO> getAllDeclarationAndNotes() {

		return declarationAndNotesRepo.findAll();
	}

	@Override
	public List<Map<String, Object>> getReceiptRegisterReport(Long orgId, String partyName, String branchCode,
			String finYear, String fromDate, String toDate) {
		Set<Object[]> register = receiptRepo.getReceiptRegisterReport(orgId, partyName, branchCode, finYear, fromDate,
				toDate);
		return getReceiptRegister(register);
	}

	private List<Map<String, Object>> getReceiptRegister(Set<Object[]> getRegister) {
		List<Map<String, Object>> doctypeMappingDetails = new ArrayList<>();
		for (Object[] sup : getRegister) {
			Map<String, Object> doctype = new HashMap<>();
			doctype.put("orgId", sup[0] != null ? sup[0].toString() : "");
			doctype.put("branchCode", sup[1] != null ? sup[1].toString() : "");
			doctype.put("finYear", sup[2] != null ? sup[2].toString() : "");
			doctype.put("createdBy", sup[3] != null ? sup[3].toString() : "");
			doctype.put("createdOn", sup[4] != null ? sup[4].toString() : "");
			doctype.put("docId", sup[5] != null ? sup[5].toString() : "");
			doctype.put("docDate", sup[6] != null ? sup[6].toString() : "");
			doctype.put("subTypeCode", sup[7] != null ? sup[7].toString() : "");
			doctype.put("chequeBank", sup[8] != null ? sup[8].toString() : "");
			doctype.put("chQnNumber", sup[9] != null ? sup[9].toString() : "");
			doctype.put("subLedgerCode", sup[10] != null ? sup[10].toString() : "");
			doctype.put("subLedgerName", sup[11] != null ? sup[11].toString() : "");
			doctype.put("receiptAmount", sup[12] != null ? new BigDecimal(sup[12].toString()) : BigDecimal.ZERO);
			doctype.put("bankChargesAmt", sup[13] != null ? new BigDecimal(sup[13].toString()) : BigDecimal.ZERO);
			doctype.put("tdsAmt", sup[14] != null ? new BigDecimal(sup[14].toString()) : BigDecimal.ZERO);
//			doctype.put("invoiceNo", sup[15] != null ? sup[15].toString() : "");
//			doctype.put("invoiceDate", sup[16] != null ? sup[16].toString() : "");
//			doctype.put("refNo", sup[17] != null ? sup[17].toString() : "");
//			doctype.put("refDate", sup[18] != null ? sup[18].toString() : "");
			doctype.put("arapAmt", sup[15] != null ? new BigDecimal(sup[15].toString()) : BigDecimal.ZERO);
			doctype.put("chargableAmt", sup[16] != null ? new BigDecimal(sup[16].toString()) : BigDecimal.ZERO);
			doctype.put("arApOutstanding", sup[17] != null ? new BigDecimal(sup[17].toString()) : BigDecimal.ZERO);
			doctype.put("arapSettled", sup[18] != null ? new BigDecimal(sup[18].toString()) : BigDecimal.ZERO);
			doctype.put("chequeDate", sup[19] != null ? sup[19].toString() : "");
			doctype.put("totalAmount", sup[20] != null ? new BigDecimal(sup[20].toString()) : BigDecimal.ZERO);
//			doctype.put("tds", sup[25] != null ? new BigDecimal(sup[25].toString()) : BigDecimal.ZERO);
			doctype.put("shortName", sup[21] != null ? sup[21].toString() : "");
			doctype.put("netAmount", sup[22] != null ? new BigDecimal(sup[22].toString()) : BigDecimal.ZERO);
			doctype.put("onAccount", sup[23] != null ? new BigDecimal(sup[23].toString()) : BigDecimal.ZERO);
			doctype.put("rn", sup[24] != null ? new BigDecimal(sup[24].toString()) : BigDecimal.ZERO);
			doctype.put("chargeAmount", sup[25] != null ? new BigDecimal(sup[25].toString()) : BigDecimal.ZERO);
			doctype.put("receivableAmount", sup[26] != null ? new BigDecimal(sup[26].toString()) : BigDecimal.ZERO);
			doctypeMappingDetails.add(doctype);
		}

		return doctypeMappingDetails;
	}

	@Override
	public List<Map<String, Object>> getPaymentRegisterReport(Long orgId, String partyCode, String branchCode,
			String finYear, String fromDate, String toDate) {
		Set<Object[]> chCode = receiptRepo.getPaymentRegisterReport(orgId, partyCode, branchCode, finYear, fromDate,
				toDate);
		return getChargeCode(chCode);
	}

	private List<Map<String, Object>> getChargeCode(Set<Object[]> chCode) {
		List<Map<String, Object>> list1 = new ArrayList<>();
		for (Object[] sup : chCode) {
			Map<String, Object> doctype = new HashMap<>();
			doctype.put("createdBy", sup[0] != null ? sup[0].toString() : "");
			doctype.put("createdOn", sup[1] != null ? sup[1].toString() : "");
			doctype.put("docId", sup[2] != null ? sup[2].toString() : "");
			doctype.put("docDate", sup[3] != null ? sup[3].toString() : "");
			doctype.put("subTypeCode", sup[4] != null ? sup[4].toString() : "");
			doctype.put("subTypeName", sup[5] != null ? sup[5].toString() : "");
			doctype.put("chequeBank", sup[6] != null ? sup[6].toString() : "");
			doctype.put("chequeNo", sup[7] != null ? sup[7].toString() : "");
			doctype.put("subLedgerCode", sup[8] != null ? sup[8].toString() : "");
			doctype.put("subLedgerName", sup[9] != null ? sup[9].toString() : "");
			doctype.put("partyShortName", sup[10] != null ? sup[10].toString() : "");
			doctype.put("PaymentAmount", sup[11] != null ? new BigDecimal(sup[11].toString()) : BigDecimal.ZERO);
			doctype.put("bankChargesAmt", sup[12] != null ? new BigDecimal(sup[12].toString()) : BigDecimal.ZERO);
			doctype.put("tdsAmt", sup[13] != null ? new BigDecimal(sup[13].toString()) : BigDecimal.ZERO);
			doctype.put("staxAmount", sup[14] != null ? new BigDecimal(sup[14].toString()) : BigDecimal.ZERO);
//			doctype.put("invoiceNo", sup[15] != null ? sup[15].toString() : "");
//			doctype.put("invoiceDate", sup[16] != null ? sup[16].toString() : "");
//			doctype.put("refNo", sup[17] != null ? sup[17].toString() : "");
//			doctype.put("refDate", sup[18] != null ? sup[18].toString() : "");
			doctype.put("arapAmount", sup[15] != null ? new BigDecimal(sup[15].toString()) : BigDecimal.ZERO);
			doctype.put("arApOutstanding", sup[16] != null ? new BigDecimal(sup[16].toString()) : BigDecimal.ZERO);
			doctype.put("arapSettled", sup[17] != null ? new BigDecimal(sup[17].toString()) : BigDecimal.ZERO);
			doctype.put("chequeDate", sup[18] != null ? sup[18].toString() : "");
			doctype.put("bankCashAcc", sup[19] != null ? sup[19].toString() : "");
			doctype.put("onaccount", sup[20] != null ? new BigDecimal(sup[20].toString()) : BigDecimal.ZERO);
			doctype.put("netamount", sup[21] != null ? new BigDecimal(sup[21].toString()) : BigDecimal.ZERO);
			doctype.put("chargeamt", sup[22] != null ? new BigDecimal(sup[22].toString()) : BigDecimal.ZERO);

			list1.add(doctype);
		}
		return list1;
	}

	@Override
	public Map<String, Object> createUpdateQuotatio(QuotationDTO quotationDTO) throws ApplicationException {
		QuotationVO quotationVO = new QuotationVO();
		String message = null;

		if (ObjectUtils.isEmpty(quotationDTO.getId())) {
			if (quotationRepo.existsByOrgIdAndQuotationNo(quotationDTO.getOrgId(), quotationDTO.getQuotationNo())) {
				String errorMessage = String.format("The QuotationNo: %s already exists This Organization.",
						quotationDTO.getQuotationNo());
				throw new ApplicationException(errorMessage);
			}
			quotationVO = new QuotationVO();
			quotationVO.setCreatedBy(quotationDTO.getCreatedBy());
			quotationVO.setUpdatedBy(quotationDTO.getCreatedBy());
			message = "Quotation Creation SuccessFully";
		} else {

			quotationVO = quotationRepo.findById(quotationDTO.getId())
					.orElseThrow(() -> new ApplicationException("Quotation not found with id: " + quotationDTO.getId()));
			quotationVO.setUpdatedBy(quotationDTO.getCreatedBy());
			if (!quotationVO.getQuotationNo().equalsIgnoreCase(quotationDTO.getQuotationNo())) {
				if (quotationRepo.existsByOrgIdAndQuotationNo(quotationDTO.getOrgId(), quotationDTO.getQuotationNo())) {
					String errorMessage = String.format("The QuotationNo : %s already exists This Organization.",
							quotationDTO.getQuotationNo());
					throw new ApplicationException(errorMessage);
				}
				quotationVO.setQuotationNo(quotationDTO.getQuotationNo().toUpperCase());
			}
			message = "Quotation Update Successfully";
		}

		getQuotationVOFromQuotationDTO(quotationVO, quotationDTO);
		quotationRepo.save(quotationVO);
		Map<String, Object> response = new HashMap<>();
		response.put("quotationVO", quotationVO);
		response.put("message", message);
		return response;	
	}

	private QuotationVO getQuotationVOFromQuotationDTO(QuotationVO quotationVO, QuotationDTO quotationDTO) {
		quotationVO.setQuotationNo(quotationDTO.getQuotationNo());
		quotationVO.setQuotationDate(quotationDTO.getQuotationDate());
		quotationVO.setDeliveryAddress(quotationDTO.getDeliveryAddress());
		quotationVO.setCustomerAddress(quotationDTO.getCustomerAddress());
		quotationVO.setOrgId(quotationDTO.getOrgId());
		quotationVO.setCode(quotationDTO.getCode());
		quotationVO.setFinYear(quotationDTO.getFinYear());
		quotationVO.setCompanyAddress(quotationDTO.getCompanyAddress());
		quotationVO.setCustomerName(quotationDTO.getCustomerName());
		quotationVO.setFinYear(quotationDTO.getFinYear());

		if (quotationDTO.getId() != null) {
			List<QuotationDetailsVO> quotationDetailsVOs = quotationDetailsRepo.findByQuotationVO(quotationVO);
			quotationDetailsRepo.deleteAll(quotationDetailsVOs);

		}
		BigDecimal subTotal = BigDecimal.ZERO;
		BigDecimal taxAmount = BigDecimal.ZERO;
		BigDecimal totalTaxAmount = BigDecimal.ZERO;
		List<QuotationDetailsVO> quotationDetailsVOs = new ArrayList<>();
		for (QuotationDetailsDTO quotationDetailsDTO : quotationDTO.getQuotationDetailsDTO()) {
			QuotationDetailsVO quotationDetailsVO = new QuotationDetailsVO();

			quotationDetailsVO.setDescription(quotationDetailsDTO.getDescription());
			quotationDetailsVO.setQuantity(quotationDetailsDTO.getQuantity());
			quotationDetailsVO.setRate(quotationDetailsDTO.getRate());
			quotationDetailsVO.setTax(quotationDetailsDTO.getTax());

			
			quotationDetailsVO.setAmount(quotationDetailsDTO.getQuantity().multiply(quotationDetailsDTO.getRate()));
			taxAmount = quotationDetailsDTO.getTax().multiply(quotationDetailsDTO.getAmount())
					.divide(BigDecimal.valueOf(100));
			quotationDetailsVO.setTaxAmount(taxAmount);
			subTotal = subTotal.add(quotationDetailsVO.getAmount());
			totalTaxAmount=totalTaxAmount.add(quotationDetailsVO.getTaxAmount());

			quotationDetailsVO.setQuotationVO(quotationVO);
			quotationDetailsVOs.add(quotationDetailsVO);
		}

		quotationVO.setSubTotal(subTotal);
		quotationVO.setTotalTaxAmount(totalTaxAmount);
		BigDecimal totalAmount=subTotal.add(totalTaxAmount);
		quotationVO.setTotalAmount(totalAmount);		
		quotationVO.setQuotationDetailsVO(quotationDetailsVOs);

		return quotationVO;
	}

	@Override
	public List<QuotationVO> getQuotationByorgId(Long orgId) {
		return quotationRepo.findQutationByOrgId(orgId);
	}

	@Override
	public Optional<QuotationVO> getQutationById(Long id) {
		return quotationRepo.findById(id);
	}

//		@Override
//		public List<IssueManifestProviderVO> getFillGridForTaxInvoice(Long orgId) {
//				List<TaxInvoiceVO> result = taxInvoiceRepo.getFillGridForTaxInvoice(orgId);
//				return getFillGridForTaxInvoice(result);
//			}
//
//			private List<Map<String, Object>> getFillGridForTaxInvoice(Set<Object[]> result) {
//				List<Map<String, Object>> details = new ArrayList<>();
//				for (Object[] fs : result) {
//					Map<String, Object> object = new HashMap<>();
//					object.put("transactionno", fs[0] != null ? fs[0].toString() : "");
//					object.put("transactiondate", fs[1] != null ? fs[1].toString() : "");
//					object.put("kitid", fs[2] != null ? fs[2].toString() : "");
//					object.put("kitname", fs[3] != null ? fs[3].toString() : "");
//					object.put("kitqty", fs[4] != null ? fs[4].toString() : "");
//					
//					details.add(object); // Add the map to the list
//
//				}
//				return details;
//			}

	@Override
	public List<Map<String, Object>> getFillGridForTaxInvoice(Long orgId) {
		Set<Object[]> result = taxInvoiceRepo.getFillGridForTaxInvoice(orgId);
		return getFillGridForTaxInvoice(result);
	}

	private List<Map<String, Object>> getFillGridForTaxInvoice(Set<Object[]> result) {
		List<Map<String, Object>> details = new ArrayList<>();
		for (Object[] fs : result) {
			Map<String, Object> object = new HashMap<>();
			object.put("setTransactionNo", fs[0] != null ? fs[0].toString() : "");
			object.put("setTransactionDate", fs[1] != null ? fs[1].toString() : "");
			object.put("setKitId", fs[2] != null ? fs[2].toString() : "");
			object.put("setKitName", fs[3] != null ? fs[3].toString() : "");
			object.put("setKitQty", fs[4] != null ? fs[4].toString() : "");
			details.add(object); // Add the map to the list

		}
		return details;
	}

	@Override
	public List<Map<String, Object>> getMimFillGridgettransaction(Long orgId, String Receiver,String docId) {
		Set<Object[]> requestedByDetails = taxInvoiceRepo.getMimFillGridgettransaction(orgId, Receiver,docId);
		return getMimFillGridgettransaction(requestedByDetails);
	}

	private List<Map<String, Object>> getMimFillGridgettransaction(Set<Object[]> chCode) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chCode) {
			Map<String, Object> map = new HashMap<>();
			map.put("transactionno", ch[0] != null ? ch[0].toString() : "");
			List1.add(map);
		}
		return List1;
	}

	@Override
	public List<Map<String, Object>> getMimFillGridgetKitDetails(Long orgId, String TransactionNo) {
		Set<Object[]> requestedByDetails = taxInvoiceRepo.getMimFillGridgetKitDetails(orgId, TransactionNo);
		return getMimFillGridgetKitDetails(requestedByDetails);
	}

	private List<Map<String, Object>> getMimFillGridgetKitDetails(Set<Object[]> chCode) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chCode) {
			Map<String, Object> map = new HashMap<>();
//				map.put("employeeId", ch[0] != null ? Integer.parseInt(ch[0].toString()) : 0);
			map.put("transactionno", ch[0] != null ? ch[0].toString() : "");
			map.put("transactiondate", ch[1] != null ? ch[1].toString() : "");
			map.put("kitid", ch[2] != null ? ch[2].toString() : "");
			map.put("kitname", ch[3] != null ? ch[3].toString() : "");
			map.put("kitqty", ch[4] != null ? ch[4].toString() : "");
			List1.add(map);
		}
		return List1;
	}

	@Override
	public List<Map<String, Object>> getOrginBillNoBased(Long orgId, String orginBillNo) {
		Set<Object[]> requestedByDetails = taxInvoiceRepo.getOrginBillNoBased(orgId, orginBillNo);
		return getOrginBillNo(requestedByDetails);
	}

	private List<Map<String, Object>> getOrginBillNo(Set<Object[]> chCode) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chCode) {
			Map<String, Object> map = new HashMap<>();
//				map.put("employeeId", ch[0] != null ? Integer.parseInt(ch[0].toString()) : 0);
			map.put("orginBillNo", ch[0] != null ? ch[0].toString() : "");
			map.put("vId", ch[1] != null ? ch[1].toString() : "");
			map.put("totalInvAmountLc", ch[2] != null ? ch[2].toString() : "");
			List1.add(map);
		}
		return List1;
	}

	@Override
	public List<Map<String, Object>> getMimReportDetails(String type, Long orgId, String customerName, String finYear,
			String toDate, String fromDate) {
		Set<Object[]> chCode = issueManifestProviderRepo.getMimReportDetails(type, orgId, customerName, finYear, toDate,
				fromDate);
		return getMimReport(chCode);
	}

	private List<Map<String, Object>> getMimReport(Set<Object[]> chCode) {
		List<Map<String, Object>> list1 = new ArrayList<>();
		for (Object[] sup : chCode) {
			Map<String, Object> doctype = new HashMap<>();
			doctype.put("transactionNo", sup[0] != null ? sup[0].toString() : "");
			doctype.put("transactionDate", sup[1] != null ? sup[1].toString() : "");
			doctype.put("transporterName", sup[2] != null ? sup[2].toString() : "");
			doctype.put("sender", sup[3] != null ? sup[3].toString() : "");
			doctype.put("receiver", sup[4] != null ? sup[4].toString() : "");
			doctype.put("amount", sup[5] != null ? new BigDecimal(sup[5].toString()) : BigDecimal.ZERO);
			doctype.put("hsnCode", sup[6] != null ? new BigDecimal(sup[6].toString()) : BigDecimal.ZERO);
			doctype.put("assetCode", sup[7] != null ? sup[7].toString() : "");
			doctype.put("asset", sup[8] != null ? sup[8].toString() : "");
			doctype.put("assetQty", sup[9] != null ? new BigDecimal(sup[9].toString()) : BigDecimal.ZERO);
			doctype.put("kitId", sup[10] != null ? sup[10].toString() : "");
			doctype.put("kitName", sup[11] != null ? sup[11].toString() : "");
			doctype.put("kitQty", sup[12] != null ? new BigDecimal(sup[12].toString()) : BigDecimal.ZERO);
			list1.add(doctype);
		}
		return list1;
	}

	@Override
	public List<IssueManifestProviderVO> findMIMReports(String type, Long orgId, String customerName, String finYear,
			String toDate, String fromDate) {

		return issueManifestProviderRepo.findMIMReports(type, orgId, customerName, finYear, toDate, fromDate);
	}

	@Override
	public List<RetrievalManifestProviderVO> findRIMReports(String type, Long orgId, String customerName,
			String finYear, String toDate, String fromDate) {

		return retrievalManifestProviderRepo.findRIMReports(type, orgId, customerName, finYear, toDate, fromDate);
	}
	
	
	@Override
	public List<Map<String, Object>> findMimSummaryReport(String type, Long orgId, String customerName, String finYear,
			String fromDate, String toDate) {
		Set<Object[]> chCode = issueManifestProviderRepo.findMimSummaryReport(type, orgId, customerName, finYear,fromDate, toDate);
		return findMimSummary(chCode);
	}

	private List<Map<String, Object>> findMimSummary(Set<Object[]> chCode) {
		List<Map<String, Object>> list1 = new ArrayList<>();
		for (Object[] sup : chCode) {
			Map<String, Object> doctype = new HashMap<>();
			doctype.put("transactionNo", sup[0] != null ? sup[0].toString() : "");
			doctype.put("transactionDate", sup[1] != null ? sup[1].toString() : "");
			doctype.put("transporterName", sup[2] != null ? sup[2].toString() : "");
			doctype.put("receiver", sup[3] != null ? sup[3].toString() : "");
			doctype.put("amount", sup[4] != null ? new BigDecimal(sup[4].toString()) : BigDecimal.ZERO);
			doctype.put("hsnCode", sup[5] != null ? Long.parseLong(sup[5].toString()) : 0L); 
			doctype.put("kitQty", sup[6] != null ? new BigDecimal(sup[6].toString()) : BigDecimal.ZERO);
			list1.add(doctype);
		}
		return list1;
	}

	
	@Override
	public List<Map<String, Object>> findRimSummaryReport(String type, Long orgId, String customerName, String finYear,
			String fromDate, String toDate) {
		Set<Object[]> chCode = issueManifestProviderRepo.findRimSummaryReport(type, orgId, customerName, finYear,fromDate, toDate);
		return findRimSummary(chCode);
	}

	private List<Map<String, Object>> findRimSummary(Set<Object[]> chCode) {
		List<Map<String, Object>> list1 = new ArrayList<>();
		for (Object[] sup : chCode) {
			Map<String, Object> doctype = new HashMap<>();
			doctype.put("transactionNo", sup[0] != null ? sup[0].toString() : "");
			doctype.put("transactionDate", sup[1] != null ? sup[1].toString() : "");
			doctype.put("transporterName", sup[2] != null ? sup[2].toString() : "");
			doctype.put("sender", sup[3] != null ? sup[3].toString() : "");
			doctype.put("amount", sup[4] != null ? new BigDecimal(sup[4].toString()) : BigDecimal.ZERO);
			doctype.put("hsnCode", sup[5] != null ? Long.parseLong(sup[5].toString()) : 0L); 
			doctype.put("kitQty", sup[6] != null ? new BigDecimal(sup[6].toString()) : BigDecimal.ZERO);
			list1.add(doctype);
		}
		return list1;
	}
	
	
	
	
	@Override
	public List<Map<String, Object>> getApAgeing(Long orgId, String branch, String partyname, String asdate, String baseType) {
		Set<Object[]> chType = receiptRepo.getApAgeing(orgId, branch, partyname, asdate,baseType);
		return getApAge(chType);
	}

	private List<Map<String, Object>> getApAge(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();
			map.put("sno", ch[0] != null ? Long.valueOf(ch[0].toString()) : "");
			map.put("arapDetailsId", ch[1] != null ? Long.valueOf(ch[1].toString()) : "");
			map.put("branch", ch[2] != null ? ch[2].toString() : ""); // 2
			map.put("subledgerCode", ch[3] != null ? ch[3].toString() : ""); // 4
			map.put("partyName", ch[4] != null ? ch[4].toString() : ""); // 3
			map.put("subledgerName", ch[5] != null ? ch[5].toString() : ""); // 4
			map.put("partyType", ch[6] != null ? ch[6].toString() : ""); // 5
			map.put("salesPerson", ch[7] != null ? ch[7].toString() : ""); // 6
			map.put("docId", ch[8] != null ? ch[8].toString() : ""); // 7
			map.put("docDate", ch[9] != null ? ch[9].toString() : ""); // 8
			map.put("supRefNo", ch[10] != null ? ch[10].toString() : ""); // 9
			map.put("supRefDate", ch[11] != null ? ch[11].toString() : ""); // 10
			map.put("dueDate", ch[12] != null ? ch[12].toString() : ""); // 11
			map.put("refNo", ch[13] != null ? ch[13].toString() : ""); // 12
			map.put("refDate", ch[14] != null ? ch[14].toString() : ""); // 13
			map.put("amount", ch[15] != null ? new BigDecimal(ch[15].toString()) : BigDecimal.ZERO); // 14
			map.put("outStanding", ch[16] != null ? new BigDecimal(ch[16].toString()) : BigDecimal.ZERO); // 15
			map.put("totalDue", ch[17] != null ? new BigDecimal(ch[17].toString()) : BigDecimal.ZERO); // 16
			

			List1.add(map);
		}
		return List1;
	}

	

//	@Override
//	public List<RetrievalManifestProviderVO> findRIMMIMReports(
//	        String type,
//	        Long orgId,
//	        String customerName,
//	        String finYear,
//	        String toDate,
//	        String fromDate) {
//
//	    if (type == null) {
//	        throw new IllegalArgumentException("Type must not be null");
//	    }
//
//	    if ("MIM".equalsIgnoreCase(type)) {
//	         issueManifestProviderRepo.findMIMReports(type, orgId, customerName, finYear, toDate, fromDate);
//	    } else if ("RIM".equalsIgnoreCase(type)) {
//	         retrievalManifestProviderRepo.findRIMReports(type, orgId, customerName, finYear, toDate, fromDate);
//	    } else {
//	        throw new IllegalArgumentException("Unsupported report type: " + type);
//	    }
//	}



//		@Override
//		public List<IssueManifestProviderVO> getFillGridForTaxInvoice(Long orgId) {
//		    Set<Object[]> result = taxInvoiceRepo.getFillGridForTaxInvoice(orgId);
//		    return (List<IssueManifestProviderVO>) getFillGridForTaxInvoice(result);
//		}
//
//		private List<Map<String, Object> getFillGridForTaxInvoice(Set<Object[]> result) {
//		    List<IssueManifestProviderVO> list = new ArrayList<>();
//		    for (Object[] fs : result) {
//		        IssueManifestProviderVO vo = new IssueManifestProviderVO();
//		        vo.setTransactionNo(fs[0] != null ? fs[0].toString() : null);
//		        vo.setTransactionDate(fs[1] != null ? fs[1].toString() : null);
//		        vo.setKitId(fs[2] != null ? fs[2].toString() : null);
//		        vo.setKitName(fs[3] != null ? fs[3].toString() : null);
//		        vo.setKitQty(fs[4] != null ? Integer.parseInt(fs[4].toString()) : 0);
//
//		        list.add(vo);
//		    }
//		    return list;
//		}

}
