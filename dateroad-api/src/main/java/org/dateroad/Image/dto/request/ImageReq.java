package org.dateroad.Image.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

@Builder(access = AccessLevel.PRIVATE)
public record ImageReq(
        MultipartFile image,
        int sequence
) {
    public static ImageReq of(MultipartFile image, int sequence) {
        return ImageReq.builder()
                .image(image)
                .sequence(sequence)
                .build();
    }
}
