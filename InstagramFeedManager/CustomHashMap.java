import java.util.ArrayList;

public class CustomHashMap<K, V> {
    private static class Entry<K, V> {
        K key;
        V value;
        boolean isActive; // Used to check if an entry is active (not removed)

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.isActive = true;
        }
    }

    private Entry<K, V>[] table; // Array of entry objects
    private int size; // Current size (number of active entries)
    private static final int INITIAL_CAPACITY = 16; // Initial capacity of the hash table

    public CustomHashMap() {
        table = new Entry[INITIAL_CAPACITY]; // Initialize the table with the initial capacity
    }

    // Hash function to compute the index for a key
    private int hash(K key) {
        return (key.hashCode() & 0x7fffffff) % table.length;
    }

    // Adds a new key-value pair to the map or updates the value of an existing key
    public V put(K key, V value) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        if (size >= 0.50 * table.length) { // Resize the table if the load factor is >= 0.5
            resize(2 * table.length);
        }
        int index = probe(key);

        if (table[index] == null) { // Insert new entry if the spot is empty
            table[index] = new Entry<>(key, value);
            size++;
            return null;
        } else { // Update existing entry
            V oldValue = table[index].value;
            table[index].value = value;
            return oldValue;
        }
    }

    // Retrieves the value associated with the given key
    public V get(K key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        int index = probe(key);
        if (table[index] != null && table[index].isActive) {
            return table[index].value;
        }
        return null;
    }

    // Removes the entry associated with the given key
    public V remove(K key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        int index = probe(key);
        if (table[index] != null && table[index].isActive) {
            V oldValue = table[index].value;
            table[index].isActive = false;
            size--;
            return oldValue;
        }
        return null;
    }

    // Checks if the key exists in the map
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    // Clears the hash table
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                table[i] = null;
            }
        }
        size = 0;
    }

    // Probes the hash table to find the index of the key or the next free slot
    private int probe(K key) {
        int index = hash(key);
        while (table[index] != null && !table[index].key.equals(key)) {
            index = (index + 1) % table.length; // Linear probing
        }
        return index;
    }

    // Resizes the table to a new capacity and rehashes all active entries
    private void resize(int newCapacity) {
        Entry<K, V>[] oldTable = table;
        table = new Entry[newCapacity];
        size = 0;
        for (Entry<K, V> entry : oldTable) {
            if (entry != null && entry.isActive) {
                put(entry.key, entry.value); // Re-insert active entries into the new table
            }
        }
    }

    // Returns the number of active entries in the map
    public int size() {
        return size;
    }

    // Retrieves a list of all active keys
    public ArrayList<K> keys() {
        ArrayList<K> keyList = new ArrayList<>();
        for (Entry<K, V> entry : table) {
            if (entry != null && entry.isActive) {
                keyList.add(entry.key); // Only add keys of active entries
            }
        }
        return keyList;
    }
}
