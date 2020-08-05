package io.vividcode.happyride.addressservice.web;

import io.vividcode.happyride.addressservice.api.AddressVO;
import io.vividcode.happyride.addressservice.api.AreaVO;
import io.vividcode.happyride.addressservice.api.web.AddressBatchRequest;
import io.vividcode.happyride.addressservice.service.AddressService;
import io.vividcode.happyride.addressservice.service.AreaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AddressController {

  @Autowired
  AddressService addressService;

  @Autowired
  AreaService areaService;

  private final AddressModelAssembler assembler = new AddressModelAssembler();

  @GetMapping("/search")
  public CollectionModel<AddressModel> search(
      @RequestParam("areaCode") Long areaCode,
      @RequestParam("query") String query) {
    return this.assembler
        .toCollectionModel(this.addressService.search(areaCode, query));
  }

  @GetMapping("/address/{addressId}")
  public ResponseEntity<AddressModel> getAddress(
      @PathVariable("addressId") String addressId,
      @RequestParam(value = "areaLevel", required = false, defaultValue = "1") int areaLevel) {
    return this.addressService.getAddress(addressId, areaLevel)
        .map(this.assembler::toModel)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping("/addresses")
  public List<AddressVO> getAddresses(
      @RequestBody AddressBatchRequest addressBatchRequest) {
    return this.addressService
        .getAddresses(addressBatchRequest.getAddressIds());
  }

  @GetMapping("/area/{areaCode}")
  public ResponseEntity<AreaVO> getArea(
      @PathVariable("areaCode") Long areaCode,
      @RequestParam(value = "ancestorLevel", required = false, defaultValue = "0") int ancestorLevel) {
    return this.areaService.getArea(areaCode, ancestorLevel)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
