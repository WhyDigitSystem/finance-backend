package com.base.basesetup.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.base.basesetup.dto.CommentsDTO;
import com.base.basesetup.dto.TicketDTO;
import com.base.basesetup.entity.CommentsVO;
import com.base.basesetup.entity.TicketVO;
import com.base.basesetup.exception.ApplicationException;

import io.jsonwebtoken.io.IOException;

@Service
public interface TicketService {

	//Ticket
	
	Map<String, Object> createUpdateTicket(@Valid TicketDTO ticketDTO) throws ApplicationException;

	TicketVO uploadTicketScreenShotInBloob(MultipartFile file, Long id) throws IOException, java.io.IOException;

	Optional<TicketVO> getTicketById(Long id);

	List<TicketVO> getTicketByOrgId(Long orgId);

	List<TicketVO> getTicketByUserName(String userName, Long orgId);
	
	TicketVO updateTicketStatus(Long orgId, Long ticketId,String status,String comments);
	
	//Comments

	Map<String, Object> updateCreateComments(@Valid CommentsDTO commentsDTO) throws ApplicationException;

	List<CommentsVO> getCommentsByTicketId(Long ticketId, Long orgId);


	void deleteCommentsById(Long id);
	
	//Notification

	List<Map<String, Object>> getTicketNotification(Long orgId);

	TicketVO updateNotification(Long orgId, Long ticketId, String status);

	List<Map<String, Object>> getNotificationFromUser(String userName);

	TicketVO clearUserNotification(Long orgId, String userName, Long ticketId, String status);

	List<CommentsVO> getAllCommentsAnotherServer(Long ticketId);

	List<CommentsVO> getAllCommentsMyServer(Long ticketId);

	Map<String, Object> createComments(CommentsDTO commentDTO);

	CommentsVO updateComments(CommentsDTO dto);

	void deleteComments(Long id, Long sourceId);

	List<TicketVO> getTicketReport(Long orgId, String fromDate, String toDate);

	


}

