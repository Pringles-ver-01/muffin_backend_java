package com.muffin.web.investProfile;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvestProfileRepository extends JpaRepository<InvestProfile, Long>, IInvestProfileRepository {

}
