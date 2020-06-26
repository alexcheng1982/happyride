package io.vividcode.happyride.addressservice.service;

import io.micrometer.core.annotation.Timed;
import io.vividcode.happyride.addressservice.dataaccess.AddressRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AddressService {

  @Autowired
  AddressRepository addressRepository;

  @Autowired
  AreaService areaService;

  @Timed(value = "happyride.address.search", percentiles = {0.5, 0.75, 0.9})
  public List<AddressView> search(final Long areaCode, final String query) {
    return this.addressRepository
        .findByAreaAreaCodeAndAddressLineContains(areaCode, query)
        .stream()
        .map(AddressView::fromAddress)
        .collect(Collectors.toList());
  }

  @Timed(value = "happyride.address.get", percentiles = {0.5, 0.75, 0.9})
  public Optional<AddressView> getAddress(final String addressId, final int level) {
    return this.addressRepository.findById(addressId)
        .map(address -> {
          final List<AreaView> areas = this.areaService
              .getAreaWithHierarchy(address.getArea(), level);
          return AddressView.fromAddress(address, areas);
        });
  }
}
