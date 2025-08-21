package price_hunt.orchestrator.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
    @Bean(name = "jsonDataFormat")
    public JacksonDataFormat userDataFormat() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        JacksonDataFormat format = new JacksonDataFormat();
        format.setObjectMapper(mapper);
        return format;
    }

//    @Bean(name = "offerDataFormat")
//    public JacksonDataFormat offerDataFormat(ObjectMapper mapper) {
//        mapper.registerModule(new JavaTimeModule());
//        JacksonDataFormat format = new JacksonDataFormat();
//        format.setObjectMapper(mapper);
//        return format;
//    }
}
