male(shrikant).
male(pratik).
male(shamsundar).
male(manish).

female(jyoti).
female(pratiksha).
female(kanchan).
female(pooja).

parent(shrikant,pratik).
parent(shrikant,pratiksha).
parent(shamsundar,shrikant).
parent(kanchan,shrikant).
parent(shamsundar,manish).
parent(kanchan,manish).

father(shamsundar,shrikant).
father(shrikant,pratik).
father(shrikant,pratiksha).
father(shamsundar,manish).

mother(jyoti,pratik).
mother(jyoti,pratiksha).
mother(kanchan,shrikant).
mother(kanchan,manish).

wife(shamsundar,kanchan).
wife(shrikant,jyoti).
wife(manish,pooja).

% X = father   Y=son/daughter 
father(X,Y) :- male(X) , parent(X,Y).

% X = mother  Y=son/daughter
mother(X,Y) :- female(X) , parent(X,Y).

% X = brother  Y = bro/sis Z = common father
brother(X,Y) :- male(X) , father(Z,X) , father(Z,Y) , X \= Z.

% X = sister  Y = bro/sis Z = common father
sister(X,Y) :- female(X) , father(Z,X) , father(Z,Y), X \=Y.

% X = gradfather Y= child
grandfather(X,Y) :- father(Z,Y) , father(X,Z).

% X = grandmother Y=child
grandmother(X,Y) :- father(Z,Y) , mother(X,Z).
 
% X= uncle Y = child  (X!=Z because brother(father,father)) 
uncle(X,Y) :- father(Z,Y) , brother(X,Z) , X \= Z.

% X= aunty Y = child
aunty(X,Y) :- father(Z,Y) , brother(P,Z) , wife(P,X) , P \= Z.

