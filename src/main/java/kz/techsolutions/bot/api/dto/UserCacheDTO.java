package kz.techsolutions.bot.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserCacheDTO {
    private String fileName;
    private Integer lastLine;
    private String englishTranslation;
    private String russianTranslation;
    private WorkLanguage currentLang;
}