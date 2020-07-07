package io.vividcode.happyride.addressservice.api.web;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class AddressBatchRequest {

  @NonNull
  private List<String> addressIds;
}
