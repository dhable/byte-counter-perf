(defproject byte-counter-perf "1.0.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [com.google.guava/guava "21.0"]
                 [com.github.mfornos/humanize-slim "1.2.2"]]
  :repositories [["clojars" {:url "https://repo.clojars.org/"}]
                 ["maven-central" {:url "https://repo1.maven.org/maven2"}]]
  :source-paths ["src/clj"]
  :java-source-paths ["src/java"]
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo
            :comments "same as Clojure"}
  :aot :all
  :main danhable.bytecount.test)
