package MFAX421A.Urlaubsplanung.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table
@Getter
@Setter
public class Urlaub {

    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private LocalDate startDate;
    private LocalDate endDate;
    private Urlaubstyp type;
    private String description;
    private Urlaubsstatus status;
    
}

enum Urlaubstyp{
    normal,
    special
}

