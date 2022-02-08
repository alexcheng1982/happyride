package io.vividcode.happyride.addressservice.service;

import io.vividcode.happyride.addressservice.api.AddressVO;
import io.vividcode.happyride.addressservice.api.AreaVO;
import io.vividcode.happyride.addressservice.model.Address;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional
public class AddressService {

  @Inject
  AreaService areaService;

  public List<AddressVO> search(Long areaCode, String query) {
    return Address.findByAreaCodeAndAddressLine(areaCode, query)
        .stream()
        .map(AddressHelper::fromAddress)
        .collect(Collectors.toList());
  }

  public List<AddressVO> getAddresses(List<String> addressIds) {
    return addressIds.stream()
        .<Optional<Address>>map(Address::findByIdOptional)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(AddressHelper::fromAddress)
        .collect(Collectors.toList());
  }

  public Optional<AddressVO> getAddress(String addressId,
      int level) {
    return Address.<Address>findByIdOptional(addressId)
        .map(address -> {
          List<AreaVO> areas = this.areaService
              .getAreaWithHierarchy(address.area, level);
          return AddressHelper.fromAddress(address, areas);
        });
  }
}
