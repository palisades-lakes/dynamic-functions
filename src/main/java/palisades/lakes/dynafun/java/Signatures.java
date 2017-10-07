package palisades.lakes.dynafun.java;

import java.util.List;

/** Utilities for signatures.
 *
 * @author palisades dot lakes at gmail dot com
 * @since 2017-06-05
 * @version 2017-08-19
 */

@SuppressWarnings("unchecked")
public final class Signatures {

  //--------------------------------------------------------------

  public static final Signature2 get (final Class c0, 
                                      final Class c1) {
    return Signature2.get(c0,c1); } 

  public static final Signature3 get (final Class c0, 
                                      final Class c1, 
                                      final Class c2) {
    return Signature3.get(c0,c1,c2); } 

  public static final SignatureN get (final Class... cs) {
    return SignatureN.get(cs); } 

  public static final Object get (final List cs) {
    if (null == cs) { return null; }
    final int n = cs.size();
    switch (n) {
    case 0:
      return null;
    case 1 : { 
      final Class c0 = (Class) cs.get(0);
      return c0; }
    case 2 : { 
      final Class c0 = (Class) cs.get(0);
      final Class c1 = (Class) cs.get(1);
      return get(c0,c1); }
    case 3 : { 
      final Class c0 = (Class) cs.get(0);
      final Class c1 = (Class) cs.get(1);
      final Class c2 = (Class) cs.get(2);
      return get(c0,c1,c2); }
    default : return SignatureN.extract(cs); } } 

  public static final Object get (final Object cs) {
    if (null == cs) { return null; }
    if (cs instanceof Class) { return cs; }
    if (cs instanceof List) { return get((List) cs); }
    if (cs instanceof Class[]) { return get((Class[]) cs); }
    throw new IllegalArgumentException(
      "can't get a signature for" + cs.getClass().getName()); }

  //--------------------------------------------------------------
  // No Signature1, just use the class

  public static final Object extract (final Object... xs) {
    final int n = xs.length;
    assert n > 1;
    switch (n) {
    case 1 : return xs[0].getClass();
    case 2 : return Signature2.extract(xs[0],xs[1]);
    case 3 : return Signature3.extract(xs[0],xs[1],xs[2]);
    default : return SignatureN.extract(xs); } } 

  //--------------------------------------------------------------

  public static final boolean isAssignableFrom (final Class[] c0,
                                                final Class... c1) {
    if (c0.length != c1.length) { return false; }
    for (int i=0;i<c0.length;i++) {
      if (! c0[i].isAssignableFrom(c1[i])) { return false; } }
    return true; }

  //--------------------------------------------------------------

  public static final boolean isAssignableFrom (final Object s0,
                                                final Object s1) {
    if ((s0 instanceof Class) && (s1 instanceof Class)) {
      return ((Class) s0).isAssignableFrom((Class) s1); }
    if ((s0 instanceof Class[]) && (s1 instanceof Class[])) {
      return isAssignableFrom((Class[]) s0, (Class[]) s1); }
    if ((s0 instanceof Signature2) && (s1 instanceof Signature2)) {
      return ((Signature2) s0).isAssignableFrom((Signature2) s1); }
    if ((s0 instanceof Signature3) && (s1 instanceof Signature3)) {
      return ((Signature3) s0).isAssignableFrom((Signature3) s1); }
    if ((s0 instanceof Class) && (s1 instanceof Class)) {
      return ((Class) s0).isAssignableFrom((Class) s1); }
    if ((s0 instanceof SignatureN) && (s1 instanceof SignatureN)) {
      return ((SignatureN) s0).isAssignableFrom((SignatureN) s1); }
    return false; }

  //--------------------------------------------------------------

  private Signatures () {
    throw new UnsupportedOperationException(
      "can't instantiate " + getClass().getName()); }


  //--------------------------------------------------------------
}
//--------------------------------------------------------------