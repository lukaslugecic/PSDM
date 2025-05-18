package com.masters.PSDMWebApi.controller;

import com.masters.PSDMWebApi.dto.UserDTO;
import com.masters.PSDMWebApi.mapper.UserMapper;
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

    @GetMapping("/currentSubSession/{id}")
    public ResponseEntity<Long> getCurrentSubSession(@PathVariable Long id) {
        Long res = userService.getCurrentSessionId(id, true);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/checkParentSession/{id}")
    public ResponseEntity<Boolean> checkParentSession(@PathVariable Long id) {
        Boolean res = userService.checkParentSession(id);
        return ResponseEntity.ok(res);
    }
}

