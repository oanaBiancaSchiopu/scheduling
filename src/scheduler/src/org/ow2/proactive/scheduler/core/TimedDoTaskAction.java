/*
 * ################################################################
 *
 * ProActive: The Java(TM) library for Parallel, Distributed,
 *            Concurrent computing with Security and Mobility
 *
 * Copyright (C) 1997-2009 INRIA/University of
 * 						   Nice-Sophia Antipolis/ActiveEon
 * Contact: proactive@ow2.org
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
 * If needed, contact us to obtain a release under GPL Version 2.
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://proactive.inria.fr/team_members.htm
 *  Contributor(s): ActiveEon Team - http://www.activeeon.com
 *
 * ################################################################
 * $ACTIVEEON_INITIAL_DEV$
 */
package org.ow2.proactive.scheduler.core;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;
import org.objectweb.proactive.core.util.log.ProActiveLogger;
import org.ow2.proactive.scheduler.common.task.TaskResult;
import org.ow2.proactive.scheduler.task.internal.InternalTask;
import org.ow2.proactive.scheduler.task.launcher.TaskLauncher;
import org.ow2.proactive.scheduler.util.SchedulerDevLoggers;
import org.ow2.proactive.threading.CallableWithTimeoutAction;


/**
 * TimedDoTaskAction is used to start the task execution in parallel.
 *
 * @author The ProActive Team
 * @since ProActive Scheduling 2.0
 */
public class TimedDoTaskAction implements CallableWithTimeoutAction<TaskResult> {

    private static final Logger logger_dev = ProActiveLogger.getLogger(SchedulerDevLoggers.SCHEDULE);

    private AtomicBoolean timeoutCalled = new AtomicBoolean(false);
    private InternalTask task;
    private TaskLauncher launcher;
    private SchedulerCore coreStub;
    private TaskResult[] parameters;

    /**
     * Create a new instance of TimedDoTaskAction
     *
     * @param task the internal task
     * @param launcher the launcher of the task
     * @param coreStub the stub on SchedulerCore
     * @param parameters the parameters to be given to the task
     */
    public TimedDoTaskAction(InternalTask task, TaskLauncher launcher, SchedulerCore coreStub,
            TaskResult[] parameters) {
        this.task = task;
        this.launcher = launcher;
        this.coreStub = coreStub;
        this.parameters = parameters;
    }

    /**
     * {@inheritDoc}
     */
    public TaskResult call() throws Exception {
        try {
            //if a task has been launched
            if (launcher != null) {
                //try launch the task
                TaskResult tr = launcher.doTask(coreStub, task.getExecutableContainer(), parameters);
                //check if timeout occurs
                if (timeoutCalled.get()) {
                    //return null if timeout occurs (task may have to be restarted later)
                    logger_dev.info("Task '" + task.getId() + "' has timed out");
                    return null;
                } else {
                    //return task result if everything was OK : normal behavior
                    return tr;
                }
            } else {
                //return null if launcher was null (should never append)
                logger_dev.warn("Launcher was null");
                return null;
            }
        } catch (Exception e) {
            //return null if something wrong occurs during task deployment
            logger_dev.warn("DoTask had an exception : " + e.getMessage());
            logger_dev.debug("StackTrace :", e);
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void timeoutAction() {
        logger_dev.debug("Task '" + task.getId() + "' timeout action called");
        timeoutCalled.set(true);
    }

}
