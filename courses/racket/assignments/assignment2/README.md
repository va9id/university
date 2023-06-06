# Assignment 2: Racket Interpreter Design and Implementation

*assignment2* refers to the Racket program that interprets a subset of Racket. File `assignment2.rkt` contains the interpreter developed at UC Berkeley. File `assignment2_trace.rkt` contains the same interpreter, but it has been modified to display information that helps us trace its execution as it evaluates expressions.

## Exercises

### Exercise 5

Evaluate the following expressions in the Racket console and note the values that are displayed:

- (quote 1)
- (quote (1 2 3))
- (quote 1 2 3)

Evaluate the same three expressions in `assignment2.rkt`. The manner in which the `assignment2` interpreter evaluates quote expressions with multiple arguments is broken. Modify it to match the same output as Racket (displays an error message and terminates when it evaluates a quote expression with multiple arguments). 

### Exercise 6

Write a `map-1` primitive for `assignment2`. For example:

```
> (map-1 (lambda (x) (* x x)) '(1 2 3 4))
'(1 4 9 16)
```

### Exercise 7

Racket's **and** form provides a way of combining expressions with the following syntax:

-  (and expr1 expr2 expr3 ...)

An **and** form produces **#f** if any of its expressions produces #f. Otherwise, it produces the value of its last expression. If no expressions are provided, the result is **#t**. If a single expression is provided, the result is that of the expression. Otherwise, the first expression is evaluated. If it produces **#f**, the result is **#f**, and the remaining expressions are not evaluated. If evaluating the first expression produces **#t**, the result is the same as an and expression containing all the remaining expressions. Refer to the following examples: 

```
> (and #f (error "doesn't get here"))
#f
> (and #t 5) 
5
> (and (= 5 (+ 2 4)) (* 3 4))
#f  ; (* 3 4) is not evaluated
> (and (= 5 (+ 2 3)) (* 3 4))
12
> (and (= 5 (+ 2 3)) (< 6 (* 4 3)))
#t
> (and (= 5 (+ 2 4)) (< 6 (* 4 3)))
#f  ; (< 6 (* 4 3)) is not evaluated
```

Modify the `assignment2` interpreter to add the **and** special form (as soon as a **#f** value is computed, returns **#f** without evaluating any further expressions).

## Usage

- Run `assignment2.rkt` in DrRacket and invoke the REPL function in the console

    ```
    (assigment2)
    ```
- Copy each test case from `test_cases.rkt` into the input box of `assignment2`'s console
