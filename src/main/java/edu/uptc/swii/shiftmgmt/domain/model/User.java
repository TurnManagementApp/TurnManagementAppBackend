package edu.uptc.swii.shiftmgmt.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Getter
    @Setter
    private Integer user_id;
    @Getter
    @Setter
    private String user_first_name;
    @Getter
    @Setter
    private String user_last_name;
    @Getter
    @Setter
    private String user_address;
    @Getter
    @Setter
    private String user_email;
    @Getter
    @Setter
    private String user_organization;
    @Getter
    @Setter
    private String user_type;
    @Getter
    @Setter
    private Set<String> user_roles;

    @OneToOne
    @JoinColumn(name = "credential_id")
    private Credentials credentials;

    public User(Integer user_id, String user_first_name, String user_last_name, String user_address, String user_email,
            String user_organization, String user_type, Credentials credentials) {
        this.user_id = user_id;
        this.user_first_name = user_first_name;
        this.user_last_name = user_last_name;
        this.user_address = user_address;
        this.user_email = user_email;
        this.user_organization = user_organization;
        this.user_type = user_type;
        this.credentials = credentials;
    }

    public User() {
    }

}
