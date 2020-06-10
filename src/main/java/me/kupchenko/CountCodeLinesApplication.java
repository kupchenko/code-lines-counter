package me.kupchenko;

import com.beust.jcommander.JCommander;
import me.kupchenko.config.ExecutionProperties;
import me.kupchenko.service.FileScannerService;

public class CountCodeLinesApplication {

    public static void main(String[] args) {
        ExecutionProperties programArgs = new ExecutionProperties();
        JCommander.newBuilder()
                .addObject(programArgs)
                .build()
                .parse(args);

        FileScannerService fileScannerService = new FileScannerService();
        fileScannerService.scan(programArgs);
    }
}