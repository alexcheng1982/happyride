package io.vividcode.happyride.passengerwebapi;

import io.vividcode.happyride.addressservice.api.AddressVO;
import io.vividcode.happyride.passengerservice.api.web.PassengerVO;
import io.vividcode.happyride.passengerservice.api.web.UserAddressVO;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerResponse {

  private String id;

  private String name;

  private String email;

  private String mobilePhoneNumber;

  private List<UserAddressResponse> userAddresses;

  public static PassengerResponse fromPassengerAndAddresses(
      final PassengerVO passenger,
      final List<AddressVO> addresses) {
    final Map<String, AddressVO> addressVOMap = addresses.stream()
        .collect(Collectors.toMap(AddressVO::getId, Function.identity()));

    final List<UserAddressResponse> userAddresses = passenger
        .getUserAddresses().stream().map(userAddressVO ->
            ImmutablePair.of(userAddressVO,
                addressVOMap.get(userAddressVO.getAddressId())))
        .filter(pair -> Objects.nonNull(pair.getRight()))
        .map(pair -> {
          final UserAddressResponse response = new UserAddressResponse();
          final UserAddressVO userAddress = pair.getLeft();
          final AddressVO address = pair.getRight();
          response.setId(userAddress.getId());
          response.setName(userAddress.getName());
          response.setAddressId(address.getId());
          response.setAddressLine(address.getAddressLine());
          response.setLat(address.getLat());
          response.setLng(address.getLng());
          response.setAreaId(address.getAreaId());
          response.setAreas(address.getAreas());
          return response;
        })
        .collect(Collectors.toList());
    return new PassengerResponse(
        passenger.getId(),
        passenger.getName(),
        passenger.getEmail(),
        passenger.getMobilePhoneNumber(),
        userAddresses
    );
  }
}
