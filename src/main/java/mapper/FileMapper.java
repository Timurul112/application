package mapper;

import dto.FileDto;
import entity.File;

public class FileMapper implements Mapper<File, FileDto> {

    public static FileMapper INSTANCE = new FileMapper();

    private FileMapper() {
    }

    @Override
    public FileDto mapToDto(File entity) {
        return FileDto.builder()
                .id(entity.getId())
                .fileName(entity.getName())
                .filePath(entity.getFilePath())
                .status(entity.getStatus())
                .build();
    }

    @Override
    public File mapToEntity(FileDto dto) {
        return File.builder()
                .name(dto.getFileName())
                .filePath(dto.getFilePath())
                .id(dto.getId())
                .build();
    }

    public static FileMapper getInstance() {
        return INSTANCE;
    }
}
