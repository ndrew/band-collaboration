(ns frontend.html-templates
  (:use [io.pedestal.app.templates :only [tfn dtfn tnodes]]))

(defmacro frontend-templates
  []
  {:frontend-page (dtfn (tnodes "frontend.html" "frontend" [[:#other-counters]]) #{:id})
   
   :other-counter (dtfn (tnodes "frontend.html" "other-counter") #{:id})
   })
