# Internship Report – Project Task List (Ticketing System)

---

## 1. Project Setup & Architecture Design
- Setup Spring Boot project structure
- Define layered architecture (Controller / Service / Repository)
- Configure database connection (PostgreSQL/MySQL)
- Organize project packages (users, tickets, agents, security)

---

## 2. User & Role Management
- Implement User entity with role-based system (ADMIN, AGENT, CLIENT)
- Configure JWT-based authentication system
- Implement Spring Security configuration
- Add role-based access control (@PreAuthorize)
- Create default SuperAdmin initialization at application startup

---

## 3. Ticket Management Module
- Create Ticket entity (status, priority, timestamps)
- Implement CRUD operations for tickets
- Define ticket lifecycle:
    - OPEN → IN_PROGRESS → RESOLVED → CLOSED
- Implement ticket assignment to agents

---

## 4. Agent Management System
- Create Agent entity using inheritance from User
- Add agent-specific attributes:
    - activeTicketsCount
    - maxCapacity
    - lastAssignedAt
    - availabilityStatus
    - performance metrics
- Implement query to retrieve available agents

---

## 5. File Upload & Attachment System
- Implement file upload using MultipartFile
- Store files in local server directory
- Generate unique file names using UUID
- Save file metadata in database (name, type, size, URL)
- Implement file deletion from storage and database

---

## 6. Ticket Status & Priority Management
- Implement ticket status update endpoint
- Implement priority update endpoint
- Add validation rules for status transitions
- Prevent invalid transitions (e.g., modifying CLOSED tickets)
- Manage timestamps:
    - resolvedAt
    - closedAt

---

## 7. Auto Ticket Assignment System
- Implement auto-assignment service for agents
- Use strategy pattern for assignment logic
- Select agents based on:
    - availability
    - workload (activeTicketsCount)
    - capacity (maxCapacity)
- Apply load balancing mechanism

---

## 8. Scheduler (Background Processing)
- Implement @Scheduled background job
- Run periodic checks for unassigned tickets
- Trigger auto-assignment after SLA expiration
- Optimize performance with filtered database queries

---

## 9. SLA (Service Level Agreement) Logic
- Define SLA per priority level:
    - CRITICAL → 7 minutes
    - HIGH → 1 hour
    - MEDIUM → 4 hours
    - LOW → 24 hours
- Compare ticket creation time with SLA limits
- Trigger automation when SLA is exceeded

---

## 10. DTOs & Mapping Layer
- Create request and response DTOs
- Implement mapping layer (Entity ↔ DTO)
- Separate API layer from database models
- Ensure clean data transfer structure

---

## 11. Exception Handling & Validation
- Implement global exception handler
- Create custom exceptions:
    - ResourceNotFoundException
    - ValidationException
- Validate incoming requests using @Valid
- Improve API error consistency

---

## 12. API Documentation
- Integrate Swagger / OpenAPI
- Document all endpoints
- Add request/response examples
- Improve API readability for external users

---

## 13. Logging & Monitoring
- Implement SLF4J logging system
- Log key business actions:
    - ticket creation
    - assignment
    - status updates
- Monitor scheduler execution logs

---

# Advanced (Architecture & Engineering Highlights)

- Designed scalable layered architecture using Spring Boot
- Implemented rule-based auto-assignment system
- Built SLA-driven workflow automation engine
- Applied Strategy Design Pattern for agent selection
- Used inheritance model for Agent/User structure
- Integrated background processing using scheduler
- Ensured separation of concerns across all layers