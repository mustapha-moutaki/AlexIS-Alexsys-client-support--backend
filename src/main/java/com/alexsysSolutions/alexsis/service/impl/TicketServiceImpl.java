package com.alexsysSolutions.alexsis.service.impl;

import com.alexsysSolutions.alexsis.dto.request.ticket.TicketCreateCommand;
import com.alexsysSolutions.alexsis.dto.response.ticket.TicketDetailDtoResponse;
import com.alexsysSolutions.alexsis.dto.response.ticket.TicketSummaryDtoResponse;
import com.alexsysSolutions.alexsis.enums.TicketStatus;
import com.alexsysSolutions.alexsis.enums.UserRole;
import com.alexsysSolutions.alexsis.exception.ResourceNotFoundException;
import com.alexsysSolutions.alexsis.exception.ValidationException;
import com.alexsysSolutions.alexsis.mapper.TicketMapper;
import com.alexsysSolutions.alexsis.model.Category;
import com.alexsysSolutions.alexsis.model.Ticket;
import com.alexsysSolutions.alexsis.model.User;
import com.alexsysSolutions.alexsis.reposiotry.CategoryRepository;
import com.alexsysSolutions.alexsis.reposiotry.TicketRepository;
import com.alexsysSolutions.alexsis.reposiotry.UserRepository;
import com.alexsysSolutions.alexsis.security.context.CurrentUserProvider;
import com.alexsysSolutions.alexsis.service.TicketService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


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

    @Override
    public TicketDetailDtoResponse create(TicketCreateCommand command) {
        logger.info("Creating ticket with title: {}", command.getTitle());

        // validate+ fetch relations
        Category category = categoryRepository.findById(command.getCategoryId())
                .orElseThrow(()->new ResourceNotFoundException("Category not found"));

        /**
         *  Check the role of the current user if it's admin we take the id from the response
         *   and if it's client we take his authentification id
         */
        User client;
        if (currentUser.getRole() == UserRole.CLIENT) {
            client = userRepository.findByEmail(currentUser.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        } else {
            // Admin creates ticket for a client
            client = userRepository.findByIdAndRole(command.getClientId(), UserRole.CLIENT)
                    .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
        }

        User assignedTo  = null;
        if(command.getAssignedToId() != null){
            assignedTo = userRepository.findById(command.getAssignedToId()).orElseThrow(
                    ()-> new ResourceNotFoundException("Assigned user not found")
            );
            // check if the user is agent
            if(assignedTo.getRole() != UserRole.AGENT){
                throw new ValidationException("Assigned user must be agent");
            }
        }

        // build entity ticket
        Ticket ticket = new Ticket();
        ticket.setTitle(command.getTitle());
        ticket.setDescription(command.getDescription());
        ticket.setPriority(command.getPriority());
        ticket.setIssueType(command.getIssueType());

        ticket.setCategory(category);
        ticket.setClient(client);
        ticket.setAssignedTo(assignedTo);
        ticket.setCreatedBy(currentUser.getEmail());

        // business rules;
        if(command.getStatus() != null){
            ticket.setStatus(command.getStatus());
        }else{
            ticket.setStatus(TicketStatus.OPEN);
        }

        // timestamp
        if(assignedTo != null){
            ticket.setAssignedAt(LocalDateTime.now());
        }

        ticketRepository.save(ticket);
        logger.info("Ticket created successfully with ID: {}", ticket.getId());
        return ticketMapper.toDtoDetailsResponse(ticket);
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
    public TicketSummaryDtoResponse getByIdSummary(Long id) {
        logger.info("Fetching ticket summary with ID: {}", id);
        Ticket ticket = ticketRepository.findById(id).orElseThrow(
                ()-> {
                    logger.error("Ticket not found with ID: {}", id);
                    return new ResourceNotFoundException("Ticket not found");
                }
        );
        logger.debug("Ticket summary retrieved successfully for ID: {}", id);
        return ticketMapper.toDtoSummaryResponse(ticket);
    }

    @Override
    public TicketDetailDtoResponse getDetailsById(Long id) {
        logger.info("Fetching ticket details with ID: {}", id);
        Ticket ticket = ticketRepository.findByIdWithDetails(id).orElseThrow(
                ()-> {
                    logger.error("Ticket not found with ID: {}", id);
                    return new ResourceNotFoundException("ticket not found");
                }
        );
        logger.debug("Ticket details retrieved successfully for ID: {}", id);
        return ticketMapper.toDtoDetailsResponse(ticket);
    }

    @Override
    public Page<TicketSummaryDtoResponse> getAllTicketsSummary(int page, int size) {
        logger.info("Fetching all tickets summary - page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page , size);
        Page<TicketSummaryDtoResponse> result = ticketRepository.findAll(pageable)
                .map(ticketMapper::toDtoSummaryResponse);
        logger.info("Retrieved {} total tickets (summary)", result.getTotalElements());
        return result;
    }

    @Override
    public Page<TicketDetailDtoResponse> getAllTicketDetailed(int page, int size) {
        logger.info("Fetching all tickets detailed - page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<TicketDetailDtoResponse> result = ticketRepository.findAll(pageable)
                .map(ticketMapper::toDtoDetailsResponse);
        logger.info("Retrieved {} total tickets (detailed)", result.getTotalElements());
        return result;
    }

    @Override
    public void delete(Long id) {
        logger.info("Deleting ticket with ID: {}", id);
        if(!ticketRepository.existsById(id)){
            logger.error("Ticket not found with ID: {}", id);
            throw new ResourceNotFoundException("Ticket not found");
        }
        ticketRepository.deleteById(id);
        logger.info("Ticket with ID {} deleted successfully", id);
    }
}
