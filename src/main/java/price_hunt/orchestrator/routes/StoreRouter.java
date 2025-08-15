package price_hunt.orchestrator.routes;



import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;

import org.springframework.stereotype.Component;
import price_hunt.orchestrator.DTO.StoreDTO;



@Component
public class StoreRouter extends RouteBuilder {
    public void configure() {
        // Configurações do Camel e de padrões de formatos
        restConfiguration().component("servlet").bindingMode(RestBindingMode.json).dataFormatProperty("objectMapper", "#objectMapper").contextPath("/api").port(8080);

        // Rotas disponíveis do orquestrador
        rest("/store")
                .post()
                .consumes("application/json")
                .produces("application/json")
                .type(StoreDTO.class)
                .to("direct:create")
                .get("/{id}")
                .to("direct:findById")
                .produces("application/json")
                .delete("/{id}")
                .to("direct:deleteById")



        ;

        from("direct:create")
                .routeId("createStore")
                .log("Criando Loja: ${body}")
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .setHeader("Accept-Encoding", constant("identity"))
                .marshal().json(JsonLibrary.Jackson)
                .log("Body depois de marshalizar e antes de mandar pra api: ${body}")
                .to("http://localhost:8080/store?bridgeEndpoint=true")
                .convertBodyTo(String.class)
                .log("Mensagem crua recebida: ${body}")
                .unmarshal("jsonDataFormat");



        from("direct:findById")
                .routeId("findStoreById")
                .log("Buscando loja com ID: ${header.id}")
                .setHeader("Accept-Encoding", constant("identity"))
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .toD("https://price-hunt-api.onrender.com/store/${header.id}?bridgeEndpoint=true")
                .unmarshal("jsonDataFormat");

        from("direct:deleteById")
                .routeId("deleteStoreById")
                .log("Deletando loja com ID: ${header.id}")
                .setHeader("Accept-Encoding", constant("identity"))
                .setHeader(Exchange.HTTP_METHOD, constant("DELETE"))
                .toD("http://localhost:8080/store/${header.id}?bridgeEndpoint=true")
                .unmarshal("jsonDataFormat");

    }


}


