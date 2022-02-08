package io.vividcode.happyride.addressservice.service;

import io.vividcode.happyride.addressservice.dataaccess.AddressRepository;
import io.vividcode.happyride.addressservice.dataaccess.AreaRepository;
import io.vividcode.happyride.addressservice.domain.Address;
import io.vividcode.happyride.addressservice.domain.Area;
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

  private final ThreadLocalRandom random = ThreadLocalRandom.current();

  @EventListener
  public void onApplicationStarted(final ApplicationStartedEvent event) {
    this.loadAddresses();
  }

  @Transactional
  public void loadAddresses() {
    if (this.addressRepository.count() > 0) {
      return;
    }
    this.areaRepository.findAll().forEach(area -> {
      final int num = this.random.nextInt(2, 5);
      for (int i = 0; i < num; i++) {
        final Address address = this.createInArea(area, i);
        this.addressRepository.save(address);
      }
    });
  }

  private Address createInArea(final Area area, final int seq) {
    final BigDecimal lat = this.adjustValue(area.getLat());
    final BigDecimal lng = this.adjustValue(area.getLng());
    final String addressLine = area.getName() + "-" + seq;
    final Address address = new Address();
    address.setArea(area);
    address.setAddressLine(addressLine);
    address.setLat(lat);
    address.setLng(lng);
    return address;
  }

  private BigDecimal adjustValue(final BigDecimal location) {
    return location
        .add(BigDecimal.valueOf(this.random.nextInt(-100, 100) * 0.000001));
  }
}
