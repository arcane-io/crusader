package crusader.multitenant.strategies;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Interface for identifying tenants from a {@link HttpServletRequest} .
 *
 * @author Catalin Manolescu <cc.manolescu@gmail.com>
 */
public interface TenantIdentificationStrategy {
    Object identifyTenant(HttpServletRequest request);
    void initialize(Map<String,String> params);
}
