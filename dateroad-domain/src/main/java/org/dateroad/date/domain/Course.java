package org.dateroad.date.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Course
{
    private Date date;

    public Course(Date date) {
        this.date = date;
    }
}
