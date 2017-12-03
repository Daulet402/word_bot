package kz.techsolutions.bot.api;

import kz.techsolutions.bot.api.dto.UserCacheDTO;
import kz.techsolutions.bot.api.exception.BotAppException;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public interface LearnWordsService {

    String getNextWordWithTranslation(@NotNull String username) throws BotAppException;

    String getNextWordInEnglish(@NotNull String username) throws BotAppException;

    String getNextWordInRussian(@NotNull String username) throws BotAppException;

    String getRandomWordInEnglish(@NotNull String username) throws BotAppException;

    String getRandomWordInRussian(@NotNull String username) throws BotAppException;

    List<String> getFileList() throws BotAppException;

    void setFileNameForUser(@NotNull String username, @NotNull String fileStr) throws BotAppException;

    Map<String, UserCacheDTO> getCache() throws BotAppException;

    UserCacheDTO getCacheByUsername(@NotNull String username) throws BotAppException;

    String getLineAndUpdateCache(String username) throws BotAppException;
}