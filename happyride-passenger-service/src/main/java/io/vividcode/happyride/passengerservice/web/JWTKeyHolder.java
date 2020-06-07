package io.vividcode.happyride.passengerservice.web;

import io.jsonwebtoken.impl.crypto.MacProvider;
import java.security.Key;

public class JWTKeyHolder {

  public static Key KEY = MacProvider.generateKey();
}
