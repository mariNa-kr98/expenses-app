package gr.aueb.cf.expensesApp.repository;

import gr.aueb.cf.expensesApp.model.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidatedTokenRepo extends JpaRepository<InvalidatedToken, Long> {
    boolean existsByToken(String token);
}
