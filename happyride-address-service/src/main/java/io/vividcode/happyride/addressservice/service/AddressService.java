package io.vividcode.happyride.addressservice.service;

import io.micrometer.core.annotation.Timed;
import io.vividcode.happyride.addressservice.dataaccess.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressService {

  @Autowired
  AddressRepository addressRepository;

  @Timed(value = "happyride.address.search", percentiles = {0.5, 0.75, 0.9})
  public List<AddressView> search(final Long areaCode, final String query) {
    return this.addressRepository.findByAreaAreaCodeAndAddressLineContains(areaCode, query)
        .stream().map(AddressView::fromAddress).collect(Collectors.toList());
  }
}
