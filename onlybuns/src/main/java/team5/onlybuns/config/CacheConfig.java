package team5.onlybuns.config;

import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.cache.Caching;
import java.io.IOException;

@Configuration
public class CacheConfig {

    @Bean
    public JCacheCacheManager cacheManager() throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource resource = resolver.getResource("classpath:ehcache.xml");

        javax.cache.CacheManager ehCacheManager = Caching.getCachingProvider()
                .getCacheManager(resource.getURI(), getClass().getClassLoader());

        return new JCacheCacheManager(ehCacheManager);
    }
}
