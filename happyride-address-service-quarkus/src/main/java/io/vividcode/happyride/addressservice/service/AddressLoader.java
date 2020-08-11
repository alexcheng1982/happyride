package io.vividcode.happyride.addressservice.service;

import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.runtime.StartupEvent;
import io.vividcode.happyride.addressservice.model.Address;
import io.vividcode.happyride.addressservice.model.Area;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.transaction.Transactional;

@ApplicationScoped
@IfBuildProfile("dev")
public class AddressLoader {

  private final ThreadLocalRandom random = ThreadLocalRandom.current();

  @Transactional
  void onStart(@Observes StartupEvent event) {
    if (Address.count() > 0) {
      return;
    }
    List<Area> areas = Area.findAll().list();
    areas.forEach(area -> {
          int num = this.random.nextInt(2, 5);
          for (int i = 0; i < num; i++) {
            Address address = this.createInArea(area, i);
            address.persist();
          }
        }
    );
  }

  private Address createInArea(Area area, int seq) {
    Address address = new Address();
    address.id = UUID.randomUUID().toString();
    address.area = area;
    address.addressLine = area.name + "-" + seq;
    address.lat = this.adjustValue(area.lat);
    address.lng = this.adjustValue(area.lng);
    return address;
  }

  private BigDecimal adjustValue(BigDecimal location) {
    return location
        .add(BigDecimal.valueOf(this.random.nextInt(-100, 100) * 0.000001));
  }
}
