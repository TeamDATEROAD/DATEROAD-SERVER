package org.dateroad.date.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Region {

    public enum MainRegion {
        SEOUL,
        GYEONGGI,
        INCHEON
    }

    @Getter
    public enum SubRegion {
        // 서울 소분류
        SEOUL_ENTIRE("서울 전체", MainRegion.SEOUL),
        GANGNAM_SEOCHO("강남/서초", MainRegion.SEOUL),
        JAMSIL_SONGPA_GANGDONG("잠실/송파/강동", MainRegion.SEOUL),
        KONDAE_SUNGSOO_WANGSIMNI("건대/성수/왕십리", MainRegion.SEOUL),
        JONGNO_JUNGRO("종로/중구", MainRegion.SEOUL),
        HONGDAE_HAPJEONG_MAPO("홍대/합정/마포", MainRegion.SEOUL),
        YEONGDEUNGPO_YEOUIDO("영등포/여의도", MainRegion.SEOUL),
        YONGSAN_ITAEWON_HANNAM("용산/이태원/한남", MainRegion.SEOUL),
        YANGCHEON_GANGSEO("양천/강서", MainRegion.SEOUL),
        SEONGBUK_NOWON_JUNGBANG("성북/노원/중랑", MainRegion.SEOUL),
        GURO_GWANAK_DONGJAK("구로/관악/동작", MainRegion.SEOUL),

        // 경기 소분류
        GYEONGGI_ENTIRE("경기 전체", MainRegion.GYEONGGI),
        SEONGNAM("성남", MainRegion.GYEONGGI),
        SUWON("수원", MainRegion.GYEONGGI),
        GOYANG_PAJU("고양/파주", MainRegion.GYEONGGI),
        GIMPO("김포", MainRegion.GYEONGGI),
        YONGIN_HWASEONG("용인/화성", MainRegion.GYEONGGI),
        ANYANG_GWACHEON("안양/과천", MainRegion.GYEONGGI),
        POCHEON_YANGJU("포천/양주", MainRegion.GYEONGGI),
        NAMYANGJU_UIJEONGBU("남양주/의정부", MainRegion.GYEONGGI),
        GWANGJU_ICHEON_YEOJU("광주/이천/여주", MainRegion.GYEONGGI),
        GAPYEONG_YANGPYEONG("가평/양평", MainRegion.GYEONGGI),
        GUNPO_UIWANG("군포/의왕", MainRegion.GYEONGGI),
        HANAM_GURI("하남/구리", MainRegion.GYEONGGI),
        SIHEUNG_GWANGMYEONG("시흥/광명", MainRegion.GYEONGGI),
        BUCHEON_ANSHAN("부천/안산", MainRegion.GYEONGGI),
        DONGDUCHEON_YEONCHEON("동두천/연천", MainRegion.GYEONGGI),
        PYEONGTAEK_OSAN_ANSEONG("평택/오산/안성", MainRegion.GYEONGGI),

        // 인천 소분류
        INCHEON_ENTIRE("인천 전체", MainRegion.INCHEON);

        private final String displayName;
        private final MainRegion mainRegion;

        SubRegion(String displayName, MainRegion mainRegion) {
            this.displayName = displayName;
            this.mainRegion = mainRegion;
        }
    }
}
