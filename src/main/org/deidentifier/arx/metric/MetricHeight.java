/*
 * ARX: Powerful Data Anonymization
 * Copyright (C) 2012 - 2014 Florian Kohlmayer, Fabian Prasser
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.deidentifier.arx.metric;

import org.deidentifier.arx.framework.check.groupify.IHashGroupify;
import org.deidentifier.arx.framework.lattice.Node;

/**
 * This class provides an implementation of the Height metric.
 * 
 * @author Fabian Prasser
 * @author Florian Kohlmayer
 */
public class MetricHeight extends MetricDefault {

    /** SVUID */
    private static final long serialVersionUID = 5911337622032778562L;

    /**
     * Creates a new instance
     */
    protected MetricHeight() {
        super(true, true);
    }

    @Override
    public String toString() {
        return "Height";
    }
    
    @Override
    public InformationLossDefault getLowerBound(Node node) {
        return new InformationLossDefault(node.getLevel());
    }

    @Override
    public InformationLossDefault getLowerBound(Node node, final IHashGroupify g) {
        return new InformationLossDefault(node.getLevel());
    }
    
    @Override
    protected BoundInformationLoss<InformationLossDefault> evaluateInternal(final Node node, final IHashGroupify g) {
        int level = node.getLevel();
        return new BoundInformationLossDefault(level, level);
    }
}
