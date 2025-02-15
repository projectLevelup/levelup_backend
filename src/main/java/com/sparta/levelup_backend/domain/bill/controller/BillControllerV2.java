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
    @GetMapping("/tutor")
    public ApiResponse<Page<BillResponseDto>> findBillsByTutor(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Long userId = authUser.getId();
        Page<BillResponseDto> billById = billService.findBillsByTutor(userId, pageable);
        return success(OK, BILL_FIND, billById);
    }

    // 결제내역 페이징 조회(student 전용)
    @GetMapping("/student")
    public ApiResponse<Page<BillResponseDto>> findBillsByStudent(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Long userId = authUser.getId();
        Page<BillResponseDto> billById = billService.findBillsByStudent(userId, pageable);
        return success(OK, BILL_FIND, billById);
    }

    // 결제내역 단건 조회(tutor 전용)
    @GetMapping("/tutor/{billId}")
    public ApiResponse<BillResponseDto> findBillByTutor(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PathVariable Long billId
    ) {
        Long userId = authUser.getId();
        BillResponseDto bill = billService.findBillByTutor(userId, billId);
        return success(OK, BILL_FIND, bill);
    }

    // 결제내역 단건 조회(student 전용)
    @GetMapping("/student/{billId}")
    public ApiResponse<BillResponseDto> findBillByStudent(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PathVariable Long billId
    ) {
        Long userId = authUser.getId();
        BillResponseDto bill = billService.findBillByStudent(userId, billId);
        return success(OK, BILL_FIND, bill);
    }
}