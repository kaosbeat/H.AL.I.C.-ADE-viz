(ns main.instruments.channel05
  (:require [quil.core :as q]))

;; template for visual instrument instance
;;
;;util
(defn drop-nth [n coll]
  (vec (keep-indexed #(if (not= %1 n) %2) coll))
  )

(defn draw [x channel]
  "main draw for this visual instrument"

  ;(dotimes [n (mod (get channel :beatnumber ) 8 )])
  (q/with-translation [(* 50 10) 100 -1000]
    (q/fill 255 (rand-int 255))
    (dotimes [y 20]
      (q/with-rotation [(rand-int 20 ) 2 1 0 ]
        (dotimes [x 20]
          (q/with-translation [ (* x 200) (* y 200 ) 0]
            (q/box 180))))))

  )

(def viz (atom []))
(def vizcount (atom 0))




(defn render [channel]
  ;;; if channeldata
  (dotimes [n (count @viz)]
    (let [x (get (nth @viz n) :x)
          y (get (nth @viz n) :y)
          z (get (nth @viz n) :z)
          w  100
          h (/  (get  channel :freq) 10)
          hfr (get channel :peak)
          vfr 1
          ]

      (q/with-translation [ x y z]
        (q/with-rotation [x 0 0 1]
          (q/stroke-weight 2)
          (draw w channel))))

    )

  )


(defn add [x y z ttl]
  (if (= 0 (count @viz))
    (reset! viz []))
  (if (= ttl 0)
    (swap! viz conj {:x x :y y :z z :ttl ttl :sticky true })
    (swap! viz conj {:x x :y y :z z :ttl ttl :sticky false }))
  )



(defn updatech5 []
  ; for some reason not all pills are deleted
  (reset! vizcount [])
  (dotimes [n (count @viz)]
    (if (false? (= 0 (get (get @viz n) :ttl)))
      ;decrease TTL in pill if ttl > 0
      (do
        (swap! viz update-in [n :ttl] dec)
;        (swap! linesquares update-in [n :z] (fn [x] (rand-int -670)))
        (swap! viz update-in [n :y] (fn [y] (rand-int 1000)))
        )
      ;else mark pill for deletion
      (swap! vizcount conj n)
      ;(reset! @pills [0 9 0])
      )
    )
  (dotimes [n (count @vizcount)]
;    (println " really dropping stuff")
    (reset! viz  (drop-nth (nth @vizcount n) @viz)))
  ;(println @pills)
  )

(add 500 200 -500 120)
(updatech5)
