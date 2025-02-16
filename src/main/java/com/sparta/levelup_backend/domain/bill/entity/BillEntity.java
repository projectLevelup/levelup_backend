package com.sparta.levelup_backend.domain.bill.entity;

import com.sparta.levelup_backend.common.entity.BaseEntity;
import com.sparta.levelup_backend.domain.order.entity.OrderEntity;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.utill.BillStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "bills")
public class BillEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id", nullable = false)
    private UserEntity tutor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private UserEntity student;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @Column(name = "bill_history", nullable = false)
    private String billHistory;

    @Column(name = "price")
    private Long price;

    @Column(length = 15, nullable = false)
    @Enumerated(EnumType.STRING)
    private BillStatus status;

    @Column(name = "tutor_is_deleted")
    private Boolean tutorIsDeleted;

    @Column(name = "student_is_deleted")
    private Boolean studentIsDeleted;

    public void billDelete() {
        this.delete();
    }

    public void billTutorDelete() {
        this.tutorIsDeleted = true;
    }

    public void billStudentDelete() {
        this.studentIsDeleted = true;
    }

    public void cancelBill() {
        this.status = BillStatus.PAYCANCELED;
    }
}
