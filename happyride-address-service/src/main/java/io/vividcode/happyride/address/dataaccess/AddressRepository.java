package io.vividcode.happyride.address.dataaccess;

import io.vividcode.happyride.address.domain.Address;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends CrudRepository<Address, String> {
  List<Address> findByAreaAreaCodeAndAddressLineContains(Long areaCode, String query);
}
