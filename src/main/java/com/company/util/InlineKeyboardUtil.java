package com.company.util;

import com.company.database.Database;
import com.company.model.Employee;
import com.company.model.Product;
import com.company.service.EmployeeService;
import com.company.service.ProductService;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InlineKeyboardUtil {


    public static InlineKeyboardMarkup AdminStart() {
        InlineKeyboardButton employeeAllList = getButton("Barcha obunachilar \uD83D\uDC65", "barchaXodimlar_button");
        InlineKeyboardButton employeeList = getButton("Buyurtma beruvchi \uD83D\uDCDD", "xodimlar_button");
        InlineKeyboardButton suplierList = getButton("Yetkazib beruvchi \uD83D\uDE9A", "yetkazibBeruvchi_button");
        InlineKeyboardButton settings = getButton("Mahsulotlar \uD83D\uDECD", "mahsulot_button");
        InlineKeyboardButton monthReport = getButton("Oylik Hisobot", "oylikHisobot_button");

        List<InlineKeyboardButton> row1 = getRow(employeeAllList);
        List<InlineKeyboardButton> row2 = getRow(employeeList, suplierList);
        List<InlineKeyboardButton> row3 = getRow(settings, monthReport);

        List<List<InlineKeyboardButton>> rowList = getRowList(row1, row2, row3);

        return new InlineKeyboardMarkup(rowList);

    }

    public static InlineKeyboardMarkup AnswerForUser() {

        InlineKeyboardButton replayAnswer = getButton("Replay message", "replay_message");


        List<InlineKeyboardButton> row3 = getRow(replayAnswer);

        List<List<InlineKeyboardButton>> rowList = getRowList(row3);

        return new InlineKeyboardMarkup(rowList);

    }


    public static InlineKeyboardMarkup AdminBarchaObunachilar() {
        InlineKeyboardButton buyurtmachi_plus = getButton("Buyurtma beruvchi \uD83D\uDCDD ➕", "buyurtmachi_plus");
        InlineKeyboardButton yetkazuvchi_plus = getButton("Yetkazib beruvchi \uD83D\uDE9A ➕", "yetkazuvchi_plus");
        InlineKeyboardButton back = getButton("⏪ Ortga qaytish", "back_button_from_AdminBarchaObunachilar");


        List<InlineKeyboardButton> row1 = getRow(buyurtmachi_plus, yetkazuvchi_plus);
        List<InlineKeyboardButton> row2 = getRow(back);


        List<List<InlineKeyboardButton>> rowList = getRowList(row1, row2);

        return new InlineKeyboardMarkup(rowList);

    }


    public static InlineKeyboardMarkup AdminBuyurtmaBeruvchilar() {
        InlineKeyboardButton buyurtmachi_plus = getButton("O'chirish ✖️", "buyurtmachi_minus");
        InlineKeyboardButton yetkazuvchi_plus = getButton("Yetkazib beruvchi \uD83D\uDE9A", "buyurtmachi_change_yetkazib_beruvci");
        InlineKeyboardButton back = getButton("⏪ Ortga qaytish", "back_button_from_AdminBuyurtmaBeruvchilar");


        List<InlineKeyboardButton> row1 = getRow(buyurtmachi_plus, yetkazuvchi_plus);
        List<InlineKeyboardButton> row2 = getRow(back);


        List<List<InlineKeyboardButton>> rowList = getRowList(row1, row2);

        return new InlineKeyboardMarkup(rowList);

    }


    public static InlineKeyboardMarkup AdminYetkazibBeruvchilar() {
        InlineKeyboardButton buyurtmachi_plus = getButton("O'chirish ✖️", "yetkazuvchi_minus");
        InlineKeyboardButton yetkazuvchi_plus = getButton("Buyurtma beruvchi \uD83D\uDCDD", "yetkazib_beruvci_change_buyurtmachi");
        InlineKeyboardButton back = getButton("⏪ Ortga qaytish", "back_button_from_AdminYetkazibBeruvchilar");


        List<InlineKeyboardButton> row1 = getRow(buyurtmachi_plus, yetkazuvchi_plus);
        List<InlineKeyboardButton> row2 = getRow(back);


        List<List<InlineKeyboardButton>> rowList = getRowList(row1, row2);

        return new InlineKeyboardMarkup(rowList);

    }


    public static InlineKeyboardMarkup EmployeePlusListForAdmin() {

        EmployeeService.loadAllEmployeeList();


        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (Employee employee : Database.ALLEMPLOYEELIST) {

            if (employee.getFirstName() != null && employee.getLastName() == null) {
                InlineKeyboardButton button = new InlineKeyboardButton(employee.getFirstName());

                List<InlineKeyboardButton> buttonList = new ArrayList<>();
                button.setCallbackData("choose_employee_plus/" + employee.getId());
                buttonList.add(button);
                rowList.add(buttonList);

            } else if (employee.getFirstName() == null && employee.getLastName() != null) {
                InlineKeyboardButton button = new InlineKeyboardButton(employee.getLastName());

                List<InlineKeyboardButton> buttonList = new ArrayList<>();
                button.setCallbackData("choose_employee_plus/" + employee.getId());
                buttonList.add(button);
                rowList.add(buttonList);

            } else {
                InlineKeyboardButton button = new InlineKeyboardButton(employee.getFirstName() + " " + employee.getLastName());

                List<InlineKeyboardButton> buttonList = new ArrayList<>();
                button.setCallbackData("choose_employee_plus/" + employee.getId());
                buttonList.add(button);
                rowList.add(buttonList);
            }
        }

        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton("⏪ Ortga qaytish");
        button.setCallbackData("back_button_to_employee_plus");
        buttonList.add(button);
        rowList.add(buttonList);

        return new InlineKeyboardMarkup(rowList);

    }


    public static InlineKeyboardMarkup EmployeeMinusListForAdmin() {

        EmployeeService.loadEmployeeList();


        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (Employee employee : Database.EMPLOYEELIST) {

            if (employee.getFirstName() != null && employee.getLastName() == null) {
                InlineKeyboardButton button = new InlineKeyboardButton(employee.getFirstName());

                List<InlineKeyboardButton> buttonList = new ArrayList<>();
                button.setCallbackData("choose_employee_minus/" + employee.getId());
                buttonList.add(button);
                rowList.add(buttonList);

            } else if (employee.getFirstName() == null && employee.getLastName() != null) {
                InlineKeyboardButton button = new InlineKeyboardButton(employee.getLastName());

                List<InlineKeyboardButton> buttonList = new ArrayList<>();
                button.setCallbackData("choose_employee_minus/" + employee.getId());
                buttonList.add(button);
                rowList.add(buttonList);

            } else {
                InlineKeyboardButton button = new InlineKeyboardButton(employee.getFirstName() + " " + employee.getLastName());

                List<InlineKeyboardButton> buttonList = new ArrayList<>();
                button.setCallbackData("choose_employee_minus/" + employee.getId());
                buttonList.add(button);
                rowList.add(buttonList);
            }
        }

        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton("⏪ Ortga qaytish");
        button.setCallbackData("back_button_to_employee_minus");
        buttonList.add(button);
        rowList.add(buttonList);

        return new InlineKeyboardMarkup(rowList);

    }


    public static InlineKeyboardMarkup EmployeeChangeSuplierListForAdmin() {

        EmployeeService.loadEmployeeList();


        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (Employee employee : Database.EMPLOYEELIST) {

            if (employee.getFirstName() != null && employee.getLastName() == null) {
                InlineKeyboardButton button = new InlineKeyboardButton(employee.getFirstName());

                List<InlineKeyboardButton> buttonList = new ArrayList<>();
                button.setCallbackData("choose_employee_change/" + employee.getId());
                buttonList.add(button);
                rowList.add(buttonList);

            } else if (employee.getFirstName() == null && employee.getLastName() != null) {
                InlineKeyboardButton button = new InlineKeyboardButton(employee.getLastName());

                List<InlineKeyboardButton> buttonList = new ArrayList<>();
                button.setCallbackData("choose_employee_change/" + employee.getId());
                buttonList.add(button);
                rowList.add(buttonList);

            } else {
                InlineKeyboardButton button = new InlineKeyboardButton(employee.getFirstName() + " " + employee.getLastName());

                List<InlineKeyboardButton> buttonList = new ArrayList<>();
                button.setCallbackData("choose_employee_change/" + employee.getId());
                buttonList.add(button);
                rowList.add(buttonList);
            }
        }

        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton("⏪ Ortga qaytish");
        button.setCallbackData("back_button_to_employee_change_suplier");
        buttonList.add(button);
        rowList.add(buttonList);

        return new InlineKeyboardMarkup(rowList);

    }


    public static InlineKeyboardMarkup SuplierPlusListForAdmin() {

        EmployeeService.loadAllEmployeeList();


        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (Employee employee : Database.ALLEMPLOYEELIST) {

            if (employee.getFirstName() != null && employee.getLastName() == null) {
                InlineKeyboardButton button = new InlineKeyboardButton(employee.getFirstName());

                List<InlineKeyboardButton> buttonList = new ArrayList<>();
                button.setCallbackData("choose_suplier_plus/" + employee.getId());
                buttonList.add(button);
                rowList.add(buttonList);

            } else if (employee.getFirstName() == null && employee.getLastName() != null) {
                InlineKeyboardButton button = new InlineKeyboardButton(employee.getLastName());

                List<InlineKeyboardButton> buttonList = new ArrayList<>();
                button.setCallbackData("choose_suplier_plus/" + employee.getId());
                buttonList.add(button);
                rowList.add(buttonList);

            } else {
                InlineKeyboardButton button = new InlineKeyboardButton(employee.getFirstName() + " " + employee.getLastName());

                List<InlineKeyboardButton> buttonList = new ArrayList<>();
                button.setCallbackData("choose_suplier_plus/" + employee.getId());
                buttonList.add(button);
                rowList.add(buttonList);
            }
        }

        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton("⏪ Ortga qaytish");
        button.setCallbackData("back_button_to_suplier_plus");
        buttonList.add(button);
        rowList.add(buttonList);

        return new InlineKeyboardMarkup(rowList);

    }


    public static InlineKeyboardMarkup SuplierMinusListForAdmin() {

        EmployeeService.loadSuplierList();


        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (Employee employee : Database.SUPLIERLIST) {

            if (employee.getFirstName() != null && employee.getLastName() == null) {
                InlineKeyboardButton button = new InlineKeyboardButton(employee.getFirstName());

                List<InlineKeyboardButton> buttonList = new ArrayList<>();
                button.setCallbackData("choose_suplier_minus/" + employee.getId());
                buttonList.add(button);
                rowList.add(buttonList);

            } else if (employee.getFirstName() == null && employee.getLastName() != null) {
                InlineKeyboardButton button = new InlineKeyboardButton(employee.getLastName());

                List<InlineKeyboardButton> buttonList = new ArrayList<>();
                button.setCallbackData("choose_suplier_minus/" + employee.getId());
                buttonList.add(button);
                rowList.add(buttonList);

            } else {
                InlineKeyboardButton button = new InlineKeyboardButton(employee.getFirstName() + " " + employee.getLastName());

                List<InlineKeyboardButton> buttonList = new ArrayList<>();
                button.setCallbackData("choose_suplier_minus/" + employee.getId());
                buttonList.add(button);
                rowList.add(buttonList);
            }
        }

        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton("⏪ Ortga qaytish");
        button.setCallbackData("back_button_to_suplier_minus");
        buttonList.add(button);
        rowList.add(buttonList);

        return new InlineKeyboardMarkup(rowList);

    }


    public static InlineKeyboardMarkup SuplierChangeEmployeeListForAdmin() {

        EmployeeService.loadSuplierList();


        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (Employee employee : Database.SUPLIERLIST) {

            if (employee.getFirstName() != null && employee.getLastName() == null) {
                InlineKeyboardButton button = new InlineKeyboardButton(employee.getFirstName());

                List<InlineKeyboardButton> buttonList = new ArrayList<>();
                button.setCallbackData("choose_suplier_change/" + employee.getId());
                buttonList.add(button);
                rowList.add(buttonList);

            } else if (employee.getFirstName() == null && employee.getLastName() != null) {
                InlineKeyboardButton button = new InlineKeyboardButton(employee.getLastName());

                List<InlineKeyboardButton> buttonList = new ArrayList<>();
                button.setCallbackData("choose_suplier_change/" + employee.getId());
                buttonList.add(button);
                rowList.add(buttonList);

            } else {
                InlineKeyboardButton button = new InlineKeyboardButton(employee.getFirstName() + " " + employee.getLastName());

                List<InlineKeyboardButton> buttonList = new ArrayList<>();
                button.setCallbackData("choose_suplier_change/" + employee.getId());
                buttonList.add(button);
                rowList.add(buttonList);
            }
        }

        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton("⏪ Ortga qaytish");
        button.setCallbackData("back_button_to_suplier_change_employee");
        buttonList.add(button);
        rowList.add(buttonList);

        return new InlineKeyboardMarkup(rowList);

    }


    public static InlineKeyboardMarkup MahsulotButton() {
        InlineKeyboardButton mahsulot_plus = getButton("Mahsulot ➕", "mahsulot_plus");
        InlineKeyboardButton mahsulot_minus = getButton("Mahsulot ✖️", "mahsulot_minus");
        InlineKeyboardButton mahsulot_edit = getButton("Mahsulot \uD83D\uDD01", "mahsulot_edit");
        InlineKeyboardButton mahsulot_korish = getButton("Mahsulotni ko'rish ⏺", "mahsulot_korish");
        InlineKeyboardButton back = getButton("⏪ Ortga qaytish", "back_from_mahsulot_button");

        List<InlineKeyboardButton> row1 = getRow(mahsulot_plus, mahsulot_minus);
        List<InlineKeyboardButton> row2 = getRow(mahsulot_edit, mahsulot_korish);
        List<InlineKeyboardButton> row3 = getRow(back);

        List<List<InlineKeyboardButton>> rowList = getRowList(row1, row2, row3);

        return new InlineKeyboardMarkup(rowList);

    }

    public static InlineKeyboardMarkup confirmAddProductMarkup() {

        InlineKeyboardButton commit = getButton("Ha", "add_product_commit");
        InlineKeyboardButton cancel = getButton("Yo'q", "add_product_cancel");

        return new InlineKeyboardMarkup(getRowList(getRow(commit, cancel)));
    }

    public static InlineKeyboardMarkup MahsulotMinus() {

        ProductService.loadProductList();

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (Product product : Database.PRODUCTLIST) {

            InlineKeyboardButton button = new InlineKeyboardButton(product.getName());
            List<InlineKeyboardButton> buttonList = new ArrayList<>();
            button.setCallbackData("mahsulot_minus/" + product.getId());
            buttonList.add(button);
            rowList.add(buttonList);
        }


        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton("⏪ Ortga qaytish");
        button.setCallbackData("back_button_from_mahsulot_minus");
        buttonList.add(button);
        rowList.add(buttonList);

        return new InlineKeyboardMarkup(rowList);

    }

    public static InlineKeyboardMarkup MahsulotEditButton() {

        InlineKeyboardButton edit_name = getButton("Nomini \uD83D\uDD01", "mahsulot_name_edit");
        InlineKeyboardButton edit_price = getButton("Narxini \uD83D\uDD01", "mahsulot_price_edit");
        InlineKeyboardButton edit_image = getButton("Rasmini \uD83D\uDD01", "mahsulot_image_edit");
        InlineKeyboardButton back = getButton("⏪ Ortga qaytish", "back_from_mahsulot_edit");

        List<InlineKeyboardButton> row1 = getRow(edit_name, edit_price, edit_image);
        List<InlineKeyboardButton> row2 = getRow(back);

        List<List<InlineKeyboardButton>> rowList = getRowList(row1, row2);

        return new InlineKeyboardMarkup(rowList);

    }


    public static InlineKeyboardMarkup MahsulotNameEdit() {

        ProductService.loadProductList();

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (Product product : Database.PRODUCTLIST) {

            InlineKeyboardButton button = new InlineKeyboardButton(product.getName());
            List<InlineKeyboardButton> buttonList = new ArrayList<>();
            button.setCallbackData("mahsulot_name_edit/" + product.getId());
            buttonList.add(button);
            rowList.add(buttonList);
        }


        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton("⏪ Ortga qaytish");
        button.setCallbackData("back_button_from_mahsulot_name_edit");
        buttonList.add(button);
        rowList.add(buttonList);

        return new InlineKeyboardMarkup(rowList);

    }


    public static InlineKeyboardMarkup MahsulotPriceEdit() {

        ProductService.loadProductList();

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (Product product : Database.PRODUCTLIST) {

            InlineKeyboardButton button = new InlineKeyboardButton(product.getName());
            List<InlineKeyboardButton> buttonList = new ArrayList<>();
            button.setCallbackData("mahsulot_price_edit/" + product.getId());
            buttonList.add(button);
            rowList.add(buttonList);
        }


        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton("⏪ Ortga qaytish");
        button.setCallbackData("back_button_from_mahsulot_price_edit");
        buttonList.add(button);
        rowList.add(buttonList);

        return new InlineKeyboardMarkup(rowList);

    }

    public static InlineKeyboardMarkup MahsulotImageEdit() {

        ProductService.loadProductList();

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (Product product : Database.PRODUCTLIST) {

            InlineKeyboardButton button = new InlineKeyboardButton(product.getName());
            List<InlineKeyboardButton> buttonList = new ArrayList<>();
            button.setCallbackData("mahsulot_image_edit/" + product.getId());
            buttonList.add(button);
            rowList.add(buttonList);
        }


        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton("⏪ Ortga qaytish");
        button.setCallbackData("back_button_from_mahsulot_image_edit");
        buttonList.add(button);
        rowList.add(buttonList);

        return new InlineKeyboardMarkup(rowList);

    }

    public static InlineKeyboardMarkup EmployeeStart() {

        InlineKeyboardButton orderList = getButton("Buyurtmalar \uD83D\uDECD", "buyurtmalar_button");

        List<InlineKeyboardButton> row1 = getRow(orderList);

        List<List<InlineKeyboardButton>> rowList = getRowList(row1);

        return new InlineKeyboardMarkup(rowList);

    }


    private static InlineKeyboardButton getButton(String demo, String data) {
        InlineKeyboardButton button = new InlineKeyboardButton(demo);
        button.setCallbackData(data);
        return button;
    }


    private static List<InlineKeyboardButton> getRow(InlineKeyboardButton... buttons) {
        return Arrays.asList(buttons);
    }


    private static List<List<InlineKeyboardButton>> getRowList(List<InlineKeyboardButton>... rows) {
        return Arrays.asList(rows);
    }

}
