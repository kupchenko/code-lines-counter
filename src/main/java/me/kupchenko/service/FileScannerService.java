package me.kupchenko.service;

import lombok.SneakyThrows;
import me.kupchenko.config.ExecutionProperties;
import me.kupchenko.dto.FileInfoDto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileScannerService {
    private static final String PATTERN_MULTI_ROW_COMMENT = "/\\*([\\S\\s]+?)\\*/";
    public static final String PREFIX_ROW_COMMENT = "//";

    @SneakyThrows
    public void scan(ExecutionProperties programArgs) {
        Path location = Path.of(programArgs.getLocation());

        if (Files.notExists(location)) {
            System.out.println("Location not found");
        } else if (Files.isDirectory(location)) {
            List<FileInfoDto> fileInfoDtos = getFileInfoDtos(location);
            long sum = fileInfoDtos.stream()
                    .mapToLong(FileInfoDto::getNumberOfLines)
                    .sum();
            System.out.print(location.getFileName().toString() + " : " + sum + ": ");
            fileInfoDtos.forEach(this::printFileInfo);
            System.out.println();
        } else {
            FileInfoDto fileInfoDto = countPathLines(location);
            printFileInfo(fileInfoDto);
        }
    }

    List<FileInfoDto> getFileInfoDtos(Path location) throws IOException {
        return Files.find(location, Integer.MAX_VALUE, (path, basicFileAttributes) -> Files.isRegularFile(path))
                .map(this::countPathLines)
                .collect(Collectors.toList());
    }

    private void printFileInfo(FileInfoDto info) {
        System.out.print(info.getFileName() + " : " + info.getNumberOfLines() + "; ");
    }

    private FileInfoDto countPathLines(Path path) {
        FileInfoDto fileInfoDto = new FileInfoDto();

        fileInfoDto.setNumberOfLines(getCodeLineCount(path));
        fileInfoDto.setFileName(path.getFileName().toString());

        return fileInfoDto;
    }

    @SneakyThrows
    private long getCodeLineCount(Path path) {
        String fileContent = Files.readString(path);

        String[] noMultiRowCommentsFileContent = fileContent.replaceAll(PATTERN_MULTI_ROW_COMMENT, "")
                .split(System.lineSeparator());
        return Arrays.stream(noMultiRowCommentsFileContent)
                .map(String::trim)
                .filter(s -> !s.startsWith(PREFIX_ROW_COMMENT))
                .filter(trimmedLine -> !trimmedLine.isEmpty())
                .count();
    }

}
