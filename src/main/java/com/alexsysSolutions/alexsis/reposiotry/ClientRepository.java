package com.alexsysSolutions.alexsis.reposiotry;

import com.alexsysSolutions.alexsis.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {


    // stats queries
    @Query("SELECT count(*) FROM Client c")
    int countTotalClients();

   @Query("SELECT count(*) FROM Client c where c.active=true AND c.deleted=false")
    int countActiveClients();

   @Query("SELECT count(*) FROM Client c WHERE DATE(c.registrationDate) = CURRENT_DATE")
    int countClientsRegisteredToday();

    @Query("SELECT avg(c.satisfactionScore) FROM Client c")
    Double averageSatisfactionScore();

    @Query("SELECT count(*) FROM Client c WHERE c.satisfactionScore < 3")
    int countLowSatisfactionClients();

}
