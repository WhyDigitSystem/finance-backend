package com.base.basesetup.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.basesetup.dto.InvoiceDTO;
import com.base.basesetup.dto.InvoiceProductLinesDTO;
import com.base.basesetup.dto.IssueManifestProviderDTO;
import com.base.basesetup.dto.IssueManifestProviderDetailsDTO;
import com.base.basesetup.dto.RetrievalManifestProviderDTO;
import com.base.basesetup.dto.RetrievalManifestProviderDetailsDTO;
import com.base.basesetup.entity.DeclarationAndNotesVO;
import com.base.basesetup.entity.InvoiceProductLinesVO;
import com.base.basesetup.entity.InvoiceVO;
import com.base.basesetup.entity.IssueManifestProviderDetailsVO;
import com.base.basesetup.entity.IssueManifestProviderVO;
import com.base.basesetup.entity.RetrievalManifestProviderDetailsVO;
import com.base.basesetup.entity.RetrievalManifestProviderVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.InvoiceProductLinesRepo;
import com.base.basesetup.repo.InvoiceRepo;
import com.base.basesetup.repo.IssueManifestProviderDetailsRepo;
import com.base.basesetup.repo.IssueManifestProviderRepo;
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
		
}
