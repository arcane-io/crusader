package crusader.multitenant.context;

/**
 * A <code>ThreadLocal</code>-based implementation of {@link TenantContextHolderStrategy}.
 *
 * @author Catalin Manolescu <cc.manolescu@gmail.com>
 */
public class ThreadLocalTenantContextHolderStrategy implements TenantContextHolderStrategy {
    private static ThreadLocal<TenantContext> contextHolder = new ThreadLocal<TenantContext>();

    @Override
    public void clearContext() {
        contextHolder.remove();
    }

    @Override
    public TenantContext getContext() {
        TenantContext context = contextHolder.get();

        if (context == null) {
            context = createEmptyContext();
            contextHolder.set(context);
        }

        return contextHolder.get();
    }

    @Override
    public void setContext(TenantContext context) {
        if (context == null) {
            throw new IllegalArgumentException("Cannot set 'null' tenant context.");
        }
        contextHolder.set(context);
    }

    public TenantContext createEmptyContext() {
        return new TenantContextImpl();
    }
}
