package edu.uptc.swii.shiftmgmt.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import edu.uptc.swii.shiftmgmt.service.user.UserMgmtService;
import edu.uptc.swii.shiftmgmt.util.SendRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.uptc.swii.shiftmgmt.controller.dto.UserDTO;
import edu.uptc.swii.shiftmgmt.domain.model.Credentials;
import edu.uptc.swii.shiftmgmt.domain.model.User;

@RestController
@RequestMapping("/keycloak/user")
public class UserController {

    @Autowired
    private UserMgmtService userMgmtService;

    private SendRequest sendRequest = new SendRequest();

    @GetMapping("/hello-1")
    @PreAuthorize("hasRole('Administrators-client-role')")
    public String helloAdmin() {
        return "Hello Spring Boot With Keycloak ADMIN";
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('Administrators-client-role')")
    public ResponseEntity<?> findAllUsers() {
        return ResponseEntity.ok(userMgmtService.findAllUsers());
    }

    @GetMapping("/search/{username}")
    @PreAuthorize("hasRole('Administrators-client-role')")
    public ResponseEntity<?> findAllUsers(@PathVariable String username) {
        return ResponseEntity.ok(userMgmtService.searchUserByUsername(username));

    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
    public String createUser(@RequestBody Map<String, Object> requestData) {
        Credentials credentials = new Credentials();
        credentials.setCredential_password((String) requestData.get("credential_password"));
        User user = new User((Integer) requestData.get("user_id"), (String) requestData.get("user_first_name"),
                (String) requestData.get("user_last_name"),
                (String) requestData.get("user_address"), (String) requestData.get("user_email"),
                (String) requestData.get("user_organization"), (String) requestData.get("user_type"),
                credentials);
        userMgmtService.saveCredential(credentials);
        userMgmtService.saveUser(user);
        // ShiftlogsMgmtService.saveShiftLogs(new ShiftLogs(null, "Users", "Saving User"
        // + user.getUser_id() + user.getUser_email()));
        String json = "{ \"shiftlogs_table_name\":\"Users\", \"shiftlogs_action\":\"Creating Users\" }";
        sendRequest.sendPostRequest(json);
        // ikeycloakService.createUser(user, credentials.getCredential_password());
        return "Userid: " + user.getUser_id();
    }

    @RequestMapping(value = "/listAll", method = RequestMethod.GET, produces = "application/json")
    @PreAuthorize("hasRole('Administrators-client-role')")
    public List<User> listUsers() {
        String json = "{ \"shiftlogs_table_name\":\"Users\", \"shiftlogs_action\":\"Searching Users\" }";
        sendRequest.sendPostRequest(json);
        return userMgmtService.listAllUser();
    }

    @PostMapping("/create2")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) throws URISyntaxException {
        String response = userMgmtService.createUser(userDTO);
        return ResponseEntity.created(new URI("/keycloak/user/create")).body(response);
    }

    @PutMapping("/update/{userId}")
    @PreAuthorize("hasRole('Users-client-role') or hasRole('Administrators-client-role')")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody UserDTO userDTO) {
        userMgmtService.updateUser(userId, userDTO);
        return ResponseEntity.ok("User updated successfully!!");
    }

    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasRole('Administrators-client-role')")
    public ResponseEntity<?> deleteUser(@PathVariable String userId) {
        userMgmtService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

}
