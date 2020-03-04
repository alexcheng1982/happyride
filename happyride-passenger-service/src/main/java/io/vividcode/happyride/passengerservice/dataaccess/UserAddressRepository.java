package io.vividcode.happyride.passengerservice.dataaccess;

import io.vividcode.happyride.passengerservice.domain.UserAddress;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAddressRepository extends CrudRepository<UserAddress, String> {

}
