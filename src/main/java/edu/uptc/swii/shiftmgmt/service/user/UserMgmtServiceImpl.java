package edu.uptc.swii.shiftmgmt.service.user;

import java.util.Collections;
import java.util.List;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.uptc.swii.shiftmgmt.controller.dto.UserDTO;
import edu.uptc.swii.shiftmgmt.domain.model.Credentials;
import edu.uptc.swii.shiftmgmt.domain.model.User;
import edu.uptc.swii.shiftmgmt.domain.repository.CredentialRepository;
import edu.uptc.swii.shiftmgmt.domain.repository.UserRepository;
import edu.uptc.swii.shiftmgmt.util.KeycloakProvider;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserMgmtServiceImpl implements UserMgmtService {
    @Autowired
    UserRepository userRepo;
    @Autowired
    CredentialRepository credRepo;

    @Override
    public void saveUser(User user) {
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
        return KeycloakProvider.getRealmResource()
                .users()
                .list();
    }

    @Override
    public List<UserRepresentation> searchUserByUsername(String username) {
        return KeycloakProvider.getRealmResource()
                .users()
                .searchByUsername(username, true);
    }

    @Override
    public User findByUser_email(String user_email) {
        return userRepo.findByUseremail(user_email);
    }

    @Override
    public String createUser(@NotNull UserDTO userDTO) {

        int status = 0;
        UsersResource userResource = KeycloakProvider.getUserResource();

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(userDTO.getFirstName());
        userRepresentation.setLastName(userDTO.getLastName());
        userRepresentation.setEmail(userDTO.getEmail());
        userRepresentation.setUsername(userDTO.getUserName());
        userRepresentation.setEmailVerified(true);
        userRepresentation.setEnabled(true);

        Response response = userResource.create(userRepresentation);
        status = response.getStatus();

        if (status == 201) {
            String path = response.getLocation().getPath();
            String userId = path.substring(path.lastIndexOf("/") + 1);

            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setTemporary(false);
            credentialRepresentation.setType(OAuth2Constants.PASSWORD);
            credentialRepresentation.setValue(userDTO.getPassword());

            userResource.get(userId).resetPassword(credentialRepresentation);

            RealmResource realmResource = KeycloakProvider.getRealmResource();
            List<RoleRepresentation> roleRepresentations = null;
            roleRepresentations = List
                    .of(realmResource.roles().get("users-role-TurnsManagementApp").toRepresentation());
            realmResource.users().get(userId).roles().realmLevel().add(roleRepresentations);

            ClientRepresentation clientRep = realmResource.clients().findByClientId("spring-client-api-rest").get(0);
            String clientId = clientRep.getClientId(); // Obtener el ID del cliente
            RoleRepresentation clientRole = realmResource.clients().get(clientId).roles().get("Users-client-role")
                    .toRepresentation();

            return "User create successfully";

        } else if (status == 409) {
            log.error("User exist already");
            return "User exist already";
        } else {
            log.error("Error creating user");
            return "Error creating user";
        }
    }

    @Override
    public void deleteUser(String userId) {
        KeycloakProvider.getUserResource()
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

        UserResource userResource = KeycloakProvider.getUserResource().get(userId);
        userResource.update(userRepresentation);
    }

}
