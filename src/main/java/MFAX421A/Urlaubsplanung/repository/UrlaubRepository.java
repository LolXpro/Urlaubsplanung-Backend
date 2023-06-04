package MFAX421A.Urlaubsplanung.repository;

import MFAX421A.Urlaubsplanung.models.Urlaub;
import MFAX421A.Urlaubsplanung.models.Urlaubsstatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlaubRepository extends JpaRepository<Urlaub, Long> {

    List<Urlaub> findAllByStatus(Urlaubsstatus status);

    List<Urlaub> findAllByUsername(String username);

    List<Urlaub> findAllByUsernameAndStatus(String username, Urlaubsstatus status);

}
