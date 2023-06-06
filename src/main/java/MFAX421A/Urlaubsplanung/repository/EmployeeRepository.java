package MFAX421A.Urlaubsplanung.repository;

import MFAX421A.Urlaubsplanung.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByUsername(String username);

    @Query("""
    SELECT e
    from Employee e
    where e.abteilung = :#{#abteilung} and e.username <> :#{#username}
    ORDER BY RAND()
    LIMIT 1
    """)
    Employee findAllByNotImUrlaubOrUsernameAndAbteilung(@Param("username") String username, @Param("abteilung") String abteilung);
}
