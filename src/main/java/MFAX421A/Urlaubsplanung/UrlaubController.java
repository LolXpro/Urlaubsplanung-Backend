package MFAX421A.Urlaubsplanung;

import MFAX421A.Urlaubsplanung.models.Employee;
import MFAX421A.Urlaubsplanung.models.Urlaub;
import MFAX421A.Urlaubsplanung.models.Urlaubsstatus;
import MFAX421A.Urlaubsplanung.models.Urlaubstyp;
import MFAX421A.Urlaubsplanung.repository.EmployeeRepository;
import MFAX421A.Urlaubsplanung.repository.UrlaubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/urlaube")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UrlaubController {

    final UrlaubRepository urlaubRepository;
    final EmployeeRepository employeeRepository;

    @GetMapping
    public List<Urlaub> getUrlaube(@RequestParam(required = false) Urlaubsstatus status,
                                   @RequestParam(required = false) String username,
                                   @RequestParam(required = false) String abteilung){

        if(status != null && username != null && abteilung != null) return urlaubRepository.findAllByUsernameAndStatusAndAbteilung(username, status, abteilung);
        if(username != null && abteilung != null) return urlaubRepository.findAllByUsernameAbteilung(username, abteilung);
        if(status != null && abteilung != null) return urlaubRepository.findAllByStatusAndAbteilung(status, abteilung);
        if(status != null && username != null) return urlaubRepository.findAllByUsernameAndStatus(username, status);
        if(status != null) return urlaubRepository.findAllByStatus(status);
        if(username != null) return urlaubRepository.findAllByUsername(username);
        if(abteilung != null) return urlaubRepository.findAllByAbteilung(abteilung);

        return urlaubRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Void> postUrlaub(@RequestBody Urlaub urlaub){

        Employee employee = employeeRepository.findByUsername(urlaub.getUsername());

        int dayCount = urlaub.daysBetween().size();

        if(urlaub.getType() == Urlaubstyp.normal) {
            if(employee.getUrlaubstage() < dayCount) urlaub.setStatus(Urlaubsstatus.abgelehnt);
            else {
                employee.setUrlaubstage((employee.getUrlaubstage() - dayCount));
                checkAbteilung(urlaub);
            }
            urlaubRepository.save(urlaub);

            return new ResponseEntity<>(HttpStatus.OK);
        }
        if(urlaub.getType() == Urlaubstyp.special) {
            if(employee.getSonderurlaubstage() < dayCount) urlaub.setStatus(Urlaubsstatus.abgelehnt);
            else {
                employee.setSonderurlaubstage((employee.getSonderurlaubstage() - dayCount));
            }
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
            case "abgelehnt" -> {
                urlaub.setStatus(Urlaubsstatus.abgelehnt);

                Employee employee = employeeRepository.findByUsername(urlaub.getUsername());

                int dayCount = urlaub.daysBetween().size();
                if (urlaub.getType() == Urlaubstyp.normal)
                    employee.setUrlaubstage((employee.getUrlaubstage() + dayCount));
                if(urlaub.getType() == Urlaubstyp.special)
                    employee.setSonderurlaubstage((employee.getSonderurlaubstage() + dayCount));
            }


        }

        urlaubRepository.save(urlaub);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteUrlaub(@PathVariable Long id){
        urlaubRepository.findById(id).ifPresent(urlaub -> {

            Employee employee = employeeRepository.findByUsername(urlaub.getUsername());

            if(urlaub.getStatus() != Urlaubsstatus.abgelehnt){
                int dayCount = urlaub.daysBetween().size();
                if (urlaub.getType() == Urlaubstyp.normal)
                    employee.setUrlaubstage((employee.getUrlaubstage() + dayCount));
                if(urlaub.getType() == Urlaubstyp.special)
                    employee.setSonderurlaubstage((employee.getSonderurlaubstage() + dayCount));
                }
        });


        urlaubRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void checkAbteilung(Urlaub urlaub){

        long otherEmployees = (employeeRepository.findAll().size() -1);
        List<Employee> notImUrlaub = employeeRepository.findAllByNotImUrlaubOrUsername(urlaub.getUsername());
        urlaub.setVertretung(String.valueOf(notImUrlaub.get(0)));

        List<LocalDate> urlaubstage = urlaub.daysBetween();
        List<Urlaub> urlaubList = urlaubRepository
                .findAllByStatusAndAbteilung(
                        Urlaubsstatus.bearbeitung,
                        employeeRepository.findByUsername(urlaub.getUsername()
                        ).getAbteilung());
        for (Urlaub val: urlaubList) {
            List<LocalDate> valTage = val.daysBetween();
            for (LocalDate date: valTage) {
                if (urlaubstage.contains(date)) {
                    otherEmployees--;
                    break;
                }
            }
        }
        if(otherEmployees < 4) return;

        urlaub.setStatus(Urlaubsstatus.genehmigt);
    }

}


