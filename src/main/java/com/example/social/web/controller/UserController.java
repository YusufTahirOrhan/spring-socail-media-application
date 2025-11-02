package com.example.social.web.controller;

import com.example.social.domain.entity.User;
import com.example.social.service.UserService;
import com.example.social.web.dto.UserDTOs;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTOs.UserResponse> getUser(@PathVariable Long id) {
        User user = userService.getUserVisibleById(id);

        return ResponseEntity.ok(new UserDTOs.UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRole().name(),
                user.getCreatedAt().toString()
        ));
    }

    @PutMapping("/users/me/password")
    public ResponseEntity<Void> updatePassword(@RequestBody @Valid UserDTOs.UpdatePasswordRequest updatePasswordRequest){
        userService.updateOwnPassword(updatePasswordRequest.currentPassword(), updatePasswordRequest.newPassword());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/me")
    public ResponseEntity<Void> deleteSelf(){
        userService.deleteSelf();

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/admin/users/{id}")
    public ResponseEntity<Void> adminDelete(@PathVariable Long id){
        userService.adminDeleteUser(id);

        return ResponseEntity.noContent().build();
    }
}
