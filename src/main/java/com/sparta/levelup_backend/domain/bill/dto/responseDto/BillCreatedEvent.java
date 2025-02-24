package com.sparta.levelup_backend.domain.bill.dto.responseDto;

import com.sparta.levelup_backend.domain.bill.entity.BillEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BillCreatedEvent {
    private final BillEntity bill;
}
