#lang racket
(require racket/trace)
; Exercise 1

(define (build-naturals n)
  (build-list n (lambda (x) x))
)

(define (build-rationals n)
  (build-list n (lambda (x) (/ 1 (+ x 1))))
) 

(define (build-evens n)
  (build-list n (lambda (x) (* x 2)))              
)

; Exercise 2

(define (cubic a b c) (lambda (x) (+ (* x x x) (* a (* x x)) (* b x) c)))

; Exercise 3

(define (twice fn) (lambda (x) (fn (fn x))))

(define (square x) (* x x))
(define (inc x) (+ x 1)) 

; Tests

;Test Cases
(display "Testing build-naturals")
(newline)
(display "Expected: '(0 1 2 3 4), actual: ")
(build-naturals 5)
(newline)

(display "Testing build-rationals")
(newline)
(display "Expected: '(1 1/2 1/3 1/4 1/5), actual: ")
(build-rationals 5)
(newline)

(display "Testing build-evens")
(newline)
(display "Expected: '(0 2 4 6 8), actual: ")
(build-evens 5)
(newline)

(display "Testing cubic")
(newline)
(display "Expected: 91, actual: ")
((cubic 1 2 3) 4)
(newline)

(display "Testing twice")
(newline)
(display "Expected: 625, actual: ")
((twice square) 5) 
(newline)
(display "Expected: 7, actual: ")
((twice square) 5) 