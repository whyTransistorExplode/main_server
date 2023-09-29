package com.netomass.main_server.services;

import com.netomass.main_server.entity.Path;
import com.netomass.main_server.entity.User;
import com.netomass.main_server.payload.ApiPayload;
import com.netomass.main_server.payload.fileSystem.FileC;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.fileupload2.core.DiskFileItem;
import org.apache.commons.fileupload2.core.DiskFileItemFactory;
import org.apache.commons.fileupload2.core.FileItem;
import org.apache.commons.fileupload2.core.ProgressListener;
import org.apache.commons.fileupload2.jakarta.JakartaServletDiskFileUpload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;

//import org.apache.commons.fileupload.disk.DiskFileItem;
//import org.apache.commons.fileupload.disk.DiskFileItemFactory;

@Service
@RequiredArgsConstructor
public class FileFolderService {
//    private final PathRepository pathRepository;
    private final PathService pathService;

    @Value("${app.max-memory-upload-size}")
    private int maxMemoryUploadSize;

    @Value("${app.max-request-size}")
    private int maxRequestSize;

    @Value("${app.max-file-size}")
    private long maxFileSize;

    @Value(value = "${app.tempdir}")
    private String tempDir;

    private static DiskFileItemFactory factory;
    private static JakartaServletDiskFileUpload uploadHandler;
    private static ProgressListener progressListener;

    public ApiPayload getTreeList(String path, int depth, User currentUser) {
        //todo: fix it, only user can access c:\ and check who has privilege to access to which drive or folder they have access from database
        if (path.equals("/"))
            return getAllowedPathList(currentUser);

        if (!checkIfCurrentUserHasPrivilege(path, currentUser))
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


    private boolean checkIfCurrentUserHasPrivilege(String path, User user){
        return pathService.checkPath(path, user);
    }
    private boolean hasAccessToPath(String path){
        try {
            UsernamePasswordAuthenticationToken userToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            User userDetails = (User) userToken.getPrincipal();
            return checkIfCurrentUserHasPrivilege(path, userDetails);
        }catch (ClassCastException e){
            return false;
        }
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

    private DiskFileItemFactory getFactory(){
        if(factory == null) factory =  new DiskFileItemFactory.Builder()
                .setPath(tempDir)
                .setBufferSize(maxMemoryUploadSize)
                .get();
        return factory;
    }
    private JakartaServletDiskFileUpload getUploadHandler(){
        if (uploadHandler == null) {
            uploadHandler = new JakartaServletDiskFileUpload(getFactory());
            uploadHandler.setFileSizeMax(maxFileSize);
            uploadHandler.setProgressListener(getProgressListener());
        }
        return uploadHandler;
    }
    private ProgressListener getProgressListener(){
        if (progressListener == null) {
            progressListener  = new ProgressListener() {
                private long megaBytes = -1;
                @Override
                public void update(long bytesRead, long contentLength, int items) {
                    long mBytes = bytesRead / 1000000;
                    if (megaBytes == mBytes) {
                        return;
                    }
                    megaBytes = mBytes;
                    System.out.println("We are currently reading item " + items);
                    if (contentLength == -1) {
                        System.out.println("So far, " + bytesRead + " bytes have been read.");
                    } else {
                        System.out.println("So far, " + bytesRead + " of " + contentLength
                                + " bytes have been read.");
                    }
                }
            };
        }

        return progressListener;
    }

    public ApiPayload fileUpload(HttpServletRequest request) throws IOException {
        final String path = request.getHeader("path");
        final File pathDir = new File(path);
        if(!pathDir.isDirectory() || !hasAccessToPath(path))
            return ApiPayload.builder().isSuccess(false).build();

        List<DiskFileItem> items = getUploadHandler().parseRequest(request);

        for (FileItem item : items) {
            if (!item.isFormField()) {
                processUploadedFile(item, path);
            }
        }
        return ApiPayload.builder().build();
    }

    private void processUploadedFile(FileItem item, String path) throws IOException {

            item.write(Paths.get(path + "/"+ item.getName()));
    }



    public ApiPayload serveFile(String path) throws FileNotFoundException {
        if (!Files.exists(Paths.get(path)) || !hasAccessToPath(path))
            return ApiPayload.builder().isSuccess(false).build();

        return ApiPayload.builder().content(new File(path)).build();
    }


    private String mapPath(String path) {
        final String s = path.replaceAll("\\\\", "/");
        return s.replace("\\", "/");

    }
}
