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
    
    @Query("""
        SELECT u
        FROM Urlaub u
        where u.status = ?#{#status}
        order by u.startDate
    """)
    List<Urlaub> findAllByStatus(Urlaubsstatus status);

    @Query("""
        SELECT u
        FROM Urlaub u
        where u.username = ?#{#username}
        order by u.startDate
    """)
    List<Urlaub> findAllByUsername(String username);

    @Query("""
        SELECT u
        FROM Urlaub u
        where u.status = ?#{#status}
        and u.username = ?#{#username}
        order by u.startDate
    """)
    List<Urlaub> findAllByUsernameAndStatus(String username, Urlaubsstatus status);

    @Query("""
        SELECT u
        FROM Urlaub u
        where u.username
        in (select e.username from Employee e where e.abteilung = ?#{#abteilung})
        order by u.startDate
    """)
    List<Urlaub> findAllByAbteilung(@Param("abteilung") String abteilung);

    @Query("""
        SELECT u
        FROM Urlaub u
        where u.username
        in (select e.username from Employee e where e.abteilung = ?#{#abteilung}
        and u.username = ?#{#username})
        and u.status = ?#{#status}
        order by u.startDate
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
        order by u.startDate
    """)
    List<Urlaub> findAllByUsernameAbteilung(@Param("username") String username,
                                            @Param("abteilung") String abteilung);

    @Query("""
        SELECT u
        FROM Urlaub u
        where u.username
        in (select e.username from Employee e where e.abteilung = ?#{#abteilung})
        and u.status = ?#{#status}
        order by u.startDate
    """)
    List<Urlaub> findAllByStatusAndAbteilung(@Param("status") Urlaubsstatus status,
                                             @Param("abteilung") String abteilung);
}
