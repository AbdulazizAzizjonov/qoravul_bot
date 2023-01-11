package com.company.controller;

import com.company.container.ComponentContainer;
import com.company.database.Database;
import com.company.enums.AdminStatus;
import com.company.model.Employee;
import com.company.model.Product;
import com.company.service.EmployeeService;
import com.company.service.ProductService;
import com.company.util.InlineKeyboardUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

import static com.company.container.ComponentContainer.*;


public class AdminController {

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
        List<PhotoSize> photoSizeList = message.getPhoto();

        String chatId = String.valueOf(message.getChatId());

        if (productStepMap.containsKey(chatId)) {

            Product product = productMap.get(chatId);

            if (productStepMap.get(chatId).equals(AdminStatus.ENTERED_PRODUCT_PRICE)) {
                product.setImage(photoSizeList.get(photoSizeList.size() - 1).getFileId());

                SendPhoto sendPhoto = new SendPhoto(chatId, new InputFile(product.getImage()));
                sendPhoto.setCaption(String.format("Mahsulot nomi: %s\n\n" +
                                "Narxi: %s\n\n\n" +
                                "Quyidagi mahsulot bazaga qo'shilsinmi?",
                        product.getName(), product.getPrice()));
                sendPhoto.setReplyMarkup(InlineKeyboardUtil.confirmAddProductMarkup());

                SUVBOT.sendMsg(sendPhoto);


            } else if (productStepMap.get(chatId).equals(AdminStatus.SELECT_EDIT_PRODUCT_IMAGE)) {

                Product product1 = ComponentContainer.crudStepMap.get(chatId);
                product1.setImage(photoSizeList.get(photoSizeList.size() - 1).getFileId());
                ProductService.updateProductImage(product1.getId(), product1.getImage());
                ComponentContainer.crudStepMap.remove(chatId);

                SendPhoto sendPhoto = new SendPhoto(chatId, new InputFile(product1.getImage()));
                sendPhoto.setCaption(String.format("Mahsulot rasmi o'zgartirildi✅!\n\nRasmini o'zgartirmoqchi bo'lgan mahsulotingizni tanlang \uD83D\uDC47\uD83C\uDFFB "));
                sendPhoto.setReplyMarkup(InlineKeyboardUtil.MahsulotImageEdit());

                SUVBOT.sendMsg(sendPhoto);
            }
        }
    }

    private void handleContact(User user, Message message) {

    }

    private void handleText(User user, Message message) {

        String text = message.getText();

        String chatId = String.valueOf(message.getChatId());
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));

        if (text.equals("/start")) {
            sendMessage.setText("Xush Kelibsiz admin, Qanday ishni bajarmohchisiz");
            sendMessage.setReplyMarkup(InlineKeyboardUtil.AdminStart());

        } else if (productStepMap.containsKey(chatId)) {

            Product product = productMap.get(chatId);

            String newName = null;
            double newPrice = 0;


            if (productStepMap.get(chatId).equals(AdminStatus.SELECT_ADD_PRODUCT)) {

                product.setName(text);
                productStepMap.put(chatId, AdminStatus.ENTERED_PRODUCT_NAME);

                sendMessage.setText("Mahsulotni narxini kiriting: ");

            } else if (ComponentContainer.productStepMap.get(chatId).equals(AdminStatus.ENTERED_PRODUCT_NAME)) {

                double price = 0;
                try {
                    price = Double.parseDouble(text.trim());
                } catch (NumberFormatException e) {
                }
                if (price <= 0) {
                    sendMessage.setText("Narx noto'g'ri kiritildi, Qaytadan narxni kiriting: ");
                } else {
                    product.setPrice(price);
                    productStepMap.put(chatId, AdminStatus.ENTERED_PRODUCT_PRICE);

                    sendMessage.setText("Mahsulotning rasmini jo'nating: ");
                }

            } else if (productStepMap.get(chatId).equals(AdminStatus.SELECT_EDIT_PRODUCT_NAME)) {

                newName = text;

                Product product1 = ComponentContainer.crudStepMap.get(chatId);
                ProductService.updateProductName(product1.getId(), newName);
                ComponentContainer.crudStepMap.remove(chatId);
                sendMessage.setText("Mahsulot nomi " + newName + " ga o'zgartirildi✅!\n\nNomini o'zgartirmoqchi bo'lgan mahsulotingizni tanlang \uD83D\uDC47\uD83C\uDFFB ");
                sendMessage.setReplyMarkup(InlineKeyboardUtil.MahsulotNameEdit());

            } else if (productStepMap.get(chatId).equals(AdminStatus.SELECT_EDIT_PRODUCT_PRICE)) {

                try {
                    newPrice = Double.parseDouble(text.trim());
                } catch (NumberFormatException e) {
                }
                if (newPrice <= 0) {
                    sendMessage.setText("Narx noto'g'ri kiritildi, Qaytadan narxni kiriting: ");
                } else {
                    Product product1 = ComponentContainer.crudStepMap.get(chatId);
                    ProductService.updateProductPrice(product1.getId(), newPrice);
                    ComponentContainer.crudStepMap.remove(chatId);
                    sendMessage.setText("Mahsulot narxi " + newPrice + " ga o'zgartirildi✅!\n\nNarxini o'zgartirmoqchi bo'lgan mahsulotingizni tanlang \uD83D\uDC47\uD83C\uDFFB ");
                    sendMessage.setReplyMarkup(InlineKeyboardUtil.MahsulotPriceEdit());

                }
            }
        }

        ComponentContainer.SUVBOT.sendMsg(sendMessage);
    }

    public void handleCallBack(User user, Message message, String data) {


        String chatId = String.valueOf(message.getChatId());

        if (data.equals("barchaXodimlar_button")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            ComponentContainer.SUVBOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Quiydagi amallardan birini tanlang \uD83D\uDC47\uD83C\uDFFB"
            );

            sendMessage.setReplyMarkup(InlineKeyboardUtil.AdminBarchaObunachilar());

            ComponentContainer.SUVBOT.sendMsg(sendMessage);


        } else if (data.equals("back_button_from_AdminBarchaObunachilar")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            ComponentContainer.SUVBOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Siz bosh menyuga qaytingiz!✅"
            );

            sendMessage.setReplyMarkup(InlineKeyboardUtil.AdminStart());

            ComponentContainer.SUVBOT.sendMsg(sendMessage);

        } else if (data.equals("buyurtmachi_plus")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            ComponentContainer.SUVBOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Bazadagi obunachilar \uD83D\uDC47\uD83C\uDFFB"
            );

            sendMessage.setReplyMarkup(InlineKeyboardUtil.EmployeePlusListForAdmin());
            ComponentContainer.SUVBOT.sendMsg(sendMessage);


        } else if (data.startsWith("choose_employee_plus/")) {

            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            ComponentContainer.SUVBOT.sendMsg(deleteMessage);

            String employeeId = String.valueOf(data.split("/")[1]);

            Employee employee = EmployeeService.getAllEmployeeById(employeeId);

            SendMessage sendMessage = new SendMessage();

            EmployeeService.changeEmployee(employee.getId());

            System.out.println(employeeId);

            if (employee.getFirstName() != null && employee.getLastName() == null) {

                sendMessage.setText(employee.getFirstName() + " buyurtma beruvchi lavozimiga tayyorlandi ✅");
                sendMessage.setChatId(chatId);

            } else if (employee.getFirstName() == null && employee.getLastName() != null) {

                sendMessage.setText(employee.getLastName() + " buyurtma beruvchi lavozimiga tayyorlandi ✅");
                sendMessage.setChatId(chatId);

            } else {

                sendMessage.setText(employee.getFirstName() + " " + employee.getLastName() + " " + " buyurtma beruvchi lavozimiga tayyorlandi ✅");
                sendMessage.setChatId(chatId);

            }

            sendMessage.setReplyMarkup(InlineKeyboardUtil.AdminBarchaObunachilar());
            ComponentContainer.SUVBOT.sendMsg(sendMessage);


        } else if (data.startsWith("back_button_to_employee_plus")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            ComponentContainer.SUVBOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "⏪ Ortga qaytish tugmasi bosildi ✅"
            );

            sendMessage.setReplyMarkup(InlineKeyboardUtil.AdminBarchaObunachilar());

            ComponentContainer.SUVBOT.sendMsg(sendMessage);


        } else if (data.equals("yetkazuvchi_plus")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            ComponentContainer.SUVBOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Bazadagi obunachilar \uD83D\uDC47\uD83C\uDFFB"
            );


            sendMessage.setReplyMarkup(InlineKeyboardUtil.SuplierPlusListForAdmin());

            ComponentContainer.SUVBOT.sendMsg(sendMessage);


        } else if (data.startsWith("back_button_to_suplier_plus")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            ComponentContainer.SUVBOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "⏪ Ortga qaytish tugmasi bosildi ✅"
            );

            sendMessage.setReplyMarkup(InlineKeyboardUtil.AdminBarchaObunachilar());

            ComponentContainer.SUVBOT.sendMsg(sendMessage);


        } else if (data.startsWith("choose_suplier_plus/")) {

            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            ComponentContainer.SUVBOT.sendMsg(deleteMessage);

            String employeeId = String.valueOf(data.split("/")[1]);

            Employee employee = EmployeeService.getAllEmployeeById(employeeId);

            SendMessage sendMessage = new SendMessage();

            EmployeeService.changeSuplier(employee.getId());

            if (employee.getFirstName() != null && employee.getLastName() == null) {

                sendMessage.setText(employee.getFirstName() + " yetkazib beruvchi lavozimiga tayyorlandi ✅");
                sendMessage.setChatId(chatId);

            } else if (employee.getFirstName() == null && employee.getLastName() != null) {

                sendMessage.setText(employee.getLastName() + " yetkazib beruvchi lavozimiga tayyorlandi ✅");
                sendMessage.setChatId(chatId);

            } else {

                sendMessage.setText(employee.getFirstName() + " " + employee.getLastName() + " " + " yetkazib beruvchi lavozimiga tayyorlandi ✅");
                sendMessage.setChatId(chatId);

            }

            sendMessage.setReplyMarkup(InlineKeyboardUtil.AdminBarchaObunachilar());
            ComponentContainer.SUVBOT.sendMsg(sendMessage);


        } else if (data.equals("xodimlar_button")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            ComponentContainer.SUVBOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Buyurtma beruvchi xodimlar uchun quyidagi amallardan birini tanlang \uD83D\uDC47\uD83C\uDFFB"
            );

            sendMessage.setReplyMarkup(InlineKeyboardUtil.AdminBuyurtmaBeruvchilar());
            ComponentContainer.SUVBOT.sendMsg(sendMessage);


        } else if (data.equals("buyurtmachi_minus")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            ComponentContainer.SUVBOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "O'chirmohchi bo'lgan buyurtmachingizni tanlang \uD83D\uDC47\uD83C\uDFFB"
            );

            sendMessage.setReplyMarkup(InlineKeyboardUtil.EmployeeMinusListForAdmin());
            ComponentContainer.SUVBOT.sendMsg(sendMessage);


        } else if (data.startsWith("choose_employee_minus/")) {

            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            ComponentContainer.SUVBOT.sendMsg(deleteMessage);

            String employeeId = String.valueOf(data.split("/")[1]);

            Employee employee = EmployeeService.getEmployeeById(employeeId);

            SendMessage sendMessage = new SendMessage();

            EmployeeService.changeEmployee2(employee.getId());

            if (employee.getFirstName() != null && employee.getLastName() == null) {

                sendMessage.setText(employee.getFirstName() + " buyurtmachi lavozimidan ozod etildi ✅");
                sendMessage.setChatId(chatId);

            } else if (employee.getFirstName() == null && employee.getLastName() != null) {

                sendMessage.setText(employee.getLastName() + " buyurtmachi lavozimidan ozod etildi ✅");
                sendMessage.setChatId(chatId);

            } else {

                sendMessage.setText(employee.getFirstName() + " " + employee.getLastName() + " " + " buyurtmachi lavozimidan ozod etildi ✅");
                sendMessage.setChatId(chatId);

            }

            sendMessage.setReplyMarkup(InlineKeyboardUtil.AdminBuyurtmaBeruvchilar());
            ComponentContainer.SUVBOT.sendMsg(sendMessage);


        } else if (data.startsWith("back_button_to_employee_minus")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            ComponentContainer.SUVBOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "⏪ Ortga qaytish tugmasi bosildi ✅\n Buyurtma beruvchi xodimlar uchun quyidagi amallardan birini tanlang \uD83D\uDC47\uD83C\uDFFB"
            );

            sendMessage.setReplyMarkup(InlineKeyboardUtil.AdminBuyurtmaBeruvchilar());

            ComponentContainer.SUVBOT.sendMsg(sendMessage);


        } else if (data.equals("buyurtmachi_change_yetkazib_beruvci")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            ComponentContainer.SUVBOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Tahrirlamohchi bo'lgan buyurtmachingizni tanlang \uD83D\uDC47\uD83C\uDFFB"
            );

            sendMessage.setReplyMarkup(InlineKeyboardUtil.EmployeeChangeSuplierListForAdmin());
            ComponentContainer.SUVBOT.sendMsg(sendMessage);


        } else if (data.startsWith("back_button_to_employee_change_suplier")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            ComponentContainer.SUVBOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "⏪ Ortga qaytish tugmasi bosildi ✅\nBuyurtma beruvchi xodimlar uchun quyidagi amallardan birini tanlang \uD83D\uDC47\uD83C\uDFFB"
            );

            sendMessage.setReplyMarkup(InlineKeyboardUtil.AdminBuyurtmaBeruvchilar());

            ComponentContainer.SUVBOT.sendMsg(sendMessage);


        } else if (data.startsWith("back_button_to_suplier_change_employee")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            ComponentContainer.SUVBOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "⏪ Ortga qaytish tugmasi bosildi ✅\nYetkazib beruvchi xodimlar uchun quyidagi amallardan birini tanlang \uD83D\uDC47\uD83C\uDFFB"
            );

            sendMessage.setReplyMarkup(InlineKeyboardUtil.AdminYetkazibBeruvchilar());

            ComponentContainer.SUVBOT.sendMsg(sendMessage);


        } else if (data.startsWith("choose_employee_change/")) {

            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            ComponentContainer.SUVBOT.sendMsg(deleteMessage);

            String employeeId = String.valueOf(data.split("/")[1]);

            Employee employee = EmployeeService.getEmployeeById(employeeId);

            SendMessage sendMessage = new SendMessage();

            EmployeeService.changeEmployeeToSuplier(employee.getId());

            if (employee.getFirstName() != null && employee.getLastName() == null) {

                sendMessage.setText(employee.getFirstName() + " yetkazib beruvchi lavozimiga tayyorlandi ✅");
                sendMessage.setChatId(chatId);

            } else if (employee.getFirstName() == null && employee.getLastName() != null) {

                sendMessage.setText(employee.getLastName() + " yetkazib beruvchi lavozimiga tayyorlandi ✅");
                sendMessage.setChatId(chatId);

            } else {

                sendMessage.setText(employee.getFirstName() + " " + employee.getLastName() + " " + " yetkazib beruvchi lavozimiga tayyorlandi ✅");
                sendMessage.setChatId(chatId);

            }

            sendMessage.setReplyMarkup(InlineKeyboardUtil.EmployeeChangeSuplierListForAdmin());
            ComponentContainer.SUVBOT.sendMsg(sendMessage);


        } else if (data.startsWith("back_button_from_AdminBuyurtmaBeruvchilar")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            ComponentContainer.SUVBOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Siz bosh menyuga qaytingiz!✅"
            );

            sendMessage.setReplyMarkup(InlineKeyboardUtil.AdminStart());

            ComponentContainer.SUVBOT.sendMsg(sendMessage);


        } else if (data.equals("yetkazibBeruvchi_button")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            ComponentContainer.SUVBOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Yetkazib beruvchi xodimlar uchun quyidagi amallardan birini tanlang \uD83D\uDC47\uD83C\uDFFB"
            );

            sendMessage.setReplyMarkup(InlineKeyboardUtil.AdminYetkazibBeruvchilar());
            ComponentContainer.SUVBOT.sendMsg(sendMessage);


        } else if (data.equals("yetkazuvchi_minus")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            ComponentContainer.SUVBOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "O'chirmohchi bo'lgan yetkazib beruvchingizni tanlang \uD83D\uDC47\uD83C\uDFFB"
            );

            sendMessage.setReplyMarkup(InlineKeyboardUtil.SuplierMinusListForAdmin());
            ComponentContainer.SUVBOT.sendMsg(sendMessage);


        } else if (data.startsWith("choose_suplier_minus/")) {

            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            ComponentContainer.SUVBOT.sendMsg(deleteMessage);

            String employeeId = String.valueOf(data.split("/")[1]);

            Employee employee = EmployeeService.getSuplierById(employeeId);

            SendMessage sendMessage = new SendMessage();

            EmployeeService.changeSuplier2(employee.getId());

            if (employee.getFirstName() != null && employee.getLastName() == null) {

                sendMessage.setText(employee.getFirstName() + " yetkazib beruvchi lavozimidan ozod etildi ✅");
                sendMessage.setChatId(chatId);

            } else if (employee.getFirstName() == null && employee.getLastName() != null) {

                sendMessage.setText(employee.getLastName() + " yetkazib beruvchi lavozimidan ozod etildi ✅");
                sendMessage.setChatId(chatId);

            } else {

                sendMessage.setText(employee.getFirstName() + " " + employee.getLastName() + " " + " yetkazib beruvchi lavozimidan ozod etildi ✅");
                sendMessage.setChatId(chatId);

            }

            sendMessage.setReplyMarkup(InlineKeyboardUtil.AdminYetkazibBeruvchilar());
            ComponentContainer.SUVBOT.sendMsg(sendMessage);


        } else if (data.startsWith("back_button_to_suplier_minus")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            ComponentContainer.SUVBOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "⏪ Ortga qaytish tugmasi bosildi ✅\n Yetkazib beruvchi xodimlar uchun quyidagi amallardan birini tanlang \uD83D\uDC47\uD83C\uDFFB"
            );

            sendMessage.setReplyMarkup(InlineKeyboardUtil.AdminYetkazibBeruvchilar());

            ComponentContainer.SUVBOT.sendMsg(sendMessage);


        } else if (data.equals("yetkazib_beruvci_change_buyurtmachi")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            ComponentContainer.SUVBOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Tahrirlamohchi bo'lgan yetkazib beruvchingizni tanlang \uD83D\uDC47\uD83C\uDFFB"
            );

            sendMessage.setReplyMarkup(InlineKeyboardUtil.SuplierChangeEmployeeListForAdmin());
            ComponentContainer.SUVBOT.sendMsg(sendMessage);


        } else if (data.startsWith("choose_suplier_change/")) {

            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            ComponentContainer.SUVBOT.sendMsg(deleteMessage);

            String employeeId = String.valueOf(data.split("/")[1]);

            Employee employee = EmployeeService.getSuplierById(employeeId);

            SendMessage sendMessage = new SendMessage();

            EmployeeService.changeSuplierToEmployee(employee.getId());

            if (employee.getFirstName() != null && employee.getLastName() == null) {

                sendMessage.setText(employee.getFirstName() + " buyurtma beruvchi lavozimiga tayyorlandi ✅");
                sendMessage.setChatId(chatId);

            } else if (employee.getFirstName() == null && employee.getLastName() != null) {

                sendMessage.setText(employee.getLastName() + " buyurtma beruvchi lavozimiga tayyorlandi ✅");
                sendMessage.setChatId(chatId);

            } else {

                sendMessage.setText(employee.getFirstName() + " " + employee.getLastName() + " " + " buyurtma beruvchi lavozimiga tayyorlandi ✅");
                sendMessage.setChatId(chatId);

            }

            sendMessage.setReplyMarkup(InlineKeyboardUtil.SuplierChangeEmployeeListForAdmin());
            ComponentContainer.SUVBOT.sendMsg(sendMessage);


        } else if (data.startsWith("back_button_from_AdminYetkazibBeruvchilar")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            ComponentContainer.SUVBOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Siz bosh menyuga qaytingiz!✅"
            );

            sendMessage.setReplyMarkup(InlineKeyboardUtil.AdminStart());

            ComponentContainer.SUVBOT.sendMsg(sendMessage);


        } else if (data.equals("mahsulot_button")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            ComponentContainer.SUVBOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Quyidagi amallardan birini tanlang \uD83D\uDC47\uD83C\uDFFB"
            );

            sendMessage.setReplyMarkup(InlineKeyboardUtil.MahsulotButton());

            ComponentContainer.SUVBOT.sendMsg(sendMessage);


        } else if (data.equals("mahsulot_plus")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            ComponentContainer.SUVBOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Mahsulot nomini kiriting \uD83D\uDC47\uD83C\uDFFB"
            );

            SUVBOT.sendMsg(sendMessage);

            productMap.remove(chatId);
            productStepMap.remove(chatId);

            productStepMap.put(chatId, AdminStatus.SELECT_ADD_PRODUCT);
            productMap.put(chatId,
                    new Product(null, null, null));


        } else if (data.equals("back_from_mahsulot_button")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            ComponentContainer.SUVBOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Siz bosh menyuga qaytingiz!✅"
            );

            sendMessage.setReplyMarkup(InlineKeyboardUtil.AdminStart());

            ComponentContainer.SUVBOT.sendMsg(sendMessage);


        } else if (data.equals("add_product_commit")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            SUVBOT.sendMsg(deleteMessage);

            Product product = productMap.get(chatId);

            ProductService.addProduct(product);

            productMap.remove(chatId);
            productStepMap.remove(chatId);

            SendMessage sendMessage = new SendMessage(
                    chatId, product.getName() + "\t ✅ saqlandi!\n\n" + "Amalni tanlang:"
            );
            sendMessage.setReplyMarkup(InlineKeyboardUtil.MahsulotButton());

            SUVBOT.sendMsg(sendMessage);


        } else if (data.equals("add_product_cancel")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            SUVBOT.sendMsg(deleteMessage);

            Product product = productMap.get(chatId);

            productMap.remove(chatId);
            productStepMap.remove(chatId);

            SendMessage sendMessage = new SendMessage(
                    chatId, product.getName() + "\t ❌ bekor qilindi!\n\n" + "Amalni tanlang:"
            );
            sendMessage.setReplyMarkup(InlineKeyboardUtil.MahsulotButton());

            SUVBOT.sendMsg(sendMessage);


        } else if (data.equals("mahsulot_minus")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            SUVBOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "O'chirmohchi bo'lgan mahsulotingizni tanlang \uD83D\uDC47\uD83C\uDFFB"
            );

            sendMessage.setReplyMarkup(InlineKeyboardUtil.MahsulotMinus());

            SUVBOT.sendMsg(sendMessage);


        } else if (data.startsWith("mahsulot_minus/")) {

            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            ComponentContainer.SUVBOT.sendMsg(deleteMessage);

            Integer productId = Integer.parseInt(data.split("/")[1]);

            Product product = ProductService.getProductById(productId);

            SendMessage sendMessage = new SendMessage();

            ProductService.deleteProduct(productId);

            sendMessage.setText(product.getName() + " o'chirildi ✅\n\nO'chirmohchi bo'lgan mahsulotingizni tanlang \uD83D\uDC47\uD83C\uDFFB");
            sendMessage.setChatId(chatId);

            sendMessage.setReplyMarkup(InlineKeyboardUtil.MahsulotMinus());

            SUVBOT.sendMsg(sendMessage);


        } else if (data.startsWith("back_button_from_mahsulot_minus")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            ComponentContainer.SUVBOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "⏪ Ortga qaytish tugmasi bosildi ✅\n Quyidagi amallardan birini tanlang \uD83D\uDC47\uD83C\uDFFB"
            );

            sendMessage.setReplyMarkup(InlineKeyboardUtil.MahsulotButton());

            ComponentContainer.SUVBOT.sendMsg(sendMessage);


        } else if (data.equals("mahsulot_edit")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            SUVBOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Mahsulotning qaysi qismni o'zgartirmohchisiz tanlang \uD83D\uDC47\uD83C\uDFFB"
            );

            productMap.put(chatId, new Product(null, null, null));
            sendMessage.setReplyMarkup(InlineKeyboardUtil.MahsulotEditButton());

            SUVBOT.sendMsg(sendMessage);


        } else if (data.equals("back_from_mahsulot_edit")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            SUVBOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "⏪ Ortga qaytish tugmasi bosildi ✅\n Quyidagi amallardan birini tanlang \uD83D\uDC47\uD83C\uDFFB"
            );

            sendMessage.setReplyMarkup(InlineKeyboardUtil.MahsulotButton());

            SUVBOT.sendMsg(sendMessage);


        } else if (data.equals("mahsulot_name_edit")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            SUVBOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Nomini o'zgartirmoqchi bo'lgan mahsulotingizni tanlang \uD83D\uDC47\uD83C\uDFFB"
            );

            sendMessage.setReplyMarkup(InlineKeyboardUtil.MahsulotNameEdit());

            SUVBOT.sendMsg(sendMessage);


        } else if (data.startsWith("mahsulot_name_edit/")) {

            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            SUVBOT.sendMsg(deleteMessage);

            Integer productId = Integer.parseInt(data.split("/")[1]);

            ProductService.getProductById(productId);

            Product product = productMap.get(chatId);
            product.setId(productId);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Mahsulotni yangi nomini kiriting: "
            );

            SUVBOT.sendMsg(sendMessage);

            ComponentContainer.crudStepMap.put(chatId, new Product(productId));
            productStepMap.put(chatId, AdminStatus.SELECT_EDIT_PRODUCT_NAME);


        } else if (data.equals("back_button_from_mahsulot_name_edit")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            SUVBOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "⏪ Ortga qaytish tugmasi bosildi ✅\n Mahsulotning qaysi qismni o'zgartirmohchisiz tanlang \uD83D\uDC47\uD83C\uDFFB"
            );

            sendMessage.setReplyMarkup(InlineKeyboardUtil.MahsulotEditButton());

            SUVBOT.sendMsg(sendMessage);


        } else if (data.equals("mahsulot_price_edit")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            SUVBOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Narxini o'zgartirmoqchi bo'lgan mahsulotingizni tanlang \uD83D\uDC47\uD83C\uDFFB"
            );

            sendMessage.setReplyMarkup(InlineKeyboardUtil.MahsulotPriceEdit());

            SUVBOT.sendMsg(sendMessage);


        } else if (data.startsWith("mahsulot_price_edit/")) {

            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            SUVBOT.sendMsg(deleteMessage);

            Integer productId = Integer.parseInt(data.split("/")[1]);

            ProductService.getProductById(productId);

            Product product = productMap.get(chatId);
            product.setId(productId);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Mahsulotni yangi narxini kiriting: "
            );

            SUVBOT.sendMsg(sendMessage);

            ComponentContainer.crudStepMap.put(chatId, new Product(productId));
            productStepMap.put(chatId, AdminStatus.SELECT_EDIT_PRODUCT_PRICE);


        } else if (data.equals("back_button_from_mahsulot_price_edit")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            SUVBOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "⏪ Ortga qaytish tugmasi bosildi ✅\n Mahsulotning qaysi qismni o'zgartirmohchisiz tanlang \uD83D\uDC47\uD83C\uDFFB"
            );

            sendMessage.setReplyMarkup(InlineKeyboardUtil.MahsulotEditButton());

            SUVBOT.sendMsg(sendMessage);

        } else if (data.equals("mahsulot_image_edit")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            SUVBOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Rasmini o'zgartirmoqchi bo'lgan mahsulotingizni tanlang \uD83D\uDC47\uD83C\uDFFB"
            );

            sendMessage.setReplyMarkup(InlineKeyboardUtil.MahsulotImageEdit());

            SUVBOT.sendMsg(sendMessage);


        } else if (data.startsWith("mahsulot_image_edit/")) {

            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            SUVBOT.sendMsg(deleteMessage);

            Integer productId = Integer.parseInt(data.split("/")[1]);

            ProductService.getProductById(productId);

            Product product = productMap.get(chatId);
            product.setId(productId);

            SendMessage sendMessage = new SendMessage(
                    chatId, "Mahsulotni yangi rasmini jo'nating: "
            );

            SUVBOT.sendMsg(sendMessage);

            ComponentContainer.crudStepMap.put(chatId, new Product(productId));
            productStepMap.put(chatId, AdminStatus.SELECT_EDIT_PRODUCT_IMAGE);


        } else if (data.equals("back_button_from_mahsulot_image_edit")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            SUVBOT.sendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage(
                    chatId, "⏪ Ortga qaytish tugmasi bosildi ✅\n Mahsulotning qaysi qismni o'zgartirmohchisiz tanlang \uD83D\uDC47\uD83C\uDFFB"
            );

            sendMessage.setReplyMarkup(InlineKeyboardUtil.MahsulotEditButton());

            SUVBOT.sendMsg(sendMessage);

        } else if (data.equals("mahsulot_korish")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            SUVBOT.sendMsg(deleteMessage);

            ProductService.loadProductList();

            for (Product product : Database.PRODUCTLIST) {
                SendPhoto sendPhoto = new SendPhoto(chatId, new InputFile(product.getImage()));
                sendPhoto.setCaption(String.format("Mahsulot nomi:  %s\n\n" +
                        "Mahsulot narxi: %s\n", product.getName(), product.getPrice()));

                SUVBOT.sendMsg(sendPhoto);
            }

            SendMessage sendMessage = new SendMessage(
                    chatId, "Amalni tanlang \uD83D\uDC47\uD83C\uDFFB"
            );

            sendMessage.setReplyMarkup(InlineKeyboardUtil.MahsulotButton());

            SUVBOT.sendMsg(sendMessage);

        } else if (data.equals("replay_message")) {
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId, message.getMessageId()
            );

            SUVBOT.sendMsg(deleteMessage);


            SendMessage sendMessage = new SendMessage(
                    chatId, "Xabarni kiriting\uD83D\uDC47\uD83C\uDFFB: "
            );

            SUVBOT.sendMsg(sendMessage);

        }
    }
}
