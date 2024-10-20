package org.dateroad.point.event;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.dateroad.course.dto.request.PointUseReq;

@Getter
@AllArgsConstructor
public class MessageDto {
    @Builder
    public static class PointMessageDTO {
        private final String userId;
        private final String point;
        private final String type;
        private final String description;

        public static PointMessageDTO of(Long userId, PointUseReq pointUseReq) {
            return PointMessageDTO.builder()
                    .userId(userId.toString())
                    .point(String.valueOf(pointUseReq.getPoint()))
                    .description(pointUseReq.getDescription())
                    .type(pointUseReq.getType().name()).build();
        }

        public Map<String, String> toMap() {
            Map<String, String> fieldMap = new HashMap<>();
            fieldMap.put("userId", userId);
            fieldMap.put("point", point);
            fieldMap.put("type", type);
            fieldMap.put("description", description);
            return fieldMap;
        }
    }

    @Builder
    @AllArgsConstructor
    public static class FreeMessageDTO {
        private final String userId;
        public static FreeMessageDTO of(Long userId) {
            return FreeMessageDTO.builder().userId(userId.toString()).build();
        }

        public Map<String, String> toMap() {
            Map<String, String> fieldMap = new HashMap<>();
            fieldMap.put("userId", userId);
            return fieldMap;
        }
    }
}
