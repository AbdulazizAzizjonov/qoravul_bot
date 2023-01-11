package com.company.controller;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

public class CustomerController {
    public void handleMessage(User user, Message message) {
        if (message.hasText()) {
            handleText(user, message);
        } else if (message.hasContact()) {
            handleContact(user, message);
        } else if (message.hasPhoto()) {
            handlePhoto(user, message);
        }
    }

    private void handleContact(User user, Message message) {

    }

    private void handlePhoto(User user, Message message) {

    }

    private void handleText(User user, Message message) {


    }
}
