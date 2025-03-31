package org.dateroad.date.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dateroad.auth.argumentresolve.UserId;
import org.dateroad.date.dto.request.DateCreateReqV2;
import org.dateroad.date.dto.response.DateCreateRes;
import org.dateroad.date.dto.response.DateDetailResV2;
import org.dateroad.date.service.DateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v2/dates")
@RestController
public class DateControllerV2 implements DateApiV2{
    private final DateService dateService;

    @PostMapping
    public ResponseEntity<DateCreateRes> createDate(@UserId final Long userId,
                                                    @RequestBody @Valid final DateCreateReqV2 dateCreateReq) {
        DateCreateRes dateCreateRes = dateService.createDateV2(userId, dateCreateReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(dateCreateRes);
    }

    @GetMapping("/{dateId}")
    public ResponseEntity<DateDetailResV2> getDateDetail(@UserId final Long userId,
                                                         @PathVariable final Long dateId) {
        DateDetailResV2 dateDetailRes = dateService.getDateDetailV2(userId, dateId);
        return ResponseEntity.ok(dateDetailRes);
    }
}
