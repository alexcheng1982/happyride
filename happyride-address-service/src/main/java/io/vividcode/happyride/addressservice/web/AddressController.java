package io.vividcode.happyride.addressservice.web;

import io.vividcode.happyride.addressservice.service.AddressService;
import io.vividcode.happyride.addressservice.service.AddressView;
import io.vividcode.happyride.addressservice.service.AreaService;
import io.vividcode.happyride.addressservice.service.AreaView;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AddressController {

  @Autowired
  AddressService addressService;

  @Autowired
  AreaService areaService;

  @GetMapping("/search")
  public List<AddressView> search(@RequestParam(value = "areaCode") final Long areaCode,
      @RequestParam("query") final String query) {
    return this.addressService.search(areaCode, query);
  }

  @GetMapping("/address/{addressId}")
  public ResponseEntity<AddressView> getAddress(
      @PathVariable("addressId") final String addressId,
      @RequestParam(value = "areaLevel", required = false, defaultValue = "0") final int areaLevel) {
    return this.addressService.getAddress(addressId, areaLevel)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping("/area/{areaId}")
  public ResponseEntity<AreaView> getArea(
      @PathVariable("areaId") final Long areaId,
      @RequestParam(value = "ancestorLevel", required = false, defaultValue = "0") final int ancestorLevel) {
    return this.areaService.getArea(areaId, ancestorLevel)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
