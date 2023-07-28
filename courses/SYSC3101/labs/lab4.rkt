#lang racket
; Exercise 3
(define (make-counter-with-let initial-count)
  (let ((counter initial-count))

    (define (count-up) (set! counter (+ counter 1)) counter)
    (define (count-down)
      (if (> counter 0)
          (begin (set! counter (- counter 1))
                 counter)
          "Counter is 0"))
    (define (dispatch cmd)
      (cond ((eq? cmd 'inc) count-up)
            ((eq? cmd 'dec) count-down)
            (else (error "Unknown command:" cmd))))
    dispatch))

;Exercise 4
(define (make-counter-ex4 initial-count)

  (let ((counter initial-count))
  
    (define (count-up)(set! counter (+ counter 1)) counter)
    (define (count-down)
      (if (> counter 0)
          (begin (set! counter (- counter 1))
                 counter)
          "Counter is 0"))
    (lambda (cmd)
      (cond [(eq? cmd 'inc) count-up]
            [(eq? cmd 'dec) count-down]
            [else (error "Unknown command")]))))

;Exercise 5
(define (make-counter-ex5 initial-count)
  (let ((counter initial-count))
  
    (define (count-up) (set! counter (+ counter 1)) counter)
    (define (count-reset) (set! counter (* 0 counter)) counter)
    (define (counter-get) counter)
    (define (count-down)
      (if (> counter 0)
          (begin (set! counter (- counter 1))
                 counter)
          "Counter is 0"))
    (lambda (cmd)
      (cond [(eq? cmd 'inc) count-up]
            [(eq? cmd 'dec) count-down]
            [(eq? cmd 'get) counter-get]
            [(eq? cmd 'reset) count-reset]
            [else (error "Unknown command")]))))

;Exercise 6
(define (make-counter-ex6 initial-count inc-value)
  (let ((counter initial-count))
  
    (define (count-up) (set! counter (+ counter inc-value)) counter)
    (define (count-reset) (set! counter (* 0 counter)) counter)
    (define (counter-get) counter)
    (define (count-down)
      (if (> counter 0)
          (begin (set! counter (- counter 1)) counter)
          "Counter is 0"))
    (lambda (cmd)
      (cond [(eq? cmd 'inc) count-up]
            [(eq? cmd 'dec) count-down]
            [(eq? cmd 'get) counter-get]
            [(eq? cmd 'reset) count-reset]
            [else (error "Unknown command")]))))

;Exercise 7
(define (make-counter-ex7 initial-count inc-value)
  (let ((counter initial-count) (water initial-count))
  
    (define (count-up)
      (begin (set! counter (+ counter inc-value))
             (set! water (+ water inc-value))
        ) counter)
    (define (count-reset)
      (begin (set! counter (* counter 0))
             (set! water (* water 0))
        ) counter)
    (define (counter-get) counter)
    (define (counter-max) water)
    (define (count-down)
      (if (> counter 0)
          (begin (set! counter (- counter 1)) counter)
          "Counter is 0"))
    (lambda (cmd)
      (cond [(eq? cmd 'inc) count-up]
            [(eq? cmd 'dec) count-down]
            [(eq? cmd 'get) counter-get]
            [(eq? cmd 'max) counter-max]
            [(eq? cmd 'reset) count-reset]
            [else (error "Unknown command")]))))

;Test Cases
(display "Test Cases")
(newline)
(define ex4 (make-counter-ex4 0))
(display "Exercise 4:")(newline)
(display "'inc: Expected: 1, actual: ")((ex4 'inc))
(display "'inc: Expected: 2, actual: ")((ex4 'inc))
(display "'dec: Expected: 1, actual: ")((ex4 'dec))
(display "'dec: Expected: 0, actual: ")((ex4 'dec))
(display "'dec: Expected: 'Counter is 0', actual: ")((ex4 'dec))
(newline)
(define ex5 (make-counter-ex5 0))
(display "Exercise 5:")(newline)
(display "'get: Expected: 0, actual: ")((ex5 'get))
(display "'inc: Expected: 1, actual: ")((ex5 'inc))
(display "'inc: Expected: 2, actual: ")((ex5 'inc))
(display "'dec: Expected: 1, actual: ")((ex5 'dec))
(display "'get: Expected: 1, actual: ")((ex5 'get))
(display "'reset: Expected: 0, actual: ")((ex5 'reset))
(display "'inc: Expected: 1, actual: ")((ex5 'inc))
(newline)
(define ex6 (make-counter-ex6 0 5))
(display "Exercise 6:")(newline)
(display "'get: Expected: 0, actual: ")((ex6 'get))
(display "'inc: Expected: 5, actual: ")((ex6 'inc))
(display "'inc: Expected: 10, actual: ")((ex6 'inc))
(display "'dec: Expected: 9, actual: ")((ex6 'dec))
(display "'inc: Expected: 14, actual: ")((ex6 'inc))
(display "'reset: Expected: 0, actual: ")((ex6 'reset))
(display "'inc: Expected: 5, actual: ")((ex6 'inc))
(newline)
(define ex7 (make-counter-ex7 0 2))
(display "Exercise 7:")(newline)
(display "'inc: Expected: 2, actual: ")((ex7 'inc))
(display "'inc: Expected: 4, actual: ")((ex7 'inc))
(display "'max: Expected: 4, actual: ")((ex7 'max))
(display "'inc: Expected: 6, actual: ")((ex7 'inc))
(display "'max: Expected: 6, actual: ")((ex7 'max))
(display "'dec: Expected: 5, actual: ")((ex7 'dec))
(display "'dec: Expected: 4, actual: ")((ex7 'dec))
(display "'max: Expected: 6, actual: ")((ex7 'max))
(display "'reset: Expected: 0, actual: ")((ex7 'reset))
(display "'max: Expected: 0, actual: ")((ex7 'max))