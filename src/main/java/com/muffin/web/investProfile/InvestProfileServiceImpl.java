package com.muffin.web.investProfile;

import com.muffin.web.user.UserRepository;
import com.muffin.web.util.GenericService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

interface InvestProfileService extends GenericService<InvestProfile>{

    void save(InvestProfileVO investProfile);

    void update(InvestProfileVO investProfile);

    Optional<InvestProfile> findByInvestProfileId(Long id);
}

@Service
@AllArgsConstructor
public class InvestProfileServiceImpl implements InvestProfileService {

    private final InvestProfileRepository repository;
    private final UserRepository userRepository;

    @Override
    public void save(InvestProfileVO investProfile) {
        InvestProfile ip = new InvestProfile(investProfile.getInvestmentPeriod(), investProfile.getInvestmentPropensity(), investProfile.getUser());
        repository.save(ip);
    }

    @Override
    public void update(InvestProfileVO investProfile) {
        InvestProfile ip = null;
        Optional<InvestProfile> findInvestProfile = repository.findById(investProfile.getUser().getUserId());
        ip = findInvestProfile.orElseGet(InvestProfile::new);
        ip.setUser(userRepository.findById(investProfile.getUser().getUserId()).get());
        ip.setInvestmentPropensity(investProfile.getInvestmentPropensity());
        ip.setInvestmentPeriod(investProfile.getInvestmentPeriod());
        repository.save(ip);
    }

    @Override
    public Optional<InvestProfile> findByInvestProfileId(Long id) {
        return Optional.empty();
    }

    @Override
    public Iterable<InvestProfile> findAll() {
        return null;
    }

    @Override
    public int count() {
        return 0;
    }

    @Override
    public void delete(InvestProfile investProfile) {

    }

    @Override
    public boolean exists(String id) {
        return false;
    }
}
