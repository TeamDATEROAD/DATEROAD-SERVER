package org.dateroad.date.api;

import lombok.RequiredArgsConstructor;
import org.dateroad.auth.argumentresolve.UserId;
import org.dateroad.date.dto.request.DateCreateReq;
import org.dateroad.date.dto.response.DateGetNearestRes;
import org.dateroad.date.service.DateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/dates")
@RestController
public class DateController {
    private final DateService dateService;

    @PostMapping
    public ResponseEntity<Void> createDate(@UserId final Long userId,
                                           @RequestBody final DateCreateReq dateCreateReq) {
        dateService.createDate(userId, dateCreateReq);
        return ResponseEntity.status(HttpStatus.CREATED).build();
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
        return ResponseEntity
                .ok(dateGetNearestRes);
    }
}
