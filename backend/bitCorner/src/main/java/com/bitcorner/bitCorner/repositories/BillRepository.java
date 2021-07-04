package com.bitcorner.bitCorner.repositories;

import com.bitcorner.bitCorner.models.Bill;
import com.bitcorner.bitCorner.models.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    Bill findById(long id);

    @Query("SELECT o FROM Bill o WHERE o.sender_id = :userid OR o.receiver_id = :userid")
    List<Bill> findBillBySenders(
            @Param("userid") long userid);
}
