package price_hunt.orchestrator.routes;



import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.transform.Json;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import price_hunt.orchestrator.DTO.OfferDTO;

import java.io.InputStream;
import java.util.List;


@Component
public class SpiderRouter extends RouteBuilder {
    @Autowired
    private ProducerTemplate producerTemplate;


    public void configure() {
        // Configurações do Camel e de padrões de formatos
        restConfiguration().component("servlet").bindingMode(RestBindingMode.json).dataFormatProperty("objectMapper", "#objectMapper").contextPath("/api").port(8080);

        // Rotas disponíveis do orquestrador
//        rest("/store")
//                .post()
//                .consumes("application/json")
//                .produces("application/json")
//                .type(StoreDTO.class)
//                .to("direct:create")
//                .get("/{id}")
//                .to("direct:findById")
//                .produces("application/json")
//                .delete("/{id}")
//                .to("direct:deleteById")
//


        ;

        from("rabbitmq:exchange.scraping"
                + "?queue=queue.scraping"
                + "&autoDelete=false"
                + "&durable=true")
                .routeId("SpiderConsumer")
//                .unmarshal("jsonDataFormat") // Use the offerDataFormat defined in JacksonConfig
                .process(exchange -> {

//                    Object body = exchange.getIn().getBody();
//                    System.out.println("Raw body class: " + body.getClass());
//                    System.out.println("Raw body content: " + body.toString());
                    ObjectMapper mapper = new ObjectMapper();
                    String body = exchange.getIn().getBody(String.class);
//                    System.out.println("Aqui" + body);
                    System.out.println("AQUI ----->" + body);
                    List<OfferDTO> offers = mapper.readValue(body, new TypeReference<List<OfferDTO>>() {});
                    System.out.println(offers.size() + " offers received");
                    offers.forEach(offer -> {
                        if(offer.description() == null || offer.description().isEmpty()){
                            System.out.println("Offer description is null or empty: " + offer);
                        } else if (offer.price()<=0) {
                            System.out.println("Price is null: ");
                        } else if (offer.url() == null || offer.url().isEmpty()) {
                            System.out.println("Url is empty: ");
                        }else  if (offer.dateTime() == null) {
                            System.out.println("DateTime is null: ");
                        }
                        else {

                            String json = null;
                            try {
                                json = mapper.writeValueAsString(offer);
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
//                            System.out.println(json);

                            producerTemplate.sendBody("rabbitmq:exchange.offer"
                                    + "?queue=queue.offer"
                                    + "&autoDelete=false"
                                    + "&durable=true", json);
                        }
                    });
                });
//                .to("http://localhost:8080/store?bridgeEndpoint=true")
//                .convertBodyTo(String.class)
//                .log("Mensagem crua recebida: ${body}")
//                .unmarshal("jsonDataFormat");



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


