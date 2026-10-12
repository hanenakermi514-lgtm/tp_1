package presentation;

import dao.DaoIMP;
import dao.IDao;
import metier.IMetier;
import metier.MetierIMP;

public class PresentationStatique {
    public static void main(String[] args) {
        IDao dao = new DaoIMP(); // instanciation statique du DAO
        IMetier metier = new MetierIMP(); // instanciation statique du métier

        ((MetierIMP) metier).setDao(dao); // INJECTION de dépendance

        System.out.println(metier.calcul());
    }
}
