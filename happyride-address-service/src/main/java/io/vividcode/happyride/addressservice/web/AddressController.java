package io.vividcode.happyride.addressservice.web;

import io.vividcode.happyride.addressservice.service.AddressService;
import io.vividcode.happyride.addressservice.service.AddressView;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AddressController {

  @Autowired
  AddressService addressService;

  @GetMapping("/search")
  public List<AddressView> search(@RequestParam(value = "areaCode") Long areaCode,
      @RequestParam("query") String query) {
    return addressService.search(areaCode, query);
  }
}
