package com.netomass.main_server.services;

import com.netomass.main_server.entity.Path;
import com.netomass.main_server.entity.User;
import com.netomass.main_server.payload.ApiPayload;
import com.netomass.main_server.payload.fileSystem.FileC;
import com.netomass.main_server.repository.PathRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileFolderService {
    private final PathRepository pathRepository;
    private final PathService pathService;

    public ApiPayload getTreeList(String path, int depth, User currentUser) {
        //todo: fix it, only admin can access c:\ and check who has privilege to access to which drive or folder they have access from database
        if (path.equals("/"))
            return getAllowedPathList(currentUser);

        if (!checkIfCurrentUserHasPrivilage(path, currentUser))
        return ApiPayload.builder().message("access denied").isSuccess(false).build();

        List<FileC> fileTreeList = new ArrayList<>();

        try {
            recursiveFileDigger(path, depth, fileTreeList);

        } catch (IOException e) {
            return ApiPayload.builder().content(fileTreeList).message("error").isSuccess(false).build();
        }

        return ApiPayload.builder().content(fileTreeList).message("success").build();
    }

    private void recursiveFileDigger(String dirPath, int depth, List<FileC> fileList) throws IOException {
        if (depth < 0) return;

        File requestPath = new File(dirPath);
        if (!requestPath.isDirectory()) throw new IOException();

        File[] files = requestPath.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (!file.exists()) return;


            final DosFileAttributes attr = Files.readAttributes(file.toPath(), DosFileAttributes.class);
            if (attr.isSystem()) continue;


            FileC filePath = FileC.builder()
                    .name(file.getName())
                    .path(mapPath(file.getPath()))
                    .isFile(file.isFile())
                    .updatedAt(fileUpdateTime(file))
                    .createdAt(fileCreationTime(file))
                    .build();

            fileList.add(filePath);

            if (file.isDirectory())
                recursiveFileDigger(file.getPath(), depth - 1, filePath.getListFiles());

        }
    }

    private long fileCreationTime(File file) throws IOException {
        FileTime fileTime = (FileTime) Files.getAttribute(Paths.get(file.getPath()), "creationTime");
        return fileTime.toMillis();
    }
    private long fileUpdateTime(File file){
        return file.lastModified();
    }
    private boolean checkIfCurrentUserHasPrivilage(String path, User user){
        return pathService.checkPath(path, user);
    }

    public ApiPayload getAllowedPathList(User user) {
        List<Path> allowedPaths = pathService.getAllowedPaths(user);
        List<FileC> pathList = new ArrayList<>();

        allowedPaths.forEach((onePath) -> {
            File file = new File(onePath.getPath());
            if (file.exists()) {
                try {
                    pathList.add(FileC.builder()
                            .isFile(file.isFile())
                            .name(file.getName())
                            .path(mapPath(file.getPath()))
                            .createdAt(fileCreationTime(file))
                            .updatedAt(fileUpdateTime(file))
                            .build());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

            return ApiPayload.builder().content(pathList).message("success").build();
    }

    private String mapPath(String path) {
        final String s = path.replaceAll("\\\\", "/");
        return s.replace("\\", "/");

    }
}
