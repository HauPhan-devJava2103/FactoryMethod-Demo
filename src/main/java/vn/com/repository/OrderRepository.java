package vn.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import vn.com.model.Order;
import vn.com.utils.EPaymentStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.paymentStatus = :status WHERE o.id = :id")
    void updatePaymentStatus(@Param("id") Long id, @Param("status") EPaymentStatus status);

}
