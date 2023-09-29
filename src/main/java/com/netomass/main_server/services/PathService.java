package com.netomass.main_server.services;

import com.netomass.main_server.entity.Path;
import com.netomass.main_server.entity.User;
import com.netomass.main_server.enums.Role;
import com.netomass.main_server.payload.ApiPayload;
import com.netomass.main_server.payload.PathPayload;
import com.netomass.main_server.payload.UserPayload;
import com.netomass.main_server.repository.PathRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PathService {
    private final PathRepository pathRepository;
    private final UserDetailsService userDetailsService;

    public ApiPayload addPath(PathPayload path, long theAdderUserId){
        ApiPayload privilegedUser= userDetailsService.getUserPayloadById(theAdderUserId);

        //if the user who is assigning this path to this other user, is user then proceed
        if (!((UserPayload)privilegedUser.getContent()).getRole().equals(Role.ADMIN.name()))
            return ApiPayload.builder().message("failed! You dont have privilege to do that").build();

        ApiPayload userById = userDetailsService.getUserById(path.getUserId());


        Path newPath = Path.builder()
                .user((User) userById.getContent())
                .path(path.getPath())
                .name(path.getName())
                .build();
        pathRepository.save(newPath);
        return ApiPayload.builder().message("success").build();
    }

    public boolean checkPath(String path, User user){
        if (path == null)
            return false;

        Optional<List<Path>> b = pathRepository.findAllByUserId(user.getId());
        if (b.isEmpty()) return false;

        for (Path path1 : b.get()) {
            if (path.toLowerCase().startsWith(path1.getPath().toLowerCase())) return true;
        }

        return false;
    }

    public List<Path> getAllowedPaths(User userDetails) {

        return pathRepository.findAllByUserId(userDetails.getId()).orElseThrow(() -> new ResourceNotFoundException("not found"));
    }
}
