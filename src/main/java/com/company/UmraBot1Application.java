package com.company;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class UmraBot1Application {

    public static void main(String[] args) {

        SpringApplication.run(UmraBot1Application.class, args);

        TelegramBotsApi telegramBotsApi;
        MyBot myBot = new MyBot();

        try {
            telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(myBot);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

}
