(ns danhable.bytecount.impl
  "This namespace contains the various implementations under test."
  (:import [java.io FilterInputStream InputStream]
           [com.google.common.io CountingInputStream]
           [java.util.concurrent.atomic AtomicLong]
           [danhable.bytecount.io JavaCountingInputStream]
           [danhable.bytecount.util SimpleLongCounter])
  (:gen-class))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; Implementation #0 ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This is the base line test case that does not wrap the stream in any
;; additional processing logic.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn no-counting
  ^InputStream [^InputStream input-stream]
  input-stream)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; Implementation #1 ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; The first implementation uses an atom to hold a long count of bytes and
;; swap! to update those values as bytes are counted.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn opsc-atom
  [input-stream]
  (let [counter (atom 0)
        update-fn (fn [x] (swap! counter #(+ % x)))]
    (proxy [FilterInputStream]
           [input-stream]
      (read
        ([]
         (let [result (.read input-stream)]
           (when (> result -1)
             (update-fn 1))
           result))

        ([^bytes bytes]
         (let [result (.read input-stream bytes)]
           (when (> result -1)
             (update-fn result))
           result))

        ([^bytes bytes ^long off ^long len]
         (let [result (.read input-stream bytes off len)]
           (when (> result -1)
             (update-fn result))
           result))))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; Implementation #2 ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Thinking the problem might lie with the locking nature of the atom during
;; updates, this implementation uses a Java class that is simply a wrapper
;; around a long and an increment operator.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn java-counter
  [input-stream]
  (let [counter (SimpleLongCounter. 0)]
    (proxy [FilterInputStream]
           [input-stream]
      (read
        ([]
          (let [result (.read input-stream)]
            (when (> result -1)
              (.increment counter 1))
            result))

        ([^bytes bytes]
          (let [result (.read input-stream bytes)]
            (when (> result -1)
              (.increment counter result))
            result))

        ([^bytes bytes ^long off ^long len]
          (let [result (.read input-stream bytes off len)]
            (when (> result -1)
              (.increment counter result))
            result))))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; Implementation #3 ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Observing a large number of reflection operations that were not displayed
;; by the reflection warning, one key type hint is added.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn optimized-atom
  [^InputStream input-stream]
  (let [counter (atom 0)
        update-fn (fn [x] (swap! counter #(+ % x)))]
    (proxy [FilterInputStream]
           [input-stream]
      (read
        ([]
         (let [result (.read input-stream)]
           (when (> result -1)
             (update-fn 1))
           result))

        ([^bytes bytes]
         (let [result (.read input-stream bytes)]
           (when (> result -1)
             (update-fn result))
           result))

        ([^bytes bytes ^long off ^long len]
         (let [result (.read input-stream bytes off len)]
           (when (> result -1)
             (update-fn result))
           result))))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; Implementation #2 ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Thinking the problem might lie with the locking nature of the atom during
;; updates, this implementation uses a Java class that is simply a wrapper
;; around a long and an increment operator.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn optimized-java-counter
  [^InputStream input-stream]
  (let [counter (SimpleLongCounter. 0)]
    (proxy [FilterInputStream]
           [input-stream]
      (read
        ([]
         (let [result (.read input-stream)]
           (when (> result -1)
             (.increment counter 1))
           result))

        ([^bytes bytes]
         (let [result (.read input-stream bytes)]
           (when (> result -1)
             (.increment counter result))
           result))

        ([^bytes bytes ^long off ^long len]
         (let [result (.read input-stream bytes off len)]
           (when (> result -1)
             (.increment counter result))
           result))))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; Implementation #4 ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; What if I spent 1 minute writing this in Java?
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn custom-java-stream
  [^InputStream input-stream]
  (JavaCountingInputStream. input-stream))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; Implementation #5 ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Uses a widely distributed implementation from the Guava library instead of
;; my own implementation.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn guava
  [^InputStream input-stream]
  (CountingInputStream. input-stream))
