import java.util.Set;

interface MyMap<K extends Comparable, V extends Comparable> {
    /** Define the default hash table size. Must be a power of 2 */
    int DEFAULT_INITIAL_CAPACITY = 4;

    /** Define the maximum hash table size. 1 << 30 is same as 2^30 */
    int MAXIMUM_CAPACITY = 1 << 30;

    /** Define default load factor */
    double DEFAULT_MAX_LOAD_FACTOR = 0.75;

    /** Add an entry (key, value) into the map */
    V put(K key, V value);

    /** Remove the entries for the specified key */
    void remove(K key);

    /** Return all values for the specified key in this map */
    Set<V> getAll(K key);

    /** Return the first value that matches the specified key */
    V get(K key);

    /** Return a set of entries in the map */
    Set<Entry<K, V>> entries();

    /** Return a set consisting of the values in this map */
    Set<V> values();

    /** Return a set consisting of the keys in this map */
    Set<K> keys();

    /** Return the number of mappings in this map */
    int size();

    /** Remove all of the entries from this map */
    void clear();

    /** Return true if this map contains no entries */
    default boolean isEmpty() {
        return size() == 0;
    }

    /** Return true if the specified key is in the map */
    default boolean containsKey(K key) {
        return get(key) != null;
    }

    /** Return true if this map contains the specified value */
    boolean containsValue(V value);

    /** Define inner class for Entry */
    class Entry<K, V> {
        private K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return key + ": " + value + " ";
        }
    }
}
