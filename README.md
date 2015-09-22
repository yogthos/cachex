# cachex

A Clojure library for caching s-expressions.

## Usage

[![Clojars Project](http://clojars.org/cachex/latest-version.svg)](http://clojars.org/cachex)

The library provides helper functions for creating different types of
caches using the [core.cache](https://github.com/clojure/core.cache).

The cache can then be used with the `memoized` macro to memoize the
results of arbitary expressions. The body of the memoized expressions
is expected to be free of side effects.

```clojure
(require '[cachex.core :as cache])

(def C (cache/lru-cache))

(time
  (cache/memoized C :foo
    (Thread/sleep 1000)
    :ok))
:ok
;"Elapsed time: 1004.221474 msecs"

(time
  (cache/memoized C :foo
    (Thread/sleep 1000)
    :ok))
:ok
;"Elapsed time: 0.335598 msecs"
```

Types caches supported:

* `lu-cache` - least used cache defaults to 32 items
* `lru-cache` - least recently used cache defaults to 32 items
* `ttl-cache` - least used cache defaults to 2 seconds


The results in the cache can be invalidated using the `invalidate!`
function and the entire cache can be cleared with the `clear!` function.

```clojure
(def C (cache/lu-cache 15))

(cache/memoized
 C :fib
 ((fn fib [n]
    (condp = n
      0 1
      1 1
      (+ (fib (dec n)) (fib (- n 2)))))
  10))

(cache/invalidte! C :fib)

(cache/memoized C :foo :a)
(cache/memoized C :bar :b)

(cache/clear!)
```

## License

Copyright © 2015 Dmitri Sotnikov

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
