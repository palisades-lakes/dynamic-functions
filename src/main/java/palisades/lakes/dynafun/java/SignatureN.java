package palisades.lakes.dynafun.java;

import java.util.List;
import java.util.Objects;

/** An immutable 'array list' of classes, for optimizing 
 * multimethod dispatch functions.
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-02-12
 */

@SuppressWarnings("unchecked")
public final class SignatureN implements Signature {

  // TODO: separate 1st k Classes to make constructor faster?
  // might also make isAssignableFrom faster in the false case.
  // TODO: replace Class[] with ASeq to make Clojure 
  // interface simpler?

  public final Class[] classes;

  //--------------------------------------------------------------

  public final boolean isAssignableFrom (final SignatureN that) {
    final Class[] those = that.classes;
    if (classes.length != those.length) { return false; }
    for (int i=0;i<classes.length;i++) {
      if (! Classes.isAssignableFrom(classes[i],those[i])) {
        return false; } }
    return true; }

  //--------------------------------------------------------------

  @Override
  public final boolean isAssignableFrom (final Signature that) {
    if (that instanceof SignatureN) {
      return isAssignableFrom((SignatureN) that); }
    return false; }

  @Override
  public final boolean isAssignableFrom (final Class k0,
                                         final Class k1) {
    return false; }

  @Override
  public final boolean isAssignableFrom (final Class k0,
                                         final Class k1,
                                         final Class k2) {
    return false; }

  @Override
  public final boolean isAssignableFrom (final Class... ks) {
    if (classes.length != ks.length) { return false; }
    for (int i=0;i<classes.length;i++) {
      if (! Classes.isAssignableFrom(classes[i],ks[i])) {
        return false; } }
    return true; }

  //--------------------------------------------------------------
  // Object interface
  //--------------------------------------------------------------

  @Override
  public final int hashCode () {
    int result = 17;
    for (final Class c : classes) {
      result = (31*result) + Objects.hashCode(c); }
    return result; }

  @Override
  public final boolean equals (final Object that) {
    if (this == that) { return true; }
    if (! (that instanceof SignatureN)) { return false; }
    final Class[] those = ((SignatureN) that).classes;
    for (int i=0;i<classes.length;i++) {
      if (! Objects.equals(classes[i],those[i])) {
        return false; } }
    return true; }

  @Override
  public final String toString () {
    final StringBuilder builder =
      new StringBuilder("(");
    builder.append(getClass().getSimpleName());
    builder.append(". ");
    for (final Class c : classes) {
      builder.append(Classes.getName(c));
      builder.append(" "); }
    builder.append(")");
    return builder.toString(); }

  //--------------------------------------------------------------
  // TODO: memoize singleton instances?

  public SignatureN (final Class... ks) {
    assert ks.length > 3;
    // is this safe?
    classes = ks; }

  public SignatureN (final Class c0,
                     final Class c1,
                     final Class c2,
                     final List as) {
    // not really safe, trying to be fast, maybe not worth it
    final Object[] cs = as.toArray();
    final int n = cs.length + 3;
    assert n > 3;
    classes = new Class[n];
    classes[0] = c0;
    classes[1] = c1;
    classes[2] = c2;
    for (int i=3,j=0;i<n;i++,j++) { 
      classes[i] = (Class) cs[j]; } }

  // TODO: copy array?
  public static final SignatureN get (final Class... ks) {
    return new SignatureN(ks); }

  public static final SignatureN get (final Class k0,
                                      final Class k1,
                                      final Class k2,
                                      final List ks) {
    final Class[] classes = new Class[3 + ks.size()];
    classes[0] = k0;
    classes[1] = k1;
    classes[2] = k2;
    int i = 3;
    for (final Object k : ks) { classes[i++] = (Class) k; }
    return new SignatureN(classes); }

  public static final SignatureN extract (final Object... xs) {
    final int n = xs.length;
    assert n > 3;
    final Class[] ks = new Class[n];
    for (int i=0;i<n;i++) { ks[i] = Classes.classOf(xs[i]); }
    return new SignatureN(ks); }

  public static final SignatureN extract (final Object x0,
                                          final Object x1,
                                          final Object x2,
                                          final List as) {
    // not really safe, trying to be fast, maybe not worth it
    final Object[] xs = as.toArray();
    final int n = xs.length + 3;
    assert n > 3;
    final Class[] cs = new Class[n];
    cs[0] = Classes.classOf(x0);
    cs[1] = Classes.classOf(x1);
    cs[2] = Classes.classOf(x2);
    for (int i=3,j=0;i<n;i++,j++) { 
      cs[i] = Classes.classOf(xs[j]); } 
    return new SignatureN(cs); }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
