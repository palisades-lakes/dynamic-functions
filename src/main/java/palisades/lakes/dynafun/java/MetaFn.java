package palisades.lakes.dynafun.java;

import clojure.lang.IFn;
import clojure.lang.IObj;
import clojure.lang.IPersistentMap;
import clojure.lang.ISeq;

/** Minimal wrapper to add meta data to functions.
 * Try to be more efficient than Clojure 1.8.0, 
 * calling <code>(with-meta f m)</code> returns an
 * instance of <code>RestFn</code>.
 *
 * @author palisades dot lakes at gmail dot com
 * @since 2017-09-16
 * @version 2017-09-16
 */

@SuppressWarnings("unchecked")
public final class MetaFn implements IFn, IObj {

  private final IFn _inner;
  private final IPersistentMap _meta;

  //--------------------------------------------------------------
  // construction
  //--------------------------------------------------------------
  // private because it doesn't copy the input map.

  private MetaFn (final IFn f,
                  final IPersistentMap m) {
    _inner = f;
    _meta = m; }
 
  // TODO: should this return IObj or IFn?
  
  public static final IObj withMeta (final IFn f,
                                    final IPersistentMap m) {
    if (f instanceof MetaFn) {
      return new MetaFn(((MetaFn) f)._inner,m); }
    if (f instanceof DynaFun) {
      return ((DynaFun) f).withMeta(m); }
    return new MetaFn(f,m); }
  
  //--------------------------------------------------------------
  // IObj interface
  //--------------------------------------------------------------

  @Override
  public final IPersistentMap meta () { return _meta; }

  @Override
  public final IObj withMeta (final IPersistentMap m) {
    return withMeta(_inner,m); }

  //--------------------------------------------------------------
  // IFn interface
  //--------------------------------------------------------------

  @Override
  public final Object invoke () { 
    return _inner.invoke(); }

  @Override
  public final Object invoke (final Object x) {
    return _inner.invoke(x); }

  @Override
  public final Object invoke (final Object x0,
                              final Object x1) {
    return _inner.invoke(x0,x1); }

  @Override
  public final Object invoke (final Object x0,
                              final Object x1,
                              final Object x2) {
    return _inner.invoke(x0,x1,x2); }

  @Override
  public final Object invoke (final Object x0,
                              final Object x1,
                              final Object x2,
                              final Object x3) {
    return _inner.invoke(x0,x1,x2,x3); }

  // TODO: fill in missing invokes
  
  @Override
  public final Object invoke (final Object x0,
                              final Object x1,
                              final Object x2,
                              final Object x3,
                              final Object x4) {
    return _inner.invoke(x0,x1,x2,x3,x4); }

  @Override
  public final Object invoke (final Object x0,
                              final Object x1,
                              final Object x2,
                              final Object x3,
                              final Object x4,
                              final Object x5) {
    return _inner.invoke(x0,x1,x2,x3,x4,x5); }

  @Override
  public final Object invoke (final Object x0,
                              final Object x1,
                              final Object x2,
                              final Object x3,
                              final Object x4,
                              final Object x5,
                              final Object x6) {
    return _inner.invoke(x0,x1,x2,x3,x4,x5,x6); }

  @Override
  public final Object invoke (final Object x0,
                              final Object x1,
                              final Object x2,
                              final Object x3,
                              final Object x4,
                              final Object x5,
                              final Object x6,
                              final Object x7) {
    return _inner.invoke(x0,x1,x2,x3,x4,x5,x6,x7); }

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
    return _inner.invoke(x0,x1,x2,x3,x4,x5,x6,x7,x8); }

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
    return _inner.invoke(x0,x1,x2,x3,x4,x5,x6,x7,x8,x9); }

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
    return _inner.invoke(x0,x1,x2,x3,x4,x5,x6,x7,x8,x9,x10); }

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
    return _inner.invoke(x0,x1,x2,x3,x4,x5,x6,x7,x8,x9,x10,x11); }

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
    return _inner.invoke(x0,x1,x2,x3,x4,x5,x6,x7,x8,x9,x10,x11,
      x12); }

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
    return _inner.invoke(x0,x1,x2,x3,x4,x5,x6,x7,x8,x9,x10,x11,
      x12,x13); }

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
    return _inner.invoke(x0,x1,x2,x3,x4,x5,x6,x7,x8,x9,x10,x11,
      x12,x13,x14); }

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
    return _inner.invoke(x0,x1,x2,x3,x4,x5,x6,x7,x8,x9,x10,x11,
      x12,x13,x14,x15); }

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
    return _inner.invoke(x0,x1,x2,x3,x4,x5,x6,x7,x8,x9,x10,x11,
      x12,x13,x14,x15,x16); }

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
    return _inner.invoke(x0,x1,x2,x3,x4,x5,x6,x7,x8,x9,x10,x11,
      x12,x13,x14,x15,x16,x17); }

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
    return _inner.invoke(x0,x1,x2,x3,x4,x5,x6,x7,x8,x9,x10,x11,
      x12,x13,x14,x15,x16,x17,x18); }

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
    return _inner.invoke(x0,x1,x2,x3,x4,x5,x6,x7,x8,x9,x10,x11,
      x12,x13,x14,x15,x16,x17,x18,x19); }

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
    return _inner.invoke(x0,x1,x2,x3,x4,x5,x6,x7,x8,x9,x10,x11,
      x12,x13,x14,x15,x16,x17,x18,x19,xs); }
  //--------------------------------------------------------------

  @Override
  public final Object call () throws Exception { 
    return _inner.call(); }

  @Override
  public final void run () { _inner.run();  }

  @Override
  public final Object applyTo (final ISeq xs) {
    return _inner.applyTo(xs); }

  //--------------------------------------------------------------
}
