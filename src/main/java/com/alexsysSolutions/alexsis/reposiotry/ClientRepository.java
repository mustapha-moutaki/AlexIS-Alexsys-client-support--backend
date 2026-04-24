package com.alexsysSolutions.alexsis.reposiotry;

import com.alexsysSolutions.alexsis.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByEmail(String email);
    Optional<Client>findByUsername(String username);
    Optional<Client> findById(Long id);

    @Query("""
SELECT c FROM Client c
WHERE (:isVip IS NULL OR c.isVip = :isVip)
  AND (:isActive IS NULL OR c.active = :isActive)
""")
    Page<Client> findAllWithFilters(
            @Param("isVip") Boolean isVip,
            @Param("isActive") Boolean isActive,
            Pageable pageable
    );

    // stats queries
    @Query("SELECT COUNT(c) FROM Client c")
    long countTotalClients();

    @Query("SELECT COUNT(c) FROM Client c WHERE c.active = true AND c.deleted = false")
    long countActiveClients();

    @Query("SELECT COUNT(c) FROM Client c WHERE FUNCTION('DATE', c.registrationDate) = CURRENT_DATE")
    long countClientsRegisteredToday();

    @Query("SELECT AVG(c.satisfactionScore) FROM Client c")
    Double averageSatisfactionScore();

    @Query("SELECT COUNT(c) FROM Client c WHERE c.satisfactionScore < 3")
    long countLowSatisfactionClients();

//     @Query("SELECT c.firstName, c.lastName FROM Client c " +
//             "WHERE c.satisfactionScore = (SELECT MIN(satisfactionScore) FROM Client)")
//    String findMostDissatisfiedClient();


}
