/*
 * ################################################################
 *
 * ProActive: The Java(TM) library for Parallel, Distributed,
 *            Concurrent computing with Security and Mobility
 *
 * Copyright (C) 1997-2002 INRIA/University of Nice-Sophia Antipolis
 * Contact: proactive-support@inria.fr
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://www.inria.fr/oasis/ProActive/contacts.html
 *  Contributor(s):
 *
 * ################################################################
 */
package nonregressiontest.activeobject.wrapper;

import java.io.IOException;
import java.io.Serializable;

import org.objectweb.proactive.Body;
import org.objectweb.proactive.ProActive;
import org.objectweb.proactive.RunActive;
import org.objectweb.proactive.Service;
import org.objectweb.proactive.core.util.wrapper.BooleanWrapper;
import org.objectweb.proactive.core.util.wrapper.DoubleWrapper;
import org.objectweb.proactive.core.util.wrapper.FloatWrapper;
import org.objectweb.proactive.core.util.wrapper.IntWrapper;
import org.objectweb.proactive.core.util.wrapper.LongWrapper;
import org.objectweb.proactive.core.util.wrapper.StringWrapper;


public class A implements RunActive, Serializable {
    public A() {
    }

    public BooleanWrapper testBooleanWrapper() {
        return new BooleanWrapper(false);
    }

    public DoubleWrapper testDoubleWrapper() {
        return new DoubleWrapper(0);
    }

    public IntWrapper testIntWrapper() {
        return new IntWrapper(0);
    }

    public LongWrapper testLongWrapper() {
        return new LongWrapper(0);
    }

    public StringWrapper testStringWrapper() {
        return new StringWrapper("Alexandre dC is a famous coder");
    }

    public FloatWrapper testFloatWrapper() {
        return new FloatWrapper(0);
    }

    public void terminate() {
        try {
            ProActive.getBodyOnThis().terminate();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void runActivity(Body body) {
        Service service = new Service(body);
        while (body.isActive()) {
            service.blockingServeOldest("terminate");
            return;
        }
    }
}
