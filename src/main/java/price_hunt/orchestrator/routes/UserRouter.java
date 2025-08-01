package price_hunt.orchestrator.routes;



import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;

import org.springframework.stereotype.Component;
import price_hunt.orchestrator.DTO.UserDTO;

import java.time.LocalDate;
import java.time.Period;
@Component
public class UserRouter extends RouteBuilder {
    public void configure() {
        // Configurações do Camel e de padrões de formatos
        restConfiguration().component("servlet").bindingMode(RestBindingMode.json).dataFormatProperty("objectMapper", "#objectMapper").contextPath("/api").port(8080);

        // Rotas disponíveis do orquestrador
        rest("/user")
                .post()
                    .consumes("application/json")
                    .produces("application/json")
                    .type(UserDTO.class)
                    .to("direct:create")
                .get("/{id}").to("direct:findById")
                    .produces("application/json")
//                .delete("/{id}").to("direct:deleteById")
//                .put("/{id}").type(UserDTO.class).to("direct:update")
//                .get("/saudacao/{id}").to("direct:saudacao")
        ;

        from("direct:create")
                .routeId("createUser")
                .log("Criando usuário: ${body}")
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .setHeader("Accept-Encoding", constant("identity"))
                .marshal().json(JsonLibrary.Jackson)
                .to("https://price-hunt-api.onrender.com/user?bridgeEndpoint=true")
                .convertBodyTo(String.class)
                .log("Mensagem crua recebida: ${body}").unmarshal("jsonDataFormat");


        from("direct:findById")
                .routeId("findUserById")
                .log("Buscando usuário com ID: ${header.id}")
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .toD("https://price-hunt-api.onrender.com/user/${header.id}?bridgeEndpoint=true")
                .unmarshal("jsonDataFormat");

                ;
    }


}


