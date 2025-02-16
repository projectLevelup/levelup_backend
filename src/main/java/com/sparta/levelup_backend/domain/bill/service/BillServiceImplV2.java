package com.sparta.levelup_backend.domain.bill.service;

import com.sparta.levelup_backend.config.CustomUserDetails;
import com.sparta.levelup_backend.domain.bill.dto.responseDto.BillResponseDto;
import com.sparta.levelup_backend.domain.bill.entity.BillEntity;
import com.sparta.levelup_backend.domain.bill.repository.BillRepository;
import com.sparta.levelup_backend.domain.order.entity.OrderEntity;
import com.sparta.levelup_backend.domain.order.repository.OrderRepository;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.exception.common.DuplicateException;
import com.sparta.levelup_backend.exception.common.ErrorCode;
import com.sparta.levelup_backend.exception.common.ForbiddenException;
import com.sparta.levelup_backend.exception.common.NotFoundException;
import com.sparta.levelup_backend.utill.BillStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BillServiceImplV2 implements BillServiceV2 {

    private final BillRepository billRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    // 거래 내역 생성 (거래중으로 바뀌었을때 생성 됌)
    public void createBill(Long userId, OrderEntity order) {

        UserEntity user = userRepository.findByIdOrElseThrow(userId); // tutor 정보

        OrderEntity findOrder = orderRepository.findByIdOrElseThrow(userId);

        BillEntity bill = BillEntity.builder()
                .tutor(order.getProduct().getUser())
                .student(user)
                .order(findOrder)
                .billHistory(findOrder.getProduct().getProductName())
                .price(findOrder.getTotalPrice())
                .status(BillStatus.PAYCOMPLETED)
                .tutorIsDeleted(false)
                .studentIsDeleted(false)
                .build();

        billRepository.save(bill);
    }

    /**
     * 결제내역 페이징 조회 (tutor 전용)
     * @param userId 사용자
     * @param pageable 삭제되지않은 내역 페이징
     * @return billId, tutorName, tutorNumber, studentName, studentNumber, billHistory, price, status
     */
    @Override
    public Page<BillResponseDto> findBillsByTutor(Long userId, Pageable pageable) {

        Page<BillEntity> tutorBills = billRepository.findTutorBills(userId, pageable);

        return tutorBills.map(BillResponseDto::new);
    }

    /**
     * 결제내역 페이징 조회 (student 전용)
     * @param userId 사용자
     * @param pageable 삭제되지않은 내역 페이징
     * @return billId, tutorName, tutorNumber, studentName, studentNumber, billHistory, price, status, paymentDate
     */
    @Override
    public Page<BillResponseDto> findBillsByStudent(Long userId, Pageable pageable) {

        Page<BillEntity> studentBills = billRepository.findStudentBills(userId, pageable);

        return studentBills.map(BillResponseDto::new);
    }

    /**
     * 결제내역 단건 조회(tutor 전용)
     * @param userId 사용자
     * @param billId 결제내역Id
     * @return billId, tutorName, tutorNumber, studentName, studentNumber, billHistory, price, status, paymentDate
     */
    @Override
    public BillResponseDto findBillByTutor(Long userId, Long billId) {

        BillEntity bill = billRepository.findByIdWithTutorAndStudent(billId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BILL_NOT_FOUND));

        if (!bill.getTutor().getId().equals(userId)) {
            throw new NotFoundException(ErrorCode.BILL_NOT_FOUND);
        }

        if (bill.getTutorIsDeleted().equals(true)) {
            throw new DuplicateException(ErrorCode.DUPLICATE_DELETED_BILL);
        }

        return new BillResponseDto(bill);
    }

    /**
     * 결제내역 단건 조회(student 전용)
     * @param userId 사용자
     * @param billId 결제내역Id
     * @return billId, tutorName, tutorNumber, studentName, studentNumber, billHistory, price, status, paymentDate
     */
    @Override
    public BillResponseDto findBillByStudent(Long userId, Long billId) {

        BillEntity bill = billRepository.findByIdWithTutorAndStudent(billId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BILL_NOT_FOUND));

        if (!bill.getStudent().getId().equals(userId)) {
            throw new NotFoundException(ErrorCode.BILL_NOT_FOUND);
        }

        if (bill.getStudentIsDeleted().equals(true)) {
            throw new DuplicateException(ErrorCode.DUPLICATE_DELETED_BILL);
        }

        return new BillResponseDto(bill);
    }
}
