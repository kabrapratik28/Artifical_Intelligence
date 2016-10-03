symptom(ram,runny_nose).
symptom(sham,fever).
symptom(sham,rash).
symptom(sham,runny_nose).


is_patient_measles(X):-
    symptom(X,fever),
    symptom(X,runny_nose),
    symptom(X,rash).

is_patient_flu(X):-
    symptom(X,fever),
    symptom(X,chills),
    symptom(X,headche),
    symptom(X,cough),
    symptom(X,runny_nose),
    symptom(X,rash).

is_patient_chicken_pox(X):-
    symptom(X,fever),
    symptom(X,chills),
    symptom(X,rash).



