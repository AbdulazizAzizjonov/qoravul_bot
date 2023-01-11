package com.company.container;

import com.company.QoravulBot;
import com.company.enums.AdminStatus;
import com.company.model.Product;

import java.util.HashMap;
import java.util.Map;

public class ComponentContainer {

    public static final String BOT_TOKEN = "5739080987:AAFx2X8bLD3utbwD3Zqp8ijbPoee3qPsG7Y";

    public static final String BOT_NAME = "newqoravul_bot";

    public static QoravulBot SUVBOT;

    public static Map<String, Product> productMap = new HashMap<>();

    public static Map<String, Product> crudStepMap = new HashMap<>();
    public static Map<String, AdminStatus> productStepMap = new HashMap<>();

}
