package crusader.multitenant.context;


import crusader.multitenant.Tenant;

/**
 * Base implementation of {@link TenantContext}.
 *
 * @author Catalin Manolescu <cc.manolescu@gmail.com>
 */
public class TenantContextImpl implements TenantContext {
    private Tenant tenant;

    public TenantContextImpl() {
        tenant = null;
    }

    public TenantContextImpl(Tenant tenant) {
        this.tenant = tenant;
    }

    @Override
    public Tenant getTenant() {
        return tenant;
    }

    @Override
    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof TenantContext)) {
            return false;
        }

        TenantContext that = (TenantContext) o;

        return !(tenant != null ? !tenant.equals(that.getTenant()) : that.getTenant() != null);

    }

    @Override
    public int hashCode() {
        return tenant != null ? tenant.hashCode() : super.hashCode();
    }

    @Override
    public String toString() {
        return "TenantContextImpl{tenant=" + tenant + '}';
    }
}
