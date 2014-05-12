(ns naptime.core
  (:gen-class :main true)
  (require [clj-http.client :as http])
  (require [clojure.data.json :as json]))

;;;; constants
(def nap-message "Someone in the house is taking a nap. Shh...")

;; relative path from project root
(def people-file "people.json")

(def people
  (:people (json/read-str 
           (slurp people-file) :key-fn keyword)))

(def textbelt-uri "http://textbelt.com/text")

(defn send-sms [phone message]
  "Send an SMS message to a phone number."
  (http/post textbelt-uri {:form-params {:number phone :message message}}))

(defn -main []
  "Send nap-message to each number in phone-numbers."
  (doall
   (for [person people]
     (send-sms (:phone person) nap-message))))  