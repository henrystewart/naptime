(ns naptime.core
  (:gen-class :main true)
  (require [clj-http.client :as http])
  (require [clojure.data.json :as json]))


(defn -main []

  ; should this be a macro?
  (def NAPMSG "Someone in the house is taking a nap. Shh...")

  ; Get the phone numbers from filesys
  (def getPhone (get-in (json/read-str (slurp ".phone-numbers.json") :key-fn keyword) [:house-mates]))
  
  ; Sends the msg
  (def sendMsg (fn [nmbr msg]
    (http/post "http://textbelt.com/text" {:form-params {:number nmbr :message msg}} )
    )
  )

  ; ; For each phone number in phone list, send msg
  
  (doall (for [x  getPhone] 
    ; (sendMsg "4147046006" "test")
    (sendMsg (val x) NAPMSG)
    )
  )


)