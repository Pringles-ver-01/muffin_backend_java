package com.muffin.web.investProfile;

import com.muffin.web.user.User;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class InvestProfileVO {
    private User user;
    private String investmentPeriod, investmentPropensity;
}


