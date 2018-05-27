(ns hello-world.content
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as r]
            [clojure.core.async :as async]
            ;;[cljs-http.client :as http]
            ))

(enable-console-print!)

(def google-input-html
  "<form target=\"_blank\" action=\"http://www.google.com/search\" method=\"get\" style=\"display: none;\"><input type=\"text\" id=\"google-input\" name=\"q\"><input type=\"submit\" value=\"Google\" id=\"google-input-button\"></form>")

(defn body-append-html-stri [html]
  (.appendChild
   (.-body
    js/document)
   (-> (js/DOMParser.)
       (.parseFromString
        html "text/html")
       .-body
       .-firstElementChild)))

(defn get-url [] (str (-> js/window .-location .-href)))

(println "Hello from cljs, This is content JS !")

;; in brower environment?
(when (.-body js/document)
  (do
    ;; A: Emacs的钢琴键组合思想:纯函数组合
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
             ;; C-g: 任意网页的谷歌搜索
             (= 71 keycode)
             (let [selector (.getSelection js/window)
                   select-stri (str (.toString selector))]
               (prn (str "正在谷歌搜索: " select-stri ", url: " (get-url)))
               (set! (.-value (.getElementById js/document "google-input")) (str select-stri))
               (.click (.getElementById js/document "google-input-button"))
               )
             (= 83 keycode)
             (prn 1111111)
             ;;
             :else (prn keycode))
           nil)
         )
       )
     )
    ;; B:Google的搜索回归: 关键词对应答案或者树 => jimw-clj搜索也可以放入
    (do
      ;; Github: Refused to send form data to '<URL>' because it violates the following Content Security Policy directive: "form-action 'self' github.com gist.github.com".
      ;; Stackoverflow: OK,append之后还可以搜索
      (body-append-html-stri google-input-html)
      
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
