import com.generation.database.Database;
import com.generation.models.Armi;
import com.generation.models.Criminali;
import com.generation.service.ArmiService;
import com.generation.service.CriminaleService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class App {

    static final Scanner sc = new Scanner(System.in);
    static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static CriminaleService criminaleService;
    static ArmiService armiService;

    public static void main(String[] args) {
        Database db = Database.getInstance();
        db.openConnection();

        if (!db.isConnected()) {
            System.out.println("Connessione fallita. Uscita.");
            return;
        }

        criminaleService = new CriminaleService();
        armiService = new ArmiService();

        boolean running = true;
        while (running) {
            menu();
            int scelta = leggiInt("Scelta: ");
            System.out.println();
            switch (scelta) {
                case 1  -> listaCriminali();
                case 2  -> trovaCriminaleById();
                case 3  -> criminaliConArmi();
                case 4  -> inserisciCriminale();
                case 5  -> aggiornaCriminale();
                case 6  -> eliminaCriminale();
                case 7  -> listaArmi();
                case 8  -> trovaArmaById();
                case 9  -> armiDiCriminale();
                case 10 -> armiSenzaProprietario();
                case 11 -> inserisciArma();
                case 12 -> aggiornaArma();
                case 13 -> assegnaArma();
                case 14 -> dissociaArma();
                case 15 -> eliminaArma();
                case 0  -> { running = false; System.out.println("Arrivederci!"); }
                default -> System.out.println("Opzione non valida.");
            }
            System.out.println();
        }

        db.closeConnection();
    }

    //MENU 

    static void menu() {
        System.out.println("        GESTIONE COSELOSCHE       ");
        System.out.println("  CRIMINALI                       ");
        System.out.println("  1  Lista tutti                  ");
        System.out.println("  2  Cerca per ID                 ");
        System.out.println("  3  Lista con le loro armi       ");
        System.out.println("  4  Inserisci                    ");
        System.out.println("  5  Modifica                     ");
        System.out.println("  6  Elimina                      ");
        System.out.println("  ARMI                            ");
        System.out.println("  7  Lista tutte                  ");
        System.out.println("  8  Cerca per ID                 ");
        System.out.println("  9  Armi di un criminale         ");
        System.out.println("  10 Armi senza proprietario      ");
        System.out.println("  11 Inserisci                    ");
        System.out.println("  12 Modifica                     ");
        System.out.println("  13 Assegna a criminale          ");
        System.out.println("  14 Dissocia proprietario        ");
        System.out.println("  15 Elimina                      ");
        System.out.println("  0  Esci                         ");
    }

    //CRIMINALI

    static void listaCriminali() {
        List<Criminali> lista = criminaleService.trovaTutti();
        if (lista.isEmpty()) { System.out.println("Nessun criminale."); return; }
        lista.forEach(System.out::println);
    }

    static void trovaCriminaleById() {
        int id = leggiInt("ID criminale: ");
        Criminali c = criminaleService.trovaById(id);
        System.out.println(c != null ? c : "Non trovato.");
    }

    static void criminaliConArmi() {
        for (Criminali c : criminaleService.trovaTuttiConArmi()) {
            System.out.println(c);
            if (c.getArmi().isEmpty()) System.out.println("  -> nessuna arma");
            else c.getArmi().forEach(a -> System.out.println("  -> " + a));
        }
    }

    static void inserisciCriminale() {
        String nome    = leggiStringa("Nome: ");
        String cognome = leggiStringa("Cognome: ");
        String alias   = leggiStringa("Alias: ");
        LocalDate dob  = leggiData("Data nascita (yyyy-MM-dd): ");
        String gruppo  = leggiStringa("Gruppo: ");
        criminaleService.inserisci(new Criminali(nome, cognome, alias, dob, gruppo));
    }

    static void aggiornaCriminale() {
        int id = leggiInt("ID criminale da modificare: ");
        Criminali c = criminaleService.trovaById(id);
        if (c == null) { System.out.println("Non trovato."); return; }
        System.out.println("Invio vuoto = mantieni valore attuale");
        c.setNome(     leggiODefault("Nome     (" + c.getNome()    + "): ", c.getNome()));
        c.setCognome(  leggiODefault("Cognome  (" + c.getCognome() + "): ", c.getCognome()));
        c.setAlias(    leggiODefault("Alias    (" + c.getAlias()   + "): ", c.getAlias()));
        String dobS =  leggiODefault("DOB      (" + c.getDob()     + "): ", c.getDob().toString());
        c.setDob(LocalDate.parse(dobS, FMT));
        c.setGruppo(   leggiODefault("Gruppo   (" + c.getGruppo()  + "): ", c.getGruppo()));
        criminaleService.aggiorna(c);
    }

    static void eliminaCriminale() {
        int id = leggiInt("ID criminale da eliminare: ");
        System.out.println("ATTENZIONE: le armi del criminale verranno dissociate (non eliminate).");
        if (leggiStringa("Confermi? (s/n): ").equalsIgnoreCase("s"))
            criminaleService.elimina(id);
        else System.out.println("Annullato.");
    }

    //ARMI 

    static void listaArmi() {
        List<Armi> lista = armiService.trovaTutte();
        if (lista.isEmpty()) { System.out.println("Nessuna arma."); return; }
        lista.forEach(System.out::println);
    }

    static void trovaArmaById() {
        int id = leggiInt("ID arma: ");
        Armi a = armiService.trovaById(id);
        System.out.println(a != null ? a : "Non trovata.");
    }

    static void armiDiCriminale() {
        int id = leggiInt("ID criminale: ");
        List<Armi> lista = armiService.trovaByCriminale(id);
        if (lista.isEmpty()) System.out.println("Nessuna arma per questo criminale.");
        else lista.forEach(System.out::println);
    }

    static void armiSenzaProprietario() {
        List<Armi> lista = armiService.trovaSenzaProprietario();
        if (lista.isEmpty()) System.out.println("Tutte le armi hanno un proprietario.");
        else lista.forEach(System.out::println);
    }

    static void inserisciArma() {
        String nome    = leggiStringa("Nome: ");
        int caric      = leggiInt("Caricatore (colpi): ");
        String calibro = leggiStringa("Calibro: ");
        boolean auto   = leggiStringa("Automatica? (s/n): ").equalsIgnoreCase("s");
        String colore  = leggiStringa("Colore: ");
        System.out.print("ID criminale proprietario (invio = nessuno): ");
        String idS     = sc.nextLine().trim();
        Integer idCrim = idS.isEmpty() ? null : Integer.parseInt(idS);
        armiService.inserisci(new Armi(nome, caric, calibro, auto, colore, idCrim));
    }

    static void aggiornaArma() {
        int id = leggiInt("ID arma da modificare: ");
        Armi a = armiService.trovaById(id);
        if (a == null) { System.out.println("Non trovata."); return; }
        System.out.println("Invio vuoto = mantieni valore attuale");
        a.setNome(    leggiODefault("Nome       (" + a.getNome()       + "): ", a.getNome()));
        String carS = leggiODefault("Caricatore (" + a.getCaricatore() + "): ", String.valueOf(a.getCaricatore()));
        a.setCaricatore(Integer.parseInt(carS));
        a.setCalibre( leggiODefault("Calibro    (" + a.getCalibre()    + "): ", a.getCalibre()));
        String autoS= leggiODefault("Automatica (" + (a.isAutomatica()?"s":"n") + "): ", a.isAutomatica()?"s":"n");
        a.setAutomatica(autoS.equalsIgnoreCase("s"));
        a.setColore(  leggiODefault("Colore     (" + a.getColore()     + "): ", a.getColore()));
        armiService.aggiorna(a);
    }

    static void assegnaArma() {
        int idArma = leggiInt("ID arma: ");
        int idCrim = leggiInt("ID criminale: ");
        armiService.assegnaCriminale(idArma, idCrim);
    }

    static void dissociaArma() {
        int id = leggiInt("ID arma: ");
        armiService.dissociaProprietario(id);
    }

    static void eliminaArma() {
        int id = leggiInt("ID arma da eliminare: ");
        if (leggiStringa("Confermi? (s/n): ").equalsIgnoreCase("s"))
            armiService.elimina(id);
        else System.out.println("Annullato.");
    }

    //HELPER INPUT 

    static int leggiInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Inserisci un numero."); }
        }
    }

    static String leggiStringa(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    static String leggiODefault(String prompt, String def) {
        System.out.print(prompt);
        String val = sc.nextLine().trim();
        return val.isEmpty() ? def : val;
    }

    static LocalDate leggiData(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return LocalDate.parse(sc.nextLine().trim(), FMT); }
            catch (DateTimeParseException e) { System.out.println("Formato non valido, usa yyyy-MM-dd."); }
        }
    }
}
