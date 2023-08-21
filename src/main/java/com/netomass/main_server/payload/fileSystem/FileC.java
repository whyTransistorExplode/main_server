package com.netomass.main_server.payload.fileSystem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileC  {
    private String name;
    private String path;
    private boolean isFile;
    private List<FileC> listFiles;
    private Long createdAt;
    private Long updatedAt;


    public List<FileC> getListFiles() {
        if (this.listFiles == null) this.listFiles = new ArrayList<>();
        return this.listFiles;
    }
}
