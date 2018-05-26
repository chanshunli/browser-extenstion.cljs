(ns hello-world.content
  ;;(:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as r]
            ;;[clojure.core.async :as async :refer [<! >!]]
            ;;[clojure.core.async :as async]
            ;;[cljs-http.client :as http]
            ))

(enable-console-print!)

(println "Hello from cljs, This is content JS !")

;; in brower environment?
(when (.-body js/document)
  (set!
   js/window.onkeydown
   (fn [e]
     (let [keycode (.-keyCode e)
           ;; 0~9 => 48~57
           ctrlkey (.-ctrlKey e)
           ;; true or false
           metakey (.-metaKey e)]
       ;; Ctrl的组合键
       (if (and ctrlkey (not= keycode 17))
         (cond
           ;; 数字键
           ((set (range 47 58)) keycode)
           (js/alert "这里是数字键!")
           ;;
           (= 71 keycode)
           (let [selector (.getSelection js/window)
                 select-stri (str (.toString selector))]
             (js/alert (str "正在谷歌搜索:" select-stri))
             )
           (= 83 keycode)
           (prn 1111111)
           ;;
           :else (prn keycode))
         nil)
       )
     )
   )

  #_(set! (.. js/document
              -body
              -style
              -backgroundColor)
          "green")

  #_(set! (.. js/document
              -body
              -innerHTML)
          "<h1 align='center'>You got an extension written in ClojureScript!😄</h1>")
  )
