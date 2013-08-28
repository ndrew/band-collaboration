(ns frontend.html-templates
  (:use [io.pedestal.app.templates :only [tfn dtfn tnodes]]))

(defmacro frontend-templates
  []
  {
   :login-page (tfn (tnodes "login.html" "login"))
   
   ;; app tutorial stuff
   :frontend-page (dtfn (tnodes "frontend.html" "frontend" [[:#other-counters]]) #{:id})
   :other-counter (dtfn (tnodes "frontend.html" "other-counter") #{:id})
   
   })
