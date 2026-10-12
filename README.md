## COMPTE RENDU

TP1 – Injection de dépendances Couplage faible, Inversion de Contrôle (IoC) et framework Spring

**Nom et prénom : Hanen Akermi**
**Date : 12/10/2026**


## 1. Objectif du TP

Ce TP explore le **couplage faible** et le principe d'**Inversion de Contrôle (IoC)** à travers quatre approches d'injection de dépendances, de la plus manuelle à la plus automatisée :

| Activité | Approche | Qui crée et relie les objets ? |
| 1-1 | Instanciation statique (`new`) | Le programmeur, dans la classe de présentation |
| 1-2 | Instanciation dynamique (`Class.forName`) | Le code de réflexion, d'après `config.txt` |
| 1-3 | Spring avec XML | Le conteneur Spring, d'après `config.xml` |
| 1-4 | Spring avec annotations | Le conteneur Spring, par scan des annotations |

---

## 2. Structure du projet

Le projet Maven **TP1** respecte l'organisation MVC avec trois packages : `dao`, `metier` et `presentation`.

```
TP1/
├── pom.xml
└── src/main/
    ├── java/
    │   ├── dao/            IDao, DaoIMP
    │   ├── metier/         IMetier, MetierIMP
    │   └── presentation/   PresentationStatique
    │                       PresentationDynamique
    │                       PresentationAvecSpring
    │                       PresentationAvecAnnotation
    └── resources/          config.txt, config.xml
```

<img width="492" height="572" alt="Capture d&#39;écran 2026-10-12 123437" src="https://github.com/user-attachments/assets/7ba75c9c-1d84-4d57-be02-9ab175aec418" />
[Arborescence du projet]

---

## 3. Dépendances Maven

Trois dépendances Spring sont ajoutées au `pom.xml` : **spring-core**, **spring-beans** et **spring-context**.

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-core</artifactId>
        <version>6.1.14</version>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-beans</artifactId>
        <version>6.1.14</version>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>6.1.14</version>
    </dependency>
</dependencies>
```

<img width="618" height="532" alt="Capture d&#39;écran 2026-10-12 120952" src="https://github.com/user-attachments/assets/131315fe-def3-42ec-818e-6656b70f9dac" />

[pom.xml]

> Après modification du `pom.xml`, il faut recharger Maven (**Reload All Maven Projects**) pour télécharger les dépendances.

---

## 4. Couches DAO et métier

### 4.1 Package `dao`

**`IDao`** : interface qui définit le contrat d'accès aux données.

```java
package dao;

public interface IDao {
    double getValue();
}
```

<img width="1183" height="312" alt="Capture d&#39;écran 2026-10-12 115155" src="https://github.com/user-attachments/assets/4e1e2cf1-3b4a-4edf-92dc-570d5b11802d" />

![IDao]


**`DaoIMP`** : implémentation qui simule un accès aux données et retourne `3`.

```java
package dao;

public class DaoIMP implements IDao {
    @Override
    public double getValue() {
        return 3; // simulation d'un accès aux données
    }
}
```

<img width="1370" height="496" alt="Capture d&#39;écran 2026-10-12 115232" src="https://github.com/user-attachments/assets/a313567f-ab4c-4652-98d7-8118f7f83a08" />

![DaoIMP]

### 4.2 Package `metier`

**`IMetier`** : interface de la couche métier.

```java
package metier;

public interface IMetier {
    double calcul();
}
```

<img width="1347" height="556" alt="Capture d&#39;écran 2026-10-12 115245" src="https://github.com/user-attachments/assets/4e8a754c-fbab-49bd-a44b-ec0e36a38414" />

![IMetier]

**`MetierIMP`** : dépend de l'**interface** `IDao` (jamais de `DaoIMP`). La dépendance lui est fournie par le setter `setDao`. Le calcul multiplie la valeur du DAO par 7.

```java
package metier;

import dao.IDao;

public class MetierIMP implements IMetier {

    private IDao dao; // dépendance vers l'INTERFACE : couplage faible

    @Override
    public double calcul() {
        double val = dao.getValue();
        return val * 7;
    }

    // Setter utilisé pour l'injection
    public void setDao(IDao dao) {
        this.dao = dao;
    }
}
```


<img width="1182" height="620" alt="Capture d&#39;écran 2026-10-12 115310" src="https://github.com/user-attachments/assets/baf1d3aa-789c-438a-aded-062f197111c9" />

![MetierIMP]

**Résultat attendu pour toutes les activités :** `3 × 7 = 21.0`
---

## 5. Activité 1-1 : instanciation statique

**Principe :** la classe `PresentationStatique` crée elle-même les objets avec `new`, puis injecte le DAO dans le métier par le setter.

```java
package presentation;

import dao.DaoIMP;
import dao.IDao;
import metier.IMetier;
import metier.MetierIMP;

public class PresentationStatique {
    public static void main(String[] args) {
        IDao dao = new DaoIMP();           // instanciation statique du DAO
        IMetier metier = new MetierIMP();  // instanciation statique du métier

        ((MetierIMP) metier).setDao(dao);  // INJECTION de dépendance

        System.out.println(metier.calcul());
    }
}
```

<img width="1053" height="591" alt="Capture d&#39;écran 2026-10-12 115331" src="https://github.com/user-attachments/assets/2ba588ea-77e0-4db7-a716-91f777702364" />

![PresentationStatique]
**Exécution :**

<img width="1865" height="302" alt="Capture d&#39;écran 2026-10-12 115504" src="https://github.com/user-attachments/assets/f3e67e1b-e74c-490a-8b4a-394eb9e0ec1a" />

![Résultat statique]

**À retenir :**
- Il y a **couplage faible** entre `MetierIMP` et `DaoIMP` : le métier ne connaît que l'interface `IDao`.
- Mais la classe de présentation reste **couplée** aux classes concrètes (`new DaoIMP()`, `new MetierIMP()`) : changer d'implémentation oblige à modifier et recompiler le code.
- L'inversion de contrôle n'est donc que **partielle**.

---

## 6. Activité 1-2 : instanciation dynamique

**Principe :** les noms des classes sont lus dans un fichier de configuration, chargés avec `Class.forName`, instanciés par réflexion, puis liés par l'appel réflexif de `setDao`. Le code ne contient plus aucun `new DaoIMP()` ni `new MetierIMP()`.

**`src/main/resources/config.txt`** :

```
dao.DaoIMP
metier.MetierIMP
```

<img width="1326" height="452" alt="Capture d&#39;écran 2026-10-12 115616" src="https://github.com/user-attachments/assets/6d29a960-f648-4c13-a33c-eaab289a6701" />

![config.txt]

**`PresentationDynamique`** :

<img width="1192" height="817" alt="Capture d&#39;écran 2026-10-12 115720" src="https://github.com/user-attachments/assets/d4c87889-92cb-40bc-8ec9-b81c52fedfef" />

![PresentationDynamique]

Les étapes du programme :
1. Lire `config.txt` depuis le classpath avec `getResourceAsStream`.
2. Récupérer les noms de classes `dao.DaoIMP` et `metier.MetierIMP`.
3. Charger les classes avec `Class.forName(...)` et créer les instances avec `getDeclaredConstructor().newInstance()`.
4. Appeler `setDao` par réflexion pour injecter le DAO dans le métier.
5. Appeler `calcul()` et afficher le résultat.

**Exécution :**

<img width="1052" height="355" alt="Capture d&#39;écran 2026-10-12 115809" src="https://github.com/user-attachments/assets/8eaf97e1-8cf6-4159-aeaf-851a0aca0931" />

![Résultat dynamique]

**Avantages**
|---|---|
 Aucune recompilation pour changer d'implémentation 
 Classes concrètes absentes du code de présentation 
 Application extensible 
 **Limites**
 |---|---|
 Code de réflexion lourd et verbeux 
  Erreurs détectées à l'exécution et non à la compilation 
 Configuration rudimentaire (ordre des lignes, pas de cycle de vie) 

> `config.txt` doit se trouver dans `src/main/resources`, sinon `getResourceAsStream` retourne `null`.

---

## 7. Activité 1-3 : Spring avec configuration XML

**Principe :** les beans et leurs dépendances sont déclarés dans `config.xml`. Le **conteneur Spring** crée les objets et réalise l'injection.

**`src/main/resources/config.xml`** :

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="d" class="dao.DaoIMP"/>

    <bean id="metier" class="metier.MetierIMP">
        <property name="dao" ref="d"/>   <!-- injection par setter -->
    </bean>
</beans>
```

**`PresentationAvecSpring`** charge le contexte avec `ClassPathXmlApplicationContext("config.xml")`, puis récupère le bean `metier` avec `getBean("metier", IMetier.class)`.

**Exécution :**

<img width="1877" height="503" alt="Capture d&#39;écran 2026-10-12 122932" src="https://github.com/user-attachments/assets/5ee0c5c8-3dc1-49ff-b265-83788e304eb7" />
![Résultat Spring XML]

**Différence avec l'activité 1-2 :** le câblage des dépendances n'est plus écrit à la main avec la réflexion, il est **déclaré** dans le XML et géré par Spring (singletons, cycle de vie, messages d'erreur clairs).

---

## 8. Activité 1-4 : Spring avec annotations

**Principe :** les classes sont déclarées comme composants Spring grâce aux annotations. Spring scanne les packages, crée les beans et injecte les dépendances automatiquement, sans fichier XML.

| Annotation | Rôle |
|---|---|
| `@Repository` | Marque `DaoIMP` comme composant de la couche d'accès aux données |
| `@Service` | Marque `MetierIMP` comme composant de la couche métier |
| `@Autowired` | Demande à Spring d'injecter automatiquement la dépendance de type `IDao` |

**`PresentationAvecAnnotation`** :

```java
ApplicationContext context =
        new AnnotationConfigApplicationContext("dao", "metier");

IMetier metier = context.getBean(IMetier.class);
System.out.println(metier.calcul());
```

**Exécution :**

<img width="1842" height="716" alt="Capture d&#39;écran 2026-10-12 123417" src="https://github.com/user-attachments/assets/d2fecfa4-adf8-4cda-ad5d-f7b6ff1ceedd" />
![Résultat annotations]

**Comment Spring procède :**
1. Il scanne les packages `dao` et `metier`.
2. Il détecte les classes annotées et crée un bean pour chacune.
3. Pour `MetierIMP`, il repère `@Autowired`, cherche un bean de type `IDao`, trouve `DaoIMP` et l'injecte.

Cette solution est plus maintenable que le XML : la configuration est à côté du code, il y a moins de risque d'erreur de frappe et le refactoring est géré par l'IDE.

---

## 9. Tableau comparatif

| Critère | 1-1 Statique | 1-2 Dynamique | 1-3 Spring XML | 1-4 Spring annotations |
|---|---|---|---|---|
| Création des objets | `new` dans le code | Réflexion | Conteneur Spring | Conteneur Spring |
| Choix des classes | Dans le code | `config.txt` | `config.xml` | Annotations |
| Recompilation pour changer | Oui | Non | Non | Oui (annotations dans le code) |
| Couplage de la présentation | Fort | Faible | Faible | Faible |
| Niveau d'IoC | Partiel | Moyen | Élevé | Complet |
| Résultat | `21.0` | `21.0` | `21.0` | `21.0` |

---

## 10. Conclusion

Les quatre activités montrent une progression vers un couplage de plus en plus faible : on passe d'un assemblage manuel dans la classe de présentation (1-1) à un assemblage entièrement délégué au conteneur Spring (1-4), en passant par la réflexion (1-2) et la configuration XML (1-3).

Dans les quatre cas le résultat est identique (**21.0**), mais la flexibilité, la maintenabilité et la testabilité du code augmentent à chaque étape.

