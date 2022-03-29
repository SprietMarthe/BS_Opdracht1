
public class SJF {
    public static void main(String[] argv){
        int aantalProcessen = 5;
        int[] pid = new int[aantalProcessen];
        int[] aankomsttijd = new int[aantalProcessen];
        int[] bedieningstijd = new int[aantalProcessen];
        ReadXMLFile.readingXMLFile(aantalProcessen, pid, aankomsttijd, bedieningstijd);

        int[] bedieningstijdKopie = new int[aantalProcessen];
        System.arraycopy(bedieningstijd, 0, bedieningstijdKopie, 0, aantalProcessen);

        double[] starttijd = new double[aantalProcessen];
        double[] eindtijd = new double[aantalProcessen];
        double[] wachttijd = new double[aantalProcessen];
        double[] omlooptijd = new double[aantalProcessen];
        double[] genormaliseerdeOmlooptijd = new double[aantalProcessen];

        double gemiddeldeOmlooptijd;
        double gemiddeldeGenormaliseerdeOmlooptijd;
        double gemiddeldeWachttijd;

        int aantalJiffys = 0;
        for (int i=0; i<aantalProcessen; i++) {
            // TODO: minder lelijk maken eventueel door andere modellering
            int min = 999999999;
            int sPID = 0;
            for(int j=0; j<aantalProcessen; j++ ) {
                if (aankomsttijd[j] <= aantalJiffys) {
                    if(bedieningstijdKopie[j]<min) {
                        min = bedieningstijdKopie[j];
                        sPID = j;
                    }
                }
            }
            starttijd[sPID] = aantalJiffys;
            eindtijd[sPID] = aantalJiffys + bedieningstijd[sPID];
            wachttijd[sPID] = aantalJiffys - aankomsttijd[sPID];
            omlooptijd[sPID] = wachttijd[sPID] + bedieningstijd[sPID];
            genormaliseerdeOmlooptijd[sPID] = omlooptijd[sPID]/bedieningstijd[sPID];

            System.out.println("Process " + (sPID+1) + ":");
            System.out.println("arrival: " + aankomsttijd[sPID]);
            System.out.println("service: " + bedieningstijd[sPID]);
            System.out.println("start: " + starttijd[sPID]);
            System.out.println("eind: " + eindtijd[sPID]);
            System.out.println("wachttijd: " + wachttijd[sPID]);
            System.out.println("omlooptijd: " + omlooptijd[sPID]);
            System.out.println("genormaliseerde omlooptijd: " + genormaliseerdeOmlooptijd[sPID]);
            System.out.println();

            //zodat deze niet meer als minimum kan gezien worden
            // TODO: mooiere manier
            bedieningstijdKopie[sPID] = 999999999;
            aantalJiffys += bedieningstijd[sPID];
        }

// globale parameters berekend
        int somOmlooptijd = 0;
        int somGenormaliseerdeOmlooptijd = 0;
        int somWachttijd = 0;
        for (int i=0; i<aantalProcessen; i++) {
            somOmlooptijd += omlooptijd[i];
            somGenormaliseerdeOmlooptijd += genormaliseerdeOmlooptijd[i];
            somWachttijd += wachttijd[i];
        }
        gemiddeldeOmlooptijd = somOmlooptijd/(double)aantalProcessen;
        gemiddeldeGenormaliseerdeOmlooptijd = somGenormaliseerdeOmlooptijd/(double)aantalProcessen;
        gemiddeldeWachttijd = somWachttijd/(double)aantalProcessen;


// globale parameters uitgeprint
        System.out.println("GLOBALE PARAMETERS");
        System.out.println("gemiddelde omlooptijd: " + gemiddeldeOmlooptijd);
        System.out.println("gemiddelde genormaliseerde omlooptijd: " + gemiddeldeGenormaliseerdeOmlooptijd);
        System.out.println("gemiddelde wachttijd: " + gemiddeldeWachttijd);
    }
}


