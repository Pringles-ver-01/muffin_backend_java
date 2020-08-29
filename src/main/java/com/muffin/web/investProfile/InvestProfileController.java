package com.muffin.web.investProfile;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RequestMapping("/investProfile")
public class InvestProfileController {

    private final InvestProfileService investProfileService;

    public InvestProfileController(InvestProfileService investProfileService) {
        this.investProfileService = investProfileService;
    }

    @PostMapping("/create")
    public void save(@RequestBody InvestProfileVO investProfile){
        System.out.println(investProfile);
        investProfileService.save(investProfile);
    }

    @PostMapping("/update")
    public void update(@RequestBody InvestProfileVO investProfile){
        System.out.println(investProfile);
        investProfileService.update(investProfile);
    }

    @GetMapping("/csv")
    public void csvRead(){
        investProfileService.readCsv();
    }

    @GetMapping("/findOne/{userId}")
    public InvestProfile findOne(@PathVariable Long userId){
        System.out.println(investProfileService.findOne(userId));
        return investProfileService.findOne(userId);
    }
}