package MFAX421A.Urlaubsplanung;

import MFAX421A.Urlaubsplanung.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping(path = "/benutzer")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class EmployeeController {

    final EmployeeRepository employeeRepository;


    @GetMapping(path = "/{username}")
    public int getUrlaubstage(@PathVariable String username,@RequestParam String typ){
        if (Objects.equals(typ, "special"))  return employeeRepository.findByUsername(username).getSonderurlaubstage();
        else return employeeRepository.findByUsername(username).getUrlaubstage();
    }

}
