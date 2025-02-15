package com.sparta.levelup_backend.domain.bill.service;

import com.sparta.levelup_backend.domain.bill.entity.BillEntity;
import com.sparta.levelup_backend.domain.bill.repository.BillRepository;
import com.sparta.levelup_backend.domain.order.entity.OrderEntity;
import com.sparta.levelup_backend.domain.order.repository.OrderRepository;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.utill.BillStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService{

    private final BillRepository billRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    // 거래 내역 생성 (거래중으로 바뀌었을때 생성 됌)
    public void createBill(Long userId, OrderEntity order) {

        UserEntity user = userRepository.findByIdOrElseThrow(userId);

        OrderEntity findOrder = orderRepository.findByIdOrElseThrow(order.getId());

        BillEntity bill = BillEntity.builder()
                .seller(user)
                .buyer(findOrder.getUser())
                .order(findOrder)
                .billHistory(findOrder.getProduct().getProductName())
                .price(findOrder.getTotalPrice())
                .status(BillStatus.PAYCOMPLETED)
                .build();

        billRepository.save(bill);
    }
}
