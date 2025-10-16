package com.juangomez.campusconnect.model.entity.event;

import com.juangomez.campusconnect.model.enums.InterestTag;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
public class EventBio {

    private UUID id;

    private String description;

    private MultipartFile image;

    private Set<InterestTag> interestTags;

    public EventBio(String description, MultipartFile image, Set<InterestTag> interestTags) {
        this.description = description;
        this.image = image;
        this.interestTags = interestTags;
    }

}
