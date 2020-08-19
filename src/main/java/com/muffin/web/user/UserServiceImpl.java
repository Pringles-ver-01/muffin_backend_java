package com.muffin.web.user;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import com.muffin.web.util.Box;
import com.muffin.web.util.GenericService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;

interface UserService extends GenericService<User> {

    User save(User user);

    Optional<User> findByEmailId(String emailId);

    void readCsv();
}

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    public Optional<User> findByEmailId(String emailId) {
        return repository.findByEmailId(emailId);
    }

    @Override
    public void readCsv() {
        InputStream is = getClass().getResourceAsStream("/static/users.csv");
        try {
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            CSVParser parser = new CSVParser(fileReader, CSVFormat.DEFAULT);
            Iterable<CSVRecord> csvRecords = parser.getRecords();
            for(CSVRecord csvRecord : csvRecords) {
                repository.save(new User(
                        csvRecord.get(0),
                        csvRecord.get(1),
                        csvRecord.get(3),
                        csvRecord.get(2)
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Iterable<User> findAll() {
        return null;
    }

    @Override
    public int count() {
        return 0;
    }

    @Override
    public void delete(User user) {
        repository.delete(user);
    }

    @Override
    public boolean exists(String emailId) {
        return repository.existsByEmailId(emailId);
    }


}