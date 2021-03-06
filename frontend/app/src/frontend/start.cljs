(ns frontend.start
  (:require [io.pedestal.app.protocols :as p]
            [io.pedestal.app :as app]
            [io.pedestal.app.render.push :as push-render]
            [io.pedestal.app.render :as render]
            [io.pedestal.app.messages :as msg]
            [frontend.behavior :as behavior]
            [frontend.rendering :as rendering]
            
            [frontend.post-processing :as post]
            [frontend.services :as services]
            ))


(defn create-app [render-config]
  (let [app (app/build (post/add-post-processors behavior/example-app))
        render-fn (push-render/renderer "content" render-config render/log-fn)
        app-model (render/consume-app-model app render-fn)]
    (app/begin app)


    (p/put-message (:input app) {msg/type :inc msg/topic [:my-counter]})

    ; start with default user
    (p/put-message (:input app) {msg/type :set-value msg/topic [:login :name] :value :default})
    
    (p/put-message (:input app) {msg/type :set-value msg/topic [:greeting] :value "Hello World!"})
     
    
        
    {:app app :app-model app-model}))


(defn ^:export main []
  (create-app (rendering/render-config)))
  
  (comment (let [app (create-app (rendering/render-config))
        services (services/->Services (:app app))]
    (app/consume-effects (:app app) services/services-fn)
    (p/start services)
    app))
