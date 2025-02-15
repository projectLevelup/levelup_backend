package com.sparta.levelup_backend.domain.bill.repository;

import com.sparta.levelup_backend.domain.bill.entity.BillEntity;
import com.sparta.levelup_backend.exception.common.ErrorCode;
import com.sparta.levelup_backend.exception.common.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<BillEntity, Long>, BillRepositoryCustom{

    default BillEntity findByIdOrElseThrow(Long billId) {
        return findById(billId).orElseThrow(() -> new NotFoundException(ErrorCode.BILL_NOT_FOUND));
    }
}
