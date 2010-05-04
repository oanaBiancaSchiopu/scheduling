/*
 * ################################################################
 *
 * ProActive Parallel Suite(TM): The Java(TM) library for
 *    Parallel, Distributed, Multi-Core Computing for
 *    Enterprise Grids & Clouds
 *
 * Copyright (C) 1997-2010 INRIA/University of 
 * 				Nice-Sophia Antipolis/ActiveEon
 * Contact: proactive@ow2.org or contact@activeeon.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; version 3 of
 * the License.
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
 * If needed, contact us to obtain a release under GPL Version 2 
 * or a different license than the GPL.
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://proactive.inria.fr/team_members.htm
 *  Contributor(s):
 *
 * ################################################################
 * $$PROACTIVE_INITIAL_DEV$$
 */
package org.ow2.proactive.resourcemanager.frontend;

import org.objectweb.proactive.annotation.PublicAPI;
import org.ow2.proactive.resourcemanager.common.RMState;
import org.ow2.proactive.resourcemanager.common.event.RMEventType;
import org.ow2.proactive.resourcemanager.common.event.RMInitialState;
import org.ow2.proactive.resourcemanager.exception.RMException;


/**
 * An interface Front-End for the Monitoring of the Resource Manager.
 * This interface provides a way for a monitor to ask at
 * Resource Manager to throw events
 * generated by nodes and nodes sources management.
 * 
 * @see org.ow2.proactive.resourcemanager.frontend.RMEventListener
 * @author The ProActive Team
 * @since ProActive Scheduling 0.9
 */
@PublicAPI
public interface RMMonitoring {

    /**
     * ping the RM
     *
     * @return true if the RM is still alive
     */
    @Deprecated
    public boolean isAlive();

    /** Register a new Resource manager listener.
     * Way to a monitor object to ask at RMMonitoring to throw
     * RM events to it.
     * @param listener a listener object which implements {@link RMEventListener}
     * interface.
     * @param events list of wanted events that must be received.
     * @return RMInitialState snapshot of RM's current state : nodes and node sources.
     */
    public RMInitialState addRMEventListener(RMEventListener listener, RMEventType... events);

    /**
     * Removes a listener from RMMonitoring. Only listener itself must call this method
     */
    public void removeRMEventListener() throws RMException;

    /**
     * Gets the current snapshot of the resource manager state providing
     * detailed nodes and node source information.
     *
     * To obtain summary of the resource manager state use {@link ResourceManager}.getState()
     *
     * @return the current state of the resource manager
     */
    public RMInitialState getState();

}
