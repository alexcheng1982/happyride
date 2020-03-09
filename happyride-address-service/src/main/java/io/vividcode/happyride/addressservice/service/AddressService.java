package io.vividcode.happyride.addressservice.service;

import io.vividcode.happyride.addressservice.dataaccess.AddressRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

  @Autowired
  AddressRepository addressRepository;

  public List<AddressView> search(Long areaCode, String query) {
    return addressRepository.findByAreaAreaCodeAndAddressLineContains(areaCode, query)
        .stream().map(AddressView::fromAddress).collect(Collectors.toList());
  }
}
