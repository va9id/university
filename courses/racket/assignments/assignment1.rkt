#lang racket
(require racket/trace)

; Exercise 1

(define (count-multiples nums n)
  (cond
    [(empty? nums) 0]
    [(= 0 (modulo (car nums) n)) (+ 1 (count-multiples (cdr nums) n))]
    [else (count-multiples (cdr nums) n)]
   ))


; Exercise 2

(define (count-multiples-iter nums n)
  (define (count-multiples-iter-helper nums count)
    (cond
      [(empty? nums) count]
      [(= 0 (modulo (car nums) n)) (count-multiples-iter-helper (cdr nums) (+ 1 count))]
      [else (count-multiples-iter-helper (cdr nums) count)]))
  (count-multiples-iter-helper nums 0)
)

; Exercise 3

(define (deep-list-remove fn nums)
  (if (empty? nums)
      '()
      (if (list? (car nums))
        (cons (deep-list-remove fn (car nums)) (deep-list-remove fn (cdr nums))) 
        (if (fn (car nums))
            (deep-list-remove fn (cdr nums))
            (cons (car nums) (deep-list-remove fn (cdr nums)))
        )  
      )
   )
)