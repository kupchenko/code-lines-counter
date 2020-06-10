package me.kupchenko.dto;

import lombok.Data;

@Data
public class FileInfoDto {
    private String fileName;
    private long numberOfLines;
}
