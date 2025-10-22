package com.mbproyect.campusconnect.dto.event.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mbproyect.campusconnect.model.entity.event.EventLocation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

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
    private EventLocation location;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

}