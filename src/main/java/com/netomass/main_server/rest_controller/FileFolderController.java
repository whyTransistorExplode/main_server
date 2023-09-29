package com.netomass.main_server.rest_controller;

import com.netomass.main_server.entity.User;
import com.netomass.main_server.payload.ApiPayload;
import com.netomass.main_server.services.FileFolderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.fileupload2.core.FileUploadException;
import org.apache.commons.fileupload2.jakarta.JakartaServletFileUpload;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/share")
public class FileFolderController {
    private final FileFolderService fileFolderService;

    @GetMapping("/path")
    public ResponseEntity<ApiPayload> getPath( @RequestParam String path) {
        UsernamePasswordAuthenticationToken userToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) userToken.getPrincipal();
        ApiPayload apiPayload = fileFolderService.getTreeList(path, 0, userDetails);
        return ResponseEntity.ok(apiPayload);
    }

    @GetMapping("/allowed_paths")
    public ResponseEntity<ApiPayload> allowedPaths(){
        UsernamePasswordAuthenticationToken userToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) userToken.getPrincipal();
        return ResponseEntity.ok(fileFolderService.getAllowedPathList(userDetails));
    }

//    @GetMapping("/volume-list")
//    public ResponseEntity<ApiPayload> getListVolumes() {
////        ApiPayload apiPayload = theService.getAllowedPath();
////        return ResponseEntity.ok(apiPayload);
//    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String uploadFile(HttpServletRequest request) throws FileUploadException {

        if (!JakartaServletFileUpload.isMultipartContent(request))
            return "error";

        try {
            ApiPayload apiPayload = fileFolderService.fileUpload(request);
            return apiPayload.isSuccess()?"succeed!":"failed";
        } catch (IOException e) {
            e.printStackTrace();
            return "couldn't write! exception: " + e.getMessage();
        }
    }


    @RequestMapping(path = "/download", method = RequestMethod.GET)
    public ResponseEntity<Resource> download(@RequestParam String path) throws IOException {
        ApiPayload apiPayload = fileFolderService.serveFile(path);

        if (!apiPayload.isSuccess())
            return ResponseEntity.status(404).body(null);
        // ...

        final File file = (File) apiPayload.getContent();
        final InputStream inputStream = new FileInputStream(file);
        final InputStreamResource resource = new InputStreamResource(inputStream);

        final HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.LAST_MODIFIED, String.valueOf(file.lastModified()));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"");
        headers.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()));


        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
