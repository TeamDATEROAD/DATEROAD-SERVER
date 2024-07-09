package org.dateroad.advertisment.api;

import lombok.RequiredArgsConstructor;
import org.dateroad.advertisment.dto.response.AdvGetAllRes;
import org.dateroad.advertisment.service.AdvertismentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/advertisments")
public class AdvertismentController {
    private final AdvertismentService advertismentService;

    @GetMapping
    public ResponseEntity<AdvGetAllRes> getAllAdvertisments(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(advertismentService.getAllAdvertisments());
    }
}
