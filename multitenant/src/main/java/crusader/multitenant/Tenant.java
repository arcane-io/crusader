package crusader.multitenant;

import java.io.Serializable;

/**
 * Identifies a tenant in a multi-tenant architecture.
 *
 * @author Catalin Manolescu <cc.manolescu@gmail.com>
 */
public interface Tenant extends Serializable {
    Object getIdentity();
}
