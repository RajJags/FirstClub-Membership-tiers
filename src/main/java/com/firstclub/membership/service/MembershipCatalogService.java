package com.firstclub.membership.service;

import com.firstclub.membership.api.dto.CatalogResponse;
import com.firstclub.membership.repository.MembershipPlanRepository;
import com.firstclub.membership.repository.MembershipTierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MembershipCatalogService {

    private final MembershipPlanRepository planRepository;
    private final MembershipTierRepository tierRepository;
    private final MembershipMapper mapper;

    public MembershipCatalogService(
            MembershipPlanRepository planRepository,
            MembershipTierRepository tierRepository,
            MembershipMapper mapper
    ) {
        this.planRepository = planRepository;
        this.tierRepository = tierRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public CatalogResponse catalog() {
        return new CatalogResponse(
                planRepository.findByActiveTrueOrderByIdAsc().stream().map(mapper::toPlanResponse).toList(),
                tierRepository.findAllByOrderByRankOrderAsc().stream().map(mapper::toTierResponse).toList()
        );
    }
}
