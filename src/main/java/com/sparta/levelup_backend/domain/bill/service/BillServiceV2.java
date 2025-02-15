package com.sparta.levelup_backend.domain.bill.service;

import com.sparta.levelup_backend.domain.bill.dto.responseDto.BillResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BillServiceV2 {
    Page<BillResponseDto> findBillByTutor(Long userId, Pageable pageable);

    Page<BillResponseDto> findBillByStudent(Long userId, Pageable pageable);
}
