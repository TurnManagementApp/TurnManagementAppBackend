package edu.uptc.swii.shiftmgmt.domain.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "credentials")
public class Credentials {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter @Setter
    private Integer credential_id;
    @Getter @Setter
    private String credential_password;
    @Getter @Setter
    private String credential_token;

    @OneToOne(mappedBy = "credentials", cascade = CascadeType.ALL)
    private User user;
}
