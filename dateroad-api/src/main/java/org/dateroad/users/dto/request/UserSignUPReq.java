package org.dateroad.users.dto.request;

import org.dateroad.tag.domain.DateTagType;

import java.util.List;

public record UserSignUpReq(
        String name,
        List<DateTagType> tag,  //todo: 열람 데이트 코스 전체 조회 API 머지 후, TagEnum으로 변경해야됨
        String image,
        String platform
) {
}
