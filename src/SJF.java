
public class SJF {
    public static void main(String[] argv){
        int aantalProcessen = 5;
        int[] pid = new int[aantalProcessen];
        int[] arrivaltime = new int[aantalProcessen];
        int[] servicetime = new int[aantalProcessen];
        ReadXMLFile.readingXMLFile(aantalProcessen, pid, arrivaltime, servicetime);

        int[] servicetimeKopie = new int[aantalProcessen];
        System.arraycopy(servicetime, 0, servicetimeKopie, 0, aantalProcessen);

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
                if (arrivaltime[j] <= aantalJiffys) {
                    if(servicetimeKopie[j]<min) {
                        min = servicetimeKopie[j];
                        sPID = j;
                    }
                }
            }
            starttijd[sPID] = aantalJiffys;
            eindtijd[sPID] = aantalJiffys + servicetime[sPID];
            wachttijd[sPID] = aantalJiffys - arrivaltime[sPID];
            omlooptijd[sPID] = wachttijd[sPID] + servicetime[sPID];
            genormaliseerdeOmlooptijd[sPID] = omlooptijd[sPID]/servicetime[sPID];

            System.out.println("Process " + (sPID+1) + ":");
            System.out.println("arrival: " + arrivaltime[sPID]);
            System.out.println("service: " + servicetime[sPID]);
            System.out.println("start: " + starttijd[sPID]);
            System.out.println("eind: " + eindtijd[sPID]);
            System.out.println("wachttijd: " + wachttijd[sPID]);
            System.out.println("omlooptijd: " + omlooptijd[sPID]);
            System.out.println("genormaliseerde omlooptijd: " + genormaliseerdeOmlooptijd[sPID]);
            System.out.println();

            //zodat deze niet meer als minimum kan gezien worden
            // TODO: mooiere manier
            servicetimeKopie[sPID] = 999999999;
            aantalJiffys += servicetime[sPID];
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


