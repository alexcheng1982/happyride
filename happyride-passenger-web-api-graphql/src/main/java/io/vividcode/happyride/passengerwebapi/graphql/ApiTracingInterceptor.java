package io.vividcode.happyride.passengerwebapi.graphql;

import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMap;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;
import okhttp3.Interceptor;
import okhttp3.Request.Builder;
import okhttp3.Response;

public class ApiTracingInterceptor implements Interceptor {

  private final Tracer tracer;

  public ApiTracingInterceptor(final Tracer tracer) {
    this.tracer = tracer;
  }

  @Override
  public Response intercept(final Chain chain) throws IOException {
    final Builder builder = chain.request().newBuilder();
    if (this.tracer.activeSpan() != null) {
      this.tracer.inject(this.tracer.activeSpan().context(),
          Format.Builtin.HTTP_HEADERS,
          new InjectOnlyTextMap(builder));
    }
    return chain.proceed(builder.build());
  }

  private static class InjectOnlyTextMap implements TextMap {

    private final Builder builder;

    public InjectOnlyTextMap(final Builder builder) {
      this.builder = builder;
    }

    @Override
    public Iterator<Entry<String, String>> iterator() {
      throw new UnsupportedOperationException("Only context injection is supported");
    }

    @Override
    public void put(final String key, final String value) {
      this.builder.addHeader(key, value);
    }
  }
}
