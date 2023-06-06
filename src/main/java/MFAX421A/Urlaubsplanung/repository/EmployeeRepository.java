package MFAX421A.Urlaubsplanung.repository;

import MFAX421A.Urlaubsplanung.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByUsername(String username);

    @Query("""
    SELECT e from Employee e where e.username not in (select u.username from Urlaub u where u.username != :#{#username})
    """)
    List<Employee> findAllByNotImUrlaubOrUsername(String username);
}
