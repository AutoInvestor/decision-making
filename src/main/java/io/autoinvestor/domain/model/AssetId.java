package io.autoinvestor.domain.model;

import io.autoinvestor.domain.Id;

public class AssetId extends Id {
    AssetId(String id) {
        super(id);
    }

    public static AssetId generate() {
        return new AssetId(generateId());
    }

    public static AssetId of(String id) {
        return new AssetId(id);
    }
}
