(ns naptime.core
  (:gen-class :main true)
  (:require [clj-http.client :as http]
            [clojure.data.json :as json]
            [clojure.tools.cli :refer [parse-opts]]
            [environ.core :refer [env]]
            [clojure.string :as string]))

;;;; Constants
;; relative path from project root
(def people-file "people.json")

(def people
  (:people (json/read-str 
           (slurp people-file) :key-fn keyword)))

(def textbelt-uri "http://textbelt.com/text")


;;;; Library functions
(defn nap-message [name]
  (format "%s is taking a nap. Shh..." name))

(defn wake-up-message [name]
  (format "%s is awake! Party!" name))

(defn send-sms [phone message]
  "Send an SMS message to a phone number."
  (http/post textbelt-uri {:form-params {:number phone :message message}}))

(defn tell-everyone [message]
  "Send a message to everyone."
  (doall
   (for [person people]
     (send-sms (:phone person) message))))


;;;; Tasks
(defn nap [options]
  "Send a message to everyone, telling them you're napping"
  (tell-everyone (nap-message (:name options))))

(defn wake-up [options]
  "Send a message to everyone, telling them you're napping"
  (tell-everyone (wake-up-message (:name options))))


;;;; Driver
(def cli-options
  [["-n" "--name NAME" "Name of the napper"
    :default (env :user)]])

(defn usage [options-summary]
  (->> ["Send a message to your friends telling them you are napping"
        ""
        "Usage: naptime [options] action"
        ""
        "Options:"
        options-summary
        ""
        "Actions:"
        "  nap      Send a message saying you're taking a nap."
        "  wake-up  Send a message saying you've woken up."]
       (string/join \newline)))

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (string/join \newline errors)))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn -main [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    ;; Handle help and error conditions
    (cond
      (:help options) (exit 0 (usage summary))
      (not= (count arguments) 1) (exit 1 (usage summary))
      errors (exit 1 (error-msg errors)))
    ;; Execute program with options
    (case (first arguments)
      "nap" (nap options)
      "wake-up" (wake-up options)
      (exit 1 (usage summary)))))
