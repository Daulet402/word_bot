package kz.techsolutions.bot.service.impl;

import kz.techsolutions.bot.api.LearnWordsService;
import kz.techsolutions.bot.api.WordBotService;
import kz.techsolutions.bot.api.exception.BotAppException;
import kz.techsolutions.bot.service.WordBotConstants;
import kz.techsolutions.bot.utils.WordBotUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class WordBotServiceImpl implements WordBotService {

    @Autowired
    private Logger log;

    @Autowired
    private LearnWordsService learnWordsService;

    @Autowired
    private WordBotConstants wordBotConstants;

    @Autowired
    @Qualifier("cacheMenu")
    private ReplyKeyboardMarkup cacheMenu;

    @Autowired
    @Qualifier("fileListMenu")
    private ReplyKeyboardMarkup fileListMenu;

    @Autowired
    @Qualifier("updateCacheMenu")
    private ReplyKeyboardMarkup updateCacheMenu;

    @Autowired
    @Qualifier("wordsMenu")
    private ReplyKeyboardMarkup wordsMenu;

    @Autowired
    @Qualifier("mainMenu")
    private ReplyKeyboardMarkup mainMenu;


    @Override
    public SendMessage processAndGetSendMessage(Update update) throws BotAppException {
        Long chatId = getChatId(update);
        try {
            if (Objects.nonNull(update.getMessage())) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                String username = Objects.nonNull(update.getMessage().getFrom()) ?
                        update.getMessage().getFrom().getUserName() : null;
                String text = Objects.nonNull(update.getMessage().getText()) ? update.getMessage().getText() : "";
                log.info(String.format("text: %s from: %s", text, username));

                if (Objects.equals(wordBotConstants.getStartText(), update.getMessage().getText())) {
                    sendMessage.setText(String.format(
                            wordBotConstants.getGreetingResponse(),
                            "@" + username
                    ));
                    sendMessage.setReplyMarkup(mainMenu);
                } else if (Objects.equals(wordBotConstants.getWordsText(), text)) {
                    sendMessage.setText(wordBotConstants.getWordsResponse());
                    sendMessage.setReplyMarkup(wordsMenu);
                } else if (Objects.equals(wordBotConstants.getWordsWithTranslationsText(), text)) {
                    sendMessage.setText(learnWordsService.getNextWordWithTranslation(username));
                } else if (Objects.equals(wordBotConstants.getAllCacheText(), text)) {
                    sendMessage.setText(String.valueOf(learnWordsService.getCache()));
                } else if (Objects.equals(wordBotConstants.getMyCacheText(), text)) {
                    sendMessage.setText(String.valueOf(learnWordsService.getCacheByUsername(username)));
                } else if (Objects.equals(wordBotConstants.getCacheText(), text)) {
                    sendMessage.setText(wordBotConstants.getCacheResponse());
                    sendMessage.setReplyMarkup(cacheMenu);
                } else if (Objects.equals(wordBotConstants.getUpdateCacheText(), text)) {
                    sendMessage.setText(wordBotConstants.getUpdateCacheText());
                    sendMessage.setReplyMarkup(updateCacheMenu);
                } else if (text.contains(wordBotConstants.getFileListText())) {
                    List<KeyboardRow> keyboardRows = new ArrayList<>();
                    learnWordsService.getFileList().forEach(fileName -> {
                        KeyboardRow fileButtons = new KeyboardRow();
                        fileButtons.add(wordBotConstants.getFileSeperator() + fileName);
                        keyboardRows.add(fileButtons);
                    });
                    fileListMenu.setKeyboard(keyboardRows);
                    keyboardRows.add(WordBotUtils.setOneButtonRow(wordBotConstants.getBackToMenuText()));
                    sendMessage.setReplyMarkup(fileListMenu);
                    sendMessage.setText(wordBotConstants.getAllFilesResponse());
                } else if (text.contains(wordBotConstants.getFileSeperator())) {
                    learnWordsService.setFileNameForUser(username, text);
                    sendMessage.setText(text + " chosen");
                    sendMessage.setReplyMarkup(mainMenu);
                } else if (Objects.equals(wordBotConstants.getBackToMenuText(), text)) {
                    sendMessage.setText(wordBotConstants.getMainMenuResponse());
                    sendMessage.setReplyMarkup(mainMenu);
                } else if (Objects.equals(wordBotConstants.getWordInEnglishText(), text)) {
                    sendMessage.setText(learnWordsService.getNextWordInEnglish(username));
                } else if (Objects.equals(wordBotConstants.getWordInRussianText(), text)) {
                    String word = learnWordsService.getNextWordInRussian(username);
                    sendMessage.setText(word);
                } else if (Objects.equals(wordBotConstants.getRandomWordsText(), text)) {
                    sendMessage.setText(learnWordsService.getRandomWordInEnglish(username));
                } else {
                    sendMessage.setText(wordBotConstants.getUnknownCommandResponse());
                }
                return sendMessage;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BotAppException(e);
        }
        return null;
    }

    @Override
    public Long getChatId(Update update) throws BotAppException {
        return Objects.nonNull(update.getMessage()) ? update.getMessage().getChatId() : null;
    }
}