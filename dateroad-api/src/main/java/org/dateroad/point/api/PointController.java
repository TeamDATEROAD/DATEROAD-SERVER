package org.dateroad.point.api;

import lombok.RequiredArgsConstructor;
import org.dateroad.auth.argumentresolve.UserId;
import org.dateroad.point.dto.response.PointGetAllRes;
import org.dateroad.point.service.PointService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/points")
@RequiredArgsConstructor
public class PointController implements PointApi {
    private final PointService pointService;

    @GetMapping
    public ResponseEntity<PointGetAllRes> getAllPoints(@UserId final Long userId){
        PointGetAllRes pointGetAllRes = pointService.getAllPoints(userId);
        return ResponseEntity.ok(pointGetAllRes);
    }
}
