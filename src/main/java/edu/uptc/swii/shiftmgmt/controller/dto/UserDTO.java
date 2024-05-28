package edu.uptc.swii.shiftmgmt.controller.dto;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
@Builder
public class UserDTO {
    // private String id;
    private String userName;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String organization;
}
