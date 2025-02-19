package com.sparta.levelup_backend.domain.bill.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.levelup_backend.domain.bill.entity.BillEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.sparta.levelup_backend.domain.bill.entity.QBillEntity.billEntity;

@Repository
@RequiredArgsConstructor
public class BillRepositoryImpl implements BillRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // 결제내역 조회 (tutor 전용)
    @Override

    public Page<BillEntity> findTutorBills(Long tutorId, Pageable pageable) {
        List<BillEntity> results = queryFactory
                .selectFrom(billEntity)
                .where(
                        billEntity.tutor.id.eq(tutorId),
                        billEntity.tutorIsDeleted.eq(false)
                )
                .orderBy(getSortedOrder())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = Optional.ofNullable(
                        queryFactory
                                .select(billEntity.count())
                                .from(billEntity)
                                .where(
                                        billEntity.tutor.id.eq(tutorId),
                                        billEntity.tutorIsDeleted.eq(false)
                                )
                                .fetchOne())
                .orElse(0L);
        return new PageImpl<>(results, pageable, totalCount);
    }

    // 결제내역 조회 (student 전용)
    @Override
    public Page<BillEntity> findStudentBills(Long studentId, Pageable pageable) {
        List<BillEntity> results = queryFactory
                .selectFrom(billEntity)
                .where(
                        billEntity.student.id.eq(studentId),
                        billEntity.studentIsDeleted.eq(false)
                )
                .orderBy(getSortedOrder())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> totalCount = queryFactory
                        .select(billEntity.count())
                        .from(billEntity)
                        .where(
                                billEntity.student.id.eq(studentId),
                                billEntity.studentIsDeleted.eq(false)
                        );
        return PageableExecutionUtils.getPage(results, pageable, totalCount::fetchOne);
    }

    private OrderSpecifier<?> getSortedOrder() {
        return billEntity.createdAt.desc();
    }
}
