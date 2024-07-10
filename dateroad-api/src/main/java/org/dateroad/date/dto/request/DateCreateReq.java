package org.dateroad.date.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record DateCreateReq(
        String title,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        LocalDate date,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm a", timezone = "Asia/Seoul")
        LocalTime startAt,
        List<TagCreateReq> tags,
        String country,
        String city,
        List<PlaceCreateReq> places
) {
}
