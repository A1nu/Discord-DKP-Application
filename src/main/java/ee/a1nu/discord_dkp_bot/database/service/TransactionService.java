package ee.a1nu.discord_dkp_bot.database.service;

import ee.a1nu.discord_dkp_bot.api.util.Action;
import ee.a1nu.discord_dkp_bot.api.util.ChangeContext;
import ee.a1nu.discord_dkp_bot.database.model.Transaction;
import ee.a1nu.discord_dkp_bot.database.repository.TransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void saveTransaction(Long initiatorSnowflake, ChangeContext changeContext, String id, Action action) {
        Transaction transaction = new Transaction();
        transaction.setChangeContext(changeContext);
        transaction.setInitiatorSnowflake(initiatorSnowflake);
        transaction.setEditedEntity(id);
        transaction.setAction(action);
        transactionRepository.save(transaction);
    }
}
