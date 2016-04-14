(ns weather-api.handler
  (:require [clj-http.client :as http]
            [compojure
             [core :refer :all]
             [route :as route]]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]))

(defn get-greeting
  "Get a greeting from the server" [name]
  (let [response (http/get "http://localhost:4567/hello"
                           {:as :json
                            :query-params {:name name}})]
    (get-in response [:body :message])))

(defroutes app-routes
  (GET "/" [] "Hello World")
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
