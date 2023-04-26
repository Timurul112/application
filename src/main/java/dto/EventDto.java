package dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class
EventDto {
     Integer userId;
     Integer fileId;
}
