package kz.techsolutions.bot.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
@Getter
@PropertySource("classpath:bot.properties")
public class WordBotConstants {

    @Value("${fileSeperator}")
    private String fileSeperator;

    @Value("${wordsText}")
    private String wordsText;

    @Value("${startText}")
    private String startText;

    @Value("${backToMenuText}")
    private String backToMenuText;

    @Value("${cacheText}")
    private String cacheText;

    @Value("${wordsWithTranslationsText}")
    private String wordsWithTranslationsText;

    @Value("${fileListText}")
    private String fileListText;

    @Value("${allCacheText}")
    private String allCacheText;

    @Value("${myCacheText}")
    private String myCacheText;

    @Value("${updateCacheText}")
    private String updateCacheText;

    @Value("${setLastLineText}")
    private String setLastLineText;

    @Value("${wordInEnglishText}")
    private String wordInEnglishText;

    @Value("${wordInRussianText}")
    private String wordInRussianText;

    @Value("${randomWordsText}")
    private String randomWordsText;

    @Value("${allFilesResponse}")
    private String allFilesResponse;

    @Value("${mainMenuResponse}")
    private String mainMenuResponse;

    @Value("${unknownCommandResponse}")
    private String unknownCommandResponse;

    @Value("${cacheResponse}")
    private String cacheResponse;

    @Value("${wordsResponse}")
    private String wordsResponse;

    @Value("${greetingResponse}")
    private String greetingResponse;

    public String getBackToMenuText() {
        try {
            return new String(backToMenuText.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return backToMenuText;
        }
    }

    public String getRandomWordsText() {
        try {
            return new String(randomWordsText.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return randomWordsText;
        }
    }
}