package io.vividcode.happyride.addressservice.grpc;

import io.grpc.stub.StreamObserver;
import io.vividcode.happyride.addressservice.api.AddressVO;
import io.vividcode.happyride.addressservice.api.AreaVO;
import io.vividcode.happyride.addressservice.grpc.AddressServiceGrpc.AddressServiceImplBase;
import io.vividcode.happyride.addressservice.service.AddressService;
import io.vividcode.happyride.addressservice.service.AreaService;
import java.util.stream.Collectors;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@GRpcService
public class AddressGrpcService extends AddressServiceImplBase {

  @Autowired
  AddressService addressService;

  @Autowired
  AreaService areaService;

  private static final Logger LOGGER = LoggerFactory
      .getLogger(AddressGrpcService.class);

  @Override
  public void getAddress(GetAddressRequest request,
      StreamObserver<GetAddressResponse> responseObserver) {
    GetAddressResponse.Builder builder = GetAddressResponse.newBuilder();
    this.addressService
        .getAddress(request.getAddressId(), request.getAreaLevel())
        .ifPresent(address -> builder.setAddress(this.buildAddress(address)));
    responseObserver.onNext(builder.build());
    responseObserver.onCompleted();
  }

  @Override
  public void getArea(GetAreaRequest request,
      StreamObserver<GetAreaResponse> responseObserver) {
    GetAreaResponse.Builder builder = GetAreaResponse.newBuilder();
    this.areaService.getArea(request.getAreaCode(), request.getAncestorLevel())
        .ifPresent(area -> builder.setArea(this.buildArea(area)));
    responseObserver.onNext(builder.build());
    responseObserver.onCompleted();
  }

  @Override
  public void search(AddressSearchRequest request,
      StreamObserver<Address> responseObserver) {
    this.addressService.search(request.getAreaCode(), request.getQuery())
        .forEach(
            address -> responseObserver.onNext(this.buildAddress(address)));
    responseObserver.onCompleted();
  }

  @Override
  public StreamObserver<GetAddressRequest> getAddresses(
      StreamObserver<Address> responseObserver) {
    return new StreamObserver<GetAddressRequest>() {
      @Override
      public void onNext(GetAddressRequest request) {
        AddressGrpcService.this.addressService
            .getAddress(request.getAddressId(), request.getAreaLevel())
            .ifPresent(
                address -> responseObserver.onNext(
                    AddressGrpcService.this.buildAddress(address)));
      }

      @Override
      public void onError(Throwable t) {
        AddressGrpcService.LOGGER.warn("Error", t);
      }

      @Override
      public void onCompleted() {
        responseObserver.onCompleted();
      }
    };
  }

  private Address buildAddress(AddressVO address) {
    return Address.newBuilder().setId(address.getId())
        .setAreaId(address.getAreaId())
        .setAddressLine(address.getAddressLine())
        .setLat(address.getLat().toPlainString())
        .setLng(address.getLng().toPlainString())
        .addAllAreas(address.getAreas().stream()
            .map(this::buildArea).collect(Collectors.toList()))
        .build();
  }

  private Area buildArea(AreaVO area) {
    return Area.newBuilder().setId(area.getId())
        .setLevel(area.getLevel())
        .setParentCode(area.getParentCode())
        .setAreaCode(area.getAreaCode())
        .setZipCode(area.getZipCode())
        .setCityCode(area.getCityCode())
        .setName(area.getName())
        .setShortName(area.getShortName())
        .setMergerName(area.getMergerName())
        .setPinyin(area.getPinyin())
        .setLat(area.getLat().toPlainString())
        .setLng(area.getLng().toPlainString())
        .build();
  }
}
