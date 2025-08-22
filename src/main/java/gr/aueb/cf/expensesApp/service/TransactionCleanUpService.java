package gr.aueb.cf.expensesApp.service;

import gr.aueb.cf.expensesApp.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionCleanUpService {

    @Autowired
    private TransactionRepository transactionRepository;

    // Runs daily at 2 AM
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanSoftDeletedTransactions() {
        LocalDateTime cleanDay = LocalDateTime.now().minusDays(30);
        transactionRepository.deleteSoftDeletedTransactions(cleanDay);
    }
}
