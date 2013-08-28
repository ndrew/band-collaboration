(ns frontend.rendering
  (:require [domina :as dom]
            [io.pedestal.app.render.push :as render]
            [io.pedestal.app.render.push.templates :as templates]
            [io.pedestal.app.render.push.handlers.automatic :as d]
            
            [io.pedestal.app.render.push.handlers :as h]
            [io.pedestal.app.render.events :as events]
            
            )
  (:require-macros [frontend.html-templates :as html-templates]))

(def templates (html-templates/frontend-templates))

;(defn render-page [renderer [_ path] transmitter]
;  (let [parent (render/get-parent-id renderer path)
;        id (render/new-id! renderer path)
;        html (templates/add-template renderer path (:frontend-page templates))]
;    (dom/append! (dom/by-id parent) (html {:id id :message ""}))))

;(defn render-message [renderer [_ path _ new-value] transmitter]
;  (templates/update-t renderer path {:message new-value}))



(defn render-template [template-name initial-value-fn]
  (fn [renderer [_ path :as delta] input-queue]
    (let [parent (render/get-parent-id renderer path)
          id (render/new-id! renderer path)
          html (templates/add-template renderer path (template-name templates))]
      (dom/append! (dom/by-id parent) (html (assoc (initial-value-fn delta) :id id))))))



(defn render-value [renderer [_ path _ new-value] input-queue]
  (let [key (last path)]
    (templates/update-t renderer [:main] {key (str new-value)})))


(defn render-other-counters-element [renderer [_ path] _]
  (render/new-id! renderer path "other-counters"))


(defn render-other-counter-value [renderer [_ path _ new-value] input-queue]
  (let [key (last path)]
    (templates/update-t renderer path {:count (str new-value)})))



(defn add-login-template [renderer [_ path :as delta] input-queue]
  (let [parent (render/get-parent-id renderer path)
        id (render/new-id! renderer path)
        html (:login-page templates)]
    (dom/append! (dom/by-id parent) (html {:id id}))))

(defn add-submit-login-handler [_ [_ path transform-name messages] input-queue]
  (events/collect-and-send :click "login-button" input-queue transform-name messages
                           {"login-name" :value}))

(defn remove-submit-login-event [_ _ _]
  (events/remove-click-event "login-button"))


(defn render-config []
  [[:node-create [:main] (render-template :frontend-page
                                          (constantly {:my-counter "0"}))]
   
   [:node-destroy [:main] h/default-destroy]
   [:transform-enable [:main :my-counter] (h/add-send-on-click "inc-button")]
   [:transform-disable [:main :my-counter] (h/remove-send-on-click "inc-button")]
   [:value [:main :*] render-value]
   [:value [:pedestal :debug :*] render-value]

   ;[:value [:login :*] render-value]
   
   
   
   [:node-create [:main :other-counters] render-other-counters-element]
   [:node-create [:main :other-counters :*]
    (render-template :other-counter
                     (fn [[_ path]] {:counter-id (last path)}))]
   [:node-destroy [:main :other-counters :*] h/default-destroy]
   [:value [:main :other-counters :*] render-other-counter-value]
   
   
   [:node-create  [:login] add-login-template]
   [:node-destroy [:login] h/default-destroy]
 
   [:transform-enable [:login :name] add-submit-login-handler]
   [:transform-disable [:login :name] remove-submit-login-event]
   
   ;[:node-destroy   [:greeting] d/default-exit]
   ;[:value [:greeting] render-message]
   ])
