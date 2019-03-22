package com.assignment.healthcheck.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    private Integer total_websites = 0;
    private Integer success = 0;
    private Integer failure = 0;
    private Long total_time = Long.MIN_VALUE;

    public Integer increaseTotalWebsites() {
        return total_websites++;
    }

    public Integer increaseSuccess() {
        return success++;
    }

    public Integer increaseFailure() {
        return failure++;
    }

}
