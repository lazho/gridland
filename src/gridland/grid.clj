(ns gridland.grid)

;;; Fun stuff with diamond-square algorithm!

(defn avg
  "Returns the average of given numbers."
  [xs]
  (float (/ (reduce + xs) (count xs))))

(defn randomise
  "Returns a number in (x-r, x+r)."
  [x r]
  (if (= 1 (rand-int 2))
    (- x (rand r))
    (+ x (rand r))))

(defn four-to-nine
  "Split a 2x2 grid into 3x3 and randomly generate the new entries."
  [g]
  (let [data (get g :data)]
    (let [up (randomise (avg (map (partial get data) [0 1])) 1)
          left (randomise (avg (map (partial get data) [0 2])) 1)
          right (randomise (avg (map (partial get data) [1 3])) 1)
          down (randomise (avg (map (partial get data) [2 3])) 1)]
      (let [center (+ (rand) (avg [up left right down]))]
        [{:size-x 2 :size-y 2
           :data [(get data 0) up left center]}
          {:size-x 2 :size-y 2
           :data [up (get data 1) center right]}
          {:size-x 2 :size-y 2
           :data [left center (get data 2) down]}
          {:size-x 2 :size-y 2
           :data [center right down (get data 3)]}]))))

(defn horizontal-merge
  "Given 2 mxn grids, merge them into 1 (2m-1)xn grid."
  [left right]
  {:size-x (- (* 2 (get left :size-x)) 1) 
   :size-y (get left :size-y)
   :data
     (vec (reduce concat 
                  (map concat
                       (partition (get left :size-x) (get left :data))
                       (map rest (partition (get right :size-x) (get right :data))))))})

(defn vertical-merge
  "Given 2 mxn grids, merge them into 1 mx(2n-1) grid."
  [up down]
  {:size-x (get up :size-x)
   :size-y (- (* 2 (get up :size-y)) 1)
   :data
     (vec (concat (get up :data)
                  (reduce concat
                          (rest (partition (get down :size-x) (get down :data))))))})

(defn four-to-one
  "Merge 4 grids to one"
  ([gs]
    (apply four-to-one gs))
  ([g0 g1 g2 g3]
    (vertical-merge (horizontal-merge g0 g1)
                    (horizontal-merge g2 g3))))

(defn make-grid
  "Makes a square grid with given complexity and corner values."
  ([c]
    (make-grid c
               {:size-x 2 :size-y 2
                :data (vec (take 4 (repeat 0.0)))}))
  ([c g]
    (if (= 0 c)
        g
        (four-to-one
          (map (partial make-grid (- c 1)) (four-to-nine g))))))

(defn normalise
  "Normalises a grid making all values between 0 and 1."
  [g]
    (let [data (get g :data)]
      (let [lo (apply min data)
            hi (apply max data)]
           {:size-x (get g :size-x)
            :size-y (get g :size-y)
            :data (vec (for [n data]
                            (/ (- n lo) (- hi lo))))})))

(defn grid-to-coords
  "Turns a grid into a list of coordinates."
  [g]
    (vec (for [n (range (* (get g :size-x) (get g :size-y)))
              :let [x (rem n (get g :size-x))
                    y (quot n (get g :size-x))]]
              [x y (get (get g :data) n)])))
