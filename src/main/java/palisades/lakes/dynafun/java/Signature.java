package palisades.lakes.dynafun.java;

/** A list of classes, for optimizing multimethod dispatch
 * functions.
 * <p>
 * TODO: eliminate interface, just use instance and static
 * static utility class to provide generic functionality?
 * 
 * @author palisades dot lakes at gmail dot com
 * @since 2017-06-05
 * @version 2017-08-18
 */

public interface Signature {

  int size();
  
  //--------------------------------------------------------------
  // inheritance partial ordering
  //--------------------------------------------------------------

  boolean isAssignableFrom (Signature that);
  
  // shortcuts to avoid creating Signature instance
  boolean isAssignableFrom (Class k);
  boolean isAssignableFrom (Class k0, Class k1);
  boolean isAssignableFrom (Class k0, Class k1, Class k2);
  boolean isAssignableFrom (Class... ks);

  //--------------------------------------------------------------
  // short cut signature instantiation
  //--------------------------------------------------------------

  boolean equiv (Class k0, Class k1);
  boolean equiv (Class k0, Class k1, Class k2);
  boolean equiv (Class... ks);

  //--------------------------------------------------------------
}
//--------------------------------------------------------------