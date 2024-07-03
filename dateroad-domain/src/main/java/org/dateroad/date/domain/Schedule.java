package org.dateroad.date.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Schedule {

    private Date date;

    public Schedule(Date date) {
        this.date = date;
    }
}
