#lang racket

; Exercise 1
; part(a)
(define (sum-numbers numbers)
  (if (empty? numbers)
      0
      (+ (car numbers) (sum-numbers (cdr numbers))))
  )

; part(b)
(define (average numbers) (/ (sum-numbers numbers) (length numbers)))


; Exercise 2

(define (occurrences numbers n)
  (define (occurrences-iter l count)
    (cond
      [(empty? l) count]
      [(= n (car l)) (occurrences-iter (cdr l) (+ 1 count))]
      [else (occurrences-iter (cdr l) count)]
     ))
  (occurrences-iter numbers 0)
)


; Exercise 3

(define (convert numbers)
  (if (empty? numbers)
      0
      (+ (first numbers) (* 10 (convert (cdr numbers)))))
  )


; Exercise 4

(define (conversion f)(* (- f 32) (/ 5 9)))

(define (convertFC farenheit)
  (if (empty? farenheit)
      '()
      (cons (conversion (car farenheit)) (convertFC (cdr farenheit)))
   )
)


; Exercise 5

(define (eliminate-threshold nums n)
  (cond
    [(empty? nums) '()]
    [(<= (car nums) n) (cons (car nums) (eliminate-threshold (cdr nums) n))]
    [else (eliminate-threshold (cdr nums) n)]
  )
)


;Test Cases
(display "Testing sum-numbers")
(newline)
(display "Expected: 0, actual: ")
(sum-numbers empty)
(display "Expected: 21, actual: ")
(sum-numbers (list 1 2 3 4 5 6))
(newline)

(display "Testing average")
(newline)
(display "Expected: 3.5, actual: ")
(average (list 1 2 3 4 5 6))
(newline)

(display "Testing occurrences")
(newline)
(display "Expected: 3, actual: ")
(occurrences '(1 3 5 2 7 5 8 9 5) 5)
(display "Expected: 0, actual: ")
(occurrences '(1 3 5 2 7 5 8 9 5) 6)
(display "Expected: 0, actual: ")
(occurrences empty 1)
(newline)

(display "Testing convert")
(newline)
(display "Expected: 0, actual: ")
(convert empty)
(display "Expected: 3, actual: ")
(convert (list 3))
(display "Expected: 543, actual: ")
(convert (list 3 4 5))
(newline)

(display "Testing convertFC")
(newline)
(display "Expected: '(), actual: ")
(convertFC empty)
(display "Expected: '(0 100 37.0), actual: ")
(convertFC (list 32 212 98.6))
(newline)

(display "Testing eliminate-threshold")
(newline)
(display "Expected: '(1 2 3 4 4 3 2 1), actual: ")
(eliminate-threshold (list 1 2 3 4 5 6 5 4 3 2 1 20) 4)
(display "Expected: '(), actual: ")
(eliminate-threshold (list 1 2 3 4 5 6 5 4 3 2 1 20) 0)
(display "Expected: '(1 2 3 4 5 6 5 4 3 2 1 20), actual: ")
(eliminate-threshold (list 1 2 3 4 5 6 5 4 3 2 1 20) 25)
(newline)