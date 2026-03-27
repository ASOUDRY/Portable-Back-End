package com.soudry.portable_back_end.user.privateLogic;
import org.springframework.stereotype.Service;

import com.soudry.portable_back_end.user.repo.UserRepo;
import com.soudry.portable_back_end.user.repo.Users;

@Service
public class PrivateService {

    private final UserRepo userRepo;

    public PrivateService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }
    public Users updateUser(Users user, Users updatedUser) {
        if (updatedUser.getName() != null) {
            user.setName(updatedUser.getName());
        }    
        if (updatedUser.getPassword() != null) {
            user.setPassword(updatedUser.getPassword());
        }
        if (updatedUser.getEmail() != null) {
            user.setEmail(updatedUser.getEmail());
        }        
        return userRepo.save(user);
    }
}
