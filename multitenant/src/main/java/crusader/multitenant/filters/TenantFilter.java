package crusader.multitenant.filters;

import crusader.commons.ReflectionUtils;
import crusader.commons.StringUtil;
import crusader.multitenant.Tenant;
import crusader.multitenant.context.TenantContext;
import crusader.multitenant.context.TenantContextHolder;
import crusader.multitenant.providers.DefaultTenantProvider;
import crusader.multitenant.providers.TenantProvider;
import crusader.multitenant.strategies.TenantIdentificationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Responsible for setting and removing the {@link TenantContext} for the scope of every request. 
 * This filter should be installed before any components that need access to the
 * {@link TenantContext}.
 *
 * @author Catalin Manolescu <cc.manolescu@gmail.com>
 */
public class TenantFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(TenantFilter.class);

    public static final String PARAM_DEFAULT_TENANT = "DEFAULT_TENANT";
    public static final String PARAM_DEFAULT_TENANT_ENABLED = "DEFAULT_TENANT_ENABLED";
    public static final String PARAM_IDENTIFICATION_STRATEGY = "IDENTIFICATION_STRATEGY";
    public static final String PARAM_TENANT_PROVIDER = "TENANT_PROVIDER";

    private String defaultTenant;
    private Boolean defaultTenantEnabled;

    private List<TenantIdentificationStrategy> strategyChain;
    private TenantProvider tenantProvider;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Map<String, String> params = getInitParametersAsMap(filterConfig);

        if (params.containsKey(PARAM_DEFAULT_TENANT)) {
            defaultTenant = StringUtil.stripToNull(params.get(PARAM_DEFAULT_TENANT));
        }

        if (params.containsKey(PARAM_DEFAULT_TENANT_ENABLED)) {
            defaultTenantEnabled = Boolean.parseBoolean(params.get(PARAM_DEFAULT_TENANT_ENABLED));
        }

        if (params.containsKey(PARAM_TENANT_PROVIDER)) {
            String providerClassName = StringUtil.stripToNull(params.get(PARAM_TENANT_PROVIDER));
            if (providerClassName == null) {
                throw new IllegalArgumentException("Tenant provider has not been specified.");
            }
            
            try {
                tenantProvider = (TenantProvider) ReflectionUtils.createInstance(providerClassName);
            } catch (ReflectiveOperationException e) {
                throw new ServletException("Unable to initialise tenant provider.", e);
            } 
        }

        if (params.containsKey(PARAM_IDENTIFICATION_STRATEGY)) {
            try {
                initIdentificationStrategyChain(params);
            } catch (ReflectiveOperationException e) {
                throw new ServletException("Unable to initialise tenant identification strategies.", e);
            }
        }

        validateSettings();
    }

    private void initIdentificationStrategyChain(Map<String, String> filterParams) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, 
            InstantiationException, IllegalAccessException {
        String identificationStrategy = filterParams.get(PARAM_IDENTIFICATION_STRATEGY);
        if (StringUtil.isBlank(identificationStrategy)) {
            return;
        }

        String[] classNames = StringUtil.toStringArray(identificationStrategy, StringUtil.STRING_ARRAY_SEPARATOR);

        List<TenantIdentificationStrategy> strategyList = new ArrayList<TenantIdentificationStrategy>();
        TenantIdentificationStrategy strategy;
        for (String className : classNames) {
            strategy = (TenantIdentificationStrategy) ReflectionUtils.createInstance(className);
            strategy.initialize(filterParams);
            strategyList.add(strategy);
        }

        setStrategyChain(strategyList);
    }

    private void validateSettings() {
        if (isDefaultTenantEnabled()) {
            if (StringUtil.isBlank(getDefaultTenant())) {
                throw new IllegalArgumentException("Default tenant is enabled but no '" + PARAM_DEFAULT_TENANT + "' was provider");
            }
        }

        if (getTenantProvider() == null) {
            setTenantProvider(new DefaultTenantProvider());
        }

        if (!isDefaultTenantEnabled() && strategyChain == null || strategyChain.isEmpty()) {
            throw new IllegalArgumentException("Default tenant is disabled but no '" + PARAM_IDENTIFICATION_STRATEGY + "' was provider");
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        TenantContext tenantContext = getTenantContext(request);
        try {
            TenantContextHolder.setContext(tenantContext);
            chain.doFilter(request, response);
        } finally {
            TenantContextHolder.clearContext();
        }
    }

    @Override
    public void destroy() {

    }

    public String getDefaultTenant() {
        return defaultTenant;
    }

    public void setDefaultTenant(String defaultTenant) {
        this.defaultTenant = defaultTenant;
    }

    public Boolean isDefaultTenantEnabled() {
        return defaultTenantEnabled;
    }

    public void setDefaultTenantEnabled(Boolean defaultTenantEnabled) {
        this.defaultTenantEnabled = defaultTenantEnabled;
    }

    public List<TenantIdentificationStrategy> getStrategyChain() {
        return strategyChain;
    }

    public void setStrategyChain(List<TenantIdentificationStrategy> strategyChain) {
        this.strategyChain = strategyChain;
    }

    public TenantProvider getTenantProvider() {
        return tenantProvider;
    }

    public void setTenantProvider(TenantProvider tenantProvider) {
        this.tenantProvider = tenantProvider;
    }

    /**
     * Initialize tenant context.
     */
    protected TenantContext getTenantContext(ServletRequest request) {
        TenantContext context = TenantContextHolder.getContext();
        context.setTenant(findTenant(request));
        return context;
    }

    /**
     * Determine tenant based on request.
     */
    protected Tenant findTenant(ServletRequest request) {
        List<TenantIdentificationStrategy> strategyChain = getStrategyChain();
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        for (TenantIdentificationStrategy strategy : strategyChain) {
            Object tenantId = strategy.identifyTenant(httpRequest);
            if (tenantId != null) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Found tenant : " + tenantId);
                }
                return tenantProvider.findTenant(tenantId);
            }
        }

        return isDefaultTenantEnabled() ? tenantProvider.findTenant(getDefaultTenant()) : null;
    }

    /**
     * Convenience method to get {@link javax.servlet.FilterConfig} init parameters.
     *
     * @param filterConfig filter configuration
     * @return <code>key</code> <code>value</code> pair of filter initialization parameters
     */
    @SuppressWarnings("unchecked")
    private Map<String, String> getInitParametersAsMap(FilterConfig filterConfig) {
        Map<String, String> parametersMap = new HashMap<String, String>();

        Enumeration<String> params = filterConfig.getInitParameterNames();
        String paramName;

        while (params.hasMoreElements()) {
            paramName = params.nextElement();
            parametersMap.put(paramName.toUpperCase(Locale.US), filterConfig.getInitParameter(paramName));
        }
        return parametersMap;
    }
}
