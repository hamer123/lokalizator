package com.pw.lokalizator.service;

/**
 * Created by wereckip on 23.08.2016.
 *
 * @param <UUID> identytyfikator zasobu
 * @param <ResourceType> typ w jakim zwracany jest zasob
 */
public interface ResourceService<UUID, ResourceType> {
    ResourceType create(UUID uuid, ResourceType resourceType);
    ResourceType update(UUID uuid, ResourceType resourceType);
    void remove(UUID uuid);
    ResourceType find(UUID uuid);
}
