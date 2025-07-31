package price_hunt.orchestrator.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer addJavaTimeModule() {
        return builder -> builder.modules(new JavaTimeModule());
    }
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
    @Bean(name = "jsonDataFormat")
    public JacksonDataFormat userDataFormat(ObjectMapper objectMapper) {
        JacksonDataFormat format = new JacksonDataFormat();
        format.setObjectMapper(objectMapper);
        return format;
    }

}
