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

    @GetMapping("/tutor/{billId}")
    public ApiResponse<Page<BillResponseDto>> findBill(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PathVariable Long billId,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Long userId = authUser.getId();
        Page<BillResponseDto> billById = billService.findBill(userId, billId, pageable);
        return success(OK, BILL_FIND, billById);
    }
}
