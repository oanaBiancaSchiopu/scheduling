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
package org.ow2.proactive.scheduler.authentication;

import java.io.File;
import java.security.KeyException;
import java.security.PrivateKey;
import java.util.*;

import org.apache.log4j.Logger;
import org.ow2.proactive.authentication.MultiLDAPLoginModule;
import org.ow2.proactive.authentication.crypto.Credentials;
import org.ow2.proactive.scheduler.core.properties.PASchedulerProperties;


/**
 * LDAP login module implementation for scheduling. Extracts LDAP configuration files from
 * scheduler configuration and uses it to authenticate users.
 */
public class SchedulerMultiLDAPLoginModule extends MultiLDAPLoginModule {

    /**
     * Returns Multiple LDAP configuration defined in scheduler configuration file
     *
     * @return a map of (domain_name,configuration_file)
     */
    @Override
    protected Map<String, String> getMultiLDAPConfig() {
        Map<String, String> answer = new HashMap<>();
        List<String> ldapConfigList = PASchedulerProperties.SCHEDULER_MULTI_LDAP_CONFIG.getValueAsList(",");
        for (String config : ldapConfigList) {
            String[] configPair = config.split("\\s*:\\s*");
            if (configPair.length != 2) {
                throw new IllegalArgumentException("invalid multi-ldap configuration string : " + config);
            }
            String domain = configPair[0];
            String ldapFile = configPair[1];
            //test that ldap file path is an absolute path or not
            if (!(new File(ldapFile).isAbsolute())) {
                //file path is relative, so we complete the path with the scheduler home
                ldapFile = PASchedulerProperties.getAbsolutePath(ldapFile);
            }
            answer.put(domain, ldapFile);
        }
        return answer;
    }

    /**
     * Returns login file name from scheduler configuration file
     * Used for authentication fall-back
     * @return login file name from scheduler configuration file
     */
    @Override
    protected String getLoginFileName() {
        return SchedulerJaasConfigUtils.getLoginFileName();
    }

    /**
     * Returns group file name from scheduler configuration file
     * Used for group membership verification fall-back.
     * @return group file name from scheduler configuration file
     */
    @Override
    protected String getGroupFileName() {
        return SchedulerJaasConfigUtils.getGroupFileName();
    }

    /**
     * Returns tenant file name from scheduler configuration file
     *
     * @return tenant file name from scheduler configuration file
     */
    @Override
    protected String getTenantFileName() {
        return SchedulerJaasConfigUtils.getTenantFileName();
    }

    @Override
    protected Set<String> getConfiguredDomains() {
        return SchedulerJaasConfigUtils.getConfiguredDomains();
    }

    @Override
    protected PrivateKey getPrivateKey() throws KeyException {
        return Credentials.getPrivateKey(PASchedulerProperties.getAbsolutePath(PASchedulerProperties.SCHEDULER_AUTH_PRIVKEY_PATH.getValueAsString()));
    }

    @Override
    protected boolean isLegacyPasswordEncryption() {
        return PASchedulerProperties.SCHEDULER_LEGACY_ENCRYPTION.getValueAsBoolean();
    }

    /**
     * Returns logger for authentication
     *
     * @return logger for authentication
     */
    public Logger getLogger() {
        return Logger.getLogger(SchedulerMultiLDAPLoginModule.class);
    }
}
