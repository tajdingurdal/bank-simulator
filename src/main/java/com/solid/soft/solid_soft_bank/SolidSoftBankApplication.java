package com.solid.soft.solid_soft_bank;

import com.solid.soft.solid_soft_bank.model.CardEntity;
import com.solid.soft.solid_soft_bank.service.CardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class SolidSoftBankApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SolidSoftBankApplication.class);
    @Autowired
    private CardService cardService;

    @Value("${sample.card.mehmetCardNo}")
    private String mehmetCardNo;

    public static void main(String[] args) {
        SpringApplication.run(SolidSoftBankApplication.class, args);
    }

    @Override
    public void run(final String... args) throws Exception {
        addSampleCards();
    }

    public void addSampleCards() {
        final String tajdinCardNo = "1234567891";
        final String yakupCardNo = "1234567892";
        final String osmanCardNo = "1234567893";
        final String dilanCardNo = "1234567894";

        if (!cardService.findByCardNoIn(List.of(mehmetCardNo, tajdinCardNo, yakupCardNo, osmanCardNo, dilanCardNo)).isEmpty()) {
            log.debug("No sample data added to the database because they already exist.");
            return;
        }

        final CardEntity card1 = new CardEntity("Mehmet", "Kurum", mehmetCardNo, "01/26", "123", 400D, "USD");
        final CardEntity card2 = new CardEntity("Tajdin", "Gürdal", tajdinCardNo, "02/27", "123", 300D, "USD");
        final CardEntity card3 = new CardEntity("Yakup", "Sahin", yakupCardNo, "02/28", "123", 1000D, "USD");
        final CardEntity card4 = new CardEntity("Osman", "Sahin", osmanCardNo, "06/27", "123", 650D, "TL");
        final CardEntity card5 = new CardEntity("Dilan", "Cenges", dilanCardNo, "05/28", "123", 750D, "EURO");
        cardService.saveAll(List.of(card1, card2, card3, card4, card5));
        log.debug("Sample data successfully added to the database.");
    }
}
