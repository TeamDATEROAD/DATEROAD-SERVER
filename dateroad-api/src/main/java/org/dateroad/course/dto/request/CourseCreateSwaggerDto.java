package org.dateroad.course.dto.request;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record CourseCreateSwaggerDto(
        CourseCreateReq course,
        List<TagCreateReq> tags,
        List<CoursePlaceGetReq> places,
        List<MultipartFile> images
) {}
