package com.company.controller;

import com.company.container.ComponentContainer;
import com.company.database.Database;
import com.company.enums.EmployeeStatus;
import com.company.model.Employee;
import com.company.service.EmployeeService;
import com.company.util.InlineKeyboardUtil;
import com.company.util.KeyboardUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;
import java.util.Random;

import static com.company.container.ComponentContainer.SUVBOT;

public class AllController {

    private void handlePhoto(User user, Message message) {

    }

    public void handleLocation(User user, Message message) {


    }

    public static void handleContact(User user, Message message, String chatId) {

        Contact contact = message.getContact();
        String employeeId = String.valueOf(contact.getUserId());

        Employee employee = EmployeeService.getEmployeeById(employeeId);
        if (employee == null) {
            employee = new Employee(employeeId, contact.getFirstName(),
                    contact.getLastName(), contact.getPhoneNumber(), EmployeeStatus.SHARE_CONTACT, false, false, false);
            EmployeeService.addEmployee(employee);
        }

        SendMessage sendMessage = new SendMessage(
                String.valueOf(message.getChatId()), "Taklif va murojaatlaringiz bo'lsa marhamat yozishingiz mumkin\uD83D\uDE0A"
        );

        ComponentContainer.SUVBOT.sendMsg(sendMessage);
    }

    public void handleMessage(User user, Message message) {
        if (message.hasText()) {
            handleText(user, message);
        } else if (message.hasPhoto()) {
            handlePhoto(user, message);
        }
    }

    private void handleText(User user, Message message) {

        String text = message.getText();
        String chatId = String.valueOf(message.getChatId());

        if (text.startsWith("http://") || text.startsWith("https://")) {

            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            System.out.println(text);
            SUVBOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Bu guruhda reklama tarqatish mumkin emas❗️");
            sendMessage.setChatId(chatId);
            ComponentContainer.SUVBOT.sendMsg(sendMessage);


        } else if (text.equals("https://") || text.equals("http://")){

            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );
            System.out.println(text);
            SUVBOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Bu guruhda reklama tarqatish mumkin emas❗️");
            sendMessage.setChatId(chatId);
            ComponentContainer.SUVBOT.sendMsg(sendMessage);


        }
    }


    public void handleCallBack(User user, Message message, String data) {

        String chatId = String.valueOf(message.getChatId());

        if (data.equals("replay_message")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            List<Employee> employeeList = Database.REPLAY_MESSAGE;
            Random random = new Random();
            Employee employee = employeeList.get((int) random.nextDouble(employeeList.size()));

            String randomId = employee.getId();
            System.out.println(randomId);


            SUVBOT.sendMsg(deleteMessage);


            SendMessage sendMessage = new SendMessage();

            if (employee.getFirstName() != null && employee.getLastName() == null) {

                sendMessage.setText(employee.getFirstName() + "\n\n" + "ga XABARNI KIRITING\uD83D\uDC47\uD83C\uDFFB");


            } else if (employee.getFirstName() == null && employee.getLastName() != null) {

                sendMessage.setText(employee.getLastName() + "\n\n" + "XABARNI KIRITING\uD83D\uDC47\uD83C\uDFFB");

            } else {

                sendMessage.setText(employee.getFirstName() + " " + employee.getLastName() + "\n\n" + "XABARNI KIRITING\uD83D\uDC47\uD83C\uDFFB");

            }

            sendMessage.setChatId(randomId);

            ComponentContainer.SUVBOT.sendMsg(sendMessage);

            SUVBOT.sendMsg(sendMessage);

        }

    }
}
