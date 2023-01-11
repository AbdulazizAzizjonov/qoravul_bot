package com.company.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Arrays;
import java.util.List;

public class InlineKeyboardUtilForEmployee {


    public static InlineKeyboardMarkup EmployeeStart() {
        InlineKeyboardButton employeeAllList = getButton("Buyurtma Berish", "orderForEmployee_button");
//        InlineKeyboardButton employeeList = getButton("Buyurtma beruvchi \uD83D\uDCDD", "xodimlar_button");
//        InlineKeyboardButton suplierList = getButton("Yetkazib beruvchi \uD83D\uDE9A", "yetkazibBeruvchi_button");
//        InlineKeyboardButton settings = getButton("Mahsulotlar \uD83D\uDECD", "mahsulot_button");
//        InlineKeyboardButton monthReport = getButton("Oylik Hisobot", "oylikHisobot_button");

        List<InlineKeyboardButton> row1 = getRow(employeeAllList);
//        List<InlineKeyboardButton> row2 = getRow(employeeList, suplierList);
//        List<InlineKeyboardButton> row3 = getRow(settings, monthReport);

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
