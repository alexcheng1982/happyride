package io.vividcode.happyride.addressservice.service.service;

import io.vividcode.happyride.addressservice.service.dataaccess.AddressRepository;
import io.vividcode.happyride.addressservice.service.dataaccess.AreaRepository;
import io.vividcode.happyride.addressservice.service.domain.Address;
import io.vividcode.happyride.addressservice.service.domain.Area;
import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AddressLoader {

  @Autowired
  AreaRepository areaRepository;

  @Autowired
  AddressRepository addressRepository;

  private ThreadLocalRandom random = ThreadLocalRandom.current();

  @EventListener
  public void onApplicationStarted(ApplicationStartedEvent event) {
    loadAddresses();
  }

  @Transactional
  public void loadAddresses() {
    addressRepository.deleteAll();
    areaRepository.findAll().forEach(area -> {
      int num = random.nextInt(1, 5);
      for (int i = 0; i < num; i++) {
        Address address = createInArea(area, i);
        addressRepository.save(address);
      }
    });
  }

  private Address createInArea(Area area, int seq) {
    BigDecimal lat = adjustValue(area.getLat());
    BigDecimal lng = adjustValue(area.getLng());
    String addressLine = area.getName() + "-" + seq;
    Address address = new Address();
    address.setArea(area);
    address.setAddressLine(addressLine);
    address.setLat(lat);
    address.setLng(lng);
    return address;
  }

  private BigDecimal adjustValue(BigDecimal location) {
    return location.add(BigDecimal.valueOf(random.nextInt(-100, 100) * 0.000001));
  }
}
