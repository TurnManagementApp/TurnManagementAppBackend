package edu.uptc.swii.shiftmgmt.controller;

import java.util.List;
import java.util.Map;

import edu.uptc.swii.shiftmgmt.service.user.UserMgmtService;
import edu.uptc.swii.shiftmgmt.util.SendRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.uptc.swii.shiftmgmt.domain.model.Credentials;
import edu.uptc.swii.shiftmgmt.domain.model.User;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserMgmtService userMgmtService;

    private SendRequest sendRequest = new SendRequest();
    //@Autowired
    //private ShiftlogsMgmtService ShiftlogsMgmtService;

    //@Autowired
    //private IkeycloakService ikeycloakService;

    @GetMapping("/hello-1")
    //@PreAuthorize("hasRole('admin-backend')")
    public String helloAdmin(){
        return "Hello ADMIN";
    }

    @GetMapping("/hello-2")
    //@PreAuthorize("hasRole('users-backend')") //or hashRole()
    public String helloUser(){
        return "Hello USER";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
    public String createUser(@RequestBody Map<String, Object> requestData) {
        Credentials credentials = new Credentials();
        credentials.setCredential_password((String) requestData.get("credential_password"));
        User user = new User((Integer) requestData.get("user_id"), (String) requestData.get("user_first_name"), (String) requestData.get("user_last_name"),
        (String) requestData.get("user_address"),(String) requestData.get("user_email"), (String) requestData.get("user_organization"), (String) requestData.get("user_type"), 
        credentials);
        userMgmtService.saveCredential(credentials);
        userMgmtService.saveUser(user);
        //ShiftlogsMgmtService.saveShiftLogs(new ShiftLogs(null, "Users", "Saving User" + user.getUser_id() + user.getUser_email()));
        String json = "{ \"shiftlogs_table_name\":\"Users\", \"shiftlogs_action\":\"Creating Users\" }";
        sendRequest.sendPostRequest(json);
        //ikeycloakService.createUser(user, credentials.getCredential_password());
        return "Userid: " + user.getUser_id();
    }

    @RequestMapping(value = "/listAll", method = RequestMethod.GET, produces = "application/json")
    public List<User> listUsers(){
        String json = "{ \"shiftlogs_table_name\":\"Users\", \"shiftlogs_action\":\"Searching Users\" }";
        sendRequest.sendPostRequest(json);
        return userMgmtService.listAllUser();
    }

}
