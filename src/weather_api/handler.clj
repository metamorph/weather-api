(ns weather-api.handler
  (:require [clj-http.client :as http]
            [compojure
             [core :refer :all]
             [route :as route]]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]))



(defn kelvin->celcius [k] (- k 273.15))
(defn extract-weather [data]
  (let [entries (:weather data)
    fn-1 #(format "%s (%s)" (:main %1) (:description %1))]
  (clojure.string/join ", " (map fn-1 entries))))
(defn extract-wind [data]
  (format "%s m/s, %s grader"
          (get-in data [:wind :speed])
          (get-in data [:wind :deg])))
(defn extract-temp [data]
  (let [min-temp (kelvin->celcius (get-in data [:main :temp_min]))
        max-temp (kelvin->celcius (get-in data [:main :temp_max]))
        main-temp (kelvin->celcius (get-in data [:main :temp]))]
    (format "%sC (min: %s, max: %s)" main-temp min-temp max-temp)))
(defn format-weather [data]
  (format "%s, %s, %s" (extract-weather data) (extract-temp data) (extract-wind data)))



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
