package org.dateroad.date.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.dateroad.date.domain.Date;
import org.dateroad.date.domain.Region;
import org.dateroad.date.dto.request.DateCreateReq;
import org.dateroad.date.dto.request.PlaceCreateReq;
import org.dateroad.date.dto.request.TagCreateReq;
import org.dateroad.date.repository.DatePlaceRepository;
import org.dateroad.date.repository.DateTagRepository;
import org.dateroad.tag.domain.DateTagType;
import org.dateroad.user.domain.Platform;
import org.dateroad.user.domain.User;
import org.dateroad.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DateServiceTest {
    @Mock
    DateRepository dateRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private DateTagRepository tagRepository;
    @Mock
    private DatePlaceRepository placeRepository;
    @InjectMocks
    DateService dateService;

    @Test
    @DisplayName("잘 생성된다")
    void dateCreate() {
      // given
        User user = User.create("가든잉", "platformUserId123", Platform.KAKAO, "imageUrl");
        user.setId(1L);
        String title = "데이트 코스 제목";
        LocalDate date = LocalDate.of(2023, 7, 19);
        LocalTime startAt = LocalTime.of(10, 0); // 10:00 AM
        List<TagCreateReq> tags = List.of(new TagCreateReq(DateTagType.DRIVE), new TagCreateReq(DateTagType.SHOPPING));
        Region.MainRegion country = Region.MainRegion.GYEONGGI;
        Region.SubRegion city = Region.SubRegion.GWANAK_DONGJAK_GEUMCHEON;
        List<PlaceCreateReq> places = List.of(
                new PlaceCreateReq("Place1", 1.5f, 1),
                new PlaceCreateReq("Place2", 2.0f, 2));
        DateCreateReq dateCreateReq = new DateCreateReq(title, date, startAt, tags, country, city, places);
        Date createdDate = Date.create(user, title, date, startAt, country, city); // Date 객체 생성
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        when(dateRepository.save(any(Date.class))).thenReturn(createdDate);

        // when
        dateService.createDate(user.getId(), dateCreateReq);

        // then
        verify(userRepository).findById(user.getId());
        verify(dateRepository).save(any(Date.class));

        assertEquals(user, createdDate.getUser());
        assertEquals(dateCreateReq.title(), createdDate.getTitle());
        assertEquals(dateCreateReq.date(), createdDate.getDate());
        assertEquals(dateCreateReq.startAt(), createdDate.getStartAt());
        assertEquals(dateCreateReq.country(), createdDate.getCountry());
        assertEquals(dateCreateReq.city(), createdDate.getCity());
    }
}