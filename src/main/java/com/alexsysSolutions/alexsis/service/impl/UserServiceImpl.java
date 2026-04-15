package com.alexsysSolutions.alexsis.service.impl;

import com.alexsysSolutions.alexsis.dto.request.user.UserCreateDtoRequest;
import com.alexsysSolutions.alexsis.dto.request.user.UserUpdateDtoRequest;
import com.alexsysSolutions.alexsis.dto.response.user.UserDtoResponse;
import com.alexsysSolutions.alexsis.exception.ResourceNotFoundException;
import com.alexsysSolutions.alexsis.exception.ValidationException;
import com.alexsysSolutions.alexsis.mapper.UserMapper;
import com.alexsysSolutions.alexsis.model.User;
import com.alexsysSolutions.alexsis.reposiotry.UserRepositroy;
import com.alexsysSolutions.alexsis.service.UserService;
import com.alexsysSolutions.alexsis.util.PasswordUtil;
import jakarta.transaction.Transactional;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
@Transactional
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepositroy userRepositroy;
    private final UserMapper userMapper;

    @Override
    public UserDtoResponse create(UserCreateDtoRequest dto) {
        if(userRepositroy.findByEmail(dto.getEmail()).isPresent()){
            throw new ValidationException("User with this email already exists");
        }
        if(userRepositroy.findByUsername(dto.getUsername()).isPresent()){
            throw new ValidationException("User with this username already exists");
        }

        User user = userMapper.toEntity(dto);
        user.setPassword(PasswordUtil.hash(dto.getPassword()));
        return userMapper.toDto(userRepositroy.save(user));
    }

    @Override
    public UserDtoResponse update(Long id, UserUpdateDtoRequest dto) {
        // FIXED LOGIC: Only block if it is the ACTUAL Super Admin (usually ID 1)
        if(id.equals(1L)){
            throw new ValidationException("You cannot update the Super Admin data");
        }

        User user = userRepositroy.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Unique checks
        if(dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())){
            if(userRepositroy.findByEmail(dto.getEmail()).isPresent())
                throw new ValidationException("Email already taken");
        }

        // Apply mapping and save
        userMapper.updateEntity(dto, user);

        // If password was provided in DTO, re-hash it
        if(dto.getPassword() != null) {
            user.setPassword(PasswordUtil.hash(dto.getPassword()));
        }

        return userMapper.toDto(userRepositroy.save(user));
    }

    @Override
    public UserDtoResponse getById(Long id) {
        return userRepositroy.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public Page<UserDtoResponse> getAll(int page, int size, String sortBy, String direction,
                                        String role, LocalDateTime startDate, LocalDateTime endDate) {

        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);


        return userRepositroy.findAllWithFilters(role, startDate, endDate, pageable)
                .map(user -> userMapper.toDto(user));
    }

    @Override
    public Page<UserDtoResponse> getAllIncludingDeleted(int page, int size) {
        return userRepositroy.findAll(PageRequest.of(page, size))
                .map(userMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        // FIXED LOGIC: Block only the Super Admin
        if(id.equals(1L)){
            throw new ValidationException("You cannot delete the Super Admin");
        }

        User user = userRepositroy.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );

        user.setDeleted(true);
        user.setActive(false);
        userRepositroy.save(user);
    }
}