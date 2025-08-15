package price_hunt.orchestrator.config;

import com.rabbitmq.client.ConnectionFactory;
import org.apache.camel.component.rabbitmq.RabbitMQComponent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean(name = "customRabbitConnectionFactory")
    public ConnectionFactory rabbitConnectionFactory() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("jackal-01.rmq.cloudamqp.com");
        factory.setPort(5671);
        factory.setUsername("auwniwgm");
        factory.setPassword("p1GHpstOPQVyxMyZZnJL7TkTSs35lhCu");
        factory.setVirtualHost("auwniwgm");
        factory.useSslProtocol(); // ⚠️ importante
        return factory;
    }

    @Bean
    public RabbitMQComponent rabbitMQComponent(@Qualifier("customRabbitConnectionFactory") ConnectionFactory connectionFactory) {
        RabbitMQComponent component = new RabbitMQComponent();
        component.setConnectionFactory(connectionFactory);
        return component;
    }
}