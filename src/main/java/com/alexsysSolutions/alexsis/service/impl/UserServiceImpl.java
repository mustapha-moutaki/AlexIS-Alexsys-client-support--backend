package com.alexsysSolutions.alexsis.service.impl;

import com.alexsysSolutions.alexsis.dto.request.user.UserCreateDtoRequest;
import com.alexsysSolutions.alexsis.dto.request.user.UserUpdateDtoRequest;
import com.alexsysSolutions.alexsis.dto.response.user.UserDtoResponse;
import com.alexsysSolutions.alexsis.enums.UserRole;
import com.alexsysSolutions.alexsis.exception.ResourceNotFoundException;
import com.alexsysSolutions.alexsis.exception.ValidationException;
import com.alexsysSolutions.alexsis.mapper.UserMapper;
import com.alexsysSolutions.alexsis.model.User;
import com.alexsysSolutions.alexsis.reposiotry.UserRepository;
import com.alexsysSolutions.alexsis.security.CustomUserDetails;
import com.alexsysSolutions.alexsis.security.context.CurrentUserProvider;
import com.alexsysSolutions.alexsis.service.UserService;
import com.alexsysSolutions.alexsis.util.PasswordUtil;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Transactional
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final CurrentUserProvider currentUser;

    @Override
    public UserDtoResponse create(UserCreateDtoRequest dto) {
        logger.info(" Creating new user with email: {}", dto.getEmail());

        // role assignment logic
        UserRole targetRole = (dto.getRole() == null) ? UserRole.ADMIN : dto.getRole();

        //  Role Validation
        // Check for AGENT or CLIENT
        if (targetRole == UserRole.AGENT || targetRole == UserRole.CLIENT) {
            logger.warn(" Attempt to create AGENT/CLIENT via admin endpoint. Role: {}", targetRole);
            throw new ValidationException(
                    String.format("Invalid role: To create a %s, please use the specialized endpoint.", targetRole)
            );
        }

        // Check for SUPER_ADMIN - 1 allowed for the system only
        if (targetRole == UserRole.SUPER_ADMIN) {
            logger.error(" Attempt to create SUPER_ADMIN (forbidden)");
            throw new ValidationException("You cannot create a Super Admin; this role is reserved for the system.");
        }

        //  Uniqueness Checks
        if(userRepository.findByEmail(dto.getEmail()).isPresent()){
            logger.warn(" Email already exists: {}", dto.getEmail());
            throw new ValidationException("User with this email already exists");
        }
        if(userRepository.findByUsername(dto.getUsername()).isPresent()){
            logger.warn(" Username already exists: {}", dto.getUsername());
            throw new ValidationException("User with this username already exists");
        }

        // Mapping and Saving
        User user = userMapper.toEntity(dto);

        // Hash password
        user.setPassword(PasswordUtil.hash(dto.getPassword()));

        // Apply the validated targetRole (IMPORTANT: don't hardcode ADMIN here)
        user.setRole(targetRole);
        user.setCreatedBy(currentUser.getEmail());

        User savedUser = userRepository.save(user);
        logger.info(" User created successfully with id: {}", savedUser.getId());
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserDtoResponse update(Long id, UserUpdateDtoRequest dto) {
        logger.info(" Updating user with id: {}", id);

        if(id.equals(1L)){
            logger.error(" Attempt to update SUPER_ADMIN (forbidden)");
            throw new ValidationException("You cannot update the Super Admin data");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("User not found with id: {}", id);
                    return new ResourceNotFoundException("User not found");
                });

        // Unique checks
        if(dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())){
            if(userRepository.findByEmail(dto.getEmail()).isPresent()){
                logger.warn(" Email already taken: {}", dto.getEmail());
                throw new ValidationException("Email already taken");
            }
        }

        //  REMOVED DUPLICATE PASSWORD HASHING - this was hashing password twice!
        // Hash password only once if provided
        if(dto.getPassword() != null){
            logger.debug("User password being updated");
            user.setPassword(PasswordUtil.hash(dto.getPassword()));
        }

        // Apply mapping and save
        userMapper.updateEntity(dto, user);
        User savedUser = userRepository.save(user);
        logger.info("User updated successfully with id: {}", id);

        return userMapper.toDto(savedUser);
    }

    @Override
    public UserDtoResponse getById(Long id) {
        logger.info("Fetching user with id: {}", id);
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> {
                    logger.error("User not found with id: {}", id);
                    return new ResourceNotFoundException("User not found");
                });
    }

    @Override
    public Page<UserDtoResponse> getAllIncludingDeleted(int page, int size) {
        logger.info("Fetching all users (including deleted) - page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDtoResponse> result = userRepository.findAll(pageable)
                .map(userMapper::toDto);
        logger.info("Retrieved {} total users", result.getTotalElements());
        return result;
    }

    @Override
    public Page<UserDtoResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction,
            UserRole role,
            LocalDateTime startDate,
            LocalDateTime endDate,
            boolean includeDeleted
    ) {
        logger.info("Fetching filtered users - page: {}, size: {}, role: {}, includeDeleted: {}",
                page, size, role, includeDeleted);

        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        // Call the repository with ALL parameters
        Page<UserDtoResponse> result = userRepository.findAllWithFilters(role, startDate, endDate, includeDeleted, pageable)
                .map(userMapper::toDto);
        logger.info("Retrieved {} filtered users", result.getTotalElements());
        return result;
    }



    @Override
    public void delete(Long id) {
        logger.info(" Deleting user with id: {}", id);

        if(id.equals(1L)) {
            logger.error("Attempt to delete SUPER_ADMIN (forbidden)");
            throw new ValidationException("You cannot delete the Super Admin");
        }

        // This trigger the @SQLDelete update automatically!
        if (!userRepository.existsById(id)) {
            logger.error("User not found for deletion with id: {}", id);
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(id);
        logger.info("User deleted successfully with id: {}", id);
    }

    // Implementing UserDetailsService to provide user details for Spring Security authentication
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Loading user details for email: {}", username);

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> {
                    logger.error("User not found with email: {}", username);
                    return new UsernameNotFoundException("User not found with email: " + username);
                });

        logger.debug("User found successfully with email: {}", username);
        return new CustomUserDetails(user);
    }
}
