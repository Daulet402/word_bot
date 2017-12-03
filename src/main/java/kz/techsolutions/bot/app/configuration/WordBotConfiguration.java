package kz.techsolutions.bot.app.configuration;

import kz.techsolutions.bot.service.WordBotConstants;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.Arrays;

import static kz.techsolutions.bot.utils.WordBotUtils.setOneButtonRow;

@Configuration
@Getter
public class WordBotConfiguration {

    @Autowired
    private WordBotConstants wordBotConstants;

    @Bean
    @Scope("singleton")
    public ReplyKeyboardMarkup cacheMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        replyKeyboardMarkup.setKeyboard(Arrays.asList(
                setOneButtonRow(wordBotConstants.getAllCacheText()),
                setOneButtonRow(wordBotConstants.getMyCacheText()),
                setOneButtonRow(wordBotConstants.getUpdateCacheText()),
                setOneButtonRow(wordBotConstants.getBackToMenuText())
        ));

        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }

    @Bean
    @Scope("singleton")
    public ReplyKeyboardMarkup fileListMenu() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    @Bean
    @Scope("singleton")
    public ReplyKeyboardMarkup updateCacheMenu() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(Arrays.asList(
                setOneButtonRow(wordBotConstants.getSetLastLineText()),
                setOneButtonRow(wordBotConstants.getBackToMenuText())
        ));
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    @Bean
    @Scope("singleton")
    public ReplyKeyboardMarkup wordsMenu() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(Arrays.asList(
                setOneButtonRow(wordBotConstants.getWordsWithTranslationsText()),
                setOneButtonRow(wordBotConstants.getWordInEnglishText()),
                setOneButtonRow(wordBotConstants.getWordInRussianText()),
                setOneButtonRow(wordBotConstants.getRandomWordsText()),
                setOneButtonRow(wordBotConstants.getBackToMenuText())
        ));
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    @Bean
    @Scope("singleton")
    public ReplyKeyboardMarkup mainMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setKeyboard(Arrays.asList(
                setOneButtonRow(wordBotConstants.getWordsText()),
                setOneButtonRow(wordBotConstants.getFileListText()),
                setOneButtonRow(wordBotConstants.getCacheText())
        ));
        return replyKeyboardMarkup;
    }
}