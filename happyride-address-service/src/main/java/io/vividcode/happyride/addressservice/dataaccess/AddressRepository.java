package io.vividcode.happyride.addressservice.dataaccess;

import io.vividcode.happyride.addressservice.domain.Address;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends CrudRepository<Address, String> {

  List<Address> findByAreaAreaCodeAndAddressLineContains(Long areaCode, String query);
}
