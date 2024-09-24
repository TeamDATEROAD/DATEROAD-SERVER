package org.dateroad.date.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.dateroad.auth.argumentresolve.UserId;
import org.dateroad.date.dto.request.DateCreateReq;
import org.dateroad.date.dto.response.DateCreateRes;
import org.dateroad.date.dto.response.DateDetailRes;
import org.dateroad.date.dto.response.DatesGetRes;
import org.dateroad.date.dto.response.DateGetNearestRes;
import org.dateroad.date.service.DateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/dates")
@RestController
public class DateController implements DateApi{
    private final DateService dateService;

    @PostMapping
    public ResponseEntity<DateCreateRes> createDate(@UserId final Long userId,
                                                    @RequestBody @Valid final DateCreateReq dateCreateReq) {
        DateCreateRes dateCreateRes = dateService.createDate(userId, dateCreateReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(dateCreateRes);
    }

    @GetMapping
    public ResponseEntity<DatesGetRes> getDates(@UserId final Long userId,
                                                @RequestParam @Valid @Pattern(regexp = "^(PAST|FUTURE)$", message = "time은 PAST 또는 FUTURE만 허용됩니다.") final String time) {
        DatesGetRes datesGetRes = dateService.getDates(userId, time);
        return ResponseEntity.ok(datesGetRes);
    }

    @GetMapping("/{dateId}")
    public ResponseEntity<DateDetailRes> getDateDetail(@UserId final Long userId,
                                                       @PathVariable final Long dateId) {
        DateDetailRes dateDetailRes = dateService.getDateDetail(userId, dateId);
        return ResponseEntity.ok(dateDetailRes);
    }

    @DeleteMapping("/{dateId}")
    public ResponseEntity<Void> deleteDate(@UserId final Long userId,
                                           @PathVariable final Long dateId) {
        dateService.deleteDate(userId, dateId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/nearest")
    public ResponseEntity<DateGetNearestRes> getNearestDate(@UserId final Long userId) {
        DateGetNearestRes dateGetNearestRes = dateService.getNearestDate(userId);
        return ResponseEntity.ok(dateGetNearestRes);
    }
}
