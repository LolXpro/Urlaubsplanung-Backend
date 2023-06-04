package MFAX421A.Urlaubsplanung.repository;

import MFAX421A.Urlaubsplanung.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByUsername(String username);
}
