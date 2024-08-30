package rahulstech.jfx.routing.parser;

import rahulstech.jfx.routing.util.StringUtil;

import java.util.*;

/**
 * The {@code AttributeSet} class represents a collection of {@link Attribute} objects,
 * organized by their namespace and name. It supports operations for adding, retrieving,
 * and iterating over attributes, as well as handling attributes with specific prefixes.
 *
 * @author Rahul Bagchi
 * @since 1.0
 */
public class AttributeSet {

    /**
     * An empty instance of {@code AttributeSet} that can be used to avoid creating multiple
     * empty instances.
     */
    public static final AttributeSet EMPTY = new AttributeSet();

    Map<String,Attribute> set;

    private Iterator<Attribute> iterator;

    /**
     * Constructs an empty {@code AttributeSet}.
     */
    public AttributeSet() {}

    /**
     * Adds all attributes from another {@code AttributeSet} to this one,
     * prefixing their keys with the provided prefix.
     *
     * @param prefix the prefix to add to each key.
     * @param attrs the {@code AttributeSet} to copy attributes from.
     * @throws IllegalArgumentException if the prefix is empty.
     */
    public void addWithPrefix(String prefix, AttributeSet attrs) {
        if (StringUtil.isEmpty(prefix)) {
            throw new IllegalArgumentException("prefix can not be empty");
        }
        if (!attrs.isEmpty()) {
            attrs.set.forEach((key,attr)->{
                String prefixedKey = getKey(prefix,key);
                add(prefixedKey,attr);
            });
        }
    }

    /**
     * Adds an attribute to this {@code AttributeSet}.
     *
     * @param attr the attribute to add.
     */
    public void add(Attribute attr) {
        add(getKey(attr.getNamespace(), attr.getName()),attr);
    }

    /**
     * Adds a collection of attributes to this {@code AttributeSet}.
     *
     * @param attrs the collection of attributes to add.
     */
    public void addAll(Collection<Attribute> attrs) {
        attrs.forEach(this::add);
    }


    private  void add(String key, Attribute attr) {
        if (null==set) {
            set = new HashMap<>();
        }
        iterator = null;
        set.put(key,attr);
    }

    /**
     * Retrieves an attribute by name from the default namespace.
     *
     * @param name the name of the attribute.
     * @return the attribute, or {@code null} if not found.
     */
    public Attribute get(String name) {
        return get(RouterXmlParser.DEFAULT_NAMESPACE,name);
    }

    /**
     * Retrieves an attribute by namespace and name.
     *
     * @param namespace the namespace of the attribute.
     * @param name the name of the attribute.
     * @return the attribute, or {@code null} if not found.
     */
    public Attribute get(String namespace, String name) {
        if (null==set) {
            return null;
        }
        return set.get(getKey(namespace,name));
    }

    /**
     * Retrieves an attribute by name from the default namespace,
     * or returns a new attribute with a default value if not found.
     *
     * @param name the name of the attribute.
     * @param defaultValue the default value to return if the attribute is not found.
     * @return the attribute, or a new attribute with the default value.
     */
    public Attribute getOrDefault(String name, String defaultValue) {
        return getOrDefault(RouterXmlParser.DEFAULT_NAMESPACE,name,defaultValue);
    }

    /**
     * Retrieves an attribute by namespace and name,
     * or returns a new attribute with a default value if not found.
     *
     * @param namespace the namespace of the attribute.
     * @param name the name of the attribute.
     * @param defaultValue the default value to return if the attribute is not found.
     * @return the attribute, or a new attribute with the default value.
     */
    public Attribute getOrDefault(String namespace, String name, String defaultValue) {
        Attribute attr = get(namespace,name);
        if (null==attr) {
            return new Attribute(namespace,name,defaultValue);
        }
        return attr;
    }

    /**
     * Retrieves a subset of attributes that have keys with the specified prefix.
     *
     * @param prefix the prefix to filter attributes by.
     * @return a new {@code AttributeSet} containing the matching attributes,
     * or {@link AttributeSet#EMPTY} if no matches are found.
     */
    public AttributeSet getAttributeSetWithPrefix(String prefix) {
        if (null==set) {
            return null;
        }
        AttributeSet subset = new AttributeSet();
        set.forEach((key,attr)->{
            if (hasPrefix(key,prefix)) {
                subset.add(attr);
            }
        });
        return subset.isEmpty() ? EMPTY : subset;
    }

    /**
     * Checks if an attribute exists by name in the default namespace.
     *
     * @param name the name of the attribute.
     * @return {@code true} if the attribute exists, {@code false} otherwise.
     */
    public boolean hasAttribute(String name) {
        return hasAttribute(RouterXmlParser.DEFAULT_NAMESPACE,name);
    }

    /**
     * Checks if an attribute exists by namespace and name.
     *
     * @param namespace the namespace of the attribute.
     * @param name the name of the attribute.
     * @return {@code true} if the attribute exists, {@code false} otherwise.
     */
    public boolean hasAttribute(String namespace, String name) {
        if (null==set) {
            return false;
        }
        String key = getKey(namespace,name);
        return set.containsKey(key);
    }

    /**
     * Returns the number of attributes in this {@code AttributeSet}.
     *
     * @return the number of attributes.
     */
    public int size() {
        return null==set ? 0 : set.size();
    }

    /**
     * Checks if this {@code AttributeSet} is empty.
     *
     * @return {@code true} if empty, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return 0==size();
    }

    /**
     * Clears all attributes from this {@code AttributeSet}.
     */
    public void clear() {
        if (null!=set) {
            set.clear();
            set = null;
        }
    }

    /**
     * Prepares this {@code AttributeSet} for iteration over its attributes.
     *
     * @return {@code true} if iteration is possible, {@code false} if there are no attributes.
     */
    public boolean beginIteration() {
        if (null==set ) {
            return false;
        }
        iterator = set.values().iterator();
        return true;
    }

    /**
     * Advances to the next attribute in the iteration.
     *
     * @return {@code true} if there is a next attribute, {@code false} otherwise.
     * @throws IllegalStateException if iteration has not been started or was invalidated by a modification.
     */
    public boolean iterateNext() {
        checkIteratorOrThrow();
        return iterator.hasNext();
    }

    /**
     * Retrieves the next attribute in the iteration.
     *
     * @return the next attribute.
     * @throws NoSuchElementException if there are no more elements.
     * @throws IllegalStateException if iteration has not been started or was invalidated by a modification.
     */
    public Attribute getNext() {
        checkIteratorOrThrow();
        try {
            return iterator.next();
        }
        catch (NoSuchElementException ex) {
            throw new NoSuchElementException("iterator do not have more element, use iterateNext() to check availability of next element");
        }
    }

    @Override
    public String toString() {
        return null==set ? "[]" : set.toString();
    }

    private void checkIteratorOrThrow() {
        if (null==iterator) {
            throw new IllegalStateException("iterator not created; " +
                    "either beginIteration() not called or any modification operation occurred since last beginIteration() called");
        }
    }

    String getKey(Attribute attr) {
        return getKey(attr.getNamespace(), attr.getName());
    }

    String getKey(String prefix, String name) {
        return "["+prefix+"]:"+name;
    }

    boolean hasPrefix(String key, String prefix) {
        return key.indexOf("["+prefix+"]")==0;
    }
}
