package com.bitcorner.bitCorner.repositories;

import com.bitcorner.bitCorner.models.OrderTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderTransactionRepository extends JpaRepository<OrderTransaction, Long> {
    OrderTransaction findById(long id);

    @Query(value = "SELECT * FROM order_transaction o ORDER BY o.transaction_date DESC LIMIT 1;", nativeQuery = true)
    OrderTransaction findLatestTransaction();

    List<OrderTransaction> findAll();
}
