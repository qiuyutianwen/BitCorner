package com.bitcorner.bitCorner.repositories;

import com.bitcorner.bitCorner.models.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    Orders findById(long id);

    @Query("SELECT o FROM Orders o WHERE o.username = :username")
    List<Orders> findOrderByUsernameNamedParams(
            @Param("username") String username);

    @Query("SELECT o FROM Orders o WHERE o.type = :type AND o.status = :status")
    List<Orders> findOrdersByTypeAndStatus(
            @Param("type") String type, @Param("status") String status);

    @Query("SELECT o FROM Orders o WHERE o.username = :username AND o.status = :status")
    List<Orders> findOrderByUsernameAndStatus(
            @Param("username") String username,
            @Param("status") String status);

    @Query(value = "SELECT * FROM orders WHERE status <> :status AND type = :type ORDER BY start_date DESC LIMIT 1;", nativeQuery = true)
    Orders findLatestOrderByNotStatusAndType(
            @Param("status") String status,
            @Param("type") String type);

    boolean existsById(long id);
}