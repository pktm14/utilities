package edu.scripps.yates.utilities.cache;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import gnu.trove.set.hash.THashSet;

public abstract class AbstractCache<T, K> implements Cache<T, K> {
	protected Map<K, T> map;
	protected final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	public AbstractCache() {
		map = createMap();
	}

	protected abstract Map<K, T> createMap();

	@Override
	public void addtoCache(T t, K key) {
		if (t == null) {
			return;
		}
		WriteLock writeLock = lock.writeLock();
		try {
			writeLock.lock();
			K processedKey = processKey(key);
			map.put(processedKey, t);
		} finally {
			writeLock.unlock();
		}

	}

	@Override
	public void addtoCache(Map<K, T> map2) {
		if (map2 == null) {
			return;
		}
		WriteLock writeLock = lock.writeLock();
		try {
			writeLock.lock();
			map.putAll(map2);
		} finally {
			writeLock.unlock();
		}

	}

	@Override
	public T getFromCache(K key) {
		ReadLock readLock = lock.readLock();
		try {
			readLock.lock();
			K processedKey = processKey(key);
			return map.get(processedKey);
		} finally {
			readLock.unlock();
		}
	}

	@Override
	public Set<T> getFromCache(Collection<K> keys) {
		ReadLock readLock = lock.readLock();
		try {
			readLock.lock();
			Set<T> ret = new THashSet<T>();
			for (K key : keys) {
				K processedKey = processKey(key);
				if (contains(processedKey)) {
					ret.add(getFromCache(processedKey));
				}
			}
			return ret;
		} finally {
			readLock.unlock();
		}
	}

	@Override
	public boolean contains(K key) {
		ReadLock readLock = lock.readLock();
		try {
			readLock.lock();
			K processedKey = processKey(key);
			return map.containsKey(processedKey);
		} finally {
			readLock.unlock();
		}
	}

	@Override
	public T removeFromCache(K key) {
		WriteLock writeLock = lock.writeLock();
		try {
			writeLock.lock();
			K processedKey = processKey(key);
			return map.remove(processedKey);
		} finally {
			writeLock.unlock();
		}
	}

	@Override
	public boolean containsAll(Collection<K> keys) {
		ReadLock readLock = lock.readLock();
		try {
			readLock.lock();
			for (K key : keys) {
				K processedKey = processKey(key);
				if (!contains(processedKey))
					return false;
			}
			return true;
		} finally {
			readLock.unlock();
		}
	}

	@Override
	public K processKey(K key) {
		return key;
	}

	@Override
	public void clearCache() {
		WriteLock writeLock = lock.writeLock();
		try {
			writeLock.lock();
			map.clear();
		} finally {
			writeLock.unlock();
		}
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

}
