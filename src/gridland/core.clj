(ns gridland.core
  (:gen-class)
  (:require [gridland.grid :as grid]
            [quil.core :as q]))

(defn setup
  []
  (q/background 10)
  (q/color-mode :hsb 1) ; colour in hsb mode, (0,1)
  (q/no-stroke)
  (q/no-loop))

(defn colourise
  [x lo hi]
  (if (> x 0.5)
    [hi (* 2 (- x 0.5)) (+ 0.875 (/ (- x 0.5) 4))]
    [lo (- 1 (* 2 x)) (+ 0.875 (/ (- (- 1 x) 0.5) 4))]))

(defn draw-point
  [c r m]
  (apply q/fill-float (colourise (get c 2) 0.63 0))
  (q/rect (* m (get c 0))
             (* m (get c 1))
             r r))

(defn draw
  [cs r m]
  (doseq [c cs]
    (draw-point c r m)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [cs (grid/grid-to-coords (grid/normalise (grid/make-grid 4)))]
    (q/defsketch dsa
        :size [510 510]
        :setup setup
        :draw (partial draw cs 30 30))))
