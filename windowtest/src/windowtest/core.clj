(ns windowtest.core
  (:import [org.eclipse.swt.widgets Display Shell FileDialog Table TableColumn TableItem]
           [org.eclipse.swt.events KeyAdapter]
           [org.eclipse.swt.layout FillLayout]
           [org.eclipse.swt SWT]))

(defn window-open []
  (let [display (Display.)
        shell (Shell. display)]
    (doto shell
      (.setText "Hello")
      (.setSize 640 480)
      (.setLayout (FillLayout.))
      (.addKeyListener
        (proxy [KeyAdapter] []
          (keyPressed [e]
                      (if (= (.character e) \q) (.close shell))))))
    (let [t (Table. shell (bit-or SWT/CHECK SWT/FULL_SELECTION))]
      (doseq [x ["hoge" "piyo" "foo"]]
        (doto (TableColumn. t SWT/LEFT)
          (.setText x)
          (.setWidth 50)))
      (dotimes [d 5]
        (doto (TableItem. t SWT/NULL)
          (.setText (into-array String (map (partial str d) ["a" "b" "c"])))))
      (.setHeaderVisible t true)
      (.selectAll t))
    ;(println
    ;  (-> (FileDialog. shell SWT/OPEN)
    ;    (.open)))
    (.pack shell)
    (.open shell)
    (doseq [_ (take-while (partial = false) (repeatedly #(.isDisposed shell)))]
      (if-not (.readAndDispatch display) (.sleep display)))
    (.dispose display)))

(defn -main []
  (println "StartWindow!!")
  (window-open))
