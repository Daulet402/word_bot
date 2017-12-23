package kz.techsolutions.bot.app.configuration;

import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.telegram.telegrambots.TelegramBotsApi;

import java.util.concurrent.TimeUnit;


@EnableCaching
@Configuration
public class BotAppConfiguration {

    @Bean
    public TelegramBotsApi telegramBotsApi() {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        return telegramBotsApi;
    }

    @Bean
    public Logger logger(InjectionPoint injectionPoint) {
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass());
    }

    @Primary
    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Lists.newArrayList(
                filesCache()
        ));
        return cacheManager;
    }

    public GuavaCache filesCache() {
        return new GuavaCache(
                "filesCache",
                CacheBuilder.newBuilder()
                        .maximumSize(100)
                        .expireAfterAccess(20, TimeUnit.MINUTES)
                        .build()
        );
    }
}