package MFAX421A.Urlaubsplanung;

import MFAX421A.Urlaubsplanung.models.Urlaub;
import MFAX421A.Urlaubsplanung.models.Urlaubsstatus;
import MFAX421A.Urlaubsplanung.repository.UrlaubRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UrlaubController {

    final UrlaubRepository urlaubRepository;

    @GetMapping(path = "/urlaube")
    public List<Urlaub> getUrlaube(@RequestParam(required = false) Urlaubsstatus status){

        if (status != null) return urlaubRepository.findAllByStatus(status);

        return urlaubRepository.findAll();
    }

}
