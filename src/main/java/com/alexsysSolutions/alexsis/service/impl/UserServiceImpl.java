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
import com.alexsysSolutions.alexsis.service.UserService;
import com.alexsysSolutions.alexsis.util.PasswordUtil;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Transactional
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDtoResponse create(UserCreateDtoRequest dto) {
        if(userRepository.findByEmail(dto.getEmail()).isPresent()){
            throw new ValidationException("User with this email already exists");
        }
        if(userRepository.findByUsername(dto.getUsername()).isPresent()){
            throw new ValidationException("User with this username already exists");
        }

        User user = userMapper.toEntity(dto);
        user.setPassword(PasswordUtil.hash(dto.getPassword()));
/*
        after i complete security feature , i'm gonna use jpa auditing to set
        createdBy
 */
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDtoResponse update(Long id, UserUpdateDtoRequest dto) {
        if(id.equals(1L)){
            throw new ValidationException("You cannot update the Super Admin data");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Unique checks
        if(dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())){
            if(userRepository.findByEmail(dto.getEmail()).isPresent())
                throw new ValidationException("Email already taken");
        }

        // Apply mapping and save
        userMapper.updateEntity(dto, user);

        // If password was provided in DTO, re-hash it
        if(dto.getPassword() != null) {
            user.setPassword(PasswordUtil.hash(dto.getPassword()));
        }

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDtoResponse getById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public Page<UserDtoResponse> getAllIncludingDeleted(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable)
                .map(userMapper::toDto);
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

        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        // Call the repository with ALL parameters
        return userRepository.findAllWithFilters(role, startDate, endDate, includeDeleted, pageable)
                .map(userMapper::toDto);
    }



    @Override
    public void delete(Long id) {
        if(id.equals(1L)) {
            throw new ValidationException("You cannot delete the Super Admin");
        }

        // This trigger the @SQLDelete update automatically!
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }
}