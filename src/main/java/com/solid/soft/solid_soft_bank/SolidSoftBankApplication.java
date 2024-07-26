package com.solid.soft.solid_soft_bank;

import com.solid.soft.solid_soft_bank.model.CardEntity;
import com.solid.soft.solid_soft_bank.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SolidSoftBankApplication implements CommandLineRunner {

    @Autowired
    private CardRepository cardRepository;

    public static void main(String[] args) {
        SpringApplication.run(SolidSoftBankApplication.class, args);
    }

    @Override
    public void run(final String... args) throws Exception {
        final String cardNo = "1234567890";
        if (cardRepository.findByCardNo(cardNo).isEmpty()) {
            cardRepository.save(new CardEntity("Murat", "Kurum", cardNo, "01/26", "123", 400D, "USD"));
        }
    }
}
