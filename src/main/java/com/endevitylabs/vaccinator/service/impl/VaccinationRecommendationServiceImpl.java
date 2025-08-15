package com.endevitylabs.vaccinator.service.impl;

import com.endevitylabs.vaccinator.config.ApiVersionConfig;
import com.endevitylabs.vaccinator.dto.recommendation.*;
import com.endevitylabs.vaccinator.exception.RecommendationNotFoundException;
import com.endevitylabs.vaccinator.model.VaccinationRecommendationDocument;
import com.endevitylabs.vaccinator.repository.VaccinationRecommendationMongoRepository;
import com.endevitylabs.vaccinator.service.VaccinationRecommendationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of VaccinationRecommendationService
 */
@Service
public class VaccinationRecommendationServiceImpl implements VaccinationRecommendationService {

    private static final Logger log = LoggerFactory.getLogger(VaccinationRecommendationServiceImpl.class);

    private final VaccinationRecommendationMongoRepository repository;
    private final ApiVersionConfig apiVersionConfig;

    @Autowired
    public VaccinationRecommendationServiceImpl(VaccinationRecommendationMongoRepository repository, 
                                               ApiVersionConfig apiVersionConfig) {
        this.repository = repository;
        this.apiVersionConfig = apiVersionConfig;
    }

    @Override
    public BulkLoadResponse bulkLoadRecommendations(VaccinationRecommendationRequest request) {
        log.info("Starting bulk load of vaccination recommendations");
        
        try {
            // Clear existing recommendations
            long existingCount = repository.count();
            repository.deleteAll();
            log.info("Cleared {} existing vaccination recommendations", existingCount);
            
            // Create and save new document
            VaccinationRecommendationDocument document = new VaccinationRecommendationDocument(request);
            VaccinationRecommendationDocument savedDocument = repository.save(document);
            
            log.info("Successfully saved vaccination recommendations: {} vaccines, schema version: {}", 
                    request.getVaccines().size(), request.getSchemaVersion());
            
            return BulkLoadResponse.builder()
                    .message("Vaccination recommendations loaded successfully")
                    .existingCount(existingCount)
                    .newCount(1)
                    .failedCount(0)
                    .failedVaccines(new ArrayList<>())
                    .schemaVersion(request.getSchemaVersion())
                    .source(request.getSourcePdf()) // Use sourcePdf since source is not in the JSON
                    .uploadedAt(LocalDateTime.now())
                    .timestamp(LocalDateTime.now())
                    .apiVersion(apiVersionConfig.getApiVersion())
                    .build();
                    
        } catch (Exception e) {
            log.error("Failed to load vaccination recommendations: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to load vaccination recommendations", e);
        }
    }

    @Override
    public RecommendationsResponse getPersonalizedRecommendations(RecommendationQuery query) {
        
        log.info("Getting personalized recommendations for query: {}", query);
        
        VaccinationRecommendationDocument document = repository.findFirstByOrderByCreatedAtDesc()
                .orElseThrow(() -> new RecommendationNotFoundException("No vaccination recommendations found"));
        
        List<PersonalizedRecommendation> recommendations = document.getVaccines().stream()
                .filter(vaccine -> hasApplicableRecommendation(vaccine, query))
                .map(vaccine -> createPersonalizedRecommendationWithReason(vaccine, query))
                .toList();
        
        log.info("Found {} applicable recommendations for query", recommendations.size());
        
        return RecommendationsResponse.builder()
                .recommendations(recommendations)
                .query(query)
                .schemaVersion(document.getSchemaVersion())
                .source(document.getSource())
                .timestamp(LocalDateTime.now())
                .apiVersion(apiVersionConfig.getApiVersion())
                .build();
    }

    @Override
    public List<String> getAllAvailableVaccines() {
        VaccinationRecommendationDocument document = repository.findFirstByOrderByCreatedAtDesc()
                .orElseThrow(() -> new RecommendationNotFoundException("No vaccination recommendations found"));
        
        return document.getVaccines().stream()
                .map(VaccinationRecommendationRequest.VaccineRecommendation::getImmunizingAgent)
                .collect(Collectors.toList());
    }

    @Override
    public RecommendationsResponse getRecommendationMetadata() {
        VaccinationRecommendationDocument document = repository.findFirstByOrderByCreatedAtDesc()
                .orElseThrow(() -> new RecommendationNotFoundException("No vaccination recommendations found"));
        
        return RecommendationsResponse.builder()
                .recommendations(new ArrayList<>())
                .query(null)
                .schemaVersion(document.getSchemaVersion())
                .source(document.getSource())
                .timestamp(LocalDateTime.now())
                .apiVersion(apiVersionConfig.getApiVersion())
                .build();
    }

    private boolean hasApplicableRecommendation(VaccinationRecommendationRequest.VaccineRecommendation vaccine,
                                              RecommendationQuery query) {
        return vaccine.getRecommendations().stream()
                .anyMatch(recommendation -> isApplicable(recommendation.getWho(), query));
    }
    
    private PersonalizedRecommendation createPersonalizedRecommendationWithReason(
            VaccinationRecommendationRequest.VaccineRecommendation vaccine,
            RecommendationQuery query) {
        
        // Find applicable recommendation based on criteria
        VaccinationRecommendationRequest.Recommendation applicableRecommendation = vaccine.getRecommendations().stream()
                .filter(recommendation -> isApplicable(recommendation.getWho(), query))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No applicable recommendation found for vaccine: " + vaccine.getImmunizingAgent()));
        
        // Generate specific applicability reason based on the matched recommendation
        String applicabilityReason = generateApplicabilityReason(applicableRecommendation.getWho(), query);
        
        // Create personalized recommendation
        return PersonalizedRecommendation.builder()
                .vaccine(vaccine.getImmunizingAgent())
                .abbr(vaccine.getAbbr())
                .why(vaccine.getWhy())
                .label(applicableRecommendation.getLabel())
                .series(applicableRecommendation.getSchedule().getSeries())
                .doses(createDoseList(applicableRecommendation.getSchedule(), query))
                .notes(applicableRecommendation.getSchedule().getNotes())
                .rules(applicableRecommendation.getSchedule().getRules())
                .annual(applicableRecommendation.getSchedule().getAnnual())
                .singleDose(applicableRecommendation.getSchedule().getSingleDose())
                .applicabilityReason(applicabilityReason)
                .citations(applicableRecommendation.getCitations())
                .build();
    }
    
    private String generateApplicabilityReason(WhoCriteria who, RecommendationQuery query) {
        StringBuilder reason = new StringBuilder("Matches criteria: ");
        
        // Age criteria
        if (who.getAge() != null && query.getAgeYears() != null) {
            reason.append("age=").append(query.getAgeYears()).append(" years");
            
            if (who.getAge().getMinYears() != null || who.getAge().getMaxYears() != null) {
                reason.append(" (range: ");
                if (who.getAge().getMinYears() != null) {
                    reason.append("≥").append(who.getAge().getMinYears());
                }
                if (who.getAge().getMaxYears() != null) {
                    if (who.getAge().getMinYears() != null) reason.append(" and ");
                    reason.append("≤").append(who.getAge().getMaxYears());
                }
                reason.append(" years)");
            }
        }
        
        // Life stage criteria
        if (who.getLifeStage() != null && query.getLifeStage() != null) {
            reason.append(", lifeStage=").append(query.getLifeStage());
            if (!who.getLifeStage().equalsIgnoreCase("any")) {
                reason.append(" (required: ").append(who.getLifeStage()).append(")");
            }
        }
        
        // Sex criteria
        if (who.getSex() != null && query.getSex() != null) {
            reason.append(", sex=").append(query.getSex());
            if (!who.getSex().equalsIgnoreCase("any")) {
                reason.append(" (required: ").append(who.getSex()).append(")");
            }
        }
        
        // Pregnancy status criteria
        if (who.getPregnancyStatus() != null && query.getPregnancyStatus() != null) {
            reason.append(", pregnancyStatus=").append(query.getPregnancyStatus());
        }
        
        // Immunocompromised criteria
        if (who.getImmunocompromised() != null && query.getImmunocompromised() != null) {
            reason.append(", immunocompromised=").append(query.getImmunocompromised());
        }
        
        // Risk factors criteria
        if (who.getRiskFactors() != null && !who.getRiskFactors().isEmpty() && 
            query.getRiskFactors() != null && !query.getRiskFactors().isEmpty()) {
            reason.append(", riskFactors=").append(String.join(", ", query.getRiskFactors()));
            reason.append(" (required: ").append(String.join(", ", who.getRiskFactors())).append(")");
        }
        
        // Special conditions criteria
        if (who.getSpecialConditions() != null && !who.getSpecialConditions().isEmpty() && 
            query.getSpecialConditions() != null && !query.getSpecialConditions().isEmpty()) {
            reason.append(", specialConditions=").append(String.join(", ", query.getSpecialConditions()));
            reason.append(" (required: ").append(String.join(", ", who.getSpecialConditions())).append(")");
        }
        
        return reason.toString();
    }

    private boolean isApplicable(WhoCriteria who, RecommendationQuery query) {
        
        return isAgeApplicable(who.getAge(), query.getAgeYears()) &&
               isLifeStageApplicable(who.getLifeStage(), query.getLifeStage()) &&
               isSexApplicable(who.getSex(), query.getSex()) &&
               isPregnancyStatusApplicable(who.getPregnancyStatus(), query.getPregnancyStatus()) &&
               isImmunocompromisedApplicable(who.getImmunocompromised(), query.getImmunocompromised()) &&
               isSpecialConditionsApplicable(who.getSpecialConditions(), query.getSpecialConditions()) &&
               isRiskFactorsApplicable(who.getRiskFactors(), query.getRiskFactors());
    }
    
    private boolean isAgeApplicable(VaccinationRecommendationRequest.AgeRange age, Double queryAge) {
        if (age == null || queryAge == null) {
            return true; // No age criteria specified
        }
        
        if (age.getMinYears() != null && queryAge < age.getMinYears()) {
            return false;
        }
        
        if (age.getMaxYears() != null && queryAge > age.getMaxYears()) {
            return false;
        }
        
        return true;
    }
    
    private boolean isLifeStageApplicable(String whoLifeStage, String queryLifeStage) {
        if (whoLifeStage == null || queryLifeStage == null) {
            return true; // No life stage criteria specified
        }
        
        return whoLifeStage.equalsIgnoreCase(queryLifeStage) || 
               whoLifeStage.equalsIgnoreCase("any");
    }
    
    private boolean isSexApplicable(String whoSex, String querySex) {
        if (whoSex == null || querySex == null) {
            return true; // No sex criteria specified
        }
        
        return whoSex.equalsIgnoreCase(querySex) || 
               whoSex.equalsIgnoreCase("any");
    }
    
    private boolean isPregnancyStatusApplicable(String whoPregnancyStatus, String queryPregnancyStatus) {
        if (whoPregnancyStatus == null || queryPregnancyStatus == null) {
            return true; // No pregnancy status criteria specified
        }
        
        return whoPregnancyStatus.equalsIgnoreCase(queryPregnancyStatus);
    }
    
    private boolean isImmunocompromisedApplicable(Boolean whoImmunocompromised, Boolean queryImmunocompromised) {
        if (whoImmunocompromised == null || queryImmunocompromised == null) {
            return true; // No immunocompromised criteria specified
        }
        
        return whoImmunocompromised.equals(queryImmunocompromised);
    }
    
    private boolean isSpecialConditionsApplicable(List<String> whoSpecialConditions, List<String> querySpecialConditions) {
        if (whoSpecialConditions == null || whoSpecialConditions.isEmpty()) {
            return true; // No special conditions required
        }
        
        if (querySpecialConditions == null || querySpecialConditions.isEmpty()) {
            return false; // WHO requires special conditions but query has none
        }
        
        // Check if at least one required special condition is present
        return whoSpecialConditions.stream()
                .anyMatch(querySpecialConditions::contains);
    }
    
    private boolean isRiskFactorsApplicable(List<String> whoRiskFactors, List<String> queryRiskFactors) {
        if (whoRiskFactors == null || whoRiskFactors.isEmpty()) {
            return true; // No risk factors required
        }
        
        if (queryRiskFactors == null || queryRiskFactors.isEmpty()) {
            return false; // WHO requires risk factors but query has none
        }
        
        // Check if at least one required risk factor is present
        return whoRiskFactors.stream()
                .anyMatch(queryRiskFactors::contains);
    }

    private List<Dose> createDoseList(VaccinationRecommendationRequest.Schedule schedule, RecommendationQuery query) {

        // If there are branches, use them instead of the main doses
        if (schedule.getBranches() != null && !schedule.getBranches().isEmpty()) {
            // Find applicable branch based on age and other criteria
            VaccinationRecommendationRequest.Branch applicableBranch = schedule.getBranches().stream()
                    .filter(branch -> isBranchApplicable(branch.getAppliesWhen(), query))
                    .findFirst()
                    .orElse(null);

            if (applicableBranch != null) {
                return applicableBranch.getDoses();
            }

        } else if (schedule.getDoses() != null) {
            // Use main schedule doses if no branches
            return schedule.getDoses();
        }
        
        return new ArrayList<>();
    }

    private boolean isBranchApplicable(VaccinationRecommendationRequest.AppliesWhen appliesWhen, RecommendationQuery query) {
        
        if (appliesWhen.getAgeYearsMin() != null && query.getAgeYears() != null) {
            if (query.getAgeYears() < appliesWhen.getAgeYearsMin()) {
                return false;
            }
        }
        
        if (appliesWhen.getAgeYearsMax() != null && query.getAgeYears() != null) {
            if (query.getAgeYears() > appliesWhen.getAgeYearsMax()) {
                return false;
            }
        }
        
        if (appliesWhen.getAgeAtSeriesStartYearsLt() != null && query.getAgeYears() != null) {
            if (query.getAgeYears() >= appliesWhen.getAgeAtSeriesStartYearsLt()) {
                return false;
            }
        }
        
        if (appliesWhen.getAgeAtSeriesStartYearsGe() != null && query.getAgeYears() != null) {
            if (query.getAgeYears() < appliesWhen.getAgeAtSeriesStartYearsGe()) {
                return false;
            }
        }
        
        if (appliesWhen.getProducts() != null && query.getProduct() != null) {
            if (!appliesWhen.getProducts().contains(query.getProduct())) {
                return false;
            }
        }
        
        if (appliesWhen.getImmunocompromised() != null && query.getImmunocompromised() != null) {
            if (!appliesWhen.getImmunocompromised().equals(query.getImmunocompromised())) {
                return false;
            }
        }
        
        if (appliesWhen.getNeedRapidProtection() != null && query.getNeedRapidProtection() != null) {
            if (!appliesWhen.getNeedRapidProtection().equals(query.getNeedRapidProtection())) {
                return false;
            }
        }
        
        return true;
    }

}
