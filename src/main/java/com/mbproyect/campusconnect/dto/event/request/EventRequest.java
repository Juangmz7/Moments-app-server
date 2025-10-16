package com.mbproyect.campusconnect.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mbproyect.campusconnect.model.entity.event.EventLocation;
import com.mbproyect.campusconnect.model.entity.event.EventOrganiser;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest {

    @NotBlank
    @Size(min = 3, max = 50)  // Min & max length
    private String name;

    @NotNull
    private EventBioRequest eventBio;

    @NotNull
    private EventOrganiser organiser;

    @NotNull
    private EventLocation location;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime starDate;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDate;

}