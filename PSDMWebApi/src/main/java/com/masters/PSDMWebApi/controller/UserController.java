package com.masters.PSDMWebApi.controller;

import com.masters.PSDMWebApi.dto.UserDTO;
import com.masters.PSDMWebApi.mapper.UserMapper;
import com.masters.PSDMWebApi.model.User;
import com.masters.PSDMWebApi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(UserMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/currentSubSession/{id}")
    public ResponseEntity<Long> getCurrentSubSession(@PathVariable Long id) {
        Long res = userService.getCurrentSubsessionId(id);
        log.debug("Request to getCurrentSubSession: {}", res );
        return ResponseEntity.ok(res);

    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO dto) {
        User user = UserMapper.toEntity(dto);
        User saved = userService.createUser(user);
        return ResponseEntity.ok(UserMapper.toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO dto) {
        User userToUpdate = UserMapper.toEntity(dto);
        User updated = userService.updateUser(id, userToUpdate);
        return ResponseEntity.ok(UserMapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

