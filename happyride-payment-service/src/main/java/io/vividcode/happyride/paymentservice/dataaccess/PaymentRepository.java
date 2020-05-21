package io.vividcode.happyride.paymentservice.dataaccess;

import io.vividcode.happyride.paymentservice.domain.Payment;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, String> {

  Optional<Payment> findByTripId(String tripId);
}
