package com.sparta.levelup_backend.domain.bill.service;

import com.sparta.levelup_backend.domain.bill.dto.responseDto.BillResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BillServiceV2 {
    Page<BillResponseDto> findBillsByTutor(Long userId, Pageable pageable);

    Page<BillResponseDto> findBillsByStudent(Long userId, Pageable pageable);

    BillResponseDto findBillByTutor(Long userId, Long billId);

    BillResponseDto findBillByStudent(Long userId, Long billId);
}
