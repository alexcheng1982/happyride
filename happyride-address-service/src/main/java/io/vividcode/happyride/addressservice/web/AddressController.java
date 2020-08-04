package io.vividcode.happyride.addressservice.web;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import io.vividcode.happyride.addressservice.api.AddressVO;
import io.vividcode.happyride.addressservice.api.AreaVO;
import io.vividcode.happyride.addressservice.api.web.AddressBatchRequest;
import io.vividcode.happyride.addressservice.service.AddressService;
import io.vividcode.happyride.addressservice.service.AreaService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AddressController {

  @Autowired
  AddressService addressService;

  @Autowired
  AreaService areaService;

  @GetMapping("/search")
  public CollectionModel<EntityModel<AddressVO>> search(
      @RequestParam("areaCode") Long areaCode,
      @RequestParam("query") String query) {
    return CollectionModel
        .of(this.addressService.search(areaCode, query)
                .stream()
                .map(address -> EntityModel.of(address,
                    linkTo(
                        methodOn(AddressController.class)
                            .getAddress(address.getId(), 1))
                        .withSelfRel())
                ).collect(Collectors.toList()),
            linkTo(methodOn(AddressController.class).search(areaCode, query))
                .withSelfRel());
  }

  @GetMapping("/address/{addressId}")
  public ResponseEntity<EntityModel<AddressVO>> getAddress(
      @PathVariable("addressId") String addressId,
      @RequestParam(value = "areaLevel", required = false, defaultValue = "1") int areaLevel) {
    return this.addressService.getAddress(addressId, areaLevel)
        .map(address -> EntityModel.of(address,
            linkTo(methodOn(AddressController.class)
                .getAddress(addressId, areaLevel)).withSelfRel()))
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
