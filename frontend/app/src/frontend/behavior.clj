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


(defn publish-counter [count]
  [{msg/type :swap msg/topic [:other-counters] :value count}])

(defn total-count [_ nums] (apply + nums))

; derived stuff

(defn maximum [old-value nums]
  (apply max (or old-value 0) nums))

(defn average-count [_ {:keys [total nums]}]
  (/ total (count nums)))

(defn merge-counters [_ {:keys [me others]}]
  (assoc others "Me" me))

(defn init-main [_]
  [
   [:transform-enable [:main :my-counter] :inc [{msg/topic [:my-counter]}]]])


(defn init-login [_]
  [{:login
    {:name
     {:transforms
      {:login [{msg/type :swap msg/topic [:login :name] (msg/param :value) {}}
               {msg/topic msg/app-model msg/type :set-focus :name :dashboard} 
               ]}}}}])


(def example-app
  {:version 2
   
   :transform [
               ; check these later
               [:set-value [:user] set-user-transform]
               [:set-value [:greeting] set-value-transform]
                
               [:set-value [:login :*] set-value-transform]

               
                              
               [:inc [:my-counter] inc-transform]
               [:swap [:**]         swap-transform]
               ]  
   
   :emit [{:init init-login}
          [#{[:login :*]} (app/default-emitter [])]
          
          {:init init-main}
           [#{[:my-counter]
              [:other-counters :*]
              [:total-count]
              [:max-count]
              [:average-count]} (app/default-emitter [:main])]
           
           [#{[:*]} (app/default-emitter [:main])]
           ]
   
   :effect #{[#{[:my-counter]} publish-counter :single-val]}
    
    
   :derive #{[{[:my-counter] :me [:other-counters] :others} [:counters] merge-counters :map]
          [#{[:counters :*]} [:total-count] total-count :vals]
          [#{[:counters :*]} [:max-count] maximum :vals]
          [{[:counters :*] :nums [:total-count] :total} [:average-count] average-count :map]}
    
   
   :focus {:login [[:login]]
        :dashboard  [[:main] 
                     [:pedestal]]
        :default :login}
   
   })

