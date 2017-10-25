(ns danhable.bytecount.test
  (:require [danhable.bytecount.impl :refer :all])
  (:import [java.io InputStream ByteArrayInputStream]
           [danhable.bytecount.io FixedMemoryTestInputStream]
           [humanize Humanize]
           [humanize.util Constants$TimeStyle])
  (:gen-class))


(defmacro time-execution
  [& body]
  `(let [start-time# (System/currentTimeMillis)]
     ~@body
     (let [end-time# (System/currentTimeMillis)]
        (- end-time# start-time#))))


(defn- drain-stream
  [^InputStream input-stream]
  (loop [value (.read input-stream)]
    (when (not= value -1)
      (recur (.read input-stream)))))


(defn- run-test
  [impl-fn size]
  (with-open [is (FixedMemoryTestInputStream. size)
              ^InputStream stream (impl-fn is)]
    (time-execution
      (drain-stream stream))))


(defn- test-impl
  [desc impl-fn small med large]
  (print (format ">>> %-26s => " desc))
  (flush)
  (print (format "%6s ms / "
                 (run-test impl-fn small)))
  (flush)
  (print (format "%6s ms / "
                 (run-test impl-fn med)))
  (flush)
  (println (format "%6s ms"
                   (run-test impl-fn large))))


(defn -main
  [& args]
  (set! *warn-on-reflection* true)
  (set! *unchecked-math* true)
  (let [small-test-size 5242880
        med-test-size 52428800
        lg-test-size 524288000
        test-cases {"No Counting"              no-counting
                    "OpsC Code - Atom"         opsc-atom
                    "Guava Stream Impl"        guava
                    "Custom Java Stream Impl"  custom-java-stream
                    "Java Counter"             java-counter
                    "Type Hinted OpsC Atom"    optimized-atom
                    "Type Hinted Java Counter" optimized-java-counter}]
    (println "\nInputStream counter benchmark suite")
    (println (format "Test Stream Sizes: %s / %s / %s "
                     (Humanize/binaryPrefix small-test-size)
                     (Humanize/binaryPrefix med-test-size)
                     (Humanize/binaryPrefix lg-test-size)))
    (println "====================================================\n")
    (doseq [[test-desc test-fn] test-cases]
      (test-impl test-desc test-fn small-test-size med-test-size lg-test-size))))
