package MFAX421A.Urlaubsplanung.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

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
    private String vertretung;

    public List<LocalDate> daysBetween(){
        Predicate<LocalDate> isWeekend = date -> date.getDayOfWeek() == DayOfWeek.SATURDAY
                || date.getDayOfWeek() == DayOfWeek.SUNDAY;

        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        return Stream.iterate(startDate, date -> date.plusDays(1))
                .limit(daysBetween)
                .filter(isWeekend.negate())
                .toList();
    }

}

