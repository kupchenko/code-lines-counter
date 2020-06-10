package me.kupchenko.service;

import lombok.SneakyThrows;
import me.kupchenko.config.ExecutionProperties;
import me.kupchenko.dto.FileInfoDto;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class FileScannerService {
    private static final String PATTERN_MULTI_ROW_COMMENT = "/\\*([\\S\\s]+?)\\*/";
    public static final String PREFIX_ROW_COMMENT = "//";

    @SneakyThrows
    public void scan(ExecutionProperties programArgs) {
        Path location = Path.of(programArgs.getLocation());

        if (Files.notExists(location)) {
            System.out.println("Location not found");
        } else if (Files.isDirectory(location)) {
            long sum = Files.find(location, Integer.MAX_VALUE, (path, basicFileAttributes) -> Files.isRegularFile(path))
                    .map(this::countPathLines)
                    .peek(this::printFileInfo)
                    .mapToLong(FileInfoDto::getNumberOfLines)
                    .sum();
            System.out.println(location.getFileName().toString() + " : " + sum);
        } else {
            FileInfoDto fileInfoDto = countPathLines(location);
            printFileInfo(fileInfoDto);
        }
    }

    private void printFileInfo(FileInfoDto info) {
        System.out.println(info.getFileName() + " : " + info.getNumberOfLines());
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
