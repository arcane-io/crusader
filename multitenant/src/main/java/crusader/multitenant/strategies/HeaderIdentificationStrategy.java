package crusader.multitenant.strategies;

import crusader.commons.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Implementation of {@link TenantIdentificationStrategy} that looks for
 * tenant identity in the request header.
 *
 * @author Catalin Manolescu <cc.manolescu@gmail.com>
 */
public class HeaderIdentificationStrategy implements TenantIdentificationStrategy {
    public static final String PARAM_HEADER_NAME = "HEADER_NAME";

    private String headerName;

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        if (StringUtil.isBlank(headerName)) {
            throw new IllegalArgumentException("Header name must not be null.");
        }
        this.headerName = headerName;
    }

    @Override
    public Object identifyTenant(HttpServletRequest request) {
        return request.getHeader(getHeaderName());
    }

    @Override
    public void initialize(Map<String, String> params) {
        String header = "";

        if (params != null && params.containsKey(PARAM_HEADER_NAME)) {
            header = params.get(PARAM_HEADER_NAME);
        }

        setHeaderName(header.trim());
    }
}
