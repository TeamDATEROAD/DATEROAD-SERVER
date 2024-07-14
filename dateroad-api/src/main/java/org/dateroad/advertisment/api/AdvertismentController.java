package org.dateroad.advertisment.api;

import lombok.RequiredArgsConstructor;
import org.dateroad.advertisment.dto.response.AdvGetAllRes;
import org.dateroad.advertisment.dto.response.AdvGetDetailRes;
import org.dateroad.advertisment.service.AdvertismentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/advertisments")
public class AdvertismentController implements AdvertismentApi {
    private final AdvertismentService advertismentService;

    @GetMapping
    public ResponseEntity<AdvGetAllRes> getAllAdvertisments(){
        return ResponseEntity.ok(advertismentService.getAllAdvertisments());
    }

    @GetMapping("{advId}")
    public ResponseEntity<AdvGetDetailRes> getAdvertismentsDetail(
            final @PathVariable Long advId
    ){
        return ResponseEntity.ok(advertismentService.getAdvertismentsDetail(advId));
    }
}
