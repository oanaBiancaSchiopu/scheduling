/*
 * ProActive Parallel Suite(TM):
 * The Open Source library for parallel and distributed
 * Workflows & Scheduling, Orchestration, Cloud Automation
 * and Big Data Analysis on Enterprise Grids & Clouds.
 *
 * Copyright (c) 2007 - 2017 ActiveEon
 * Contact: contact@activeeon.com
 *
 * This library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation: version 3 of
 * the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 */
package org.ow2.proactive.scheduler.core;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.ow2.proactive.scheduler.job.InternalJob;
import org.ow2.proactive.scheduler.job.UserIdentificationImpl;

import com.google.common.collect.Lists;


class SubmitHandler implements Runnable {

    static final Logger logger = Logger.getLogger(SchedulingService.class);

    private final List<InternalJob> jobs;

    private final SchedulingService service;

    private final SchedulerFrontendState frontendState;

    private final UserIdentificationImpl ident;

    SubmitHandler(SchedulingService service, InternalJob job, SchedulerFrontendState frontendState,
            UserIdentificationImpl ident) {
        this.service = service;
        this.jobs = Lists.newArrayList(job);
        this.frontendState = frontendState;
        this.ident = ident;
    }

    SubmitHandler(SchedulingService service, List<InternalJob> jobs, SchedulerFrontendState frontendState,
            UserIdentificationImpl ident) {
        this.service = service;
        this.jobs = jobs;
        this.frontendState = frontendState;
        this.ident = ident;
    }

    @Override
    public void run() {
        if (logger.isDebugEnabled()) {
            if (jobs.size() == 1) {
                if (jobs.get(0) != null) {
                    logger.debug("Submitting a new job '" + jobs.get(0).getName() + "'");
                }
            } else {
                logger.debug("Submitting new jobs: " + jobs.stream()
                                                           .filter(job -> job != null)
                                                           .map(job -> job.getName())
                                                           .collect(Collectors.joining(",")));
            }
        }

        for (InternalJob job : jobs) {
            if (job != null) {
                service.getJobs().jobSubmitted(job);
                if (frontendState != null) {
                    frontendState.jobSubmitted(job, ident);
                }
            }
        }

        service.wakeUpSchedulingThread();
    }

}
