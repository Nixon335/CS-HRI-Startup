(define (caar x) (car (car x)))
(define (cadr x) (car (cdr x)))
(define (cdar x) (cdr (car x)))
(define (cddr x) (cdr (cdr x)))
(define (cadar x) (car (cdr (car x))))

; Some utility functions that you may find useful to implement.
(define (map proc items)
  (if (null? items)
    nil
    (cons (proc (car items)) (map proc (cdr items)))
    )
  )

(define (cons-all first rests)
  (if (null? rests)
    nil
    (cons (cons first (car rests)) (cons-all first (cdr rests)))
    )
  )

(define (zip pairs)
  (if (null? pairs) 
      (list nil nil)
      (list (helper1 pairs) (helper2 pairs))))

(define (helper1 s)
  (if (null? s)
      nil
      (cons (caar s) (helper1 (cdr s)))))
   
(define (helper2 s)
  (if (null? s)
       nil
      (cons (cadar s) (helper2 (cdr s)))))

;; Problem 17
;; Returns a list of two-element lists
(define (enumerate s)
  ; BEGIN PROBLEM 17
  (define (enumerate_helper s index)
    (if (null? s)
      nil
      (cons (cons index (cons (car s) nil)) (enumerate_helper (cdr s) (+ index 1)))
      )
    )
  (enumerate_helper s 0)
  )
  ; END PROBLEM 17e

;; Problem 18
;; List all ways to make change for TOTAL with DENOMS
(define (list-change total denoms)
  ; BEGIN PROBLEM 18
  (cond
    ((null? denoms) nil)
    ((< total (car denoms)) (list-change total (cdr denoms)))
    ((= total (car denoms)) (cons (cons total nil) (list-change total (cdr denoms))))
    ((> total (car denoms)) (append (cons-all (car denoms) (list-change (- total (car denoms)) denoms)) 
                            (list-change total (cdr denoms)) ))
    )
  )
  ; END PROBLEM 18

;; Problem 19
;; Returns a function that checks if an expression is the special form FORM
(define (check-special form)
  (lambda (expr) (equal? form (car expr))))

(define lambda? (check-special 'lambda))
(define define? (check-special 'define))
(define quoted? (check-special 'quote))
(define let?    (check-special 'let))

;; Converts all let special forms in EXPR into equivalent forms using lambda
(define (let-to-lambda expr)
  (cond ((atom? expr)
         ; BEGIN PROBLEM 19
         expr
         ; END PROBLEM 19
         )
        ((quoted? expr)
         ; BEGIN PROBLEM 19
         expr
         ; END PROBLEM 19
         )
        ((or (lambda? expr)
             (define? expr))
         (let ((form   (car expr))
               (params (cadr expr))
               (body   (cddr expr)))
           ; BEGIN PROBLEM 19
           (cons form (cons params (map let-to-lambda body)))
           ; END PROBLEM 19
           ))
        ((let? expr)
         (let ((values (cadr expr))
               (body   (cddr expr)))
           ; BEGIN PROBLEM 19
           (define formals (map (lambda (x) (car x)) values))
           (define values (map (lambda (x) (let-to-lambda (cadr x))) values))
           (cons (cons 'lambda (cons formals (map let-to-lambda body)))values)
           ; END PROBLEM 19
           ))
        (else
         ; BEGIN PROBLEM 19
         (map let-to-lambda expr)
         ; END PROBLEM 19
         )))
