(ns frontend.html-templates
  (:use [io.pedestal.app.templates :only [tfn dtfn tnodes]]))

(defmacro frontend-templates
  []
  {:frontend-page (dtfn (tnodes "frontend.html" "hello") #{:id})})
