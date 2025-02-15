package com.sparta.levelup_backend.domain.bill.controller;

import com.sparta.levelup_backend.common.ApiResponse;
import com.sparta.levelup_backend.config.CustomUserDetails;
import com.sparta.levelup_backend.domain.bill.dto.responseDto.BillResponseDto;
import com.sparta.levelup_backend.domain.bill.service.BillServiceImplV2;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.sparta.levelup_backend.common.ApiResMessage.*;
import static com.sparta.levelup_backend.common.ApiResponse.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/v2/bills")
@RequiredArgsConstructor
public class BillControllerV2 {

    private final BillServiceImplV2 billService;

    // 결제내역 페이징 조회(tutor 전용)
    @GetMapping("/tutor/{billId}")
    public ApiResponse<Page<BillResponseDto>> findBillByTutor(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Long userId = authUser.getId();
        Page<BillResponseDto> billById = billService.findBillByTutor(userId, pageable);
        return success(OK, BILL_FIND, billById);
    }

    // 결제내역 페이징 조회(student 전용)
    @GetMapping("/student/{billId}")
    public ApiResponse<Page<BillResponseDto>> findBillByStudent(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Long userId = authUser.getId();
        Page<BillResponseDto> billById = billService.findBillByStudent(userId, pageable);
        return success(OK, BILL_FIND, billById);
    }
}
