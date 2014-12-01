package crusader.multitenant.strategies;

import crusader.commons.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link TenantIdentificationStrategy} that matches the tenant identity
 * based on configuration.
 *
 * @author Catalin Manolescu <cc.manolescu@gmail.com>
 */
public class HostURLIdentificationStrategy implements TenantIdentificationStrategy {
    public static final String PARAM_URL_CONFIG_MAP = "URL_HOST_CONFIG_MAP";
    public static final String HEADER_HOST = "host";

    private static Logger logger = LoggerFactory.getLogger(HostURLIdentificationStrategy.class);

    private Map<String, String> configuration;

    public Map<String, String> getConfiguration() {
        return configuration == null ? null : new HashMap<String, String>(configuration);
    }

    public void setConfiguration(Map<String, String> configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("Configuration must not be null.");
        }
        this.configuration = configuration;
    }

    @Override
    public Object identifyTenant(HttpServletRequest request) {
        String hostname = request.getHeader(HEADER_HOST);
        if (logger.isDebugEnabled()) {
            logger.debug("Identify tenant for hostname : " + hostname + " | origin : " + request.getHeader("origin"));
        }

        if (configuration.containsKey(hostname)) {
            return configuration.get(hostname);
        }

        return null;
    }

    /**
     * <code>PARAM_URL_CONFIG_MAP</code> format <code>host1=tenant1,host2=tenant2,...</code> .
     */
    @Override
    public void initialize(Map<String, String> params) {
        Map<String,String> config = null;

        if (params.containsKey(PARAM_URL_CONFIG_MAP)) {
            config = StringUtil.getKeyValuePair(params.get(PARAM_URL_CONFIG_MAP), StringUtil.STRING_ARRAY_SEPARATOR);
        }

        setConfiguration(config == null ? Collections.<String, String>emptyMap() : config);
    }
}
