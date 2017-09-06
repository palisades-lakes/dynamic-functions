package palisades.lakes.dynafun.java;

import java.util.Collections;
import java.util.Map;

import clojure.lang.IFn;
import palisades.lakes.dynafun.java.signature.Signature2;

/** minimal immutable lookup table with nested linear search.
 *
 * @author palisades dot lakes at gmail dot com
 * @since 2017-09-03
 * @version 2017-09-05
 */

@SuppressWarnings("unchecked")
public final class ClassCache2 {

  private final Class[] classes0;
  private final Class[][] classes1;
  private final IFn[][] methods;

  /** When there are too many methods for nested linear search. */
  private final Map<Signature2,IFn> nonlinear;

  //--------------------------------------------------------------

  public final IFn get (final Class c0,
                        final Class c1) {
    for (int i0=0;i0<classes0.length;i0++) {
      if (c0.equals(classes0[i0])) { 
        final Class[] cs1 = classes1[i0];
        for (int i1=0;i1<cs1.length;i1++) {
          if (c1.equals(cs1[i1])) {
            return methods[i0][i1]; } } } } 
    return nonlinear.get(new Signature2(c0,c1)); }

  //--------------------------------------------------------------

  private ClassCache2 (final Class[] ks0, 
                       final Class[][] ks1, 
                       final IFn[][] ms,
                       final Map m) {
    classes0 = ks0;
    classes1 = ks1;
    methods = ms; 
    nonlinear = m; }

  public static final ClassCache2 empty () {
    return new ClassCache2(
      new Class[0],
      new Class[0][],
      new IFn[0][],
      Collections.emptyMap()); }

  //--------------------------------------------------------------

  private final int index (final Class c0) {
    for (int i0=0;i0<classes0.length;i0++) {
      if (c0.equals(classes0[i0])) { return i0; } }
    return -1; }

  private final int index (final int i0,
                           final Class c1) {
    final Class[] cs1 = classes1[i0];
    for (int i1=0;i1<cs1.length;i1++) {
      if (c1.equals(cs1[i1])) { return i1; } }
    return -1; }

  //--------------------------------------------------------------

  private final ClassCache2 addCell (final Class c0,
                                     final Class c1) {
    return new ClassCache2(
      Util.append(classes0,c0),
      Util.append(classes1,new Class[] { c1 }),
      Util.append(methods,new IFn[] { null }),
      nonlinear); }

  private final ClassCache2 addCell (final int i0,
                                     final Class c1) {
    return new ClassCache2(
      Util.copy(classes0),
      Util.append(classes1,i0,c1),
      Util.append(methods,i0,null),
      nonlinear); }

  private final ClassCache2 ensureCell (final Class c0,
                                        final Class c1) {
    final int i0 = index(c0);
    if (0 > i0) { return addCell(c0,c1); }
    final int i1 = index(i0,c1);
    if (0 > i1) { return addCell(i0,c1); }
    return this; }

  //--------------------------------------------------------------

  private final ClassCache2 set (final Class c0,
                                 final Class c1,
                                 final IFn m) {
    final int i0 = index(c0);
    assert (0 <= i0);
    final int i1 = index(i0,c1);
    assert (0 <= i1);

    final IFn[][] ms = Util.copy(methods);
    ms[i0][i1] = m;
    // defensive copying; not really necessary
    return new ClassCache2(
      Util.copy(classes0),
      Util.copy(classes1),
      ms,
      nonlinear); }

  //--------------------------------------------------------------

  public final ClassCache2 assoc (final Class c0,
                                  final Class c1,
                                  final IFn m) {
    return ensureCell(c0,c1).set(c0,c1,m); }

  //--------------------------------------------------------------
}
