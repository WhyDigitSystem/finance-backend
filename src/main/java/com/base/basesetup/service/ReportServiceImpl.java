package com.base.basesetup.service;

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

@Service
public class ReportServiceImpl implements ReportService{

	public static final Logger LOGGER = LoggerFactory.getLogger(ReportService.class);
	
	@Autowired
	InvoiceRepo invoiceRepo;
	
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
	
	//Issue Manifest
		
		@Override
		public Map<String, Object> createUpdateIssuemanifest(IssueManifestProviderDTO issueManifestProviderDTO)
				throws ApplicationException {
			IssueManifestProviderVO issueManifestProviderVO = null;
			String message = null;
			if (issueManifestProviderDTO.getId() != null) {
				// Update existing entity
				issueManifestProviderVO = issueManifestProviderRepo.findById(issueManifestProviderDTO.getId())
						.orElseThrow(() -> new ApplicationException(
								"This Id Not Found Any Information, Invalid Id: " + issueManifestProviderDTO.getId()));
				issueManifestProviderVO.setUpdatedBy(issueManifestProviderDTO.getCreatedBy());
				if(!issueManifestProviderVO.getTransactionNo().equals(issueManifestProviderDTO.getTransactionNo()))
				{
					if(issueManifestProviderRepo.existsByOrgIdAndTransactionNo(issueManifestProviderDTO.getOrgId(),issueManifestProviderDTO.getTransactionNo()))
					{
						throw new ApplicationException("TransactionNo already Exists");
					}
					issueManifestProviderVO.setTransactionNo(issueManifestProviderDTO.getTransactionNo());
							
				}
				message = "IssueManifestProvider Updation Sucessfully";

			} else {

				issueManifestProviderVO = new IssueManifestProviderVO();
				issueManifestProviderVO.setCreatedBy(issueManifestProviderDTO.getCreatedBy());
				issueManifestProviderVO.setUpdatedBy(issueManifestProviderDTO.getCreatedBy());
				if(issueManifestProviderRepo.existsByOrgIdAndTransactionNo(issueManifestProviderDTO.getOrgId(),issueManifestProviderDTO.getTransactionNo()))
				{
					throw new ApplicationException("TransactionNo already Exists");
				}
				issueManifestProviderVO.setTransactionNo(issueManifestProviderDTO.getTransactionNo());
				message = "IssueManifestProvider Creatrion Sucessfully";
			}
			getIssueManifestProviderVOFromIssueManifestProviderDTO(issueManifestProviderVO, issueManifestProviderDTO);
			issueManifestProviderRepo.save(issueManifestProviderVO);

			// Prepare the response
			Map<String, Object> response = new HashMap<>();
			response.put("message", message);
			response.put("issueManifestProviderVO", issueManifestProviderVO);
			return response;
		}

		private IssueManifestProviderVO getIssueManifestProviderVOFromIssueManifestProviderDTO(
				IssueManifestProviderVO issueManifestProviderVO, IssueManifestProviderDTO issueManifestProviderDTO) throws ApplicationException {
			issueManifestProviderVO.setTransactionDate(issueManifestProviderDTO.getTransactionDate());
			issueManifestProviderVO.setDispatchDate(issueManifestProviderDTO.getDispatchDate());
			issueManifestProviderVO.setTransactionType(issueManifestProviderDTO.getTransactionType());
			issueManifestProviderVO.setSender(issueManifestProviderDTO.getSender());
			issueManifestProviderVO.setSenderAddress(issueManifestProviderDTO.getSenderAddress());
			issueManifestProviderVO.setReceiver(issueManifestProviderDTO.getReceiver());
			issueManifestProviderVO.setReceiverAddress(issueManifestProviderDTO.getReceiverAddress());
			issueManifestProviderVO.setReceiverGst(issueManifestProviderDTO.getReceiverGst());
			issueManifestProviderVO.setAmountInWords(issueManifestProviderDTO.getAmountInWords());
			issueManifestProviderVO.setAmount(issueManifestProviderDTO.getAmount());
			issueManifestProviderVO.setTransporterName(issueManifestProviderDTO.getTransporterName());
			issueManifestProviderVO.setVehicleNo(issueManifestProviderDTO.getVehicleNo());
			issueManifestProviderVO.setDriverPhoneNo(issueManifestProviderDTO.getDriverPhoneNo());
			issueManifestProviderVO.setActive(issueManifestProviderDTO.isActive());
			issueManifestProviderVO.setCancel(issueManifestProviderDTO.isCancel());
			issueManifestProviderVO.setOrgId(issueManifestProviderDTO.getOrgId());
			if (issueManifestProviderDTO.getId() != null) {

				List<IssueManifestProviderDetailsVO> issueManifestProviderDetailsVOs = issueManifestProviderDetailsRepo
						.findByIssueManifestProviderVO(issueManifestProviderVO);
				issueManifestProviderDetailsRepo.deleteAll(issueManifestProviderDetailsVOs);
			}

			List<IssueManifestProviderDetailsVO> detailsVOs = new ArrayList<IssueManifestProviderDetailsVO>();

			for (IssueManifestProviderDetailsDTO detailsDTO : issueManifestProviderDTO
					.getIssueManifestProviderDetailsDTO()) {

				IssueManifestProviderDetailsVO issueManifestProviderDetailsVO = new IssueManifestProviderDetailsVO();

				issueManifestProviderDetailsVO.setAsset(detailsDTO.getAsset());
				issueManifestProviderDetailsVO.setAssetCode(detailsDTO.getAssetCode());
				issueManifestProviderDetailsVO.setAssetQty(detailsDTO.getAssetQty());
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
		public List<IssueManifestProviderVO> getAllIssueManifestProvider() {
			
			return issueManifestProviderRepo.findAll();
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
				
				if(!retrievalManifestProviderVO.getTransactionNo().equals(retrievalManifestProviderDTO.getTransactionNo()))
				{
					if(retrievalManifestProviderRepo.existsByOrgIdAndTransactionNo(retrievalManifestProviderDTO.getOrgId(),retrievalManifestProviderDTO.getTransactionNo()))
					{
						throw new ApplicationException("TransactionNo already Exists");
					}
					retrievalManifestProviderVO.setTransactionNo(retrievalManifestProviderDTO.getTransactionNo());
							
				}
				message = "IssueManifestProvider Updation Sucessfully";

			} else {

				retrievalManifestProviderVO = new RetrievalManifestProviderVO();
				retrievalManifestProviderVO.setCreatedBy(retrievalManifestProviderDTO.getCreatedBy());
				retrievalManifestProviderVO.setUpdatedBy(retrievalManifestProviderDTO.getCreatedBy());
					if(retrievalManifestProviderRepo.existsByOrgIdAndTransactionNo(retrievalManifestProviderDTO.getOrgId(),retrievalManifestProviderDTO.getTransactionNo()))
					{
						throw new ApplicationException("TransactionNo already Exists");
					}
					retrievalManifestProviderVO.setTransactionNo(retrievalManifestProviderDTO.getTransactionNo());
				message = "IssueManifestProvider Creatrion Sucessfully";
			}
			getRetrievalManifestProviderVOFromRetrievalManifestProviderDTO(retrievalManifestProviderVO, retrievalManifestProviderDTO);
			retrievalManifestProviderRepo.save(retrievalManifestProviderVO);

			// Prepare the response
			Map<String, Object> response = new HashMap<>();
			response.put("message", message);
			response.put("retrievalManifestProviderVO", retrievalManifestProviderVO);
			return response;
		}

		private RetrievalManifestProviderVO getRetrievalManifestProviderVOFromRetrievalManifestProviderDTO(
				RetrievalManifestProviderVO retrievalManifestProviderVO, RetrievalManifestProviderDTO retrievalManifestProviderDTO) throws ApplicationException {
					
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
	
		//DECLARATION PART
		
		@Override
		public DeclarationAndNotesVO createDeclarationAndNotes(DeclarationAndNotesVO declarationAndNotesVO) {
			declarationAndNotesVO=new DeclarationAndNotesVO();
			StringBuilder builder=new StringBuilder();
			builder.append("The packaging products given on hire shall always remain the property of SCM AI-PACKS Private Limited and shall not be used for the purpose otherwise agreed upon. ");
			builder.append("same shall be returned at the address notified by SCM AI-PACKS Private Limited.");
			String builder1=builder.toString().replace(",", " ");
			declarationAndNotesVO.setDeclaration(builder1.toString());
			declarationAndNotesVO.setNote1("1. The goods listed in the above manifest are used empty packaging issued to customer on a daily hire basis. The service is packaging on.".replace(","," "));
			declarationAndNotesVO.setNote1Bold("rental model and not sale to customer.".replace(","," "));
			declarationAndNotesVO.setNote2("2. No E-Way Bill is required for Empty Cargo Containers. Refer, Rule 14 of Central Goods and Services Tax (Second Amendment) Rules, 2018.".replace(","," "));
			
			return declarationAndNotesRepo.save(declarationAndNotesVO);
		}

		@Override
		public List<DeclarationAndNotesVO> getAllDeclarationAndNotes() {
			
			return declarationAndNotesRepo.findAll();
		}

		@Override
		public List<Map<String, Object>> getReceiptRegisterReport(Long orgId, String partyName, String branchCode,
				String finYear, String fromDate, String toDate) {
			Set<Object[]> register = receiptRepo.getReceiptRegisterReport(orgId, partyName,branchCode,finYear,fromDate,toDate);
			return getReceiptRegister(register);
		}

		private List<Map<String, Object>> getReceiptRegister(Set<Object[]> getRegister) {
			List<Map<String, Object>> doctypeMappingDetails = new ArrayList<>();
			for (Object[] sup : getRegister) {
				Map<String, Object> doctype = new HashMap<>();
				doctype.put("arapDetailsId", sup[0] != null ? sup[0].toString() : "");
				doctype.put("branch", sup[1] != null ? sup[1].toString() : "");
				doctype.put("subLedgerCode", sup[2] != null ? sup[2].toString() : "");
				doctype.put("vId", sup[3] != null ? sup[3].toString() : "");
				doctype.put("vDatae", sup[4] != null ? sup[4].toString() : "");
				doctype.put("refNo", sup[5] != null ? sup[5].toString() : "");
				doctype.put("refDate", sup[6] != null ? sup[6].toString() : "");
				doctype.put("supprefNo", sup[7] != null ? sup[7].toString() : "");
				doctype.put("supprefDate", sup[8] != null ? sup[8].toString() : "");
				doctype.put("acccurrency", sup[9] != null ? sup[9].toString() : "");
				doctype.put("amount", sup[10] != null ? sup[10].toString() : "");
				doctype.put("arapSettled", sup[11] != null ? sup[11].toString() : "");
				doctype.put("chargableAmt", sup[12] != null ? sup[12].toString() : "");
				doctype.put("tdsAmt", sup[13] != null ? sup[13].toString() : "");

				doctypeMappingDetails.add(doctype);
			}

			return doctypeMappingDetails;
		}
		
		

		@Override
		public Map<String, Object> createUpdateQuotatio(QuotationDTO quotationDTO) throws ApplicationException {

		    QuotationVO quotationVO;
		    String message = null;

		    // Check if quotationDTO has an id
		    if (ObjectUtils.isEmpty(quotationDTO.getId())) {
		        // If no id, create a new quotation
		        quotationVO = new QuotationVO();
		        quotationVO.setCreatedBy(quotationDTO.getCreatedBy());
		        quotationVO.setUpdatedBy(quotationDTO.getCreatedBy());
		        message = "Quotation Created Successfully";
		    } else {
		        // If id exists, update the existing quotation
		        quotationVO = quotationRepo.findById(quotationDTO.getId()).orElseThrow(
		                () -> new ApplicationException("Quotation Not Found with id: " + quotationDTO.getId()));
		        quotationVO.setUpdatedBy(quotationDTO.getCreatedBy());
		        message = "Quotation Updation Successfully";
		    }

		    // Convert DTO to entity and set additional fields
		    quotationVO = getQuotationVOFromQuotationDTO(quotationVO, quotationDTO);

		    // Save or update the quotation in the database
		    quotationRepo.save(quotationVO);

		    // Prepare response
		    Map<String, Object> response = new HashMap<>();
		    response.put("message", message);
		    response.put("quotationVO", quotationVO);
		    return response;
		}

		private QuotationVO getQuotationVOFromQuotationDTO(QuotationVO quotationVO, QuotationDTO quotationDTO) {

		    // Set the basic details
		    quotationVO.setQuotationTo(quotationDTO.getQuotationTo());
		    quotationVO.setShippingAddress(quotationDTO.getShippingAddress());
		    quotationVO.setCustomerAddress(quotationDTO.getCustomerAddress());
//		    quotationVO.setFinYear(quotationDTO.getFinYear());
		    quotationVO.setOrgId(quotationDTO.getOrgId());
		    quotationVO.setCode(quotationDTO.getCode());

		    // Build the code using the prefix, financial year, date, and constant
//		    String code = quotationVO.getPrefix() + quotationDTO.getFinYear() + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMM")) + "-1";
//		    quotationVO.setCode(code);
		    
		    
		    if (quotationDTO.getId() != null) {
				// Clear previous items from the database
				List<QuotationDetailsVO> quotationDetailsVOs = quotationDetailsRepo.findByQuotationVO(quotationVO);
				quotationDetailsRepo.deleteAll(quotationDetailsVOs);

			}

		    // Set the list of quotation details
		    List<QuotationDetailsVO> quotationDetailsVOs = new ArrayList<>();
		    for (QuotationDetailsDTO quotationDetailsDTO : quotationDTO.getQuotationDetailsDTO()) {

		        QuotationDetailsVO quotationDetailsVO = new QuotationDetailsVO();

		        // Map the details to the entity
		        quotationDetailsVO.setDescription(quotationDetailsDTO.getDescription());
		        quotationDetailsVO.setPricre(quotationDetailsDTO.getPricre());
		        quotationDetailsVO.setUnit(quotationDetailsDTO.getUnit());
		        quotationDetailsVO.setTotal(quotationDetailsDTO.getTotal());

		        // Link back to the main quotation
		        quotationDetailsVO.setQuotationVO(quotationVO);
		        quotationDetailsVOs.add(quotationDetailsVO);
		    }

		    // Set the quotation details list in the main quotation entity
		    quotationVO.setQuotationDetailsVO(quotationDetailsVOs);

		    return quotationVO;
		}

		@Override
		public List<Map<String, Object>> getQuotationByorgId(Long orgId) {
			return quotationRepo.findQutationByOrgId(orgId);
		}

		@Override
		public Optional<QuotationVO> getQutationById(Long id) {
			return quotationRepo.findById(id);
		}
		
}
