package learn.mastery.data;

import learn.mastery.models.Host;

import java.util.List;

public interface HostRepository {

    Host findByEmail(String email);

    List<Host> findAll();
}
