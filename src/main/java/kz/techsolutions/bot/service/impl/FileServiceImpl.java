package kz.techsolutions.bot.service.impl;

import kz.techsolutions.bot.api.FileService;
import kz.techsolutions.bot.api.exception.BotAppException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private Logger log;

    private Random random;

    @PostConstruct
    public void init() {
        random = new Random();
    }

    @Override
    public String readFileByLine(int startLine, String fileName) throws BotAppException {
        try (InputStream inputStream = new FileInputStream(new File(fileName))) {
            LineIterator lineIterator = IOUtils.lineIterator(inputStream, Charset.forName("UTF-8"));
            int i = 0;
            while (lineIterator.hasNext()) {
                String line = lineIterator.next();
                if (i++ >= startLine) {
                    return line.replaceAll("=", "-");
                }
            }
            return "";
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new BotAppException(e);
        }
    }

    @Override
    public String readRandomLineFromFile(String fileName) throws BotAppException {
        try {
            long fileLines = Files.lines(Paths.get(fileName)).count();
            int startLine = random.nextInt(Long.valueOf(fileLines).intValue() + 1);
            return readFileByLine(startLine, fileName);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new BotAppException(e);
        }
    }
}