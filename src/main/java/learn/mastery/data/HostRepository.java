package learn.mastery.data;

import learn.mastery.models.Host;

import java.util.List;

public interface HostRepository {

    List<Host> findAll();

    Host findByEmail(String email);
}
