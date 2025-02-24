package com.sparta.levelup_backend.domain.bill.service;

import com.sparta.levelup_backend.domain.bill.dto.responseDto.BillCreatedEvent;
import com.sparta.levelup_backend.domain.bill.entity.BillEntity;
import com.sparta.levelup_backend.domain.bill.repository.BillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sparta.levelup_backend.utill.BillStatus.*;

@Service
@RequiredArgsConstructor
public class BillEventPubService {
    private final BillRepository billRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public BillEntity createPaidEvent(BillEntity bill) {
        bill.setStatus(PAID);
        BillEntity saveBill = billRepository.save(bill);

        eventPublisher.publishEvent(new BillCreatedEvent(saveBill));

        return saveBill;
    }

    @Transactional
    public BillEntity createCancelEvent(BillEntity bill) {
        bill.setStatus(PAYCANCELED);
        BillEntity saveBill = billRepository.save(bill);

        eventPublisher.publishEvent(new BillCreatedEvent(saveBill));

        return saveBill;
    }
}
