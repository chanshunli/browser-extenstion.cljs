(ns hello-world.background)

(enable-console-print!)

(println "Hello from cljs, This is background JS")

;; in chrome extension environment ?
(when (goog.object/getValueByKeys js/window "chrome" "browserAction")
  (.addListener js/chrome.browserAction.onClicked
                (fn [_]
                  (js/alert "Write browser extension in ClojureScript!")))
  #_(.addListener js/chrome.runtime.onMessage
                  (fn [msg sender send-response]
                    (println (str "listen message done~~~" (js->clj msg :keywordize-keys true)))
                    true))
  #_(println "listen request done!")
  #_(.addListener js/chrome.runtime.onMessage
                  (fn [msg sender send-response]
                    (prn 111111111)
                    #_(match [(js->clj msg :keywordize-keys true)]
                             [{:sandbox test-url}] (send-response (modify-url test-url))
                             [{:url goto-url}] (.create js/chrome.tabs (clj->js {:url goto-url}))
                             [msg] (println "Unknown: " msg))
                    ;; https://developer.chrome.com/extensions/runtime#event-onMessage
                    true))
  )
