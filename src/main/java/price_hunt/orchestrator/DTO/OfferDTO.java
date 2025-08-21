package price_hunt.orchestrator.DTO;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record OfferDTO (
        String description,
        double price,
        String url,
        String storeUrl,

        String dateTime

) {
}
