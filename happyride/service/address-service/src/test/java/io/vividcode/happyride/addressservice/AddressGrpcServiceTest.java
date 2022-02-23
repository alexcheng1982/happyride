package io.vividcode.happyride.addressservice;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.playtika.test.common.spring.EmbeddedContainersShutdownAutoConfiguration;
import com.playtika.test.postgresql.EmbeddedPostgreSQLBootstrapConfiguration;
import com.playtika.test.postgresql.EmbeddedPostgreSQLDependenciesAutoConfiguration;
import io.vividcode.happyride.addressservice.dataaccess.AddressRepository;
import io.vividcode.happyride.addressservice.grpc.Address;
import io.vividcode.happyride.addressservice.grpc.AddressGrpcService;
import io.vividcode.happyride.addressservice.grpc.AddressSearchRequest;
import io.vividcode.happyride.addressservice.grpc.AddressServiceGrpc.AddressServiceBlockingStub;
import io.vividcode.happyride.addressservice.service.AddressService;
import io.vividcode.happyride.postgres.common.EmbeddedPostgresConfiguration;
import java.util.Iterator;
import net.devh.boot.grpc.client.autoconfigure.GrpcClientAutoConfiguration;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.server.autoconfigure.GrpcServerAutoConfiguration;
import net.devh.boot.grpc.server.autoconfigure.GrpcServerFactoryAutoConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = {
    AddressRepository.class,
    AddressService.class,
    AddressGrpcService.class
})
@ContextConfiguration(classes = {
    EmbeddedPostgresConfiguration.class
})
@ImportAutoConfiguration(classes = {
    EmbeddedPostgreSQLDependenciesAutoConfiguration.class,
    EmbeddedPostgreSQLBootstrapConfiguration.class,
    EmbeddedContainersShutdownAutoConfiguration.class,
    GrpcServerAutoConfiguration.class,
    GrpcServerFactoryAutoConfiguration.class,
    GrpcClientAutoConfiguration.class
})
@TestPropertySource(properties = {
    "embedded.postgresql.docker-image=postgres:12-alpine",
    "grpc.server.port=8888",
    "grpc.client.addressService.address=localhost:8888",
    "grpc.client.addressService.negotiationType=PLAINTEXT"
})
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DirtiesContext
@DisplayName("Address service (gPRC)")
public class AddressGrpcServiceTest {

  @GrpcClient("addressService")
  AddressServiceBlockingStub addressService;

  @Test
  @DisplayName("Search address")
  void testSearchAddress() {
    Iterator<Address> result = addressService
        .search(AddressSearchRequest.newBuilder()
            .setAreaCode(110101001015L)
            .setQuery("王府井社区居委会")
            .build());
    assertThat(result.hasNext()).isTrue();
  }
}
