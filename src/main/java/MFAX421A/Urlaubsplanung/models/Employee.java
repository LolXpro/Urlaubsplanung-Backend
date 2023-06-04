package MFAX421A.Urlaubsplanung.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
public class Employee {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private int urlaubstage = 28;
    private int sonderurlaubstage = 2;
}
