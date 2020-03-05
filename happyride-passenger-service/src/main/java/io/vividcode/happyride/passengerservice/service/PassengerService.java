package io.vividcode.happyride.passengerservice.service;

import io.vividcode.happyride.passengerservice.api.web.CreatePassengerRequest;
import io.vividcode.happyride.passengerservice.api.web.CreateUserAddressRequest;
import io.vividcode.happyride.passengerservice.dataaccess.PassengerRepository;
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
            .map(this::createUserAddress)
            .collect(Collectors.toList())
        ).orElse(new ArrayList<>());
    passenger.setUserAddresses(userAddresses);
    return passengerRepository.save(passenger);
  }

  public UserAddress addAddress(String passengerId, CreateUserAddressRequest request) {
    Passenger passenger = passengerRepository.findById(passengerId)
        .orElseThrow(() -> new PassengerNotFoundException(passengerId));
    UserAddress userAddress = createUserAddress(request);
    passenger.addUserAddress(userAddress);
    passengerRepository.save(passenger);
    return userAddress;
  }

  public Optional<UserAddress> getAddress(String passengerId, String addressId) {
    return passengerRepository.findById(passengerId)
        .flatMap(passenger -> passenger.getUserAddress(addressId));
  }

  public void deleteAddress(String passengerId, String addressId) {
    Passenger passenger = passengerRepository.findById(passengerId)
        .orElseThrow(() -> new PassengerNotFoundException(passengerId));
    passenger.getUserAddress(addressId).ifPresent(passenger::removeUserAddress);
    passengerRepository.save(passenger);
  }

  private UserAddress createUserAddress(CreateUserAddressRequest request) {
    UserAddress address = new UserAddress();
    address.generateId();
    address.setName(request.getName());
    address.setAddressId(request.getAddressId());
    return address;
  }
}
