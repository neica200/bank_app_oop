import com.pao.project.banca.model.*;
import com.pao.project.banca.service.*;
import com.pao.project.banca.exception.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
    //Initializare servicii
        ClientService clientService = ClientService.getInstance();
        ContService contService = ContService.getInstance();
        CardService cardService = CardService.getInstance();
        TranzactieService tranzactieService = TranzactieService.getInstance();

        System.out.println("SISTEM BANCAR - DEMO ACTIUNI ");

        try {
            //1.Creeaza un client nou
            Client c1 = new Client( "Ionescu", "Andrei", "andrei@pao.ro");
            clientService.adaugaClient(c1);

            //2.Deschide un cont bancar
            IBAN ibanC = new IBAN("RO-CUR-001");
            IBAN ibanE = new IBAN("RO-ECO-002");
            Cont contCurent = new ContCurent(ibanC, c1, 1000.0); // Overdraft 1000
            Cont contEconomii = new ContEconomii(ibanE, c1, 5.0); // Dobanda 5%
            contService.adaugaCont(contCurent);
            contService.adaugaCont(contEconomii);

            //3.Depune bani intr-un cont
            contCurent.depune(5000);
            System.out.println("Dupa depunere, sold: " + contCurent.getSold());

            // 4.Adauga un card la un cont
            Card card = new Card("1234-5678-9012", "0000", ibanC);
            contCurent.ataseazaCard(card);
            cardService.inregistreazaCard(card);

            //5.Seteaza Pin
            card.setPin("1111");

            //6.Schmba Pin
            card.setPin("9999");
            System.out.println("PIN card actualizat.");

            //7.Transfer IntraBancar
            tranzactieService.executaTransfer(ibanC, ibanE, 1500.0, null);

            //8.Plateste cu cardul
            tranzactieService.platesteCuCard("1234-5678-9012", 200.0, "Magazin IT");

            //9.Blocheaza un card
            cardService.blocheazaCard("1234-5678-9012");

            //10.Deblocheaza un card
            card.setEsteBlocat(false);
            System.out.println("Status card: " + (card.isEsteBlocat() ? "Blocat" : "Activ"));

            //11.Limiteaza suma zilnica de trazactionare
            card.setLimitaZilnica(3000.0);

            //12.Calculează dobânda pentru conturile de economii
            tranzactieService.proceseazaDobanda(ibanE);

            //13.Afișează toate conturile unui client
            System.out.println("\nConturile lui " + c1.getNume() + ":");
            contService.listeazaToateConturile().stream()
                    .filter(c -> c.getTitular().equals(c1))
                    .forEach(System.out::println);

            //14.Cauta cont dupa IBAN
            Cont cautat = contService.gasesteCont(ibanC);
            System.out.println("Cont gasit: " + cautat.getIBAN());

            //15.Afiseaza soldul unui cont
            System.out.println("Sold curent: " + cautat.getSold() + " RON");

            //16.Afiseaza istoricul tranzactiilor unui cont
            System.out.println("\nIstoric tranzactii IBAN: " + ibanC);
            cautat.getIstoricTranzactii().forEach(System.out::println);

            //17.Afiseaza tranzactiile realizate cu un card
            System.out.println("\nPlati cu cardul:");
            cautat.getIstoricTranzactii().stream()
                    .filter(t -> t instanceof Plata)
                    .forEach(System.out::println);

            //18.Afiseaza toate cardurile unui cont si statusul lor
            System.out.println("\nCarduri asociate contului:");
            cautat.getCarduri().forEach(System.out::println);

            //19.Afiseaza detaliile unui card
            Card cDetalii = cardService.gasesteCard("1234-5678-9012");
            System.out.println("Detalii Card: " + cDetalii.toString());

            //20.generare extras de cont
            System.out.println("\n--- EXTRAS DE CONT GENERAT PENTRU " + ibanC + " ---");
            System.out.println("Titular: " + cautat.getTitular().getNume());
            System.out.println("Sold Final: " + cautat.getSold());

            //21.Sterge Card
            cardService.stergeCard("1234-5678-9012");

            //22.Inchide cont
            contService.stergeCont(ibanC);

            //23.Sterge client
            clientService.stergeClient(c1.getId());
        } catch (Exception e) {
            System.err.println("Eroare in timpul executiei: " + e.getMessage());
        }


    }
}