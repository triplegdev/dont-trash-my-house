package learn.mastery.data;

import learn.mastery.models.Host;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HostRepositoryDouble implements HostRepository {

    private final static Host HOST = makeHost();

    private final ArrayList<Host> hosts = new ArrayList<>();

    public HostRepositoryDouble() { hosts.add(HOST); }

    @Override
    public List<Host> findAll() {
        return hosts;
    }

    @Override
    public Host findByEmail(String email) {
        return findAll().stream()
                .filter(i -> i.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }


    private static Host makeHost() {
        Host host = new Host();
        host.setId("5a7bca2e-9e4d-4346-b719-f0f7c1f8e43b");
        host.setLastName("Smith");
        host.setEmail("smith@example.com");
        host.setPhone("(123) 4567890");
        host.setAddress("123 Main St");
        host.setCity("Los Angeles");
        host.setState("CA");
        host.setPostalCode("12345");
        host.setStandardRate(new BigDecimal(150));
        host.setWeekendRate(new BigDecimal(175));
        return host;
    }
}
