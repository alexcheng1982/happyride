package io.vividcode.happyride.security;

import io.jsonwebtoken.impl.crypto.MacProvider;
import java.security.Key;

public class JWTKeyHolder {

  public static Key KEY = MacProvider.generateKey();
}
