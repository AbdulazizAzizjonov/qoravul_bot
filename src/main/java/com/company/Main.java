package com.company;

import com.company.container.ComponentContainer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {


        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

            QoravulBot qoravulBot = new QoravulBot();

            ComponentContainer.SUVBOT = qoravulBot;

            telegramBotsApi.registerBot(qoravulBot);

        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
