package com.company;

import com.company.container.ComponentContainer;
import com.company.controller.AdminController;
import com.company.controller.AllController;
import com.company.controller.CustomerController;
import com.company.controller.EmployeeController;
import com.company.model.Employee;
import com.company.service.EmployeeService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

public class QoravulBot extends TelegramLongPollingBot {

    public AdminController adminController = new AdminController();
    public AllController allController = new AllController();
    public CustomerController customerController = new CustomerController();
    public EmployeeController employeeController = new EmployeeController();

    @Override
    public String getBotUsername() {
        return ComponentContainer.BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return ComponentContainer.BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {

        try {
            if (update.hasMessage()) {

                Message message = update.getMessage();
                User user = message.getFrom();
                String chatId = String.valueOf(message.getChatId());
                Employee adminList = EmployeeService.getAdminList(chatId);
                Employee employeeList = EmployeeService.getEmployeeList(chatId);
                Employee suplierList = EmployeeService.getSuplierList(chatId);

                if (adminList != null) {

                    adminController.handleMessage(user, message);

                } else if (employeeList != null) {

                    employeeController.handleMessage(user, message);

                } else if (suplierList != null) {


                } else {

                    allController.handleMessage(user, message);

                    if (message.hasLocation()) {
                        allController.handleLocation(user, message);
                    } else if (message.hasContact()) {
                        allController.handleContact(user, message, chatId);
                    }

                }

            } else if (update.hasCallbackQuery()) {

                CallbackQuery callbackQuery = update.getCallbackQuery();
                Message message = callbackQuery.getMessage();
                User user = callbackQuery.getFrom();
                String data = callbackQuery.getData();
                String chatId = String.valueOf(message.getChatId());
                Employee employee = EmployeeService.getAdminList(chatId);

                if (employee != null) {
                    adminController.handleCallBack(user, message, data);
                } else {
                    allController.handleCallBack(user, message, data);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sendMsg(Object message) {

        try {
            if (message instanceof SendMessage) {
                execute((SendMessage) message);
            }
            if (message instanceof SendDocument) {
                execute((SendDocument) message);
            }
            if (message instanceof DeleteMessage) {
                execute((DeleteMessage) message);
            }
            if (message instanceof EditMessageText) {
                execute((EditMessageText) message);
            }
            if (message instanceof SendSticker) {
                execute((SendSticker) message);
            }
            if (message instanceof SendPhoto) {
                execute((SendPhoto) message);
            }
            if (message instanceof SendDice) {
                execute((SendDice) message);
            }
            if (message instanceof SendVenue) {
                execute((SendVenue) message);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
