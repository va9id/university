#lang racket

(define (calc)
  (display "calc: ")
  (flush-output)
  (print (calc-eval (read)))
  (newline)
  (calc))

; Evaluate an expression:

(define (calc-eval exp)
  (cond ((number? exp) exp)
        ((list? exp) (calc-apply (car exp) (map calc-eval (cdr exp))))
        (else (error "Calc: bad expression: " exp))))

; Apply a function to arguments:

(define (calc-apply fn args)
  (cond ((eq? fn '+) (foldr + 0 args))
        ((eq? fn '-) (cond ((null? args) (error "Calc: no args to -"))
                           ((= (length args) 1) (- (car args)))
                           (else (- (car args) (foldr + 0 (cdr args))))))
        ((eq? fn '*) (foldr * 1 args))
        ((eq? fn '/) (cond ((null? args) (error "Calc: no args to /"))
                           ((= (length args) 1) (/ (car args)))
                           (else (/ (car args) (foldr * 1 (cdr args))))))
        ; Exercise 1
        ((eq? fn 'sqrt) (if (= (length args) 1)
                          (sqrt (car args))
                          (error "Calc: sqrt requires exactly 1 arg")))
        ; Exercise 2
        ((eq? fn '**) (if (= (length args) 2)
                          (expt (car args) (car (cdr args)))
                          (error "Calc: ** requires exactly 2 args")))
        ; Exercise 3
        ((eq? fn 'min) (if (> (length args) 0)
                           (foldr min (car args) (cdr args)) ; error
                           (error "Calc: min requires 1 or more args")))
                        
        (else (error "Calc: bad operator:" fn))))

(calc)

; Test Cases:
; Exercise 1:
; (sqrt 9) = 3
; (sqrt (+ (* 10 2) (- 10 5)) = 5
; Exercise 2:
; (** 3 2) = 9
; (** (+ 1 1 1) 2) = 9
;
; Exercise 3:
; (min 1 2 3) = 1
; (min (+ 2 1) (* 3 2) (- 4 2)) = 2

; All combined functions: 
; (+ 3 (sqrt -4) (** 2 3) (min 4 6 5) (sqrt (* -2 3))) = 15 + 4.49i
; (+ 3 (sqrt 4) (** 2 3) (min 4 6 5) (sqrt (* 3 3))) = 20