package edu.uptc.swii.shiftmgmt.domain.model;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Document(collection = "appshifts")
public class Shift {
    @Id
    @Getter
    @Setter
    private Integer _id;
    @Getter
    @Setter
    private Integer shift_user_id;
    @Getter
    @Setter
    private String shift_dependent;
    @Getter
    @Setter
    private String shift_date;

    public Shift(Integer shift_user_id, String shift_dependent, String shift_date) {
        this.shift_user_id = shift_user_id;
        this.shift_dependent = shift_dependent;
        this.shift_date = shift_date;
        this._id = shift_user_id;
    }

}
