package org.dateroad.admin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseFilterReq {
    private Boolean latest;
    private Boolean popular;
}
