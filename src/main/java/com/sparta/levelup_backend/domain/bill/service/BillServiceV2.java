package com.sparta.levelup_backend.domain.bill.service;

import com.sparta.levelup_backend.domain.bill.dto.responseDto.BillResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BillServiceV2 {
    Page<BillResponseDto> findBill(Long userId, Long billId, Pageable pageable);
}
