/*
 * ARX: Powerful Data Anonymization
 * Copyright 2012 - 2015 Florian Kohlmayer, Fabian Prasser
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.deidentifier.arx.criteria;

import org.deidentifier.arx.ARXPopulationModel;
import org.deidentifier.arx.framework.check.groupify.HashGroupifyDistribution;
import org.deidentifier.arx.risk.RiskModelPopulationBasedUniquenessRisk;
import org.deidentifier.arx.risk.RiskModelPopulationBasedUniquenessRisk.StatisticalModel;

/**
 * This criterion ensures that the fraction of population uniques falls below a given threshold.
 * 
 * @author Fabian Prasser
 */
public class RiskBasedThresholdPopulationUniques extends RiskBasedPrivacyCriterion{

    /** SVUID */
    private static final long  serialVersionUID = 618039085843721351L;

    /** The statistical model */
    private StatisticalModel   statisticalModel;

    /** The population model */
    private ARXPopulationModel populationModel;

    /**
     * Creates a new instance of this criterion. Uses Dankar's method for estimating population uniqueness.
     *  
     * @param riskThreshold
     * @param populationModel
     */
    public RiskBasedThresholdPopulationUniques(double riskThreshold, ARXPopulationModel populationModel){
        this(riskThreshold, StatisticalModel.DANKAR, populationModel);
    }

    /**
     * Creates a new instance of this criterion. Uses the specified method for estimating population uniqueness.
     *  
     * @param riskThreshold
     * @param statisticalModel
     * @param populationModel
     */
    public RiskBasedThresholdPopulationUniques(double riskThreshold,
                                               StatisticalModel statisticalModel, 
                                               ARXPopulationModel populationModel){
        super(false, riskThreshold);
        this.statisticalModel = statisticalModel;
        this.populationModel = populationModel;
    }

    /**
     * @return the populationModel
     */
    public ARXPopulationModel getPopulationModel() {
        return populationModel;
    }

    /**
     * @return the statisticalModel
     */
    public StatisticalModel getStatisticalModel() {
        return statisticalModel;
    }

    @Override
    public String toString() {
        return "(<" + getRiskThreshold() + "-population-uniques (" + statisticalModel.toString().toLowerCase() + ")";
    }

    /**
     * We currently assume that at any time, at least one statistical model converges.
     * This might not be the case, and 0 may be returned instead. That's why we only
     * accept estimates of 0, if the number of equivalence classes of size 1 in the sample is also zero
     * 
     * @param distribution
     * @return
     */
    protected boolean isFulfilled(HashGroupifyDistribution distribution) {

        // TODO: Now, there really is a case in which we should only evaluate the required model
        RiskModelPopulationBasedUniquenessRisk riskModel = new RiskModelPopulationBasedUniquenessRisk(this.populationModel, 
                                                                                                      distribution.getEquivalenceClasses(), 
                                                                                                      distribution.getNumberOfTuples());
        
        double populationUniques = riskModel.getFractionOfUniqueTuples(this.statisticalModel);
        if (populationUniques > 0d && populationUniques < getRiskThreshold()) {
            return true;
        } else if (populationUniques == 0d && distribution.getFractionOfTuplesInClassesOfSize(1) == 0d) {
            return true;
        } else {
            return false;
        }
    }
}