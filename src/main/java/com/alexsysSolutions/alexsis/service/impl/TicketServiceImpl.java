package com.alexsysSolutions.alexsis.service.impl;

import com.alexsysSolutions.alexsis.dto.request.ticket.TicketCreateCommand;
import com.alexsysSolutions.alexsis.dto.request.ticket.TicketUpdateStatusDtoRequest;
import com.alexsysSolutions.alexsis.dto.response.ticket.TicketDetailDtoResponse;
import com.alexsysSolutions.alexsis.dto.response.ticket.TicketSummaryDtoResponse;
import com.alexsysSolutions.alexsis.enums.*;
import com.alexsysSolutions.alexsis.exception.ResourceNotFoundException;
import com.alexsysSolutions.alexsis.exception.ValidationException;
import com.alexsysSolutions.alexsis.mapper.TicketMapper;
import com.alexsysSolutions.alexsis.model.*;
import com.alexsysSolutions.alexsis.reposiotry.*;
import com.alexsysSolutions.alexsis.security.context.CurrentUserProvider;
import com.alexsysSolutions.alexsis.service.TicketService;
import com.alexsysSolutions.alexsis.service.WorkloadService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CurrentUserProvider currentUser;
    private static final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);
    private final AttachmentRepository attachmentRepository;
    private final AgentRepository agentRepository;
    private final WorkloadService workloadService;


    @Override
    public TicketDetailDtoResponse create(TicketCreateCommand command) {

        Category category = categoryRepository.findById(command.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Long currentUserId = currentUser.getUserId();
        User client;

        // CLIENT creates own ticket
        if (currentUser.getRole() == UserRole.CLIENT) {
            client = userRepository.findById(currentUserId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        }
        // ADMIN creates ticket for client
        else {
            client = userRepository.findByIdAndRole(command.getClientId(), UserRole.CLIENT)
                    .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
        }

        User assignedTo = null;
        if (command.getAssignedToId() != null) {
            assignedTo = userRepository.findById(command.getAssignedToId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assigned user not found"));

            if (assignedTo.getRole() != UserRole.AGENT) {
                throw new ValidationException("Assigned user must be agent");
            }
            // check availability
            if(assignedTo instanceof Agent agent){
                if(!agent.isActive()){
                    throw new ValidationException("Agent is not active");
                }
                if(agent.getAvailabilityStatus() != AvailabilityStatus.AVAILABLE){
                    throw new ValidationException("Agent is not available");
                }
                if(agent.getActiveTicketsCount() >= agent.getMaxCapacity()){
                    throw new ValidationException("Agent is at capacity");
                }
            }
        }

        Ticket ticket = new Ticket();
        ticket.setTitle(command.getTitle());
        ticket.setDescription(command.getDescription());
        ticket.setPriority(command.getPriority());
        ticket.setIssueType(command.getIssueType());
        ticket.setCategory(category);
        ticket.setClient(client);
        ticket.setAssignedTo(assignedTo);

        ticket.setCreatedBy(currentUser.getEmail());
        ticket.setStatus(command.getStatus() != null ? command.getStatus() : TicketStatus.OPEN);

        if (assignedTo instanceof Agent agent) {

            workloadService.incrementTicketsActiveCount(agent);
        }

        if (assignedTo != null) {
            ticket.setAssignedAt(LocalDateTime.now());
        }

        Ticket savedTicket = ticketRepository.save(ticket);

        // attachments business logic
        if(command.getAttachmentIds() != null && !command.getAttachmentIds().isEmpty()){
            List<Attachment>attachments = attachmentRepository.findAllByIdIn(command.getAttachmentIds());
            for(Attachment attachment : attachments){
                attachment.setTicket(ticket);
                attachment.setStatus(AttachmentStatus.LINKED);
            }
            attachmentRepository.saveAll(attachments);
        }
//        Ticket createdTicket = ticketRepository.save(ticket)/
        return ticketMapper.toDtoDetailsResponse(savedTicket);
    }


    @Override
    public TicketSummaryDtoResponse update(TicketCreateCommand command) {
        logger.info("Updating ticket with ID: {}", command.getId());

        // Fetch existing ticket
        Ticket ticket = ticketRepository.findById(command.getId())
                .orElseThrow(() -> {
                    logger.error("Ticket not found with ID: {}", command.getId());
                    return new ResourceNotFoundException("Ticket not found");
                });

        // Update title if provided
        if (command.getTitle() != null && !command.getTitle().isEmpty()) {
            ticket.setTitle(command.getTitle());
            logger.debug("Ticket title updated to: {}", command.getTitle());
        }

        // Update description if provided
        if (command.getDescription() != null && !command.getDescription().isEmpty()) {
            ticket.setDescription(command.getDescription());
            logger.debug("Ticket description updated");
        }

        // Update priority if provided
        if (command.getPriority() != null) {
            ticket.setPriority(command.getPriority());
            logger.debug("Ticket priority updated to: {}", command.getPriority());
        }

        // Update issue type if provided
        if (command.getIssueType() != null) {
            ticket.setIssueType(command.getIssueType());
            logger.debug("Ticket issue type updated to: {}", command.getIssueType());
        }

        // Update category if provided
        if (command.getCategoryId() != null) {
            Category category = categoryRepository.findById(command.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            ticket.setCategory(category);
            logger.debug("Ticket category updated to: {}", command.getCategoryId());
        }

        // Update client if provided (admin only)
        if (command.getClientId() != null) {
            User client = userRepository.findByIdAndRole(command.getClientId(), UserRole.CLIENT)
                    .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
            ticket.setClient(client);
            logger.debug("Ticket client updated to: {}", command.getClientId());
        }

        // Update assigned agent if provided
        if (command.getAssignedToId() != null) {
            User agent = userRepository.findById(command.getAssignedToId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assigned user not found"));
            if (agent.getRole() != UserRole.AGENT) {
                throw new ValidationException("Assigned user must be agent");
            }

            Agent oldAgent = (Agent) ticket.getAssignedTo();
            if(oldAgent != null){
                workloadService.decrementTicketsActiveCount(oldAgent);
                agentRepository.save(oldAgent);
            }

            Agent newAgent = (Agent) agent;
            if(!newAgent.isActive()){
                throw new ValidationException("Agent is not active");
            }
            if(newAgent.getAvailabilityStatus() != AvailabilityStatus.AVAILABLE){
                throw new ValidationException("Agent is not available");
            }
            if(newAgent.getActiveTicketsCount() >= newAgent.getMaxCapacity()){
                throw new ValidationException("Agent is at capacity");
            }
            workloadService.incrementTicketsActiveCount(newAgent);
            agentRepository.save(newAgent);
            ticket.setAssignedTo(agent);
            if (ticket.getAssignedAt() == null) {
                ticket.setAssignedAt(LocalDateTime.now());
            }
            logger.debug("Ticket assigned to agent with ID: {}", command.getAssignedToId());
        }

        // Update status if provided (admin only)
        if (command.getStatus() != null) {
            TicketStatus oldStatus = ticket.getStatus();
            ticket.setStatus(command.getStatus());
            logger.debug("Ticket status updated from {} to {}", oldStatus, command.getStatus());

            // Set resolved/closed timestamps based on status
            if (command.getStatus() == TicketStatus.RESOLVED && ticket.getResolvedAt() == null) {
                ticket.setResolvedAt(LocalDateTime.now());
                logger.debug("Ticket marked as resolved at: {}", ticket.getResolvedAt());
            } else if (command.getStatus() == TicketStatus.CLOSED && ticket.getClosedAt() == null) {
                ticket.setClosedAt(LocalDateTime.now());
                logger.debug("Ticket marked as closed at: {}", ticket.getClosedAt());
            }
        }

        Ticket updatedTicket = ticketRepository.save(ticket);
        logger.info("Ticket with ID {} updated successfully", command.getId());
        return ticketMapper.toDtoSummaryResponse(updatedTicket);
    }

    @Override
    public TicketDetailDtoResponse getDetailsForAdmin(Long id) {
        Ticket ticket = getTicketOrThrow(id);
        return ticketMapper.toDtoDetailsResponse(ticket);
    }


    @Override
    public TicketDetailDtoResponse getDetailsForClient(Long id) {

        Long clientId = currentUser.getUserId();

        Ticket ticket = getTicketOrThrow(id);

        if (!ticket.getClient().getId().equals(clientId)) {
            throw new ValidationException("Access denied");
        }

        return ticketMapper.toDtoDetailsResponse(ticket);
    }

    @Override
    public TicketSummaryDtoResponse getSummaryForAdmin(Long id) {

        Ticket ticket = getTicketOrThrow(id);

        return ticketMapper.toDtoSummaryResponse(ticket);
    }

    @Override
    public TicketSummaryDtoResponse getSummaryForClient(Long id) {

        Long clientId = currentUser.getUserId();

        Ticket ticket = getTicketOrThrow(id);

        if (!ticket.getClient().getId().equals(clientId)) {
            throw new ValidationException("Access denied");
        }

        return ticketMapper.toDtoSummaryResponse(ticket);
    }


    @Override
    public Page<TicketSummaryDtoResponse> getAllSummaryForAdmin(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return ticketRepository.findAll(pageable)
                .map(ticketMapper::toDtoSummaryResponse);
    }

    @Override
    public Page<TicketSummaryDtoResponse> getAllSummaryForClient(int page, int size) {

        Long clientId = currentUser.getUserId();
        Pageable pageable = PageRequest.of(page, size);

        return ticketRepository.findByClientId(clientId, pageable)
                .map(ticketMapper::toDtoSummaryResponse);
    }

    @Override
    public Page<TicketDetailDtoResponse> getAllDetailsForAdmin(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return ticketRepository.findAll(pageable)
                .map(ticketMapper::toDtoDetailsResponse);
    }


    @Override
    public void delete(Long id) {

        Ticket ticket = getTicketOrThrow(id);

        if (currentUser.getRole() == UserRole.CLIENT &&
                !ticket.getClient().getId().equals(currentUser.getUserId())) {
            throw new ValidationException("Not allowed");
        }
        // if the ticket deleted then the agent gonna be busy his entire life
        if(ticket.getAssignedTo() != null && ticket.getAssignedTo() instanceof Agent agent) {
            workloadService.decrementTicketsActiveCount(agent);
            agentRepository.save(agent);
        }
        ticketRepository.delete(ticket);

    }

    private Ticket getTicketOrThrow(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
    }


    @Override
    public TicketSummaryDtoResponse updateTicketStatus(Long ticketId, TicketUpdateStatusDtoRequest dto) {

        if (dto.getStatus() == null) {
            throw new ValidationException("Status must not be null");
        }

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

        TicketStatus currentStatus = ticket.getStatus();
        TicketStatus newStatus = dto.getStatus();

        //cannot change a CLOSED ticket
        if (currentStatus == TicketStatus.CLOSED) {
            throw new ValidationException("You cannot change the status of a CLOSED ticket");
        }


        ticket.setStatus(newStatus);

        // business logic (timestamps)
        if (newStatus == TicketStatus.RESOLVED && ticket.getResolvedAt() == null) {
            ticket.setResolvedAt(LocalDateTime.now());

            if (ticket.getAssignedTo() != null && ticket.getAssignedTo() instanceof Agent agent) {
                workloadService.decrementTicketsActiveCount(agent);
                agentRepository.save(agent);
            }
        }

        if (newStatus == TicketStatus.CLOSED && ticket.getClosedAt() == null && ticket.getResolvedAt() == null) {

            ticket.setClosedAt(LocalDateTime.now());

            if (ticket.getAssignedTo() != null) {

                Agent agentAssigned = (Agent) ticket.getAssignedTo();
                workloadService.decrementTicketsActiveCount(agentAssigned);
                agentRepository.save(agentAssigned);
            }
        }

        Ticket updatedTicket = ticketRepository.save(ticket);

        return ticketMapper.toDtoSummaryResponse(updatedTicket);
    }

    @Override
    public TicketSummaryDtoResponse reAssignedTicket(Long ticketId, Long agentId) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

        User agent = userRepository.findById(agentId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (agent.getRole() != UserRole.AGENT) {
            throw new ValidationException("Assigned user must be an agent");
        }

        // remove activeTicketCount from the old agent
        if(ticket.getAssignedTo() != null && ticket.getAssignedTo() instanceof Agent oldAgent){
            workloadService.decrementTicketsActiveCount(oldAgent);
            agentRepository.save(oldAgent);
        }
        // reAssigned it to new agent
        workloadService.incrementTicketsActiveCount((Agent) agent);
        ticket.setAssignedTo(agent);
        ticket.setAssignedAt(LocalDateTime.now());

        Ticket updatedTicket = ticketRepository.save(ticket);

        return ticketMapper.toDtoSummaryResponse(updatedTicket);
    }

    // update the priority of ticket
    @Override
    public TicketSummaryDtoResponse updateTicketPriority(Long ticketId, Priority priority ){

       if(priority ==  null){
           throw new ValidationException("Ticket is not found");
       }
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(
                ()-> new ResourceNotFoundException("ticket is not found")
        );

       ticket.setPriority(priority);
       Ticket updatedTicketPriority = ticketRepository.save(ticket);
       return ticketMapper.toDtoSummaryResponse(updatedTicketPriority);

    }
}
