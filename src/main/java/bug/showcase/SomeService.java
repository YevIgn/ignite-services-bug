package bug.showcase;

import org.apache.ignite.services.Service;
import org.apache.ignite.services.ServiceContext;

import java.util.concurrent.atomic.AtomicInteger;

public class SomeService implements Service {
    public static final AtomicInteger SERVICE_NUMBER = new AtomicInteger();

    @Override
    public void init(ServiceContext context) {
        SERVICE_NUMBER.incrementAndGet();
    }

    @Override
    public void execute(ServiceContext context) {
    }

    @Override
    public void cancel(ServiceContext context) {
        SERVICE_NUMBER.decrementAndGet();
    }
}
