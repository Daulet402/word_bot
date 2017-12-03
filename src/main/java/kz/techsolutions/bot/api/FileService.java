package kz.techsolutions.bot.api;

import kz.techsolutions.bot.api.exception.BotAppException;

import javax.validation.constraints.NotNull;

public interface FileService {

    String readFileByLine(int startLine, @NotNull String fileName) throws BotAppException;

    String readRandomLineFromFile(@NotNull String fileName) throws BotAppException;
}