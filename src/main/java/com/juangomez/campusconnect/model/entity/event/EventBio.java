package com.juangomez.campusconnect.model.entity.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
@AllArgsConstructor
public class EventBio {

    private UUID id;

    private String description;

    private MultipartFile image;

}
