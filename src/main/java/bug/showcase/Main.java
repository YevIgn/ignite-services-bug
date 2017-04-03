package bug.showcase;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.services.ServiceConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;

import java.util.Collections;

public final class Main {
    private static final String SERVICE_NAME = "service";
    private static final String DISCOVERY_ADDRESSES = "127.0.0.1:47500..47509";
    private static final long SERVICE_AWAIT_TIME = 5_000;
    private static final ServiceConfiguration serviceConfig = new ServiceConfiguration();

    static {
        serviceConfig.setName(SERVICE_NAME);
        serviceConfig.setMaxPerNodeCount(1);
        serviceConfig.setTotalCount(3);
        serviceConfig.setService(new SomeService());
    }

    private Main() {
    }

    private static IgniteConfiguration getIgniteConfig(String gridName) {
        TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();

        ipFinder.setAddresses(Collections.singletonList(DISCOVERY_ADDRESSES));
        return new IgniteConfiguration()
                .setGridName(gridName)
                .setDiscoverySpi(new TcpDiscoverySpi()
                        .setIpFinder(ipFinder))
                .setServiceConfiguration(serviceConfig);
    }

    public static void main(String[] args) throws Exception {

        try (Ignite ignite1 = Ignition.start(getIgniteConfig("ignite1"));
             Ignite ignite2 = Ignition.start(getIgniteConfig("ignite2"))
        ) {
            Thread.sleep(SERVICE_AWAIT_TIME);

            System.out.printf("Started %d services%n", SomeService.SERVICE_NUMBER.get());

            ignite1.services().cancelAll();
            Thread.sleep(SERVICE_AWAIT_TIME);
            ignite1.services().deploy(serviceConfig);
            Thread.sleep(SERVICE_AWAIT_TIME);

            System.out.printf("Started %d services%n", SomeService.SERVICE_NUMBER.get());
        }
    }
}



