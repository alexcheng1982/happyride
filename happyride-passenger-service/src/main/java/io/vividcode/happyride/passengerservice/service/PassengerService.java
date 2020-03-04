package io.vividcode.happyride.passengerservice.service;

import io.vividcode.happyride.passengerservice.api.web.CreatePassengerRequest;
import io.vividcode.happyride.passengerservice.api.web.CreateUserAddressRequest;
import io.vividcode.happyride.passengerservice.dataaccess.PassengerRepository;
import io.vividcode.happyride.passengerservice.dataaccess.UserAddressRepository;
import io.vividcode.happyride.passengerservice.domain.Passenger;
import io.vividcode.happyride.passengerservice.domain.UserAddress;
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

  @Autowired
  UserAddressRepository userAddressRepository;

  public Optional<Passenger> getPassenger(String passengerId) {
    return passengerRepository.findById(passengerId);
  }

  public Passenger createPassenger(CreatePassengerRequest request) {
    Passenger passenger = new Passenger();
    passenger.setName(request.getName());
    passenger.setEmail(request.getEmail());
    passenger.setMobilePhoneNumber(request.getMobilePhoneNumber());
    List<UserAddress> userAddresses = Optional.ofNullable(request.getUserAddresses())
        .map(requests -> requests.stream()
            .map(req -> createUserAddress(passenger, req))
            .collect(Collectors.toList())
        ).orElse(new ArrayList<>());
    passenger.setUserAddresses(userAddresses);
    return passengerRepository.save(passenger);
  }

  public UserAddress addAddress(String passengerId, CreateUserAddressRequest request) {
    Passenger passenger = passengerRepository.findById(passengerId)
        .orElseThrow(() -> new PassengerNotFoundException(passengerId));
    UserAddress userAddress = createUserAddress(passenger, request);
    passenger.getUserAddresses().add(userAddress);
    passengerRepository.save(passenger);
    return userAddressRepository.save(userAddress);
  }

  public Optional<UserAddress> getAddress(String passengerId, String addressId) {
    return passengerRepository.findById(passengerId)
        .flatMap(passenger -> passenger.getUserAddress(addressId));
  }

  public void deleteAddress(String passengerId, String addressId) {
    Passenger passenger = passengerRepository.findById(passengerId)
        .orElseThrow(() -> new PassengerNotFoundException(passengerId));
    passenger.getUserAddress(addressId).ifPresent(address -> {
      passenger.removeAddress(address);
      userAddressRepository.delete(address);
    });
  }

  private UserAddress createUserAddress(Passenger passenger, CreateUserAddressRequest request) {
    UserAddress address = new UserAddress();
    address.setPassenger(passenger);
    address.setName(request.getName());
    address.setAddressId(request.getAddressId());
    return address;
  }
}
