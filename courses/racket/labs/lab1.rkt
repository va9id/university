#lang racket

; Exercise 1
(define (sum-coins p n d q) (+ p (* n 5) (* d 10) (* q 25)))
(printf "Exercise #1\n")
(sum-coins 1 0 0 0); result should be 1
(sum-coins 0 1 0 0); result should be 15
(sum-coins 0 0 1 0); result should be 10
(sum-coins 0 0 0 1); result should be 25
(sum-coins 1 1 1 1); result should be 41

; Exercise 2
(define (interest b) (cond [(<= b 1000) (* (/ 4 100) b)]
                           [(> b 5000)(* (/ 5 100) b)]
                           [else (* (/ (/ 9 2) 100) b)]))
(printf "Exercise #2\n")
(interest 500); result should be 20
(interest 1000); result should be 40
(interest 2000); result should be 90
(interest 5000); result should be 225
(interest 10000); result should be 500

; Exercise 3
(define (balance b) (+ b (interest b)))
(printf "Exercise #3\n")
(balance 500); result should be 520
(balance 1000); result should be 1040
(balance 2000); result should be 2090
(balance 5000); result should be 5225
(balance 10000); result should be 10500

; Exercise 4
(define (variable_Interest b) (cond [(> b 5000)(+ (interest 1000) (interest 4000) (* (/ 5 100) (- b 5000)))]
                                    [(<= b 1000)(interest b)]
                                    [else (+ (interest 1000) (* (/ (/ 9 2) 100)(- b 1000)))]))
(printf "Exercise #4\n")
(variable_Interest 500); result should be 20
(variable_Interest 1000); result should be 40
(variable_Interest 2000); result should be 285
(variable_Interest 5000); result should be 220
(variable_Interest 10000); result should be 470