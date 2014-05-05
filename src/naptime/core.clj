(ns naptime.core
  (:gen-class :main true)
  (require [clj-http.client :as http])
  (require [clojure.data.json :as json]))


(defn -main []
  ; (println "Hello World!")


  ; Get the phone numbers from filesys
  (def getPhone (get-in (json/read-str (slurp "phone-numbers.json") :key-fn keyword) [:house-mates]))
  
  ; Sends the msg
  (def sendMsg (fn [nmbr msg]
    (http/post "http://textbelt.com/text" {:form-params {:number nmbr :message msg}} )
    )
  )

  ; For each phone number in phone list, send msg
  
  (sendMsg "4147046006" "test")
  (sendMsg "6305894634" "test: I'm taking a nap. Clojure app")
  
  ; (doall (for [x  getPhone] 
  ;   (sendMsg "4147046006" "test")))
  

  (println getPhone)
)