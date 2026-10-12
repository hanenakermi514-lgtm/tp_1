COMPTE RENDU
TP1 – Injection de dépendances
Couplage faible, Inversion de Contrôle (IoC) et framework Spring
Projet Maven : TP1 (IntelliJ IDEA)
Packages : presentation, metier, dao
Technologies : Java, Maven, Spring 6.1.14
Nom et prénom : Hanen Akermi
Date : 12/10/2026

Objectif du TP
Ce TP explore le couplage faible et le principe d'Inversion de Contrôle (IoC) à travers
quatre approches successives d'injection de dépendances : instanciation statique,
instanciation dynamique par réflexion (Class.forName), conteneur Spring configuré en XML,
puis conteneur Spring configuré par annotations.
Structure du projet

Le projet Maven respecte l'organisation MVC demandée : un package dao (IDao, DaoIMP), un
package metier (IMetier, MetierIMP) et un package presentation contenant les quatre
classes de test. Les fichiers config.txt et config.xml se trouvent dans
src/main/resources.

Dépendances Maven (pom.xml)
Les trois dépendances Spring demandées (spring-core, spring-beans, spring-context)
ont été ajoutées au pom.xml, puis les changements Maven ont été rechargés.

Activité 1-1 : Injection par instanciation statique
Les interfaces et classes de la couche DAO et de la couche métier sont d'abord créées.
MetierIMP ne dépend que de l'interface IDao et reçoit son DAO via un setter.
Couche DAO
Figure
