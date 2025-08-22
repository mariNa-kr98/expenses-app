package gr.aueb.cf.expensesApp;

import gr.aueb.cf.expensesApp.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class ExpensesAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpensesAppApplication.class, args);
	}
}
