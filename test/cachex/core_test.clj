(ns cachex.core-test
  (:require [clojure.test :refer :all]
            [cachex.core :as cache]))

(deftest cache-test
  (testing "memoized"
    (let [c          (cache/lru-cache)
          start-time (System/currentTimeMillis)]
      (dotimes [x 2]
        (cache/memoized c :foo (Thread/sleep 100) :ok))
      (is (< (- (System/currentTimeMillis) start-time) 200))))
  (testing "invalidate!"
    (let [c          (cache/lru-cache)
          start-time (System/currentTimeMillis)]
      (dotimes [x 2]
        (cache/invalidate! c :foo)
        (cache/memoized c :foo (Thread/sleep 100) :ok))
      (is (> (- (System/currentTimeMillis) start-time) 200))))
  (testing "clear!"
    (let [c          (cache/lru-cache)
          start-time (System/currentTimeMillis)]
      (cache/memoized c :foo :ok)
      (cache/memoized c :bar :ok)
      (cache/clear! c)
      (is (empty? @c)))))
