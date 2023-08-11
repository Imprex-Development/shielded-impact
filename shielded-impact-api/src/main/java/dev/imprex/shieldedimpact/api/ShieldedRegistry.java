package dev.imprex.shieldedimpact.api;

import java.util.List;

public interface ShieldedRegistry {

	ShieldedReference register(Class<? extends Shielded> shieldClass);

	void unregister(ShieldedReference reference);

	boolean isRegistered(Class<? extends Shielded> shieldClass);

	boolean isRegistered(ShieldedReference reference);

	ShieldedReference getReference(String name);

	ShieldedReference getReference(Class<? extends Shielded> shieldClass);

	List<ShieldedReference> getReferences();
}
