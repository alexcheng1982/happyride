package io.vividcode.happyride.addressservice.service;

import com.google.common.collect.Streams;
import io.micrometer.core.annotation.Timed;
import io.vividcode.happyride.addressservice.api.AddressVO;
import io.vividcode.happyride.addressservice.api.AreaVO;
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
  public List<AddressVO> search(final Long areaCode, final String query) {
    return this.addressRepository
        .findByAreaAreaCodeAndAddressLineContains(areaCode, query)
        .stream()
        .map(AddressHelper::fromAddress)
        .collect(Collectors.toList());
  }

  @Timed(value = "happyride.address.get", percentiles = {0.5, 0.75, 0.9})
  public Optional<AddressVO> getAddress(final String addressId,
      final int level) {
    return this.addressRepository.findById(addressId)
        .map(address -> {
          final List<AreaVO> areas = this.areaService
              .getAreaWithHierarchy(address.getArea(), level);
          return AddressHelper.fromAddress(address, areas);
        });
  }

  public List<AddressVO> getAddresses(final List<String> addressIds) {
    return Streams.stream(this.addressRepository.findAllById(addressIds))
        .map(AddressHelper::fromAddress)
        .collect(Collectors.toList());
  }
}
