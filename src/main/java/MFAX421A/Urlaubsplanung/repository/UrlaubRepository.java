package MFAX421A.Urlaubsplanung.repository;

import MFAX421A.Urlaubsplanung.models.Urlaub;
import MFAX421A.Urlaubsplanung.models.Urlaubsstatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UrlaubRepository extends JpaRepository<Urlaub, Long> {

    List<Urlaub> findAllByStatus(Urlaubsstatus status);


}
