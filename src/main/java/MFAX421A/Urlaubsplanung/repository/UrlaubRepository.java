package MFAX421A.Urlaubsplanung.repository;

import MFAX421A.Urlaubsplanung.models.Urlaub;
import MFAX421A.Urlaubsplanung.models.Urlaubsstatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlaubRepository extends JpaRepository<Urlaub, Long> {

    List<Urlaub> findAllByStatus(Urlaubsstatus status);

    List<Urlaub> findAllByUsername(String username);

    List<Urlaub> findAllByUsernameAndStatus(String username, Urlaubsstatus status);

    @Query("""
        SELECT u
        FROM Urlaub u
        where u.username
        in (select e.username from Employee e where e.abteilung = ?#{#abteilung})
    """)
    List<Urlaub> findAllByAbteilung(@Param("abteilung") String abteilung);

    @Query("""
        SELECT u
        FROM Urlaub u
        where u.username
        in (select e.username from Employee e where e.abteilung = ?#{#abteilung}
        and u.username = ?#{#username})
        and u.status = ?#{#status}
    """)
    List<Urlaub> findAllByUsernameAndStatusAndAbteilung(@Param("username") String username,
                                                        @Param("status") Urlaubsstatus status,
                                                        @Param("abteilung") String abteilung);

    @Query("""
        SELECT u
        FROM Urlaub u
        where u.username
        in (select e.username from Employee e where e.abteilung = ?#{#abteilung}
        and u.username = ?#{#username})
    """)
    List<Urlaub> findAllByUsernameAbteilung(@Param("username") String username,
                                            @Param("abteilung") String abteilung);

    @Query("""
        SELECT u
        FROM Urlaub u
        where u.username
        in (select e.username from Employee e where e.abteilung = ?#{#abteilung})
        and u.status = ?#{#status}
    """)
    List<Urlaub> findAllByStatusAndAbteilung(@Param("status") Urlaubsstatus status,
                                             @Param("abteilung") String abteilung);
}
