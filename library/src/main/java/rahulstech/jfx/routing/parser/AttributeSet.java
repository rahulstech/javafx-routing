package rahulstech.jfx.routing.parser;

import rahulstech.jfx.routing.util.StringUtil;

import java.util.*;

@SuppressWarnings("unused")
public class AttributeSet {

    public static final AttributeSet EMPTY = new AttributeSet();

    Map<String,Attribute> set;

    private Iterator<Attribute> iterator;

    public AttributeSet() {}

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

    public void add(Attribute attr) {
        add(getKey(attr.getNamespace(), attr.getName()),attr);
    }

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

    public Attribute get(String name) {
        return get(RouterXmlParser.DEFAULT_NAMESPACE,name);
    }

    public Attribute get(String namespace, String name) {
        if (null==set) {
            return null;
        }
        return set.get(getKey(namespace,name));
    }

    public Attribute getOrDefault(String name, String defaultValue) {
        return getOrDefault(RouterXmlParser.DEFAULT_NAMESPACE,name,defaultValue);
    }

    public Attribute getOrDefault(String namespace, String name, String defaultValue) {
        Attribute attr = get(namespace,name);
        if (null==attr) {
            return new Attribute(namespace,name,defaultValue);
        }
        return attr;
    }

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

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean hasAttribute(String name) {
        return hasAttribute(RouterXmlParser.DEFAULT_NAMESPACE,name);
    }

    public boolean hasAttribute(String namespace, String name) {
        if (null==set) {
            return false;
        }
        String key = getKey(namespace,name);
        return set.containsKey(key);
    }

    public int size() {
        return null==set ? 0 : set.size();
    }

    public boolean isEmpty() {
        return 0==size();
    }

    public void clear() {
        if (null!=set) {
            set.clear();
            set = null;
        }
    }

    public boolean beginIteration() {
        if (null==set ) {
            return false;
        }
        iterator = set.values().iterator();
        return true;
    }

    public boolean iterateNext() {
        checkIteratorOrThrow();
        return iterator.hasNext();
    }

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
