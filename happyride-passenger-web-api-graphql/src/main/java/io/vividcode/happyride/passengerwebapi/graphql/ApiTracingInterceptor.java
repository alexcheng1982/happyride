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

  public ApiTracingInterceptor(Tracer tracer) {
    this.tracer = tracer;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Builder builder = chain.request().newBuilder();
    if (this.tracer.activeSpan() != null) {
      this.tracer.inject(this.tracer.activeSpan().context(),
          Format.Builtin.HTTP_HEADERS,
          new TextMap() {
            @Override
            public Iterator<Entry<String, String>> iterator() {
              throw new UnsupportedOperationException("");
            }

            @Override
            public void put(String key, String value) {
              builder.addHeader(key, value);
            }
          });
    }
    return chain.proceed(builder.build());
  }
}
