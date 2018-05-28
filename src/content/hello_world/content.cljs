(ns hello-world.content
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as r]
            [clojure.core.async :as async]
            [alandipert.storage-atom :refer [local-storage] :as st]
            [hello-world.something :as something]
            [hello-world.token :as token]
            [cljs-http.client :as http]))

(def api-token (local-storage (r/atom token/token) :api-token))

(def domain-google-search-history (local-storage (r/atom []) :google-history))

(enable-console-print!)

(defn record-event
  [{:keys [event_name event_data op-fn]}]
  (go (let [response
            (<!
             (http/post "http://67.216.200.53/record-event"
                        {:headers {"jimw-clj-token" @api-token}
                         :json-params
                         {:event_name event_name :event_data event_data}}))]
        (let [data (:body response)]
          (op-fn data)))))

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

(defn recur-match [re-text selector]
  (let [bp-ele (-> selector .-baseNode .-parentElement)]
    ((fn [n]
       (loop [cnt n]
         (if (re-matches re-text (.-textContent cnt))
           cnt
           (recur (.-parentElement cnt))))) bp-ele)))

(println (str "Hello from cljs, This is content JS ! " (something/hello)))

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
                   select-stri (str (.toString selector))
                   url (get-url)]
               (prn (str "正在谷歌搜索: " select-stri ", url: " url))
               ;; 临时的fn多线程跑自己:
               (swap! domain-google-search-history conj {:url url :search-data select-stri})
               (prn (str "所有搜索历史: " @domain-google-search-history))
               (set! (.-value (.getElementById js/document "google-input")) select-stri)
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
