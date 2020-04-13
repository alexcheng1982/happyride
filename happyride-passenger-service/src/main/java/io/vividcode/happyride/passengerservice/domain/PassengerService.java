package io.vividcode.happyride.passengerservice.domain;

import static io.vividcode.happyride.passengerservice.domain.PassengerUtils.createPassengerVO;

import com.google.common.collect.Streams;
import io.vividcode.happyride.passengerservice.api.web.CreatePassengerRequest;
import io.vividcode.happyride.passengerservice.api.web.CreateUserAddressRequest;
import io.vividcode.happyride.passengerservice.api.web.PassengerVO;
import io.vividcode.happyride.passengerservice.api.web.UserAddressVO;
import io.vividcode.happyride.passengerservice.dataaccess.PassengerRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PassengerService {

  @Autowired
  PassengerRepository passengerRepository;

  public List<PassengerVO> findAll() {
    return Streams.stream(passengerRepository.findAll())
        .map(PassengerUtils::createPassengerVO)
        .collect(Collectors.toList());
  }

  public Optional<PassengerVO> getPassenger(String passengerId) {
    return passengerRepository.findById(passengerId)
        .map(PassengerUtils::createPassengerVO);
  }

  public PassengerVO createPassenger(CreatePassengerRequest request) {
    Passenger passenger = new Passenger();
    passenger.setName(request.getName());
    passenger.setEmail(request.getEmail());
    passenger.setMobilePhoneNumber(request.getMobilePhoneNumber());
    List<UserAddress> userAddresses = Optional.ofNullable(request.getUserAddresses())
        .map(requests -> requests.stream()
            .map(this::createUserAddress)
            .collect(Collectors.toList())
        ).orElse(new ArrayList<>());
    passenger.setUserAddresses(userAddresses);
    return createPassengerVO(passengerRepository.save(passenger));
  }

  public PassengerVO addAddress(String passengerId, CreateUserAddressRequest request) {
    Passenger passenger = passengerRepository.findById(passengerId)
        .orElseThrow(() -> new PassengerNotFoundException(passengerId));
    UserAddress userAddress = createUserAddress(request);
    passenger.addUserAddress(userAddress);
    return createPassengerVO(passenger);
  }

  public Optional<UserAddressVO> getAddress(String passengerId, String addressId) {
    return passengerRepository.findById(passengerId)
        .flatMap(passenger -> passenger.getUserAddress(addressId))
        .map(PassengerUtils::createUserAddressVO);
  }

  public PassengerVO deleteAddress(String passengerId, String addressId) {
    Passenger passenger = passengerRepository.findById(passengerId)
        .orElseThrow(() -> new PassengerNotFoundException(passengerId));
    passenger.getUserAddress(addressId).ifPresent(passenger::removeUserAddress);
    return createPassengerVO(passenger);
  }


  private UserAddress createUserAddress(CreateUserAddressRequest request) {
    UserAddress address = new UserAddress();
    address.generateId();
    address.setName(request.getName());
    address.setAddressId(request.getAddressId());
    address.setAddressName(request.getAddressName());
    return address;
  }


}
