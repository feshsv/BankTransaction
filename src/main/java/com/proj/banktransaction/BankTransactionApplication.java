package com.proj.banktransaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Это основной класс, стартующий приложение которое переводит указанную сумму от одного клиента другому.
 * Клиенты находятся в БД H2 в оперативной памяти. База заполнятся при каждом запуске из файлов schema.sql и data.sql.
 */

@SpringBootApplication
public class BankTransactionApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankTransactionApplication.class, args);
    }
}

