package com.sparta.levelup_backend.domain.bill.repository;

import com.sparta.levelup_backend.domain.bill.entity.BillEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BillRepositoryCustom {
    Page<BillEntity> findTutorBills(Long tutorId, Pageable pageable);
}
