-- 1. Insert Base Users
-- Password 'password' (pre-encoded if using Spring Security, otherwise raw for testing)
Insert INTO users (id, username, email, first_name, last_name, password, role, is_active, is_deleted, created_at)
VALUES
    (1, 'admin_user', 'admin@example.com', 'Alex', 'Admin', 'password', 'ADMIN', true, false, NOW()),
    (2, 'agent_smith', 'smith@example.com', 'John', 'Smith', 'password', 'AGENT', true, false, NOW()),
    (3, 'client_doe', 'doe@example.com', 'Jane', 'Doe', 'password', 'CLIENT', true, false, NOW());

-- 2. Insert Agents (ID must match the user ID)
INSERT INTO agents (id, specialization, availability_status, performance_rating, average_resolution_time)
VALUES
    (2, 'Technical_Support', 'AVAILABLE', 4.5, 300);
UPDATE agents SET specialization = 'TECHNICAL_SUPPORT' WHERE id = 2;
-- 3. Insert Clients (ID must match the user ID)
INSERT INTO clients (id, registration_date, satisfaction_score)
VALUES
    (3, '2024-01-10', 95.0);
INSERT INTO agents (id, specialization, availability_status)
VALUES (3, 'TECHNICAL_SUPPORT', 'AVAILABLE');