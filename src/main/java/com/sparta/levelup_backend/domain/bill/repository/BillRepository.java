package com.sparta.levelup_backend.domain.bill.repository;

import com.sparta.levelup_backend.domain.bill.entity.BillEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<BillEntity, Long> {
}
