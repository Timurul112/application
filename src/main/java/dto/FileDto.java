package dto;


import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FileDto {
    String fileName;
    String filePath;
    Integer id;
    String status;
}
