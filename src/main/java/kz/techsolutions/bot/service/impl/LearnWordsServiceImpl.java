package kz.techsolutions.bot.service.impl;

import kz.techsolutions.bot.api.FileService;
import kz.techsolutions.bot.api.LearnWordsService;
import kz.techsolutions.bot.api.dto.UserCacheDTO;
import kz.techsolutions.bot.api.dto.WorkLanguage;
import kz.techsolutions.bot.api.exception.BotAppException;
import kz.techsolutions.bot.service.WordBotConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@Service
@PropertySource("classpath:application.properties")
public class LearnWordsServiceImpl implements LearnWordsService {

    @Autowired
    private Logger log;

    private Map<String, UserCacheDTO> userCache;

    @PostConstruct
    public void init() {
        userCache = new HashMap<>();
    }

    @Autowired
    private FileService fileService;

    @Value("${filesDirectory}")
    private String filesDirectory;

    @Value("${noCacheMessage}")
    private String noCacheMessage;

    @Autowired
    private WordBotConstants wordBotConstants;

    @Override
    public String getNextWordWithTranslation(String username) throws BotAppException {
        UserCacheDTO userCacheDTO = getCacheInternal(username);
        if (Objects.nonNull(userCacheDTO.getCurrentLang())) {
            return noCacheMessage;
        }
        return getLineAndUpdateCache(username);
    }

    @Override
    public String getNextWordInEnglish(String username) throws BotAppException {
        UserCacheDTO userCacheDTO = getCacheInternal(username);
        if (Objects.equals(userCacheDTO.getCurrentLang(), WorkLanguage.RUSSIAN)
                || Objects.isNull(userCacheDTO.getFileName())) {
            return noCacheMessage;
        }
        userCacheDTO.setCurrentLang(WorkLanguage.ENGLISH);
        if (Objects.isNull(userCacheDTO.getRussianTranslation())) {
            String line = getLineAndUpdateCache(username);
            if (StringUtils.isEmpty(line) || line.split("-").length != 2) {
                return noCacheMessage;
            }
            String[] translations = line.split("-");
            String russianTranslation = translations[1];
            userCacheDTO.setRussianTranslation(russianTranslation);
            userCache.put(username, userCacheDTO);
            return translations[0];
        }
        String russianTranslation = userCacheDTO.getRussianTranslation();
        userCacheDTO.setRussianTranslation(null);
        userCache.put(username, userCacheDTO);
        return russianTranslation;
    }

    @Override
    public String getNextWordInRussian(String username) throws BotAppException {
        UserCacheDTO userCacheDTO = getCacheInternal(username);
        if (Objects.equals(userCacheDTO.getCurrentLang(), WorkLanguage.ENGLISH)
                || Objects.isNull(userCacheDTO.getFileName())) {
            return noCacheMessage;
        }
        userCacheDTO.setCurrentLang(WorkLanguage.RUSSIAN);
        if (Objects.isNull(userCacheDTO.getEnglishTranslation())) {
            String line = getLineAndUpdateCache(username);
            if (StringUtils.isEmpty(line) || line.split("-").length != 2) {
                return noCacheMessage;
            }
            String[] translations = line.split("-");
            String englishTranslation = translations[0];
            userCacheDTO.setEnglishTranslation(englishTranslation);
            userCache.put(username, userCacheDTO);
            return translations[1];
        }
        String englishTranslation = userCacheDTO.getEnglishTranslation();
        userCacheDTO.setEnglishTranslation(null);
        userCache.put(username, userCacheDTO);
        return englishTranslation;
    }

    @Override
    public String getRandomWordInEnglish(String username) throws BotAppException {
        UserCacheDTO userCacheDTO = getCacheInternal(username);
        if (Objects.isNull(userCacheDTO.getFileName())) {
            return noCacheMessage;
        }
        return fileService.readRandomLineFromFile(setAbsoluteFileName(userCacheDTO.getFileName()));
    }

    @Override
    public String getRandomWordInRussian(String username) throws BotAppException {
        UserCacheDTO userCacheDTO = getCacheInternal(username);
        if (Objects.isNull(userCacheDTO.getFileName())) {
            return noCacheMessage;
        }
        String line = fileService.readRandomLineFromFile(setAbsoluteFileName(userCacheDTO.getFileName()));
        return line;
    }

    @Override // TODO: 12/3/17 Add Spring cache
    public List<String> getFileList() throws BotAppException {
        List<String> fileNames = new ArrayList<>();
        try (Stream<Path> paths = Files.list(Paths.get(filesDirectory))) {
            paths.filter(Files::isRegularFile).forEach(path -> fileNames.add(path.toFile().getName()));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new BotAppException(e);
        }
        return fileNames;
    }

    @Override
    public void setFileNameForUser(String username, String fileStr) throws BotAppException {
        String fileName = fileStr.split(wordBotConstants.getFileSeperator())[1];
        UserCacheDTO userCacheDTO = getCacheInternal(username);
        userCacheDTO.setFileName(fileName);
        userCacheDTO.setLastLine(0);
        userCacheDTO.setCurrentLang(null);
        userCache.put(username, userCacheDTO);
    }

    @Override
    public Map<String, UserCacheDTO> getCache() throws BotAppException {
        return userCache;
    }

    @Override
    public UserCacheDTO getCacheByUsername(String username) throws BotAppException {
        return userCache.get(username);
    }

    @Override
    public String getLineAndUpdateCache(String username) throws BotAppException {
        UserCacheDTO userCacheDTO = getCacheInternal(username);
        if (Objects.isNull(userCacheDTO.getFileName())) {
            return noCacheMessage;
        }
        Integer lastLine = userCacheDTO.getLastLine();
        if (Objects.isNull(lastLine)) {
            lastLine = 0;
        }
        String line = fileService.readFileByLine(lastLine, setAbsoluteFileName(userCacheDTO.getFileName()));
        userCacheDTO.setLastLine(lastLine + 1);
        userCache.put(username, userCacheDTO);
        return line;
    }

    @Override
    public void setLastLine(String username, int line) throws BotAppException {
        UserCacheDTO userCacheDTO = getCacheInternal(username);
        userCacheDTO.setLastLine(line);
        userCache.put(username, userCacheDTO);
    }

    private UserCacheDTO getCacheInternal(String username) {
        UserCacheDTO userCacheDTO = userCache.get(username);
        if (Objects.isNull(userCacheDTO)) {
            userCacheDTO = new UserCacheDTO();
        }
        return userCacheDTO;
    }

    private String setAbsoluteFileName(String fileName) {
        return filesDirectory.concat(fileName);
    }
}