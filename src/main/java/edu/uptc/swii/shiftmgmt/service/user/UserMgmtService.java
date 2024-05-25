package edu.uptc.swii.shiftmgmt.service.user;

import java.util.List;

import edu.uptc.swii.shiftmgmt.controller.dto.UserDTO;
import edu.uptc.swii.shiftmgmt.domain.model.Credentials;
import edu.uptc.swii.shiftmgmt.domain.model.User;
import org.keycloak.representations.idm.UserRepresentation;

public interface UserMgmtService {
    public void saveUser(User user);
    public void saveCredential(Credentials credentials);

    public List<User> listAllUser();

    List<UserRepresentation> findAllUsers();

    List<UserRepresentation> searchUserByUsername(String username);

    String createUser(UserDTO userDTO);

    void deleteUser(String userId);

    void updateUser(String userId, UserDTO userDTO);
}
