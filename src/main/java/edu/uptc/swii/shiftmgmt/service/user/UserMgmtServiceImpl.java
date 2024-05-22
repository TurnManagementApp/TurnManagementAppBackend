package edu.uptc.swii.shiftmgmt.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.uptc.swii.shiftmgmt.domain.model.Credentials;
import edu.uptc.swii.shiftmgmt.domain.model.User;
import edu.uptc.swii.shiftmgmt.domain.repository.CredentialRepository;
import edu.uptc.swii.shiftmgmt.domain.repository.UserRepository;


@Service
public class UserMgmtServiceImpl implements UserMgmtService{
    @Autowired
    UserRepository userRepo;
    @Autowired
    CredentialRepository credRepo;
    
    @Override
    public void saveUser(User user){
        userRepo.save(user);

    }

    @Override
    public void saveCredential(Credentials credentials) {
        credRepo.save(credentials);
    }

    @Override
    public List<User> listAllUser() {
        return userRepo.findAll();
    }

    // @Override
    // public User findByUserId(String userId) {
    //     return userRepo.findByUserId(userId);
    // }

    // @Override
    // public void deleteUser(String UserId){
    //     User user = userRepo.findByUserId(UserId);
    //     userRepo.delete(user);
    // }

    // @Override
    // public List<User> listAllUser(){
    //     return userRepo.findAll();
    // }

}
