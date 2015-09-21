(ns cachex.core
  (:require [clojure.core.cache :as cc]))

(defn invalidate!
  "removes the id and the content associated with it from cache"
  [cache id]
  (swap! cache cc/evict id))

(defn clear! [cache]
  (swap! cache empty))

(defmacro memoized
  "checks if there is a cached result of the body available,
  if so the result is returned, otherwise the body
  is evaluated to produce a new result"
  [cache id & body]
  `(get
    (swap! ~cache
           (fn [cache#]
             (if (clojure.core.cache/has? cache# ~id)
               (clojure.core.cache/hit cache# ~id)
               (clojure.core.cache/miss cache# ~id (do ~@body)))))
    ~id))

(defn fifo-cache
  "if size is not specified defaults to 32 items"
  [& [size]]
  (atom (cc/fifo-cache-factory {} :threshold (or size 32))))

(defn lu-cache
  "if size is not specified defaults to 32 items"
  [& [size]]
  (atom (cc/lu-cache-factory {} :threshold (or size 32))))

(defn lru-cache
  "if size is not specified defaults to 32 items"
  [& [size]]
  (atom (cc/lru-cache-factory {} :threshold (or size 32))))

(defn ttl-cache
  "if timeout is not specified the timeout defaults to 2 seconds"
  [& [timeout]]
  (atom (cc/lru-cache-factory {} :ttl (or timeout 2))))
