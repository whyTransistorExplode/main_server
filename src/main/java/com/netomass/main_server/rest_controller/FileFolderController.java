package com.netomass.main_server.rest_controller;

import com.netomass.main_server.entity.User;
import com.netomass.main_server.payload.ApiPayload;
import com.netomass.main_server.payload.StringWrapper;
import com.netomass.main_server.payload.UserPayload;
import com.netomass.main_server.services.FileFolderService;
import com.netomass.main_server.services.PathService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/share")
public class FileFolderController {
    private final FileFolderService theService;
    private final FileFolderService fileFolderService;

    @GetMapping("/path")
    public ResponseEntity<ApiPayload> getPath( @RequestParam String path) throws IOException {
        UsernamePasswordAuthenticationToken userToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) userToken.getPrincipal();
        ApiPayload apiPayload = theService.getTreeList(path, 0, userDetails);
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


}
