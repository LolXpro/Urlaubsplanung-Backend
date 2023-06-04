package MFAX421A.Urlaubsplanung;

import MFAX421A.Urlaubsplanung.models.Employee;
import MFAX421A.Urlaubsplanung.models.Urlaub;
import MFAX421A.Urlaubsplanung.models.Urlaubsstatus;
import MFAX421A.Urlaubsplanung.models.Urlaubstyp;
import MFAX421A.Urlaubsplanung.repository.EmployeeRepository;
import MFAX421A.Urlaubsplanung.repository.UrlaubRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;


@Slf4j
@RestController
@RequestMapping(path = "/urlaube")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UrlaubController {

    final UrlaubRepository urlaubRepository;
    final EmployeeRepository employeeRepository;

    @GetMapping
    public List<Urlaub> getUrlaube(@RequestParam(required = false) Urlaubsstatus status, @RequestParam(required = false) String username){

        if (status != null && username != null) return urlaubRepository.findAllByUsernameAndStatus(username, status);
        if (status != null) return urlaubRepository.findAllByStatus(status);
        if (username != null) return urlaubRepository.findAllByUsername(username);

        return urlaubRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Void> postUrlaub(@RequestBody Urlaub urlaub){

        Employee employee = employeeRepository.findByUsername(urlaub.getUsername());

        Predicate<LocalDate> isWeekend = date -> date.getDayOfWeek() == DayOfWeek.SATURDAY
                || date.getDayOfWeek() == DayOfWeek.SUNDAY;

        long daysBetween = ChronoUnit.DAYS.between(urlaub.getStartDate(), urlaub.getEndDate()) + 1;

        List<LocalDate> workDays = Stream.iterate(urlaub.getStartDate(), date -> date.plusDays(1))
                .limit(daysBetween)
                .filter(isWeekend.negate())
                .toList();
        long actualDaysBetween = workDays.size();
        log.info(String.valueOf(workDays));

        if(urlaub.getType() == Urlaubstyp.normal && employee.getUrlaubstage() >= actualDaysBetween) {
            employee.setUrlaubstage((int) (employee.getUrlaubstage() - actualDaysBetween));
            urlaubRepository.save(urlaub);

            return new ResponseEntity<>(HttpStatus.OK);
        }
        if(urlaub.getType() == Urlaubstyp.special && employee.getSonderurlaubstage() >= actualDaysBetween) {
            employee.setSonderurlaubstage((int) (employee.getSonderurlaubstage() - actualDaysBetween));
            urlaubRepository.save(urlaub);

            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Void> putStatus(@PathVariable Long id, @RequestParam String status){

        Optional<Urlaub> optionalUrlaub = urlaubRepository.findById(id);
        if(optionalUrlaub.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        Urlaub urlaub = optionalUrlaub.get();
        switch (status) {
            case "bearbeitung" -> urlaub.setStatus(Urlaubsstatus.bearbeitung);
            case "genehmigt" -> urlaub.setStatus(Urlaubsstatus.genehmigt);
            case "abgelehnt" -> urlaub.setStatus(Urlaubsstatus.abgelehnt);
        }

        urlaubRepository.save(urlaub);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteUrlaub(@PathVariable Long id){
        urlaubRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}


