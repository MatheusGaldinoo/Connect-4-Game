;; Connect Four - Functional Approach - Matheus Galdino 

(defn printScoreboard [victories]
    (println (str "| ScoreBoard: " (first victories) " X " (second victories) "  |"))
    (println "----------------------"))

(defn color [player]
    (if (= player 1) "🟡"
                     "🔴"))

(defn printGrid [grid]
    (doseq [lin (range 6)]
        (doseq [col (range 7)]
            (cond
                (= 1 (get-in grid [lin col])) (print "|🟡")
                (= 2 (get-in grid [lin col])) (print "|🔴")
                :else (print "|  ")))
        (println "|"))
    (println "----------------------")
    (println "| 1| 2| 3| 4| 5| 6| 7|")
    (println))


(defn updateGrid [grid col player]
    (loop [lin 5]
        (if (>= lin 0)
            (if (= 0 (get-in grid [lin col]))
                [(assoc-in grid [lin col] player) lin]
                (recur (dec lin)))
            [-1 -1])))

(defn isColValid [grid col]
    (and (>= col 0) (< col 7) (= (get-in grid [0 col]) 0)))

(defn checkLine [grid lin col player]
  (let [checkLeft (loop [aux (dec col) count 0]
                    (if (and (>= aux 0) (= player (get-in grid [lin aux])))
                      (recur (dec aux) (inc count))
                      count))
        checkRight (loop [aux (inc col) count 0]
                    (if (and (< aux 7) (= player (get-in grid [lin aux])))
                      (recur (inc aux) (inc count))
                      count))]
    (+ checkLeft checkRight 1)))

(defn checkColumn 
    ([grid lin col player] (checkColumn grid lin col player 0))
    ([grid lin col player count]
        (if (and (< lin 6) (= player (get-in grid [lin col])))
            (checkColumn grid (inc lin) col player (inc count))
            count)))

(defn checkDiagonal1 [grid lin col player]
  (let [checkTopLeft (loop [aux 0 count 0]
                       (if (and (>= (- lin aux) 0) (>= (- col aux) 0) 
                                (= (get-in grid [(- lin aux) (- col aux)]) player))
                         (recur (inc aux) (inc count))
                         count))
        checkBottomRight (loop [aux 1 count 0]
                          (if (and (< (+ lin aux) 6) (< (+ col aux) 7) 
                                   (= (get-in grid [(+ lin aux) (+ col aux)]) player))
                            (recur (inc aux) (inc count)) 
                            count))]
    (+ checkTopLeft checkBottomRight)))

(defn checkDiagonal2 [grid lin col player]
  (let [checkBottomLeft (loop [aux 0 count 0]
                          (if (and (< (+ lin aux) 6) (>= (- col aux) 0) 
                                   (= (get-in grid [(+ lin aux) (- col aux)]) player))
                            (recur (inc aux) (inc count))
                            count))
        checkTopRight (loop [aux 1 count 0]
                       (if (and (>= (- lin aux) 0) (< (+ col aux) 7) 
                                (= (get-in grid [(- lin aux) (+ col aux)]) player))
                         (recur (inc aux) (inc count)) 
                         count))]
    (+ checkBottomLeft checkTopRight)))

(defn hasWinner [grid lin col player]
    (or (= 4 (checkLine grid lin col player)) 
        (= 4 (checkColumn grid lin col player)) 
        (= 4 (checkDiagonal1 grid lin col player)) 
        (= 4 (checkDiagonal2 grid lin col player))))

(defn addVictory [victories player]
    (println (str "Player " player " wins!"))
    (if (= player 1) [(inc (first victories)) (second victories)]
        [(first victories) (inc (second victories))]))

(defn isFull [grid]
    (every? #(not= 0 %) (map #(get-in grid [0 %]) (range 7))))

(defn changePlayer [player]
    (if (= 1 player) 2 
        1))

(defn startRound [DEFAULT_GRID victories player]
  (loop [grid (mapv vec DEFAULT_GRID) 
         player player]

    (println)
    (printGrid grid)
    
    (println (str "Which column do you want to put " (color player) "? (1-7) or 0 for resignation: "))
    (let [move (try
                 (dec (Integer/parseInt (read-line)))
                 (catch Exception e
                   (println "INVALID MOVE!\n")
                   -2))]

      (cond
        (or (< move -1) (> move 6) (and (>= move 0) (not (isColValid grid move))))
          (do
            (println "INVALID MOVE!\n")
            (recur grid player))

        (= move -1)
          (do
            (println)
            (println (str "Player " player " resigned!"))
            (addVictory victories (changePlayer player))
            (println))

        :else
          (let [[newGrid lin] (updateGrid grid move player)]
            (if (>= lin 0)
              (cond
                (hasWinner newGrid lin move player)
                  (do
                    (printGrid newGrid)
                    (addVictory victories player))

                (isFull newGrid)
                  (do
                    (printGrid newGrid)
                    (println "Draw!\n")
                    victories)

                :else
                  (recur newGrid (changePlayer player)))
              (do
                (println "Column is full! Choose another column.\n")
                (recur grid player))))))))

(defn startGame []
    (loop [DEFAULT_GRID [[0 0 0 0 0 0 0]
                         [0 0 0 0 0 0 0]
                         [0 0 0 0 0 0 0]
                         [0 0 0 0 0 0 0]
                         [0 0 0 0 0 0 0]
                         [0 0 0 0 0 0 0]]
            victories [0 0]
            continueGame true
            player 1]

        (if continueGame
            (do
                (println)
                (printScoreboard victories)
                (let [newVictories (startRound DEFAULT_GRID victories player)]
                    (println "Do you want to keep playing? ('y' to continue / any other to quit): ")
                    (if (not= "y" (read-line))
                        (do
                            (println "\nThe End!")
                            (recur [[0 0 0 0 0 0 0]
                                    [0 0 0 0 0 0 0]
                                    [0 0 0 0 0 0 0]
                                    [0 0 0 0 0 0 0]
                                    [0 0 0 0 0 0 0]
                                    [0 0 0 0 0 0 0]] 
                            newVictories 
                            false 
                            1))

                        (recur [[0 0 0 0 0 0 0]
                                [0 0 0 0 0 0 0]
                                [0 0 0 0 0 0 0]
                                [0 0 0 0 0 0 0]
                                [0 0 0 0 0 0 0]
                                [0 0 0 0 0 0 0]] 
                        newVictories 
                        true 
                        (changePlayer player))))))))

(startGame)
