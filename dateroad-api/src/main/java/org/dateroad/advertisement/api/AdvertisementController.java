package org.dateroad.advertisement.api;

import lombok.RequiredArgsConstructor;
import org.dateroad.advertisement.dto.response.AdvGetAllRes;
import org.dateroad.advertisement.dto.response.AdvGetDetailRes;
import org.dateroad.advertisement.service.AdvertisementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/advertisements")
public class AdvertisementController {
    private final AdvertisementService advertisementService;

    @GetMapping
    public ResponseEntity<AdvGetAllRes> getAllAdvertisements(){
        return ResponseEntity.ok(advertisementService.getAllAdvertisements());
    }

    @GetMapping("{advId}")
    public ResponseEntity<AdvGetDetailRes> getAllAdvertisements(
            final @PathVariable Long advId
    ){
        return ResponseEntity.ok(advertisementService.getAdvertisementsDetail(advId));
    }
}
