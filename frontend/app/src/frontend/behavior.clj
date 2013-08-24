(ns ^:shared frontend.behavior
    (:require [clojure.string :as string]
              [io.pedestal.app.messages :as msg]
              
              [io.pedestal.app :as app]))

(defn set-value-transform [old-value message]
  (:value message))

(defn set-user-transform [old-value message]
  (:value message))


(defn inc-transform [old-value _]
  ((fnil inc 0) old-value))


(defn swap-transform [_ message]
  (:value message))

(defn init-main [_]
  [
   [:transform-enable [:main :my-counter] :inc [{msg/topic [:my-counter]}]]])

(def example-app
  {:version 2
   
   :transform [
               ; check these later
               [:set-value [:user] set-user-transform]
               [:set-value [:greeting] set-value-transform]
               
               [:inc [:my-counter] inc-transform]
               [:swap [:**]         swap-transform]
               ]  
   
   :emit [{:init init-main}
           [#{[:my-counter] [:other-counters :*]} (app/default-emitter [:main])]
           [#{[:*]} (app/default-emitter [:main])]
           
          ;[#{[:*]} (app/default-emitter [:main])]
          
          ] 
   })

