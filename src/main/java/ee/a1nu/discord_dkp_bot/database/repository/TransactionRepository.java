package ee.a1nu.discord_dkp_bot.database.repository;

import ee.a1nu.discord_dkp_bot.database.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

}
