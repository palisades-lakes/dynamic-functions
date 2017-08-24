# dynamic-functions [![Clojars Project](https://img.shields.io/clojars/v/palisades-lakes/dynamic-functions.svg)](https://clojars.org/palisades-lakes/dynamic-functions)

This library is currently a vehicle for experimenting with 
design and implementation alternatives for generic functions
(aka multimethods)
in Clojure.
It is therefore completely unstable 
(I'm committed to nothing for now)
until at least version 0.1.0.

Initial ideas:
- all functions are generic functions
- trade flexibility of Clojure multimethods for performance
- no hierarchies
- dispatch on class-based signatures only
- signature specified via arglist type hints
- only simple arglists (no destructuring)
- single method per arity equivalent to Clojure `defn`
- high performance with hinted primitives in and out
- Clojure functions are nearly opaque; 
the only API is `clojure.lang.IFn/invoke().  
    Dynamic functions should be fully first class, with 
    an API exposing, at a minimum:
    * the function name
    * any hints on the return value
    * the arglists, including type hints
    * the relevant parts of the lexical environment in which it
    was created, a list of bindings including names, values,
    and type hints, possibly other hints as well.
    * possibly also the dynamic environment
    * the expressions that defined the bodies
    * constructors that are functions, not just macros and special
     forms.
    For example, it should be possible to do something like
    ```
    (defn compose ^IFn [^IFn f ^IFn g]
      (assert (matches? (arglist f) (return-value g))
      (function (str (name f) "-o-" (name g)) (arglist g)
        `(f (g ~(arglist g)))))
    ```
    It should be possible to examine the returned function object,
    determine that it is the composition of `f` and `g`, and 
    generate a new function by supplying a modified environment, 
    say, by substituting something for 'f' or 'g'. 
 
## Usage

### Dependency 

Available from 
[Clojars](https://clojars.org/palisades-lakes/dynamic-functions):

Maven:

```xml
<dependency>
  <groupId>palisades-lakes</groupId>
  <artifactId>dynamic-functions</artifactId>
  <version>0.0.x</version>
</dependency>
```

Leiningen/Boot:
```clojure
Leiningen/Boot
[palisades-lakes/dynamic-functions "0.0.x"]
```

### Code examples

(require `[palisades.lakes.dynafn.api :as df])

(df/defn intersects? 
  [^IntegerInterval s0 ^java.util.Set s1]
  (.intersects s0 s1))
  
## Acknowledgments

### ![Yourkit](https://www.yourkit.com/images/yklogo.png)

YourKit is kindly supporting open source projects with its full-featured Java
Profiler.

YourKit, LLC is the creator of innovative and intelligent tools for profiling
Java and .NET applications. Take a look at YourKit's leading software products:

* <a href="http://www.yourkit.com/java/profiler/index.jsp">YourKit Java Profiler</a> and
* <a href="http://www.yourkit.com/.net/profiler/index.jsp">YourKit .NET Profiler</a>.

