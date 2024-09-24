package org.dateroad.config;

import lombok.RequiredArgsConstructor;
import org.dateroad.auth.argumentresolve.UserIdArgumentResolver;
import org.dateroad.date.domain.Region;
import org.dateroad.date.domain.Region.MainRegion;
import org.dateroad.date.domain.Region.SubRegion;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final UserIdArgumentResolver userIdArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userIdArgumentResolver);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToSubRegionConverter());
        registry.addConverter(new StringToMainRegionConverter());
    }

    static class StringToSubRegionConverter implements Converter<String, SubRegion> {
        @Override
        public Region.SubRegion convert(final String source) {
            return Region.SubRegion.fromString(source);
        }
    }

    static class StringToMainRegionConverter implements Converter<String, MainRegion>{
        @Override
        public Region.MainRegion convert(final String source) {
            return Region.MainRegion.fromString(source);
        }
    }
}
