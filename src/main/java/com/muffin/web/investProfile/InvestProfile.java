package com.muffin.web.investProfile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.muffin.web.user.User;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "invest_profile")
public class InvestProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="invest_profile_id") private Long investProfileId;
    @Column(name="investment_period") private String investmentPeriod;
    @Column(name="investment_propensity") private String investmentPropensity;

    @Builder
    public InvestProfile(String investmentPeriod, String investmentPropensity, User user) {
        this.investmentPeriod = investmentPeriod;
        this.investmentPropensity = investmentPropensity;
        this.user = user;
    }

    @OneToOne @JoinColumn(name="user_id") @JsonIgnore
    private User user;

}
