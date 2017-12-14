package palisades.lakes.dynafun.java;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import clojure.lang.IFn;

/** minimal immutable lookup table with linear search.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2017-12-13
 */

@SuppressWarnings("unchecked")
public final class ClassCache1 {

  private final Class[] classes0;
  private final IFn[] methods;
  /** When there are too many methods for linear search. */
  private final Map<Class,IFn> nonlinear;

  //--------------------------------------------------------------

  public final IFn get (final Class c) {
    for (int i=0;i<classes0.length;i++) {
      if (Objects.equals(c,classes0[i])) { return methods[i]; } }
    return nonlinear.get(c); }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------
  /** private because keeps unsafe references to mutable args.
   */

  private ClassCache1 (final Class[] ks, 
                       final IFn[] vs,
                       final Map m) {
    classes0 = ks;
    methods = vs; 
    nonlinear = m; }

  public static final ClassCache1 empty () {
    return new ClassCache1(
      new Class[0], 
      new IFn[0],
      Collections.emptyMap()); }

  //--------------------------------------------------------------

  private final int index (final Class c) {
    for (int i=0;i<classes0.length;i++) {
      if (Objects.equals(c,classes0[i])) { return i; } }
    return -1; }

  //--------------------------------------------------------------

  private final ClassCache1 set (final int i0,
                                 final Class c,
                                 final IFn m) {
    final Class[] cs = Util.copy(classes0);
    final IFn[] ms = Util.copy(methods);
    cs[i0] = c;
    ms[i0] = m;
    return new ClassCache1(cs,ms,nonlinear); }

  //--------------------------------------------------------------

  public final ClassCache1 assoc (final Class c,
                                  final IFn m) {
    final int i0 = index(c);
    if (0 <= i0) { return set(i0,c,m); }

    return new ClassCache1(
      Util.append(classes0,c),
      Util.append(methods,m),
      nonlinear); }

  //--------------------------------------------------------------
}
