package io.vividcode.happyride.passengerservice.service;

import io.vividcode.happyride.passengerservice.api.web.CreatePassengerRequest;
import io.vividcode.happyride.passengerservice.api.web.CreateUserAddressRequest;
import io.vividcode.happyride.passengerservice.dataaccess.PassengerRepository;
import io.vividcode.happyride.passengerservice.domain.Passenger;
import io.vividcode.happyride.passengerservice.domain.UserAddress;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
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
    Set<UserAddress> userAddresses = createUserAddresses(request.getUserAddresses());
    passenger.setUserAddresses(userAddresses);
    passengerRepository.save(passenger);
    return passenger;
  }

  public Passenger addAddress(String passengerId, CreateUserAddressRequest request) {
    Passenger passenger = passengerRepository.findById(passengerId)
        .orElseThrow(() -> new PassengerNotFoundException(passengerId));
    UserAddress userAddress = createUserAddress(request);
    passenger.getUserAddresses().add(userAddress);
    passengerRepository.save(passenger);
    return passenger;
  }

  private Set<UserAddress> createUserAddresses(Set<CreateUserAddressRequest> requests) {
    if (requests == null) {
      return new HashSet<>();
    }
    return requests.stream()
        .map(this::createUserAddress)
        .collect(Collectors.toSet());
  }

  private UserAddress createUserAddress(CreateUserAddressRequest request) {
    UserAddress address = new UserAddress();
    address.setName(request.getName());
    address.setAddressId(request.getAddressId());
    return address;
  }
}
