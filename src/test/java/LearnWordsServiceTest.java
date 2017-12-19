import kz.techsolutions.bot.api.LearnWordsService;
import kz.techsolutions.bot.app.BotApplication;
import kz.techsolutions.bot.service.WordBotConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BotApplication.class)
public class LearnWordsServiceTest {

    @Autowired
    private LearnWordsService learnWordsService;

    @Autowired
    private WordBotConstants botConstants;

    @Value("${filesDirectory}")
    private String filesDirectory;

    @Before
    public void init() {
    }

    @Test
    public void testField() {
        assertNotNull(learnWordsService);
    }

    @Test
    public void testFileList() throws Exception {
        assertNotNull(learnWordsService.getFileList());
        assertTrue(learnWordsService.getFileList().size() > 0);
    }

    @Test
    public void testSetFile() throws Exception {
        List<String> files = learnWordsService.getFileList();
        for (String file : files) {
            learnWordsService.setFileNameForUser("daulet", botConstants.getFileSeperator() + file);
        }
    }

    @Test
    public void testGetNextWordWithTranslation() throws Exception {
        List<String> files = learnWordsService.getFileList();
        Integer count = 0;
        for (String file : files) {
            learnWordsService.setFileNameForUser("daulet", botConstants.getFileSeperator() + file);
            long fileLines = Files.lines(Paths.get(filesDirectory + file)).count();

            for (int i = 0; i < fileLines; i++) {
                String line = learnWordsService.getNextWordWithTranslation("daulet");
                assertNotNull(line);
            }
            count++;
            System.out.println("success for " + file);
        }
        assertEquals(count.longValue(), files.size());
    }

    @Test
    public void testGetNextWordInEnglish() throws Exception {
        List<String> files = learnWordsService.getFileList();
        Integer count = 0;
        for (String file : files) {
            learnWordsService.setFileNameForUser("daulet", botConstants.getFileSeperator() + file);
            long fileLines = Files.lines(Paths.get(filesDirectory + file)).count();

            for (int i = 0; i < fileLines; i++) {
                String line = learnWordsService.getNextWordInEnglish("daulet");
                assertNotNull(line);
            }
            count++;
            System.out.println("success for " + file);
        }
        assertEquals(count.longValue(), files.size());
    }

    @Test
    public void testGetNextWordInRussian() throws Exception {
        List<String> files = learnWordsService.getFileList();
        Integer count = 0;
        for (String file : files) {
            learnWordsService.setFileNameForUser("daulet", botConstants.getFileSeperator() + file);
            long fileLines = Files.lines(Paths.get(filesDirectory + file)).count();

            for (int i = 0; i < fileLines; i++) {
                String line = learnWordsService.getNextWordInRussian("daulet");
                assertNotNull(line);
            }
            count++;
            System.out.println("success for " + file);
        }
        assertEquals(count.longValue(), files.size());
    }

    @Test
    public void testGetRandomWordInEnglish() throws Exception {
        List<String> files = learnWordsService.getFileList();
        Integer count = 0;
        for (String file : files) {
            learnWordsService.setFileNameForUser("daulet", botConstants.getFileSeperator() + file);
            long fileLines = Files.lines(Paths.get(filesDirectory + file)).count();

            for (int i = 0; i < fileLines; i++) {
                String line = learnWordsService.getRandomWordInEnglish("daulet");
                assertNotNull(line);
            }
            count++;
            System.out.println("success for " + file);
        }
        assertEquals(count.longValue(), files.size());
    }

    @Test
    public void testSetLineMEthod() throws Exception {
        learnWordsService.setLastLine("daulet", 25);
    }

    @Test
    public void testAdminUserNames() throws Exception {
        assertTrue(!CollectionUtils.isEmpty(botConstants.getAdminUserNames()));
    }
}

