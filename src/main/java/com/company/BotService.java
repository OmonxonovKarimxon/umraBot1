package com.company;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BotService {
    static List<User> tgUsers = new ArrayList<>();

    public static User getUserFromListByChatId(long chatId) {
        for (User user : tgUsers) {
            if (user.getChatId() == chatId) {
                return user;
            }
        }
        User tgUser = new User();
        tgUser.setChatId(chatId);
        tgUser.setBotState(BotState.START);
        tgUsers.add(tgUser);
        return tgUser;
    }

    public static long getChatId(Update update) {
        return update.getCallbackQuery() != null ?
                update.getCallbackQuery().getMessage().getChatId()
                : update.getMessage().getChatId();
    }

    public static SendMessage start(Update update) {
        SendMessage sendMessage = new SendMessage();
        long chatId = getChatId(update);
        sendMessage.setChatId(chatId);
        sendMessage.setText("assalomu alaykum\n ziyorat turini kiriting \nmasalan: umra-2022");
        User user = getUserFromListByChatId(chatId);

        user.setBotState(BotState.TRIP_TYPE);
        return sendMessage;
    }

    public static SendMessage ziyoratTuri(Update update) {
        SendMessage sendMessage = new SendMessage();
        long chatId = getChatId(update);
        sendMessage.setText("guruh raqamini kiriting");
        sendMessage.setChatId(chatId);
        User user = getUserFromListByChatId(chatId);
        user.setBotState(BotState.GROUP_NUMBER);
        return sendMessage;
    }

    public static SendMessage groupNumber(Update update) {
        SendMessage sendMessage = new SendMessage();
        long chatId = getChatId(update);
        sendMessage.setText("excel faylni yuboring");
        sendMessage.setChatId(chatId);
        User user = getUserFromListByChatId(chatId);
        user.setBotState(BotState.SEND_FILE);
        return sendMessage;
    }


    public static String sendFile(List<String> row, String ziyoratTuri, String groupNumber, long chatId) throws IOException {

        String name =row.get(0).charAt(0)+" "+ row.get(2).toUpperCase() + " " + row.get(3).toUpperCase();
        String passport = row.get(6);
        String imagePath = "./src/resources/"+name+".png";

        String surname;
        if (row.get(4).equals("XXX")) {
            surname = "";
        } else {
            surname = row.get(4).toUpperCase();
        }


        //Read the image
        BufferedImage image = ImageIO.read(new File("./src/resources/copy1.png"));

        //get the Graphics object
        Graphics g = image.getGraphics();

        // haj umra
        g.setColor(Color.decode("#01602f"));
        g.setFont(new Font("TimesRoman", Font.BOLD, 85));
        g.drawString(ziyoratTuri, 280, 150);

        //ism familya

        if (name.length() >= 15 && name.length() <= 20) {
            g.setColor(Color.RED);
            g.setFont(new Font("TimesRoman", Font.BOLD, 50));
            g.drawString(name, 50, 300);
            g.drawString(surname, 85, 352);
        } else if (name.length() >= 20 && name.length() < 25) {
            g.setColor(Color.RED);
            g.setFont(new Font("TimesRoman", Font.BOLD, 50));
            g.drawString(name, 50, 300);
            g.drawString(surname, 85, 352);
        } else if (name.length() >= 25) {
            g.setColor(Color.RED);
            g.setFont(new Font("TimesRoman", Font.BOLD, 50));
            g.drawString(name, 50, 300);
            g.drawString(surname, 85, 352);
        }
        if (name.length() < 15) {
            g.setColor(Color.RED);
            g.setFont(new Font("TimesRoman", Font.BOLD, 50));
            g.drawString(name, 50, 300);
            g.drawString(surname, 85, 352);
        }


        //gruppa nomeri
        g.setColor(Color.RED);
        g.setFont(new Font("TimesRoman", Font.BOLD, 50));
        g.drawString(groupNumber, 520, 459);

        //pasport seriya
        g.setColor(Color.RED);
        g.setFont(new Font("TimesRoman", Font.BOLD, 21));
        g.drawString(passport, 100, 512);

        g.dispose();
        //write the image

        ImageIO.write(image, "png", new File(imagePath));




        return imagePath;
    }


    public static Map<Integer, java.util.List<String>> readExcelFile(String filePath) {
        Map<Integer, java.util.List<String>> data = new HashMap<>();
        FileInputStream file;
        try {
            file = new FileInputStream(new File(filePath));
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);

            int i = 0;
            for (Row row : sheet) {
                data.put(i, new ArrayList<String>());
                for (Cell cell : row) {
                    switch (cell.getCellType()) {
                        case STRING:
                            data.get(new Integer(i)).add(cell.getRichStringCellValue().getString());
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                data.get(i).add(cell.getDateCellValue() + "");
                            } else {
                                data.get(i).add(cell.getNumericCellValue() + "");
                            }
                            break;
                        case BOOLEAN:
                            data.get(i).add(cell.getBooleanCellValue() + "");
                            break;
                        case FORMULA:
                            data.get(i).add(cell.getCellFormula() + "");
                            break;
                        default:
                            data.get(new Integer(i)).add(" ");
                    }
                }
                i++;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return data;

    }

    public static void changeState(Update update) {
        User user = getUserFromListByChatId(getChatId(update));
        user.setBotState(BotState.START);

    }
}
