package com.sparta.levelup_backend.domain.bill.dto.responseDto;

import com.sparta.levelup_backend.domain.bill.entity.BillEntity;
import com.sparta.levelup_backend.utill.BillStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BillResponseDto {

    private final Long billId;

    private final String tutorName;

    private final String tutorNumber;

    private final String studentName;

    private final String studentNumber;

    private final String billHistory;

    private final Long price;

    private final BillStatus status;

    public BillResponseDto(BillEntity bill) {
        this.billId = bill.getId();
        this.tutorName = bill.getTutor().getNickName();
        this.tutorNumber = bill.getTutor().getPhoneNumber();
        this.studentName = bill.getStudent().getNickName();
        this.studentNumber = bill.getStudent().getPhoneNumber();
        this.billHistory = bill.getBillHistory();
        this.price = bill.getPrice();
        this.status = bill.getStatus();
    }

}
