(defproject gridland "0.1.0-SNAPSHOT"
  :description "Draws randomly generated 3D terrains."
  :url "https://github.com/lazho/gridland"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [quil "2.5.0"]]
  :main ^:skip-aot gridland.core)
