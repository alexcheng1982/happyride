package io.vividcode.happyride.addressservice.web;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import io.vividcode.happyride.addressservice.api.AddressVO;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

public class AddressModelAssembler extends
    RepresentationModelAssemblerSupport<AddressVO, AddressModel> {

  public AddressModelAssembler() {
    super(AddressController.class, AddressModel.class);
  }

  @Override
  public AddressModel toModel(AddressVO entity) {
    AddressModel model = new AddressModel();
    model.add(linkTo(
        methodOn(AddressController.class)
            .getAddress(entity.getId(), 1))
        .withSelfRel());
    model.id = entity.getId();
    model.areaId = entity.getAreaId();
    model.areaCode = entity.getAreaCode();
    model.addressLine = entity.getAddressLine();
    model.lat = entity.getLat();
    model.lng = entity.getLng();
    return model;
  }
}
