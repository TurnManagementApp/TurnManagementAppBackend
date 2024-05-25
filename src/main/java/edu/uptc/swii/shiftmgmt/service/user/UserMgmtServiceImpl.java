package edu.uptc.swii.shiftmgmt.service.user;

import java.util.List;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import edu.uptc.swii.shiftmgmt.controller.dto.UserDTO;
import edu.uptc.swii.shiftmgmt.util.KeycloackProvider;
import jakarta.validation.constraints.NotNull;

import edu.uptc.swii.shiftmgmt.domain.model.Credentials;
import edu.uptc.swii.shiftmgmt.domain.model.User;
import edu.uptc.swii.shiftmgmt.domain.repository.CredentialRepository;
import edu.uptc.swii.shiftmgmt.domain.repository.UserRepository;


@Service
@Slf4j
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

    @Override
    public List<UserRepresentation> findAllUsers() {
        return KeycloackProvider.getRealmResource()
                .users()
                .list();
    }

    @Override
    public List<UserRepresentation> searchUserByUsername(String username) {
        return KeycloackProvider.getRealmResource()
                .users()
                .searchByUsername(username, true);
    }

    @Override
    public String createUser(@NotNull UserDTO userDTO) {
                
        int status = 0;
        UsersResource userResource = KeycloackProvider.getUserResource();

        UserRepresentation userRepresentation = new UserRepresentation(); 
        userRepresentation.setFirstName(userDTO.getFirstName());
        userRepresentation.setLastName(userDTO.getLastName());
        userRepresentation.setEmail (userDTO.getEmail());
        userRepresentation.setUsername(userDTO.getUserName());
        userRepresentation.setEmailVerified(true); userRepresentation.setEnabled(true);

        Response response = userResource.create(userRepresentation); 
        status = response.getStatus();
        
        if(status == 201){
            String path = response.getLocation().getPath();
            String userId = path.substring(path.lastIndexOf("/") + 1);

            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setTemporary(false);
            credentialRepresentation.setType(OAuth2Constants.PASSWORD);
            credentialRepresentation.setValue(userDTO.getPassword());

            userResource.get(userId).resetPassword(credentialRepresentation);

            RealmResource realmResource = KeycloackProvider.getRealmResource();
            List<RoleRepresentation> roleRepresentations = null;

            if(userDTO.getRoles() == null || userDTO.getRoles().isEmpty()){
                roleRepresentations = List.of(realmResource.roles().get("users-role-TurnsManagementApp").toRepresentation());
            } else {
                roleRepresentations = realmResource.roles()
                        .list()
                        .stream()
                        .filter(role -> userDTO.getRoles()
                                .stream()
                                .anyMatch(roleName -> roleName.equalsIgnoreCase(role.getName())))
                        .toList();
            }
            realmResource.users().get(userId).roles().realmLevel().add(roleRepresentations);
            return "User create successfully";
            
        }else if(status == 409){
            log.error("User exist already");
            return "usuario ya existe";
        }else {
            log.error("Error creating user");
            return "contacte al administrador";
        }
    }

    @Override
    public void deleteUser(String userId) {
        KeycloackProvider.getUserResource()
        .get(userId)
        .remove();
    }

    @Override
    public void updateUser(String userId, UserDTO userDTO) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(OAuth2Constants.PASSWORD);
        credentialRepresentation.setValue(userDTO.getPassword());

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(userDTO.getFirstName());
        userRepresentation.setLastName(userDTO.getLastName());
        userRepresentation.setEmail(userDTO.getEmail());
        userRepresentation.setUsername(userDTO.getEmail().split("@")[0]);
        userRepresentation.setEmailVerified(true); // ideal enviar un correo electronico para verificarlo
        userRepresentation.setEnabled(true);
        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));

        UserResource userResource = KeycloackProvider.getUserResource().get(userId);
        userResource.update(userRepresentation);
    }

}
