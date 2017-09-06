package palisades.lakes.dynafun.java;

import java.util.Collections;
import java.util.Map;

import clojure.lang.IFn;
import palisades.lakes.dynafun.java.signature.SignatureN;

/** minimal immutable lookup table with linear search.
 *
 * @author palisades dot lakes at gmail dot com
 * @since 2017-08-31
 * @version 2017-09-05
 */

@SuppressWarnings("unchecked")
public final class SignatureNCache {

  private final SignatureN[] signatures;
  private final IFn[] methods;

  /** When there are too many methods for linear search. */
  private final Map<SignatureN,IFn> nonlinear;
  
  //--------------------------------------------------------------

  public final IFn get (final SignatureN k) {
    for (int i=0;i<signatures.length;i++) {
      if (k.equals(signatures[i])) { return methods[i]; } }
    return nonlinear.get(k); }

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------
  /** private because keeps unsafe references to mutable args.
   */

  private SignatureNCache (final SignatureN[] ks, 
                          final IFn[] vs,
                          final Map m) {
    signatures = ks;
    methods = vs; 
    nonlinear = m; }

  public static final SignatureNCache empty () {
    return new SignatureNCache(
      new SignatureN[0], 
      new IFn[0],
      Collections.emptyMap()); }

  //--------------------------------------------------------------

  private final int index (final SignatureN k) {
    for (int i=0;i<signatures.length;i++) {
      if (k.equals(signatures[i])) { return i; } }
    return -1; }

  //--------------------------------------------------------------

  private final SignatureNCache set (final int i0,
                                    final SignatureN c,
                                    final IFn m) {
    final SignatureN[] cs = Util.copy(signatures);
    final IFn[] ms = Util.copy(methods);
    cs[i0] = c;
    ms[i0] = m;
    return new SignatureNCache(cs,ms,nonlinear); }

  //--------------------------------------------------------------
  public final SignatureNCache assoc (final SignatureN k,
                                     final IFn m) {
    final int i0 = index(k);
    if (0 <= i0) { return set(i0,k,m); }

    return new SignatureNCache(
      Util.append(signatures,k),
      Util.append(methods,m),
      nonlinear); }

  //--------------------------------------------------------------
}
