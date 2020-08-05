package io.vividcode.happyride.addressservice.web;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class AreaProcessor implements
    RepresentationModelProcessor<AddressModel> {

  @Override
  public AddressModel process(AddressModel model) {
    model.add(
        linkTo(methodOn(AddressController.class).getArea(model.areaCode, 1))
            .withRel("area"));
    return model;
  }
}
