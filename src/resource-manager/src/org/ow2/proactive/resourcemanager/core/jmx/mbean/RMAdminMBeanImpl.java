/*
 * ################################################################
 *
 * ProActive: The Java(TM) library for Parallel, Distributed,
 *            Concurrent computing with Security and Mobility
 *
 * Copyright (C) 1997-2008 INRIA/University of Nice-Sophia Antipolis
 * Contact: proactive@ow2.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version
 * 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://proactive.inria.fr/team_members.htm
 *  Contributor(s):
 *
 * ################################################################
 * $$PROACTIVE_INITIAL_DEV$$
 */
package org.ow2.proactive.resourcemanager.core.jmx.mbean;

import org.objectweb.proactive.annotation.PublicAPI;
import org.ow2.proactive.resourcemanager.utils.AtomicRMStatisticsHolder;

/**
 * This class implements {@link RMAdminMBean} to allow the management of the Resource
 * Manager following the JMX standard for management. It provides a consistent view of 
 * the Resource Manager statistics.
 * 
 * @author The ProActive Team
 * @see org.ow2.proactive.resourcemanager.core.jmx.mbean.RMAdminMBean
 * @since ProActive Scheduling 1.1
 */
@PublicAPI
public final class RMAdminMBeanImpl extends RMAnonymMBeanImpl implements
		RMAdminMBean {	

	/**
	 * Creates a new instance of this class.
	 * 
	 * @param rmStatisticsHolder The statistics holder
	 */
	public RMAdminMBeanImpl(final AtomicRMStatisticsHolder rmStatisticsHolder) {
		super(RMAdminMBean.class, rmStatisticsHolder);
	}

	/**
	 * @see org.ow2.proactive.resourcemanager.core.jmx.mbean.RMAdminMBean#getMaxFreeNodes()
	 */
	public int getMaxFreeNodes() {
		return super.rmStatisticsHolder.getStatistics().getMaxFreeNodes();
	}

	/**
	 * @see org.ow2.proactive.resourcemanager.core.jmx.mbean.RMAdminMBean#getMaxBusyNodes()
	 */
	public int getMaxBusyNodes() {
		return super.rmStatisticsHolder.getStatistics().getMaxBusyNodes();
	}

	/**
	 * @see org.ow2.proactive.resourcemanager.core.jmx.mbean.RMAdminMBean#getMaxToBeReleasedNodes()
	 */
	public int getMaxToBeReleasedNodes() {
		return super.rmStatisticsHolder.getStatistics().getMaxBusyNodes();
	}

	/**
	 * @see org.ow2.proactive.resourcemanager.core.jmx.mbean.RMAdminMBean#getMaxDownNodes()
	 */
	public int getMaxDownNodes() {
		return super.rmStatisticsHolder.getStatistics().getMaxDownNodes();
	}

	/**
	 * @see org.ow2.proactive.resourcemanager.core.jmx.mbean.RMAdminMBean#getActivityTimePercentage()
	 */
	public double getActivityTimePercentage() {
		return super.rmStatisticsHolder.getStatistics().getActivityTimePercentage();
	}

	/**
	 * @see org.ow2.proactive.resourcemanager.core.jmx.mbean.RMAdminMBean#getInactivityTimePercentage()
	 */
	public double getInactivityTimePercentage() {
		return super.rmStatisticsHolder.getStatistics()
				.getInactivityTimePercentage();
	}
}