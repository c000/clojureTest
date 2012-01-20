(ns windowtest.core
  (:import [org.eclipse.swt.widgets Display Shell FileDialog Table TableColumn TableItem Text]
           [org.eclipse.swt.events KeyAdapter ModifyListener]
           [org.eclipse.swt.layout GridLayout GridData]
           [org.eclipse.swt SWT]))

(defn window-open []
  (let [display (Display.)
        shell (Shell. display)]
    (doto shell
      (.setText "Hello")
      (.setLayout 
        (let [l (GridLayout. 1 true)]
          (set! (.verticalSpacing l) 10)
          l))
      (.addKeyListener
        (proxy [KeyAdapter] []
          (keyPressed [e]
                      (if (= (.character e) \q) (.close shell))))))
    (let [t (Table. shell (bit-or SWT/BORDER_DASH SWT/FULL_SELECTION))]
      (doseq [x ["hoge" "piyo" "foo"]]
        (doto (TableColumn. t SWT/LEFT)
          (.setText x)
          (.setWidth 50)))
      (dotimes [d 5]
        (doto (TableItem. t SWT/NULL)
          (.setText (into-array String (map (partial str d) ["a" "b" "c"])))))
      (.setHeaderVisible t true)
      (.selectAll t)
      (.setLayoutData t (GridData. GridData/FILL_BOTH)))
    (let [t (Text. shell (bit-or SWT/SINGLE SWT/BORDER))]
      (.setLayoutData t (GridData. GridData/FILL_HORIZONTAL))
      (.addModifyListener t
        (proxy [ModifyListener] []
          (modifyText [_]
            (.setText shell (.getText t))))))
    ;(println
    ;  (-> (FileDialog. shell SWT/OPEN)
    ;    (.open)))
    (.setSize shell 640 480)
    (.open shell)
    (doseq [_ (take-while (partial = false) (repeatedly #(.isDisposed shell)))]
      (if-not (.readAndDispatch display) (.sleep display)))
    (.dispose display)))

(defn -main []
  (println "StartWindow!!")
  (window-open))
