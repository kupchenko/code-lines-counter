package me.kupchenko.service;

import me.kupchenko.config.ExecutionProperties;
import me.kupchenko.dto.FileInfoDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FileScannerServiceTest {

    public static final String SRC_MAIN_RESOURCES_TEST_FILES = "src/test/resources/test-files";
    private FileScannerService fileScannerService;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        fileScannerService = new FileScannerService();
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void scan_shouldReturnCorrectNumberOfLines() {
        ExecutionProperties executionProperties = prepareExecutionProperties();

        fileScannerService.scan(executionProperties);

        assertThat("test-files : 11: Hello.java : 6; Dave.java : 5; \n", is(outContent.toString()));
    }

    @Test
    public void getFileInfoDtos_shouldReturnCorrectNumberOfLines() throws IOException {
        List<FileInfoDto> fileInfoDtos = fileScannerService.getFileInfoDtos(Path.of(SRC_MAIN_RESOURCES_TEST_FILES));

        assertThat(fileInfoDtos.size(), is(2));
        assertThat(fileInfoDtos.get(0).getFileName(), is("Hello.java"));
        assertThat(fileInfoDtos.get(0).getNumberOfLines(), is(6L));
        assertThat(fileInfoDtos.get(1).getFileName(), is("Dave.java"));
        assertThat(fileInfoDtos.get(1).getNumberOfLines(), is(5L));
    }

    private ExecutionProperties prepareExecutionProperties() {
        ExecutionProperties executionProperties = new ExecutionProperties();
        executionProperties.setLocation(SRC_MAIN_RESOURCES_TEST_FILES);
        return executionProperties;
    }

}
