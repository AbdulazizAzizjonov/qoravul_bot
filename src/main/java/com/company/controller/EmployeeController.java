package com.company.controller;

import com.company.container.ComponentContainer;
import com.company.model.Employee;
import com.company.service.EmployeeService;
import com.company.util.InlineKeyboardUtilForEmployee;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

public class EmployeeController {
    public void handleMessage(User user, Message message) {
        if (message.hasText()) {
            handleText(user, message);
        } else if (message.hasContact()) {
            handleContact(user, message);
        } else if (message.hasPhoto()) {
            handlePhoto(user, message);
        }
    }

    private void handlePhoto(User user, Message message) {

    }

    private void handleContact(User user, Message message) {

    }

    private void handleText(User user, Message message) {

        String text = message.getText();
        String chatId = String.valueOf(message.getChatId());
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        Employee employee = EmployeeService.getEmployeeById(chatId);

        if (text.equals("/start")) {

            if (employee.getFirstName() != null && employee.getLastName() == null) {
                sendMessage.setText("Xush Kelibsiz" + " " + employee.getFirstName() + " qanday ishni bajarmohchisiz.");
            } else if (employee.getFirstName() == null && employee.getLastName() != null) {
                sendMessage.setText("Xush Kelibsiz" + " " + employee.getLastName() + " qanday ishni bajarmohchisiz. ");
            } else {
                sendMessage.setText("Xush Kelibsiz" + " " + employee.getFirstName() + " " + employee.getLastName()+ " qanday ishni bajarmohchisiz");
            }

            sendMessage.setChatId(chatId);
            sendMessage.setReplyMarkup(InlineKeyboardUtilForEmployee.EmployeeStart());
            ComponentContainer.SUVBOT.sendMsg(sendMessage);

        } else {
            sendMessage.setText("BILMADIM");
            ComponentContainer.SUVBOT.sendMsg(sendMessage);
        }



    }
}
