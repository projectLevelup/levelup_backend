package com.sparta.levelup_backend.domain.bill.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.levelup_backend.enums.BillStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PubBillDto {
    private Long billId;
    private BillStatus status;
    private Long tutorId;
    private Long studentId;
}
