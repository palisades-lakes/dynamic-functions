package palisades.lakes.dynafun.java;

import java.util.Collections;

import clojure.lang.IFn;

/** Less flexible, but faster alternative to 
 * <code>clojure.lang.MultiFn</code>
 *
 * @author palisades dot lakes at gmail dot com
 * @since 2017-08-30
 * @version 2017-08-30
 */

public interface DynaFun extends IFn {

  public static DynaFun make (final String name) {
    return new DynaFun1(
      name,
      Collections.emptyMap(),
      Collections.emptyMap()); }

  public DynaFun addMethod (final Object signature,
                            final IFn method);

  public DynaFun preferMethod (final Object x,
                               final Object y) ;
}
