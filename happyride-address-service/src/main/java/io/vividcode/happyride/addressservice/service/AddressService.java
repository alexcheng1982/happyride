package io.vividcode.happyride.addressservice.service;

import io.vividcode.happyride.addressservice.dataaccess.AddressRepository;
import io.vividcode.happyride.addressservice.domain.Address;
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
