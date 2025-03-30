package org.dateroad.date.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import org.dateroad.date.domain.Course;
import org.dateroad.date.domain.Region;
import org.dateroad.tag.domain.DateTagType;
import org.dateroad.user.domain.User;

@Builder(access = AccessLevel.PRIVATE)
public record CourseGetDetailResV2(
        Long courseId,
        List<ImagesList> images,
        int like,
        float totalTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        LocalDate date,
        Region.SubRegion city,
        String title,
        String description,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm a", timezone = "Asia/Seoul",locale = "en")
        LocalTime startAt,
        List<Places> places,
        int totalCost,
        List<CourseTag> tags,
        boolean isAccess,
        int free,
        int totalPoint,
        boolean isCourseMine,
        boolean isUserLiked
) {

    public static CourseGetDetailResV2 of(Course course,
                                        List<ImagesList> images,
                                        int like,
                                        List<Places> places,
                                        List<CourseTag> tags,
                                        boolean isAccess,
                                        User user,
                                        boolean isCourseMine,
                                        boolean isUserLiked) {
        return CourseGetDetailResV2.builder()
                .courseId(course.getId())
                .images(images)
                .like(like)
                .totalTime(course.getTime())
                .date(course.getDate())
                .city(course.getCity())
                .title(course.getTitle())
                .description(course.getDescription())
                .startAt(course.getStartAt())
                .places(places)
                .totalCost(course.getCost())
                .tags(tags)
                .isAccess(isAccess)
                .free(user.getFree())
                .totalPoint(user.getTotalPoint())
                .isCourseMine(isCourseMine)
                .isUserLiked(isUserLiked)
                .build();
    }

    @Builder(access = AccessLevel.PRIVATE)
    public record ImagesList(
            String imageUrl,
            int sequence
    ) {
        public static ImagesList of(final String imageUrl, final int sequence) {
            return ImagesList.builder()
                    .imageUrl(imageUrl)
                    .sequence(sequence)
                    .build();
        }
    }

    @Builder(access = AccessLevel.PRIVATE)
    public record Places(
            int sequence,
            String title,
            float duration,
            String address
    ) {
        public static Places of(final int sequence, final String title, final float duration, final String address) {
            return Places.builder()
                    .sequence(sequence)
                    .title(title)
                    .duration(duration)
                    .address(address)
                    .build();
        }
    }

    @Builder(access = AccessLevel.PRIVATE)
    public record CourseTag(
            DateTagType tag
    ) {
        public static CourseTag of(final DateTagType tag) {
            return CourseTag.builder()
                    .tag(tag)
                    .build();
        }
    }
}
