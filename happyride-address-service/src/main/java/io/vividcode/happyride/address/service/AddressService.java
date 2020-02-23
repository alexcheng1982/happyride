package io.vividcode.happyride.address.service;

import io.vividcode.happyride.address.dataaccess.AddressRepository;
import io.vividcode.happyride.address.domain.Address;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

  @Autowired
  AddressRepository addressRepository;

  public List<Address> search(Long areaCode, String query) {
    return addressRepository.findByAreaAreaCodeAndAddressLineContains(areaCode, query);
  }
}
