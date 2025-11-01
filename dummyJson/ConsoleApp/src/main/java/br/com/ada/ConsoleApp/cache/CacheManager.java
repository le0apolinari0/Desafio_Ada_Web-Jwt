package br.com.ada.ConsoleApp.cache;

import br.com.ada.ConsoleApp.exception.AppException;
import br.com.ada.ConsoleApp.exception.ErrorType;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CacheManager {

    private static class CacheEntry {
        private final Object data;
        private final Instant expiryTime;

        CacheEntry(Object data, Instant expiryTime) {
            this.data = data;
            this.expiryTime = expiryTime;
        }

        boolean isExpired() {
            return Instant.now().isAfter(expiryTime);
        }
    }

    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();
    private final ScheduledExecutorService cleanupScheduler = Executors.newScheduledThreadPool(1);
    private final long defaultTtlSeconds;

    public CacheManager(long defaultTtlSeconds) {
        this.defaultTtlSeconds = defaultTtlSeconds;
        startCleanupTask();
    }

    public void put(String key, Object data) {
        put(key, data, defaultTtlSeconds);
    }

    public void put(String key, Object data, long ttlSeconds) {
        try {
            Instant expiryTime = Instant.now().plusSeconds(ttlSeconds);
            cache.put(key, new CacheEntry(data, expiryTime));
            log.debug("Cache put - Key: {}, TTL: {}s", key, ttlSeconds);
        } catch (Exception e) {
            throw new AppException(ErrorType.CACHE_ERROR, "Erro ao adicionar no cache", e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        try {
            CacheEntry entry = cache.get(key);
            if (entry != null && !entry.isExpired()) {
                log.debug("Cache hit - Key: {}", key);
                return (T) entry.data;
            }

            if (entry != null && entry.isExpired()) {
                cache.remove(key);
            }

            log.debug("Cache miss - Key: {}", key);
            return null;
        } catch (Exception e) {
            throw new AppException(ErrorType.CACHE_ERROR, "Erro ao recuperar do cache", e.getMessage(), e);
        }
    }

    public void remove(String key) {
        try {
            cache.remove(key);
            log.debug("Cache remove - Key: {}", key);
        } catch (Exception e) {
            throw new AppException(ErrorType.CACHE_ERROR, "Erro ao remover do cache", e.getMessage(), e);
        }
    }

    public void clear() {
        try {
            cache.clear();
            log.debug("Cache cleared");
        } catch (Exception e) {
            throw new AppException(ErrorType.CACHE_ERROR, "Erro ao limpar cache", e.getMessage(), e);
        }
    }

    private void startCleanupTask() {
        cleanupScheduler.scheduleAtFixedRate(() -> {
            try {
                int initialSize = cache.size();
                cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
                int removed = initialSize - cache.size();
                if (removed > 0) {
                    log.debug("Cache cleanup removed {} expired entries", removed);
                }
            } catch (Exception e) {
                log.error("Erro durante limpeza do cache: {}", e.getMessage());
            }
        }, 1, 1, TimeUnit.MINUTES);
    }

    public void shutdown() {
        cleanupScheduler.shutdown();
        try {
            if (!cleanupScheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                cleanupScheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            cleanupScheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
