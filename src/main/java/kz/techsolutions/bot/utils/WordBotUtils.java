package kz.techsolutions.bot.utils;

import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

public class WordBotUtils {

    public static KeyboardRow setOneButtonRow(String text) {
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(text);
        return keyboardRow;
    }
}