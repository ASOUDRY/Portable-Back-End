package com.soudry.portable_back_end.user.admin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.soudry.portable_back_end.user.controllerDto.ErrorResponse;
import com.soudry.portable_back_end.user.repo.Users;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    @PatchMapping("/adminPromotion/{id}")
    public ResponseEntity<?> promoteUser(@PathVariable String id) {
        Optional<UserResponse> updated = adminService.promoteToAdmin(id);
        if (updated.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("User not found"));
        }
        return ResponseEntity.ok(updated.get());
    }
    @PatchMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody UpdateAnyUser request) {
        Optional<Users> updatedUser = adminService.updateAnyUser(request);
        if (updatedUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("User not found"));
        }
        return ResponseEntity.ok(updatedUser.get());
    }
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        boolean deleted = adminService.deleteAnyUser(id);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("User not found"));
        }
        return ResponseEntity.noContent().build();
    }
}