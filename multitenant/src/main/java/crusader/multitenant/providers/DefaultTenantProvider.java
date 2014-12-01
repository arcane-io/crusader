package crusader.multitenant.providers;

import crusader.multitenant.Tenant;

/**
 * Provider for the default tenant.
 *
 * @author Catalin Manolescu <cc.manolescu@gmail.com>
 */
public class DefaultTenantProvider implements TenantProvider {

    @Override
    public Tenant findTenant(Object identity) {
        return new SimpleTenant(identity);
    }
}
