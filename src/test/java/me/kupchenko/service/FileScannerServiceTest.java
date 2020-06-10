package me.kupchenko.service;

import me.kupchenko.config.ExecutionProperties;
import org.junit.Before;
import org.junit.Test;

public class FileScannerServiceTest {

    private FileScannerService fileScannerService;

    @Before
    public void setUp() {
        fileScannerService = new FileScannerService();
    }

    @Test
    public void scan_shouldReturnCorrectNumberOfLines() {
        ExecutionProperties executionProperties = prepareExecutionProperties();
        fileScannerService.scan(executionProperties);
    }

    private ExecutionProperties prepareExecutionProperties() {
        ExecutionProperties executionProperties = new ExecutionProperties();
        executionProperties.setLocation("src/main/resources/test-files");
        return executionProperties;
    }

}
