package palisades.lakes.dynafun.java;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import clojure.lang.IFn;
import clojure.lang.IObj;
import clojure.lang.IPersistentMap;
import clojure.lang.ISeq;
import clojure.lang.PersistentArrayMap;
import palisades.lakes.dynafun.java.signature.Signature2;
import palisades.lakes.dynafun.java.signature.Signature3;
import palisades.lakes.dynafun.java.signature.SignatureN;
import palisades.lakes.dynafun.java.signature.Signatures;

/** Dynamic functions whose methods are all arity 1.
 *
 * @author palisades dot lakes at gmail dot com
 * @since 2017-09-03
 * @version 2017-09-22
 */

@SuppressWarnings("unchecked")
public final class DynaFun implements IFn, IObj {

  private final String _name;
  private final IPersistentMap _meta;

  private final Map<Class,IFn> methodTable;

  private final Map<Class,Set> preferTable;

  // cache by arity
  private volatile ClassCache1 cache1;
  private volatile ClassCache2 cache2;
  private volatile ClassCache3 cache3;
  private volatile SignatureNCache cacheN;

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------
  // private because it doesn't copy the input maps.

  private DynaFun (final String n,
                   final IPersistentMap m,
                   final Map mTable,
                   final Map pTable) {
    assert (null != n) && (! n.isEmpty());
    _name = n;
    _meta = m;
    methodTable = mTable;
    preferTable = pTable;
    cache1 = ClassCache1.empty(); 
    cache2 = ClassCache2.empty(); 
    cache3 = ClassCache3.empty(); }


  public static final DynaFun empty (final String name) {
    return new DynaFun(
      name,
      PersistentArrayMap.EMPTY,
      Collections.emptyMap(),
      Collections.emptyMap()); }

  //--------------------------------------------------------------
  // IObj interface
  //--------------------------------------------------------------

  @Override
  public final IPersistentMap meta () { return _meta; }

  @Override
  public final IObj withMeta (final IPersistentMap m) {
    return 
      new DynaFun(
        _name,
        m,
        methodTable,
        preferTable); }

  //--------------------------------------------------------------
  // DynaFun 'interface'
  //--------------------------------------------------------------

  public final DynaFun addMethod (final Object signature,
                                  final IFn method) {
    return 
      new DynaFun(
        _name,
        _meta,
        Util.assoc(methodTable,signature,method),
        preferTable); }

  //--------------------------------------------------------------

  private final boolean prefers (final Object x, 
                                 final Object y) {

    final Set xprefs = preferTable.get(x);

    if (xprefs != null) {
      // is there an explicit prefer-method entry for (x,y)?
      if (xprefs.contains(y)) { return true; }
      // transitive closure of prefer-method relation
      // is x preferred to anything that is preferred to y?
      for (final Object xx : xprefs) {
        if (prefers(xx,y)) { return true; } } }

    // For multi-arity dispatch functions, we need to check the
    // keys of the preferTable.
    for (final Object k : preferTable.keySet()) {
      if ((!x.equals(k)) 
        && Signatures.isAssignableFrom(k,x) 
        && prefers(k,y)) { 
        return true; } }

    return false; }

  //--------------------------------------------------------------

  public final DynaFun preferMethod (final Object x,
                                     final Object y) {
    if (prefers(y,x)) { 
      throw new IllegalStateException(
        String.format(
          "Preference conflict in multimethod '%s':" + 
            "%s is already preferred to %s",
            _name,y,x)); }
    return 
      new DynaFun(
        _name,
        _meta,
        methodTable,
        Util.add(preferTable,x,y)); }

  //--------------------------------------------------------------

  private final boolean dominates (final Object x,
                                   final Object y) {
    return prefers(x,y) || Signatures.isAssignableFrom(y,x); }

  //--------------------------------------------------------------
  // arity 1
  //--------------------------------------------------------------

  private final Set updateMinima (final Map.Entry e0,
                                  final Set<Map.Entry> minima) {
    boolean add = true;
    final Set<Map.Entry> updated = new HashSet(minima.size());
    final Object k0 = e0.getKey();
    for (final Map.Entry e : minima) {
      final Object k = e.getKey();
      if (dominates(k,k0)) { add = false; }
      if (! dominates(k0,k)) { updated.add(e); } } 
    if (add) { updated.add(e0); }
    return updated; }
  
  private static final Map.Entry first (final Set<Map.Entry> i) {
    return i.iterator().next(); }
  
  private final IFn findAndCacheBestMethod (final Class k) {
    Set<Map.Entry> minima = new HashSet(); // should be immutable?
    for (final Object o : methodTable.entrySet()) {
      final Map.Entry e = (Map.Entry) o;
      if (Signatures.isAssignableFrom(e.getKey(),k)) {
        minima = updateMinima(e,minima); } } 
     if (minima.isEmpty()) { return null; } 
     else if (1 != minima.size()) {
       throw new IllegalArgumentException(
         String.format(
           "Multiple methods in multimethod '%s' " + 
             "match dispatch value: %s -> %s, " +
             "and none is preferred",
             _name, k, minima));  }
    final IFn method = (IFn) first(minima).getValue();
    cache1 = cache1.assoc(k,method);
    return method; }

  //--------------------------------------------------------------

  private final IFn getMethod (final Class k) {
    final IFn cached = cache1.get(k);
    if (null != cached) { return cached; }
    final IFn method = findAndCacheBestMethod(k); 
    if (method == null) { 
      throw new IllegalArgumentException(
        String.format(
          "No method in multimethod '%s' for signature: %s",_name,
          k)); }
    return method; }

  //--------------------------------------------------------------
  // arity 2
  //--------------------------------------------------------------

  private final IFn findAndCacheBestMethod (final Class k0,
                                            final Class k1) {
    final Signature2 k = new Signature2(k0,k1);
    Map.Entry bestEntry = null;
    for (final Object o : methodTable.entrySet()) {
      final Map.Entry e = (Map.Entry) o;
      if (Signatures.isAssignableFrom(e.getKey(),k)) {
        if ((bestEntry == null)
          || dominates(e.getKey(),bestEntry.getKey())) {
          bestEntry = e; }
        if (!dominates(bestEntry.getKey(),e.getKey())) { 
          throw new IllegalArgumentException(
            String.format(
              "Multiple methods in multimethod '%s' "
                + "match signature value: %s -> %s and %s, "
                + "and neither is preferred",
                _name,
                k,
                e.getKey(),
                bestEntry.getKey())); } } }
    if (null == bestEntry) { return null; }
    final IFn method = (IFn) bestEntry.getValue();
    cache2 = cache2.assoc(k0,k1,method);
    return method; }

  private final IFn getMethod (final Class k0,
                               final Class k1) {
    final IFn cached = cache2.get(k0,k1);
    if (null != cached) { return cached; }
    final IFn method = findAndCacheBestMethod(k0,k1); 
    if (method == null) { 
      throw new IllegalArgumentException(
        String.format(
          "No method in multimethod '%s' for: %s, %s",_name,
          k0,k1)); }
    return method; }

  //--------------------------------------------------------------
  // arity 3
  //--------------------------------------------------------------

  private final IFn findAndCacheBestMethod (final Class k0,
                                            final Class k1,
                                            final Class k2) {
    final Signature3 k = new Signature3(k0,k1,k2);
    Map.Entry bestEntry = null;
    for (final Object o : methodTable.entrySet()) {
      final Map.Entry e = (Map.Entry) o;
      if (Signatures.isAssignableFrom(e.getKey(),k)) {
        if ((bestEntry == null)
          || dominates(e.getKey(),bestEntry.getKey())) {
          bestEntry = e; }
        if (!dominates(bestEntry.getKey(),e.getKey())) { 
          throw new IllegalArgumentException(
            String.format(
              "Multiple methods in multimethod '%s' "
                + "match signature value: %s -> %s and %s, "
                + "and neither is preferred",
                _name,
                k,
                e.getKey(),
                bestEntry.getKey())); } } }
    if (null == bestEntry) { return null; }
    final IFn method = (IFn) bestEntry.getValue();
    cache3 = cache3.assoc(k0,k1,k2,method);
    return method; }

  private final IFn getMethod (final Class k0,
                               final Class k1,
                               final Class k2) {
    final IFn cached = cache3.get(k0,k1,k2);
    if (null != cached) { return cached; }
    final IFn method = findAndCacheBestMethod(k0,k1,k2); 
    if (method == null) { 
      throw new IllegalArgumentException(
        String.format(
          "No method in multimethod '%s' for: %s, %s, %s",_name,
          k0,k1,k2)); }
    return method; }

  //--------------------------------------------------------------
  // arity > 3 via Signature
  //--------------------------------------------------------------

  private final IFn findAndCacheBestMethod (final SignatureN k) {
    Map.Entry bestEntry = null;
    for (final Object o : methodTable.entrySet()) {
      final Map.Entry e = (Map.Entry) o;
      if (Signatures.isAssignableFrom(e.getKey(),k)) {
        if ((bestEntry == null)
          || dominates(e.getKey(),bestEntry.getKey())) {
          bestEntry = e; }
        if (!dominates(bestEntry.getKey(),e.getKey())) { 
          throw new IllegalArgumentException(
            String.format(
              "Multiple methods in multimethod '%s' "
                + "match signature value: %s -> %s and %s, "
                + "and neither is preferred",
                _name,
                k,
                e.getKey(),
                bestEntry.getKey())); } } }
    if (null == bestEntry) { return null; }
    final IFn method = (IFn) bestEntry.getValue();
    cacheN = cacheN.assoc(k,method);
    return method; }

  //--------------------------------------------------------------

  private final IFn getMethod (final SignatureN k) {
    final IFn cached = cacheN.get(k);
    if (null != cached) { return cached; }
    final IFn method = findAndCacheBestMethod(k); 
    if (method == null) { 
      throw new IllegalArgumentException(
        String.format(
          "No method in multimethod '%s' for signature: %s",_name,
          k)); }
    return method; }

  //--------------------------------------------------------------
  // IFn interface
  //--------------------------------------------------------------

  private final UnsupportedOperationException 
  illegalArity (final int i) {
    return
      new UnsupportedOperationException(
        getClass().getSimpleName() + 
        " can't handle " + i + " operands"); }

  @Override
  public final Object invoke () { throw illegalArity(0); }

  @Override
  public final Object invoke (final Object x) {
    return
      getMethod(
        x.getClass())
      .invoke(x); }

  @Override
  public final Object invoke (final Object x0,
                              final Object x1) {
    return
      getMethod(
        x0.getClass(),
        x1.getClass())
      .invoke(x0,x1); }

  @Override
  public final Object invoke (final Object x0,
                              final Object x1,
                              final Object x2) {
    return
      getMethod(
        x0.getClass(),
        x1.getClass(),
        x2.getClass())
      .invoke(
        x0,x1,x2); }

  @Override
  public final Object invoke (final Object x0,
                              final Object x1,
                              final Object x2,
                              final Object x3) {
    final SignatureN k = 
      new SignatureN(
        x0.getClass(),
        x1.getClass(),
        x2.getClass(),
        x3.getClass());
    return
      getMethod(k)
      .invoke(
        x0,x1,x2,x3); }

  @Override
  public final Object invoke (final Object x0,
                              final Object x1,
                              final Object x2,
                              final Object x3,
                              final Object x4) {
    throw illegalArity(5); }

  @Override
  public final Object invoke (final Object x0,
                              final Object x1,
                              final Object x2,
                              final Object x3,
                              final Object x4,
                              final Object x5) {
    throw illegalArity(6); }

  @Override
  public final Object invoke (final Object x0,
                              final Object x1,
                              final Object x2,
                              final Object x3,
                              final Object x4,
                              final Object x5,
                              final Object x6) {
    throw illegalArity(7); }

  @Override
  public final Object invoke (final Object x0,
                              final Object x1,
                              final Object x2,
                              final Object x3,
                              final Object x4,
                              final Object x5,
                              final Object x6,
                              final Object x7) {
    throw illegalArity(8); }

  @Override
  public final Object invoke (final Object x0,
                              final Object x1,
                              final Object x2,
                              final Object x3,
                              final Object x4,
                              final Object x5,
                              final Object x6,
                              final Object x7,
                              final Object x8) {
    throw illegalArity(9); }

  @Override
  public final Object invoke (final Object x0,
                              final Object x1,
                              final Object x2,
                              final Object x3,
                              final Object x4,
                              final Object x5,
                              final Object x6,
                              final Object x7,
                              final Object x8,
                              final Object x9) {
    throw illegalArity(10); }

  @Override
  public final Object invoke (final Object x0,
                              final Object x1,
                              final Object x2,
                              final Object x3,
                              final Object x4,
                              final Object x5,
                              final Object x6,
                              final Object x7,
                              final Object x8,
                              final Object x9,
                              final Object x10) {
    throw illegalArity(11); }

  @Override
  public final Object invoke (final Object x0,
                              final Object x1,
                              final Object x2,
                              final Object x3,
                              final Object x4,
                              final Object x5,
                              final Object x6,
                              final Object x7,
                              final Object x8,
                              final Object x9,
                              final Object x10,
                              final Object x11) {
    throw illegalArity(12); }

  @Override
  public final Object invoke (final Object x0,
                              final Object x1,
                              final Object x2,
                              final Object x3,
                              final Object x4,
                              final Object x5,
                              final Object x6,
                              final Object x7,
                              final Object x8,
                              final Object x9,
                              final Object x10,
                              final Object x11,
                              final Object x12) {
    throw illegalArity(13); }

  @Override
  public final Object invoke (final Object x0,
                              final Object x1,
                              final Object x2,
                              final Object x3,
                              final Object x4,
                              final Object x5,
                              final Object x6,
                              final Object x7,
                              final Object x8,
                              final Object x9,
                              final Object x10,
                              final Object x11,
                              final Object x12,
                              final Object x13) {
    throw illegalArity(14); }

  @Override
  public final Object invoke (final Object x0,
                              final Object x1,
                              final Object x2,
                              final Object x3,
                              final Object x4,
                              final Object x5,
                              final Object x6,
                              final Object x7,
                              final Object x8,
                              final Object x9,
                              final Object x10,
                              final Object x11,
                              final Object x12,
                              final Object x13,
                              final Object x14) {
    throw illegalArity(15); }

  @Override
  public final Object invoke (final Object x0,
                              final Object x1,
                              final Object x2,
                              final Object x3,
                              final Object x4,
                              final Object x5,
                              final Object x6,
                              final Object x7,
                              final Object x8,
                              final Object x9,
                              final Object x10,
                              final Object x11,
                              final Object x12,
                              final Object x13,
                              final Object x14,
                              final Object x15) {
    throw illegalArity(16); }

  @Override
  public final Object invoke (final Object x0,
                              final Object x1,
                              final Object x2,
                              final Object x3,
                              final Object x4,
                              final Object x5,
                              final Object x6,
                              final Object x7,
                              final Object x8,
                              final Object x9,
                              final Object x10,
                              final Object x11,
                              final Object x12,
                              final Object x13,
                              final Object x14,
                              final Object x15,
                              final Object x16) {
    throw illegalArity(17); }

  @Override
  public final Object invoke (final Object x0,
                              final Object x1,
                              final Object x2,
                              final Object x3,
                              final Object x4,
                              final Object x5,
                              final Object x6,
                              final Object x7,
                              final Object x8,
                              final Object x9,
                              final Object x10,
                              final Object x11,
                              final Object x12,
                              final Object x13,
                              final Object x14,
                              final Object x15,
                              final Object x16,
                              final Object x17) {
    throw illegalArity(18); }

  @Override
  public final Object invoke (final Object x0,
                              final Object x1,
                              final Object x2,
                              final Object x3,
                              final Object x4,
                              final Object x5,
                              final Object x6,
                              final Object x7,
                              final Object x8,
                              final Object x9,
                              final Object x10,
                              final Object x11,
                              final Object x12,
                              final Object x13,
                              final Object x14,
                              final Object x15,
                              final Object x16,
                              final Object x17,
                              final Object x18) {
    throw illegalArity(19); }

  @Override
  public final Object invoke (final Object x0,
                              final Object x1,
                              final Object x2,
                              final Object x3,
                              final Object x4,
                              final Object x5,
                              final Object x6,
                              final Object x7,
                              final Object x8,
                              final Object x9,
                              final Object x10,
                              final Object x11,
                              final Object x12,
                              final Object x13,
                              final Object x14,
                              final Object x15,
                              final Object x16,
                              final Object x17,
                              final Object x18,
                              final Object x19) {
    throw illegalArity(20); }

  @Override
  public final Object invoke (final Object x0,
                              final Object x1,
                              final Object x2,
                              final Object x3,
                              final Object x4,
                              final Object x5,
                              final Object x6,
                              final Object x7,
                              final Object x8,
                              final Object x9,
                              final Object x10,
                              final Object x11,
                              final Object x12,
                              final Object x13,
                              final Object x14,
                              final Object x15,
                              final Object x16,
                              final Object x17,
                              final Object x18,
                              final Object x19,
                              final Object... xs) {
    throw illegalArity(20 + xs.length); } 
  //--------------------------------------------------------------

  @Override
  public final Object call () throws Exception { throw illegalArity(0); }

  @Override
  public final void run () { throw illegalArity(0);  }

  @Override
  public final Object applyTo (final ISeq xs) {
    if (1 == xs.count()) { return invoke(xs.first()); } 
    throw illegalArity(xs.count()); }

  //--------------------------------------------------------------
}
