package price_hunt.orchestrator.DTO;

import price_hunt.orchestrator.enums.Category;

import java.util.List;

public record UserDTO (String name,
                       String platform,
                       String platformUserId,
                       List<Category> interests)
{
}
