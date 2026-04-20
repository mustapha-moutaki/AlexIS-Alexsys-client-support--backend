package com.alexsysSolutions.alexsis.service.impl;

import com.alexsysSolutions.alexsis.dto.request.ticket.TicketCreateCommand;
import com.alexsysSolutions.alexsis.dto.response.ticket.TicketDetailDtoResponse;
import com.alexsysSolutions.alexsis.dto.response.ticket.TicketSummaryDtoResponse;
import com.alexsysSolutions.alexsis.enums.TicketStatus;
import com.alexsysSolutions.alexsis.exception.ResourceNotFoundException;
import com.alexsysSolutions.alexsis.mapper.TicketCommandMapper;
import com.alexsysSolutions.alexsis.mapper.TicketMapper;
import com.alexsysSolutions.alexsis.model.Category;
import com.alexsysSolutions.alexsis.model.Ticket;
import com.alexsysSolutions.alexsis.model.User;
import com.alexsysSolutions.alexsis.reposiotry.CategoryRepository;
import com.alexsysSolutions.alexsis.reposiotry.TicketRepository;
import com.alexsysSolutions.alexsis.reposiotry.UserRepository;
import com.alexsysSolutions.alexsis.service.TicketService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
    private final TicketCommandMapper ticketCommandMapper;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public TicketDetailDtoResponse create(TicketCreateCommand command) {

        // validate+ fetch relations
        Category category = categoryRepository.findById(command.getCategoryId())
                .orElseThrow(()->new ResourceNotFoundException("Category not found"));

        User client = userRepository.findById(command.getClientId())
                .orElseThrow(()-> new ResourceNotFoundException("Client not found"));

        User assignedTo  = null;
        if(command.getAssignedToId() != null){
            assignedTo = userRepository.findById(command.getAssignedToId()).orElseThrow(
                    ()-> new ResourceNotFoundException("Assigned user not found")
            );
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
        return ticketMapper.toDtoDetailsResponse(ticket);
    }

    @Override
    public TicketSummaryDtoResponse update(TicketCreateCommand command) {
        return null;
    }

    @Override
    public TicketSummaryDtoResponse getByIdSummary(Long id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Ticket not found")
        );
        return ticketMapper.toDtoSummaryResponse(ticket);
    }

    @Override
    public TicketDetailDtoResponse getDetailsById(Long id) {
        Ticket ticket = ticketRepository.findByIdWithDetails(id).orElseThrow(
                ()-> new ResourceNotFoundException("ticket not found")
        );
        return ticketMapper.toDtoDetailsResponse(ticket);
    }

    @Override
    public Page<TicketSummaryDtoResponse> getAllTicketsSummary(int page, int size) {
        Pageable pageable = PageRequest.of(page , size);
        return ticketRepository.findAll(pageable)
                .map(ticket -> ticketMapper.toDtoSummaryResponse(ticket));
    }

    @Override
    public Page<TicketDetailDtoResponse> getAllTicketDetailed(int page, int size) {
        return null;
    }

    @Override
    public void delete(Long id) {
        if(!ticketRepository.existsById(id)){
            throw new ResourceNotFoundException("Ticket not found");
        }
        ticketRepository.deleteById(id);
    }
}
