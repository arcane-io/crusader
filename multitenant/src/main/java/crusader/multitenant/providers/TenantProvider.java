package crusader.multitenant.providers;

import crusader.multitenant.Tenant;

/**
 * Interface for providing a tenant for a given identity.
 *
 * @author Catalin Manolescu <cc.manolescu@gmail.com>
 */
public interface TenantProvider {
    /**
     * Find a tenant based on the given identity.
     *
     * @param identity the identity of the tenant
     * @return a tenant for the given identity or <code>null</code>
     */
    Tenant findTenant(Object identity);
}
