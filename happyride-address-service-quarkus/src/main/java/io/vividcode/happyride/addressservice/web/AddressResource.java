package io.vividcode.happyride.addressservice.web;

import io.vividcode.happyride.addressservice.api.AddressVO;
import io.vividcode.happyride.addressservice.api.web.AddressBatchRequest;
import io.vividcode.happyride.addressservice.service.AddressService;
import io.vividcode.happyride.addressservice.service.AreaService;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/")
public class AddressResource {

  @Inject
  AddressService addressService;

  @Inject
  AreaService areaService;

  @Path("/search")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<AddressVO> search(
      @QueryParam("areaCode") Long areaCode,
      @QueryParam("query") String query) {
    return this.addressService.search(areaCode, query);
  }

  @Path("/address/{addressId}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAddress(
      @PathParam("addressId") String addressId,
      @QueryParam("areaLevel") @DefaultValue("1") int areaLevel) {
    return this.addressService.getAddress(addressId, areaLevel)
        .map(Response::ok)
        .orElseGet(() -> Response.status(Status.NOT_FOUND))
        .build();
  }

  @Path("/addresses")
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public List<AddressVO> getAddresses(AddressBatchRequest addressBatchRequest) {
    return this.addressService.getAddresses(addressBatchRequest.getAddressIds());
  }

  @Path("/area/{areaCode}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getArea(
      @PathParam("areaCode") Long areaCode,
      @QueryParam("ancestorLevel") @DefaultValue("0") int ancestorLevel) {
    return this.areaService.getArea(areaCode, ancestorLevel)
        .map(Response::ok)
        .orElseGet(() -> Response.status(Status.NOT_FOUND))
        .build();
  }
}
