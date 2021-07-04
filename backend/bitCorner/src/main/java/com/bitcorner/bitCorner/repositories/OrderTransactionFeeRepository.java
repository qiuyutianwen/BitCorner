package com.bitcorner.bitCorner.repositories;

import com.bitcorner.bitCorner.models.OrderTransactionFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderTransactionFeeRepository extends JpaRepository<OrderTransactionFee, Long> {
    OrderTransactionFee findById(long id);
}