(ns naptime.core
  (:gen-class :main true)
  (require [clj-http.client :as http])
  (require [clojure.data.json :as json]))


(defn -main []
  ; (println "Hello World!")


  ; Get the phone numbers from filesys
  (def getPhone (get-in (json/read-str (slurp "phone-numbers.json") :key-fn keyword) [:house-mates]))
  
  ; Sends the msg
  (def sendMsg [nmbr msg]
    (http/post "http://textbelt.com/text" {:form-params {:number nmbr :message msg}} )

  ; For each phone number in phone list, send msg

  ; (http/post "http://textbelt.com/text" {:form-params {:number "4147046006" :message "henry sending sms through clojure lein"}} )
  ;(http/post "http://textbelt.com/text" {:form-params {:number "4147046006" :message "henry sending sms through clojure lein"}} )
  ;(http/post "http://textbelt.com/text" {:form-params {:number "4147046006" :message "henry sending sms through clojure lein"}} )
  

  (doall (for [x getPhone] 
    (println x)))
  

  (println getPhone)
)

; (http/get "http://google.com")