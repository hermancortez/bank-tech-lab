package cl.banktech.customer_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "cl.banktech.customer")
@EntityScan(basePackages = "cl.banktech.customer.infrastructure.persistence")
@EnableJpaRepositories(basePackages = "cl.banktech.customer.infrastructure.persistence")
public class CustomerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
	}

}
