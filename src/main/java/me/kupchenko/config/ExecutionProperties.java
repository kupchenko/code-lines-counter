package me.kupchenko.config;

import com.beust.jcommander.Parameter;
import lombok.Data;

@Data
public class ExecutionProperties {
    @Parameter(names = {"-f", "-file", "-folder"}, description = "File or folder")
    private String location;
}
